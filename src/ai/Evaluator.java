package ai;

import java.util.List;
import model.GameCharacter;
import model.GameState;
import model.HourglassCalculator;

public class Evaluator {

    private static final double WIN_SCORE = 100000.0;

    private static final double ELIMINATED_SUSPECT_WEIGHT = 100.0;
    private static final double VISIBILITY_BALANCE_WEIGHT = 40.0;
    private static final double JACK_HOURGLASS_WEIGHT = 120.0;
    private static final double JACK_GROUP_SIZE_WEIGHT = 50.0;


    private Evaluator() {
        // Utility class
    }

    public static double evaluate(GameState state, EvaluationPerspective perspective) {
        if (state == null) {
            throw new IllegalArgumentException("GameState cannot be null.");
        } 
        int suspectCount = countSuspects(state);
        int eliminatedSuspects = 9 - suspectCount;

        int visibilityBalance = calculateVisibilityBalance(state, suspectCount);

        double jackHourglasses = getJackHourglassesForEvaluation(state, perspective);

        double score = eliminatedSuspects * ELIMINATED_SUSPECT_WEIGHT
                + visibilityBalance * VISIBILITY_BALANCE_WEIGHT
                - jackHourglasses * JACK_HOURGLASS_WEIGHT;

        if (perspective == EvaluationPerspective.JACK) {
            int jackGroupSize = calculateJackGroupSize(state);
            score -= jackGroupSize * JACK_GROUP_SIZE_WEIGHT;
        }

        return score;
    }

    private static int countSuspects(GameState state) {
        int count = 0;

        for (GameCharacter character : state.getCharacters()) {
            if (character.isSuspect()) {
                count++;
            }
        }

        return count;
    }

    private static int calculateVisibilityBalance(GameState state, int suspectCount) {
        List<GameCharacter> visibleCharacters =
                state.getLineOfSightService().getVisibleCharacters(
                        state.getBoard(),
                        state.getDetectiveTokens(),
                        state.getCharacters()
                );

        int visibleSuspects = visibleCharacters.size();
        int hiddenSuspects = suspectCount - visibleSuspects;

        return Math.min(visibleSuspects, hiddenSuspects);
    }
    private static int calculateJackGroupSize(GameState state) {
        GameCharacter jackCharacter = state.getJackCharacter();

        if (jackCharacter == null || !jackCharacter.isSuspect()) {
            return 0;
        }

        List<GameCharacter> visibleCharacters =
                state.getLineOfSightService().getVisibleCharacters(
                        state.getBoard(),
                        state.getDetectiveTokens(),
                        state.getCharacters()
                );

        int visibleSuspects = 0;

        for (GameCharacter character : visibleCharacters) {
            if (character.isSuspect()) {
                visibleSuspects++;
            }
        }

        int suspectCount = countSuspects(state);
        int hiddenSuspects = suspectCount - visibleSuspects;

        if (jackCharacter.isVisible()) {
            return visibleSuspects;
        }

        return hiddenSuspects;
    }

    private static double getJackHourglassesForEvaluation(
            GameState state,
            EvaluationPerspective perspective
    ) {
        if (perspective == EvaluationPerspective.JACK) {
            return getActualJackHourglasses(state);
        }

        return getEstimatedJackHourglassesForInvestigator(state);
    }

    private static double getActualJackHourglasses(GameState state) {
        return HourglassCalculator.calculateActualJackHourglasses(
                state.getAlibiDeckManager(),
                state.getJackPlayerState()
        );
    }

    private static double getEstimatedJackHourglassesForInvestigator(GameState state) {
        return HourglassCalculator.calculateEstimatedJackHourglassesForInvestigator(
                state.getAlibiDeckManager(),
                state.getJackPlayerState()
        );
    }
}