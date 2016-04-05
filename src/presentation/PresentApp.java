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
            new ImageSlideScreen("data/slides/wow_0.jpg"),
            new ImageSlideScreen("data/slides/wow_1.jpg"),
            new ImageSlideScreen("data/slides/wow_2.jpg"),
            new ImageSlideScreen("data/slides/wow_3.jpg"),
            new ImageSlideScreen("data/slides/gw2_0.jpg"),
            new ImageSlideScreen("data/slides/gw2_1.jpg"),
            new ImageSlideScreen("data/slides/gw2_2.jpg"),
            new ImageSlideScreen("data/slides/wow_4.jpg"),
            new ImageSlideScreen("data/slides/wow_5.jpg"),
            new ImageSlideScreen("data/slides/wow_6.png"),
            new ImageSlideScreen("data/slides/gw2_3.jpg"),
            new ImageSlideScreen("data/slides/gw2_5.jpg"),
            new ImageSlideScreen("data/slides/gw2_4.jpg"),
            new ImageSlideScreen("data/slides/wow.jpg"),
            new ImageSlideScreen("data/slides/gw2.jpg"),
            new LightningScreen("data/LightningImage.png"),
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
