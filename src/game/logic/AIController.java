package game.logic;

import game.data.Character;
import game.data.GameState;

/**
 * \brief
 */
public class AIController {
    public GameState state;

    public void init(GameState s) {
        state = s;
    }
    public void dispose() {

    }

    public void update(float dt) {
        for (Character c : state.characters) {
            if (c.info.isPlayer) continue;

            if (c.body.getPosition().x < state.player.body.getPosition().x) {
                c.input.moveDirection = 1.0f;
            }
            else {
                c.input.moveDirection = -1.0f;
            }
        }
    }

}
