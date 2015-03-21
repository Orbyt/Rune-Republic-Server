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

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;

/**
 * Fletching skill handler
 * 
 * @author Jonas
 * 
 */
public class Fletching {

	public static final int[] LOGS = { 1511, 1521, 1519, 1517, 1515, 1513 };
	public static final int[] UNSTRUNG_BOWS = { 50, 48, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72 };
	public static final int[] STRUNG_BOWS = { 841, 839, 843, 845, 847, 849,	851, 853, 857, 855, 861, 859 };
	public static final int[] FLETCHING_LEVELS = { 5, 10, 20, 25, 40, 35, 55, 50, 65, 70, 80, 85 };
	public static final int[] EXPERIENCE = { 10, 20, 30, 50, 40, 60, 80, 70, 100, 90, 120, 110 };

	public static final int[] ARROWS = { 882, 884, 886, 888, 890, 892 };
	public static final int[] ARROW_HEADS = { 39, 40, 41, 42, 43, 44 };
	public static final int[] ARROW_LEVELS = { 1, 15, 30, 45, 60, 75 };
	public static final int[] ARROW_EXPERIENCE = { 10, 20, 30, 40, 50, 60 };

	public static final int[] DARTS = { 806, 807, 808, 809, 810, 811 };
	public static final int[] DART_TIPS = { 819, 820, 821, 822, 823, 824 };
	public static final int[] DART_LEVELS = { 1, 22, 37, 52, 67, 81 };
	public static final int[] DART_EXPERIENCE = { 10, 20, 30, 40, 50, 60 };

	public static final int[] LEFT_ITEM = { 50, 54, 60, 64, 68, 72 };
	public static final int[] RIGHT_ITEM = { 48, 56, 58, 62, 66, 70 };
	public static String[] LEFT_ITEM_NAME = { "Longbow(u)", "Oak Longbow(u)",
			"Willow Longbow(u)", "Maple Longbow(u)", "Yew Longbow(u)",
			"Magic Longbow(u)" };
	public static String[] RIGHT_ITEM_NAME = { "Shortbow(u)",
			"Oak Shortbow(u)", "Willow Shortbow(u)", "Maple Shortbow(u)",
			"Yew Shortbow(u)", "Magic Shortbow(u)" };

	public static final int FLETCHING_DELAY = 2500;

	// private static int logId = 0;

	public static int getLogId(Client client) {
		if (client.getExtraData().get("logId") == null) {
			return -1;
		}
		int log = (Integer) client.getExtraData().get("logId");
		return log;
	}

	public static void setLogId(Client client, int id) {
		client.getExtraData().put("logId", (Integer) id);
	}

	public static int getIntArray(int[] array1, int[] array2, int bow) {
		int a = 0;
		for (int object : array1) {
			if (object == bow) {
				return array2[a];
			}
			a++;
		}
		return -1;
	}

	public static String getStringArray(int[] array1, String[] array2, int bow) {
		int a = 0;
		for (int object : array1) {
			if (object == bow) {
				return array2[a];
			}
			a++;
		}
		return "";
	}

	public static int contains(int[] array, int value) {
		for (int i : array) {
			if (i == value)
				return value;
		}
		return 0;
	}

	public static boolean isFletchable(Client client, int slot1, int slot2) {
		int useItem = client.playerItems[slot1] - 1;
		int usedItem = client.playerItems[slot2] - 1;

		if (useItem == 946 && usedItem == contains(LOGS, usedItem)
				|| useItem == contains(LOGS, useItem) && usedItem == 946) {
			return chooseItem(client,(useItem == contains(LOGS, useItem) ? useItem : usedItem));
		} else if (useItem == 1777
				&& usedItem == contains(UNSTRUNG_BOWS, usedItem)
				|| useItem == contains(UNSTRUNG_BOWS, useItem)
				&& usedItem == 1777) {
			return stringBow(client, (useItem == contains(UNSTRUNG_BOWS,
					useItem) ? useItem : usedItem));
		} else if (useItem == 52 && usedItem == 314 || useItem == 314
				&& usedItem == 52) {
			return createHeadlessArrows(client, 52,314);
		} else if (useItem == 53 && usedItem == contains(ARROW_HEADS, usedItem)
				|| useItem == contains(ARROW_HEADS, useItem) && usedItem == 53) {
			return createArrows(client, (useItem == contains(ARROW_HEADS,
					useItem) ? useItem : usedItem));
		} else if (useItem == 314 && usedItem == contains(DART_TIPS, usedItem)
				|| useItem == contains(DART_TIPS, useItem) && usedItem == 314) {
			return createDarts(client,
					(useItem == contains(DART_TIPS, useItem) ? useItem
							: usedItem));
		} else
			return false;
	}

	public static boolean createHeadlessArrows(Client client, int item, int item2) {

		if (client.checkBusy()) {
			return true;
		}

		int amount = client.getActionAssistant().getItemAmount(item);
		int amount2 = client.getActionAssistant().getItemAmount(item2);
		if (client.getActionAssistant().freeSlots() >= 1) {
			if(amount2 >= 15 && amount >= 15){
				client.getActionAssistant().deleteItem(item2,15);
				client.getActionAssistant().deleteItem(item,15);
				client.getActionAssistant().addItem(53, 15);
				client.getActionAssistant().addSkillXP(10,SkillConstants.FLETCHING);
				client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
				client.getActionAssistant().sendMessage("You make some headless arrows.");
			
			}
			if(amount2 < 15 && amount >= 15){
				client.getActionAssistant().deleteItem(item2,amount2);
				client.getActionAssistant().deleteItem(item,amount2);
				client.getActionAssistant().addItem(53, amount2);
				client.getActionAssistant().addSkillXP(10,SkillConstants.FLETCHING);
				client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
				client.getActionAssistant().sendMessage("You make some headless arrows.");
			}
			if(amount2 >= 15 && amount < 15){			
				client.getActionAssistant().deleteItem(item2,amount);
				client.getActionAssistant().deleteItem(item,amount);
				client.getActionAssistant().addItem(53, amount);
				client.getActionAssistant().addSkillXP(10,SkillConstants.FLETCHING);
				client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
				client.getActionAssistant().sendMessage("You make some headless arrows.");
			}
			if(amount2 < 15 && amount < 15){
				if(amount2 >= amount){
					client.getActionAssistant().deleteItem(item2,amount);
					client.getActionAssistant().deleteItem(item,amount);
					client.getActionAssistant().addItem(53, amount);
					client.getActionAssistant().addSkillXP(10,SkillConstants.FLETCHING);
					client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
					client.getActionAssistant().sendMessage("You make some headless arrows.");
				}
				if(amount2 <= amount){
					client.getActionAssistant().deleteItem(item2,amount2);
					client.getActionAssistant().deleteItem(item,amount2);
					client.getActionAssistant().addItem(53, amount2);
					client.getActionAssistant().addSkillXP(10,SkillConstants.FLETCHING);
					client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
					client.getActionAssistant().sendMessage("You make some headless arrows.");
				}		
			}
		} else {
			client.getActionAssistant().sendMessage(
					"You have no space in your inventory");
		}
		return true;
	}

	public static boolean createDarts(Client client, int item) {

		if (client.checkBusy()) {
			return true;
		}

		int amount = client.getActionAssistant().getItemAmount(item);
		int amount2 = client.getActionAssistant().getItemAmount(314);
		if (client.getActionAssistant().freeSlots() >= 1) {
			if (client.playerLevel[SkillConstants.FLETCHING] >= getIntArray(DART_TIPS, DART_LEVELS, item)) {
			if(amount2 >= 15 && amount >= 15){
				client.getActionAssistant().deleteItem(314,15);
				client.getActionAssistant().deleteItem(item,15);
				client.getActionAssistant().addItem(getIntArray(DART_TIPS, DARTS, item), 15);
				client.getActionAssistant().addSkillXP(getIntArray(DART_TIPS, DART_EXPERIENCE, item),
						SkillConstants.FLETCHING);
				client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
				client.getActionAssistant().sendMessage("You make some darts.");
			
			}
			if(amount2 < 15 && amount >= 15){
				client.getActionAssistant().deleteItem(314,amount2);
				client.getActionAssistant().deleteItem(item,amount2);
				client.getActionAssistant().addItem(getIntArray(DART_TIPS, DARTS, item), amount2);
				client.getActionAssistant().addSkillXP(10,SkillConstants.FLETCHING);
				client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
				client.getActionAssistant().sendMessage("You make some darts.");
			}
			if(amount2 >= 15 && amount < 15){			
				client.getActionAssistant().deleteItem(314,amount);
				client.getActionAssistant().deleteItem(item,amount);
				client.getActionAssistant().addItem(getIntArray(DART_TIPS, DARTS, item), amount);
				client.getActionAssistant().addSkillXP(getIntArray(DART_TIPS, DART_EXPERIENCE, item),
						SkillConstants.FLETCHING);
				client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
				client.getActionAssistant().sendMessage("You make some darts.");
			}
			if(amount2 < 15 && amount < 15){
				if(amount2 >= amount){
					client.getActionAssistant().deleteItem(314,amount);
					client.getActionAssistant().deleteItem(item,amount);
					client.getActionAssistant().addItem(getIntArray(DART_TIPS, DARTS, item), amount);
					client.getActionAssistant().addSkillXP(getIntArray(DART_TIPS, DART_EXPERIENCE, item),
						SkillConstants.FLETCHING);
					client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
					client.getActionAssistant().sendMessage("You make some darts.");
				}
				if(amount2 <= amount){
					client.getActionAssistant().deleteItem(314,amount2);
					client.getActionAssistant().deleteItem(item,amount2);
					client.getActionAssistant().addItem(getIntArray(DART_TIPS, DARTS, item), amount2);
					client.getActionAssistant().addSkillXP(getIntArray(DART_TIPS, DART_EXPERIENCE, item),
						SkillConstants.FLETCHING);
					client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
					client.getActionAssistant().sendMessage("You make some darts.");
				}		
			}			
			
				client.getActionAssistant().deleteItem(314,
						amount > 15 ? 15 : amount);
				client.getActionAssistant().deleteItem(item,
						amount > 15 ? 15 : amount);
				client.getActionAssistant().addItem(
						getIntArray(DART_TIPS, DARTS, item),
						amount > 15 ? 15 : amount);
				client.getActionAssistant().addSkillXP(
						getIntArray(DART_TIPS, DART_EXPERIENCE, item),
						SkillConstants.FLETCHING);
						client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
				client.getActionAssistant().sendMessage("You make some darts.");
			} else {
				client.getActionAssistant().sendMessage(
						"You need a fletching level of" + " "
								+ getIntArray(DART_TIPS, DART_LEVELS, item)
								+ " to make these darts.");
			}
		} else {
			client.getActionAssistant().sendMessage(
					"You have no space in your inventory");
		}
		return true;
	}

	public static boolean createArrows(Client client, int item) {

		if (client.checkBusy()) {
			return true;
		}

		int amount = client.getActionAssistant().getItemAmount(item);
		int amount2 = client.getActionAssistant().getItemAmount(53);
		if (client.getActionAssistant().freeSlots() >= 1) {
			if (client.playerLevel[SkillConstants.FLETCHING] >= getIntArray(ARROW_HEADS, ARROW_LEVELS, item)) {
				if(amount2 >= 15 && amount >= 15){
					client.getActionAssistant().deleteItem(53,15);
					client.getActionAssistant().deleteItem(item,15);
					client.getActionAssistant().addItem(getIntArray(ARROW_HEADS, ARROWS, item), 15);
					client.getActionAssistant().addSkillXP(
						getIntArray(ARROW_HEADS, ARROW_EXPERIENCE, item),
						SkillConstants.FLETCHING);
					client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
					client.getActionAssistant().sendMessage("You make some arrows.");
				
				}
				if(amount2 < 15 && amount >= 15){
					client.getActionAssistant().deleteItem(53,amount2);
					client.getActionAssistant().deleteItem(item,amount2);
					client.getActionAssistant().addItem(getIntArray(ARROW_HEADS, ARROWS, item), amount2);
					client.getActionAssistant().addSkillXP(
						getIntArray(ARROW_HEADS, ARROW_EXPERIENCE, item),
						SkillConstants.FLETCHING);
					client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
					client.getActionAssistant().sendMessage("You make some arrows.");
				}
				if(amount2 >= 15 && amount < 15){			
					client.getActionAssistant().deleteItem(53,amount);
					client.getActionAssistant().deleteItem(item,amount);
					client.getActionAssistant().addItem(getIntArray(ARROW_HEADS, ARROWS, item), amount);
					client.getActionAssistant().addSkillXP(
						getIntArray(ARROW_HEADS, ARROW_EXPERIENCE, item),
						SkillConstants.FLETCHING);
					client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
					client.getActionAssistant().sendMessage("You make some arrows.");
				}
				if(amount2 < 15 && amount < 15){
					if(amount2 >= amount){
						client.getActionAssistant().deleteItem(53,amount);
						client.getActionAssistant().deleteItem(item,amount);
						client.getActionAssistant().addItem(getIntArray(ARROW_HEADS, ARROWS, item), amount);
						client.getActionAssistant().addSkillXP(
						getIntArray(ARROW_HEADS, ARROW_EXPERIENCE, item),
						SkillConstants.FLETCHING);
						client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
						client.getActionAssistant().sendMessage("You make some arrows.");
					}
					if(amount2 <= amount){
						client.getActionAssistant().deleteItem(53,amount2);
						client.getActionAssistant().deleteItem(item,amount2);
						client.getActionAssistant().addItem(getIntArray(ARROW_HEADS, ARROWS, item), amount2);
						client.getActionAssistant().addSkillXP(
						getIntArray(ARROW_HEADS, ARROW_EXPERIENCE, item),
						SkillConstants.FLETCHING);
						client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
						client.getActionAssistant().sendMessage("You make some arrows.");
					}		
				}			
			} else {
				client.getActionAssistant().sendMessage(
						"You need a fletching level of" + " "
								+ getIntArray(ARROW_HEADS, ARROW_LEVELS, item)
								+ " to make these arrows.");
			}
		} else {
			client.getActionAssistant().sendMessage(
					"You have no space in your inventory");
		}
		return true;
	}

	public static boolean stringBow(Client client, int bow) {

		if (client.checkBusy()) {
			return true;
		}

		if (client.playerLevel[SkillConstants.FLETCHING] >= getIntArray(
				UNSTRUNG_BOWS, FLETCHING_LEVELS, bow)) {
			client.getActionAssistant().deleteItem(bow, 1);
			client.getActionAssistant().deleteItem(1777, 1);
			client.getActionAssistant().addItem(
					getIntArray(UNSTRUNG_BOWS, STRUNG_BOWS, bow), 1);
			client.getActionAssistant().sendMessage(
					"You attach the bowstring to the bow.");
			client.getActionAssistant().addSkillXP(getIntArray(UNSTRUNG_BOWS, EXPERIENCE,bow),
									SkillConstants.FLETCHING);
									client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);					
		} else {
			client.getActionAssistant().sendMessage(
					"You need a fletching level of" + " "
							+ getIntArray(UNSTRUNG_BOWS, FLETCHING_LEVELS, bow)
							+ " to string that bow.");
		}
		return true;
	}

	public static boolean chooseItem(Client client, int log) {

		if (client.checkBusy()) {
			return true;
		}

		client.getActionAssistant().removeAllWindows();
		client.getActionAssistant().sendFrame164(8880);

		client.getActionAssistant().sendFrame246(8883, 200,
				getIntArray(LOGS, LEFT_ITEM, log));
		client.getActionAssistant().sendFrame246(8884, 200,
				(log == 1511 ? 52 : -1));
		client.getActionAssistant().sendFrame246(8885, 200,
				getIntArray(LOGS, RIGHT_ITEM, log));

		client.getActionAssistant().sendQuest(
				getStringArray(LOGS, LEFT_ITEM_NAME, log), 8897);
		client.getActionAssistant().sendQuest(log == 1511 ? "Arrow Shaft" : "",
				8893);
		client.getActionAssistant().sendQuest(
				getStringArray(LOGS, RIGHT_ITEM_NAME, log), 8889);
		setLogId(client, log);
		return true;
	}

	public static boolean startFletching(final Client client, final int amount,
			final String length) {
		if (client.checkBusy()) {
			return true;
		}
		client.setBusy(true);
		client.getActionAssistant().startAnimation(1248);
		if(client.sounds == 1) {
		client.getActionAssistant().frame174(375,000,010);}
		client.walked=false;
		EventManager.getSingleton().addEvent(client,"fletching", new Event() {
			int amountLeft = amount;
			int log2 = getLogId(client);
			int unstrungBow = 0;

			public void stop() {
				client.getActionAssistant().startAnimation(2552);
				client.setBusy(false);
				client.getExtraData().remove("logId");
			}

			public void execute(EventContainer c) {
				if (amountLeft == 0 || log2 == -1 || client.walked) {
					c.stop();
					return;
				}

				if (length == "short") {
					unstrungBow = getIntArray(LOGS, LEFT_ITEM, log2);
				} else if (length == "long") {
					unstrungBow = getIntArray(LOGS, RIGHT_ITEM, log2);
				} else {
					unstrungBow = 52;
				}

				if (client.getActionAssistant().freeSlots() >= 0) {
					if (client.getActionAssistant().getItemAmount(log2) > 0) {
						if (client.playerLevel[SkillConstants.FLETCHING] >= getIntArray(
								UNSTRUNG_BOWS, FLETCHING_LEVELS, unstrungBow)) {

							client.getActionAssistant().deleteItem(log2, 1);
							client.getActionAssistant().addItem(
									unstrungBow == 52 ? 52 : unstrungBow,
									unstrungBow == 52 ? 15 : 1);
							client.getActionAssistant().addSkillXP(
									unstrungBow == 52 ? 50 : getIntArray(
											UNSTRUNG_BOWS, EXPERIENCE,
											unstrungBow),
									SkillConstants.FLETCHING);
									client.getActionAssistant().refreshSkill(SkillConstants.FLETCHING);
							client.getActionAssistant().sendMessage(
									"You fletch the bow.");
							amountLeft--;

						} else {
							client.getActionAssistant().sendMessage(
									"You need a fletching level of"
											+ " "
											+ getIntArray(UNSTRUNG_BOWS,
													FLETCHING_LEVELS,
													unstrungBow)
											+ " to fletch that bow.");
							c.stop();
						}
					} else {
						client.getActionAssistant().sendMessage(
								"You don't have the item to fletch");
						c.stop();
					}
				} else {
					client.getActionAssistant().sendMessage(
							"You have no space in your inventory");
					c.stop();
				}
			}
		}, FLETCHING_DELAY);
		return true;
	}

}
