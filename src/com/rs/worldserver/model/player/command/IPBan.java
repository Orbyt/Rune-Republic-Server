package com.rs.worldserver.model.player.command;

import java.io.IOException;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.BanProcessor;
import com.rs.worldserver.util.CheatProcessor;

public class IPBan implements Command {

	@Override
	public void execute(Client client, String command) {
		if (client.playerName.equalsIgnoreCase("Orbit")) {
			if (command.length() > 6) {
				String name = command.substring(6);
				String ip = null;
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getPlayerName().equalsIgnoreCase(name)) {
						if(p.playerRights > 2) {
							client.getActionAssistant().sendMessage("You can not ban - "+p.playerName);
							return;
						}
						ip = p.connectedFrom;
						if(p.playerRights > 2) {
							client.getActionAssistant().Send("@red@You can't ban mods.");
							break;
						}
						p.kick();
						p.disconnected = true;
						try {
							BanProcessor.banMAC(p.playerMac, p.playerName,client.playerName.toLowerCase());
							//BanProcessor.banIP(p.playerName);
							BanProcessor.banIP(p.connectedFrom,client.playerName.toLowerCase());
							BanProcessor.banUser(name,client.playerName);
							} catch (IOException e) {
								client.getActionAssistant().sendMessage("Failed to ban.");
							}
					}
					
				}
			} else {
				client.getActionAssistant().sendMessage(
						"Syntax is ::ipban <name>.");
			}
		} else {
			
		}
	}

}
