package com.rs.worldserver.events;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Constants;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.npc.NPCCombat;
import com.rs.worldserver.model.npc.NPCAction;
import com.rs.worldserver.*;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Config;
import com.rs.worldserver.events.EventManager;

public class FollowingEvent implements Event {

	@Override
	public void execute(EventContainer container) {
		for (Player p : PlayerManager.getSingleton().getPlayers()) {
			if (p == null)
				continue;
			Client client = (Client) p;
			if(client.follower){
				client.getActionAssistant().followPlayer();
			}
		}
	}

	public void stop() {
	}

}