package engine.rts.model;

import java.util.ArrayList;

import com.badlogic.gdx.utils.JsonValue;

//TODO:Make sure these fields are not public.
public class Resource implements java.io.Serializable{


    public static ArrayList<Resource> resourceList=new ArrayList<Resource>();
 
    
    public static ArrayList<String> getResourceNames(){
    	ArrayList<String> names=new ArrayList<String>();
    	for(Resource res: resourceList){
    		names.add(res.getName());
    	}
    	return names;
    }
 
    public final String name;
    public final String image;
    public final int id;
    
    public Resource(JsonValue v){
    	name=v.getString("name");
    	image=v.getString("image");
    	id=v.getInt("id");
    	resourceList.add(this);
    }
    
 
    public String getName(){
    	return name;
    }
    
    public String getResourceFile(){
    	return image;
    }
    
    public int getId(){
    	return id;
    }
    
    public void printData(){
    	System.out.println(name+":"+image);
    }
    
    
}