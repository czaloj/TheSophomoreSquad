package game;

import blister.GameScreen;
import blister.GameTime;
import blister.ScreenState;
import game.data.CharacterInformation;
import game.data.LevelInformation;
import game.logic.GameEngine;
import game.data.GameState;

import java.util.ArrayList;

public class MenuScreen extends GameScreen {
    // Screen references
    private GameScreen gameplayScreen;

    /**
     * Used to begin loading the level from this screen
     */
    private LevelLoadArgs loadArgs;

    /**
     * A list of all the levels known by the menu
     */
    private final ArrayList<LevelInformation> levels = new ArrayList<>();
    /**
     * A list of all the characters known by the menu
     */
    private final ArrayList<CharacterInformation> characters = new ArrayList<>();

    /**
     * Screen for which to transition to
     */
    private int nextScreen = -1;

    @Override
    public int getNext() {
        return  nextScreen;
    }
    @Override
    protected void setNext(int next) {
        // Empty
    }

    @Override
    public int getPrevious() {
        return getIndex() - 1;
    }
    @Override
    protected void setPrevious(int previous) {
        // Empty
    }

    public void setScreenReferences(GameScreen gameplay) {
        gameplayScreen = gameplay;

        // By default, we'll go to ourselves
        nextScreen = getIndex();
    }

    @Override
    public void build() {
        // Find all the levels at the beginning
        GameEngine.findAllLevelData(levels);

        // Also load all the characters
        GameEngine.loadAllCharacterData(characters);

        // Loading arguments will persist until the game finishes
        loadArgs = new LevelLoadArgs();
    }
    @Override
    public void destroy(GameTime gameTime) {
        // Empty
    }

    @Override
    public void onEntry(GameTime gameTime) {
        // Empty
    }
    @Override
    public void onExit(GameTime gameTime) {
        // Empty
    }

    @Override
    public void update(GameTime gameTime) {
        // TODO: Temp until UI performs this action
        loadArgs.level = levels.get(0);
        loadArgs.playerCharacter = characters.get(0);

        if (loadArgs.level != null) {
            // TODO: Move to a loading thread?

            GlobalState.instance.state = new GameState();
            GameEngine.loadState(GlobalState.instance.state, loadArgs);

            // After loading, we transition to the next screen on the list
            nextScreen = gameplayScreen.getIndex();
            setState(ScreenState.ChangeNext);
        }
    }
    @Override
    public void draw(GameTime gameTime) {
        // TODO: Draw UI
    }
}
