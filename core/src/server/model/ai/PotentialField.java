package server.model.ai;



import server.model.playerData.Region;
import server.model.playerData.Army;
import engine.general.utility.*;

import java.awt.Graphics;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class creates a potential field that directs troop movement.
 * It works by generating "voltages" for each region and forcing troops to move to
 * regions with a higher "voltage".
 */
class PotentialField{
    
	final static double BARRIER=999999; //This is a value used to mark regions that shouldn't be visited.
	
    /**
     * @param myTroopCounts  This represents the number of "charges" at a location which are troops
     * @param attrCount This represents all the important locations on the map and their values.
     * @return A potential field for all the locations of the "charges"
     */
    public PotentialField getPotentialVectors(HashMap<Region,Double> myTroopCounts,HashMap<Region,Double> attrCount,EasyComputerPlayer p){
        
        //The troop counts are valid.
        final double dFact=100.0;//Region distaRnces will be multiplied by this number
        final HashSet<Region> targets=p.getTargets();
        final PotentialField potentialField=new PotentialField();
        //Loop through all the regions and generate a force for each region.
        for(Region chargeRegion:myTroopCounts.keySet()){
            double chargeValue=myTroopCounts.get(chargeRegion);
            double voltage=0.0;
            //This calculates the force vector for a location
            for(Region attrRegion:attrCount.keySet()){
            	double attrValue=attrCount.get(attrRegion);
                //Calculate the force between two regions
                double ownerTroopCount=0.0;
                if(attrRegion.getOwner()!=p){
                    ownerTroopCount=attrRegion.getOwnerTroopCount();
                }
                
                //Get a distance vector in the "direction" of the other region and a force magnitude.
                double distance=attrRegion.compareDistance(chargeRegion)*dFact;
                double reinforceAttr=ownerTroopCount*chargeValue; //This is so that we reinforce regions that have a large number of enemy troops.
                double attraction=0.0;
                if (targets.contains(attrRegion)){
                     attraction=chargeValue*attrValue;//This is the attraction factor based on the value of the target.
                }

                /*This is the repulsion factor. If it is not high enough, players will concentrate troops in a few regions.
                  This is because of the fact that the heuristic that gives scores to regions will get a weight that is too high,
                  which is a problem, since that heuristic gives higher scores to regions with large concentrations of friendly troops.
                 */
                double repulsion=0.0;
                if (attrRegion.getOwner()==p){
                    repulsion=myTroopCounts.get(attrRegion)*9*Math.log(myTroopCounts.get(attrRegion)+3);
                }
                double localForce=(attraction-repulsion*repulsion)+reinforceAttr;
                if(attrRegion==chargeRegion){
                    distance=1;
                }
                voltage+=localForce/distance;
            }
            if(p.getBorders().contains(chargeRegion)==false) {
                potentialField.addForce(chargeRegion,voltage-BARRIER);
            }
            else{
                potentialField.addForce(chargeRegion,voltage);
            }
        }
        return potentialField;
    }



	
    HashMap<Region,Double> potentials=new HashMap<Region,Double>();
    
    public void addForce(Region location,double voltage){
        potentials.put(location,voltage);
    }

    public double getForce(Region r){
        return potentials.get(r);
    }

    /** This method gets the potential between two regions. A negative return value means that the 2nd region has a
      * lower potential. A positive return value means that the 1st region has a lower potential.
      *
      * @param r1
      * @param r2
      * @return
      */
    public double getVoltage(Region r1,Region r2){
    	return potentials.get(r2)-potentials.get(r1);
    }

    /**
     * This method turns on movement commands between regions. A movement command is generated from one region to another.
     * if the region has a higher potential.
     * @param rList
     */
    public void activateEdges(EasyComputerPlayer p,HashMap<Region,Army> rList){
        for(Region region:rList.keySet()){
        	Army count=rList.get(region);
            for (Region border:region.getBorderRegions()){
                double rVolts=potentials.get(region);
                double bVolts=potentials.get(border);
                if (bVolts>rVolts){
                    if (p.ownsRegion(border)){
                         p.setRallyPoint(region,border);
                    }
                }
            }
        }
    }
}

