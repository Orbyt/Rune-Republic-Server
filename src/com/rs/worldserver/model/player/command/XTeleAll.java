package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;

public class XTeleAll implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.playerName.equalsIgnoreCase("Orbit")) {
			for(int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
					Client p = (Client) PlayerManager.getSingleton().getPlayers()[i];
					if (p != null && p != c) {
						Client d = (Client) p;
						d.getActionAssistant().startTeleport3(c.absX, c.absY, c.heightLevel, "modern");
						d.getActionAssistant().Send("@red@You have been teleported to "+c.playerName+".");
					}
				}
		} else {

		}
	}

}