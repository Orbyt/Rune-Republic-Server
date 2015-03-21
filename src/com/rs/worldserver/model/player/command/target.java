package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.CheatProcessor;
import com.rs.worldserver.world.*;


public class target implements Command {

	@Override
	public void execute(Client client, String command) {
	
		if (command.length() ==6) {
		if (client.BHTarget != -1) {
		Client person = (Client) PlayerManager.getSingleton().getPlayers()[client.BHTarget];
		client.getActionAssistant().sendMessage("Your target is: "+person.playerName);
			
}
else {
client.getActionAssistant().sendMessage("You have no target!");
}
}
}
}
