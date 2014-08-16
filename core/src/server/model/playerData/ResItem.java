package server.model.playerData;

import java.util.HashMap;
class ResItem{

    static final float BASE_COST=1.0f;
    static final float TROOP_UP=0.05f;
    static final float GROWTH_UP=0.01f;
    static final float TAX_UP=0.05f;
    
    static final String UP_MOVE="Upgrade troop movement";
    static final String UP_PROD="Upgrade troop production";
    static final String UP_GROWTH="Upgrade population growth";
    static final String UP_TAX="Upgrade tax production";

    public static final HashMap<String,ResItem> resList;
    
    static{
    	HashMap<String,ResItem> temp=new HashMap<String,ResItem>();  
    	temp.put(UP_MOVE,new ResItem(2,0,0,0));
    	temp.put(UP_PROD,new ResItem(0,TROOP_UP,0,0));
    	temp.put(UP_GROWTH,new ResItem(0,0,GROWTH_UP,0));
    	temp.put(UP_TAX,new ResItem(0,0,0,TAX_UP));	
    	resList=temp;
    }

    
    final float troopMov;
    final float troopProd;
    final float foodProd;
    final float moneyProd;
    private ResItem(float tMov,float tProd,float fProd,float mProd){
    	troopMov=tMov;
    	troopProd=tProd;
    	foodProd=fProd;
    	moneyProd=mProd;
    }


}