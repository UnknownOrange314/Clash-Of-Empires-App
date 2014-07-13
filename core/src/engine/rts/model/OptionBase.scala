package engine.rts.model

import javax.swing.JSlider

class OptionBase (val numSlider:JSlider){

    val defaultMapDir = "mapData/TinyMap/"
    private var mapName=defaultMapDir//This is the name of the map we will use
    private var time=0.0
    val maxTime=Math.pow(10,9)*15*60 //This is used to represent how much time is left in the game.
    /**
     * These functions are used to keep track of how much time has elapsed in the game.
     */
    def startTime(){
        time=System.nanoTime()
    }

    def getTimeElapsed()= System.nanoTime()-time
    def getRemainingTime()=maxTime-getTimeElapsed()
    def isDone()=getTimeElapsed()<maxTime
    def getPlayerCount()=numSlider.getValue()
    def setMapName(name:String){
        mapName=name.substring(0, name.length()-4)
    }
    def getMapName()=mapName
}
