package server.model.mapData;

import server.clientCom.GameStateData;
import server.clientCom.RegionRenderData;
import server.controller.playerControls.HumanPlayer;
import server.model.ai.EasyComputerPlayer;
import server.model.playerData.Region;
import javax.swing.*;
import java.util.ArrayList;
import server.model.playerData.Player;

public class MapFacade {
	
    private GameMap myMap;
    private MapFacade() { }
    /**
    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
    private static class SingletonHolder { 
            public static final MapFacade INSTANCE = new MapFacade();
    }
    
    /**
     * This method sets up a test map.
     * @param iterations The number of initial iterations.
     * @param nTroops Will neutral troops be present.
     */
    public static void setupTestMap(int iterations,boolean nTroops){
        int NUM_PLAYERS=5;
        setupTestMap(iterations,NUM_PLAYERS,nTroops);
    }

    public static void setupTestMap(GameOption options){
        setupMap(options);
    }
    
    public static void setupTestMap(int iterations,int numPlayers,boolean nTroops){
        GameOption opt=new GameOption(numPlayers,nTroops);
        setupMap(opt);
        for(int i=0;i<iterations;i++){
            updateGame();
        }

    }

    public static ArrayList<HumanPlayer> getHumans(){
       ArrayList<HumanPlayer> arr=new ArrayList<HumanPlayer>();
       for(Player p:getMap().getPlayers()){
           if(p.isHuman()) {
               arr.add(p.convert());
           }

       }
        return arr;
    }
    
    public static ResourceMarket getResourceMarket() {
        return getMap().getMarket();
    }
    
    public static void setupMap(GameOption myOptions){
    	SingletonHolder.INSTANCE.myMap=new GameMap(myOptions);
    }

    public static void updateGame(){
    	SingletonHolder.INSTANCE.myMap.update();
    }

    public static ArrayList<Region> getRegions(){
    	return SingletonHolder.INSTANCE.myMap.getRegions();
    }

    public static int getElapsedTime(){
        return getMap().getCycles();
    }

    public static int countTroops(){
        int troopCount=0;
        for(Player p:MapFacade.getPlayers()){
            troopCount=troopCount+p.countTroops();
        }
        return troopCount;
    }

    public static ArrayList<Player> getPlayers(){
    	return getMap().getPlayers();
    } 

    public static EasyComputerPlayer getTestPlayer(){
        return getMap().getTestPlayer();
    }

    private static GameMap getMap(){
        return SingletonHolder.INSTANCE.myMap;
    }

    public static RegionRenderData getRegionData(){
        return getMap().getRegionRenderData();
    }

    public static GameStateData compressData(){
        return SingletonHolder.INSTANCE.myMap.compressData();
    }
}
