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
import com.jbs.universe.gamedata.item.type.Armor;
import com.jbs.universe.gamedata.mob.Mob;
import com.jbs.universe.gamedata.player.Player;
import com.jbs.universe.gamedata.world.Location;
import com.jbs.universe.gamedata.world.area.Area;
import com.jbs.universe.gamedata.world.galaxy.Galaxy;
import com.jbs.universe.gamedata.world.planet.Planet;
import com.jbs.universe.gamedata.world.room.Room;
import com.jbs.universe.gamedata.world.solarsystem.SolarSystem;
import com.jbs.universe.gamedata.world.spaceship.Spaceship;
import com.jbs.universe.gamedata.world.star.Star;
import com.jbs.universe.screen.console.ColorString;
import com.jbs.universe.screen.console.Console;
import com.jbs.universe.screen.inputbar.InputBar;
import com.jbs.universe.screen.minimap.MiniMap;
import com.jbs.universe.screen.roomscreen.RoomScreen;

import java.util.ArrayList;
import java.util.Arrays;

import static com.jbs.universe.components.Utility.combineStringArray;
import static com.jbs.universe.components.Utility.getDirectionString;

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

		// Load Player //
		player = new Player(new Location(0, 0, 0, 1, 0));

		// Galaxy - Milky Way //
		ColorString galaxyMilkyWayName = new ColorString("Milky Way", "6shim-w3shim-w");
		Galaxy galaxyMilkyWay = new Galaxy(galaxyMilkyWayName);
		galaxyList.add(galaxyMilkyWay);
		
		// Solar System - Center Of The Universe //
		ColorString systemCotuName = new ColorString("Cotu System", "5shim-w6shim-w");
		ColorString starKonstantineName = new ColorString("Konstantine", "11shim-w");
		Star starKonstantine = new Star(starKonstantineName);
		SolarSystem systemCotu = new SolarSystem(systemCotuName, starKonstantine);
		galaxyMilkyWay.systemList.add(systemCotu);

		ColorString planetCotuName = new ColorString("Cotu", "4shim-w");
		Location planetCotuLocation = new Location(0, 0, 0);
		Planet planetCotu = new Planet(planetCotuName, planetCotuLocation,93456, 1440, 525600, 23.43f, 7917);
		systemCotu.planetList.add(planetCotu);

		planetCotu.minutesInDay = 425;
		planetCotu.minutesInYear = 425;
		planetCotu.updateNightDayTimers();
		planetCotu.updatePosition();

		ColorString areaLimboName = new ColorString("Limbo", "5shim-w");
		Area areaLimbo = new Area(areaLimboName);
		planetCotu.areaList.add(areaLimbo);

		ColorString roomLimboName = new ColorString("Limbo", "5shim-w");
		Location locationLimbo = new Location(0, 0, 0, 0, 0);
		Room roomLimbo = new Room(roomLimboName, null, locationLimbo);
		areaLimbo.roomList.add(roomLimbo);

		// Area - Center Of The Universe //
		ColorString areaCotuName = new ColorString("Center of the Universe", "7shim-w3shim-w4shim-w8shim-w");
		Area areaCotu = new Area(areaCotuName);
		planetCotu.areaList.add(areaCotu);

		ColorString roomCotu000Name = new ColorString("Center of the Universe", "7shim-w3shim-w4shim-w8shim-w");
		Location locationCotu000 = new Location(0, 0, 0, 1, 0);
		Room roomCotu000 = new Room(roomCotu000Name, null, locationCotu000);
		areaCotu.roomList.add(roomCotu000);

		roomCotu000.itemList.add(Armor.load(1));
		roomCotu000.itemList.add(Armor.load(2));

		roomCotu000.mobList.add(Mob.load(1));

		ColorString roomCotu001Name = new ColorString("A Peaceful Garden", "2w9shim-w6shim-g");
		Location locationCotu001 = new Location(0, 0, 0, 1, 1);
		Room roomCotu001 = new Room(roomCotu001Name, null, locationCotu001);
		areaCotu.roomList.add(roomCotu001);
		roomCotu001.makeExit("South", roomCotu000, true);

		ColorString roomCotu002Name = new ColorString("Launch Pad A", "7shim-w3shim-w2w");
		Location locationCotu002 = new Location(0, 0, 0, 1, 2);
		Room roomCotu002 = new Room(roomCotu002Name, null, locationCotu002);
		areaCotu.roomList.add(roomCotu002);
		roomCotu002.makeExit("North", roomCotu000, true);

		ColorString roomCotu003Name = new ColorString("Launch Pad B", "7shim-w3shim-w2w");
		Location locationCotu003 = new Location(0, 0, 0, 1, 3);
		Room roomCotu003 = new Room(roomCotu003Name, null, locationCotu003);
		areaCotu.roomList.add(roomCotu003);
		roomCotu003.makeExit("North", roomCotu002, true);

		// Transport Ship //
		ColorString spaceshipCotuShipName = new ColorString("Large Transport Ship", "6w10shim-w4shim-w");
		Spaceship spaceshipCotuShip = Spaceship.create(spaceshipCotuShipName, "");
		spaceshipCotuShip.park(galaxyList, roomCotu002);
	}

	@Override
	public void create() {
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 800, 600, false);
		spriteBatch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("Fonts/Code_New_Roman_18.fnt"), Gdx.files.internal("Fonts/Code_New_Roman_18.png"), false);
		font.setFixedWidthGlyphs("-");

		keyboard = new Keyboard();
		roomScreen = new RoomScreen();
		miniMap = new MiniMap();
		console = new Console();
		inputBar = new InputBar();

		frameTimer = 0;

		galaxyList = new ArrayList<>();
		player = null;

		loadGame();
	}

	public void handleInput() {
		Gdx.input.setInputProcessor(new InputAdapter() {

			@Override
			public boolean keyDown(int keyCode) {
				String key = Input.Keys.toString(keyCode);

				if(key.equals("Delete")) {
					Keyboard.backspace = true;
				} else if(Arrays.asList("L-Shift", "R-Shift").contains(key)) {
					Keyboard.shift = true;
				} else if(Arrays.asList("L-Ctrl", "R-Ctrl").contains(key)) {
					Keyboard.control = true;
				}

				else if(Keyboard.control && (Arrays.asList("Up", "Down", "Left", "Right").contains(key))) {
					player.move(console, galaxyList, Keyboard.getArrowDirectionString(key));
				}

				else if(Keyboard.inputCharacterList.contains(key)) {
					inputBar.inputKey(key, Keyboard.shift);
				} else if(Arrays.asList("Up", "Down").contains(key)) {
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
				}

				else if(key.equals("Escape")) {
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
				} else if(Arrays.asList("L-Shift", "R-Shift").contains(key)) {
					Keyboard.shift = false;
				} else if(Arrays.asList("L-Ctrl", "R-Ctrl").contains(key)) {
					Keyboard.control = false;
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

		if(frameTimer == 0) {
			SolarSystem.getSystem(galaxyList, player.location).update(console, player);
		}

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

		// Look //
		if(userInputList.length == 1 && Arrays.asList("look", "loo", "lo", "l").contains(userInput.toLowerCase())) {
			playerRoom.display(console, galaxyList, player);
		}

		// Movement //
		else if(userInputList.length == 1 && Arrays.asList(directionStringList).contains(userInput.toLowerCase())) {
			player.move(console, galaxyList, getDirectionString(userInput.toLowerCase()));
		}

		// Enter //
		else if(Arrays.asList("enter", "ente", "ent", "en").contains(userInputList[0])) {
			if(userInputList.length > 1) {
				String targetSpaceshipString = combineStringArray(Arrays.copyOfRange(userInputList, 1, userInputList.length));
				player.enterShip(console, galaxyList, targetSpaceshipString);
			}
			else {
				console.write(new ColorString("Enter what ship?", "15w1y"), true);
			}
		}

		else {
			console.write(new ColorString("Huh?", "1w2ddw1y"), true);
		}
	}
}
