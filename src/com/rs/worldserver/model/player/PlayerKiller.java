package com.rs.worldserver.model.player;

import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.ActionAssistant;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Server;
import java.util.*;
import java.util.concurrent.*;
import java.math.*;
/** 
@author Orbit
*/
public class PlayerKiller {
static final int MAX_TIME_SINCE_HIT = 300; // 3 mins
    int playerId;
    int hits;
	int timeSinceLastHit;
	
	public PlayerKiller(int PlayerId) {
        playerId = PlayerId;
        hits = 0;
		timeSinceLastHit = 0;

    }

	
	public static void addHits(Client p1, Client p2, int Hits) {
        PlayerKiller tempPlayerKiller = new PlayerKiller(p2.playerId);
        for (PlayerKiller pk : p1.killer) {
            if (pk.playerId == tempPlayerKiller.playerId) {
                pk.hits += Hits;
     			pk.timeSinceLastHit = 0;
				//p2.getActionAssistant().sendMessage("@red@You've done " +pk.hits + " damage!");
                return;
            }
        }
        tempPlayerKiller.hits = Hits;
		//p2.getActionAssistant().sendMessage("@red@You did " + tempPlayerKiller.hits + " damage!");
        p1.killer.add(tempPlayerKiller);
		
    }
	public static void playerLog(Client p) {
        for (Player player : Server.getPlayerManager().getPlayers()) {
			if (player.killer.contains(p)){
				player.killer.remove(p);
			}
        }
	}

	public static void processList(Client p) {
        for (PlayerKiller pker : p.killer) {
            pker.timeSinceLastHit += 1;
            if (pker.timeSinceLastHit >= MAX_TIME_SINCE_HIT) {
				Iterator i =  p.killer.iterator();				
				while(i.hasNext()){
					if(i.equals(pker)){
						i.remove();
					}
					i.next();
				}
			}
		}
	}

	
	public static int getKiller(Client p) {
        int PlayerId = 0;
        int maxHits = 0;
        for (PlayerKiller P : p.killer) {
            if (P.hits > maxHits) {
                maxHits = P.hits;
                PlayerId = P.playerId;
            }
        }
        return PlayerId;
    }


}