package starterpack.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Represents the entire state of a Player. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerState {
  @JsonProperty("class")
  private CharacterClass characterClass;

  @JsonProperty("item")
  private Item item = Item.NONE;

  @JsonProperty("position")
  private Position position;

  @JsonProperty("gold")
  private int gold;

  @JsonProperty("score")
  private int score;
  @JsonProperty("health")
  private int health;

  @JsonCreator
  public PlayerState(
          @JsonProperty("class") CharacterClass characterClass,
          @JsonProperty("position") Position position) {
    this.characterClass = characterClass;
    this.position = position;
  }

  public Item getItem() {
    return this.item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public CharacterClass getCharacterClass() {
    return this.characterClass;
  }

  public void setCharacterClass(CharacterClass characterClass) {
    this.characterClass = characterClass;
  }

  public int getGold() {
    return gold;
  }

  public void setGold(int amount) {
    gold = amount;
  }

  @JsonProperty("stat_set")
  private StatSet statSet;

  public StatSet getStatSet() {
    return statSet;
  }

  public void setStatSet(StatSet statSet) {
    this.statSet = statSet;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

}
