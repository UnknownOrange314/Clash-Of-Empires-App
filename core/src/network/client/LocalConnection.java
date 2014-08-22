package network.client;


import network.DataUpdater;
import server.model.mapData.GameOption;
import server.model.mapData.MapFacade;
import server.model.mapData.TerrainType;
import server.model.playerData.Player;
import server.model.UpgradeDefinition;
import server.clientCom.GameStateData;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.Polygon;

import network.server.LocalCommunicator;
import server.controller.playerControls.HumanPlayer;
import server.clientCom.PlayerStats;
import engine.general.network.DisplayCommunicator;
import engine.rts.model.Resource;

import com.badlogic.gdx.utils.Timer;
/**
 * This class will represent a local connection with the game data and will not involve networking.
 */
public class LocalConnection extends GameConnection{

    DataUpdater upCallback=null;
    
    //Initialize the objects used to communicate with the server.
    ObjectInputStream renderInput=null;
    Socket serverConnection=null;
    InputStream renderStream=null;
    ObjectOutputStream commandOutput=null;

    final int NUM_PLAYERS = 4; //TODO: Check to see if this value is necessary. If it is make sure that it is not hardcoded.
    GameOption opt = new GameOption(NUM_PLAYERS,true);
    DisplayCommunicator gameListener=new LocalCommunicator(1,opt);
    
    Timer updateTimer=new Timer();

    public LocalConnection(){
        opt.addPlayer(gameListener);
        gameListener.start();
        setupGame();
        updateTimer.scheduleTask(new updateThread(),0.0f,0.05f);

    }

    //Initialize the timer and wait for the callback methods to be passed

    public void setupGame(){
        MapFacade.setupMap(opt);
        regionData=MapFacade.getRegionData();
        resourceData=Resource.resourceList;
        upgradeData=UpgradeDefinition.upgradeList;
        terrainTypes=TerrainType.terrainList;
    }

    /**
     * This function is called at regular intervals to obtain the game state so it can be rendered.
     */
    class updateThread extends Timer.Task{
        public void run(){   
            MapFacade.updateGame(); //Update game.
            gameStateData=MapFacade.compressData();//Get the data for the game state.

            //See if the players have received any input
            for (HumanPlayer player:MapFacade.getHumans()){
                player.clientListen();
            }

            //See if any additional data has ben sent.
            LocalCommunicator lCom=(LocalCommunicator)gameListener;
            while(lCom.emptyOutput()==false){
                Object out=lCom.getOutput();
                if(out instanceof PlayerStats){
                    myStats=(PlayerStats)out;
                    upCallback.update(myStats);//Update market data.
                }
            }
        }
    }

    /**
     * Send input to the game so that it can be processed.
     * @param input
     */
    public void sendInput(Object input){
        ((LocalCommunicator)gameListener).writeToServer(input); //TODO: Figure out a way to write this without the class cast.
    }

    /**
     * This method checks if there is any null data.
     * @return
     */
    public boolean nullData(){
        if (regionData==null){
            System.out.println("No region data");
            return true;
        }
        if (gameStateData==null){
            System.out.println("No game state data");
            return true;
        }
        if (myStats==null){
            System.out.println("No statistics");
            return true;
        }
        if (upgradeData==null){
        	System.out.println("No upgrade data");
            return true;
        }
        if (resourceData==null){
        	System.out.println("No resource data");
            return true;
        }
        return false;
    }

    
  

   
    public void addDataCallback(DataUpdater c2){
        upCallback=c2;
        updateTimer.start();     
    }
    
}
