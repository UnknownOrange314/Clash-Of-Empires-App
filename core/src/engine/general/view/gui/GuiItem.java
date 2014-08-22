package engine.general.view.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class GuiItem{

	protected float xPos;
	protected float yPos;
	
	public GuiItem(int x,int y){
		xPos=(float)x;
		yPos=(float)y;
	}
	
    public abstract void draw(ShapeRenderer render,SpriteBatch batch);
}