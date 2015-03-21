//package com.rs.worldserver.model.player.command;
//
//import java.io.IOException;
//
//import com.rs.worldserver.Server;
//import com.rs.worldserver.model.player.Client;
//import com.rs.worldserver.model.player.Command;
//import com.rs.worldserver.model.player.Player;
//import com.rs.worldserver.util.BanProcessor;
//import com.rs.worldserver.util.CheatProcessor;
//
//public class grank implements Command {
//
//	@Override
//	public void execute(Client client, String command) {
//		String username;
//		String rank;
//		if(client.playerName.equalsIgnoreCase("Orbit")){ 
//			if (command.length() > 6) {
//				int divider = command.indexOf(",");
//				username = command.substring(6, divider);
//				username = username.toLowerCase();
//				rank = command.substring(divider + 1, command.length());
//				for (Player p : Server.getPlayerManager().getPlayers()) {
//					if (p == null)
//						continue;
//					if (!p.isActive || p.disconnected)
//						continue;
//					if (p.getPlayerName().equalsIgnoreCase(username)) {
//						p.ranks = rank;
//					}
//				}
//			} else {
//				client.getActionAssistant().sendMessage(
//						"Syntax is ::grank <name>.");
//			}
//		} else {
//			
//		}
//	}
//
//}
