package ai;

import engine.ActionType;
import engine.GameEngine;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.GameState;
import model.Tile;
import model.Token;

public class RandomAI implements AIPlayer {

    private final Random random = new Random();

    @Override
    public void play(GameEngine engine) {
        GameState gameState = engine.getGameState();

        int tokenIndex = chooseRandomUnusedActionTokenIndex(gameState);

        engine.selectActionToken(tokenIndex);
        ActionType actionType = engine.getSelectedActionType();

        System.out.println("[RandomAI] Current player: " + engine.getCurrentPlayer());
        System.out.println("[RandomAI] Selected token index: " + tokenIndex);
        System.out.println("[RandomAI] Selected action: " + actionType);

        switch (actionType) {
            case HOLMES:
                int holmesSteps = randomSteps();
                engine.moveHolmes(holmesSteps);
                System.out.println("[RandomAI] Holmes moved " + holmesSteps + ".");
                break;

            case WATSON:
                int watsonSteps = randomSteps();
                engine.moveWatson(watsonSteps);
                System.out.println("[RandomAI] Watson moved " + watsonSteps + ".");
                break;

            case TOBY:
                int tobySteps = randomSteps();
                engine.moveToby(tobySteps);
                System.out.println("[RandomAI] Toby moved " + tobySteps + ".");
                break;

            case JOKER:
                playRandomJoker(engine);
                break;

            case ROTATE:
                playRandomRotate(engine);
                break;

            case EXCHANGE:
                playRandomExchange(engine);
                break;

            case ALIBI:
                playAlibi(engine);
                break;

            default:
                throw new IllegalStateException("Unsupported action type: " + actionType);
        }
    }

    private int chooseRandomUnusedActionTokenIndex(GameState gameState) {
        Token[] tokens = gameState.getActionTokens().getActionTokens();

        List<Integer> availableIndexes = new ArrayList<>();

        for (int i = 0; i < tokens.length; i++) {
            if (!tokens[i].isUsed()) {
                availableIndexes.add(i);
            }
        }

        if (availableIndexes.isEmpty()) {
            throw new IllegalStateException("No available action tokens.");
        }

        return availableIndexes.get(random.nextInt(availableIndexes.size()));
    }

    private int randomSteps() {
        return random.nextBoolean() ? 1 : 2;
    }

    private void playRandomJoker(GameEngine engine) {
        GameState gameState = engine.getGameState();

        if (gameState.getTurnManager().isJackTurn() && random.nextBoolean()) {
            engine.skipJokerMove();
            System.out.println("[RandomAI] Jack skipped Joker move.");
            return;
        }

        String[] detectives = {"Holmes", "Watson", "Toby"};
        String detectiveName = detectives[random.nextInt(detectives.length)];

        engine.moveDetectiveWithJoker(detectiveName);

        System.out.println("[RandomAI] Joker moved " + detectiveName + " by 1.");
    }

    private void playRandomRotate(GameEngine engine) {
        List<Tile> tiles = engine.getGameState().getBoard().getAllTiles();

        Tile tile = tiles.get(random.nextInt(tiles.size()));
        int rotations = 1 + random.nextInt(3);

        engine.rotateTile(tile, rotations);

        System.out.println(
                "[RandomAI] Rotated tile id=" + tile.getId()
                        + " at (" + tile.getRow() + "," + tile.getCol() + ")"
                        + " rotations=" + rotations
        );
    }

    private void playRandomExchange(GameEngine engine) {
        List<Tile> tiles = engine.getGameState().getBoard().getAllTiles();

        Tile tileA = tiles.get(random.nextInt(tiles.size()));
        Tile tileB = tiles.get(random.nextInt(tiles.size()));

        while (tileA == tileB) {
            tileB = tiles.get(random.nextInt(tiles.size()));
        }

        int oldRowA = tileA.getRow();
        int oldColA = tileA.getCol();
        int oldRowB = tileB.getRow();
        int oldColB = tileB.getCol();

        engine.exchangeTiles(tileA, tileB);

        System.out.println(
                "[RandomAI] Exchanged tiles: "
                        + "(" + oldRowA + "," + oldColA + ") <-> "
                        + "(" + oldRowB + "," + oldColB + ")"
        );
    }

    private void playAlibi(GameEngine engine) {
        GameState gameState = engine.getGameState();

        if (gameState.getTurnManager().isInvestigatorTurn()) {
            engine.investigatorDrawsAlibi();
            System.out.println("[RandomAI] Investigator drew an alibi.");
        } else if (gameState.getTurnManager().isJackTurn()) {
            engine.jackDrawsAlibi();
            System.out.println("[RandomAI] Jack drew an alibi.");
        } else {
            throw new IllegalStateException("Cannot draw alibi. Current player: "
                    + gameState.getTurnManager().getCurrentPlayer());
        }
    }
}