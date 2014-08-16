package engine.rts.model

import scala.collection.JavaConversions._
import java.util.ArrayList
import java.util.ArrayList
import java.util

object Resource{
    var resourceList=new ArrayList[Resource]()
    def getResourceNames():ArrayList[String]={
        var rList=new ArrayList[String];
        for(res<-resourceList){
            rList.add(res.getName())
        }
        return rList
    }
}


/**
 * This class represents all the resources that will be present in the game.
 * @param resourceMap
 */
class Resource(resourceMap:Map[Any,Any])  extends java.io.Serializable {

    val name=resourceMap.get("name").get.asInstanceOf[String] //Set the name of the resource
    val resourceFile=resourceMap.get("image").get.asInstanceOf[String]//Set the image for the resource
    val id=Resource.resourceList.size()
    Resource.resourceList.add(this)
    def getName():String=name
    def getResourceFile():String=resourceFile
    def getID():Integer=id
    def printData(){println(name+":"+resourceFile)}
}
