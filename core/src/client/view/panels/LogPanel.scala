package client.view.panels

import java.awt.Color
import scala.collection.JavaConversions._
import network.Client.GameConnection
import java.util.LinkedList
import java.util.ArrayList
import engine.general.view.{drawArea}
import engine.general.view.gui.Label

class LogPanel(x:Integer,y:Integer,width:Integer,height:Integer,val gameConnection:GameConnection) extends drawArea(x,y,width,height){

    private var log=new LinkedList[String]
    private var top=new Label(50,50,"Messages")

    def render(messages:ArrayList[String]){
        for (message<-messages){
            log.addFirst(message)
        }

        clearImage()
        imageGraphics.setColor(Color.BLACK)
        imageGraphics.fillRect(2,2,400,400)
        top.draw(imageGraphics)

        var drawY=90
        var drawX=50
        for (message<-log){
            imageGraphics.drawString(message,drawX,drawY)
            drawY+=20
            if(drawY>height){
                return
            }
        }
    }
}
