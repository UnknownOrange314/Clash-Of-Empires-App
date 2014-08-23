package engine.general.utility;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;

public class Line implements java.io.Serializable{
	
	public static class LineBuilder{
	  
		private short xA=0;
		private short xB=0;
		private short yA=0;
		private short yB=0;
		public LineBuilder x1(short x){
		    xA=x;
		    return this;
		}
		
		public LineBuilder x2(short x){
		 	xB=x;
		 	return this;
		}
		public LineBuilder y1(short y){
		    yA=y;
		    return this;
		}
		
		public LineBuilder y2(short y){
		    yB=y;
		    return this;
		}
		
		public Line build(){
			return new Line(this);
		}
 	} 

	private short xA;
	private short xB;
	private short yA;
	private short yB;
	
	private Line(Line.LineBuilder build) {
	
		xA=build.xA;
		xB=build.xB;
		yA=build.yA;
		yB=build.yB;
	}
		
    public int width(){
    	return Math.abs(xB-xA);
    }
    
    public int height(){
    	return Math.abs(yB-yA);
    }

    public void drawLine(ShapeRenderer bufferGraphics){
    	bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.line(xA,yA,xB,yB);
    }
	
	/**
	 * This method scales the line by making it smaller,while maintaining the same midpoint.
	 * @param scale
	 */
	public void shrink(double scale){
	    
	    double dX=(double)(xB-xA);
	    double dY=(double)(yB-yA);
	    double mul=(1-scale);
	
	    xA=(short)(xA+dX*mul);
	    xB=(short)(xB-dX*mul);
	    yA=(short)(yA+dY*mul);
	    yB=(short)(yB-dY*mul);

	}
	
	public short getX1(){
		return xA;
	}
	
	public short getX2(){
		return xB;
	}
	
	public short getY1(){
		return yA;
	}
	
	public short getY2(){
		return yB;
	}
	
	public void drawArrow(ShapeRenderer bufferGraphics){
	    
		bufferGraphics.begin(ShapeType.Line);
	    Location finalLoc = new Location(xA,yA);
	    Location startLoc = new Location(xB,yB);
	    drawLine(bufferGraphics);
	    bufferGraphics.end();
	    
	    //Get the midpoint of the line
	    int midX = (xA + xB) / 2;
        int midY= (yA + yB) / 2;

        double distance = finalLoc.distance(startLoc);
        float [] xPts = new float[3];
        float [] yPts = new float[3];
        float xW = (float)(20 * (yB - yA) / distance);
        float yW = (float)(20 * (xB - xA) / distance);

        xPts[0] = midX + xW;
        yPts[0] = midY - yW;
        xPts[1] = midX - xW;
        yPts[1] = midY + yW;
        xPts[2] = (float)(midX + (30*(xB - xA)/distance));
        yPts[2] = (float)(midY + (30*(yB -yA)/distance));
    	
    	bufferGraphics.begin(ShapeType.Filled);
    	bufferGraphics.setColor(Color.BLACK);
    	bufferGraphics.triangle(xPts[0],yPts[0],xPts[1],yPts[1],xPts[2],yPts[2]);
        bufferGraphics.end();
	  
	    }
	
	@Override
	public String toString(){
		return xA+":"+yA+" "+xB+":"+yB;
	}

}
