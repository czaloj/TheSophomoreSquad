package presentation;

import blister.MainGame;
import blister.ScreenList;

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

    }

    public static void main(String[] args) {
        PresentApp app = new PresentApp();
        app.run();
        app.dispose();
    }
}
