package model;

public class GameCharacter {
    private int id;
    private String name;
    private String color;

    private boolean isSuspect;
    private boolean isJack;
    private boolean isVisible;

    private Tile tile;

    public GameCharacter(int id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;

        this.isSuspect = true;
        this.isJack = false;
        this.isVisible = false;

        this.tile = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public boolean isSuspect() {
        return isSuspect;
    }

    public void eliminate() {
        this.isSuspect = false;
    }

    public void markAsSuspect() {
        this.isSuspect = true;
    }

    public boolean isJack() {
        return isJack;
    }

    public void setJack(boolean jack) {
        this.isJack = jack;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public boolean isEliminated() {
        return !isSuspect;
    }

    public GameCharacter deepCopy(){
        GameCharacter copy = new GameCharacter(this.id, this.name, this.color);
        copy.isSuspect = this.isSuspect;
        copy.isJack = this.isJack;
        copy.isVisible = this.isVisible;
        copy.tile = null;

        return copy;
    }
}