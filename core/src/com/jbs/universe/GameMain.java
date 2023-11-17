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
import com.jbs.universe.gamedata.Config;
import com.jbs.universe.gamedata.player.Player;
import com.jbs.universe.gamedata.world.Galaxy;
import com.jbs.universe.gamedata.world.Room;
import com.jbs.universe.screen.InputBar;
import com.jbs.universe.screen.MiniMap;
import com.jbs.universe.screen.console.ColorString;
import com.jbs.universe.screen.console.Console;
import com.jbs.universe.screen.roomscreen.RoomScreen;

import java.util.ArrayList;

public class GameMain extends ApplicationAdapter {
	FrameBuffer frameBuffer;
	SpriteBatch spriteBatch;
	BitmapFont font;

	Keyboard keyboard;
	RoomScreen roomScreen;
	MiniMap miniMap;
	public Console console;
	InputBar inputBar;
	Config config;

	int frameTimer;

	Player player;
	ArrayList<Galaxy> galaxyList;
	
	@Override
	public void create() {
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 800, 600, false);
		spriteBatch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("Fonts/Code_New_Roman_18.fnt"), Gdx.files.internal("Fonts/Code_New_Roman_18.png"), false);

		keyboard = new Keyboard();
		roomScreen = new RoomScreen();
		miniMap = new MiniMap();
		console = new Console();
		inputBar = new InputBar();
		config = new Config();

		player = new Player(0, 0, 3, 1, 1, -1, -1);
		galaxyList = new ArrayList<Galaxy>();

		frameTimer = 0;

		loadGame();
	}

	public void loadGame() {
		Galaxy galaxyProtoMilkyWay = new Galaxy();
	}

	public void handleInput() {
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
				} else if(key.equals("Up") || key.equals("Down") || key.equals("[") || key.equals("]")) {
					inputBar.handleInput(miniMap, key);
				} else if(key.equals("Enter")) {
					String userInput = inputBar.enterInput();
					if(!userInput.isEmpty()) {
						try {
							processInput(userInput);
						} catch(Exception e) {
							// Write Error Report
							e.printStackTrace();
							System.exit(0);
						}
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

			@Override
			public boolean scrolled(float amountX, float amountY) {
				System.out.println(amountY);
				return true;
			}
		});
	}

	public void update() {
		handleInput();
		keyboard.update();
		inputBar.update(this);

		frameTimer += 1;
		if(frameTimer >= 60) {
			frameTimer = 0;
		}
	}

	@Override
	public void render() {
		update();

		ScreenUtils.clear(0, 0, 0, 1);
		roomScreen.draw(frameBuffer, spriteBatch);
		miniMap.draw(frameBuffer, spriteBatch, font);
		console.draw(frameBuffer, spriteBatch, font);
		inputBar.draw(frameBuffer, spriteBatch, font, galaxyList, player);

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
		String[] userInputList = userInput.replaceAll(" +", " ").split(" ");
//		Room currentRoom = Room.exists(galaxyList, player.spaceship, player.galaxy, player.system, player.planet, player.area, player.num);
//		if(currentRoom == null) {
//			currentRoom = galaxyList.get(0).systemList.get(0).planetList.get(0).areaList.get(0).roomList.get(0);
//		}

		// Basic Commands //
		// Look //
//		if(userInputList.length == 1 && (userInputList[0].equals("look") || userInputList[0].equals("loo") || userInputList[0].equals("lo") || userInputList[0].equals("l"))) {
//			currentRoom.display(console, galaxyList, player);
//		}

		console.write(new ColorString("Huh?", "3w1y"), true);
	}
}
