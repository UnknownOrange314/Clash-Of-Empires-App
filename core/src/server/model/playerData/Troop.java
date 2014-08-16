package server.model.playerData;

import engine.rts.model.TroopDef;

public class Troop extends TroopDef{
	
	/**
	 * This class represents troops. It is currently a wrapper for TroopDef.scala.
	 * @author Bharat
	 */
	public Troop(Player owner){
		super(owner);
	}
}