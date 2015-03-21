package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.util.CheatProcessor;
import com.rs.worldserver.util.BankProcessor;


public class delmypin implements Command {
	public String temppin = null;
	@Override
	public void execute(Client c, String command) {
	 String name = command.substring(9);
		if (c.bankpin.equalsIgnoreCase(name)) {
			c.getActionAssistant().sendMessage("@red@Your bank pin was deleted");
			c.bankPinActived = 1;
			c.bankPinSet = 0;
		} else {
				c.getActionAssistant().sendMessage("@red@Your bank pin was invalid!");
				return;
			
		}

	}

}