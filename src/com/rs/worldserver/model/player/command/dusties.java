package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;


public class dusties implements Command {

	@Override
	public void execute(Client client, String command) {
		if (client.inDuelArena()) {
			client.getActionAssistant().Send("You cannot use that here.");
			return;
		}
		if (client.inWild()) {
			client.getActionAssistant().Send("You cannot use this command in the wilderness.");
			return;
		}
		
		client.inAir = false;
		client.stopMovement();
		//client.teleDelay = System.currentTimeMillis();
		client.getActionAssistant().startTeleport(3206, 9379, 0, "modern");
	}
}
