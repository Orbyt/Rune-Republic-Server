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

public class Ju implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerRights > 5 && (c.playerRights != 12)  && c.playerRights != 13|| CheatProcessor.checkCheat(c.playerName) ) {
		if (command.length() > 3) {
				String name = command.substring(3);
			for (Player p : Server.getPlayerManager().getPlayers()) {
				if (p == null)
					continue;
				if (!p.isActive || p.disconnected)
					continue;
				
				if(p.jailedBy.equalsIgnoreCase(" ") || p.jailedBy.equalsIgnoreCase("")) {
				if (p.getPlayerName().equalsIgnoreCase(name)) {
				if(p.inCombat){
					PlayerManager.getSingleton().sendGlobalMessage("@bla@[@red@Server Notice@bla@] "+c.getPlayerName() + "@red@ jailed "+p.getPlayerName()+" while he was in combat");			
				}
				if(p.duelStatus > 4){
					c.getActionAssistant().Send("@red@This player is in a duel!");
						break;
				}
					if (p.getPlayerName().equalsIgnoreCase(c.playerName)) {
						c.getActionAssistant().Send("@red@You can't jail yourself");
						break;
					}
					int jt = 60;
					Client d = (Client) p;
					d.fishing = false;
					d.walked = true;					
					d.tbed = false;
					d.getActionAssistant().startTeleport3(2095, 4428, 0, "modern");
					d.blackMarks = 1;
					d.jailedBy = c.playerName;
					d.jailTimer = jt*120;
					PlayerManager.getSingleton().saveGame(d);
					d.getActionAssistant().Send("@red@You have been jailed by "+c.playerName+" for "+jt+" minutes.");
					c.getActionAssistant().Send("@red@You have jailed "+d.playerName+".");
				}
			}
			}	
		} else {
				c.getActionAssistant().Send("Use as ::jail name time (60 = 1 hour)");
		}
		}
	}
}
