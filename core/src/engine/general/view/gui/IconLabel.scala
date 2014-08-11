package engine.general.view.gui

import java.awt.{Image, Graphics}
import java.util.Locale
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

object IconLabel{
	class Builder(){
		var x:Int=0
		var y:Int=0
		var image:Texture=null
		var h:Int=0
		var w:Int=0
		
		def xPos(xP:Int):Builder={
			x=xP
			return this
		}
		
		def yPos(yP:Int):Builder={
			y=yP
			return this
		}
		
		def image(img:Texture):Builder={
			image=img
			return this
		}
		
		def height(ht:Integer):Builder={
			h=ht
			return this
		}
		
		def width(wh:Integer):Builder={
			w=wh
			return this
		}
		
		def size(sz:Integer):Builder={
			h=sz
			w=sz
			return this
		}
		
		def build():IconLabel=return new IconLabel(this)
	} 
}

/**
 * This object is a container for storing images.
 */
class IconLabel(xPos:Integer,yPos:Integer,val builder:IconLabel.Builder) extends GuiItem(xPos,yPos){
    
	val height=builder.h
	val width=builder.w
	val image=builder.image 
	
	def this(builder:IconLabel.Builder){
		this(builder.x,builder.y,builder)
	}
	
    val drawX=xPos.toFloat
    val drawY=yPos.toFloat

    def draw(render:ShapeRenderer,batch:SpriteBatch){
    	batch.begin()
    	batch.draw(image, drawX, drawY, width,height)
    	batch.end()
    }
}
