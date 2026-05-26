package engine;

import java.io.Serializable;
import model.AlibiCards;
import model.GameCharacter;
import model.GameState;
import model.Tile;
import model.Token;

public class ActionEngine implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private GameState gameState;
    private MoveValidator moveValidator;

    private Token selectedActionToken;

    public ActionEngine(GameState gameState) {
        this.gameState = gameState;
        this.moveValidator = new MoveValidator();
        this.selectedActionToken = null;
    }

    public Token selectActionToken(int index) {
        if (selectedActionToken != null) {
            throw new IllegalStateException("Resolve selected action before choosing another token.");
        }

        if (gameState.getTurnManager().isRoundOver()) {
            throw new IllegalStateException("Round is already over.");
        }

        selectedActionToken = gameState.getActionTokens().selectToken(index);
        return selectedActionToken;
    }

    public ActionType getSelectedActionType() {
        ensureActionSelected();
        return parseActionType(selectedActionToken.getCurrentSide());
    }

    public void moveHolmes(int steps) {
        ensureSelectedActionIs(ActionType.HOLMES);

        Token holmes = gameState.getDetectiveTokens().getHolmes();
        moveValidator.validateDetectiveMove(holmes, steps);

        holmes.move(steps);
        gameState.updateVisibility();

        finishAction();
    }

    public void moveWatson(int steps) {
        ensureSelectedActionIs(ActionType.WATSON);

        Token watson = gameState.getDetectiveTokens().getWatson();
        moveValidator.validateDetectiveMove(watson, steps);

        watson.move(steps);
        gameState.updateVisibility();

        finishAction();
    }

    public void moveToby(int steps) {
        ensureSelectedActionIs(ActionType.TOBY);

        Token toby = gameState.getDetectiveTokens().getToby();
        moveValidator.validateDetectiveMove(toby, steps);

        toby.move(steps);
        gameState.updateVisibility();

        finishAction();
    }

    public GameCharacter investigatorDrawsAlibi() {
        ensureSelectedActionIs(ActionType.ALIBI);

        if (!gameState.getTurnManager().isInvestigatorTurn()) {
            throw new IllegalStateException("Only Investigator can draw investigator alibi.");
        }

        GameCharacter eliminatedCharacter =
                gameState.getAlibiDeckManager().investigatorDraws();

        if (eliminatedCharacter != null && eliminatedCharacter.getTile() != null) {
            eliminatedCharacter.getTile().flipToEmptySide();
        }

        gameState.updateVisibility();

        finishAction();

        return eliminatedCharacter;
    }
    public GameCharacter investigatorDrawsAlibiCardById(int cardId) {
        ensureSelectedActionIs(ActionType.ALIBI);

        if (!gameState.getTurnManager().isInvestigatorTurn()) {
            throw new IllegalStateException("Only Investigator can draw investigator alibi.");
        }

        GameCharacter eliminatedCharacter =
                gameState.getAlibiDeckManager().investigatorDrawsCardById(cardId);

        if (eliminatedCharacter != null && eliminatedCharacter.getTile() != null) {
            eliminatedCharacter.getTile().flipToEmptySide();
        }

        gameState.updateVisibility();

        finishAction();

        return eliminatedCharacter;
    }

public AlibiCards jackDrawsAlibi() {
    ensureSelectedActionIs(ActionType.ALIBI);

    if (!gameState.getTurnManager().isJackTurn()) {
        throw new IllegalStateException("Only Jack can draw Jack alibi.");
    }

    AlibiCards drawnCard = gameState.getAlibiDeckManager().mrJackDraws(false);

    finishAction();

    return drawnCard;
}
public AlibiCards jackDrawsAlibiCardById(int cardId) {
    ensureSelectedActionIs(ActionType.ALIBI);

    if (!gameState.getTurnManager().isJackTurn()) {
        throw new IllegalStateException("Only Jack can draw Jack alibi.");
    }

    AlibiCards drawnCard = gameState.getAlibiDeckManager().mrJackDrawsCardById(cardId, false);

    finishAction();

    return drawnCard;
}

    public void rotateTile(Tile tile, int rotations) {
        ensureSelectedActionIs(ActionType.ROTATE);

        moveValidator.validateRotateTile(gameState.getBoard(), tile);
        validateRotationCount(rotations);
        validateTileNotAlreadyRotatedThisRound(tile);

        for (int i = 0; i < rotations; i++) {
            tile.rotate();
        }

        gameState.markTileRotatedThisRound(tile);
        gameState.updateVisibility();

        finishAction();
    }

    public void exchangeTiles(Tile tileA, Tile tileB) {
        ensureSelectedActionIs(ActionType.EXCHANGE);

        moveValidator.validateExchangeTiles(gameState.getBoard(), tileA, tileB);

        gameState.getBoard().swapTiles(tileA, tileB);
        gameState.updateVisibility();

        finishAction();
    }

    public void moveDetectiveWithJoker(String detectiveName, int steps) {
        ensureSelectedActionIs(ActionType.JOKER);

        Token detective;

        if (detectiveName.equalsIgnoreCase("Holmes")) {
            detective = gameState.getDetectiveTokens().getHolmes();
        } else if (detectiveName.equalsIgnoreCase("Watson")) {
            detective = gameState.getDetectiveTokens().getWatson();
        } else if (detectiveName.equalsIgnoreCase("Toby")) {
            detective = gameState.getDetectiveTokens().getToby();
        } else {
            throw new IllegalArgumentException("Unknown detective: " + detectiveName);
        }

        moveValidator.validateDetectiveMove(detective, 1);

        detective.move(1);
        gameState.updateVisibility();

        finishAction();
    }

    public void skipJokerMove() {
        ensureSelectedActionIs(ActionType.JOKER);

        if (!gameState.getTurnManager().isJackTurn()) {
            throw new IllegalStateException("Only Jack can skip Joker move.");
        }

        finishAction();
    }

    public boolean hasSelectedAction() {
        return selectedActionToken != null;
    }
    
    public void resetRotatedTilesThisRound() {
        gameState.resetRotatedTilesThisRound();
    }

    public boolean hasTileBeenRotatedThisRound(Tile tile) {
        return gameState.hasTileBeenRotatedThisRound(tile);
    }


    private void validateRotationCount(int rotations) {
        if (rotations < 1 || rotations > 3) {
            throw new IllegalArgumentException("Rotation must be 1, 2, or 3.");
        }
    }

    private void validateTileNotAlreadyRotatedThisRound(Tile tile) {
        if (gameState.hasTileBeenRotatedThisRound(tile)) {
            throw new IllegalStateException("This tile has already been rotated this round.");
        }
    }

    private void finishAction() {
        selectedActionToken = null;
        gameState.getTurnManager().completeActionTurn();
    }

    private void ensureActionSelected() {
        if (selectedActionToken == null) {
            throw new IllegalStateException("No action token selected.");
        }
    }

    private void ensureSelectedActionIs(ActionType expected) {
        ensureActionSelected();

        ActionType actual = getSelectedActionType();

        if (actual != expected) {
            throw new IllegalStateException(
                    "Selected action is " + actual + ", expected " + expected
            );
        }
    }

    private ActionType parseActionType(String actionName) {
        switch (actionName) {
            case "Holmes":
                return ActionType.HOLMES;
            case "Watson":
                return ActionType.WATSON;
            case "Toby":
                return ActionType.TOBY;
            case "Alibi":
                return ActionType.ALIBI;
            case "Exchange":
                return ActionType.EXCHANGE;
            case "Rotate":
                return ActionType.ROTATE;
            case "Joker":
                return ActionType.JOKER;
            default:
                throw new IllegalArgumentException("Unknown action type: " + actionName);
        }
    }
}