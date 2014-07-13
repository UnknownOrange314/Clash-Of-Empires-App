package server.model.playerData

import java.util.HashSet
import scala.collection.JavaConversions._
import engine.rts.model.TroopCollection

/**
 * This class represents a collection of troops as an army. Each troop is a part of a army.
 */
class Army(val owner:Player) extends TroopCollection{
    
    val BASE_HIT_CHANCE=0.2
    /**
     * This represents the troops in the army.
     */
    private var troops=new HashSet[Troop]
    private def getTroops()=troops
    def getDeathCount()=deathCount //Where is death count being updated???
    def printTroopCount()=println(troops.size())
    def size():Int=troops.size
    def hasTroops:Boolean=troops.size>0

    def clearArmy(){
        troops.clear()
    }

    def add(t:Troop) {
        troops.add(t)
    }

    /**
     * This method removes a troops from the army.
     * @param t The troop to be removed.
     */
    def remove(t:Troop){
        //If t is null, that means that the army has no troops,so the death count should not be updated.
        if(t!=null){
            deathCount+=1
            troops.remove(t)
        }
    }

    /**
     * This method removes all the troops in this army that exist in the other army.
     * @param a The other army
     */
    def filter(a:Army){
        troops=new HashSet[Troop](troops.filter(troop=>(a.troops.contains(troop)==false)))
    }

    def getMoveCount(mBonus:Double):Double=2.0*mBonus
    
    /**
     * This method creates an army for movement.
     * @return
     */
    def createMovementArmy(mBonus:Double):Army= {
        val moveTroops=new Army(owner)
        var moveCount=getMoveCount(mBonus)
        val iter:Iterator[Troop] =troops.iterator
        while(iter.hasNext){
            val t=iter.next()
            if(moveCount==0){
                return moveTroops
            }
            if(t.canMove()){
               iter.remove()//Remove troops from current army.
               moveTroops.add(t)
               moveCount-=1
            }
            else{
                println("Frozen")
            }
        }
        return moveTroops
    }

    /**
     * This method combines two armies together.
     * @param other
     */
    def combineArmy(other:Army){
        troops.addAll(other.troops)
    }

    /**
     * This method gets a troop that will be a casualty.
     * @return
     */
    def getCasualty():Troop={
        for(t<-troops){
            return t
        }
        return null
    }

    /**
     * This method creates a new army. There should not be any shared troops between the
     * two armies.
     * @param addSize The maximum size of the movie.
     */
    def createNewArmy(addSize:Int):Army={
        var addCount=addSize
        var newArmy=new Army(owner)
        for (troop:Troop <- getTroops()){
            if (addCount>0){
                newArmy.add(troop)
                addCount=addCount-1
            }
            else{
                filter(newArmy)//We should not have the same troop be part of two different armies.
                return newArmy
            }
        }
        filter(newArmy)//We should not have the same troop be part of two different armies.
        return newArmy
    }

    def removeTroop()=remove(getCasualty())

    /**
     * This method causes two armies to fight.
     * @param dBonus The defense bonus of the opposing army.
     * @param other
     */
    def fight(dBonus:Double,other:Army){
        //Do not fight if one side has no troops.
        if ((hasTroops==false)||(other.hasTroops==false)){
            return
        }
        val fightRoll=Math.random
        if (fightRoll < BASE_HIT_CHANCE){
            removeTroop()
        }
        if (fightRoll<BASE_HIT_CHANCE/dBonus){
            other.removeTroop()

        }
    }
}
