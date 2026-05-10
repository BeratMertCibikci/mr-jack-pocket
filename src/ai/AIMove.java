package ai;

import engine.ActionType;

public class AIMove {

    private final int actionTokenIndex;
    private final ActionType actionType;

    private String detectiveName;
    private int steps;

    private int row;
    private int col;
    private int rotations;

    private int rowA;
    private int colA;
    private int rowB;
    private int colB;

    private boolean jokerSkip;

    private AIMove(int actionTokenIndex, ActionType actionType) {
        this.actionTokenIndex = actionTokenIndex;
        this.actionType = actionType;

        this.detectiveName = null;
        this.steps = 0;

        this.row = -1;
        this.col = -1;
        this.rotations = 0;

        this.rowA = -1;
        this.colA = -1;
        this.rowB = -1;
        this.colB = -1;

        this.jokerSkip = false;
    }

    public static AIMove detectiveMove(
            int actionTokenIndex,
            ActionType actionType,
            String detectiveName,
            int steps
    ) {
        AIMove move = new AIMove(actionTokenIndex, actionType);
        move.detectiveName = detectiveName;
        move.steps = steps;
        return move;
    }

    public static AIMove jokerMove(
            int actionTokenIndex,
            String detectiveName
    ) {
        AIMove move = new AIMove(actionTokenIndex, ActionType.JOKER);
        move.detectiveName = detectiveName;
        move.steps = 1;
        return move;
    }

    public static AIMove jokerSkip(int actionTokenIndex) {
        AIMove move = new AIMove(actionTokenIndex, ActionType.JOKER);
        move.jokerSkip = true;
        return move;
    }

    public static AIMove rotate(
            int actionTokenIndex,
            int row,
            int col,
            int rotations
    ) {
        AIMove move = new AIMove(actionTokenIndex, ActionType.ROTATE);
        move.row = row;
        move.col = col;
        move.rotations = rotations;
        return move;
    }

    public static AIMove exchange(
            int actionTokenIndex,
            int rowA,
            int colA,
            int rowB,
            int colB
    ) {
        AIMove move = new AIMove(actionTokenIndex, ActionType.EXCHANGE);
        move.rowA = rowA;
        move.colA = colA;
        move.rowB = rowB;
        move.colB = colB;
        return move;
    }

    public static AIMove alibi(int actionTokenIndex) {
        return new AIMove(actionTokenIndex, ActionType.ALIBI);
    }

    public int getActionTokenIndex() {
        return actionTokenIndex;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getDetectiveName() {
        return detectiveName;
    }

    public int getSteps() {
        return steps;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getRotations() {
        return rotations;
    }

    public int getRowA() {
        return rowA;
    }

    public int getColA() {
        return colA;
    }

    public int getRowB() {
        return rowB;
    }

    public int getColB() {
        return colB;
    }

    public boolean isJokerSkip() {
        return jokerSkip;
    }

    @Override
    public String toString() {
        switch (actionType) {
            case HOLMES:
            case WATSON:
            case TOBY:
                return actionType + " tokenIndex=" + actionTokenIndex
                        + " detective=" + detectiveName
                        + " steps=" + steps;

            case JOKER:
                if (jokerSkip) {
                    return "JOKER tokenIndex=" + actionTokenIndex + " skip";
                }

                return "JOKER tokenIndex=" + actionTokenIndex
                        + " detective=" + detectiveName;

            case ROTATE:
                return "ROTATE tokenIndex=" + actionTokenIndex
                        + " tile=(" + row + "," + col + ")"
                        + " rotations=" + rotations;

            case EXCHANGE:
                return "EXCHANGE tokenIndex=" + actionTokenIndex
                        + " (" + rowA + "," + colA + ")"
                        + " <-> "
                        + "(" + rowB + "," + colB + ")";

            case ALIBI:
                return "ALIBI tokenIndex=" + actionTokenIndex;

            default:
                return actionType + " tokenIndex=" + actionTokenIndex;
        }
    }
}