package model;

import java.util.Random;

public class ActionTokens {
    private Token t1 = new Token("Holmes", "Alibi");
    private Token t2 = new Token("Watson", "Toby");
    private Token t3 = new Token("Exchange", "Rotate");
    private Token t4 = new Token("Joker", "Rotate");

    private Random rand = new Random();
    private Token[] actionTokens;

    public ActionTokens() {
        actionTokens = new Token[]{t1, t2, t3, t4};
    }

    public void lancer() {
        for (Token token : actionTokens) {
            token.resetUsed();

            if (rand.nextBoolean()) {
                token.turn();
            }
        }
    }

    public Token selectToken(int index) {
        if (index < 0 || index >= actionTokens.length) {
            throw new IndexOutOfBoundsException("Invalid action token index: " + index);
        }

        Token token = actionTokens[index];

        if (token.isUsed()) {
            throw new IllegalStateException("Action token already used: " + token.getCurrentSide());
        }

        token.use();

        return token;
    }

    public boolean allTokensUsed() {
        for (Token token : actionTokens) {
            if (!token.isUsed()) {
                return false;
            }
        }

        return true;
    }

    public Token[] getActionTokens() {
        return actionTokens;
    }

    public void flipTokensForEvenRound() {
        for (Token token : actionTokens) {
            token.resetUsed();
            token.turn();
        }
    }

    public ActionTokens deepCopy(){
        ActionTokens copy = new ActionTokens();

        copy.rand = this.rand;

        Token[] original = this.actionTokens;
        Token[] copied = new Token[original.length];

        for (int i = 0;  i < original.length; i++){
            copied[i] = original[i].deepCopy();
        }

        copy.actionTokens = copied;

        return copy;
    }
}