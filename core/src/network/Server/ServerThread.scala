package network.Server

import java.net.{Socket, ServerSocket}
import java.io._
import server.clientCom.GameStateData
import server.model.mapData.{GameOption}
import engine.general.network.DisplayCommunicator
import server.clientCom.RegionRenderData

object ServerThread{
    var portNum=4444
}

class ServerThread(val numPlayers:Integer,var gameOptions:GameOption) extends DisplayCommunicator{

    var connections:Integer=0
    
    //Initialize data
    var socket:ServerSocket=_
    var clientSocket:Socket=_
    var out:OutputStream=_
    var displayWriter: ObjectOutputStream = _
    var objectReader:ObjectInputStream=_
    override def run(){
        //Run this loop until enough players have connected
        //TODO: Make sure that data that defines each region is sent to every client.
        while(connections<numPlayers){
            
            socket=new ServerSocket(ServerThread.portNum)
            clientSocket=socket.accept()
            displayWriter=new ObjectOutputStream(clientSocket.getOutputStream())
            displayWriter.flush()
            objectReader=new ObjectInputStream(clientSocket.getInputStream())

            gameOptions.addPlayer(this)
            connections=connections+1
            ServerThread.portNum=ServerThread.portNum+1//We want to make sure that two players do not connect on the same port
        }
    }

    /**
     * This method sends data that represents the game state.
     * @param renderData
     */
    def sendGameState(renderData:GameStateData){
        displayWriter.writeObject(renderData)
        displayWriter.flush()
    }

    /**
     * This method sends data that represents the region data.
     * @param regionData
     */
    def sendRegionData(regionData:RegionRenderData){
        displayWriter.writeObject(regionData)
        displayWriter.flush()
    }

    def flushInput(){
        displayWriter.flush()
    }

    def writeToClient(obj: Object){
        displayWriter.writeObject(obj)
        displayWriter.flush()
    }

    def readFromClient():Object={
        return objectReader.readObject()
    }
}
