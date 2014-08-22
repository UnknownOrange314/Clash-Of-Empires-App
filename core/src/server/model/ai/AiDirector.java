package server.model.ai;


import java.util.ArrayList;
import java.util.HashMap;
import server.model.playerData.Player;
import server.model.playerData.Region;

/**
 * This class is responsible for controlling how the AI behaves.
 * @author Bharat
 *
 */
public class AiDirector{
    
    private static ArrayList<Player> myPlayers=null;
    private static ArrayList<Region> myRegions=null;
    private static HashMap<Player,PlayerHeuristic> heuristics=null;

    public static void init(ArrayList<Player>p2,ArrayList<Region>r){
        myRegions=r;
        myPlayers=p2;
        heuristics=new HashMap<Player,PlayerHeuristic>();
        for(Player p:myPlayers){
            heuristics.put(p, new PlayerHeuristic(p));
        }
    }

    public static void calculatePower(){
        for(Player p:myPlayers){
            heuristics.get(p).calculatePower();
        }
    }

    /**
     * @param p
     * @param loc
     * @return A value representing the influence that the zones have over the selected location.
     */
     public static double rPower(Player p,Region loc){
    	 return heuristics.get(p).getRegionPower(loc);
     }

    /**
     * @param p
     * @param loc
     * @return A value representing the influence that the troops have over the selected location.
     */
    public static double tPower(Player p,Region loc){
    	return heuristics.get(p).getTroopPower(loc);
    }
    
    public static ArrayList<Region> regionList(){
    	return myRegions;
    }


    public ArrayList<Region> getRegions(){
    	return AiDirector.regionList();
    }
    
    public double getRegionPower(Player p,Region r){
    	return AiDirector.rPower(p,r);
    }
    
    public double getTroopPower(Player p,Region r){
    	return AiDirector.tPower(p,r);
    }
}
