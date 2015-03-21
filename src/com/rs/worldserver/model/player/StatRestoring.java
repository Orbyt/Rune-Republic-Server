package com.rs.worldserver.model.player;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;

public class StatRestoring {
	
	private Client 		client;
	
	public StatRestoring(Client c) {
		this.client = c;
		startRestoring();
	}

	private void startRestoring() {
		EventManager.getSingleton().addEvent(client,"Stat restoring", new Event() {
			@Override
			public void execute(EventContainer c) {
				if (client.disconnected || client == null) {
					c.stop();
					return;
				}
				for (int level = 0; level < client.playerLevel.length; level++)  {
					if (client.playerLevel[level] < client.getLevelForXP(client.playerXP[level]) || (level == 3 && (client.playerLevel[level] < client.getLevelForXP(client.playerXP[level]) +client.getActionAssistant().getMaxHP()))) {
						if(level != 5) { 
                            client.playerLevel[level] += 1;
                            client.getActionAssistant().setSkillLevel(level, client.playerLevel[level], client.playerXP[level]);
                            client.NewHP = client.playerLevel[3];
                            client.getActionAssistant().refreshSkill(level);
						}
						} else if (client.playerLevel[level] > client.getLevelForXP(client.playerXP[level])) {
							client.playerLevel[level] -= 1;
							client.getActionAssistant().setSkillLevel(level, client.playerLevel[level], client.playerXP[level]);
							client.getActionAssistant().refreshSkill(level);
						}
				}       
			}

			@Override
			public void stop() {
			}
		}, 150000);
	}

}