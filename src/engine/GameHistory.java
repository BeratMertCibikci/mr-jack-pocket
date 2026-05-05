package engine;

import java.util.Stack;
import model.GameState;

public class GameHistory{
    private Stack<GameState> undoStack;
    private Stack<GameState> redoStack;

    public GameHistory(){
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void save(GameState state){
        undoStack.push(state.deepCopy());
        redoStack.clear();
    }

    public GameState undo(GameState currentState){
        if (undoStack.size() <= 1){
            throw new IllegalStateException("No undo available");
        }
        
        undoStack.pop();
        redoStack.push(currentState.deepCopy());

        return undoStack.peek();
    }

    public GameState redo(GameState currentState){
        if (redoStack.isEmpty()){
            throw new IllegalStateException("No redo available");
        }

        GameState nextState = redoStack.pop();
        undoStack.push(nextState.deepCopy());
        
        return nextState;
    }

    public boolean canUndo(){
        return undoStack.size() > 1;
    }

    public boolean canRedo(){
        return !redoStack.isEmpty();
    }
}