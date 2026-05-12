package main;

import ai.EvaluationPerspective;
import ai.Evaluator;
import ai.RandomAI;
import engine.GameEngine;
import model.GameCharacter;
import model.HourglassCalculator;

public class EvaluatorTest {

    public static void main(String[] args) {
        GameEngine engine = new GameEngine("Investigator");
        engine.startGame();

        RandomAI randomAI = new RandomAI();

        System.out.println("=== Evaluator Test Started ===");
        printEvaluation(engine);

        while (!engine.getGameState().isGameOver() && engine.getRoundNumber() <= 3) {
            System.out.println();
            System.out.println("========== ROUND " + engine.getRoundNumber() + " ==========");

            while (!engine.isRoundOver() && !engine.getGameState().isGameOver()) {
                randomAI.play(engine);
                printEvaluation(engine);
            }

            if (!engine.getGameState().isGameOver()) {
                System.out.println("--- Ending round " + engine.getRoundNumber() + " ---");
                engine.endRound();
                printEvaluation(engine);
            }
        }

        System.out.println("=== Evaluator Test Finished ===");
    }

    private static void printEvaluation(GameEngine engine) {
        double investigatorScore = Evaluator.evaluate(
                engine.getGameState(),
                EvaluationPerspective.INVESTIGATOR
        );

        double jackScore = Evaluator.evaluate(
                engine.getGameState(),
                EvaluationPerspective.JACK
        );

        int suspectCount = countSuspects(engine);

        int actualJackHourglasses =
                HourglassCalculator.calculateActualJackHourglasses(
                        engine.getGameState().getAlibiDeckManager(),
                        engine.getGameState().getJackPlayerState()
                );

        double estimatedJackHourglasses =
                HourglassCalculator.calculateEstimatedJackHourglassesForInvestigator(
                        engine.getGameState().getAlibiDeckManager(),
                        engine.getGameState().getJackPlayerState()
                );

        System.out.println(
                "[Eval] suspects=" + suspectCount
                        + " | investigatorScore=" + investigatorScore
                        + " | jackKnowledgeScore=" + jackScore
                        + " | actualJackHourglasses=" + actualJackHourglasses
                        + " | estimatedJackHourglasses=" + estimatedJackHourglasses
        );
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