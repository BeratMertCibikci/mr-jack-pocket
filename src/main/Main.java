package main;

import engine.ActionType;
import engine.GameEngine;
import model.GameCharacter;
import model.Tile;
import model.Token;

public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.startGame();

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
                    System.out.println("Investigator drew alibi: " + eliminated.getName());
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
}