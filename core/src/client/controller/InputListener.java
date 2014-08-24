package client.controller;

import network.client.GameConnection;
import client.view.Camera;

import java.awt.Polygon;
import java.awt.geom.Point2D;

import java.awt.Shape;

import client.view.panels.*;
import engine.general.utility.IntLoc;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;
import com.mygdx.game.MyGdxGame;

/**
 * This class processes mouse input.
 * @param serverConnection This represents the connection with the server.
 * @param gameView This represents the camera position.
 */
public class InputListener implements InputProcessor{

	final GameConnection serverConnection;
	final Camera gameView;
	final GameDisplay disp;
	final RegionInfo regionInterface;
	final InfoPanel playerInfo;
    final IntLoc NO_CLICK=new IntLoc(-1,-1);

	ClickCommand prevClick=null; //This represents the last mouse click in model coordinates.
	ClickCommand leftClick=null;
	
	public InputListener (GameConnection con,Camera gView,GameDisplay d,RegionInfo rC,InfoPanel pInfo){
		serverConnection=con;
		gameView=gView;
		disp=d;
		regionInterface=rC;
		playerInfo=pInfo;
	}
    
    public Point2D.Float getClickPoint(){
        if(prevClick!=null){
            return prevClick.getPoint();
        }else{
            return null;
        }
    }
    
    public IntLoc getLeftClick(){
    	int x=(int)leftClick.x;
    	int y=(int)leftClick.y;
        if (leftClick!=null){
            return new IntLoc(x,y);
        }
        else{
            return NO_CLICK;
        }
    }

    public boolean containsLeftClick(Shape s){
        if(leftClick==null){
            return false;
        }
        Point2D.Float d=new Point2D.Float(leftClick.x,leftClick.y);
        return s.contains(d);
    }
    
    /**
     * This method determines if the last mouse click was on the region defined by the polygon.
     * @param regionBounds The bounds of the region in model coordinates.
     * @return
     */
    public boolean regionClick(Polygon regionBounds){
        if (prevClick==null){
        	return false;
        }
        return regionBounds.contains(prevClick.getPoint());
    }

    public void clearClick(){
        prevClick=null;
    }
    
    /**
     * Send a mouse click to the server
     * @param clickEvent
     */
    public boolean touchDown(int x,int yPos,int pointer,int button){
        int y=MyGdxGame.HEIGHT-yPos;  
    	ClickCommand event=null;
        if(button==Buttons.LEFT){
        	event=new ClickCommand(x,y,ClickCommand.LEFT_CLICK);
            playerInfo.processClick(x,y);
            if(regionInterface.isOn()){
                regionInterface.processClick(x,y);
                regionInterface.hide();
            }
            else{
                regionInterface.show();
            }
            leftClick=event;
            prevClick=null; //Clear the previous click.
        }
        
        else{
        	event=new ClickCommand(x,y,ClickCommand.RIGHT_CLICK);
            prevClick=event; //Save the click.
        }
        serverConnection.sendInput(event); //Send the click for processing. 
        return true;
    }
    
    public boolean keyDown(int k){
        return false;
    }
    
    public boolean keyUp(int k){
    	return false;
    }
    
    public boolean  keyTyped(char c){
    	return false;
    }
    
    public boolean  touchUp(int x,int y,int pointer,int button){
    	return false;
    }
    
    public boolean touchDragged(int x,int y,int pt){
    	return false;
    }
    
    public boolean mouseMoved(int x,int y){
    	return false;
    }
    
    public boolean scrolled(int a){
    	return false;
    }

}