package client.controller

import javax.swing.event.MouseInputAdapter
import java.awt.event.MouseEvent
import network.client.GameConnection
import client.view.{Camera}
import java.awt.Polygon
import scala.collection.Map
import java.awt.Graphics
import java.awt.Point
import java.awt.Shape
import client.view.panels.{InfoPanel, RegionInfo}
import java.awt.geom.Point2D
import java.awt.Graphics2D
import engine.general.utility.IntLoc

/**
 * This class processes mouse input.
 * @param serverConnection //This represents the connection with the server.
 * @param gameView //This represents the camera position.
 */
class MouseInputListener(val serverConnection:GameConnection, val gameView:Camera, val regionInterface:RegionInfo,val playerInfo:InfoPanel) extends MouseInputAdapter{

    var prevClick:MouseEvent=null //This represents the last mouse click in model coordinates.
    def getClick():MouseEvent=prevClick
    private var leftClick:MouseEvent=null
    
    def getLeftClick():IntLoc={
        if (leftClick!=null){
            new IntLoc(leftClick.getX,leftClick.getY)
        }
        else{
            new IntLoc(-999999,-99999)
        }
    }

    def containsLeftClick(s:Shape):Boolean={
        if(leftClick==null){
            return false
        }
        val d=new Point2D.Double(leftClick.getX,leftClick.getY)
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
        return regionBounds.contains(prevClick.getPoint)
    }

    def clearClick(){
        prevClick=null
    }
    
    /**
     * Send a mouse click to the server
     * @param clickEvent
     */
    override def mouseClicked(clickEvent: MouseEvent){
        
        gameView.transformClick(clickEvent)
        if(clickEvent.getButton==MouseEvent.BUTTON1){
            playerInfo.processClick(clickEvent.getX,clickEvent.getY)
            if(regionInterface.isOn()){
                regionInterface.processClick(clickEvent.getX,clickEvent.getY)
                regionInterface.hide()
            }
            else{
                regionInterface.show()
            }
            leftClick=clickEvent
            prevClick=null //Clear the previous click.
        }
        
        else{
            prevClick=clickEvent //Save the click.
        }
        serverConnection.sendInput(clickEvent) //Send the click for processing.
    }
    override def mousePressed(clickEvent: MouseEvent){}
    override def mouseReleased(clickEvent: MouseEvent){}
}
