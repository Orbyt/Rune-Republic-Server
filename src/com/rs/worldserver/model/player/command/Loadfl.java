package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;

public class Loadfl implements Command {

	@Override
	public void execute(Client c, String command) {
			/*if (c.checkFriends == 1) {
					c.getActionAssistant().Send("You already loaded your friends list!");
					return;
			} else {				
				c.friendsAssistant.initialize();
				c.checkFriends = 1;
			}*/
	}
}