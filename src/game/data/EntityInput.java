package game.data;

public class EntityInput {
    /**
     * Number [-1,1] designating intended moving direction of the entity
     */
    public float moveDirection;
    /**
     * Press state of the jump action
     */
    public boolean jump;

    /**
     * Press state of the melee action
     */
    public boolean melee = false;
    /**
     * Press state of the shoot action
     */
    public boolean shoot = false;
    /**
     * Press state of the grapple action
     */
    public boolean grapple = false;
    /**
     * Press state of the special action
     */
    public boolean special = false;
}
