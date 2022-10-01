package starterpack.strategy;

import starterpack.Config;
import starterpack.game.CharacterClass;
import starterpack.game.GameState;
import starterpack.game.Item;
import starterpack.game.Position;
import starterpack.util.Utility;

import java.util.concurrent.ThreadLocalRandom;

public class RandomStrategy implements Strategy {


    public CharacterClass strategyInitialize(int myPlayerIndex) {

        return Utility.randomEnum(CharacterClass.class);
    }

    public Position moveActionDecision(GameState gameState, int myPlayerIndex) {
        while (true) {
            int randomX = ThreadLocalRandom.current().nextInt(0, Config.BOARD_SIZE + 1);
            int randomY = ThreadLocalRandom.current().nextInt(0, Config.BOARD_SIZE + 1);

            if (new Position(randomX, randomY) == Utility.spawnPoints.get(myPlayerIndex) ||
                    Utility.manhattanDistance(new Position(randomX, randomY),
                    gameState.getPlayerStateByIndex(myPlayerIndex).getPosition())
                    <= gameState.getPlayerStateByIndex(myPlayerIndex).getCharacterClass().getStatSet().getSpeed()) {
                return new Position(randomX, randomY);
            }
        }
    }

    public int attackActionDecision(GameState gameState, int myPlayerIndex) {
        int res = 0;
        for (int i = 0; i < 4; i++) {
            
            if (i != myPlayerIndex) {
                if (Utility.chebyshevDistance(
                        gameState.getPlayerStateByIndex(myPlayerIndex).getPosition(),
                        gameState.getPlayerStateByIndex(i).getPosition()) <=
                        gameState.getPlayerStateByIndex(myPlayerIndex).getCharacterClass().getStatSet().getRange()) {
                    return i;
                }
                res = i;
            }
        }
        return res;
    }

    public Item buyActionDecision(GameState gameState, int myPlayerIndex) {
        return Utility.randomEnum(Item.class);
    }

    public boolean useActionDecision(GameState gameState, int myPlayerIndex) {
        return Utility.randomInt(0, 100) % 2 == 0;
    }
}
