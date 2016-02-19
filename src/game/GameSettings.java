package game;

/**
 * \brief A struct of various application settings
 */
public class GameSettings {
    /**
     * The single instance of game settings
     */
    public static final GameSettings global = new GameSettings();

    /**
     * A number between 0 - 2 (a higher number is better quality)
     */
    public int lightingQuality = 0;

    /**
     * Resolution of the game (width and height)
     */
    public int resolutionWidth = 1280;
    public int resolutionHeight = 720;

    /**
     * Physics settings
     */
    public int physicsVelocityIterations = 4; ///< Should be a number between 4 and 16
    public int physicsPositionIterations = 4; ///< Should be a number between 4 and 16

    private GameSettings() {
        // TODO: Load settings from file
    }
}
