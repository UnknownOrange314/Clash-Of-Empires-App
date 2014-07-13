package server.model.playerData

import engine.rts.model.Resource
import java.util.HashMap
import scala.collection.JavaConversions._
import java.util.{BitSet, HashSet}
import server.model.UpgradeDefinition
import collection.mutable.Map
import java.util
import engine.rts.model.Resource

/**
 * This class represents the people that are in the region.
 */
class Population{
    var total=0
    var START_POP=1000000
    def setTotal(t:Int)=total=t

}

/**
 * This object represents the economy of a region.
 */
class Economy {

    /**
     * This is the number of people in a region. The population will slowly grow over time as long as a player
     * has enough food. A high population gives more money in taxes and means that a player score will be higher.
     */
    private var population=1000000
    var myResource:Resource=null
    
    /**
     * This represents the abundance of the resources in a territory.
     * It is up to the specific implementation to determine how abundance of resources
     * translates to region income.
     */
    var myResources=new HashMap[String,Double]()
    def getAbundance(s:String)=myResources.get(s)

    /**
     * This represents the upgrades that are present in a region.
     */
    var myUpgrades=new HashSet[UpgradeDefinition]()

    if(Resource.resourceList.size()>0){
        findResources()
    }

    /**
     * This method determines the resources that an economy will have.
     */
    def findResources(){
        var rNum=(Math.random*(Resource.resourceList.size())).toInt
        myResource=Resource.resourceList.get(rNum)
        myResources.put(myResource.getName(),1.0)
        myResources.put("coin",1.0)
    }

    /**
     * This method may not be needed.
     * @return The resource number for the region's resource.
     */
    
    def getResourceNumber()= myResource.getID()
    def hasUpgrade(up:UpgradeDefinition)=myUpgrades.contains(up)
    def getUpgradeDefense=myUpgrades.foldLeft(0.0)((tot,up)=>tot+up.defenseBonus)
    def getUpgradeAttack=myUpgrades.foldLeft(0.0)((tot,up)=>tot+up.attackBonus)
    def getMovement():Double=myUpgrades.foldLeft(1.0)((tot,up)=>tot+up.getMoveBonus)
    def getAttackStrength():Double= myUpgrades.foldLeft(0.0)((tot,up)=>tot+up.getAttackStr)
    def getPopulation=population
    
    def rBonus(res:Resource)=myUpgrades
        .foldLeft(getAbundance(res.getName))((tot,up)=>tot*(up.getResourceBonus(res.getName)))
    /**
     * This method is used to calculate the resource income for a region.
     * @return A HashMap indicating how much of a resource should be generated.
     */
    def getResourceIncome(resourceList:util.ArrayList[Resource])=resourceList.
        map(res=>(res.getName,rBonus(res))).toMap


    def addUpgrade(upgrade:UpgradeDefinition){
        myUpgrades.add(upgrade)
    }

    /**
     * This sends the encoding of a region's improvements as a character as a BitSet
     * The purpose of this method is to send compressed data about a region to the client.
     * @return A bitset representing the improvements
     */
    def getImprovements():BitSet={
        
        var upgrades=UpgradeDefinition.upgradeList
        var improvementData=new BitSet(upgrades.size())
        var i=0
        for(upgrade<-upgrades){
            if(myUpgrades.contains(upgrade)){
                improvementData.set(i)
            }
            i+=1
        }
        return improvementData
    }

    def growPopulation(growth:Double) {
        if(population<100) //Prevent population from being too small.
            return
        val overPop=population/1000000
        population=(population*(growth-overPop/100)).toInt
    }

    /**
     * This method returns the resource income of a region as a string.
     * @return
     */
    def incomeString():String={
        var inStr=""
        for(res<-getResourceIncome(Resource.resourceList).keySet){
            var income=getResourceIncome(Resource.resourceList).get(res)
            inStr+=" "+res+":"+income
        }
        return inStr
    }
}
