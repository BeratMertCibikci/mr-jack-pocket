package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerState implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Player player;
    private List<Token> ownedTurnTokens;

    public PlayerState(Player player) {
        this.player = player;
        this.ownedTurnTokens = new ArrayList<>();
    }

    public Player getPlayer() {
        return player;
    }

    public void addTurnToken(Token token) {
        if (token == null) {
            throw new IllegalArgumentException("Turn token cannot be null.");
        }

        ownedTurnTokens.add(token);
    }

    public List<Token> getOwnedTurnTokens() {
        return new ArrayList<>(ownedTurnTokens);
    }

    public int getOwnedTurnTokenCount() {
        return ownedTurnTokens.size();
    }

    public PlayerState deepCopy(){
        PlayerState copy = new PlayerState(this.player);

        for (Token token : this.ownedTurnTokens){
            copy.ownedTurnTokens.add(token.deepCopy());
        }
        return copy;
    }
}