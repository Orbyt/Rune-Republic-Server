package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;


public class brim implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.inDuelArena()) {
			c.getActionAssistant().Send("You cannot use that here.");
			return;
		}
		if (c.inWild()) {
			c.getActionAssistant().Send("You cannot use this command in the wilderness.");
			return;
		}	
		c.inAir = false;
		c.stopMovement();
		c.getActionAssistant().startTeleport(2667, 9561, 0, "modern");
	}
}
