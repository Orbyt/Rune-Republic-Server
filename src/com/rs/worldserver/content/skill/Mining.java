package com.rs.worldserver.content.skill;

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

import com.rs.worldserver.Server;
import com.rs.worldserver.WorldMap;
import com.rs.worldserver.content.Skill;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.object.GameObject;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;

/**
 * Mining skill handler
 * 
 * @author Graham Wafer
 * 
 */
public class Mining implements Skill {

	// TODO a better method of this than these ugly arrays
	// Clay 434,1,5
	// Tin 438,1,17
	// Copper 436,1,17 
	// Iron 440, 15 ,35
	// Coal 453,40,65
	// silver 442,40,20
	// Gold 444,55,65
	// 
	private static final int[] ROCKS = {14863,14862,14864,14857,14858,14856,14853,14854,14855,14850,
	14851,14852,9713,9711,11189,11190,11191,11190,9714,9716,
	11187,11186,11188,9722,9720,11185,11184,11183,9718,9717,
	9719,9709,9708,9710,11960,11954,11956,11957,11958,11959,
	11962,11964, 11963,14859,14860,2106,2103,2104,2098,2096,
	2491, 2108, 2109, 2090, 2091, 2094,2095, 2110, 4030, 2092,
	2093, 2101, 3403, 2097, 2103, 2105,2107,2102,2099 };
	private static final int[] ROCKS_ORES = {449,449,449,453,453,453,447,447,447,453,
	453,453,434,434,434,434,434,434,442,442,
	442,442,442,444,444,444,444,444,440,440,
	440,436,436,436,434,440,440,438,438,438,
	440,453,453,451,451,451, 447,449,444,453,
	1436, 434, 434, 436, 436, 438,438, 668, 3211,
	440, 440, 442, 2892, 453, 447, 449, 451,447,444 };
	private static final int[] ROCK_REQUIRED_LEVELS = {70,70,70,40,40,40,55,55,55,40,
	40,40,1,1,1,1,1,1,20,20,
	20,20,20,40,40,40,40,40,15,15,
	15,1,1,1,1,15,15,1,1,1,
	15,40,40,85,85,85,55,70,40, 40,
	1, 1, 1, 1, 1, 1, 1,10, 10, 15, 
	15, 20, 30, 40, 40, 70, 85, 55, 55 };
	private static final int[] ROCKS_ORES_XPS = {95,95,95,65,65,65,80,80,80,65,65,65,5,5,5,5,5,5,20,20,20,20,20,65,65,65,65,65,35,35,35,17,17,17,5,35,35,17,17,17,35, 65,65,125,125,125,80,95,65,50,5, 5, 5, 17, 17, 17, 17, 17,
			26, 35, 35, 40, 0, 50, 65, 80, 95, 125,80 };
	private static final String[] ROCKS_ORES_NAMES = {"adamant","adamant","adamant","coal","coal","coal","mithril","mithril","mithril","coal","coal","coal","clay","clay","clay","clay","clay","clay","silver","silver","silver","silver","silver","gold","gold","gold","gold","gold","iron","iron", "iron","copper","copper","copper","clay","iron","iron","tin","tin", "tin","iron","coal","coal","runite","runite","runite", "mithril","adamant","coal","coal","rune essence", "clay",
			"clay", "copper", "copper", "tin", "tin", "bluerite", "limestone",
			"iron", "iron", "silver", "elemental", "coal", "gold", "adamant",
			"runite", "mithril","mithril" };
	private static final int[] PICKAXES = { 1275, 1273, 1271, 1269, 1267, 1265, 15259, 9970,15259};
	private static final int[] PICKAXE_ANIMATIONS = { 624, 628, 629, 627, 626,625, 12188, 12188, 12188};
	private static final int[] PICKAXES_REQUIRED_LEVELS = { 41, 31, 21, 6, 1, 1, 99, 99 ,99};
	private static final int MINING_DELAY = 5500;
	private static final int PROSPECTING_DELAY = 1000;
	private static double MINING_FACTOR = 0.2;
	private static final int EXPIRED_ORE = 450;
	private static final int ORE_RESPAWN_TIME = 2000;

	public static boolean prospect(final Client player, final int type,
			final int x, final int y) {
		if (!isRock(type)) {
			return false;
		} else {
			if (player.checkBusy()) {
				return true;
			} else {
				player.setBusy(true);
			}
			final String name = getOreName(type);
			player.getActionAssistant().sendMessage(
					"You examine the rock for ores...");
			player.getActionAssistant().turnTo(x, y);
			EventManager.getSingleton().addEvent(player,"mining", new Event() {
				public void execute(EventContainer c) {
					player.getActionAssistant().sendMessage(
							"This rock contains " + name + ".");
					c.stop();
				}

				@Override
				public void stop() {
					player.setBusy(false);
				}
			}, PROSPECTING_DELAY);
			return true;
		}
	}

	public static boolean mine(final Client player, final int objectID,
			final int objectX, final int objectY) {
		int miningDelay=1000;
		int startedMining = 0;
		if (objectID == 450) {
			player.getActionAssistant().sendMessage(
					"That rock contains no ore.");
			return true;
		}
		if (!isRock(objectID)) {
			return false;
		}
		else {
			if (player.checkBusy()) {
				return true;
			}
			
			if (player.getActionAssistant().freeSlots() == 0) {
				player.getActionAssistant().sendMessage(
						"There is not enough space in your inventory.");
				return true;
			}
			final int pickaxe = hasPickaxe(player);
			if (pickaxe == -1) {
				player.getActionAssistant().sendMessage(
						"You do not have a pickaxe that you can use.");
				return true;
			}
			if (player.playerLevel[SkillConstants.MINING] < getOreLevel(objectID)) {
				player.getActionAssistant().sendMessage(
						"You need a mining level of " + getOreLevel(objectID)
								+ " to mine this rock.");
				return true;
			}
						if (!Server.getObjectManager().xcoords.contains(objectX)
			&& !Server.getObjectManager().ycoords.contains(objectY)
			&& !WorldMap.xcoords2.contains(objectX)
			&& !WorldMap.ycoords2.contains(objectY)) {
				return false;
			}
			
			final int oreType = getOre(objectID);
			if (oreType == -1) {
				player.getActionAssistant().sendMessage(
						"You cannot mine that ore.");
				return true;
			}
			GameObject g = Server.getObjectManager().getObjectAt(objectX, objectY,
					player.getHeightLevel());
			if (g != null && g.getType() == EXPIRED_ORE) {
				player.getActionAssistant().sendMessage(
						"There is no ore currently available in this rock.");
				return true;
			}
			player.setBusy(true);
			if (player.withinInteractionDistanceWC(objectX,objectY,player.getHeightLevel())) {
				miningDelay=MINING_DELAY;
				player.getActionAssistant().sendMessage("You swing your pick at the rock...");
				startedMining=1;
				player.getActionAssistant().startAnimation(PICKAXE_ANIMATIONS[pickaxe]);
				if(player.sounds == 1) {		
					player.getActionAssistant().frame174(1230,000,000);
				}
			}
			final int hasMined = startedMining;
			player.getActionAssistant().turnTo(objectX, objectY);
			player.walked = false;
			if(objectID == 2491) {
				player.setBusy(true);
				EventManager.getSingleton().addEvent(player,"mining2", new Event() {
					public int cycle = 28;	
					public int startedMining = hasMined;
					public void execute(EventContainer c) {
						if(player.walked) {
							player.setBusy(false);
							c.stop();
							return;
						}								
						if (!player.withinInteractionDistanceWC(objectX,objectY,player.getHeightLevel())) {
							//c.setTick(1000);
							return; // do nothing until player arrives at the rock
						}
						else if (startedMining==0) {
							player.getActionAssistant().sendMessage("You swing your pick at the rock...");
							startedMining=1;
							player.getActionAssistant().startAnimation(PICKAXE_ANIMATIONS[pickaxe]);
							if(player.sounds == 1) {		
								player.getActionAssistant().frame174(1230,000,000);
							}
							c.setTick(MINING_DELAY);
							return;
						}
						if (player.getActionAssistant().freeSlots() == 0) {
							player.getActionAssistant().sendMessage(
									"There is not enough space in your inventory.");
							c.stop();
							return;
						}	
						if (Math.random() > MINING_FACTOR) {
							player.getActionAssistant().addItem(oreType, 1);
							player.getActionAssistant().sendMessage("You manage to mine some essence.");
							int xp = getOreXp(objectID);
							if(player == null) { c.stop();}
							player.getActionAssistant().addSkillXP(xp,SkillConstants.MINING);
							player.getActionAssistant().refreshSkill(SkillConstants.MINING);
						}
						player.getActionAssistant().startAnimation(PICKAXE_ANIMATIONS[pickaxe]);
						if(player.sounds == 1) {		
							player.getActionAssistant().frame174(1230,000,000);	
						}
						cycle--;
						if(player.walked) {
							player.setBusy(false);
							c.stop();
							return;
						}
						if (cycle == 0) {
							c.stop();
						}
					}
					@Override
					public void stop() {
						player.setBusy(false);
						
					}
				}, miningDelay);
					
			} 
			else {
				EventManager.getSingleton().addEvent(player,"mining3", new Event() {
					public int startedMining = hasMined;
					public void execute(EventContainer c) {
						if(player.walked) {
							c.stop();
							return;
						}
						if (!player.withinInteractionDistance(objectX,objectY,player.getHeightLevel())) {
							//c.setTick(1000);
							return; // do nothing until player arrives at the rock
						}
						else if (startedMining==0) {
							player.getActionAssistant().sendMessage("You swing your pick at the rock...");
							startedMining=1;
							player.getActionAssistant().startAnimation(PICKAXE_ANIMATIONS[pickaxe]);
							if(player.sounds == 1) {		
								player.getActionAssistant().frame174(1230,000,000);
							}
							c.setTick(MINING_DELAY);
							return;
						}
						GameObject g = Server.getObjectManager().getObjectAt(objectX, objectY,
								player.getHeightLevel());
						if (g != null && g.getType() == EXPIRED_ORE) {
							c.stop();
							player.getActionAssistant().sendMessage("There is currently no ore available in this rock.");
							return;
						}
						if (player.getActionAssistant().freeSlots() == 0) {
							player.getActionAssistant().sendMessage("Your inventory is too full to hold any more.");
							c.stop();
							return;
						} else {
							if (Math.random() > MINING_FACTOR) {
							if (oreType == 451) {
								player.achievementProgress[5] += 1;
								if (player.achievementProgress[5] == 500) {
										player.getNRAchievements().checkMisc(player,19);
									}
								}
								player.getActionAssistant().addItem(oreType, 1);
								player.getActionAssistant().sendMessage("You manage to mine some ore.");
								int xp = getOreXp(objectID);
								if(player == null) { c.stop();}
								player.getActionAssistant().addSkillXP(xp,SkillConstants.MINING);
								player.getActionAssistant().refreshSkill(SkillConstants.MINING);
								c.stop();
								if(objectID != 2491){
								final GameObject expired = new GameObject(objectX,
										objectY, player.getHeightLevel(),
										GameObject.Face.NORTH, 10, Server
												.getObjectManager().getDefinition(
														EXPIRED_ORE));
								final GameObject normal = new GameObject(objectX,
										objectY, player.getHeightLevel(),
										GameObject.Face.NORTH, 10, Server
												.getObjectManager().getDefinition(
														objectID));
								Server.getObjectManager().addObject(expired);
								EventManager.getSingleton().addEvent(null,"mining4",
										new Event() {

											@Override
											public void execute(
													EventContainer container) {
												Server.getObjectManager()
														.replaceTemporaryObject(
																expired, normal);
												container.stop();
											}

											@Override
											public void stop() {

											}

										}, ORE_RESPAWN_TIME);
										}
							} else {
								player.getActionAssistant().startAnimation(
										PICKAXE_ANIMATIONS[pickaxe]);
								if(player.sounds == 1) {		
								player.getActionAssistant().frame174(432,000,000);	
								player.getActionAssistant().frame174(432,000,050);
								player.getActionAssistant().frame174(432,000,100);		}								
							}
						}
					}

					@Override
					public void stop() {
						player.setBusy(false);
						player.getActionAssistant().resetAnimation();
					}
				}, miningDelay);
			}
			return true;
		}
	}

	/**
	 * Gets the ore item id for a rock type.
	 * 
	 * @param rock
	 *            The rock.
	 * @return The ore id.
	 */
	public static int getOre(int rock) {
		int ct = 0;
		for (int obj : ROCKS) {
			if (obj == rock) {
				return ROCKS_ORES[ct];
			}
			ct++;
		}
		return -1;
	}

	/**
	 * Gets level needed to mine ore
	 * 
	 * @param rock
	 *            The rock.
	 * @return The level needed to mine the ore.
	 */
	public static int getOreLevel(int rock) {
		int ct = 0;
		for (int obj : ROCKS) {
			if (obj == rock) {
				return ROCK_REQUIRED_LEVELS[ct];
			}
			ct++;
		}
		return -1;
	}

	/**
	 * Checks if an object is in the rocks array.
	 * 
	 * @param id
	 *            The object id.
	 * @return True if the object is a rock, false if not.
	 */
	public static boolean isRock(int id) {
		// check if id is a valid rock
		for (int obj : ROCKS) {
			if (obj == id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks ore name
	 * 
	 * @param rock
	 *            The object id.
	 * @return The rock ores name
	 */
	public static String getOreName(int rock) {
		int ct = 0;
		for (int obj : ROCKS) {
			if (obj == rock) {
				return ROCKS_ORES_NAMES[ct];
			}
			ct++;
		}
		return "-1";
	}

	/**
	 * Gets ore XP.
	 * 
	 * @param rock
	 * @return
	 */
	public static int getOreXp(int rock) {
		int ct = 0;
		for (int obj : ROCKS) {
			if (obj == rock) {
				return ROCKS_ORES_XPS[ct];
			}
			ct++;
		}
		return -1;
	}

	/**
	 * Checks if the player has a pick axe.
	 * 
	 * @param player
	 *            The player.
	 * @return True if the player has one, false if not.
	 */
	public static int hasPickaxe(Client client) {
		int ct = 0;
		int level = client.playerLevel[SkillConstants.MINING];
		for (int id : PICKAXES) {
			if (level >= PICKAXES_REQUIRED_LEVELS[ct]) {
				if ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) == id) {
					return (ct);
				} else if (client.getActionAssistant().isItemInBag(id)) {
					return (ct);
				}
			}
			ct++;
		}
		return (-1);
	}

	/**
	 * Gets level needed to use pick axe
	 * 
	 * @param pickaxe
	 *            The pick axe.
	 * @return The level needed to use the pick axe.
	 */
	public static int getPickLevel(int pickaxe) {
		int ct = 0;
		for (int obj : PICKAXES) {
			if (obj == pickaxe) {
				return PICKAXES_REQUIRED_LEVELS[ct];
			}
			ct++;
		}
		return -1;
	}

}
