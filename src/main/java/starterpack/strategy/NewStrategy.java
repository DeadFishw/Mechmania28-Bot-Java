package starterpack.strategy;

import starterpack.Config;
import starterpack.game.*;
import starterpack.util.Utility;

import java.util.*;

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

        //If can attack no one return
        if (getAttactableEnemies(gameState, myPlayerIndex) == null) {
            return 0;
        }

        //If can kill one then kill one
        List<Integer> killList = new ArrayList<>();
        for (int i: getAttactableEnemies(gameState, myPlayerIndex)) {
            PlayerState myPlayer = gameState.getPlayerStateByIndex(myPlayerIndex);
            PlayerState enemy = gameState.getPlayerStateByIndex(i);
            if (myPlayer.getStatSet().getDamage() > enemy.getHealth()) {
                killList.add(i);
            }
        }
        //if there are multiple killables kill the one that has most threat;
        if (findMostDangerousPlayer(killList, gameState, myPlayerIndex) != -1) {
            return findMostDangerousPlayer(killList, gameState, myPlayerIndex);
        }

        //If cannot kill attack the one on control tile
        List<Integer> onTileList = new ArrayList<>();
        for (int i: getAttactableEnemies(gameState, myPlayerIndex)) {
            PlayerState myPlayer = gameState.getPlayerStateByIndex(myPlayerIndex);
            PlayerState enemy = gameState.getPlayerStateByIndex(i);
            if (checkOnTile(gameState, i)) {
                onTileList.add(i);
            }
        }
        //If there are multiple on tile kill the one that has the most threat;
        if (findMostDangerousPlayer(onTileList, gameState, myPlayerIndex) != -1) {
            return findMostDangerousPlayer(onTileList, gameState, myPlayerIndex);
        }

        //If cannot kill one attack the one that can attack me;
        List<Integer> attackList = new ArrayList<>();
        for (int i: getAttactableEnemies(gameState, myPlayerIndex)) {
            PlayerState myPlayer = gameState.getPlayerStateByIndex(myPlayerIndex);
            PlayerState enemy = gameState.getPlayerStateByIndex(i);
            if (Utility.chebyshevDistance(enemy.getPosition(), myPlayer.getPosition()) > enemy.getStatSet().getRange()) {
                attackList.add(i);
            }
        }
        if (findMostDangerousPlayer(attackList, gameState, myPlayerIndex) != -1) {
            return findMostDangerousPlayer(attackList, gameState, myPlayerIndex);
        }

        //If cannot kill one attack the one with most damage;
        int result = 0;
        int damage = 0;
        for (int i: getAttactableEnemies(gameState, myPlayerIndex)) {
            PlayerState myPlayer = gameState.getPlayerStateByIndex(myPlayerIndex);
            PlayerState enemy = gameState.getPlayerStateByIndex(i);
            if (enemy.getStatSet().getDamage() > damage) {
                damage = enemy.getStatSet().getDamage();
                result = i;
            }
        }
        return result;
    }

    private int findMostDangerousPlayer(List<Integer> indexList, GameState gameState, int myPlayerIndex) {
        if (!indexList.isEmpty()) {
            int damage = 0;
            int indexToKill = indexList.get(0);
            for (int i: indexList) {
                PlayerState enemy = gameState.getPlayerStateByIndex(i);
                PlayerState myPlayer = gameState.getPlayerStateByIndex(myPlayerIndex);
                if (Utility.manhattanDistance(enemy.getPosition(), myPlayer.getPosition()) <= enemy.getStatSet().getRange()) {
                    if (damage < enemy.getStatSet().getDamage()) {
                        damage = enemy.getStatSet().getDamage();
                        indexToKill = i;
                    }
                }
            }
            return indexToKill;
        } else {
            return -1;
        }
    }

    private boolean checkOnTile(GameState gameState, int index){
        Position myPosition = gameState.getPlayerStateByIndex(index).getPosition();
        if(myPosition.getX()<=5 && myPosition.getX()>=4 && myPosition.getY()<=5 && myPosition.getY()>=4){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Item buyActionDecision(GameState gameState, int myPlayerIndex) {
        return null;
    }

    private ArrayList<Integer> getAttactableEnemies(GameState gameState, int myPlayerIndex) {
        List<Integer> ints = new ArrayList<>(Arrays.asList(new Integer[]{0, 1, 2, 3}));
        ints.remove(myPlayerIndex);
        for(Integer i: ints) {
            PlayerState myPlayer = gameState.getPlayerStateByIndex(myPlayerIndex);
            PlayerState enemy = gameState.getPlayerStateByIndex(i);
            if (Utility.chebyshevDistance(enemy.getPosition(), myPlayer.getPosition()) > myPlayer.getStatSet().getRange()) {
                ints.remove(i);
            }
        }
        return (ArrayList<Integer>) ints;
    }
    private void updateState(GameState gameState, int myPlayerIndex) {

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
