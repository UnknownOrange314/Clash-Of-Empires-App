package server.model.playerData

import collection.JavaConverters._
import scala.collection.JavaConversions._
import java.awt._
import java.awt.geom.Rectangle2D
import java.util._
import engine.general.utility.IntLoc
import engine.general.utility.Location
import engine.rts.model.RegionDef
import engine.rts.model.Resource
import server.model.mapData.TerrainType
import server.model.UpgradeDefinition
import collection.mutable.Map

object Region{
    val  MAX_POINTS=10000
    val HP_LOSS=100 //The hit points that are subtracted from a region when it is attacked and has no defending troops.
}

/**
 * This class represents the physical regions in the game.
 * @author Bharat
 */
class Region(var myOwner:Player,val myLocation:Location) extends RegionDef{
    
    private val myEconomy=new Economy()
    private var REGION_DISTANCES:Map[Region,Double]=null//This represents the distances to each region.
    private var regionType:TerrainType=null //This object describes the terrain of this region.
    
    /**
     * This is the number of hit points a region has. When you attack a region with no troops, it will lose hit points.
     * When you attack a region with 0 hit points, you take control of that region. Hit points regenerate over time.
     */
    private var hitPoints=Region.MAX_POINTS

    /**
     * This is a list of regions that are adjacent to this one. Troops can only move between adjacent regions.
     */
    var BORDER_ZONES=new ArrayList[Region]()

    /**
     * This method randomly determines the resources that a region will have.
     */
    def findResources()=myEconomy.findResources()

    /**
     * This method calculates the upgrade score for a region.
     * @return
     */
    def getUpgradeScore()=myEconomy.myUpgrades.size

    /**
     * This method calculates the distances between regions and saves them.
     */
    def updateDistances(){
        REGION_DISTANCES=Map[Region,Double]()
        //Perform a BFS to search regions.
        val queue=new java.util.LinkedList[Region]()
        queue.add(this)
        REGION_DISTANCES.put(this,0.0)
        while(queue.isEmpty()==false){
            var r=queue.remove()
            var distance=REGION_DISTANCES(r)
            val addR=r.getBorderRegions().filter(reg=>(REGION_DISTANCES.contains(reg))==false)
            queue.addAll(addR)
            REGION_DISTANCES++=addR.map(border=>(border,distance+1)).toMap
        }
    }
    
    def getMoveCost()=getTerrain().getMoveCost

    def getHitPoints()=hitPoints

    def resetHitPoints(){
        hitPoints=Region.MAX_POINTS
    }

    def hasHitPoints()=hitPoints>0

    /**
     * This method subtracts hit points from a region under attack that has no defending troops.
     */
    def loseHitPoints(){
        hitPoints=Math.max(0,hitPoints-Region.HP_LOSS)
    }

    /**
     * This method regenerates hit points
     */
    def regenHP(){
        hitPoints=Math.min(hitPoints+1,Region.MAX_POINTS)
        if(isCapital()){
            hitPoints=Math.min(hitPoints+10,Region.MAX_POINTS)
        }
    }

    /**
     * Add an upgrade to this region.
     * @param upgrade The upgrade that is being added to the region.
     */
    def addUpgrade(upgrade:UpgradeDefinition)=myEconomy.addUpgrade(upgrade)

    def getType()=regionType.getName

    def getTerrain()=regionType
    
    /**
     * Remove borders with water regions.
     */
    def removeWaterBorder(){
        var iter=BORDER_ZONES.iterator()
        while(iter.hasNext()){
            var border=iter.next()
            if(border.getType().equals("water")){
                iter.remove()
            }
        }
        if(regionType.equals(TerrainType.getType("water"))){
            BORDER_ZONES.clear()
        }
    }

    /**
     * Set the region type for this region
     * @param r
     */
    def setType(r:TerrainType){
        regionType=r
    }

    /**
     * @return The number of troops that a region owner has in that region.
     */
    def getOwnerTroopCount()=myOwner.countTroops(this)
    def isCapital()= myOwner.getCapital()==this;

    /**
     * This method returns the defense bonus for a region.
     * @return The defense bonus for a region.
     */
    def getDefenseBonus():Double={
        var bonus=myEconomy.getUpgradeDefense
        if(myOwner.getCapital()==this){
            bonus=bonus*4.0; //The capital region gets a high defense bonus.
        }
        return bonus
    }

    def hasUpgrade(up:UpgradeDefinition)=myEconomy.hasUpgrade(up)

    /**
     * This method returns the attack bonus for a region.
     * @return The attack bonus for a region.
     */
    def getAttackBonus():Double=myEconomy.getUpgradeAttack

    /**
     * This method is used to calculate the resource income for a region.
     * @return A HashMap indicating how much of a resource should be generated.
     */
    def getResourceIncome():collection.immutable.Map[String,Double]=myEconomy.getResourceIncome(Resource.resourceList)


    def getTroopProduction()= myOwner.getBuildCounts.get(this).toInt

    /**
     * This method returns the resource income of a region as a string.
     * @return
     */
    def incomeString():String=myEconomy.incomeString()

    def getResourceNumber()=myEconomy.getResourceNumber()

    /**
     * This sends the encoding of a region's improvements as a character as a BitSet
     * The purpose of this method is to send compressed data about a region to the client.
     * @return A bitset representing the improvements
     */
    def getImprovements():BitSet=myEconomy.getImprovements()

    /**
     * This method adds a one way connection between two regions.
     */
    def addBorder(other:Region){
    	BORDER_ZONES.add(other)
    }
    


    /**
     * This method is used by the map editor to draw the locations of regions.
     * TODO:Make this method draw region bounds as well as the "central" location of each region.
     * @param g
     */
    def draw(g:Graphics){
        var x=myLocation.getX().toInt
        var y=myLocation.getY().toInt
        g.fillRect(x-5, y-5, 10, 10)
    }

    def compareDistance(loc:Region):Double={
        return REGION_DISTANCES(loc)*regionType.getDistanceFactor(loc.regionType)
    }

    def distanceVector(other:Region):Location=other.getCenterLoc()-getCenterLoc()
    def getCenterLoc()=myLocation


    def xCenterRender()=myLocation.getX()
    def yCenterRender()= myLocation.getY()
    def getPopulation()=myEconomy.getPopulation
    def getBorderRegions()=BORDER_ZONES
    def getOwner()=myOwner
    def getMovement():Double=myEconomy.getMovement()
    def getAttackStrength():Double= myEconomy.getMovement()
    def growPopulation(g:Double)=myEconomy.growPopulation(g)
   
    /**
     * This method is used to attack nearby enemy regions with improvements.
     */
    def attack(){
        var enemyRegions=new ArrayList[Region]()
        for(r<-getBorderRegions()){
            if(r.getOwner()!=getOwner()){
                enemyRegions.add(r)
            }
        }
        if(enemyRegions.size()==0){
            return;
        }

        var j=getAttackStrength()
        if(isCapital()){
            j+=5
        }
        for(i<-0 until j.toInt){
            var n=(Math.random*enemyRegions.size()).toInt
            var r=enemyRegions.get(n)
            var minScore=1/(r.getDefenseBonus())  //Account for a region defense bonus.
            if(Math.random<minScore){
                r.getOwner().removeTroop(r)
            }
        }
    }

    /**
     * @param loc
     * @return
     */
    def contains(loc:Location)=getBounds().contains(loc)
    def getOwnerNum()=myOwner.getNum()

    /**
     * This method sets the owner of the region.
     * @param p The new region owner.
     */
    def setOwner(p:Player){
        myOwner.removeZone(this)
        myOwner=p
        p.addZone(this)
    }

    /**
     * This method returns the midpoint between two regions.
     * @param r The other region.
     * @return
     */
    def midPoint(r:Region):IntLoc={
        var xLoc=(getCenterLoc()+r.getCenterLoc())/2
        return xLoc.intLoc
    }
    
    def getTroopCount(p:Player)=p.countTroops(this)
}
