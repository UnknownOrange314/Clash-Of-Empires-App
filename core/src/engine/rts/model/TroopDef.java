package engine.rts.model;

import server.model.playerData.Player;

public class TroopDef{
    
	static final String DEAD_STATE="dead";
    static final String FREEZE_STATE="noMove";
    static final String NORMAL_STATE="canMove";
    
    final Player owner;
    private String myState=TroopDef.NORMAL_STATE;
    
    public TroopDef(Player o){
    	owner=o;
    }
    
    public void die(){
    	myState=TroopDef.DEAD_STATE;
    }
    
    public boolean canMove(){
    	return myState==TroopDef.NORMAL_STATE;
    }
    
   
}