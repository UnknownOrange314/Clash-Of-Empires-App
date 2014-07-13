package client.view.animation

import java.awt.Color
import java.awt.Graphics
import engine.general.utility.IntLoc
import client.view.Camera

/**
 * This is an animation to represent troop deaths in a region.
 * @param loc Location of troop losses.
 * @param count Number of troops lost.
 */
class LossAnimation(val loc:IntLoc,count:Int){


    private var time=20 //Time that animation will run for.
    private val text="-"+count
    private var x=loc.getX()
    private var y=loc.getY()

    def done():Boolean=time<0

    def update(g:Graphics,c:Camera){
        g.setColor(Color.BLACK)
        x+=2
        y-=2
        if(c.showDeaths()){
            g.drawString(text,c.transformX(x),c.transformY(y))
        }
        time-=1
    }
}
