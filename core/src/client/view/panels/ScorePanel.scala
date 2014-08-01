package client.view.panels

import client.ImageManager
import server.clientCom.GameStateData
import java.util.HashMap
import engine.general.view.drawArea
import engine.general.view.gui.Label
import engine.general.view.gui.IconLabel
import scala.collection.JavaConversions._
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import java.text.NumberFormat
import java.util.Locale
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import javax.imageio.ImageIO
import java.io.File
import network.client.GameConnection
import engine.rts.model.Resource
import java.util._
import java.awt.image.BufferedImage
import scala.collection.JavaConversions._

/**
 * This object shows information about each player
 */
class ScorePanel(val imageData:ImageManager,x:Integer,y:Integer,width:Integer,height:Integer) extends drawArea(x,y,width,height){

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
    
    def loadFlags(){
      
        for( a <- 0 to nationNames.length-1){
            var file=imageData.getFlag(nationNames(a))
            flags.put(nationNames(a),new Texture(file))
        }
                
        var xD=20
        var yD=20
        
        for ((name)<-nationNames){            
        	components.add(new Label(xD,yD,name))
            components.add(new IconLabel(xD,yD-10,flags(name),20))
            var sLabel=new Label(xD+65,yD,"")
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