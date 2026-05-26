package engine;

import java.io.Serializable;
import model.GameCharacter;
import model.GameState;
import model.Tile;
import model.Token;

public class GameEngine implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private GameState gameState;
    private ActionEngine actionEngine;
    private RoundEngine roundEngine;
    private GameHistory history;

    public GameEngine(String player1Role) {
        this.gameState = new GameState(player1Role); 
    }
    public GameEngine(GameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException("GameState cannot be null.");
        }

        this.gameState = gameState;
        this.actionEngine = new ActionEngine(gameState);
        this.roundEngine = new RoundEngine(gameState);
        this.history = new GameHistory();
    }

    public void startGame() {
        gameState.setupGame();

        this.actionEngine = new ActionEngine(gameState);
        this.roundEngine = new RoundEngine(gameState);

        this.history = new GameHistory();
    }

    public Token selectActionToken(int index) {
        ensureGameRunning();
        history.save(gameState);
        return actionEngine.selectActionToken(index);
    }

    public ActionType getSelectedActionType() {
        ensureGameRunning();
        return actionEngine.getSelectedActionType();
    }

    public void moveHolmes(int steps) {
        ensureGameRunning();
        actionEngine.moveHolmes(steps);
    }

    public void moveWatson(int steps) {
        ensureGameRunning();
        actionEngine.moveWatson(steps);
    }

    public void moveToby(int steps) {
        ensureGameRunning();
        actionEngine.moveToby(steps);
    }

    public void setGameState(model.GameState gameState) {
    
    this.gameState = gameState;
    
    
    if (gameState != null) {
        this.actionEngine = new engine.ActionEngine(gameState);
    }
    
    
}

    public GameCharacter investigatorDrawsAlibi() {
        ensureGameRunning();
        return actionEngine.investigatorDrawsAlibi();
    }
    public GameCharacter investigatorDrawsAlibiCardById(int cardId) {
        ensureGameRunning();
        return actionEngine.investigatorDrawsAlibiCardById(cardId);
    }

    public void jackDrawsAlibi() {
        ensureGameRunning();
        actionEngine.jackDrawsAlibi();
    }
    public void jackDrawsAlibiCardById(int cardId) {
        ensureGameRunning();
        actionEngine.jackDrawsAlibiCardById(cardId);
    }

    public void rotateTile(Tile tile, int rotations) {
        ensureGameRunning();
        actionEngine.rotateTile(tile, rotations);
    }

    public void exchangeTiles(Tile tileA, Tile tileB) {
        ensureGameRunning();
        actionEngine.exchangeTiles(tileA, tileB);
    }

    public void moveDetectiveWithJoker(String detectiveName) {
        ensureGameRunning();
        actionEngine.moveDetectiveWithJoker(detectiveName, 1); // Joker can only move 1 step
    }

    public void skipJokerMove() {
        ensureGameRunning();
        actionEngine.skipJokerMove();
    }

    public void endRound() {
        ensureGameRunning();

        if (actionEngine.hasSelectedAction()) {
            throw new IllegalStateException("Resolve selected action before ending round.");
        }
        //history.save(gameState);

        roundEngine.endRound();

        if (!gameState.isGameOver()) {
            gameState.resetRotatedTilesThisRound();
        }
    }

    public void accuse(GameCharacter accusedCharacter) {
        ensureGameRunning();

        if (accusedCharacter == null) {
            throw new IllegalArgumentException("Accused character cannot be null.");
        }

        history.save(gameState);

        if (accusedCharacter.isJack()) {
            gameState.finishGame("Investigator");
        } else {
            gameState.finishGame("Jack");
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public ActionEngine getActionEngine() {
        return actionEngine;
    }

    public RoundEngine getRoundEngine() {
        return roundEngine;
    }

    public String getCurrentPlayer() {
        return gameState.getTurnManager().getCurrentPlayer();
    }

    public int getRoundNumber() {
        return gameState.getTurnManager().getRoundNumber();
    }

    public int getCurrentActionIndex() {
        return gameState.getTurnManager().getCurrentActionIndex();
    }

    public boolean isRoundOver() {
        return gameState.getTurnManager().isRoundOver();
    }

    private void ensureGameRunning() {
        if (!gameState.isGameStarted()) {
            throw new IllegalStateException("Game has not started.");
        }

        if (gameState.isGameOver()) {
            throw new IllegalStateException("Game is already over.");
        }
    }

    public void undo(){
        if (!history.canUndo()){
            System.out.println("No undo available");
            return;
        }
        this.gameState = history.undo(this.gameState);

        this.actionEngine = new ActionEngine(gameState);
        this.roundEngine = new RoundEngine(gameState);
    }

    public void redo(){
        if (!history.canRedo()){
            System.out.println("No redo available");
            return;
        }
        this.gameState = history.redo(this.gameState);

        this.actionEngine = new ActionEngine(gameState);
        this.roundEngine = new RoundEngine(gameState);
    }

    public void saveState(){
        history.save(gameState);
    }

    public void saveGame(String filename){
        SaveManager.saveGame(this, filename);
    }

    public static GameEngine loadGame(String filename){
        return SaveManager.loadGame(filename);
    }

    /*
    public void loadGame(String filename){
        GameState loaded = SaveManager.loadGame(filename);

        if (loaded != null){
            this.gameState = loaded;

            this.actionEngine = new ActionEngine(gameState);
            this.roundEngine = new RoundEngine(gameState);
        }
    }
    */
}