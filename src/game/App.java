package game;

import blister.ScreenList;

/**
 * \brief
 */
public class App extends blister.MainGame {
    public App(String title, int w, int h) {
        super(title, w, h);
    }

    @Override
    protected void buildScreenList() {
        screenList = new ScreenList(this, 0, new GameplayScreen());
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
