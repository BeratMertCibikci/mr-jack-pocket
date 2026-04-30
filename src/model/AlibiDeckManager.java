import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlibiDeckManager {
    private List<AlibiCards> cards;
    private int jackHourglassTotal;
    private List<String> eliminatedCharacters;

    public AlibiDeckManager() {
        this.cards = new ArrayList<>();
        this.jackHourglassTotal = 0;
        this.eliminatedCharacters = new ArrayList<>();
        this.initializeDeck();
    }

    private void initializeDeck() {
        cards.add(new AlibiCards(1, "Madame", 2));
        cards.add(new AlibiCards(2, "Sgt Goodley", 0));
        cards.add(new AlibiCards(3, "Jeremy Bert", 1));
        cards.add(new AlibiCards(4, "William Gull", 1));
        cards.add(new AlibiCards(5, "Miss Stealthy", 1));
        cards.add(new AlibiCards(6, "John Smith", 1));
        cards.add(new AlibiCards(7, "Insp. Lestrade", 0));
        cards.add(new AlibiCards(8, "John Pizer", 1));
        cards.add(new AlibiCards(9, "Joseph Lane", 1));
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

    public String investigatorDraws() {
        AlibiCards card = drawCard();
        if (card != null) {
            card.setOwner("Investigator");
            eliminatedCharacters.add(card.getCharacter());
            return card.getCharacter();
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

    public List<String> getEliminatedCharacters() {
        return eliminatedCharacters;
    }
}