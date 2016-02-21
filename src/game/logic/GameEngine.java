package game.logic;

import com.sun.java.accessibility.util.TopLevelWindowListener;
import game.LevelLoadArgs;
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



    public static void loadState(GameState state, LevelLoadArgs loadArgs) {
        // TODO: Load everything but the physics

        // Create the physics world
        PhysicsController.initState(state);

        // TODO: Finish up loading and binding references here
    }
}
