package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;


public class Duel implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.inDuelArena()) {
			c.getActionAssistant().Send("You are already in duel arena.");
			return;
		}
		if (c.inWild()) {
			c.getActionAssistant().Send("You cannot use this command in the wilderness.");
			return;
		}	
		
		c.inAir = false;
		c.poisontimer = 1;
		c.poisondamage = 0;
		c.stopMovement();
		c.getActionAssistant().startTeleport(3362, 3266, 0, "modern");
	}
}
