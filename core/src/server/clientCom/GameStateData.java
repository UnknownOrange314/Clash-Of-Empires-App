package server.clientCom;

import java.util.ArrayList;
import engine.general.utility.IntLoc;
import java.util.HashMap;
import java.lang.Double;
import engine.general.utility.Line;

/**
 * This class stores all the data necessary for rendering the game state.
 * It will be used to transfer data across the network in a serialized state and
 * will be designed in a way that reduces the amount of data that needs to be sent.
 * A copy of the map will be stored on each of the client machines, so there is no need to 
 * send data that defines regions.
 */
public class GameStateData{
	
	public final HashMap<IntLoc,Integer> deathCounts;
	public final double passTime;
	public final ArrayList<RegionState> regionStates;
	public final HashMap<IntLoc,Line> conflictLocs;
	public final HashMap<String,Double> marketPrices;
	public final HashMap<String,String> nationInfo;
	
	public static class Builder{
		HashMap<IntLoc,Integer> dC=null;
		double pT=0.0;
		ArrayList<RegionState> rS=null;
		HashMap<IntLoc,Line> cLocs=null;
		HashMap<String,Double> mP=null;
		HashMap<String,String> nI=null;
		
		public Builder(){
			
		}
		
		public Builder deathCounts(HashMap<IntLoc,Integer> counts){
			dC=counts;
			return this;
		}
		
		public Builder passTime(double t){
			pT=t;
			return this;
		}
		
		public Builder regionStates(ArrayList<RegionState> rState){
			rS=rState;
			return this;
		}
		
		public Builder conflictLocs(HashMap<IntLoc,Line> c){
			cLocs=c;
			return this;
		}
		
		public Builder marketPrices(HashMap<String,Double> p){
			mP=p;
			return this;
		}
		
		public Builder nationInfo(HashMap<String,String> i){
			nI=i;
			return this;
		}
		
		public GameStateData build(){
			return new GameStateData(this);
		}
		
	}
	
	private GameStateData(Builder b){
		deathCounts=b.dC;
		passTime=b.pT;
		regionStates=b.rS;
		conflictLocs=b.cLocs;
		marketPrices=b.mP;
		nationInfo=b.nI;
	}
	
}

