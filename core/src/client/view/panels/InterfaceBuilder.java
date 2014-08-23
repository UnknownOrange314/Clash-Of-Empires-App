package client.view.panels;


import client.ImageManager;
import network.client.GameConnection;
import com.mygdx.game.MyGdxGame;

public class InterfaceBuilder {
  
    final static int SCORE_X=MyGdxGame.WIDTH;
    final static int SCORE_Y=0;
    final static int SCORE_WIDTH=300;
    final static int SCORE_HEIGHT=300;
	public static ScorePanel createScorePanel(ImageManager data){
	    return new ScorePanel(data,SCORE_X,SCORE_Y,SCORE_WIDTH,SCORE_HEIGHT);
	}
	
    final static int RINFO_X=0;
    final static int RINFO_Y=0;
    final static int RINFO_WIDTH=600;
    final static int RINFO_HEIGHT=350;
	public static RegionInfo createRegionPanel(GameConnection serverConnection){
	    return new RegionInfo(RINFO_X,RINFO_Y,RINFO_WIDTH,RINFO_HEIGHT,serverConnection);
	}
	
	final static int LOG_X=1500;
	final static int LOG_Y=0;
	final static int LOG_WIDTH=400;
	final static int LOG_HEIGHT=200;
	public static LogPanel createLogPanel(GameConnection serverConnection){
        return new LogPanel(LOG_X,LOG_Y,LOG_WIDTH,LOG_HEIGHT,serverConnection);
	}
	
	//The dimensions of the game map TODO: Make sure this isn't hardcoded
	final static int MAP_WIDTH=2000;
	final static int MAP_HEIGHT=2000;
	final static int MINIMAP_X=1500;
	final static int MINIMAP_Y=700;
	final static int MINIMAP_HEIGHT=400;
	final static int MINIMAP_WIDTH=400;
	public static Minimap createMinimap(){
        return new Minimap(MAP_WIDTH,MAP_HEIGHT,MINIMAP_X,MINIMAP_Y,MINIMAP_HEIGHT,MINIMAP_WIDTH);
	}
	
	final static int INFO_X=1500;
	final static int INFO_Y=200;
	final static int INFO_WIDTH=400;
	final static int INFO_HEIGHT=500;
	public static InfoPanel createInfoPanel(GameConnection serverConnection){
        return new InfoPanel(INFO_X,INFO_Y,INFO_WIDTH,INFO_HEIGHT,serverConnection);
	}
}