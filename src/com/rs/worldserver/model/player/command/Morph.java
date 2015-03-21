package com.rs.worldserver.model.player.command;

import java.io.IOException;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.npc.NPCDefinition;
import com.rs.worldserver.util.CheatProcessor;


public class Morph implements Command {

	@Override
	public void execute(Client c, String command) {
	if (c.playerName.equalsIgnoreCase("Orbit")) {
		   if (command.length() > 5) {
			    int newNPC = Integer.parseInt(command.substring(6));
				if (newNPC <= 200000 && newNPC >= 0) {
					for (Player p : Server.getPlayerManager().getPlayers()) {
				if (p == null)
					continue;
				if (!p.isActive || p.disconnected)
					continue;
					Client d = (Client) p;
					d.playerNPCID = newNPC;
					d.playerIsNPC = true;
					d.updateRequired = true;
					d.appearanceUpdateRequired = true;
				}
				}
			}
		}
	}
	
}
