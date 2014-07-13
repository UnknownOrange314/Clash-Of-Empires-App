package  engine.general.view.gui

import java.awt.Graphics

abstract class GuiItem(val xPos:Integer,val yPos:Integer){
    def draw(g:Graphics)
}
