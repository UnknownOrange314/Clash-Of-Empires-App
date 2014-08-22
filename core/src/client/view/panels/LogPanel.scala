package client.view.panels

import scala.collection.JavaConversions._

import network.client.GameConnection
import java.util.LinkedList
import java.util.ArrayList
import engine.general.view.{DrawArea}
import engine.general.view.gui.Label
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Matrix4
import com.mygdx.game.MyGdxGame
import com.badlogic.gdx.graphics.Color

class LogPanel(x:Integer,y:Integer,w:Integer,h:Integer,val gameConnection:GameConnection) extends DrawArea(x,y,w,h){

    private var log=new LinkedList[String]
    log.add("Testing alert view")
    private var top=new Label(50,50,"Messages",font)
    var font=new BitmapFont()
    font.setColor(Color.WHITE)
    
    def render(messages:ArrayList[String]){
    	
    	shapeDraw.begin(ShapeType.Filled)
    	shapeDraw.setColor(Color.BLACK)
    	shapeDraw.rect(0,0,width,height)
    	shapeDraw.end()
    	
    	batch.begin()
    	font.draw(batch,"Game log",20,20)

        for (message<-messages){
            log.addFirst(message)
        }
          
    
        var drawY=90
        var drawX=50
        for (message<-log){
        	font.draw(batch,message,myX,myY);
            drawY+=20
            if(drawY>height){
                batch.end()
                return
            }
        }
        batch.end()
    }
}