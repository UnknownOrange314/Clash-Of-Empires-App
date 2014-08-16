package client.controller;

public class RegionCommand {
	
	public static class Builder{
		
		String command="";
		int owner=0;
		String name="";
		public Builder(){
			
		}
		
		public Builder command(String c){
			command=c;
			return this;
		}
		
		public Builder owner(int own){
			owner=own;
			return this;
		}
		
		public Builder name(String nm){
			name=nm;
			return this;
		}
		
		public RegionCommand build(){
			return new RegionCommand(this);
		}
	}
	
	public final String command;
	public final int owner;
	public final String name;
	
	public RegionCommand(Builder b){
		command=b.command;
		owner=b.owner;
		name=b.name;
	}
}
