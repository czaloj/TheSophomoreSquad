package game.data;

import egl.math.Vector4;

import java.util.ArrayList;

public class LevelInformation {
    /**
     * The file name relative to the level directory
     */
    public String fileName;

    /**
     * The friendly name of the level
     */
    public String name;

    /**
     * List of rectangles specifying colliding surfaces
     * (center x, center y, half width, half height)
     */
    public final ArrayList<Vector4> levelGeometry = new ArrayList<>();

    
}
