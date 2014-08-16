package client.view.animation;


import com.badlogic.gdx.graphics.Color;
import engine.general.utility.IntLoc;
import client.view.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class LossAnimation {
	
	static int START_CYCLES=20;
	static int X_MOV=2;
	static int Y_MOV=2;
	
	private int time;
	private String count;
	private float xPos;
	private float yPos;
	
	public LossAnimation(IntLoc location,int ct){
		time=START_CYCLES;
		count="-"+ct;
		xPos=(float)location.getX();
		yPos=(float)location.getY();
	}
 
	public boolean done(){
		return time<0;
	}
    
	public void update(){
		xPos+=X_MOV;
		yPos+=Y_MOV;
	}

	public void render(BitmapFont font, SpriteBatch batch){
        font.setColor(Color.BLACK);
        batch.begin();
        font.draw(batch,count,xPos,yPos);
        batch.end();
        time-=1;	
	}

}