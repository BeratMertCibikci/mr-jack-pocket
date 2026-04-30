package model;

public class Tiles {
    Tile blue = new Tile("blue", Direction.NORTH, false);
    Tile orange = new Tile("orange", Direction.NORTH, false);
    Tile white = new Tile("white", Direction.NORTH, false);
    Tile yellow = new Tile("yellow", Direction.NORTH, false);
    Tile grey = new Tile("grey", Direction.NORTH, true);
    Tile pink = new Tile("pink", Direction.NORTH, false);
    Tile green = new Tile("green", Direction.NORTH, false);
    Tile black = new Tile("black", Direction.NORTH, false);
    Tile purple = new Tile("purple", Direction.NORTH, false);
    private Tile[] tiles;

    public Tiles() {
        tiles = new Tile[]{blue, orange, white, yellow, grey, pink, green, black, purple};
    }

    public Tile[] all() {
        return tiles;
    }

    public Tile get(int index) {
        return tiles[index];
    }

    public int size() {
        return tiles.length;
    }

    public int remainingSuspects() {
        int count = 0;

        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].isSuspectVisible()) {
                count++;
            }
        }

        return count;
    }
}

class Tile extends Token {
    private Direction wallDirection;
    private boolean greySpecialTile;

    Tile(String color, Direction wallDirection, boolean greySpecialTile) {
        super(color, "Empty");

        this.wallDirection = wallDirection;
        this.greySpecialTile = greySpecialTile;
    }

    public String color() {
        if (head().equals("Empty")) {
            return tail();
        }

        return head();
    }

    public boolean isSuspectVisible() {
        return !head().equals("Empty");
    }

    public void clearSuspect() {
        if (isSuspectVisible()) {
            turn();
        }
    }

    public void rotate() {
        switch (wallDirection) {
            case NORTH:
                wallDirection = Direction.EAST;
                break;
            case EAST:
                wallDirection = Direction.SOUTH;
                break;
            case SOUTH:
                wallDirection = Direction.WEST;
                break;
            case WEST:
                wallDirection = Direction.NORTH;
                break;
        }
    }

    public Direction wallDirection() {
        return wallDirection;
    }

    public boolean hasWall(Direction direction) {
        if (greySpecialTile && !isSuspectVisible()) {
            return false;
        }

        return wallDirection == direction;
    }

    public boolean hasRoad(Direction direction) {
        return !hasWall(direction);
    }
}