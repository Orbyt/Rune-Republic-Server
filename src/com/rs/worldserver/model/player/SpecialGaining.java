package com.rs.worldserver.model.player;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.world.PlayerManager;

public class SpecialGaining {
	
	private Client 		client;
	
	public SpecialGaining(Client c) {
		this.client = c;
		startGaining();
	}

	private void startGaining() {
		EventManager.getSingleton().addEvent(client, "special gaining",new Event() {
			@Override
			public void execute(EventContainer ce) {
				if (client.disconnected || client == null) {
					ce.stop();
					return;
				}
				client.specialAmount++;
                client.getSpecials().specialBar();	     
			}

			@Override
			public void stop() {
			}
			}, 3500);
	}

}
