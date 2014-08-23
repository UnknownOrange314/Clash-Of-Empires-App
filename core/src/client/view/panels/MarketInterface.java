package client.view.panels;

import network.client.GameConnection;

import com.badlogic.gdx.graphics.Color;

import java.text.DecimalFormat;
import java.util.HashMap;

import server.clientCom.PlayerStats;
import client.controller.MarketCommand;
import engine.rts.model.Resource;
import engine.general.view.DrawArea;
import engine.general.view.gui.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * This class represents the interface for using the game market.
 * @param myClient
 */
public class MarketInterface extends DrawArea{
	
	private GameConnection myClient;

    DecimalFormat priceFormat=new DecimalFormat("###.##");
    HashMap<String,Button> sellMap = new HashMap<String, Button>();
    HashMap<String,Button> buyMap = new HashMap<String, Button>();
    BitmapFont font=new BitmapFont();
    
    int xP=20;
    int yP=50;
    int colSize=100;
    int rowSize=30;
    
	public MarketInterface(int x,int y,int w,int h,GameConnection myC){
		super(x,y,w,h);
		
		myClient=myC;
	    components.add(new Label(x,y,"Resource name",font));
	    components.add(new Label(x,y,"Sell",font));
	    components.add(new Label(x,y,"Buy",font));
	    font.setColor(Color.WHITE);

	    xP=20;
	    yP+=rowSize;
	    
	    for (Resource resource:Resource.resourceList) {

	        String resourceName = resource.getName();
	        if (resource.getName() != "coin"){
	            
	        	Texture resourceImage=new Texture("images/resources/"+resource.getResourceFile());
	            components.add((new IconLabel.Builder())
	            				.xPos(x)
	            				.yPos(y)
	            				.image(resourceImage)
	            				.build()
	            				);
	            xP+=colSize;
	            Button sellButton =(new Button.ButtonBuilder())
	            						.x(x)
	            						.y(y+12)
	            						.text("Sell $" + resourceName)
	            						.font(font)
	            						.build();
	            components.add(sellButton);
	            sellMap.put(resourceName, sellButton);
	            xP+=colSize;

	            Button buyButton =(new Button.ButtonBuilder())
	            						.x(x)
	            						.y(y+12)
	            						.text("Buy $" + resourceName)
	            						.font(font)
	            						.build();
	            components.add(buyButton);
	            buyMap.put(resourceName, buyButton);
	            yP+=rowSize;
	            xP=20;
	        }
	    }
    }

    /**
     * This method checks to see if a button was clicked.
     * @param clickX
     * @param clickY
     */
    public void  processClick(int clickX,int clickY){

        float x=clickX-myX;
        float y=clickY-myY;

        for(String name:sellMap.keySet()){
            Button button=sellMap.get(name);
        	if (button.contains(x,y)){
        		
                MarketCommand sellCommand = (new MarketCommand.Builder())
                					.instruction(MarketCommand.SELL)
                					.resource(name)
                					.build();
                
                myClient.sendInput(sellCommand);
                return;
            }
        }
        
        for (String name:buyMap.keySet()){
        	Button button=buyMap.get(name);
            if(button.contains(x,y)){
            	MarketCommand buyCommand = (new MarketCommand.Builder())
                					.instruction(MarketCommand.BUY)
                					.resource(name)
                					.build();
                myClient.sendInput(buyCommand);
                return;
            }
        }
    }

    public void updateMarket(PlayerStats statistics) {
    	
        HashMap<String,Double> resourceCosts=myClient.getGameState().marketPrices;
        for (Resource resource:myClient.getResourceDefs()) {
            String resourceName = resource.getName();
            if (resource.getName() != "coin"){ 
                Button sellButton = sellMap.get(resourceName);
                sellButton.setText("$"+priceFormat.format(resourceCosts.get(resourceName)*5));
                Button buyButton = buyMap.get(resourceName);
                buyButton.setText("$"+priceFormat.format(resourceCosts.get(resourceName)*10));
            }
        }
    }
}