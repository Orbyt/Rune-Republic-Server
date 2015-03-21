package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.Item;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.Config;
import com.rs.worldserver.world.ItemManager;
/**
 * give command
 * Use as ::give-[name]-[item]-[amount]
 * 
 * @author Orbit
 * 
 */
public class give implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {
			String spltcommand = command.substring(5);
			String[] args = spltcommand.split("-");
			if (args.length == 3) {
				String name = args[0];
				int itemID = Integer.parseInt(args[1]);
				int amount = Integer.parseInt(args[2]);
				String giver = c.getPlayerName();
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getPlayerName().equalsIgnoreCase(name)) {
						Client d = (Client) p;
						c.getActionAssistant().sendMessage("You have given " + amount + " " +
								Server.getItemManager().getItemDefinition(itemID).getName() + " to " + d.getPlayerName());
						d.getActionAssistant().sendMessage(giver + " Has given you "
								+ amount + " " + Server.getItemManager().getItemDefinition(itemID).getName());
						d.getActionAssistant().addItem(itemID, amount);
					}
				}
			} else {
				c.getActionAssistant().sendMessage("Syntax is ::give [name]-[id]-[amount]");
			}
		}
	}
}

			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			//			String playername = "";
//			command=command.substring(5);
//			boolean success = false;
//			Integer itemid=0,amount=0;
//			if (command.indexOf('-') > -1) {
//				playername = command.substring(0, command.indexOf('-'));
//				itemid = Integer.parseInt(command.substring(command.indexOf('-')+1,command.lastIndexOf('-')));
//				amount = Integer.parseInt(command.substring(command.lastIndexOf('-')+1));
//			}
//			Client o = (Client)Server.getPlayerManager().getPlayerByName(playername.toLowerCase());
//			if(o==null) {
//				client.getActionAssistant().sendMessage("That player is not online.");
//				return;
//			}
//			if(Item.itemStackable[itemid]) {
//				success=o.getActionAssistant().addItem(itemid, amount);
//				if(success)
//					client.getActionAssistant().sendMessage("Gave " + playername + "  " + itemid + "[" + amount + "]");
//				return;
//			}
//			for(int i=0; i<amount; i++)
//				success=o.getActionAssistant().addItem(itemid, 1);
//			if(success)
//				client.getActionAssistant().sendMessage("Gave " + playername + "  " + itemid + "[" + amount + "]");