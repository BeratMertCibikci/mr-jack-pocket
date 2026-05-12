package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CharacterFactory implements Serializable {

    private static final long serialVersionUID = 1L;

    public static List<GameCharacter> createCharacters() {
        List<GameCharacter> characters = new ArrayList<>();

        characters.add(new GameCharacter(1, "Madame", "Purple"));
        characters.add(new GameCharacter(2, "Sgt Goodley", "Red"));
        characters.add(new GameCharacter(3, "Jeremy Bert", "Orange"));
        characters.add(new GameCharacter(4, "William Gull", "Black"));
        characters.add(new GameCharacter(5, "Miss Stealthy", "Gray"));
        characters.add(new GameCharacter(6, "John Smith", "Green"));
        characters.add(new GameCharacter(7, "Insp. Lestrade", "Blue"));
        characters.add(new GameCharacter(8, "John Pizer", "Brown"));
        characters.add(new GameCharacter(9, "Joseph Lane", "Yellow"));

        return characters;
    }
}