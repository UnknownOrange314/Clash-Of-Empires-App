package engine.general.utility

import java.awt.{Color, BasicStroke, Graphics2D, Graphics}

/**
 * This class represents lines. The coordinates are stored as shorts because they will be sent over a network.
 * @param xA
 * @param yA
 * @param xB
 * @param yB
 */
class Line(var xA:Short,var yA:Short,var xB:Short,var yB:Short) extends java.io.Serializable{

    def width()=Math.abs(xB-xA)
    def height()=Math.abs(yB-yA)

    def drawLine(bufferGraphics:Graphics){
        bufferGraphics.asInstanceOf[Graphics2D].setStroke(new BasicStroke(5))
        bufferGraphics.asInstanceOf[Graphics2D].setColor(Color.BLACK)
        bufferGraphics.asInstanceOf[Graphics2D].drawLine(xA,yA,xB,yB)
        bufferGraphics.asInstanceOf[Graphics2D].setStroke(new BasicStroke(1))
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
    
    def drawArrow(bufferGraphics:Graphics){
        
        val finalLoc: Location = new Location(xA,yA)
        val startLoc: Location = new Location(xB,yB)
        drawLine(bufferGraphics)

        //Get the midpoint of the line
        val midX: Int = (xA + xB) / 2
        val midY: Int = (yA + yB) / 2

        val distance: Double = finalLoc.distance(startLoc)
        val xPoints: Array[Int] = new Array[Int](3)
        val yPoints: Array[Int] = new Array[Int](3)
        val xW: Int = (20 * (yB - yA) / distance).asInstanceOf[Int]
        val yW: Int = (20 * (xB - xA) / distance).asInstanceOf[Int]

        xPoints(0) = midX + xW
        yPoints(0) = midY - yW
        xPoints(1) = midX - xW
        yPoints(1) = midY + yW
        xPoints(2) = midX + (30*(xB - xA)/distance).asInstanceOf[Int]
        yPoints(2) = midY + (30*(yB -yA)/distance).asInstanceOf[Int]
        bufferGraphics.fillPolygon(xPoints, yPoints, 3)
    }
    override def toString():String=xA+":"+yA+" "+xB+":"+yB
}
