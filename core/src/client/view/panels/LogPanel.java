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

public class LogPanel extends DrawArea{

	GameConnection gameConnection;
    private LinkedList<String> log=new LinkedList<String>();
    private Label top;
    private BitmapFont font;
    
	public LogPanel(int x, int y,int w,int h,GameConnection gCon){
		super(x,y,w,h);
		gameConnection=gCon;
	    log.add("Testing alert view");
	    
	    font=new BitmapFont();
	    font.setColor(Color.WHITE);
	    top=new Label(50,50,"Messages",font);
	}
    
    public void render(ArrayList<String> messages){
    	
    	shapeDraw.begin(ShapeType.Filled);
    	shapeDraw.setColor(Color.BLACK);
    	shapeDraw.rect(0,0,width,height);
    	shapeDraw.end();
    	
    	batch.begin();
    	font.draw(batch,"Game log",20,20);

        for (String message:messages){
            log.addFirst(message);
        }
          
        int drawY=90;
        int drawX=50;
        
        for(String message:log){
        	font.draw(batch,message,myX,myY);
            drawY+=20;
            if(drawY>height){
                batch.end();
                return;
            }
        }
        batch.end();
    }
}