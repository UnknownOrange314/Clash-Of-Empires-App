package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import network.Client.*;

public class MyGdxGame extends ApplicationAdapter {
	
	SpriteBatch batch;
	Texture img;
	
	ClientDriver main;
	
	@Override
	public void create () {
		main=new ClientDriver(false);
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		System.out.print("Started");
		Gdx.gl.glClearColor(1-Test.getColor(), Test.getColor(),0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
}
