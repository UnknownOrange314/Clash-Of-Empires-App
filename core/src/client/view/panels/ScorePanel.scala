package client.view.panels

import client.ImageManager
import server.clientCom.GameStateData
import java.util.HashMap
import engine.general.view.DrawArea
import engine.general.view.gui.Label
import engine.general.view.gui.IconLabel
import scala.collection.JavaConversions._
import java.io.File
import java.text.NumberFormat
import java.util.Locale
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import java.io.File
import network.client.GameConnection
import engine.rts.model.Resource
import java.util._
import java.awt.image.BufferedImage
import scala.collection.JavaConversions._
import network.client.GameConnection
import com.badlogic.gdx.graphics.Color
import com.mygdx.game.MyGdxGame
import com.badlogic.gdx.graphics.g2d.BitmapFont

object ScorePanel{
    val SCORE_X=MyGdxGame.WIDTH
	val SCORE_Y=0
	val SCORE_WIDTH=300
	val SCORE_HEIGHT=300
	def createScorePanel(data:ImageManager):ScorePanel={
	    return new ScorePanel(data,SCORE_X,SCORE_Y,SCORE_WIDTH,SCORE_HEIGHT)
	}
}
/**
 * This object shows information about each player
 */
class ScorePanel(val imageData:ImageManager,x:Integer,y:Integer,width:Integer,height:Integer) extends DrawArea(x,y,width,height){

    var nationNames=new Array[String](6)
    nationNames(0)="Turkey"
    nationNames(1)="Russia"
    nationNames(2)="Spain"
    nationNames(3)="England"
    nationNames(4)="Prussia"
    nationNames(5)="Austria"
    
    var stats:HashMap[String,String]=null
    var labels=new HashMap[IconLabel,Label]()
    var flags=new HashMap[String,Texture]()
    
    var scoreList=new HashMap[String,Label]
    
    var font=new BitmapFont()
    font.setColor(Color.WHITE)
    
    def loadFlags(){
      
        for( a <- 0 to nationNames.length-1){
            var file=imageData.getFlag(nationNames(a))
            flags.put(nationNames(a),new Texture(file))
        }
                
        var xD=20
        var yD=20
        
        for ((name)<-nationNames){            
        	components.add(new Label(xD,yD,name,font))
            components.add((new IconLabel.Builder())
            				.xPos(xD)
            				.yPos(yD-10)
            				.image(flags(name))
            				.size(20)
            				.build()
            		      )

            var sLabel=new Label(xD+65,yD,"",font)
        	components.add(sLabel)
	        scoreList.put(name,sLabel)
            xD+=100
            if (x>200){
                xD=20
                yD+=50
            }                      
        }
    }
    
    def setStats(data:GameStateData){
        if (stats==null){
            loadFlags()
        }
        
        var xD=20
        var yD=20
        stats=data.nationInfo

        for ((name,pop:String)<-stats){
            var str:String=pop.asInstanceOf[String]
            var score=NumberFormat.getNumberInstance(Locale.US).format(str.toInt)
            scoreList.get(name).setText(score)
        }
    }
}