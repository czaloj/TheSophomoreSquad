package game;

import blister.GameScreen;
import blister.GameTime;
import blister.ScreenState;
import game.logic.GameEngine;
import game.logic.GameState;

public class MenuScreen extends GameScreen {
    private GameScreen gameplayScreen;

    private int nextScreen = -1;

    @Override
    public int getNext() {
        return  nextScreen;
    }
    @Override
    protected void setNext(int next) {

    }

    @Override
    public int getPrevious() {
        return getIndex() - 1;
    }
    @Override
    protected void setPrevious(int previous) {

    }

    public void setScreenReferences(GameScreen gameplay) {
        gameplayScreen = gameplay;

        nextScreen = gameplayScreen.getIndex();
    }

    @Override
    public void build() {

    }
    @Override
    public void destroy(GameTime gameTime) {

    }

    @Override
    public void onEntry(GameTime gameTime) {

    }
    @Override
    public void onExit(GameTime gameTime) {

    }

    @Override
    public void update(GameTime gameTime) {
        GlobalState.instance.state = new GameState();
        GameEngine.loadState(GlobalState.instance.state);
        setState(ScreenState.ChangeNext);
    }
    @Override
    public void draw(GameTime gameTime) {

    }
}
