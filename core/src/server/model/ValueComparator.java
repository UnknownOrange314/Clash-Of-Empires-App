package server.model;

import java.util.Comparator;
import java.util.Map;

import server.model.playerData.Region;

public class ValueComparator implements Comparator<Region>{
	Map<Region,Double> base;
	public ValueComparator(Map<Region,Double> base){
		this.base=base;
	}
	
	public int compare(Region a, Region b){
		if(base.get(a)>=base.get(b)){
			return -1;
		}else{
			return 1;
		}
	}
}