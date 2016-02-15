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
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL40.*;
import static org.lwjgl.opengl.GL41.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.opengl.GL44.*;

import egl.*;
import egl.math.Color;
import egl.math.Matrix4;
import egl.math.Vector2;
import ext.java.IOUtils;
import game.GameSettings;
import org.ietf.jgss.GSSManager;

import java.io.BufferedReader;
import java.nio.IntBuffer;

/**
 * \brief
 */
public class RenderPassLighting {
    private static final int QUALITY_LEVEL_FULL_RESOLUTION = 2;
    private static final String SHADER_FILE_LIGHTING = "game/graphics/shaders/LightingPostProcess.glsl";

    private GLTexture texLight = new GLTexture(GL.TextureTarget.Texture2D, false);
    private GLProgram progLighting = new GLProgram(false);
    private GLProgram progSimple = new GLProgram(false);

    SpriteBatch batch;

    public RenderPassLighting() {

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

        progSimple.quickCreateResource("Quick Render", "game/graphics/shaders/fullscreen.vert", "game/graphics/shaders/composite.frag", null);
        progSimple.use();
//        glUniform1i(progSimple.getUniform("unTexture"), 0);
        GLProgram.unuse();

        batch = new SpriteBatch(true);
        batch.begin();
        batch.draw(texLight, new Vector2(0, 0), new Vector2(100, 100), Color.White, 0.0f);
        batch.end(SpriteSortMode.None);
    }
    public void dispose() {
        texLight.dispose();
        progLighting.dispose();

        batch.dispose();
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
        texLight.setImage(width, height, GL_RGBA, GL_UNSIGNED_BYTE, null, false);
    }

    public void draw() {
        progLighting.use();
        glUniform1i(progLighting.getUniform("unTexture"), 0);
        glBindImageTexture(0, texLight.getID(), 0, false, 0, GL_READ_WRITE, GL_RGBA8);
        glDispatchCompute(16, 16, 1);
        glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT);
        glBindImageTexture(0, 0, 0, false, 0, GL_READ_WRITE, GL_RGBA8);

        progSimple.use();
        texLight.bindToUnit(GL_TEXTURE0);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        GLProgram.unuse();
        GLError.get("Drawing Batch");


//        batch.renderBatch(SpriteBatch.createCameraFromWindow(GameSettings.global.resolutionWidth, GameSettings.global.resolutionHeight), new Matrix4(), null, null, null, null);
//        GLError.get("Drawing Batch");
    }

}
