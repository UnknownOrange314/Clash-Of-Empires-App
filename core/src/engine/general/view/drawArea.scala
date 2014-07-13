package engine.general.view

import scala.collection.JavaConversions._
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import client.view.panels.GameDisplay
import java.util
import engine.general.view.gui.GuiItem

/**
 *This class represents drawing panels for the game
 */
class drawArea(val myX:Integer,val myY:Integer, val width:Integer,val height:Integer){

    var components=new util.ArrayList[GuiItem]()
    val image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB)//All the components will be drawn to this image.
    val imageGraphics=image.getGraphics.asInstanceOf[Graphics2D]
    def getImage():BufferedImage=image
    def containsPoint(x:Int,y:Int)=x>myX&&x<myX+width&&y>myY&&y<myY+height //Is a point within the dimensions of the component.

    def clearImage(){
        imageGraphics.clearRect(0,0,image.getWidth,image.getHeight)
        imageGraphics.setColor(Color.BLACK)
        imageGraphics.fillRect(0,0,image.getWidth,image.getHeight)
    }

    /**
     * Draws the component on the screen.
     * @param pGraphics The graphics object associated with the frame.
     * @param disp The frame that contains the game output.
     */
    def drawToPanel(pGraphics:Graphics,disp:Display){
        pGraphics.drawImage(image,myX,myY,disp)
    }

    def render(g:Graphics){
        clearImage()
        for(component<-components){
            component.draw(g)
        }
    }
}
