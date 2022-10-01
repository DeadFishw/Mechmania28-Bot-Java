package starterpack.strategy;

public class StrategyConfig {

    /**
     * Return the strategy that your bot should use.
     * @param playerIndex A player index that can be used if necessary.
     * @return A Strategy object.
     */
    public static Strategy getStrategy(int playerIndex) {
//        if (playerIndex == 0) return new OnlyBuyAndUseShieldStrategy();
//        else return new OnlyGoto00AndAttackStrategy();
        return new RandomStrategy();
    }
}
