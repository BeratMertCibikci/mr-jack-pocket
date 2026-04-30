import java.util.ArrayList;
import java.util.List;

public class Board {

    private static final int SIZE = 3;

    private Tile[][] tiles;

    public Board() {
        tiles = new Tile[SIZE][SIZE];
    }

    public Tile getTile(int row, int column) {
        validatePosition(row, column);
        return tiles[row][column];
    }

    public void setTile(int row, int column, Tile tile) {
        validatePosition(row, column);

        tiles[row][column] = tile;

        if (tile != null) {
            tile.setRow(row);
            tile.setColumn(column);
        }
    }

    public void swapTiles(Tile tileA, Tile tileB) {
        if (tileA == null || tileB == null) {
            throw new IllegalArgumentException("Tile cannot be null.");
        }

        int rowA = tileA.getRow();
        int columnA = tileA.getColumn();

        int rowB = tileB.getRow();
        int columnB = tileB.getColumn();

        validatePosition(rowA, columnA);
        validatePosition(rowB, columnB);

        tiles[rowA][columnA] = tileB;
        tiles[rowB][columnB] = tileA;

        tileA.setRow(rowB);
        tileA.setColumn(columnB);

        tileB.setRow(rowA);
        tileB.setColumn(columnA);
    }

    public List<Tile> getAllTiles() {
        List<Tile> allTiles = new ArrayList<>();

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (tiles[row][column] != null) {
                    allTiles.add(tiles[row][column]);
                }
            }
        }

        return allTiles;
    }

    public void resetBoard() {
        tiles = new Tile[SIZE][SIZE];
    }

    public Tile[][] getBoardForUI() {
        Tile[][] boardCopy = new Tile[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                boardCopy[row][column] = tiles[row][column];
            }
        }

        return boardCopy;
    }

    private void validatePosition(int row, int column) {
        if (row < 0 || row >= SIZE || column < 0 || column >= SIZE) {
            throw new IndexOutOfBoundsException(
                "Invalid board position: row=" + row + ", column=" + column
            );
        }
    }
}