package client.controller;

public class MarketCommand implements java.io.Serializable{
	
	public static final String SELL="sell";
	public static final String BUY="buy";
	
	public static class Builder{
		String instruction="";
		String resource="";
		
		public Builder(){
			
		}
		
		public Builder instruction(String in){
			instruction=in;
			return this;
		}
		
		public Builder resource(String res){
			resource=res;
			return this;
		}
		
		public MarketCommand build(){
			return new MarketCommand(this);
		}
	}
	
	public final String instruction;
	public final String resource;
	
	public MarketCommand(Builder b){
		instruction=b.instruction;
		resource=b.resource;
	}
}