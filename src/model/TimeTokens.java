package model;

public class TimeTokens {
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
        switch (token.getFrontSide()) {
            case "one":
                return 1;
            case "two":
                return 2;
            case "three":
                return 3;
            case "four":
                return 4;
            case "five":
                return 5;
            case "six":
                return 6;
            case "seven":
                return 7;
            case "eight":
                return 8;
            default:
                System.err.println("Token pas reconnue");
                return 0;
        }
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
}