package game;

import blister.GameTime;
import egl.BlendState;
import egl.DepthState;
import egl.RasterizerState;
import game.graphics.DebugRenderer;
import game.graphics.Renderer;
import game.data.GameState;
import game.logic.AIController;
import game.logic.GameplayController;
import game.logic.PlayerInputController;
import org.lwjgl.opengl.GL11;

/**
 * \brief
 */
public class GameplayScreen extends blister.GameScreen {
    private final DebugRenderer debugRenderer = new DebugRenderer();
    private final Renderer renderer = new Renderer();
    private GameState state;
    private final GameplayController gameplayController = new GameplayController();

    private final PlayerInputController playerInputController = new PlayerInputController();
    private final AIController aiController = new AIController();


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
        debugRenderer.init();
        renderer.init();
    }
    @Override
    public void destroy(GameTime gameTime) {
        debugRenderer.dispose();
        renderer.dispose();
    }

    @Override
    public void onEntry(GameTime gameTime) {
        state = GlobalState.instance.state;
        gameplayController.init(state);
        debugRenderer.setState(state);

        // Initialize input controllers
        playerInputController.init();
        playerInputController.reset(state.player.input);
        aiController.init(state);
    }
    @Override
    public void onExit(GameTime gameTime) {
        // Destroy input controllers
        playerInputController.dispose();
        aiController.dispose();
    }

    @Override
    public void update(GameTime gameTime) {
        // TODO: Use fixed time step
        float dt = 1.0f / 60.0f;
        playerInputController.update(dt);
        gameplayController.update(dt);
    }
    @Override
    public void draw(GameTime gameTime) {
        // Clear to black color
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        if (GlobalState.instance.debugMode) {
            // Draw using the debug renderer
            debugRenderer.draw();
        }
        else {
            // Setup default rendering state
            BlendState.OPAQUE.set();
            DepthState.NONE.set();
            RasterizerState.CULL_NONE.set();

            // Draw using the full renderer
            renderer.draw();
        }
    }
}
