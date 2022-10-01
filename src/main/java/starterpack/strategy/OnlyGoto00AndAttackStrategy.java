package starterpack.strategy;

import starterpack.game.CharacterClass;
import starterpack.game.GameState;
import starterpack.game.Item;
import starterpack.game.Position;
import starterpack.util.Utility;

public class OnlyGoto00AndAttackStrategy implements Strategy {


    public Position moveActionDecision(GameState gameState, int myPlayerIndex) {

        if (myPlayerIndex == 1) {
            return new Position(0, 1);
        }

        if (myPlayerIndex == 3) {
            return new Position(1, 0);
        }

        if (myPlayerIndex == 2) {
            return new Position(1, 1);
        }
        return new Position(0, 0);
    }

    public int attackActionDecision(GameState gameState, int myPlayerIndex) {
        return 0;
    }

    public Item buyActionDecision(GameState gameState, int myPlayerIndex) {
        return Utility.randomEnum(Item.class);
    }

    /**
     * Before the game starts, pick a class for your bot to start with.
     *
     * @param myPlayerIndex
     * @return A game.CharacterClass Enum.
     */
    @Override
    public CharacterClass strategyInitialize(int myPlayerIndex) {
        if (myPlayerIndex == 0) {
            return CharacterClass.KNIGHT;
        }
        if (myPlayerIndex == 1 || myPlayerIndex == 3) {
            return CharacterClass.ARCHER;
        }
        return CharacterClass.WIZARD;
    }

    public boolean useActionDecision(GameState gameState, int myPlayerIndex) {
        return true;
    }
}
