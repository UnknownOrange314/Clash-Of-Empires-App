package engine.general.view.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class IconLabel extends GuiItem{
  
	public static class Builder{
		
		int x=0;
		int y=0;
		Texture image=null;
		int h=0;
		int w=0;
		
		public Builder xPos(int xP){
			x=xP;
			return this;
		}
		
		public Builder yPos(int yP){
			y=yP;
			return this;
		}
		
		public Builder image(Texture img){
			image=img;
			return this;
		}
		
		public Builder height(Integer ht){
			h=ht;
			return this;
		}
		
		public Builder width(Integer wh){
			w=wh;
			return this;
		}
		
		public Builder size(int sz){
			h=sz;
			w=sz;
			return this;
		}
		
		public IconLabel build(){
			return new IconLabel(this);
		}
	} 
	
	final int height;
	final int width;
	final Texture image;
	    
	public IconLabel(Builder b){
		super(b.x,b.y);
	    height=b.h;
		width=b.w;
		image=b.image;
	}

    public void draw(ShapeRenderer render,SpriteBatch batch){
    	batch.begin();
    	batch.draw(image, xPos, yPos, width,height);
    	batch.end();
    }
}
