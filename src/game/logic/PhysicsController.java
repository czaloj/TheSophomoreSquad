package game.logic;

import game.GameSettings;
import game.data.GameState;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 * \brief
 */
public class PhysicsController {
    public static final float DEFAULT_GRAVITY = -10;

    public static void initState(GameState state) {
        state.physicsWorld = new World(new Vec2(0, DEFAULT_GRAVITY));
    }

    public void update(GameState state, float dt) {
        // TODO: Perform analyses?

        // Integrate the physics world
        state.physicsWorld.step(dt, GameSettings.global.physicsVelocityIterations, GameSettings.global.physicsPositionIterations);

        // TODO: Parse interactions
    }
}
