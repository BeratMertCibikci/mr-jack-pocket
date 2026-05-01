package engine;

import model.Board;
import model.Tile;
import model.Token;

public class MoveValidator {

    public void validateDetectiveMove(Token detectiveToken, int steps) {
        if (detectiveToken == null) {
            throw new IllegalArgumentException("Detective token cannot be null.");
        }

        if (!detectiveToken.isDetectiveToken()) {
            throw new IllegalArgumentException("Token must be a detective token.");
        }

        if (steps <= 0) {
            throw new IllegalArgumentException("Detective must move at least 1 step.");
        }

        if (steps > 12) {
            throw new IllegalArgumentException("Detective cannot move more than 12 steps.");
        }
    }

    public void validateRotateTile(Board board, Tile tile) {
        validateTileOnBoard(board, tile);
    }

    public void validateExchangeTiles(Board board, Tile tileA, Tile tileB) {
        validateTileOnBoard(board, tileA);
        validateTileOnBoard(board, tileB);

        if (tileA == tileB) {
            throw new IllegalArgumentException("Cannot exchange a tile with itself.");
        }
    }

    private void validateTileOnBoard(Board board, Tile tile) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null.");
        }

        if (tile == null) {
            throw new IllegalArgumentException("Tile cannot be null.");
        }

        if (tile.getRow() < 0 || tile.getCol() < 0) {
            throw new IllegalArgumentException("Tile is not placed on board.");
        }

        Tile boardTile = board.getTile(tile.getRow(), tile.getCol());

        if (boardTile != tile) {
            throw new IllegalArgumentException("Tile does not belong to this board.");
        }
    }
}