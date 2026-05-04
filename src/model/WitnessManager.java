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

    public List<GameCharacter> resolveAppealForWitnesses(
            List<GameCharacter> characters,
            GameCharacter jackCharacter,
            Token currentTurnToken,
            PlayerState detectivePlayerState,
            PlayerState jackPlayerState
    ) {
        if (characters == null) {
            throw new IllegalArgumentException("Characters cannot be null.");
        }

        if (jackCharacter == null) {
            throw new IllegalArgumentException("Jack character cannot be null.");
        }

        if (currentTurnToken == null) {
            throw new IllegalArgumentException("Current turn token cannot be null.");
        }

        if (detectivePlayerState == null || jackPlayerState == null) {
            throw new IllegalArgumentException("Player states cannot be null.");
        }

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
                shouldEliminate = character.isSuspect() && !character.isVisible();
            } else {
                shouldEliminate = character.isSuspect() && character.isVisible();
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

        if (jackVisible) {
            // Mr. Jack can be seen:
            // Investigator takes the current turn token.
            // It should NOT be hourglass side up.
            if (currentTurnToken.getCurrentSide().equals("Hourglass")) {
                currentTurnToken.turn();
            }

            detectivePlayerState.addTurnToken(currentTurnToken);
        } else {
            // Mr. Jack cannot be seen:
            // Mr. Jack takes the current turn token hourglass side up.
            if (!currentTurnToken.getCurrentSide().equals("Hourglass")) {
                currentTurnToken.turn();
            }

            jackPlayerState.addTurnToken(currentTurnToken);
        }

        return eliminatedCharacters;
    }
}