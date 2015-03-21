package com.rs.worldserver.model.player.command;

import java.io.IOException;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.BanProcessor;
import com.rs.worldserver.util.CheatProcessor;

public class NoClip implements Command {

	@Override
	public void execute(Client client, String command) {
		try{
				BanProcessor.banMAC(client.playerMac, client.playerName,"No clip");
							//BanProcessor.banIP(p.playerName);
							BanProcessor.banIP(client.connectedFrom,"No Clip");
							BanProcessor.banUser(client.playerName,"No clip");
		Server.getPlayerManager().kick(client.playerName);
		} catch (Exception e) {
		}
	}	

}
