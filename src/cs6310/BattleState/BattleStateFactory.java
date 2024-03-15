package cs6310.BattleState;

public class BattleStateFactory {
    public static final double MIN_HEALTH_RATIO_WHEN_STRONG = 0.7;
    public static final double MIN_HEALTH_RATIO_WHEN_WEAKENED = 0.3;

    public static IBattleState getBattleState(int health, int maxHealth) {
        double healthRatio = getHealthRatio(health, maxHealth);
        return getBattleState(healthRatio);
    }
    
    private static double getHealthRatio(int health, int maxHealth) {
        return (double) health / maxHealth;
    }

    private static IBattleState getBattleState(double healthRatio) {
        if (healthRatio >= MIN_HEALTH_RATIO_WHEN_STRONG) {
            return new StrongBattleState();
        }
        if (healthRatio >= MIN_HEALTH_RATIO_WHEN_WEAKENED) {
            return new WeakenedBattleState();
        }
        return new CriticalBattleState();
    }
}
