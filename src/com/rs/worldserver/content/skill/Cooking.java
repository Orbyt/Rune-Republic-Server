package com.rs.worldserver.content.skill;

import com.rs.worldserver.content.Skill;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.ActionAssistant;
import com.rs.worldserver.model.Item;
import com.rs.worldserver.Server;
import com.rs.worldserver.Server.*;


/**
 * Cooking handler
 * 
 * @author Chachi
 *   based of work from Jonas
 */
public class Cooking {

	private static final int[] COOKING_OBJECTS = {2728,114,12269,2732,3038};
	private static final int[] RAW_ITEMS = {7944,7942,2132,317,321,327,331,335,341,345,349,353,359,363,371,377,383,389,395,15270};
	private static final int[] LEVEL_REQUIREMENTS = {62,62,1,1,1,1,25,15,18,5,20,10,30,43,45,40,80,91,82,99};
	private static final int[] BURNT_ITEMS = {7943,7943,2146,323,323,369,343,343,343,357,343,357,367,367,375,381,387,393,399,15274};
	private static final int[] COOKED_ITEMS = {7946,7946,2142,315,319,325,329,333,339,347,351,355,361,365,373,379,385,391,397,15272};
	private static final int[] EXPERIENCE_TABLE = {150,150,10,30,30,40,90,70,75,50,80,60,100,130,140,120,210,217,211,275};
	private static final int[] STOP_BURNING_LEVELS = {	};
	public static final int COOKING_DELAY = 1200;

	// private static int fishId = 0;

	public static int getfishId(Client client) {
		if (client.getExtraData().get("fishId") == null) {
			return -1;
		}
		int fish = (Integer) client.getExtraData().get("fishId");
		return fish;
	}

	public static void setfishId(Client client, int id) {
		client.getExtraData().put("fishId", (Integer) id);
	}

	public static int getIntArray(int[] array1, int[] array2, int fish) {
		int a = 0;
		for (int object : array1) {
			if (object == fish) {
				return array2[a];
			}
			a++;
		}
		return -1;
	}

	public static String getStringArray(int[] array1, String[] array2, int fish) {
		int a = 0;
		for (int object : array1) {
			if (object == fish) {
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
	public static boolean isCookable(Client client, int itemSlot, int itemId, int object) {
		int useItem = itemId;
		//int usedItem = client.playerItems[slot1] - 1;
		//int object = client.playerItems[slot2] - 1;

		if (useItem == contains(RAW_ITEMS, useItem) && object == contains(COOKING_OBJECTS, object)) {
			return startCooking(client,useItem,28);
		} else
			return false;
	}

	public static boolean startCooking(final Client client,final int fish2, final int amount) {
		if (client.checkBusy()) {
			return true;
		}
		client.walked = false;
		client.setBusy(true);
		EventManager.getSingleton().addEvent(client, "cooking", new Event() {
			int amountLeft = amount;
			//int fish2 = getfishId(client);
			int uncookedFish = 0;

			public void stop() {
				client.getActionAssistant().startAnimation(2552);
				client.setBusy(false);
				client.getExtraData().remove("fishId");
			}

			public void execute(EventContainer c) {
				if (amountLeft == 0 || fish2 == -1 || client.walked) {
					client.setBusy(false);
					c.stop();
					return;
				}
				uncookedFish = getIntArray(RAW_ITEMS, COOKED_ITEMS, fish2);
					if (client.getActionAssistant().getItemAmount(fish2) > 0) {
						if (client.playerLevel[SkillConstants.COOKING] >= getIntArray(
								RAW_ITEMS, LEVEL_REQUIREMENTS, fish2)) {
							client.getActionAssistant().startAnimation(883);
							if(client.sounds == 1) {
							client.getActionAssistant().frame174(240,000,050);}
							client.getActionAssistant().deleteItem(fish2, 1);
							client.getActionAssistant().addItem(uncookedFish, 1);
							client.getActionAssistant().addSkillXP(getIntArray(COOKED_ITEMS, EXPERIENCE_TABLE, uncookedFish),SkillConstants.COOKING);
							client.getActionAssistant().refreshSkill(SkillConstants.COOKING);
							client.getActionAssistant().sendMessage("You cook the "+Server.getItemManager().getItemDefinition(uncookedFish).getName().toLowerCase()+".");
									if (uncookedFish == 15272) {
								client.achievementProgress[1] += 1;
								if (client.achievementProgress[1] == 1000) {
									client.getNRAchievements().checkSkilling(client,12);
								}
								if (client.achievementProgress[1] == 5000) {
									client.getNRAchievements().checkSkilling(client,15);
								}
							}
							amountLeft--;
							if(client.walked) {
								client.setBusy(false);
								c.stop();
								return;
							}
						} else {
							client.getActionAssistant().sendMessage(
									"You need a cooking level of"
											+ " "
											+ getIntArray(RAW_ITEMS,
													LEVEL_REQUIREMENTS,
													fish2)
											+ " to cook that fish.");
											client.setBusy(false);
							c.stop();
						}
					} else {
						client.getActionAssistant().sendMessage(
								"You don't have a fish to cook.");
						client.setBusy(false);
						c.stop();
					}
			}
		}, COOKING_DELAY);
		return true;
	}
	
	
}	
