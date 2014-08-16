package server.model.ai

import server.model.playerData.Player
import java.util.HashMap
import java.util.ArrayList
import java.util.HashSet
import engine.general.utility.Location
import scala.collection.JavaConversions._
import server.model.playerData.Region

/**
 * This class contains engine.general.utility methods for creating AI.
 */
class ComputerPlayer extends Player(){
    
    var enemyBorderRegions=new HashMap[Location,Region]()
    var myBorderRegions=new HashMap[Location, Region]()
    
    override def act(myPlayers:ArrayList[Player]) { }
    def getBorders()=myBorderRegions.values()

    /**
     * This method sets the bordering zones for a player
     */
    def setBorderingZones(){
        myBorderRegions.clear()
        enemyBorderRegions.clear()
        for(reg:Region<-regions) {
            var regionBorders=reg.getBorderRegions()
            for(potBorder<-regionBorders){
                var potLoc=potBorder.getCenterLoc();
                if(!regions.contains(potBorder)){
                    myBorderRegions.put(reg.getCenterLoc(),reg);//Add region to list of border regions.
                    enemyBorderRegions.put(potLoc, potBorder);//Add enemy border to list of enemy border regions.
                }
            }
        }
    }
}