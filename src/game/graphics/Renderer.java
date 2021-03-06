package game.graphics;

// Import all OpenGL functions
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

import blister.input.KeyboardEventDispatcher;
import blister.input.KeyboardKeyEventArgs;
import egl.NativeMem;
import ext.csharp.ACEventFunc;
import org.lwjgl.input.Keyboard;

import java.nio.IntBuffer;


/**
 * \brief
 */
public class Renderer {
    private int fboRender = 0;
    private int fboLighting = 0;

    private final RenderPassLighting passLighting = new RenderPassLighting();

    private final RenderData data = new RenderData();

    public Renderer() {
        // Empty
    }

    public void init() {
        passLighting.init();
    }
    public void dispose() {
        passLighting.dispose();
    }

    private void createRenderTargets(int width, int height, int lightWidth, int lightHeight) {
        IntBuffer ib = NativeMem.createIntBuffer(2);

        // Dispose the framebuffers if they already exist
        if (fboRender != 0) {
            ib.put(fboRender);
            ib.put(fboLighting);
            ib.flip();
            glDeleteFramebuffers(ib);
        }

        // Generate the new framebuffer objects
        glGenBuffers(ib);
        fboRender = ib.get(0);
        fboLighting = ib.get(1);

        // Create them from the sizes
        glBindFramebuffer(GL_FRAMEBUFFER, fboRender);
    }

    public void draw() {
        // TODO: Draw the environment of the level

        // TODO: Draw entities

        passLighting.draw();
    }
}
