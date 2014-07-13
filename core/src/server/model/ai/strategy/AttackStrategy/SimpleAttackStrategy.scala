package server.model.ai.strategy.AttackStrategy

import server.model.ai.EasyComputerPlayer
import java.util.{HashMap,HashSet}
import server.model.playerData.Region
import server.model.playerData.{ Player}
import scala.collection.JavaConversions._
import server.model.playerData.Army

class SimpleAttackStrategy extends AttackStrategy {

    val ATTACK_MUL=1.1
    def findStrength(p:Player,targetRegion:Region,defendingTroops:HashMap[Region,Army])= p.getTroopCount(targetRegion)+
            targetRegion.getBorderRegions.filter(reg=>reg.getOwner==p)
                .foldLeft(0.0)((a,b)=>(a+defendingTroops.get(b).size))

    val attackStrengths=new HashMap[Region,String]()

    def getAttackStrength(r:Region):Double={
        val aStr=attackStrengths.get(r)
        return aStr.split(":")(0).toDouble
    }

    def attackTargets( p: EasyComputerPlayer, myTargets: HashSet[Region]){

        attackStrengths.clear()
        if(p.getPersonalityName().equals("neutral")){
            return
        }

        val myTroops: HashMap[Region, Army] = p.getTroopData
        val defendingTroops=new HashMap[Region,Army]
        for((region,army)<-myTroops){
            defendingTroops.put(region,army)
        }

        for (( targetRegion: Region) <- myTargets){
            
            var myStrength =findStrength(p,targetRegion,defendingTroops)
            var enemyStrength = 0

            enemyStrength+=targetRegion.getOwnerTroopCount
            var attackCount=0 //This is the number of troops that are attacking
            attackStrengths.put(targetRegion,myStrength+":"+enemyStrength)//Save the troop strength.
            val maxCount=(enemyStrength+1)*ATTACK_MUL

            if (myStrength>enemyStrength*ATTACK_MUL){
                for ( (region:Region) <- targetRegion.getBorderRegions){
                
                    val army=p.getTroopData().get(region)//Get the troops in a region.
                    var movingTroops=new Army(p)

                    if(p.getTroopCount(region)>0) {
                        p.setRallyPoint(region,targetRegion)

                        /**
                         * We want to allocate troops to attack a region and we want to make sure that
                         * they are not counted as able to attack another region.
                          */
                        var addSize=(maxCount-attackCount).toInt
                        movingTroops=army.createNewArmy(addSize)
                        attackCount+=addSize //We might have a problem if there aren't enough troops
                        //Remove all the troops that have already been allocated.
                        defendingTroops.get(region).filter(movingTroops)
                    }
                }
            }
        }
    }
}
