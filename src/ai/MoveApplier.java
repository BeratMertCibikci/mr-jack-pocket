package ai;

import engine.ActionType;
import engine.GameEngine;
import model.GameState;
import model.Tile;

public class MoveApplier {

    private MoveApplier() {
        // Utility class, object creation is not needed.
    }

    public static void apply(GameEngine engine, AIMove move) {
        if (engine == null) {
            throw new IllegalArgumentException("GameEngine cannot be null.");
        }

        if (move == null) {
            throw new IllegalArgumentException("AIMove cannot be null.");
        }

        engine.selectActionToken(move.getActionTokenIndex());

        ActionType selectedActionType = engine.getSelectedActionType();

        if (selectedActionType != move.getActionType()) {
            throw new IllegalStateException(
                    "Move action type does not match selected token. "
                            + "Move says " + move.getActionType()
                            + ", token says " + selectedActionType
            );
        }

        switch (move.getActionType()) {
            case HOLMES:
                engine.moveHolmes(move.getSteps());
                break;

            case WATSON:
                engine.moveWatson(move.getSteps());
                break;

            case TOBY:
                engine.moveToby(move.getSteps());
                break;

            case JOKER:
                applyJoker(engine, move);
                break;

            case ROTATE:
                applyRotate(engine, move);
                break;

            case EXCHANGE:
                applyExchange(engine, move);
                break;

            case ALIBI:
                applyAlibi(engine);
                break;

            default:
                throw new IllegalStateException("Unsupported action type: " + move.getActionType());
        }
    }

    private static void applyJoker(GameEngine engine, AIMove move) {
        if (move.isJokerSkip()) {
            engine.skipJokerMove();
        } else {
            engine.moveDetectiveWithJoker(move.getDetectiveName());
        }
    }

    private static void applyRotate(GameEngine engine, AIMove move) {
        Tile tile = engine.getGameState()
                .getBoard()
                .getTile(move.getRow(), move.getCol());

        engine.rotateTile(tile, move.getRotations());
    }

    private static void applyExchange(GameEngine engine, AIMove move) {
        Tile tileA = engine.getGameState()
                .getBoard()
                .getTile(move.getRowA(), move.getColA());

        Tile tileB = engine.getGameState()
                .getBoard()
                .getTile(move.getRowB(), move.getColB());

        engine.exchangeTiles(tileA, tileB);
    }

    private static void applyAlibi(GameEngine engine) {
        GameState gameState = engine.getGameState();

        if (gameState.getTurnManager().isInvestigatorTurn()) {
            engine.investigatorDrawsAlibi();
        } else if (gameState.getTurnManager().isJackTurn()) {
            engine.jackDrawsAlibi();
        } else {
            throw new IllegalStateException(
                    "Cannot apply alibi. Current player: "
                            + gameState.getTurnManager().getCurrentPlayer()
            );
        }
    }
}