package model;

public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public Direction rotateClockwise() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }
        public Direction rotateCounterClockwise() {

        switch (this) {

            case NORTH:

                return WEST;

            case WEST:

                return SOUTH;

            case SOUTH:

                return EAST;

            case EAST:

                return NORTH;

            default:

                throw new IllegalStateException("Unknown direction: " + this);

        }

    }

    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }
}