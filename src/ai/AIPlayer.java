package ai;

import engine.GameEngine;

public interface AIPlayer {
    AIMove play(GameEngine engine);
}