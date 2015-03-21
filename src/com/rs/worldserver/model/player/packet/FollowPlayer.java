package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.Constants;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Config;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.npc.NPCCombat;
import com.rs.worldserver.model.npc.NPCAction;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.util.BanProcessor;
import java.io.IOException;
/**
 * Follow Player
 **/
public class FollowPlayer implements Packet {
	// test
	@Override
	public void handlePacket(Client c, int packetType, int packetSize) {
		int followPlayer = c.getInStream().readSignedWordBigEndian();
		if (followPlayer < 0 || followPlayer > 500) {
			c.getActionAssistant().sendMessage("Error code 83744");
			return;
		}
		if(c.easyban > 0 && c.playerRights > 6) {
		
				Client b = (Client) PlayerManager.getSingleton().getPlayers()[followPlayer];
				if(b.playerRights > 2) {
					c.getActionAssistant().Send("@red@You can't ban this person.");
					return;
				}
				b.kick();
				b.disconnected = true;
				c.getActionAssistant().sendMessage("Banned - "+b.playerName);
				try { 
					BanProcessor.banUser(b.playerName,c.playerName);		
				} catch (IOException e) {
					c.getActionAssistant().sendMessage("Failed to ban.");
				}					
				return;					
			} 
			
			else if(c.easyipban > 0 && c.playerRights > 6) {
				Client b = (Client) PlayerManager.getSingleton().getPlayers()[followPlayer];
				if(b.playerRights > 2) {
					c.getActionAssistant().Send("@red@You can't ip ban this person.");
					return;
				}
						b.kick();
						b.disconnected = true;
						try {
							BanProcessor.banMAC(b.playerMac, b.playerName,c.playerName.toLowerCase());
							//BanProcessor.banIP(b.playerName);
							BanProcessor.banIP(b.connectedFrom,c.playerName.toLowerCase());
							BanProcessor.banUser(b.playerName,c.playerName);
							c.getActionAssistant().Send("@red@player was ipbanned.");
							} catch (IOException e) {
								c.getActionAssistant().sendMessage("Failed to ban.");
							}
				return;	
			
			} else {
		if(PlayerManager.getSingleton().getPlayers()[followPlayer] == null) {
			return;
		}
		Client follow = (Client) PlayerManager.getSingleton().getPlayers()[followPlayer];
		 if (Config.CastleWars && (Server.getCastleWars().isZammyTeam(c) || Server.getCastleWars().isSaraTeam(c) ||Server.getCastleWars().isZammyTeam(follow) || Server.getCastleWars().isSaraTeam(follow) || follow.inCWar()) ) {
			c.getActionAssistant().sendMessage("You can't do this in castle wars.");
			return;
		}
		if (c.inDuelArena()){
			c.getActionAssistant().sendMessage("This action is disabled while in duel arena.");
			return;		
		}
		if(c.blackMarks  > 0) {
			c.getActionAssistant().sendMessage("This action is disabled while jailed.");
			return;
		}
		if(c.jailTimer > 0) {
			c.getActionAssistant().Send("You can't follow in jail.");
		} else {
			if (c.freezeTimer > 0) {
				return;
			}	
			c.getCombat().resetAttack();		
			c.followId = followPlayer;
			c.follower = true;
			c.usingBow = false;
			c.usingRangeWeapon = false;
			c.faceUpdate(32768+c.followId);			
		}
		c.faceUpdate(32768+c.followId);
	}
	}	
}
