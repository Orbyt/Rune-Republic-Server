package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;

public class RemoveFriends implements Command {

	@Override
	public void execute(Client client, String command) {
		for (int i = 0; i < client.getFriends().length; i++) {
				client.getFriends()[i] = 0;
		}
	
	}
}			