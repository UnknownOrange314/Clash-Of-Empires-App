package  engine.general.view.gui;

import java.awt.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Label extends GuiItem{

	String text;
	BitmapFont font;
    public Label (int xPos,int yPos,String t,BitmapFont f){
    	super (xPos,yPos);
    	text=t;
    	font=f;
	}
    
    public void draw(ShapeRenderer render,SpriteBatch batch){  	
	  	font.setColor(Color.WHITE);
	  	batch.begin();
    	font.draw(batch,text,xPos,yPos);
    	batch.end();
    }

    public void setText(String s){
        text=s;
    }
}