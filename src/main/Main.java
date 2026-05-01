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

        printStatus(engine);

        playOneAction(engine, 0);
        printStatus(engine);

        playOneAction(engine, 1);
        printStatus(engine);

        playOneAction(engine, 2);
        printStatus(engine);

        playOneAction(engine, 3);
        printStatus(engine);

        if (engine.isRoundOver()) {
            engine.endRound();
        }

        printStatus(engine);

        scanner.close();
    }

    private static void playOneAction(GameEngine engine, int tokenIndex) {
        Token selectedToken = engine.selectActionToken(tokenIndex);
        ActionType actionType = engine.getSelectedActionType();

        System.out.println("Selected token: " + selectedToken.getCurrentSide());
        System.out.println("Action type: " + actionType);

        switch (actionType) {
            case HOLMES:
                engine.moveHolmes(1);
                break;

            case WATSON:
                engine.moveWatson(1);
                break;

            case TOBY:
                engine.moveToby(1);
                break;

            case ROTATE:
                Tile tileToRotate = engine.getGameState().getBoard().getTile(1, 1);
                engine.rotateTile(tileToRotate);
                break;

            case EXCHANGE:
                Tile tileA = engine.getGameState().getBoard().getTile(0, 0);
                Tile tileB = engine.getGameState().getBoard().getTile(2, 2);
                engine.exchangeTiles(tileA, tileB);
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
                Tile jokerTile = engine.getGameState().getBoard().getTile(0, 1);
                engine.useJokerAsRotate(jokerTile);
                break;
        }

        System.out.println();
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
                + " (" + tile.getRow()
                + "," + tile.getCol()
                + ")";
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

                    System.out.print("[A" + tile.getId() + ": " + characterName + "] ");
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
                            + " - front=" + token.getFrontSide()
                            + " | back=" + token.getBackSide()
                            + " | current=" + token.getCurrentSide()
                            + " | used=" + token.isUsed()
            );
        }

        System.out.println();
    }

    private static void printDetectiveTokens(GameState gameState) {
        System.out.println("=== DETECTIVES ===");

        for (Token detective : gameState.getDetectiveTokens().getAllDetectives()) {
            System.out.println(
                    detective.getName()
                            + " | position=" + detective.getPosition()
            );
        }

        System.out.println();
    }
}