package client.view;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.Polygon;
import engine.general.utility.Line;
import engine.general.utility.IntLoc;
import java.awt.event.MouseEvent;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * This class is responsible for handling transformations when the user zooms or scrolls
 */
public class Camera{

	
    final double minX = 0.0;
    final double minY = 0.0;
    final double maxX = 2000;
    final double maxY = 2000;
    final double minZoom = 0.05;
    final double maxZoom = 10;
    final double startZoom=0.7;

    final double xScroll = 20.0;
    final double yScroll = 20.0;
    final double zoomFactor = 0.05;
	
	final double viewWidth;
	final double viewHeight;
	final OrthographicCamera camera;

    double curZoom = startZoom;
    double curX = 1000.0;
    double curY = 1000.0;
   
    final double minResourceZoom = 0.8;
    final double minUpgradeZoom = 2.0;
    
    AffineTransform transform = new AffineTransform();

	public Camera(int w,int h,OrthographicCamera c) {
		viewWidth=(double)w;
		viewHeight=(double)w;
		camera=c;
	}

    public Shape transformPolygon(Polygon s){
    	return transform.createTransformedShape(s);
    }

    public void reset() {
        curZoom = startZoom;
        //TODO: Make sure these two values are not hardcoded.
        curX = 1000.0; 
        curY = 1000.0;
        transform=new AffineTransform();
    }

    /**
     * This class transforms a point
     */
    public Point2D.Double inverseTrans(int x,int y){
        Point2D.Double p=new Point2D.Double(x,y);
        try{
            return (Point2D.Double)(transform.createInverse().transform(p,null));     	
        }catch(Exception e){
        	e.printStackTrace();
        	return null;
        }
      
    }
    
    public void transformClick(MouseEvent click){
        
    	try{
            int x=click.getX();
            int y=click.getY();
            click.translatePoint(-x,-y);
            Point2D.Double p=new Point2D.Double(x,y);
            Point2D newPoint=transform.createInverse().transform(p,null);
            click.translatePoint((int)newPoint.getX(),(int)newPoint.getY());    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    public Line transform(Line l){
        int x1=(int)transformX(l.getX1());
        int x2=(int)transformX(l.getX2());
        int y1=(int)transformY(l.getY1());
        int y2=(int)transformY(l.getY2());       
        return (new Line.LineBuilder())
            .x1((short)x1)
        	.x2((short)x2)
        	.y1((short)y1)
        	.y2((short)y2)
        	.build();
    }

    public void moveUp() {
        transform.translate(0,yScroll);
        curY+=yScroll;
    }

    public void moveDown(){
        transform.translate(0,-yScroll);
        curY-=yScroll;
    }

    public void moveLeft() {
        transform.translate(xScroll,0);
        curX-=xScroll;
    }

    public void moveRight() {
        transform.translate(-xScroll,0);
        curX-=xScroll;
    }

    public void zoomIn() {
        if (curZoom > maxZoom){
            return; 	
        }
        curZoom += zoomFactor;
        AffineTransform newTrans=new AffineTransform();
        newTrans.scale(1+zoomFactor,1+zoomFactor);
        newTrans.translate(-viewWidth*zoomFactor*0.5,-viewHeight*zoomFactor*0.5);
        transform.preConcatenate(newTrans);
    }

    public void zoomOut() {
        if (curZoom < minZoom){
            return;   	
        }
        curZoom -= zoomFactor;
        AffineTransform newTrans=new AffineTransform();
        newTrans.scale(1-zoomFactor,1-zoomFactor);
        newTrans.translate(viewWidth*zoomFactor*0.5,viewHeight*zoomFactor*0.5);
        transform.preConcatenate(newTrans);
    }

    public int transformX(int x){
        Point2D result = transform.transform(new Point2D.Double((double)x, 1.0), null);
        return (int)result.getX();
    }

    public int transformY(int y){
        Point2D result = transform.transform(new Point2D.Double(1.0, (double)y), null);
        return (int)result.getY();
    }

    public boolean showDeaths(){
    	return transform.getScaleX()>0.6;
    }
    
    /**
     * How big should the troop labels be?
     * @return
     */
    public double getTroopLabelScale(){
        if (transform.getScaleX() < 0.4) {
            return 0.0;
        }
        if (transform.getScaleX() < 1.0) {
            return transform.getScaleX();
        }
        return 1.0;
    }

    public double getSiegeScale() {
        if (transform.getScaleX() < 0.4) {
            return 0;
        }
        if (transform.getScaleX() < 1.0) {
            return transform.getScaleX();
        }
        return 1.0;
    }

    public IntLoc transform(IntLoc loc){
    	return new IntLoc(transformX(loc.getX()),transformY(loc.getY()));
    }
    
    public boolean renderBattles(){
    	return transform.getScaleX()>0.9;
    }
    
    /**
     * Are we zoomed in close enough so that resources of each region can be rendered?
     * @return
     */
    public boolean renderResources(){
    	return transform.getScaleX() >=startZoom;
    }

    /**
     * Are we zoomed in close enough so that region upgrades can be rendered?
     * @return
     */
    public boolean renderUpgrades(){
    	return transform.getScaleX() >=startZoom;
    }
}
