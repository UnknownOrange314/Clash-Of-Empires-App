package engine.rts.model

import engine.general.model.PlayArea
import scala.collection.JavaConversions._
import com.badlogic.gdx.graphics.Color
import scala.Array
import java.util.ArrayList
import server.model.playerData.Player

object StratMap{

    //These values represent player colors.
    final val playerColors: Array[Color] = Array(null,null,null,null,null, Color.PINK, Color.GRAY, Color.BLUE, Color.LIGHT_GRAY, Color.WHITE, Color.BLACK, Color.WHITE, Color.GRAY)
    playerColors(0) = new Color(178f/255f, 34f/255f, 34f/255f,1)
    playerColors(1) = new Color(34f/255f, 139f/255f, 34f/255f,1)
    playerColors(2) = new Color(92f/255f, 64f/255f, 51f/255f,1)
    playerColors(3) = new Color(50f/255f, 50f/255f, 178f/255f,1)
    playerColors(4) = new Color(184f/255f, 134f/255f, 11f/255f,1)
    playerColors(5) = new Color(102f/255f, 2f/255f, 60f/255f,1)
}

/**
 * This represents a generic map object that can be applied to many different
 * RTS games.
 */
abstract class StratMap extends PlayArea{

    protected var myPlayers: ArrayList[Player] = new ArrayList[Player]  //The list of players in the game.
    protected def setPlayerNames(){
        for (p:Player <- myPlayers) {
            p.setName
        }
    }

    def getPlayers:ArrayList[Player]={
        return myPlayers
    }
}
