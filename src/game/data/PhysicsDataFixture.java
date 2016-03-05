package game.data;

/**
 * This data is attached to all Box2D fixtures
 */
public class PhysicsDataFixture {
    public static final int OBJECT_TYPE_MAP = 0;
    public static final int OBJECT_TYPE_CHARACTER = 1;

    public int objectType;
    public Object object;
}
