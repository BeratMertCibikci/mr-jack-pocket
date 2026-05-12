package ai;

import engine.ActionType;
import engine.GameEngine;
import model.GameState;
import model.Tile;
import model.Token;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    private MoveGenerator() {
        // Utility class
    }

    public static List<AIMove> generateMoves(GameEngine engine) {
        if (engine == null) {
            throw new IllegalArgumentException("GameEngine cannot be null.");
        }

        GameState gameState = engine.getGameState();
        List<AIMove> moves = new ArrayList<>();

        Token[] tokens = gameState.getActionTokens().getActionTokens();

        for (int tokenIndex = 0; tokenIndex < tokens.length; tokenIndex++) {
            Token token = tokens[tokenIndex];

            if (token.isUsed()) {
                continue;
            }

            ActionType actionType = parseActionType(token.getCurrentSide());

            switch (actionType) {
                case HOLMES:
                    addDetectiveMoves(moves, tokenIndex, ActionType.HOLMES, "Holmes");
                    break;

                case WATSON:
                    addDetectiveMoves(moves, tokenIndex, ActionType.WATSON, "Watson");
                    break;

                case TOBY:
                    addDetectiveMoves(moves, tokenIndex, ActionType.TOBY, "Toby");
                    break;

                case JOKER:
                    addJokerMoves(engine, moves, tokenIndex);
                    break;

                case ROTATE:
                    addRotateMoves(engine, moves, tokenIndex);
                    break;

                case EXCHANGE:
                    addExchangeMoves(engine, moves, tokenIndex);
                    break;

                case ALIBI:
                    moves.add(AIMove.alibi(tokenIndex));
                    break;

                default:
                    throw new IllegalStateException("Unsupported action type: " + actionType);
            }
        }

        return moves;
    }

    private static void addDetectiveMoves(
            List<AIMove> moves,
            int tokenIndex,
            ActionType actionType,
            String detectiveName
    ) {
        moves.add(AIMove.detectiveMove(tokenIndex, actionType, detectiveName, 1));
        moves.add(AIMove.detectiveMove(tokenIndex, actionType, detectiveName, 2));
    }

    private static void addJokerMoves(
            GameEngine engine,
            List<AIMove> moves,
            int tokenIndex
    ) {
        GameState gameState = engine.getGameState();

        if (gameState.getTurnManager().isJackTurn()) {
            moves.add(AIMove.jokerSkip(tokenIndex));
        }

        moves.add(AIMove.jokerMove(tokenIndex, "Holmes"));
        moves.add(AIMove.jokerMove(tokenIndex, "Watson"));
        moves.add(AIMove.jokerMove(tokenIndex, "Toby"));
    }

    private static void addRotateMoves(
            GameEngine engine,
            List<AIMove> moves,
            int tokenIndex
    ) {
        List<Tile> tiles = engine.getGameState().getBoard().getAllTiles();

        for (Tile tile : tiles) {
            if (engine.getActionEngine().hasTileBeenRotatedThisRound(tile)) {
                continue;
            }

            moves.add(AIMove.rotate(tokenIndex, tile.getRow(), tile.getCol(), 1));
            moves.add(AIMove.rotate(tokenIndex, tile.getRow(), tile.getCol(), 2));
            moves.add(AIMove.rotate(tokenIndex, tile.getRow(), tile.getCol(), 3));
        }
    }

    private static void addExchangeMoves(
            GameEngine engine,
            List<AIMove> moves,
            int tokenIndex
    ) {
        List<Tile> tiles = engine.getGameState().getBoard().getAllTiles();

        for (int i = 0; i < tiles.size(); i++) {
            Tile tileA = tiles.get(i);

            for (int j = i + 1; j < tiles.size(); j++) {
                Tile tileB = tiles.get(j);

                moves.add(AIMove.exchange(
                        tokenIndex,
                        tileA.getRow(),
                        tileA.getCol(),
                        tileB.getRow(),
                        tileB.getCol()
                ));
            }
        }
    }

    private static ActionType parseActionType(String actionName) {
        switch (actionName) {
            case "Holmes":
                return ActionType.HOLMES;
            case "Watson":
                return ActionType.WATSON;
            case "Toby":
                return ActionType.TOBY;
            case "Alibi":
                return ActionType.ALIBI;
            case "Exchange":
                return ActionType.EXCHANGE;
            case "Rotate":
                return ActionType.ROTATE;
            case "Joker":
                return ActionType.JOKER;
            default:
                throw new IllegalArgumentException("Unknown action type: " + actionName);
        }
    }
}