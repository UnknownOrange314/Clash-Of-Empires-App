package server.model.playerData;

import java.util.HashMap;
import server.model.playerData.ResItem;

public class Research {

	private static final double COST_MUL=1.1;
	private HashMap<String,Double> research;
	
	public Research(){
		research=new HashMap<String,Double>();
		for(String s:ResItem.resList.keySet()){
			research.put(s,1.0);
		}
	}

	public HashMap<String,String> sendData(){
		HashMap<String,String> data=new HashMap<String,String>();
		for(String s:ResItem.resList.keySet()){
			data.put(s,""+getCost(s));
		}
		return data;
	}
	
	
	public double getCost(String name){
		return ResItem.BASE_COST*Math.pow(COST_MUL,research.get(name));
	}
	
	public double getMoveBonus(){
		double bonus=1.0;
		for(String s: research.keySet()){
			bonus=bonus*(1.0+research.get(s)*ResItem.resList.get(s).troopMov);
		}
		return bonus;
	}

	public double getProdBonus(){
		double bonus=1.0;
		for(String s: research.keySet()){
			bonus=bonus*(1.0+research.get(s)*ResItem.resList.get(s).troopProd);
		}
		return bonus;
	}
	
	public double getGrowthBonus(){
		double bonus=1.0;
		for(String s: research.keySet()){
			bonus=bonus*(1.0+research.get(s)*ResItem.resList.get(s).foodProd);
		}
		return bonus;
	}
	
	public double getTaxBonus(){
		double bonus=1.0;
		for(String s: research.keySet()){
			bonus=bonus*(1.0+research.get(s)*ResItem.resList.get(s).moneyProd);
		}
		return bonus;
	}
	
	public void upgrade(String name){
		research.put(name,research.get(name)+1.0);
	}
}
