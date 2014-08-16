package client.view.panels

import network.client.GameConnection
import com.badlogic.gdx.graphics.Color
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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont

/**
 * This class represents the interface for using the game market.
 * @param myClient
 */
class MarketInterface(x:Int,y:Int,w:Int,h:Int,var myClient: GameConnection)  extends drawArea(x,y,w,h){

    var priceFormat=new DecimalFormat("###.##")
    var sellMap = new HashMap[String, Button]
    var buyMap = new HashMap[String, Button]

    var xP=20
    var yP=50
    val colSize=100
    val rowSize=30

    var font=new BitmapFont()
    font.setColor(Color.WHITE)
    components.add(new Label(x,y,"Resource name",font))
    xP+=colSize
    components.add(new Label(x,y,"Sell",font))
    xP+=colSize
    components.add(new Label(x,y,"Buy",font))
    xP=20
    yP+=rowSize

    for (resource <- Resource.resourceList) {

        val resourceName = resource.getName()
        if (resource.getName() != "coin"){
            
        	val resourceImage=new Texture("images/resources/"+resource.getResourceFile())
            components.add((new IconLabel.Builder)
            				.xPos(x)
            				.yPos(y)
            				.image(resourceImage)
            				.build()
            				)
            xP+=colSize

            var sellButton:Button =(new Button.ButtonBuilder)
            						.x(x)
            						.y(y+12)
            						.text("Sell $" + resourceName)
            						.font(font)
            						.build()
            components.add(sellButton)
            sellMap.put(resourceName, sellButton)
            xP+=colSize

            var buyButton:Button =(new Button.ButtonBuilder)
            						.x(x)
            						.y(y+12)
            						.text("Buy $" + resourceName)
            						.font(font)
            						.build()
            components.add(buyButton)
            buyMap.put(resourceName, buyButton)
            yP+=rowSize
            xP=20
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
            if (button.contains(x.toInt,y.toInt)){
                import client.controller.MarketCommand
                val sellCommand = (new MarketCommand.Builder())
                					.instruction(MarketCommand.SELL)
                					.resource(name)
                					.build();
                
                //new MarketCommand("sell", name)
                myClient.sendInput(sellCommand)
                return
            }
        }
        
        for ((name,button)<-buyMap){
            if(button.contains(x.toInt,y.toInt)){
                val buyCommand = (new MarketCommand.Builder())
                					.instruction(MarketCommand.BUY)
                					.resource(name)
                					.build();
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
    }
}
