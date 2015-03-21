package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;

public class GiftDps implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerRights >= 0) {
			String[] args = command.split(" ");
			if (args.length == 3) {
				if (c.dPoints < Integer.parseInt(args[2])) {
					c.getActionAssistant().sendMessage("You dont have enough donator points!");
					return;
				}
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getPlayerName().equalsIgnoreCase(args[1])) {
						Client d = (Client) p;
						d.dPoints = d.dPoints + Integer.parseInt(args[2]);
						c.dPoints = c.dPoints - Integer.parseInt(args[2]);
						d.getActionAssistant().Send("@dre@" + c.playerName + " has Gifted you " + Integer.parseInt(args[2]) + " Donator Points!");
						c.getActionAssistant().sendMessage("@dre@You have gifted " + d.playerName + " " + Integer.parseInt(args[2]) + " Donator Points!");
					}	
				}
			} else {
				c.getActionAssistant().sendMessage("Syntax is ::GiftDps [name] [amount]");
			}
		}
	}
}