package engine.general.utility

import engine.rts.model.StratMap

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.Color

import java.util.Arrays
import java.awt.Shape
import java.awt.Polygon

/**
 * This file contains helper methods for drawing.
 */
object DrawHelper{
  
	def fillHexagon(shape:Polygon,sRender:ShapeRenderer,col:Color){
		fillHexagon(shape,sRender,col,1.0f)
	}  
	
	def fillHexagon(shape:Polygon,sRender:ShapeRenderer,col:Color,scale:Float){
	  
		var cX=0.0f
    	var cY=0.0f
    	var xPts=new Array[Float](6)
    	var yPts=new Array[Float](6)
    	for(i<-0 until 6){
    		xPts(i)=(scale*shape.xpoints(i)).toFloat
    		yPts(i)=(scale*shape.ypoints(i)).toFloat
    		cX=cX+xPts(i)
    		cY=cY+yPts(i)            		
    	}
    	cX=cX/6
    	cY=cY/6
    	sRender.begin(ShapeType.Filled)
    	sRender.setColor(col)
    	for(i<- 0 until 6){
    		sRender.triangle(xPts(i),yPts(i),cX,cY,xPts((i+1)%6),yPts((i+1)%6))
    	}
        sRender.end()
	}  
	
	def drawHexagon(regionShape:Polygon,sRender:ShapeRenderer,col:Color,scale:Float){
            sRender.begin(ShapeType.Line)
        	sRender.setColor(Color.BLACK)
        	var pts=new Array[Float](12)
        	for(i<-0 until 6){
        		pts(2*i)=regionShape.xpoints(i)*scale
        		pts(2*i+1)=regionShape.ypoints(i)*scale
        	}
            sRender.polygon(pts)
            sRender.end()	  
	}
	def drawHexagon(shape:Polygon,sRender:ShapeRenderer,col:Color){
		drawHexagon(shape,sRender,col,1.0f)
	}
}