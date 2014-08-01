package network.client

import java.util
import server.model.UpgradeDefinition
import server.clientCom.GameStateData
import java.awt.{Point, Polygon}
import server.model.mapData.{TerrainType}
import util.ArrayList
import engine.rts.model.Resource;
import server.clientCom.{RegionRenderData, PlayerStats}

/**
 * This class defines an interface for the view to interact with the game data.
 */
abstract class GameConnection() extends Thread{

    var improvementData:util.ArrayList[UpgradeDefinition]=null //This represents the list of improvements the game uses.
    var resourceData:util.ArrayList[Resource]=_ //This represents the list of resources the game uses.
    var upgradeData:util.ArrayList[UpgradeDefinition]=_ //This represents the list of improvements the game uses.
    var terrainTypes:util.ArrayList[TerrainType]=_  //This represents the different terrain types that each region can have.
    var gameStateData:GameStateData=_ //This variable represents the parts of the game state that are rendered.
    var regionData:RegionRenderData=_ //This variable represents the data for drawing each region.
    var myStats:PlayerStats=_

    def sendInput(input:Object)
    def getResourceDefs():util.ArrayList[Resource]=resourceData
    def getImprovementDefs():util.ArrayList[UpgradeDefinition]=upgradeData
    def getGameState():GameStateData=gameStateData
    def getRegionBounds():ArrayList[Polygon]=regionData.regionBounds
    def getXPositions():ArrayList[Integer]=regionData.xLocs
    def getYPositions():ArrayList[Integer]=regionData.yLocs

    //For some reason, not specifying a return type with these two methods does not cause problems.
    def getRallyPoints() = myStats.getRallyPoints()
    def getImprovementCount() = upgradeData.size()
    def getRightClick():Point=myStats.getRightClick()
    def getLeftClick():Point=myStats.getLeftClick()
    def nullData():Boolean
    def addMarketCallback(c1:(PlayerStats)=>Unit)
    def addDataCallback(c2:(PlayerStats)=>Unit)

    def addCallbacks(c1:(PlayerStats)=>Unit,c2:(PlayerStats)=>Unit)
    override def run(){
    }
}
