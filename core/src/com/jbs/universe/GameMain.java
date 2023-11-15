package com.jbs.universe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jbs.universe.components.InputManager;

public class GameMain extends ApplicationAdapter {
	OrthographicCamera camera;
	SpriteBatch spriteBatch;
	BitmapFont font;

	InputManager inputManager;
	
	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 600);
		spriteBatch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("Fonts/Consolas_18.fnt"), Gdx.files.internal("Fonts/Consolas_18.png"), false);
		font.setColor(Color.WHITE);

		inputManager = new InputManager();
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
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();

		font.draw(spriteBatch, String.valueOf(Gdx.graphics.getFramesPerSecond()), 782, 600);

		for (int i = 0; i < 59; i++) {
			font.draw(spriteBatch, "This is a test line.", 0, 18 * (i + 1));
		}

		spriteBatch.end();
	}
	
	@Override
	public void dispose() {
		spriteBatch.dispose();
		font.dispose();
	}
}
