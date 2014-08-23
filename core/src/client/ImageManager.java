package client;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

import javax.imageio.ImageIO;

import server.model.UpgradeDefinition;

import java.io.File;
import java.util.ArrayList;

import network.client.GameConnection;
import engine.rts.model.Resource;

import java.awt.image.BufferedImage;

/**
 * This class is responsible for loading images and storing them.
 */
public class ImageManager{

    String imageDir="images/";
    String combatImageDir="images/combat/";
    String flagDir="images/flags/";
    String configLoc="mapData/TestMap/";

    public final Texture armyImage=new Texture(combatImageDir+"troop.png");
    public final Texture siegeImage=new Texture(combatImageDir+"fire.jpg");
    public final Texture battleImage=new Texture(combatImageDir+"battle.png");
    public final Texture mapBackground=new Texture(imageDir+"MapBackground.jpg");
    public final Texture capitalImage=new Texture(imageDir+"MapBackground.jpg");

    ArrayList<Texture> resourceImages=new ArrayList<Texture>();
    ArrayList<Texture> improvementImages=new ArrayList<Texture>();
    
	final GameConnection serverConnection;
	
	public ImageManager(GameConnection sCon){
		serverConnection=sCon;
	    readImages();

	}
	
    public String getFlag(String pName){
    	return flagDir+pName+".jpg";
    }
    
    public boolean unreadImages(){
    	return resourceImages==null;
    }
    
    public Texture getUpgradeImage(int uNum){
    	return improvementImages.get(uNum);
    }
    
    public Texture getResourceImage(int rNum){
    	return resourceImages.get(rNum);
    }
       
    /**
     * This method reads the images that represent the resources and improvements.
     */
    private void readImages() {

        ArrayList<Resource> resourceData=serverConnection.getResourceDefs();
        try {
        	for(Resource resource:resourceData){
            	Texture resourceImage = new Texture("images/resources/" + resource.getResourceFile());
            	resourceImages.add(resourceImage);
            }
            for(UpgradeDefinition improvement:serverConnection.improvementDefs()){
                Texture upgradeImage = new Texture("images/improvements/" + improvement.getImageLocation());
                improvementImages.add(upgradeImage);
            }
        } 
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}