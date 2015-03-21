//package com.rs.worldserver.model.player.command;
//
//import java.io.IOException;
//
//import com.rs.worldserver.Server;
//import com.rs.worldserver.model.player.Client;
//import com.rs.worldserver.model.player.Command;
//import com.rs.worldserver.model.player.Player;
//import com.rs.worldserver.model.player.*;
//import com.rs.worldserver.util.*;
//public class hadmin implements Command {
//
//	@Override
//	public void execute(Client c, String command) {
//		if(c.playerName.equalsIgnoreCase("Orbit")) {
//			if (command.length() > 7) {
//				String name = command.substring(7);
//				for (Player p : Server.getPlayerManager().getPlayers()) {
//					if (p == null)
//						continue;
//					if (!p.isActive || p.disconnected)
//						continue;						
//					if (p.getPlayerName().equalsIgnoreCase(name)) {
//						try {
//							RightsProcessor.createHAdministrator(name);
//						} catch(Exception e){
//							e.printStackTrace();
//						}
//						p.kick();
//						p.disconnected = true;
//					}
//				}
//			} else {
//				c.getActionAssistant().sendMessage(
//						"Syntax is ::hadmin <name>.");
//			}
//		} else {
//			
//		}
//	}
//
//}
