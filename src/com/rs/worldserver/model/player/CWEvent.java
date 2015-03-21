package com.rs.worldserver.model.player;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.util.DuelProcessor;
import com.rs.worldserver.Config;
public class CWEvent{
	private Client 		client;
	
	public CWEvent(Client c) {
		this.client = c;
		startCheck();
	}

	private boolean flag = false;
	public void stopCheck(){
		flag = true;
	}
	private void startCheck() {
		flag = false;
			EventManager.getSingleton().addEvent(client,"cwcheck", new Event() {
			@Override
			public void execute(EventContainer c) {
				if (client.disconnected || client == null) {
					c.stop();
					return;
				}
				if (Config.CastleWars) { 
					if (Server.getCastleWars().isInCw(client)) {
						if(client.isInSaraSafe() || client.isInZamSafe()){
							if(client.playerEquipment[3] == 4039){
								client.getActionAssistant().movePlayer(2421,3082, client.heightLevel);
							} else if(client.playerEquipment[3] == 4037){
								client.getActionAssistant().movePlayer(2378,3125, client.heightLevel);
							}
						}
					}
				}
				if(flag){
					c.stop();
					return;
				}
			}
			

			@Override
			public void stop() {
			}
			}, 100);
                    
        
	}

}
