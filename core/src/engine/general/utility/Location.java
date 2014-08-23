package engine.general.utility;

import java.awt.geom.Point2D;



/**
 * This class represents locations on the game display.
 * @param myX
 * @param myY
 */
public class Location extends Point2D{

	private double xPos;
	private double yPos;
	
	public Location(double x,double y){
		xPos=x;
		yPos=y;
	}
	
	public Location(int x,int y){
		xPos=x;
		yPos=y;
	}
	
	public double getX(){
		return xPos;
	}
	
	public double getY(){
		return yPos;
	}

   public void setLocation(double x,double y){
       xPos=x;
	   yPos=y;
   }

   public void setX(double x){
       xPos=x;
   }

   public void setY(double y){
       yPos=y;
   }


   public double magnitude(){
	   return this.compareDistance(new Location(0,0));
   }
   
   public double compareDistance(Location loc){
	   return Math.sqrt(Math.pow(xPos-loc.getX(),2)+Math.pow(yPos-loc.getY(),2));
   }
   
   @Override
   public int hashCode(){
	   return (int)(xPos*100)+(int)(yPos*1000000);
   }
   
   @Override
   public boolean equals(Object other){
	   return this.compareTo((Location)other)==0;
   }
   
   @Override
   public String toString(){
	   return xPos+":"+yPos;
   }
   
   @Override
   public Location clone(){
	  return new Location(xPos,yPos);
   }

    /**
     * This method returns an IntLoc where the x and y coordinates of this location are converted to integers.
     * @return
     */
   public IntLoc intLoc(){
	   return new IntLoc((int)this.xPos,(int)this.yPos);
   }
   
   //Compares two locations based on their distance from the top left corner.
   public int compareTo(Location loc2){
	 	double d1=Math.sqrt (Math.pow(xPos,2)+ Math.pow(yPos,2));
		double d2=Math.sqrt (Math.pow(loc2.getX(),2)+ Math.pow(loc2.getY(),2));
		if(Math.abs(d1-d2)<(Math.pow(10,-5))){
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

    public double xDistance(Location loc2){
    	return loc2.xPos-yPos;
    }
    
    public double yDistance(Location loc2){
    	return loc2.xPos-yPos;
    }
    
    public Location midpoint(Location loc2){
    	return new Location((xPos+loc2.xPos)/2,(yPos+loc2.yPos)/2);
    }

    public Location divide(double v){
    	double newX=xPos/v;
        double newY=yPos/v;
        return new Location(newX,newY);
    }
    
    public Location add(Location other){
        double newX=xPos+other.xPos;
        double newY=yPos+other.yPos;
        return new Location(newX,newY);
    }

    public Location subtract(Location other){
        double newX=xPos-other.xPos;
        double newY=yPos-other.yPos;
        return new Location(newX,newY);
    }
}
