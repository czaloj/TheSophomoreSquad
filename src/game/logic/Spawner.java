package game.logic;

import game.data.Character;
import game.data.CharacterInformation;

/**
 * Handles creation of entities
 */
public class Spawner {
    public static void initializeCharacter(Character c, CharacterInformation info) {
        c.jumpPower = info.jumpPower;
        c.movePower = info.movePower;
        c.stoppingPower = info.stoppingPower;
        c.airMovementRatio = info.airMovementRatio;
    }
}
