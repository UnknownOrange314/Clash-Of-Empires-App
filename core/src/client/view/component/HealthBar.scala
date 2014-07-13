package client.view.component

import java.awt.Graphics
import java.awt.Color
import engine.general.view.gui.GuiItem

class HealthBar(x:Int,y:Int, val w:Int,val h:Int,val maxVal:Int) extends GuiItem(x,y){

    var curVal=maxVal
    def setValue(c:Int)=curVal=c

    def draw(g:Graphics){
        g.setColor(Color.RED)
        g.fillRect(x,y,w,h)
        var left=w*curVal/maxVal
        g.setColor(Color.GREEN)
        g.fillRect(x,y,left,h)
    }
}
