package engine.general.view

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import javax.imageio.ImageIO
import java.io.File
import network.client.GameConnection
import engine.rts.model.Resource
import java.util._
import java.awt.image.BufferedImage
import scala.collection.JavaConversions._

import engine.general.utility.IntLoc
import network.client.GameConnection
import java.awt.image.BufferedImage
import java.util.ArrayList
import scala.collection.JavaConversions._
import java.util.Calendar
import java.text.SimpleDateFormat
import java.io.IOException
import java.io.File
import javax.imageio.ImageIO
import com.mygdx.game.MyGdxGame
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
/**
 * This class is used to represent the display
 * @param serverConnection The connection with the server
 */
abstract class Display(var serverConnection:GameConnection){
  
    //These values represent the height and the width of the game display.
    val DRAW_HEIGHT=MyGdxGame.HEIGHT
    val DRAW_WIDTH=MyGdxGame.WIDTH
    
    //These represent the objects used to draw on the screen.
    var batch=new SpriteBatch()
	var sRender=new ShapeRenderer()
	var drawFont=new BitmapFont()
    
    var gameComponents=new ArrayList[drawArea]//These represent the interface components.

    /**
     * This method adds a drawArea object to the display.
     */
    def addComponent(c:drawArea)=gameComponents.add(c)
    
    def drawText(text:String,x:Int,y:Int,offset:IntLoc){
    	drawFont.draw(batch,text,x+offset.getX(),y+offset.getY())
    }
    
    def drawImage(image:Texture,xPos:Int,yPos:Int,offset:IntLoc,size:Int){
        batch.draw(image,xPos+offset.getX(),yPos+offset.getY(),size,size)  
    }
}