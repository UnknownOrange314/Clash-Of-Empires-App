package engine.general.utility

/**
 * This class represents points as integers.
 * @param xc
 * @param yc
 */
class IntLoc(xc:Int, yc:Int){

    var myX: Int=xc
	var myY: Int=yc
	def getX(): Integer=myX
	def  getY(): Integer=myY
	
	def setLocation(x:Int, y:Int){
		myX=x
		myY=y
	}
	
	def setX(x:Int){
		myX=x
	}
	
	def setY(y: Int){
		myY=y
	}
	def compareDistance(loc: Location): Double=Math.sqrt(Math.pow(myX-loc.getX(),2)+Math.pow(myY-loc.getY(),2))

    override def hashCode():Int=myY*10000+myX
    
    override def equals(loc:Any):Boolean={
        val other=loc.asInstanceOf[IntLoc]
        return other.myX==myX&&other.myY==myY
    }
    
    override def toString(): String=myX+":"+myY
    override def clone(): Location=new Location(myX,myY)//returns a copy of the location
    def equals(o:Location)=this.compareTo(o)==0

    //Compares two locations based on their distance from the top left corner.
    def compareTo(loc2:Location):Int={
    	
	 	val d1=Math.sqrt (Math.pow(myX,2)+ Math.pow(myY,2))
		val d2=Math.sqrt (Math.pow(loc2.getX(),2)+ Math.pow(loc2.getY(),2))
		if((d1-d2)<(Math.pow(10,-5)))
			return 0
		if(d2<d1)
			return -1
		if(d1<d2)
			return 1
		else return 0
    }	
    def  midpoint( loc2:Location): Location=new Location((myX+loc2.myX)/2,(myY+loc2.myY)/2)
}
