package engine.general.utility;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import java.awt.Polygon;

/**
 * This file contains helper methods for drawing.
 */
public class DrawHelper{
  
	public static void fillHexagon(Polygon shape,ShapeRenderer sRender,Color col){
		fillHexagon(shape,sRender,col,1.0f);
	}  
	
	public static void fillHexagon(Polygon shape,ShapeRenderer sRender,Color col,Float scale){
	  
		float cX=0.0f;
    	float cY=0.0f;
    	float[] xPts=new float[6];
    	float[] yPts=new float[6];
    	for(int i=0;i<6;i++){
    		xPts[i]=(float)(scale*shape.xpoints[i]);
    		yPts[i]=(float)(scale*shape.ypoints[i]);
    		cX=cX+xPts[i];
    		cY=cY+yPts[i];            		
    	}
    	cX=cX/6;
    	cY=cY/6;
    	sRender.begin(ShapeType.Filled);
    	sRender.setColor(col);
    	for(int i=0;i<6;i++){
    		sRender.triangle(xPts[i],yPts[i],cX,cY,xPts[(i+1)%6],yPts[(i+1)%6]);
    	}
        sRender.end();
	}  
	
	public static void drawHexagon(Polygon regionShape,ShapeRenderer sRender,Color col,float scale){
            sRender.begin(ShapeType.Line);
        	sRender.setColor(Color.BLACK);
        	float [] pts=new float[12];
        	for(int i=0;i<6;i++){
        		pts[2*i]=regionShape.xpoints[i]*scale;
        		pts[2*i+1]=regionShape.ypoints[i]*scale;
        	}
            sRender.polygon(pts);
            sRender.end();	  
	}
	
	public static void drawHexagon(Polygon shape,ShapeRenderer sRender,Color col){
		drawHexagon(shape,sRender,col,1.0f);
	}
}