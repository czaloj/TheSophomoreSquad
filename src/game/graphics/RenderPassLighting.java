package game.graphics;

// Import all OpenGL functions
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL43.*;

import blister.input.KeyboardEventDispatcher;
import blister.input.KeyboardKeyEventArgs;
import egl.*;
import ext.csharp.ACEventFunc;
import ext.java.IOUtils;
import game.GameSettings;
import org.lwjgl.input.Keyboard;

import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * \brief
 */
public class RenderPassLighting {
    private static final int QUALITY_LEVEL_FULL_RESOLUTION = 2;
    private static final String SHADER_FILE_LIGHTING = "game/graphics/shaders/GaussianBlur.glsl";

    private GLTexture texLight = new GLTexture(GL.TextureTarget.Texture2D, false);
    private GLTexture texLightBuffer = new GLTexture(GL.TextureTarget.Texture2D, false);
    private GLProgram progBlurVertical = new GLProgram(false);
    private GLProgram progBlurHorizontal = new GLProgram(false);
    private GLProgram progSimple = new GLProgram(false);
    private int dummyVAO;
    boolean drawn = true;

    public RenderPassLighting() {
        // Empty
    }

    public void init() {
        KeyboardEventDispatcher.OnKeyPressed.add(new ACEventFunc<KeyboardKeyEventArgs>() {
            @Override
            public void receive(Object sender, KeyboardKeyEventArgs args) {
                if(args.key == Keyboard.KEY_SPACE) drawn = false;
            }
        });

        // Create the target
        generateTexture();

        // Load the compute shaders
        BufferedReader reader = IOUtils.openReaderResource(SHADER_FILE_LIGHTING);
        String src = IOUtils.readFull(reader);
        int shader = glCreateShader(GL_COMPUTE_SHADER);
        glShaderSource(shader, "#version 430\n" + src);
        glCompileShader(shader);
        progBlurHorizontal.quickCreateShaderCompute("Light Blur Horizontal", shader);
        shader = glCreateShader(GL_COMPUTE_SHADER);
        glShaderSource(shader, "#version 430\n#define VERTICAL\n" + src);
        glCompileShader(shader);
        progBlurVertical.quickCreateShaderCompute("Light Blur Vertical", shader);

        // Set uniforms
        FloatBuffer fbWeights = NativeMem.createFloatBuffer(8);
        fbWeights.put(new float[] { 0.5f, 0.2f, 0.05f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f });
        fbWeights.flip();

        progBlurHorizontal.use();
        glUniform1i(progBlurHorizontal.getUniform("unTexture"), 0);
        glUniform1i(progBlurHorizontal.getUniform("unTextureDest"), 1);
        glUniform1i(progBlurHorizontal.getUniform("unBlurDistance"), 2);
        glUniform1(progBlurHorizontal.getUniformArray("unBlurWeights"), fbWeights);
        fbWeights.rewind();
        progBlurVertical.use();
        glUniform1i(progBlurVertical.getUniform("unTexture"), 1);
        glUniform1i(progBlurVertical.getUniform("unTextureDest"), 0);
        glUniform1i(progBlurVertical.getUniform("unBlurDistance"), 2);
        glUniform1(progBlurVertical.getUniformArray("unBlurWeights"), fbWeights);
        GLProgram.unuse();

        // Visibility testing
        progSimple.quickCreateResource("Quick Render", "game/graphics/shaders/fullscreen.glsl", "game/graphics/shaders/composite.glsl", null);
        progSimple.use();
        glUniform1i(progSimple.getUniform("unTexture"), 0);
        GLProgram.unuse();

        dummyVAO = glGenVertexArrays();
    }
    public void dispose() {
        texLight.dispose();
        progBlurHorizontal.dispose();
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
        texLight.internalFormat = GL_RGBA16F;
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
            bb.put((byte)~0);
        }
        bb.flip();
        System.out.println("Count: " + count);
        texLight.setImage(width, height, GL_RGBA, GL_UNSIGNED_BYTE, bb, false);

        texLightBuffer.internalFormat = GL_RGBA16F;
        texLightBuffer.init();
        texLightBuffer.setImage(width, height, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null, false);
    }

    public void draw() {
        if (!drawn) {
            drawn = true;
            glBindImageTexture(0, texLight.getID(), 0, false, 0, GL_READ_WRITE, GL_RGBA16F);
            glBindImageTexture(1, texLightBuffer.getID(), 0, false, 0, GL_READ_WRITE, GL_RGBA16F);

            progBlurHorizontal.use();
            glDispatchCompute(texLight.getWidth() / 16, texLight.getHeight() / 16, 1);
            glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT);
            progBlurVertical.use();
            glDispatchCompute(texLight.getWidth() / 16, texLight.getHeight() / 16, 1);
            glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT);
            glBindImageTexture(0, 0, 0, false, 0, GL_READ_WRITE, GL_RGBA16F);
            glBindImageTexture(1, 0, 0, false, 0, GL_READ_WRITE, GL_RGBA16F);
        }

        progSimple.use();
        texLight.bindToUnit(GL_TEXTURE0);
        glBindVertexArray(dummyVAO);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        GLProgram.unuse();
    }
}
