package presentation;

import blister.MainGame;
import blister.ScreenList;
import game.GameSettings;
import org.lwjgl.opengl.Display;

/**
 * Created by czalo_000 on 3/21/2016.
 */
public class PresentApp extends MainGame {
    public PresentApp() {
        super("Tech Art", 1920, 1080);
    }

    @Override
    protected void buildScreenList() {
        screenList = new ScreenList(this, 0,
            new ImageSlideScreen("data/slides/Hi.png"),
            new TileScreen()
            );
    }

    @Override
    protected void fullInitialize() {

    }

    @Override
    protected void fullLoad() {
        // We'll check out what we can find at the beginning of the OpenGL context
        GameSettings.global.queryCapabilities();

        try {
            // TODO: Enable this later
            Display.setDisplayMode(GameSettings.availableDisplayModes.get(GameSettings.availableDisplayModes.size() - 1));
            Display.setFullscreen(true);
        }
        catch (Exception e) {
            System.err.println("Can't change the display mode");
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        PresentApp app = new PresentApp();
        app.run();
        app.dispose();
    }
}
