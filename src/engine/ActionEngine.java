package engine;

import model.GameCharacter;
import model.GameState;
import model.Tile;
import model.Token;

public class ActionEngine {
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

    public void jackDrawsAlibi() {
        ensureSelectedActionIs(ActionType.ALIBI);

        if (!gameState.getTurnManager().isJackTurn()) {
            throw new IllegalStateException("Only Jack can draw Jack alibi.");
        }

        gameState.getAlibiDeckManager().mrJackDraws(false);

        finishAction();
    }

    public void rotateTile(Tile tile) {
        ensureSelectedActionIs(ActionType.ROTATE);

        moveValidator.validateRotateTile(gameState.getBoard(), tile);

        tile.rotate();
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

    public void useJokerAsRotate(Tile tile) {
        ensureSelectedActionIs(ActionType.JOKER);

        moveValidator.validateRotateTile(gameState.getBoard(), tile);

        tile.rotate();
        gameState.updateVisibility();

        finishAction();
    }

    public GameCharacter useJokerAsAlibiForInvestigator() {
        ensureSelectedActionIs(ActionType.JOKER);

        if (!gameState.getTurnManager().isInvestigatorTurn()) {
            throw new IllegalStateException("Only Investigator can use Joker as investigator alibi.");
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

    public void useJokerAsAlibiForJack() {
        ensureSelectedActionIs(ActionType.JOKER);

        if (!gameState.getTurnManager().isJackTurn()) {
            throw new IllegalStateException("Only Jack can use Joker as Jack alibi.");
        }

        gameState.getAlibiDeckManager().mrJackDraws(false);

        finishAction();
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
    public boolean hasSelectedAction() {
        return selectedActionToken != null;
    }
}