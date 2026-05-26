package main;

import ai.RandomAI;
import engine.GameEngine;
import model.GameCharacter;

public class RandomAIFullGameTest {

    public static void main(String[] args) {
        GameEngine engine = new GameEngine("Investigator");
        engine.startGame();

        RandomAI randomAI = new RandomAI();

        //System.out.println("=== Random AI Full Game Test Started ===");
        //System.out.println("Jack identity: " + engine.getGameState().getJackCharacter().getName());

        while (!engine.getGameState().isGameOver()) {
            //System.out.println();
            //System.out.println("========== ROUND " + engine.getRoundNumber() + " ==========");

            while (!engine.isRoundOver() && !engine.getGameState().isGameOver()) {
                //System.out.println("--------------------------------");
                randomAI.play(engine);
            }

            if (!engine.getGameState().isGameOver()) {
                //System.out.println("--------------------------------");
                //System.out.println("Ending round " + engine.getRoundNumber() + "...");

                engine.endRound();

                printRoundSummary(engine);
            }
        }

        //System.out.println();
        //System.out.println("=== Random AI Full Game Test Finished ===");
        //System.out.println("Winner: " + engine.getGameState().getWinner());
    }

    private static void printRoundSummary(GameEngine engine) {
        int suspectCount = 0;

        System.out.println("After witness phase:");

        for (GameCharacter character : engine.getGameState().getCharacters()) {
            if (character.isSuspect()) {
                suspectCount++;
            }
        }

        System.out.println("Remaining suspects: " + suspectCount);

        for (GameCharacter character : engine.getGameState().getCharacters()) {
            if (character.isSuspect()) {
                System.out.println("- " + character.getName()
                        + " | visible=" + character.isVisible()
                        + " | jack=" + character.isJack());
            }
        }

        System.out.println("Game over: " + engine.getGameState().isGameOver());
        System.out.println("Winner: " + engine.getGameState().getWinner());
    }
}