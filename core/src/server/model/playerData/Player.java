package server.model.playerData;

import engine.rts.model.Resource;
import server.model.mapData.MapFacade;
import server.controller.playerControls.HumanPlayer;
import engine.general.utility.Location;
import engine.general.utility.Line;
import engine.general.utility.IntLoc;
import engine.general.model.GamePlayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Player extends GamePlayer{

    private Research research=new Research(); //This represents the technology level for a player
    private Population score=new Population();  //This represents the number of people in the region.
    private Semaphore countLock=new Semaphore(1);

    HashMap<Region,Army> myTroops=new HashMap<Region,Army>();
    protected HashSet<Region> regions=new HashSet<Region>();
    HashMap<Region,HashSet<RegionBorder>> borders=null;
    
    protected Stockpile resources=null;
    Region myCapital=null;

    protected ArrayList<Player> playerList=null;

    public Player(){
    	super();
    }
    public boolean isHuman(){
    	return (this instanceof HumanPlayer);
    }
    
    //TODO:Find a way to refactor this method.
    public HumanPlayer convert(){
    	return (HumanPlayer)this;
    }
    
    public Stockpile myResources(){
    	return resources;
    }
    
    public Research myResearch(){
    	return research;
    }
    
    public HashSet<Region> myRegions(){
    	return regions;
    }
    
    public HashMap<Region,HashSet<RegionBorder>> myBorders(){
    	return borders;
    }

    public void removeTroop(Region r){
        myTroops.get(r).removeTroop();
    }

    /**
     *This method will set the capital for a player.
     */
    public void setCapital(Region r){
        myCapital=r;
    }

    public void setName(){
        setName(myCapital.getNationName());
    }

    public Region getCapital(){
    	return myCapital;
    }

    /**
     * This method returns the number of troops that have been lost in each region. The results are used for
     * animations on the client.
     * @return
     */
    public HashMap<IntLoc,Integer>getTroopDeaths(){
        /**
         * Calcuate the losses from defending.
         */
        HashMap<IntLoc,Integer> deaths=new HashMap<IntLoc,Integer>();
        for(Region r:MapFacade.getRegions()){
            if(myTroops.get(r).getDeathCount()>0){
                Location rLoc=r.getCenterLoc();
                int deathCount=myTroops.get(r).getDeathCount();
                deaths.put(rLoc.intLoc(),deathCount);
                myTroops.get(r).resetDeathCount();//Reset the death count.
            }
        }
        
        /**
         * Calculate the losses from attacking.
         */
        for(HashSet<RegionBorder> r:borders.values()){
            for(RegionBorder b:r){
                int deathCount=b.getDeaths();
                if(deathCount>0){
                    IntLoc iLoc=b.getConflictLoc();
                    deaths.put(iLoc,deathCount);
                    b.resetDeaths();
                }
            }
        }
        return deaths;
    }

    //TODO: Find a way to initialize the resource stockpile without this method.
    public void initStockpile(){
        resources=new Stockpile(Resource.getResourceNames());
    }

    /**
     * This method creates armies for each region and creates objects to represent connections between regions.
     * @param rList
     */
    public void initCounts(ArrayList<Region> rList){
        
        borders=new HashMap<Region, HashSet<RegionBorder>>();
        for(Region r:rList){
            myTroops.put(r, new Army(this)); //Create a new army for the regin.
            HashSet<RegionBorder> cur=new HashSet<RegionBorder>();
            for(Region other:r.getBorderRegions()){
                RegionBorder connection=new RegionBorder(this,r,other);
                cur.add(connection);
            }
            borders.put(r,cur);
        }
    }

    public void addPlayers(ArrayList<Player> gamePlayers){
        playerList=gamePlayers;
    }

    /**
     * This is a helper method that picks a region to remove troops from during bankruptcy.
     * @return
     */
    public Region pickLossRegion(){
        HashSet<Region> armies=new HashSet<Region>();
        for(Region r:regions){
            if(r.getTroopCount(this)>0){
                armies.add(r);
            }
        }
        int i=(int)(Math.random()*armies.size());
        for(Region r:armies){
            if(i==0){
                return r;
            }
            i-=1;
        }
        return null;
    }

    /**
     * This method is used for players to automatically build troops.
     */
    public void autobuild() {
        
        //If we do not have enough troops, start disbanding random troops.
        if(resources.hasMoney()==false){
            int remCount=5;
            for(int i=1;i<remCount;i++) {
                Region r=pickLossRegion();
                if(r==null){
                    return;
                }
                removeTroop(r);
            }
            return;
        }

        HashMap<Region,Integer> buildCounts=getBuildCounts();
        for(Region reg:buildCounts.keySet()){
            for(int i=0;i<buildCounts.get(reg);i++){
                this.buildTroop(reg);
            }
        }
    }

    /**
     * This method adds an army to a region.
     * @param a The army that is being added.
     * @param r The region that the army is being added to.
     */
    public void addArmy(Army a,Region r){
        myTroops.get(r).combineArmy(a);
    }

    /**
     * @return The number of regions that a player owns
     */
    public int getRegionCount(){
    	return regions.size();
    }

    public HashMap<Region,Integer> getBuildCounts(){
    	
        double prodBonus=research.getProdBonus();
        HashMap<Region,Integer> buildCounts=new HashMap<Region,Integer>();
        Location centerLoc=determineCenter();
        for(Region r:regions){
        	Location rLoc=r.getCenterLoc();

            //Make sure the production power of distant regions is reduced.
            double distance=centerLoc.compareDistance(rLoc);
            double rFact=(distance+0.1)/1000;
            double x=Math.random()*rFact;
            if(x<1){
                buildCounts.put(r,1);
            }
            if(distance<50){
                buildCounts.put(r, (int)(10+prodBonus*300/playerList.size()));
            }
        }
        return buildCounts;
    }

    public Location determineCenter(){
    	Location newLoc=new Location(0,0);
    	for(Region r:regions){
    		newLoc=newLoc.add(r.getCenterLoc());
    	}
    	return newLoc.divide(regions.size());
    }

    public void addZone(Region c){
        regions.add(c);
    }

    public int getPopulation(){
    	int total=0;
    	for(Region r:regions){
    		total+=r.getPopulation();
    	}
    	return total;
    }

    /**
     * When this method is called, a player will get money.
     */
    public void receiveMoney(){
        
        resources.income(regions,myCapital,getPopulation(),research);  //Get resource income.
        resources.upkeep(getRegionCount(),countTroops()); //Pay upkeep costs.

        //It might be a good idea to place this code somewhere else.
        double growth=resources.getGrowthRate();
        for(Region r:regions){
            if(growth<1.0){
                r.growPopulation(growth);
            }
            else{
                r.growPopulation(growth*research.getGrowthBonus());
            }
        }
    }

    /**
     * Counts the number of troops a player has
     * @return The number of troops a player has.
     */
    public int countTroops(){
    	int total=0;
    	for(Army army:myTroops.values()){
    		total+=army.size();
    	}
    	return total;
    }

    public void clearRallyPoints(){
        for(Region r:borders.keySet()){
            clearRallyPoints(r);
        }
    }
    
    /**
     * This method clears the rally points for a region
     * @param r
     */
    public void clearRallyPoints(Region r){
        for(RegionBorder connection:borders.get(r)){
            connection.turnOff();
        }
    }

    /**
     * Heuristic that returns a player's score.
     * @return
     */
    public Population getScore(){
    	int total=0;
    	for(Region r:regions){
    		total+=r.getPopulation();
    	}
    	score.setTotal(total);
        return score;
    }

    public void removeZone(Region c){
        regions.remove(c);
    }

    /**
     * This method commands a player to build a troop.
     * @param buildRegion The location where you are building a troop.
     * @return The troop you have built or null if a troop is unable to be built.
     */
    public Troop buildTroop(Region buildRegion){
        Troop t=new Troop(this);
        myTroops.get(buildRegion).add(t);
        return t;
    }

    public int countTroops(Region r){
    	return myTroops.get(r).size();
    }
    
    public Army getArmy(Region r){
    	return myTroops.get(r);
    }

    //This method is designed to be used by SimpleAttackStrategy.java. It is a hack that needs to be refactored.
    public HashMap<Region,Army> getTroopData(){
    	return myTroops;
    }



    public boolean ownsRegion(Region r){
    	return regions.contains(r);
    }

    public void removeRallyPoint(Region start,Region end){
        for(RegionBorder border:borders.get(start)){
            if(border.getDestination()==end){
                border.turnOff();  //Turn off border.
                return;
            }
        }
    }
    
    /**
     * This method sets the rally point for a region. This will command all the troops from one region to move to
     * another region.
     * @param start The start region.
     * @param end The destination region.
     */
    public void setRallyPoint(Region start, Region end){

        if(start.getOwner()!=this){
            return;
        }
        //This is a hack that needs to be refactored into a better solution.
        //The fact that start and end can be null means that there is a major problem.
        if(start!=null&&end!=null){
            //Find the connection that connects the two regions and turns it on.
            for(RegionBorder border:borders.get(start)){
                if(border.getDestination()==end){
                    if(border.isOn()){
                       // border.turnOff()
                    }
                    else{
                        border.turnOn();
                    }
                }
            }
        }
    }

    /**
     * This method will be called to move troops to a location.
     * TODO:  Make sure that a stronger player does not gain a unfair advantage.
     * TODO:  Make it harder to take undefended regions after breaking through a front line.
     * If it is too easy to take undefended regions after a breakthrough, the attacker gains an unfair advantage.t.
     * Possible solutions
     *  -Make troops move more slowly: This would slow down attacking, but it would also make it harder for a player to deal with a breakthrough.
     *  -Give each player a region to be the capital and prevent troops from fighting or moving if there are cut off: This forces the attacker to leave troops behind.
     *  -Force a player to occupy a region for a period of time before it changes ownership.
     */
    public void moveTroops(){

        HashSet<Region> copy=new HashSet<Region>();
        copy.addAll(regions);
        for(Region r:copy){
            for(RegionBorder conn:borders.get(r)){
                if(conn.isOn()){
                    Army moveArmy=myTroops.get(r).createMovementArmy(research.getMoveBonus()*r.getMovement());//Create army for movement.
                    Region dest=conn.moveTroops(moveArmy);
                    this.addArmy(moveArmy,dest);
                    if(dest.getOwner()!=this){
                        dest.setOwner(this);
                    }
                }
            }
        }
    }

    public HashMap<IntLoc,Line> getConflicts(){      
        HashMap<IntLoc,Line> conflicts=new HashMap<IntLoc,Line>();
        for(HashSet<RegionBorder> regionMoves:borders.values()){
            for(RegionBorder m:regionMoves){
                if(m.hasConflict()){
                    conflicts.put(m.getConflictLoc(),m.getLine());
                    m.endConflict();//We want to send data about the conflicts at the same rate that they are happening.
                }
            }
        }
        return conflicts;
    }

    public int getTroopCount(Region r){
    	return myTroops.get(r).size();
    }

    public void act(ArrayList<Player> myPlayers){
    }
}