package model;

import java.util.ArrayList;
import java.util.List;

public class LineOfSightService {

    public void updateVisibility(Board board, DetectiveTokens detectiveTokens, List<GameCharacter> characters) {
        resetVisibility(characters);

        for (Token detective : detectiveTokens.getAllDetectives()) {
            List<Tile> visibleTiles = getVisibleTilesForDetective(board, detective);

            for (Tile tile : visibleTiles) {
                if (tile.hasCharacter()) {
                    tile.getCharacter().setVisible(true);
                }
            }
        }
    }

    public List<GameCharacter> getVisibleCharacters(
            Board board,
            DetectiveTokens detectiveTokens,
            List<GameCharacter> characters
    ) {
        updateVisibility(board, detectiveTokens, characters);

        List<GameCharacter> visibleCharacters = new ArrayList<>();

        for (GameCharacter character : characters) {
            if (character.isSuspect() && character.isVisible()) {
                visibleCharacters.add(character);
            }
        }

        return visibleCharacters;
    }

    public List<GameCharacter> getHiddenCharacters(
            Board board,
            DetectiveTokens detectiveTokens,
            List<GameCharacter> characters
    ) {
        updateVisibility(board, detectiveTokens, characters);

        List<GameCharacter> hiddenCharacters = new ArrayList<>();

        for (GameCharacter character : characters) {
            if (character.isSuspect() && !character.isVisible()) {
                hiddenCharacters.add(character);
            }
        }

        return hiddenCharacters;
    }

    public boolean canJackBeSeen(
            Board board,
            DetectiveTokens detectiveTokens,
            List<GameCharacter> characters,
            GameCharacter jackCharacter
    ) {
        if (jackCharacter == null) {
            throw new IllegalArgumentException("Jack character cannot be null.");
        }

        updateVisibility(board, detectiveTokens, characters);

        return jackCharacter.isVisible();
    }

    public List<Tile> getVisibleTilesForDetective(Board board, Token detective) {
        if (detective == null || !detective.isDetectiveToken()) {
            throw new IllegalArgumentException("Token must be a detective token.");
        }

        SightLine sightLine = getSightLineFromDetectivePosition(detective.getPosition());

        return getVisibleTilesInLine(
                board,
                sightLine.startRow,
                sightLine.startCol,
                sightLine.rowStep,
                sightLine.colStep,
                sightLine.direction
        );
    }

    private List<Tile> getVisibleTilesInLine(
            Board board,
            int row,
            int col,
            int rowStep,
            int colStep,
            Direction direction
    ) {
        List<Tile> visibleTiles = new ArrayList<>();

        if (!isInsideBoard(board, row, col)) {
            return visibleTiles;
        }

        Tile currentTile = board.getTile(row, col);

        if (currentTile == null) {
            return visibleTiles;
        }

        if (currentTile.blocksLineOfSight(direction)) {
            return visibleTiles;
        }

        visibleTiles.add(currentTile);

        while (true) {
            int nextRow = row + rowStep;
            int nextCol = col + colStep;

            if (!isInsideBoard(board, nextRow, nextCol)) {
                break;
            }

            Tile nextTile = board.getTile(nextRow, nextCol);

            if (nextTile == null) {
                break;
            }

            if (!canSightPassBetween(currentTile, nextTile, direction)) {
                break;
            }

            visibleTiles.add(nextTile);

            currentTile = nextTile;
            row = nextRow;
            col = nextCol;
        }

        return visibleTiles;
    }

    private boolean canSightPassBetween(Tile currentTile, Tile nextTile, Direction direction) {
        if (currentTile.hasBarricade() || nextTile.hasBarricade()) {
            return false;
        }

        boolean currentTileHasExit = currentTile.hasRoad(direction);
        boolean nextTileHasEntrance = nextTile.hasRoad(direction.opposite());

        return currentTileHasExit && nextTileHasEntrance;
    }

    private SightLine getSightLineFromDetectivePosition(int position) {
        switch (position) {
            case 0:
                return new SightLine(0, 0, 1, 0, Direction.SOUTH);
            case 1:
                return new SightLine(0, 1, 1, 0, Direction.SOUTH);
            case 2:
                return new SightLine(0, 2, 1, 0, Direction.SOUTH);

            case 3:
                return new SightLine(0, 2, 0, -1, Direction.WEST);
            case 4:
                return new SightLine(1, 2, 0, -1, Direction.WEST);
            case 5:
                return new SightLine(2, 2, 0, -1, Direction.WEST);

            case 6:
                return new SightLine(2, 2, -1, 0, Direction.NORTH);
            case 7:
                return new SightLine(2, 1, -1, 0, Direction.NORTH);
            case 8:
                return new SightLine(2, 0, -1, 0, Direction.NORTH);

            case 9:
                return new SightLine(2, 0, 0, 1, Direction.EAST);
            case 10:
                return new SightLine(1, 0, 0, 1, Direction.EAST);
            case 11:
                return new SightLine(0, 0, 0, 1, Direction.EAST);

            default:
                throw new IllegalArgumentException("Invalid detective position: " + position);
        }
    }

    private boolean isInsideBoard(Board board, int row, int col) {
        return row >= 0
                && row < board.getSize()
                && col >= 0
                && col < board.getSize();
    }

    private void resetVisibility(List<GameCharacter> characters) {
        for (GameCharacter character : characters) {
            character.setVisible(false);
        }
    }

    private static class SightLine {
        private int startRow;
        private int startCol;
        private int rowStep;
        private int colStep;
        private Direction direction;

        public SightLine(int startRow, int startCol, int rowStep, int colStep, Direction direction) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.rowStep = rowStep;
            this.colStep = colStep;
            this.direction = direction;
        }
    }
}