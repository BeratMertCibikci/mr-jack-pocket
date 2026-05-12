package engine;

import java.io.Serializable;
import java.util.Stack;
import model.GameState;

public class GameHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Stack<GameState> undoStack;
    private Stack<GameState> redoStack;

    public GameHistory() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void save(GameState state) {
        undoStack.push(state.deepCopy());
        redoStack.clear();
    }

    public GameState undo(GameState currentState) {
        if (!canUndo()) {
            throw new IllegalStateException("No undo available");
        }

        redoStack.push(currentState.deepCopy());

        return undoStack.pop().deepCopy();
    }

    public GameState redo(GameState currentState) {
        if (!canRedo()) {
            throw new IllegalStateException("No redo available");
        }

        undoStack.push(currentState.deepCopy());

        return redoStack.pop().deepCopy();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}