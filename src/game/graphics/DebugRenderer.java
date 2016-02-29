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
public class DebugRenderer implements DebugDraw {
    private GameState state;
    private SpriteBatch batch;
    private final Matrix4 cameraMatrix = new Matrix4();
    private final Matrix4 identityMatrix = new Matrix4();

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
        state.physicsWorld.setDebugDraw(this);
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

    @Override
    public void drawPoint(Vec2 argPoint, float argRadiusOnScreen, Color3f argColor) {

    }

    @Override
    public void drawSolidPolygon(Vec2[] vertices, int vertexCount, Color3f color) {

    }

    @Override
    public void drawCircle(Vec2 center, float radius, Color3f color) {

    }

    @Override
    public void drawSolidCircle(Vec2 center, float radius, Vec2 axis, Color3f color) {

    }

    @Override
    public void drawSegment(Vec2 p1, Vec2 p2, Color3f color) {

    }

    @Override
    public void drawTransform(Transform xf) {

    }

    @Override
    public void drawString(float x, float y, String s, Color3f color) {

    }
}
