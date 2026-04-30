package model;

public class DetectiveTokens{
    private final Token HOLMES = new Token("Holmes", "Holmes");
    private final Token WATSON = new Token("Watson", "Watson");
    private final Token TOBY = new Token("Toby", "Toby");
    Token[] detectiveTokens;

    DetectiveTokens(){
        detectiveTokens = new Token[]{HOLMES, WATSON, TOBY};
    }

    public String head(Token t){
        return t.head();
    }
}