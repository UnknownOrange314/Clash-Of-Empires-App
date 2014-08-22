package server.model.ai.strategy.AttackStrategy;


import server.model.ai.EasyComputerPlayer;
import java.util.HashMap;
import java.util.HashSet;
import server.model.playerData.Region;
import server.model.playerData.Player;
import server.model.playerData.Army;

public class SimpleAttackStrategy implements AttackStrategy {

    static final double ATTACK_MUL=1.1;
    
    
    HashMap<Region,String> attackStrengths=new HashMap<Region,String>();

    public double findStrength(Player p,Region targetRegion,HashMap<Region,Army> defendingTroops){
    	double str=p.getTroopCount(targetRegion);
    	for(Region border:targetRegion.getBorderRegions()){
    		if(border.getOwner()==p){
    			str+=defendingTroops.get(border).size();
    		}
    	}
    	return str;
    }
    
    public HashMap<Region,String> getAttackPower(){
    	return attackStrengths;
    }

    public double getAttackStrength(Region r){
        String aStr=attackStrengths.get(r);
        return Double.parseDouble(aStr.split(":")[0]);
    }

    public void attackTargets(EasyComputerPlayer p,HashSet<Region>myTargets){

        attackStrengths.clear();
        if(p.getPersonalityName().equals("neutral")){
            return;
        }

        HashMap<Region,Army> myTroops = p.getTroopData();
        HashMap<Region,Army> defendingTroops=new HashMap<Region,Army>();
        for(Region region:myTroops.keySet()){
            Army army=myTroops.get(region);
        	defendingTroops.put(region,army);
        }

        for (Region targetRegion:myTargets){
            
            double myStrength =findStrength(p,targetRegion,defendingTroops);
            double enemyStrength = 0;
            
            enemyStrength+=targetRegion.getOwnerTroopCount();
            double attackCount=0; //This is the number of troops that are attacking
            attackStrengths.put(targetRegion,myStrength+":"+enemyStrength);//Save the troop strength.
            double maxCount=(enemyStrength+1)*ATTACK_MUL;

            if (myStrength>enemyStrength*ATTACK_MUL){
                for (Region region:targetRegion.getBorderRegions()){
                    Army army=p.getTroopData().get(region);//Get the troops in a region.
                    Army movingTroops=new Army(p);
                    if(p.getTroopCount(region)>0) {
                        p.setRallyPoint(region,targetRegion);
                        /**
                         * We want to allocate troops to attack a region and we want to make sure that
                         * they are not counted as able to attack another region.
                          */
                        int addSize=(int)(maxCount-attackCount);
                        movingTroops=army.createNewArmy(addSize);
                        attackCount+=addSize; //We might have a problem if there aren't enough troops
                        //Remove all the troops that have already been allocated.
                        defendingTroops.get(region).filter(movingTroops);
                    }
                }
            }
        }
    }
}
