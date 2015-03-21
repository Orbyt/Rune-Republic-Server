package com.rs.worldserver.model.player;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Config;

public class AutoRetaliate {
	private Client client;
	public AutoRetaliate(Client c){
		this.client = c;
	}
	

public void retaliate(int id) {	
	if(client == null)
		return;
	if(client.inWild()){
				if (client.duelStatus < 4 && !Server.getCastleWars().isSaraTeam(client) && !Server.getCastleWars().isZammyTeam(client)) {
					if((client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == -1) || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == -1) ||
						(client.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == -1) ||
						(client.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == -1)) {
						client.getActionAssistant().Send("You need a body, leg's, weapon, and amulet to attack someone.");
						return;
					}
					}
				}
				
				client.playerIndex = id;
				Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.playerIndex];
				// if (client.absX == o.absX && client.absY == o.absY && client.freezeTimer > 0) {
						// client.getCombat().resetAttack();
						// return;
				// }
				// if(o.newFag > 0){
					// o.getActionAssistant().Send("You can not Pk under New Player Protection.");
					// client.getActionAssistant().Send("You can not Pk a person under New Player Protection.");
					// return;
				// }				
				if(!client.getCombat().checkReqs()) {
					client.getCombat().resetAttack();
					return;
				}
				if(o == null){
					return;
				}
				if(client.respawnTimer > 0) {
					return;
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
				if(!usingArrows2 && usingBow2 && (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] < 4212 || client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > 4223)) {
					client.getActionAssistant().sendMessage("You have run out of arrows.");
					return;
				} 
				if(usingBow2){
				if (!(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > 4219 && client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] < 4225) && client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 4212) {
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
				return;
				}}
				}
				}
				if (Config.CastleWars) {
								if (Server.getCastleWars().isZammyTeam(client) ) {
				if (!Server.getCastleWars().isSaraTeam(o)) {
						client.getActionAssistant().Send("@red@This player is on your team!");
						client.getCombat().resetAttack();
						return;
						}
						}
							if (Server.getCastleWars().isSaraTeam(client) ) {
				if (!Server.getCastleWars().isZammyTeam(o)) {
						client.getActionAssistant().Send("@red@This player is on your team!");
						client.getCombat().resetAttack();
						return;
						}
						}
						if (Server.getCastleWars().isInCwWait(client)) {
						client.getActionAssistant().Send("@red@You can't attack here!");
						client.getCombat().resetAttack();
						return;
						}
						if (Server.getCastleWars().isInCwWait(o)) {
						client.getActionAssistant().Send("@red@You can't attack here!");
						client.getCombat().resetAttack();
						return;
						}
						if ((client.isInZamSafe() || client.isInSaraSafe() )&& client.heightLevel == 1) {
		client.actionAssistant.sendMessage("You can't attack from a safe zone!");
		return;
		}
		if ((o.isInZamSafe() && o.heightLevel == 1)|| (o.isInSaraSafe() && o.heightLevel == 1)) {
		client.actionAssistant.sendMessage("You can't attack a player in a safe zone!");
		return;
		}
						}
				// if(!client.getCombat().checkReqs()) {
					// client.getCombat().resetAttack();
					// return;
				// }				
				o.underAttackBy = client.playerId;
				client.getCombat().attackPlayer(client.playerIndex);
			return;
			
	}
	
}
