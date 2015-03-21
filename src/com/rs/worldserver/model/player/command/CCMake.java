package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.model.player.ClanChat;
public class CCMake implements Command {

	@Override
	public void execute(Client c, String command) {
	if(command.length() == 6 ||command.length() == 7 ) {
		c.getActionAssistant().sendMessage("Enter a clan name!");
		return;
	}
	if(command.length() <= 22) {
		ClanChat.createClan(c,command.substring(7));
		ClanChat.updateCCMenu(c);
		/* if(c.clanName.equalsIgnoreCase("none") && c.clanLeader.equalsIgnoreCase("nobody")) {
			c.clanName = command.substring(7);
			c.clanLeader = c.playerName;
			for(int i=0; i<=17; i++) {
				if(c.clanMembers[i] == null) {
					c.getActionAssistant().sendMessage("Clan "+command.substring(7)+" has been created successfully.");
					c.clanMembers[i] = c.playerName;
					break;
				}
			}
			c.getActionAssistant().updateCCMenu();
		} else {
			c.getActionAssistant().sendMessage("You are already in a clan!");
		} */
	} else {
		c.getActionAssistant().sendMessage("Your clan name can be no longer than 15 characters.");
	}

	}
}