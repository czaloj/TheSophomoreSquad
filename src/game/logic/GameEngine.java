package game.logic;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class GameEngine {
    public static void loadState(GameState state) {
        state.physicsWorld = new World(new Vec2(0, -10));
    }
}
