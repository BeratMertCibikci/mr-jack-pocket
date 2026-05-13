package main;

import ai.EvaluationPerspective;
import ai.MinimaxAI;
import engine.GameEngine;
import model.Token;

public class MinimaxBenchmarkTest {

    private static final int RUNS = 10;

    public static void main(String[] args) {
        runBenchmark(1);
        runBenchmark(2);
        runBenchmark(3);
        runBenchmark(4);

    }

    private static void runBenchmark(int depth) {
        long totalNodes = 0;
        long totalLeaves = 0;
        double totalMs = 0.0;
        long totalPruned = 0;
        

        long minNodes = Long.MAX_VALUE;
        long maxNodes = Long.MIN_VALUE;

        for (int i = 0; i < RUNS; i++) {
            GameEngine engine = new GameEngine("Investigator");
            engine.startGame();
            printActionTokens(engine,depth, i + 1);

            MinimaxAI ai = new MinimaxAI(depth, EvaluationPerspective.INVESTIGATOR);

            long start = System.nanoTime();
            ai.chooseBestMove(engine);
            long end = System.nanoTime();

            double elapsedMs = (end - start) / 1_000_000.0;

            long nodes = ai.getNodesVisited();
            long leaves = ai.getLeafEvaluations();
            long pruned = ai.getPrunedBranches();
            
            totalPruned += pruned;
            totalNodes += nodes;
            totalLeaves += leaves;
            totalMs += elapsedMs;

            minNodes = Math.min(minNodes, nodes);
            maxNodes = Math.max(maxNodes, nodes);
        }

            System.out.println();
            System.out.println("=== Depth " + depth + " Benchmark, runs=" + RUNS + " ===");
            System.out.println("Average nodes visited: " + (totalNodes / RUNS));
            System.out.println("Average leaf evaluations: " + (totalLeaves / RUNS));
            System.out.println("Average pruned branches: " + (totalPruned / RUNS));
            System.out.printf("Average elapsed time: %.2f ms%n", totalMs / RUNS);
            System.out.println("Min nodes: " + minNodes);
            System.out.println("Max nodes: " + maxNodes);
    }
    private static void printActionTokens(GameEngine engine, int depth, int runNumber) {    
        Token[] tokens = engine.getGameState().getActionTokens().getActionTokens();

        System.out.print("[Depth " + depth + " | Run " + runNumber + "] Round "
                + engine.getRoundNumber()
                + " tokens: ");

        for (int i = 0; i < tokens.length; i++) {
            System.out.print(i + "=" + tokens[i].getCurrentSide());

            if (i < tokens.length - 1) {
                System.out.print(", ");
            }
        }

        System.out.println();
    }
}