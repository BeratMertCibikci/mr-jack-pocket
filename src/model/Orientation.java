package model;

public enum Orientation {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public Orientation rotateClockwise() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }
}