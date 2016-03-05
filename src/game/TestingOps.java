package game;

import egl.math.Vector4;
import game.data.LevelInformation;

public class TestingOps {
    /**
     * Fill out a simple level
     * @param levelInfo Reference to level information
     */
    public static void createLevel(LevelInformation levelInfo) {
        levelInfo.fileName = "NoFile";
        levelInfo.name = "Test Level";

        // Floor
        levelInfo.levelGeometry.add(new Vector4(0, 1, 50, 1));
        // Roof
        levelInfo.levelGeometry.add(new Vector4(0, 49, 50, 1));
        // Right side
        levelInfo.levelGeometry.add(new Vector4(49, 25, 1, 23));
        // Left side
        levelInfo.levelGeometry.add(new Vector4(-49, 25, 1, 23));

        // Interesting thing
        levelInfo.levelGeometry.add(new Vector4(1, 2.4f, 0.25f, 0.4f));

        // Spawning point of the character
        levelInfo.spawnPoint.set(-1, 5);
    }
}
