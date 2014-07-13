package client.controller

import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import network.Client.GameConnection
import client.view.Camera
import client.view.panels.GameDisplay

/**
 * This class is responsible for processing keyboard input from the player.
 * @param connection The client object which contains a connection to the server.
 */
class KeyInputListener(val connection:GameConnection,val gameView:Camera,val disp:GameDisplay) extends KeyAdapter{
    override def keyPressed(e: KeyEvent){
        e.getKeyCode match {
            case KeyEvent.VK_UP=>gameView.moveUp()
            case KeyEvent.VK_DOWN=>gameView.moveDown()
            case KeyEvent.VK_LEFT=>gameView.moveLeft()
            case KeyEvent.VK_RIGHT=>gameView.moveRight()
            case KeyEvent.VK_Z=>gameView.zoomIn()
            case KeyEvent.VK_X=>gameView.zoomOut()
            case KeyEvent.VK_SPACE=>gameView.reset()
            case KeyEvent.VK_ENTER=>disp.takeScreenshot()
            case _ =>connection.sendInput(e) //Send key comment to client to process
        }
    }
    override def keyReleased(e: KeyEvent){}
    override def keyTyped(e: KeyEvent){}
}