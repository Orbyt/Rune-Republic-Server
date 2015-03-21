package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;

public class Empty implements Command {

	@Override
	public void execute(Client c, String command) {
			if (c.inWild()) {
				c.getActionAssistant().Send("You can not empty in the wild.");
				return;
			} else {				
				c.getActionAssistant().removeAllItems();
			}
	}
}