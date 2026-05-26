package reseau;

import java.io.Serializable;
import engine.ActionType;
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

    public Message(MessageType type, String senderRole) {
        this.type = type;
        this.senderRole = senderRole;
    }


    public GameCharacter getCharacterPayload() { return characterPayload; }
    public void setCharacterPayload(GameCharacter cp) { this.characterPayload = cp; }

    public MessageType getType() { return type; }
    public String getSenderRole() { return senderRole; }
    
    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }
    
    public int getTokenIndex() { return tokenIndex; }
    public void setTokenIndex(int tokenIndex) { this.tokenIndex = tokenIndex; }
    
    public int getSteps() { return steps; }
    public void setSteps(int steps) { this.steps = steps; }
    
    public String getDetectiveName() { return detectiveName; }
    public void setDetectiveName(String detectiveName) { this.detectiveName = detectiveName; }
    
    public Tile getTileA() { return tileA; }
    public void setTileA(Tile tileA) { this.tileA = tileA; }
    
    public Tile getTileB() { return tileB; }
    public void setTileB(Tile tileB) { this.tileB = tileB; }
    
    public int getRotations() { return rotations; }
    public void setRotations(int rotations) { this.rotations = rotations; }
    
    public GameCharacter getAccusedCharacter() { return accusedCharacter; }
    public void setAccusedCharacter(GameCharacter accusedCharacter) { this.accusedCharacter = accusedCharacter; }

    public Object getGameStatePayload() { return gameStatePayload; }
    public void setGameStatePayload(Object gameStatePayload) { this.gameStatePayload = gameStatePayload; }
}