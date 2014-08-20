package server.model.ai.strategy.FortifyStrategy;

import server.model.ai.EasyComputerPlayer;
import server.model.ai.PotentialField;
import server.model.playerData.Army;
import server.model.playerData.Region;
import java.util.HashMap;

public interface ForitfyStrategy{
	public PotentialField fortifyTroops(HashMap<Region,Army> regionTroops, HashMap<Region,Double> regionScores,EasyComputerPlayer p);
}