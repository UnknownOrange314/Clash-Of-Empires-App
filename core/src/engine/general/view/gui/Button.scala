package engine.general.view.gui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class Button( x:Integer,y:Integer, var text:String,val width:Int,val height:Int) extends GuiItem(x,y){

    val myColor=Color.WHITE
    def getWidth=width
    
    var drawRect=new Rectangle2D.Double(xPos.toDouble,(yPos-height/2).toDouble,width.toDouble,height.toDouble)

    def this(xPos:Integer,yPos:Integer,text:String){
        this(xPos,yPos,text,90,25)
    }

    def draw(render:ShapeRenderer){
      
      
    	render.begin(ShapeType.Filled)
    	render.setColor(1,1,1,1)
    	render.rect(xPos,yPos,drawRect.width.toFloat,drawRect.height.toFloat)
    	render.end()
    	
    	render.begin(ShapeType.Line)
    	render.setColor(0,0,0,0)
    	render.rect(xPos,yPos,drawRect.width.toFloat,drawRect.height.toFloat)
    	render.end()
    	
    	batch.begin()
    	font.draw(batch,text,xPos,yPos)
    	batch.end()

    }

    def getText=text
    
    def setText(s:String){
        text=s
    }
    def contains(x:Int,y:Int):Boolean=drawRect.contains(x.toDouble,y.toDouble)
}
