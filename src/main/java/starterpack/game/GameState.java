package starterpack.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import starterpack.util.Utility;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameState {

  /** Holds the {@link PlayerState} of each of the 4 players in player order. */
  @JsonProperty("player_states")
  private final List<PlayerState> playerStateList = Arrays.asList(new PlayerState[4]);

  /** Holds the {@link Position} (spawn point) of each of the 4 players in player order CW. */


  @JsonProperty
  private int turn;

  public int getTurn() {
    return turn;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }

  public GameState() {
  }

  /**
   * Constructor that takes a list of character classes for each of the players respectively.
   *
   * @param playerClasses List of character classes.
   */
  public GameState(List<CharacterClass> playerClasses) {
    for (int i = 0; i < 4; i++) {
      playerStateList.set(i, new PlayerState(playerClasses.get(i), Utility.spawnPoints.get(i)));
    }
  }

  public PlayerState getPlayerStateByIndex(int index) {
    return playerStateList.get(index);
  }






}
