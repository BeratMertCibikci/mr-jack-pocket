package model;

import java.util.ArrayList;
import java.util.List;

public class DetectiveTokens {
    
    public static class Token {
        private int id;
        private String name;
        private int position;
        private String currentSide;

        public Token(int id, String name, int initialPosition) {
            this.id = id;
            this.name = name;
            this.position = initialPosition;
            this.currentSide = "Standard";
        }

        public void move(int steps) {
            this.position = (this.position + steps) % 12;
        }

        public int getPosition() { return position; }
        public String getName() { return name; }
        public String getCurrentSide() { return currentSide; }
    }

    private final Token holmes;
    private final Token watson;
    private final Token toby;
    private List<Token> allDetectives;

    public DetectiveTokens() {
        this.holmes = new Token(1, "Holmes", 11);
        this.watson = new Token(2, "Watson", 3);
        this.toby = new Token(3, "Toby", 7);

        this.allDetectives = new ArrayList<>();
        allDetectives.add(holmes);
        allDetectives.add(watson);
        allDetectives.add(toby);
    }

    public String head(Token t) {
        return t.getName() + " is at position " + t.getPosition();
    }

    public Token getHolmes() { return holmes; }
    public Token getWatson() { return watson; }
    public Token getToby() { return toby; }
    public List<Token> getAllDetectives() { return allDetectives; }
}