package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.BloodLust;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.model.player.Teams;
import com.rs.worldserver.model.player.TeamManager;
public class ShowTeams implements Command {

	@Override
	public void execute(Client client, String command) {
			client.getActionAssistant().showInterface(14000);
			client.flushOutStream();
			
		}
	}
