package server.model.ai;

import java.util.ArrayList;
import com.badlogic.gdx.utils.JsonValue;


/**
 * Created with IntelliJ IDEA.
 * User: Bharat
 * Date: 1/9/13
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class AiPersona implements java.io.Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static ArrayList<AiPersona> AiList=new ArrayList<AiPersona>();
    		
    /**
     * This method will pick the AI persona that is listed first in the configuration file.
     */
    public static AiPersona pickDefaultPersona(){
    	return AiList.get(0);
    }
    
    /**
     *This method will pick the neutral AI persona
     */
    public static AiPersona getNeutralPersona(){
        for (AiPersona persona:AiList){
            if (persona.getName().equals("neutral")){
                return persona;
            }
        }
        System.err.println("There is no neutral AI personality");
        return null;
    }
    
    final String name;
    final double reinforceRate;
    final double aggression;
    final double paranoia;
    
    public AiPersona(JsonValue data){
    	name=data.getString("name");
    	reinforceRate=data.getDouble("reinforce_rate");
    	aggression=data.getDouble("aggression");
    	paranoia=data.getDouble("paranoia");
    	AiPersona.AiList.add(this);
    }
    
    public double getAggression(){
    	return aggression;
    }
    
    public double getParanoia(){
    	return paranoia;
    }
    
    public String getName(){
    	return name;
    }
}