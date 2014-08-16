package server.clientCom;

import java.util.ArrayList;
import java.util.BitSet;

public class RegionState implements java.io.Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static class Builder{
    	
    	int myOwner=999;
	    int resourceNum=999;
	    int hP=0;
	    int myPopulation=0;
	    	    
	    float tProd=0.0f;
	    float defenseBonus=1.0f;
	    float attackBonus=1.0f;
	    
	    ArrayList<Integer> tC=null;
	    BitSet improvementData=null;
	    
	    String name=null;
	    String in=null;
	    String tT="";
	    		
	    boolean iC=false;
	    
	    public Builder owner(int o){
	    	myOwner=o;
	    	return this;
	    }
	    public Builder hitPoints(int p){
	    	hP=p;
	    	return this;
	    }
	     
	    public Builder capital(boolean b){
	    	iC=b;
	    	return this;
	    }
	    
	    public Builder terrain(String t){
	    	tT=t;
	    	return this;
	    }
	    
	    public Builder income(String i){
	    	in=i;
	    	return this;
	    }
	    
	    public Builder troopProduction(Float prod){
	    	tProd=prod;
	    	return this;
	    }
	    
	    public Builder population(int pop){
	    	myPopulation=pop;
	    	return this;
	    }
	    
	    public Builder name(String n){
	    	name=n;
	    	return this;
	    }
	    
	    public Builder defenseBonus(float d){
	    	defenseBonus=d;
	    	return this;
	    }

	    public Builder attackBonus(float a){
	    	attackBonus=a;
	    	return this;
	    }
	    
	    public Builder resourceNum(int rNum){
	    	resourceNum=rNum;
	    	return this;
	    }
	    
	    public Builder improvementData(BitSet data){
	    	improvementData=data;
	    	return this;
	    }
	    
	    public Builder troopCounts(ArrayList<Integer> counts){
	    	tC=counts;
	    	return this;
	    }
	    
	    public RegionState build(){
	    	return new RegionState(this);
	    }
    }
    
	public final int ownerNum;
	public final int resourceNum;
	public final int hitPoints;
	public final int population;
    	    
	public final float troopProduction;
	public final float defenseBonus;
	public final float attackBonus;
    
	public final ArrayList<Integer> troopCounts;
	public final BitSet upgradeData;
    
	public final String name;
	public final String income;
	public final String terrainType;
    		
	public final boolean isCapital;
    
    public RegionState(Builder b){
    	
    	ownerNum=b.myOwner;
    	resourceNum=b.resourceNum;
    	hitPoints=b.hP;
    	population=b.myPopulation;
    	
    	troopProduction=b.tProd;
    	defenseBonus=b.defenseBonus;
    	attackBonus=b.attackBonus;
    	
    	troopCounts=b.tC;
    	upgradeData=b.improvementData;    	
    	
    	name=b.name;
    	income=b.in;
    	terrainType=b.tT;
    	isCapital=b.iC;
    	
    }
    
    public int getOwnerTroopCount(){
    	return troopCounts.get(ownerNum);
    }
}