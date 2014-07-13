package server.view.start_menu;

import server.controller.listeners.startMenuListeners;
import javax.swing.*;
import java.awt.*;

public class StartMenu extends JPanel {
	
    private JButton start;//This button starts the game
    private JButton instructions;//This button shows instructions on how to play the game
    private JButton editor;//This button opens up the map editor
    private JButton quit; //This button is used to quit the game
    public StartMenu(Container c,JPanel top,BattleDriver Driver){
	
	setSize(1000,800);
        setLayout(new GridBagLayout());
        GridBagConstraints c2=new GridBagConstraints();

        c2.gridx=2;
        c2.gridy=0;
        c2.weighty=0.5;
        c2.ipadx=200;
        c2.ipady=100;
        start=new JButton("start");
        add(start,c2);
        	
        quit=new JButton("quit");
        c2.gridx=2;
        c2.gridy=2;
        c2.weighty=0.5;
        c2.ipady=100;
        c2.ipadx=200;
        add(quit,c2);

        instructions=new JButton("instructions");
        c2.gridx=2;
        c2.gridy=1;
        c2.weighty=0.5;
        c2.ipady=100;
        c2.ipadx=200;
        add(instructions,c2);
        
        startMenuListeners startCont=new startMenuListeners(this,c,instructions,start,quit,top);
        instructions.addActionListener(startCont);
        quit.addActionListener(startCont);
        start.addActionListener(startCont);
        	
        c2.gridx=2;
        c2.gridy=3;
        c2.weighty=0.5;
        c2.ipady=100;
        c2.ipadx=200;
        editor=new JButton("Click on button to open map editor");
        add(editor,c2);
    }
}
