package model;

public class DetectiveTokens{
    private final Token HOLMES = new Token("Holmes", "");
    private final Token WATSON = new Token("Watson", "");
    private final Token TOBY = new Token("Toby", "");



    public Token Holmes(){
        return HOLMES;
    }

    public Token Watson(){
        return WATSON;
    }

    public Token Toby(){
        return TOBY;
    }

    public String head(Token t){
        return t.head();
    }

}