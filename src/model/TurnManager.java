package model;

public class TurnManager {
    private int roundNumber;
    private int currentActionIndex;
    private String currentPlayer;
    private String startingPlayer;

    public TurnManager() {
        this.roundNumber = 1;
        this.currentActionIndex = 0;
        this.currentPlayer = "Investigator";
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getCurrentActionIndex() {
        return currentActionIndex;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isInvestigatorTurn() {
        return currentPlayer.equals("Investigator");
    }

    public boolean isJackTurn() {
        return currentPlayer.equals("Jack");
    }

    public void completeActionTurn() {
        currentActionIndex++;

        if (!isRoundOver()) {
            switchPlayer();
        }
    }

    public boolean isRoundOver() {
        return currentActionIndex >= 4;
    }

    public void startNextRound() {
        roundNumber++;
        currentActionIndex = 0;
        currentPlayer = getStartingPlayerForRound(roundNumber);
    }

    private void switchPlayer() {
        if (currentPlayer.equals("Investigator")) {
            currentPlayer = "Jack";
        } else {
            currentPlayer = "Investigator";
        }
    }

    private String getStartingPlayerForRound(int roundNumber) {
        if (roundNumber % 2 == 1) {
            return "Investigator";
        }
        return "Jack";
    }
}