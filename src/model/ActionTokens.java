package model;

import java.util.ArrayList;
import java.util.Random;

public class ActionTokens{
    private ArrayList<Token> actionTokens;
    private Random rand = new Random();

    ActionTokens(){
        actionTokens = new ArrayList<>();
        actionTokens.add(new Token("Holmes", "Alibi"));
        actionTokens.add(new Token("Watson", "The Dog"));
        actionTokens.add(new Token("Exchange", "Rotate"));
        actionTokens.add(new Token("Joker", "Rotate"));
    }

    public void lancer(){
        for (Token t : actionTokens){
            if (rand.nextBoolean()){
                t.turn();
            }
        }
    }

    public Token[] getActionTokens() {
        return actionTokens;
    }
}