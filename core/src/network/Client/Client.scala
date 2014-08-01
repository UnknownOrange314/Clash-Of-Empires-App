package network.client

import java.net.{Socket}
import java.io._
import server.clientCom.GameStateData
import server.model.mapData.{TerrainType}
import java.util.ArrayList
import java.awt.Polygon
import java.awt.event.{KeyEvent, MouseEvent}
import java.util
import server.model.UpgradeDefinition
import engine.rts.model.Resource;
import server.clientCom.PlayerStats
import server.clientCom.RegionRenderData

object Client{
    val DEFAULT_HOST="Bharat-PC"
    val DEFAULT_PORT=4444
}

class Client() extends GameConnection(){

    var updateCallback:(PlayerStats)=>Unit=null
    var marketCallback:(PlayerStats)=>Unit=null

    //Initialize the objects used to communicate with the server.
    var renderInput:ObjectInputStream=null
    var serverConnection:Socket=null
    var renderStream:InputStream=null
    var commandOutput:ObjectOutputStream=null
    
    try{
        serverConnection=new Socket(Client.DEFAULT_HOST,Client.DEFAULT_PORT)//Connect to server
        commandOutput = new ObjectOutputStream(serverConnection.getOutputStream()) //Used to send input to server

        //Create object to read input from the server
        renderStream=serverConnection.getInputStream()
        renderInput=new ObjectInputStream(renderStream)
    }
    
    catch{
        case ioe: IOException =>ioe.printStackTrace()
        case e =>e.printStackTrace()
    }

    /**
     * Writes an object to the server.
     * @param input The object that will be written to the server.
     */
    def sendInput(input:Object){
        commandOutput.writeObject(input)
    }

    /**
     * This starts a new thread for the current client object that waits for data from the server
     */
    override def run(){
        listen()
    }

    /**
     * Is the client still waiting for data from the server?
     * @return
     */
    def nullData():Boolean={
        if (gameStateData==null){
            return true
        }
        if (regionData==null){
            return true
        }
        if (resourceData==null){
            return true
        }
        if (improvementData==null){
            return true
        }
        if(terrainTypes==null){
            return true
        }
        return false
    }

    def listen(){
        var serverInput:Object=null
        try{
            while((serverInput=renderInput.readObject())!=null){
                if(serverInput.isInstanceOf[GameStateData]){
                    gameStateData=serverInput.asInstanceOf[GameStateData]
                }
                if(serverInput.isInstanceOf[RegionRenderData]){
                    regionData=serverInput.asInstanceOf[RegionRenderData]
                }
                if(serverInput.isInstanceOf[PlayerStats]){
                    updateCallback(serverInput.asInstanceOf[PlayerStats])
                    myStats=serverInput.asInstanceOf[PlayerStats]
                }
                if (serverInput.isInstanceOf[util.ArrayList[Any]]){
                    val first=serverInput.asInstanceOf[util.ArrayList[Any]].get(0)
                    //Process data that was read from the configuration files
                    if(first.isInstanceOf[Resource]){
                        resourceData=serverInput.asInstanceOf[util.ArrayList[Resource]]
                    }
                    if(first.isInstanceOf[UpgradeDefinition]){
                        improvementData=serverInput.asInstanceOf[util.ArrayList[UpgradeDefinition]]
                    }
                    if (first.isInstanceOf[TerrainType]){
                        terrainTypes=serverInput.asInstanceOf[util.ArrayList[TerrainType]]
                    }
                }
            }
        }
        catch{
            case e:Exception =>println("Problem with listening")
        }
    }

    def addCallbacks(c1: (PlayerStats) => Unit, c2: (PlayerStats) => Unit){
        System.err.println("Not implemented")
    }

    def addMarketCallback(c1: (PlayerStats) => Unit) {}
    def addDataCallback(c2: (PlayerStats) => Unit) {}
}
