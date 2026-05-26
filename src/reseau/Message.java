package reseau;

import engine.ActionType;
import java.io.Serializable;
import model.GameCharacter;
import model.Tile;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum MessageType {
        CONNECT,
        START_GAME,
        ACTION_SELECT,
        ACTION_EXECUTE,
        UPDATE_STATE,
        UNDO,
        REDO,
        ERROR
    }

    private MessageType type;
    private String senderRole;

    private ActionType actionType;
    private int tokenIndex;
    private int steps;
    private String detectiveName;
    private Tile tileA;
    private Tile tileB;
    private int rotations;

    private GameCharacter accusedCharacter;
    private GameCharacter characterPayload;
    private Object gameStatePayload;

    // Network highlight payload
    private boolean hasHighlight = false;
    private int highlightRow1 = -1;
    private int highlightCol1 = -1;
    private int highlightRow2 = -1;
    private int highlightCol2 = -1;

    public Message(MessageType type, String senderRole) {
        this.type = type;
        this.senderRole = senderRole;
    }

    public MessageType getType() {
        return type;
    }

    public String getSenderRole() {
        return senderRole;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public int getTokenIndex() {
        return tokenIndex;
    }

    public void setTokenIndex(int tokenIndex) {
        this.tokenIndex = tokenIndex;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getDetectiveName() {
        return detectiveName;
    }

    public void setDetectiveName(String detectiveName) {
        this.detectiveName = detectiveName;
    }

    public Tile getTileA() {
        return tileA;
    }

    public void setTileA(Tile tileA) {
        this.tileA = tileA;
    }

    public Tile getTileB() {
        return tileB;
    }

    public void setTileB(Tile tileB) {
        this.tileB = tileB;
    }

    public int getRotations() {
        return rotations;
    }

    public void setRotations(int rotations) {
        this.rotations = rotations;
    }

    public GameCharacter getAccusedCharacter() {
        return accusedCharacter;
    }

    public void setAccusedCharacter(GameCharacter accusedCharacter) {
        this.accusedCharacter = accusedCharacter;
    }

    public GameCharacter getCharacterPayload() {
        return characterPayload;
    }

    public void setCharacterPayload(GameCharacter characterPayload) {
        this.characterPayload = characterPayload;
    }

    public Object getGameStatePayload() {
        return gameStatePayload;
    }

    public void setGameStatePayload(Object gameStatePayload) {
        this.gameStatePayload = gameStatePayload;
    }

    public boolean hasHighlight() {
        return hasHighlight;
    }

    public void setHighlight(int row, int col) {
        this.hasHighlight = true;
        this.highlightRow1 = row;
        this.highlightCol1 = col;
        this.highlightRow2 = -1;
        this.highlightCol2 = -1;
    }

    public void setHighlight(int row1, int col1, int row2, int col2) {
        this.hasHighlight = true;
        this.highlightRow1 = row1;
        this.highlightCol1 = col1;
        this.highlightRow2 = row2;
        this.highlightCol2 = col2;
    }

    public void clearHighlight() {
        this.hasHighlight = false;
        this.highlightRow1 = -1;
        this.highlightCol1 = -1;
        this.highlightRow2 = -1;
        this.highlightCol2 = -1;
    }

    public int getHighlightRow1() {
        return highlightRow1;
    }

    public int getHighlightCol1() {
        return highlightCol1;
    }

    public int getHighlightRow2() {
        return highlightRow2;
    }

    public int getHighlightCol2() {
        return highlightCol2;
    }
}