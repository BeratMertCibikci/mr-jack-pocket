package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class DetectiveTokens implements Serializable {

    private static final long serialVersionUID = 1L;
    
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

    public DetectiveTokens deepCopy(){
        DetectiveTokens copy = new DetectiveTokens();

        copyToken(this.holmes, copy.getHolmes());
        copyToken(this.watson, copy.getWatson());
        copyToken(this.toby, copy.getToby());

        return copy;
    }

    private void copyToken(Token original, Token target){
        target.setPosition(original.getPosition());

        if (original.isFrontSideUp() != target.isFrontSideUp()){
            target.turn();
        }
        target.setUsed(original.isUsed());
    }
}