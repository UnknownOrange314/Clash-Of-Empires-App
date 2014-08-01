package client.controller

import network.client.GameConnection
import client.view.Camera
import client.view.panels.{InfoPanel, RegionInfo, GameDisplay}
import java.awt.Shape

class InputProcess(val connection:GameConnection,val gameView:Camera,val disp:GameDisplay,val regionControl:RegionInfo,val playerInfo:InfoPanel) {
   
	System.err.println("Input not working")
    var mList=new MouseInputListener(connection,gameView,regionControl,playerInfo)
    def getClick=mList.getClick
    def containsLeftClick(s:Shape)=mList.containsLeftClick(s)
}
