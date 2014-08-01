package engine.general.view

import scala.collection.JavaConversions._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import client.view.panels.GameDisplay
import java.util.ArrayList
import engine.general.view.gui.GuiItem
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.mygdx.game.MyGdxGame
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.Color
/**
 *This class represents drawing panels for the game
 */
class drawArea(val x:Integer,val y:Integer, val w:Integer,val h:Integer){

	val width=w.toFloat
	val height=h.toFloat
	val myX=x.toFloat
	val myY=y.toFloat
	
	var shapeDraw=new ShapeRenderer()
    var batch=new SpriteBatch()
	var camera=shapeDraw.getProjectionMatrix()
	camera.translate(myX,myY,0)
	shapeDraw.setProjectionMatrix(camera)
	camera=batch.getProjectionMatrix()
	camera.translate(myX,myY,0)
	batch.setProjectionMatrix(camera)
    
	var components=new ArrayList[GuiItem]()
    def containsPoint(x:Int,y:Int)=x>myX&&x<myX+width&&y>myY&&y<myY+height //Is a point within the dimensions of the component.
    
    def render(){
        shapeDraw.begin(ShapeType.Filled)
    	shapeDraw.setColor(Color.BLACK)
    	shapeDraw.rect(0,0,width,height)
    	shapeDraw.end()
    	
        for(component<-components){
            component.draw(shapeDraw,batch)
        }
    }
}