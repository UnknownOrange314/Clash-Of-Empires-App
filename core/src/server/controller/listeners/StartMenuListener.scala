package server.controller.listeners

import javax.swing._
import java.awt._
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import server.view.start_menu.{MainMenuFacade, StartMenu}

class startMenuListeners(var startMenu: StartMenu, var c: Container, var inst: JButton, var start: JButton, var quit: JButton, var top: JPanel) extends ActionListener {

    def actionPerformed(e: ActionEvent) {
        if (e.getSource eq inst) {
            c.remove(startMenu)
            MainMenuFacade.showInstructions
        }
        if (e.getSource eq start) {
            c.remove(startMenu)
            c.remove(top)
            MainMenuFacade.showGameSetup
        }
        if (e.getSource eq quit) {
            System.exit(1)
        }
    }
}
