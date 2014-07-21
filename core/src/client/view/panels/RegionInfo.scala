package client.view.panels

import java.text.NumberFormat
import java.util.Locale
import network.Client.GameConnection
import java.awt.Color
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

class RegionInfo(x:Integer,y:Integer,width:Integer,height:Integer,val gameConnection:GameConnection) extends drawArea(x,y,width,height){

    var on=false
    var selRegion:RegionState=null
    var buttons= Map[String,Button]()

    def drawRegionState(state:RegionState){

        selRegion=state
        components.clear()
        buttons.clear()
        components.add(new Label(50,50,"Improvement list"))

        for (i<-0 to UpgradeDefinition.upgradeList.size()-1){
            
            var y=80+i*30
            var x=20
            var upgrade=UpgradeDefinition.upgradeList.get(i)
            components.add(new Label(x,y,upgrade.name))
            x+=100
            components.add(new Label(x,y,upgrade.costStr()))
            x+=100

            if(state.improvementData.get(i)){
                components.add(new Label(x,y,"Already built"))
            }
            else{
                buttons(upgrade.name)=(new Button(x,y,"Buy "+upgrade.name))
            }
            
            x+=100
            components.add(new Label(x,y,upgrade.getInfo))
        }
        
        buttons("Close")=(new Button(250,50,"Close"))
        components.add(new Label(20,20,"Data for "+state.name))
        components.add(new Label(50,210,"Defense bonus:"+state.getDefenseBonus))
        components.add(new Label(50,230,"Region income: "+state.income))
        components.add(new Label(50,250,"Troops:"+state.getOwnerTroopCount()))
        components.add(new Label(50,270,"Hit points status:"+state.getHitPoints))
        components.add(new Label(50,290,"Troop production"+state.tProd))
        components.add(new Label(50,310,"Attack bonus"+state.getAttackBonus))

        components.add(new Label(50,330,"Population:"+NumberFormat.getNumberInstance(Locale.US).format(state.population)))
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
                gameConnection.sendInput(new RegionCommand(info,selRegion.getOwnerNum(),selRegion.name))
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

    //TODO: Find a way to implement this using OpenGL.
    def drawToPanel(pGraphics:Graphics,disp:Display){
        if (on){
            //super.drawToPanel(pGraphics,disp)
        }
    }
    
    def render(){
        if (on){
           // super.render(imageGraphics)
        }
    }
}
