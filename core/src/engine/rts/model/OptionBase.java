package engine.rts.model;

import javax.swing.JSlider;

public abstract class OptionBase{
	
    final static String DEFALUT_MAP = "mapData/TinyMap/";    		
	final int numSlider;

    private String mapName=DEFALUT_MAP;//This is the name of the map we will use
    private double time=0.0;

    //TODO:Make sure this value is not 
    final double maxTime=(int)Math.pow(10,9)*15*60; //This is used to represent how much time is left in the game.

	public OptionBase(int n){
		numSlider=n;
	}

    /**
     * These functions are used to keep track of how much time has elapsed in the game.
     */
    public void startTime(){
        time=System.nanoTime();
    }

    public double getTimeElapsed(){
    	return System.nanoTime()-time;
    }
    
    public double getRemainingTime(){
    	return maxTime-getTimeElapsed();
    }
    
    public boolean isDone(){
    	return getTimeElapsed()<maxTime;
    }
    
    public int getPlayerCount(){
    	return numSlider;
    }
    
    public void setMapName(String name){
        mapName=name.substring(0, name.length()-4);
    }
    
    public String getMapName(){
    	return mapName;
    }
}
