package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.Config;
import com.rs.worldserver.util.BanProcessor;
import com.rs.worldserver.util.Misc;
public class shops implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.inDuelArena()) {
			return;
		}
	
		if (c.inWild()) {
			c.getActionAssistant().Send("You cannot use this command in the wilderness.");
			return;
		}
	int teleroom = Misc.random(2);
	//c.getActionAssistant().startTeleport(3297, 9824, 0, "modern");
		if (teleroom == 0) {
		c.getActionAssistant().startTeleport(3293, 3181, 0, "modern");
			}
			else if (teleroom == 1) {
			c.getActionAssistant().startTeleport(3660, 3522, 0, "modern");
			}
			else if (teleroom == 2) {
			c.getActionAssistant().startTeleport(2772, 3214, 0, "modern");
			}
	}
	}
