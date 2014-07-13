package server.view.start_menu;

import javax.swing.*;
import java.awt.*;

public class BattleDriver extends JFrame{
	
    private static final long serialVersionUID = 938117280571199025L;
    private static final int DEFAULT_FPS=60;
    private static long period1=1000/DEFAULT_FPS;
    public static final int WIDTH=1600;
    public static final int HEIGHT=800;
    private StartMenu startMenu;
    private JPanel top;
    private GameSetupMenu gameSetup;
    private JPanel optionPanel;

    InstructionPanel instructionPanel;
    private Container c;

    public static void main(String[] args){
    	new BattleDriver();
    }
    
    public BattleDriver(){
    	
         super("Pixel Wars");
         pack();
         setResizable(true);
         this.setSize(WIDTH,HEIGHT);
         this.setVisible(true);
         c=getContentPane();
         setResizable(true);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         MainMenuFacade.addDriver(this);
         displayOpeningMenu();
    }

    /**
     * Show the starting menu.
     */
    public void displayOpeningMenu(){
    	
    	top=new JPanel();
        startMenu=new StartMenu(c,top,this);
        c.add(top,"North");
        c.add(startMenu,"Center");
        setVisible(true);
    }
    
    public void showGameSetup(){
    	gameSetup=new GameSetupMenu(this,c);
    	c.add(gameSetup,"Center");
    	setVisible(true);
    }

    public void showInstructions(){
    	instructionPanel=new InstructionPanel(c,this);  
    }
}
