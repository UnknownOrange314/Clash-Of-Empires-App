package client.view.component;

import java.awt.Graphics;
import java.awt.Color;
import engine.general.view.gui.GuiItem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HealthBar extends GuiItem{
	
	final static int HEIGHT=10;
	final static int WIDTH=50;
	
	public static HealthBar createHealthBar(int x,int y,int maxVal){
		return new HealthBar(x,y,WIDTH,HEIGHT,maxVal);
	}

	float fX;
	float fY;
	float fW;
	float fH;
	float curVal;
	float maxVal;
	
	private HealthBar(int x,int y,int w,int h,int max){
		super(x,y);
	
		fX=x;
		fY=y;
		fW=w;
		fH=h;
	    curVal=max;
	    maxVal=max;
	}
    
    public void setValue(int c){
    	curVal=c;
    }

    public void setX(int x){
    	fX=(float)x;
	}
	
	public void setY(int y){
		fY=(float)y;
	}
	
    public void draw(ShapeRenderer render,SpriteBatch batch){
    
    	render.begin(ShapeType.Filled);
    	render.setColor(1,0,0,1);
    	render.rect(fX,fY,fW,fH);
    	render.setColor(0,1,0,1);
    	double left=fW*curVal/maxVal;
    	render.rect(fX,fY,(float)left,fH);
    	render.end();
    }
}