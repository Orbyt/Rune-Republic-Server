package com.rs.worldserver.model.npc;

import java.util.Random;
import java.util.List;
import java.util.Map;

import com.rs.worldserver.Server;
import com.rs.worldserver.Constants;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.CombatManager;
import com.rs.worldserver.model.FloorItem;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;
import com.rs.worldserver.world.AnimationManager;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.npc.*;
import com.rs.worldserver.model.player.ActionAssistant;

/**
 * Handles anything to do with NPC combat.
 * @author Jonas(++)
 * @author mad turnip - completly modified
 */

public class NPCCombat {
/*
	public NPCCombat(){
		// Starting events for player and npc combat
		final NPC npc = client.targetNPC;
		startPlayerOnNpc();
		startNpcOnPlayer();
	}
	public void startNpcOnPlayer(){
		EventManager.getSingleton().addEvent(null, new Event() {
			
			@Override
			public void execute(EventContainer container) {
				for (Map.Entry<Integer, NPC> entry : Server.getNpcManager().npcMap.entrySet()) {
					NPC n = entry.getValue();
					npcAtkPlayer(n);
				}
			}
			
			@Override
			public void stop() {
				// Nothing has to be done.
			}
			
		}, 500);	
	}
	public void startPlayerOnNpc(){
		EventManager.getSingleton().addEvent(null, new Event() {
			
			@Override
			public void execute(EventContainer container) {
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if(p == null)
						continue;
					Client c = (Client) p;
					if(c.targetNPC != null)
						playerAtkNpc(c);
				}
				
			}
			
			@Override
			public void stop() {
				// Nothing has to be done.
			}
			
		}, 500);	
	}
	public void resetAtk(NPC n){
		n.setAttacker(null);
		n.setWalking(n.isWasWalking());
	}
	public void resetAtk(Client c){
		c.targetNPC = null;
	}
	
	private static final Random r = new Random();
	
	
	public void playerAtkNpc(final Client client){
		if(client.atkTimer > 0){
			client.atkTimer--;
			return;
		}
	
		final NPC npc = client.targetNPC;
		if(npc == null){
			return;
		}
		// Check if we died, teleported.
		if (client.isDead() || client.didTeleport) {
			resetAtk(client);
			resetAtk(npc);
			return;
		}
		
		// Check if the NPC is dead.
		if(npc.isDead()) {
			resetAtk(client);
			resetAtk(npc);
			return;
		}
		
		// Face to the npc.
		int k = 0/*npc.getDefinition().sizeX*//*;//if you add npc sizes
		/*client.getActionAssistant().turnTo(client,npc.getAbsX()+k, npc.getAbsY()+k);
		
		client.atkTimer = 3;//we have client attack timer here
		
		if(client.autocastId > 1000){// i ahve included a small base for autocasting so its done right this time
		
			//Magic.castSpellNPC(client.autocastId, client, npc);//this is magic on npc
			
		} else { // We're now using melee.			
			//TODO: specials			
			
			// Check if we're close enough.

			int i = 0/*(npc.getDefinition().sizeX)*//*;//distance checks with npc size again you got to add npc sizes
		/*	if(Misc.distance(npc.getAbsX()+i,npc.getAbsY()+i,client.getAbsX(),client.getAbsY()) <= (1 + i)){
				//stop movement
				client.stopMovement();//stop movement because we dont wanna be on top of the npc duh
			} else {
				return;
			}

			// Show the animation.
			client.getActionAssistant().startAnimation(client,0x326);			
			
			// Start a new event for a delay.
			EventManager.getSingleton().addEvent(null, new Event() {//hit delay cause i aint god and it takes time to swing your wepon duh
				
				@Override
				public void execute(EventContainer container) {
					// Check if the NPC still exist.
					if(npc == null || client.disconnected) {
						container.stop();
						return;
					}
					
					npc.faceTo(client.playerId);//npc faces us
		
					if (npc.getAttacker() == null) {//npc fights back
						npc.setAttacker(client);
					}
					
					// Calculate the hit.
					CombatManager.calculateMaxHit(client);//we get max hit
					int hit = (int) Math.floor(Math.random() * (double)  client.playerMaxHit);//random value of maxhit
					
					// Show the hit on the NPC.
					npc.hit(client, hit, 1);
					
					// Show defend animation on the NPC.
					if (npc.getAttacker() == client) {
						npc.setAnimNumber(404);
						npc.setAnimUpdateRequired(true);
						npc.setUpdateRequired(true);
					}
					
					client.getActionAssistant().addSkillXP(10, 0);
					client.getActionAssistant().addSkillXP(11, 1);
					client.getActionAssistant().addSkillXP(12, 2);
					client.getActionAssistant().addSkillXP(13,SkillConstants.HITPOINTS);
					
					// Stop the event.
					container.stop();
				}
				
				@Override
				public void stop() {
					// Nothing has to be done.
				}
				
			}, 500);	
		}
	}
	
	public void playerSetNpc(Client c,int npcId){
		final NPC npc = Server.getNpcManager().getNPC(npcId);//this is what we do once the player sends attack npc packet
		if(npc != null)
			npc.getAttackers().put(c, 0);
		client.targetNPC = npc;
	}
	
	public void agressiveNPC(NPC npc){
		for (Player p : Server.getPlayerManager().getPlayers()) {
			if(p == null)
				continue;
			Client client = (Client) p;
			if(client.getCombatLevel() / 2 > npc.getDefinition().getCombat() && (npc.getDefinition().getType() < 2627 && npc.getDefinition().getType() > 2745) ){
				continue;
			}
			if(client.getHeightLevel() == npc.getHeightLevel()){
				int k = /*npc.getDefinition().sizeX*//*0;//npc sizes removed again lol
		/*		if(Misc.distance(npc.getAbsX()+k,npc.getAbsY()+k,client.getAbsX(),client.getAbsY()) <= npc.agressive+k){
					npc.setAttacker(client);
					return;
				}
			}
		}
	}
	
	public void npcAtkPlayer(final NPC npc){
		// Get the NPCId.
		final int npcId = npc.getDefinition().getType();
		final Client client = npc.getAttacker();
		// Check if the NPC exists.
		if (npc == null)
			return;
		
		if(npc.atkTimer > 0){//we use this old shit timer lolz a long would be better but oh well lol
			npc.atkTimer--;
			return;
		}
		if(npc.getAttacker() == null){
			if(npc.agressive > 0){
				agressiveNPC(npc);
			}
			return;
		}
		npc.atkTimer = 3;
		
		// Check if the NPC died.
		if(npc.isDead()){
			resetAtk(npc);
			return;
		}
		if(client.getHeightLevel() != npc.getHeightLevel()){
			resetAtk(npc);
			return;
		}	
		// Face the NPC to the player.
		if (npc.getAttacker() == client)
			npc.faceTo(client.playerId);
		int k = 0/*npc.getDefinition().sizeX*/;//npc size removed again lol
	/*	int dis = Misc.distance(npc.getAbsX()+k,npc.getAbsY()+k,client.getAbsX(),client.getAbsY());
		
		// Check if the NPC is close enough.
		if(dis > 15){
			resetAtk(npc);
			return;
		}
	
		// Start the animation.
		npc.setAnimNumber(0x326);
		npc.setAnimUpdateRequired(true);
		npc.setUpdateRequired(true);		

		EventManager.getSingleton().addEvent(null, new Event() {
			
			@Override
			public void execute(EventContainer container) {
				// Check if the NPC still exist
				if(npc == null || client.isDead() || client.disconnected) {
					container.stop();
					return;
				}
				// Face the NPC to the player.
				npc.faceTo(client.playerId);
				
				// Calculate the hit.
				int hit =(int) Math.floor(Math.random()	* (double) /*npc.getDefinition().getMaximumHit()*//*10);//npc max damg 10?
	/*			client.getActionAssistant().hit(client,hit, 1);
				// Show the defend animation on the player.
				client.getActionAssistant().startAnimation(client,404);
				
				// Stop the event.
				container.stop();
			}
			
			@Override
			public void stop() {
				// Nothing has to be done.
			}
			
		}, 750);	
	}*/

}