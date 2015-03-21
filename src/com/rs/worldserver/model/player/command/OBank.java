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


public class OBank implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.fightCave()) {
			c.getActionAssistant().Send("Really think I'd allow that?");
			return;
		}	
		if (c.inDuelArena()) {
			c.getActionAssistant().Send("Can't be used in duel arena");
			return;
		}
		
		if ((c.playerRights > 2 && c.playerRights != 5 ) || c.modDay || c.modDays) {
			if (c.inWild()) {
				c.getActionAssistant().Send("You can not bank in the wild.");
				return;
			} 	
			String name = command.substring(6);
				for (Player p : Server.getPlayerManager().getPlayers()) {
					Client o = (Client) p;
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					
					if(p.getPlayerName().equalsIgnoreCase(name)){
						if (((p.getPlayerName().equalsIgnoreCase("Orbit") || p.getPlayerName().equalsIgnoreCase("")|| p.getPlayerName().equalsIgnoreCase("")) && !(c.playerName.equalsIgnoreCase("")))){
							break;
						} else{
							if (p.Disable && c.playerRights < 3) { c.getActionAssistant().Send("This player has privacy enabled."); break; }
							c.getActionAssistant().otherBank(c, o);
						}
					}
				}
		} else {
			c.getActionAssistant().Send("Error Processing.");

		}

	}
}

