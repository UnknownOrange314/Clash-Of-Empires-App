package server.model.ai

import java.util.ArrayList
import java.util.HashMap
import server.model.playerData.Player
import scala.collection.JavaConversions._
import server.model.playerData.Region

/**
 * This class is responsible for controlling how the AI behaves.
 * @author Bharat
 *
 */
object AiDirector{
    
    var myPlayers:ArrayList[Player]=null
    var myRegions:ArrayList[Region]=null
    var heuristics:HashMap[Player,PlayerHeuristic]=null

    def init(p2:ArrayList[Player],r:ArrayList[Region]){
        myRegions=r
        myPlayers=p2
        heuristics=new HashMap[Player,PlayerHeuristic]();
        for(p<-myPlayers){
            heuristics.put(p, new PlayerHeuristic(p));
        }
    }

    def calculatePower(){
        for(p<-myPlayers){
            heuristics.get(p).calculatePower()
        }
    }

    /**
     * @param p
     * @param loc
     * @return A value representing the influence that the zones have over the selected location.
     */
    def getRegionPower(p:Player,loc:Region)= heuristics.get(p).getRegionPower(loc);

    /**
     * @param p
     * @param loc
     * @return A value representing the influence that the troops have over the selected location.
     */
    def getTroopPower(p:Player,loc:Region)=heuristics.get(p).getTroopPower(loc)
    def getRegions=myRegions
}

/**
 * This is a wrapper class that is necessary for mock testing.
 */
class AiDirObj{
    def getRegions=AiDirector.getRegions
    def getRegionPower(p:Player,r:Region)=AiDirector.getRegionPower(p,r)
    def getTroopPower(p:Player,r:Region)=AiDirector.getTroopPower(p,r)
}
