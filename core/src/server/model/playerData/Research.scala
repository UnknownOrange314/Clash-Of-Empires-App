package server.model.playerData

import collection.JavaConverters._
import scala.collection.JavaConversions._
import java.util.HashMap
import server.model.playerData.ResItem._
import collection.mutable.Map

object ResItem{
    
    val baseCost=1
    val TROOP_UP=0.05
    val GROWTH_UP=0.01
    val TAX_UP=0.05
    val move="Upgrade troop movement"
    val tProd="Upgrade troop production" //Upgrades troop production in capital.
    val growthUp="Upgrade population growth"
    val mProd="Upgrade tax production"

    val resList=new HashMap[String,ResItem]()
    resList.put(move,new ResItem(2,0,0,0))
    resList.put(tProd,new ResItem(0,TROOP_UP,0,0))
    resList.put(growthUp,new ResItem(0,0,GROWTH_UP,0))
    resList.put(mProd,new ResItem(0,0,0,TAX_UP))

}

/**
 * This class represents a research item. The parameters represent the upgrade effect per level.
 */
class ResItem(val troopMov:Double,val troopProd:Double,val foodProd:Double,val moneyProd:Double)

/**
 * This class represents the research for players.
 */
class Research{
 
   val research:Map[String,Double]=ResItem.resList.map(name=>name._1->1.0)
   def sendData():Map[String,String]= ResItem.resList.map(name=>name._1->(getCost(name._1).toString))

   //BUG:They are all getting the wrong research item.
   def getMoveBonus()=resList.foldLeft(1.0)((a,b)=>a*(1.0+b._2.troopMov*research(move)))
   def getProdBonus()=resList.foldLeft(1.0)((a,b)=>a*(1.0+b._2.troopProd*research(b._1)))
   def getGrowthBonus()=resList.foldLeft(1.0)((a,b)=>a*(1.0+b._2.foodProd*research(growthUp)))
   def getTaxBonus()=resList.foldLeft(1.0)((a,b)=>a*(1.0+b._2.moneyProd*research(b._1)))
   def upgrade(name:String)=  research.put(name,research(name)+1.0)
   def getCost(name:String)=baseCost*Math.pow(1.1,research(name))

}
