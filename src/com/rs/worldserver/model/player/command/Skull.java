package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.model.player.*;

public class Skull implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerRights > 2) {
                    int Sound = Integer.parseInt(command.substring(6));
				c.skullIcon = Sound;
				c.getActionAssistant().requestUpdates();
		}
	}
}