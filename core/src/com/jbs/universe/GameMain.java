package com.jbs.universe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jbs.universe.components.Keyboard;
import com.jbs.universe.screen.InputBar;
import com.jbs.universe.screen.console.Console;
import com.jbs.universe.screen.console.Line;

public class GameMain extends ApplicationAdapter {
	FrameBuffer frameBuffer;
	SpriteBatch spriteBatch;
	BitmapFont font;

	Keyboard keyboard;
	InputBar inputBar;
	Console console;
	
	@Override
	public void create() {
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 800, 600, false);
		spriteBatch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("Fonts/Code_New_Roman_18.fnt"), Gdx.files.internal("Fonts/Code_New_Roman_18.png"), false);

		keyboard = new Keyboard();
		inputBar = new InputBar();
		console = new Console();
	}

	protected void handleInput() {
		Gdx.input.setInputProcessor(new InputAdapter() {

			@Override
			public boolean keyDown(int keyCode) {
				String key = Input.Keys.toString(keyCode);

				if(key.equals("Delete")) {
					Keyboard.backspace = true;
				} else if(key.equals("L-Shift") || key.equals("R-Shift")) {
					Keyboard.shift = true;
				} else if(Keyboard.inputCharacterList.contains(key)) {
					inputBar.inputKey(key, Keyboard.shift);
				} else if(key.equals("Up") || key.equals("Down")) {
					inputBar.scrollInput(key);
				} else if(key.equals("Enter")) {
					String userInput = inputBar.enterInput();
					if(!userInput.isEmpty()) {
						processInput(userInput);
					}
				} else if(key.equals("Escape")) {
					System.exit(0);
				}

				return true;
			}

			@Override
			public boolean keyUp(int keyCode) {
				String key = Input.Keys.toString(keyCode);

				if(key.equals("Delete")) {
					Keyboard.backspace = false;
					Keyboard.backspaceTimer = -1;
				} else if(key.equals("L-Shift") || key.equals("R-Shift")) {
					Keyboard.shift = false;
				}

				return true;
			}
		});
	}

	public void update() {
		handleInput();
		keyboard.update();
		inputBar.update();
	}

	@Override
	public void render() {
		update();

		ScreenUtils.clear(0, 0, 0, 1);
		inputBar.draw(frameBuffer, spriteBatch, font);
		console.draw(frameBuffer, spriteBatch, font);

		spriteBatch.begin();
		font.setColor(Color.WHITE);
		font.draw(spriteBatch, String.valueOf(Gdx.graphics.getFramesPerSecond()), 782, 600);
		spriteBatch.end();
	}
	
	@Override
	public void dispose() {
		font.dispose();
		spriteBatch.dispose();
		frameBuffer.dispose();
	}

	public void processInput(String userInput) {
		console.write(new Line("Huh?", "3w1y"), true);
	}
}
