package com.rs.worldserver.model.player;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.model.player.*;

public class CT {
	
	private Client 		client;
	
	public CT(Client c) {
		this.client = c;
		startSave();
	}

	private void startSave() {
			EventManager.getSingleton().addEvent(client,"CT file", new Event() {
			@Override
			public void execute(EventContainer c) {
				if (client.disconnected || client == null) {
					c.stop();
					return;
				}
				if(System.currentTimeMillis() - client.singleCombatDelay >  5000) {
					client.underAttackBy = 0;
				}
				if(System.currentTimeMillis() - client.singleCombatDelay2 >  15000) {
					client.totalPlayerDamageDealt = 0;
					client.killedBy = 0;
				}
			}
			@Override
			public void stop() {
			}
			}, 500);
	}

}