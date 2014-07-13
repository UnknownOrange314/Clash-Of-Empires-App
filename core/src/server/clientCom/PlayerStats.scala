package server.clientCom

import java.awt.Point
import java.lang.Double
import java.util
import java.util.{HashMap, HashSet}
import engine.general.utility.Line
import server.model.playerData.{Research, Population}

/**
 * This class sends all the important statistics for a player to the game display so that they can be processed and rendered.
 * @param resources
 * @param elapsedTime
 * @param score
 * @param rallyPoints
 * @param leftClick
 * @param rightClick
 */
class PlayerStats(resources:HashMap[String,Integer],elapsedTime:Integer,score:Population,rallyPoints:HashSet[Line],val leftClick:Point,val rightClick:Point,val upkeep:Double,val income:HashMap[String,Double],val failLog:util.ArrayList[String],val research:Research) extends Serializable{
    val res=research.sendData()
    val resourceData=resources
    val timeElapsed=elapsedTime
    val playerScore=score
    val myRallyPoints=rallyPoints

    def getRallyPoints():HashSet[Line]=myRallyPoints
    def getResources():HashMap[String,Integer]=resources
    def getIncome():HashMap[String,Double]=income
    def getUpkeep():Double=upkeep
    def getTimeElapsed():Integer=timeElapsed
    def getPlayerScore():Population=playerScore
    def getRightClick():Point=rightClick
    def getLeftClick():Point=leftClick
}
