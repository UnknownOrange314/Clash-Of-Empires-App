package client.view.panels

import java.awt.Color
import scala.collection.JavaConversions._
import network.Client.GameConnection
import java.util.LinkedList
import java.util.ArrayList
import engine.general.view.{drawArea}
import engine.general.view.gui.Label
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


class LogPanel(x:Integer,y:Integer,width:Integer,height:Integer,val gameConnection:GameConnection) extends drawArea(x,y,width,height){

    private var log=new LinkedList[String]
    private var top=new Label(50,50,"Messages")

    var render=new ShapeRenderer()
    var batch=new SpriteBatch()
    var font=new BitmapFont()
    
    def render(messages:ArrayList[String]){
        for (message<-messages){
            log.addFirst(message)
        }
          
        render.begin(ShapeType.Filled)
        render.setColor(0,0,0,1);
        render.rect(2,2,400,400)        
        render.end()

        var drawY=90
        var drawX=50
        for (message<-log){
        	font.draw(batch,message,drawX,drawY);
            drawY+=20
            if(drawY>height){
                return
            }
        }
    }
}
