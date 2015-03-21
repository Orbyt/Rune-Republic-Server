package com.rs.worldserver.model.player.command;

import java.io.*;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.BanProcessor;


public class UnIP2 implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {
			if (command.length() > 6) {
			    String victim = command.substring(6);
				BanProcessor.loadPunishments();
				BanProcessor.unIP2(victim);
				c.getActionAssistant().sendMessage("All IP bans for " + victim + " cleared."); 
			}
		}
	}
}
