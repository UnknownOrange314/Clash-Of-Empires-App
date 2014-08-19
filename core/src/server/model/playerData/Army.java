package server.model.playerData;


import java.util.HashSet;
import java.util.Iterator;

import engine.rts.model.TroopCollection;

/**
 * This class represents a collection of troops as an army. Each troop is a part of a army.
 */
public class Army extends TroopCollection{

    final static double BASE_HIT_CHANCE=0.2;

	final Player owner;
	private HashSet<Troop> troops=new HashSet<Troop>();
	
	public Army(Player own){
		owner=own;
	}
	
	public HashSet<Troop> getTroops(){
		return troops;
	}
	
	public int getDeathCount(){
		return deathCount();
	}
	
	public void printTroopCount(){
		System.out.println(troops.size());
	}

    public int size(){
    	return troops.size();
    }
    
    public boolean hasTroops(){
    	return size()>0;
    }

    public void clearArmy(){
        troops.clear();
    }

    public void add(Troop t) {
        troops.add(t);
    }

    /**
     * This method removes a troops from the army.
     * @param t The troop to be removed.
     */
    public void remove(Troop t){
        //If t is null, that means that the army has no troops,so the death count should not be updated.
        if(t!=null){
            addDeath();
            troops.remove(t);
        }
    }

    /**
     * This method removes all the troops in this army that exist in the other army.
     * @param a The other army
     */
    public void filter(Army a){
    	HashSet<Troop> newTroops=new HashSet<Troop>();
    	for(Troop t:troops){
    		if(a.troops.contains(t)==false){
    			newTroops.add(t);
    		}
    	}
        troops=newTroops;
    }

    public double getMoveCount(double mBonus){
    	return 2.0*mBonus;
    }
    
    /**
     * This method creates an army for movement.
     * @return
     */
    public Army createMovementArmy(double mBonus){
        Army moveTroops=new Army(owner);
        int moveCount=(int)getMoveCount(mBonus);
        Iterator<Troop> iter=troops.iterator();
        while(iter.hasNext()){
            Troop t=iter.next();
            if(moveCount==0){
                return moveTroops;
            }
            if(t.canMove()){
               iter.remove();//Remove troops from current army.
               moveTroops.add(t);
               moveCount--;
            }
        }
        return moveTroops;
    }

    /**
     * This method combines two armies together.
     * @param other
     */
    public void combineArmy(Army other){
        troops.addAll(other.troops);
    }

    /**
     * This method gets a troop that will be a casualty.
     * @return
     */
    private Troop getCasualty(){
        for(Troop t:troops){
            return t;
        }
        return null;
    }

    /**
     * This method creates a new army. There should not be any shared troops between the
     * two armies.
     * @param addSize The maximum size of the movie.
     */
    public Army createNewArmy(int addSize){
        int addCount=addSize;
        Army newArmy=new Army(owner);
        for (Troop troop:getTroops()){
            if (addCount>0){
                newArmy.add(troop);
                addCount=addCount-1;
            }
            else{
                filter(newArmy);//We should not have the same troop be part of two different armies.
                return newArmy;
            }
        }
        filter(newArmy);//We should not have the same troop be part of two different armies.
        return newArmy;
    }

    public void removeTroop(){
    	remove(getCasualty());
    }

    /**
     * This method causes two armies to fight.
     * @param dBonus The defense bonus of the opposing army.
     * @param other
     */
    public void fight(double dBonus,Army other){
        //Do not fight if one side has no troops.
        if ((hasTroops()==false)||(other.hasTroops()==false)){
            return;
        }
        double fightRoll=Math.random();
        if (fightRoll < BASE_HIT_CHANCE){
            removeTroop();
        }
        if (fightRoll<BASE_HIT_CHANCE/dBonus){
            other.removeTroop();
        }
    }
}
