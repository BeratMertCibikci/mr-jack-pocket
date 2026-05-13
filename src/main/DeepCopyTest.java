package main;

import ai.AIMove;
import ai.MoveApplier;
import ai.MoveGenerator;
import engine.GameEngine;
import model.GameCharacter;

import java.util.List;

public class DeepCopyTest {

    public static void main(String[] args) {
        GameEngine originalEngine = new GameEngine("Investigator");
        originalEngine.startGame();

        int originalRoundBefore = originalEngine.getRoundNumber();
        int originalActionIndexBefore = originalEngine.getCurrentActionIndex();
        int originalSuspectsBefore = countSuspects(originalEngine);

        GameEngine copyEngine = new GameEngine(originalEngine.getGameState().deepCopy());

        List<AIMove> moves = MoveGenerator.generateMoves(copyEngine);

        if (moves.isEmpty()) {
            throw new IllegalStateException("No moves generated for copied engine.");
        }

        AIMove move = moves.get(0);

        System.out.println("=== DeepCopy Test Started ===");
        System.out.println("Move applied only on copy: " + move);

        MoveApplier.apply(copyEngine, move);

        System.out.println("--- Original engine ---");
        System.out.println("Round: " + originalEngine.getRoundNumber());
        System.out.println("Action index: " + originalEngine.getCurrentActionIndex());
        System.out.println("Suspects: " + countSuspects(originalEngine));

        System.out.println("--- Copy engine ---");
        System.out.println("Round: " + copyEngine.getRoundNumber());
        System.out.println("Action index: " + copyEngine.getCurrentActionIndex());
        System.out.println("Suspects: " + countSuspects(copyEngine));

        boolean originalUnchanged =
                originalEngine.getRoundNumber() == originalRoundBefore
                        && originalEngine.getCurrentActionIndex() == originalActionIndexBefore
                        && countSuspects(originalEngine) == originalSuspectsBefore;

        if (!originalUnchanged) {
            throw new IllegalStateException("Original engine changed after applying move to copy.");
        }

        System.out.println("Original unchanged: true");
        System.out.println("=== DeepCopy Test Finished ===");
    }

    private static int countSuspects(GameEngine engine) {
        int count = 0;

        for (GameCharacter character : engine.getGameState().getCharacters()) {
            if (character.isSuspect()) {
                count++;
            }
        }

        return count;
    }
}