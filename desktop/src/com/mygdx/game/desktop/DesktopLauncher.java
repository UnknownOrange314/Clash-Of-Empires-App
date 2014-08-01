package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

/**
 * This is the launcher for the desktop version.
 * @author Gulu
 *
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height=MyGdxGame.HEIGHT;
		config.width=MyGdxGame.WIDTH;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
