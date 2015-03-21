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


public class Bank implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.inDuelArena()) {
			c.getActionAssistant().Send("Nice attempt, but it's already patched");
			return;
		}
		if (c.fightCave()) {
			c.getActionAssistant().Send("Really think I'd allow that you dumbass?");
			return;
		}		
		
		if ((c.playerRights > 3 && c.playerRights != 5)|| BankProcessor.checkBank(c.playerName)||c.modDay || c.modDays) {
			if (c.inWild()) {
					c.getActionAssistant().Send("You can not bank in the wild cunt.");
					return;
			} else {				
				c.getActionAssistant().openUpBank();
			}
		}
	}
}