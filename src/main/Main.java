package main;

import model.GameCharacter;
import model.GameState;
import model.Tile;
import model.Token;

public class Main {
    public static void main(String[] args) {
        GameState gameState = new GameState();
        gameState.setupGame();

        printGameInfo(gameState);
        printCharacters(gameState);
        printBoard(gameState);
        printActionTokens(gameState);
        printDetectiveTokens(gameState);

        System.out.println("=== PLAY ACTION TOKENS ===");
        Token played1 = gameState.playActionToken(0);

        System.out.println("Played token: " + played1.getCurrentSide());

        System.out.println("Next player: " + gameState.getTurnManager().getCurrentPlayer());

        Token played2 = gameState.playActionToken(1);

        System.out.println("Played token: " + played2.getCurrentSide());

        System.out.println("Next player: " + gameState.getTurnManager().getCurrentPlayer());
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