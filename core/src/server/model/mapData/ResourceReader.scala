package server.model.mapData

import com.badlogic.gdx.Gdx;
import scala.util.parsing.json._
import java.util.ArrayList
import server.model.UpgradeDefinition
import server.model.ai.AiPersona
import scala.collection.JavaConversions._
import java.awt.Polygon
import java.util.HashMap
import java.io.{ObjectOutputStream, FileOutputStream}
import java.io.FileInputStream
import java.io.ObjectInputStream
import server.model.playerData.Region
import java.awt.geom.Point2D
import engine.general.utility.Location
import engine.rts.model.Resource
import client.clientResources

/**
 * This class contains methods for reading game configuration data from JSON files.
 */
object ResourceReader{
    def readRegionShapes(regions:ArrayList[Region],saveLoc:String){
        var regionShapes:HashMap[Point2D,Polygon]=null
        try{
            var fileIn =new FileInputStream(saveLoc)
            var in = new ObjectInputStream(fileIn)
            regionShapes =in.readObject().asInstanceOf[HashMap[Point2D,Polygon]]
            in.close()
            fileIn.close()
        }
        catch{
            case e:Exception=>e.printStackTrace()
        }
        for((loc:Point2D,poly:Polygon)<-regionShapes){
            var cLoc=new Location(loc.getX(),loc.getY())
            for(region:Region<-regions) {
                if(region.getCenterLoc().equals(cLoc)){
                    region.setBounds(poly)
                }
            }
        }
    }

    /**
     * This method saves the region shapes
     * @param regions
     */
    def saveRegionShapes(regions:ArrayList[Region],saveLoc:String){
        
        var regionShapes=new HashMap[Point2D,Polygon]
        for(region:Region<-regions){
            var cLoc=region.getCenterLoc()
            regionShapes.put(new Point2D.Double(cLoc.getX(),cLoc.getY()),region.getBounds())
        }
        try{
            var fileOut =new FileOutputStream(saveLoc)
            var out =new ObjectOutputStream(fileOut)
            out.writeObject(regionShapes)
            out.close()
            fileOut.close()
        }
        catch{
            case e: Exception =>e.printStackTrace()
         }
    }

    /**
     * This method reads the types of resources that will be used in the game.
     * @return
     */
    def readResources():ArrayList[Resource]={

        var resources=new ArrayList[Resource]()
        println(clientResources.resourceDataLoc)
        var handle=Gdx.files.internal(clientResources.resourceDataLoc)
        val text=handle.readString()
        val jsonResources:Option[Any]=JSON.parseFull(text)  
        for (item<-jsonResources){
            for(subItem<-item.asInstanceOf[Map[Any,Any]]){
                for(resource<-subItem._2.asInstanceOf[List[Any]]){
                    val resourceMap=resource.asInstanceOf[Map[Any,Any]]
                    val newResource=new Resource(resourceMap)
                    resources.add(newResource)
                }
            }
        }

        for (resource <-resources){
            resource.printData()
        }
        return resources
    }

    /**
     * This method will be used to read the types of upgrades that will be used in the game.
     * @param resourceList The list of resources used in the game.
     * @return
     */
    def readUpgrades(resourceList:ArrayList[Resource]):ArrayList[UpgradeDefinition]={
        
        var improvements=new ArrayList[UpgradeDefinition]()
        var handle=Gdx.files.internal(clientResources.improvementDataLoc)
        val text=handle.readString()
        val jsonResources:Option[Any]=JSON.parseFull(text)  

        for (item<-jsonResources){
            for(subItem<-item.asInstanceOf[Map[Any,Any]]){
                for(improvement<-subItem._2.asInstanceOf[List[Any]]){
                    val improvementMap=improvement.asInstanceOf[Map[Any,Any]]
                    val upgrade=new UpgradeDefinition(improvementMap,resourceList)
                    improvements.add(upgrade)
                }
            }
        }
        return improvements
    }

    /**
     * This method is used to read the terrain types used.
     * @return
     */
    def readTerrainTypes():ArrayList[TerrainType]={

        var terrainTypes=new ArrayList[TerrainType]()
        val handle=Gdx.files.internal(clientResources.regionDataLoc)
        val text=handle.readString()
        val jsonResources:Option[Any]=JSON.parseFull(text)  
        
        for (item<-jsonResources){
            for(subItem<-item.asInstanceOf[Map[Any,Any]]){
                for(resource<-subItem._2.asInstanceOf[List[Any]]){
                    val resourceMap=resource.asInstanceOf[Map[Any,Any]]
                    val newResource=new TerrainType(resourceMap)
                    terrainTypes.add(newResource)
                }
            }
        }
        return terrainTypes
    }

    /**
     * This method is used to read the different AI personalities
     */
    def readAIPersonalities():ArrayList[AiPersona]={

        var personaList=new ArrayList[AiPersona]()
        val handle=Gdx.files.internal("mapData/TestMap/AI_Personalities.json")
        val text=handle.readString()
        val jsonResources:Option[Any]=JSON.parseFull(text)  
        
        for (item<-jsonResources){
            for(subItem<-item.asInstanceOf[Map[Any,Any]]){
                for(resource<-subItem._2.asInstanceOf[List[Any]]){
                    val resourceMap=resource.asInstanceOf[Map[Any,Any]]
                    val persona=new AiPersona(resourceMap)
                    personaList.add(persona)
                }
            }
        }
        return personaList
    }

    /**
     * This method reads configuration data about the game from JSON files.
     */
    def readConfigData(){
        ResourceReader.readResources()
        ResourceReader.readUpgrades(Resource.resourceList)
        ResourceReader.readTerrainTypes()
        ResourceReader.readAIPersonalities()
    }
}
