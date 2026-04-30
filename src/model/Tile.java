package model;

import java.util.HashSet;
import java.util.Set;

public class Tile {
    private int id;
    private GameCharacter character;

    private Orientation orientation;

    private boolean isSuspectSide;
    private boolean isEliminated;

    private Set<Direction> walls;

    private Position position;

    private boolean hasBarricade;

    public Tile(int id, GameCharacter character, Position position) {
        this.id = id;
        this.character = character;
        this.position = position;

        this.orientation = Orientation.NORTH;
        this.isSuspectSide = true;
        this.isEliminated = false;
        this.walls = new HashSet<>();
        this.hasBarricade = false;

        if (character != null) {
            character.setTile(this);
        }
    }

    public Tile(int id, Position position) {
        this(id, null, position);
    }

    public int getId() {
        return id;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public boolean hasCharacter() {
        return character != null && isSuspectSide && !isEliminated;
    }

    public void setCharacter(GameCharacter character) {
        this.character = character;

        if (character != null) {
            character.setTile(this);
        }
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void rotateClockwise() {
        this.orientation = this.orientation.rotateClockwise();
    }

    public boolean isSuspectSide() {
        return isSuspectSide;
    }

    public void flipToEmptySide() {
        this.isSuspectSide = false;
    }

    public void flipToSuspectSide() {
        if (!isEliminated) {
            this.isSuspectSide = true;
        }
    }

    public boolean isEliminated() {
        return isEliminated;
    }

    public void eliminate() {
        this.isEliminated = true;
        this.isSuspectSide = false;

        if (character != null) {
            character.eliminate();
        }
    }

    public Set<Direction> getWalls() {
        return walls;
    }

    public void addWall(Direction direction) {
        walls.add(direction);
    }

    public void removeWall(Direction direction) {
        walls.remove(direction);
    }

    public boolean hasWall(Direction direction) {
        return walls.contains(direction);
    }

    public Position getPosition() {
        return position;
    }

    public int getRow() {
        return position.getRow();
    }

    public int getCol() {
        return position.getCol();
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setRow(int row) {
        this.position.setRow(row);
    }

    public void setCol(int col) {
        this.position.setCol(col);
    }

    public boolean hasBarricade() {
        return hasBarricade;
    }

    public void setBarricade(boolean hasBarricade) {
        this.hasBarricade = hasBarricade;
    }

    public boolean blocksLineOfSight(Direction direction) {
        return hasBarricade || hasWall(direction);
    }

    public boolean canBeSeen() {
        return hasCharacter() && !isEliminated;
    }
}