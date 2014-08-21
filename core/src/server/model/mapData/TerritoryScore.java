package server.model.mapData;

import server.model.playerData.Region;

public class TerritoryScore implements Comparable{
	final Region reg;
	private double score;
	public TerritoryScore(Region r, double s){
		reg=r;
		score=s;
	}
	@Override
	public int compareTo(Object arg0) {	 
		TerritoryScore other=(TerritoryScore)(arg0);
		if(other.score<=score){
			return 1;
		}
		return -1;
	}	
	public double getScore(){
		return score;
	}
	
	public void addScore(double d){
		score+=d;
	}
}