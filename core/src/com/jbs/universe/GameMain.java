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
import com.jbs.universe.gamedata.world.*;
import com.jbs.universe.gamedata.world.Room.*;
import com.jbs.universe.screen.InputBar;
import com.jbs.universe.screen.MiniMap;
import com.jbs.universe.screen.console.*;
import com.jbs.universe.screen.roomscreen.RoomScreen;

import java.util.ArrayList;
import java.util.Arrays;

import static com.jbs.universe.components.Utility.createMob;

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

		player = new Player(0, 0, 3, 0, 0, -1);
		galaxyList = new ArrayList<Galaxy>();

		frameTimer = 0;

		loadGame();
	}

	public void loadGame() {
		Galaxy galaxyProtoMilkyWay = new Galaxy(new ColorString("Proto Milky Way", "15w"));
		galaxyList.add(galaxyProtoMilkyWay);

		// (System) Proto Sol //
		if(true) {
			SolarSystem systemProtoSol = new SolarSystem(new ColorString("Proto Sol", "9w"));
			galaxyProtoMilkyWay.systemList.add(systemProtoSol);

			Planet starProtoSol = new Planet(0, 0, 0, new ColorString("Proto Sol", "9w"), "Star", 0, 38880, 0, 865370, 7.25f);
			systemProtoSol.planetList.add(starProtoSol);

			Area areaLimbo = new Area(0, new ColorString("Limbo", "5w"));
			starProtoSol.areaList.add(areaLimbo);

			Room roomLimbo = new Room(0, 0, 0, 0, 0, new ColorString("Limbo", "5w"));
			areaLimbo.roomList.add(roomLimbo);

			Planet planetProtoMercury = new Planet(0, 0, 1, new ColorString("Proto Mercury", "13w"), "Planet", 29945, 84960, 126720, 3031, 0);
			systemProtoSol.planetList.add(planetProtoMercury);
			Planet planetProtoVenus = new Planet(0, 0, 2, new ColorString("Proto Venus", ""), "Planet", 67443, 349920, 324000, 7520, 2.63f);
			planetProtoVenus.orbit = "Clockwise";
			systemProtoSol.planetList.add(planetProtoVenus);

			Planet planetProtoEarth = new Planet(0, 0, 3, new ColorString("Proto Earth", ""), "Planet", 93456, 1440, 525600, 7917, 23.43f);
			systemProtoSol.planetList.add(planetProtoEarth);

			planetProtoEarth.currentMinutesInDay = 350;
			planetProtoEarth.currentMinutesInYear = 350;
//			planetProtoEarth.updateNightDayTimers();
//			planetProtoEarth.updatePosition();

			Planet planetProtoMars = new Planet(0, 0, 4, new ColorString("Proto Mars", "10w"), "Planet", 148120, 1477, 989280, 4212, 25.19f);
			systemProtoSol.planetList.add(planetProtoMars);
			Planet planetProtoJupiter = new Planet(0, 0, 5, new ColorString("Proto Jupiter", "13w"), "Planet", 484000000, 596, 6239520, 86881, 3.13f);
			systemProtoSol.planetList.add(planetProtoJupiter);
			Planet planetProtoSaturn = new Planet(0, 0, 6, new ColorString("Proto Saturn", "12w"), "Planet", 886000000, 634, 15448640, 72367, 26.73f);
			systemProtoSol.planetList.add(planetProtoSaturn);
			Planet planetProtoUranus = new Planet(0, 0, 7, new ColorString("Proto Uranus", "12w"), "Planet", 1824000000, 1034, 44189280, 31518, 97.77f);
			systemProtoSol.planetList.add(planetProtoUranus);
			Planet planetProtoNeptune = new Planet(0, 0, 8, new ColorString("Proto Neptune", "13w"), "Planet", 2779300000L, 966, 86673600, 30599, 28.32f);
			systemProtoSol.planetList.add(planetProtoNeptune);
			Planet planetProtoPluto = new Planet(0, 0, 9, new ColorString("Proto Pluto", "11w"), "Planet", 3700000000L, 9180, 130348800, 1476, 119.61f);
			systemProtoSol.planetList.add(planetProtoPluto);
		}

		// (Area) Center Of The Universe //
		if(true) {
			Planet planetProtoEarth = galaxyProtoMilkyWay.systemList.get(0).planetList.get(3);
			int areaCOTUNum = planetProtoEarth.areaList.size();

			Area areaCOTU = new Area(areaCOTUNum, new ColorString("Center of the Universe", "22w"));
			planetProtoEarth.areaList.add(areaCOTU);

			Room roomCOTU000 = new Room(0, 0, 3, areaCOTUNum, 0, new ColorString("Center of the Universe", "1w1ddw1da2dw2da1dw2ddw1da1dw1w2w2dw2ddw1da2ddw"));
			areaCOTU.roomList.add(roomCOTU000);
			roomCOTU000.description.label = "You stand on a large floating platform at the Center of the Universe. Billions of multi-colored stars twinkle and flash from ages past. You see a bridge leading to the Spaceport to the South and a garden to the North.";
			roomCOTU000.description.colorCode = "46w1w1ddw1da2dw2da1dw2ddw1da1dw2w1w2dw2ddw1da2ddw2y13w1dc1c1ddc1w1y1r1dr1do1o1y1dy1ddy7w1c1dc1ddc2dc1c1ddc5w1c1dc1ddc1dc1c1w14w2y32w1w1dw2ddw1dw1w1dw1ddw1dw1w7w1w5ddw6w1g1dg1ddg1g1dg1ddg8w1w4ddw1y";
			roomCOTU000.exit.put("South", new Exit(0, 0, 3, areaCOTUNum, 1, -1));
			roomCOTU000.exit.put("North", new Exit(0, 0, 3, areaCOTUNum, 3, -1));
			//roomCOTU000.exit.put("East", new int[]{0, 0, 3, areaIceCavernNum, 0}); // Ice Cavern

			Room roomCOTU001 = new Room(0, 0, 3, areaCOTUNum, 1, new ColorString("Bridge To The Spaceport", "14w1w1dw2ddw1dw1w1dw1ddw1dw"));
			areaCOTU.roomList.add(roomCOTU001);
			roomCOTU001.exit.put("North", new Exit(0, 0, 3, areaCOTUNum, 0, -1));
			roomCOTU001.exit.put("South", new Exit(0, 0, 3, areaCOTUNum, 2, -1));
			createMob(1, roomCOTU001, null);

			Room roomCOTU002 = new Room(0, 0, 3, areaCOTUNum, 2, new ColorString("Spaceport Entrance", "1w1dw2ddw1dw1w1dw1ddw1dw9w"));
			areaCOTU.roomList.add(roomCOTU002);
			roomCOTU002.exit.put("North", new Exit(0, 0, 3, areaCOTUNum, 1, -1));
			roomCOTU002.exit.put("South", new Exit(0, 0, 3, areaCOTUNum, 5, -1));
			roomCOTU002.exit.put("East", new Exit(0, 0, 3, areaCOTUNum, 6, -1));

			Room roomCOTU003 = new Room(0, 0, 3, areaCOTUNum, 3, new ColorString("A Peaceful Garden", "2w1w1dw2ddw1dw1w1ddw1da1w1g1dg1ddg1g1dg1ddg"));
			areaCOTU.roomList.add(roomCOTU003);
			roomCOTU003.exit.put("South", new Exit(0, 0, 3, areaCOTUNum, 0, -1));
			roomCOTU003.exit.put("West", new Exit(0, 0, 3, areaCOTUNum, 4, -1));

			Room roomCOTU004 = new Room(0, 0, 3, areaCOTUNum, 4, new ColorString("A Little Wooden Shack", "9w1do1ddo1dddo1do1ddo1dddo6w"));
			areaCOTU.roomList.add(roomCOTU004);
			roomCOTU004.exit.put("East", new Exit(0, 0, 3, areaCOTUNum, 3, -1));

			Room roomCOTU005 = new Room(0, 0, 3, areaCOTUNum, 5, new ColorString("COTU Landing Pad", "16w"));
			areaCOTU.roomList.add(roomCOTU005);
			roomCOTU005.exit.put("North", new Exit(0, 0, 3, areaCOTUNum, 2, -1));

			Room roomCOTU006 = new Room(0, 0, 3, areaCOTUNum, 6, new ColorString("COTU Training Center Hall", "25w"));
			areaCOTU.roomList.add(roomCOTU006);
			roomCOTU006.exit.put("West", new Exit(0, 0, 3, areaCOTUNum, 2, -1));
			roomCOTU006.exit.put("Up", new Exit(0, 0, 3, areaCOTUNum, 7, -1));

			Room roomCOTU007 = new Room(0, 0, 3, areaCOTUNum, 7, new ColorString("COTU Training Center Floor 1", "28w"));
			areaCOTU.roomList.add(roomCOTU007);
			roomCOTU007.exit.put("Down", new Exit(0, 0, 3, areaCOTUNum, 6, -1));
			roomCOTU007.exit.put("North", new Exit(0, 0, 3, areaCOTUNum, 8, -1));

			Room roomCOTU008 = new Room(0, 0, 3, areaCOTUNum, 8, new ColorString("Unarmed Mobs Room", "17w"));
			areaCOTU.roomList.add(roomCOTU008);
			roomCOTU008.exit.put("South", new Exit(0, 0, 3, areaCOTUNum, 7, -1));
		}
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
		Room currentRoom = Room.exists(galaxyList, player.spaceship, player.galaxy, player.system, player.planet, player.area, player.room);
		if(currentRoom == null) {
			currentRoom = galaxyList.get(0).systemList.get(0).planetList.get(0).areaList.get(0).roomList.get(0);
		}

		// Basic Commands //
		// Look //
		if(userInputList.length == 1 && Arrays.asList("look", "loo", "lo", "l").contains(userInputList[0])) {
			currentRoom.display(console, galaxyList, player);
		}

		// Movement //
		else if(userInputList.length == 1 && Arrays.asList(directionStringList).contains(userInputList[0])) {

			// Move Direction '#' //

			// Move Direction //
			player.moveCheck(console, miniMap, galaxyList, player, currentRoom, userInputList[0]);
		}

		else {
			console.write(new ColorString("Huh?", "3w1y"), true);
		}
	}
}
