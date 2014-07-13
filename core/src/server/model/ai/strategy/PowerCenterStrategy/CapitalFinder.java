package server.model.ai.strategy.PowerCenterStrategy;

import java.util.HashSet;
import server.model.playerData.Region;

public interface CapitalFinder {
	public void findPowerCenter(HashSet<Region> myRegions,Region test);
}
