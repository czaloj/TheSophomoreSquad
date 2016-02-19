package game.logic;

import game.GameSettings;

public class GameplayController {

    private GameState state;
    private GameSettings settings;

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
        state.physicsWorld.step(dt, settings.physicsVelocityIterations, settings.physicsPositionIterations);

        // TODO: Interactions and cleanups
    }
}
