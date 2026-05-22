package main;

import ai.AIDifficulty;
import ai.AIMove;
import ai.EvaluationPerspective;
import ai.MinimaxAI;
import ai.MoveGenerator;
import engine.GameEngine;
import model.Token;

import java.util.List;
import java.util.Random;

public class AIComparisonTest {

    private static final Random RANDOM = new Random(42);

    public static void main(String[] args) {
        GameEngine baseEngine = new GameEngine("Investigator");
        baseEngine.startGame();

        printInitialState(baseEngine);

        EvaluationPerspective perspective = getPerspective(baseEngine);

        System.out.println();
        System.out.println("=== AI MOVE COMPARISON ===");
        System.out.println("Current player: " + baseEngine.getCurrentPlayer());
        System.out.println("Perspective: " + perspective);
        System.out.println();

        compareDifficulty(baseEngine, AIDifficulty.EASY, perspective);
        compareDifficulty(baseEngine, AIDifficulty.MEDIUM, perspective);
        compareDifficulty(baseEngine, AIDifficulty.HARD, perspective);
        compareDifficulty(baseEngine, AIDifficulty.EXPERT, perspective);
        compareDifficulty(baseEngine, AIDifficulty.ULTRA, perspective);
    }

    private static void compareDifficulty(
            GameEngine baseEngine,
            AIDifficulty difficulty,
            EvaluationPerspective perspective
    ) {
        GameEngine engineCopy = copyEngine(baseEngine);

        System.out.println("--- " + difficulty + " ---");

        if (difficulty == AIDifficulty.EASY) {
            compareRandomAI(engineCopy);
            return;
        }

        int depth = getDepthForDifficulty(difficulty);

        MinimaxAI ai = new MinimaxAI(depth, perspective);

        long startTime = System.nanoTime();
        AIMove move = ai.chooseBestMove(engineCopy);
        long endTime = System.nanoTime();

        double elapsedMs = (endTime - startTime) / 1_000_000.0;

        System.out.println("Depth: " + depth);
        System.out.println("Selected move: " + move);
        System.out.println("Nodes visited: " + ai.getNodesVisited());
        System.out.println("Leaf evaluations: " + ai.getLeafEvaluations());
        System.out.println("Pruned branches: " + ai.getPrunedBranches());
        System.out.printf("Elapsed time: %.2f ms%n", elapsedMs);
        System.out.println();
    }

    private static void compareRandomAI(GameEngine engine) {
        List<AIMove> legalMoves = MoveGenerator.generateMoves(engine);

        if (legalMoves.isEmpty()) {
            System.out.println("Selected move: none");
            System.out.println("Reason: no legal moves available");
            System.out.println();
            return;
        }

        long startTime = System.nanoTime();
        AIMove move = legalMoves.get(RANDOM.nextInt(legalMoves.size()));
        long endTime = System.nanoTime();

        double elapsedMs = (endTime - startTime) / 1_000_000.0;

        System.out.println("Depth: random");
        System.out.println("Legal moves: " + legalMoves.size());
        System.out.println("Selected move: " + move);
        System.out.println("Nodes visited: 0");
        System.out.println("Leaf evaluations: 0");
        System.out.println("Pruned branches: 0");
        System.out.printf("Elapsed time: %.2f ms%n", elapsedMs);
        System.out.println();
    }

    private static int getDepthForDifficulty(AIDifficulty difficulty) {
        switch (difficulty) {
            case MEDIUM:
                return 1;

            case HARD:
                return 2;

            case EXPERT:
                return 4;

            case ULTRA:
                return 8;

            default:
                throw new IllegalArgumentException(
                        "Difficulty has no minimax depth: " + difficulty
                );
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

    private static void printInitialState(GameEngine engine) {
        System.out.println("=== INITIAL STATE ===");
        System.out.println("Current player: " + engine.getCurrentPlayer());
        System.out.println("Round: " + engine.getGameState().getTurnManager().getRoundNumber());
        System.out.println("Action index: " + engine.getGameState().getTurnManager().getCurrentActionIndex());

        System.out.println();
        System.out.println("Action tokens:");

        Token[] tokens = engine.getGameState()
                .getActionTokens()
                .getActionTokens();

        for (int i = 0; i < tokens.length; i++) {
            System.out.println(i + " = " + tokens[i].getCurrentSide()
                    + ", used=" + tokens[i].isUsed());
        }
    }
}