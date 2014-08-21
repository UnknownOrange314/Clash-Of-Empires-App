package server.model.playerData;


import java.util.HashMap;
import server.model.mapData.ResourceMarket;
import java.util.HashSet;
import engine.rts.model.*;
import java.util.ArrayList;

public class Stockpile extends ResourceStock{

	private static double ADD_R_COST=0.2;
	
	public static void setMaintenanceFactor(double cost){
	        ADD_R_COST=cost;
    }
	
	private static double MIN_INCOME=4.0;
	private static double TAX=0.000001; //This is the amount of money each person pays in taxes.
	private static double FOOD_C=0.0000002; //This is the amount of food that each person consumes.
	private static double MAX_LOG_SIZE=10;
	private static double TROOP_UPKEEP=0.002;
    private static double BASE_GROWTH=1.01; //Growth when there is enough food.
	private static double BASE_STARVATION=0.98; //Growth when there is not enough food.
	
	private final String FOOD="food";
	private final String COIN="coin";
	
	HashMap<String,Double> resourceIncome=new HashMap<String,Double>();
	
	double bonus=1.0;//TODO: Consider why this is used.
	
	public Stockpile(ArrayList<String> resourceList){
		
		for(String resource:resourceList){
			myResources.put(resource,0.0);
		}
			
	}
	
	public int resCount(){
		return myResources.size();
	}
	
	public double getStockpile(String res){
		return myResources.get(res);
	}
	

	/**
     * This function generates an upkeep cost for regions.
     */
	public double regionUpkeep(int rCount){	
		return Stockpile.ADD_R_COST/2.0*Math.pow(rCount,2);
	}

    /**
     * This function generates an upkeep cost for troops.
     */
    public double troopUpkeep(int tCount){
    	return Stockpile.TROOP_UPKEEP*tCount;
    }

    /**
     * This method generates an upkeep cost for the number of troops and regions.
     * The upkeep cost is currently rounded to the nearest integer.
     * @param rNum
     * @param tNum
     */
    public double upkeep(int rNum, int tNum){
    	double coinCost=getUpkeepCost(rNum,tNum);
	    myResources.put("coin",myResources.get(COIN)-(int)coinCost);
	    return coinCost;
	}
    
    public boolean hasMoney(){
    	return myResources.get(COIN)>0;
    }


    /**
     * This method sets the resource bonus that a player should get.
      * @param newB The resource bonus.
     */
    public void setBonus(double newB){
        bonus=newB;
    }

    public double getGrowthRate(){
        if (myResources.get(FOOD)>0){
            return Stockpile.BASE_GROWTH;  //There is enough food for population growth.
        }
        else{
            return Stockpile.BASE_STARVATION; //Not enough food.
        }
    }

    public HashMap<String,Double> calculateIncome(HashSet<Region> myZones,Region myCapital, int myPopulation, Research research){
        HashMap<String,Double> resourceIncome=new HashMap<String,Double>();
        for(String r:myResources.keySet()){
        	resourceIncome.put(r, MIN_INCOME);
        }
        for(Region reg: myZones){
        	HashMap<String,Double> in=reg.getResourceIncome();
        	for(String s:in.keySet()){
        		double newStock=(Double)resourceIncome.get(s)+(Double)in.get(s);
        		resourceIncome.put(s,newStock);
        	}
        }
        return resourceIncome;
    }

	public void income(HashSet<Region> myZones, Region myCapital, int myPopulation, Research research){
	     HashMap<String,Double>resourceIncome=calculateIncome(myZones,myCapital,myPopulation,research);
		 for(String resStr:myResources.keySet()){
			 double v=myResources.get(resStr);
		     double temp=new Double(v);
		     temp+=resourceIncome.get(resStr);
		     bonus=1.0;
			 myResources.put(resStr, temp);
		 }
	   
        double taxes=Stockpile.TAX*myPopulation*research.getTaxBonus();
        myResources.put(COIN,myResources.get(COIN)+(int)taxes);
        double foodCost=Stockpile.FOOD_C*myPopulation/research.getTaxBonus();//More production=more efficient food usage.
        myResources.put(FOOD,myResources.get(FOOD)-(int)foodCost);
	}


    /**
     * Sells a resource and gives additional gold.
     * @param resourceString
     * @param price
     *   @return Returns true if successful or false if unsuccessful.
     */
    public boolean sell(String resourceString,double price){
        //We do not have enough resources
        if(myResources.get(resourceString)-ResourceMarket.BASE_SELL_NUM<0){
            getFailMessages().add("Not enough "+resourceString+" to sell");
            return false;
        }
        myResources.put(resourceString,(myResources.get(resourceString)-ResourceMarket.BASE_SELL_NUM));
        myResources.put(COIN,(myResources.get(COIN)+ResourceMarket.BASE_BUY_NUM*price));
        return true;
    }

	    /**
	     * Buys a resource using gold.
	     * @param resourceString
	     * @param price
	     * @return Returns true if successful or false if unsuccessful.
	     */
    public boolean buy(String resourceString,double price){
        //We do not have enough money
        if(myResources.get(COIN)-ResourceMarket.BASE_SELL_NUM*price<0){
            getFailMessages().add("Not enough coin to buy "+resourceString);
            return false;
        }
        myResources.put(resourceString,(myResources.get(resourceString)+ResourceMarket.BASE_BUY_NUM));
        myResources.put(COIN,(myResources.get(COIN)-ResourceMarket.BASE_SELL_NUM*price));
        return true;
    }

    /**
     * This method is used to buy research. The research is only bought if
     * they player has enough money.
     * @param price The price of the research.
     * @return True if the research was successfully bought, false otherwise.
     */
    public boolean buyResearch(int price){
        if(myResources.get(COIN)<price){
            return false;
        }
        else{
            myResources.put(COIN,myResources.get(COIN)-price);
        }
        return true;
    }

    /**
     * TODO: Refactor this method so that it calculates income without relying on
     * a previous state. It should calculate income based on an argument to the function instead.
     * This method returns a value representing resource income.
     * The results of this method may be somewhat inaccurate due to rounding errors that need to be fixed
     * @return
     */
     public HashMap<String,Double> getIncome(){
    	 return resourceIncome;
     }
     
     public double getUpkeepCost(int rNum,int tNum){
    	 return regionUpkeep(rNum)+troopUpkeep(tNum);
     }
     
     
    /**
     * This checks to see if a player has too many resources.
     * TODO:Make the result dependent on the price of improvements.
     * @return
     */
     boolean rich(){
        for(String r:myResources.keySet()){
        	double amount=myResources.get(r);
            if (amount<10){
                return false;
            }
        }
        return true;
     }

     public ArrayList<String> getFailMessages(){
     	while(failMessages.size()>Stockpile.MAX_LOG_SIZE){
     		failMessages.remove(1);
     	}
     	ArrayList<String> failList=new ArrayList<String>();
     	failList.addAll(failMessages);
     	if (hasMoney()==false){
     		failList.add("You have no money. Take more regions, build improvements, or get rid of troops to get more");
     	}
     	if (rich()==true){
     		failList.add("You are accumulating a large resource stockpile, which does not help you. Left click on a region to buy an upgrade");
     	}
     	return failList;
	}

}
