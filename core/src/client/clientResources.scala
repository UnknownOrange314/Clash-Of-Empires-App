package client

import javax.imageio.ImageIO
import java.io.File

object clientResources {

    var imageDir="images/"
    var combatImageDir="images/combat/"
    var flagDir="images/flags/"
    var configLoc="mapData/TestMap/"

    def armyImage=ImageIO.read(new File(combatImageDir+"troop.png"))
    def siegeImage=ImageIO.read(new File(combatImageDir+"fire.jpg"))
    def battleImage=ImageIO.read(new File(combatImageDir+"battle.png"))

    def mapBackground=ImageIO.read(new File(imageDir+"MapBackground.jpg"))
    def capitalImage=ImageIO.read(new File(imageDir+"MapBackground.jpg"))

    def getFlag(nName:String)=flagDir+nName+".jpg"
    
    def resourceDataLoc=configLoc+"Resources.json"
    def improvementDataLoc=configLoc+"Improvements.json"
    def regionDataLoc=configLoc+"regions.json"
    def personalityLoc=configLoc+"AI_Personalities.json"
}
