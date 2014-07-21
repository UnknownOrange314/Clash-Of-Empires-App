package client.view.panels

import com.badlogic.gdx.utils.TimeUtils
import network.Client.GameConnection
import client.controller.{KeyInputListener, MouseInputListener}
import java.awt._
import java.io.File
import java.util
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import server.clientCom.{PlayerStats, GameStateData, RegionState}
import engine.general.utility.IntLoc
import engine.general.utility.Line
import client.view._
import client.view.animation.{SelectAnimation, TroopAnimation, LossAnimation}
import scala.collection.JavaConversions._
import server.model.playerData.Region
import client.view.component.HealthBar
import engine.rts.model.StratMap
import client.controller.InputProcess
import client.ImageManager
import engine.general.view.Display
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import java.awt.Polygon
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import java.awt.geom.Point2D.Double

/**
 * This class represents the display for the game.
 * @param serverConnection The class that has a connection with the rest of the game data.
 */
class GameDisplay(serverConnection: GameConnection) extends Display(serverConnection) {

	var batch=new SpriteBatch()
	var sRender=new ShapeRenderer()
	var drawFont=new BitmapFont();
	
    var imageData=new ImageManager(serverConnection)
    var selectAnimation:SelectAnimation=null//This object represents an animation that is generated when a key press is registered.
    var attackAnimations=new HashSet[TroopAnimation]() //These objects represents animations.
    var deathAnimations=collection.mutable.Set[LossAnimation]()
    var polyMap=new HashMap[Shape,Color]()

    //This will display the score for each nation.
    var scoreDisplay=new ScorePanel(imageData,DRAW_WIDTH-300,0,300,300)
    addComponent(scoreDisplay)

    //This gives options for each region.
    var regionControl=new RegionInfo(0,0,600,350,serverConnection)
    addComponent(regionControl)
    regionControl.show()

    //This shows important game messages.
    var gameLog=new LogPanel(screenW-panelW,600,400,200,serverConnection)
    addComponent(gameLog)

    //This shows information about a player's nation
    var playerInfo=new InfoPanel(screenW-panelW,200,400,500,serverConnection)

    var updateCallback:(PlayerStats)=>Unit=null
    updateCallback=(stats:PlayerStats)=>playerInfo.updateLabels(stats:PlayerStats)
    serverConnection.addDataCallback(updateCallback)

    //The use of magic numbers here should be fixed.
    var miniMap=new Minimap(2000,2000,screenW-panelW,0,400,400)//This is a minimap that shows the whole map
    addComponent(miniMap)
    addComponent(playerInfo) //This must be added last because the minimap overlaps this.

    var gameView = new Camera(DRAW_WIDTH,DRAW_HEIGHT) //This class is responsible for doing transforms when zooming or scrolling.

    //Add a class to deal with input.
    var inProcess=new InputProcess(serverConnection, gameView,this,regionControl,playerInfo)
    setFocusable(true)

    imageData.readImages()

 


    /**
    * This is a private helper method that renders the upgrades for each region.
    * @param upgradeList
    * @param xPos
    * @param yPos
    */
    private def renderUpgrades(upgradeList:util.BitSet,xPos:Integer,yPos:Integer){
        if (gameView.renderUpgrades()) {
            val builtUpgrades = new ArrayList[Integer]
            for (upgradeNum <- 0 until serverConnection.getImprovementCount()) {
                if (upgradeList.get(upgradeNum) == true) {
                    builtUpgrades.add(upgradeNum)
                    var dX=xPos + 20 * (upgradeNum % 2).toFloat
                    var dY=yPos +20+ 20 * (upgradeNum / 2).toFloat
                    var size=(25.0).toFloat
                    batch.begin()
                    batch.draw(imageData.getUpgradeImage(upgradeNum), dX,dY ,size,size)
                    batch.end()
                }
            }
        }
    }

    /**
     * This method renders the troops and sieges for a region.
     * @param regionState The state of the current region.
     * @param xPos The x position of the region center.
     * @param yPos The y position of the region center.
     */
    private def renderRegionState(rNum:Int,color:Color,regionState:RegionState,xPos:Int,yPos:Int){
    	batch.begin()
    	drawFont.draw(batch,regionState.name,xPos-30,yPos-35)
    	batch.end()
        var scale = 18 * gameView.getTroopLabelScale()
        if(scale>0){ //Do not draw troop label if it will be too small
        	batch.begin();
        	batch.draw(imageData.armyImage,xPos-30,yPos-20);
        	drawFont.draw(batch,""+regionState.getOwnerTroopCount(),xPos-5,yPos-5)
        	batch.end();        	
        }
        val h=new HealthBar(xPos,yPos,50,10,Region.MAX_POINTS)
        h.setValue(regionState.getHitPoints)
        h.draw(sRender)
    }

    /**
     * This method draws arrows that represent troop movement.
     * @param rallyPoints Arrows that represent player troop movement.
     * @param conflictLocs Locations where there is a conflict.
     */
    private def renderTroopMovement(rallyPoints:HashSet[Line],conflictLocs:HashMap[IntLoc,Line]){
        
        for (line <- rallyPoints) {
            var trans=gameView.transform(line)
            trans.shrink(0.8)
            trans.drawArrow(sRender)
        }

        //Show areas where there is a battle.
        for((cLoc:IntLoc,cLine)<-conflictLocs){
            val cX=gameView.transformX(cLine.xA).toDouble
            val cY=gameView.transformY(cLine.yA).toDouble
            val p=new java.awt.geom.Point2D.Double(cX,cY)
            for((poly,color)<-polyMap) //This is to select the correct color of the pixel animation
            {
                if(poly.contains(p))
                {
                     attackAnimations.add(new TroopAnimation(color,cLine))
                }
            }
        }

        val remove=new HashSet[TroopAnimation]()
        for ((anim:TroopAnimation)<-attackAnimations){
            anim.update()
            anim.render(sRender,gameView)
            if(anim.finished()){
                remove.add(anim)
            }
        }

        for(loc<-remove){
            attackAnimations.remove(loc)
        }
    }

    def showDeaths(counts:HashMap[IntLoc,Integer]){
        for((loc,loss)<-counts){
            deathAnimations.add(new LossAnimation(loc,loss))
        }
        for(loss<-deathAnimations){
            loss.update(drawFont,batch,gameView)
        }
        deathAnimations=deathAnimations.filter(anim=>(!anim.done())) //Remove completed animations
    }

    /**
     * This method renders the game state for the client
     * @param panelGraphics
     */
    def update() {

        polyMap.clear()
        //Do not draw anything if the server has not sent any data.
        if (serverConnection.nullData()) {
            return
        }
        
        if (imageData.unreadImages == null){
            imageData.readImages()
        }

        
    	batch.begin();
    	batch.draw(imageData.mapBackground,0,0);
    	batch.end();

        //Get the data that the server has sent.
        val gameStateData = serverConnection.getGameState()
        val regionShapes: ArrayList[Polygon] = serverConnection.getRegionBounds()
        val xLabelPositions: ArrayList[Integer] = serverConnection.getXPositions() //The x and y positions for region labels.
        val yLabelPositions: ArrayList[Integer] = serverConnection.getYPositions()
        val rallyPoints: util.HashSet[engine.general.utility.Line] = serverConnection.getRallyPoints()

        //Set the data for the score panel.
        scoreDisplay.setStats(gameStateData)
        //Loop through all the regions and show the relevant data for each region.
        //There may be an issue here because of the ordering of the data.
        var rNum = 0
        for (regionState <- gameStateData.regionStates) {
            
            val ownerNum: Int = regionState.getOwnerNum()
            val regionShape = regionShapes.get(rNum)
            miniMap.render(regionShape,StratMap.playerColors(ownerNum))

            //Determine if we have clicked on a region and must generate an animation.
            if(selectAnimation==null&&inProcess.getClick!=null){
                if (regionShape.contains(new Point(inProcess.getClick.getX,inProcess.getClick.getY))){
                    selectAnimation=new SelectAnimation(regionShape,inProcess.getClick)
                }
            }

            if(selectAnimation!=null){
                if(selectAnimation.mouseClick!=inProcess.getClick){
                    selectAnimation=null
                }
            }

            //Draw the region shape and account for aniation.
            polyMap.put(regionShape,StratMap.playerColors(ownerNum))
            
            //Fill regions.
            if(selectAnimation==null||inProcess.getClick==null){
            	sRender.begin(ShapeType.Line)
            	var col=StratMap.playerColors(ownerNum)
            	var r=col.getRed().toFloat
            	var g=col.getGreen().toFloat
            	var b=col.getBlue().toFloat
            	sRender.setColor(r,g,b,1)
            	var pts=regionShape.xpoints++regionShape.ypoints
            	var trans=pts.map(x=>x.toFloat)
                sRender.polygon(trans)
                sRender.end()
            }
            
         
            //Draw boundaries.
            sRender.begin(ShapeType.Line)
        	var col=StratMap.playerColors(ownerNum)
        	var r=col.getRed().toFloat
        	var g=col.getGreen().toFloat
        	var b=col.getBlue().toFloat
        	sRender.setColor(r,g,b,1)
        	var pts=regionShape.xpoints++regionShape.ypoints
        	var trans=pts.map(x=>x.toFloat)
            sRender.polygon(trans)
            sRender.end()


       
            if(inProcess.containsLeftClick(regionShape)){
                regionControl.drawRegionState(regionState)
            }
      

            //Draw the troop count labels.
            val xPos = gameView.transformX(xLabelPositions.get(rNum))
            val yPos = gameView.transformY(yLabelPositions.get(rNum))

            //We will render resources
            if (gameView.renderResources()){
            	var x=(xPos-30).toFloat
            	var y=(yPos-20).toFloat
            	var sz=(20).toFloat
            	batch.begin()
                batch.draw(imageData.getResourceImage(regionState.getResourceNum()), x,y,sz,sz)
                batch.end()
            }

            //Draw the upgrade list.
            renderUpgrades(regionState.getUpgradeData(),xPos,yPos)

            if(regionState.isCapital){
              	batch.begin();
              	batch.draw(imageData.capitalImage,xPos+10,yPos-30);
              	batch.end();
            }
            
            //Show the troop count.
            renderRegionState(rNum,StratMap.playerColors(ownerNum),regionState,xPos,yPos)
            rNum = rNum + 1


        }

        showDeaths(gameStateData.deathCounts)
        renderTroopMovement(rallyPoints,gameStateData.conflictLocs)
        regionControl.render()
        gameLog.render(serverConnection.myStats.failLog)
    }
}
