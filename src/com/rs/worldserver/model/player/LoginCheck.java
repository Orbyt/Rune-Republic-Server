package com.rs.worldserver.model.player;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;

public class LoginCheck {
	private Client 		client;
	public LoginCheck(Client c) {
		this.client = c;
		startCheck();
	}

	private void startCheck() {
			EventManager.getSingleton().addEvent(client,"logincheck", new Event() {
			@Override
			public void execute(EventContainer c) {
				if (client.disconnected || client == null) {
					c.stop();
					return;
				}
				if(client.loginTimes > 2) {
					if (!client.inWild() || client.wildLevel < 20) {
					client.stuckX = client.absX;
					client.stuckY = client.absY;
					client.stuckHeight = client.heightLevel;
						client.getActionAssistant().Send("@red@You have been auto detected as stuck logging in, type ::return to go back");
						client.teleportToX = Misc.random(500);
						client.teleportToY = Misc.random(500);
						c.stop();
					}	
				}

				if(client.loginTimes >= 3) {
					if (!client.inWild() || client.wildLevel < 20) {
					client.stuckX = client.absX;
					client.stuckY = client.absY;
					client.stuckHeight = client.heightLevel;
						client.getActionAssistant().Send("@red@You have been auto detected as stuck logging in, type ::return to go back");
						client.teleportToX = Misc.random(500);
						client.teleportToY = Misc.random(500);
						client.loginTimes = 0;
						c.stop();
					}
				}
				if(client.loginTimer > 0) {
					client.loginTimer--;
				}
				if (client.loginTimer == 0) {
					c.stop();
				}
	
			}

			@Override
			public void stop() {
			}
			}, 2000);
	}

}