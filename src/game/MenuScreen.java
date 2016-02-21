package game;

import blister.GameScreen;
import blister.GameTime;
import blister.ScreenState;
import game.data.LevelInformation;
import game.logic.GameEngine;
import game.data.GameState;

import java.util.ArrayList;

public class MenuScreen extends GameScreen {
    private GameScreen gameplayScreen;
    private LevelLoadArgs loadArgs;
    private final ArrayList<LevelInformation> levels = new ArrayList<>();

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
        // Find all the levels at the beginning
        GameEngine.findAllLevelData(levels);

        // Loading arguments will persist until the game finishes
        loadArgs = new LevelLoadArgs();
        loadArgs.level = levels.get(0);
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
        GameEngine.loadState(GlobalState.instance.state, loadArgs);
        setState(ScreenState.ChangeNext);
    }
    @Override
    public void draw(GameTime gameTime) {
        
    }
}
