package engine;

import model.GameCharacter;
import model.GameState;
import model.Tile;
import model.Token;

public class GameEngine {
    private GameState gameState;
    private ActionEngine actionEngine;
    private RoundEngine roundEngine;

    public GameEngine(String player1Role) {
        this.gameState = new GameState(player1Role);
    }

    public void startGame() {
        gameState.setupGame();

        this.actionEngine = new ActionEngine(gameState);
        this.roundEngine = new RoundEngine(gameState);
    }

    public Token selectActionToken(int index) {
        ensureGameRunning();
        return actionEngine.selectActionToken(index);
    }

    public ActionType getSelectedActionType() {
        ensureGameRunning();
        return actionEngine.getSelectedActionType();
    }

    public void moveHolmes(int steps) {
        ensureGameRunning();
        actionEngine.moveHolmes(steps);
    }

    public void moveWatson(int steps) {
        ensureGameRunning();
        actionEngine.moveWatson(steps);
    }

    public void moveToby(int steps) {
        ensureGameRunning();
        actionEngine.moveToby(steps);
    }

    public GameCharacter investigatorDrawsAlibi() {
        ensureGameRunning();
        return actionEngine.investigatorDrawsAlibi();
    }

    public void jackDrawsAlibi() {
        ensureGameRunning();
        actionEngine.jackDrawsAlibi();
    }

    public void rotateTile(Tile tile, int rotations) {
        ensureGameRunning();
        actionEngine.rotateTile(tile, rotations);
    }

    public void exchangeTiles(Tile tileA, Tile tileB) {
        ensureGameRunning();
        actionEngine.exchangeTiles(tileA, tileB);
    }

    public void moveDetectiveWithJoker(String detectiveName, int steps) {
        ensureGameRunning();
        actionEngine.moveDetectiveWithJoker(detectiveName, steps);
    }

    public void endRound() {
        ensureGameRunning();

        if (actionEngine.hasSelectedAction()) {
            throw new IllegalStateException("Resolve selected action before ending round.");
        }

        roundEngine.endRound();

        if (!gameState.isGameOver()) {
            actionEngine.resetRotatedTilesThisRound();
        }
    }

    public void accuse(GameCharacter accusedCharacter) {
        ensureGameRunning();

        if (accusedCharacter == null) {
            throw new IllegalArgumentException("Accused character cannot be null.");
        }

        if (accusedCharacter.isJack()) {
            gameState.finishGame("Investigator");
        } else {
            gameState.finishGame("Jack");
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public ActionEngine getActionEngine() {
        return actionEngine;
    }

    public RoundEngine getRoundEngine() {
        return roundEngine;
    }

    public String getCurrentPlayer() {
        return gameState.getTurnManager().getCurrentPlayer();
    }

    public int getRoundNumber() {
        return gameState.getTurnManager().getRoundNumber();
    }

    public int getCurrentActionIndex() {
        return gameState.getTurnManager().getCurrentActionIndex();
    }

    public boolean isRoundOver() {
        return gameState.getTurnManager().isRoundOver();
    }

    private void ensureGameRunning() {
        if (!gameState.isGameStarted()) {
            throw new IllegalStateException("Game has not started.");
        }

        if (gameState.isGameOver()) {
            throw new IllegalStateException("Game is already over.");
        }
    }
}