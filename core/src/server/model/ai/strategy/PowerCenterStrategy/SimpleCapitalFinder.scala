package server.model.ai.strategy.PowerCenterStrategy

import scala.collection.JavaConversions._
import server.model.playerData.Region
import java.util.HashMap
import java.util.HashSet
import java.util.Iterator
import server.model.ai.AiDirector
import java.util.HashMap
import engine.general.utility.Location

class SimpleCapitalFinder extends CapitalFinder{

    /**
     * This method finds a location for the capital by picking a region that is close to other regions the player owns.
     * @param myRegions
     * @param powerCenter
     */
    def findPowerCenter(myRegions:HashSet[Region],powerCenter:Region){
        var minDist:Double=99999999
	var powerCenter:Region=null
	for(regionA <- myRegions){
            var dist:Double=0
	    for(regionB<-myRegions){
	         dist=dist+regionA.compareDistance(regionB)
    	    }
            if(dist<minDist){
                minDist=dist
                powerCenter=regionA
            }
        }
    }
}
