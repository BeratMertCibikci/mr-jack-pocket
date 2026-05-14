package model;

import java.io.Serializable;

public class TimeTokens implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Token one = new Token("one", "Hourglass");
    private Token two = new Token("two", "Hourglass");
    private Token three = new Token("three", "Hourglass");
    private Token four = new Token("four", "Hourglass");
    private Token five = new Token("five", "Hourglass");
    private Token six = new Token("six", "Hourglass");
    private Token seven = new Token("seven", "Hourglass");
    private Token eight = new Token("eight", "Hourglass");

    private Token[] timeTokens;

    public TimeTokens() {
        timeTokens = new Token[]{one, two, three, four, five, six, seven, eight};
    }

    public int value(Token token) {
        return 1;
    }

    public Token extraite() {
        for (int i = 0; i < timeTokens.length; i++) {
            if (timeTokens[i] != null) {
                Token token = timeTokens[i];
                timeTokens[i] = null;
                return token;
            }
        }

        return null;
    }

    public Token[] getTimeTokens() {
        return timeTokens;
    }

    public TimeTokens deepCopy(){
        TimeTokens copy = new TimeTokens();

        Token[] original = this.timeTokens;
        Token[] copied = new Token[original.length];

        for (int i = 0; i < original.length; i++){
            if (original[i] != null){
                copied[i] = original[i].deepCopy();
            }else{
                copied[i] = null;
            }
        }
        copy.timeTokens = copied;

        return copy;
    }
}