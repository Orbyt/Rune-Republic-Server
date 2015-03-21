//package com.rs.worldserver.model.player.command;
//
//import com.rs.worldserver.Server;
//import com.rs.worldserver.model.player.Client;
//import com.rs.worldserver.model.player.Command;
//import com.rs.worldserver.content.skill.SkillConstants;
//import com.rs.worldserver.content.skill.Magic;
//import com.rs.worldserver.events.Event;
//import com.rs.worldserver.events.EventContainer;
//import com.rs.worldserver.events.EventManager;
//import com.rs.worldserver.Config;
//import com.rs.worldserver.util.TeleProcessor;
//import com.rs.worldserver.util.BanProcessor;
//
//public class beta implements Command {
//
//	@Override
//	public void execute(Client c, String command) {
//		if (c.inDuelArena()) {
//			return;
//		}
//		if (!c.inWild()) {
//		
//		if(BanProcessor.checkBetaNames(c.playerName.toLowerCase())) {
//			c.getActionAssistant().startTeleport(2393, 9890, 0, "modern");
//		}
//		}
//	}
//
//}
