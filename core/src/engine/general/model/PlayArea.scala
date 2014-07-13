package engine.general.model

import server.DataLog
import engine.general.utility.GameTimer

/**
 * This class represents the virtual "play area" for the game.
 * For example, this would represent the map for an RTS or a race course for a racing game.
 */
abstract class PlayArea{
   
   private var log=new DataLog() //This represents the
   val upRate=50 //Number of milliseconds between upgrades. TODO:Make sure that th
   var startTime=System.nanoTime()
   var runCycles=0 //The number of cycles for which the game has bee running for.

   def update(){
       runCycles+=1
       updateGame()
   }

    /**
     * Should processing be done for something.
     * @param freq The frequency of updates.
     * @return
     */
   def shouldProcess(freq:Int)=runCycles%freq==1
   def getCycles=runCycles

    /**
     *
     * @return The number of seconds that have elapsed since the start.
     */
   def getElapsedTime=Math.pow(10,-9)*(System.nanoTime()-startTime)
   def printElapsedTime=println("Elapsed time:"+getElapsedTime)
   def updateGame()
}
