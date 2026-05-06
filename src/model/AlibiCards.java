package model;
import java.io.Serializable;
public class AlibiCards implements Serializable {
    private int id;
    private GameCharacter character;
    private int hourglassValue;
    private boolean isDrawn;
    private String owner;

    public AlibiCards(int id, GameCharacter character, int hourglassValue) {
        this.id = id;
        this.character = character;
        this.hourglassValue = hourglassValue;
        this.isDrawn = false;
        this.owner = "Deck";
    }

    public int getId() {
        return id;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public int getHourglassValue() {
        return hourglassValue;
    }

    public boolean isDrawn() {
        return isDrawn;
    }

    public void setDrawn(boolean drawn) {
        isDrawn = drawn;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}