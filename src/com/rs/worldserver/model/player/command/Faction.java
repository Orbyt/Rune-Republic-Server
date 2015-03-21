package com.rs.worldserver.model.player.command;

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;

public class Faction implements Command {

	@Override
	public void execute(Client client, String command) {
		try {
			if ( !client.hasSetFaction ) {
		
//			client.getActionAssistant().clearQuestInterface();
//			client.getActionAssistant().sendFrame126("Faladorian", 2494);
//			client.getActionAssistant().sendFrame126("", 2495);
//			client.getActionAssistant().sendFrame126("Ardougnian", 2496);
//			client.getActionAssistant().sendFrame126("", 2497);
//			client.getActionAssistant().sendFrame126("Varrockian", 2498);
//			client.getActionAssistant().sendQuestSomething(8143);
//			client.getActionAssistant().sendFrame164(2492);
//			client.flushOutStream();
			String[] args = command.split(" ");
			if (args[1].equalsIgnoreCase("varrock")) {
				client.faction = 1;
				client.hasSetFaction = true;
				client.getActionAssistant().sendMessage("You have set your faction to Varrock");
				return;
			}
			if (args[1].equalsIgnoreCase("falador")) {
				client.faction = 2;
				client.hasSetFaction = true;
				client.getActionAssistant().sendMessage("You have set your faction to Falador");
				return;
			}
			if (args[1].equalsIgnoreCase("ardougne")) {
				client.faction = 3;
				client.hasSetFaction = true;
				client.getActionAssistant().sendMessage("You have set your faction to Ardougne");
				return;
			}
			
			
		}
		client.getActionAssistant().sendMessage("Either you already have set your faction or your using the wrong syntax.");
		client.getActionAssistant().sendMessage("Example: '::faction varrock'. Syntax is '::faction [city]'.");
	
		} catch (Exception e) {
			client.getActionAssistant().sendMessage("Syntax is ::faction [city]    Example--::faction varrock");
	}
}
}
			
	

