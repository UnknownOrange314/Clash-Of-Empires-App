package client.view.animation

import com.badlogic.gdx.graphics.Color
import java.awt.Graphics
import engine.general.utility.Line
import client.view.Camera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
/**
 * This class renders troop movements.
 */
class TroopAnimation(moveLine:Line){
    
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
     */
    def render(g:ShapeRenderer){
        g.begin(ShapeType.Filled)
        g.setColor(0,0,0,0.5f)
        var size=(10).toFloat
        g.ellipse(curX.toFloat,curY.toFloat,size,size)
        g.end()
    }
}
