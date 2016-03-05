package game.data;

import org.jbox2d.dynamics.Body;

/**
 * This is a moving entity in the world (does not have to be the player)
 */
public class Character {
    /**
     * Physics information for the entity
     */
    public Body body;

    // Movement information
    public float movePower;
    public float jumpPower;
}
