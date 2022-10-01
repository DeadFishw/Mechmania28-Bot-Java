package starterpack.strategy;

import starterpack.Config;
import starterpack.game.*;
import starterpack.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewStrategy implements Strategy {
    Map<Position, Integer> dangerMap = new HashMap<>();
    GameState currentGameState;
    State currentState;
    int round = 0;
    Position currentPosition;
    final static int PLAYER_NUMBER = 4;
    @Override
    public CharacterClass strategyInitialize(int myPlayerIndex) {
        return CharacterClass.WIZARD;
    }

    @Override
    public boolean useActionDecision(GameState gameState, int myPlayerIndex) {
        updateDangerMap(gameState, myPlayerIndex);
        return false;
    }

    @Override
    public Position moveActionDecision(GameState gameState, int myPlayerIndex) {
        currentGameState = gameState;
        updateDangerMap(gameState, myPlayerIndex);
        updateState(gameState, myPlayerIndex);
        Position destiny = null;//destiny position that varies according to state
        return moveActionDecision(gameState.getPlayerStateByIndex(myPlayerIndex).getPosition(), destiny,
                gameState.getPlayerStateByIndex(myPlayerIndex).getStatSet().getSpeed(), myPlayerIndex);
    }

    @Override
    public int attackActionDecision(GameState gameState, int myPlayerIndex) {
        updateDangerMap(gameState, myPlayerIndex);
        return 0;
    }

    @Override
    public Item buyActionDecision(GameState gameState, int myPlayerIndex) {
        return null;
    }

    public void updateState(GameState gameState, int myPlayerIndex) {

    }

    /**
     * find the next place in map to go; avoid dangerous places; open for modification
     * to avoid dangerous locations.
     * @param currentPosition
     * @param destiny
     * @param speed
     * @param myPlayerIndex
     * @return position to go
     */
    private Position moveActionDecision(Position currentPosition, Position destiny, int speed, int myPlayerIndex) {
        //if can move to destiny
        if (Utility.manhattanDistance(currentPosition, destiny) <= speed ||
                destiny == Utility.spawnPoints.get(myPlayerIndex)) {
            return destiny;
        }
        int minDistance = 100;
        Position movePosition = destiny;
        for(int i = 0; i < Config.BOARD_SIZE; i++) {
            for(int j = 0; j < Config.BOARD_SIZE; j++) {
                Position position = new Position(i, j);
                if (dangerMap.get(position) > currentGameState.getPlayerStateByIndex(myPlayerIndex).getHealth()) {
                    continue;
                }
                if (speed >= Utility.manhattanDistance(currentPosition, position) &&
                        minDistance > Utility.manhattanDistance(destiny, position)) {
                    minDistance = Utility.manhattanDistance(destiny, position);
                    movePosition = position;
                }
            }
        }
        return movePosition;
    }

    private void updateDangerMap(GameState gameState, int myPlayerIndex) {
        dangerMap.clear();
        for(int playerIndex = 0; playerIndex < PLAYER_NUMBER; playerIndex++) {
            if (playerIndex == myPlayerIndex) {
                currentPosition = gameState.getPlayerStateByIndex(playerIndex).getPosition();
                continue;
            }
            PlayerState playerState = gameState.getPlayerStateByIndex(playerIndex);
            for(int i = 0; i < Config.BOARD_SIZE; i++) {
                for(int j = 0; j < Config.BOARD_SIZE; j++) {
                    Position position = new Position(i, j);
                    if (canAttack(playerState, position)) {
                        if (dangerMap.containsKey(position)) {
                            dangerMap.replace(position, dangerMap.get(position) + playerState.getStatSet().getDamage());
                        } else {
                            dangerMap.put(position, playerState.getStatSet().getDamage());
                        }
                    }
                }
            }
        }
    }

    private boolean canAttack(PlayerState playerState, Position position) {
        for(Position position1: getMovePosition(playerState)) {
            if (Utility.chebyshevDistance(position1, position) <= playerState.getStatSet().getRange()) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Position> getMovePosition(PlayerState playerState) {
        ArrayList<Position> positions = new ArrayList<>();
        for(int i = 0; i < Config.BOARD_SIZE; i++) {
            for(int j = 0; j < Config.BOARD_SIZE; j++) {
                if (Utility.manhattanDistance(playerState.getPosition(), new Position(1, j)) <= playerState.getStatSet().getSpeed()) {
                    positions.add(new Position(i, j));
                }
            }
        }
        return positions;
    }
}
