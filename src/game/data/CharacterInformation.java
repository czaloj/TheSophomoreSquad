package game.data;

import egl.math.Vector2;

/**
 * This information is used solely for spawning characters
 */
public class CharacterInformation {
    /**
     * The friendly name of the character
     */
    public String name;

    /**
     * The size of the character for movement purposes
     */
    public final Vector2 size = new Vector2();
    /**
     * The roundness of the edges for movement purposes
     */
    public float roundness;
    /**
     * Density of the character for mass
     */
    public float density;

    /**
     * Move power
     */
    public float movePower;
    /**
     * Jumping power
     */
    public float jumpPower;
}

