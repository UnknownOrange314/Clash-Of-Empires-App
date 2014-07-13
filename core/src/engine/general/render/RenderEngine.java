package engine.general.render;


import javax.swing.*;

public class RenderEngine {
	public RenderEngine(){
	    try {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	    }
	    catch(Exception e){
	        e.printStackTrace();
	    }	
	}
	
}
