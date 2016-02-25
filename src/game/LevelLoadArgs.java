package game;

import game.data.CharacterInformation;
import game.data.LevelInformation;

import java.util.logging.Level;

/**
 * \brief
 */
public class LevelLoadArgs {
    /**
     * The level used to play
     */
    public LevelInformation level;

    /**
     * The character used for playing on the level
     */
    public CharacterInformation character;

    // TODO: Add information about the character and other stage debuffs etc.
}
