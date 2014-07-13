package client

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import javax.imageio.ImageIO
import java.io.File
import network.Client.GameConnection
import engine.rts.model.Resource
import java.util._
import java.awt.image.BufferedImage
import scala.collection.JavaConversions._

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

    var resourceImages: ArrayList[BufferedImage] = null
    var improvementImages: ArrayList[BufferedImage] = null

    def getFlag(nName:String)=flagDir+nName+".jpg"
    def unreadImages:Boolean=resourceImages==null

    def getUpgradeImage(uNum:Int)=improvementImages.get(uNum)
    def getResourceImage(rNum:Int):BufferedImage={
        return resourceImages.get(rNum)
    }
    
    /**
     * This method reads the images that represent the resources and improvements.
     */
     def readImages() {

        resourceImages = new ArrayList[BufferedImage]()
        improvementImages = new ArrayList[BufferedImage]()
        val resourceData:ArrayList[Resource]=serverConnection.getResourceDefs()
        println("Resource data:"+resourceData)

        try {
            resourceData.foreach {resource:Resource=>
            val resourceImage = ImageIO.read(new File("images/resources/" + resource.getResourceFile()))
            resourceImages.add(resourceImage)
        }

        //Add the list of improvements.
        serverConnection.getImprovementDefs().foreach {improvement=>
                val upgradeImage = ImageIO.read(new File("images/improvements/" + improvement.getImageLocation()))
                improvementImages.add(upgradeImage)
            }

        }
        
        catch {
            case e: Exception => e.printStackTrace()
        }
    }
}
