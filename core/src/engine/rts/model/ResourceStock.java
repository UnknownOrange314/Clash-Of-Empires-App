package engine.rts.model;

import java.util.HashMap;
import java.util.LinkedList;
import server.model.UpgradeDefinition;

public abstract class ResourceStock {

	protected LinkedList<String> failMessages;
	protected HashMap<String,Double> myResources;
	
	public ResourceStock(){
		failMessages=new LinkedList<String>(); //This log all the failure messages when the user does not have enough resources to do something
	    myResources=new HashMap<String,Double>(); //This represents the amount of resources that a player has.
	}
  

    public HashMap<String,Integer> getResourceData(){
        HashMap<String,Integer> resources=new HashMap<String,Integer>();
        for(String name:myResources.keySet()){
        	resources.put(name,myResources.get(name).intValue());
        }
        return resources;
    }

    /**
     * Can a player buy an upgrade.
     * @param rCost The cost of buying an upgrade.
     * @return
     */
    public boolean canUpgrade(HashMap<String,Integer> rCost){
    	for(String str:myResources.keySet()){
    		double amt=myResources.get(str);
    		if(amt<rCost.get(str)){
    			failMessages.add("Not enough resources to buy upgrade");
    			return false;
    		}
    	}
    	return true;
    }

    /**
     * This method is used to buy an upgrade.
     * @param upgrade The type of upgrade that should be purchased.
     */
    public boolean buyUpgrade(UpgradeDefinition upgrade){
    	for(String name: upgrade.getCost().keySet()){
    		double cost=upgrade.getCost().get(name);
    		myResources.put(name,myResources.get(name)-cost);
    	}
    	return true;
    }
}
