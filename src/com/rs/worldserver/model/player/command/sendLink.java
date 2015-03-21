package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.model.player.*;
public class sendLink implements Command {


	@Override
	public void execute(Client c, String command) {
	if(c.playerName.equalsIgnoreCase("Orbit")) {
		c.getActionAssistant().sendMessage("This is the text before the link+http://google.com$link$");
	}
	}
}
