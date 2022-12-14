package starterpack.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AttackAction extends Action {
    @JsonProperty("target")
    private int targetPlayerIndex;

    @JsonCreator
    public AttackAction(@JsonProperty("executor")int executingPlayerIndex, @JsonProperty("target")int targetPlayerIndex) {
        super(executingPlayerIndex);
        this.targetPlayerIndex = targetPlayerIndex;
    }

}
