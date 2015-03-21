package com.rs.worldserver.model.player;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;
public class MessagesCheck {
	public String Messages[] = {"Type ::vote for daily rewards and help Rune Republic grow!", 
		                        "Staff members have crowns via PM! Not [ADMIN] or any other prefix!",
								"If you need server help, PM an available [SUPPORT], [MOD] or [ADMIN]",
								"New to the game? Type ::links to start!",
								"Don't know where to train? Try ::train!",
								"Have a suggestion? Go ahead and post it on the forums!",
								"Pk in 40+ Wilderness for Double PkP!",
								"Did you know Mystery Boxes contain Ancient Mace & Lime whip?",
								"There is lots of great information on the wiki, check it out!",
								"Ask staff members to middleman donator pin trades to avoid scams!",
								"Vote now for Mystery Boxes and have the chance to acquire upto 1.2k Pkp",
								"Visit our forums at www.runerepublic.com",
								"Feel you have a topic mastered? Why not write a guide on the forums?",
								"Please do not pm staff for prices!",
								"::commands has a list of some things you can use!"};
								//"::Don't forget to set a recovery email, ::setrecoveryemail"};
								//"Type ::wiki <word> to search over 50 guides on our wiki!"}; 
	
	private Client 		client;
	
	public MessagesCheck(Client c) {
		this.client = c;
		startCheck();
	}

	private void startCheck() {
			EventManager.getSingleton().addEvent(client,"messagescheck",new Event() {
			@Override
			public void execute(EventContainer c) {
				if (client.disconnected || client == null) {
					c.stop();
					return;
				}
				
				client.getActionAssistant().sendMessage("[@blu@SERVER NOTICE@bla@] " + Messages[Misc.random(Messages.length - 1)]);  
			}

			@Override
			public void stop() {
			}
			}, 300000);
	}

}