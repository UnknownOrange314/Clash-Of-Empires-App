package client.controller

import network.client.GameConnection
import client.view.{Camera}
import java.awt.Polygon
import java.awt.geom.Point2D
import java.awt.geom.Point2D.Double
import scala.collection.Map
import java.awt.Graphics
import java.awt.Point
import java.awt.Shape
import client.view.panels.{InfoPanel, RegionInfo}
import engine.general.utility.IntLoc
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Input.Buttons
import java.awt.event.MouseEvent
import com.badlogic.gdx.Input.Keys
import java.awt.Button
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.MyGdxGame
/**
 * This class processes mouse input.
 * @param serverConnection This represents the connection with the server.
 * @param gameView This represents the camera position.
 */
class InputListener(val serverConnection:GameConnection, val gameView:Camera, val regionInterface:RegionInfo,val playerInfo:InfoPanel) extends InputProcessor{

    val NO_CLICK=new IntLoc(-1,-1)
	var prevClick:ClickCommand=null //This represents the last mouse click in model coordinates.
    
    def getClickPoint():Point2D.Float={
        if(prevClick!=null){
            return prevClick.getPoint()
        }else{
            return null
        }
    }
    private var leftClick:ClickCommand=null
    
    def getLeftClick():IntLoc={
    	var x=leftClick.x.toInt
    	var y=leftClick.y.toInt
        if (leftClick!=null){
            return new IntLoc(x,y)
        }
        else{
            return NO_CLICK
        }
    }

    def containsLeftClick(s:Shape):Boolean={
        if(leftClick==null){
            return false
        }
        val d=new Point2D.Float(leftClick.x,leftClick.y)
        return s.contains(d)
    }
    
    /**
     * This method determines if the last mouse click was on the region defined by the polygon.
     * @param regionBounds The bounds of the region in model coordinates.
     * @return
     */
    def regionClick(regionBounds:Polygon):Boolean={
        if (prevClick==null)
            return false
        return regionBounds.contains(prevClick.getPoint())
    }

    def clearClick(){
        prevClick=null
    }
    
    /**
     * Send a mouse click to the server
     * @param clickEvent
     */
    override def touchDown(x:Int,yPos:Int,pointer:Int,button:Int):Boolean={
        val y=MyGdxGame.HEIGHT-yPos  
    	var event:ClickCommand=null
        if(button==Buttons.LEFT){
        	event=new ClickCommand(x,y,ClickCommand.LEFT_CLICK )
            playerInfo.processClick(x,y)
            if(regionInterface.isOn()){
                regionInterface.processClick(x,y)
                regionInterface.hide()
            }
            else{
                regionInterface.show()
            }
            leftClick=event
            prevClick=null //Clear the previous click.
        }
        
        else{
        	event=new ClickCommand(x,y,ClickCommand.RIGHT_CLICK)
            prevClick=event //Save the click.
        }
        serverConnection.sendInput(event) //Send the click for processing. 
        return true;
    }
    
    override def keyDown(k:Int):Boolean={
        k match {
            case Keys.UP=>gameView.moveUp()
            case Keys.DOWN=>gameView.moveDown()
            case Keys.LEFT=>gameView.moveLeft()
            case Keys.RIGHT=>gameView.moveRight()
            case Keys.Z=>gameView.zoomIn()
            case Keys.X=>gameView.zoomOut()
            case Keys.SPACE=>gameView.reset()
            case _ =>println("Key not supported")
        }
        return true
    }
    
    override def keyUp(k:Int)=false
    override def keyTyped(c:Char)=false
    override def touchUp(x:Int,y:Int,pointer:Int,button:Int)=false
    override def touchDragged(x:Int,y:Int,pt:Int)=false
    override def mouseMoved(x:Int,y:Int)=false
    override def scrolled(a:Int)=false

}