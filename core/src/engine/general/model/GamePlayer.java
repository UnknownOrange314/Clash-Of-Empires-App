package engine.general.model;

import com.badlogic.gdx.graphics.Color;
import engine.rts.model.StratMap;

/**
 * This object represents the people and computer players in the game.
 */
public class GamePlayer{
    
	static int numPlayers=0;
	
	//TODO: Refactor so that this function can be removed.
    public static void setPlayerCount(int c){
    	numPlayers=c;
    }

    final int myNum;
    final Color myColor;
    String myName=null;
    
    public GamePlayer(){
    	myNum=numPlayers;
        myColor=StratMap.playerColors[myNum];
        GamePlayer.numPlayers+=1;
    }

    public int getNum(){
    	return myNum;
    }
    
    public String getName(){
    	return myName;
    }
    
    public void setName(String n){
    	myName=n;
    }
}