package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Board implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int SIZE = 3;

    private Tile[][] tiles;
    private Random random;

    public Board() {
        tiles = new Tile[SIZE][SIZE];
        random = new Random();
    }

    public void setupRandomBoard(Tile[] areas) {
        if (areas == null || areas.length != SIZE * SIZE) {
            throw new IllegalArgumentException("Board needs exactly 9 areas.");
        }

        List<Tile> shuffledAreas = new ArrayList<>();

        for (Tile area : areas) {
            shuffledAreas.add(area);
        }

        Collections.shuffle(shuffledAreas);

        int index = 0;

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Tile tile = shuffledAreas.get(index);

                randomizeOrientation(tile);
                setTile(row, column, tile);

                index++;
            }
        }
    }

    private void randomizeOrientation(Tile tile) {
        int rotations = random.nextInt(4);

        for (int i = 0; i < rotations; i++) {
            tile.rotate();
        }
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
            tile.setCol(column);
        }
    }

    public void swapTiles(Tile tileA, Tile tileB) {
        if (tileA == null || tileB == null) {
            throw new IllegalArgumentException("Tile cannot be null.");
        }

        int rowA = tileA.getRow();
        int columnA = tileA.getCol();

        int rowB = tileB.getRow();
        int columnB = tileB.getCol();

        validatePosition(rowA, columnA);
        validatePosition(rowB, columnB);

        tiles[rowA][columnA] = tileB;
        tiles[rowB][columnB] = tileA;

        tileA.setRow(rowB);
        tileA.setCol(columnB);

        tileB.setRow(rowA);
        tileB.setCol(columnA);
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

    public int getSize() {
        return SIZE;
    }

    private void validatePosition(int row, int column) {
        if (row < 0 || row >= SIZE || column < 0 || column >= SIZE) {
            throw new IndexOutOfBoundsException(
                    "Invalid board position: row=" + row + ", column=" + column
            );
        }
    }

    public Board deepCopy(List<GameCharacter> characterCopies){
        Board copy = new Board();
        for (int l = 0; l < SIZE; l++){
            for (int c = 0; c < SIZE; c++){
                Tile originalTile = this.tiles[l][c];

                if (originalTile != null){
                    GameCharacter originalCharacter = originalTile.getCharacter();
                    GameCharacter characterCopy = null;

                    if (originalCharacter != null){
                        characterCopy = findCharacterCopy(characterCopies, originalCharacter.getId());
                    }
                    Tile tileCopy = originalTile.deepCopy(characterCopy);
                    copy.setTile(l, c, tileCopy);
                }
            }
        }
        return copy;
    }

    private GameCharacter findCharacterCopy(List<GameCharacter> copies, int id){
        for (GameCharacter c : copies){
            if (c.getId() == id){
                return c;
            }
        }
        return null;
    }
}