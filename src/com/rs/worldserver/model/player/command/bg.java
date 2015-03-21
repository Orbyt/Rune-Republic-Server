package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.player.NRAchievements;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.util.ItemProcessor;
import com.rs.worldserver.util.Misc;
public class bg implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.playerName.equalsIgnoreCase("Orbit")) {
				//c.getFriendsAssistant().sendNewPM(Misc.playerNameToInt64(c.playerName),"test");
				//c.getNRAchievements().getReward(1,0);
				//c.getActionAssistant().sendMessage(AchievementManager.getMisc().get(0).getItemAmount());
				///AchievementManager.getMisc().get(id).getItemAmount();
			 	//TeamManager.getTeams().clear();
				//BloodLustLoader.loadTeams();
				//BloodLust.resetBloodlust();
				//BloodLustLoader.saveBloodLustTeams();
				//c.getActionAssistant().sendMessage(""+EventManager.getSingleton().getEventCount());
	//			String[] commands = command.split(" ");
	//			c.rewardPoints = 500;
				//Server.getBloodLust().showRankings(c,0);
	//			Server.getDegradeManager().repair(c);
				//EventManager.getSingleton().writeEvents();
		} else {

		}			
	
	}
}