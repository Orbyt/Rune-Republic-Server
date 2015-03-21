package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;

public class CCHelp implements Command {

	@Override
	public void execute(Client c, String command) {
			c.getActionAssistant().clearQuestInterface();
			c.getActionAssistant().sendFrame126("@dre@Rune Republic Clan Chat Help Menu", 8144); // Title
			c.getActionAssistant().sendFrame126("::ccmake - Make A Clan", 8147);
			c.getActionAssistant().sendFrame126("::cckick - Kick Someone From A Clan", 8148);
			c.getActionAssistant().sendFrame126("::ccleave - Leave A Clan", 8149);
			c.getActionAssistant().sendFrame126("::ccdelete - Delete Your Clan", 8150);
			c.getActionAssistant().sendFrame126("::ccrename - Rename Your Clan", 8151);
			c.getActionAssistant().sendFrame126("::ccjoin - Join A Clan", 8152);
		//	c.getActionAssistant().sendFrame126("::cm - Sends Clan Chat Messages Example ::cm Hello", 8153);
			c.getActionAssistant().sendFrame126("Use '/' To Send Clan Chat Messages", 8153);
			c.getActionAssistant().showInterface(8134);
			c.flushOutStream();
	
	}
}			