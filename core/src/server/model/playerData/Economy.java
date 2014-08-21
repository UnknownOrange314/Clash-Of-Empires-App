package server.model.playerData;

import engine.rts.model.Resource;
import java.util.HashMap;
import java.util.*;
import server.model.UpgradeDefinition;
import engine.rts.model.Resource;



/**
 * This object represents the economy of a region.
 */
class Economy {

    /**
     * This is the number of people in a region. The population will slowly grow over time as long as a player
     * has enough food. A high population gives more money in taxes and means that a player score will be higher.
     */
    private int population=1000000;
    Resource myResource=null;
    
    /**
     * This represents the abundance of the resources in a territory.
     * It is up to the specific implementation to determine how abundance of resources
     * translates to region income.
     */
    HashMap<String,Double> myResources=new HashMap<String,Double>();
    
    /**
     * This represents the upgrades that are present in a region.
     */
    HashSet<UpgradeDefinition> myUpgrades=new HashSet<UpgradeDefinition>();

    public Economy(){
    	//TODO: Figure out a way to implement constructor without needing this if statement.
        if(Resource.resourceList.size()>0){
            findResources();
        }
    }
    
    public int getUpgradeScore(){
    	return myUpgrades.size();
    }
    public double getAbundance(String s){
    	System.out.println(myResources);
    	System.out.println(s);
    	if(myResources.containsKey(s)){
        	return myResources.get(s);   		
    	}
    	return 0.0;
    }
  
    /**
     * This method determines the resources that an economy will have.
     */
    public void findResources(){
        int rNum=(int)(Math.random()*(Resource.resourceList.size()));
        myResource=Resource.resourceList.get(rNum);
        myResources.put(myResource.getName(),1.0);
        myResources.put("coin",1.0);
    }

    /**
     * This method may not be necessary.
     * @return The resource number for the region's resource.
     */   
    int getResourceNumber(){
    	return myResource.getId();
    }
    
    public boolean hasUpgrade(UpgradeDefinition up){
    	return myUpgrades.contains(up);
    }
    
    public double getUpgradeDefense(){
    	double def=0.0;
    	for(UpgradeDefinition up:myUpgrades){
    		def+=up.defenseBonus;
    	}
    	return def;
    }
    
    public double getUpgradeAttack(){
    	double att=0.0;
    	for(UpgradeDefinition up:myUpgrades){
    		att+=up.attackBonus;
    	}
    	return att;
    }
    
    public double getMovement(){
    	double mov=0.0;
    	for(UpgradeDefinition up:myUpgrades){
    		mov+=up.getMoveBonus();
    	}
    	return mov;
    }
    
    public double getAttackStrength(){
    	double str=0.0;
    	for(UpgradeDefinition up:myUpgrades){
    		str+=up.getAttackStr();
    	}
    	return str;
    }
    
    public double getPopulation(){
    	return population;
    }
      
    public double rBonus(Resource res){
    	double bonus=getAbundance(res.getName());
    	for(UpgradeDefinition up:myUpgrades){
    		bonus+=up.getResourceBonus(res.getName());
    	}
    	return bonus;
    }
    
    /**
     * This method is used to calculate the resource income for a region.
     * @return A HashMap indicating how much of a resource should be generated.
     */
    public HashMap<String,Double> getResourceIncome(){
    	HashMap<String,Double> data=new HashMap<String,Double>();
    	for(Resource res:Resource.resourceList){
    		data.put(res.getName(),rBonus(res));
    	}
    	return data;
    }

    public void addUpgrade(UpgradeDefinition upgrade){
        myUpgrades.add(upgrade);
    }

    /**
     * This sends the encoding of a region's improvements as a character as a BitSet
     * The purpose of this method is to send compressed data about a region to the client.
     * @return A bitset representing the improvements
     */
    public BitSet getImprovements(){
        
        ArrayList<UpgradeDefinition> upgrades=UpgradeDefinition.upgradeList;
        BitSet improvementData=new BitSet(upgrades.size());
        for(int i=0;i<upgrades.size();i++){
        	UpgradeDefinition up=upgrades.get(i);
            if(myUpgrades.contains(up)){
                improvementData.set(i);
            }
        }
        return improvementData;
    }

    /**
     * Grows the population according to a logistical growth model.
     * @param growth
     */
    public void growPopulation(double growth) {
        if(population<100){ //Prevent population from being too small.
            return;
        }
        double maxPop=Math.pow(10,6)*2;
        double chg=growth*population*(maxPop-population);
        double overPop=population/maxPop;
        population=(int)(population*(growth-overPop));
    }

    /**
     * This method returns the resource income of a region as a string.
     * @return
     */
    public String incomeString(){
    	StringBuilder inStr=new StringBuilder();
    	HashMap<String,Double> income=getResourceIncome();
        for(String res:income.keySet()){
            double i=income.get(res);
            inStr.append(" "+res+":"+income);
        }
        return inStr.toString();
    }
}
