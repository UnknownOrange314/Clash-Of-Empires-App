package engine.general.utility;


/**
 * This class represents points as integers.
 * @param xc
 * @param yc
 */
public class IntLoc{
	
	private int myX;
	private int myY;
	
	public IntLoc(int xc,int yc){
		myX=xc;
		myY=yc;
	}

	public int getX(){
		return myX;
	}
	
	public int getY(){
		return myY;
	}

	public double compareDistance(Location loc){
		return Math.sqrt(Math.pow(myX-loc.getX(),2)+Math.pow(myY-loc.getY(),2));
	}

    @Override
    public int hashCode(){
    	return myY*10000+myX;
    }
    
    @Override
    public boolean equals(Object loc){
        IntLoc other=(IntLoc)loc;
        return this.compareTo(other)==0;
    }
    
    @Override
    public String toString(){
    	return myX+":"+myY;
    }
    
    @Override
    public Location clone(){
    	return new Location(myX,myY);
    }
    

    //Compares two locations based on their distance from the top left corner.
    public int compareTo(IntLoc loc2){
    	
	 	double d1=Math.sqrt (Math.pow(myX,2)+ Math.pow(myY,2));
		double d2=Math.sqrt (Math.pow(loc2.getX(),2)+ Math.pow(loc2.getY(),2));
		if((d1-d2)<(Math.pow(10,-5))){
			return 0;
		}
		if(d2<d1){
			return -1;
		}
		if(d1<d2){
			return 1;
		}
		return 0;
    }
    
    //TODO: Make this return an IntLoc object
    public Location midpoint(Location loc2){
    	return new Location((myX+loc2.getX())/2,(myY+loc2.getY())/2);
    }
}