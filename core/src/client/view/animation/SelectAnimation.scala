package client.view.animation

import java.awt.Graphics2D
import java.awt.Polygon
import java.awt.Shape
import java.awt.event.MouseEvent
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * This class represents an animation for clicking on a region.
 * @param regionShape The shape of the region in model coordinates.
 */
class SelectAnimation(val regionShape:Polygon,val mouseClick:MouseEvent){

    val pi=3.14159
    val myColor=Color.WHITE
    var time=0.0
    val per=20.0

    /**
     * This method updates the animation
     * @param g
     * @param p //The shape of the region about to be drawn in transformed coordinates.
     * @param region //The shape of the region about to be drawn in model coordinates
     * @return
     */
    def update(g:ShapeRenderer,p:Shape,region:Polygon,color:Color):Boolean={
        if(regionShape==region){

            //We will produce an animation that makes the color slowly change from white to the region color.
            time+=pi/per
            val amp=0.5*(Math.cos(time)+1)
            val red=(myColor.r-color.r)*amp+color.r
            val green=(myColor.g-color.g)*amp+color.g
            val blue=(myColor.b-color.b)*amp+color.b
            val drawCol=new Color(red.toFloat,green.toFloat,blue.toFloat,1.0f)

            g.setColor(drawCol)
            System.err.println("Area is not being filled")
            return true
        }
        return false
    }
}
