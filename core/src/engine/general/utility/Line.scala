package engine.general.utility

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.Color

object Line{
	class LineBuilder(){
	  
		var xA:Short=0
		var xB:Short=0
		var yA:Short=0
		var yB:Short=0
		def x1(x:Short):LineBuilder={
		    xA=x
		    return this
		}
		def x2(x:Short):LineBuilder={
		 	xB=x
		 	return this
		}
		def y1(y:Short):LineBuilder={
		    yA=y
		    return this
		}
		
		def y2(y:Short):LineBuilder={
		    yB=y
		    return this
		}
		def build():Line={
		  new Line(this)
		}
 	} 
}

/**
 * This class represents lines. The coordinates are stored as shorts because they will be sent over a network.
 * @param xA
 * @param yA
 * @param xB
 * @param yB
 */
class Line(build:Line.LineBuilder) extends java.io.Serializable{

	var xA=build.xA 
	var xB=build.xB
	var yA=build.yA 
	var yB=build.yB
    def width()=Math.abs(xB-xA)
    def height()=Math.abs(yB-yA)

    def drawLine(bufferGraphics:ShapeRenderer){
    	bufferGraphics.setColor(Color.BLACK)
        bufferGraphics.line(xA,yA,xB,yB)
    }

    /**
     * This method scales the line by making it smaller,while maintaining the same midpoint.
     * @param scale
     */
    def shrink(scale:Double){
        
        val dX=(xB-xA).toDouble
        val dY=(yB-yA).toDouble
        val mul=(1-scale)

        xA=(xA+(dX*mul)).toShort
        xB=(xB-dX*mul).toShort
        yA=(yA+dY*mul).toShort
        yB=(yB-dY*mul).toShort

    }
    
    def drawArrow(bufferGraphics:ShapeRenderer){
        
    	bufferGraphics.begin(ShapeType.Line)
        val finalLoc: Location = new Location(xA,yA)
        val startLoc: Location = new Location(xB,yB)
        drawLine(bufferGraphics)
        
        bufferGraphics.end()
        //Get the midpoint of the line
        val midX: Int = (xA + xB) / 2
        val midY: Int = (yA + yB) / 2

        val distance: Double = finalLoc.distance(startLoc)
        val xPts = new Array[Float](3)
        val yPts = new Array[Float](3)
        val xW = (20 * (yB - yA) / distance).asInstanceOf[Float]
        val yW = (20 * (xB - xA) / distance).asInstanceOf[Float]

        xPts(0) = midX + xW
        yPts(0) = midY - yW
        xPts(1) = midX - xW
        yPts(1) = midY + yW
        xPts(2) = midX + (30*(xB - xA)/distance).asInstanceOf[Float]
        yPts(2) = midY + (30*(yB -yA)/distance).asInstanceOf[Float]
    	
    	bufferGraphics.begin(ShapeType.Filled)
    	bufferGraphics.setColor(Color.BLACK)
    	bufferGraphics.triangle(xPts(0),yPts(0),xPts(1),yPts(1),xPts(2),yPts(2))
        bufferGraphics.end()
  
    }
    override def toString():String=xA+":"+yA+" "+xB+":"+yB
}
