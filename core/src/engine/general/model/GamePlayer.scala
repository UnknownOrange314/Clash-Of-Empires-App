package engine.general.model

import server.model.mapData.GameMap
import engine.rts.model.StratMap

/**
 * This object represents the people and computer players in the game.
 */
object GamePlayer{
    var numPlayers=0
    def setPlayerCount(c:Int)=numPlayers=c
}

class GamePlayer{

    val myNum=GamePlayer.numPlayers
    val myColor=StratMap.playerColors(myNum)
    var myName:String=null //The name of the player
    GamePlayer.numPlayers+=1
    def getNum()=myNum
    def getName()= myName
}
