package server.model.mapData;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

import server.model.UpgradeDefinition;
import server.model.ai.AiPersona;

import java.awt.Polygon;
import java.util.HashMap;
import java.io.*;

import server.model.playerData.Region;

import java.awt.geom.Point2D;

import engine.general.utility.Location;
import engine.rts.model.Resource;

/**
 * This class contains methods for reading game configuration data from JSON files.
 */
class ResourceReader{ 
  
    final static String imageDir="images/";
    final static String combatImageDir="images/combat/";
    final static String flagDir="images/flags/";
    final static String configLoc="mapData/TestMap/";

    static String getFlag(String nName){
    	return flagDir+nName+".jpg"; 
    }
    
    static String resourceDataLoc= configLoc+"Resources.json";
    static String improvementDataLoc= configLoc+"Improvements.json"; 
    static String regionDataLoc=configLoc+"regions.json";
    static String personalityLoc=configLoc+"AI_Personalities.json";

    static void readRegionShapes(ArrayList<Region> regions,String saveLoc){
        HashMap<Point2D,Polygon> regionShapes=new HashMap<Point2D,Polygon>();
        try{
            FileInputStream fileIn =new FileInputStream(saveLoc);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            regionShapes =(HashMap<Point2D,Polygon>)in.readObject();
            in.close();
            fileIn.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        for(Point2D loc:regionShapes.keySet()){
            Polygon poly=regionShapes.get(loc);
        	Location cLoc=new Location(loc.getX(),loc.getY());
            for(Region region:regions) {
                if(region.getCenterLoc().equals(cLoc)){
                    region.setBounds(poly);
                }
            }
        }
    }

    /**
     * This method saves the region shapes
     * @param regions
     */
    public static void saveRegionShapes(ArrayList<Region> regions,String saveLoc){
        
        HashMap<Point2D,Polygon> regionShapes=new HashMap<Point2D,Polygon>();
        for(Region region:regions){
            Location cLoc=region.getCenterLoc();
            regionShapes.put(new Point2D.Double(cLoc.getX(),cLoc.getY()),region.getBounds());
        }
        try{
            FileOutputStream fileOut =new FileOutputStream(saveLoc);
            ObjectOutputStream out =new ObjectOutputStream(fileOut);
            out.writeObject(regionShapes);
            out.close();
            fileOut.close();
        }
        catch(Exception e){
            e.printStackTrace();
         }
    }

    /**
     * This method reads the types of resources that will be used in the game.
     * @return
     */
    public static ArrayList<Resource> readResources(){

        FileHandle handle=Gdx.files.internal(resourceDataLoc);
        String text=handle.readString();
        JsonValue root=new JsonReader().parse(text);
        for(JsonValue v:root.child()){
        	new Resource(v);
        }
        ArrayList<Resource> resources=Resource.resourceList;
        System.out.println("Number of resources:"+resources.size());
        return resources;
        
    }

    /**
     * This method will be used to read the types of upgrades that will be used in the game.
     * @param resourceList The list of resources used in the game.
     * @return
     */
    public static ArrayList<UpgradeDefinition>readUpgrades(ArrayList<Resource> resourceList){
        
        ArrayList<UpgradeDefinition> improvements=new ArrayList<UpgradeDefinition>();
        FileHandle handle=Gdx.files.internal(improvementDataLoc);
        String text=handle.readString();
        JsonValue root=new JsonReader().parse(text);

        for (JsonValue v:root.child()){
        	improvements.add(new UpgradeDefinition(v,resourceList));
        }
        System.out.println("Number of upgrades:"+improvements.size());
        return improvements;
    }

    /**
     * This method is used to read the terrain types used.
     * @return
     */
    public static ArrayList<TerrainType> readTerrainTypes(){

    	ArrayList<TerrainType> terrainTypes=new ArrayList<TerrainType>();
        FileHandle handle=Gdx.files.internal(regionDataLoc);
        String text=handle.readString();
        JsonValue root=new JsonReader().parse(text);  
        
        for (JsonValue item:root.child()){
        	terrainTypes.add(new TerrainType(item));
        }
        return terrainTypes;
    }

    /**
     * This method is used to read the different AI personalities
     */
    public static ArrayList<AiPersona> readAIPersonalities(){

        ArrayList<AiPersona> personaList=new ArrayList<AiPersona>();
        FileHandle handle=Gdx.files.internal("mapData/TestMap/AI_Personalities.json");
        String text=handle.readString();
        JsonValue root=new JsonReader().parse(text);
        for (JsonValue item:root.child()){
        	personaList.add(new AiPersona(item));
        }
        return personaList;
    }

    /**
     * This method reads configuration data about the game from JSON files.
     */
    public static void readConfigData(){
        ResourceReader.readResources();
        ResourceReader.readUpgrades(Resource.resourceList);
        ResourceReader.readTerrainTypes();
        ResourceReader.readAIPersonalities();
    }
}
