package game;

import blister.GameTime;
import egl.math.Vector2;
import org.lwjgl.opengl.GL11;

/**
 * \brief
 */
public class GameplayScreen extends blister.GameScreen {
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
        // Empty
    }
    @Override
    public void destroy(GameTime gameTime) {
        // Empty
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
        GL11.glClearColor(1.0f, 1.0f, 0.0f, 0.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }
}
