package network;

import server.clientCom.PlayerStats;
import client.view.panels.InfoPanel;

//TODO: Consider the use of an anoymous function here.
public class DataUpdater {
	
	InfoPanel playerInfo=null;
	
	public DataUpdater(InfoPanel p){
		playerInfo=p;
	}
	
	public void update(PlayerStats stats){
		playerInfo.updateLabels(stats);
	}
	
}