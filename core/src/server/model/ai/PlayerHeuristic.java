package server.model.ai;


import server.model.playerData.Player;
import java.util.HashMap;
import server.model.playerData.Region;

/**
 * This class contains heuristics for each player.
 * @author Bharat
 */
class PlayerHeuristic{
    
    static final double P_DECAY_RATE=0.99; //This is the rate at which power decays based on distance.
    static final double MAX_INFLUENCE_DIST=200; //Maximum region distance for calculations of power.
    static final double DIST_MUL=100; //The distance between regions is multiplied by this value.
    static final double MIN_POW=0.001; //Minimum score to avoid divide by 0 errors.
    private HashMap<Region,Double> regionPower=new HashMap<Region,Double>();//This is the power that a player has in a region based on nearby regions.
    private HashMap<Region,Double> troopPower=new HashMap<Region,Double>();//This is the power that a player has in a region based on nearby troops.

    private final Player myPlayer;
    
    public PlayerHeuristic(Player p){
    	myPlayer=p;
    }
    
    
    public double getRegionPower(Region loc){
    	return regionPower.get(loc);//Get the result of the region power heuristic.
    }
    
    public double getTroopPower(Region loc){
    	return troopPower.get(loc);//Get the result of the troop power heuristic.
    }

    /**
     * This method calculates the power that a player has over each region of the map.
     */
    public void calculatePower(){
        for(Region region:AiDirector.regionList()){
            findRegionPower(region);
            findTroopPower(region);
        }
    }

    /**
     * Calculates the influence that a players' zones have on a particular location.
     * @param loc Location to calculate influence for.
     */
    private void findRegionPower(Region loc){
        
        regionPower.put(loc, MIN_POW);
        double score=0.0;
        for(Region c:AiDirector.regionList()){
            if(c.getOwner()==myPlayer){
                double distance=DIST_MUL*c.compareDistance(loc);
                if(distance<MAX_INFLUENCE_DIST){
                    score=score+Math.pow(P_DECAY_RATE,distance);
                }
            }
        }
        regionPower.put(loc, score);
    }

    /**
     * Calculates the influence that a players' troops have on a particular location.
     * @param loc Location to calculate influence for.
     */
    private void findTroopPower(Region loc){
        double pow=0.0;
        for(Region r:AiDirector.regionList()){
            int tCount=myPlayer.getTroopCount(r);
            pow=30;
            pow+=30*tCount/(100*r.compareDistance(loc)+MIN_POW);
        }
        troopPower.put(loc,pow);
    }
}
