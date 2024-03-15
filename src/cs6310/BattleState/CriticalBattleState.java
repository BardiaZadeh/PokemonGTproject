package cs6310.BattleState;

import java.util.Random;

public class CriticalBattleState implements IBattleState {
    // 30% chance of attack means 7,8,9 is attack
    public static final int ATTACK_IF_GREATER_THAN_OR_EQUAL_TO = 7;
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
