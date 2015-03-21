package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.ClanChat;
import com.rs.worldserver.content.skill.Magic;

public class CCJoin implements Command {

	@Override
	public void execute(Client c, String command) {
		if(command.length() == 6 ||command.length() == 7 ) {
			c.getActionAssistant().sendMessage("Enter a clan name!");
			return;
		}
		ClanChat.addMember(c,command.substring(7));
		ClanChat.updateCCMenu(c);
/* 		if(Server.getPlayerManager().getPlayerByName(command.substring(7)) != null) {
		if(c.clanName.equalsIgnoreCase("none") && c.clanLeader.equalsIgnoreCase("nobody")) {
			Client c2 = (Client) Server.getPlayerManager().getPlayerByName(command.substring(7)); //clanLeader
			if(c2.clanLeader == c2.playerName) {
				c.clanName = c2.clanName;
				c.clanLeader = c2.playerName;
				for(int i=0; i<=17; i++) {
					if(c2.clanMembers[i] == null) {
						c2.clanMembers[i] = c.playerName;
						c.getActionAssistant().sendMessage("You have joined the clan "+c.clanName+".");
						c2.getActionAssistant().sendMessage(c.playerName+" has joined your clan.");
						break;
					}
				}
				c.getActionAssistant().updateCCMenu();
			} else {
				c.getActionAssistant().sendMessage("That player is not the leader of a clan.");
			}
		} else {
			c.getActionAssistant().sendMessage("You are already in a clan!");
		}
	} else {
		c.getActionAssistant().sendMessage("Invalid player specified. Please make sure you've entered their name correctly.");
	} */


	}
}