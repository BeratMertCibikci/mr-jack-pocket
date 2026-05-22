package ai;

import engine.GameEngine;
import model.GameState;
import model.AlibiCards;
import engine.ActionType;
import java.util.ArrayList;
import java.util.Comparator;

import java.util.List;

public class MinimaxAI implements AIPlayer {

    private final int depth;
    private final EvaluationPerspective perspective;
    private long nodesVisited;
    private long leafEvaluations;
    private long prunedBranches;

    public MinimaxAI(int depth, EvaluationPerspective perspective) {
        if (depth < 1) {
            throw new IllegalArgumentException("Depth must be at least 1.");
        }

        this.depth = depth;
        this.perspective = perspective;
    }
    public long getNodesVisited() {
        return nodesVisited;
    }

    public long getLeafEvaluations() {
        return leafEvaluations;
    }
    public long getPrunedBranches() {
        return prunedBranches;
    }

    @Override
    public void play(GameEngine engine) {
        AIMove bestMove = chooseBestMove(engine);

        //System.out.println("[MinimaxAI] Current player: " + engine.getCurrentPlayer());
        //System.out.println("[MinimaxAI] Selected move: " + bestMove);

        MoveApplier.apply(engine, bestMove);
    }

    public AIMove chooseBestMove(GameEngine engine) {
        nodesVisited = 0;
        leafEvaluations = 0;
        prunedBranches = 0;

        long startTime = System.nanoTime();

        List<AIMove> legalMoves = MoveGenerator.generateMoves(engine);

        if (legalMoves.isEmpty()) {
            throw new IllegalStateException("MinimaxAI cannot play. No legal moves available.");
        }
        legalMoves = orderMoves(engine, legalMoves);

        boolean maximizing = isInvestigatorTurn(engine);

        AIMove bestMove = null;
        double bestScore = maximizing
                ? Double.NEGATIVE_INFINITY
                : Double.POSITIVE_INFINITY;

        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        for (AIMove move : legalMoves) {
            double score = evaluateMove(
                    engine,
                    move,
                    depth - 1,
                    alpha,
                    beta
            );

            if (maximizing) {
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }

                alpha = Math.max(alpha, bestScore);
            } else {
                if (score < bestScore) {
                    bestScore = score;
                    bestMove = move;
                }

                beta = Math.min(beta, bestScore);
            }
        }

        long endTime = System.nanoTime();
        double elapsedMs = (endTime - startTime) / 1_000_000.0;

        //System.out.println("[MinimaxAI] Best score: " + bestScore);
        //System.out.println("[MinimaxAI] Nodes visited for this move: " + nodesVisited);
        //System.out.println("[MinimaxAI] Leaf evaluations for this move: " + leafEvaluations);
        //System.out.println("[MinimaxAI] Pruned branches for this move: " + prunedBranches);
        //System.out.printf("[MinimaxAI] Search time for this move: %.2f ms%n", elapsedMs);

        return bestMove;
    }
    private double minimax(
            GameEngine engine,
            int remainingDepth,
            double alpha,
            double beta
    ) {
        nodesVisited++;

        if (engine.getGameState().isGameOver()) {
            leafEvaluations++;
            return Evaluator.evaluate(engine.getGameState(), perspective);
        }

        if (engine.isRoundOver()) {
            int currentRound = engine.getRoundNumber();

            engine.endRound();

            if (engine.getGameState().isGameOver()) {
                leafEvaluations++;
                return Evaluator.evaluate(engine.getGameState(), perspective);
            }

            int nextRound = engine.getRoundNumber();

            if (nextRound % 2 == 1 && nextRound > currentRound) {
                leafEvaluations++;
                return Evaluator.evaluate(engine.getGameState(), perspective);
            }
        }

        if (remainingDepth == 0) {
            leafEvaluations++;
            return Evaluator.evaluate(engine.getGameState(), perspective);
        }

        List<AIMove> legalMoves = MoveGenerator.generateMoves(engine);

        if (legalMoves.isEmpty()) {
            leafEvaluations++;
            return Evaluator.evaluate(engine.getGameState(), perspective);
        }
        legalMoves = orderMoves(engine, legalMoves);

        boolean maximizing = isInvestigatorTurn(engine);

        if (maximizing) {
            double bestScore = Double.NEGATIVE_INFINITY;

            for (AIMove move : legalMoves) {
                double score = evaluateMove(
                        engine,
                        move,
                        remainingDepth - 1,
                        alpha,
                        beta
                );

                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);

                if (beta <= alpha) {
                    prunedBranches++;
                    break;
                }
            }

            return bestScore;
        } else {
            double bestScore = Double.POSITIVE_INFINITY;

            for (AIMove move : legalMoves) {
                double score = evaluateMove(
                        engine,
                        move,
                        remainingDepth - 1,
                        alpha,
                        beta
                );

                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);

                if (beta <= alpha) {
                    prunedBranches++;
                    break;
                }
            }

            return bestScore;
        }
    }
  
    private boolean isInvestigatorTurn(GameEngine engine) {
        return engine.getGameState().getTurnManager().isInvestigatorTurn();
    }

    private GameEngine copyEngine(GameEngine engine) {
        GameState copiedState = engine.getGameState().deepCopy();
        return new GameEngine(copiedState);
    }

    private double evaluateMove(
            GameEngine engine,
            AIMove move,
            int nextDepth,
            double alpha,
            double beta
    ) {
        if (move.getActionType() != ActionType.ALIBI) {
            GameEngine copiedEngine = copyEngine(engine);
            MoveApplier.apply(copiedEngine, move);

            return minimax(copiedEngine, nextDepth, alpha, beta);
        }

        return evaluateAlibiMove(engine, move, nextDepth, alpha, beta);
    }
    private double evaluateAlibiMove(
            GameEngine engine,
            AIMove move,
            int nextDepth,
            double alpha,
            double beta
    ) {
        List<AlibiCards> availableCards =
                engine.getGameState().getAlibiDeckManager().getAvailableCards();

        if (availableCards.isEmpty()) {
            GameEngine copiedEngine = copyEngine(engine);
            MoveApplier.apply(copiedEngine, move);

            return minimax(copiedEngine, nextDepth, alpha, beta);
        }

        double totalScore = 0.0;

        for (AlibiCards card : availableCards) {
            GameEngine copiedEngine = copyEngine(engine);

            MoveApplier.applyAlibiWithSpecificCard(
                    copiedEngine,
                    move,
                    card.getId()
            );

            totalScore += minimax(copiedEngine, nextDepth, alpha, beta);
        }

        return totalScore / availableCards.size();
    }
    private List<AIMove> orderMoves(GameEngine engine, List<AIMove> moves) {
        List<AIMove> orderedMoves = new ArrayList<>(moves);

        boolean maximizing = isInvestigatorTurn(engine);

        orderedMoves.sort(new Comparator<AIMove>() {
            @Override
            public int compare(AIMove first, AIMove second) {
                int firstPriority = getMovePriority(first, maximizing);
                int secondPriority = getMovePriority(second, maximizing);

                return Integer.compare(firstPriority, secondPriority);
            }
        });

        return orderedMoves;
    }

    private int getMovePriority(AIMove move, boolean maximizing) {
        if (maximizing) {
            return getInvestigatorMovePriority(move);
        }

        return getJackMovePriority(move);
    }

    private int getInvestigatorMovePriority(AIMove move) {
        switch (move.getActionType()) {
            case ALIBI:
                return 1;
            case EXCHANGE:
                return 2;
            case ROTATE:
                return 3;
            case HOLMES:
            case WATSON:
            case TOBY:
                return 4;
            case JOKER:
                return 5;
            default:
                return 99;
        }
    }

    private int getJackMovePriority(AIMove move) {
        switch (move.getActionType()) {
            case ROTATE:
                return 1;
            case EXCHANGE:
                return 2;
            case JOKER:
                return 3;
            case HOLMES:
            case WATSON:
            case TOBY:
                return 4;
            case ALIBI:
                return 5;
            default:
                return 99;
        }
    }
    
    
}