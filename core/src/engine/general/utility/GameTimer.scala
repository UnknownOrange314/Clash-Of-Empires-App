package engine.general.utility

/**
 * This is a simple timer class
 */
class GameTimer(){
    
    val startTime=System.nanoTime()
    var maxTime=9999999.0

    def this(mTime:Double){
        this()
        maxTime=mTime
    }

    def getTime= (System.nanoTime()-startTime)*Math.pow(10,-9)

    def printTime(data:String){
        var time=(System.nanoTime()-startTime)*Math.pow(10,-9)
        if(time>maxTime){
            println("There is a speed issue")
        }
        println(data+" "+time)
    }
}
