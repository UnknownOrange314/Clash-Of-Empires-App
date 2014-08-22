package engine.general.model;



/**
 * This class represents the virtual "play area" for the game.
 * For example, this would represent the map for an RTS or a race course for a racing game.
 */
public abstract class PlayArea{
   
   final int upRate=50; //Number of milliseconds between upgrades. TODO:Make sure that th
   long startTime=System.nanoTime();
   int runCycles=0; //The number of cycles for which the game has bee running for.

   public void update(){
       runCycles+=1;
       updateGame();
   }

    /**
     * Should processing be done for something.
     * @param freq The frequency of updates.
     * @return
     */
   protected boolean shouldProcess(int freq){
	   return runCycles%freq==1;
   }
   
   public int getCycles(){
	   return runCycles;
   }

    /**
     *
     * @return The number of seconds that have elapsed since the start.
     */
   public double getElapsedTime(){
	   return Math.pow(10,-9)*(System.nanoTime()-startTime);
   }
   
   public void printElapsedTime(){
	   System.out.println("Elapsed time:"+getElapsedTime());
   }
   
   public abstract void updateGame();
}