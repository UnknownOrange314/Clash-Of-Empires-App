package network.client

import network.DataUpdater;
import server.model.mapData.{TerrainType, MapFacade, GameOption}
import server.model.playerData.{Player}
import java.util
import server.model.UpgradeDefinition
import server.clientCom.GameStateData
import java.io.{ObjectOutputStream, InputStream, ObjectInputStream}
import java.net.Socket
import java.awt.event.{ActionEvent, ActionListener}
import util.ArrayList
import java.awt.Polygon
import network.server.{LocalCommunicator}
import server.controller.playerControls.HumanPlayer
import scala.collection.JavaConversions._
import server.clientCom.PlayerStats
import engine.general.network.DisplayCommunicator
        import engine.rts.model.Resource
import com.badlogic.gdx.utils.Timer
/**
 * This class will represent a local connection with the game data and will not involve networking.
 */
class LocalConnection() extends GameConnection(){

    var upCallback:DataUpdater=null
    var marketCallback:(PlayerStats)=>Unit=null

    //Initialize the objects used to communicate with the server.
    var renderInput:ObjectInputStream=_
    var serverConnection:Socket=_
    var renderStream:InputStream=_
    var commandOutput:ObjectOutputStream=_

    val NUM_PLAYERS: Int = 4
    val opt: GameOption = new GameOption(NUM_PLAYERS,true)
    val gameListener:DisplayCommunicator=new LocalCommunicator(1,opt)
    opt.addPlayer(gameListener)
    gameListener.start()
    setupGame()

    //Initialize the timer and wait for the callback methods to be passed
    var updateTimer=new Timer()
    updateTimer.scheduleTask(new updateThread,0.0f,0.05f)

    def setupGame(){
        MapFacade.setupMap(opt)
        regionData=MapFacade.getRegionData()
        resourceData=Resource.resourceList
        upgradeData=UpgradeDefinition.upgradeList
        terrainTypes=TerrainType.terrainList
    }

    /**
     * This function is called at regular intervals to obtain the game state so it can be rendered.
     */
    class updateThread extends Timer.Task(){
        override def run(){   
            MapFacade.updateGame() //Update game.
            gameStateData=MapFacade.compressData()//Get the data for the game state.

            //See if the players have received any input
            for (player:HumanPlayer<-MapFacade.getHumans()){
                player.clientListen()
            }

            //See if any additional data has ben sent.
            val lCom=gameListener.asInstanceOf[LocalCommunicator]
            while(lCom.emptyOutput()==false){
                val out=lCom.getOutput()
                if(out.isInstanceOf[PlayerStats]){
                    myStats=out.asInstanceOf[PlayerStats]
                    upCallback.update(myStats)//Update market data.
                }
            }
        }
    }

    /**
     * Send input to the game so that it can be processed.
     * @param input
     */
    def sendInput(input: Object){
        gameListener.asInstanceOf[LocalCommunicator].writeToServer(input)
    }

    /**
     * This method checks if there is any null data.
     * @return
     */
    def nullData():Boolean={
        if (regionData==null){
            println("No region data")
            return true
        }
        if (gameStateData==null){
            println("No game state data")
            return true
        }
        if (myStats==null){
            println("No statistics")
            return true
        }
        if (upgradeData==null){
            println("No upgrade data")
            return true
        }
        if (resourceData==null){
            println("No resource data")
            return true
        }
        return false
    }

    
    def addMarketCallback(c1:(PlayerStats)=>Unit){
        marketCallback=c1
        if(marketCallback!=null&&upCallback!=null){
            updateTimer.start()
        }
    }

   
    def addDataCallback(c2:DataUpdater){
        upCallback=c2
        updateTimer.start()     
    }
    
    def addCallbacks(c1: (PlayerStats) => Unit, c2: (PlayerStats) => Unit) {System.err.println("Not implemented")}
}
