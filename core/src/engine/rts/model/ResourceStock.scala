package engine.rts.model

import scala.collection.JavaConversions._
import java.util.LinkedList
import java.util.HashMap
import server.model.UpgradeDefinition

/**
 * This class is used to define resources for a player.
 */
class ResourceStock{

    var failMessages=new LinkedList[String]()  //This log all the failure messages when the user does not have enough resources to do something
    var myResources=new HashMap[String,Integer]()  //This represents the amount of resources that a player has.

    /**
     * This method returns the resource data for a player
     */
    def getResourceData(): HashMap[String,Integer]={
        var resources=new HashMap[String,Integer]()
        for(  (name:String,v) <- myResources){
            resources.put(name,v)
        }
        return resources
    }

    /**
     * Can a player buy an upgrade.
     * @param rCost The cost of buying an upgrade.
     * @return
     */
    def canUpgrade(rCost: HashMap[String,Integer]): Boolean={
        for( (str,amt) <- myResources){
            if(amt<rCost.get(str)){
                failMessages.add("Not enough resources to buy upgrade")
                return false
            }
        }
        return true
    }

    /**
     * This method is used to buy an upgrade.
     * @param upgrade The type of upgrade that should be purchased.
     */
    def buyUpgrade(upgrade:UpgradeDefinition){
        for ( (resourceName,cost)<-upgrade.getCost()){
            myResources.put(resourceName,myResources.get(resourceName)-cost)
        }
    }
}
