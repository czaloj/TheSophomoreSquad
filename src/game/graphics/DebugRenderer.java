package game.graphics;

import egl.*;
import egl.math.Matrix4;
import game.data.GameState;

/**
 * Renders the game as simple as possible
 */
public class DebugRenderer {
    private GameState state;
    private final Matrix4 cameraMatrix = new Matrix4();
    private final Box2DBatcher batcher = new Box2DBatcher();
    private final GLProgram progPolygon = new GLProgram(false);

    public DebugRenderer() {
        // Empty
    }


    public void init() {
        batcher.init();
        progPolygon.setHeader(4, 3);
        progPolygon.quickCreateResource("DebugRenderPolygon", "game/graphics/shaders/", "game/graphics/shaders/", null);

    }
    public void dispose() {
        batcher.dispose();
    }

    public void setState(GameState s) {
        state = s;
        state.physicsWorld.setDebugDraw(batcher);
    }


    public void draw() {
        // Set the camera
        Matrix4.createOrthographic2D(
                state.cameraCenter.x - state.cameraHalfViewSize.x,
                state.cameraCenter.x + state.cameraHalfViewSize.x,
                state.cameraCenter.y - state.cameraHalfViewSize.y,
                state.cameraCenter.y + state.cameraHalfViewSize.y,
                cameraMatrix);

        // Draw objects to vertex data
        state.physicsWorld.drawDebugData();
        batcher.pushData();


    }
}
