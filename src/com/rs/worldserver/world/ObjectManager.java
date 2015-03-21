package com.rs.worldserver.world;

//Shard Revolutions Generic MMORPG Server
//Copyright (C) 2008  Graham Edgecombe

//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.

//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.object.GameObject;
import com.rs.worldserver.model.object.ObjectDefinition;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.Config;
import com.rs.worldserver.Server;

/**
 * Manages objects
 * 
 * @author Graham
 * 
 */
public class ObjectManager {

	public List<GameObject> objects;
	public List<Integer> xcoords = new ArrayList<Integer>();
	public List<Integer> ycoords = new ArrayList<Integer>();

	private Map<Integer, ObjectDefinition> definitions;

	/**
	 * Initialises the object manager.
	 * 
	 * @throws IOException
	 */
	public ObjectManager() throws IOException {
		objects = new ArrayList<GameObject>();
		definitions = new HashMap<Integer, ObjectDefinition>();
		loadDefinitions("config/objects/definitions.cfg");
		loadSpawns("config/objects/spawns.cfg");
	}

	/**
	 * Loads spawns from a file.
	 * 
	 * @param name
	 * @throws IOException
	 */
	private void loadSpawns(String name) throws IOException {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(name));
			while (true) {
				String line = file.readLine();
				if (line == null)
					break;
				int spot = line.indexOf('=');
				if (spot > -1) {
					String values = line.substring(spot + 1);
					values = values.replace("\t\t", "\t");
					values = values.replace("\t\t", "\t");
					values = values.replace("\t\t", "\t");
					values = values.trim();
					String[] valuesArray = values.split("\t");
					int id = Integer.valueOf(valuesArray[0]);
					ObjectDefinition definition = definitions.get(id);
					int absX = Integer.valueOf(valuesArray[1]);
					int absY = Integer.valueOf(valuesArray[2]);
					xcoords.add(absX);
					ycoords.add(absY);
					int heightLevel = Integer.valueOf(valuesArray[3]);
					String face = valuesArray[4];
					GameObject.Face faceEnum = GameObject.Face.NORTH;
					if (face.equals("FACE_EAST"))
						faceEnum = GameObject.Face.EAST;
					if (face.equals("FACE_SOUTH"))
						faceEnum = GameObject.Face.SOUTH;
					if (face.equals("FACE_WEST"))
						faceEnum = GameObject.Face.WEST;
					int type = Integer.valueOf(valuesArray[5]);
					GameObject g = new GameObject(absX, absY, heightLevel,
							faceEnum, type, definition);
			
					System.out.println(definition.getName());
					
					objects.add(g);
				
				}
			}
			System.out.println("Loaded " + objects.size() + " object spawns.");
		} finally {
			if (file != null)
				file.close();
		}
	}

	/**
	 * Loads definitions from a file
	 * 
	 * @param name
	 * @throws IOException
	 */
	private void loadDefinitions(String name) throws IOException {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(name));
			while (true) {
				String line = file.readLine();
				if (line == null)
					break;
				int spot = line.indexOf('=');
				if (spot > -1) {
					String values = line.substring(spot + 1);
					values = values.replace("\t\t", "\t");
					values = values.replace("\t\t", "\t");
					values = values.replace("\t\t", "\t");
					values = values.trim();
					String[] valuesArray = values.split("\t");
					int type = Integer.valueOf(valuesArray[0]);
					String objectName = valuesArray[1];
					String size = valuesArray[2];
					int split = size.indexOf('x');
					int xsize = Integer.valueOf(size.substring(0, split));
					int ysize = Integer.valueOf(size.substring(split + 1));
					String description = valuesArray[3].substring(1,
							valuesArray[3].length() - 1);
					ObjectDefinition def = new ObjectDefinition(type,
							objectName, description, xsize, ysize);
					definitions.put(def.getType(), def);
				}
			}
			System.out.println("Loaded " + definitions.size()
					+ " object definitions.");
		} finally {
			if (file != null)
				file.close();
		}
	}

	/**
	 * Processes objects.
	 */
	public void process() {

	}

	/**
	 * Updates a client's objects.
	 * 
	 * @param client
	 */
	public void update(Client p) {
		for (GameObject g : objects) {
			if (g.getHeightLevel() != p.getHeightLevel())
				continue;
			int tmpX = g.getAbsX() - p.getAbsX();
			int tmpY = g.getAbsY() - p.getAbsY();
			if (tmpX >= -96 && tmpX <= 96 && tmpY >= -96 && tmpY <= 96) {
				p.getActionAssistant().sendReplaceObject(g.getAbsX(),
						g.getAbsY(), g.getDefinition().getType(),
						g.getFaceID(), g.getType());
			}
		}
	}

	public GameObject getObjectAt(int x, int y, int z) {
		for (GameObject g : objects) {
			if (g.getAbsX() == x && g.getAbsY() == y && g.getHeightLevel() == z) {
				return g;
			}
		}
		return null;
	}
	
	public boolean stumpAt(int x, int y) {
		for (GameObject g : objects) {
			if (g.getAbsX() == x && g.getAbsY() == y) {
				if (g.getDefinition()==Server.getObjectManager().getDefinition(1341))
					return true;
			}
		}
		return false;
	}
	public void removeObject(GameObject object) {
		objects.remove(object);
		//showObject(object);
	}
	public void addObject(GameObject object) {
		objects.add(object);
		showObject(object);
	}

	public void replaceObject(GameObject oldObject, GameObject newObject) {
		objects.remove(oldObject);
		objects.add(newObject);
		showObject(newObject);
	}

	public void replaceTemporaryObject(GameObject oldObject,
			GameObject newObject) {
		objects.remove(oldObject);
		showObject(newObject);
	}

	
	

	public void replaceObjectWebs(GameObject oldObject, GameObject newObject, int respawnTime) {
		if(oldObject == null)
		System.out.println("old is null");
		if(newObject == null)
		System.out.println("new is null");
		addObject(newObject);
		
		final GameObject nObj = newObject;
		final GameObject oObj = oldObject;
		
		EventManager.getSingleton().addEvent(null,"replace", new Event() {

			@Override
			public void execute(EventContainer container) {
				removeObject(nObj);
				addObject(oObj);
				stop();
			}

			@Override
			public void stop() {
				removeObject(oObj);
			}

		}, respawnTime * 1000);
	}
	
	public void showObject(GameObject object) {
		for (Player p : PlayerManager.getSingleton().getPlayers()) {
			if (p == null)
				continue;
			if (!p.isActive)
				continue;
			if (p.disconnected)
				continue;
			Client player = (Client) p;
			if (object.getHeightLevel() != player.getHeightLevel())
				continue;
			int tmpX = object.getAbsX() - player.getAbsX();
			int tmpY = object.getAbsY() - player.getAbsY();
			if (tmpX >= -96 && tmpX <= 96 && tmpY >= -96 && tmpX <= 96) {

				player.getActionAssistant().sendReplaceObject(object.getAbsX(),object.getAbsY(), object.getDefinition().getType(),
						object.getFaceID(), object.getType());
			}
		}
	}

	public ObjectDefinition getDefinition(int id) {
		return definitions.get(id);
	}

}
