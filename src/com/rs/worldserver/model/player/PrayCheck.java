package com.rs.worldserver.model.player;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Config;


public class PrayCheck {
	
	private Client 		client;
	
	public PrayCheck(Client c) {
		this.client = c;
		startCheck();
	}

	private void startCheck() {
			EventManager.getSingleton().addEvent(client,"praycheck",new Event() {
			@Override
			public void execute(EventContainer c) {
				if (client.disconnected || client == null) {
					c.stop();
					return;
				}
		if(client.prayerActive[16] && client.isSkulled){
			client.headIcon = 2;
			client.skullIcon = 0;
		}
		if(client.prayerActive[17] && client.isSkulled){
			client.headIcon = 1;
			client.skullIcon = 0;
		} 	
		if(client.prayerActive[18] && client.isSkulled){
			client.headIcon = 0;
			client.skullIcon = 0;
		}
		if(client.prayerActive[21] && client.isSkulled){
			client.headIcon = 3;
			client.skullIcon = 0;
		}
		if(client.prayerActive[22] && client.isSkulled){
			client.headIcon = 5;
			client.skullIcon = 0;
		}
		if(client.prayerActive[23] && client.isSkulled){
			client.headIcon = 4;
			client.skullIcon = 0;
		}
		if(client.prayerActive[16] && !client.isSkulled){
			client.headIcon = 2;
		}
		if(client.prayerActive[17] && !client.isSkulled){
			client.headIcon = 1;
		} 	
		if(client.prayerActive[18] && !client.isSkulled){
			client.headIcon = 0;
		}
		if(client.prayerActive[21] && !client.isSkulled){
			client.headIcon = 3;
		}
		if(client.prayerActive[22] && !client.isSkulled){
			client.headIcon = 5;
		}
		if(client.prayerActive[23] && !client.isSkulled){
			client.headIcon = 4;
		}
		if(client.playerLevel[5] <= 0) {
			if(client.prayOff == 1) {
				client.actionAssistant.sendMessage("You have run out of prayer points!");
				client.actionAssistant.resetPrayers();
				if(client.sounds == 1) {
					client.actionAssistant.frame174(437,050,000);
				}
				client.prayOff = 0;
			}
			if(client.isSkulled) {
				client.skullIcon = 0;
			}
		}
			}
			@Override
			public void stop() {
			}
			}, 500);
	}

}