package server.model.ai.strategy.FortifyStrategy;


import java.util.HashMap;
import server.model.ai.EasyComputerPlayer;
import server.model.ai.PotentialField;
import server.model.playerData.Army;
import server.model.playerData.Region;

public class SimpleFortifyStrategy implements ForitfyStrategy{

    /**
     *
     * @param regionTroops The army for each region.
     * @param regionScores The score for each region.
     * @param player The player that owns the troops and regions.
     * @return
     */
   public PotentialField fortifyTroops(HashMap<Region, Army> regionTroops, HashMap<Region, Double> regionScores, EasyComputerPlayer player){
       
	   HashMap<Region,Double> troopCounts=new HashMap<Region,Double>();
	   for(Region r: regionTroops.keySet()){
		   troopCounts.put(r, (double)regionTroops.get(r).size());
	   }
       PotentialField potentialField=PotentialField.getPotentialVectors(troopCounts,regionScores,player);
       player.clearRallyPoints();
       potentialField.activateEdges(player,regionTroops);
       return potentialField;
   }


}