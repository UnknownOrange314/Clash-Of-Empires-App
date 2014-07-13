package client.view.panels

import network.Client.GameConnection
import java.awt.Color
import java.io.File
import java.text.DecimalFormat
import java.util
import java.util.HashMap
import javax.imageio.ImageIO
import server.clientCom.PlayerStats
import client.controller.MarketCommand
import scala.collection.JavaConversions._
import engine.rts.model.Resource
import engine.general.view._
import engine.general.view.gui.{Label, Button,IconLabel}

/**
 * This class represents the interface for using the game market.
 * @param myClient
 */
class MarketInterface(xP:Int,yP:Int,w:Int,h:Int,var myClient: GameConnection)  extends drawArea(xP,yP,w,h){

    var priceFormat=new DecimalFormat("###.##")
    var sellMap = new HashMap[String, Button]
    var buyMap = new HashMap[String, Button]

    var x=20
    var y=50
    val colSize=100
    val rowSize=30

    components.add(new Label(x,y,"Resource name"))
    x+=colSize
    components.add(new Label(x,y,"Sell"))
    x+=colSize
    components.add(new Label(x,y,"Buy"))
    x=20
    y+=rowSize

    for (resource <- Resource.resourceList) {

        val resourceName = resource.getName()
        if (resource.getName() != "coin"){
            
            val resourceImage =  ImageIO.read(new File("images/resources/" + resource.getResourceFile()))
            components.add(new IconLabel(x,y,resourceImage,20))
            x+=colSize

            var sellButton:Button = new Button(x,y+12,"Sell $" + resourceName)
            components.add(sellButton)
            sellMap.put(resourceName, sellButton)
            x+=colSize

            var buyButton:Button = new Button(x,y+12,"Buy $" + resourceName)
            components.add(buyButton)
            buyMap.put(resourceName, buyButton)
            y+=rowSize
            x=20
        }
    }

    /**
     * This method checks to see if a button was clicked.
     * @param clickX
     * @param clickY
     */
    def processClick(clickX:Integer,clickY:Integer){

        val x=clickX-myX
        val y=clickY-myY

        for((name,button)<-sellMap){
            if (button.contains(x,y)){
                import client.controller.MarketCommand
                val sellCommand = new MarketCommand("sell", name)
                myClient.sendInput(sellCommand)
                return
            }
        }
        
        for ((name,button)<-buyMap){
            if(button.contains(x,y)){
                val buyCommand = new MarketCommand("buy", name)
                myClient.sendInput(buyCommand)
                return
            }
        }
    }

    def updateMarket(statistics: PlayerStats) {
        val resourceCosts=myClient.getGameState().marketPrices
        for (resource <- myClient.getResourceDefs()) {
            val resourceName = resource.getName()
            if (resource.getName() != "coin"){
                
                val sellButton: Button = sellMap.get(resourceName)
                sellButton.setText("$"+priceFormat.format(resourceCosts.get(resourceName)*5))
                val buyButton: Button = buyMap.get(resourceName)
                buyButton.setText("$"+priceFormat.format(resourceCosts.get(resourceName)*10))
            }
        }
        render(imageGraphics)
    }
}
