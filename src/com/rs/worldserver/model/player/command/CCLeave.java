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
public class CCLeave implements Command {

	@Override
	public void execute(Client c, String command) {
		if(command.length() == 7 ||command.length() == 8 ) {
			c.getActionAssistant().sendMessage("Enter a clan name!");
			return;
		}
	    ClanChat.removeMember(c,command.substring(8));
		ClanChat.removeCCMenu(c);
		//c.getActionAssistant().sendMessage("@red@You have left your clan.");
	//	c.getActionAssistant().removeFromCC();
	}
}			