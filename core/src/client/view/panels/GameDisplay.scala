package client.view.panels

import server.clientCom.{PlayerStats, GameStateData, RegionState}
import server.model.playerData.Region

import client.controller.{KeyInputListener, MouseInputListener}
import client.view.animation.{SelectAnimation, TroopAnimation, LossAnimation}
import client.view.component.HealthBar
import client.controller.InputProcess
import client.ImageManager
import client.view.Camera
import client.view.animation.AnimationManager

import network.client.GameConnection

import engine.general.utility.DrawHelper
import engine.general.view.Display
import engine.general.utility.IntLoc
import engine.general.utility.Line
import engine.rts.model.StratMap

import java.awt.Polygon
import java.awt.geom.Point2D.Double
import java.awt.Point
import java.io.File
import java.awt.Shape
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import java.util.Arrays
import java.util.BitSet

import scala.collection.JavaConversions._

import com.badlogic.gdx.graphics.g2d.PolygonSprite
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Matrix4

/**
 * This class represents the display for the game.
 * @param serverConnection The class that has a connection with the rest of the game data.
 * @param camera The camera for the game view.
 */
class GameDisplay(serverConnection: GameConnection,val camera:OrthographicCamera) extends Display(serverConnection) {

    var imageData=new ImageManager(serverConnection)
	var animations=new AnimationManager(batch,sRender,drawFont)
   
    //This will display the score for each nation.
    var scoreDisplay=InterfaceBuilder.createScorePanel(imageData)
    addComponent(scoreDisplay)
    

    //This gives options for each region.
    var regionControl=InterfaceBuilder.createRegionPanel(serverConnection)
    addComponent(regionControl)
    regionControl.show()

    //This shows important game messages.
    var logPanel=InterfaceBuilder.createLogPanel(serverConnection)
    addComponent(logPanel)

    //This is a callback method to update the labels.
    var updateCallback:(PlayerStats)=>Unit=null
    updateCallback=(stats:PlayerStats)=>playerInfo.updateLabels(stats:PlayerStats)
    serverConnection.addDataCallback(updateCallback)

    var miniMap=InterfaceBuilder.createMinimap()
    addComponent(miniMap)
    
    //This shows information about a player's nation.
    var playerInfo=InterfaceBuilder.createInfoPanel(serverConnection)
    addComponent(playerInfo) //This must be added last because the minimap overlaps this.

    var gameView = new Camera(DRAW_WIDTH,DRAW_HEIGHT,camera) //This class is responsible for doing transforms when zooming or scrolling.

    //Add a class to deal with input.
    var inProcess=new InputProcess(serverConnection, gameView,this,regionControl,playerInfo)
    
    var healthBars=new HashMap[Int,HealthBar]

    /**
    * This is a private helper method that renders the upgrades for each region.
    * @param upgradeList
    * @param xPos
    * @param yPos
    */
    private def renderUpgrades(upgradeList:BitSet,xPos:Integer,yPos:Integer){
        if (gameView.renderUpgrades()) {
            val builtUpgrades = new ArrayList[Integer]
            for (upgradeNum <- 0 until serverConnection.getImprovementCount()) {
                if (upgradeList.get(upgradeNum) == true) {
                    builtUpgrades.add(upgradeNum)
                    var dX=xPos + 20 * (upgradeNum % 2).toFloat
                    var dY=yPos +20+ 20 * (upgradeNum / 2).toFloat
                    var size=(25.0).toFloat
                    batch.draw(imageData.getUpgradeImage(upgradeNum), dX,dY ,size,size)                  
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
    private def renderRegionState(rNum:Int,color:Color,regionState:RegionState,xPos:Int,yPos:Int){
    	batch.begin()
        drawFont.draw(batch,regionState.name,xPos-40,yPos-35)
        var scale = 18 * gameView.getTroopLabelScale()
        if(scale>0){ //Do not draw troop label if it will be too small
            batch.draw(imageData.armyImage,xPos-30,yPos+30,20,20)
        	drawFont.draw(batch,""+regionState.getOwnerTroopCount(),xPos-30,yPos+30)
        }
    	batch.end()
    	var h:HealthBar=null
    	if(!healthBars.containsKey(rNum)){
    		h=new HealthBar(xPos,yPos,50,10,Region.MAX_POINTS)
    	}else{
    	  h=healthBars.get(rNum)
    	}
    	h.setX(xPos)
    	h.setY(yPos)   	
        h.setValue(regionState.getHitPoints)
        h.draw(sRender,batch)
        healthBars.put(rNum,h) 
    }

    /**
     * This method draws arrows that represent troop movement.
     * @param rallyPoints Arrows that represent player troop movement.
     * @param conflictLocs Locations where there is a conflict.
     */
    private def renderTroopMovement(rallyPoints:HashSet[Line],conflictLocs:HashMap[IntLoc,Line],polyMap:HashMap[Shape,Color]){
        
        for (line <- rallyPoints) {
            var trans=gameView.transform(line)
            trans.shrink(0.8)
            trans.drawArrow(sRender)
        }

        //Show areas where there is a battle.
        for((cLoc,cLine)<-conflictLocs){
            val cX=gameView.transformX(cLine.xA).toDouble
            val cY=gameView.transformY(cLine.yA).toDouble
            val p=new java.awt.geom.Point2D.Double(cX,cY)
            for((poly,color)<-polyMap) {
                if(poly.contains(p)){
                	animations.addAttackAnimation(color,cLine)
                }
            }
        }
    }

    def showDeaths(counts:HashMap[IntLoc,Integer]){
        for((loc,loss)<-counts){
            animations.addDeathAnimation(loc,loss)
        }
    }

    /**
     * This method renders the game state.
     */
    def update() {

        var ownerData=new HashMap[Shape,Color]()
        //Do not draw anything if the server has not sent any data.
        if (serverConnection.nullData()) {
            return
        }
        
    	batch.begin();
    	batch.draw(imageData.mapBackground,0,0);
    	batch.end();

        //Get the data that the server has sent.
        val gameStateData = serverConnection.getGameState()
        val regionShapes = serverConnection.getRegionBounds()
        val xLabelPositions = serverConnection.getXPositions() //The x and y positions for region labels.
        val yLabelPositions = serverConnection.getYPositions()
        val rallyPoints = serverConnection.getRallyPoints()
        
        //Set the data for the score panel.
        scoreDisplay.setStats(gameStateData)
        
        var rNum = 0
        for (regionState <- gameStateData.regionStates) {
            	
            val ownerNum = regionState.getOwnerNum()
            val regionShape = regionShapes.get(rNum)
            
            //Determine if we have clicked on a region and must generate an animation.
            animations.updateSelectAnimation(inProcess,regionShape)
 	      
            ownerData.put(regionShape,StratMap.playerColors(ownerNum))
                      
            //This draws a filled hexagon.
            if(animations.hasSelectAnimation||inProcess.getClick==null){ 
            	DrawHelper.fillHexagon(regionShape,sRender,StratMap.playerColors(ownerNum))
            }
            
            //Draw hexagon boundaries.
            DrawHelper.drawHexagon(regionShape, sRender,StratMap.playerColors(ownerNum))
            sRender.begin(ShapeType.Line)
        	var col=StratMap.playerColors(ownerNum)
        	sRender.setColor(Color.BLACK)
        	var pts=new Array[Float](12)
        	for(i<-0 until 6){
        		pts(2*i)=regionShape.xpoints(i)
        		pts(2*i+1)=regionShape.ypoints(i)
        	}
            sRender.polygon(pts)
            sRender.end()

            if(inProcess.containsLeftClick(regionShape)){
                regionControl.drawRegionState(regionState)
            }
      
            //Draw the troop count labels.
            val xPos = gameView.transformX(xLabelPositions.get(rNum))
            val yPos = gameView.transformY(yLabelPositions.get(rNum))
 
            batch.begin()

            //We will render resources
            if (gameView.renderResources()){
            	var x=(xPos-30).toFloat
            	var y=(yPos-20).toFloat
            	var sz=(20).toFloat
                batch.draw(imageData.getResourceImage(regionState.getResourceNum()), x,y,sz,sz)
            }

            //Draw the upgrade list.
            renderUpgrades(regionState.getUpgradeData(),xPos,yPos)
            if(regionState.isCapital){
              	batch.draw(imageData.capitalImage,xPos+10,yPos-30)
            }
           
             batch.end()
            //Show the troop count.
            renderRegionState(rNum,StratMap.playerColors(ownerNum),regionState,xPos,yPos)
            rNum = rNum + 1         
        }
           
        showDeaths(gameStateData.deathCounts)
        renderTroopMovement(rallyPoints,gameStateData.conflictLocs,ownerData)
        animations.updateAnimations()

        regionControl.render()
        miniMap.render(gameStateData, regionShapes)
        logPanel.render(serverConnection.myStats.failLog)
        scoreDisplay.render()
        playerInfo.render()
    }
}
