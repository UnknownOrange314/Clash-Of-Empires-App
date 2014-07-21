package client.view.animation

import java.awt.Color
import java.awt.Graphics
import engine.general.utility.Line
import client.view.Camera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
/**
 * This class renders troop movements.
 */
class TroopAnimation(val myCol:Color, moveLine:Line){
    
    private var count=0
    private val maxCount=30.0
    private val h=moveLine.width().toDouble
    private val w=moveLine.height().toDouble
    private var dx=0.5*h/maxCount
    private var dy=0.5*w/maxCount

    /**
     * The following code is to adjust the animation direction for a negative height and/or width.
     */
    if(moveLine.xB-moveLine.xA<0){
        dx=(-1*dx)
    }

    if(moveLine.yB-moveLine.yA<0){
        dy=(-1*dy)
    }
    
    private var curX=moveLine.xA.toDouble
    private var curY=moveLine.yA.toDouble

    def update(){
        count+=1
        curX+=dx
        curY+=dy
    }

    /**
     * Is the animation finished?
     * @return
     */
    def finished()=count>=maxCount

    /**
     * This method renders the animation.
     * @param g The graphics object used.
     * @param c The camera object used for doing transformations when there is zooming or scrolling.
     */
    def render(g:ShapeRenderer,c:Camera){
        g.begin(ShapeType.Line)
        var x=c.transformX(curX.toInt-5).toFloat
        var y=c.transformY(curY.toInt-5).toFloat
        var size=(10).toFloat
        g.ellipse(x,y,size,size)
        g.end()
    }
}
