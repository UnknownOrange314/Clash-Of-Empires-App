package com.mygdx.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import network.client.GameConnection;
import network.client.LocalConnection;
import client.view.panels.GameDisplay;

import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

public class MyGdxGame extends ApplicationAdapter {
	
	public final static int WIDTH=1900;
	public final static int HEIGHT=900;
	final static Long PAINT_INTERVAL=40L;
	
	SpriteBatch batch;
	Texture img;
	
	GameConnection serverConnection;
	GameDisplay display;
	boolean network=false; //Is the game running over a network.
	OrthographicCamera camera;
	private Rectangle glViewport;
	
	@Override
	public void create () {
		camera=new OrthographicCamera(WIDTH,HEIGHT);
		camera.position.set(WIDTH/2,HEIGHT/2,0);

		serverConnection=new LocalConnection();
		serverConnection.start();
		display=new GameDisplay(serverConnection,camera);
        glViewport = new Rectangle(0, 0, WIDTH, HEIGHT);

	}

	@Override
	public void render () {
		
		GL20 g=Gdx.graphics.getGL20();
		g.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		Gdx.gl.glClearColor(1,1,1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		g.glViewport((int) glViewport.x, (int) glViewport.y,
	                (int) glViewport.width, (int) glViewport.height);

	    camera.update();
		
		Long start=TimeUtils.millis();
		display.update();
		Long curTime=TimeUtils.timeSinceMillis(start);
		Long pauseTime=PAINT_INTERVAL-curTime;
		try{
			if(pauseTime>0){
				Thread.sleep(pauseTime);
			}else{
				System.out.println("Game taking too long:"+pauseTime);
			}
		}catch(Exception e){
			System.out.println("Error:"+e);
		}

		
	}
}
