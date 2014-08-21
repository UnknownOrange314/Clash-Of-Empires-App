package server.model.ai;


import server.model.playerData.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;

import engine.general.utility.Location;
import server.model.playerData.Region;

/**
 * This class contains engine.general.utility methods for creating AI.
 */
class ComputerPlayer extends Player{
    
    HashMap<Location,Region> enemyBorderRegions=new HashMap<Location,Region>();
    HashMap<Location,Region> myBorderRegions=new HashMap<Location, Region>();
    
    @Override
    public void act(ArrayList<Player> myPlayers) { }
    public Collection<Region> getBorders(){
    	return myBorderRegions.values();
    }

    /**
     * This method sets the bordering zones for a player
     */
    public void setBorderingZones(){
        myBorderRegions.clear();
        enemyBorderRegions.clear();
        for(Region reg:regions) {
            ArrayList<Region> regionBorders=reg.getBorderRegions();
            for(Region potBorder:regionBorders){
                Location potLoc=potBorder.getCenterLoc();
                if(!regions.contains(potBorder)){
                    myBorderRegions.put(reg.getCenterLoc(),reg);//Add region to list of border regions.
                    enemyBorderRegions.put(potLoc, potBorder);//Add enemy border to list of enemy border regions.
                }
            }
        }
    }
}