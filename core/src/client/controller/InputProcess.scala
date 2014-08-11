package client.controller

import com.badlogic.gdx.Gdx;

import network.client.GameConnection
import client.view.Camera
import client.view.panels.{InfoPanel, RegionInfo, GameDisplay}
import java.awt.Shape

class InputProcess(val connection:GameConnection,val gameView:Camera,val disp:GameDisplay,val regionControl:RegionInfo,val playerInfo:InfoPanel) {
   
    var mList=new InputListener(connection,gameView,regionControl,playerInfo)
    Gdx.input.setInputProcessor(mList)
    def getClick=mList.getClickPoint()
    def containsLeftClick(s:Shape)=mList.containsLeftClick(s)
}