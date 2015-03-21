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


public class Pin implements Command {
	public String temppin = null;
	@Override
	public void execute(Client c, String command) {
	try {
	 if (command.length() > 4 && command.length() < 10)  {
	 String name = command.substring(4);
		if (c.bankpin.equalsIgnoreCase(name)) {
			c.getActionAssistant().sendMessage("@red@Your bank pin was accepted!");
			c.getActionAssistant().sendMessage("@red@You can now bank for this session!");
			c.bankPinActived = 1;
		} else {
			if(c.bankPinSet == 0){
				c.getActionAssistant().Send("@red@This account does not have a bank pin!");
				c.getActionAssistant().Send("@red@For extra security you should set a bank pin!");
				c.getActionAssistant().Send("@red@::setpin to generate one!");
			} else {
				c.getActionAssistant().sendMessage("@red@Your bank pin was invalid!");
				return;
			}
		}

	}
	}
	catch (Exception e) {
				c.getActionAssistant().sendMessage("@red@Use ::pin ####");
				return;
	}
	}

}