package model;

import java.util.HashSet;
import java.util.Set;

public class Tile {
    private int id;
    private GameCharacter character;

    private Orientation orientation;

    private boolean isSuspectSide;
    private boolean isEliminated;

    private Set<Direction> suspectSideRoads;
    private Set<Direction> emptySideRoads;

    private Position position;

    private boolean hasBarricade;

    public Tile(
            int id,
            GameCharacter character,
            Position position,
            Set<Direction> suspectSideRoads,
            Set<Direction> emptySideRoads
    ) {
        this.id = id;
        this.character = character;
        this.position = position;

        this.orientation = Orientation.NORTH;
        this.isSuspectSide = true;
        this.isEliminated = false;

        this.suspectSideRoads = new HashSet<>(suspectSideRoads);
        this.emptySideRoads = new HashSet<>(emptySideRoads);

        this.hasBarricade = false;

        if (character != null) {
            character.setTile(this);
        }
    }

    public int getId() {
        return id;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public void setCharacter(GameCharacter character) {
        this.character = character;

        if (character != null) {
            character.setTile(this);
        }
    }

    public boolean hasCharacter() {
        return character != null && isSuspectSide && !isEliminated;
    }

    public boolean isSuspectSide() {
        return isSuspectSide;
    }

    public boolean isEmptySide() {
        return !isSuspectSide;
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

    public boolean isSuspectVisible() {
        return hasCharacter();
    }

    public void clearSuspect() {
        eliminate();
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public boolean hasRoad(Direction direction) {
        if (isSuspectSide) {
            return suspectSideRoads.contains(direction);
        }

        return emptySideRoads.contains(direction);
    }

    public boolean hasWall(Direction direction) {
        return !hasRoad(direction);
    }

    public Set<Direction> getCurrentRoads() {
        if (isSuspectSide) {
            return new HashSet<>(suspectSideRoads);
        }

        return new HashSet<>(emptySideRoads);
    }

    public Set<Direction> getSuspectSideRoads() {
        return new HashSet<>(suspectSideRoads);
    }

    public Set<Direction> getEmptySideRoads() {
        return new HashSet<>(emptySideRoads);
    }

    public void rotateClockwise() {
        this.orientation = this.orientation.rotateClockwise();

        this.suspectSideRoads = rotateRoadSetClockwise(this.suspectSideRoads);
        this.emptySideRoads = rotateRoadSetClockwise(this.emptySideRoads);
    }

    public void rotate() {
        rotateClockwise();
    }

    private Set<Direction> rotateRoadSetClockwise(Set<Direction> roads) {
        Set<Direction> rotatedRoads = new HashSet<>();

        for (Direction road : roads) {
            rotatedRoads.add(road.rotateClockwise());
        }

        return rotatedRoads;
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
        return hasBarricade || !hasRoad(direction);
    }

    public boolean canBeSeen() {
        return hasCharacter() && !isEliminated;
    }
}