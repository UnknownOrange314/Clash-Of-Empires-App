package client.view.animation

import com.badlogic.gdx.graphics.Color;
import engine.general.utility.IntLoc
import client.view.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.g2d.BitmapFont

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

    def update(){
    	x+=2
    	y+=2
    }
    
    def render(font:BitmapFont,batch:SpriteBatch){
        font.setColor(Color.BLACK)
        batch.begin()
        font.draw(batch,text,x.toFloat,y.toFloat)
        batch.end();
        time-=1
    }
}