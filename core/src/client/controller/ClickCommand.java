package client.controller;

import java.awt.geom.Point2D;

public class ClickCommand{
	
	public static final int LEFT_CLICK=1;
	public static final int RIGHT_CLICK=2;
	
	public final int x;
	public final int y;
	public final int clickType;
	public final Point2D.Float pt;
	
	public ClickCommand(int xP,int yP,int cT){
		
		x=xP;
		y=yP;
		clickType=cT;
		pt=new Point2D.Float(x,y);
	}
	
	public Point2D.Float getPoint(){
		return pt;
	}
}