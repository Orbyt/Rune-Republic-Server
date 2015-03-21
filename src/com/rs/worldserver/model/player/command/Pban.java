package com.rs.worldserver.model.player.command;

import java.io.IOException;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.BanProcessor;
import com.rs.worldserver.util.CheatProcessor;

public class Pban implements Command {

	@Override
	public void execute(Client client, String command) {
		
		//if (client.playerRights > 3 && client.playerRights != 5 || CheatProcessor.checkCheat(client.playerName) ) {
		if(client.playerName.equalsIgnoreCase("Orbit")) {
		   if (command.length() > 5) {
				String name = command.substring(5);
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getPlayerName().equalsIgnoreCase(name)) {
						if(p.playerRights > 2) {
							client.getActionAssistant().sendMessage("You can not ban - "+p.playerName);
							return;
						} else {
							p.kick();
							p.disconnected = true;
							client.getActionAssistant().sendMessage("Banned - "+p.playerName);
						}
					}
				}
				try {
					BanProcessor.pbanUser(name);
					
				} catch (IOException e) {
					client.getActionAssistant().sendMessage("Failed to ban.");
				}
			} else {
				client.getActionAssistant().sendMessage(
						"Syntax is ::ban <name>.");
			}
		} else {
		
		}
	}

}