package game.graphics;

// Import all OpenGL functions
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

import egl.NativeMem;

import java.nio.IntBuffer;


/**
 * \brief
 */
public class Renderer {
    private int fboRender = 0;
    private int fboLighting = 0;

    private final RenderPassLighting passLighting = new RenderPassLighting();

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
        passLighting.draw();
    }
}
