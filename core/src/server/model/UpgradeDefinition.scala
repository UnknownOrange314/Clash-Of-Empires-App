package server.model

import java.util.ArrayList
import java.util.HashMap
import java.awt.image.BufferedImage
import java.awt.Graphics
import engine.general.utility.Location
import scala.collection.JavaConversions._
import engine.rts.model.Resource

object UpgradeDefinition{
    var upgradeList=new ArrayList[UpgradeDefinition]()
    def getUpgrade(keyPress:Integer):UpgradeDefinition={
        for (upgrade<-upgradeList){
            if (upgrade.getBuildKey()==keyPress){
                return upgrade
            }
        }
        return null
    }
}


/**
 * This class will store the data for each upgrade type after reading from the file.
 * @param improvementMap The data that was read from the JSON file.
 * @param resourceList The list of resources in the game.
 */
class UpgradeDefinition(improvementMap:Map[Any,Any],resourceList:ArrayList[Resource]) extends java.io.Serializable{

    val name=improvementMap.get("name").get.asInstanceOf[String]//Set the name
    val imageName=improvementMap.get("image").get.asInstanceOf[String] //Set the name
    var resourceCosts=new HashMap[String,Integer]() //This represents how much an upgrade costs.
    var resourceBonus=new HashMap[String,Double]()//This represents the resource income setBonus.

    for(resource<-resourceList){
        val resName:String=resource.getName()
        resourceCosts.put(resName,(improvementMap.get(resName+"_cost").get.asInstanceOf[String]).toInt)

        if(improvementMap.contains(resName+"_bonus")){
            resourceBonus.put(resName,improvementMap.get(resName+"_bonus").get.asInstanceOf[String].toDouble)
        }
        else{
            resourceBonus.put(resName,1.0)
        }
    }

    val  moveBonus=improvementMap.get("move_bonus").get.asInstanceOf[String].toDouble
    val description=improvementMap.get("descr").get.asInstanceOf[String]

    //Set more of the attributes.
    val attackPower=improvementMap.get("attack_power").get.asInstanceOf[String]
    val attackBonus=improvementMap.get("attack_bonus").get.asInstanceOf[String].toDouble
    val defenseBonus=improvementMap.get("defense_bonus").get.asInstanceOf[String].toDouble
    val keyTemp=improvementMap.get("build_key").get.asInstanceOf[String].toCharArray()
    val buildKey=keyTemp(0)

    //Add the upgrade to the list of upgrades.
    val id=UpgradeDefinition.upgradeList.size()
    UpgradeDefinition.upgradeList.add(this)//Add improvement to list of upgrades.
    val myImage: BufferedImage = null //This represents the image

    def getInfo=description
    def getMoveBonus=moveBonus
    def getID():Integer=id
    def getImageLocation():String=imageName

    //TODO:Make sure different improvements aren't drawn in same location
    def draw (g: Graphics, loc: Location){
        val dx: Int = loc.getX.asInstanceOf[Int]
        val dy: Int = loc.getY.asInstanceOf[Int] - 15
        g.drawImage(myImage, dx - 40, dy - 10, 30, 30, null)
    }

    def getResourceBonus(res:String)=resourceBonus.get(res)
    def getAttackStr=attackPower.toInt
    def getResourceBonus():HashMap[String,Double]=resourceBonus
    def getCost():HashMap[String,Integer]=resourceCosts
    def getBuildKey():Integer=buildKey

    def costStr():String={
        var s="Cost:"
        for ((name,cost)<-resourceCosts){
            s+=","+cost
        }
        return s
    }
    
    def printData(){
        println(name+" "+imageName+" costs:"+resourceCosts+" bonuses:"+resourceBonus+" attackBonus:"+attackBonus+" attackPower:"+attackPower+" defenseBonus:"+defenseBonus)
    }
}
