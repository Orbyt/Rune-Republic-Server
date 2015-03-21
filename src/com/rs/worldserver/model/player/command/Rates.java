//package com.rs.worldserver.model.player.command;
//
//import com.rs.worldserver.Server;
//import com.rs.worldserver.model.player.Client;
//import com.rs.worldserver.model.player.Command;
//import com.rs.worldserver.content.skill.SkillConstants;
//import com.rs.worldserver.*;
//import com.rs.worldserver.world.*;
//import com.rs.worldserver.model.player.Player;
//import com.rs.worldserver.content.skill.Magic;

//public class Rates implements Command {
//
//	@Override
//	public void execute(Client c, String command) {
//	if (c.inDuelArena()) {
//			c.getActionAssistant().Send("You cannot use this here.");
//			return;
//		}
//			c.getActionAssistant().clearQuestInterface();
//			c.getActionAssistant().sendFrame126("@dre@Rune Republic Rates.", 8144); // Title
//			c.getActionAssistant().sendFrame126("@dbl@Players Online: @gre@" + PlayerManager.getSingleton().getPlayerCount(),8146 );
//			c.getActionAssistant().sendFrame126("@dre@Rates for Donating for Items.", 8147);
//			c.getActionAssistant().sendFrame126("@blu@These rates have not been establish", 8148);
//			c.getActionAssistant().sendFrame126("@blu@This command will be updated later", 8149);
//			c.getActionAssistant().sendFrame126("@dre@Thanks for testing the command",8150);
//			c.getActionAssistant().sendFrame126("@blu@Stay Classy",8151);
//			c.getActionAssistant().sendFrame126("@blu@You suck",8152);
//			c.getActionAssistant().showInterface(8134);
//			c.flushOutStream();
//	
//	}
//}