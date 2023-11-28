package com.jbs.universe;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jbs.universe.components.Keyboard;
import com.jbs.universe.gamedata.player.Player;
import com.jbs.universe.gamedata.world.Area.Area;
import com.jbs.universe.gamedata.world.Galaxy.Galaxy;
import com.jbs.universe.gamedata.world.Location;
import com.jbs.universe.gamedata.world.Planet.Planet;
import com.jbs.universe.gamedata.world.Room.Room;
import com.jbs.universe.gamedata.world.SolarSystem.SolarSystem;
import com.jbs.universe.gamedata.world.Star.Star;
import com.jbs.universe.screen.inputbar.InputBar;
import com.jbs.universe.screen.minimap.MiniMap;
import com.jbs.universe.screen.console.*;
import com.jbs.universe.screen.roomscreen.RoomScreen;

import java.util.*;

public class GameMain extends ApplicationAdapter {
	FrameBuffer frameBuffer;
	SpriteBatch spriteBatch;
	BitmapFont font;

	Keyboard keyboard;
	RoomScreen roomScreen;
	MiniMap miniMap;
	public Console console;
	InputBar inputBar;

	int frameTimer;

	ArrayList<Galaxy> galaxyList;
	Player player;

	public void loadGame() {
		ColorString galaxyMilkyWayName = new ColorString("Milky Way", "6shim-w3shim-w");
		Galaxy galaxyMilkyWay = new Galaxy(galaxyMilkyWayName);
		galaxyList.add(galaxyMilkyWay);
		
		// Center Of The Universe //
		ColorString systemCotuName = new ColorString("Cotu System", "5shim-w6shim-w");
		ColorString starKonstantineName = new ColorString("Konstantine", "11shim-w");
		Star starKonstantine = new Star(starKonstantineName);
		SolarSystem systemCotu = new SolarSystem(systemCotuName, starKonstantine);
		galaxyMilkyWay.systemList.add(systemCotu);

		ColorString planetCotuName = new ColorString("Cotu", "4shim-w");
		Planet planetCotu = new Planet(planetCotuName);
		systemCotu.planetList.add(planetCotu);

		ColorString areaLimboName = new ColorString("Limbo", "5shim-w");
		Area areaLimbo = new Area(areaLimboName);
		planetCotu.areaList.add(areaLimbo);

		ColorString roomLimboName = new ColorString("Limbo", "5shim-w");
		Room roomLimbo = new Room(roomLimboName, null);
		areaLimbo.roomList.add(roomLimbo);
		
		// Sol System //
		ColorString systemSolName = new ColorString("Sol System", "4shim-w6shim-w");
		ColorString starSolName = new ColorString("Sol", "3shim-w");
		Star starSol = new Star(starSolName);
		SolarSystem systemSol = new SolarSystem(systemSolName, starSol);
		galaxyMilkyWay.systemList.add(systemSol);

		ColorString planetEarthName = new ColorString("Earth", "5shim-w");
		Planet planetEarth = new Planet(planetEarthName);
		systemSol.planetList.add(planetEarth);
	}

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

		frameTimer = 0;

		galaxyList = new ArrayList<>();
		player = new Player(new Location(0, 0, 0, 0, 0));

		loadGame();
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
					inputBar.handleInput(key);
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
		roomScreen.draw(spriteBatch);
		miniMap.draw(spriteBatch, font);
		console.draw(spriteBatch, font);
		inputBar.draw(spriteBatch, font, galaxyList, player);

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
		String[] directionStringList = new String[] {"north", "nort", "nor", "no", "n", "east", "eas", "ea", "e", "south", "sout", "sou", "so", "s", "west", "wes", "we", "w", "up", "u", "down", "dow", "do", "d"};
		String[] userInputList = userInput.toLowerCase().replaceAll(" +", " ").split(" ");
		Room playerRoom = Room.getRoom(galaxyList, player.location);

		if(Arrays.asList("look", "loo", "lo", "l").contains(userInput.toLowerCase())) {
			playerRoom.display(console, galaxyList);
		}

		else {
			console.write(new ColorString("Huh?", "1w2ddw1y"), true);
		}
	}
}
