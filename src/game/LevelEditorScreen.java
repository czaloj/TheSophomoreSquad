package game;

import blister.GameScreen;
import blister.GameTime;
import blister.MainGame;

public class LevelEditorScreen extends GameScreen {
    /**
     * This gameplay screen will be used to test levels
     */
    private GameplayScreen gameplayScreen = null;

    private boolean isTestingLevel;

    @Override
    public int getNext() {
        return isTestingLevel ? getIndex() : 0;
    }
    @Override
    protected void setNext(int next) {

    }

    @Override
    public int getPrevious() {
        return isTestingLevel ? getIndex() : 0;
    }
    @Override
    protected void setPrevious(int previous) {

    }

    @Override
    public void setParentGame(MainGame pgame, int index) {
        super.setParentGame(pgame, index);
        gameplayScreen.setParentGame(pgame, index);
    }

    @Override
    public void build() {

    }
    @Override
    public void destroy(GameTime gameTime) {
        gameplayScreen.build();
        gameplayScreen.destroy(gameTime);
    }

    @Override
    public void onEntry(GameTime gameTime) {

    }
    @Override
    public void onExit(GameTime gameTime) {

    }

    @Override
    public void update(GameTime gameTime) {
        if (isTestingLevel) {
            // Check to make sure we have an active screen
            if (gameplayScreen == null) {
                gameplayScreen = new GameplayScreen();
                gameplayScreen.setParentGame(game, 0);
                gameplayScreen.build();
                // TODO: Modify GlobalState.instance to contain the current level here
                gameplayScreen.onEntry(gameTime);
            }

            // Simulate the game going on right now
            gameplayScreen.update(gameTime);
            return;
        }

        // Destroy a screen if we find one
        if (gameplayScreen != null) {
            gameplayScreen.onExit(gameTime);
            gameplayScreen.destroy(gameTime);
            gameplayScreen = null;
        }

        // TODO: Custom level editor control
    }
    @Override
    public void draw(GameTime gameTime) {
        // Draw the gameplay if currently testing
        if (isTestingLevel && gameplayScreen != null) {
            gameplayScreen.draw(gameTime);
            return;
        }

        // TODO: Custom level editor drawing
    }
}
