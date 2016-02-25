package game.logic;

import egl.math.Vector2;
import egl.math.Vector4;
import game.GameSettings;
import game.LevelLoadArgs;
import game.data.CharacterInformation;
import game.data.GameState;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.lwjgl.Sys;

/**
 * Handles the physics of the game
 */
public class PhysicsController {
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
        obstacleFixtureDef.restitution = 1.0f;
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
        addEntity(state.physicsWorld, args.character, args.level.spawnPoint);
    }

    public static void addEntity(World world, CharacterInformation character, Vector2 spawn) {
        // TODO: Figure it out
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(spawn.x, spawn.y);
        bodyDef.angle = 0.0f;
        // TODO: Fill out special body userdata
        Body body = world.createBody(bodyDef);

        // Create the shape of the obstacle

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 1.0f;
        PolygonShape s = new PolygonShape();
        s.setAsBox(character.size.x * 0.5f, character.size.y * 0.5f, new Vec2(0.0f, 0.0f), 0.0f);
        fixtureDef.shape = s;
        // TODO: Fill out special joint userdata and filter

        // Body now has its fixture
        body.createFixture(fixtureDef);
    }

    public void update(GameState state, float dt) {
        // TODO: Perform analyses?

        // Integrate the physics world
        state.physicsWorld.step(dt, GameSettings.global.physicsVelocityIterations, GameSettings.global.physicsPositionIterations);

        // TODO: Parse interactions

        // TODO: Temp remove
        for (Body b = state.physicsWorld.getBodyList(); b != null; b = b.getNext()) {
            if (b.getType() == BodyType.DYNAMIC) {
                System.out.println("Position Y: " + b.getPosition().y);
            }
        }
    }
}
