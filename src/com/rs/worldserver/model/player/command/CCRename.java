package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;

public class CCRename implements Command {

	@Override
	public void execute(Client c, String command) {
	if(command.length() == 8 ||command.length() == 9 ) {
		c.getActionAssistant().sendMessage("Enter a new name!");
		return;
	}
	if(command.length() <= 24) {
		if(!c.clanName.equalsIgnoreCase("none") && !c.clanLeader.equalsIgnoreCase("nobody") && c.clanLeader == c.playerName) {
			c.clanName = command.substring(9);
			for(int i=0; i<=16; i++) {
				if(c.clanMembers[i] != null && c.clanMembers[i] != c.playerName) {
					Client cmem = (Client) Server.getPlayerManager().getPlayerByName(c.clanMembers[i]);
					cmem.clanName = c.clanName;
					cmem.getActionAssistant().sendMessage("The clan you're in has been renamed to "+c.clanName+".");
					cmem.getActionAssistant().updateCCMenu();
				}
			}
			c.getActionAssistant().updateCCMenu();
			c.getActionAssistant().sendMessage("Your clan has been renamed to "+c.clanName+".");
		}
	} else {
		c.getActionAssistant().sendMessage("Your clan name can be no longer than 15 characters.");
	}

	}
}