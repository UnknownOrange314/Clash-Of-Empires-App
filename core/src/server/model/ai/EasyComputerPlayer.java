package server.model.ai;

import server.model.UpgradeDefinition;

import java.awt.Color;

import server.model.mapData.MapFacade;
import server.model.ai.strategy.AttackStrategy.SimpleAttackStrategy;
import server.model.ai.strategy.FortifyStrategy.SimpleFortifyStrategy;
import server.model.playerData.Player;
import server.model.playerData.RegionBorder;

import java.util.HashMap;
import java.util.HashSet;

import engine.general.utility.Location;
import server.model.playerData.Region;

import java.util.ArrayList;

import engine.general.utility.Line;

import java.awt.Graphics;

public class EasyComputerPlayer extends ComputerPlayer{
	
	 final AiDirector powerEval;
	 final AiPersona myPersonality;
	 
	 HashMap<Location,Double> ignoreList=null; //The list of regions that the AI should not reinforce due to a low score.
	 HashSet<Region> myTargets=null; //The regions that the AI plans to attack.
	 Region powerCenter=null; //The region that is the "power center".
	 HashSet<Region> visited=null;  //This is used by the isConnected method.
	 
	 final SimpleAttackStrategy attackStrat;
	 final SimpleFortifyStrategy fortityStrat;
	
	 PotentialField potentialField=null;  //The potential field that determines how the AI should move
	 HashMap<Region,Double> locationScores=new HashMap<Region,Double>();
	 
	 public EasyComputerPlayer(AiPersona persona,double num,AiDirector pEval){
		 powerEval=pEval;
		 myPersonality=persona;
		 attackStrat=new SimpleAttackStrategy();
		 fortityStrat=new SimpleFortifyStrategy();
	 }


     public AiPersona getPersonality(){
    	 return myPersonality;
     }

     @Override
     public void act(ArrayList<Player> Players){
    	
         if(isNeutral()){
             return;
         }

         int MIN_SCORE=0;
         setBorderingZones();
         locationScores=TargetEval.scoreLocations(powerEval,playerList,this); //Figure out the value of each region.
         myTargets=TargetEval.findTargets(MIN_SCORE,locationScores,this);  //Find locations to attack.
         ignoreList=TargetEval.ignoreRegions(locationScores); //Find locations that the AI should ignore.
         potentialField=fortityStrat.fortifyTroops(getTroopData(),locationScores,this);
         attackStrat.attackTargets(this, myTargets);
         upgradeRegions();
     }

     public boolean isNeutral(){
    	 return getPersonality().equals(AiPersona.getNeutralPersona());
     }
     
     /**
      * This method determines how the AI should spend resources on upgrades.
      */
     public void  upgradeRegions(){
         for(Region region:regions){
             for(UpgradeDefinition upgrade:UpgradeDefinition.upgradeList){
                 if(region.hasUpgrade(upgrade)==false){
                     if(resources.canUpgrade(upgrade.getCost())){
                         resources.buyUpgrade(upgrade);
                         region.addUpgrade(upgrade);
                     }
                 }
             }
         }
     }

    public HashMap<Region,String>getStrengthRatio(){
    	return ((SimpleAttackStrategy)attackStrat).attackStrengths();  //This method returns strength ratios for regions it plans to attack for debugging purposes.
    }
    
    public HashSet<Region> getTargets(){
    	return myTargets;
    }
    
    public PotentialField getPotentialField(){
    	return potentialField;
    }

    /**
     * This method determines the number of troops the AI autobuilds, and accounts for the fact that some algorithms say an AI should
     * produce fewer troops.
     */
    @Override
    public HashMap<Region,Integer> getBuildCounts(){
        HashMap<Region,Integer> buildCounts=super.getBuildCounts();
        HashMap<Region,Integer> finalCounts=new HashMap<Region,Integer>();
        double buildFactor=myPersonality.reinforceRate;
        for(Region buildLoc:buildCounts.keySet()){
        	int buildCount=buildCounts.get(buildLoc);
            finalCounts.put(buildLoc,(int)(buildCount*buildFactor));
        }
        return finalCounts;
    }


   //TODO:Make this method private to testing classes
    public ArrayList<Player> getPlayers(){
    	return playerList;
    }
    
    public HashMap<Region,Double> getLocationScores(){
        return locationScores;
    }

    public String getPersonalityName(){
    	return getPersonality().getName();
    }
    
    public HashMap<Location,Region> getEnemyBorders(){
    	return enemyBorderRegions;
    }

   /**
    * Checks to see if the zone is connected. This should be part of the heuristic for determining city value.
    * TODO: Come up with a better name for the method. The current name does not reflect what the method actually does.
    * @param c
    * @return
    */
    public double  isConnected(Region c){
        return 10;//Temporary hack
    }

    //TODO: Consider removing this method
    public double ignoreScore(){
        return 0;
    }   
}