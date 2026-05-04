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

        // Current turn token is given to Detective or Mr. Jack here.
        gameState.applyWitnessPhase();

        gameState.getTurnManager().startNextRound();

        int nextRound = gameState.getTurnManager().getRoundNumber();

        if (nextRound > MAX_ROUNDS) {
            gameState.finishGame("Jack");
            return;
        }

        // Draw the next turn/time token for the new round.
        gameState.drawNextTurnToken();

        if (nextRound % 2 == 1) {
            gameState.getActionTokens().lancer();
        } else {
            gameState.getActionTokens().flipTokensForEvenRound();
        }

        gameState.updateVisibility();
    }
}