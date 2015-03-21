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


public class prot implements Command {

	@Override
	public void execute(Client c, String command) {
		
		
			if (c.prayerActive[10] || c.curseActive[0]) {
					c.getActionAssistant().Send("@gre@Your protect item is active");
					return;
			} else {				
			c.getActionAssistant().Send("@red@Your protect item is not active");
			}
		}
	
}