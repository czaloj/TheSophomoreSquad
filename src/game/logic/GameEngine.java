package game.logic;

import com.sun.java.accessibility.util.TopLevelWindowListener;
import egl.math.Vector2;
import game.GameSettings;
import game.GlobalState;
import game.LevelLoadArgs;
import game.TestingOps;
import game.data.Character;
import game.data.CharacterInformation;
import game.data.GameState;
import game.data.LevelInformation;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.logging.Level;

public class GameEngine {
    /**
     * Every file found here is a level
     */
    private static final String DIRECTORY_LEVELS = "data/levels";

    /**
     * Preload all GPU resources needed for rendering:
     * Tilesets, animations, textures, etc.
     */
    public static void loadGraphicResources() {
        // TODO: Implement
    }

    /**
     * Refresh the levels found in the levels directory
     * @param levels Output list
     */
    public static void findAllLevelData(ArrayList<LevelInformation> levels) {
        // TODO: Temp remove for testing only
        LevelInformation liTest = new LevelInformation();
        TestingOps.createLevel(liTest);
        levels.add(liTest);

        File dirLevels = new File(DIRECTORY_LEVELS);
        File[] files = dirLevels.listFiles();
        for (File file : files) {
            if (file.isDirectory()) continue;

            LevelInformation li = new LevelInformation();
            li.fileName = file.getName();

            // TODO: Load contents from the file

            levels.add(li);
        }
    }

    /**
     * Load all the characters
     * @param characters Output list
     */
    public static void loadAllCharacterData(ArrayList<CharacterInformation> characters) {
        // Add Walker McMillan White
        characters.add(new CharacterInformation() {{
            name = "Walker";
            isPlayer = true;
            size.set(0.5f, 0.8f);
            roundness = 0.1f;
            density = 1.0f;
            movePower = 1.0f;
            jumpPower = 30.0f;
            stoppingPower = 2.4f;
            airMovementRatio = 0.6f;
        }});

        characters.add(new CharacterInformation() {{
            name = "Minion";
            isPlayer = false;
            size.set(0.4f, 0.6f);
            roundness = 0.1f;
            density = 0.6f;
            movePower = 1.0f;
            jumpPower = 30.0f;
            stoppingPower = 1.4f;
            airMovementRatio = 0.2f;
        }});
    }


    public static void loadState(GameState state, LevelLoadArgs loadArgs) {
        // Create the player
        state.player = new Character();
        Spawner.initializeCharacter(state.player, loadArgs.playerCharacter);
        state.characters.add(state.player);

        // TODO: Temp test code
        Character minion = new Character();
        Spawner.initializeCharacter(minion, loadArgs.enemies.get(0));
        state.characters.add(minion);

        // TODO: Load everything but the physics

        // TODO: Check this part out
        state.cameraCenter.set(0, 3);
        state.cameraHalfViewSize.set(GameSettings.global.resolutionWidth, GameSettings.global.resolutionHeight).mul(0.01f);

        // Create the physics world
        PhysicsController.initState(state, loadArgs);

        // TODO: Finish up loading and binding references here
        PhysicsController.addEntity(state.physicsWorld, new Vector2(4, 6), minion);
    }
}
