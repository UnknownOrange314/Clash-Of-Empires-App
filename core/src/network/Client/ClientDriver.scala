package network.Client

import javax.swing.{JApplet, UIManager, JFrame}
import java.awt._
import client.view.panels.GameMenu
import client.view.panels.GameDisplay
import engine.general.render.AndroidEngine
import engine.general.render.SwingEngine
import engine.general.render.RenderEngine

/**
 * This is used as an entry point to start the client.
 */
object ClientDriver{
    def main(args: Array[String]){
    	var e=new SwingEngine();
        new ClientDriver(true)
    }
}


/**
 *
 * @param network  Is the game running over a network.
 */
class ClientDriver(network:Boolean) extends JFrame{
    


    this.setTitle("Pixel Wars")
    this.setLayout(new BorderLayout())

    //Initialize a thread that creates a connection with the server.
    var serverConnection:GameConnection=null
    if(network==true){
        serverConnection=new Client()
    }
    else{
        serverConnection=new LocalConnection()
    }

    serverConnection.start()  //Start the connection with the server

    //Create the JPanel that will render the game state.
    var gameDisplay=new GameDisplay(serverConnection)
    this.add(gameDisplay,BorderLayout.CENTER)
    var menu=new GameMenu()
    menu.setFocusable(false)
    this.add(menu,BorderLayout.NORTH)

    //Setup the display
    val screenSize = Toolkit.getDefaultToolkit().getScreenSize()
    setSize(screenSize)
    this.setVisible(true)
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
}
