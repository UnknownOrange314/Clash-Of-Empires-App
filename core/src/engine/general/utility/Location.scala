package engine.general.utility

import java.awt.geom.Point2D

class Point(val x:Int, val y:Int)

/**
 * This class represents locations on the game display.
 * @param myX
 * @param myY
 */
class Location(var myX:Double, var myY:Double) extends Point2D(){

    def this(x1:Int, x2:Int)=this(x1.toDouble,x2.toDouble)
    def getX(): Double=myX
    def  getY(): Double=myY

   def setLocation(x:Double, y:Double){
       myX=x
	   myY=y
   }

   def setX(x:Double){
       myX=x
   }

   def setY(y: Double){
       myY=y
   }


    def magnitude():Double=this.compareDistance(new Location(0,0))
    def compareDistance(loc: Location): Double=Math.sqrt(Math.pow(myX-loc.getX(),2)+Math.pow(myY-loc.getY(),2))
    override def toString(): String=myX+":"+myY
    override def clone(): Location=new Location(myX,myY)//returns a copy of the location
    def equals(o:Location):Boolean=this.compareTo(o)==0

    /**
     * This method returns an IntLoc where the x and y coordinates of this location are converted to integers.
     * @return
     */
    def intLoc=new IntLoc(this.myX.toInt,this.myY.toInt)

    def compareTo(loc2:Location):Integer={
	 	val d1=Math.sqrt (Math.pow(myX,2)+ Math.pow(myY,2))
		val d2=Math.sqrt (Math.pow(loc2.getX(),2)+ Math.pow(loc2.getY(),2))
		if(Math.abs(d1-d2)<(Math.pow(10,-5)))
			return 0
		if(d2<d1)
			return -1
		if(d1<d2)
			return 1
		else return 0
    }

    def xDistance(loc2:Location):Double=loc2.myX-myX
    def yDistance(loc2:Location):Double=loc2.myY-myY
    def midpoint( loc2:Location): Location=new Location((myX+loc2.myX)/2,(myY+loc2.myY)/2)

    def divide(v:Double):Location={
      return this/v;
    }
    def /(v:Double):Location={
        val newX=myX/v
        val newY=myY/v
        return new Location(newX,newY)
    }
    
    def add(other:Location):Location={
      return other+this;
    }
    def +(other:Location):Location={
        val newX=myX+other.myX
        val newY=myY+other.myY
        return new Location(newX,newY)
    }

    def subtract(other:Location):Location={
    	return this-other;
    }
    
    def -(other:Location):Location={
        val newX=myX-other.myX
        val newY=myY-other.myY
        return new Location(newX,newY)
    }
}
