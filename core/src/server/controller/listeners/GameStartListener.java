package server.controller.listeners;

import server.clientCom.GameStateData;
import server.controller.playerControls.HumanPlayer;
import server.model.UpgradeDefinition;
import server.model.ai.AiPersona;
import server.model.mapData.MapFacade;
import server.clientCom.RegionRenderData;
import server.model.mapData.TerrainType;
import engine.rts.model.Resource;
import server.model.mapData.GameOption;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This class is responsible for starting the server and making sure that it sends data to each client.
 */
public class GameStartListener implements ActionListener{
	
    private GameOption options; //This object represents the game settings and data about the clients.

    /**
     * The constructor for the game.
     * @param opt Game options
     */
    public GameStartListener(GameOption opt){
       options=opt;
    }

    /**
     * This method starts a thread for each client when the game starts
     * @param e
     */
    public void actionPerformed(ActionEvent e){
        ServerUpdateThread update=new ServerUpdateThread();
        Timer updateTimer=new Timer(50,update);
        updateTimer.start();
    }

    /**
     *This class represents a thread that each HumanPlayer will use to listen to input from the client and send output.
     */
    public class ClientConnectionThread extends Thread{
    	
        HumanPlayer clientPlayer;

        /**
         * Constructor for the client.
         * @param player The player that the client will control
         */
        public ClientConnectionThread(HumanPlayer player){
            clientPlayer=player;
        }

        public void run(){
            clientPlayer.clientListen();
        }

        /**
         * Sends data to the client.
         * @param data Data that will be sent to the client.
         */
        public void sendData(Object data){
            clientPlayer.sendData(data);
        }
        public void sendStatistics(){
            clientPlayer.sendStatistics();
        }
    }

    /**
     * This class is responsible for periodically sending data to each client
     * TODO: Make sure that the data is sent to each client and not just one of them.
     */
    public class ServerUpdateThread implements ActionListener{
        private ArrayList<ClientConnectionThread> clientConnections; //The list of connections to the clients
        public ServerUpdateThread(){
            MapFacade.setupMap(options);
            RegionRenderData regionData=MapFacade.getRegionData(); //Get the data that describes each region.

            //Start a thread for each player so that they can listen to input from the client
            clientConnections=new ArrayList<ClientConnectionThread>();

            //Initialize client connections for each client.
            for(HumanPlayer p:options.getClients()){
                ClientConnectionThread listener=new ClientConnectionThread(p);
                clientConnections.add(listener);
            }

            for(ClientConnectionThread clientConnection:clientConnections){
            	
                clientConnection.sendData(regionData);//Send the data that defines each region

                //Send data from JSON configuration files
                clientConnection.sendData(UpgradeDefinition.upgradeList());
                clientConnection.sendData(AiPersona.AiList());
                clientConnection.sendData(Resource.resourceList());
                clientConnection.sendData(TerrainType.terrainList());
                clientConnection.start();
            }
        }
        
        public void actionPerformed(ActionEvent e){
            MapFacade.updateGame(); //Update the game state.
            try{
                //Send the data to each of the clients
                GameStateData displayData=MapFacade.compressData();
                for(ClientConnectionThread clientConnection:clientConnections) {
                    clientConnection.sendData(displayData);
                    clientConnection.sendStatistics();
                }
            }
            
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
 }
