package main;

import model.GameSetup;
import model.GameState;

public class Main {
    public static void main(String[] args) {
        GameSetup setup = new GameSetup();
        GameState gameState = setup.setupGame();

        System.out.println("Game setup completed.");
        System.out.println(gameState);
    }
}