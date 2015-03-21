package com.rs.worldserver;

import com.rs.worldserver.Server;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.Config;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.*;

public class Restarter {

    public int ResetTimer = 7200;

    public void process() {
        ResetTimer -= 1;
		if (ResetTimer == 120) {
			PlayerManager.getSingleton().sendGlobalMessage("@red@Server Restarting DO NOT TRADE DURING THIS TIME");
			PlayerManager.updateRunning = true;
			PlayerManager.updateSeconds = 60;
			PlayerManager.updateStartTime = System.currentTimeMillis();
			for (Player p : Server.getPlayerManager().getPlayers()) {
				if(p != null) {
					Client person = (Client)p;
					person.getTradeAssistant().decline();
				}			
			}
        }
		if (ResetTimer == 15) {
			for (Player p : Server.getPlayerManager().getPlayers()) {
				if(p != null) {
					Client person = (Client)p;
					person.getTradeAssistant().decline();
					person.cancelTasks();
					PlayerManager.getSingleton().saveGame(person);
				}			
			}
		}
		if (ResetTimer == 6) {
			for (Player p : Server.getPlayerManager().getPlayers()) {
				if(p != null) {
					Client person = (Client)p;
					person.getTradeAssistant().decline();
				}	
			Server.getPlayerManager().kickAllPlayers = true;
			Server.shutdown();
			}
		}		
	}
}