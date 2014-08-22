package server.model.mapData;


import engine.general.utility.Location;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import server.model.playerData.Region;

import java.awt.geom.Rectangle2D;


/**
 * This class generates a simple map with the regions arranged in a grid.
 */
class HexGen implements MapGen{
  
    public void generateShape(ArrayList<Region> regionList){ 
    	for(Region reg:regionList){  
            Location cLoc=reg.getCenterLoc();
            double hexSize=82.0; //Size of hex from center to edge intersection. 
            int[] xPts=new int[6];
            int[] yPts=new int[6];
            for(int i=0;i<6;i++){
            	double angle=(((double)i)/6.0)*(2*Math.PI);
                double x=hexSize*Math.cos(angle);
                double y=hexSize*Math.sin(angle);
                xPts[i]=(int)(x+cLoc.getX());
                yPts[i]=(int)(y+cLoc.getY());              
            }
            Polygon p=new Polygon(xPts,yPts,6);
            reg.setBounds(p);
        }
    }
    
    /**
     * Automatically sets up a list of border zones for this zone.
     */        
    public void setBorderingZones(Region region,ArrayList<Region> regions){
    	Rectangle2D bounds=region.getBounds().getBounds2D();
        Rectangle2D boundBox=new Rectangle2D.Double(bounds.getMinX()-1,bounds.getMinY()-1,bounds.getWidth()+2,bounds.getHeight()+2);
        for(Region other:regions){
            Rectangle2D otherBounds=other.getBounds().getBounds2D();
            Rectangle2D boundBox2=new Rectangle2D.Double(otherBounds.getMinX()-3,otherBounds.getMinY()-3,otherBounds.getWidth()+6,otherBounds.getHeight()+6);
            if(boundBox.intersects(boundBox2)&&other!=region){
            	region.addBorder(other);
            }
        }      
    }
    
    public ArrayList<Region> generateMap(GameMap m){
      
        int startX=100;
        int endX=1400;
        int startY=100;
        int endY=900;
        int zoneSize=140;
        int rowOffset=zoneSize/2;
        ArrayList<Region>regions=new ArrayList<Region>();
        
        for(int x=startX;x<endX;x+=(int)(zoneSize*.89)){
            for( int y=startY;y<endY;y+=zoneSize){
                Location cityLoc=new Location(x,y+rowOffset);
                Region z=new Region(m.getPlayers().get(0),cityLoc);
                regions.add(z);
                if(Math.random()<0.1){
                    boolean waterBorder=false;
                	for(Region border:z.getBorderRegions()){
                		if(z.getType()==TerrainType.WATER){
                			waterBorder=true;
                		}
                	}
                	if(waterBorder==true){
                		z.setType(TerrainType.getType("water"));  
                	}else{
                		z.setType(TerrainType.getType("standard"));
                	}
                }
                else{
                	z.setType(TerrainType.getType("standard"));
                }
            }
            if (rowOffset==zoneSize/2){
                rowOffset=0;
            }
            else{
                rowOffset=zoneSize/2;
            }
        }
        
        generateShape(regions);

        for (Region reg:regions) {
            setBorderingZones(reg,regions);
        }
        
        for (Region reg:regions) {
            reg.removeWaterBorder();
        }
        
        Iterator<Region> rr = regions.iterator();
        while (rr.hasNext()) {
            if (rr.next().getType() == "water") {
                rr.remove();
            }
        }
        
        for (Region r:regions) {
            r.updateDistances();
        }
    
        return regions;
    }
}