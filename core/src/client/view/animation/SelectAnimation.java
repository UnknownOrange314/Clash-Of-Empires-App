package client.view.animation;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Point2D;

/**
 * This class represents an animation for clicking on a region.
 * @param regionShape The shape of the region in model coordinates.
 * //TODO: Refactor mouseClick parameter.
 */
class SelectAnimation{
	
	final Color myColor=Color.WHITE;
	double time=0.0;
	final double per=20.0;//TOOD: Come up with a better name for this variable.
	
	final Polygon regionShape;
	final Point2D mouseClick;
	
	public SelectAnimation(Polygon rShape,Point2D mC){
		regionShape=rShape;
		mouseClick=mC;
	}
   

    /**
     * This method updates the animation
     * @param g
     * @param p //The shape of the region about to be drawn in transformed coordinates.
     * @param region //The shape of the region about to be drawn in model coordinates
     * @return
     */
    public boolean update(ShapeRenderer g,Shape p,Polygon region,Color color){
        
    	if(regionShape==region){

            //We will produce an animation that makes the color slowly change from white to the region color.
            time+=Math.PI/per;
            double amp=0.5*(Math.cos(time)+1);
            double red=(myColor.r-color.r)*amp+color.r;
            double green=(myColor.g-color.g)*amp+color.g;
            double blue=(myColor.b-color.b)*amp+color.b;
            Color drawCol=new Color((float)red,(float)green,(float)blue,1.0f);
            g.setColor(drawCol);
            return true;
        }
        return false;
    }
}
