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
//import com.rs.worldserver.model.player.NRAchievements;
//
//public class GiveAchievement implements Command {
//
//	@Override
//	public void execute(Client c, String command) {
//		if (c.playerName.equalsIgnoreCase("Orbit")) {
//			String str = command.substring(3, command.length());
//			String[] tokens = str.split(",");
//			for (Player p : Server.getPlayerManager().getPlayers()) {
//					if (p == null)
//						continue;
//					if (!p.isActive || p.disconnected)
//						continue;
//					if (p.getPlayerName().equalsIgnoreCase(tokens[0])) {
//							Client d = (Client) p;
//							d.getNRAchievements().displayCongrats(d,Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]));
//							
//					}			
//			}
//		}
//	}
//
//}
