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

import com.rs.worldserver.content.Skill;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.util.*;

/**
 * Fishing skill handler
 * 
 * @author Ventrillo
 * 
 */
public class Fishing implements Skill {

	private static final int FISHING_DELAY = 3000;
	private static final double FISHING_FACTOR = 0.4;

	public static class FishingFirstClick {

		/**
		 * A huge collection of constants here.
		 */
		private static final int[] FISHING_SPOT_NET_SM = { 313,316, 320,319, 323,
				325, 327, 326, 332, 330 };
		private static final int[] FISHING_SPOT_NET_BG = { 322 };				
		private static final int[] FISHING_SPOT_FLY = { 309, 310, 311, 314,
				315, 317, 318, 328, 329, 331 };
		private static final int[] FISHING_SPOT_LOBSTER = { 334, 312, 324 };
		private static final int[] FISHING_SPOT_TUNA = { 312 };
		private static final int[] FISHING_SPOT_MONK = { 952 };
		private static final int[] FISHING_SPOT_MT = { 1174 };		
		private static final int[] FISHING_NET = { 303 };
		@SuppressWarnings("unused")
		private static final int[] BIG_FISHING_NET = { 305};
		private static final int[] FLY_ROD = { 309 };
		private static final int[] FEATHER = { 314 };
		private static final int[] LOBSTER_POT = { 301 };
		private static final int[] HARPOON = { 311 };

		/**
		 * Click object hook.
		 * 
		 * @param client
		 * @param object
		 * @param x
		 * @param y
		 * @return
		 */
		public static final boolean clickObject(final Client client,
				final int object, final int x, final int y) {

			if (!isFishNet(object) &&!isFishNetBG(object) && !isFishLobster(object)
					&& !isFishFly(object) && !isFishTuna(object) && !isFishMonk(object)&& !isFishMT(object) && !isFishRock(client, x, y)) {
				return false;
			}

			if (client.checkBusy()) {
				return true;
			}
			client.setBusy(true);
			if (isFishNet(object)) {
				final int net = hasNet(client);
				if (net == -1) {
					client.getActionAssistant().sendMessage(
							"You need a small net to fish here.");
					client.setBusy(false);
					return true;
				}
			}else if (isFishNetBG(object)) {
				final int net = hasBigNet(client);
				if (net == -1) {
					client.getActionAssistant().sendMessage(
							"You need a big net to fish here.");
					client.setBusy(false);
					return true;
				}	
			}else if (isFishMonk(object)) {
				final int net = hasBigNet(client);
				if (net == -1) {
					client.getActionAssistant().sendMessage(
							"You need a big net to fish here.");
					client.setBusy(false);
					return true;
				}	
			}else if (isFishMT(object)) {
				final int net = hasBigNet(client);
				if (net == -1) {
					client.getActionAssistant().sendMessage(
							"You need a big net to fish here.");
					client.setBusy(false);
					return true;
				}				
			} else if (isFishFly(object)) {
				final int fly = hasFly(client);
				if (fly == -1) {
					client.getActionAssistant().sendMessage(
							"You need a fly fishing rod to fish here.");
					client.setBusy(false);
					return true;
				}
			} else if (isFishFly(object)) {
				final int feather = hasFeather(client);
				if (feather == -1) {
					client.getActionAssistant().sendMessage(
							"You need feathers to fish here.");
					client.setBusy(false);
					return true;
				}
			} else if (isFishLobster(object)) {
				final int cage = hasCage(client);
				if (cage == -1) {
					client.getActionAssistant().sendMessage(
							"You need a lobster cage to fish here.");
					client.setBusy(false);
					return true;
				}
			} else if (isFishTuna(object) || isFishRock(client,x,y )) {
				final int harpoon = hasHarpoon(client);
				if (harpoon == -1) {
					client.getActionAssistant().sendMessage(
							"You need a harpoon to fish here.");
					client.setBusy(false);
					return true;
				}
			}
			else {

				return false;
			}

			if (client.getActionAssistant().freeSlots() == 0) {
				client.getActionAssistant().sendMessage(
						"There is not enough space in your inventory.");
						client.setBusy(false);
				return true;
			}

			final int level = getFishLevel(object, client);
			if (!(client.playerLevel[SkillConstants.FISHING] >= level)) {
				client.getActionAssistant().sendMessage(
						"You do not have the required level to fish that.");
						client.setBusy(false);
				return true;
			}

			int animationID = 618;
			if (contains(FISHING_SPOT_NET_SM, object)) {
				animationID = 621;
			} else if (contains(FISHING_SPOT_NET_BG, object)) {
				animationID = 621;	
			} else if (contains(FISHING_SPOT_MONK, object)) {
				animationID = 621;	
			} else if (contains(FISHING_SPOT_MT, object)) {
				animationID = 621;				
			} else if (contains(FISHING_SPOT_LOBSTER, object)) {
				animationID = 619;
			} else if (contains(FISHING_SPOT_TUNA, object) || isFishRock(client,x, y)) {
				animationID = 618;
			} else if (contains(FISHING_SPOT_FLY, object)) {
				animationID = 622;
			}
			client.getActionAssistant().startAnimation(animationID);
			if(client.sounds == 1) {
				client.getActionAssistant().frame174(379,000,000);
			}
			client.getActionAssistant().sendMessage("You start to fish...");
			client.fishing = true;
			client.walked = false;
			client.getActionAssistant().turnTo(x, y);
			final int fNumberOfCycles = 20;//(int) Math.random() * 10 + 5;

			EventManager.getSingleton().addEvent(client,"fishing", new Event() {
				public int cycle = -1;

				public void execute(EventContainer c) {
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

					final int FEATHERSLOT = getFeatherSlot(client);

					if (cycle == 1 || Math.random() > FISHING_FACTOR) {
						int xp = 0;
						boolean got = false;
						if (contains(FISHING_SPOT_NET_SM, object)) {
							if (Math.random() < 0.45) {
								xp = 15;
								client.getActionAssistant().addItem(321, 1);
								client.getActionAssistant().sendMessage(
										"You catch Anchovies.");
										
							} else {
								xp = 15;
								client.getActionAssistant().addItem(317, 1);
								client.getActionAssistant().sendMessage(
										"You catch a shrimp.");
							}
						} else if (contains(FISHING_SPOT_MT, object)) {
							if (Math.random() < 0.95 && client.playerLevel[SkillConstants.FISHING] >= 79 && !got) {
								if(Math.random() < 0.85 && client.playerLevel[SkillConstants.FISHING] >= 81 && !got){
									xp = 200;
									client.getActionAssistant().addItem(389, 1);
									client.getActionAssistant().sendMessage(
											"You catch a Manta Ray.");
									got = true;	
								}
								else {
								xp = 170;
								client.getActionAssistant().addItem(395, 1);
								client.getActionAssistant().sendMessage(
										"You catch a Sea Turtle.");
								got = true;		}
								
								}							
						} else if (contains(FISHING_SPOT_NET_BG, object)) {
							if (Math.random() < 0.45 && client.playerLevel[SkillConstants.FISHING] >= 23 && !got) {
								xp = 45;
								client.getActionAssistant().addItem(341, 1);
								client.getActionAssistant().sendMessage(
										"You catch a cod.");
								got = true;		
							}
							if(Math.random() < 0.45 && client.playerLevel[SkillConstants.FISHING] >= 16 && !got){
								xp = 20;
								client.getActionAssistant().addItem(353, 1);
								client.getActionAssistant().sendMessage(
										"You catch a Mackerel.");
								got = true;		
							}	
							if(Math.random() < 0.45 && client.playerLevel[SkillConstants.FISHING] >= 45 && !got ){
								xp = 100;
								client.getActionAssistant().addItem(363, 1);
								client.getActionAssistant().sendMessage(
										"You catch a Bass.");
								got = true;		
							}								
						} else if (contains(FISHING_SPOT_LOBSTER, object)) {
							xp = 100;
							client.getActionAssistant().addItem(377, 1);
							client.getActionAssistant().sendMessage(
									"You catch a lobster.");
						} else if (contains(FISHING_SPOT_MONK, object)) {
							if (Math.random() < 0.45) {
								xp = 120;
								client.getActionAssistant().addItem(7944, 1);
								client.getActionAssistant().sendMessage(
										"You catch a monkfish.");

							}											
						} else if (contains(FISHING_SPOT_TUNA, object)) {
							if (Math.random() < 0.45) {
								xp = 70;
								client.getActionAssistant().addItem(371, 1);
								client.getActionAssistant().sendMessage(
										"You catch a swordfish.");

							} else {
								xp = 50;
								client.getActionAssistant().addItem(359, 1);
								client.getActionAssistant().sendMessage(
										"You catch a tuna.");
							}			
						} 
						else if (isFishRock(client, x, y)) {
								xp = 150;
								client.getActionAssistant().addItem(15270, 1);
								client.getActionAssistant().sendMessage(
										"You catch a rocktail.");
											client.achievementProgress[2] += 1;
								if (client.achievementProgress[2] == 1000) {
									client.getNRAchievements().checkSkilling(client,14);
								}
								if (client.achievementProgress[2] == 5000) {
									client.getNRAchievements().checkSkilling(client,13);
								}

						}else if (contains(FISHING_SPOT_FLY, object)) {
							if (client.getActionAssistant().isItemInBag(
									FEATHER[0])) {
								if (client.playerLevel[SkillConstants.FISHING] >= 30) {
									if (Math.random() < 0.45) {
										xp = 50;
										client.getActionAssistant().addItem(
												331, 1);
										client.getActionAssistant()
												.sendMessage(
														"You catch a salmon.");
										client.getActionAssistant().deleteItem(
												314, FEATHERSLOT, 1);
									} else {
										xp = 35;
										client.getActionAssistant().addItem(
												335, 1);
										client.getActionAssistant()
												.sendMessage(
														"You catch a trout.");
										client.getActionAssistant().deleteItem(
												314, FEATHERSLOT, 1);
									}
								} else if (client.playerLevel[SkillConstants.FISHING] < 30) {
									xp = 150;
									client.getActionAssistant().addItem(335, 1);
									client.getActionAssistant().sendMessage(
											"You catch a trout.");
									client.getActionAssistant().deleteItem(314,
											FEATHERSLOT, 1);
								}
							} else {
								client.getActionAssistant().sendMessage(
										"You need a feather to fish here.");
								c.stop();
							}
						}
						// add xp
						if(client == null) { c.stop();}
						client.getActionAssistant().addSkillXP(xp,
								SkillConstants.FISHING);
								client.getActionAssistant().refreshSkill(SkillConstants.FISHING);
					}

					int animationID = 0;
					if (contains(FISHING_SPOT_NET_SM, object)) {
						animationID = 621;
					} else if (contains(FISHING_SPOT_NET_BG, object)) {
						animationID = 621;	
					} else if (contains(FISHING_SPOT_MONK, object)) {
						animationID = 621;						
					} else if (contains(FISHING_SPOT_LOBSTER, object)) {
						animationID = 619;
					} else if (contains(FISHING_SPOT_MT, object)) {
						animationID = 621;							
					} else if (contains(FISHING_SPOT_TUNA, object) || isFishRock(client,x, y)) {
						animationID = 618;
					} else if (contains(FISHING_SPOT_FLY, object)) {
						animationID = 622;
					}
					client.getActionAssistant().startAnimation(animationID);
					if(client.sounds == 1) {
						client.getActionAssistant().frame174(379,000,000);
					}					
					cycle--;			
					if (client.walked) {
						c.stop();
					}
				}

				@Override
				public void stop() {
					client.setBusy(false);
					client.getActionAssistant().startAnimation(2552);
					client.fishing = false;
				}
			}, FISHING_DELAY);
			return true;
		}

		public static int getFishLevel(int id, Client c) {
			int level = 1;
			if (contains(FISHING_SPOT_NET_SM, id)) {
				level = 1;
			} else if (contains(FISHING_SPOT_NET_BG, id)) {
				level = 16;	
			} else if (contains(FISHING_SPOT_FLY, id)) {
				level = 20;
			} else if (contains(FISHING_SPOT_LOBSTER, id)) {
				level = 40;
			} else if (contains(FISHING_SPOT_TUNA, id)) {
				level = 35;
			} else if (contains(FISHING_SPOT_MONK, id)) {
				level = 62;	
			} else if (contains(FISHING_SPOT_MT, id)) {
				level = 79;				
			}
			else if (isFishRock(c, 2590,9441)) {
			level = 99;
			}
			return level;
		}

		/**
		 * Is fishing spot Net?
		 * 
		 * @param id
		 * @return
		 */
		public static boolean isFishNet(int id) {
			return contains(FISHING_SPOT_NET_SM, id);
		}
		public static boolean isFishNetBG(int id) {
			return contains(FISHING_SPOT_NET_BG, id);
		}
		/**
		 * Is fishing spot Fly/Bait?
		 * 
		 * @param id
		 * @return
		 */
		public static boolean isFishFly(int id) {
			return contains(FISHING_SPOT_FLY, id);
		}
		public static boolean isFishMT(int id) {
			return contains(FISHING_SPOT_MT, id);
		}
		public static boolean checkFeather(Client client) {
			for (int id : FEATHER) {
				if (client.getActionAssistant().isItemInBag(id)) {
					return true;
				}

			}
			return false;
		}

		/**
		 * Is fishing spot Lobster/Tuna?
		 * 
		 * @param id
		 * @return
		 */
		public static boolean isFishLobster(int id) {
			return contains(FISHING_SPOT_LOBSTER, id);
		}

		/**
		 * Is fishing spot Lobster/Tuna?
		 * 
		 * @param id
		 * @return
		 */
		public static boolean isFishTuna(int id) {
			return contains(FISHING_SPOT_TUNA, id);
		}
		public static boolean isFishRock(Client c, int x , int y){
			if (c.absY > 9300 && c.absY < 9500 && (x == 2594 || x == 2590) && (y ==9441 || y == 9440) ) {
			return true;
			}
			return false;
			}
		public static boolean isFishMonk(int id) {
			return contains(FISHING_SPOT_MONK, id);
		}
		/**
		 * Net check.
		 * 
		 * @param client
		 * @return
		 */
		private static final int hasNet(Client client) {
			int ct = 0;
			for (int id : FISHING_NET) {
				if (client.getActionAssistant().isItemInBag(id)) {
					return ct;
				}
				ct++;
			}
			return -1;
		}
		private static final int hasBigNet(Client client) {
			int ct = 0;
			for (int id : BIG_FISHING_NET) {
				if (client.getActionAssistant().isItemInBag(id)) {
					return ct;
				}
				ct++;
			}
			return -1;
		}
		private static final int getFeatherSlot(Client client) {
			return client.getActionAssistant().getItemSlot(314);
		}

		private static final int hasFly(Client client) {
			int ct = 0;
			for (int id : FLY_ROD) {
				if (client.getActionAssistant().isItemInBag(id)) {
					return ct;
				}
				ct++;
			}
			return -1;
		}

		private static final int hasFeather(Client client) {
			int ct = 0;
			for (int id : FEATHER) {
				if (client.getActionAssistant().isItemInBag(id)) {
					return ct;
				}
				ct++;
			}
			return -1;
		}

		private static final int hasCage(Client client) {
			int ct = 0;
			for (int id : LOBSTER_POT) {
				if (client.getActionAssistant().isItemInBag(id)) {
					return ct;
				}
				ct++;
			}
			return -1;
		}

		private static final int hasHarpoon(Client client) {
			int ct = 0;
			for (int id : HARPOON) {
				if (client.getActionAssistant().isItemInBag(id)) {
					return ct;
				}
				ct++;
			}
			return -1;
		}

		/**
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

	public static class FishingSecondClick {
		/**
		 * A huge collection of constants here.
		 */
		private static final int[] FISHING_SPOT_BAIT = { 316, 320, 319, 323,
				325, 327, 326, 332, 330 };
		private static final int[] FISHING_SPOT_HARPOON_SHARK = { 313, 322 };
		private static final int[] FISHING_SPOT_TUNA = { 312 };

		private static final int[] BAIT_ROD = { 307 };
		private static final int[] BAIT = { 313 };
		private static final int[] HARPOON = { 311, 312 };

		/**
		 * Click object hook.
		 * 
		 * @param client
		 * @param object
		 * @param x
		 * @param y
		 * @return
		 */
		public static final boolean clickObject(final Client client,
				final int object, final int x, final int y) {

			if (!isFishBait(object) && !isFishShark(object)&&!isFishTuna(object)) {
				return false;
			}

			if (client.checkBusy()) {
				return true;
			}
			client.setBusy(true);

			if (client.getActionAssistant().freeSlots() == 0) {
				client.getActionAssistant().sendMessage(
						"There is not enough space in your inventory.");
				return true;
			}

			if (isFishBait(object)) {
				final int rod = hasRod(client);
				if (rod == -1) {
					client.getActionAssistant().sendMessage(
							"You need a rod to fish here.");
					client.setBusy(false);
					return true;
				}
			} else if (isFishBait(object)) {
				final int bait = hasBait(client);
				if (bait == -1) {
					client.getActionAssistant().sendMessage(
							"You need bait to fish here.");
					client.setBusy(false);
					return true;
				}
			} else if (isFishShark(object)) {
				final int harpoon2 = hasHarpoon(client);
				if (harpoon2 == -1) {
					client.getActionAssistant().sendMessage(
							"You need a harpoon to fish here.");
					client.setBusy(false);
					return true;
				}
			} else if (isFishTuna(object)) {
				final int harpoon2 = hasHarpoon(client);
				if (harpoon2 == -1) {
					client.getActionAssistant().sendMessage(
							"You need a harpoon to fish here.");
					client.setBusy(false);
					return true;
				}	
			} else {

				// not supported
				return false;
			}

			final int level = getFishLevel(object);
			if (!(client.playerLevel[SkillConstants.FISHING] >= level)) {
				client.getActionAssistant().sendMessage(
						"You do not have the required level to fish that.");
						client.setBusy(false);
				return true;
			}

			int animationID = 0;
			if (contains(FISHING_SPOT_BAIT, object)) {
				animationID = 622;
			} else if (contains(FISHING_SPOT_HARPOON_SHARK, object)) {
				animationID = 618;
			} else if (contains(FISHING_SPOT_TUNA, object)) {
				animationID = 618;	
			}
			client.getActionAssistant().startAnimation(animationID);
			if(client.sounds == 1) {
				client.getActionAssistant().frame174(379,000,000);
			}			
			client.fishing = true;
			client.walked = false;
			client.getActionAssistant().sendMessage("You start to fish...");

			client.getActionAssistant().turnTo(x, y);
			final int fNumberOfCycles = (int) Math.random() * 5 + 10;

			EventManager.getSingleton().addEvent(client,"fishing2", new Event() {
				public int cycle = -1;

				public void execute(EventContainer c) {
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

					final int BAITSLOT = getBaitSlot(client);

					if (cycle == 1 || Math.random() > FISHING_FACTOR) {
						int xp = 0;
						if (contains(FISHING_SPOT_BAIT, object)) {
							if (client.getActionAssistant().isItemInBag(313)) {
								xp = 25;
								client.getActionAssistant().addItem(327, 1);
								client.getActionAssistant().sendMessage(
										"You catch a sardine.");
								client.getActionAssistant().deleteItem(313,
										BAITSLOT, 1);
							} else {
								client.getActionAssistant().sendMessage(
										"You need bait to fish here.");
								c.stop();
							}
						} else if (contains(FISHING_SPOT_TUNA, object)) {
							if(Misc.random(5) == 3 && client.playerLevel[SkillConstants.FISHING] >= 50){
								xp = 100;
								client.getActionAssistant().addItem(371, 1);
								client.getActionAssistant().sendMessage(
										"You catch a swordfish.");
							} else {
								xp = 80;
								client.getActionAssistant().addItem(359, 1);
								client.getActionAssistant().sendMessage(
										"You catch a tuna.");							
							}						
						} 
						else if (contains(FISHING_SPOT_HARPOON_SHARK, object)) {
							xp = 150;
							client.getActionAssistant().addItem(383, 1);
							client.getActionAssistant().sendMessage(
									"You catch a shark.");
						}
						// add xp
						client.getActionAssistant().addSkillXP(xp,
								SkillConstants.FISHING);
						client.getActionAssistant().refreshSkill(SkillConstants.FISHING);
					}
					int animationID = 0;
					if (contains(FISHING_SPOT_BAIT, object)) {
						animationID = 622;
					} else if (contains(FISHING_SPOT_HARPOON_SHARK, object)) {
						animationID = 618;
					} else if (contains(FISHING_SPOT_TUNA, object)) {
						animationID = 618;						
					}
					client.getActionAssistant().startAnimation(animationID);
					if(client.sounds == 1) {
						client.getActionAssistant().frame174(379,000,000);
					}
					cycle--;
					if (client.walked) {
						c.stop();
					}
				}

				@Override
				public void stop() {
					client.setBusy(false);
					client.getActionAssistant().startAnimation(2552);
					client.fishing = false;
				}
			}, FISHING_DELAY);
			return true;
		}

		public static int getFishLevel(int id) {
			int level = 1;
			if (contains(FISHING_SPOT_BAIT, id)) {
				level = 5;
			} else if (contains(FISHING_SPOT_HARPOON_SHARK, id)) {
				level = 76;
			} else if (contains(FISHING_SPOT_TUNA, id)) {
				level = 35;				
			}
			return level;
		}

		/**
		 * Is fishing spot Bait?
		 * 
		 * @param id
		 * @return
		 */
		public static boolean isFishBait(int id) {
			return contains(FISHING_SPOT_BAIT, id);
		}

		public static boolean checkBait(Client client) {
			for (int id : BAIT) {
				if (client.getActionAssistant().isItemInBag(id)) {
					return true;
				}

			}
			return false;
		}

		/**
		 * Is fishing spot Shark?
		 * 
		 * @param id
		 * @return
		 */
		public static boolean isFishShark(int id) {
			return contains(FISHING_SPOT_HARPOON_SHARK, id);
		}
		public static boolean isFishTuna(int id) {
			return contains(FISHING_SPOT_TUNA, id);
		}
		private static final int getBaitSlot(Client client) {
			return client.getActionAssistant().getItemSlot(313);

		}

		private static final int hasRod(Client client) {
			int ct = 0;
			for (int id : BAIT_ROD) {
				if (client.getActionAssistant().isItemInBag(id)) {
					return ct;
				}
				ct++;
			}
			return -1;
		}

		private static final int hasBait(Client client) {
			int ct = 0;
			for (int id : BAIT) {
				if (client.getActionAssistant().isItemInBag(id)) {
					return ct;
				}
				ct++;
			}
			return -1;
		}

		private static final int hasHarpoon(Client client) {
			int ct = 0;
			for (int id : HARPOON) {
				if (client.getActionAssistant().isItemInBag(id)) {
					return ct;
				}
				ct++;
			}
			return -1;
		}

	}

	/**
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