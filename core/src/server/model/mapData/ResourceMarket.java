package server.model.mapData;

import java.util.HashMap;
import java.lang.Double;
import engine.rts.model.Resource;

public class ResourceMarket{
	
    public static final int BASE_SELL_NUM=10; //The default amount of resources you will lose per market transaction.
    public static final int BASE_BUY_NUM=5;   //The default amount of resources you wil gain per market transaction.
    
    static HashMap<String,Double> resourceData=new HashMap<String,Double>();
    
    static{
        for(Resource resource:Resource.resourceList){
            resourceData.put(resource.getName(),1.0);
        }	
    }


    public void sell(String resourceString){
        resourceData.put(resourceString,resourceData.get(resourceString)*0.95);
    }

    public void buy(String resourceString){
        resourceData.put(resourceString,resourceData.get(resourceString)*1.05);
    }

    public double getPrice(String str){
    	return resourceData.get(str);
    }

    HashMap<String,Double> getAllPrices(){
        HashMap<String,Double> prices=new HashMap<String,Double>();
        for (Resource resource:Resource.resourceList){
            String resStr=resource.getName();
            double price=resourceData.get(resStr);
            prices.put(resStr,price);
        }
        return prices;
    }
    
    public void updatePrices(){
        for (Resource resource:Resource.resourceList){
            String resourceString=resource.getName();
            if (resourceData.get(resourceString)>1){
                resourceData.put(resourceString,resourceData.get(resourceString)*0.99);
            }
            else{
                resourceData.put(resourceString,resourceData.get(resourceString)*1.01);
            }
        }
    }
}
