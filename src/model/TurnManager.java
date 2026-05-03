package model;

public class TurnManager {
    private int roundNumber;
    private int currentActionIndex;
    //private String currentPlayer;

    public TurnManager() {
        this.roundNumber = 1;
        this.currentActionIndex = 0;
        //this.currentPlayer = "Investigator";
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getCurrentActionIndex() {
        return currentActionIndex;
    }

    public String getCurrentPlayer() {
        if (roundNumber % 2 == 1){
            String[] order = {"Investigator", "Jack", "Jack", "Investigator"};
            return order[currentActionIndex];
        }else{
            String order[] = {"Jack", "Investigator", "Investigator", "Jack"};
            return order[currentActionIndex];
        }
    }

    public boolean isInvestigatorTurn() {
        return getCurrentPlayer().equals("Investigator");
    }

    public boolean isJackTurn() {
        return getCurrentPlayer().equals("Jack");
    }

    public void nextActionTurn() {
        currentActionIndex ++;
    }

    public boolean isRoundOver() {
        return currentActionIndex >= 4;
    }

    public void startNextRound() {
        roundNumber++;
        currentActionIndex = 0;
        //currentPlayer = getStartingPlayerForRound(roundNumber);
    }

    /*
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
    */
}
