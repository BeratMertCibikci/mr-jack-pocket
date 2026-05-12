package main;

import ai.AIMove;
import ai.MoveGenerator;
import engine.GameEngine;

import java.util.List;

public class MoveGeneratorTest {

    public static void main(String[] args) {
        GameEngine engine = new GameEngine("Investigator");
        engine.startGame();

        System.out.println("=== MoveGenerator Test Started ===");
        System.out.println("Round: " + engine.getRoundNumber());
        System.out.println("Current player: " + engine.getCurrentPlayer());

        List<AIMove> moves = MoveGenerator.generateMoves(engine);

        System.out.println("Legal move count: " + moves.size());

        for (AIMove move : moves) {
            System.out.println("- " + move);
        }

        System.out.println("=== MoveGenerator Test Finished ===");
    }
}