package client.view.component

import java.awt.Graphics
import java.awt.Color
import engine.general.view.gui.GuiItem
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class HealthBar(x:Int,y:Int, val w:Int,val h:Int,val maxVal:Int) extends GuiItem(x,y){

	var fX=x.toFloat
	var fY=y.toFloat
	var fW=w.toFloat
	var fH=h.toFloat
	
    var curVal=maxVal
    def setValue(c:Int)=curVal=c

    def setX(x:Int){
	  fX=x.toFloat
	}
	
	def setY(y:Int){
		fY=y.toFloat
	}
	
    def draw(render:ShapeRenderer,batch:SpriteBatch){
    
    	render.begin(ShapeType.Filled)
    	render.setColor(1,0,0,1)
    	render.rect(fX,fY,fW,fH)
    	render.setColor(0,1,0,1)
    	var left=fW*curVal/maxVal
    	render.rect(fX,fY,left,fH)
    	render.end()

    }
}
