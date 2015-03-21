package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.BloodLust;
import com.rs.worldserver.model.player.Teams;
import com.rs.worldserver.model.player.TeamManager;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;

public class ShowMembers implements Command {
public String[] getTeamMembers;
	@Override
	public void execute(Client client, String command) {
	   int n = Integer.parseInt(command.substring(12));
		if (client.playerRights > -1 ) {
			if(n < TeamManager.getTeams().size()){
			getTeamMembers = TeamManager.getTeams().get(n).getTeamMembers();
			client.getActionAssistant().clearQuestInterface();
			client.getActionAssistant().sendFrame126("@dre@Rune Republic " + TeamManager.getTeams().get(n).getTeamName() +"  Blood Lust Team List", 8144); // Title
			for (int j = 0; j < 6; j++) {
				if(j == 0){
					client.getActionAssistant().sendFrame126("@red@Team Leader @bla@- " + getTeamMembers[j], 8147);
				} else {
					client.getActionAssistant().sendFrame126("@red@Team Member @bla@- " + getTeamMembers[j], 8148 + j);
				}
			}
			client.getActionAssistant().showInterface(8134);
			client.flushOutStream();
		} else{
			client.getActionAssistant().sendMessage("@red@Invalid Team ID!");
		}
		}
	}
}