package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.model.player.*;

public class Head implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {
                int Sound = Integer.parseInt(command.substring(5));
				c.headIcon = Sound;
				c.getActionAssistant().requestUpdates();
		}
	}
}