package client.view.panels

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSlider

class GameMenu extends JPanel{

    var zoomSlider=new JSlider(-10,10,0)
    var menu=new JPanel()
    var quit=new JButton("Quit")
    var save=new JButton("Save")
    var pause=new JButton("Pause")
    var options=new JButton("Options")

    quit.addActionListener(new QuitListener())

    add(new JLabel("Zoom"))
    add(zoomSlider)
    add(quit)
    add(save)
    add(pause)
    add(options)

}

class QuitListener extends ActionListener{
    def actionPerformed(e:ActionEvent){
        System.exit(1)
    }
}
