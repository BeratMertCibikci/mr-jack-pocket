package model;

public class DetectiveTokens{
    private final Token HOLMES = new Token("Holmes", "");
    private final Token WATSON = new Token("Watson", "");
    private final Token TOBY = new Token("Toby", "");
    Token[] detectiveTokens;

    DetectiveTokens{
        detectiveTokens = new Token[]{HOLMES, WATSON, TOBY};
    }

    public String head(Token t){
        return t.head();
    }
}