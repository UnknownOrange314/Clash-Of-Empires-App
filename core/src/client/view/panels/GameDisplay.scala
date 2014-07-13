package client.view.panels

import network.Client.GameConnection
import client.controller.{KeyInputListener, MouseInputListener}
import java.awt._
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import java.io.File
import java.util
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import javax.imageio.ImageIO
import javax.swing.{Timer, JPanel}
import server.clientCom.{PlayerStats, GameStateData, RegionState}
import engine.general.utility.IntLoc
import engine.general.utility.Line
import client.view._
import client.view.animation.{SelectAnimation, PixelAnimation, LossAnimation}
import scala.collection.JavaConversions._
import server.model.playerData.Region
import client.view.component.HealthBar
import engine.rts.model.StratMap
import client.controller.InputProcess
import client.ImageManager
import engine.general.view.Display

/**
 * This class represents the display for the game.
 * @param serverConnection The class that has a connection with the rest of the game data.
 */
class GameDisplay(serverConnection: GameConnection) extends Display(serverConnection) {

    var imageData=new ImageManager(serverConnection)
    var selectAnimation:SelectAnimation=null//This object represents an animation that is generated when a key press is registered.
    var attackAnimations=new HashSet[PixelAnimation]() //These objects represents animations.
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

    //Create a timer to periodically render the game state.
    val PAINT_INTERVAL=40//Number of milliseconds between each frame
    var drawTimer = new Timer(PAINT_INTERVAL, new drawListener())
    drawTimer.start()
    imageData.readImages

    /**
     * This class is an ActionListener that is responsible for rendering data that has been sent by the client.
     */
    class drawListener extends ActionListener {
        def actionPerformed(e: ActionEvent) {
            val top=gameView.inverseTrans(0,0)  //This is buggy.
            val bot=gameView.inverseTrans(DRAW_WIDTH,DRAW_HEIGHT)
            miniMap.updateViewLoc(top,bot)
            repaint()
        }
    }

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
                    bufferGraphics.drawImage(imageData.getUpgradeImage(upgradeNum), xPos + 20 * (upgradeNum % 2), yPos +20+ 20 * (upgradeNum / 2), 25, 25, null)
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
        bufferGraphics.setColor(Color.BLACK)
        var scale = 18 * gameView.getTroopLabelScale()
        bufferGraphics.setFont(new Font("Serif", Font.PLAIN, scale.toInt))

        bufferGraphics.drawString(regionState.name,xPos-30,yPos-35)
        if(scale>0){ //Do not draw troop label if it will be too smal.
            bufferGraphics.drawImage(imageData.armyImage,xPos-30,yPos-20,15,15,null)
            bufferGraphics.drawString("" + regionState.getOwnerTroopCount(), xPos - 5, yPos-5)
        }
        val h=new HealthBar(xPos,yPos,50,10,Region.MAX_POINTS)
        h.setValue(regionState.getHitPoints)
        h.draw(bufferGraphics)
    }

    /**
     * This method draws arrows that represent troop movement.
     * @param rallyPoints Arrows that represent player troop movement.
     * @param conflictLocs Locations where there is a conflict.
     */
    private def renderTroopMovement(rallyPoints:HashSet[Line],conflictLocs:HashMap[IntLoc,Line]){
        
        //Draw lines that represent troop movement.
        bufferGraphics.setColor(Color.BLACK)
        for (line <- rallyPoints) {
            var trans=gameView.transform(line)
            trans.shrink(0.8)
            trans.drawArrow(bufferGraphics)
        }

        //Show areas where there is a battle.
        for((cLoc:IntLoc,cLine)<-conflictLocs){
            val cX=gameView.transformX(cLine.xA).toDouble
            val cY=gameView.transformY(cLine.yA).toDouble
            val p=new Point2D.Double(cX,cY)
            for((poly,color)<-polyMap) //This is to select the correct color of the pixel animation
            {
                if(poly.contains(p))
                {
                     attackAnimations.add(new PixelAnimation(color,cLine))
                }
            }
        }

        val remove=new HashSet[PixelAnimation]()
        for ((anim:PixelAnimation)<-attackAnimations){
            anim.update()
            anim.render(bufferGraphics,gameView)
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
            loss.update(bufferGraphics,gameView)
        }
        deathAnimations=deathAnimations.filter(anim=>(!anim.done())) //Remove completed animations
    }

    /**
     * This method renders the game state for the client
     * @param panelGraphics
     */
    override def paint(panelGraphics: Graphics) {

        polyMap.clear()
        //Do not draw anything if the server has not sent any data.
        if (serverConnection.nullData()) {
            return
        }
        
        if (imageData.unreadImages == null){
            imageData.readImages()
        }

        setupFrame()
        bufferGraphics.drawImage(imageData.mapBackground,0,0,DRAW_WIDTH,DRAW_HEIGHT,null)

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
            var g2d = bufferGraphics.asInstanceOf[Graphics2D]
            val transform=gameView.transformPolygon(regionShape)
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
            polyMap.put(transform,StratMap.playerColors(ownerNum))
            
            if(selectAnimation==null||inProcess.getClick==null){
                g2d.setColor(StratMap.playerColors(ownerNum))
                g2d.fill(transform)
            }
            
            else if(selectAnimation.update(g2d,transform,regionShape,StratMap.playerColors(ownerNum))==false) {
               g2d.setColor(StratMap.playerColors(ownerNum))
               g2d.fill(transform)
            }

            g2d.setColor(Color.black)
            g2d.draw(transform)

            if(inProcess.containsLeftClick(transform)){
                regionControl.drawRegionState(regionState)
            }

            //Draw the troop count labels.
            val xPos = gameView.transformX(xLabelPositions.get(rNum))
            val yPos = gameView.transformY(yLabelPositions.get(rNum))

            //We will render resources
            if (gameView.renderResources()){
                bufferGraphics.drawImage(imageData.getResourceImage(regionState.getResourceNum()), xPos-30, yPos+10, 20, 20, null)
            }

            //Draw the upgrade list.
            renderUpgrades(regionState.getUpgradeData(),xPos,yPos)

            if(regionState.isCapital){
                bufferGraphics.drawImage(imageData.capitalImage,xPos+10,yPos-30,20,20,null)
            }
            
            //Show the troop count.
            renderRegionState(rNum,StratMap.playerColors(ownerNum),regionState,xPos,yPos)
            rNum = rNum + 1

            //Testing hack
            if(regionState.getTerrain.equals("water")){
                g2d.setColor(Color.BLUE)
                g2d.fill(transform)
            }
        }

        showDeaths(gameStateData.deathCounts)
        renderTroopMovement(rallyPoints,gameStateData.conflictLocs)
        regionControl.render()
        gameLog.render(serverConnection.myStats.failLog)
        panelGraphics.drawImage(offscreen, 0, 0, this)
        drawComponents(panelGraphics)
    }
}
