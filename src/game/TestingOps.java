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
        levelInfo.levelGeometry.add(new Vector4(0, 1, 50, 2));
        // Roof
        levelInfo.levelGeometry.add(new Vector4(0, 49, 50, 2));
        // Right side
        levelInfo.levelGeometry.add(new Vector4(49, 25, 2, 23));
        // Left side
        levelInfo.levelGeometry.add(new Vector4(-49, 25, 2, 23));
    }
}
