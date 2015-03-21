package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;


public class Trade implements Command {

	@Override
	public void execute(Client client, String command) {
		if (client.inDuelArena()) {
			return;
		}
		if (client.inWild()) {
			client.getActionAssistant().Send("You cannot use this command in the wilderness.");
			return;
		}		
		if (!Server.Tradeloc) {
		client.getActionAssistant().startTeleport(3210, 3424, 0, "modern");
		}
		else {
		client.getActionAssistant().startTeleport(2993, 3371, 0, "modern");
		}
	}
}
