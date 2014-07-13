package client.view.animation

import java.awt.Color
import java.awt.Graphics2D
import java.awt.Polygon
import java.awt.Shape
import java.awt.event.MouseEvent

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
    def update(g:Graphics2D,p:Shape,region:Polygon,color:Color):Boolean={
        if(regionShape==region){

            //We will produce an animation that makes the color slowly change from white to the region color.
            time+=pi/per
            val amp=0.5*(Math.cos(time)+1)
            val red=(myColor.getRed-color.getRed)*amp+color.getRed
            val green=(myColor.getGreen-color.getGreen)*amp+color.getGreen
            val blue=(myColor.getBlue-color.getBlue)*amp+color.getBlue
            val drawCol=new Color(red.toInt,green.toInt,blue.toInt)

            g.setColor(drawCol)
            g.fill(p)
            return true
        }
        return false
    }
}
