package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import network.Client.Client;
import network.Client.GameConnection;
import network.Client.LocalConnection;
import client.view.panels.GameDisplay;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGdxGame extends ApplicationAdapter {
	
	final Long PAINT_INTERVAL=40L;
	SpriteBatch batch;
	Texture img;
	
	GameConnection serverConnection;
	GameDisplay display;
	boolean network=false; //Is the game running over a network.
	
	@Override
	public void create () {
		if(network==true){
			serverConnection=new Client();
		}else{
			serverConnection=new LocalConnection();
		}
		serverConnection.start();
		display=new GameDisplay(serverConnection);

	}

	@Override
	public void render () {
		System.out.print("Started");
		Gdx.gl.glClearColor(1,1,0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		Long start=TimeUtils.millis();
		display.update();
		Long curTime=TimeUtils.timeSinceMillis(start);
		Long pauseTime=PAINT_INTERVAL-curTime;
		try{
			if(pauseTime>0){
				Thread.sleep(pauseTime);
			}else{
				System.out.println("Taking too much time:"+pauseTime);
			}
		}catch(Exception e){
			System.out.println("Error:"+e);
		}

		
	}
}
