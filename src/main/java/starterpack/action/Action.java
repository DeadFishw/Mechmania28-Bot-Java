package starterpack.action;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
//@JsonDeserialize(as = UseAction.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {
  @JsonProperty("executor")
  private final int executingPlayerIndex;

  @JsonCreator
  public Action(@JsonProperty("executor")int executingPlayerIndex) {
    this.executingPlayerIndex = executingPlayerIndex;
  }
  public int getExecutingPlayerIndex() {
    return executingPlayerIndex;
  }

}
