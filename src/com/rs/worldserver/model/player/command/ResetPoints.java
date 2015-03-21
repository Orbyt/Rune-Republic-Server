package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;

public class ResetPoints implements Command {

	@Override
	public void execute(Client c, String command) {
		if ((c.playerRights > 3) &&  (c.playerRights != 12) && c.playerRights != 13) {
		if (command.length() > 12) {
			String name = command.substring(12);
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getPlayerName().equalsIgnoreCase(name)) {
						Client d = (Client) p;
						c.getActionAssistant().Send("@red@" + d.playerName +" had " +d.pkpoints + " points");
						d.pkpoints = 0;
						d.getActionAssistant().Send("@red@Your Pk Points have been reset. DON'T CHEAT.");
						c.getActionAssistant().Send("@red@Pk Points for " + d.playerName +" have been reset");
					}	
				}
			}
		} else {
			c.getActionAssistant().sendMessage(
					"You do not have rights for this command.");}
	}	
}