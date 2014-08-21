package server.model.mapData;

import engine.general.network.DisplayCommunicator;
import server.controller.playerControls.HumanPlayer;
import java.util.ArrayList;
import engine.rts.model.OptionBase;

public class GameOption extends OptionBase{
	
    ArrayList<HumanPlayer> players=new ArrayList<HumanPlayer>();//This represents a list of players that have connected to the client
    boolean neutralTroops;
	public GameOption(int numPlayers,boolean nTroop){
		super(numPlayers);
		neutralTroops=nTroop;
	}

    public boolean neutralTroop(){
    	return neutralTroops;
    }
    
    public void addPlayer(DisplayCommunicator com){
        players.add(new HumanPlayer(com));
    }
    
    public ArrayList<HumanPlayer> getClients(){
    	return players;
    }
}