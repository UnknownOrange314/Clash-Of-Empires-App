package server.model.mapData

import server.DataLog
import engine.general.model.GamePlayer
import engine.rts.model.{ StratMap}
import server.clientCom.GameStateData
import server.clientCom.RegionRenderData
import server.clientCom.RegionState
import server.controller.playerControls.HumanPlayer
import server.model.ai.AiDirector
import server.model.ai.AiPersona
import server.model.ai.EasyComputerPlayer
import server.model.playerData._
import engine.general.utility.IntLoc
import engine.general.utility.Line
import java.awt.Polygon
import scala.collection.JavaConversions._
import java.util.ArrayList
import java.util.HashMap
import server.model.ai.AiDirObj

/**
 * This class represents the map that the game is played on.
 * @author Bharat
 *
 */
object GameMap {
    private[mapData] final val NEUTRAL_TROOPS: Int = 0
    /**
     * These values represent the intervals between updated.
     */
    private[mapData] final val INCOME_FREQ: Int = 250
    private[mapData] final val RECRUIT_FREQ: Int = 20
    private[mapData] final val COMPUTER_FREQ: Int = 40
    private[mapData] final val MOVE_FREQ: Int = 10
    private[mapData] final val REGION_ATTACK_FREQ: Int = 10
    private[mapData] final val MARKET_FREQ: Int = 10
}

class GameMap extends StratMap{
   
    private var myRegions: ArrayList[Region] = new ArrayList[Region]  //The list of regions in the game.
    private var myMarket: ResourceMarket = null //The market used to exchange resources.
    private var myOptions: GameOption = null
    private[mapData] var init: Boolean = false

    /**
     * @param opt  The list of options for the game.
     */
    def this(opt: GameOption) {
        
        this()
        GamePlayer.setPlayerCount(1)
        myOptions = opt

        //Set the colors for each player
        ResourceReader.readConfigData
        addPlayers(opt)
        setStartingOwners
        myMarket = new ResourceMarket
        myOptions.startTime

        //Give a name to each player
        setPlayerNames()
    }

    /**
     * This method returns the market used to track resource prices.
     * @return
     */
    protected def getMarket=myMarket

    /**
     * Adds players to the game.
     * @param myOptions
     */
    private def addPlayers(myOptions: GameOption) {
        //Add the clients connecting to the server.
        for (human:HumanPlayer <- myOptions.getClients){
            myPlayers.add(human)
        }

        //Fill in the remaining slots with computer players.
        var pCount: Int = myOptions.getPlayerCount
        pCount=5
        for (pNum <-myOptions.getClients.size until pCount){
            myPlayers.add(new EasyComputerPlayer(AiPersona.pickDefaultPersona, pNum,new AiDirObj))
        }

        //Add a neutral players.
        myPlayers.add(new EasyComputerPlayer(AiPersona.pickDefaultPersona, pCount,new AiDirObj))
        for (p <- myPlayers){
            p.initStockpile  //The game crashes if a player's resource stockpile is initialized in the player constructor.
            p.addPlayers(myPlayers)
        }
        
        val mapGen: MapGen = new HexGen
        myRegions=mapGen.generateMap(this)
        
        val cost: Double = (2.0 / myRegions.size)
        Stockpile.setCost(cost)
             
        for (p <- myPlayers) {
            p.initCounts(myRegions)
        }
        
        AiDirector.init(myPlayers, myRegions)
    }

    /**
     * This method returns a test computer player if one exists.
     * @return
     */
    def getTestPlayer: EasyComputerPlayer = {
        for (p <- getPlayers) {
            if (p.isInstanceOf[EasyComputerPlayer]) {
                return p.asInstanceOf[EasyComputerPlayer]
            }
        }
        return null
    }

    /**
     *
     * @return A list of zones that are on the map
     */
    def getRegions: ArrayList[Region] = {
        val temp: ArrayList[Region] = new ArrayList[Region]
        temp.addAll(myRegions)
        return temp
    }

    /**
     * This method randomly sets the starting owners for each city.
     */
    private def setStartingOwners {     
        TerritoryPicker.pickOwners(myRegions, myPlayers,myOptions)
    }

    /**
     * This method gives resources to each player based on the regions controlled.
     */
    private def giveResources {
        for (p <-myPlayers) {
            p.receiveMoney
        }
    }

    /**
     * This method loops through all the regions and resolves conflicts for each player.
     */
    private def battle {
        for (r <- myRegions) {
            for (p <- myPlayers) {
                if ((p != r.getOwner) && (p.countTroops(r)) > 0) {
                    resolveConflict(r)
                }
            }
        }
    }

    /**
     * This method resolves a conflict for a region when there are troops from multiple players in a region.
     * @param r The region for which there is a conflict.
     */
    private def resolveConflict(r: Region) {
        for (p <- myPlayers) {
            val owner: Player = r.getOwner
            if (owner!= p) {
                p.getArmy(r).fight(r.getDefenseBonus, owner.getArmy(r))
            }
        }
    }

    /**
     * This method adds a region to the map.
     * @param r The region that needs to be added to the map.
     */
    def addRegion(r: Region) {
        import java.lang.Math
        if (myRegions == null) {
            myRegions = new ArrayList[Region]
        }
        val d: Double = Math.random * 9
        if (d == 8) r.setType(TerrainType.terrainList.get(3))
        else if (d == 7) r.setType(TerrainType.terrainList.get(2))
        else if (d == 6) r.setType(TerrainType.terrainList.get(1))
        else r.setType(TerrainType.terrainList.get(0))
        myRegions.add(r)
    }

    /**
     * This method carries out actions for each player.
     */
    private def computerActions {

        AiDirector.calculatePower
        for (player <- myPlayers)
        {
            val p2= new Array[Player](myPlayers.size)
            for(i <-0 to myPlayers.size-1)  //Didn't we already add a list of players???
            {
                p2(i)=myPlayers(i)
            }
            player.act(p2)
        }
    }

    /**
     * This method is used to move the troops for each player.
     */
    private def moveTroops {

        for(player<-myPlayers)
        {
            player.moveTroops
        }
    }

    /**
     * This will automatically build troops in every city.
     */
    private def buildTroops {
        for (p <- myPlayers) {
            p.autobuild
        }
    }

    private def updateRegions {
        for (r <- myRegions) {
            r.regenHP
        }
    }

    /**
     * This method updates the game state. It will periodically be called.
     */
    def updateGame {

        for (p <- myPlayers) {
            if (p.isInstanceOf[HumanPlayer]) {
                (p.asInstanceOf[HumanPlayer]).sendStatistics //This probably belongs in the act() method in player.
            }
        }

        if (shouldProcess(GameMap.MARKET_FREQ)) {
            myMarket.updatePrices
        }
        if (shouldProcess(GameMap.INCOME_FREQ)) {
            giveResources
        }
        if (shouldProcess(GameMap.RECRUIT_FREQ)) {
            this.buildTroops
        }
        if (shouldProcess(GameMap.COMPUTER_FREQ)) {
            this.computerActions
        }
        if (shouldProcess(GameMap.MOVE_FREQ)) {
            this.moveTroops
        }
        if (shouldProcess(GameMap.REGION_ATTACK_FREQ)) {
            for (r <- myRegions) {
                r.attack
            }
        }

      
        updateRegions
        battle
    }

    /**
     * This will return the data for the region shapes and the city locations. This method should return an instance
     * of RegionRenderData with the same amount of data regardless of when the game
     */
    def getRegionRenderData: RegionRenderData = {
        val regionBounds: ArrayList[Polygon] = new ArrayList[Polygon]
        val xLocations: ArrayList[Integer] = new ArrayList[Integer]
        val yLocations: ArrayList[Integer] = new ArrayList[Integer]
        import scala.collection.JavaConversions._
        for (r <- myRegions) {
            regionBounds.add(r.getBounds)
            xLocations.add(r.xCenterRender.asInstanceOf[Int])
            yLocations.add(r.yCenterRender.asInstanceOf[Int])
        }
        return new RegionRenderData(regionBounds, xLocations, yLocations)
    }

    /**
     * This method will send a compressed version of the state of the game with the data
     * that could have changed. This returns the region owners and the number of troops
     * in each region.
     */
    def compressData: GameStateData = {
        val regionData: ArrayList[RegionState] = new ArrayList[RegionState]
        for (r <- myRegions) {
            val troopCounts: ArrayList[Integer] = new ArrayList[Integer]
            for (p <- myPlayers) {
                troopCounts.add(p.countTroops(r))
            }
            val regionState = (new RegionState.Builder())
            									.troopCounts(troopCounts)
            									.improvementData(r.getImprovements)
            									.resourceNum(r.getResourceNumber)
            									.name(r.getName)
            									.owner(r.getOwnerNum)
            									.troopProduction(r.getTroopProduction)
            									.income(r.incomeString)
            									.defenseBonus((r.getDefenseBonus).toFloat)
            									.attackBonus((r.getAttackBonus).toFloat)
            									.hitPoints(r.getHitPoints)
            									.terrain(r.getType)
            									.population(r.getPopulation)
            									.capital(r.isCapital)
            									.build()

            regionData.add(regionState)
           
        }
        
        val moveData: HashMap[IntLoc, Line] = new HashMap[IntLoc, Line]
        for (p <- myPlayers) {
            val conflicts: HashMap[IntLoc, Line] = p.getConflicts
            for (cLoc <- conflicts.keySet) {
                moveData.put(cLoc, conflicts.get(cLoc))
            }
        }
        
        val deathCounts: HashMap[IntLoc, Integer] = new HashMap[IntLoc, Integer]
        for (p <- myPlayers) {
            val pDeaths: HashMap[IntLoc, Integer] = p.getTroopDeaths
            import scala.collection.JavaConversions._
            for (i <- pDeaths.keySet) {
                deathCounts.put(i, pDeaths.get(i))
            }
        }
        val nationData: HashMap[String, String] = new HashMap[String, String]
        for (p <- myPlayers) {
            nationData.put(p.getName, "" + p.getPopulation)
        }
        return (new GameStateData.Builder())
        		.deathCounts(deathCounts)
        		.passTime(myOptions.getRemainingTime)
        		.regionStates(regionData)
        		.conflictLocs(moveData)
        		.marketPrices(myMarket.getAllPrices)
        		.nationInfo(nationData)
        		.build();
    }
}
