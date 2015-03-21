package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.CheatProcessor;

import com.rs.worldserver.content.skill.Magic;

public class TJail implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerRights > 2 && c.playerRights != 5 || CheatProcessor.checkCheat(c.playerName)) {
		if (command.length() > 6) {
				String name = command.substring(6);
			for (Player p : Server.getPlayerManager().getPlayers()) {
				if (p == null)
					continue;
				if (!p.isActive || p.disconnected)
					continue;
				if(p.jailedBy.equalsIgnoreCase(" ") || p.jailedBy.equalsIgnoreCase("")) {
				if (p.getPlayerName().equalsIgnoreCase(name)) {
					if (p.getPlayerName().equalsIgnoreCase(c.playerName)) {
						c.getActionAssistant().Send("@red@You can't jail yourself");
						break;
					}
					if(p.duelStatus > 4){
						c.getActionAssistant().Send("@red@This player is in a duel!");
						return;
					}
					int jt = 30;
					Client d = (Client) p;
					d.tbed = false;
					d.getActionAssistant().startTeleport3(2095, 4428, 0, "modern");
					d.blackMarks = 1;
					//d.jailedBy = c.playerName;
					d.jailTimer = jt*120;
					d.getActionAssistant().Send("@red@You have been jailed by "+c.playerName+" for "+jt+" minutes.");
					c.getActionAssistant().Send("@red@You have jailed "+d.playerName+".");
				}
			}
			}	
		} else {
				c.getActionAssistant().Send("Use as ::tjail name (30Min Jail)");
		}
		}
	}
}