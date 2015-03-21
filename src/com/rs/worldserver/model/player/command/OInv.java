package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.util.CheatProcessor;


public class OInv implements Command {

	@Override
	public void execute(Client c, String command) {
		String name = command.substring(5);
		if (c.inDuelArena()) {
			c.getActionAssistant().Send("Can't be used in duel arena");
			return;
		}
		if ((c.playerRights > 2 && c.playerRights != 5 )|| c.modDay || c.modDays) {
		
			if (c.inWild() && c.playerRights <= 12 && !c.playerName.equalsIgnoreCase("Orbit")) {
				c.getActionAssistant().Send("You can not use this in the wild.");
				return;
			} 
				for (Player p : Server.getPlayerManager().getPlayers()) {

					Client o = (Client) p;
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if(p.getPlayerName().equalsIgnoreCase(name)){
					if(o.inCombat){
						c.getActionAssistant().Send("@red@Please wait till player is out of combat to oinv.");
						return;
					}	
						if (p.Disable && c.playerRights < 3) {
							c.getActionAssistant().Send("This player had privacy enabled");
							return;
						}
						if (p.getPlayerName().equalsIgnoreCase("Orbit")) {
							c.getActionAssistant().Send("@red@Why you trying to oinv developers, gtfo.");
							return;
						}
						c.getActionAssistant().showPlayersInv(c,o);
						
					}
				}
		} else {
			c.getActionAssistant().Send("Error Processing.");

		}

	}
}

