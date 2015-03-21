package com.rs.worldserver.model.player.command;

import java.io.*;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.BanProcessor;


public class UnIP implements Command {

	@Override
	public void execute(Client c, String command) {
		if ((c.playerRights > 5) &&  (c.playerRights != 12) && c.playerRights != 13) {
		//(c.playerName.equalsIgnoreCase("Orbit") || c.playerName.equalsIgnoreCase("Orbit")) {
			if (command.length() > 5) {
			    String victim = command.substring(5);
				BanProcessor.loadPunishments();
				BanProcessor.unMAC(victim);
				BanProcessor.unIP(victim);
				BanProcessor.unban(victim);
				BanProcessor.unipMute(victim);
				
				c.getActionAssistant().sendMessage("All IP bans and mutes for " + victim + " successfully cleared."); 
			}
		}
	}
}