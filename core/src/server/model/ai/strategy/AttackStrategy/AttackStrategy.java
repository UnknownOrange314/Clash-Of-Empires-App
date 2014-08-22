package server.model.ai.strategy.AttackStrategy;

import server.model.ai.EasyComputerPlayer;
import server.model.playerData.Region;
import java.util.HashSet;

public interface AttackStrategy {
    public void attackTargets(EasyComputerPlayer p,HashSet<Region> myTargets);
}
