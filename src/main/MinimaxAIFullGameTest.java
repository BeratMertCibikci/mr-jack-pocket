package main;

import ai.EvaluationPerspective;
import ai.Evaluator;
import ai.MinimaxAI;
import engine.GameEngine;
import model.GameCharacter;

public class MinimaxAIFullGameTest {

    public static void main(String[] args) {
        GameEngine engine = new GameEngine("Investigator");
        engine.startGame();

        MinimaxAI investigatorAI = new MinimaxAI(2, EvaluationPerspective.INVESTIGATOR);
        MinimaxAI jackAI = new MinimaxAI(2, EvaluationPerspective.JACK);

        System.out.println("=== Minimax AI Full Game Test Started ===");
        System.out.println("Jack identity: " + engine.getGameState().getJackCharacter().getName());

        while (!engine.getGameState().isGameOver()) {
            System.out.println();
            System.out.println("========== ROUND " + engine.getRoundNumber() + " ==========");

            while (!engine.isRoundOver() && !engine.getGameState().isGameOver()) {
                if (engine.getGameState().getTurnManager().isInvestigatorTurn()) {
                    investigatorAI.play(engine);
                } else {
                    jackAI.play(engine);
                }

                printShortStatus(engine);
            }

            if (!engine.getGameState().isGameOver()) {
                System.out.println("--- Ending round " + engine.getRoundNumber() + " ---");
                engine.endRound();
                printRoundSummary(engine);
            }
        }

        System.out.println();
        System.out.println("=== Minimax AI Full Game Test Finished ===");
        System.out.println("Winner: " + engine.getGameState().getWinner());
    }

    private static void printShortStatus(GameEngine engine) {
        System.out.println(
                "[Status] round=" + engine.getRoundNumber()
                        + " actionIndex=" + engine.getCurrentActionIndex()
                        + " currentPlayer=" + engine.getCurrentPlayer()
                        + " suspects=" + countSuspects(engine)
                        + " invScore=" + Evaluator.evaluate(engine.getGameState(), EvaluationPerspective.INVESTIGATOR)
                        + " jackScore=" + Evaluator.evaluate(engine.getGameState(), EvaluationPerspective.JACK)
        );
    }

    private static void printRoundSummary(GameEngine engine) {
        System.out.println("[Round Summary]");
        System.out.println("Game over: " + engine.getGameState().isGameOver());
        System.out.println("Winner: " + engine.getGameState().getWinner());
        System.out.println("Remaining suspects: " + countSuspects(engine));

        for (GameCharacter character : engine.getGameState().getCharacters()) {
            if (character.isSuspect()) {
                System.out.println("- " + character.getName()
                        + " | visible=" + character.isVisible()
                        + " | jack=" + character.isJack());
            }
        }
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