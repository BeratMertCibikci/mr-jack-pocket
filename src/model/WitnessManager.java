package model;

import java.util.ArrayList;
import java.util.List;

public class WitnessManager {

    public boolean isJackVisible(GameCharacter jackCharacter) {
        if (jackCharacter == null) {
            throw new IllegalArgumentException("Jack character cannot be null.");
        }

        return jackCharacter.isVisible();
    }

    public List<GameCharacter> eliminateCharactersByWitness(
            List<GameCharacter> characters,
            GameCharacter jackCharacter
    ) {
        boolean jackVisible = isJackVisible(jackCharacter);

        List<GameCharacter> eliminatedCharacters = new ArrayList<>();

        for (GameCharacter character : characters) {
            if (character.isJack()) {
                continue;
            }

            if (character.isEliminated()) {
                continue;
            }

            boolean shouldEliminate;

            if (jackVisible) {
                shouldEliminate = !character.isVisible();
            } else {
                shouldEliminate = character.isVisible();
            }

            if (shouldEliminate) {
                character.eliminate();

                Tile tile = character.getTile();
                if (tile != null) {
                    tile.flipToEmptySide();
                }

                eliminatedCharacters.add(character);
            }
        }

        return eliminatedCharacters;
    }
}