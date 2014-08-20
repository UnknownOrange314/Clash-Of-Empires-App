package server.model.ai;

import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

import server.model.playerData.Player;
import server.model.playerData.Region;
import engine.general.utility.Location;

import java.util.TreeMap;

/**
 * This class contains functions used to evaluate a potential target before
 * attacking. The functions should be stateless.
 */
public class TargetEval {
    
	final static double MIN_SCORE=0.0;
    final static double F_TROOP_SCORE=0.00001;
    final static double E_TROOP_SCORE=0.00001;
    /**
     * This method will score all the cities. The score should be based on the value of cities.
     */
    public static HashMap<Region,Double> scoreLocations(AiDirObj powerEval,ArrayList<Player> playerList,EasyComputerPlayer curPlayer){
        
        HashMap<Region,Double> scores=new HashMap<Region,Double>();

        //This is the algorithm for scoring cities. It will be modified later to use a more intelligent algorithm.
        for(Region region:powerEval.getRegions()){
            double myTroopScore=F_TROOP_SCORE;
            double enemyTroopScore=E_TROOP_SCORE;
            double myRegionScore=0.0;
            double enemyRegionScore=0.0;

            for(Player player:playerList) {
                //A player will prefer to attack regions where it has regions and troops nearby
                if(player==curPlayer){
                    double conFactor=curPlayer.isConnected(region); //We do not want regions that are not connected to our server power base.
                    myRegionScore+=powerEval.getRegionPower(player,region)*conFactor;
                    myTroopScore+=powerEval.getTroopPower(player,region);
                }
                else{
                    if(player instanceof EasyComputerPlayer){
                        if( ((EasyComputerPlayer)player).isNeutral()){
                            enemyTroopScore+=player.getTroopCount(region);
                        }
                        else{
                            enemyTroopScore+=powerEval.getTroopPower(player,region);
                            enemyRegionScore+=powerEval.getRegionPower(player,region);
                        }
                    }
                    else{
                        enemyTroopScore+=powerEval.getTroopPower(player,region);
                        enemyRegionScore+=powerEval.getRegionPower(player,region);
                    }
                }
            }
            
            if(myRegionScore>enemyRegionScore){
                scores.put(region,(myRegionScore-enemyRegionScore)*((1+myTroopScore)/enemyTroopScore));
            }
            
            else{
                scores.put(region,(myRegionScore-enemyRegionScore)+(myRegionScore)+((1+myTroopScore)/enemyTroopScore)); //Black
            }
        }
        return scores;
    }

    /**
     * This method will find targets for a player to attack.
     */
    public static HashSet<Region>  findTargets(double minScore,HashMap<Region,Double> locationScores,EasyComputerPlayer p){
        
        HashSet<Region> myTargets=new HashSet<Region>();
        TreeMap<Region,Double> locVals=new TreeMap<Region,Double>();

        /**Copy the AI scores into a new list that will be modified.
         **Only worry about regions that are controlled by an opponent and are bordering regions
         */
        for (Region region:locationScores.keySet()){
        	double score=locationScores.get(region);
            if(p.getEnemyBorders().containsKey(region.getCenterLoc())){
                locVals.put(region,score);
            }
        }

        /**
         * Calculate the relative troop strength of the AI.
         * It does not want to be as aggressive if opponents have more troops than us.
         */
        double myTroopCount=(double)p.countTroops();
        double totalCount=0.0;
        for(Player player:p.getPlayers()){
            if(player instanceof EasyComputerPlayer){
                if(!((EasyComputerPlayer)player).isNeutral()){
                    totalCount=totalCount+(double)player.countTroops();
                }
            }
            else{
                totalCount=totalCount+(double)player.countTroops();
            }
        }
        double strRatio=Math.pow((myTroopCount/(totalCount/p.getPlayers().size())),2);
        myTargets.clear(); //Clear the targets
        double power_factor=1.5; //A higher value means that an AI will be less likely to attack multiple targets
        double curScore=(1-strRatio)*500-500;
        
        for( Region key:locVals.descendingKeySet()){
            double conquestVal=locVals.get(key);
        	double defenseScore=minScore+curScore;
            if(conquestVal*strRatio>defenseScore){
                curScore=curScore*power_factor;
                myTargets.add(key);
            }
        }
        return myTargets;
    }

    /**
     * This method defines a set of controlled regions that should be ignored and not defended.
     * Regions will be ignored if they have a low score.
     */
    public static HashMap<Location,Double> ignoreRegions(HashMap<Region,Double> regionValues){
        HashMap<Location,Double> lowScores=new HashMap<Location,Double>();
        for(Region r:regionValues.keySet()){
        	double score=regionValues.get(r);
        	if(score<MIN_SCORE){
        		lowScores.put(r.getCenterLoc(),score);
        	}
        }
    	return lowScores;
    }
}
