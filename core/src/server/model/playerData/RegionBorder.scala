package server.model.playerData

import engine.general.utility.{IntLoc, Line}

/**
 * This class represents a connection between regions.
 * RegionBorders are owned by players instead of having one set of borders per player because it makes conflict
 * resolution and activating movement easier.
 */
class RegionBorder(val owner:Player,val start:Region, val end:Region){
   
    private var on=false //Is the border active?
    private var lossCount=0
    private var conflict=false //This variable indicates if there is attempted movement into an enemy region
    def hasConflict=conflict
    def turnOn(){
        on=true
    }

    def turnOff(){
        on=false
    }

    def isOn()=on

    def endConflict() {
        conflict=false
    }

    def getDeaths():Int=lossCount

    def resetDeaths(){
        lossCount=0
    }

    def getConflictLoc():IntLoc=start.midPoint(end)

    def getLine():Line= {
        val s=start.getCenterLoc
        val e=end.getCenterLoc
        return (new Line.LineBuilder)
            .x1(s.getX().toShort)
            .y1(s.getY().toShort)
            .x2(e.getX().toShort)
            .y2(e.getY().toShort)
            .build()
    }

    def startSiege(troops:Army):Region={
        for(i<-0 until troops.size()){
            end.loseHitPoints()
        }
        if(!end.hasHitPoints()){
            if(end.isCapital){
                return start
            }
            else{
                return end
            }
        }
        else{
            return start
        }
    }

    /**
     * This method updates the edge.
     *  @return  The region that the troops will move to.
     */
    def moveTroops(troops:Army):Region={
        //Check to see if we are moving into an enemy region.
        conflict=(end.getOwner()!=owner)
        if (conflict){
            val dBonus=end.getDefenseBonus/start.getAttackBonus
            troops.fight(dBonus,end.getOwner.getArmy(end)) //Resolve conflict.
            lossCount+=troops.getDeathCount()
        
            if(end.getOwner.getTroopCount(end)==0){
                return startSiege(troops)
            }
            return start
        }
        else{
            return end
        }
    }

    def getDestination():Region=end
    def getOrigin():Region=start
}
