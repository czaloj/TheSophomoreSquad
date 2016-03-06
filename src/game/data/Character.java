package game.data;

import org.jbox2d.dynamics.Body;

/**
 * This is a moving entity in the world (does not have to be the player)
 */
public class Character {
    /**
     * Reference to the original information used to spawn this character
     */
    public CharacterInformation info;

    /**
     * Physics information for the entity
     */
    public Body body;

    // Movement information
    public final EntityInput input = new EntityInput();
    public float movePower;
    public float jumpPower;
    public float stoppingPower;
    public float airMovementRatio;
    public boolean isGrounded;
}
