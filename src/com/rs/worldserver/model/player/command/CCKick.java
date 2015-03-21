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
public class CCKick implements Command {

	@Override
	public void execute(Client c, String command) {
		String username;
		String clanchat;
		int divider = command.indexOf(",");
		username = command.substring(7, divider);
		username = username.toLowerCase();
		username = username.replaceAll(" ", "_");
		clanchat = command.substring(divider + 1, command.length());
	if(command.length() == 6 ||command.length() == 7 ) {
		c.getActionAssistant().sendMessage("Enter a name!");
		return;
	}
	if(c.playerName.equalsIgnoreCase(ClanChat.getClanLeader(clanchat))){
	Client p2 = (Client) Server.getPlayerManager().getPlayerByName(username);

	if(p2 == null) {
		return;
	}
		ClanChat.removeMember(p2,clanchat);
		ClanChat.removeCCMenu(p2);
		c.getActionAssistant().sendMessage(username+" has been kicked successfully.");
	} else {
		c.getActionAssistant().sendMessage("@red@ You are not the clan chat leader!");
	}
	/*if(c.clanName != "none" && c.clanLeader != "nobody" && p2.clanName == c.clanName && p2.clanLeader == c.clanLeader && p2.playerName != c.clanLeader) {
		p2.getActionAssistant().sendMessage("You have been kicked from your clan by "+c.playerName+".");
		p2.getActionAssistant().removeFromCC();
		c.getActionAssistant().sendMessage(command.substring(7)+" has been kicked successfully.");
	}*/

	}
}