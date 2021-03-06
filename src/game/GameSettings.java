package game;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Array;
import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * \brief A struct of various application settings
 */
public class GameSettings {
    public static ArrayList<DisplayMode> availableDisplayModes = new ArrayList<>();

    static {
        // Grab a list of display modes sorted by size
        DisplayMode[] modes = null;
        try {
            modes = Display.getAvailableDisplayModes();
        }
        catch (Exception e) {
            System.err.println("Error in getting display modes");
            System.err.println(e.getMessage());
        }
        Arrays.sort(modes, (DisplayMode o1, DisplayMode o2) -> {
            int cmp = Integer.compare(o1.getWidth(), o2.getWidth());
            if (cmp != 0) return cmp;
            return Integer.compare(o1.getHeight(), o2.getHeight());
        });

        // Add all possible unique display modes that match bpp and refresh rate of the user desktop mode
        DisplayMode desktopMode = Display.getDesktopDisplayMode();
        DisplayMode lastMode = null;
        for (DisplayMode mode : modes) {
            // TODO: We only care about fullscreen modes?
            if (!mode.isFullscreenCapable()) continue;

            // TODO: Should we limit this way?
            if (mode.getFrequency() != desktopMode.getFrequency() || mode.getBitsPerPixel() != desktopMode.getBitsPerPixel()) continue;

            // Make sure the mode is unique in size
            if (lastMode != null) {
                if (lastMode.getWidth() == mode.getWidth() && lastMode.getHeight() == mode.getHeight() && lastMode.getFrequency() == mode.getFrequency()) continue;
            }

            // Add unique mode to the list
            availableDisplayModes.add(mode);
            lastMode = mode;
        }
    }

    /**
     * Various options presets for game quality
     */
    public static enum Preset {
        DEFAULT,
        LOW,
        MEDIUM,
        HIGH,
        MAMMOTH
    }

    /**
     * The single instance of game settings
     */
    public static final GameSettings global = new GameSettings();

    /**
     * A number between 0 - 2 (a higher number is better quality)
     */
    public int lightingQuality;

    /**
     * Damage quality where 1 is pixel-accurate and higher numbers mean less pixel accuracy (1 - 12)
     */
    public int damageQuality;

    /**
     * Resolution of the game (width and height)
     */
    public int resolutionWidth;
    public int resolutionHeight;

    /**
     * If true, use OpenGL version 4.3 and GLSL version 4.3
     * Otherwise, use OpenGL 3.2 and GLSL version 1.5
     */
    public boolean useModernOpenGL;

    /**
     * Capabilities of the graphics card
     */
    public int maxTextureSize;

    /**
     * Physics settings
     */
    public int physicsVelocityIterations = 4; ///< Should be a number between 4 and 16
    public int physicsPositionIterations = 4; ///< Should be a number between 4 and 16

    private GameSettings() {
        // Figure out certain modes
        DisplayMode highestMode = availableDisplayModes.get(availableDisplayModes.size() - 1);
        resolutionWidth = highestMode.getWidth();
        resolutionHeight = highestMode.getHeight();

        setToPreset(Preset.DEFAULT);
        loadFromFile();
    }

    private void loadFromFile() {
        // TODO: Load settings from file if it exists
    }
    public void setToPreset(Preset p) {
        // TODO: Display modes should only be enumerated once




        // Fill out other parts for the presets
        switch (p) {
            case DEFAULT:
                lightingQuality = 1;
                damageQuality = 1;
                useModernOpenGL = true;
                physicsVelocityIterations = 4;
                physicsPositionIterations = 4;
                break;
            case LOW:
                // TODO: Finish settings to special preset
                break;
            case MEDIUM:
                // TODO: Finish settings to special preset
                break;
            case HIGH:
                // TODO: Finish settings to special preset
                break;
            case MAMMOTH:
                // TODO: Finish settings to special preset
                break;
        }
    }
    public void saveToFile() {
        // TODO: Save settings to a file
    }

    public void queryCapabilities() {
        maxTextureSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
    }

}
