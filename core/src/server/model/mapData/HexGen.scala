package server.model.mapData

import engine.general.utility.Location
import java.awt.Polygon
import java.util.ArrayList
import java.util.Arrays
import scala.collection.JavaConversions._
import server.model.playerData.Region
import java.awt.geom.Rectangle2D


/**
 * This class generates a simple map with the regions arranged in a grid.
 */
class HexGen extends MapGen{
  
    def generateShape(regionList:ArrayList[Region]){ 
    	for(region:Region<-regionList){  
            val cLoc=region.getCenterLoc
            val hexSize=82.0 //Size of hex from center to edge intersection. 
            val xPts: Array[Int] = new Array[Int](6)
            val yPts: Array[Int] = new Array[Int](6)
            for(i<-0 until 6){
            	var angle=(i.toDouble/6.0)*(2*Math.PI)
                val x=hexSize*Math.cos(angle)
                val y=hexSize*Math.sin(angle)
                xPts(i)=(x+cLoc.getX()).toInt
                yPts(i)=(y+cLoc.getY()).toInt              
            }
            val p=new Polygon(xPts,yPts,6)
            region.setBounds(p)
        }
    }
    
    /**
     * Automatically sets up a list of border zones for this zone.
     */        
    def setBorderingZones(region:Region,regions:ArrayList[Region]){
    	var bounds=region.getBounds().getBounds2D()
        var boundBox=new Rectangle2D.Double(bounds.getMinX()-1,bounds.getMinY()-1,bounds.getWidth()+2,bounds.getHeight()+2)
        for(other<-regions){
            var otherBounds=other.getBounds().getBounds2D()
            var boundBox2=new Rectangle2D.Double(otherBounds.getMinX()-3,otherBounds.getMinY()-3,otherBounds.getWidth()+6,otherBounds.getHeight()+6)
            if(boundBox.intersects(boundBox2)&&other!=this){
            	region.addBorder(other)
            }
        }      
    }
    
    def generateMap(m: GameMap):ArrayList[Region]={
      
        val startX=100
        val endX=1400
        val startY=100
        val endY=900
        val zoneSize: Int=140
        var rowOffset=zoneSize/2
        var regions=new ArrayList[Region]
        
        for(x <-startX until endX by(zoneSize*.89).toInt){
            for( y <-startY until endY by(zoneSize)){
                val cityLoc: Location=new Location(x,y+rowOffset)
                var z: Region=new Region(m.getPlayers.get(0),cityLoc)
                regions.add(z)
                if(Math.random<0.1){
                    var waterBorder=false
                	for(border<-z.getBorderRegions()){
                		if(z.getType==TerrainType.getType("water")){
                			waterBorder=true
                		}
                	}
                	if(waterBorder==true){
                		z.setType(TerrainType.getType("water"))  
                	}else{
                		z.setType(TerrainType.getType("standard"))
                	}
                }
                else{
                	z.setType(TerrainType.getType("standard"))
                }
            }
            if (rowOffset==zoneSize/2){
                rowOffset=0
            }
            else{
                rowOffset=zoneSize/2
            }
        }
        
        generateShape(regions)

        for (reg <- regions) {
            setBorderingZones(reg,regions)
        }
        
        for (reg <- regions) {
            reg.removeWaterBorder()
        }
        
        val rr: Iterator[Region] = regions.iterator
        while (rr.hasNext) {
            if (rr.next.getType == "water") {
                rr.remove
            }
        }
        
        for (r <- regions) {
            r.updateDistances
        }
        
        return regions
    }
}