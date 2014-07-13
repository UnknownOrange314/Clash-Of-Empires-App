package engine.rts.model

import engine.general.model.PlayArea
import scala.collection.JavaConversions._
import java.awt.Color
import scala.Array
import java.util.ArrayList
import server.model.playerData.Player

object StratMap{

    //These values represent player colors.
    final val playerColors: Array[Color] = Array(Color.red, Color.green, Color.orange, Color.cyan, Color.yellow, Color.PINK, Color.GRAY, Color.BLUE.darker, Color.LIGHT_GRAY, Color.WHITE, Color.BLACK, Color.WHITE, Color.GRAY)
    playerColors(0) = new Color(178, 34, 34)
    playerColors(1) = new Color(34, 139, 34)
    playerColors(2) = new Color(92, 64, 51)
    playerColors(3) = new Color(10, 10, 128)
    playerColors(4) = new Color(184, 134, 11)
    playerColors(5) = new Color(102, 2, 60)
}

/**
 * This represents a generic map object that can be applied to many different
 * RTS games.
 */
abstract class StratMap extends PlayArea{

    protected var myPlayers: ArrayList[Player] = new ArrayList[Player]  //The list of players in the game.
    protected def setPlayerNames(){
        for (p:Player <- myPlayers) {
            println("Capital name:"+p.getCapital().getName())
            p.setName
        }
    }

    def getPlayers:ArrayList[Player]={
        return myPlayers
    }
}
