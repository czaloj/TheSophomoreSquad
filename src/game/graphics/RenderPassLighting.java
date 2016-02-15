package game.graphics;

// Import all OpenGL functions
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL43.*;

import egl.*;
import egl.math.Color;
import egl.math.Vector2;
import ext.java.IOUtils;
import game.GameSettings;
import jdk.nashorn.internal.objects.NativeObject;
import org.lwjgl.Sys;

import java.io.BufferedReader;
import java.nio.ByteBuffer;

/**
 * \brief
 */
public class RenderPassLighting {
    private static final int QUALITY_LEVEL_FULL_RESOLUTION = 2;
    private static final String SHADER_FILE_LIGHTING = "game/graphics/shaders/LightingPostProcess.glsl";

    private GLTexture texLight = new GLTexture(GL.TextureTarget.Texture2D, false);
    private GLProgram progLighting = new GLProgram(false);
    private GLProgram progSimple = new GLProgram(false);
    private int dummyVAO;

    public RenderPassLighting() {
        // Empty
    }

    public void init() {
        // Create the target
        generateTexture();

        // Load the compute shader
        BufferedReader reader = IOUtils.openReaderResource(SHADER_FILE_LIGHTING);
        String src = IOUtils.readFull(reader);
        int shader = glCreateShader(GL_COMPUTE_SHADER);
        glShaderSource(shader, src);
        glCompileShader(shader);
        progLighting.quickCreateShaderCompute("Lighting Post Process", shader);

        // Visibility testing
        progSimple.quickCreateResource("Quick Render", "game/graphics/shaders/fullscreen.glsl", "game/graphics/shaders/composite.glsl", null);
        progSimple.use();
        glUniform1i(progSimple.getUniform("unTexture"), 0);
        GLProgram.unuse();

        dummyVAO = glGenVertexArrays();
    }
    public void dispose() {
        texLight.dispose();
        progLighting.dispose();
    }

    private void generateTexture() {
        // Find the width and height of the light buffer texture
        int width = GameSettings.global.resolutionWidth;
        int height = GameSettings.global.resolutionHeight;
        // On lower qualities, reduce the size of lighting quality
        if (GameSettings.global.lightingQuality < QUALITY_LEVEL_FULL_RESOLUTION) {
            width >>= 1;
            height >>= 1;
        }

        // Build the texture
        texLight.init();
        ByteBuffer bb = NativeMem.createByteBuffer(4 * width * height);
        int count = 0;
        for (int i = 0; i < width * height; i++) {
            if (Math.random() > 0.8) {
                count++;
                bb.put((byte)~0);
            }
            else {
                bb.put((byte)0);
            }

            bb.put((byte)0);
            bb.put((byte)0);
            bb.put((byte)0);
        }
        bb.flip();
        System.out.println("Count: " + count);
        texLight.setImage(width, height, GL_RGBA, GL_UNSIGNED_BYTE, bb, false);
    }

    public void draw(boolean sim) {
        if (sim) {
            progLighting.use();
            glUniform1i(progLighting.getUniform("unTexture"), 0);
            glBindImageTexture(0, texLight.getID(), 0, false, 0, GL_READ_WRITE, GL_RGBA8);
            glDispatchCompute(texLight.getWidth() / 16, texLight.getHeight() / 16, 1);
            glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT);
            glBindImageTexture(0, 0, 0, false, 0, GL_READ_WRITE, GL_RGBA8);
        }

        progSimple.use();
        texLight.bindToUnit(GL_TEXTURE0);
        glBindVertexArray(dummyVAO);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        GLProgram.unuse();
    }
}
