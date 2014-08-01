package client.view.panels

import scala.collection.JavaConversions._

import network.client.GameConnection
import java.util.LinkedList
import java.util.ArrayList
import engine.general.view.{drawArea}
import engine.general.view.gui.Label
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Matrix4
import com.mygdx.game.MyGdxGame
import com.badlogic.gdx.graphics.Color
import engine.rts.model.StratMap

import java.awt._
import java.awt.geom.AffineTransform
import java.awt.geom.Point2D
import engine.general.view.drawArea
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.Color
import engine.general.utility.DrawHelper
import server.clientCom.GameStateData
import java.util.ArrayList
/**
 * This class displays a minimap of the current game map.
 * @param mapWidth The model width of the map in pixels.
 * @param mapHeight The model height of the map in pixels.
 * @param x The x coordinate for the top corner of the minimap on the game display.
 * @param y The y coordinate for the top corner of the minimap on the game display.
 * @param w The width of the minimap.
 * @param h The height of the minimap.
 */
class Minimap(mapWidth:Int,mapHeight:Int,x:Int,y:Int,w:Int,h:Int) extends drawArea(x,y,w,h){
    
	println("Location:"+x+":"+y)
    val scale=width/(Math.min(mapWidth,mapHeight).toDouble)
    val drawBounds=new Rectangle(x,y,width.toInt,height.toInt)//This is to make sure that nothing is drawn on top of the minimap.
    
    var viewTop:Point2D=null
    var viewBot:Point2D=null

    /**
     * This method checks to see if a polygon intersects with this region.
     * @param shape
     */
    def intersection(shape:Shape):Boolean=return shape.getBounds.intersects(drawBounds)

    private def renderHex(drawShape:Polygon,drawColor:Color){
    	
	    shapeDraw.begin(ShapeType.Filled)
    	shapeDraw.setColor(Color.WHITE)
    	//shapeDraw.rect(0,0,width,height)
    	shapeDraw.end()
    	//println("Drawing mini hexagon")
    	DrawHelper.fillHexagon(drawShape, shapeDraw, drawColor,0.2f)
    	DrawHelper.drawHexagon(drawShape,shapeDraw,Color.BLACK,0.2f)
    	//println("Done drawing mini")
	}
	
	def render(gameState:GameStateData, regionShapes:ArrayList[Polygon]){
	  
		var rNum=0
		for(regionState<-gameState.regionStates){
			val ownerNum=regionState.getOwnerNum()
			val regionShape=regionShapes.get(rNum)
			renderHex(regionShape,StratMap.playerColors(ownerNum))
			rNum+=1
		}		
	}

    def updateViewLoc(t:Point2D.Double,b:Point2D.Double){
        viewTop=t
        viewBot=b
    }
}
