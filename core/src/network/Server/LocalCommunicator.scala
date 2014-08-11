package network.server

import java.util.concurrent.ConcurrentLinkedQueue
import server.model.mapData.GameOption
import engine.general.network.DisplayCommunicator

class LocalCommunicator(val some1:Integer,val options:GameOption) extends DisplayCommunicator{

    var inputQueue=new ConcurrentLinkedQueue[Object]()
    var outputQueue=new ConcurrentLinkedQueue[Object]()
    def flushInput() {}

    def writeToClient(obj: Object){
        outputQueue.add(obj)
    }

    def emptyOutput():Boolean=outputQueue.isEmpty()
    
    def getOutput():Object={
       return outputQueue.poll()
    }

    def readFromClient():Object={
    	if(inputQueue.size()>0){
    		println("Input size:"+inputQueue.size())  
    	}
        if (inputQueue.isEmpty()){
            return null
        }
        return inputQueue.poll()
    }

    def writeToServer(obj:Object){
        inputQueue.add(obj)
    }
}
