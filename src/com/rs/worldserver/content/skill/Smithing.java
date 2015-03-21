package com.rs.worldserver.content.skill;

import com.rs.worldserver.Server;
import com.rs.worldserver.content.Skill;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.object.GameObject;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;
import com.rs.worldserver.util.Misc;

/**
 * Smithing skill handler
 * 
 * @author Chachi
 * 
 */
public class Smithing implements Skill {

	private static final int[] ORES = { 436, 438, 440, 442, 444, 447, 449, 451, 453};
	private static final int[] BARS = { 2349, 2351, 2353, 2355, 2357, 2359, 2361, 2363};
	private static final String[] ROCKS_ORES_NAMES = { "copper", "tin",	"iron", "silver", "coal", "gold", "mithril","adamant", "runite" };
	private static final int[] BAR_EXP = { 5, 5, 5, 10, 20, 30, 40, 50, 60};
	private static final int[] BAR_REQUIRED_LEVELS = { 1, 1, 10, 10, 30, 40, 55, 70, 85};
	private static final boolean COAL_NEEDED = false;
	public static final int	SMELT_DELAY = 2100;
/**
* Iron = 0
* Steel = 2
* Mith = 4 ORE ID 447
* Addy = 6 ORE ID 449
* Rune = 8 ORE ID 451
*/

	public static void smelt(final Client player, final int newItem, final int delItem, final int xpGain, final int req) {
			if (player.playerLevel[13] < req) {
			player.getActionAssistant().Send("You need "+req+" or higher Smithing to smelt this item.");
			return;
			}
			player.getActionAssistant().startAnimation(0x383);
			player.getActionAssistant().deleteItem(delItem, player.getActionAssistant().getItemSlot(delItem), 1);
			if (player.checkBusy()) {
				return;
			}
			
			player.setBusy(true);
			EventManager.getSingleton().addEvent(player,"smithing", new Event() {

			@Override
			public void execute(EventContainer c) {
				if (Misc.random(3) == 1) {
					player.getActionAssistant().sendMessage("You failed..");
				} else {
					player.getActionAssistant().Send("You make a "+Server.getItemManager().getItemDefinition(newItem).getName().toLowerCase()+".");
					player.getActionAssistant().addSkillXP(xpGain, 13);
					player.getActionAssistant().addItem(newItem, 1);	
					player.getActionAssistant().refreshSkill(13);
				}
				c.stop();
			}

			@Override
			public void stop() {
				player.getActionAssistant().startAnimation(2552);
				player.setBusy(false);

			}

		}, SMELT_DELAY);
	}
}