package ai;

import engine.GameEngine;
import java.util.List;
import java.util.Random;

public class RandomAI implements AIPlayer {

    private final Random random = new Random();

    @Override
    public AIMove play(GameEngine engine) {
        List<AIMove> legalMoves = MoveGenerator.generateMoves(engine);

        if (legalMoves.isEmpty()) {
            throw new IllegalStateException("RandomAI cannot play. No legal moves available.");
        }

        AIMove selectedMove = legalMoves.get(random.nextInt(legalMoves.size()));

        //System.out.println("[RandomAI] Current player: " + engine.getCurrentPlayer());
        //System.out.println("[RandomAI] Legal move count: " + legalMoves.size());
        //System.out.println("[RandomAI] Selected move: " + selectedMove);

        MoveApplier.apply(engine, selectedMove);

        return selectedMove;
    }
}