package server.model.mapData

import com.badlogic.gdx.Gdx;
import java.util
import server.model.playerData.{Player}
import collection.mutable.Map
import scala.collection.mutable
import scala.collection.JavaConversions._
import scala.collection.JavaConversions._
import server.model.UpgradeDefinition
import server.model.playerData.Region

/**
 *This class sets up a balanced starting position that is randomized at the same time.
 */
object TerritoryPicker{
    def pickOwners( regions:util.ArrayList[Region], players:util.ArrayList[Player],myOptions:GameOption){       
    
      val neutral: Player = players.get(players.size - 1)
      for (c <- regions) {
    	  c.setOwner(neutral)
      }   
      
      val score =new mutable.ListMap[Region,Double]() //Score the regions based on desirability. Regions with higher scores are less desirable.
      for (r <- regions){
          //We want to favor regions that are close to other regions, so we calculate the total distance to every other region.
    	  var distTot=0.0
          for( r2<-regions){
        	  if(r!=r){
        		  distTot+=r.compareDistance(r2)*r.compareDistance(r2)
              }
          }
          var distScore=(distTot+Math.random*20)*(r.getMoveCost());
          score.put(r, distScore)
       }

       //Iterate through all the other players and assign them a starting region.
       for(player<-players){        
    	   val topScore=score.toList.sortBy(_._2 ).head
           val topRegion=topScore._1
           topRegion.setOwner(player)
           player.setCapital(topRegion)
           for (u <- UpgradeDefinition.upgradeList) {
        	   topRegion.addUpgrade(u)
           }
           for(other<-regions){
        	   println(regions.size())
               val distance=topRegion.compareDistance(other)
               if(distance==0){
            	   score(other)+=99
               }
               else{
            	   score(other)+=40/distance
               }
            }
        }

        var idx=0
        val handle=Gdx.files.internal("mapData/defaultNames.txt")
        val capitals=Gdx.files.internal("mapData/capitalNames.txt").readString().split('\n')
        var capCount=0
        
        for(line <-(handle.readString()).split('\n')){
            if(idx>=regions.size()){
                return
            }
            val region=regions.get(idx)
            if(region.isCapital){
                region.setName(capitals(capCount))
                capCount+=1
            }
            else{
                regions.get(idx).setName(line)
            }
            idx+=1
        }
        
        if (myOptions.neutralTroop) {
        	for (c <- regions) {
        		if (c.getOwner eq neutral) {
        			for(x<-0 until GameMap.NEUTRAL_TROOPS ){
        				neutral.buildTroop(c)
        			}
                }
            }
        }
    }
}
