package server.model.playerData

import engine.rts.model.Resource
import scala.collection.JavaConversions._
import server.model.mapData.MapFacade
import server.controller.playerControls.HumanPlayer
import engine.general.utility.Location
import engine.general.utility.Line
import engine.general.utility.IntLoc
import engine.general.model.GamePlayer
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import java.util.concurrent.Semaphore

class Player() extends GamePlayer(){

    private var research=new Research() //This represents the technology level for a player
    private var score=new Population()  //This represents the number of people in the region.
    private var countLock=new Semaphore(1)

    var myTroops:HashMap[Region,Army]=new HashMap[Region,Army]() //The troops that a player has
    var regions:HashSet[Region]=new HashSet[Region]//A list of regions owned by each player
    var borders:HashMap[Region,HashSet[RegionBorder]]=null//This is a list of connections used for moving troops.
    var resources:Stockpile=null //The resources owned by each player
    private var myCapital:Region=null

    var playerList:ArrayList[Player]=null //The list of players in the game.

    def isHuman()=this.isInstanceOf[HumanPlayer]
    def convert()=this.asInstanceOf[HumanPlayer]

    def myResources()=resources
    def myResearch()=research
    def myRegions()=regions
    def myBorders()=borders

    def removeTroop(r:Region){
        myTroops.get(r).removeTroop()
    }

    /**
     *This method will set the capital for a player.
     */
    def setCapital(r:Region){
        myCapital=r
    }

    def setName(){
        myName=myCapital.getNationName()
    }

    def getCapital()= myCapital

    /**
     * This method returns the number of troops that have been lost in each region. The results are used for
     * animations on the cleint.
     * @return
     */
    def getTroopDeaths():HashMap[IntLoc,Integer ]={
        /**
         * Calcuate the losses from defending.
         */
        val  deaths=new HashMap[IntLoc,Integer]()
        for(r:Region<-MapFacade.getRegions()){
            if(myTroops.get(r).getDeathCount()>0){
                var rLoc=r.getCenterLoc()
                var deathCount=myTroops.get(r).getDeathCount()
                deaths.put(rLoc.intLoc,deathCount)
                myTroops.get(r).resetDeathCount()//Reset the death count.
            }
        }
        
        /**
         * Calculate the losses from attacking.
         */
        for(r<-borders.values()){
            for(b:RegionBorder<-r){
                var deathCount=b.getDeaths()
                if(deathCount>0){
                    var iLoc=b.getConflictLoc()
                    deaths.put(iLoc,deathCount)
                    b.resetDeaths()
                }
            }
        }
        return deaths
    }

    def initStockpile(){
        resources=new Stockpile(Resource.getResourceNames)
    }

    /**
     * This method creates armies for each region and creates objects to represent connections between regions.
     * @param rList
     */
    def initCounts(rList:ArrayList[Region]){
        
        borders=new HashMap[Region, HashSet[RegionBorder]]()
        for(r:Region<-rList){
            myTroops.put(r, new Army(this)) //Create a new army for the regin.
            var cur=new HashSet[RegionBorder]()
            for(other<-r.getBorderRegions()){
                var connection=new RegionBorder(this,r,other)
                cur.add(connection)
            }
            borders.put(r,cur)
        }
    }

    def addPlayers(gamePlayers:ArrayList[Player]){
        playerList=gamePlayers
    }

    /**
     * This is a helper method that picks a region to remove troops from during bankruptcy.
     * @return
     */
    def pickLossRegion():Region={
        var armies=new HashSet[Region]()
        for(r<-regions){
            if(r.getTroopCount(this)>0){
                armies.add(r)
            }
        }

        var i=(Math.random*armies.size()).toInt
        for(r<-armies){
            if(i==0){
                return r
            }
            i-=1
        }
        return null
    }

    /**
     * This method is used for players to automatically build troops.
     */
    def autobuild() {
        
        //If we do not have enough troops, start disbanding random troops.
        if(resources.hasMoney()==false){
            var remCount=5
            for(i<-1 until remCount) {
                var r=pickLossRegion()
                if(r==null){
                    return
                }
                removeTroop(r)
            }
            return
        }

        var buildCounts=getBuildCounts
        for(reg:Region<-buildCounts.keySet()){
            for(i<-0 until buildCounts.get(reg)){
                this.buildTroop(reg)
            }
        }
    }

    /**
     * This method adds an army to a region.
     * @param a The army that is being added.
     * @param r The region that the army is being added to.
     */
    def addArmy(a:Army,r:Region){
        myTroops.get(r).combineArmy(a)
    }

    /**
     * @return The number of regions that a player owns
     */
    def getRegionCount()= regions.size()

    def getBuildCounts:HashMap[Region,Integer]={
        
        var prodBonus=research.getProdBonus()
        var buildCounts=new HashMap[Region,Integer]()
        var centerLoc=determineCenter()
        for(region:Region<-regions){
            
            var rLoc=region.getCenterLoc()

            //Make sure the production power of distant regions is reduced.
            var distance=centerLoc.compareDistance(rLoc)
            var rFact=(distance+0.1)/1000
            var x=Math.random*rFact
            if(x<1){
                buildCounts.put(region,1)
            }
            if(distance<50){
                buildCounts.put(region, (10+prodBonus*300/playerList.size()).toInt)
            }
        }
        return buildCounts
    }

    def determineCenter():Location={
        var newLoc=regions.foldLeft(new Location(0,0))((tot,reg)=>tot+reg.getCenterLoc())
        return newLoc/regions.size()
    }

    def addZone(c:Region){
        regions.add(c)
    }

    def getPopulation():Int=regions.foldLeft(0)((tot,reg)=>tot+reg.getPopulation())

    /**
     * When this method is called, a player will get money.
     */
    def receiveMoney(){
        
        resources.income(regions,myCapital,getPopulation(),research);  //Get resource income.
        resources.upkeep(getRegionCount(),getTroopCount()); //Pay upkeep costs.

        //It might be a good idea to place this code somewhere else.
        var growth:Double=resources.getGrowthRate()
        for(r:Region<-regions){
            if(growth<1.0){
                r.growPopulation(growth)
            }
            else{
                r.growPopulation(growth*research.getGrowthBonus())
            }
        }
    }

    /**
     * Counts the number of troops a player has
     * @return The number of troops a player has.
     */
    def countTroops()=myTroops.values().foldLeft(0)((tot,troops)=>tot+troops.size())

    def clearRallyPoints(){
        for(r<-borders.keySet()){
            clearRallyPoints(r)
        }
    }
    
    /**
     * This method clears the rally points for a region
     * @param r
     */
    def clearRallyPoints(r:Region){
        for(connection:RegionBorder<-borders.get(r)){
            connection.turnOff()
        }
    }

    /**
     * Heuristic that returns a player's score.
     * It may be a better idea to regenHP the score somewhere else.
     * @return
     */
    def getScore():Population={
        var total=regions.foldLeft(0)((a,r)=>a+r.getPopulation)
        score.setTotal(total)
        return score
    }

    def removeZone(c:Region){
        regions.remove(c)
    }

    /**
     * This method commands a player to build a troop.
     * @param buildRegion The location where you are building a troop.
     * @return The troop you have built or null if a troop is unable to be built.
     */
    def buildTroop(buildRegion:Region):Troop={
        val t=new Troop(this,buildRegion)
        myTroops.get(buildRegion).add(t)
        return t
    }

    def countTroops(r:Region)=myTroops.get(r).size()
    def getArmy(r:Region):Army=myTroops.get(r)

    //This method is designed to be used by SimpleAttackStrategy.java. It is a hack that needs to be refactored.
    def getTroopData():HashMap[Region,Army]=myTroops

    def getTroopCount():Int={
        var c=0
        for(r:Region<-myTroops.keySet()){
            c+=this.getTroopCount(r)
        }
        return c
    }

    def ownsRegion(r:Region)=regions.contains(r)

    def removeRallyPoint(start:Region,end:Region){
        for(border:RegionBorder<-borders.get(start)){
            if(border.end==end){
                border.turnOff()  //Turn off border.
                return
            }
        }
    }
    
    /**
     * This method sets the rally point for a region. This will command all the troops from one region to move to
     * another region.
     * @param start The start region.
     * @param end The destination region.
     */
    def setRallyPoint(start:Region,end:Region){

        if(start.getOwner()!=this){
            return
        }
        //This is a hack that needs to be refactored into a better solution.
        //The fact that start and end can be null means that there is a major problem.
        if(start!=null&&end!=null){
            //Find the connection that connects the two regions and turns it on.
            for(border<-borders.get(start)){
                if(border.end==end){
                    if(border.isOn()){
                       // border.turnOff()
                    }
                    else{
                        border.turnOn()
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
    def moveTroops(){

        var copy=new HashSet[Region]()
        copy.addAll(regions)
        for(r:Region<-copy){
            for(conn:RegionBorder<-borders.get(r)){
                if(conn.isOn()){
                    if(this.isInstanceOf[HumanPlayer]){
                    	println("Adding rally point")
			    	}
                    var moveArmy=myTroops.get(r).createMovementArmy(research.getMoveBonus()*r.getMovement())//Create army for movement.
                    val dest=conn.moveTroops(moveArmy)
                    this.addArmy(moveArmy,dest)
                    if(dest.getOwner()!=this){
                        dest.setOwner(this)
                    }
                }
            }
        }
    }

    def getConflicts:HashMap[IntLoc,Line]={
        
        var conflicts=new  HashMap[IntLoc,Line]()
        for(regionMoves<-borders.values()){
            for(m:RegionBorder<-regionMoves){
                if(m.hasConflict){
                    conflicts.put(m.getConflictLoc(),m.getLine())
                    m.endConflict();//We want to send data about the conflicts at the same rate that they are happening.
                }
            }
        }
        return conflicts
    }

    def getTroopCount(r:Region):Int= myTroops.get(r).size()

    def act(myPlayers:Array[Player]){
    }
}