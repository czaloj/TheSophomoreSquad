package game;

import blister.ScreenList;
import egl.GLDiagnostic;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;

import java.util.logging.Level;

/**
 * \brief
 */
public class App extends blister.MainGame {
    public GameplayScreen gameplayScreen;
    public MenuScreen menuScreen;
    public LevelEditorScreen editorScreen;

    public App(String title, int w, int h) {
        // TODO: (maybe never...) support other versions of OpenGL
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

    @Override
    public void exit() {
        // Settings should be attempted to be saved before the application exits
        GameSettings.global.saveToFile();

        // Truly exit the application
        super.exit();
    }


    public static void main(String[] args) {
        App app = new App("Walker's Game", 1200, 800);
        app.run();
        app.dispose();
    }

}
