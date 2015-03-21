package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Config;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.CheatProcessor;



public class Notification implements Command {

	@Override
	public void execute(Client client, String command) {
	
		if (client.playerName.equalsIgnoreCase("Orbit")) {
			String args[] = command.split(" ", 3);
			String message = args[2];
			String messageColor = args[1];
			if (!messageColor.equalsIgnoreCase("red") && !messageColor.equalsIgnoreCase("blue") && !messageColor.equalsIgnoreCase("green") && !messageColor.equalsIgnoreCase("black")) {
				client.getActionAssistant().sendMessage("Available message colors are: red, blue, green, black. Syntax: ::notification [color] [msg]");
				return;
			}
			for (Player player : Server.getPlayerManager().getPlayers()) {
				if (player == null)
					continue;
				if (!player.isActive || player.disconnected)
					continue;
					Client client2 = (Client) player;
					client2.getActionAssistant().sendMessage("Notification##" + message + "##" + messageColor);
					
		}
	}
}
}