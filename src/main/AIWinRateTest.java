package main;

import ai.AIDifficulty;
import ai.AIFactory;
import ai.AIPlayer;
import ai.EvaluationPerspective;
import engine.GameEngine;

public class AIWinRateTest {

    private static final int GAMES_PER_DIFFICULTY = 40;
    private static final int MAX_ACTIONS_PER_GAME = 200;

    public static void main(String[] args) {
        runDifficulty(AIDifficulty.EASY);
        runDifficulty(AIDifficulty.MEDIUM);
        runDifficulty(AIDifficulty.HARD);
        runDifficulty(AIDifficulty.EXPERT);

        // ULTRA çok yavaş olabilir.
        // runDifficulty(AIDifficulty.ULTRA);
    }

    private static void runDifficulty(AIDifficulty difficulty) {
        int investigatorWins = 0;
        int jackWins = 0;
        int drawsOrStopped = 0;

        long totalTimeMs = 0;
        int totalActions = 0;
        int totalRounds = 0;
        int totalJackHourglasses = 0;

        long investigatorWinTimeMs = 0;
        int investigatorWinActions = 0;
        int investigatorWinRounds = 0;

        long jackWinTimeMs = 0;
        int jackWinActions = 0;
        int jackWinRounds = 0;
        int jackWinHourglasses = 0;

        System.out.println();
        System.out.println("======================================");
        System.out.println("AI vs AI Win Rate Test: " + difficulty);
        System.out.println("Games: " + GAMES_PER_DIFFICULTY);
        System.out.println("======================================");

        for (int gameIndex = 1; gameIndex <= GAMES_PER_DIFFICULTY; gameIndex++) {
            GameResult result = playOneGame(difficulty);

            totalTimeMs += result.elapsedMs;
            totalActions += result.actions;
            totalRounds += result.rounds;
            totalJackHourglasses += result.jackHourglasses;

            if ("Investigator".equals(result.winner)) {
                investigatorWins++;
                investigatorWinTimeMs += result.elapsedMs;
                investigatorWinActions += result.actions;
                investigatorWinRounds += result.rounds;

            } else if ("Jack".equals(result.winner)) {
                jackWins++;
                jackWinTimeMs += result.elapsedMs;
                jackWinActions += result.actions;
                jackWinRounds += result.rounds;
                jackWinHourglasses += result.jackHourglasses;

            } else {
                drawsOrStopped++;
            }

            System.out.println(
                    "Game " + gameIndex
                            + " winner: " + result.winner
                            + " | actions=" + result.actions
                            + " | rounds=" + result.rounds
                            + " | jackHourglasses=" + result.jackHourglasses
                            + " | time=" + result.elapsedMs + " ms"
            );
        }

        printSummary(
                difficulty,
                investigatorWins,
                jackWins,
                drawsOrStopped,
                totalTimeMs,
                totalActions,
                totalRounds,
                totalJackHourglasses,
                investigatorWinTimeMs,
                investigatorWinActions,
                investigatorWinRounds,
                jackWinTimeMs,
                jackWinActions,
                jackWinRounds,
                jackWinHourglasses
        );
    }

    private static GameResult playOneGame(AIDifficulty difficulty) {
        long start = System.nanoTime();

        GameEngine engine = new GameEngine("Investigator");
        engine.startGame();

        AIPlayer investigatorAI = AIFactory.create(
                difficulty,
                EvaluationPerspective.INVESTIGATOR
        );

        AIPlayer jackAI = AIFactory.create(
                difficulty,
                EvaluationPerspective.JACK
        );

        int actions = 0;

        while (!engine.getGameState().isGameOver()
                && actions < MAX_ACTIONS_PER_GAME) {

            if (engine.isRoundOver()) {
                engine.endRound();
                continue;
            }

            if (engine.getGameState().getTurnManager().isInvestigatorTurn()) {
                investigatorAI.play(engine);
            } else if (engine.getGameState().getTurnManager().isJackTurn()) {
                jackAI.play(engine);
            } else {
                throw new IllegalStateException(
                        "Unknown current player: " + engine.getCurrentPlayer()
                );
            }

            actions++;

            if (engine.isRoundOver()
                    && !engine.getGameState().isGameOver()) {
                engine.endRound();
            }
        }

        long end = System.nanoTime();
        long elapsedMs = Math.round((end - start) / 1_000_000.0);

        String winner;

        if (engine.getGameState().isGameOver()) {
            winner = engine.getGameState().getWinner();
        } else {
            winner = "Stopped";
        }

        int rounds = engine.getGameState().getTurnManager().getRoundNumber();
        int jackHourglasses = getJackHourglasses(engine);

        return new GameResult(
                winner,
                actions,
                rounds,
                jackHourglasses,
                elapsedMs
        );
    }

    private static int getJackHourglasses(GameEngine engine) {
        try {
            return engine.getGameState()
                    .getWinConditionChecker()
                    .calculateJackHourglassTotal(
                            engine.getGameState().getAlibiDeckManager(),
                            engine.getGameState().getJackPlayerState()
                    );
        } catch (Exception e) {
            return 0;
        }
    }

    private static void printSummary(
            AIDifficulty difficulty,
            int investigatorWins,
            int jackWins,
            int drawsOrStopped,
            long totalTimeMs,
            int totalActions,
            int totalRounds,
            int totalJackHourglasses,
            long investigatorWinTimeMs,
            int investigatorWinActions,
            int investigatorWinRounds,
            long jackWinTimeMs,
            int jackWinActions,
            int jackWinRounds,
            int jackWinHourglasses
    ) {
        int totalGames = investigatorWins + jackWins + drawsOrStopped;

        double investigatorRate = percent(investigatorWins, totalGames);
        double jackRate = percent(jackWins, totalGames);
        double stoppedRate = percent(drawsOrStopped, totalGames);

        System.out.println();
        System.out.println("----- SUMMARY " + difficulty + " -----");

        System.out.println("Investigator wins: " + investigatorWins
                + "/" + totalGames
                + " (" + String.format("%.1f", investigatorRate) + "%)");

        System.out.println("Jack wins: " + jackWins
                + "/" + totalGames
                + " (" + String.format("%.1f", jackRate) + "%)");

        System.out.println("Stopped/draws: " + drawsOrStopped
                + "/" + totalGames
                + " (" + String.format("%.1f", stoppedRate) + "%)");

        System.out.println();

        System.out.println("Average game time: "
                + String.format("%.2f", average(totalTimeMs, totalGames)) + " ms");

        System.out.println("Average actions per game: "
                + String.format("%.2f", average(totalActions, totalGames)));

        System.out.println("Average rounds per game: "
                + String.format("%.2f", average(totalRounds, totalGames)));

        System.out.println("Average Jack hourglasses at end: "
                + String.format("%.2f", average(totalJackHourglasses, totalGames)));

        System.out.println();

        if (investigatorWins > 0) {
            System.out.println("When Investigator wins:");
            System.out.println("  Avg time: "
                    + String.format("%.2f", average(investigatorWinTimeMs, investigatorWins)) + " ms");
            System.out.println("  Avg actions: "
                    + String.format("%.2f", average(investigatorWinActions, investigatorWins)));
            System.out.println("  Avg rounds: "
                    + String.format("%.2f", average(investigatorWinRounds, investigatorWins)));
        }

        if (jackWins > 0) {
            System.out.println("When Jack wins:");
            System.out.println("  Avg time: "
                    + String.format("%.2f", average(jackWinTimeMs, jackWins)) + " ms");
            System.out.println("  Avg actions: "
                    + String.format("%.2f", average(jackWinActions, jackWins)));
            System.out.println("  Avg rounds: "
                    + String.format("%.2f", average(jackWinRounds, jackWins)));
            System.out.println("  Avg Jack hourglasses: "
                    + String.format("%.2f", average(jackWinHourglasses, jackWins)));
        }
    }

    private static double percent(int value, int total) {
        if (total == 0) {
            return 0.0;
        }

        return value * 100.0 / total;
    }

    private static double average(long value, int count) {
        if (count == 0) {
            return 0.0;
        }

        return value / (double) count;
    }

    private static double average(int value, int count) {
        if (count == 0) {
            return 0.0;
        }

        return value / (double) count;
    }

    private static class GameResult {
        private final String winner;
        private final int actions;
        private final int rounds;
        private final int jackHourglasses;
        private final long elapsedMs;

        private GameResult(
                String winner,
                int actions,
                int rounds,
                int jackHourglasses,
                long elapsedMs
        ) {
            this.winner = winner;
            this.actions = actions;
            this.rounds = rounds;
            this.jackHourglasses = jackHourglasses;
            this.elapsedMs = elapsedMs;
        }
    }
}