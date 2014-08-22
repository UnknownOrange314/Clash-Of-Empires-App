package engine.rts.model;

import java.awt.Polygon;
import java.util.HashMap;


/**
 * This class defines regions for the game.
 * In this class, regions are defined as places on the map that are
 * controllable by individual players
 */
public abstract class RegionDef{
    
	static int rCount=0;

	String regionName;
    private Polygon regionBounds=null;

	public RegionDef(){
		rCount+=1;
		regionName=""+rCount;
	}

    public String getName(){
    	return regionName;
    }
    
    public void setName(String s){
    	regionName=s;
    }
    
    public Polygon getBounds(){
    	return regionBounds;
    }
    
    public void setBounds(Polygon p){
    	regionBounds=p;
    }

    public boolean nameEquals(String s){
    	return s.equals(regionName);
    }

    public String getNationName(){
        if(regionName.length()>7){
            if(regionName.substring(0,14).equals(("Constantionople"))) {
                return "Turkey";
            }
        }

        if (regionName.substring(0,6) == "Moscow") {
            return "Russia";
        }

        if (regionName.substring(0,6) == "Madrid") {
            return "Spain";
        }

        if (regionName.substring(0,6) == "London") {
            return "England";
        }

        if (regionName.substring(0,6) == "Berlin") {
            return "Prussia";
        }

        if (regionName.substring(0,6) == "Vienna") {
            return "Austria";
        }
        return "Turkey";
    }
}