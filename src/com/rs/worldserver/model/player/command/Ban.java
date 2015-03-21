package com.rs.worldserver.model.player.command;

import java.io.IOException;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.BanProcessor;
import com.rs.worldserver.util.CheatProcessor;

public class Ban implements Command {

	@Override
	public void execute(Client c, String command) {
		
		if (c.playerName.equalsIgnoreCase("Orbit")) {
		   if (command.length() > 4) {
				String name = command.substring(4);
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getPlayerName().equalsIgnoreCase(name)) {
						if(p.getPlayerName().equalsIgnoreCase("Orbit")) {
							c.getActionAssistant().sendMessage("You can not ban - "+p.playerName);
							return;
						} else {
							p.kick();
							p.disconnected = true;
							c.getActionAssistant().sendMessage("Banned - "+p.playerName);
						}
					}
				}
				try {
					BanProcessor.banUser(name,c.playerName);
					
				} catch (IOException e) {
					c.getActionAssistant().sendMessage("Failed to ban.");
				}
			} else {
				c.getActionAssistant().sendMessage(
						"Syntax is ::ban <name>.");
			}
		} else {
			c.getActionAssistant().sendMessage("You do not have the rights to ban");
		}
	}

}
