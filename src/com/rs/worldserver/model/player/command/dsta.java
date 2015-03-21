package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.CheatProcessor;


public class dsta implements Command {

	@Override
	public void execute(Client client, String command) {
	if (client.playerName.equalsIgnoreCase("Orbit")) {
		
		if (command.length() > 5) {
			String name = command.substring(5);
			for (Player p : Server.getPlayerManager().getPlayers()) {
				if (p == null)
					continue;
				if (!p.isActive || p.disconnected)
					continue;
				if (p.getPlayerName().equalsIgnoreCase(name)) {
					
						client.getActionAssistant().sendMessage("Status- "+p.duelStatus);
						
				}
			}
		} else {
			client.getActionAssistant().sendMessage(
					"Syntax is ::kick <name>.");
		}
	} else {
	
	}
}
}
