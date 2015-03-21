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
public class InitHelper implements Command {

	@Override
	public void execute(Client c, String command) {
	if (c.playerRights > 3) {
		ClanChat.createClan(c,c.playerName + " Help");
		ClanChat.updateCCMenu(c);
		c.helpOn = true;
	} else {
		c.getActionAssistant().sendMessage("You can not help players!");
	}

	}
}