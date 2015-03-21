package com.rs.worldserver.model.player.packet;

//Shard Revolutions Generic MMORPG Server
//Copyright (C) 2008  Graham Edgecombe

//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.

//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

import com.rs.worldserver.Constants;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.npc.NPCCombat;
import com.rs.worldserver.model.npc.NPCAction;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Config;
import java.io.*;
import com.rs.worldserver.util.BanProcessor;
/**
 * Attack packet handler
 * 
 * @author Graham
 * 
 */
public class Attack implements Packet {
	public static final int NPC = 72;
	public static final int MAGE_NPC = 131;
	public static final int PLAYER = 73;
	public static final int MAGE_PLAYER = 249;

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		// client.playerIndex = 0;
		// client.npcIndex = 0;
		// client.stopMoves = true;
		int npcid = 0;
		if(packetType==NPC) {
			npcid = client.getInStream().readUnsignedWordA();
			NPC npc = Server.getNpcManager().getNPC(npcid);
		if (client.getCombatManager().owedDouble) {
			return;
		}
		if (npc.petOwner != 0) {
			Client k = (Client)	PlayerManager.getSingleton().getPlayers()[npc.petOwner];
			if (k != null) {
				client.getActionAssistant().Send("You cannot attack "+k.playerName+"'s pet!");
			}
			else {	
				client.getActionAssistant().Send("You cannot attack another player's pet!");
			}
			return;
		}
		if (npc.killerId!=client.playerId)
				client.getCombat().resetAttack();
		}
		else {
			client.getCombat().resetAttack();
		}
		client.playerIndex = 0;
		client.npcIndex = 0;
		if(client.dHit){
			return;
		}
		
		client.stopMoves = true;
		if(!client.npcClick) return;
		switch(packetType) {
		
			case MAGE_NPC:
				if(!client.npcClick) break;
				if(client.glitchTimer > 0) {
					return;
				}
			// for(int p = 0; p < Shane3.length; p++){
				// if (client.getActionAssistant().playerHasItem((Shane3[p]),1) == true) {					
				// client.getActionAssistant().sendMessage("You cannot attack this monster with some of those items to prevent abuse");
				// return;
				// }
			// }				
			int attackNPC =client.getInStream().readSignedWordBigEndianA();
				
				NPC npc = Server.getNpcManager().getNPC(attackNPC);
				if(npc.definition.type == 14696){
							switch(npc.phase){
								case 0:
								case 1:
									client.getActionAssistant().Send("@red@You can not mage Ganodermic Beast in this phase!");
			
									client.getCombat().resetAttack();
								return;
							}
						}
		if (npc.petOwner != 0) {
			Client k = (Client)	PlayerManager.getSingleton().getPlayers()[npc.petOwner];
			if (k != null) {
				client.getActionAssistant().Send("You cannot attack "+k.playerName+"'s pet!");
			}
			else {	
				client.getActionAssistant().Send("You cannot attack another player's pet!");
			}
			return;
		}
			if(npc.underAttack2 && npc.killerId != 0 && npc.killerId != client.playerId && !client.inMulti()){
				client.getActionAssistant().Send("Someone else is already fighting your opponent.");
				client.faceUpdate(client.npcIndex);
				return;
			}
			
			if(!npc.underAttack2 && client.isFighting && npc.npcId != client.underAttackByNPC && !client.inMulti()){
				client.getActionAssistant().Send("I'm already under attack.");
				client.faceUpdate(client.npcIndex);
				return;
			}	
				client.npcIndex = attackNPC;
				client.usingMagic = false;
				int castingSpellId = client.getInStream().readSignedWordA();
				int type2 = client.npcIndex;
			
				if(Server.getNpcManager().getNPC(type2) == null){
					break;
				}

			/*	if (npc.definition.type == 596 || npc.definition.type == 2253 || npc.definition.type == 605 || npc.definition.type == 2102) {
				client.getActionAssistant().sendMessage("You can't attack this NPC anymore!");
					client.getCombat().resetAttack();
					return;
				}				
				if(((npc.definition.type == 1115) ||
				   (npc.definition.type == 3790) ||
				   (npc.definition.type == 3791) ||
				   (npc.definition.type == 3792)) && (client.absX < 2864)){
					client.getActionAssistant().sendMessage("You can't attack this NPC from here!");
					client.getCombat().resetAttack();
					return;
				}
				else if(((npc.definition.type == 3810) ||
				   (npc.definition.type == 774) ||
				   (npc.definition.type == 772) ||
				   (npc.definition.type == 773)) && (client.absY > 5331)){
					client.getActionAssistant().sendMessage("You can't attack this NPC from here!");
					client.getCombat().resetAttack();
					return;
				}
				else if(((npc.definition.type == 3811) ||
				   (npc.definition.type == 765) ||
				   (npc.definition.type == 764) ||
				   (npc.definition.type == 763)) && (client.absY < 5296)){
					client.getActionAssistant().sendMessage("You can't attack this NPC from here!");
					client.getCombat().resetAttack();
					return;
				}
				else if(((npc.definition.type == 3783) ||
				   (npc.definition.type == 769) ||
				   (npc.definition.type == 770) ||
				   (npc.definition.type == 771)) && (client.absX > 2907)){
					client.getActionAssistant().sendMessage("You can't attack this NPC from here!");
					client.getCombat().resetAttack();
					return;
				}
				else if ((npc.definition.type == 3818) && (client.playerLevel[18] < 83)) {
					client.getActionAssistant().sendMessage("You need 83 slayer to attack this monster!");
					client.getCombat().resetAttack();
					return;
				}*/
				
		
				if(npc.hp <= 0){
					client.getActionAssistant().sendMessage("You can't attack this NPC.");
					client.getCombat().resetAttack();
					break;
				}	
				if(!client.getCombat().checkNpcReqs()) {
					client.getCombat().resetAttackVsNpc();
					return;
				}							
				for(int i = 0; i < client.MAGIC_SPELLS.length; i++){
					if(castingSpellId == client.MAGIC_SPELLS[i][0]) {
						client.spellId = i;
						client.usingMagic = true;
						client.attackTimer = 0;
						break;
					}
				}	
				if(client.usingMagic) {

					if(client.goodDistance(client.getAbsX(), client.getAbsY(), Server.getNpcManager().getNPC(client.npcIndex).getAbsX(), Server.getNpcManager().getNPC(client.npcIndex).getAbsY(), 6)) {
						client.stopMovement();
					}
				if(client.autoCast) {
					client.usingMagic = true;
					client.stopMovement();
				}			
				if(!client.getCombatManager().checkMagicReqs(client.spellId)) {
					client.stopMovement();
					client.attackTimer += 8000;
					//client.getCombat().resetAttack();
					return;
				}			
				if(client.autoCast && (client.AutoCast_SpellIndex != castingSpellId) ) {
					client.newSpell = true;
				}				
					client.getCombat().attackNPC(client.npcIndex);
				}
			break;
			case NPC:
				if(!client.npcClick) break;
				if(client.castingMagic) break;
				if(client.glitchTimer > 0) {
					return;
				}			
						
				int attacknpc = npcid;
				NPC npc2 = Server.getNpcManager().getNPC(attacknpc);
				/*client.getActionAssistant().Send("1 " +npc2.underAttack2);
				client.getActionAssistant().Send("2 " +npc2.killerId);
				client.getActionAssistant().Send("3 " +client.playerId);*/
		if (npc2.petOwner != 0) {
			Client k = (Client)	PlayerManager.getSingleton().getPlayers()[npc2.petOwner];
			if (k != null) {
				client.getActionAssistant().Send("You cannot attack "+k.playerName+"'s pet!");
			}
			else {	
				client.getActionAssistant().Send("You cannot attack another player's pet!");
			}
			return;
		}
			if(npc2.underAttack2 && npc2.killerId != 0 && npc2.killerId != client.playerId && !client.inMulti()){
				client.getActionAssistant().Send("Someone else is already fighting your opponent .");
				client.faceUpdate(client.npcIndex);
				client.getCombat().resetAttackVsNpc();
				return;
			}
			if(!npc2.underAttack2 && client.isFighting && npc2.npcId != client.underAttackByNPC && !client.inMulti()){
				client.getActionAssistant().Send("I'm already under attack.");
				client.faceUpdate(client.npcIndex);
				client.getCombat().resetAttackVsNpc();
				return;
			}		
				client.npcIndex=attacknpc;
				if(npc2.hp <= 0){
					client.getActionAssistant().sendMessage("You can't attack this NPC.");
					break;
				}
				/*if(((npc2.definition.type == 1115) ||
				   (npc2.definition.type == 3790) ||
				   (npc2.definition.type == 3791) ||
				   (npc2.definition.type == 3792)) && (client.absX < 2864)){
					client.getActionAssistant().sendMessage("You can't attack this NPC from here!");
					client.getCombat().resetAttack();
					return;
				}
				  // if((npc2.definition.type == 1115) && (client.bandosPoints < 0) ||
				   // (npc2.definition.type == 3790) && (client.bandosPoints < 0) ||
				   // (npc2.definition.type == 3791) && (client.bandosPoints < 0) ||
				   // (npc2.definition.type == 3792) && (client.bandosPoints < 0)){
					// client.getActionAssistant().sendMessage("You can't attack this NPC without 20 kills!");
					// client.getCombat().resetAttack();
					// return;
				// }
				
			
				else if(((npc2.definition.type == 3810) ||
				   (npc2.definition.type == 774) ||
				   (npc2.definition.type == 772) ||
				   (npc2.definition.type == 773)) && (client.absY > 5331)){
					client.getActionAssistant().sendMessage("You can't attack this NPC from here!");
					client.getCombat().resetAttack();
					return;
				}
				// if((npc2.definition.type == 3810) && (client.zamPoints < 0) ||
				   // (npc2.definition.type == 774) && (client.zamPoints < 0) ||
				   // (npc2.definition.type == 772) && (client.zamPoints < 0) ||
				   // (npc2.definition.type == 773) && (client.zamPoints < 0)){
					// client.getActionAssistant().sendMessage("You can't attack this NPC without 20 kills!");
					// client.getCombat().resetAttack();
					// return;
				// }
				
				else if(((npc2.definition.type == 3811) ||
				   (npc2.definition.type == 765) ||
				   (npc2.definition.type == 764) ||
				   (npc2.definition.type == 763)) && (client.absY < 5296)){
					client.getActionAssistant().sendMessage("You can't attack this NPC from here!");
					client.getCombat().resetAttack();
					return;
				}
				else if(((npc2.definition.type == 3783) ||
				   (npc2.definition.type == 769) ||
				   (npc2.definition.type == 770) ||
				   (npc2.definition.type == 771)) && (client.absX > 2907)){
					client.getActionAssistant().sendMessage("You can't attack this NPC from here!");
					client.getCombat().resetAttack();
					return;
				}
				else if ((npc2.definition.type == 3818) && (client.playerLevel[18] < 83)) {
					client.getActionAssistant().sendMessage("You need 83 slayer to attack this monster!");
					client.getCombat().resetAttack();
					return;
				}
				
				// if((npc2.definition.type == 3783) && (client.saraPoints < 0) ||
				   // (npc2.definition.type == 769) && (client.saraPoints < 0) ||
				   // (npc2.definition.type == 770) && (client.saraPoints < 0) ||
				   // (npc2.definition.type == 771) && (client.saraPoints < 0)){
					// client.getActionAssistant().sendMessage("You can't attack this NPC without 20 kills!");
					// client.getCombat().resetAttack();
					// return;
				// }
				// if((npc2.definition.type == 3811) && (client.armaPoints < 0) ||
				   // (npc2.definition.type == 765) && (client.armaPoints < 0) ||
				   // (npc2.definition.type == 764) && (client.armaPoints < 0) ||
				   // (npc2.definition.type == 763) && (client.armaPoints < 0)){
					// client.getActionAssistant().sendMessage("You can't attack this NPC without 20 kills!");
					// client.getCombat().resetAttack();
					// return;
				// }*/
				
				if(!client.getCombat().checkNpcReqs()) {
					client.getCombat().resetAttackVsNpc();
					return;
				}					
				client.usingMagic = false;
			boolean usingBow= false;
				boolean usingArrows =  false;
				boolean usingOtherRangeWeapons =  false;
				for (int bowId : client.BOWS) {
							if(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == bowId) {
								//c.tempusingBow = true;
								 usingBow = true;
								for (int arrowId : client.ARROWS) {
									if(client.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == arrowId) {
									//	c.tempusingArrows = true;

			usingArrows = true;
									}
								}
							}
						}				
					
						for (int otherRangeId : client.OTHER_RANGE_WEAPONS) {
							if(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == otherRangeId) {
							//	c.tempusingOtherRangeWeapons = true;
								usingOtherRangeWeapons = true;
							}
						}	
			if(usingBow && client.goodDistance(client.getX(), client.getY(), Server.getNpcManager().getNPC(client.npcIndex).getAbsX(), Server.getNpcManager().getNPC(client.npcIndex).getAbsY(), 10)) {
				client.usingBow = true;
				client.stopMovement();
			}
			
			if(usingOtherRangeWeapons && client.goodDistance(client.getX(), client.getY(), Server.getNpcManager().getNPC(client.npcIndex).getAbsX(), Server.getNpcManager().getNPC(client.npcIndex).getAbsY(), 10)) {
				client.usingRangeWeapon = true;
				client.stopMovement();
			}
				if(client.autoCast) {
					client.usingMagic = true;
					client.stopMovement();
				}
			if(!usingArrows && usingBow && ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] < 4212 || client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > 4223) && client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 20171)) {
				client.getActionAssistant().sendMessage("You have run out of arrows.");
				break;
			} 
			/*if(client.getCombat().usingHally() && client.goodDistance(client.getX(), client.getY(), Server.getNpcManager().getNPC(client.npcIndex).getAbsX(), Server.getNpcManager().getNPC(client.npcIndex).getAbsY(), 4)) {
				client.stopMovement();
			}	*/		
				if(usingBow){
				if (!((client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > 4219 && client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] < 4225) || client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 20171|| client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4212)) {
					client.getCombat().correctBowAndArrows();
					if(!client.bowlevel){
						client.getActionAssistant().sendMessage("You can't use "+Server.getItemManager().getItemDefinition(client.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS]).getName().toLowerCase()+"s with a "+Server.getItemManager().getItemDefinition(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]).getName().toLowerCase()+".");
						client.npcIndex = 0;
						return;					
					}
					}
				}		
		
			/*if(client.getCombat().correctBowAndArrows() > client.playerEquipment[PlayerConstants.PLAYER_WEAPON] && Config.CORRECT_ARROWS && usingBow) {
				client.getActionAssistant().sendMessage("You can't use "+Server.getItemManager().getItemDefinition(client.playerEquipment[PlayerConstants.PLAYER_ARROWS]).getName().toLowerCase()+"s with a "+Server.getItemManager().getItemDefinition(client.playerEquipment[PlayerConstants.PLAYER_WEAPON]).getName().toLowerCase()+".");
				client.npcIndex = 0;
				break;
			}		*/						
				client.getCombatManager().attackNPC(attacknpc);
			break;
			case PLAYER:
				if(!client.npcClick) break;
				//if(client.castingMagic) break;	
				// if(client.newFag > 0){
					// client.getActionAssistant().Send("You can not Pk under New Player Protection.");
					// break;
				// }	


					if (client.getCombatManager().owedDouble || client.dHit || client.getCombatManager().doubleWait) {
						 return;
					}
					//if (System.currentTimeMillis() - client.lastAttack < 1000 ) { break; }
					//client.lastAttack =System.currentTimeMillis();
					
				if(client.inWild()){
				if (client.duelStatus < 4 && !Server.getFightPits().isInFightPitsGame(client) && !Server.getCastleWars().isSaraTeam(client) && !Server.getCastleWars().isZammyTeam(client)) {
					if((client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == -1) || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == -1) ||
						(client.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == -1) ||
						(client.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == -1)) {
						client.getActionAssistant().Send("You need a body, leg's, weapon, and amulet to attack someone.");
						break;
					}
					}
				}
				client.oldTempIndex = client.playerIndex;
				client.playerIndex = client.getInStream().readSignedWordBigEndian();
				//if (client.oldTempIndex == client.playerIndex) { return; }
				Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.playerIndex];
				// if (client.absX == o.absX && client.absY == o.absY && client.freezeTimer > 0) {
						// client.getCombat().resetAttack();
						// break;
				// }
				// if(o.newFag > 0){
					// o.getActionAssistant().Send("You can not Pk under New Player Protection.");
					// client.getActionAssistant().Send("You can not Pk a person under New Player Protection.");
					// break;
				// }				
				if(!client.getCombat().checkReqs()) {
					client.getCombat().resetAttack();
					return;
				}	
				if(o == null){
					break;
				}
				if(client.respawnTimer > 0) {
					break;
				}
			
				client.usingMagic = false;
				boolean usingBow2= false;
				boolean usingArrows2 =  false;
				boolean usingOtherRangeWeapons2 =  false;
				for (int bowId : client.BOWS) {
							if(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == bowId) {
								//c.tempusingBow = true;
								 usingBow2 = true;
								for (int arrowId : client.ARROWS) {
									if(client.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == arrowId) {
									//	c.tempusingArrows = true;

			usingArrows2 = true;
									}
								}
							}
						}				
					
						for (int otherRangeId : client.OTHER_RANGE_WEAPONS) {
							if(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == otherRangeId) {
							//	c.tempusingOtherRangeWeapons = true;
								usingOtherRangeWeapons2 = true;
							}
						}
							
	if(client.autoCast || client.autoCastMarker) {
	if (client.autoCastMarker) {
	client.autoCast = true;
	}
					client.usingMagic = true;
					client.stopMovement();
				}	
				//int castingSpellId = client.getInStream().readSignedWordA();
								
				if(usingBow2 && !usingOtherRangeWeapons2 && client.goodDistance(client.getX(), client.getY(), o.getAbsX(), o.getAbsY(), 6)) {
					client.usingBow = true;
					client.stopMovement();
				} else if (usingBow2) {
				
				}
				if(usingOtherRangeWeapons2 && !usingBow2 && client.goodDistance(client.getX(), client.getY(), o.getAbsX(), o.getAbsY(), 6)) {
					client.usingRangeWeapon = true;
					client.stopMovement();
				} else if (usingOtherRangeWeapons2) {
					
				}
				if(!usingArrows2 && usingBow2 && ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] < 4212 || client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > 4223) && client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 20171)) {
					client.getActionAssistant().sendMessage("You have run out of arrows.");
					break;
				} 
				if(usingBow2){
				if ((!((client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > 4219 && client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] < 4225) || client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 20171 || client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4212)) && client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 4212) {
					client.getCombat().correctBowAndArrows();
					if(!client.bowlevel){
						if(client.duelRule[16]){
							usingBow2 = false;
							usingOtherRangeWeapons2 = false;
							client.getActionAssistant().sendMessage("@red@Hrm... that shouldn't have happened?");
							client.getCombat().resetAttack();
							client.tempusingBow = false;
							return;	
						}
							client.getActionAssistant().sendMessage("You can't use "+Server.getItemManager().getItemDefinition(client.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS]).getName().toLowerCase()+"s with a "+Server.getItemManager().getItemDefinition(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]).getName().toLowerCase()+".");
							client.playerIndex = 0;
							return;				
						}
					}
				}			
					if(client.duelStatus == 5) {
					if(client.duelCount > 0) {
						client.getActionAssistant().sendMessage("The duel hasn't started yet!");
						client.getCombat().resetAttack();
						client.getCombat().resetAttack2();
						return;
					}
					if(client.duelRule[2] && (usingBow2 || usingOtherRangeWeapons2)) {
						client.getActionAssistant().sendMessage("Range has been disabled in this duel!");
						client.getCombat().resetAttack();
						return;
					}
					if(client.duelRule[3] && (!usingBow2 && !usingOtherRangeWeapons2)) {
						client.getActionAssistant().sendMessage("Melee has been disabled in this duel!");
						client.getCombat().resetAttack();
						return;
					}
					if(o.duelingWith != client.getId()){
						client.getActionAssistant().sendMessage("@red@That is not your opponent!");
						client.getCombat().resetAttack();
						return;
					}
							if (client.duelStatus >4) {
				if (client.playerIndex == client.duelingWith) {
				if (!usingOtherRangeWeapons2 && !usingBow2) {
				//client.getActionAssistant().Send("true");
				break;
				}}
				}
				}
				if(Server.getFightPits().isInFightPitsGame(client) && !Server.getFightPits().canAttack){
					client.getActionAssistant().Send("@red@You can not attack yet!");
					client.getCombat().resetAttack();
					break;
				}
				if (Config.CastleWars) {
								if (Server.getCastleWars().isZammyTeam(client) ) {
				if (!Server.getCastleWars().isSaraTeam(o)) {
						client.getActionAssistant().Send("@red@This player is on your team!");
						client.getCombat().resetAttack();
						break;
						}
						}
							if (Server.getCastleWars().isSaraTeam(client) ) {
				if (!Server.getCastleWars().isZammyTeam(o)) {
						client.getActionAssistant().Send("@red@This player is on your team!");
						client.getCombat().resetAttack();
						break;
						}
						}
						if (Server.getCastleWars().isInCwWait(client)) {
						client.getActionAssistant().Send("@red@You can't attack here!");
						client.getCombat().resetAttack();
						break;
						}
						if (Server.getCastleWars().isInCwWait(o)) {
						client.getActionAssistant().Send("@red@You can't attack here!");
						client.getCombat().resetAttack();
						break;
						}
						if ((client.isInZamSafe() || client.isInSaraSafe() )&& client.heightLevel == 1) {
		client.actionAssistant.sendMessage("You can't attack from a safe zone!");
		break;
		}
		if ((o.isInZamSafe() && o.heightLevel == 1)|| (o.isInSaraSafe() && o.heightLevel == 1)) {
		client.actionAssistant.sendMessage("You can't attack a player in a safe zone!");
		break;
		}
						}
				// if(!client.getCombat().checkReqs()) {
					// client.getCombat().resetAttack();
					// return;
				// }				
				o.underAttackBy = client.playerId;
				client.getCombat().attackPlayer(client.playerIndex);
			break;
			case MAGE_PLAYER:
				if(!client.npcClick) break;
				if(client.autoCast) {
					client.autoCast = false;
					client.autoCastMarker = true;
				}	
				if(Server.getFightPits().isInFightPitsGame(client) && !Server.getFightPits().canAttack){
					client.getActionAssistant().Send("@red@You can not attack yet!");
					client.getCombat().resetAttack();
					break;
				}
				// if(client.newFag > 0){
					// client.getActionAssistant().Send("You can not Pk under New Player Protection.");
					// break;
				// }				
			//	if(client.inWild()){
			if (client.duelStatus < 4 && !Server.getCastleWars().isSaraTeam(client) && !Server.getCastleWars().isZammyTeam(client) ) {
					if((client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == -1) || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == -1) ||
						(client.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == -1) ||
						(client.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == -1)) {
						client.getActionAssistant().Send("You need a body, leg's, weapon, and amulet to attack someone.");
						break;
					}
					}
			//	}		
				client.playerIndex = client.getInStream().readSignedWordA();
				Client targetPlayer = (Client) PlayerManager.getSingleton().getPlayers()[client.playerIndex];
				// if (client.absX == targetPlayer.absX && client.absY == targetPlayer.absY && client.freezeTimer > 0) {
						// client.getCombat().resetAttack();
						// break;
				// }
				if(Server.getFightPits().isInFightPitsGame(client) && !Server.getFightPits().canAttack){
					client.getActionAssistant().Send("@red@You can not attack yet!");
					client.getCombat().resetAttack();
					break;
				}
				// if(targetPlayer.newFag > 0){
					// targetPlayer.getActionAssistant().Send("You can not Pk under New Player Protection.");
					// client.getActionAssistant().Send("You can not Pk a person under New Player Protection.");
					// break;
				// }				
				if(!client.getCombat().checkReqs()) {
					client.getCombat().resetAttack();
					return;
				}	
				// if (((targetPlayer.absX > 3186 && targetPlayer.absX < 3195 && targetPlayer.absY >3957 && targetPlayer.absY <3963) &&
				// (client.absX <3187 || client.absX >3194 || client.absY >3962 || client.absY <3958)) ||
				// ((client.absX > 3186 && client.absX < 3195 && client.absY >3957 && client.absY <3963) &&
				// (targetPlayer.absX <3187 || targetPlayer.absX >3194 || targetPlayer.absY >3962 ||targetPlayer.absY <3958))) {
					// client.getCombat().resetAttack();
					// break;
				// }				
				// if (((targetPlayer.absX > 3037 && targetPlayer.absX < 3045 && targetPlayer.absY >3948 && targetPlayer.absY <3960) &&
				// (client.absX <3038 || client.absX >3044 || client.absY >3959 || client.absY <3949)) ||
				// ((client.absX > 3037 && client.absX < 3045 && client.absY >3948 && client.absY <3960) &&
				// (targetPlayer.absX <3038 || targetPlayer.absX >3044 || targetPlayer.absY >3959 ||targetPlayer.absY <3949))) {
					// client.getCombat().resetAttack();
					// break;
				// }		
				int castingSpellId2 = client.getInStream().readSignedWordBigEndian();
				if(targetPlayer == null){
					break;
				}
				if(client.respawnTimer > 0) {
					break;
				}
					if (!client.getActionAssistant().checkSpellId(client, castingSpellId2)) {
						
						//client.sqlLog(client.playerName.toLowerCase(), 1);
						System.out.println("ID "+castingSpellId2);
						try {
						BanProcessor.banUser(client.playerName,"Server using wrong spell book");
						client.kick();
						client.disconnected = true;
						//bankTrade(client.playerName);
						} catch (IOException e) {
						}
				}
			if(client.duelStatus == 5) {	
					if(client.duelCount > 0) {
						client.getActionAssistant().sendMessage("The duel hasn't started yet!");
						client.getCombat().resetAttack();
						return;
					}
					if(client.duelRule[4]) {
						client.getActionAssistant().sendMessage("Magic has been disabled in this duel!");
						client.getCombat().resetAttack();
						return;
					}
				}			
				for(int i = 0; i < client.MAGIC_SPELLS.length; i++){
					if(castingSpellId2 == client.MAGIC_SPELLS[i][0]) {
						client.spellId = i;
						client.usingMagic = true;
						break;
					}
				}
				if(!client.getCombatManager().checkMagicReqs(client.spellId)) {
					client.stopMovement();
					client.getCombat().resetAttack();
					client.attackTimer += 2000;
					client.reCast = 5;
					break;
				}
				if (Config.CastleWars) {
					
					if (Server.getCastleWars().isZammyTeam(client) ) {
						if (!Server.getCastleWars().isSaraTeam(targetPlayer)) {
							client.getActionAssistant().Send("@red@This player is on your team!");
							client.stopMovement();
							client.getCombat().resetAttack();
							client.attackTimer += 2000;
							client.reCast = 5;
							break;
						}
					}
				if (Server.getCastleWars().isSaraTeam(client) ) {
					if (!Server.getCastleWars().isZammyTeam(targetPlayer)) {
						client.getActionAssistant().Send("@red@This player is on your team!");
						client.stopMovement();
						client.getCombat().resetAttack();
						client.attackTimer += 2000;
						client.reCast = 5;
						break;
					}
				}
				if (Server.getCastleWars().isInCwWait(targetPlayer)) {
						client.getActionAssistant().Send("@red@You can't attack here!");
						client.stopMovement();
						client.getCombat().resetAttack();
						client.attackTimer += 2000;
						client.reCast = 5;
						break;
					}
					if ((client.isInZamSafe() || client.isInSaraSafe() )&& client.heightLevel == 1) {
		client.actionAssistant.sendMessage("You can't attack from a safe zone!");
		break;
		}
		if ((targetPlayer.isInZamSafe() && targetPlayer.heightLevel == 1)|| (targetPlayer.isInSaraSafe() && targetPlayer.heightLevel == 1)) {
		client.actionAssistant.sendMessage("You can't attack a player in a safe zone!");
		break;
		}
				}

				if(client.usingMagic) {
					if(client.goodDistance(client.getAbsX(), client.getAbsY(), targetPlayer.getAbsX(), targetPlayer.getAbsY(), 10)) {
						client.stopMovement();
					} else {
						
						
					}
					// if(!client.getCombat().checkReqs()) {
						// client.getCombat().resetAttack();
						// return;
					// }					
					targetPlayer.underAttackBy = client.playerId;
					client.getCombat().attackPlayer(client.playerIndex);
					
				}
			break;
		}
	}
}
