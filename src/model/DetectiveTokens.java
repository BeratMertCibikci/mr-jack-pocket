package model;

import java.util.ArrayList;

public class DetectiveTokens{
    private ArrayList<Token> detectiveTokens;

    DetectiveTokens(){
        detectiveTokens = new ArrayList<>();
        detectiveTokens.add(new Token("Holmes", ""));
        detectiveTokens.add(new Token("Watson", ""));
        detectiveTokens.add(new Token("Toby", ""));
    }

    public String head(Token t){
        return t.head();
    }
}