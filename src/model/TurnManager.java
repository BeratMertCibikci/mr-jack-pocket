package model;

public class TurnManager {
    private int roundNumber;
    private int currentActionIndex;

    public TurnManager() {
        this.roundNumber = 1;
        this.currentActionIndex = 0;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getCurrentActionIndex() {
        return currentActionIndex;
    }

    public String getCurrentPlayer() {
        if (isRoundOver()) {
            return "RoundOver";
        }

        String[] order;

        if (roundNumber % 2 == 1) {
            order = new String[]{"Investigator", "Jack", "Jack", "Investigator"};
        } else {
            order = new String[]{"Jack", "Investigator", "Investigator", "Jack"};
        }

        return order[currentActionIndex];
    }

    public boolean isInvestigatorTurn() {
        return getCurrentPlayer().equals("Investigator");
    }

    public boolean isJackTurn() {
        return getCurrentPlayer().equals("Jack");
    }

    public void completeActionTurn() {
        currentActionIndex++;
    }

    public boolean isRoundOver() {
        return currentActionIndex >= 4;
    }

    public void startNextRound() {
        roundNumber++;
        currentActionIndex = 0;
    }
}