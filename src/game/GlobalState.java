package game;

import game.data.GameState;

public class GlobalState {
    public static final GlobalState instance = new GlobalState();

    // Holding ground for any game state that should be running or is the process of getting loaded
    public GameState state = null;

    // True if the game is in debug mode
    public boolean debugMode = true; // TODO: Default should be false

    private GlobalState() {
        // Empty
    }
}
