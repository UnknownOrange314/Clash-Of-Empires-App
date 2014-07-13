package engine.general.view.gui

import java.awt.{Image, Graphics}

class IconLabel(xPos:Integer,yPos:Integer,val image:Image,val size:Integer) extends GuiItem(xPos,yPos){
    def draw(g: Graphics){
        g.drawImage(image,xPos,yPos,size,size,null)
    }
}
