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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import com.rs.worldserver.Server;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.FloorItem;
import com.rs.worldserver.model.ItemDefinition;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.Config;
/**
 * Rewritten item handler.
 * 
 * @author Graham
 * 
 */
public class ItemManager {

	private static final int HIDE_FOR = 90000;
	private static final int STAY_FOR = 120000;
	private static final int DROPS_RESPAWN_TIME = HIDE_FOR;

	public List<FloorItem> list;
	private Map<Integer, ItemDefinition> itemDefinitions;

	public void addItem(FloorItem item) {
		list.add(item);
	}
	/**
	 * Loads the items, drops, etc.
	 * 
	 * @throws IOException
	 */
	public ItemManager() throws IOException {
		list = new ArrayList<FloorItem>();
		itemDefinitions = new HashMap<Integer, ItemDefinition>();
		loadDefinitions("config/items/definitions.cfg");
		loadSpawns("config/items/spawns.cfg");
	}

	// TODO cache this!
	/**
	 * Gets the unnoted item for an item.
	 * 
	 * @param normalId
	 * @return
	 */
	public int getUnnotedItem(int normalId) {
		int newId = -1;
		String notedName = itemDefinitions.get(normalId).getName();
		for (Map.Entry<Integer, ItemDefinition> entry : itemDefinitions
				.entrySet()) {
			if (entry.getValue().getName().equals(notedName)) {
				if (!entry.getValue().getDescription().startsWith(
						"Swap this note at any bank for a")) {
					newId = entry.getKey();
				}
			}
		}
		return newId;
	}

	/**
	 * Loads definitions.
	 * 
	 * @param string
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
				if (spot > 0) {
					String values = line.substring(spot + 1);
					values = values.replace("\t\t", "\t");
					values = values.replace("\t\t", "\t");
					values = values.trim();
					String[] valuesArray = values.split("\t");
					int id = Integer.valueOf(valuesArray[0]);
					String iname = valuesArray[1].replaceAll("_", " ");
					String examine = valuesArray[2].replaceAll("_", " ");
					double shopValue = Double.valueOf(valuesArray[3]);
					double lowAlch = Double.valueOf(valuesArray[4]);
					double highAlch = Double.valueOf(valuesArray[5]);
					int[] bonuses = new int[12];
					int ptr = 6;
					try{ 
					for (int i = 0; i < bonuses.length; i++) {
						bonuses[i] = Integer.valueOf(valuesArray[ptr]);
						ptr++;
					}
					}catch(Exception e){
						System.out.println(line);
					}
					ItemDefinition def = new ItemDefinition(id, iname, examine,
							shopValue, lowAlch, highAlch, bonuses);
					itemDefinitions.put(id, def);
				}
			}
			System.out.println("Loaded " + itemDefinitions.size()
					+ " item definitions.");
		} finally {
			if (file != null)
				file.close();
		}
	}

	/**
	 * Gets an item definition.
	 * 
	 * @param id
	 * @return
	 */
	public ItemDefinition getItemDefinition(int id) {
		return itemDefinitions.get(id);
	}

	/**
	 * Loads drops.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public void loadSpawns(String name) throws IOException {
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
					int x = Integer.parseInt(valuesArray[1]);
					int y = Integer.parseInt(valuesArray[2]);
					int amount = Integer.parseInt(valuesArray[3]);
					int height = Integer.parseInt(valuesArray[4]);
					FloorItem i = new FloorItem(id, amount, null, x, y, height);
					i.setSpawn(true);
					list.add(i);
				}
			}
			System.out.println("Loaded " + list.size() + " item spawns.");
		} finally {
			if (file != null)
				file.close();
		}
	}

	/**				

	 * Periodically remove old items.
	 */
/*	public void process() {
		Queue<FloorItem> remove = new LinkedList<FloorItem>();
		for (FloorItem item : list) {
			if (item.isSpawn())
				continue;
			if (System.currentTimeMillis() > (item.getDroppedAt() + HIDE_FOR)) { //&& item.getDroppedBy() != null) {
				item.resetOwner();
				remove.add(item);
				hideDrop(item);
				if(item.getX() > 2941 && item.getX() < 3392 && item.getY() > 3518 && item.getY() < 3966) {
					remove.add(item);
				} else {
					showDrop(item);
				}
				
			} else if (System.currentTimeMillis() > (item.getDroppedAt() + STAY_FOR)) {
				remove.add(item);
				hideDrop(item);
			}
		}
		for (FloorItem r : remove) {
			list.remove(r);
		}
	}*/
	public void reloadItems(Client c) {	
		for (FloorItem item : list) {
			if(c != null){
				if (c.distanceToPoint(item.getX(), item.getY()) <= 60) {
					//if(System.currentTimeMillis() > (item.getDroppedAt() + HIDE_FOR) && item.getDroppedBy() != null) {
						hideDrop(item);
						showDrop(item);
					//}
				}
			}
		}
	}	
	Queue<FloorItem> remove = null;
	public void process() {
		remove = new LinkedList<FloorItem>();
		for (FloorItem item : list) {
			if (item.isSpawn())
				continue;
			if (System.currentTimeMillis() > (item.getDroppedAt() + HIDE_FOR) && item.getDroppedBy() != null) {
			/*	if(item.getX() > 2941 && item.getX() < 3392 && item.getY() > 3518 && item.getY() < 3966) {
					if(item.id == 10551 || item.id == 7454 || item.id == 7455  || item.id == 7456 || item.id == 7457 || item.id == 7458||
					item.id == 7459 || item.id == 7460 || item.id == 7461  || item.id == 7462 || item.id == 7463 ||
					item.id == 11663 || item.id == 8839 || item.id == 7923  || item.id == 8840 || item.id == 11664 ||
					item.id == 7940 || item.id == 11665 || item.id == 7942  || item.id == 8842 || item.id == 18353 ||
					item.id == 18351 || item.id == 6570 || item.id == 6656 || item.id == 6571){
						remove.add(item);
						hideDrop(item);						
					} else {
						item.resetOwner();
						remove.add(item);
						hideDrop(item);					
					}
				} else {*/
					if(item.id == 10551 || item.id == 7454 || item.id == 7455  || item.id == 7456 || item.id == 7457 || item.id == 7458||
					item.id == 7459 || item.id == 7460 || item.id == 7461  || item.id == 7462 || item.id == 7463 ||
					item.id == 11663 || item.id == 8839 || item.id == 7923  || item.id == 8840 || item.id == 11664 ||
					item.id == 7940 || item.id == 11665 || item.id == 7942  || item.id == 8842 || item.id == 18353 ||
					item.id == 18351 || item.id == 6570 || item.id == 6656 || item.id == 6571){
						remove.add(item);
						hideDrop(item);						
					} else {
						item.resetOwner();
						hideDrop(item);
						showDrop(item);				
					}				
				//}
			} else if (System.currentTimeMillis() > (item.getDroppedAt() + STAY_FOR)) {
				remove.add(item);
				hideDrop(item);
			}
		}
		for (FloorItem r : remove) {
			list.remove(r);
		}
		remove.clear();
		remove = null;
	}
	/**
	 * Shows items when the player loads a new region.
	 * 
	 * @param p
	 *            The player.
	 */
	public void update(Client p) {
		for (FloorItem item : list) {
			if (item.getHeight() != p.getHeightLevel())
				continue;
			int tmpX = item.getX() - p.getAbsX();
			int tmpY = item.getY() - p.getAbsY();
			if (tmpX >= -96 && tmpX <= 96 && tmpY >= -96 && tmpY <= 96) {
				if (item.getDroppedBy() != null || !item.isSpawn()) {
					if (item.getDroppedBy() != p) {
						continue;
					}
				}
				p.getOutStream().createFrame(85);
				p.getOutStream().writeByteC((item.getY() - 8 * p.mapRegionY));
				p.getOutStream().writeByteC((item.getX() - 8 * p.mapRegionX));
				// hide existing drop
				p.getOutStream().createFrame(156);
				p.getOutStream().writeByteS(0);
				p.getOutStream().writeWord(item.getId());
				// show new drop
				p.getOutStream().createFrame(44);
				p.getOutStream().writeWordBigEndianA(item.getId());
				p.getOutStream().writeWord(item.getAmount());
				p.getOutStream().writeByte(0);
				p.flushOutStream();
			}
		}
	}

	public void destruct(Client p) {
		for (FloorItem item : list) {
			if (item.getDroppedBy() != null) {
				if (item.getDroppedBy().equals(p)) {
					if(item.id == 10551 || item.id == 7454 || item.id == 7455  || item.id == 7456 || item.id == 7457 || item.id == 7458||
					item.id == 7459 || item.id == 7460 || item.id == 7461  || item.id == 7462 || item.id == 7463 ||
					item.id == 11663 || item.id == 8839 || item.id == 7923  || item.id == 8840 || item.id == 11664 ||
					item.id == 7940 || item.id == 11665 || item.id == 7942  || item.id == 8842 || item.id == 18353 ||
					item.id == 18351 || item.id == 6570 || item.id == 6571){
					
					} else {
						item.resetOwner();
						hideDrop(item);
						showDrop(item);					
					}

				}
			}
		}
	}
	public void newDropAmount(FloorItem i, Client p, int amount) {
		list.add(i);
		if(p == null) return;
		//i.setPos(p.getAbsX(), p.getAbsY(), p.getHeightLevel());
		p.getOutStream().createFrame(85);
		p.getOutStream().writeByteC((i.getY() - 8 * p.mapRegionY));
		p.getOutStream().writeByteC((i.getX() - 8 * p.mapRegionX));
		p.getOutStream().createFrame(44);
		p.getOutStream().writeWordBigEndianA(i.getId());
		p.getOutStream().writeWord(i.getAmount()+amount);
		p.getOutStream().writeByte(0);
		p.flushOutStream();
	}
	public void newDrop(FloorItem i, Client p) {
		list.add(i);
		if(p == null) return;
		//i.setPos(p.getAbsX(), p.getAbsY(), p.getHeightLevel());
		p.getOutStream().createFrame(85);
		p.getOutStream().writeByteC((i.getY() - 8 * p.mapRegionY));
		p.getOutStream().writeByteC((i.getX() - 8 * p.mapRegionX));
		p.getOutStream().createFrame(44);
		p.getOutStream().writeWordBigEndianA(i.getId());
		p.getOutStream().writeWord(i.getAmount());
		p.getOutStream().writeByte(0);
		p.flushOutStream();
	}
	
	public void rareDrop(Client c, int itemId, int amount) {
	
	boolean rare = false;
	for (int i : Config.ITEM_RARE) {
		if (i == itemId) {
			rare = true;
			break;
		}
	}		
	if(rare) {
		PlayerManager.getSingleton().sendGlobalMessage("[@red@PvM@bla@]@mag@ " + c.playerName + " has received @dre@1 x " + Server.getItemManager().getItemDefinition(itemId).getName() + "@mag@.");
	}
	}

	public void newDropFromNPC(FloorItem l, Client attacking) {
		if (l.getId() == -1 || l.getId() == 0)
			return;
		list.add(l);
		if (attacking == null) {
			showDrop(l);
			return;
		}
		attacking.getOutStream().createFrame(85);
		attacking.getOutStream().writeByteC(
				(l.getY() - 8 * attacking.mapRegionY));
		attacking.getOutStream().writeByteC(
				(l.getX() - 8 * attacking.mapRegionX));
		attacking.getOutStream().createFrame(44);
		attacking.getOutStream().writeWordBigEndianA(l.getId());
		attacking.getOutStream().writeWord(l.getAmount());
		attacking.getOutStream().writeByte(0);
		attacking.flushOutStream();
	}

	public void showDrop(FloorItem i) {
		for (Player p : PlayerManager.getSingleton().getPlayers()) {
			if (p == null)
				continue;
			if (!p.isActive)
				continue;
			if (p.disconnected)
				continue;
			Client player = (Client) p;
			if (i.getHeight() != player.getHeightLevel())
				continue;
			int tmpX = i.getX() - player.getAbsX();
			int tmpY = i.getY() - player.getAbsY();
			if (tmpX >= -96 && tmpX <= 96 && tmpY >= -96 && tmpX <= 96) {
				player.getOutStream().createFrame(85);
				player.getOutStream().writeByteC(
						(i.getY() - 8 * player.mapRegionY));
				player.getOutStream().writeByteC(
						(i.getX() - 8 * player.mapRegionX));
				player.getOutStream().createFrame(44);
				player.getOutStream().writeWordBigEndianA(i.getId());
				player.getOutStream().writeWord(i.getAmount());
				player.getOutStream().writeByte(0);
				player.flushOutStream();
			}
		}
	}

	public void hideDrop(FloorItem i) {
		for (Player p : PlayerManager.getSingleton().getPlayers()) {
			if (p == null)
				continue;
			if (!p.isActive)
				continue;
			if (p.disconnected)
				continue;
			Client player = (Client) p;
			int tmpX = i.getX() - player.getAbsX();
			int tmpY = i.getY() - player.getAbsY();
			if (tmpX >= -96 && tmpX <= 96 && tmpY >= -96 && tmpX <= 96
					&& player.getHeightLevel() == i.getHeight()) {
				player.getOutStream().createFrame(85);
				player.getOutStream().writeByteC(
						(i.getY() - 8 * player.mapRegionY));
				player.getOutStream().writeByteC(
						(i.getX() - 8 * player.mapRegionX));
				player.getOutStream().createFrame(156);
				player.getOutStream().writeByteS(0);
				player.getOutStream().writeWord(i.getId());
				player.flushOutStream();
			}
		}
	}

	public void pickupDrop(Client p, int absX, int absY, int height, int itemID) {
		try {
			for (FloorItem i : list) {
				int diffX = i.getX() - p.getAbsX();
				int diffY = i.getY() - p.getAbsY();
				if (diffX <= 1 && diffX >= -1 && diffY <= 1 && diffY >= -1) {
					if (i.getId() == itemID && i.getX() == absX
							&& i.getY() == absY && i.getHeight() == height) {
						if (i.isSpawn()) {
							if (p.getActionAssistant().addItem(i.getId(),
									i.getAmount())) {
								list.remove(i);
								hideDrop(i);
								final FloorItem f = i;
								EventManager.getSingleton().addEvent(null,"null item man",
										new Event() {
											public void execute(EventContainer c) {
												FloorItem drop = new FloorItem(f.getId(), f.getAmount(), null, f.getX(), f.getY(), f.getHeight());
												drop.setSpawn(true);
												showDrop(drop);
												list.add(drop);
												c.stop();
											}
											@Override
											public void stop() {
											}
										}, DROPS_RESPAWN_TIME);
								break;
							}
						} else if (i.getDroppedBy() == null && i.canPickup()) {
							if (p.getActionAssistant().addItem(i.getId(),
									i.getAmount())) {
								i.setTaken();
								list.remove(i);
								hideDrop(i);
								break;
							}
						} else if (i.getDroppedBy().equals(p) && i.canPickup()) {
							if (p.getActionAssistant().addItem(i.getId(),
									i.getAmount())) {
								i.setTaken();
								list.remove(i);
								hideDrop(i);
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public int itemAmount(int itemId, int itemX, int itemY) {
		for(FloorItem i : list) {
			if(i.getId() == itemId && i.getX() == itemX && i.getY() == itemY) {
				return i.getAmount();
			}
		}
		return 0;
	}

}
