package engine.rts.model

import java.awt.Polygon
import java.util.HashMap

object RegionDef{
    var rCount=0
}

/**
 * This class defines regions for the game.
 * In this class, regions are defined as places on the map that are
 * controllable by individual players
 */
abstract class RegionDef{
    
    RegionDef.rCount+=1
    var regionName:String=""+RegionDef.rCount
    def getName():String=regionName
    def setName(s:String)=regionName=s
    private var regionBounds: Polygon = null
    def getBounds()=regionBounds
    def setBounds(p:Polygon)=regionBounds=p

    def nameEquals(s:String)=s.equals(regionName)

    def getNationName():String={
        if(regionName.length>7){
            if(regionName.substring(0,14).equals(("Constantionople"))) {
                return "Turkey"
            }
        }

        if (regionName.substring(0,6) == "Moscow") {
            return "Russia"
        }

        if (regionName.substring(0,6) == "Madrid") {
            return "Spain"
        }

        if (regionName.substring(0,6) == "London") {
            return "England"
        }

        if (regionName.substring(0,6) == "Berlin") {
            return "Prussia"
        }

        if (regionName.substring(0,6) == "Vienna") {
            return "Austria"
        }
        return "Turkey"
    }
}
