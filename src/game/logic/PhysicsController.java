package game.logic;

import egl.math.Vector2;
import egl.math.Vector4;
import game.GameSettings;
import game.LevelLoadArgs;
import game.data.*;
import game.data.Character;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * Handles the physics of the game
 */
public class PhysicsController implements ContactListener {
    public static final float DEFAULT_GRAVITY = -10;

    public static void initState(GameState state, LevelLoadArgs args) {
        // Create the world with a default gravity
        state.physicsWorld = new World(new Vec2(0, DEFAULT_GRAVITY));

        // Create the body that houses the level geometry
        BodyDef obstacleBodyDef = new BodyDef();
        obstacleBodyDef.type = BodyType.STATIC;
        obstacleBodyDef.fixedRotation = true;
        obstacleBodyDef.position.set(0.0f, 0.0f);
        obstacleBodyDef.angle = 0.0f;
        // TODO: Fill out special body userdata
        Body obstacleBody = state.physicsWorld.createBody(obstacleBodyDef);

        // Add all the collision obstacles
        FixtureDef obstacleFixtureDef = new FixtureDef();
        obstacleFixtureDef.density = 0.0f;
        obstacleFixtureDef.friction = 1.0f;
        obstacleFixtureDef.restitution = 0.0f;
        for (Vector4 rect : args.level.levelGeometry) {
            // Create the shape of the obstacle
            PolygonShape s = new PolygonShape();
            s.setAsBox(rect.z, rect.w, new Vec2(rect.x, rect.y), 0.0f);
            obstacleFixtureDef.shape = s;
            // TODO: Fill out special joint userdata and filter

            // Body now has its fixture
            obstacleBody.createFixture(obstacleFixtureDef);
        }

        // TODO: Add entities
        // Create the player
        addEntity(state.physicsWorld, args.playerCharacter, args.level.spawnPoint, state.player);
    }

    public static void addEntity(World world, CharacterInformation character, Vector2 spawn, Character outCharacter) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(spawn.x, spawn.y);
        bodyDef.angle = 0.0f;
        PhysicsDataBody dataBody = new PhysicsDataBody();
        // TODO: Fill out special body userdata
        bodyDef.userData = dataBody;
        outCharacter.body = world.createBody(bodyDef);

        // Create the movement shape of the character
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = character.density;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;
        PolygonShape s = new PolygonShape();
        s.setAsBox(character.size.x * 0.5f - character.roundness, character.size.y * 0.5f - character.roundness, new Vec2(0.0f, 0.0f), 0.0f);
        s.setRadius(character.roundness);
        fixtureDef.shape = s;
        PhysicsDataFixture dataFixture = new PhysicsDataFixture();
        // TODO: Fill out special joint userdata and filter
        fixtureDef.userData = dataFixture;

        // Body now has its fixture
        outCharacter.body.createFixture(fixtureDef);
    }

    public PhysicsController(GameState state) {
        // Add self to the contact listener
        state.physicsWorld.setContactListener(this);
    }

    public void update(GameState state, float dt) {
        // TODO: Perform analyses?

        // Integrate the physics world
        state.physicsWorld.step(dt, GameSettings.global.physicsVelocityIterations, GameSettings.global.physicsPositionIterations);

        // TODO: Parse interactions
    }

    @Override
    public void beginContact(Contact contact) {
        // TODO: Implement
    }

    @Override
    public void endContact(Contact contact) {
        // TODO: Implement
        PhysicsDataFixture dataA = (PhysicsDataFixture)contact.getFixtureA().getUserData();
        PhysicsDataFixture dataB = (PhysicsDataFixture)contact.getFixtureB().getUserData();
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO: Implement
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO: Implement
    }
}
