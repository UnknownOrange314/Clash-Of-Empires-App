package server.model.ai

import server.model.playerData.Player
import java.util.HashMap
import scala.collection.JavaConversions._
import server.model.playerData.Region

/**
 * This class contains heuristics for each player.
 * @author Bharat
 */
class PlayerHeuristic(val myPlayer:Player){
    
    val P_DECAY_RATE=0.99 //This is the rate at which power decays based on distance.
    val MAX_INFLUENCE_DIST=200 //Maximum region distance for calculations of power.
    val DIST_MUL=100 //The distance between regions is multiplied by this value.
    private val regionPower=new HashMap[Region,Double]//This is the power that a player has in a region based on nearby regions.
    private val troopPower=new HashMap[Region,Double]//This is the power that a player has in a region based on nearby troops.
    def getRegionPower(loc:Region)=regionPower.get(loc)//Get the result of the region power heuristic.
    def getTroopPower(loc:Region)=troopPower.get(loc)//Get the result of the troop power heuristic.

    /**
     * This method calculates the power that a player has over each region of the map.
     */
    def calculatePower(){
        for(region<-AiDirector.myRegions){
            findRegionPower(region)
            findTroopPower(region)
        }
    }

    /**
     * Calculates the influence that a players' zones have on a particular location.
     * @param loc Location to calculate influence for.
     */
    private def findRegionPower(loc:Region){
        
        regionPower.put(loc, 0.001)
        var score=0.0
        for(c<-AiDirector.myRegions){
            if(c.getOwner()==myPlayer){
                val distance=50*c.compareDistance(loc)
                if(distance<MAX_INFLUENCE_DIST){
                    score=score+Math.pow(P_DECAY_RATE,distance)
                }
            }
        }
        regionPower.put(loc, score)
    }

    /**
     * Calculates the influence that a players' troops have on a particular location.
     * @param loc Location to calculate influence for.
     */
    def findTroopPower(loc:Region){
        var pow=0.0
        for(r<-AiDirector.myRegions){
            val tCount=myPlayer.getTroopCount(r)
            pow=30
            pow+=30*tCount/(100*r.compareDistance(loc)+0.01)
        }
        troopPower.put(loc,pow);
    }
}
