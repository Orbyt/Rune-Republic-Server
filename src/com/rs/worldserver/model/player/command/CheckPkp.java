package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;

public class CheckPkp implements Command {

	@Override
	public void execute(Client c, String command) {
		if ((c.playerRights > 2) &&  (c.playerRights != 12) && c.playerRights != 13) {
		if (command.length() > 9) {
			String name = command.substring(9);
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getPlayerName().equalsIgnoreCase(name)) {
						Client d = (Client) p;
						c.getActionAssistant().Send("@dre@" + d.playerName +" has " + d.pkpoints + " points.");
					}	
				}
			}
		} else {
			c.getActionAssistant().sendMessage(
					"You do not have rights for this command.");}
	}	
}