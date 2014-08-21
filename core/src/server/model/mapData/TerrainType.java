package server.model.mapData;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.utils.JsonValue;

public class TerrainType implements java.io.Serializable{
    
    public static final String WATER="water";
    public static ArrayList<TerrainType> terrainList=new ArrayList<TerrainType>();
    
    public static TerrainType getType(String name){
        for(TerrainType t:terrainList){
            if (t.getName().equals(name)){
                return t;
            }
        }
        return null;  
    }
    
    final String name;
    final String defenseBonus;
    final String attackBonus;
    final double moveBonus;
    final boolean hasResources;
    
    public TerrainType(JsonValue data){
        name=data.getString("name");
        defenseBonus=data.getString("defense_bonus");
        attackBonus=data.getString("attack_bonus");
        moveBonus=data.getDouble("move_bonus");
        hasResources=data.getBoolean("has_resources");
        terrainList.add(this);
    }
    
    public String getName(){
    	return name;
    }

    public void printData(){
        System.out.println(" terrain name:"+name);
        System.out.println(" defenseBonus:"+defenseBonus);
        System.out.println(" attackBonus:"+attackBonus);
        System.out.println(" moveBonus:"+moveBonus);
        System.out.println(" hasResources:"+hasResources);
    }
    
    public double getMoveBonus(){
    	return moveBonus;
    }
    
    /**
     * Returns the cost of moving through a region.
     */
    public double getMoveCost(){
        if(moveBonus==0){
        	return  1/(moveBonus+0.0001);
        }else{
        	return 1/moveBonus;
        }
    }
    
    /**
     * Returns the cost factor for moving between two regions.
     */
    public double getDistanceFactor(TerrainType other){
    	return (getMoveCost()+other.getMoveCost())/2;
    } 
}