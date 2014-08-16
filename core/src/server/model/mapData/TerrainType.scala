package server.model.mapData

import java.util.ArrayList
import scala.collection.JavaConversions._

object TerrainType{
    
    val WATER="water"
      
    var terrainList=new ArrayList[TerrainType]
    
    def getType(name:String):TerrainType={
        for(t:TerrainType<-terrainList){
            if (t.getName.equals(name)){
                return t
            }
        }
      
        return null  
    }
}

class TerrainType(dataMap:Map[Any,Any]) extends java.io.Serializable{
    
    val name=dataMap.get("name").get.asInstanceOf[String] //Set the name of the resource
    val defenseBonus=dataMap.get("defense_bonus").get.asInstanceOf[String]//Set the image for the resource
    val attackBonus=dataMap.get("attack_bonus").get.asInstanceOf[String]//Set the image for the resource
    val moveBonus=dataMap.get("move_bonus").get.asInstanceOf[String].toDouble//Set the image for the resource
    val hasResources=dataMap.get("has_resources").get.asInstanceOf[String].toBoolean

    def getName=name
    TerrainType.terrainList.add(this)

    def printData(){
        print(" terrain name:"+name)
        print(" defenseBonus:"+defenseBonus)
        print(" attackBonus:"+attackBonus)
        print(" moveBonus:"+moveBonus)
        print(" hasResources:"+hasResources)

    }
    
    def getMoveBonus()=moveBonus
    
    /**
     * Returns the cost of moving through a region.
     */
    def getMoveCost:Double={
      if(moveBonus==0){
    	  return  1/(moveBonus+0.0001)
      }else{
        return 1/moveBonus
      }
    }
    
    /**
     * Returns the cost factor for moving between two regions.
     */
    def getDistanceFactor(other:TerrainType): Double={
    	return (getMoveCost+other.getMoveCost)/2
    } 
}