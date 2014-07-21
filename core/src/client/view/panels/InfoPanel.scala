package client.view.panels

import network.Client.{GameConnection}
import scala.collection.JavaConversions._
import java.awt.Color
import java.awt.{Graphics}
import java.io.File
import java.util.HashMap
import javax.imageio.ImageIO
import server.clientCom.PlayerStats
import server.model.playerData.Population
import engine.general.view.{drawArea}
import engine.rts.model.Resource
import client.controller.ResearchCommand
import engine.general.view.gui.{Label,Button,IconLabel}
import java.text.NumberFormat
import java.util.Locale
import com.badlogic.gdx.graphics.Texture;

/**
 * This class represents the menu for the game.
 * The buttons currently do not have any functionality.
 */
class InfoPanel(x:Integer,y:Integer,width:Integer,height:Integer,val gameConnection:GameConnection) extends drawArea(x,y,width,height){
    
    //This represent the coordinates of the component
    val startX=50
    var xPos=startX
    var yPos=20
    val xSpace=80
    val ySpace=40

    components.add(new Label(xPos,yPos,"Resource"))
    components.add(new Label(xPos+xSpace,yPos,"Stockpile"))
    components.add(new Label(xPos+xSpace*2,yPos,"Income"))

    var resourceAmts = new HashMap[String, Label]() //These display the amount of a resource a player has.
    var incomeAmts=new HashMap[String,Label]()   //These display the income that a player receives for each resource.

    yPos+=ySpace
    for (resource <- Resource.resourceList) {

        val resName = resource.getName()
        val resourceImage = new Texture("images/resources/" + resource.getResourceFile())
        components.add(new IconLabel(xPos,yPos-15,resourceImage,20))
        xPos+=xSpace

        var resourceLabel = new Label(xPos,yPos,resName)
        resourceAmts.put(resName, resourceLabel)
        components.add(resourceLabel)
        xPos+=xSpace

        var incomeLabel=new Label(xPos,yPos,resName)
        incomeAmts.put(resName,incomeLabel)
        components.add(incomeLabel)
        xPos=startX
        yPos+=ySpace
    }

    val upkeepLabel=new Label(xPos,yPos,"Upkeep")
    components.add(upkeepLabel)
    val timeLabel = new Label(xPos,yPos+ySpace,"Time")
    components.add(timeLabel)
    val scoreLabel = new Label(xPos,yPos+2*ySpace,"Score")
    components.add(scoreLabel)

    val h=30
    val w=290
    val resButtons=new Array[Button](4)

    val upMove=new Button(xPos,yPos+3*ySpace,"",w,h)
    components.add(upMove)
    resButtons(0)=upMove

    val upProd=new Button(xPos,yPos+4*ySpace,"",w,h)
    components.add(upProd)
    resButtons(1)=upProd

    val fProd=new Button(xPos,yPos+5*ySpace,"",w,h)
    components.add(fProd)
    resButtons(2)=fProd

    val upTax=new Button(xPos,yPos+6*ySpace,"",w,h)
    components.add(upTax)
    resButtons(3)=upTax

    /**
     * This method is used to regenHP the labels. It is designed to be a callback method.
     * @param statistics
     */
    def updateLabels(statistics: PlayerStats) {


        //Show the time that has passed.
        val gameData=gameConnection.getGameState().passTime
        var seconds=(gameData/Math.pow(10,9)).toInt
        var minutes=seconds/60
        seconds=seconds-minutes*60

        if(seconds>10){
            timeLabel.setText("Time remaining "+minutes+":"+seconds)
        }
        else{
            timeLabel.setText("Time remaining "+minutes+":0"+seconds)
        }

        val score:Population=statistics.getPlayerScore()
        scoreLabel.setText("Total population:" + NumberFormat.getNumberInstance(Locale.US).format(score.total))

        var a=0
        for((item,cost)<-statistics.res){
            resButtons(a).setText(item+": "+"$"+cost)
            a+=1
        }

        for ((resource, amount) <- statistics.getResources()){
            if(resource.equals("coin")){
                resourceAmts.get(resource).setText("$" + amount)
            }
            else{
                resourceAmts.get(resource).setText("" + amount)
            }
        }
        
        for((resource,income)<-statistics.getIncome()){
            if(resource.equals("coin")){
                incomeAmts.get(resource).setText("$"+income.toInt)
            }
            else{
                incomeAmts.get(resource).setText(""+income.toInt)
            }
        }
        
        upkeepLabel.setText("Upkeep: $"+statistics.getUpkeep().toInt)
        var messageString="Message log"
        for (failStr<-statistics.failLog){
            messageString+=failStr
        }
    }

    def processClick(cX:Int,cY:Int){
        val x=cX-myX
        val y=cY-myY
        resButtons.foreach{res=>
            if(res.contains(x.toInt,y.toInt)){
                val command=res.getText.split(":")(0)
                gameConnection.sendInput(new ResearchCommand(command))
            }
        }
    }
    

}
