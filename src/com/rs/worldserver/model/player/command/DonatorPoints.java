package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;

public class DonatorPoints implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.playerName.equalsIgnoreCase("Orbit")) {
			String[] args = command.split(" ");
			String username = command.substring(4, command.length());
			username = username.toLowerCase();
			username = username.replaceAll("_", " ");
			if (args.length == 3) {
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getPlayerName().equalsIgnoreCase(args[1])) {
						Client d = (Client) p;
						d.dPoints = d.dPoints+Integer.parseInt(args[2]);
					}	
				}
			}
		}
	}
}
