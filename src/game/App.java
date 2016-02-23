package game;

import blister.ScreenList;
import egl.GLDiagnostic;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
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
        super(title, w, h,
            GameSettings.global.useModernOpenGL ? new ContextAttribs(4, 3) : new ContextAttribs(3, 2),
            new PixelFormat() // TODO: Do we need to check out the pixel format?
        );
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
        // We'll check out what we can find at the beginning of the OpenGL context
        GameSettings.global.queryCapabilities();

        try {
            Display.setDisplayMode(GameSettings.availableDisplayModes.get(GameSettings.availableDisplayModes.size() - 1));
            Display.setFullscreen(true);
        }
        catch (Exception e) {
            System.err.println("Can't change the display mode");
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void exit() {
        // Settings should be attempted to be saved before the application exits
        GameSettings.global.saveToFile();

        // Truly exit the application
        super.exit();
    }


    public static void main(String[] args) {
        App app = new App("Walker's Game", GameSettings.global.resolutionWidth, GameSettings.global.resolutionHeight);
        app.run();
        app.dispose();
    }

}
