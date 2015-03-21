package com.rs.worldserver.world;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.npc.NPCAction;
import com.rs.worldserver.model.npc.NPCDefinition;
import com.rs.worldserver.model.npc.NPCDrop;
import com.rs.worldserver.Config;
import com.rs.worldserver.Server;
import java.util.concurrent.*;

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

/**
 * Manages NPCs
 * 
 * @author Graham
 * 
 */
public class NPCManager {

	public static final int MAXIMUM_NPCS = 3000;

	private static final int DEFAULT_RESPAWN_TIME = 16;

	public ConcurrentMap<Integer, NPC> npcMap;
	private Map<Integer, NPCDefinition> npcDefinitions;
	private Map<Integer, NPCAction> npcFirstClickActions;
	private Map<Integer, NPCAction> npcSecondClickActions;
	private Map<Integer, NPCAction> npcThirdClickActions;
	public Map<Integer, NPC> toBeRemoved;
	public int spawnNPC(int id, int x, int y, int z, int walktype) {
		int slot = freeSlot();
		NPCDefinition def = npcDefinitions.get(id);
		if (def == null) {
			//return -1;
			def = npcDefinitions.get(477);
			}
			def.type = id;
			System.out.println(def.getType());
		NPC npc = new NPC(slot, def, x, y, z, walktype);
		npc.setX1(0);
		npc.setY1(0);
		npc.setX2(0);
		npc.setY2(0);
		if (walktype == 1 || walktype == 2) {
			npc.setWalking(true);
		}
		npcMap.put(npc.getNpcId(), npc);
		return slot;
		
	}

	public void deleteNPC(int id) {
		//System.out.println("ID: " +id);
		NPC npc = npcMap.get(id);
		npc.setHidden(true);
		npcMap.remove(id);
		slotSearchStart = id;
	}
	/**
	 * Creates the NPC manager.
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public NPCManager() throws NumberFormatException, Exception {
		npcMap = new ConcurrentHashMap<Integer, NPC>();
		toBeRemoved = new HashMap<Integer, NPC>();
		npcDefinitions = new HashMap<Integer, NPCDefinition>();
		npcFirstClickActions = new HashMap<Integer, NPCAction>();
		npcSecondClickActions = new HashMap<Integer, NPCAction>();
		npcThirdClickActions = new HashMap<Integer, NPCAction>();
		loadDefinitions("config/npcs/definitions.cfg");
		loadSpawns("config/npcs/spawns.cfg");
		loadActions("config/npcs/actions.cfg");
		loadDrops("config/npcs/drops.cfg");
	}

	private void loadDrops(String name) throws IOException {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(name));
			int dropDefs = 0;
			int dropItems = 0;
			while (true) {
				String line = file.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				int spot = line.indexOf('=');
				if (spot > -1) {
					String values = line.substring(spot + 1);
					values = values.replace("\t\t", "\t");
					values = values.replace("\t\t", "\t");
					values = values.trim();
					String[] valuesArray = values.split("\t");
					int npc = Integer.valueOf(valuesArray[0]);
					int ptr = 1;
					while (true) {
						if (valuesArray.length <= ptr)
							break;
						int percent = Integer.valueOf(valuesArray[ptr++]);
						int id = Integer.valueOf(valuesArray[ptr++]);
						int amount = Integer.valueOf(valuesArray[ptr++]);
						NPCDrop drop = new NPCDrop(id, amount, percent);
						npcDefinitions.get(npc).addDrop(drop);
						dropItems++;
					}
					dropDefs++;
				}
			}
			System.out.println("Loaded " + dropDefs
					+ " npc drop definitions (total " + dropItems + " items).");
		} finally {
			if (file != null)
				file.close();
		}
	}

	public NPCAction getFirstClickNPCAction(int type) {
		return npcFirstClickActions.get(type);
	}

	public NPCAction getSecondClickNPCAction(int type) {
		return npcSecondClickActions.get(type);
	}

	public NPCAction getThirdClickNPCAction(int type) {
		return npcThirdClickActions.get(type);
	}

	/**
	 * Loads actions from a file
	 * 
	 * @param name
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	private void loadActions(String name) throws NumberFormatException,
			Exception {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(name));
			while (true) {
				String line = file.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				int spot = line.indexOf('=');
				if (spot > -1) {
					String values = line.substring(spot + 1);
					values = values.replace("\t\t", "\t");
					values = values.replace("\t\t", "\t");
					values = values.trim();
					String[] valuesArray = values.split("\t");
					int id = Integer.valueOf(valuesArray[0]);
					int clickId = Integer.valueOf(valuesArray[1]);
					String action = valuesArray[2];
					NPCAction.Type type;
					if (action.equals("TALK")) {
						type = NPCAction.Type.TALK;
					} else if (action.equals("OPEN_BANK")) {
						type = NPCAction.Type.OPEN_BANK;
					} else if (action.equals("OPEN_SHOP")) {
						type = NPCAction.Type.OPEN_SHOP;
					} else {
						continue;
					}
					NPCAction a = new NPCAction(type);
					if (type == NPCAction.Type.TALK) {
						a.setTalkDialogueId(Integer.valueOf(valuesArray[3]));
					} else if (type == NPCAction.Type.OPEN_SHOP) {
						a.setShopId(Integer.valueOf(valuesArray[3]));
					}
					if (clickId == 1) {
						npcFirstClickActions.put(id, a);
					} else if (clickId == 2) {
						npcSecondClickActions.put(id, a);
					} else if (clickId == 3) {
						npcThirdClickActions.put(id, a);
					} else {
						continue;
					}
				}
			}
			System.out.println("Loaded " + npcFirstClickActions.size()
					+ " npc first click actions.");
			System.out.println("Loaded " + npcSecondClickActions.size()
					+ " npc second click actions.");
			System.out.println("Loaded " + npcThirdClickActions.size()
					+ " npc third click actions.");
		} finally {
			if (file != null)
				file.close();
		}
	}

	/**
	 * Loads spawns from a file
	 * 
	 * @param name
	 * @throws IOException
	 */
	public int walkingType;
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
					values = values.trim();
					String[] valuesArray = values.split("\t");
					int id = Integer.valueOf(valuesArray[0]);
					int slot = freeSlot();
					NPCDefinition def = npcDefinitions.get(id);
					if (def == null)
						continue;
					NPC npc = new NPC(slot, def, Integer
							.valueOf(valuesArray[1]), Integer
							.valueOf(valuesArray[2]), Integer
							.valueOf(valuesArray[3]), Integer
							.valueOf(valuesArray[4]));
					
					if (walkingType == 1 || walkingType == 2) {
						npc.randomWalk = true;
					}
					npc.maxHit(Integer.valueOf(valuesArray[5]));
					npc.attack(Integer.valueOf(valuesArray[6]));
					npc.defence(Integer.valueOf(valuesArray[7]));
					npcMap.put(npc.getNpcId(), npc);
				}
			}
			System.out.println("Loaded " + npcMap.size() + " npc spawns.");
		} finally {
			if (file != null)
				file.close();
		}
	}

	/**
	 * Search for a free slot.
	 */
	private int slotSearchStart = 1;

	/**
	 * Gets a free slot.
	 * 
	 * @return
	 */
	private int freeSlot() {
		int slot = slotSearchStart;
		while (true) {
			if (npcMap.get(slot) == null) {
				slotSearchStart = slot + 1;
				Server.npcSlot = slot;
				return slot;
			}
			else {
				slot++;
			}
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
					values = values.trim();
					String[] valuesArray = values.split("\t");
					int time = DEFAULT_RESPAWN_TIME;
					if (valuesArray.length > 4) {
						time = Integer.valueOf(valuesArray[4]);
					}
					NPCDefinition def = new NPCDefinition(Integer
							.valueOf(valuesArray[0]), Integer
							.valueOf(valuesArray[2]), Integer
							.valueOf(valuesArray[3]), valuesArray[1]
							.replaceAll("_", " "), time);
					npcDefinitions.put(def.getType(), def);
				}
			}
			System.out.println("Loaded " + npcDefinitions.size()
					+ " npc definitions.");
			//[00:00:02]: Loaded 2888 npc definitions.
			//10000
			//System.out.println(npcDefinitions.get(14696).getHealth());
		} finally {
			if (file != null)
				file.close();
		}
	}

	/**
	 * Gets the NPC with the specified id.
	 * 
	 * @param id
	 * @return
	 */
	public NPC getNPC(int id) {
		return npcMap.get(id);
	}

	public NPC newNpc(int id,int x,int y,int heightLevel,int maxHit,int attack,int defence,int walkingType){
		int slot = freeSlot();
		NPCDefinition def = npcDefinitions.get(id);
		if(def == null) {
			return null;
		}
		NPC npc = new NPC(slot, def, x, y, heightLevel, walkingType);
		npc.npcId = npc.getNpcId();
		npc.definition = def;
		npc.maxHP = def.getHealth();
		npc.hp = def.getHealth();
		npc.setAbsX(x);
		npc.setAbsY(y);
		npc.makeX = x;
		npc.makeY = y;
		npc.spawnAbsX = x;
		npc.spawnAbsY = y;
		npc.setHeightLevel(heightLevel);
		npc.maxHit = maxHit;
		npc.attack = attack;
		npc.defence = defence;
		npc.walkingType = walkingType;
		npc.randomWalk = true;		
		npcMap.put(npc.getNpcId(), npc);
		return npc;
	}	
	public void process() {
		/*for (Map.Entry<Integer, NPC> entry : npcMap.entrySet()) {
			NPC n = entry.getValue();
			n.process();
		}*/
		try {
			toBeRemoved = new HashMap<Integer, NPC>();
			for (Map.Entry<Integer, NPC> entry : npcMap.entrySet()) {
				NPC n = entry.getValue();
				if(n == null)
					continue;
				if(n.isToBeRemoved)
					toBeRemoved.put(n.getNpcId(),n);
				else
					n.process();
			}
			/*for (Map.Entry<Integer, NPC> entry : npcAdd.entrySet()) {
				NPC n = entry.getValue();
				npcMap.put(n.getNpcId(), n);
			}
			npcAdd.clear();*/
			for (Map.Entry<Integer, NPC> entry : toBeRemoved.entrySet()) {
				NPC n = entry.getValue();
				npcMap.remove(n.getNpcId());
			}
			toBeRemoved.clear();
			toBeRemoved = null;
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Get NPC definition.
	 * 
	 * @param npc
	 * @return
	 */
	public NPCDefinition getNPCDefinition(int npc) {
		return npcDefinitions.get(npc);
	}

	/**
	 * Gets the NPC map
	 * 
	 * @return
	 */
	public Map<Integer, NPC> getNPCMap() {
		return this.npcMap;
	}

}
