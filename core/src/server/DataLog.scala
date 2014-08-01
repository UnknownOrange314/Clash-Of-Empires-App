package server

import java.io._
import server.model.mapData.GameMap
import java.util.ArrayList
import server.model.playerData.Player

/**
 * This class logs important information to a text file.
 */
class DataLog{

    var time=System.nanoTime()
    var ms=time*Math.pow(10,-5)
    val log= new PrintWriter(new File("log"+ms.toInt+".txt" ))
    var tNum=0

    def updateLog(pList:ArrayList[Player]){
        tNum+=1
    }

    def write(message:String){
        log.write(message+"\n")
    }
}
