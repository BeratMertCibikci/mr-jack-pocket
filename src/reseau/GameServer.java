package reseau;

import engine.ActionType;
import engine.GameEngine;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import model.Tile;

public class GameServer {
    private int port;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();

    private GameEngine gameEngine;

    private boolean gameStarted = false;
    private int readyClients = 0;

    private Message lastHighlightMessage;

    public GameServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port + ".");

            while (clients.size() < 2) {
                Socket socket = serverSocket.accept();
                System.out.println("New connection: " + socket.getInetAddress());

                ClientHandler handler = new ClientHandler(socket, this);
                clients.add(handler);

                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public synchronized void clientReady() {
        readyClients++;

        if (readyClients == 2 && !gameStarted) {
            initializeGame();
        }
    }

    private void initializeGame() {
        String player1Role = clients.get(0).getRole();

        gameEngine = new GameEngine(player1Role);
        gameEngine.startGame();

        gameStarted = true;

        broadcastUpdate();
        System.out.println("Game started and state broadcasted.");
    }

    public synchronized void broadcastUpdate() {
        if (gameEngine == null) {
            return;
        }

        Message updateMsg = new Message(Message.MessageType.UPDATE_STATE, "Server");
        updateMsg.setGameStatePayload(gameEngine.getGameState());

        copyLastHighlightTo(updateMsg);

        for (ClientHandler client : clients) {
            client.sendMessage(updateMsg);
        }
    }

    private void copyLastHighlightTo(Message updateMsg) {
        if (lastHighlightMessage == null || !lastHighlightMessage.hasHighlight()) {
            return;
        }

        if (lastHighlightMessage.getHighlightRow2() >= 0) {
            updateMsg.setHighlight(
                    lastHighlightMessage.getHighlightRow1(),
                    lastHighlightMessage.getHighlightCol1(),
                    lastHighlightMessage.getHighlightRow2(),
                    lastHighlightMessage.getHighlightCol2()
            );
        } else {
            updateMsg.setHighlight(
                    lastHighlightMessage.getHighlightRow1(),
                    lastHighlightMessage.getHighlightCol1()
            );
        }
    }

    public synchronized void handleAction(Message msg) {
        try {
            boolean shouldBroadcast = processMessageOnEngine(msg);

            if (shouldBroadcast) {
                rememberHighlightIfNeeded(msg);
                broadcastUpdate();
            }

        } catch (Exception e) {
            System.err.println("Action error: " + e.getMessage());
        }
    }

    private void rememberHighlightIfNeeded(Message msg) {
        if (msg == null || !msg.hasHighlight()) {
            return;
        }

        lastHighlightMessage = msg;
    }

    private boolean processMessageOnEngine(Message msg) {
        if (gameEngine == null || msg == null) {
            return false;
        }

        if (msg.getType() == Message.MessageType.ACTION_SELECT) {
            return false;
        }

        if (msg.getType() == Message.MessageType.UNDO) {
            gameEngine.undo();
            lastHighlightMessage = null;
            return true;
        }

        if (msg.getType() == Message.MessageType.REDO) {
            gameEngine.redo();
            lastHighlightMessage = null;
            return true;
        }

        if (msg.getType() == Message.MessageType.ACTION_EXECUTE) {
            executeAction(msg);
            finishRoundIfNeeded();
            return true;
        }

        return false;
    }

    private void executeAction(Message msg) {
        gameEngine.selectActionToken(msg.getTokenIndex());

        ActionType type = msg.getActionType();

        if (type == null) {
            throw new IllegalArgumentException("Action type cannot be null.");
        }

        switch (type) {
            case HOLMES:
                gameEngine.moveHolmes(msg.getSteps());
                break;

            case WATSON:
                gameEngine.moveWatson(msg.getSteps());
                break;

            case TOBY:
                gameEngine.moveToby(msg.getSteps());
                break;

            case ROTATE:
                executeRotate(msg);
                break;

            case EXCHANGE:
                executeExchange(msg);
                break;

            case ALIBI:
                executeAlibi();
                break;

            case JOKER:
                executeJoker(msg);
                break;

            default:
                throw new IllegalArgumentException("Unsupported action type: " + type);
        }
    }

    private void executeRotate(Message msg) {
        if (msg.getTileA() == null) {
            throw new IllegalArgumentException("Rotate action requires tileA.");
        }

        Tile serverTileToRotate = gameEngine.getGameState()
                .getBoard()
                .getTile(
                        msg.getTileA().getRow(),
                        msg.getTileA().getCol()
                );

        gameEngine.rotateTile(serverTileToRotate, msg.getRotations());
    }

    private void executeExchange(Message msg) {
        if (msg.getTileA() == null || msg.getTileB() == null) {
            throw new IllegalArgumentException("Exchange action requires tileA and tileB.");
        }

        Tile serverTileA = gameEngine.getGameState()
                .getBoard()
                .getTile(
                        msg.getTileA().getRow(),
                        msg.getTileA().getCol()
                );

        Tile serverTileB = gameEngine.getGameState()
                .getBoard()
                .getTile(
                        msg.getTileB().getRow(),
                        msg.getTileB().getCol()
                );

        gameEngine.exchangeTiles(serverTileA, serverTileB);
    }

    private void executeAlibi() {
        if (gameEngine.getGameState().getTurnManager().isInvestigatorTurn()) {
            gameEngine.investigatorDrawsAlibi();
        } else {
            gameEngine.jackDrawsAlibi();
        }
    }

    private void executeJoker(Message msg) {
        if (msg.getDetectiveName() != null && !msg.getDetectiveName().isEmpty()) {
            gameEngine.moveDetectiveWithJoker(msg.getDetectiveName());
        } else {
            gameEngine.skipJokerMove();
        }
    }

    private void finishRoundIfNeeded() {
        if (!gameEngine.getGameState().isGameOver() && gameEngine.isRoundOver()) {
            gameEngine.endRound();
        }
    }

    public class ClientHandler implements Runnable {
        private Socket socket;
        private GameServer server;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private String role;

        public ClientHandler(Socket socket, GameServer server) {
            this.socket = socket;
            this.server = server;
        }

        public String getRole() {
            return role;
        }

        public void sendMessage(Message msg) {
            try {
                if (out != null) {
                    out.writeObject(msg);
                    out.flush();
                    out.reset();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                Message connectMsg = (Message) in.readObject();

                if (connectMsg != null && connectMsg.getType() == Message.MessageType.CONNECT) {
                    this.role = connectMsg.getSenderRole();

                    System.out.println("Player connected. Role: " + this.role);

                    server.clientReady();
                }

                while (true) {
                    Message msg = (Message) in.readObject();

                    if (msg != null) {
                        server.handleAction(msg);
                    }
                }

            } catch (Exception e) {
                System.out.println((role != null ? role : "Unknown player") + " disconnected.");
            }
        }
    }
}