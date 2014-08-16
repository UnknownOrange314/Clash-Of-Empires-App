package client.view.panels

import client.ImageManager
import network.client.GameConnection
import com.mygdx.game.MyGdxGame

object InterfaceBuilder {
  
    val SCORE_X=MyGdxGame.WIDTH
	val SCORE_Y=0
	val SCORE_WIDTH=300
	val SCORE_HEIGHT=300
	def createScorePanel(data:ImageManager):ScorePanel={
	    return new ScorePanel(data,SCORE_X,SCORE_Y,SCORE_WIDTH,SCORE_HEIGHT)
	}
	
	val RINFO_X=0
	val RINFO_Y=0
	val RINFO_WIDTH=600
	val RINFO_HEIGHT=350
	def createRegionPanel(serverConnection:GameConnection):RegionInfo={
	    return new RegionInfo(RINFO_X,RINFO_Y,RINFO_WIDTH,RINFO_HEIGHT,serverConnection)
	}
	
	var LOG_X=1500
	var LOG_Y=0
	var LOG_WIDTH=400
	var LOG_HEIGHT=200
	def createLogPanel(serverConnection:GameConnection):LogPanel={
        return new LogPanel(LOG_X,LOG_Y,LOG_WIDTH,LOG_HEIGHT,serverConnection)
	}
	
	//The dimensions of the game map TODO: Make sure this isn't hardcoded
	var MAP_WIDTH=2000
	var MAP_HEIGHT=2000
	var MINIMAP_X=1500
	var MINIMAP_Y=700
	var MINIMAP_HEIGHT=400
	var MINIMAP_WIDTH=400
	def createMinimap():Minimap={
        return new Minimap(MAP_WIDTH,MAP_HEIGHT,MINIMAP_X,MINIMAP_Y,MINIMAP_HEIGHT,MINIMAP_WIDTH)
	}
	
	var INFO_X=1500
	var INFO_Y=200
	var INFO_WIDTH=400
	var INFO_HEIGHT=500
	def createInfoPanel(serverConnection:GameConnection):InfoPanel={
        return new InfoPanel(INFO_X,INFO_Y,INFO_WIDTH,INFO_HEIGHT,serverConnection)
	}
}