package starterpack.strategy;

import starterpack.game.CharacterClass;
import starterpack.game.GameState;
import starterpack.game.Item;
import starterpack.game.Position;

public class OnlyBuyAndUseShieldStrategy implements Strategy {


    public CharacterClass strategyInitialize(int myPlayerIndex) {
        return CharacterClass.KNIGHT;
    }

    public Position moveActionDecision(GameState gameState, int myPlayerIndex) {
        return new Position(0, 0);
    }

    public int attackActionDecision(GameState gameState, int myPlayerIndex) {

        return myPlayerIndex;
    }

    public Item buyActionDecision(GameState gameState, int myPlayerIndex) {
        return Item.SHIELD;
    }

    public boolean useActionDecision(GameState gameState, int myPlayerIndex) {
        return true;
    }
}
