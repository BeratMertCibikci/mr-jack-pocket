package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
public class AlibiDeckManager implements Serializable {
    private List<AlibiCards> cards;
    private int jackHourglassTotal;

    public AlibiDeckManager(List<GameCharacter> characters) {
        this.cards = new ArrayList<>();
        this.jackHourglassTotal = 0;
        this.initializeDeck(characters);
    }

    private void initializeDeck(List<GameCharacter> characters) {
        cards.add(new AlibiCards(1, findCharacterByName(characters, "Madame"), 2));
        cards.add(new AlibiCards(2, findCharacterByName(characters, "Sgt Goodley"), 0));
        cards.add(new AlibiCards(3, findCharacterByName(characters, "Jeremy Bert"), 1));
        cards.add(new AlibiCards(4, findCharacterByName(characters, "William Gull"), 1));
        cards.add(new AlibiCards(5, findCharacterByName(characters, "Miss Stealthy"), 1));
        cards.add(new AlibiCards(6, findCharacterByName(characters, "John Smith"), 1));
        cards.add(new AlibiCards(7, findCharacterByName(characters, "Insp. Lestrade"), 0));
        cards.add(new AlibiCards(8, findCharacterByName(characters, "John Pizer"), 1));
        cards.add(new AlibiCards(9, findCharacterByName(characters, "Joseph Lane"), 1));
    }

    private GameCharacter findCharacterByName(List<GameCharacter> characters, String name) {
        for (GameCharacter character : characters) {
            if (character.getName().equals(name)) {
                return character;
            }
        }

        throw new IllegalArgumentException("Character not found: " + name);
    }

    public void shuffleCards() {
        Collections.shuffle(this.cards);
    }

    public AlibiCards drawCard() {
        for (AlibiCards card : cards) {
            if (!card.isDrawn()) {
                card.setDrawn(true);
                return card;
            }
        }

        return null;
    }

    public GameCharacter investigatorDraws() {
        AlibiCards card = drawCard();

        if (card != null) {
            card.setOwner("Investigator");

            GameCharacter character = card.getCharacter();
            character.eliminate();

            return character;
        }

        return null;
    }

    public AlibiCards mrJackDraws(boolean isInitialIdentity) {
        AlibiCards card = drawCard();

        if (card != null) {
            card.setOwner("Jack");

            if (!isInitialIdentity) {
                jackHourglassTotal += card.getHourglassValue();
            }

            return card;
        }

        return null;
    }

    public int getJackHourglassTotal() {
        return jackHourglassTotal;
    }

    public List<GameCharacter> getEliminatedCharacters() {
        List<GameCharacter> eliminated = new ArrayList<>();

        for (AlibiCards card : cards) {
            GameCharacter character = card.getCharacter();

            if (character.isEliminated()) {
                eliminated.add(character);
            }
        }

        return eliminated;
    }

    public List<AlibiCards> getCards() {
        return cards;
    }
}