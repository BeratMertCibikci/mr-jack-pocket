package reseau;

import java.io.*;
import java.net.*;
import java.util.*;
import engine.GameEngine;
import engine.ActionType;
import model.Tile;

public class GameServer {
    private int port;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private GameEngine gameEngine;
    private boolean gameStarted = false;
    private int readyClients = 0;

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
        Message updateMsg = new Message(Message.MessageType.UPDATE_STATE, "Server");
        updateMsg.setGameStatePayload(gameEngine.getGameState());

        for (ClientHandler client : clients) {
            client.sendMessage(updateMsg);
        }
    }

    public synchronized void handleAction(Message msg) {
        try {
            processMessageOnEngine(msg);
            broadcastUpdate();
        } catch (Exception e) {
            System.err.println("Action error: " + e.getMessage());
        }
    }

    private void processMessageOnEngine(Message msg) {
        if (msg.getType() == Message.MessageType.ACTION_SELECT) {
            gameEngine.selectActionToken(msg.getTokenIndex());
        } 
        else if (msg.getType() == Message.MessageType.ACTION_EXECUTE) {
            ActionType type = msg.getActionType();
            
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
                    Tile serverTileToRotate = gameEngine.getGameState().getBoard().getTile(
                        msg.getTileA().getRow(), 
                        msg.getTileA().getCol()
                    );
                    gameEngine.rotateTile(serverTileToRotate, msg.getRotations());
                    break;
                case EXCHANGE:
                    Tile serverTileA = gameEngine.getGameState().getBoard().getTile(
                        msg.getTileA().getRow(), 
                        msg.getTileA().getCol()
                    );
                    Tile serverTileB = gameEngine.getGameState().getBoard().getTile(
                        msg.getTileB().getRow(), 
                        msg.getTileB().getCol()
                    );
                    gameEngine.exchangeTiles(serverTileA, serverTileB);
                    break;
                case ALIBI:
                    if (gameEngine.getGameState().getTurnManager().isInvestigatorTurn()) {
                        gameEngine.investigatorDrawsAlibi();
                    } else {
                        gameEngine.jackDrawsAlibi();
                    }
                    break;
                case JOKER:
                    gameEngine.moveDetectiveWithJoker(msg.getDetectiveName(), msg.getSteps());
                    break;
            }
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

        public String getRole() { return role; }

        public void sendMessage(Message msg) {
            try {
                out.writeObject(msg);
                out.flush();
                out.reset();
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