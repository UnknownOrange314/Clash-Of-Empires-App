package server.model.ai

import server.model.UpgradeDefinition
import java.awt.Color
import server.model.mapData.MapFacade
import server.model.ai.strategy.AttackStrategy.SimpleAttackStrategy
import server.model.ai.strategy.FortifyStrategy.SimpleFortifyStrategy
import server.model.playerData.{Player}
import server.model.playerData.RegionBorder
import scala.collection.JavaConversions._
import java.util.HashMap
import java.util.HashSet
import engine.general.utility.Location
import server.model.playerData.Region
import java.util.ArrayList
import engine.general.utility.Line
import java.awt.Graphics
import server.model.ai.strategy.TargetEval

class EasyComputerPlayer(persona:AiPersona,num:Double,pEval:AiDirObj) extends ComputerPlayer(){
	
     val powerEval=pEval
     val myPersonality=persona //This describes the personality of the AI.
     var ignoreList:Map[Location,Region]=null//The list of regions that the AI should not reinforce due to a low score.
     var myTargets=new HashSet[Region] //The regions that the AI plans to attack.
     var powerCenter:Region=null //The region that is the "power center".
     var visited:HashSet[Region]=null //This is used by the isConnected method.
   
     val attackStrat = new SimpleAttackStrategy()
     val foritfyStrat = new SimpleFortifyStrategy()
     var potentialField:PotentialField=null  //The potential field that determines how the AI should move
     var locationScores=new HashMap[Region,Double]
     def getPersonality():AiPersona=myPersonality

     override def act(Players:ArrayList[Player]){
    	
         if(isNeutral){
             return
         }

         val MIN_SCORE:Int=0
         setBorderingZones()
         locationScores=TargetEval.scoreLocations(powerEval,playerList,this) //Figure out the value of each region.
         myTargets=TargetEval.findTargets(MIN_SCORE,locationScores,this)  //Find locations to attack.
         ignoreList=TargetEval.ignoreRegions(locationScores) //Find locations that the AI should ignore.
         potentialField=foritfyStrat.fortifyTroops(getTroopData,locationScores,this)
         attackStrat.attackTargets(this, myTargets)
         upgradeRegions()
     }

     def isNeutral=getPersonality().equals(AiPersona.getNeutralPersona())
     
     /**
      * This method determines how the AI should spend resources on upgrades.
      */
     def upgradeRegions(){
         for(region<-regions){
             for(upgrade<-UpgradeDefinition.upgradeList){
                 if(region.hasUpgrade(upgrade)==false){
                     if(resources.canUpgrade(upgrade.getCost())){
                         resources.buyUpgrade(upgrade)
                         region.addUpgrade(upgrade)
                     }
                 }
             }
         }
     }

    def getStrengthRatio():HashMap[Region,String]=attackStrat.asInstanceOf[SimpleAttackStrategy].attackStrengths  //This method returns strength ratios for regions it plans to attack for debugging purposes.
    def getTargets():HashSet[Region]=myTargets
    def getPotentialField():PotentialField=potentialField

    /**
     * This method determines the number of troops the AI autobuilds, and accounts for the fact that some algorithms say an AI should
     * produce fewer troops.
     */
    override def getBuildCounts(): HashMap[Region, Integer]={
        val buildCounts:HashMap[Region,Integer]=super.getBuildCounts
        var finalCounts=new HashMap[Region,Integer]()
        val buildFactor=myPersonality.reinforceRate
        for((buildLoc,buildCount)<-buildCounts){
            finalCounts.put(buildLoc,(buildCount*buildFactor).toInt)
        }
        return finalCounts
    }


   //TODO:Make this method private to testing classes
    def getPlayers()=playerList
    def getLocationScores():HashMap[Region,Double]={
        locationScores
    }

    def getPersonalityName()=getPersonality().getName
    def getEnemyBorders=enemyBorderRegions

   /**
    * Checks to see if the zone is connected. This should be part of the heuristic for determining city value.
    * @param c
    * @return
    */
    def  isConnected(c:Region):Double={
        return 10;//Temporary hack
        visited.add(c)
        if(c.equals(powerCenter)){
            return 1
        }
        var border:ArrayList[Region] =c.getBorderRegions()
        var temp=new HashSet[Region]()
        for((region) <- border){
            if(regions.contains(region)){
                if(visited.contains(region)){
	    	    temp.add(region)
	         }
    	    }
    	}
    	if(temp.size()==0){
    	    return 10
    	}
    	var  min:Double=9999
    	for( z <- temp){
    	    visited=new HashSet[Region]()
    	    var a=isConnected(z)
    	    if(a<min){
    	        min=a
    	    }
    	}
        return min
    }

    def ignoreScore():Double={
        return 0
    }


   
}
