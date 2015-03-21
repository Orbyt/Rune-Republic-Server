package com.rs.worldserver.content.skill;

import com.rs.worldserver.Server;
import com.rs.worldserver.*;
import com.rs.worldserver.content.Skill;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.object.GameObject;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;
import com.rs.worldserver.util.Misc;

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
 * Woodcutting skill handler
 * 
 * @author Graham
 * 
 */
public class Woodcutting implements Skill {

	/**
	 * A huge collection of constants here.
	 */
	private static final int[] NORMAL_TREES = { 1276, 1277, 1278, 1279, 1280,
			1282, 1283, 1284, 1285, 1286, 1289, 1290, 1291, 1315, 1316, 1318,
			1319, 1330, 1331, 1332, 1365, 1383, 1384, 2409, 3033, 3034, 3035,
			3036, 3881, 3882, 3883, 5902, 5903, 5904 };
	private static final int[] WILLOW_TREES = { 1308, 5551, 5552, 5553 };
	private static final int[] OAK_TREES = { 1281, 3037 };
	private static final int[] MAGIC_TREES = { 1292, 1306 };
	private static final int[] MAPLE_TREES = { 1307, 4677 };
	private static final int[] MAHOGANY_TREES = { 9034 };
	private static final int[] TEAK_TREES = { 9036 };
	private static final int[] ACHEY_TREES = { 2023 };
	private static final int[] YEW_TREES = { 1309 };
	private static final int[] AXES = { 6739,1359, 1357, 1355, 1353, 1349, 1351 };
	private static final int[] AXES_REQUIRED_LEVElS = {61, 41, 31, 21, 6, 6, 1, 1 };
	private static final int[] AXES_ANIMATIONS = { 2846,867, 869, 871, 875, 877, 879 };
	private static final int WOODCUTTING_DELAY = 3000;
	protected static final double WOODCUTTING_FACTOR = 0.5;
	// TODO different IDs for various trees?
	private static final int TREE_STUMP = 1341;
	private static final int TREE_RESPAWN_TIME = Misc.randomRange(5000,12000);

	/**
	 * Click object hook.
	 * 
	 * @param client
	 * @param object
	 * @param x
	 * @param y
	 * @return
	 */
	public static final boolean cut(final Client client, final int object,
			final int x, final int y) {
		int startedCutting = 0;
		if (!isTree(object))
			return false;
		if (client.getActionAssistant().freeSlots() == 0) {
			client.getActionAssistant().sendMessage(
					"There is not enough space in your inventory.");
			return true;
		}
		if (client.checkBusy()) {
			return true;
		}
		
			/*if (!Server.getObjectManager().xcoords.contains(x)){
			client.getActionAssistant().sendMessage(
					"You are not clicking an x 1");
				//return true;
			}
			if (
			!Server.getObjectManager().ycoords.contains(y)) {
			client.getActionAssistant().sendMessage(
					"You are not clicking an y 1");
				//return true;
			}
		
			if (!WorldMap.xcoords2.contains(x)) {
			client.getActionAssistant().sendMessage(
					"You are not clicking an x 2");
			
			}if (!WorldMap.ycoords2.contains(y)) {
			client.getActionAssistant().sendMessage(
					"You are not clicking an y 2");
			
			}*/
			if (!Server.getObjectManager().xcoords.contains(x)
			&& !Server.getObjectManager().ycoords.contains(y)
			&& !WorldMap.xcoords2.contains(x)
			&& !WorldMap.ycoords2.contains(y)) {
				return false;
			}
		final int axe = hasAxe(client);
		if (axe == -1) {
			client.getActionAssistant().sendMessage(
					"You do not have an axe that you can use.");
			return true;
		}
		final int level = getTreeLevel(object);
		if (!(client.playerLevel[SkillConstants.WOODCUTTING] >= level)) {
			client
					.getActionAssistant()
					.sendMessage(
							"You do not have the required level to cut down that tree.");
			return true;
		}
		client.setBusy(true);
		int numberOfCycles = (int) Math.random() * 5 + 15;
		if (contains(NORMAL_TREES, object)) {
			numberOfCycles = 1;
		}
		if (client.withinInteractionDistanceWC(x,y,client.getHeightLevel())) {
			client.getActionAssistant().sendMessage(
				"You swing your axe at the tree...");
			client.getActionAssistant().startAnimation(AXES_ANIMATIONS[axe]);
			startedCutting = 1;
			//client.getActionAssistant().startAnimation(1833);
			if(client.sounds == 1) {
				client.getActionAssistant().frame174(471,000,000);
				client.getActionAssistant().frame174(472,000,050);
				client.getActionAssistant().frame174(473,000,100);
			}
		}
		final int hasCut = startedCutting;
		client.getActionAssistant().turnTo(x, y);
		client.fishing = true;
		final int fNumberOfCycles = numberOfCycles;
		client.walked = false;
		EventManager.getSingleton().addEvent(client,"woodcutting", new Event() {
			public int cycle = -1;
			public int startedCutting = hasCut;
			public void execute(EventContainer c) {
				// tree cut down by another player
				if (object != 1306 && Server.getObjectManager().getObjectAt(x,y,client.getHeightLevel()) != null) {
					c.stop();
					return;
				}
				else if (object==1306) {
					if (Server.getObjectManager().stumpAt(x,y)) {
						c.stop();
						return;
					}
				}
				if(client.walked) {
					client.setBusy(false);
					cycle = -1;
					c.stop();
					return;
				}
				
				if (!client.withinInteractionDistanceWC(x,y,client.getHeightLevel())) {
					return; // do nothing until player arrives at the tree
				}
				else if (startedCutting==0) {
					client.getActionAssistant().sendMessage("You swing your axe at the tree...");
					client.getActionAssistant().startAnimation(AXES_ANIMATIONS[axe]);
					if(client.sounds == 1) {
						client.getActionAssistant().frame174(471,000,000);
						client.getActionAssistant().frame174(472,000,050);
						client.getActionAssistant().frame174(473,000,100);
					}
					startedCutting=1;
					return;
				}
				client.getActionAssistant().turnTo(x, y);

				if (client.getActionAssistant().freeSlots() == 0) {
					client.getActionAssistant().sendMessage(
							"There is not enough space in your inventory.");
					c.stop();
					return;
				}

				if (cycle == -1) {
					cycle = fNumberOfCycles;
				}

				if (fNumberOfCycles == 1 || Math.random() > WOODCUTTING_FACTOR) {
					int xp = 0;
					if (contains(NORMAL_TREES, object)) {
						xp = 25;
						client.getActionAssistant().addItem(1511, 1);
						client.getActionAssistant().sendMessage(
								"You get some logs.");
					} else if (contains(WILLOW_TREES, object)) {
						xp = 68;
						client.getActionAssistant().addItem(1519, 1);
						client.getActionAssistant().sendMessage(
								"You get some willow logs.");
					} else if (contains(OAK_TREES, object)) {
						xp = 38;
						client.getActionAssistant().addItem(1521, 1);
						client.getActionAssistant().sendMessage(
								"You get some oak logs.");
					} else if (contains(MAGIC_TREES, object)) {
						xp = 250;
						client.getActionAssistant().addItem(1513, 1);
						client.getActionAssistant().sendMessage(
								"You get some magic logs.");
					} else if (contains(MAPLE_TREES, object)) {
						xp = 100;
						client.getActionAssistant().addItem(1517, 1);
						client.getActionAssistant().sendMessage(
								"You get some maple logs.");
					} else if (contains(MAHOGANY_TREES, object)) {
						xp = 125;
						client.getActionAssistant().addItem(6332, 1);
						client.getActionAssistant().sendMessage(
								"You get some mahogany logs.");
					} else if (contains(TEAK_TREES, object)) {
						xp = 85;
						client.getActionAssistant().addItem(6333, 1);
						client.getActionAssistant().sendMessage(
								"You get some teak logs.");
					} else if (contains(ACHEY_TREES, object)) {
						xp = 25;
						client.getActionAssistant().addItem(2862, 1);
						client.getActionAssistant().sendMessage(
								"You get some achey logs.");
					} else if (contains(YEW_TREES, object)) {
						xp = 175;
						client.getActionAssistant().addItem(1515, 1);
						client.getActionAssistant().sendMessage(
								"You get some yew logs.");
					}
					// add xp
					client.getActionAssistant().addSkillXP(xp,
							SkillConstants.WOODCUTTING);
					client.getActionAssistant().refreshSkill(SkillConstants.WOODCUTTING);
				}

				if (cycle != 1) {
					client.getActionAssistant().startAnimation(AXES_ANIMATIONS[axe]);
					//client.getActionAssistant().startAnimation(1833);
					if(client.sounds == 1) {		
					client.getActionAssistant().frame174(471,000,000);
					client.getActionAssistant().frame174(472,000,050);
					client.getActionAssistant().frame174(473,000,100);
					}
				}

				cycle--;

				if (cycle == 0) {
					client.setBusy(false);
					c.stop();
					final GameObject stump = new GameObject(x, y, client
							.getHeightLevel(), GameObject.Face.NORTH, 10,
							Server.getObjectManager().getDefinition(TREE_STUMP));
					final GameObject tree = new GameObject(x, y, client
							.getHeightLevel(), GameObject.Face.NORTH, 10,
							Server.getObjectManager().getDefinition(object));
					Server.getObjectManager().addObject(stump);
					EventManager.getSingleton().addEvent(null,"null woodcutting", new Event() {

						@Override
						public void execute(EventContainer container) {
							Server.getObjectManager().replaceTemporaryObject(
									stump, tree);
							container.stop();
						}

						@Override
						public void stop() {

						}

					}, TREE_RESPAWN_TIME);
				}
			}

			@Override
			public void stop() {
				client.setBusy(false);
				client.fishing = false;
				client.getActionAssistant().resetAnimation();
			}
		}, WOODCUTTING_DELAY);
		return true;
	}

	public static int getTreeLevel(int id) {
		int level = 1;
		if (contains(NORMAL_TREES, id)) {
			level = 1;
		} else if (contains(OAK_TREES, id)) {
			level = 15;
		} else if (contains(WILLOW_TREES, id)) {
			level = 30;
		} else if (contains(MAGIC_TREES, id)) {
			level = 75;
		} else if (contains(MAPLE_TREES, id)) {
			level = 45;
		} else if (contains(MAHOGANY_TREES, id)) {
			level = 50;
		} else if (contains(TEAK_TREES, id)) {
			level = 35;
		} else if (contains(ACHEY_TREES, id)) {
			level = 1;
		} else if (contains(YEW_TREES, id)) {
			level = 60;
		}
		return level;
	}

	/**
	 * Axe check.
	 * 
	 * @param client
	 * @return
	 */
	private static final int hasAxe(Client client) {
		int ct = 0;
		int level = client.playerLevel[SkillConstants.WOODCUTTING];
		for (int id : AXES) {
			if (level >= AXES_REQUIRED_LEVElS[ct]) {
				if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == id) {
					return ct;
				} else if (client.getActionAssistant().isItemInBag(id)) {
					return ct;
				}
			}
			ct++;
		}
		return -1;
	}

	/**
	 * Is tree?
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isTree(int id) {
		return contains(NORMAL_TREES, id) || contains(OAK_TREES, id)
				|| contains(WILLOW_TREES, id) || contains(MAGIC_TREES, id)
				|| contains(MAPLE_TREES, id) || contains(MAHOGANY_TREES, id)
				|| contains(TEAK_TREES, id) || contains(ACHEY_TREES, id)
				|| contains(YEW_TREES, id);
	}

	/**
	 * Should really be moved to a utility class!
	 * 
	 * @param array
	 * @param value
	 * @return
	 */
	public static boolean contains(int[] array, int value) {
		for (int i : array) {
			if (i == value) {
				return true;
			}
		}
		return false;
	}

}
