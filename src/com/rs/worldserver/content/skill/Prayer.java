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

/**
 * Prayer skill handler
 * 
 * @author Graham
 * 
 */
public class Prayer implements Skill {

	public static boolean buryBones(final Client client, int fromSlot) {
		final int buryItem = client.playerItems[fromSlot] - 1;
		int buryXP = 4;
		switch (buryItem) {
		case 532:
		case 526:
		case 3125:
		case 3127:
		case 3128:
		case 3129:
		case 3130:
		case 3131:
		case 3132:
		case 3133:
			buryXP = 15;
			break;
		case 536:
			buryXP = 72;
			break;
		case 534:
			buryXP = 30;
			break;
		case 4812:
			buryXP = 25;
			break;
		case 4830:
			buryXP = 348;
			break;
		case 4832:
			buryXP = 384;
			break;
		case 4834:
			buryXP = 560;
			break;
		default:
			return false;
		}

		final int fBuryXP = buryXP;

		if (client.checkBusy()) {
			return true;
		}
		client.resetWalkingQueue();
		client.setBusy(true);
		client.setCanWalk(false);
		client.getActionAssistant().sendMessage(
				"You dig a hole in the ground...");
		client.getActionAssistant().startAnimation(827);

		EventManager.getSingleton().addEvent(client,"prayer", new Event() {
			public void execute(EventContainer c) {
				if (client.getActionAssistant().getItemSlot((buryItem)) == -1) {
					c.stop();
					return;
				}
				// here we finally change the skill
				client.getActionAssistant().addSkillXP(fBuryXP, 5); // 5 for
																	// prayer
																	// skill
				client.getActionAssistant().deleteItem(buryItem,
						client.getActionAssistant().getItemSlot(buryItem), 1);
				client.getActionAssistant().sendMessage("You bury the bones.");
				client.setBusy(false);
				client.setCanWalk(true);
				c.stop();
			}

			public void stop() {
				client.setBusy(false);
				client.getActionAssistant().startAnimation(2552);
			}
		}, 1000);

		return true;
	}

}
