package server.view.start_menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstructionPanel extends JPanel{
	
    ImageIcon instruct;
    JLabel gameInfo;
    JPanel instructionBottom;
    JButton back;
    InstructionPanel inst;
    Container cont;
    BattleDriver main;
	
    public InstructionPanel(Container c,BattleDriver m){
	main=m;
	inst=this;
	cont=c;
        setLayout(new GridLayout(1,1));

        instruct=new ImageIcon("instructions.jpg", "How to play");
        gameInfo=new JLabel(instruct);
        gameInfo.setIcon(instruct);
        add(gameInfo);
        instructionBottom=new JPanel();
        instructionBottom.setLayout(new GridLayout(1,1));
        back=new JButton("back");
        instructionBottom.add(back);
        back.addActionListener(new BackListener());
        c.add(instructionBottom,"South");

        c.add(this,"Center");
        setVisible(true);
    }

    private class BackListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
	    instructionBottom.remove(back);
	    cont.remove(inst);
	    cont.remove(instructionBottom);
	    main.displayOpeningMenu();
	}
    }
}
