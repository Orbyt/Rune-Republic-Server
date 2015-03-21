package com.rs.worldserver.model.player;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Config;


public class Jailed {
	
	private Client 		client;
	
	public Jailed(Client c) {
		this.client = c;
		startCheck();
	}

	private void startCheck() {
			EventManager.getSingleton().addEvent(client,"jailed", new Event() {
			@Override
			public void execute(EventContainer c) {
				if (client.disconnected || client == null) {
					c.stop();
					return;
				}
				if (client.jailTimer > 0) {
					client.jailTimer -= 1;
					if(client.jailTimer <= 0) {
						client.getActionAssistant().startTeleport3(3089, 3490, 0, "modern");
						client.blackMarks = 0;
						client.jailedBy = " ";
						client.jailTimer = 0;
						client.getActionAssistant().Send("@red@Times Up! You have been unjailed!");
					}
				}
			}
			@Override
			public void stop() {
			}
			}, 500);
	}

}