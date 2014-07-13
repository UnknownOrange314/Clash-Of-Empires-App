package server.view.start_menu

import java.awt.Container
import java.io._
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.List
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSlider
import server.model.mapData.GameOption
import server.controller.listeners.GameStartListener
import scala.collection.JavaConversions._
import java.net.{Socket, ServerSocket}
import network.Server.{ServerThread}
import engine.general.network.DisplayCommunicator

object GameSetupMenu{
    val easyDiff="Easy: Integrated Graphics"
    val medDiff="Medium: Hybrid Graphics"
    val hardDiff="Hard: Discrete Graphics"
}

/**
 *
 * @param m The class that represents the instance of the server that is running.
 * @param c The container that contains all the Swing components.
 */
class GameSetupMenu(m:BattleDriver, c:Container) extends JPanel{

    //Initialize the slider that controls the number of players in a game
    val sliderLabel=new JLabel("Select number of computer players")
    add(sliderLabel)
    val numSelect=new JSlider(1,10)
    numSelect.setMajorTickSpacing(2)
    numSelect.setMinorTickSpacing(1)
    numSelect.setPaintTicks(true)
    numSelect.setPaintLabels(true)
    add(numSelect)

    //Add the label that displays background information for the game.
    val storyLabel=new JLabel()
    readStoryText()
    add(storyLabel)

    //Add the label that shows which map has been selected.
    val loadedMap=new JLabel("No map selected")
    add(loadedMap)

    //Add a label to keep track of the number of clients that have connected
    var clientCount=new JLabel("No other players connected")
    add(clientCount)
    val loadCallback=(f:java.io.File)=>editLoadLabel(f:java.io.File)//This is used to communicate the selected map.
    var mapFile:String=null//This is the name of the map file that contains the map for the game.

    //Start the server
    val humanPlayers=1
    var option=new GameOption(numSelect,true)
    val serverListener:DisplayCommunicator=new ServerThread(1,option)
    serverListener.start()

    //Add the button used to start the game and pass the server thread used to start the game.
    val gameStart=new JButton("start")
    gameStart.addActionListener(new GameStartListener(option))
    add(gameStart)

    var numClients=0
    setVisible(true)

    /**
     * This method is called to regenHP the label that shows the number of clients that have connected.
     * TODO: Fix bug where this method is not called when a client connects
     */
    def addClient(){
    	numClients+=1
    	clientCount.setText("Number of players connected:"+numClients)
    }

    /**
     * This method edits the label that shows which map file has been loaded.
     * TODO: Make sure that this method does not return an integer. There is no reason why this method should return
     * an integer.
     * @param fileName
     * @return
     */
    def editLoadLabel(fileName:File):Integer={
        loadedMap.setText("Selected map:"+fileName.getName())
        option.setMapName(fileName.getName())
        return -1
    }

    /**
     * This method reads the background story and displays it
     */
    def readStoryText(){
        val path:Path = Paths.get("docs/story.txt")
	try{
	    val story:List[String] =Files.readAllLines(path, StandardCharsets.UTF_8)
	    var storyText=""
	    for(line <- story){	
	        storyText+=line
	    }
	storyLabel.setText(storyText)
        } 
        
	catch{
	    case e =>e.printStackTrace()
	}
    }
}
