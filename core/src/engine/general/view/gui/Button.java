package engine.general.view.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;
import java.awt.geom.Rectangle2D;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class Button extends GuiItem{
	
	public static class ButtonBuilder{
	  
		int x=0;
		int y=0;
		String text="";
		int width=90;
		int height=25;
		BitmapFont font=null;
		
		public ButtonBuilder x(int xT){
			x=xT;
			return this;
		}
		
		public ButtonBuilder y(int yT){
			y=yT;
			return this;
		}
		
		public ButtonBuilder text(String t){
			text=t;
			return this;
		}
		
		public ButtonBuilder width(int w){
			width=w;
			return this;
		}
		
		public ButtonBuilder height(int h){
			height=h;
			return this;
		}
		
		public ButtonBuilder font(BitmapFont f){
		    font=f;
		    return this;
		}
		
		public Button build(){
		    return new Button(this);
		}
	}
	
	final int width;
	final int height;
	private String text;
	final BitmapFont font;
	final Color myColor=Color.WHITE;
	
	private Rectangle2D.Double drawRect;
	
	private Button(ButtonBuilder b){
		super(b.x,b.y);
		width=b.width;
		height=b.height;
		text=b.text;
		font=b.font;
	    drawRect=new Rectangle2D.Double((double)xPos,(double)(yPos-height/2),(double)width,(double)height);
	}
	
	public int getWidth(){
		return width;
	}
    
    public void draw(ShapeRenderer render,SpriteBatch batch){
      
    	render.begin(ShapeType.Filled);
    	render.setColor(Color.GRAY);
    	render.rect(xPos,yPos,(float)drawRect.width,(float)drawRect.height);
    	render.end();
    	
    	render.begin(ShapeType.Line);
    	render.setColor(0,0,0,1);
    	render.rect(xPos,yPos,(float)drawRect.width,(float)drawRect.height);
    	render.end();
    	
    	batch.begin();
    	font.setColor(Color.WHITE);
    	font.draw(batch,text,xPos,yPos+height);
    	batch.end();
    }

    public String getText(){
    	return text;
    }
    
    public void setText(String s){
        text=s;
    }
    
    public boolean contains(int x,int y){
    	return drawRect.contains((double)x,(double)y);
    }
}