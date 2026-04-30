package model;

import java.util.Random;

public class ActionTokens{
    private Token t1 = new Token("Holmes", "Alibi");
    private Token t2 = new Token("Watson", "The Dog");
    private Token t3 = new Token("Exchange", "Rotate");
    private Token t4 = new Token("Joker", "Rotate");
    Token[] actionTokens;

    ActionTokens(){
        actionTokens = new Token[]{t1, t2, t3, t4};
    }

    public void lancer(){
        for (int i = 0; i < actionTokens.length; i++){
            int r = new Random().nextInt(10);
            while (r != 0){
                actionTokens[i].turn();
            }
        }
    }
}