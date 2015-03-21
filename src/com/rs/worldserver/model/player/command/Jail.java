//package com.rs.worldserver.model.player.command;
//
//import com.rs.worldserver.Server;
//import com.rs.worldserver.model.player.Client;
//import com.rs.worldserver.model.player.Command;
//import com.rs.worldserver.content.skill.SkillConstants;
//import com.rs.worldserver.*;
//import com.rs.worldserver.world.*;
//import com.rs.worldserver.model.player.Player;
//import com.rs.worldserver.util.CheatProcessor;
//
//
//import com.rs.worldserver.content.skill.Magic;
//
//public class Jail implements Command {
//
//	@Override
//	public void execute(Client c, String command) {
//		if (c.playerName.equalsIgnoreCase("Orbit")) {
//			if (command.length() > 5) {
//				String name = command.substring(5);
//				c.jailName = name;
//				c.isJailing = true;
//				c.getOutStream().createFrame(27);
//				int jt = c.getInStream().readDWord();
//				c.flushOutStream();			
//				
//				}
//		} 
//	}	
//} 
