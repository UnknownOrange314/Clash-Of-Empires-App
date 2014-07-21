package engine.general.view

import scala.collection.JavaConversions._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import client.view.panels.GameDisplay
import java.util
import engine.general.view.gui.GuiItem
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 *This class represents drawing panels for the game
 */
class drawArea(val x:Integer,val y:Integer, val width:Integer,val height:Integer){

	val myX=x.toFloat;
	val myY=y.toFloat;
	
    var components=new util.ArrayList[GuiItem]()
    def containsPoint(x:Int,y:Int)=x>myX&&x<myX+width&&y>myY&&y<myY+height //Is a point within the dimensions of the component.


    def render(g:ShapeRenderer){
        for(component<-components){
            component.draw(g)
        }
    }
}
