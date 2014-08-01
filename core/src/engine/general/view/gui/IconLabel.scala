package engine.general.view.gui

import java.awt.{Image, Graphics}
import java.util.Locale
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * This object is a container for storing images.
 */
class IconLabel(xPos:Integer,yPos:Integer,val image:Texture,val sz:Integer) extends GuiItem(xPos,yPos){
    val drawX=xPos.toFloat
    val drawY=yPos.toFloat
    val size=sz.toFloat
	def draw(render:ShapeRenderer,batch:SpriteBatch){
    	batch.begin()
    	batch.draw(image, drawX, drawY, size, size)
    	batch.end()
    }
}
