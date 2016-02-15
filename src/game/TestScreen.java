package game;

import blister.GameScreen;
import blister.GameTime;
import egl.*;
import org.lwjgl.opengl.GL11;

/**
 * \brief
 */
public class TestScreen extends GameScreen {
    private GLProgram program;

    @Override
    public int getNext() {
        return 0;
    }
    @Override
    protected void setNext(int next) {

    }

    @Override
    public int getPrevious() {
        return 0;
    }
    @Override
    protected void setPrevious(int previous) {

    }

    @Override
    public void build() {
        program = new GLProgram(true);
        program.quickCreateResource("TestDraw", "game/graphics/shaders/fullscreen.vert", "game/graphics/shaders/simpleout.glsl", null);

    }
    @Override
    public void destroy(GameTime gameTime) {

    }

    @Override
    public void onEntry(GameTime gameTime) {
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        DepthState.NONE.set();
        RasterizerState.CULL_NONE.set();
        BlendState.OPAQUE.set();
    }
    @Override
    public void onExit(GameTime gameTime) {

    }

    @Override
    public void update(GameTime gameTime) {

    }
    @Override
    public void draw(GameTime gameTime) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        program.use();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
        GLProgram.unuse();

        GLError.get("Drawing Error");
    }
}
