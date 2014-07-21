package  engine.general.view.gui

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

abstract class GuiItem(val x:Integer,val y:Integer){
  
	val font=new BitmapFont()
	val batch=new SpriteBatch()	
	font.setColor(Color.BLACK)
	
	val xPos=x.toFloat
	val yPos=y.toFloat
	
    def draw(render:ShapeRenderer)
}