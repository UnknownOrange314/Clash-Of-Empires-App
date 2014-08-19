package engine.rts.model

/**
 * This class represents a collection of troops.
 */
class TroopCollection{
   var deathCount=0 //This is to keep track of the number of deaths
   
   def addDeath(){
       deathCount+=1
   }
   def resetDeathCount(){
       deathCount=0
   }
}