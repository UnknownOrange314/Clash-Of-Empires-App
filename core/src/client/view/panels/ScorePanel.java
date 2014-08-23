package client.view.panels;

import client.ImageManager;
import server.clientCom.GameStateData;
import java.util.HashMap;
import engine.general.view.DrawArea;
import engine.general.view.gui.Label;
import engine.general.view.gui.IconLabel;
import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import java.io.File;
import network.client.GameConnection;
import engine.rts.model.Resource;
import java.awt.image.BufferedImage;
import network.client.GameConnection;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.MyGdxGame;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class ScorePanel extends DrawArea{
    
	final static int SCORE_X=MyGdxGame.WIDTH;
	final static int SCORE_Y=0;
	final static int SCORE_WIDTH=300;
	final static int SCORE_HEIGHT=300;
	
	public static ScorePanel createScorePanel(ImageManager data){
	    return new ScorePanel(data,SCORE_X,SCORE_Y,SCORE_WIDTH,SCORE_HEIGHT);
	}

	final ImageManager imageData;
   
	//TODO:Make sure the nation names are not hardcoded.
	String[] nationNames=new String[6];
	   
    HashMap<String,String> stats=null;
    HashMap<IconLabel,Label> labels=new HashMap<IconLabel,Label>();
    HashMap<String,Texture> flags=new HashMap<String,Texture>();
    HashMap<String,Label> scoreList=new HashMap<String,Label>();
    
    BitmapFont font=new BitmapFont();
    
	/**
	 * This object shows information about each player
	 */
	public ScorePanel(ImageManager iData,int x,int y,int w,int h){
		super(x,y,w,h);
		imageData=iData;
		nationNames[0]="Turkey";
	    nationNames[1]="Russia";
	    nationNames[2]="Spain";
	    nationNames[3]="England";
	    nationNames[4]="Prussia";
	    nationNames[5]="Austria";
	    font.setColor(Color.WHITE);
	}
	  
    public void loadFlags(){
      
        for(int a=0;a<nationNames.length;a++){
            String file=imageData.getFlag(nationNames[a]);
            flags.put(nationNames[a],new Texture(file));
        }
                
        int xD=20;
        int yD=20;
        
        for (String name:nationNames){            
        	components.add(new Label(xD,yD,name,font));       	
        	IconLabel label=(new IconLabel.Builder())
						.xPos(xD)
						.yPos(yD-10)
						.image(flags.get(name))
						.size(20)
						.build();
            components.add(label);
            Label sLabel=new Label(xD+65,yD,"",font);
        	components.add(sLabel);
	        scoreList.put(name,sLabel);
            xD+=100;                      
        }
    }
    
    public void setStats(GameStateData data){
        
    	if (stats==null){
            loadFlags();
        }

        stats=data.nationInfo;

        for (String name:stats.keySet()){
        	String pop=stats.get(name);
            String score=NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(pop));
            scoreList.get(name).setText(score);
        }
    }
}