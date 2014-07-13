package  engine.general.view.gui

import java.awt.Graphics
import java.awt.Color

class Label(xPos:Integer,yPos:Integer,var text:String) extends GuiItem(xPos,yPos){
    def draw(g:Graphics){
        g.setColor(Color.WHITE)

        g.drawString(text,xPos,yPos)
    }

    def setText(s:String){
        text=s
    }
}
