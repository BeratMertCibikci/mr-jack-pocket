package model;

import java.util.List;

public class WinConditionChecker {

    public boolean hasJackWonByTime(int currentRound, int maxRounds) {
        return currentRound > maxRounds;
    }

    public boolean hasInvestigatorWonByAccusation(GameCharacter accusedCharacter) {
        if (accusedCharacter == null) {
            return false;
        }

        return accusedCharacter.isJack();
    }

    public boolean hasJackWonByWrongAccusation(GameCharacter accusedCharacter) {
        if (accusedCharacter == null) {
            return false;
        }

        return !accusedCharacter.isJack();
    }

    public boolean hasOnlyJackRemaining(List<GameCharacter> characters, GameCharacter jackCharacter) {
        int activeSuspects = 0;

        for (GameCharacter character : characters) {
            if (character.isSuspect()) {
                activeSuspects++;
            }
        }

        return activeSuspects == 1 && jackCharacter.isSuspect();
    }
}