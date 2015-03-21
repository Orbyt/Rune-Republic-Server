package com.rs.worldserver.model.player;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.model.player.*;

public class Saving {
	
	private Client 		client;
	
	public Saving(Client c) {
		this.client = c;
		startSave();
	}

	private void startSave() {
			EventManager.getSingleton().addEvent(client,"saving", new Event() {
			@Override
			public void execute(EventContainer c) {
				if (client.disconnected || client == null) {
					c.stop();
					return;
				}
				PlayerManager.getSingleton().saveGame(client);
			}
			@Override
			public void stop() {
			}
			}, 10000);
	}

}