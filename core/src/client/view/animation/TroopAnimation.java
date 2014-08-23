package client.view.animation;


import com.badlogic.gdx.graphics.Color;
import java.awt.Graphics;
import engine.general.utility.Line;
import client.view.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
/**
 * This class renders troop movements.
 */
class TroopAnimation{
    
	final Line moveLine;
    private int count=0;   
    private float maxCount=30.0f;
    
    private float h;
    private float w;
    private float dx;
    private float dy;
    
    private float curX;
    private float curY;
    
	public TroopAnimation(Line mL){
		
		moveLine=mL;
	    h=moveLine.width();
	    w=moveLine.height();
	    dx= 0.5f*h/maxCount;
	    dy= 0.5f*w/maxCount;
	    curX=moveLine.getX1();
	    curY=moveLine.getY1();
	    
	    if(moveLine.getX2()-moveLine.getX1()<0){
	        dx=(-1*dx);
	    }

	    if(moveLine.getY2()-moveLine.getY1()<0){
	        dy=(-1*dy);
	    }
	}
	
    public void update(){
        count+=1;
        curX+=dx;
        curY+=dy;
    }

    /**
     * Is the animation finished?
     * @return
     */
    public boolean finished(){
    	return count>=maxCount;
    }
    

    /**
     * This method renders the animation.
     * @param g The graphics object used.
     */
    public void render(ShapeRenderer g){
        g.begin(ShapeType.Filled);
        g.setColor(0,0,0,0.5f);
        float size=10f;
        g.ellipse(curX,curY,size,size);
        g.end();
    }
}
