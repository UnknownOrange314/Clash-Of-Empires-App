package engine.general.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import javax.imageio.ImageIO;
import java.io.File;
import network.client.GameConnection;
import engine.rts.model.Resource;
import java.awt.image.BufferedImage;

import engine.general.utility.IntLoc;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.io.IOException;

import com.mygdx.game.MyGdxGame;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/**
 * This class is used to represent the display
 * @param serverConnection The connection with the server
 */
public abstract class Display{

	private static final int DRAW_HEIGHT=MyGdxGame.HEIGHT;
	private static final int DRAW_WIDTH=MyGdxGame.WIDTH;
	protected GameConnection serverConnection;
	
    //These represent the objects used to draw on the screen.
    protected SpriteBatch batch=new SpriteBatch();
    protected ShapeRenderer sRender=new ShapeRenderer();
    protected BitmapFont drawFont=new BitmapFont();
    protected ArrayList<DrawArea> gameComponents=new ArrayList<DrawArea>();//These represent the interface components.

	public Display(GameConnection sCon){
		serverConnection=sCon;
	}
	
	public int getWidth(){
		return DRAW_WIDTH;
	}
	
	public int getHeight(){
		return DRAW_HEIGHT;
	}
  
    /**
     * This method adds a drawArea object to the display.
     */
    public void addComponent(DrawArea c){
    	gameComponents.add(c);
    }
    
    public void drawText(String text,int x,int y,IntLoc offset){
    	drawFont.draw(batch,text,x+offset.getX(),y+offset.getY());
    }
    
    public void drawImage(Texture image,int xPos,int yPos,IntLoc offset,int size){
        batch.draw(image,xPos+offset.getX(),yPos+offset.getY(),size,size);  
    }
}