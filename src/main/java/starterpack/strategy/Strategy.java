package starterpack.strategy;

import starterpack.game.GameState;
import starterpack.game.CharacterClass;
import starterpack.game.Item;
import starterpack.game.Position;

public interface Strategy {

    /**
     * Before the game starts, pick a class for your bot to start with.
     * @return A game.CharacterClass Enum.
     */
    CharacterClass strategyInitialize(int myPlayerIndex);

    /**
     * Each turn, decide if you should use the item you're holding. Do not try to use the
     * legendary Item.None!
     * @param gameState A provided GameState object, contains every piece of info on the game board.
     * @param myPlayerIndex You may find out which player on the board you are.
     * @return If you want to use your item
     */
    boolean useActionDecision(GameState gameState, int myPlayerIndex);

    /**
     * Each turn, pick a position on the board that you want to move towards. Be careful not to
     * fall out of the board!
     * @param gameState A provided GameState object, contains every piece of info on the game board.
     * @param myPlayerIndex You may find out which player on the board you are.
     * @return A game.Position object.
     */
    Position moveActionDecision(GameState gameState, int myPlayerIndex);

    /**
     * Each turn, pick a player you would like to attack. Feel free to be a pacifist and attack no
     * one but yourself.
     * @param gameState A provided GameState object, contains every piece of info on the game board.
     * @param myPlayerIndex You may find out which player on the board you are.
     * @return Your target's player index.
     */
    int attackActionDecision(GameState gameState, int myPlayerIndex);

    /**
     * Each turn, pick an item you want to buy. Return Item.None if you don't think you can 
     * afford anything.
     * @param gameState A provided GameState object, contains every piece of info on the game board.
     * @param myPlayerIndex You may find out which player on the board you are.
     * @return A game.Item object.
     */
    Item buyActionDecision(GameState gameState, int myPlayerIndex);



}
