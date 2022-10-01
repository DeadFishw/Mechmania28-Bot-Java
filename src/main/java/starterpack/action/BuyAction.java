package starterpack.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import starterpack.game.Item;

public class BuyAction extends Action {

  private final Item item;

  @JsonCreator
  public BuyAction(@JsonProperty("executor") int executingPlayerIndex, @JsonProperty("item") Item item) {
    super(executingPlayerIndex);
    this.item = item;
  }

  public Item getItem() {
    return item;
  }

}
