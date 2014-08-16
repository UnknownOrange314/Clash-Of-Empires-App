package server.model.playerData;


import engine.general.utility.IntLoc;
import engine.general.utility.Location;
import engine.general.utility.Line;
/**
 * This class represents a connection between regions.
 * RegionBorders are owned by players instead of having one set of borders between regions because it makes conflict
 * resolution and activating movement easier.
 */
public class RegionBorder{

	private Player owner;
	private Region start;
	private Region end;
	
	private boolean on=false;
	private int lossCount=0;
	private boolean conflict=false;
	
	public RegionBorder(Player o,Region s,Region e){
		owner=o;
		start=s;
		end=e;
	}
   
	public boolean hasConflict(){
		return conflict;
	}
	
    public void turnOn(){
        on=true;
    }

    public void turnOff(){
        on=false;
    }

    public boolean isOn(){
    	return on;
    }

    public void endConflict(){
    	conflict=false;
    }
    
    public int getDeaths(){
    	return lossCount;
    }

    public void resetDeaths(){
    	lossCount=0;
    }
   
    public IntLoc getConflictLoc(){
    	return start.midPoint(end);
    }


    public Line getLine(){
        Location s=start.getCenterLoc();
        Location e=end.getCenterLoc();
        return (new Line.LineBuilder())
            .x1((short)s.getX())
            .y1((short)s.getY())
            .x2((short)e.getX())
            .y2((short)e.getY())
            .build();
    }
    

    public Region startSiege(Army troops){
        for(int i=0;i<troops.size();i++){
            end.loseHitPoints();
        }
        if(!end.hasHitPoints()){
            if(end.isCapital()){
                return start;
            }
            else{
                return end;
            }
        }
        else{
            return start;
        }
    }

    /**
     * This method updates the edge.
     *  @return  The region that the troops will move to.
     */
   public Region moveTroops(Army troops){
        //Check to see if we are moving into an enemy region.
        conflict=(end.getOwner()!=owner);
        if (conflict){
            double dBonus=end.getDefenseBonus()/start.getAttackBonus();
            troops.fight(dBonus,end.getOwner().getArmy(end)); //Resolve conflict.
            lossCount+=troops.getDeathCount();
            if(end.getOwner().getTroopCount(end)==0){
                return startSiege(troops);
            }
            return start;
        }
        else{
            return end;
        }
    }

    public Region getDestination(){
    	return end;
    }
    
    public Region getOrigin(){
    	return start;
    }
}
