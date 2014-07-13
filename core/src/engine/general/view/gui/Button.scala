package engine.general.view.gui

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

class Button( xPos:Integer,yPos:Integer, var text:String,val width:Int,val height:Int) extends GuiItem(xPos,yPos){

    val myColor=Color.WHITE
    //TODO:Figure out a way to define the width and height without hardcoding
    def getWidth=width
    var drawRect=new Rectangle2D.Double(xPos.toDouble,(yPos-height/2).toDouble,width.toDouble,height.toDouble)

    def this(xPos:Integer,yPos:Integer,text:String){
        this(xPos,yPos,text,90,25)
    }

    def draw(g: Graphics){
        g.setColor(Color.WHITE)
        g.asInstanceOf[Graphics2D].fill(drawRect)
        g.setColor(myColor)
        g.asInstanceOf[Graphics2D].draw(drawRect)
        g.setColor(Color.BLACK)
        g.drawString(text,xPos,yPos) //Draw the button text.
    }

    def getText=text
    
    def setText(s:String){
        text=s
    }
    def contains(x:Int,y:Int):Boolean=drawRect.contains(x.toDouble,y.toDouble)
}
