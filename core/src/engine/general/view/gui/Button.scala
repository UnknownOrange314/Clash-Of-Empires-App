package engine.general.view.gui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.g2d.BitmapFont


object Button{
	class ButtonBuilder(){
		var x:Int=0
		var y:Int=0
		var text:String=""
		var width:Int=90
		var height:Int=25
		var font:BitmapFont=null
		
		def x(xT:Int):ButtonBuilder={
			x=xT
			return this
		}
		
		def y(yT:Int):ButtonBuilder={
			y=yT
			return this
		}
		
		def text(t:String):ButtonBuilder={
			text=t
			return this
		}
		
		def width(w:Int):ButtonBuilder={
			width=w
			return this
		}
		
		def height(h:Int):ButtonBuilder={
			height=h
			return this
		}
		
		def font(f:BitmapFont):ButtonBuilder={
		    font=f
		    return this
		}
		
		def build():Button={
		    return new Button(this)
		}
	}
}
class Button(x:Integer,y:Integer,builder:Button.ButtonBuilder) extends GuiItem(x,y){

	val width=builder.width
	val height=builder.height
	var text=builder.text 
	val font=builder.font 
	
    val myColor=Color.WHITE
    def getWidth=width
    
    var drawRect=new Rectangle2D.Double(xPos.toDouble,(yPos-height/2).toDouble,width.toDouble,height.toDouble)

	def this(builder:Button.ButtonBuilder){
	    this(builder.x,builder.y,builder)
	}

    def draw(render:ShapeRenderer,batch:SpriteBatch){
      
    	render.begin(ShapeType.Filled)
    	render.setColor(Color.GRAY)
    	render.rect(xPos,yPos,drawRect.width.toFloat,drawRect.height.toFloat)
    	render.end()
    	
    	render.begin(ShapeType.Line)
    	render.setColor(0,0,0,1)
    	render.rect(xPos,yPos,drawRect.width.toFloat,drawRect.height.toFloat)
    	render.end()
    	
    	batch.begin()
    	font.setColor(Color.WHITE)
    	font.draw(batch,text,xPos,yPos+height)
    	batch.end()
    }

    def getText=text
    
    def setText(s:String){
        text=s
    }
    
    def contains(x:Int,y:Int):Boolean=drawRect.contains(x.toDouble,y.toDouble)
}