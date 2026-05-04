package model;

import java.util.List;

public class WinConditionChecker {
    private static final int JACK_HOURGLASS_TARGET = 6;

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

    public boolean hasOnlyOneSuspectRemaining(List<GameCharacter> characters) {
        int activeSuspects = 0;

        for (GameCharacter character : characters) {
            if (character.isSuspect()) {
                activeSuspects++;
            }
        }

        return activeSuspects == 1;
    }

    public boolean hasOnlyJackRemaining(List<GameCharacter> characters, GameCharacter jackCharacter) {
        if (jackCharacter == null) {
            return false;
        }

        return hasOnlyOneSuspectRemaining(characters) && jackCharacter.isSuspect();
    }

    public int calculateJackHourglassTotal(
            AlibiDeckManager alibiDeckManager,
            PlayerState jackPlayerState
    ) {
        int total = 0;

        if (alibiDeckManager != null) {
            total += alibiDeckManager.getJackHourglassTotal();
        }

        if (jackPlayerState != null) {
            for (Token token : jackPlayerState.getOwnedTurnTokens()) {
                total += getTurnTokenValue(token);
            }
        }

        return total;
    }

    public boolean hasJackWonByHourglasses(
            AlibiDeckManager alibiDeckManager,
            PlayerState jackPlayerState
    ) {
        return calculateJackHourglassTotal(alibiDeckManager, jackPlayerState) >= JACK_HOURGLASS_TARGET;
    }

    public String determineWinnerAfterWitnessPhase(
            List<GameCharacter> characters,
            GameCharacter jackCharacter,
            AlibiDeckManager alibiDeckManager,
            PlayerState jackPlayerState
    ) {
        boolean investigatorReachedGoal = hasOnlyJackRemaining(characters, jackCharacter);
        boolean jackReachedGoal = hasJackWonByHourglasses(alibiDeckManager, jackPlayerState);

        if (investigatorReachedGoal && jackReachedGoal) {
            if (jackCharacter.isVisible()) {
                return "Investigator";
            }

            return "Jack";
        }

        if (investigatorReachedGoal) {
            return "Investigator";
        }

        if (jackReachedGoal) {
            return "Jack";
        }

        return null;
    }

    private int getTurnTokenValue(Token token) {
        if (token == null) {
            return 0;
        }

        switch (token.getFrontSide()) {
            case "one":
                return 1;
            case "two":
                return 2;
            case "three":
                return 3;
            case "four":
                return 4;
            case "five":
                return 5;
            case "six":
                return 6;
            case "seven":
                return 7;
            case "eight":
                return 8;
            default:
                return 0;
        }
    }
}