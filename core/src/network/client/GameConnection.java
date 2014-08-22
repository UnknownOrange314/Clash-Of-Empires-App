package network.client;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashSet;

import engine.general.utility.Line;
import engine.rts.model.Resource;
import server.model.UpgradeDefinition;
import server.clientCom.GameStateData;
import server.clientCom.PlayerStats;
import server.clientCom.RegionRenderData;
import server.model.mapData.TerrainType;
import network.DataUpdater;
/**
 * This class defines an interface for the view to interact with the game data.
 */
public abstract class GameConnection extends Thread{

    ArrayList<UpgradeDefinition> improvementData=null; //This represents the list of improvements the game uses.
    ArrayList<Resource> resourceData=null;//This represents the list of resources the game uses.
    ArrayList<UpgradeDefinition> upgradeData=null; //This represents the list of improvements the game uses.
    ArrayList<TerrainType> terrainTypes=null;  //This represents the different terrain types that each region can have.
    GameStateData gameStateData=null;//This variable represents the parts of the game state that are rendered.
    RegionRenderData regionData=null; //This variable represents the data for drawing each region.
    public PlayerStats myStats=null; //TODO: Make this variable private.

    public abstract void sendInput(Object input);
    
    public ArrayList<Resource> getResourceDefs(){
    	return resourceData;
    }
    
    public ArrayList<UpgradeDefinition> improvementDefs(){
    	return upgradeData;
    }
    
    public GameStateData getGameState(){
    	return gameStateData;
    }
    
    public ArrayList<Polygon> getRegionBounds(){
    	return regionData.regionBounds;
    }
    
    public ArrayList<Integer> getXPositions(){
    	return regionData.xLocs;
    }
    
    public ArrayList<Integer> getYPositions(){
    	return regionData.yLocs;
    }

    public HashSet<Line> getRallyPoints(){
    	return myStats.getRallyPoints();
    }
    
    public int getImprovementCount(){
    	return upgradeData.size();
    }
    
    public Point getRightClick(){
    	return myStats.getRightClick();
    }
    
    public Point getLeftClick(){
    	return myStats.getLeftClick();
    }
    
    public abstract boolean nullData();
    
    public abstract void addDataCallback(DataUpdater c2);
    
    //TODO:Consider removing this method since it does nothing.
    @Override
    public void run(){
    }
}
