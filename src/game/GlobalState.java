package game;

import game.logic.GameState;

public class GlobalState {
    public static final GlobalState instance = new GlobalState();

    // Holding ground for any game state that should be running or is the process of getting loaded
    public GameState state = null;

    private GlobalState() {
        // Empty
    }
}
