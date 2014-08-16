package server.clientCom;

import java.awt.Polygon;

import java.util.ArrayList;

public class RegionRenderData implements java.io.Serializable{

	private static final long serialVersionUID = 648250564749668957L;

	public static class Builder{
		public  ArrayList<Polygon> rBounds;
		public  ArrayList<Integer> xL;
		public  ArrayList<Integer> yL;	
		
		public Builder(){
			
		}
		
		public Builder rBounds(ArrayList<Polygon> bounds){
			rBounds=bounds;
			return this;
		}
		
		public Builder xLocs(ArrayList<Integer> x){
			xL=x;
			return this;
		}
		
		public Builder yLocs(ArrayList<Integer> y){
			yL=y;
			return this;
		}
		
		public RegionRenderData build(){
			return new RegionRenderData(this);
		}
	}
	
	public final ArrayList<Polygon> regionBounds;
	public final ArrayList<Integer> xLocs;
	public final ArrayList<Integer> yLocs;
	
	public RegionRenderData(Builder b){
		regionBounds=b.rBounds;
		xLocs=b.xL;
		yLocs=b.yL;
	}	
}
