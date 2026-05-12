package model;

public class HourglassCalculator {

    private static final double AVERAGE_ALIBI_HOURGLASS = 8.0 / 9.0;

    private HourglassCalculator() {
        // Utility class
    }

    public static int calculateKnownJackTurnTokenHourglasses(PlayerState jackPlayerState) {
        if (jackPlayerState == null) {
            return 0;
        }

        // In this implementation, each turn token owned by Jack is worth 1 hourglass.
        return jackPlayerState.getOwnedTurnTokenCount();
    }

    public static int calculateActualJackHourglasses(
            AlibiDeckManager alibiDeckManager,
            PlayerState jackPlayerState
    ) {
        int total = 0;

        if (alibiDeckManager != null) {
            total += alibiDeckManager.getJackHourglassTotal();
        }

        total += calculateKnownJackTurnTokenHourglasses(jackPlayerState);

        return total;
    }

    public static double calculateEstimatedJackHourglassesForInvestigator(
            AlibiDeckManager alibiDeckManager,
            PlayerState jackPlayerState
    ) {
        double knownTurnTokenHourglasses =
                calculateKnownJackTurnTokenHourglasses(jackPlayerState);

        double estimatedHiddenAlibiHourglasses = 0.0;

        if (alibiDeckManager != null) {
            estimatedHiddenAlibiHourglasses =
                    alibiDeckManager.getJackHiddenAlibiDrawCount()
                            * AVERAGE_ALIBI_HOURGLASS;
        }

        return knownTurnTokenHourglasses + estimatedHiddenAlibiHourglasses;
    }
}