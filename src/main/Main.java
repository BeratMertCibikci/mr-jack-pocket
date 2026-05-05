package main;

import engine.ActionType;
import engine.GameEngine;
import java.util.Scanner;
import model.GameCharacter;
import model.GameState;
import model.Tile;
import model.Token;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Player 1, which role would you like to play? (Investigator / Jack): ");
        String player1Role = scanner.nextLine();

        while (!player1Role.equals("Investigator") && !player1Role.equals("Jack")) {
            System.out.print("Invalid input. Player 1, please enter 'Investigator' or 'Jack': ");
            player1Role = scanner.nextLine();
        }

        GameEngine engine = new GameEngine(player1Role);
        engine.startGame();

        GameState gameState = engine.getGameState();

        System.out.println("Player 1 is playing as: " + gameState.getPlayer1Role());
        System.out.println("Player 2 is playing as: " + gameState.getPlayer2Role());
        System.out.println();

        printGameInfo(gameState);
        printCharacters(gameState);
        printBoard(gameState);
        printActionTokens(gameState);
        printDetectiveTokens(gameState);
        printTurnTokens(gameState);
        printStatus(engine);

        while (!engine.getGameState().isGameOver()) {

            while (!engine.isRoundOver() && !engine.getGameState().isGameOver()) {

                System.out.println("\nCommand (play / undo / redo): ");
                String cmd = scanner.next();

                if (cmd.equalsIgnoreCase("undo")){
                    engine.undo();

                    printBoard(engine.getGameState());
                    printCharacters(engine.getGameState());
                    printDetectiveTokens(engine.getGameState());
                    printStatus(engine);

                    continue;
                }

                if (cmd.equalsIgnoreCase("redo")){
                    engine.redo();

                    printBoard(engine.getGameState());
                    printCharacters(engine.getGameState());
                    printDetectiveTokens(engine.getGameState());
                    printStatus(engine);

                    continue;
                }

                if (!cmd.equalsIgnoreCase("play")){
                    System.out.println("Unknown command. Type play / undo / redo.");
                    continue;
                }

                playOneActionWithInput(engine, scanner);

                printBoard(engine.getGameState());
                printCharacters(engine.getGameState());
                printDetectiveTokens(engine.getGameState());
                printStatus(engine);
            }

            if (!engine.getGameState().isGameOver()) {
                System.out.println("--- Ending Round " + engine.getRoundNumber() + " ---");

                engine.endRound();

                System.out.println("--- Appeal for Witnesses resolved ---");
                printTurnTokens(engine.getGameState());
                printCharacters(engine.getGameState());

                if (!engine.getGameState().isGameOver()) {
                    printActionTokens(engine.getGameState());
                    printStatus(engine);
                }
            }
        }

        System.out.println("Game Over! Winner: " + engine.getGameState().getWinner());

        scanner.close();
    }

    private static void playOneActionWithInput(GameEngine engine, Scanner scanner) {
        String currentPlayerRole = engine.getCurrentPlayer();

        System.out.println("\n>>> It is " + currentPlayerRole + "'s turn.");
        System.out.println("Available tokens:");

        Token[] tokens = engine.getGameState().getActionTokens().getActionTokens();

        for (int i = 0; i < tokens.length; i++) {
            if (!tokens[i].isUsed()) {
                System.out.println("  " + i + ": " + tokens[i].getCurrentSide());
            }
        }

        int tokenIndex;

        while (true) {
            System.out.print(currentPlayerRole + ", select a token index (0-3): ");

            if (scanner.hasNextInt()) {
                tokenIndex = scanner.nextInt();

                if (tokenIndex >= 0 && tokenIndex < tokens.length && !tokens[tokenIndex].isUsed()) {
                    break;
                }

                System.out.println("Invalid or already used token index.");
            } else {
                System.out.println("Please enter a valid number.");
                scanner.next();
            }
        }

        Token selectedToken = engine.selectActionToken(tokenIndex);
        ActionType actionType = engine.getSelectedActionType();

        System.out.println("Selected token: " + selectedToken.getCurrentSide());
        System.out.println("Action type: " + actionType);

        switch (actionType) {
            case HOLMES:
                int holmesSteps = getDetectiveSteps(scanner, "Holmes");
                engine.moveHolmes(holmesSteps);
                System.out.println("Holmes moved " + holmesSteps + " step(s).");
                break;

            case WATSON:
                int watsonSteps = getDetectiveSteps(scanner, "Watson");
                engine.moveWatson(watsonSteps);
                System.out.println("Watson moved " + watsonSteps + " step(s).");
                break;

            case TOBY:
                int tobySteps = getDetectiveSteps(scanner, "Toby");
                engine.moveToby(tobySteps);
                System.out.println("Toby moved " + tobySteps + " step(s).");
                break;

            case ROTATE:
                Tile tileToRotate = getTileInput(scanner, engine, "tile to rotate");
                int rotations = getRotationInput(scanner);

                engine.rotateTile(tileToRotate, rotations);

                System.out.println(
                        "Rotated tile at ("
                                + tileToRotate.getRow()
                                + ","
                                + tileToRotate.getCol()
                                + ") "
                                + rotations
                                + " time(s)."
                );
                break;

            case EXCHANGE:
                Tile tileA = getTileInput(scanner, engine, "first tile");
                Tile tileB = getTileInput(scanner, engine, "second tile");

                int oldRowA = tileA.getRow();
                int oldColA = tileA.getCol();
                int oldRowB = tileB.getRow();
                int oldColB = tileB.getCol();

                engine.exchangeTiles(tileA, tileB);

                System.out.println(
                        "Exchanged tiles ("
                                + oldRowA
                                + ","
                                + oldColA
                                + ") and ("
                                + oldRowB
                                + ","
                                + oldColB
                                + ")."
                );
                break;

            case ALIBI:
                if (engine.getGameState().getTurnManager().isInvestigatorTurn()) {
                    GameCharacter eliminated = engine.investigatorDrawsAlibi();

                    if (eliminated != null) {
                        System.out.println("Investigator drew alibi: " + eliminated.getName());
                    } else {
                        System.out.println("Investigator tried to draw alibi, but deck is empty.");
                    }
                } else {
                    engine.jackDrawsAlibi();
                    System.out.println("Jack drew alibi.");
                }
                break;

            case JOKER:
                if (engine.getGameState().getTurnManager().isJackTurn()) {
                    System.out.print("Jack: do you want to move a detective with Joker? (yes/no): ");
                    String answer = scanner.next();

                    while (!answer.equalsIgnoreCase("yes") && !answer.equalsIgnoreCase("no")) {
                        System.out.print("Invalid choice. Please enter yes or no: ");
                        answer = scanner.next();
                    }

                    if (answer.equalsIgnoreCase("no")) {
                        engine.skipJokerMove();
                        System.out.println("Jack used Joker and did not move any detective.");
                        break;
                    }
                }

                System.out.print("Which detective do you want to move with Joker? (Holmes / Watson / Toby): ");
                String targetDetective = scanner.next();

                while (!targetDetective.equalsIgnoreCase("Holmes")
                        && !targetDetective.equalsIgnoreCase("Watson")
                        && !targetDetective.equalsIgnoreCase("Toby")) {
                    System.out.print("Invalid choice. Please enter Holmes, Watson or Toby: ");
                    targetDetective = scanner.next();
                }

                engine.moveDetectiveWithJoker(targetDetective);

                System.out.println(
                        "Used Joker to move "
                                + targetDetective
                                + " 1 step."
                );
                break;
        }
    }

    private static int getDetectiveSteps(Scanner scanner, String detectiveName) {
        int steps;

        while (true) {
            System.out.print("How many steps should " + detectiveName + " move? (1 or 2): ");

            if (scanner.hasNextInt()) {
                steps = scanner.nextInt();

                if (steps == 1 || steps == 2) {
                    return steps;
                }

                System.out.println("Invalid number. Must be 1 or 2.");
            } else {
                System.out.println("Please enter a valid number.");
                scanner.next();
            }
        }
    }

    private static Tile getTileInput(Scanner scanner, GameEngine engine, String tileName) {
        while (true) {
            System.out.print("Enter row and column for " + tileName + " (e.g. 0 2): ");

            if (scanner.hasNextInt()) {
                int row = scanner.nextInt();

                if (scanner.hasNextInt()) {
                    int col = scanner.nextInt();

                    if (row >= 0 && row < 3 && col >= 0 && col < 3) {
                        return engine.getGameState().getBoard().getTile(row, col);
                    }

                    System.out.println("Invalid coordinates. Row and column must be 0, 1, or 2.");
                } else {
                    System.out.println("Invalid column input.");
                    scanner.next();
                }
            } else {
                System.out.println("Invalid row input.");
                scanner.next();
            }
        }
    }

    private static int getRotationInput(Scanner scanner) {
        int rotations;

        while (true) {
            System.out.print("How many times do you want to rotate it clockwise? (1 to 3): ");

            if (scanner.hasNextInt()) {
                rotations = scanner.nextInt();

                if (rotations >= 1 && rotations <= 3) {
                    return rotations;
                }

                System.out.println("Invalid number. Please enter 1, 2, or 3.");
            } else {
                System.out.println("Please enter a valid number.");
                scanner.next();
            }
        }
    }

    private static void printStatus(GameEngine engine) {
        System.out.println("----- STATUS -----");
        System.out.println("Round: " + engine.getRoundNumber());
        System.out.println("Current player: " + engine.getCurrentPlayer());
        System.out.println("Current action index: " + engine.getCurrentActionIndex());
        System.out.println("Round over: " + engine.isRoundOver());
        System.out.println("Game over: " + engine.getGameState().isGameOver());
        System.out.println("Winner: " + engine.getGameState().getWinner());
        System.out.println();
    }

    private static void printGameInfo(GameState gameState) {
        System.out.println("=== GAME INFO ===");
        System.out.println("Game started: " + gameState.isGameStarted());
        System.out.println("Game over: " + gameState.isGameOver());
        System.out.println("Winner: " + gameState.getWinner());
        System.out.println("Round: " + gameState.getTurnManager().getRoundNumber());
        System.out.println("Current player: " + gameState.getTurnManager().getCurrentPlayer());

        GameCharacter jack = gameState.getJackCharacter();
        System.out.println("Mr. Jack identity: " + jack.getName());
        System.out.println();
    }

    private static void printCharacters(GameState gameState) {
        System.out.println("=== CHARACTERS ===");

        for (GameCharacter character : gameState.getCharacters()) {
            System.out.println(
                    character.getId()
                            + " - " + character.getName()
                            + " | color=" + character.getColor()
                            + " | suspect=" + character.isSuspect()
                            + " | jack=" + character.isJack()
                            + " | visible=" + character.isVisible()
                            + " | tile=" + getTileInfo(character.getTile())
            );
        }

        System.out.println();
    }

    private static String getTileInfo(Tile tile) {
        if (tile == null) {
            return "none";
        }

        return "Area " + tile.getId()
                + " ("
                + tile.getRow()
                + ","
                + tile.getCol()
                + ")"
                + " | side="
                + (tile.isSuspectSide() ? "Suspect" : "Empty");
    }

    private static void printBoard(GameState gameState) {
        System.out.println("=== BOARD ===");

        Tile[][] board = gameState.getBoard().getBoardForUI();

        for (int row = 0; row < gameState.getBoard().getSize(); row++) {
            for (int col = 0; col < gameState.getBoard().getSize(); col++) {
                Tile tile = board[row][col];

                if (tile == null) {
                    System.out.print("[Empty] ");
                } else {
                    String characterName = tile.getCharacter() != null
                            ? tile.getCharacter().getName()
                            : "No Character";

                    String side = tile.isSuspectSide() ? "S" : "E";

                    System.out.print("[A" + tile.getId() + ":" + characterName + ":" + side + "] ");
                }
            }

            System.out.println();
        }

        System.out.println();
    }

    private static void printActionTokens(GameState gameState) {
        System.out.println("=== ACTION TOKENS ===");

        Token[] actionTokens = gameState.getActionTokens().getActionTokens();

        for (int i = 0; i < actionTokens.length; i++) {
            Token token = actionTokens[i];

            System.out.println(
                    i
                            + " - front="
                            + token.getFrontSide()
                            + " | back="
                            + token.getBackSide()
                            + " | current="
                            + token.getCurrentSide()
                            + " | used="
                            + token.isUsed()
            );
        }

        System.out.println();
    }

    private static void printDetectiveTokens(GameState gameState) {
        System.out.println("=== DETECTIVES ===");

        for (Token detective : gameState.getDetectiveTokens().getAllDetectives()) {
            System.out.println(
                    detective.getName()
                            + " | position="
                            + detective.getPosition()
            );
        }

        System.out.println();
    }

    private static void printTurnTokens(GameState gameState) {
        System.out.println("=== TURN / TIME TOKENS ===");

        Token currentTurnToken = gameState.getCurrentTurnToken();

        if (currentTurnToken == null) {
            System.out.println("Current turn token: none");
        } else {
            System.out.println(
                    "Current turn token: "
                            + currentTurnToken.getFrontSide()
                            + " | current side="
                            + currentTurnToken.getCurrentSide()
            );
        }

        System.out.println("Detective owned turn tokens:");

        for (Token token : gameState.getDetectivePlayerState().getOwnedTurnTokens()) {
            System.out.println(
                    "- "
                            + token.getFrontSide()
                            + " | current side="
                            + token.getCurrentSide()
            );
        }

        System.out.println("Mr. Jack owned turn tokens:");

        for (Token token : gameState.getJackPlayerState().getOwnedTurnTokens()) {
            System.out.println(
                    "- "
                            + token.getFrontSide()
                            + " | current side="
                            + token.getCurrentSide()
            );
        }

        System.out.println();
    }
}