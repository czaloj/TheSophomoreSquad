package game;

import blister.GameTime;
import egl.BlendState;
import egl.DepthState;
import egl.RasterizerState;
import egl.math.Vector2;
import game.graphics.Renderer;
import org.lwjgl.opengl.GL11;

/**
 * \brief
 */
public class GameplayScreen extends blister.GameScreen {
    private final Renderer renderer = new Renderer();

    @Override
    public int getNext() {
        return 0;
    }
    @Override
    protected void setNext(int next) {
        // Empty
    }

    @Override
    public int getPrevious() {
        return 0;
    }
    @Override
    protected void setPrevious(int previous) {
        // Empty
    }

    @Override
    public void build() {
        renderer.init();
    }
    @Override
    public void destroy(GameTime gameTime) {
        renderer.dispose();
    }

    @Override
    public void onEntry(GameTime gameTime) {
        // Empty
    }
    @Override
    public void onExit(GameTime gameTime) {
        // Empty
    }

    @Override
    public void update(GameTime gameTime) {
        // Empty
    }
    @Override
    public void draw(GameTime gameTime) {
        GL11.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        BlendState.OPAQUE.set();
        DepthState.NONE.set();
        RasterizerState.CULL_NONE.set();
        GL11.glViewport(0, 0, 1280, 720);

        renderer.draw();
    }
}
