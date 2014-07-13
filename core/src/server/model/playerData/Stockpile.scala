package server.model.playerData

import java.util.{HashMap}
import scala.collection.JavaConversions._
import server.model.mapData.ResourceMarket
import java.util.HashSet
import engine.rts.model.ResourceStock
import java.util.ArrayList
import collection.mutable.Map

object Stockpile{
    def setCost(cost:Double){
        ADD_R_COST=cost
    }
    var MIN_INCOME=4.0
    val TAX=0.000001 //This is the amount of money each person pays in taxes.
    val FOOD_C=0.0000002 //This is the amount of food that each person consumes.
    val MAX_LOG_SIZE=10
    val TROOP_UPKEEP=0.002
    var ADD_R_COST=0.02 //The additional cost for maintaining each region. A player can control up to 2/ADD_R_COST regions and have a net gain of resource quantities from each additional region.
    val BASE_GROWTH=1.01 //Growth when there is enough food.
    val BASE_STARVATION=0.98 //Growth when there is not enough food.
}

/**
 * This class represents the resources that a user has.
 */
class Stockpile(resourceList:List[String]) extends ResourceStock{
    var resourceIncome=new HashMap[String,java.lang.Double]()
    for(resource <-resourceList){
        myResources.put(resource,0)
    }

    var bonus:Double=1 //This value determines if a player gains a resource bonus.
    def resCount=myResources.size()
    def getStockpile(res:String)=myResources(res)

    /**
     * This function generates an upkeep cost for regions.
     */
    private def regionUpkeep(rCount:Integer):Double=(Stockpile.ADD_R_COST/2.0)*Math.pow(rCount.toDouble,2)

    /**
     * This function generates an upkeep cost for troops.
     */
    private def troopUpkeep(tCount:Integer):Double=Stockpile.TROOP_UPKEEP*tCount

    /**
     * This method generates an upkeep cost for the number of troops and regions.
     * The upkeep cost is currently rounded to the nearest integer.
     * @param rNum
     * @param tNum
     */
    def upkeep(rNum:Integer,tNum:Integer):Double={
        val coinCost=getUpkeepCost(rNum,tNum)
        myResources.put("coin",myResources.get("coin")-coinCost.toInt)
        return coinCost
    }

    def hasMoney():Boolean=myResources.get("coin")>0

    /**
     * This method sets the resource bonus that a player should get.
      * @param newB The resource bonus.
     */
    def setBonus(newB:Double){
        bonus=newB
    }

    def getGrowthRate():Double={
        if (myResources.get("food")>0){
            return Stockpile.BASE_GROWTH  //There is enough food for population growth.
        }
        else{
            return Stockpile.BASE_STARVATION //Not enough food.
        }
    }

    def calculateIncome(myZones:HashSet[Region],myCapital:Region,myPopulation:Int,research:Research):Map[String,Double]={
        var resourceIncome:Map[String,Double]=myResources.map(rVal=>rVal._1->Stockpile.MIN_INCOME)
        for( (reg:Region) <-myZones){
            for( (resourceName:String,income:Double)<-reg.getResourceIncome()){
                val newStock:Double=resourceIncome(resourceName)+income
                resourceIncome.put(resourceName,newStock)
            }
        }
        return resourceIncome
    }

    def income(myZones: java.util.HashSet[Region],myCapital:Region,myPopulation:Int,research:Research){
         val resourceIncome=calculateIncome(myZones,myCapital,myPopulation,research)
	 for((resStr,v) <- myResources) {
	      var temp:Double=v.toDouble
              temp+=resourceIncome(resStr)
              bonus=1.0
	      myResources.put(resStr, temp.toInt)
	}
        val taxes:Double=Stockpile.TAX*myPopulation*research.getTaxBonus()
        myResources.put("coin",myResources.get("coin")+taxes.toInt)
        val foodCost=Stockpile.FOOD_C*myPopulation/research.getTaxBonus()//More production=more efficient food usage.
        myResources.put("food",myResources.get("food")-foodCost.toInt)
    }


    /**
     * Sells a resource and gives additional gold.
     * @param resourceString
     * @param price
     *   @return Returns true if successful or false if unsuccessful.
     */
    def sell(resourceString:String,price:Double):Boolean={
        //We do not have enough resources
        if(myResources.get(resourceString)-ResourceMarket.baseSellNum<0){
            failMessages.addLast("Not enough "+resourceString+" to sell")
            return false
        }
        myResources.put(resourceString,(myResources.get(resourceString)-ResourceMarket.baseSellNum))
        myResources.put("coin",(myResources.get("coin")+ResourceMarket.baseBuyNum*price).toInt)
        return true
    }

    /**
     * Buys a resource using gold.
     * @param resourceString
     * @param price
     * @return Returns true if successful or false if unsuccessful.
     */
    def buy(resourceString:String,price:Double):Boolean={
        //We do not have enough money
        if(myResources.get("coin")-ResourceMarket.baseSellNum*price<0){
            failMessages.addLast("Not enough coin to buy "+resourceString)
            return false
        }
        myResources.put(resourceString,(myResources.get(resourceString)+ResourceMarket.baseBuyNum))
        myResources.put("coin",(myResources.get("coin")-ResourceMarket.baseSellNum*price).toInt)
        return true
    }

    /**
     * This method is used to buy research. The research is only bought if
     * they player has enough money.
     * @param price The price of the research.
     * @return True if the research was successfully bought, false otherwise.
     */
    def buyResearch(price:Int):Boolean= {
        if(myResources.get("coin")<price){
            return false
        }
        else{
            myResources.put("coin",myResources.get("coin")-price)
        }
        return true
    }

    /**
     * TODO: Refactor this method so that it calculates income without relying on
     * a previous state. It should calculate income based on an argument to the function instead.
     * This method returns a value representing resource income.
     * The results of this method may be somewhat inaccurate due to rounding errors that need to be fixed
     * @return
     */
     def getIncome():HashMap[String,java.lang.Double]=resourceIncome
     def getUpkeepCost(rNum:Integer,tNum:Integer):Double=regionUpkeep(rNum)+troopUpkeep(tNum)

    /**
     * This checks to see if a player has too many resources.
     * TODO:Make the result dependent on the price of improvements.
     * @return
     */
     def rich():Boolean={
        for((resource,amount)<-myResources){
            if (amount<10){
                return false
            }
        }
        return true
     }

     def getFailMessages():ArrayList[String]={
          while(failMessages.size()>Stockpile.MAX_LOG_SIZE){
            failMessages.removeFirst()
          }
          var failList=new ArrayList[String]()
          failList.addAll(failMessages)
          if (hasMoney()==false){
                failList.add("You have no money. Take more regions, build improvements, or get rid of troops to get more")
          }
          if (rich()==true){
                failList.add("You are accumulating a large resource stockpile, which does not help you. Left click on a region to buy an upgrade")
          }
          return failList
      }
}
