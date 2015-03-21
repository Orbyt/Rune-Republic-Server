package com.rs.worldserver.model.player.command;

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Config;
import java.sql.*;
import com.rs.worldserver.util.BankProcessor;


public class mma implements Command {

	@Override
	public void execute(Client client, String command) {
	if (client.playerRights > 1 || BankProcessor.checkMM(client.playerName)) {
	
				if (client.inDuelArena()) {
	client.getActionAssistant().Send("You are already in duel arena.");
	return;
		}
			if (client.inWild()) {
			client.getActionAssistant().Send("You cannot use this command in the wilderness.");
			return;
		}	
		client.inAir = false;
		client.poisontimer = 1;
		client.poisondamage = 0;
		client.stopMovement();
		client.getActionAssistant().startTeleport(2038, 4497, 0, "modern");
	}


	}}
