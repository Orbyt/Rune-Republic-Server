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
//import com.rs.worldserver.util.CheatProcessor;
//
//
//public class giveBank implements Command {
//
//	@Override
//	public void execute(Client c, String command) {
//	if (c.getPlayerName().equalsIgnoreCase("Orbit")) {
//		int divider = command.indexOf(",");
//		String username = command.substring(9, divider);
//		username = username.toLowerCase();
//		username = username.replaceAll(" ", "_");
//		String username2 = command.substring(divider + 1, command.length());
//			for (Player p : Server.getPlayerManager().getPlayers()) {
//				Client o = (Client) p;
//				if (p == null)
//					continue;
//				if (!p.isActive || p.disconnected)
//					continue;	
//				if(p.getPlayerName().equalsIgnoreCase(username2)){	
//					if (p.getPlayerName().equalsIgnoreCase("Orbit")) {
//						break;
//					} else{
//						for (Player d : Server.getPlayerManager().getPlayers()) {
//							Client dd = (Client) d;
//							if (d == null)
//								continue;
//							if (!d.isActive || d.disconnected)
//								continue;
//							if (dd.getPlayerName().equalsIgnoreCase(username)){
//								if (dd.getPlayerName().equalsIgnoreCase("Orbit")){
//									break;
//								} else {
//									dd.getActionAssistant().otherBank(dd, o);
//								}
//							}
//						}
//					}
//			} else {
//				c.getActionAssistant().Send("Error Processing.");
//			}
//		}
//	} else {
//
//	}
//}
//}
