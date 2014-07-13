package client.controller

import network.Client.GameConnection
import client.view.Camera
import client.view.panels.{InfoPanel, RegionInfo, GameDisplay}
import java.awt.Shape

class InputProcess(val connection:GameConnection,val gameView:Camera,val disp:GameDisplay,val regionControl:RegionInfo,val playerInfo:InfoPanel) {
   
    disp.addKeyListener(new KeyInputListener(connection,gameView,disp))
    var mList=new MouseInputListener(connection,gameView,regionControl,playerInfo)
    disp.addMouseListener(mList)
    def getClick=mList.getClick
    def containsLeftClick(s:Shape)=mList.containsLeftClick(s)
}
