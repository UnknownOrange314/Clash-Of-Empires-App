package server.model.mapData

import engine.general.network.DisplayCommunicator
import server.controller.playerControls.HumanPlayer
import server.model.playerData.Player
import javax.swing._
import java.util.ArrayList
import engine.rts.model.OptionBase

class GameOption(numSlider:JSlider,val nTroop:Boolean) extends OptionBase(numSlider){

    var players=new ArrayList[HumanPlayer]//This represents a list of players that have connected to the client
    def neutralTroop()=nTroop
    def addPlayer(com:DisplayCommunicator){
        players.add(new HumanPlayer(com))
    }
    def getClients()=players
}
