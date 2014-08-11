package client.view.panels

import java.text.NumberFormat
import java.util.Locale
import network.client.GameConnection
import java.util.ArrayList
import server.model.UpgradeDefinition
import collection.mutable.Map
import java.awt.Graphics
import server.clientCom.RegionState
import scala.collection.JavaConversions._
import client.controller.RegionCommand
import engine.general.view.drawArea
import engine.general.view.gui.{Label, GuiItem,Button,IconLabel}
import engine.general.view.Display
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont

object RegionInfo{
	val RINFO_X=0
	val RINFO_Y=0
	val RINFO_WIDTH=600
	val RINFO_HEIGHT=350
	def createRegionPanel(serverConnection:GameConnection):RegionInfo={
	    return new RegionInfo(RINFO_X,RINFO_Y,RINFO_WIDTH,RINFO_HEIGHT,serverConnection)
	}  
}


class RegionInfo(x:Integer,y:Integer,width:Integer,height:Integer,val gameConnection:GameConnection) extends drawArea(x,y,width,height){

    var on=false
    var selRegion:RegionState=null
    var buttons= Map[String,Button]()

    val font=new BitmapFont()
	font.setColor(Color.BLACK)
	
	//TODO: Rewrite method so that the method does not repeatedly create new objects.
    def drawRegionState(state:RegionState){
    	
 
        selRegion=state
        components.clear()
        buttons.clear()
        components.add(new Label(50,50,"Improvement list",font))

        for (i<-0 to UpgradeDefinition.upgradeList.size()-1){
            
            var y=80+i*30
            var x=20
            var upgrade=UpgradeDefinition.upgradeList.get(i)
            components.add(new Label(x,y,upgrade.name,font))
            x+=100
            components.add(new Label(x,y,upgrade.costStr(),font))
            x+=100

            if(state.upgradeData.get(i)){
                components.add(new Label(x,y,"Already built",font))
            }
            else{
                buttons(upgrade.name)=(new Button.ButtonBuilder)
                						.x(x)
                						.y(y)
                						.text("Buy "+upgrade.name)
                						.font(font)
                						.build()
            }
            
            x+=100
            components.add(new Label(x,y,upgrade.getInfo,font))
        }
        
        buttons("Close")=(new Button.ButtonBuilder)
        					.x(250)
        					.y(50)
        					.text("Close")
        					.font(font)
        					.build()
        					
        components.add(new Label(20,20,"Data for "+state.name,font))
        components.add(new Label(50,210,"Defense bonus:"+state.defenseBonus,font))
        components.add(new Label(50,230,"Region income: "+state.income,font))
        components.add(new Label(50,250,"Troops:"+state.getOwnerTroopCount(),font))
        components.add(new Label(50,270,"Hit points status:"+state.hitPoints,font))
        components.add(new Label(50,290,"Troop production"+state.troopProduction,font))
        components.add(new Label(50,310,"Attack bonus"+state.attackBonus,font))

        components.add(new Label(50,330,"Population:"+NumberFormat.getNumberInstance(Locale.US).format(state.population),font))
        for((name,button)<-buttons){
            components.add(button)
        }
    }

    /**
     * This method processes a click. It might be a good idea to refactor this into a superclass method because
     * other drawArea objects have buttons to click and the fact that you need to transform the mouse click from
     * screen coordinates to panel coordinates.
     * @param x x coordinate of click.
     * @param y y coordinate of click
     */
    def processClick(x:Int,y:Int){
        for ((info:String,button:Button)<-buttons){
            if(button.contains(x,y)){
                gameConnection.sendInput(new RegionCommand(info,selRegion.ownerNum,selRegion.name))
            }
        }
    }

    def show(){
        on=true
    }
    
    def hide(){
        on=false
    }
    
    def isOn():Boolean=on

}