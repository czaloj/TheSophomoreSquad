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
import egl.math.Color;
import egl.math.Colorf;
import game.GameSettings;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import java.nio.ByteBuffer;

public class Box2DBatcher extends DebugDraw {
    /**
     * Position 3 floats
     * Color 4 bytes
     *
     */
    public static final int SIZE_VERTEX_POLY = 12 + 4;
    /**
     * Position 3 floats
     * UV Coordinate 2 floats
     * Color 4 bytes
     */
    public static final int SIZE_VERTEX_QUAD = 12 + 8 + 4;

    /**
     * Vertex data for simple polygons
     */
    private int vertexPolys;
    private int vertexPolyCount;
    private int vertexPolyCapacity;
    private ByteBuffer dataPolys;
    private boolean updatePolys = false;

    /**
     * Vertex data for textured quads (circles, axes, etc.)
     */
    private int vertexQuads;
    private int vertexQuadCount;
    private int vertexQuadCapacity;
    private ByteBuffer dataQuads;
    private boolean updateQuads = false;

    public Box2DBatcher() {
        // Create the batcher with an identity transform
        super(new OBBViewportTransform());
        viewportTransform.setCamera(0.0f, 0.0f, 1.0f);
        viewportTransform.setCenter(0.0f, 0.0f);
        viewportTransform.setExtents(
            GameSettings.global.resolutionWidth * 0.5f,
            GameSettings.global.resolutionHeight * 0.5f
        );

        setFlags(DebugDraw.e_shapeBit);
    }

    public void init() {
        // Create the vertex buffer for the polygons
        vertexPolys = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexPolys);
        vertexPolyCapacity = 10;
        vertexPolyCount = 0;
        dataPolys = NativeMem.createByteBuffer(SIZE_VERTEX_POLY * vertexPolyCapacity);
        dataPolys.position(0);
        dataPolys.limit(SIZE_VERTEX_POLY * vertexPolyCapacity);
        glBufferData(GL_ARRAY_BUFFER, dataPolys, GL_DYNAMIC_DRAW);

        // Create the vertex buffer for the quads
        vertexQuads = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexQuads);
        vertexQuadCapacity = 10;
        vertexQuadCount = 0;
        dataQuads = NativeMem.createByteBuffer(SIZE_VERTEX_QUAD * vertexQuadCapacity);
        dataQuads.position(0);
        dataQuads.limit(SIZE_VERTEX_QUAD * vertexQuadCapacity);
        glBufferData(GL_ARRAY_BUFFER, dataQuads, GL_DYNAMIC_DRAW);
    }
    public void dispose() {
        glDeleteBuffers(vertexPolys);
        glDeleteBuffers(vertexQuads);
    }

    public int getPolyVBO() {
        return vertexPolys;
    }
    public int getQuadVBO() {
        return vertexQuads;
    }

    public int getPolyVertexCount() {
        return vertexPolyCount;
    }
    public int getQuadVertexCount() {
        return vertexQuadCount;
    }

    public void begin() {
        vertexPolyCount = 0;
        dataPolys.rewind();
        vertexQuadCount = 0;
        dataQuads.rewind();
    }


    @Override
    public void drawPoint(Vec2 argPoint, float argRadiusOnScreen, Color3f argColor) {
        System.out.println("Draw Point");
    }

    @Override
    public void drawSolidPolygon(Vec2[] vertices, int vertexCount, Color3f color) {
        System.out.println("Draw Polygon");
        vertexPolyCount += 3 * (vertexCount - 2);
        ensurePolyCapacity(vertexPolyCount);
        Color c = new Color().set(new Colorf(color.x, color.y, color.z));
        for (int i = 2; i < vertexCount; i++) {
            dataPolys.putFloat(vertices[0].x);
            dataPolys.putFloat(vertices[0].y);
            dataPolys.putFloat(0.0f);
            dataPolys.put(c.R);
            dataPolys.put(c.G);
            dataPolys.put(c.B);
            dataPolys.put(c.A);

            dataPolys.putFloat(vertices[i - 1].x);
            dataPolys.putFloat(vertices[i - 1].y);
            dataPolys.putFloat(0.0f);
            dataPolys.put(c.R);
            dataPolys.put(c.G);
            dataPolys.put(c.B);
            dataPolys.put(c.A);

            dataPolys.putFloat(vertices[i].x);
            dataPolys.putFloat(vertices[i].y);
            dataPolys.putFloat(0.0f);
            dataPolys.put(c.R);
            dataPolys.put(c.G);
            dataPolys.put(c.B);
            dataPolys.put(c.A);
        }
    }

    @Override
    public void drawCircle(Vec2 center, float radius, Color3f color) {
        System.out.println("Draw Circle");
    }

    @Override
    public void drawSolidCircle(Vec2 center, float radius, Vec2 axis, Color3f color) {
        System.out.println("Draw Solid Circle");
    }

    @Override
    public void drawSegment(Vec2 p1, Vec2 p2, Color3f color) {
        System.out.println("Draw Segment");
    }

    @Override
    public void drawTransform(Transform xf) {
        System.out.println("Draw Transform");
    }

    @Override
    public void drawString(float x, float y, String s, Color3f color) {
        System.out.println("Draw String");
    }

    public void ensurePolyCapacity(int desiredCapacity) {
        if (desiredCapacity > vertexPolyCapacity) {
            vertexPolyCapacity *= 2;
            ByteBuffer newData = NativeMem.createByteBuffer(SIZE_VERTEX_POLY * vertexPolyCapacity);
            dataPolys.flip();
            newData.put(dataPolys);
            dataPolys = newData;
            updatePolys = true;
        }
    }
    public void ensureQuadCapacity(int desiredCapacity) {
        if (desiredCapacity > vertexQuadCapacity) {
            vertexQuadCapacity *= 2;
            ByteBuffer newData = NativeMem.createByteBuffer(SIZE_VERTEX_QUAD * vertexQuadCapacity);
            dataQuads.flip();
            newData.put(dataQuads);
            dataQuads = newData;
            updateQuads = true;
        }
    }


    /**
     * Pushes stored drawing data to the GPU
     */
    public void pushData() {
        // Send the polygon information to the GPU
        dataPolys.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vertexPolys);
        if (updatePolys) {
            // Resize required
            dataPolys.limit(SIZE_VERTEX_POLY * vertexPolyCapacity);
            glBufferData(GL_ARRAY_BUFFER, dataPolys, GL_DYNAMIC_DRAW);
            updatePolys = false;
        }
        else {
            // Push a subportion of the data
            glBufferSubData(GL_ARRAY_BUFFER, 0, dataPolys);
        }

        // Send the quad information to the GPU
        dataQuads.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vertexQuads);
        if (updateQuads) {
            // Resize required
            dataQuads.limit(SIZE_VERTEX_QUAD * vertexQuadCapacity);
            glBufferData(GL_ARRAY_BUFFER, dataQuads, GL_DYNAMIC_DRAW);
            updateQuads = false;
        }
        else {
            // Push a subportion of the data
            glBufferSubData(GL_ARRAY_BUFFER, 0, dataQuads);
        }

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

}
