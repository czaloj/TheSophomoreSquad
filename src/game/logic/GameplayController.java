package game.logic;

import game.GameSettings;
import game.data.GameState;

public class GameplayController {

    private GameState state;
    private GameSettings settings;
    private final PhysicsController physicsController = new PhysicsController();

    public void init(GameState s) {
        if (state != null) {
            // TODO: State cleanup
        }

        // Setup references
        state = s;
        settings = GameSettings.global;

        // TODO: Add extra stuff
    }

    public void update(float dt) {
        // TODO: Input and pre-physics logic

        // Integrate the physics world and collect all interactions
        physicsController.update(state, dt);

        // TODO: Interactions and cleanups
    }
}
