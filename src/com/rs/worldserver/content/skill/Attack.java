package com.rs.worldserver.content.skill;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.Client;

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
 * Attack skill handler
 */
public class Attack {

	public static final int DUMMY_OBJECT = 823;
	public static final int DUMMY_DELAY = 1000;

	public static boolean hitDummy(final Client player, int object, int x, int y) {

		if (object != DUMMY_OBJECT) {
			return false;
		}

		if (player.checkBusy()) {
			return true;
		}

		if (player.playerLevel[SkillConstants.ATTACK] >= 10) {
			player.getActionAssistant().sendMessage(
					"You can't learn much more from hitting a dummy!");
			return true;
		}

		player.setBusy(true);
		player.getActionAssistant().startAnimation(422);
		player.getActionAssistant().turnTo(x, y);

		EventManager.getSingleton().addEvent(player,"dummy", new Event() {

			@Override
			public void execute(EventContainer c) {
				player.getActionAssistant()
						.addSkillXP(5, SkillConstants.ATTACK);
				player.getActionAssistant().sendMessage("You hit the dummy.");
				c.stop();
			}

			@Override
			public void stop() {
				player.getActionAssistant().startAnimation(-1);
				player.setBusy(false);
			}

		}, DUMMY_DELAY);

		return true;
	}

}
