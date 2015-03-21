package com.rs.worldserver.model.player.command;

import java.io.*;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.BanProcessor;


public class Unban implements Command {

	@Override
	public void execute(Client c, String command) {
		if ((c.playerRights > 3) && (c.playerRights != 5) && c.playerRights != 13 && (c.playerRights != 12)) {
			if (command.length() > 6) {
			    String victim = command.substring(6);
				BanProcessor.unban(victim);
				c.getActionAssistant().sendMessage("Player " + victim + " successfully unbanned."); 
			}
		}
	}
}