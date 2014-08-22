package engine.rts.model;

import com.badlogic.gdx.graphics.Color;

import engine.general.model.PlayArea;

import java.util.ArrayList;

import server.model.playerData.Player;

/**
 * This represents a generic map object that can be applied to many different
 * RTS games.
 */
public abstract class StratMap extends PlayArea{

    //These values represent player colors.
    public final static Color[] playerColors=new Color[10];
    static{
        playerColors[0] = new Color(178f/255f, 34f/255f, 34f/255f,1);
        playerColors[1] = new Color(34f/255f, 139f/255f, 34f/255f,1);
        playerColors[2] = new Color(92f/255f, 64f/255f, 51f/255f,1);
        playerColors[3] = new Color(50f/255f, 50f/255f, 178f/255f,1);
        playerColors[4] = new Color(184f/255f, 134f/255f, 11f/255f,1);
        playerColors[5] = new Color(102f/255f, 2f/255f, 60f/255f,1);
    }


    protected ArrayList<Player> myPlayers = new ArrayList<Player>();  //The list of players in the game.
    
    protected void setPlayerNames(){
        for (Player p:myPlayers) {
            p.setName();
        }
    }

    public ArrayList<Player> getPlayers(){
        return myPlayers;
    }
}
