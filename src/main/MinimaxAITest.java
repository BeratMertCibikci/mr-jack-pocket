package main;

import ai.EvaluationPerspective;
import ai.Evaluator;
import ai.MinimaxAI;
import engine.GameEngine;
import model.GameCharacter;

public class MinimaxAITest {

    public static void main(String[] args) {
        GameEngine engine = new GameEngine("Investigator");
        engine.startGame();

        MinimaxAI investigatorAI = new MinimaxAI(1, EvaluationPerspective.INVESTIGATOR);
        MinimaxAI jackAI = new MinimaxAI(1, EvaluationPerspective.JACK);

        System.out.println("=== Minimax AI Test Started ===");
        printStatus(engine);

        while (!engine.isRoundOver() && !engine.getGameState().isGameOver()) {
            System.out.println("--------------------------------");

            if (engine.getGameState().getTurnManager().isInvestigatorTurn()) {
                investigatorAI.play(engine);
            } else {
                jackAI.play(engine);
            }

            printStatus(engine);
        }

        if (!engine.getGameState().isGameOver()) {
            System.out.println("--------------------------------");
            System.out.println("Ending round...");
            engine.endRound();
            printStatus(engine);
        }

        System.out.println("=== Minimax AI Test Finished ===");
    }

    private static void printStatus(GameEngine engine) {
        double investigatorScore = Evaluator.evaluate(
                engine.getGameState(),
                EvaluationPerspective.INVESTIGATOR
        );

        double jackScore = Evaluator.evaluate(
                engine.getGameState(),
                EvaluationPerspective.JACK
        );

        System.out.println("Round: " + engine.getRoundNumber());
        System.out.println("Current player: " + engine.getCurrentPlayer());
        System.out.println("Action index: " + engine.getCurrentActionIndex());
        System.out.println("Round over: " + engine.isRoundOver());
        System.out.println("Suspects: " + countSuspects(engine));
        System.out.println("Investigator score: " + investigatorScore);
        System.out.println("Jack knowledge score: " + jackScore);
        System.out.println("Game over: " + engine.getGameState().isGameOver());
        System.out.println("Winner: " + engine.getGameState().getWinner());
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