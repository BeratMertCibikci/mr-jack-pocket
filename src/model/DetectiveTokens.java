package model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class DetectiveTokens implements Serializable {
    private final Token holmes;
    private final Token watson;
    private final Token toby;

    private List<Token> allDetectives;

    public DetectiveTokens() {
        this.holmes = new Token(1, "Holmes", 11);
        this.watson = new Token(2, "Watson", 3);
        this.toby = new Token(3, "Toby", 7);

        this.allDetectives = new ArrayList<>();
        this.allDetectives.add(holmes);
        this.allDetectives.add(watson);
        this.allDetectives.add(toby);
    }

    public String head(Token token) {
        return token.getName() + " is at position " + token.getPosition();
    }

    public Token getHolmes() {
        return holmes;
    }

    public Token getWatson() {
        return watson;
    }

    public Token getToby() {
        return toby;
    }

    public List<Token> getAllDetectives() {
        return allDetectives;
    }
}