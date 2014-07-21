package client.view.component

import java.awt.Graphics
import java.awt.Color
import engine.general.view.gui.GuiItem
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType

class HealthBar(x:Int,y:Int, val w:Int,val h:Int,val maxVal:Int) extends GuiItem(x,y){

	var fX=x.toFloat
	var fY=y.toFloat
	var fW=w.toFloat
	var fH=h.toFloat
	
    var curVal=maxVal
    def setValue(c:Int)=curVal=c

    def draw(render:ShapeRenderer){
    
    	render.begin(ShapeType.Filled)
    	render.setColor(1,0,0,1)
    	render.rect(x.toFloat,y.toFloat,w.toFloat,h.toFloat)
    	render.setColor(0,1,0,1)
    	var left=w*curVal/maxVal
    	render.rect(fX,fY,fW,fH)
    	render.end()

    }
}
