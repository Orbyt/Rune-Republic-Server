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

public class barrows implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.inDuelArena()) {
			return;
		}
if (c.inWild()) {
			c.getActionAssistant().Send("You cannot use this command in the wilderness.");
			return;
		}		
			c.getActionAssistant().startTeleport(3567, 3318, 0, "modern");

		}
	

}
