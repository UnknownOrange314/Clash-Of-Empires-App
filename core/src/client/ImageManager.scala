package client

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import javax.imageio.ImageIO
import java.io.File
import network.client.GameConnection
import engine.rts.model.Resource
import java.util._
import java.awt.image.BufferedImage
import scala.collection.JavaConversions._

/**
 * This class is responsible for loading images and storing them.
 */
class ImageManager(val serverConnection:GameConnection) {

    val imageDir="images/"
    val combatImageDir="images/combat/"
    val flagDir="images/flags/"
    val configLoc="mapData/TestMap/"

    val armyImage=new Texture(combatImageDir+"troop.png")
    val siegeImage=new Texture(combatImageDir+"fire.jpg")
    val battleImage=new Texture(combatImageDir+"battle.png")
    val mapBackground=new Texture(imageDir+"MapBackground.jpg")
    val capitalImage=new Texture(imageDir+"MapBackground.jpg")

    var resourceImages=new ArrayList[Texture]
    var improvementImages=new ArrayList[Texture]
    
    readImages()

    def getFlag(pName:String)=flagDir+pName+".jpg"
    def unreadImages:Boolean=resourceImages==null
    def getUpgradeImage(uNum:Int)=improvementImages.get(uNum)
    def getResourceImage(rNum:Int)=resourceImages.get(rNum)
       
    /**
     * This method reads the images that represent the resources and improvements.
     */
    private def readImages() {

        val resourceData:ArrayList[Resource]=serverConnection.getResourceDefs()
        try {
            resourceData.foreach {resource:Resource=>
            	val resourceImage = new Texture("images/resources/" + resource.getResourceFile())
            	resourceImages.add(resourceImage)
            }
            serverConnection.improvementDefs().foreach {improvement=>
                val upgradeImage = new Texture("images/improvements/" + improvement.getImageLocation())
                improvementImages.add(upgradeImage)
            }
        } 
        catch {
            case e: Exception => e.printStackTrace()
        }
    }
}