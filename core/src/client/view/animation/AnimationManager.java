package client.view.animation;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.HashSet;
import java.util.Iterator;

import client.controller.InputProcess;
import engine.general.utility.Line;
import engine.general.utility.IntLoc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class AnimationManager{
	

	private final SpriteBatch batch;
	private final ShapeRenderer sRender;
	private final BitmapFont drawFont;
	
	SelectAnimation selectAnimation=null;//This object represents an animation that is generated when a key press is registered.
    HashSet<TroopAnimation> attackAnimations=new HashSet<TroopAnimation>(); //These objects represent animations.
    HashSet<LossAnimation> deathAnimations=new HashSet<LossAnimation>(); //These object represent animations for a troop losses.

	public AnimationManager(SpriteBatch bt,ShapeRenderer sr,BitmapFont df) {
		batch=bt;
		sRender=sr;
		drawFont=df;
	}

    public void createSelectAnimation(InputProcess inProcess,Polygon regionShape){
		if(selectAnimation==null&&inProcess.getClick()!=null){
			if (regionShape.contains(inProcess.getClick())){
				selectAnimation=new SelectAnimation(regionShape,inProcess.getClick());
			}
		}
	    if(selectAnimation!=null){
            if(selectAnimation.mouseClick!=inProcess.getClick()){
                selectAnimation=null;
            }
        }
	}
	
	public boolean hasSelectAnimation(){
		return selectAnimation!=null;
	}
	
    public void addAttackAnimation(Line cLine){
		attackAnimations.add(new TroopAnimation(cLine));
	}
	
	public void addDeathAnimation(IntLoc loc,int count){
		deathAnimations.add(new LossAnimation(loc,count));
	}
		
	public void updateAnimations(){
		for(TroopAnimation anim:attackAnimations){
			anim.update();
			anim.render(sRender);
		}	
		for(LossAnimation anim:deathAnimations){
			anim.update();
			anim.render(drawFont,batch);			
		}		
		
		Iterator<TroopAnimation> iter=attackAnimations.iterator();
		while(iter.hasNext()){
			if(iter.next().finished()){
				iter.remove();
			}
		}
		
		Iterator<LossAnimation> iter2=deathAnimations.iterator();
		while(iter2.hasNext()){
			if(iter2.next().done()){
				iter2.remove();
			}
		}		
	}
}