package engine.general.view;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import client.view.panels.GameDisplay;
import java.util.ArrayList;
import engine.general.view.gui.GuiItem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.MyGdxGame;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;

public class DrawArea {

	public final float width;
	public final float height;
	public final float myX;
	public final float myY;
	
	protected ShapeRenderer shapeDraw=new ShapeRenderer();
	protected SpriteBatch batch=new SpriteBatch();
	protected Matrix4 camera=shapeDraw.getProjectionMatrix();
	
	protected ArrayList<GuiItem> components=new ArrayList<GuiItem>();

	/**
	 *This class represents drawing panels for the game
	 */
	public DrawArea(int xT,int yT,int wT,int hT){
		width=(float)wT;
		height=(float)hT;
		myX=(float)xT;
		myY=(float)yT;
		camera.translate(myX,myY,0);
		shapeDraw.setProjectionMatrix(camera);
		camera=batch.getProjectionMatrix();
		camera.translate(myX,myY,0);
		batch.setProjectionMatrix(camera);
	}
		
   public boolean containsPoint(int x,int y){
	   return x>myX&&x<myX+width&&y>myY&&y<myY+height;
   }
	    
   public void render(){
	   shapeDraw.begin(ShapeType.Filled);
	   shapeDraw.setColor(Color.BLACK);
	   shapeDraw.rect(0,0,width,height);
	   shapeDraw.end();
	   for(GuiItem component:components){
		   component.draw(shapeDraw,batch);
	   }
   }
	
}
