package client.view.panels;


import network.client.GameConnection;

import java.util.LinkedList;
import java.util.ArrayList;

import engine.general.view.DrawArea;
import engine.general.view.gui.Label;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.mygdx.game.MyGdxGame;
import com.badlogic.gdx.graphics.Color;

import engine.rts.model.StratMap;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import engine.general.utility.DrawHelper;
import server.clientCom.GameStateData;
import server.clientCom.RegionState;
/**
 * This class displays a minimap of the current game map.
 * @param mapWidth The model width of the map in pixels.
 * @param mapHeight The model height of the map in pixels.
 * @param x The x coordinate for the top corner of the minimap on the game display.
 * @param y The y coordinate for the top corner of the minimap on the game display.
 * @param w The width of the minimap.
 * @param h The height of the minimap.
 */
public class Minimap extends DrawArea{
	
	final double scale;
	final Rectangle drawBounds;
    
    Point2D viewTop=null;
    Point2D viewBot=null;
    
	public Minimap(int mapWidth,int mapHeight,int x,int y,int w,int h){
		super(x,y,w,h);
	    scale=(double)(width/(Math.min(mapWidth,mapHeight)));
	    drawBounds=new Rectangle(x,y,(int)width,(int)height);//This is to make sure that nothing is drawn on top of the minimap.
	}

    /**
     * This method checks to see if a polygon intersects with this region.
     * @param shape
     */
    public boolean intersection(Shape shape){
    	return shape.getBounds().intersects(drawBounds);
    }

    private void renderHex(Polygon drawShape, Color drawColor){   	
	    shapeDraw.begin(ShapeType.Filled);
    	shapeDraw.setColor(Color.WHITE);
    	shapeDraw.end();
    	DrawHelper.fillHexagon(drawShape, shapeDraw, drawColor,0.2f);
    	DrawHelper.drawHexagon(drawShape,shapeDraw,Color.BLACK,0.2f);
	}
	
	public void render(GameStateData gameState, ArrayList<Polygon> regionShapes){
	  
		int rNum=0;
		for(RegionState regionState:gameState.regionStates){
			int ownerNum=regionState.ownerNum;
			Polygon regionShape=regionShapes.get(rNum);
			renderHex(regionShape,StratMap.playerColors[ownerNum]);
			rNum+=1;
		}		
	}

    public void  updateViewLoc(Point2D.Double t,Point2D.Double b){
        viewTop=t;
        viewBot=b;
    }
}