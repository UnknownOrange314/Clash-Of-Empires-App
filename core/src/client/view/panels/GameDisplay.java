package client.view.panels;

import server.clientCom.*;
import server.model.playerData.Region;
import client.view.animation.*;
import client.view.component.HealthBar;
import client.controller.InputProcess;
import client.ImageManager;
import client.view.Camera;
import client.view.animation.AnimationManager;
import network.DataUpdater;
import network.client.GameConnection;
import engine.general.utility.DrawHelper;
import engine.general.view.Display;
import engine.general.utility.IntLoc;
import engine.general.utility.Line;
import engine.rts.model.StratMap;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.Point;
import java.io.File;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.BitSet;

import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;

/**
 * This class represents the display for the game.
 * @param serverConnection The class that has a connection with the rest of the game data.
 * @param camera The camera for the game view.
 */
public class GameDisplay extends Display{

	final OrthographicCamera camera;
	Camera gameView=null;

	ImageManager imageData;
	AnimationManager animations;
	InputProcess inProcess;
	
	ScorePanel scoreDisplay;
	RegionInfo regionControl;
	Minimap miniMap;
	LogPanel logPanel;
	InfoPanel playerInfo;
	
	HashMap<Integer,HealthBar> healthBars=new HashMap<Integer,HealthBar>();

	public GameDisplay(GameConnection serverConnection,OrthographicCamera cam){
		super(serverConnection);

		imageData=new ImageManager(serverConnection);
		animations=new AnimationManager(batch,sRender,drawFont);
   
		//This will display the score for each nation.
		scoreDisplay=InterfaceBuilder.createScorePanel(imageData);
		addComponent(scoreDisplay);
		
		//This gives options for each region.
		regionControl=InterfaceBuilder.createRegionPanel(serverConnection);
		addComponent(regionControl);
		
		//This shows important game messages.
		logPanel=InterfaceBuilder.createLogPanel(serverConnection);
		addComponent(logPanel);
		
		miniMap=InterfaceBuilder.createMinimap();
		addComponent(miniMap);
		
		//This shows information about a player's nation.
		playerInfo=InterfaceBuilder.createInfoPanel(serverConnection);
		addComponent(playerInfo); //This must be added last because the minimap overlaps this.
		
		//This is a callback method to update the labels.
		DataUpdater updateCallback=new DataUpdater(playerInfo);
		serverConnection.addDataCallback(updateCallback);
		
		camera=cam;
		gameView = new Camera(getWidth(),getHeight(),camera); //This class is responsible for doing transforms when zooming or scrolling.
		
		//Add a class to deal with input.
		inProcess=new InputProcess(serverConnection, gameView,this,regionControl,playerInfo);		
	}
		

    /**
    * This is a private helper method that renders the upgrades for each region.
    * @param upgradeList
    * @param xPos
    * @param yPos
    */
    private void renderUpgrades(BitSet upgradeList,int xPos,int yPos){
        if (gameView.renderUpgrades()) {
            for (int upgradeNum=0;upgradeNum<serverConnection.getImprovementCount();upgradeNum++) {
                if (upgradeList.get(upgradeNum) == true) {
                	int SIZE=25;
                    float dX=xPos +(float)(SIZE * (upgradeNum % 2));
                    float dY=yPos +(float)(SIZE+ SIZE * (upgradeNum / 2));
                    float size=(float)SIZE;
                    batch.draw(imageData.getUpgradeImage(upgradeNum), dX,dY ,(float)SIZE,(float)SIZE);                  
                }
            }
        }
    }


    
    /**
     * This method renders information about a region.
     * @param regionState The state of the current region.
     * @param xPos The x position of the region center.
     * @param yPos The y position of the region center.
     */
    private void renderRegionState(int rNum,Color color,RegionState regionState,int xPos,int yPos){
    	final IntLoc NAME_OFFSET=new IntLoc(-40,-35);
    	final IntLoc TROOP_OFFSET=new IntLoc(-30,30);
    	final IntLoc NUM_OFFSET=new IntLoc(-30,30);
    	int TROOP_SIZE=20;
        batch.begin();
    	drawText(regionState.name,xPos,yPos,NAME_OFFSET);
        double scale = TROOP_SIZE * gameView.getTroopLabelScale();
        if(scale>0){ //Do not draw troop label if it will be too small
            drawImage(imageData.armyImage,xPos,yPos,TROOP_OFFSET,TROOP_SIZE);
            drawText(""+regionState.getOwnerTroopCount(),xPos,yPos,NUM_OFFSET);
        }
    	batch.end();
    	HealthBar h=null;
    	if(!healthBars.containsKey(rNum)){
    		h=HealthBar.createHealthBar(xPos,yPos,Region.MAX_POINTS);
    	}else{
    	  h=healthBars.get(rNum);
    	}
    	h.setX(xPos);
    	h.setY(yPos);   	
        h.setValue(regionState.hitPoints);
        h.draw(sRender,batch);
        healthBars.put(rNum,h);
    }

    /**
     * This method draws arrows that represent troop movement.
     * @param rallyPoints Arrows that represent player troop movement.
     * @param conflictLocs Locations where there is a conflict.
     */
    private void renderTroopMovement(HashSet<Line> rallyPoints,HashMap<IntLoc,Line> conflictLocs,ArrayList<Polygon> polyMap){
        
        double ARROW_SCALE=0.8;
        for (Line line:rallyPoints) {
            Line trans=gameView.transform(line);
            trans.shrink(ARROW_SCALE);
            trans.drawArrow(sRender);
        }

        //Show areas where there is a battle.
        for(IntLoc cLoc:conflictLocs.keySet()){
        	Line cLine=conflictLocs.get(cLoc);
            double cX=gameView.transformX(cLine.getX1());
            double cY=gameView.transformY(cLine.getY1());
            Point2D.Double p=new Point2D.Double(cX,cY);
            for(Polygon poly:polyMap) {
                if(poly.contains(p)){
                	animations.addAttackAnimation(cLine);
                }
            }
        }
    }

    public void showDeaths(HashMap<IntLoc,Integer> counts){
        for(IntLoc loc:counts.keySet()){
        	int loss=counts.get(loc);
            animations.addDeathAnimation(loc,loss);
        }
    }

    /**
     * This method renders the game state.
     */
    public void update() {

        //Do not draw anything if the server has not sent any data.
        if (serverConnection.nullData()) {
            return;
        }
        
    	batch.begin();
    	batch.draw(imageData.mapBackground,0,0);
    	batch.end();

        //Get the data that the server has sent.
        GameStateData gameStateData = serverConnection.getGameState();
        ArrayList<Polygon> regionShapes = serverConnection.getRegionBounds();
        ArrayList<Integer> xLabelPositions = serverConnection.getXPositions(); //The x and y positions for region labels.
        ArrayList<Integer> yLabelPositions = serverConnection.getYPositions();
        HashSet<Line> rallyPoints = serverConnection.getRallyPoints();
        
        //Set the data for the score panel.
        scoreDisplay.setStats(gameStateData);
        
        int rNum = 0;
        for (RegionState regionState:gameStateData.regionStates) {
            	
            int ownerNum = regionState.ownerNum;
            Polygon regionShape = regionShapes.get(rNum);
            
            //Determine if we have clicked on a region and must generate an animation.
            animations.createSelectAnimation(inProcess,regionShape);
 	      
                      
            //This draws a filled hexagon.
            if(animations.hasSelectAnimation()||inProcess.getClick()==null){ 
            	DrawHelper.fillHexagon(regionShape,sRender,StratMap.playerColors[ownerNum]);
            }
            
            //Draw hexagon boundaries.
            int HEX_SIDES=6;
            DrawHelper.drawHexagon(regionShape, sRender,StratMap.playerColors[ownerNum]);
            
            //TODO: See if the following lines of code for drawing a hexagon are necessary.
            sRender.begin(ShapeType.Line);
        	Color col=StratMap.playerColors[ownerNum];
        	sRender.setColor(Color.BLACK);
        	float [] pts=new float[2*HEX_SIDES];
        	for(int i=0;i<HEX_SIDES;i++){
        		pts[2*i]=regionShape.xpoints[i];
        		pts[2*i+1]=regionShape.ypoints[i];
        	}
            sRender.polygon(pts);
            sRender.end();

            if(inProcess.containsLeftClick(regionShape)){
                regionControl.drawRegionState(regionState);
            }
      
            //Draw the troop count labels.
            int xPos = gameView.transformX(xLabelPositions.get(rNum));
            int yPos = gameView.transformY(yLabelPositions.get(rNum));
 
            batch.begin();

            //We will render resources
            if (gameView.renderResources()){
            	int size=20;
            	IntLoc offset=new IntLoc(-30,-20);
            	drawImage(imageData.getResourceImage(regionState.resourceNum),xPos,yPos,offset,size);
            }

            //Draw the upgrade list.
            renderUpgrades(regionState.upgradeData,xPos,yPos);
            if(regionState.isCapital){
            	int size=20;
            	IntLoc offset=new IntLoc(10,-30);
            	drawImage(imageData.capitalImage,xPos,yPos,offset,size);
            }
           
            batch.end();
            
            //Show the troop count.
            renderRegionState(rNum,StratMap.playerColors[ownerNum],regionState,xPos,yPos);
            rNum = rNum + 1;         
        }
          
        showDeaths(gameStateData.deathCounts);
        renderTroopMovement(rallyPoints,gameStateData.conflictLocs,regionShapes);
        animations.updateAnimations();
        if(regionControl.isOn()){
        	regionControl.render();    
        }
        miniMap.render(gameStateData, regionShapes);
        logPanel.render(serverConnection.myStats.fails);
        scoreDisplay.render();
        playerInfo.render();      
    }
}