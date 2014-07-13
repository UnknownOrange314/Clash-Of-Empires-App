package engine.general.view

import javax.swing.{JPanel}
import network.Client.GameConnection
import java.awt.image.BufferedImage
import java.util.ArrayList
import scala.collection.JavaConversions._
import java.util.Calendar
import java.text.SimpleDateFormat
import java.io.IOException
import java.io.File
import javax.imageio.ImageIO
import java.awt.Graphics
import java.awt.Toolkit

/**
 * This class is used to represent the display
 * @param serverConnection The connection with the server
 */
abstract class Display(var serverConnection:GameConnection) extends JPanel{

    val screenSize = Toolkit.getDefaultToolkit().getScreenSize()
    val screenW = screenSize.getWidth().toInt-50
    val screenH= screenSize.getHeight().toInt
    val panelW=400 //This is the width of the interface on the right of the screen.

    /**
     * These values represent the height and the width of the game display.
     * Interface components may be outside these bounds.
     */
    val DRAW_HEIGHT=screenH
    val DRAW_WIDTH=screenW-panelW
    var gameComponents=new ArrayList[drawArea]//These represent the interface components.
    var offscreen = new BufferedImage(DRAW_WIDTH, DRAW_HEIGHT, BufferedImage.TYPE_INT_RGB)
    var bufferGraphics = offscreen.getGraphics()

    /**
     * This method returns the object used to draw graphics on the screen.
     * @return
     */
    def graphicsWriter=bufferGraphics

    /**
     * This function is used to save an image of the game.
     */
    def takeScreenshot(){

        val dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
        val cal = Calendar.getInstance()
        val name="screenshot"+dateFormat.format(cal.getTime())+".png"
        try{
            val outputfile = new File(name)
            ImageIO.write(offscreen, "png", outputfile)
        }
        catch{
            case e:IOException => e.printStackTrace()
        }
    }

    /**
     * This method adds a drawArea object to the display.
     */
    def addComponent(c:drawArea)=gameComponents.add(c)

    /**
     * This method draws the components on the display.
     * @param panelGraphics The graphics object. It is a parameter
     * for the JFrame's paint method, so there should
     * be a way to avoid passing this as a parameter.
     *
     */
    def drawComponents(panelGraphics:Graphics){
        gameComponents.foreach(c=>c.drawToPanel(panelGraphics,this))
    }

    /**
     * This method sets up the drawing area so that it renders the game state.
     */
    protected def setupFrame(){
        //Prepare for double buffering
        bufferGraphics.clearRect(0, 0, DRAW_WIDTH, DRAW_HEIGHT)
        super.paint(bufferGraphics)
        //Make sure that key presses work.
        this.requestFocusInWindow(false)
    }
}
