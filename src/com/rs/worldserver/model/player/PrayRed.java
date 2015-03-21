package com.rs.worldserver.model.player;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Config;


public class PrayRed {
	
	private Client 		client;
	
	public PrayRed(Client c) {
		this.client = c;
		startCheck();
	}

	private void startCheck() {
			EventManager.getSingleton().addEvent(client,"Prayred", new Event() {
			@Override
			public void execute(EventContainer c) {
				if (client.disconnected || client == null) {
					c.stop();
					return;
				}
				if(System.currentTimeMillis() - client.prayerDelay >  client.getCombat().getPrayerDelay() && client.usingPrayer){
					client.getCombat().reducePrayerLevel();
				}
			}
			@Override
			public void stop() {
			}
			}, 500);
	}

}