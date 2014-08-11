package client.view

import java.awt.geom.AffineTransform
import java.awt.geom.Point2D
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.awt.Graphics
import java.awt.Shape
import java.awt.Polygon
import engine.general.utility.Line
import engine.general.utility.IntLoc
import java.awt.event.MouseEvent
import com.badlogic.gdx.graphics.OrthographicCamera

/**
 * This class is responsible for handling transformations when the user zooms or scrolls
 */
class Camera( w:Integer, h:Integer,val camera:OrthographicCamera) {

    val viewWidth=w.toDouble
    val viewHeight=w.toDouble

    val minX = 0.0
    val minY = 0.0
    val maxX = 2000
    val maxY = 2000
    val minZoom = 0.05
    val maxZoom = 10
    val startZoom=0.7

    val xScroll = 20.0
    val yScroll = 20.0
    val zoomFactor = 0.05

    var curZoom = startZoom
    var curX = 1000.0
    var curY = 1000.0

    var transform = new AffineTransform()
    val minResourceZoom = 0.8
    val minUpgradeZoom = 2.0

    def transformPolygon(s: Polygon): Shape = transform.createTransformedShape(s)

    def reset() {
        curZoom = startZoom
        curX = 1000.0
        curY = 1000.0
        transform=new AffineTransform()
    }

    /**
     * This class transforms a pont
     */
    def inverseTrans(x:Int,y:Int):Point2D.Double={
        val p=new Point2D.Double(x,y)
        return transform.createInverse().transform(p,null).asInstanceOf[Point2D.Double]
    }
    
    def transformClick(click:MouseEvent){
        
        val x=click.getX
        val y=click.getY
        click.translatePoint(-x,-y)
        var p=new Point2D.Double(x,y)
        val newPoint=transform.createInverse().transform(p,null)
        click.translatePoint(newPoint.getX.toInt,newPoint.getY.toInt)

    }

    def transform(l:Line):Line={
        val x1=transformX(l.xA.toInt)
        val x2=transformX(l.xB.toInt)
        val y1=transformY(l.yA.toInt)
        val y2=transformY(l.yB.toInt)        
        return (new Line.LineBuilder)
            .x1(x1.toShort)
        	.x2(x2.toShort)
        	.y1(y1.toShort)
        	.y2(y2.toShort)
        	.build()
    }

    def moveUp() {
        transform.translate(0,yScroll)
        curY+=yScroll
    }

    def moveDown(){
        transform.translate(0,-yScroll)
        curY-=yScroll
    }

    def moveLeft() {
        transform.translate(xScroll,0)
        curX-=xScroll
    }

    def moveRight() {
        transform.translate(-xScroll,0)
        curX-=xScroll
    }

    def zoomIn() {
        if (curZoom > maxZoom)
            return
        curZoom += zoomFactor
        var newTrans=new AffineTransform()
        newTrans.scale(1+zoomFactor,1+zoomFactor)
        newTrans.translate(-viewWidth*zoomFactor*0.5,-viewHeight*zoomFactor*0.5)
        transform.preConcatenate(newTrans)
    }

    def zoomOut() {
        if (curZoom < minZoom)
            return
        curZoom -= zoomFactor
        var newTrans=new AffineTransform()
        newTrans.scale(1-zoomFactor,1-zoomFactor)
        newTrans.translate(viewWidth*zoomFactor*0.5,viewHeight*zoomFactor*0.5)
        transform.preConcatenate(newTrans)

    }

    def transformX(x: Integer): Integer = {
        val result = transform.transform(new Point2D.Double(x.toDouble, 1.0), null)
        return result.getX.toInt
    }

    def transformY(y: Integer): Integer = {
        val result = transform.transform(new Point2D.Double(1.0, y.toDouble), null)
        return result.getY.toInt
    }

    def showDeaths()=transform.getScaleX>0.6
    
    /**
     * How big should the troop labels be?
     * @return
     */
    def getTroopLabelScale(): Double = {
        if (transform.getScaleX < 0.4) {
            return 0
        }
        if (transform.getScaleX < 1.0) {
            return transform.getScaleX
        }
        return 1.0
    }

    def getSiegeScale(): Double = {
        if (transform.getScaleX < 0.4) {
            return 0
        }
        if (transform.getScaleX < 1.0) {
            return transform.getScaleX
        }
        return 1.0
    }

    def transform(loc:IntLoc)=new IntLoc(transformX(loc.getX()),transformY(loc.getY()))
    def renderBattles():Boolean=transform.getScaleX>0.9
    
    /**
     * Are we zoomed in close enough so that resources of each region can be rendered?
     * @return
     */
    def renderResources(): Boolean = transform.getScaleX >=startZoom

    /**
     * Are we zoomed in close enough so that region upgrades can be rendered?
     * @return
     */
    def renderUpgrades(): Boolean = transform.getScaleX >=startZoom
}
