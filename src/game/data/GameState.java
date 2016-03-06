package game.data;

import egl.math.Vector2;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;

public class GameState {
    /**
     * Contains all the physics information
     */
    public World physicsWorld;

    /**
     * The center point of where the camera is looking
     */
    public final Vector2 cameraCenter = new Vector2();
    /**
     * The half-size of the bounds that the camera views
     */
    public final Vector2 cameraHalfViewSize = new Vector2();

    /**
     * The main player
     */
    public Character player;
    /**
     * A list of all characters found in a level
     */
    public final ArrayList<Character> characters = new ArrayList<>();
}
