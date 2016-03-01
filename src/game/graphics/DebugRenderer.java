package game.graphics;

import egl.*;
import egl.math.Matrix4;
import game.data.GameState;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import javax.swing.*;

import static org.lwjgl.opengl.GL30.*;

/**
 * Renders the game as simple as possible
 */
public class DebugRenderer {
    private GameState state;
    private final Matrix4 cameraMatrix = new Matrix4();
    private final Box2DBatcher batcher = new Box2DBatcher();
    private final GLProgram progPolygon = new GLProgram(false);

    private int vertexDeclPolys;
    private int vertexDeclQuads;

    public DebugRenderer() {
        // Empty
    }


    public void init() {
        progPolygon.setHeader(4, 3);
        progPolygon.quickCreateResource(
            "DebugRenderPolygon",
            "game/graphics/shaders/VDebug.glsl",
            "game/graphics/shaders/FDebug.glsl",
            null);

        batcher.init();

        vertexDeclPolys = glGenVertexArrays();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, batcher.getPolyVBO());
        glBindVertexArray(vertexDeclPolys);
        GL20.glEnableVertexAttribArray(progPolygon.getAttribute("vPosition"));
        glVertexAttribIPointer(progPolygon.getAttribute("vPosition"), 3, GL11.GL_FLOAT, Box2DBatcher.SIZE_VERTEX_POLY, 0);
        GL20.glEnableVertexAttribArray(progPolygon.getAttribute("vColor"));
        glVertexAttribIPointer(progPolygon.getAttribute("vColor"), 4, GL11.GL_BYTE, Box2DBatcher.SIZE_VERTEX_POLY, 12);
        vertexDeclQuads = glGenVertexArrays();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, batcher.getQuadVBO());
        glBindVertexArray(vertexDeclQuads);
        // TODO: Fill out
        glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    public void dispose() {
        batcher.dispose();

        glDeleteVertexArrays(vertexDeclPolys);
        glDeleteVertexArrays(vertexDeclQuads);
    }

    public void setState(GameState s) {
        state = s;
        state.physicsWorld.setDebugDraw(batcher);
    }


    public void draw() {
        RasterizerState.CULL_NONE.set();
        DepthState.NONE.set();

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

        progPolygon.use();
        glBindVertexArray(vertexDeclPolys);

        GLUniform.setST(progPolygon.getUniform("unVP"), cameraMatrix, false);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, batcher.getPolyVertexCount());
    }
}
