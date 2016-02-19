package game;

import blister.ScreenList;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.PixelFormat;

/**
 * \brief
 */
public class App extends blister.MainGame {
    public GameplayScreen gameplayScreen;
    public MenuScreen menuScreen;

    public App(String title, int w, int h) {
        super(title, w, h, new ContextAttribs(4, 3), new PixelFormat());
    }

    @Override
    protected void buildScreenList() {
        gameplayScreen = new GameplayScreen();
        menuScreen = new MenuScreen();

        screenList = new ScreenList(this, 0,
            menuScreen,
            gameplayScreen,
            new TestScreen()
        );
        menuScreen.setScreenReferences(gameplayScreen);
    }

    @Override
    protected void fullInitialize() {

    }

    @Override
    protected void fullLoad() {

    }

    public static void main(String[] args) {
        App app = new App("Walker's Game", 1200, 800);
        app.run();
        app.dispose();
    }

}
