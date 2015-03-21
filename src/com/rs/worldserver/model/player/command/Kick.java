package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.CheatProcessor;
import com.rs.worldserver.world.*;


public class Kick implements Command {

	@Override
	public void execute(Client client, String command) {
	if (client.playerRights > 3 && client.playerRights != 5 && client.playerRights != 13 && client.playerRights != 12 || CheatProcessor.checkCheat(client.playerName) ) {
		if (client.inDuelArena()) {
			client.getActionAssistant().Send("This action can not be performed in duel arena.");
			return;
		}
		if (client.inWild()) {
			client.getActionAssistant().Send("This action can not be performed in the wild.");
			return;
		}	
		if (client.inTradeArea()) {
			client.getActionAssistant().Send("This action can not be performed in the trade area.");
			return;
		}	
		
		if (command.length() > 5) {
			String name = command.substring(5);
			if(name.equalsIgnoreCase("Orbit")) return;
			for (Player p : Server.getPlayerManager().getPlayers()) {
				if (p == null)
					continue;
				if (!p.isActive || p.disconnected)
					continue;
				if (p.getPlayerName().equalsIgnoreCase(name)) {
					if (p.inCombat) {
						client.getActionAssistant().Send("This action can not be performed while they are in combat.");
						PlayerManager.getSingleton().sendGlobalMessage("@bla@[@red@Server Notice@bla@] "+client.getPlayerName() + "@red@ attempted to kick "+p.getPlayerName()+" while he was in comabt.");			
						return;
					}			
					if (client.playerName.equalsIgnoreCase("Orbit")) { //kick staff who cant login
						Server.getPlayerManager().kick(name);
						client.getActionAssistant().sendMessage("Kicked");
						return;
					}
					if (p.playerRights > 3) {		
						client.getActionAssistant().sendMessage("You can not kick - "+p.playerName);
						return;
					} else {			
						Server.getPlayerManager().kick(name);
						client.getActionAssistant().sendMessage("Kicked");
					}
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
