package engine.rts.model;

public class TroopCollection {
	
   protected int deathCount=0;//This is to keep track of the number of deaths
			   
   public void addDeath(){
       deathCount+=1;
   }
   
   public void resetDeathCount(){
       deathCount=0;
   }
     
}