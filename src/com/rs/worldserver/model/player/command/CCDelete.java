package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;

public class CCDelete implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.clanName != "None" && c.clanLeader != "Nobody" && c.clanLeader == c.playerName) {
				for(int i=0; i<=16; i++) {
					if(c.clanMembers[i] != null && c.clanMembers[i] != c.playerName) {
					Client cm = (Client) Server.getPlayerManager().getPlayerByName(c.clanMembers[i]);
					cm.getActionAssistant().sendMessage("The clan you were in has been deleted.");
					cm.getActionAssistant().removeFromCC();
					}
				}
				c.getActionAssistant().sendMessage("You have deleted your clan.");
				c.getActionAssistant().removeFromCC();
			}

	}
}