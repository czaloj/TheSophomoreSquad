package presentation;

import blister.GameTime;
import egl.*;
import egl.math.Color;
import egl.math.Matrix4;
import egl.math.Vector2;
import ext.java.IOUtils;
import org.lwjgl.opengl.GL11;
import presentation.lightning.LightningRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 *
 */
public class LightningScreen extends SlideScreen {
    private String lightningTextureFile;
    private GLTexture lightningTexture;
    private LightningRenderer lightningRenderer = new LightningRenderer();
    private SpriteBatch batch;
    private Matrix4 mIdentity = new Matrix4();

    public LightningScreen(String f) {
        super();
        lightningTextureFile = f;
    }

    @Override
    public void onEntry(GameTime gameTime) {
        super.onEntry(gameTime);

        lightningRenderer = new LightningRenderer();

        try {
            InputStream s = IOUtils.openFile(lightningTextureFile);
            if(s == null) throw new Exception("Could Not Open Image File: " + lightningTextureFile);
            BufferedImage image = ImageIO.read(s);

            lightningTexture = new GLTexture(GL.TextureTarget.Texture2D, true);
            lightningTexture.setImage2D("data/Lightning.png", false);

            lightningRenderer.init(image, lightningTexture);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        batch = new SpriteBatch(true);
    }
    @Override
    public void onExit(GameTime gameTime) {
        lightningTexture.dispose();
        lightningTexture = null;
        lightningRenderer.dispose();
        batch.dispose();
        batch = null;
        super.onExit(gameTime);
    }

    @Override
    public void update(GameTime gameTime) {
        super.update(gameTime);

        lightningRenderer.updateLightning((float)gameTime.elapsed);
        GL11.glViewport(0, 0, game.getWidth(), game.getHeight());
    }

    @Override
    public void draw(GameTime gameTime) {
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Draw the lightning
        Vector2 origin = new Vector2();
        Vector2 size = new Vector2();
        float aspectWindow = (float)game.getWidth() / (float)game.getHeight();
        float aspectImage = (float)lightningRenderer.getLightningTexture().getWidth() / (float)lightningRenderer.getLightningTexture().getHeight();
        if (aspectImage > aspectWindow) {
            origin.set(-1, -1 * aspectWindow / aspectImage);
            size.set(2, 2 * aspectWindow / aspectImage);
        }
        else {
            origin.set(-1 * aspectImage / aspectWindow, -1);
            size.set(2 * aspectImage / aspectWindow, 2);
        }
        batch.begin();
        batch.draw(lightningRenderer.getLightningTexture(), origin, size, Color.White, 0.0f);
        batch.end(SpriteSortMode.None);
        batch.renderBatch(mIdentity, mIdentity, BlendState.ADDITIVE, SamplerState.LINEAR_CLAMP, DepthState.NONE, RasterizerState.CULL_NONE);

        super.draw(gameTime);
    }
}
