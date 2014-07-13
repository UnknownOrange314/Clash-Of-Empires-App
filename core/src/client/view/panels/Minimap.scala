package client.view.panels

import java.awt._
import java.awt.geom.AffineTransform
import java.awt.geom.Point2D
import engine.general.view.drawArea

/**
 * This class displays a minimap of the current game map.
 * @param mapWidth The model width of the map in pixels.
 * @param mapHeight The model height of the map in pixels.
 * @param x The x coordinate for the top corner of the minimap on the game display.
 * @param y The y coordinate for the top corner of the minimap on the game display.
 * @param w The width of the minimap.
 * @param h The height of the minimap.
 */
class Minimap(mapWidth:Int,mapHeight:Int,x:Int,y:Int,w:Int,h:Int) extends drawArea(x,y,w,h){
    
    val scale=width/(Math.min(mapWidth,mapHeight).toDouble)
    val drawBounds=new Rectangle(x,y,width.toInt,height.toInt)//This is to make sure that nothing is drawn on top of the minimap.
    val transform=new AffineTransform()
    
    transform.scale(scale,scale)

    var viewTop:Point2D=null
    var viewBot:Point2D=null

    /**
     * This method checks to see if a polygon intersects with this region.
     * @param shape
     */
    def intersection(shape:Shape):Boolean=return shape.getBounds.intersects(drawBounds)

    def render(regionShape:Polygon,drawColor:Color){
        val drawShape=transform.createTransformedShape(regionShape)
        imageGraphics.setColor(drawColor)
        imageGraphics.fill(drawShape)
        imageGraphics.setColor(Color.BLACK)
        imageGraphics.draw(drawShape)
        imageGraphics.setColor(Color.WHITE)
    }

    def updateViewLoc(t:Point2D.Double,b:Point2D.Double){
        viewTop=t
        viewBot=b
    }
}
