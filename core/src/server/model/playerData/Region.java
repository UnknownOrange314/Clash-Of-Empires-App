package server.model.playerData;



import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import engine.general.utility.IntLoc;
import engine.general.utility.Location;
import engine.rts.model.RegionDef;
import engine.rts.model.Resource;
import server.model.mapData.TerrainType;
import server.model.UpgradeDefinition;
public class Region extends RegionDef{
	
	public final static int MAX_POINTS=10000;
	private final static int HP_LOSS=100;
	private final static int REGEN=10;

	private Player myOwner;
	private final Location myLocation;
	
	private Economy myEconomy=new Economy();
	private HashMap<Region,Double>  REGION_DISTANCES=new HashMap<Region,Double>();
	private TerrainType regionType=null;
	
	
	/**
     * This is the number of hit points a region has. When you attack a region with no troops, it will lose hit points.
     * When you attack a region with 0 hit points, you take control of that region. Hit points regenerate over time.
     */
	private int hitPoints=MAX_POINTS;
	
	/**
     * This is a list of regions that are adjacent to this one. Troops can only move between adjacent regions.
     */
	private ArrayList<Region> BORDER_ZONES=new ArrayList<Region>();
	
	public Region(Player myOwn, Location myLoc){
		myOwner=myOwn;
		myLocation=myLoc;
	}

    

    /**
     * This method randomly determines the resources that a region will have.
     */
	public void findResources(){
		myEconomy.findResources();
	}
	
    /**
     * This method calculates the upgrade score for a region.
     * @return
     */
    public int getUpgradeScore(){
    	return myEconomy.getUpgradeScore();
    }

    /**
     * This method calculates the distances between regions and saves them.
     */
    public void updateDistances(){
        REGION_DISTANCES=new HashMap<Region,Double>();
        //Perform a BFS to search regions.
        LinkedList<Region> queue=new LinkedList<Region>();
        queue.add(this);
        REGION_DISTANCES.put(this,0.0);
        while(queue.isEmpty()==false){
            Region r=queue.remove();
            double distance=REGION_DISTANCES.get(r);
            LinkedList<Region> addR=new LinkedList<Region>();
            for(Region reg: r.getBorderRegions()){
            	if(!REGION_DISTANCES.containsKey(reg)){
            		addR.add(reg);
            	}
            }
            queue.addAll(addR);
            for(Region reg:addR){
            	REGION_DISTANCES.put(reg,distance+1);
            }
        }
    }
    
    public double getMoveCost(){
    	return getTerrain().getMoveCost();
    }

    public int getHitPoints(){
    	return hitPoints;
    }

    public void resetHitPoints(){
        hitPoints=Region.MAX_POINTS;
    }

    public boolean hasHitPoints(){
    	return hitPoints>0;
    }

    /**
     * This method subtracts hit points from a region under attack that has no defending troops.
     */
    public void loseHitPoints(){
        hitPoints=Math.max(0,hitPoints-Region.HP_LOSS);
    }

    /**
     * This method regenerates hit points
     */
    public void regenHP(){
        hitPoints=Math.min(hitPoints+1,Region.MAX_POINTS);
        if(isCapital()){
            hitPoints=Math.min(hitPoints+Region.REGEN,Region.MAX_POINTS);
        }
    }

    /**
     * Add an upgrade to this region.
     * @param upgrade The upgrade that is being added to the region.
     */
    public void addUpgrade(UpgradeDefinition upgrade){
    	myEconomy.addUpgrade(upgrade);
    }

    public String getType(){
    	return regionType.getName();
    }

    public TerrainType getTerrain(){
    	return regionType;
    }
    
    /**
     * Remove borders with water regions.
     */
    public void removeWaterBorder(){
        Iterator iter=BORDER_ZONES.iterator();
        while(iter.hasNext()){
            Region border=(Region)iter.next();
            if(border.getType().equals(TerrainType.WATER )){
                iter.remove();
            }
        }
        if(regionType.equals(TerrainType.getType(TerrainType.WATER))){
            BORDER_ZONES.clear();
        }
    }

    /**
     * Set the region type for this region
     * @param r
     */
    public void setType(TerrainType r){
        regionType=r;
    }

    /**
     * @return The number of troops that a region owner has in that region.
     */
    public int getOwnerTroopCount(){
    	return myOwner.countTroops(this);
    }
    
    public boolean isCapital(){
    	return myOwner.getCapital()==this;
    }

    /**
     * This method returns the defense bonus for a region.
     * @return The defense bonus for a region.
     */
    public double getDefenseBonus(){
        double bonus=myEconomy.getUpgradeDefense();
        if(myOwner.getCapital()==this){
            bonus=bonus*4.0; //The capital region gets a high defense bonus.
        }
        return bonus;
    }

    public boolean hasUpgrade(UpgradeDefinition up){
    	return myEconomy.hasUpgrade(up);
    }

    /**
     * This method returns the attack bonus for a region.
     * @return The attack bonus for a region.
     */
    public double getAttackBonus(){
    	return myEconomy.getUpgradeAttack();
    }

    /**
     * This method is used to calculate the resource income for a region.
     * @return A HashMap indicating how much of a resource should be generated.
     */
    HashMap<String,Double> getResourceIncome(){	
    	HashMap<String,Double> income=new HashMap<String,Double>();
    	HashMap<String,Double> data=myEconomy.getResourceIncome();
    	for(String res:data.keySet()){
    		income.put(res,data.get(res));
    	}
    	return income;
    }


    public int getTroopProduction(){
    	return (int)myOwner.getBuildCounts().get(this);
    }

    /**
     * This method returns the resource income of a region as a string.
     * @return
     */
    public String incomeString(){
    	return myEconomy.incomeString();
    }

    public int getResourceNumber(){
    	return myEconomy.getResourceNumber();
    }

    /**
     * This sends the encoding of a region's improvements as a character as a BitSet
     * The purpose of this method is to send compressed data about a region to the client.
     * @return A bitset representing the improvements
     */
    public BitSet getImprovements(){
    	return myEconomy.getImprovements();
    }

    /**
     * This method adds a one way connection between two regions.
     */
    public void addBorder(Region other){
    	BORDER_ZONES.add(other);
    }
    
    /**
     * This method is used by the map editor to draw the locations of regions.
     * TODO:Make this method draw region bounds as well as the "central" location of each region.
     * @param g
     */
    public void draw(Graphics g){
        int x=(int)myLocation.getX();
        int y=(int)myLocation.getY();
        g.fillRect(x-5, y-5, 10, 10);
    }

    public double compareDistance(Region loc){
        return REGION_DISTANCES.get(loc)*regionType.getDistanceFactor(loc.regionType);
    }

    public Location distanceVector(Region other){
    	return other.getCenterLoc().subtract(getCenterLoc());
    }
    
    public Location getCenterLoc(){
    	return myLocation;
    }


    public double xCenterRender(){
    	return myLocation.getX();
    }
    
    public double yCenterRender(){
    	return myLocation.getY();
    }
    
    public int getPopulation(){
    	return (int)myEconomy.getPopulation();
    }
    
    public ArrayList<Region> getBorderRegions(){
    	return BORDER_ZONES;
    }
    
    public Player getOwner(){
    	return myOwner;
    }
    
    public double getMovement(){
    	return myEconomy.getMovement();
    }
    
    public double getAttackStrength(){
    	return myEconomy.getAttackStrength();
    }
  
    public void growPopulation(double rate){
    	myEconomy.growPopulation(rate);
    }
   
    /**
     * This method is used to attack nearby enemy regions with improvements.
     */
    public void attack(){
        ArrayList<Region> enemyRegions=new ArrayList<Region>();
        for(Region r:getBorderRegions()){
            if(r.getOwner()!=getOwner()){
                enemyRegions.add(r);
            }
        }
        if(enemyRegions.size()==0){
            return;
        }

        double str=getAttackStrength();
        if(isCapital()){
            str+=5;
        }
        for(int i=0;i<str;i++){
            int n=(int)(Math.random()*enemyRegions.size());
            Region r=enemyRegions.get(n);
            double minScore=1/(r.getDefenseBonus());  //Account for a region defense bonus.
            if(Math.random()<minScore){
                r.getOwner().removeTroop(r);
            }
        }
    }

    /**
     * @param loc
     * @return
     */
    public boolean contains(Location loc){
    	return getBounds().contains(loc);
    }
    
    public int getOwnerNum(){
    	return myOwner.getNum();
    }

    /**
     * This method sets the owner of the region.
     * @param p The new region owner.
     */
    public void setOwner(Player p){
        myOwner.removeZone(this);
        myOwner=p;
        p.addZone(this);
    }

    /**
     * This method returns the midpoint between two regions.
     * @param r The other region.
     * @return
     */
    public IntLoc midPoint(Region r){
        Location xLoc=(getCenterLoc().add(r.getCenterLoc())).divide(2);
        return xLoc.intLoc();
    }
    
    public int getTroopCount(Player p){
    	return p.countTroops(this);
    }
}
