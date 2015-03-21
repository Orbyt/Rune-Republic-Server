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

public class Gwd implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.inDuelArena()) {
			return;
		}
		if(c.playerRights >= 0) {
		if (c.inWild()) {
			c.getActionAssistant().Send("You cannot use this command in the wilderness.");
			return;
		}	
		if (c.absX >= 2800 && c.absX <= 2950 && c.absY >= 5245 && c.absY <= 5385) {
		c.getActionAssistant().Send("You are already in godwars!");
		}
		else {
			c.getActionAssistant().startTeleport(2881, 5310, 2, "modern");
		}
		}
	}

}
