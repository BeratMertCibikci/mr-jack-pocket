package ai;

public class AIFactory {

    private AIFactory() {
        // Utility class
    }

    public static AIPlayer create(AIDifficulty difficulty, EvaluationPerspective perspective) {
        switch (difficulty) {
            case EASY:
                return new RandomAI();

            case MEDIUM:
                return new MinimaxAI(1, perspective);

            case HARD:
                return new MinimaxAI(2, perspective);

            case EXPERT:
                return new MinimaxAI(4, perspective);
            
            case ULTRA:
                return new MinimaxAI(8, perspective);

            default:
                throw new IllegalArgumentException("Unknown AI difficulty: " + difficulty);
        }
    }
}