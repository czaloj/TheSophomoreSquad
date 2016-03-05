package game.logic;

import egl.math.Vector2;
import game.GameSettings;
import game.data.Character;
import game.data.GameState;
import org.jbox2d.common.Vec2;
import org.lwjgl.Sys;

public class GameplayController {
    public static final float AXIS_DEAD_ZONE = 0.03f;
    public static final float VELOCITY_DEAD_ZONE = 0.1f;

    private GameState state;
    private GameSettings settings;
    private PhysicsController physicsController;

    public void init(GameState s) {
        if (state != null) {
            // TODO: State cleanup
        }

        // Setup references
        state = s;
        settings = GameSettings.global;

        if (state != null) {
            physicsController = new PhysicsController(s);
            // TODO: Add extra stuff
        }
    }

    public void update(float dt) {
        // TODO: Input and pre-physics logic
        float moveForce = state.player.movePower * state.player.input.moveDirection;
        updateInput(state.player, dt);

        // Integrate the physics world and collect all interactions
        state.player.isGrounded = false;
        physicsController.update(state, dt);

        // TODO: Interactions and cleanups
    }

    private void updateInput(Character c, float dt) {
        if (c.input.moveDirection > AXIS_DEAD_ZONE || c.input.moveDirection < -AXIS_DEAD_ZONE) {
            // Move the player
            Vec2 force = new Vec2(c.movePower * c.input.moveDirection, 0.0f);
            if (!c.isGrounded) force.x *= c.airMovementRatio;

            // If the character is moving in an opposite direction, then apply stopping
            if (Math.signum(c.body.getLinearVelocity().x) != Math.signum(c.input.moveDirection) && Math.abs(c.body.getLinearVelocity().x) > VELOCITY_DEAD_ZONE) {
                force.x -= (c.isGrounded ? 1.0f : c.airMovementRatio) * c.body.getLinearVelocity().x * c.stoppingPower;
            }

            c.body.applyForce(force, new Vec2());
        }
        else {
            // Apply a stopping force
            if (c.body.getLinearVelocity().x < VELOCITY_DEAD_ZONE && c.body.getLinearVelocity().x > -VELOCITY_DEAD_ZONE) {
                // Stop moving completely if on the ground
                if (c.isGrounded) {
                    c.body.setLinearVelocity(new Vec2(0.0f, c.body.getLinearVelocity().y));
                }
            }
            else {
                // Apply the stopping force
                Vec2 force = new Vec2(-c.body.getLinearVelocity().x * c.stoppingPower, 0.0f);
                if (!c.isGrounded) force.x *= c.airMovementRatio;
                c.body.applyForce(force, new Vec2());
            }
        }


        Vec2 impulse = new Vec2(0.0f, 0.0f);
        if (c.input.jump && c.isGrounded) {
            impulse.y = c.jumpPower * dt;
            c.body.applyLinearImpulse(impulse, new Vec2());
        }
    }
}
