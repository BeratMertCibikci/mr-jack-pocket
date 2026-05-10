package engine;

import java.io.*;
//import model.GameState;

public class SaveManager{
    
    public static void saveGame(GameEngine engine, String filename){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));

            System.out.println("Saving...");

            out.writeObject(engine);

            out.close();

            System.out.println("Game Saved.");

            System.out.println(new java.io.File(filename).getAbsolutePath());

        } catch (Exception e) {
            System.out.println("SAVE FAILED");
            e.printStackTrace();
        }
    }

    public static GameEngine loadGame(String filename){
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));

            GameEngine loaded = (GameEngine) in.readObject();

            in.close();

            System.out.println("Game Loaded.");

            System.out.println(new java.io.File(filename).getAbsolutePath());

            return loaded;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}