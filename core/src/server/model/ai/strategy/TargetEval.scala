package server.model.ai.strategy

import java.util.HashSet
import java.util.HashMap
import java.util.ArrayList
import server.model.playerData.Player
import server.model.playerData.Region
import server.model.ai.AiDirObj
import server.model.ai.EasyComputerPlayer
import scala.collection.JavaConversions._
import engine.general.utility.Location

/**
 * This class contains functions used to evaluate a potential target before
 * attacking. The functions should be stateless.
 */
object TargetEval {

    val F_TROOP_SCORE=0.00001
    val E_TROOP_SCORE=0.00001
    /**
     * This method will score all the cities. The score should be based on the value of cities.
     */
    def  scoreLocations(powerEval:AiDirObj,playerList:ArrayList[Player],curPlayer:EasyComputerPlayer):HashMap[Region,Double]={
        
        var scores=new HashMap[Region,Double]()

        //This is the algorithm for scoring cities. It will be modified later to use a more intelligent algorithm.
        for(region <- powerEval.getRegions){
            var myTroopScore=F_TROOP_SCORE
            var enemyTroopScore=E_TROOP_SCORE
            var myRegionScore=0.0
            var enemyRegionScore=0.0

            for(player:Player <- playerList) {
                //A player will prefer to attack regions where it has regions and troops nearby
                if(player==curPlayer){
                    val conFactor:Double=curPlayer.isConnected(region) //We do not want regions that are not connected to our server power base.
                    myRegionScore+=powerEval.getRegionPower(player,region)*conFactor
                    myTroopScore+=powerEval.getTroopPower(player,region)
                }
                else{
                    if(player.isInstanceOf[EasyComputerPlayer]){
                        if(player.asInstanceOf[EasyComputerPlayer].isNeutral){
                            enemyTroopScore+=player.getTroopCount(region)
                        }
                        else{
                            enemyTroopScore+=powerEval.getTroopPower(player,region)
                            enemyRegionScore+=powerEval.getRegionPower(player,region)
                        }
                    }
                    else{
                        enemyTroopScore+=powerEval.getTroopPower(player,region)
                        enemyRegionScore+=powerEval.getRegionPower(player,region)
                    }
                }
            }
            
            if(myRegionScore>enemyRegionScore){
                scores.put(region,(myRegionScore-enemyRegionScore)*((1+myTroopScore)/enemyTroopScore))
            }
            
            else{
                scores.put(region,(myRegionScore-enemyRegionScore)+(myRegionScore)+((1+myTroopScore)/enemyTroopScore)) //Black
            }
        }
        return scores
    }

    /**
     * This method will find targets for a player to attack.
     */
    def  findTargets(minScore:Double,locationScores:HashMap[Region,Double],p:EasyComputerPlayer):HashSet[Region]={
        
        var myTargets=new HashSet[Region]()
        var locVals=new HashMap[Region,Double]

        /**Copy the AI scores into a new list that will be modified.
         **Only worry about regions that are controlled by an opponent and are bordering regions
         */
        for ((location,score)<-locationScores){
            if(p.getEnemyBorders.containsKey(location.getCenterLoc())){
                locVals.put(location,score)
            }
        }

        /**
         * Calculate the relative troop strength of the AI.
         * It does not want to be as aggressive if opponents have more troops than us.
         */
        val myTroopCount=p.countTroops().toDouble
        var totalCount=0.0
        for(player<-p.getPlayers){
            if(player.isInstanceOf[EasyComputerPlayer]){
                if(!player.asInstanceOf[EasyComputerPlayer].isNeutral){
                    totalCount=totalCount+player.countTroops().toDouble
                }
            }
            else{
                totalCount=totalCount+player.countTroops().toDouble
            }
        }
        
        val strRatio=Math.pow((myTroopCount/(totalCount/p.getPlayers.size())),2)
        myTargets.clear() //Clear the targets
        val power_factor=1.5 //A higher value means that an AI will be less likely to attack multiple targets
        var curScore=(1-strRatio)*500-500
        
        for( (key:Region,value:Double)<-locVals.toList sortBy ( 1/_._2 )){
            val score:Double=minScore+curScore
            if(value*strRatio>score){
                curScore=curScore*power_factor
                myTargets.add(key)
            }
        }
        return myTargets
    }

    val MIN_SCORE=0.0
    /**
     * This method defines a set of controlled regions that should be ignored and not defended.
     * Regions will be ignored if they have a low score.
     */
    def ignoreRegions(regionValues:HashMap[Region,Double]):Map[Location,Region]={
        var temp=regionValues.filter(_._2<MIN_SCORE) //Remove regions with low scores.
        return temp.map(rVal=>rVal._1.getCenterLoc()->rVal._1).toMap
    }
}
