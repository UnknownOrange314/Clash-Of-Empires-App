package server.model.mapData

import scala.collection.JavaConversions._
import scala.collection.mutable.Map
import java.util.HashMap
import java.lang.Double
import engine.rts.model.Resource

object ResourceMarket{
    def baseSellNum=10 //The default amount of resources you will lose per market transaction.
    def baseBuyNum=5   //The default amount of resources you wil gain per market transaction.
}

class ResourceMarket{

    var resourceData:Map[String,Double]=Map()
    for(resource<-Resource.resourceList){
        resourceData(resource.getName())=1.0
    }

    def sell(resourceString:String){
        resourceData(resourceString)=resourceData(resourceString)*0.95
    }

    def buy(resourceString:String){
        resourceData(resourceString)=resourceData(resourceString)*1.05
    }

    def getPrice(str:String):Double=resourceData(str)

    def getAllPrices():HashMap[String,Double]={
        var prices=new HashMap[String,Double]()
        for (resource<-Resource.resourceList){
            val resStr=resource.getName()
            val price=resourceData(resStr)
            prices.put(resStr,price)
        }
        return prices
    }
    
    def updatePrices(){
        for (resource<-Resource.resourceList){
            val resourceString=resource.getName()
            if (resourceData(resourceString)>1){
                resourceData.put(resourceString,resourceData(resourceString)*0.99)
            }
            else{
                resourceData.put(resourceString,resourceData(resourceString)*1.01)
            }
        }
    }
}
