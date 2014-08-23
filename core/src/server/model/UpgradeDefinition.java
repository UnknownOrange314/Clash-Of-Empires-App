package server.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Graphics;

import com.badlogic.gdx.utils.JsonValue;

import engine.general.utility.Location;
import engine.rts.model.Resource;

public class UpgradeDefinition implements java.io.Serializable{

	public static class UpgardeFactory{
		public ArrayList<Resource> resourceList=new ArrayList<Resource>();
	}
	
	public static ArrayList<UpgradeDefinition>upgradeList=new ArrayList<UpgradeDefinition>();
    public static void setResourceList(ArrayList<UpgradeDefinition> u){
    	upgradeList=u;
    }
    
	public static UpgradeDefinition getUpgrade(int keyPress){
        for (UpgradeDefinition upgrade:upgradeList){
            if (upgrade.getBuildKey()==keyPress){
                return upgrade;
            }
        }
        return null;
    }
	
	final String name;
	final String imageName;
	final String description;
	
	final double moveBonus;
	final String attackPower;
	public final double attackBonus;
	public final double defenseBonus;
	final char buildKey;
	
	HashMap<String,Integer> resourceCosts=new HashMap<String,Integer>();
	HashMap<String,Double> resourceBonus=new HashMap<String,Double>();
	
	final int id;
	
	public UpgradeDefinition(JsonValue data,ArrayList<Resource> resourceList){
		name=data.getString("name");
		imageName=data.getString("image");
		for(Resource resource:resourceList){
			String resName=resource.getName();
			resourceCosts.put(resName, Integer.parseInt(data.getString(resName+"_cost")));
			if(data.hasChild(resName+"_bonus")){
				resourceBonus.put(resName,Double.parseDouble(data.getString(resName+"_cost")));
			}else{
				resourceBonus.put(resName,1.0);
			}
		}
		moveBonus=data.getDouble("move_bonus");
		description=data.getString("descr");
		attackPower=data.getString("attack_power");
		attackBonus=data.getDouble("attack_bonus");
		defenseBonus=data.getDouble("defense_bonus");
		buildKey=data.getChar("build_key");
	
		//Add the upgrade to the list of upgrades.
		id=upgradeList.size();
		upgradeList.add(this);//Add improvement to list of upgrades.
	}
	
	public String getName(){
		return name;
	}
	public String getInfo(){
		return description;
	}

	public double getMoveBonus(){
		return moveBonus;
	}
	
	public int getID(){
		return id;
	}
	
	public String getImageLocation(){
		return imageName;
	}


    //TODO:Refactor code so that this method is unecessary
    public void draw (Graphics g,Location loc){
        System.err.println("This method not yet implemented");
    }

    public double getResourceBonus(String res){
    	return resourceBonus.get(res);
    }
    
    public int getAttackStr(){
    	return Integer.parseInt(attackPower);
    }
    
    public HashMap<String,Double> getResourceBonus(){
    	return resourceBonus;
    }
    
    public HashMap<String,Integer >getCost(){
    	return resourceCosts;
    }
    
    public Integer getBuildKey(){
    	return (int)buildKey;
    }

    public String costStr(){
        String s="Cost:";
        for (String name:resourceCosts.keySet()){
            s+=","+resourceCosts.get(name);
        }
        return s;
    }
    
    public void printData(){
        System.out.println(name+" "+imageName+" costs:"+resourceCosts+" bonuses:"+resourceBonus+" attackBonus:"+attackBonus+" attackPower:"+attackPower+" defenseBonus:"+defenseBonus);
    }
}
