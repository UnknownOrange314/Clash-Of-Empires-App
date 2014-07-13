package server.model.ai

import java.util
import scala.collection.JavaConversions._

/**
 * Created with IntelliJ IDEA.
 * User: Bharat
 * Date: 1/9/13
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
object AiPersona{
    
    var AiList=new util.ArrayList[AiPersona]
    /**
     * This method will pick the AI persona that is listed first in the configuration file.
     */
    def pickDefaultPersona():AiPersona={return AiList.get(0)}

    /**
     *This method will pick the neutral AI persona
     */
    def getNeutralPersona():AiPersona={
        for (persona:AiPersona<-AiList){
            if (persona.getName().equals("neutral")){
                return persona
            }
        }
        System.err.println("There is no neutral AI personality")
        return null
    }
}

class AiPersona(resourceMap:Map[Any,Any])  extends java.io.Serializable{
    
    val name=resourceMap.get("name").get.asInstanceOf[String]
    val reinforceRate=resourceMap.get("reinforce_rate").get.asInstanceOf[String].toDouble
    val aggression=resourceMap.get("aggression").get.asInstanceOf[String].toDouble
    val paranoia=resourceMap.get("paranoia").get.asInstanceOf[String].toDouble

    def getAggression():Double=aggression
    def getParanoia():Double=paranoia
    def getName():String=name
    AiPersona.AiList.add(this)
}
