package server.clientCom

import java.util.ArrayList
import java.util
import engine.general.utility.IntLoc
import java.util.HashMap
import java.lang.Double
import engine.general.utility.Line

/**
 * This class stores all the data necessary for rendering the game state.
 * It will be used to transfer data across the network in a serialized state and
 * will be designed in a way that reduces the amount of data that needs to be sent.
 * A copy of the map will be stored on each of the client machines, so there is no need to 
 * send data that defines regions.
 */
class GameStateData(val deathCounts:HashMap[IntLoc,Integer],val passTime:Double,val regionStates:ArrayList[RegionState],val conflictLocs:HashMap[IntLoc,Line],val marketPrices:HashMap[String,Double],val nationInfo:HashMap[String,String]) extends java.io.Serializable

/**
 * This class represents information about the state of the region.
 */
class RegionState extends java.io.Serializable{

    var myOwner=999
    var troopCounts:util.ArrayList[Integer]=null
    var improvementData:util.BitSet=null
    var resourceNum:Integer=999
    var _name:String=null

    var income:String=""
    var tProd=0.0
    var defenseBonus=1.0
    var attackBonus=1.0
    var hitPoints=0
    var capital=false
    var terrainType=""

    def setHitPoints(p:Int)=hitPoints=p
    def getHitPoints=hitPoints
    def setCapital(b:Boolean)=capital=b
    def isCapital()=capital
    def setTerrain(t:String)=terrainType=t
    def getTerrain=terrainType

    def setIncome(in:String){
        income=in
    }

    def setTroopProd(prod:Double){
        tProd=prod
    }

    var myPopulation=0
    def setPopulation(pop:Int)=myPopulation=pop
    def population=myPopulation
    def name=_name //Get the name.

    def setName(n:String){
        _name=n
    }

    def setDefenseBonus(d:Double){
        defenseBonus=d
    }
    
    def getDefenseBonus=defenseBonus

    def setAttackBonus(a:Double){
        attackBonus=a
    }
    
    def getAttackBonus=attackBonus
    def addResourceNum(rNum:Integer)={resourceNum=rNum}
    def addImprovementData(data:util.BitSet)={improvementData=data}
    def addTroopCounts(counts:util.ArrayList[Integer])={troopCounts=counts}

    def getResourceNum():Integer=resourceNum
    def getOwnerNum():Integer=myOwner
    def getUpgradeData():util.BitSet=improvementData
    def getOwnerTroopCount():Integer=troopCounts.get(myOwner)
}
