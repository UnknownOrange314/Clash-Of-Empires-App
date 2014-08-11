package  engine.general.view.gui

import java.awt.Graphics
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class Label(xPos:Integer,yPos:Integer,var text:String,font:BitmapFont) extends GuiItem(xPos,yPos){
    
  def draw(render:ShapeRenderer,batch:SpriteBatch){  	
	  	font.setColor(Color.WHITE) 	
	  	batch.begin()
    	font.draw(batch,text,xPos,yPos)
    	batch.end()
    }

    def setText(s:String){
        text=s
    }
}