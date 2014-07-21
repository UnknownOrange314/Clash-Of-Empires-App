package client.view.panels

import java.awt._
import java.awt.geom.AffineTransform
import java.awt.geom.Point2D
import engine.general.view.drawArea
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
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
    val render=new ShapeRenderer()
    

    var viewTop:Point2D=null
    var viewBot:Point2D=null

    /**
     * This method checks to see if a polygon intersects with this region.
     * @param shape
     */
    def intersection(shape:Shape):Boolean=return shape.getBounds.intersects(drawBounds)

    def render(drawShape:Polygon,drawColor:Color){
        
    	var drawPoints=(drawShape.xpoints++drawShape.ypoints)
    						.map(pt=>pt.toFloat)
    	render.begin(ShapeType.Line)
    	
        var rCol=(drawColor.getRed()/255.0).toFloat
        var gCol=(drawColor.getGreen()/255.0).toFloat
        var bCol=(drawColor.getBlue()/255.0).toFloat   
        
        render.setColor(rCol,gCol,bCol,1)
        render.polygon(drawPoints)
    	render.setColor(0,0,0,1)
        render.end()
    	
    	render.begin(ShapeType.Line)
    	render.setColor(1,1,1,1)
        render.polygon(drawPoints)
        render.end()
    }

    def updateViewLoc(t:Point2D.Double,b:Point2D.Double){
        viewTop=t
        viewBot=b
    }
}
