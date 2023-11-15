package com.jbs.universe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jbs.universe.screen.console.Console;

public class GameMain extends ApplicationAdapter {
	OrthographicCamera camera;
	FrameBuffer frameBuffer;
	SpriteBatch spriteBatch;

	Console console;
	
	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 600);
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 800, 600, false);
		spriteBatch = new SpriteBatch();

		console = new Console();
	}

	protected void handleInput() {
		Gdx.input.setInputProcessor(new InputAdapter() {

			@Override
			public boolean keyDown (int keyCode) {
				if(keyCode == 111) {
					System.exit(0);
				}

				return true;
			}
		});
	}

	public void update() {
		handleInput();
	}

	@Override
	public void render() {
		update();

		ScreenUtils.clear(0, 0, 0, 1);
		console.draw(frameBuffer, spriteBatch, camera);

		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		console.font.draw(spriteBatch, String.valueOf(Gdx.graphics.getFramesPerSecond()), 782, 600);
		spriteBatch.end();
	}
	
	@Override
	public void dispose() {
		frameBuffer.dispose();
		spriteBatch.dispose();
		console.dispose();
	}
}
