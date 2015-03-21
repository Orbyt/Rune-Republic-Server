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


public class Uj implements Command {

	@Override
	public void execute(Client c, String command) {
	
		if (c.playerRights > 5 && (c.playerRights != 12)  && c.playerRights != 13 || CheatProcessor.checkCheat(c.playerName) ) {
			if (command.length() > 3) {
				String name = command.substring(3);
				
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getPlayerName().equalsIgnoreCase(name)) {
							Client d = (Client) p;
							if (!c.playerName.equalsIgnoreCase("Orbit")) {							
							if (d.getPlayerName().equalsIgnoreCase(c.playerName)) {
								c.getActionAssistant().Send("@red@You can't unjail yourself.");
								break;
							}
							}
							if (d.jailTimer <= 0 && !c.inJail()) {
								c.getActionAssistant().Send("@red@You can't unjail someone who isn't jailed.");
								break;
							}
							d.blackMarks = 0;
							d.jailedBy = " ";
							d.jailTimer = 0;
							d.getActionAssistant().startTeleport(3089, 3490, 0, "modern");
							d.getActionAssistant().Send("@red@You have been unjailed by "+c.playerName+".");
							c.getActionAssistant().Send("@red@You have unjailed "+d.playerName+".");							
						}
				}
			}

		}
	}
}