package client.view.panels;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import network.client.GameConnection;

import java.util.ArrayList;

import server.model.UpgradeDefinition;

import java.awt.Graphics;

import server.clientCom.RegionState;
import client.controller.RegionCommand;
import engine.general.view.DrawArea;
import engine.general.view.gui.*;
import engine.general.view.Display;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

 public class RegionInfo extends DrawArea{
	 
	static final int RINFO_X=0;
	static final int RINFO_Y=0;
	static final int RINFO_WIDTH=600;
	static final int RINFO_HEIGHT=350;
	
	public static RegionInfo createRegionPanel(GameConnection serverConnection){
	    return new RegionInfo(RINFO_X,RINFO_Y,RINFO_WIDTH,RINFO_HEIGHT,serverConnection);
	}  

    boolean on=false;
    RegionState selRegion=null;
    
    HashMap<String,Button> buttons= new HashMap<String,Button>();
    
	private final GameConnection gameConnection;
    final BitmapFont font=new BitmapFont();

	public RegionInfo(int x,int y,int w,int h,GameConnection gCon){
		super(x,y,w,h);
		gameConnection=gCon;
		font.setColor(Color.BLACK);
		System.out.println("");
	}
	
	//TODO: Rewrite method so that the method does not repeatedly create new objects.
    public void drawRegionState(RegionState state){
 
        selRegion=state;
        components.clear();
        buttons.clear();
        components.add(new Label(50,50,"Improvement list",font));

        for (int i=0;i<UpgradeDefinition.upgradeList.size()-1;i++){      

            int x=20;
        	int y=80+i*30;
            UpgradeDefinition upgrade=UpgradeDefinition.upgradeList.get(i);
            components.add(new Label(x,y,upgrade.getName(),font));
            x+=100;
            components.add(new Label(x,y,upgrade.costStr(),font));
            x+=100;

            if(state.upgradeData.get(i)){
                components.add(new Label(x,y,"Already built",font));
            }
            else{ //TODO: Reconsider the use of the list of buttons stored in this class since the superclass has a list of components, including the buttons.
                Button add=(new Button.ButtonBuilder())
        						.x(x)
        						.y(y)
        						.text("Buy "+upgrade.getName())
        						.font(font)
        						.build();
                buttons.put(upgrade.getName(),add);
            }   
            x+=100;
            components.add(new Label(x,y,upgrade.getInfo(),font));
        }
        
        buttons.put("Close",(new Button.ButtonBuilder())
        					.x(250)
        					.y(50)
        					.text("Close")
        					.font(font)
        					.build());
        					
        components.add(new Label(20,20,"Data for "+state.name,font));
        components.add(new Label(50,210,"Defense bonus:"+state.defenseBonus,font));
        components.add(new Label(50,230,"Region income: "+state.income,font));
        components.add(new Label(50,250,"Troops:"+state.getOwnerTroopCount(),font));
        components.add(new Label(50,270,"Hit points status:"+state.hitPoints,font));
        components.add(new Label(50,290,"Troop production"+state.troopProduction,font));
        components.add(new Label(50,310,"Attack bonus"+state.attackBonus,font));

        components.add(new Label(50,330,"Population:"+NumberFormat.getNumberInstance(Locale.US).format(state.population),font));
        for(String name:buttons.keySet()){
        	Button button=buttons.get(name);
            components.add(button);
        }
    }

    /**
     * This method processes a click. It might be a good idea to refactor this into a superclass method because
     * other drawArea objects have buttons to click and the fact that you need to transform the mouse click from
     * screen coordinates to panel coordinates.
     * @param x x coordinate of click.
     * @param y y coordinate of click
     */
    public void  processClick(int x,int y){
        for (String info:buttons.keySet()){
        	Button button=buttons.get(info);
            if(button.contains(x,y)){
                gameConnection.sendInput((new RegionCommand.Builder())
                							.command(info)
                							.owner(selRegion.ownerNum)
                							.name(selRegion.name)
                							.build());
            }
        }
    }

    public void show(){
        on=true;
    }
    
    public void  hide(){
        on=false;
    }
    
    public boolean isOn(){
    	return on;
    }

}