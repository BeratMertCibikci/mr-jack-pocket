package main;

import ai.RandomAI;
import engine.GameEngine;

public class RandomAITest {

    public static void main(String[] args) {
        GameEngine engine = new GameEngine("Investigator");
        engine.startGame();

        RandomAI randomAI = new RandomAI();

        System.out.println("=== Random AI One Round Test Started ===");
        System.out.println("Round: " + engine.getRoundNumber());
        System.out.println("Jack identity: " + engine.getGameState().getJackCharacter().getName());

        while (!engine.isRoundOver() && !engine.getGameState().isGameOver()) {
            System.out.println("--------------------------------");
            randomAI.play(engine);
            System.out.println("Next player: " + engine.getCurrentPlayer());
        }

        if (!engine.getGameState().isGameOver()) {
            System.out.println("--------------------------------");
            System.out.println("Round is over. Ending round...");
            engine.endRound();
        }

        System.out.println("Winner: " + engine.getGameState().getWinner());
        System.out.println("Game over: " + engine.getGameState().isGameOver());
        System.out.println("=== Random AI One Round Test Finished ===");
    }
}