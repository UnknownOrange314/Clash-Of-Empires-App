package server.model.mapData;

import engine.general.model.GamePlayer;
import engine.rts.model.StratMap;
import server.clientCom.GameStateData;
import server.clientCom.RegionRenderData;
import server.clientCom.RegionState;
import server.controller.playerControls.HumanPlayer;
import server.model.ai.AiDirector;
import server.model.ai.AiPersona;
import server.model.ai.EasyComputerPlayer;
import engine.general.utility.IntLoc;
import engine.general.utility.Line;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;

import server.model.playerData.Region;
import server.model.playerData.Player;
import server.model.playerData.Stockpile;

/**
 * This class represents the map that the game is played on.
 * @author Bharat
 *
 */
class GameMap extends StratMap {
	
    final static int NEUTRAL_TROOPS = 0;
    
    public static int getNeutrals(){
    	return NEUTRAL_TROOPS;
    }
    
    /**
     * These values represent the intervals between updated.
     */
    final static int INCOME_FREQ = 250;
    final static int RECRUIT_FREQ= 20;
    final static int COMPUTER_FREQ = 40;
    final static int MOVE_FREQ = 10;
    final static int REGION_ATTACK_FREQ = 10;
    final static int MARKET_FREQ = 10;

   
    private ArrayList<Region> myRegions=new ArrayList<Region>(); //The list of regions in the game.
    private ResourceMarket myMarket = null; //The market used to exchange resources.
    private GameOption myOptions = null;
    private boolean init=false;

    /**
     * @param opt  The list of options for the game.
     */
    public GameMap(GameOption opt) {
       
        GamePlayer.setPlayerCount(1);
        myOptions = opt;

        //Set the colors for each player
        ResourceReader.readConfigData();
        addPlayers(opt);
        setStartingOwners();
        myMarket = new ResourceMarket();
        myOptions.startTime();

        //Give a name to each player
        setPlayerNames();
    }

    /**
     * This method returns the market used to track resource prices.
     * @return
     */
    protected ResourceMarket getMarket(){
    	return myMarket;
    }

    /**
     * Adds players to the game.
     * @param myOptions
     */
    private void addPlayers(GameOption myOptions) {
        //Add the clients connecting to the server.
        for (HumanPlayer human: myOptions.getClients()){
            myPlayers.add(human);
        }

        //Fill in the remaining slots with computer players.
        int pCount= myOptions.getPlayerCount();
        pCount=5;
        for (int pNum=myOptions.getClients().size(); pNum< pCount;pNum++){
            myPlayers.add(new EasyComputerPlayer(AiPersona.pickDefaultPersona(), pNum,new AiDirector()));
        }

        //Add a neutral players.
        myPlayers.add(new EasyComputerPlayer(AiPersona.pickDefaultPersona(), pCount,new AiDirector()));
        for (Player p:myPlayers){
            p.initStockpile();  //The game crashes if a player's resource stockpile is initialized in the player constructor.
            p.addPlayers(myPlayers);
        }
        
        MapGen mapGen= new HexGen();
        myRegions=mapGen.generateMap(this);
        
        double cost= (2.0 / myRegions.size());
        Stockpile.setMaintenanceFactor(cost);
             
        for (Player p:myPlayers) {
            p.initCounts(myRegions);
        }
        
        AiDirector.init(myPlayers, myRegions);
    }

    /**
     * This method returns a test computer player if one exists.
     * @return
     */
    public EasyComputerPlayer getTestPlayer(){
        for (Player p:super.getPlayers()) {
            if (p instanceof EasyComputerPlayer) {
                return (EasyComputerPlayer)p;
            }
        }
        return null;
    }

    /**
     *
     * @return A list of zones that are on the map
     */
    public ArrayList<Region> getRegions() {
        ArrayList<Region> temp = new ArrayList<Region>();
        temp.addAll(myRegions);
        return temp;
    }

    /**
     * This method randomly sets the starting owners for each city.
     */
    private void setStartingOwners() {     
        TerritoryPicker.pickOwners(myRegions, myPlayers,myOptions);
    }

    /**
     * This method gives resources to each player based on the regions controlled.
     */
    private void giveResources() {
        for (Player p:myPlayers) {
            p.receiveMoney();
        }
    }

    /**
     * This method loops through all the regions and resolves conflicts for each player.
     */
    private void battle() {
        for (Region r: myRegions) {
            for (Player p : myPlayers) {
                if ((p != r.getOwner()) && (p.countTroops(r)) > 0) {
                    resolveConflict(r);
                }
            }
        }
    }

    /**
     * This method resolves a conflict for a region when there are troops from multiple players in a region.
     * @param r The region for which there is a conflict.
     */
    private void resolveConflict(Region r) {
        for (Player p : myPlayers) {
            Player owner = r.getOwner();
            if (owner!= p) {
                p.getArmy(r).fight(r.getDefenseBonus(), owner.getArmy(r));
            }
        }
    }


    /**
     * This method carries out actions for each player.
     */
    private void computerActions() {
        AiDirector.calculatePower();
        for (Player player:myPlayers){
            player.act(myPlayers);
        }
    }

    /**
     * This method is used to move the troops for each player.
     */
    private void moveTroops() {
        for(Player player:myPlayers){
            player.moveTroops();
        }
    }

    /**
     * This will automatically build troops in every city.
     */
    private void buildTroops() {
        for (Player p : myPlayers) {
            p.autobuild();
        }
    }

    private void updateRegions() {
        for (Region r:myRegions) {
            r.regenHP();
        }
    }

    /**
     * This method updates the game state. It will periodically be called.
     */
    public void updateGame() {

        for (Player p: myPlayers) {
            if (p instanceof HumanPlayer) {
                ((HumanPlayer)p).sendStatistics(); //This probably belongs in the act() method in player.
            }
        }

        if (shouldProcess(GameMap.MARKET_FREQ)) {
            myMarket.updatePrices();
        }
        if (shouldProcess(GameMap.INCOME_FREQ)) {
            giveResources();
        }
        if (shouldProcess(GameMap.RECRUIT_FREQ)) {
            this.buildTroops();
        }
        if (shouldProcess(GameMap.COMPUTER_FREQ)) {
            this.computerActions();
        }
        if (shouldProcess(GameMap.MOVE_FREQ)) {
            this.moveTroops();
        }
        if (shouldProcess(GameMap.REGION_ATTACK_FREQ)) {
            for (Region r:myRegions) {
                r.attack();
            }
        }   
        updateRegions();
        battle();
    }

    /**
     * This will return the data for the region shapes and the city locations. This method should return an instance
     * of RegionRenderData with the same amount of data regardless of when the game
     */
    public RegionRenderData getRegionRenderData(){
        ArrayList<Polygon> regionBounds = new ArrayList<Polygon>();
        ArrayList<Integer> xLocations = new ArrayList<Integer>();
        ArrayList<Integer> yLocations = new ArrayList<Integer>();
        for (Region r:myRegions) {
            regionBounds.add(r.getBounds());
            xLocations.add((int)r.xCenterRender());
            yLocations.add((int)r.yCenterRender());
        }
        return (new RegionRenderData.Builder())
        		.rBounds(regionBounds)
        		.xLocs(xLocations)
        		.yLocs(yLocations)
        		.build();
    }

    /**
     * This method will send a compressed version of the state of the game with the data
     * that could have changed. This returns the region owners and the number of troops
     * in each region.
     */
    public GameStateData compressData(){
        ArrayList<RegionState> regionData = new ArrayList<RegionState>();
        for (Region r :myRegions) {
            ArrayList<Integer >troopCounts = new ArrayList<Integer>();
            for (Player p:myPlayers) {
                troopCounts.add(p.countTroops(r));
            }
            RegionState regionState = (new RegionState.Builder())
            									.troopCounts(troopCounts)
            									.improvementData(r.getImprovements())
            									.resourceNum(r.getResourceNumber())
            									.name(r.getName())
            									.owner(r.getOwnerNum())
            									.troopProduction((float)r.getTroopProduction())
            									.income(r.incomeString())
            									.defenseBonus((float)(r.getDefenseBonus()))
            									.attackBonus((float)(r.getAttackBonus()))
            									.hitPoints(r.getHitPoints())
            									.terrain(r.getType())
            									.population(r.getPopulation())
            									.capital(r.isCapital())
            									.build();
            regionData.add(regionState);        
        }
        
        HashMap<IntLoc,Line> moveData = new HashMap<IntLoc, Line>();
        for (Player p:myPlayers) {
            HashMap<IntLoc,Line> conflicts = p.getConflicts();
            for (IntLoc cLoc :conflicts.keySet()) {
                moveData.put(cLoc, conflicts.get(cLoc));
            }
        }
        
        HashMap<IntLoc, Integer> deathCounts = new HashMap<IntLoc, Integer>();
        for (Player p : myPlayers) {
            HashMap<IntLoc, Integer>pDeaths = p.getTroopDeaths();
            for (IntLoc i:pDeaths.keySet()) {
                deathCounts.put(i, pDeaths.get(i));
            }
        }
        
        HashMap<String,String> nationData= new HashMap<String, String>();
        for (Player p:myPlayers) {
            nationData.put(p.getName(), "" + p.getPopulation());
        }
        
        return (new GameStateData.Builder())
        		.deathCounts(deathCounts)
        		.passTime(myOptions.getRemainingTime())
        		.regionStates(regionData)
        		.conflictLocs(moveData)
        		.marketPrices(myMarket.getAllPrices())
        		.nationInfo(nationData)
        		.build();
    }
}