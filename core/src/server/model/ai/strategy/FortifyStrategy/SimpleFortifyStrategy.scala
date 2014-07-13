package server.model.ai.strategy.FortifyStrategy

import scala.collection.JavaConversions._
import java.util.HashMap
import server.model.ai.EasyComputerPlayer
import server.model.playerData.{Troop}
import server.model.ai.PotentialField
import server.model.playerData.Army
import java.util.HashSet
import server.model.playerData.Region

class SimpleFortifyStrategy extends ForitfyStrategy{

    /**
     *
     * @param regionTroops The army for each region.
     * @param regionScores The score for each region.
     * @param player The player that owns the troops and regions.
     * @return
     */
   def fortifyTroops(regionTroops: HashMap[Region, Army], regionScores: HashMap[Region, Double], player: EasyComputerPlayer): PotentialField ={
       
       val troopCounts=regionTroops.map(rVal=>rVal._1->rVal._2.size().toDouble).toMap //Create new data for troop counts.
       val potentialField=PotentialField.getPotentialVectors(troopCounts,regionScores,player)
       player.clearRallyPoints()
       potentialField.activateEdges(player,regionTroops)
       return potentialField
   }
}
