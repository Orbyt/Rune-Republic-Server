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

public class UnJail implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerRights > 3 && c.playerRights != 5 || CheatProcessor.checkCheat(c.playerName) ) {
			if (command.length() > 7) {
				String name = command.substring(7);
				
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getPlayerName().equalsIgnoreCase(name)) {
						if(p.jailedBy.equalsIgnoreCase(c.playerName)) {	
							Client d = (Client) p;
							if (d.getPlayerName().equalsIgnoreCase(c.playerName)) {
								c.getActionAssistant().Send("@red@You can't unjail yourself");
								break;
							}
							
							d.blackMarks = 0;
							d.jailedBy = " ";
							d.jailTimer = 0;
							d.getActionAssistant().startTeleport(3089, 3490, 0, "modern");
							d.getActionAssistant().Send("@red@You have been unjailed by "+c.playerName+".");
							c.getActionAssistant().Send("@red@You have unjailed "+d.playerName+".");							
						} else {
							Client d = (Client) p;
							d.getActionAssistant().Send("@red@You have to be Unjailed by "+c.playerName);
							c.getActionAssistant().Send("@red@Only "+d.jailedBy+" can unjail this person");
							return;
						}
					}
				}
			}

		}
	}
}
