package engine;

import model.GameState;

public class RoundEngine {
    private static final int MAX_ROUNDS = 8;

    private GameState gameState;

    public RoundEngine(GameState gameState) {
        this.gameState = gameState;
    }

    public void endRound() {
        if (!gameState.getActionTokens().allTokensUsed()) {
            throw new IllegalStateException("Cannot end round before all action tokens are used.");
        }

        gameState.applyWitnessPhase();

        gameState.getTurnManager().startNextRound();

        if (gameState.getTurnManager().getRoundNumber() > MAX_ROUNDS) {
            gameState.finishGame("Jack");
            return;
        }

        gameState.getActionTokens().lancer();
        gameState.updateVisibility();
    }
}