package server.clientCom;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import engine.general.utility.Line;
import server.model.playerData.Population;
import server.model.playerData.Research;


/**
 * This class sends all the important statistics for a player to the game display so that they can be processed and rendered.
 * @param resources
 * @param elapsedTime
 * @param score
 * @param rallyPoints
 * @param leftClick
 * @param rightClick
 */
public class PlayerStats implements Serializable{
	
	public static class Builder{
		
		HashMap<String, String> res;
		HashMap<String,Integer> resourceData;
		int timeElapsed;
		Population playerScore;
		HashSet<Line> myRallyPoints;
		HashMap<String,Double> income;
		double upkeep;
		Point rightClick;
		Point leftClick;
		ArrayList<String> fails;
		
		public Builder fails(ArrayList<String> f){
			fails=f;
			return this;
		}
		
		public Builder research(Research r){
			res=r.sendData();
			return this;
		}
		
		public Builder resourceData(HashMap<String,Integer> r){
			resourceData=r;
			return this;
		}
		
		public Builder timeElapsed(int t){
			timeElapsed=t;
			return this;
		}
		
		public Builder playerScore(Population p){
			playerScore=p;
			return this;
		}
		
		public Builder rallyPoints(HashSet<Line> r){
			myRallyPoints=r;
			return this;
		}
		
		public Builder income(HashMap<String,Double> i){
			income=i;
			return this;
		}
		
		public Builder upkeep(double u){
			upkeep=u;
			return this;
		}
		
		public Builder rightClick(Point r){
			rightClick=r;
			return this;
		}
		
		public Builder leftClick(Point l){
			leftClick=l;
			return this;
		}
		
		public PlayerStats build(){
			return new PlayerStats(this);
		}
		
		
		
	}


	private static final long serialVersionUID = 1L;
	public final HashMap<String, String> res;
	final HashMap<String,Integer> resourceData;
	final int timeElapsed;
	final Population playerScore;
	final HashSet<Line> myRallyPoints;
	final HashMap<String,Double> income;
	final double upkeep;
	final Point rightClick;
	final Point leftClick;
	public final ArrayList<String> fails;
 	public PlayerStats(Builder b){
		res=b.res;
		resourceData=b.resourceData;
		timeElapsed=b.timeElapsed;
		playerScore=b.playerScore;
		myRallyPoints=b.myRallyPoints;
		income=b.income;
		upkeep=b.upkeep;
		rightClick=b.rightClick;
		leftClick=b.leftClick;
		fails=b.fails;
	}
	
	public HashSet<Line> getRallyPoints(){
		return myRallyPoints;
	}

	public HashMap<String,Integer> getResources(){
		return resourceData;
	}

    public HashMap<String,Double> getIncome(){
    	return income;
    }
    
    public double getUpkeep(){
    	return upkeep;
    }
    
    public int getTimeElapsed(){
    	return timeElapsed;
    }
    
    public Population getPlayerScore(){
    	return playerScore;
    }
    
    public Point getRightClick(){
    	return rightClick;
    }
    
    public Point getLeftClick(){
    	return leftClick;
    }
}