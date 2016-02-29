package game.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL40.*;
import static org.lwjgl.opengl.GL41.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.opengl.GL44.*;

import egl.NativeMem;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import java.nio.ByteBuffer;

public class Box2DBatcher extends DebugDraw {
    /**
     * Position 3 floats
     * Color 4 bytes
     *
     */
    private static final int SIZE_VERTEX_POLY = 12 + 4;

    private int vertexPolys;
    private int vertexPolyCount;
    private int vertexPolyCapacity;
    private int vertexQuads;
    private int vertexQuadCount;
    private int vertexQuadCapacity;
    private int indexQuads;
    private int defaultVAO;

    public void init() {

        vertexPolys = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexPolys);
        vertexPolyCapacity = 10;
        vertexPolyCount = 0;
        ByteBuffer bbData = NativeMem.createByteBuffer(SIZE_VERTEX_POLY * vertexPolyCount);
        glBufferData(GL_ARRAY_BUFFER, );

        vertexQuads = glGenBuffers();
        glBindBuffer();

        indexQuads = glGenBuffers();
        defaultVAO = glGenVertexArrays();
    }
    public void dispose() {

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
