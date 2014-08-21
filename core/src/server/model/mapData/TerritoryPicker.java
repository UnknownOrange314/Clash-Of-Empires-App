package server.model.mapData;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import server.model.playerData.Player;
import server.model.UpgradeDefinition;
import server.model.playerData.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
/**
 *This class sets up a balanced starting position that is randomized at the same time.
 */
class TerritoryPicker{
  

	
    public static void pickOwners(ArrayList<Region> regions, ArrayList<Player> players,GameOption myOptions){       
    
      final Player neutral = players.get(players.size() - 1);
      for (Region c:regions) {
    	  c.setOwner(neutral);
      }   
      final double DIST_RAND=20.0;
      final double DIST_ZERO=99; //The score that is added when the distance is 0.
      
      TreeSet<TerritoryScore> score=new TreeSet<TerritoryScore>();
      for (Region r:regions){
          //We want to favor regions that are close to other regions, so we calculate the total distance to every other region.
    	  double distTot=0.0;
          for( Region r2:regions){
        	  distTot+=r.compareDistance(r2)*r.compareDistance(r2);
          }
          double distScore=(distTot+Math.random()*DIST_RAND)*(r.getMoveCost());
          score.add(new TerritoryScore(r, distScore));
       }

       //Iterate through all the other players and assign them a starting region.
       for(Player player:players){
    	   TerritoryScore max=score.pollLast();
    	   Region topRegion=max.reg;
           topRegion.setOwner(player);
           player.setCapital(topRegion);
           for (UpgradeDefinition u:UpgradeDefinition.upgradeList) {
        	   topRegion.addUpgrade(u);
           }
           
           TreeSet<TerritoryScore> temp=new TreeSet<TerritoryScore>();
           for(TerritoryScore tScore:score){
        	   Region other=tScore.reg;
               double distance=topRegion.compareDistance(other);
               if(distance<=1.0){
            	   temp.add(new TerritoryScore(tScore.reg,tScore.getScore()+DIST_ZERO));
               }
               else{
            	   temp.add(new TerritoryScore(tScore.reg,tScore.getScore()+DIST_ZERO/distance));
               }
            }
           score=temp;
        }

        int idx=0;
        FileHandle handle=Gdx.files.internal("mapData/defaultNames.txt");
        String[] capitals=Gdx.files.internal("mapData/capitalNames.txt").readString().split("\n");
        int capCount=0;
        
        for(String line :(handle.readString()).split("\n")){
            if(idx>=regions.size()){
                return;
            }
            Region region=regions.get(idx);
            if(region.isCapital()){
                region.setName(capitals[capCount]);
                capCount+=1;
            }
            else{
                regions.get(idx).setName(line);
            }
            idx+=1;
        }
        
        if (myOptions.neutralTroop()) {
        	for (Region c :regions) {
        		if (c.getOwner()== neutral) {
        			for(int x=0;x<GameMap.getNeutrals();x++ ){
        				neutral.buildTroop(c);
        			}
                }
            }
        }
    }
}