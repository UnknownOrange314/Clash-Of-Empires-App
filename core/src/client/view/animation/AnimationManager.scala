package client.view.animation

import java.awt.Graphics
import java.awt.Point
import java.awt.Polygon

import client.controller.InputProcess
import engine.general.utility.Line
import engine.general.utility.IntLoc

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType

class AnimationManager(val batch:SpriteBatch,val sRender:ShapeRenderer,val drawFont:BitmapFont) {
	
	var selectAnimation:SelectAnimation=null//This object represents an animation that is generated when a key press is registered.
    var attackAnimations=collection.mutable.Set[TroopAnimation]() //These objects represent animations.
    var deathAnimations=collection.mutable.Set[LossAnimation]() //These object represent animations for a troop losses.

    def createSelectAnimation(inProcess:InputProcess,regionShape:Polygon){
		if(selectAnimation==null&&inProcess.getClick!=null){
			if (regionShape.contains(inProcess.getClick)){
				selectAnimation=new SelectAnimation(regionShape,inProcess.getClick)
				println("Creating select animation")
			}
		}
	    if(selectAnimation!=null){
            if(selectAnimation.mouseClick!=inProcess.getClick){
                selectAnimation=null
                println("Select animation done")
            }
        }
	}
	
	def hasSelectAnimation()=selectAnimation!=null
	
    def addAttackAnimation(cLine:Line){
		attackAnimations.add(new TroopAnimation(cLine))
	}
	
	def addDeathAnimation(loc:IntLoc,count:Int){
		deathAnimations.add(new LossAnimation(loc,count))
	}
		
	def updateAnimations(){
		for(anim<-attackAnimations){
			anim.update()
			anim.render(sRender)
		}	
		for(anim<-deathAnimations){
			anim.update();
			anim.render(drawFont,batch)			
		}		
		attackAnimations=attackAnimations.filter(anim=>(!anim.finished()))
		deathAnimations=deathAnimations.filter(anim=>(!anim.done())) 		
	}
}