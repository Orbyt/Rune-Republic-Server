package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.CheatProcessor;


public class dist implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerRights > 3) {
			
			for (Player p : Server.getPlayerManager().getPlayers()) {
					if(p != null) {
						Client d = (Client) p;
						if (c.withinDistance(c.absX, c.absY,d.absX, d.absY, 8)) {
							c.getActionAssistant().sendMessage("Name: " +d.playerName);
						}
					}
			}
		}
	}	
}
