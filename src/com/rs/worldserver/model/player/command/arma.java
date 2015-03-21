package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.Config;
import com.rs.worldserver.util.TeleProcessor;

public class arma implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.inDuelArena()) {
			return;
		}
		if(TeleProcessor.checkTele(c.playerName)) {
			c.getActionAssistant().startTeleport(2838, 5296, 2, "modern");
		}
	}

}
