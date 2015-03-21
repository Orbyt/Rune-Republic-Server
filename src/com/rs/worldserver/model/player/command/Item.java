package com.rs.worldserver.model.player.command;

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.util.TeleProcessor;

public class Item implements Command {

	@Override
	public void execute(Client c, String command) {
		String[] args = command.split(" ");
		if (args.length < 2) {
			c.getActionAssistant().sendMessage("Syntax is ::item [ID]");
				return;
		} else {
			int itemID = Integer.parseInt(args[1]);
			switch (args.length) {
			
			case 2:
				c.getActionAssistant().addItem(itemID, 1);
			break;
			
			case 3:
				int itemAmount = Integer.parseInt(args[2]);
				c.getActionAssistant().addItem(itemID, itemAmount);
			}
		}
	}

}
