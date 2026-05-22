package main;

import ai.AIMove;
import ai.EvaluationPerspective;
import ai.MinimaxAI;
import engine.GameEngine;
import model.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepthStabilityComparisonTest {

    private static final int[] DEPTHS = {4, 5, 6, 7, 8};
    private static final int REFERENCE_DEPTH = 8;

    // Depth 8 yavaş olabilir. İlk test için 3-5 iyi.
    private static final int RUNS = 5;

    public static void main(String[] args) {
        Map<Integer, Integer> sameAsReferenceCount = new HashMap<>();
        Map<Integer, Integer> totalNodesByDepth = new HashMap<>();
        Map<Integer, Long> totalTimeByDepthMs = new HashMap<>();

        List<String> differenceLogs = new ArrayList<>();

        for (int depth : DEPTHS) {
            sameAsReferenceCount.put(depth, 0);
            totalNodesByDepth.put(depth, 0);
            totalTimeByDepthMs.put(depth, 0L);
        }

        for (int run = 1; run <= RUNS; run++) {
            System.out.println();
            System.out.println("==================================================");
            System.out.println("RUN " + run);
            System.out.println("==================================================");

            GameEngine baseEngine = new GameEngine("Investigator");
            baseEngine.startGame();

            printStateInfo(baseEngine);

            EvaluationPerspective perspective = getPerspective(baseEngine);

            Map<Integer, AIMove> selectedMoves = new HashMap<>();
            Map<Integer, Long> nodesByDepth = new HashMap<>();
            Map<Integer, Long> leavesByDepth = new HashMap<>();
            Map<Integer, Long> prunesByDepth = new HashMap<>();
            Map<Integer, Double> timeByDepthMs = new HashMap<>();

            for (int depth : DEPTHS) {
                GameEngine engineCopy = copyEngine(baseEngine);

                MinimaxAI ai = new MinimaxAI(depth, perspective);

                long start = System.nanoTime();
                AIMove move = ai.chooseBestMove(engineCopy);
                long end = System.nanoTime();

                double elapsedMs = (end - start) / 1_000_000.0;

                selectedMoves.put(depth, move);
                nodesByDepth.put(depth, ai.getNodesVisited());
                leavesByDepth.put(depth, ai.getLeafEvaluations());
                prunesByDepth.put(depth, ai.getPrunedBranches());
                timeByDepthMs.put(depth, elapsedMs);

                totalNodesByDepth.put(
                        depth,
                        totalNodesByDepth.get(depth) + (int) ai.getNodesVisited()
                );

                totalTimeByDepthMs.put(
                        depth,
                        totalTimeByDepthMs.get(depth) + Math.round(elapsedMs)
                );
            }

            AIMove referenceMove = selectedMoves.get(REFERENCE_DEPTH);
            String referenceKey = moveKey(referenceMove);

            System.out.println();
            System.out.println("Reference depth " + REFERENCE_DEPTH + " move:");
            System.out.println(referenceMove);

            System.out.println();
            System.out.println("--- Depth results ---");

            for (int depth : DEPTHS) {
                AIMove move = selectedMoves.get(depth);
                boolean sameAsReference = moveKey(move).equals(referenceKey);

                if (sameAsReference) {
                    sameAsReferenceCount.put(depth, sameAsReferenceCount.get(depth) + 1);
                } else {
                    String log =
                            "Run " + run +
                            " | depth " + depth +
                            " differs from depth " + REFERENCE_DEPTH +
                            "\n  depth " + depth + ": " + move +
                            "\n  depth " + REFERENCE_DEPTH + ": " + referenceMove;

                    differenceLogs.add(log);
                }

                System.out.println(
                        "Depth " + depth
                                + " | sameAsDepth" + REFERENCE_DEPTH + "=" + sameAsReference
                                + " | move=" + move
                );
                System.out.println(
                        "        nodes=" + nodesByDepth.get(depth)
                                + ", leaves=" + leavesByDepth.get(depth)
                                + ", prunes=" + prunesByDepth.get(depth)
                                + ", time=" + String.format("%.2f", timeByDepthMs.get(depth)) + " ms"
                );
            }
        }

        printSummary(sameAsReferenceCount, totalNodesByDepth, totalTimeByDepthMs);
        printDifferences(differenceLogs);
    }

    private static void printSummary(
            Map<Integer, Integer> sameAsReferenceCount,
            Map<Integer, Integer> totalNodesByDepth,
            Map<Integer, Long> totalTimeByDepthMs
    ) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("SUMMARY");
        System.out.println("==================================================");

        System.out.println("Runs: " + RUNS);
        System.out.println("Reference depth: " + REFERENCE_DEPTH);
        System.out.println();

        for (int depth : DEPTHS) {
            int sameCount = sameAsReferenceCount.get(depth);
            double samePercent = (sameCount * 100.0) / RUNS;

            double avgNodes = totalNodesByDepth.get(depth) / (double) RUNS;
            double avgTime = totalTimeByDepthMs.get(depth) / (double) RUNS;

            System.out.println(
                    "Depth " + depth
                            + " | same as depth " + REFERENCE_DEPTH + ": "
                            + sameCount + "/" + RUNS
                            + " (" + String.format("%.1f", samePercent) + "%)"
                            + " | avg nodes=" + String.format("%.0f", avgNodes)
                            + " | avg time=" + String.format("%.2f", avgTime) + " ms"
            );
        }
    }

    private static void printDifferences(List<String> differenceLogs) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("DIFFERENT MOVE DETAILS");
        System.out.println("==================================================");

        if (differenceLogs.isEmpty()) {
            System.out.println("All depths selected the same moves as the reference depth.");
            return;
        }

        for (String log : differenceLogs) {
            System.out.println(log);
            System.out.println();
        }
    }

    private static String moveKey(AIMove move) {
        if (move == null) {
            return "null";
        }

        switch (move.getActionType()) {
            case HOLMES:
            case WATSON:
            case TOBY:
                return move.getActionType()
                        + "|token=" + move.getActionTokenIndex()
                        + "|detective=" + move.getDetectiveName()
                        + "|steps=" + move.getSteps();

            case JOKER:
                return move.getActionType()
                        + "|token=" + move.getActionTokenIndex()
                        + "|detective=" + move.getDetectiveName()
                        + "|skip=" + move.isJokerSkip();

            case ROTATE:
                return move.getActionType()
                        + "|token=" + move.getActionTokenIndex()
                        + "|row=" + move.getRow()
                        + "|col=" + move.getCol()
                        + "|rotations=" + move.getRotations();

            case EXCHANGE:
                return move.getActionType()
                        + "|token=" + move.getActionTokenIndex()
                        + "|a=(" + move.getRowA() + "," + move.getColA() + ")"
                        + "|b=(" + move.getRowB() + "," + move.getColB() + ")";

            case ALIBI:
                return move.getActionType()
                        + "|token=" + move.getActionTokenIndex();

            default:
                return move.toString();
        }
    }

    private static EvaluationPerspective getPerspective(GameEngine engine) {
        if (engine.getGameState().getTurnManager().isInvestigatorTurn()) {
            return EvaluationPerspective.INVESTIGATOR;
        }

        return EvaluationPerspective.JACK;
    }

    private static GameEngine copyEngine(GameEngine engine) {
        return new GameEngine(engine.getGameState().deepCopy());
    }

    private static void printStateInfo(GameEngine engine) {
        System.out.println("Current player: " + engine.getCurrentPlayer());
        System.out.println("Round: " + engine.getGameState().getTurnManager().getRoundNumber());
        System.out.println("Action index: " + engine.getGameState().getTurnManager().getCurrentActionIndex());

        System.out.println("Action tokens:");

        Token[] tokens = engine.getGameState()
                .getActionTokens()
                .getActionTokens();

        for (int i = 0; i < tokens.length; i++) {
            System.out.println(
                    "  " + i + " = " + tokens[i].getCurrentSide()
                            + ", used=" + tokens[i].isUsed()
            );
        }
    }
}