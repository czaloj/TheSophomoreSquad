package presentation;

import blister.GameTime;
import egl.*;
import egl.math.Color;
import egl.math.Matrix4;
import egl.math.Vector2;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

/**
 * \brief
 */
public class ImageSlideScreen extends SlideScreen {
    private static final Matrix4 identity = new Matrix4();
    private static SpriteBatch batch = null;
    private static int batchCount = 0;

    private final String image;
    private GLTexture texture;
    private final Vector2 origin = new Vector2();
    private final Vector2 size = new Vector2();

    public ImageSlideScreen(String image) {
        super();
        this.image = image;
    }

    @Override
    public void build() {
        super.build();

        // Create the global SpriteBatch
        batchCount++;
        if (batch == null) {
            batch = new SpriteBatch(false);
        }
    }
    @Override
    public void destroy(GameTime gameTime) {
        super.destroy(gameTime);

        // Dispose the global SpriteBatch (if no one is using it)
        batchCount--;
        if (batch != null && batchCount == 0) {
            batch.dispose();
            batch = null;
        }
    }

    @Override
    public void onEntry(GameTime gameTime) {
        super.onEntry(gameTime);

        // Load the image
        texture = new GLTexture(GL.TextureTarget.Texture2D);
        try {
            texture.setImage2D(image, false);
        } catch (Exception e) {
            System.err.println("Image " + image + " could not be loaded");
        }

        float aspectWindow = (float)game.getWidth() / (float)game.getHeight();
        float aspectImage = (float)texture.getWidth() / (float)texture.getHeight();
        // TODO: Finish computation

        // Draw only that image
        batch.begin();
        batch.draw(texture, origin, size, Color.White, 0.0f);
        batch.end(SpriteSortMode.None);

        // Default clearing state
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1.0);
    }
    @Override
    public void onExit(GameTime gameTime) {
        super.onExit(gameTime);

        // Dispose of GPU resources
        texture.dispose();
        texture = null;
    }

    @Override
    public void draw(GameTime gameTime) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        batch.renderBatch(identity, identity, BlendState.OPAQUE, SamplerState.LINEAR_CLAMP, DepthState.NONE, RasterizerState.CULL_NONE);
        super.draw(gameTime);
    }
}
