package cs6310.BattleState;

import java.util.Random;

public class WeakenedBattleState implements IBattleState {
    // 50% chance of attack means 5,6,7,8,9 is attack
    public static final int ATTACK_IF_GREATER_THAN_OR_EQUAL_TO = 5;
    public static final int MAX_ROLL_EXCLUSIVE = 10;

    @Override
    public boolean shouldAttack(Random rng) {
        int roll = rng.nextInt(MAX_ROLL_EXCLUSIVE);
        if (roll >= ATTACK_IF_GREATER_THAN_OR_EQUAL_TO) {
            return true;
        }
        return false;
    }
}
