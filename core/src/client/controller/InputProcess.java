package client.controller;

import com.badlogic.gdx.Gdx;

import network.client.GameConnection;
import client.view.Camera;
import client.view.panels.*;
import engine.general.utility.IntLoc;

import java.awt.Shape;
import java.awt.geom.Point2D;

public class InputProcess{
	

	InputListener mList;
	
	public InputProcess(GameConnection con,Camera gView,GameDisplay d,RegionInfo rC,InfoPanel pInfo) {

	    mList=new InputListener(con,gView,d, rC,pInfo);
	    Gdx.input.setInputProcessor(mList);

	}
	
    public Point2D.Float getClick(){
    	return mList.getClickPoint();
    }
    
    public boolean containsLeftClick(Shape s){
    	return mList.containsLeftClick(s);
    }
}