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
import com.rs.worldserver.util.BanProcessor;
import java.io.*;

public class DonCheck {
	private Client 		client;
	
	public DonCheck(Client c) {
		this.client = c;
		startCheck();
	}
	public final int[] BANNED_GEAR = {16689,20822,17361,20826,20825,20824,16687,17258,17359,16357,16292,16709,17339,17317,17215,17193,17601,167551,16931,17171,17237,16865};
	
	
private boolean inBGear(Client c){
	for(int i = 0; i < c.playerEquipment.length; i++)
		for(int j = 0; j < BANNED_GEAR.length;j++){
			if(c.playerEquipment[i] == BANNED_GEAR[j])
				return true;
			if(c.getActionAssistant().playerHasItem(BANNED_GEAR[j]))
				return true;
		}
	
	return false;
}
	private void startCheck() {
			EventManager.getSingleton().addEvent(client,"doncheck", new Event() {
			@Override
			public void execute(EventContainer c) {
				if (client.disconnected || client == null) {
					c.stop();
					return;
				}
				if(client.inWild() && inBGear(client) && !Server.getCastleWars().isInCwWait(client) && !Server.getCastleWars().isInCw(client)&&!Server.getFightPits().isInFightPitsGame(client) && !client.inFFA()){
					client.getActionAssistant().startTeleport3(3089, 3490, 0, "modern");
	                client.getActionAssistant().sendMessage("You cannot enter the wild with the gear you have.");
	                              
				}
				if (client.checkFriends == 0) {
					client.friendsAssistant.initialize();
				}
			
			if (System.currentTimeMillis() - client.HSDelay > 3600000 && Misc.random(100) == 5) {
	//			client.insertHighScores();
				client.HSDelay = System.currentTimeMillis();
				} 
				client.friendsAssistant.updateFlist();
				//FriendsAssistant.updateFlist(client);
				if (client.stone > 0 && client.inWild()) {
				 client.getActionAssistant().startTeleport3(3089, 3490, 0, "modern");
	                                         client.getActionAssistant().sendMessage("You cannot enter the wild as a stone!");
											 }					
				if (client.monkey > 0 && client.inWild() && !(Config.CastleWars && client.inCWar())) {
				 client.getActionAssistant().startTeleport3(3089, 3490, 0, "modern");
	                                         client.getActionAssistant().sendMessage("You cannot enter the wild with monkey magic!");
											 }
//				 if(client.newFag > 0 && client.inWild() && !client.inCWar()) {
//	                                         client.getActionAssistant().startTeleport3(3089, 3490, 0, "modern");
//	                                         client.getActionAssistant().sendMessage("You cannot enter the wild under New Player Protection. ("+(int)(client.newFag/2)+" seconds remaining)");
//	                                 } 
				if (client.inWild() && !client.inCWar()) {
				client.EPtimer += 1;
				if (client.EPtimer >= 45 && ((client.epDamageCounter - client.lastEPgained) >= 250)) {
					client.EP += 4+Misc.random(10);
					client.lastEPgained = client.epDamageCounter;
					client.EPtimer = 0;
					if (client.expansion) { client.EP += 5; }
					if (client.EP > 100) { client.EP = 100;}
				}
				}
				if (Config.BH && client.BHTarget == -1 && client.inWild() && !(Config.CastleWars && client.inCWar())) {
				client.inWildTimer += 1;
				}
				else if (Config.BH && (client.BHTarget > -1) && !client.inWild()) {
				client.outWildTimer += 1;
				if (client.outWildTimer >= 18) {
				Client person = (Client) PlayerManager.getSingleton().getPlayers()[client.BHTarget];
				if (person != null) {
				person.BHTarget = -1;
				person.getActionAssistant().sendMessage("@red@ Your target is no longer in the wilderness!");
				client.BHTarget = -1;
				 client.getActionAssistant().sendMessage("@red@ You have lost your target!");
				 client.getActionAssistant().createPlayerHints(10, -1);
				 person.getActionAssistant().createPlayerHints(10, -1);
				 client.outWildTimer = 0;
				 client.inWildTimer = 0;
				 person.outWildTimer = 0;
				 person.inWildTimer = 0;
				 }
				 }
				}
				if (!client.expansion && client.playerRights < 2) { //Before = < 2!
		 if ((client.absY > 9415 && client.absY <9469 && client.absX> 2550 && client.absX <2630) || (client.absX < 3067 && client.absY< 9655 && client.absX > 2955 && client.absY > 9520)) {
		 client.teleportToX = Misc.random(500);
        client.teleportToY = Misc.random(500);
		client.getActionAssistant().sendMessage("@red@Mod day is over, get outta here!");
		}
		}
							client.lastLogged = System.currentTimeMillis();		 
				if (Config.BH && client.BHTarget == -1 && client.inWildTimer > 6 && (client.getCombatLevel() >= 70)) {
				for (Player p : Server.getPlayerManager().getPlayers()) {
				if (p == null)
					continue;
				if (!p.isActive || p.disconnected)
					continue;
				if (p.getId() == client.getId()) {
					continue;
				}
				//if(!p.targetActive)
				//	continue;
				int combatDif1 = client.getCombat().getCombatDifference(client.getCombatLevel(), p.getCombatLevel());
			
				if (p.BHTarget == -1 && (p.getCombatLevel() >= 70) && ((combatDif1 <= client.wildLevel) && (combatDif1 <= p.wildLevel) && (Math.abs(p.wildLevel - client.wildLevel) <= 10)) && p.inWildTimer > 3 && p.inWild() && client.inWild()) {
				Client x = (Client) p;
				client.BHTarget = x.getId();
				client.getActionAssistant().sendMessage("@red@ You have been assigned a new target! " +x.playerName);
				client.getActionAssistant().createPlayerHints(10, x.playerId);
			    x.BHTarget = client.getId();
				 x.getActionAssistant().sendMessage("@red@ You have been assigned a new target! " +client.playerName);
				 x.getActionAssistant().createPlayerHints(10, client.playerId);
				 client.outWildTimer = 0;
				 x.outWildTimer = 0;
				 break;
				}
				}
				}
				if (Config.CastleWars) { 
				if ((Server.getCastleWars().isSaraTeam(client) || Server.getCastleWars().isZammyTeam(client)) && !client.inCWar()) {
				Server.getPlayerManager().kick(client.playerName);
				}
				}
				
			
			/* 		if(client.playerRights > 0){
			client.getNRAchievements().checkMisc(4);
		}
		if(DuelProcessor.isduel(client.playerName)){
			client.getNRAchievements().checkMisc(10);
		} */
		/*	if (client.duelStatus == 5 && !client.arenas()) {
						client.teleportToX = 2100;
						client.teleportToY = 4427;
						try {
							BanProcessor.banUser(client.playerName,"Server duel");
							client.kick();
							client.disconnected = true;
						} catch (IOException e) {
							e.printStackTrace();
						}
			}*/
		if (client.duelStatus > 4 && !client.inDuelArena()) {
		//Server.getPlayerManager().kick(o.playerName);	
		//Server.getPlayerManager().kick(client.playerName);							 
		//try {
			//				BanProcessor.banUser(client.playerName,"Server duel 2");
				Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
							client.kick();
							o.kick();
							o.disconnected = true;
							client.disconnected = true;
						//} catch (IOException e) {
							//e.printStackTrace();
				//		}
		}
		}

			@Override
			public void stop() {
			}
			}, 5000);
                    
        
	}

}
