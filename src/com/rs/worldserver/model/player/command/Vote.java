package com.rs.worldserver.model.player.command;

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
//import com.rs.worldserver.model.player.ActionAssistant;

public class Vote implements Command {

	@Override
	public void execute(Client c, String command) {
		
		/*if(VoteSql.checkVotes(c.playerName)){
			if(c.getActionAssistant().freeSlots() >= 4)	{
				if(VoteSql.voteGiven(c.playerName)){
					c.getActionAssistant().addItem(995,2000000);
					c.getActionAssistant().addItem(555,2000);
					c.getActionAssistant().addItem(560,1000);
					c.getActionAssistant().addItem(565,1000);
				//	client.pkpoints = client.pkpoints + 200;
					c.getActionAssistant().sendMessage("@red@Thank you for voting!");
				}
			}else{
				c.getActionAssistant().sendMessage("@red@You do not have enough free space to receive items");
			}
		}*/ 	
//c.getActionAssistant().sendMessage("Unfortunatly due to RuneLocus rules, we have been forced to disable the VoteForCash system");	
c.getActionAssistant().sendMessage("Voting is currently non operational.");	
	}
}