package game;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.lang.reflect.Array;
import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.Comparator;

/**
 * \brief A struct of various application settings
 */
public class GameSettings {
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
     * Physics settings
     */
    public int physicsVelocityIterations = 4; ///< Should be a number between 4 and 16
    public int physicsPositionIterations = 4; ///< Should be a number between 4 and 16

    private GameSettings() {
        setToPreset(Preset.DEFAULT);
        loadFromFile();
    }

    private void loadFromFile() {
        // TODO: Load settings from file if it exists
    }
    public void setToPreset(Preset p) {
        DisplayMode[] modes = null;
        try {
            modes = Display.getAvailableDisplayModes();
        }
        catch (Exception e) {
            System.err.println("Error in getting display modes");
            System.err.println(e.getMessage());
        }
        Arrays.sort(modes, new Comparator<DisplayMode>() {
            @Override
            public int compare(DisplayMode o1, DisplayMode o2) {
                int cmp = Integer.compare(o1.getWidth(), o2.getWidth());
                if (cmp != 0) return cmp;
                return Integer.compare(o1.getHeight(), o2.getHeight());
            }
        });
        int availableCount = 0;
        DisplayMode availableModes[] = new DisplayMode[modes.length];
        for (DisplayMode mode : modes) {
            // TODO: We only care about fullscreen modes?
            if (!mode.isFullscreenCapable()) continue;

            // Make sure the mode is unique in size
            if (availableCount > 0) {
                DisplayMode lastMode = availableModes[availableCount - 1];
                if (lastMode.getWidth() == mode.getWidth() && lastMode.getHeight() == mode.getHeight() && lastMode.getFrequency() == mode.getFrequency()) continue;
            }

            // Add unique mode to the list
            availableModes[availableCount++] = mode;
        }

        // Figure out certain modes
        DisplayMode highestMode = availableModes[availableCount - 1];
        resolutionWidth = highestMode.getWidth();
        resolutionHeight = highestMode.getHeight();

        // Fill out other parts for the presets
        switch (p) {
            case DEFAULT:
                lightingQuality = 1;
                useModernOpenGL = true;
                physicsVelocityIterations = 4;
                physicsPositionIterations = 4;
            // TODO: Finish settings to a special preset

        }
    }
    public void saveToFile() {
        // TODO: Save settings to a file
    }
}
