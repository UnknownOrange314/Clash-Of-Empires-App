package client.controller

import java.awt.geom.Point2D

object ClickCommand{
	val LEFT_CLICK=1
	val RIGHT_CLICK=2
}

class ClickCommand(val x:Int, val y:Int,val clickType:Int)  {
	val pt=new Point2D.Float(x,y)
	def getPoint()=pt
}