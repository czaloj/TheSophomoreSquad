package game.graphics;

import egl.*;
import egl.math.Matrix4;
import egl.math.Vector4;
import game.data.GameState;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.JointEdge;

/**
 * Renders the game as simple as possible
 */
public class DebugRenderer {
    private GameState state;
    private SpriteBatch batch;
    private final Matrix4 cameraMatrix = new Matrix4();
    private final Matrix4 identityMatrix = new Matrix4();
    private final Box2DBatcher batcher = new Box2DBatcher();

    public DebugRenderer() {
        // Empty
    }


    public void init() {
        batch = new SpriteBatch(true);
    }
    public void dispose() {
        batch.dispose();
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

        // Draw objects
        batch.begin();
        state.physicsWorld.drawDebugData();
        batch.end(SpriteSortMode.BackToFront);
        batch.renderBatch(
            cameraMatrix, identityMatrix,
            BlendState.ALPHA_BLEND,
            SamplerState.POINT_WRAP,
            DepthState.DEFAULT,
            RasterizerState.CULL_NONE
        );
    }
}
