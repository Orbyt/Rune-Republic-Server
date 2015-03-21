package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.model.player.*;

public class ExpLock implements Command {

	@Override
	public void execute(Client c, String command) {
	/*	if(c.expLock == true) {
			c.expLock = false;
			c.getActionAssistant().sendMessage("@red@Exp Locking Turned Off.");
		} else {
			c.expLock = true;
			c.getActionAssistant().sendMessage("@red@Exp Locking Turned On.");
		}*/
		c.getActionAssistant().sendMessage("Feature needs to be enabled");
	}
}