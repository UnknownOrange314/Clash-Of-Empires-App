package client.view.panels;

import network.client.GameConnection;
import java.io.File;
import java.util.HashMap;
import server.clientCom.PlayerStats;
import server.model.playerData.Population;
import engine.general.view.DrawArea;
import engine.rts.model.Resource;
import client.controller.ResearchCommand;
import engine.general.view.gui.*;
import java.text.NumberFormat;
import java.util.Locale;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;

/**
 * This class represents the menu for the game.
 * The buttons currently do not have any functionality.
 */
public class InfoPanel extends DrawArea{
	
	final static int BUTTON_HEIGHT=30;
	final static int BUTTON_WIDTH=220;
	
    //TODO:Come up with more descriptive names for these variables.
    private final int startX=50;
    private int xPos=startX;
    private int yPos=20;
    private int xSpace=80;
    private int ySpace=40;
    private int top=260;

	private GameConnection gameConnection;
    private final BitmapFont font;
	
	Button[] resButtons=new Button[4];
	private HashMap<String,Label> resourceAmts = new HashMap<String, Label>(); //These display the amount of a resource a player has.
	private HashMap<String,Label> incomeAmts=new HashMap<String,Label>();   //These display the income that a player receives for each resource.
	Label upkeepLabel;
	Label timeLabel;
	Label scoreLabel;
	
	public InfoPanel(int x,int y,int w,int h,GameConnection gc){
		
		super (x,y,w,h);
		gameConnection=gc;
		
		font=new BitmapFont();
		font.setColor(Color.BLACK);
		
		upkeepLabel=new Label(xPos,yPos,"Upkeep",font);
		timeLabel = new Label(xPos,yPos+ySpace,"Time",font);
		scoreLabel = new Label(xPos,yPos+2*ySpace,"Score",font);
		components.add(upkeepLabel);
		components.add(timeLabel);
		components.add(scoreLabel);

		
		components.add(new Label(xPos,top-yPos,"Resource",font));
		components.add(new Label(xPos+xSpace,top-yPos,"Stockpile",font));
		components.add(new Label(xPos+xSpace*2,top-yPos,"Income",font));

		yPos+=ySpace;
		
		for (Resource resource:Resource.resourceList) {
		
		    String resName = resource.getName();
		    Texture resourceImage = new Texture("images/resources/" + resource.getResourceFile());
		    components.add((new IconLabel.Builder())
		    				.xPos(x)
		    				.yPos(top-(yPos+15))
		    				.image(resourceImage)
		    				.size(20)
		    				.build()
		    			);
		    xPos+=xSpace;
		
		    Label resourceLabel = new Label(xPos,top-yPos,resName,font);
		    resourceAmts.put(resName, resourceLabel);
		    components.add(resourceLabel);
		    xPos+=xSpace;
		
		    Label incomeLabel=new Label(xPos,top-yPos,resName,font);
		    incomeAmts.put(resName,incomeLabel);
		    components.add(incomeLabel);
		    xPos=startX;
		    yPos+=ySpace;
		}
		
	
		
		Button upMove=(new Button.ButtonBuilder())
					.x(xPos)
					.y(yPos+3*(ySpace-10))
					.width(BUTTON_WIDTH)
					.height(BUTTON_HEIGHT)
					.font(font)
					.build();
		components.add(upMove);
		resButtons[0]=upMove;
		
		Button upProd=(new Button.ButtonBuilder())
					.x(xPos)
					.y(yPos+4*(ySpace-10))
					.width(BUTTON_WIDTH)
					.height(BUTTON_HEIGHT)
					.font(font)
					.build();
		components.add(upProd);
		resButtons[1]=upProd;
		
		Button fProd=(new Button.ButtonBuilder())
					.x(xPos)
					.y(yPos+5*(ySpace-10))
					.width(BUTTON_WIDTH)
					.height(BUTTON_HEIGHT)
					.font(font)
					.build();
		components.add(fProd);
		resButtons[2]=fProd;
		
		Button upTax=(new Button.ButtonBuilder())
					.x(xPos)
					.y(yPos+6*(ySpace-10))
					.width(BUTTON_WIDTH)
					.height(BUTTON_HEIGHT)
					.font(font)
					.build();
		components.add(upTax);
		resButtons[3]=upTax;
	}
	
    /**
     * This method is used to regenHP the labels. It is designed to be a callback method.
     * @param statistics
     */
    public void updateLabels(PlayerStats statistics) {

        //Show the time that has passed.
        double passTime=gameConnection.getGameState().passTime;
        int seconds=(int)(passTime/Math.pow(10,9));
        int minutes=seconds/60;
        seconds=seconds-minutes*60;

        if(seconds>10){
            timeLabel.setText("Time remaining "+minutes+":"+seconds);
        }
        else{
            timeLabel.setText("Time remaining "+minutes+":0"+seconds);
        }

        Population score=statistics.getPlayerScore();
        scoreLabel.setText("Total population:" + NumberFormat.getNumberInstance(Locale.US).format(score.getTotal()));

        int a=0;
        for(String item:statistics.res.keySet()){
        	String cost=statistics.res.get(item);
        	resButtons[a].setText(item+": "+"$"+cost);
            a+=1;
        }

        HashMap<String,Integer> resAmts=statistics.getResources();
        for (String resource:resAmts.keySet()){
        	int amount=resAmts.get(resource);
        	if(resource.equals("coin")){
                resourceAmts.get(resource).setText("$" + amount);
            }
            else{
                resourceAmts.get(resource).setText("" + amount);
            }
        }
        
        HashMap<String,Double> inAmts=statistics.getIncome();
        for(String resource:inAmts.keySet()){
        	double income=inAmts.get(resource);
            if(resource.equals("coin")){
                incomeAmts.get(resource).setText("$"+(int)income);
            }
            else{
                incomeAmts.get(resource).setText(""+(int)income);
            }
        }
        
        upkeepLabel.setText("Upkeep: $"+(int)statistics.getUpkeep());
        String messageString="Message log";
        for (String failStr:statistics.fails){
            messageString+=failStr;
        }
    }

    public void processClick(int cX,int cY){
        float x=cX-myX;
        float y=cY-myY;
        for(Button res: resButtons){
            if(res.contains((int)x,(int)y)){
                String command=res.getText().split(":")[0];
                gameConnection.sendInput(new ResearchCommand(command));
            }
        }
    }
}