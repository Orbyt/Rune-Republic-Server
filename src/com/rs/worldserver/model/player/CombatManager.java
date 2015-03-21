package com.rs.worldserver.model.player;

import java.util.HashMap;
import java.util.Map;

import com.rs.worldserver.Constants;
import com.rs.worldserver.*;
import com.rs.worldserver.Config;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.events.*;
import com.rs.worldserver.model.FloorItem;
import com.rs.worldserver.model.ItemDefinition;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.npc.NPCKiller;
import com.rs.worldserver.model.npc.NPCDefinition;
import com.rs.worldserver.world.AnimationManager;
import com.rs.worldserver.world.StillGraphicsManager;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.util.*;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.player.PlayerKiller;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.model.player.packet.Attack;
import com.rs.worldserver.WorldMap;
import com.rs.worldserver.model.player.RegionManager.Region.Tile;
import java.io.*;
import com.rs.worldserver.util.BanProcessor;

public class CombatManager 
{
	private Client attacker;
	private Map<Client, Integer> attackers = new HashMap<Client, Integer>();
	private Map<Client, Integer> previousAttackers = null;


	public int packetNum = 0;
	public boolean owedDouble = false;
	public boolean doubleWait = false;
	public long temptime = 0;
	public long specDelay = 0;
	public int clawStrikes = 0;
	public int firstClawStrikeDamage = 0;
	private boolean isMyTurn = false,magicFailed = false;
	private NPC targetNPC;
	private Client targetPlayer;
	private Client c;
	public int damage = 0, magicDamage = 0;
	
	public void playerDamage()
	{
		damage = Misc.random((int)(calculateMeleeMaxHit()));
	}
	public void givepoison() 
	{
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > -1) 
		{
			String WeaponName = Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]).getName().toLowerCase();
			WeaponName = WeaponName.replaceAll("_", " ");
			WeaponName = WeaponName.trim();	
			if (WeaponName.contains("(p)") || WeaponName.contains("(P)")) 
			{
				if (c.isPvp) 
				{
					Client t = (Client) PlayerManager.getSingleton().getPlayers()[c.playerIndex];
					if(!t.isDead)
					{
						t.getCombat().poison(4);
					}
				} 
			}
			if (WeaponName.contains("(+)")) 
			{
				if (c.isPvp) 
				{
					Client t = (Client) PlayerManager.getSingleton().getPlayers()[c.playerIndex];
					if(!t.isDead)
					{
						t.getCombat().poison(5);
					}
				}
			}		
			if (WeaponName.contains("(s)") || WeaponName.contains("(S)")) 
			{
				if (c.isPvp) 
				{
					Client t = (Client) PlayerManager.getSingleton().getPlayers()[c.playerIndex];
					if (t != null) 
					{
						if(!t.isDead)
						{
							t.getCombat().poison(6);
						}
					}
				}
			}		
		}
	}
	public void giveArrowPoison() 
	{
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] > -1) 
		{
			String Arrow = Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS]).getName().toLowerCase();
			Arrow = Arrow.replaceAll("_", " ");
			Arrow = Arrow.trim();		
			if (Arrow.contains("(p)") || Arrow.contains("(P)")) 
			{
				if (c.isPvp) 
				{
					Client t = (Client) PlayerManager.getSingleton().getPlayers()[c.playerIndex];
					if(!t.isDead)
					{
						t.getCombat().poison(4);
					}
				}
			}
			if (Arrow.contains("(+)")) 
			{
				if (c.isPvp) 
				{
					Client t = (Client) PlayerManager.getSingleton().getPlayers()[c.playerIndex];
					if(!t.isDead)
					{
						t.getCombat().poison(5);
					}
				}
			}		
			if (Arrow.contains("(s)") || Arrow.contains("(S)")) 
			{
				if (c.isPvp) 
				{
					Client t = (Client) PlayerManager.getSingleton().getPlayers()[c.playerIndex];
					if(!t.isDead)
					{
						t.getCombat().poison(6);
					}
				} 
			}		
		}
	}
	public void curepoison() 
	{
		c.poisontimer = 0;
		c.poisondamg = 0;
		c.poisondamage = 0;
	}

	public void poison(int damage){
		if (c.poisondamg == 0){
			if (!c.inDuelArena()){
				c.getActionAssistant().sendMessage("You have been poisoned!");
				c.poisondamage = damage;
				c.poisontimer = Misc.random(30);
				c.poisondamg = 4;
				
				EventManager.getSingleton().addEvent(c,"poison event", new Event() {
					@Override
					public void execute(EventContainer cs) {
						if(c.disconnected || c == null) {
							cs.stop();
						}
						if (c.poisondamage != 0) {
							c.poisonHit = true;
							c.hitDiff2 = c.poisondamage;
							c.playerLevel[3] -= c.hitDiff2;
							c.updateRequired = true;
							c.hitUpdateRequired2 = true;
							c.getActionAssistant().refreshSkill(3);
							c.poisondamg -= 1;
							c.getActionAssistant().sendMessage("You start to die of poison.");
							c.poisontimer = 30;
							
						}
						if (c.poisondamg == 1) {
							c.poisondamage -= 1;
							c.poisondamg = 4;
						}
						if (c.poisondamage <= 0) {
							c.getActionAssistant().sendMessage("Your poison fades off");
							c.newhptype = false;
							c.hptype = 0;							
							cs.stop();
						}					
					}
					@Override
					public void stop() {
					
					}
				}, 30000+Misc.random(30000));
			}
		}
	}

	private int[] bonus = new int[12];

	public CombatManager(Client client) 
	{
		this.c = client;
	}
	public void dropItem(int id, int amount, int x, int y, int z) 
	{
		if(Misc.random(1) == 1)
		{
			FloorItem i = new FloorItem(id,amount, c, x, y, z);
			for(FloorItem f : Server.getItemManager().list) 
			{
				if(f.getX() == i.getX() && f.getY() == i.getY() && f.getHeight() == i.getHeight() && f.getId() == i.getId()) 
				{
					Server.getItemManager().hideDrop(i);
					Server.getItemManager().list.remove(f);
					i.setAmount(f.getAmount() + 1);
					Server.getItemManager().newDrop(i, c);
					return;
				}
			}
			Server.getItemManager().newDrop(i, c);
		}
	}

	public int smited;
	/**
	 * Attack Npcs
	 */
	 


	public boolean checkBolts() 
	{
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7858|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7859 || 
			c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7860 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7861 ||
			c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7862 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7863 ||
			c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7864 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7865 ||
			c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7866 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7867 ||
			c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7868 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7869 ||
			c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7870 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7871 ||
			c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7872 
			) 
		{
			return true;
		}
		return false;
	}
	public boolean checkxBolts() 
	{
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 4740) 
		{
			return true;
		}
		return false;
	}	
	public void resetAttackVsNpc() {
	
	c.bonusDamage = 0;
		c.FrozenBook = false;
			clawStrikes = 0;
			owedDouble = false;
			doubleWait = false;
			c.isFighting = false;
			c.playerIndex = 0;
			c.usingMagic = false;
			c.faceUpdate(0);
			c.doublePlayer = 0;
			c.doubleNpc = 0;
			c.oldNpcIndex = 0;
			c.npcIndex = 0;		
			c.dHit = false;
			c.atObjectStartTime=System.currentTimeMillis();
			c.getFollowing().resetFollowing();
	}

	public void resetAttack2() 
	{
	
		if(!c.inAir)
		{
		c.bonusDamage = 0;
		c.FrozenBook = false;
			clawStrikes = 0;
			owedDouble = false;
			doubleWait = false;
			//c.isFighting = false;
			c.playerIndex = 0;
			c.usingMagic = false;
			c.faceUpdate(0);
			c.doublePlayer = 0;
			c.doubleNpc = 0;
			c.oldNpcIndex = 0;
			c.npcIndex = 0;		
			c.dHit = false;
			c.atObjectStartTime=System.currentTimeMillis();
			c.getFollowing().resetFollowing();
			if (targetNPC != null) 
			{
				targetNPC.killerId = 0;
				targetNPC.underAttack = false;
			//	targetNPC.isWalking = false;
				targetNPC.getAttackers().remove(c);
				//c.getActionAssistant().sendMessage("You have run out of arrows!");
				if (targetNPC.getAttacker() == c) 
				{
					if (targetNPC.getAttackers().entrySet().iterator().hasNext()) 
					{
						targetNPC.setAttacker(targetNPC.getAttackers().entrySet()
							.iterator().next().getKey());
					} 
					else 
					{
						targetNPC.setWalking(targetNPC.isWasWalking());
						targetNPC.setAttacker(null);
					}
				}
				targetNPC = null; // lose our ref
			}
			if(targetPlayer != null) 
			{
				targetPlayer.getCombatManager().attackers.remove(c);
				if(targetPlayer.getCombatManager().attacker == c) 
				{
					targetPlayer.getCombatManager().attacker = null;
					if(targetPlayer.getCombatManager().attackers.entrySet().iterator().hasNext()) 
					{
						targetPlayer.getCombatManager().attacker = targetPlayer.getCombatManager().attackers.entrySet().iterator().next().getKey();
					} 
					else 
					{
						targetPlayer.getCombatManager().resetAttack();
					}
				}
				targetPlayer = null;
			}
		}
	}

	public void resetAttack() 
	{
	
	
	c.dScim = false;
	c.oldTempIndex = 0;
		if(!c.inAir || c.duelStatus > 1)
		{
		c.bonusDamage = 0;
		c.FrozenBook = false;
			clawStrikes = 0;
			owedDouble = false;
			doubleWait = false;
			//c.isFighting = false;
			c.playerIndex = 0;
			c.usingMagic = false;
			c.faceUpdate(0);
			c.doublePlayer = 0;
			c.doubleNpc = 0;
			c.oldNpcIndex = 0;
			c.npcIndex = 0;		
			c.atObjectStartTime=System.currentTimeMillis();
			c.dHit = false;
			c.getFollowing().resetFollowing();
			if (targetNPC != null) 
			{
				if (targetNPC.killerId == c.playerId) 
				{
					//c.getActionAssistant().Send("I'm killing this one.");
					return;
				}
			//	targetNPC.isWalking = false;
				c.getFollowing().resetFollowing();
				targetNPC.killerId = 0;
				targetNPC.underAttack = false;
				targetNPC.getAttackers().remove(c);
				//c.getActionAssistant().sendMessage("You have run out of arrows!");
				if (targetNPC.getAttacker() == c) 
				{
					if (targetNPC.getAttackers().entrySet().iterator().hasNext()) 
					{
						targetNPC.setAttacker(targetNPC.getAttackers().entrySet()
							.iterator().next().getKey());
					} 
					else 
					{
						targetNPC.setWalking(targetNPC.isWasWalking());
						targetNPC.setAttacker(null);
					}
				}
				targetNPC = null; // lose our ref
			}
			if(targetPlayer != null) 
			{
				targetPlayer.getCombatManager().attackers.remove(c);
				if(targetPlayer.getCombatManager().attacker == c) 
				{
					targetPlayer.getCombatManager().attacker = null;
					if(targetPlayer.getCombatManager().attackers.entrySet().iterator().hasNext()) 
					{
						targetPlayer.getCombatManager().attacker = targetPlayer.getCombatManager().attackers.entrySet().iterator().next().getKey();
					} 
					else 
					{
						targetPlayer.getCombatManager().resetAttack();
					}
				}
				targetPlayer = null;
			}
		}
	}
	public void resetAttack3() 
	{
		if(true)
		{
		c.bonusDamage = 0;
		c.FrozenBook = false;
			clawStrikes = 0;
			owedDouble = false;
			doubleWait = false;
			//c.isFighting = false;
			c.playerIndex = 0;
			c.usingMagic = false;
			c.faceUpdate(0);
			c.doublePlayer = 0;
			c.doubleNpc = 0;
			c.oldNpcIndex = 0;
			c.npcIndex = 0;		
			c.dHit = false;
			c.atObjectStartTime=System.currentTimeMillis();
			c.getFollowing().resetFollowing();
			if (targetNPC != null) 
			{
				targetNPC.killerId = 0;
				targetNPC.underAttack = false;
			//	targetNPC.isWalking = false;
				targetNPC.getAttackers().remove(c);
				//c.getActionAssistant().sendMessage("You have run out of arrows!");
				if (targetNPC.getAttacker() == c) 
				{
					if (targetNPC.getAttackers().entrySet().iterator().hasNext()) 
					{
						targetNPC.setAttacker(targetNPC.getAttackers().entrySet()
							.iterator().next().getKey());
					} 
					else 
					{
						targetNPC.setWalking(targetNPC.isWasWalking());
						targetNPC.setAttacker(null);
					}
				}
				targetNPC = null; // lose our ref
			}
			if(targetPlayer != null) 
			{
				targetPlayer.getCombatManager().attackers.remove(c);
				if(targetPlayer.getCombatManager().attacker == c) 
				{
					targetPlayer.getCombatManager().attacker = null;
					if(targetPlayer.getCombatManager().attackers.entrySet().iterator().hasNext()) 
					{
						targetPlayer.getCombatManager().attacker = targetPlayer.getCombatManager().attackers.entrySet().iterator().next().getKey();
					} 
					else 
					{
						targetPlayer.getCombatManager().resetAttack();
					}
				}
				targetPlayer = null;
			}
		}
	}
	
	
	public void attackPlayer(int attackplayer) 
	{
		if(!c.npcClick) return;
		c.isPvp = true;
		Client targetPlayer = (Client) PlayerManager.getSingleton().getPlayers()[attackplayer];
		c.faceUpdate(attackplayer+32768);
	
		if(!c.getCombat().checkReqs()) 
		{
			resetAttack();
			return;
		}		
		if (PlayerManager.getSingleton().getPlayers()[attackplayer] != null) 
		{
			if(!c.usingMagic){
				if (!c.follower || c.followId != targetPlayer.playerId){
					c.followId = targetPlayer.playerId;
					c.follower = true;
				}
			}
			if(c.autoCast){
				if (!c.follower || c.followId != targetPlayer.playerId){
					c.followId = targetPlayer.playerId;
					c.follower = true;
				}
			}			
			if (PlayerManager.getSingleton().getPlayers()[attackplayer].isDead) 
			{
				resetAttack();
				return;
			}
			if(c.respawnTimer > 0) 
			{
				resetAttack();
				return;
			}
			if(!c.goodDistance(PlayerManager.getSingleton().getPlayers()[c.playerIndex].getAbsX(), PlayerManager.getSingleton().getPlayers()[c.playerIndex].getAbsY(), c.getAbsX(), c.getAbsY(), 16)) 
			{
				resetAttack();
				//c.getActionAssistant().follow(playerId, 1, 1);
				return;
			}
			if(Server.getPlayerManager().getPlayers()[c.playerIndex].respawnTimer > 0) 
			{
				Server.getPlayerManager().getPlayers()[c.playerIndex].playerIndex = 0;
				resetAttack();
				return;
			}
			if ((System.currentTimeMillis() - c.attackTimer) < c.lastWeaponSpeed)
			{//WeaponSpeed.getInstance().getWeaponSpeed(c.playerEquipment[PlayerConstants.PLAYER_WEAPON])){
				return;
			} 	
			if (!Region.canAttack(c, targetPlayer)) {
			boolean fix = false;
			if((c.getX() == 3094 && c.getY() == 3957 && targetPlayer.getX() == 3093 && targetPlayer.getY() == 3957) ||
			   (c.getX() == 3093 && c.getY() == 3958 && targetPlayer.getX() == 3093 && targetPlayer.getY() == 3957) ||
			   (c.getX() == 3093 && c.getY() == 3958 && targetPlayer.getX() == 3093 && targetPlayer.getY() == 3957) ||
			   (c.getX() == 3093 && c.getY() == 3958 && targetPlayer.getX() == 3093 && targetPlayer.getY() == 3957) ||
			   (c.getX() == 3094 && c.getY() == 3958 && targetPlayer.getX() == 3093 && targetPlayer.getY() == 3957) ||
			   (c.getX() == 3093 && c.getY() == 3956 && targetPlayer.getX() == 3093 && targetPlayer.getY() == 3957) ||
			   (c.getX() == 3092 && c.getY() == 3957 && targetPlayer.getX() == 3093 && targetPlayer.getY() == 3957) ||
			   (c.getX() == 3095 && c.getY() == 3958 && targetPlayer.getX() == 3095 && targetPlayer.getY() == 3957) ||
				(c.getX() == 3095 && c.getY() == 3957 && targetPlayer.getX() == 3095 && targetPlayer.getY() == 3957) ||
				(c.getX() == 3095 && c.getY() == 3956 && targetPlayer.getX() == 3095 && targetPlayer.getY() == 3957) ||
			   (c.getX() == 3096 && c.getY() == 3958 && targetPlayer.getX() == 3095 && targetPlayer.getY() == 3957) ||
				(c.getX() == 3096 && c.getY() == 3957 && targetPlayer.getX() == 3095 && targetPlayer.getY() == 3957) ||
				(c.getX() == 3094 && c.getY() == 3957 && targetPlayer.getX() == 3095 && targetPlayer.getY() == 3957) ||
				(c.getX() == 3096 && c.getY() == 3956 && targetPlayer.getX() == 3095 && targetPlayer.getY() == 3957) ||
				(c.getX() == 3097 && c.getY() == 3957 && targetPlayer.getX() == 3095 && targetPlayer.getY() == 3957) ||
				(c.getX() == 3098 && c.getY() == 3957 && targetPlayer.getX() == 3095 && targetPlayer.getY() == 3957))
			{
				fix = true;
			}
			if(c.freezeTimer > 0){
				c.faceUpdate(targetPlayer.playerId+32768);	
				return;
			}
			
			c.getActionAssistant().playerWalk(targetPlayer.getX(), targetPlayer.getY(),"cm 520 attack");
			c.faceUpdate(targetPlayer.playerId+32768);
			if(!fix){
				//c.getActionAssistant().sendMessage("Hrm");
				return;
				}
		}
			
			c.inCombat = true;
			targetPlayer.inCombat = true;
			c.usingBow = false;
			c.usingRangeWeapon = false;
			c.lastWeaponUsed = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
			c.lastWeaponSpeed = WeaponSpeed.getInstance().getWeaponSpeed(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]);
			Server.getDegradeManager().degradeNew(c);
			if(c.usingMagic)
			{
				magicDamage = 0;
					
				if (c.freezeTimer > 0) 
				{ // cannot freeze someone if you are already underneath them frozen.
					if (c.absX == targetPlayer.absX && c.absY == targetPlayer.absY) 
					{
						c.getCombat().resetAttack();
						return;
					}
				}
				// barrage someone more than 1 square away & they no longer auto attack you.
				//if(!c.goodDistance(PlayerManager.getSingleton().getPlayers()[c.playerIndex].getAbsX(), PlayerManager.getSingleton().getPlayers()[c.playerIndex].getAbsY(), c.getAbsX(), c.getAbsY(), 1)) {
				//	targetPlayer.getCombatManager().resetAttack();
				//}
										
				if(((c.duelStatus >3) && (Misc.random(10+targetPlayer.getCombat().calculateMagicDefence()) > Misc.random(12+calculateMagicAttack()))) || ((c.duelStatus <1) && Misc.random(10+targetPlayer.getCombat().calculateMagicDefence()) > Misc.random(calculateMagicAttack())))
				{
					magicDamage = 1;
					magicFailed = true;
					targetPlayer.underAttackBy = c.playerId;
					targetPlayer.logoutDelay = System.currentTimeMillis();;
					targetPlayer.singleCombatDelay = System.currentTimeMillis();
					targetPlayer.singleCombatDelay2 = System.currentTimeMillis();
				} 
				else 
				{
					magicDamage = 0;
					magicFailed = false;	
						
					int freezeDelay = c.getMagicData().getFreezeTime()*2;//freeze time
					if(freezeDelay > 0 && targetPlayer.freezeTimer == -5) 
					{ 
						targetPlayer.freezeTimer = freezeDelay;
					if(targetPlayer.prayerActive[16] || targetPlayer.curseActive[7]) {
						freezeDelay = (int)freezeDelay/2;
					}
						targetPlayer.stopMovement();
						targetPlayer.getActionAssistant().Send("You have been frozen!");
						targetPlayer.newFreeze = true;
						targetPlayer.frozenBy = c.playerId;
					}
					targetPlayer.underAttackBy = c.playerId;
					targetPlayer.logoutDelay = System.currentTimeMillis();;
					targetPlayer.singleCombatDelay = System.currentTimeMillis();
					targetPlayer.singleCombatDelay2 = System.currentTimeMillis();
				}
					
			}
				/*	boolean usingBow = c.tempusingBow;
			boolean usingArrows = c.tempusingArrows;
			boolean usingOtherRangeWeapons = c.tempusingOtherRangeWeapons;*/
				boolean usingBow = false;
		boolean usingArrows = false;
		boolean usingOtherRangeWeapons = false;
			c.projectileStage = 0;
			if(!c.usingMagic) 
			{
					//set bow and arrow here
			for (int bowId : c.BOWS) {
							if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == bowId) {
								//c.tempusingBow = true;
								 usingBow = true;
								for (int arrowId : c.ARROWS) {
									if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == arrowId) {
									//	c.tempusingArrows = true;

			usingArrows = true;
									}
								}
							}
						}				
					
						for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
							if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == otherRangeId) {
							//	c.tempusingOtherRangeWeapons = true;
								usingOtherRangeWeapons = true;
							}
						}
				if(c.getCombatFormulas().diagonal(c.getAbsX(),c.getAbsY(),targetPlayer.getAbsX(),targetPlayer.getAbsY()) && !usingOtherRangeWeapons && !usingBow){
					//c.getActionAssistant().sendMessage("from here");
					if (c.freezeTimer <= 0){
						int r = Misc.random(3);
						switch (r) {
							case 0:
								c.getActionAssistant().playerWalk(targetPlayer.absX, targetPlayer.absY - 1,"cm 621 attack");
								break;
							case 1:
								c.getActionAssistant().playerWalk(targetPlayer.absX, targetPlayer.absY + 1,"cm 624 attack");
								break;
							case 2:
								c.getActionAssistant().playerWalk(targetPlayer.absX + 1, targetPlayer.absY,"cm 627 attack");
								break;
							case 3:
								c.getActionAssistant().playerWalk(targetPlayer.absX - 1, targetPlayer.absY,"cm 630 attack");
								break;
						}
						c.faceUpdate(targetPlayer.playerId+32768);
					} else {
						c.faceUpdate(targetPlayer.playerId+32768);
						return;
					}				
				}						
			}

			if(!usingArrows && usingBow && ((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] < 4212 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > 4223) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 20171)) 
			{
				c.getActionAssistant().sendMessage("You have run out of arrows!");
				return;
			} 
				
			if(((usingOtherRangeWeapons && !usingBow && !c.usingMagic) && !c.goodDistance(c.getAbsX(), c.getAbsY(), PlayerManager.getSingleton().getPlayers()[c.playerIndex].getAbsX(), PlayerManager.getSingleton().getPlayers()[c.playerIndex].getAbsY(), 4)) 
				|| ((!usingOtherRangeWeapons && usingHally() && !usingBow && !c.usingMagic) && !c.goodDistance(c.getAbsX(), c.getAbsY(), PlayerManager.getSingleton().getPlayers()[c.playerIndex].getAbsX(), PlayerManager.getSingleton().getPlayers()[c.playerIndex].getAbsY(), 2))
				|| ((usingBow || c.usingMagic) && !c.goodDistance(c.getAbsX(), c.getAbsY(), PlayerManager.getSingleton().getPlayers()[c.playerIndex].getAbsX(), PlayerManager.getSingleton().getPlayers()[c.playerIndex].getAbsY(), 16))) 
			{
				//c.attackTimer += 1000;
				return;
			}
			if(targetPlayer.freezeTimer <= 0) {
				if(!c.goodDistance(targetPlayer.getAbsX(),	targetPlayer.getAbsY(), c.getAbsX(), c.getAbsY(), getRequiredDistance())  
					&& !usingOtherRangeWeapons && !usingBow && !c.usingMagic){ 
					return;
				}
			} else if(targetPlayer.freezeTimer > 0) {
				if(!c.goodDistance(targetPlayer.getAbsX(),	targetPlayer.getAbsY(), c.getAbsX(), c.getAbsY(), 1)  
					&& !usingOtherRangeWeapons && !usingBow && !c.usingMagic){ 
					return;
				}
			}
			if(usingBow)
			{
				if (!(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > 4219 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] < 4225) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 4212&& c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 20171) 
				{
					c.getCombat().correctBowAndArrows();
					if(!c.bowlevel)
					{
						c.getActionAssistant().sendMessage("You can't use "+Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS]).getName().toLowerCase()+"s with a "+Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]).getName().toLowerCase()+".");
						c.stopMovement();
						resetAttack();
						return;					
					}
				}
			}
			if(usingBow && checkBolts() && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 9185 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 18357) 
			{
				c.getActionAssistant().sendMessage("You can't use bolt's with this.");
				c.stopMovement();
				resetAttack();
				return;
			}
			if(usingBow && checkxBolts() && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 4734) 
			{
				c.getActionAssistant().sendMessage("You can't use bolt's with this.");
				c.stopMovement();
				resetAttack();
				return;
			}
			if(usingBow || c.usingMagic || usingOtherRangeWeapons ||(usingHally() && c.goodDistance(c.getAbsX(), c.getAbsY(), PlayerManager.getSingleton().getPlayers()[c.playerIndex].getAbsX(), PlayerManager.getSingleton().getPlayers()[c.playerIndex].getAbsY(), 2))) 
			{
				c.stopMovement();
				if(c.inDuelArena()) 
				{ //markerduel
					c.singleCombatDelay = System.currentTimeMillis();
				}
			}
			if(c.duelStatus != 5 && !c.isInCwars()) 
			{ //markerduel
				if(!c.attackedPlayers.contains(c.playerIndex) && !PlayerManager.getSingleton().getPlayers()[c.playerIndex].attackedPlayers.contains(c.playerId)) 
				{
					c.attackedPlayers.add(c.playerIndex);
					c.isSkulled = true;
					c.skullTimer = Config.SKULL_TIMER;
					if(Config.SKULL_HEAD_ICON) 
					{
						c.skullIcon = 0;
					}
					c.getActionAssistant().requestUpdates();
				} 	
			}			
			if(!c.usingMagic) 
			{
				if (c.freezeTimer > 0) 
				{
					if (c.absX == targetPlayer.absX && c.absY == targetPlayer.absY) 
					{
						c.getCombat().resetAttack();
						return;
					}
				}	
				if(!c.goodDistance(targetPlayer.getAbsX(),	targetPlayer.getAbsY(), c.getAbsX(), c.getAbsY(), 1)  
					&& !usingOtherRangeWeapons && !usingBow && !c.usingMagic && !c.follower){ 
					return;
				}  							
				if (c.heightLevel != targetPlayer.heightLevel) 
				{
					c.getCombat().resetAttack();
					return;
				}
				if(!c.specOn && !owedDouble) 
				{
					if (doubleWait) {
						if(System.currentTimeMillis()-temptime < 2000)
							return;
						else
							doubleWait=false;
					}
					c.getActionAssistant().startAnimation(c.getCombatEmotes().getWepAnim());
					c.lastWeaponUsed = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
					c.attackTimer = System.currentTimeMillis();
					if(c.sounds == 1) 
					{
						c.getActionAssistant().frame174(Server.GetWepSound.GetWeaponSound(c.playerId), 030, 000);
					}
				}
				else if(owedDouble) {
					c.dHit=true;
					owedDouble=false;
					doubleWait=true;
					temptime = System.currentTimeMillis();
					specDelay = System.currentTimeMillis();
					c.attackTimer = System.currentTimeMillis() + 400;
				}				
				else 
				{
					if(!c.goodDistance(targetPlayer.getAbsX(),	targetPlayer.getAbsY(), c.getAbsX(), c.getAbsY(), 1)  
						&& !usingOtherRangeWeapons && !usingBow && !c.usingMagic && !c.follower){ 
						return;
					}
					if(doubleWait) {
						if(System.currentTimeMillis()-temptime < 2000) // how long to wait between dclaw specs
							return;
						else
							doubleWait=false;
					}
					if(c.getSpecials().checkSpecial(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON])){
						if (System.currentTimeMillis()- specDelay < 2000) {
						return;
					}
						if(c.duelRule[10] && c.duelStatus == 5) 
						{ //markerduel
							c.getActionAssistant().sendMessage("Special attacks have been disabled during this duel!");
							c.usingSpecial = false;
							c.getSpecials().specialBar();
							resetAttack();
							return;
						}	
						c.getActionAssistant().startAnimation(c.getSpecials().getSpecialAnim(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]));
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 14484) {  // d claws
							Server.getStillGraphicsManager().stillGraphics(c,
							c.getAbsX(), c.getAbsY(), 0, c.getSpecials().getSpecialGFX(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]), 0);
						} else {
							c.gfx100(c.getSpecials().getSpecialGFX(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]));
						}
						//c.getActionAssistant().sendMessage("g: " + c.getSpecials().getSpecialGFX(c.playerEquipment[PlayerConstants.PLAYER_WEAPON]));
						c.attackTimer = System.currentTimeMillis();
						c.lastWeaponUsed = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
						Client o = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
						c.specAch = true;
						
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 14484) {  // d claws
							owedDouble=true; 
							c.attackTimer = 1000; 
						}
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 5698 && c.sounds == 1) 
						{
							c.getActionAssistant().frame174(385, 050, 000);
						}
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1215 && c.sounds == 1) 
						{
							c.getActionAssistant().frame174(385, 050, 000);
						}			
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1231 && c.sounds == 1) 
						{
							c.getActionAssistant().frame174(385, 050, 000);
						}	
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 5680 && c.sounds == 1) 
						{
							c.getActionAssistant().frame174(385, 050, 000);
						}							
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1305 && c.sounds == 1) 
						{
							c.getActionAssistant().frame174(390, 050, 000);
						}							
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4153) 
						{
						if(c.duelRule[3]) {
						c.getActionAssistant().sendMessage("Melee has been disabled in this duel!");
						c.getCombat().resetAttack();
						return;
						}
							Client s = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
							c.hitDelay = 1;
							c.attackTimer += 500;
							c.gfx100(340);
							c.specOn = false;
							applyPlayerMeleeDamage(c.playerIndex, 1);
							double factor = 1;
							if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 19669) {
								 factor = 0.9;
							}
							c.specialAmount -= (c.getSpecials().getSpecialDrain(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) *factor);
							return;
						}								
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4151) 
						{
							Client s = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
							if(c.sounds == 1) 
							{
								c.getActionAssistant().frame174(1081, 050, 000);
							}
							s.gfx100(341);
						}
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 21371) {
							Client s = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
							s.gfx100(2011);
						}
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 21372) {
							Client s = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
							s.gfx100(2011);
						}
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 21373) {
							Client s = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
							s.gfx100(2011);
						}
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 21374) {
							Client s = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
							s.gfx100(2011);
						}
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 21375) {
							Client s = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
							s.gfx100(2011);
						}
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 7822) 
						{
							Client s = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
							if(c.sounds == 1) 
							{
								c.getActionAssistant().frame174(1081, 050, 000);
							}
							s.gfx100(78);
						}
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11730) 
						{
							c.SARASWORD = true;
						}
							if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 15241) 
						{
							c.HANDCANNON = true;
						}
								
							
						if((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1249) ||c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11716 || (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1263) || (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 5716)  || (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 5730))
						{
												o.underAttackBy = c.playerId;
							o.logoutDelay = System.currentTimeMillis();;
							o.singleCombatDelay = System.currentTimeMillis();
							o.singleCombatDelay2 = System.currentTimeMillis();
							o.getActionAssistant().sendMessage("You've been stunned.");
							o.inCombat = true;
							double factor = 1;
							if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 19669) {
								 factor = 0.9;
							}
							//c.specialAmount -= (c.getSpecials().getSpecialDrain(c.playerEquipment[PlayerConstants.PLAYER_WEAPON]) *factor);
							o.foodDelay = (System.currentTimeMillis() + 1500);
							o.attackTimer += 2500;
							//c.dSpear = false;
							o.frozenBy = c.playerId;
							//System.out.println("called");
							if(c.absX > o.absX)
							{
									o.freezeTimer = 0;
									o.getActionAssistant().playerWalk(o.absX - 1, o.absY,"cm 886 attack");
									o.gfx100(254); 
									o.freezeTimer = 8; // 20 seconds
								//return;
							}
							if(c.absX < o.absX)
							{
									o.freezeTimer = 0;
									o.getActionAssistant().playerWalk(o.absX + 1, o.absY ,"cm 891 attack");
									o.gfx100(254); 
									o.freezeTimer = 8; // 20 seconds
								//return;
							}
							if(c.absY > o.absY)
							{
								o.freezeTimer = 0;
								o.getActionAssistant().playerWalk(o.absX, o.absY - 1,"cm 898 attack");
									o.gfx100(254); 
									o.freezeTimer = 8; // 20 seconds
								//return;
							}
							if(c.absY < o.absY)
							{
								o.freezeTimer = 0;
								o.getActionAssistant().playerWalk(o.absX, o.absY + 1,"cm 904 attack");
									o.gfx100(254); 
									o.freezeTimer = 8; // 20 seconds
								//return;
							}
						}
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13879) 
						{
							Client s = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
							int pX = c.getX();
							int pY = c.getY();
							int nX = Server.getPlayerManager().getPlayers()[c.playerIndex].getAbsX();
							int nY = Server.getPlayerManager().getPlayers()[c.playerIndex].getAbsY();
							int offX = (pY - nY)* -1;
							int offY = (pX - nX)* -1;
							createPlayersProjectile(pY, pX, offY, offX, 50, 28, 1837, 43, 31, attackplayer - 1, 5);
							if(c.sounds == 1) 
							{
								c.getActionAssistant().frame174(1700, 020, 000);
							}
							//s.gfx100(399);
						}							
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13883) 
						{
							Client s = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
							int pX = c.getX();
							int pY = c.getY();
							int nX = Server.getPlayerManager().getPlayers()[c.playerIndex].getAbsX();
							int nY = Server.getPlayerManager().getPlayers()[c.playerIndex].getAbsY();
							int offX = (pY - nY)* -1;
							int offY = (pX - nX)* -1;
							createPlayersProjectile(pY, pX, offY, offX, 50, 28, 1839, 43, 31, attackplayer - 1, 5);
							if(c.sounds == 1) 
							{
								c.getActionAssistant().frame174(1700, 020, 000);
							}
							//s.gfx100(399);
						}
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4587) 
						{
							c.dScim = true;
						}
							
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11700) 
						{
							c.zamGod = true;
						}

						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11696) 
						{
							c.bandosGod = true;
						}
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 22406) 
						{
							c.amace = true;
						}
						if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13671) 
						{
							c.blackWhip = true;
						}
						if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 19784) 
						{
							c.KorasiSpec = true;
						}
						if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13669) 
						{
							c.eleWhip = true;
						}
						if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13666) 
						{
							c.runeWhip = true;
						}
						if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13668) 
						{
							c.bWhip = true;
						}
						if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13667) 
						{
							c.dragonWhip = true;
						}
						if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1215 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 5698) 
						{
							c.inAir = true;
						}
							
						// if (c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 4220) {
						// c.abow = true;
						// }
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 20171) 
						{
							c.zaryteBow = true;
						}
						if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4221) 
						{
							c.sbow = true;
						}
						if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4222) 
						{
							c.zbow = true;
						}if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4223) 
						 {
							 c.bbow = true;
						 }
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 861) 
						{
							c.getCombat().correctBowAndArrows();
							if(!c.bowlevel)
							{
								c.getCombat().resetAttack();
								c.getActionAssistant().sendMessage("@red@Hrm... that shouldn't have happened?");
								return;				
							}
							int pX = c.getX();
							int pY = c.getY();
							int nX = Server.getPlayerManager().getPlayers()[c.playerIndex].getAbsX();
							int nY = Server.getPlayerManager().getPlayers()[c.playerIndex].getAbsY();
							int offX = (pY - nY)* -1;
							int offY = (pX - nX)* -1;
							createPlayersProjectile(pY, pX, offY, offX, 50, 50, 249, 43, 31, attackplayer - 1, 25);
							createPlayersProjectile(pY, pX, offY, offX, 50, 75, 249, 43, 31, attackplayer - 1, 50);
							c.projectileStage = 8;
							c.castingMagic = false;
							c.doubleHit3 = 3;
							c.rangeSpec = true;
							c.inAir = true;
							if(c.sounds == 1) 
							{
								c.getActionAssistant().frame174(386, 020, 000);
							}								
						}	
							
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4827 || (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] >= 15701 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] <= 15704)) 
						{
						if (!c.DarkBowSpec) {
							c.DarkBowSpec = true;
						}
							int pX = c.getX();
							int pY = c.getY();
							int nX = Server.getPlayerManager().getPlayers()[c.playerIndex].getAbsX();
							int nY = Server.getPlayerManager().getPlayers()[c.playerIndex].getAbsY();
							int offX = (pY - nY)* -1;
							int offY = (pX - nX)* -1;
							createPlayersProjectile(pY, pX, offY, offX, 50, 50, 1099, 43, 31, attackplayer - 1, 25);
							createPlayersProjectile(pY, pX, offY, offX, 50, 75, 1099, 43, 31, attackplayer - 1, 50);
							c.projectileStage = 8;
							c.castingMagic = false;
							c.doubleHit4 = 0;
							c.rangeSpec = true;
							c.inAir = true;
						}															
						c.specOn = false;
						double factor = 1;
							if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 19669) {
								 factor = 0.9;
							}
							c.specialAmount -= (c.getSpecials().getSpecialDrain(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) *factor);
						
						if(c.getSpecials().doubleHit(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON])) 
						{
							if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 3204)
							{
							} 
							else 
							{
								c.dHit = true;
							}
						}
						c.bonusDamage = c.getSpecials().getBonusDamage(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]);
						c.lastSpecialUsedWeapon = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
						c.getSpecials().specialBar();
					} 
					else 
					{
						c.getActionAssistant().startAnimation(c.getCombatEmotes().getWepAnim());
						c.specOn = false;
						c.getActionAssistant().sendMessage("You don't have enough special power left.");
						c.attackTimer += 3000;
					}
				}
			} 
			else 
			{
				if(c.usingMagic && Config.MAGIC_LEVEL_REQUIRED) 
				{ // check magic level
					if(c.playerLevel[6] < c.MAGIC_SPELLS[c.spellId][1]) 
					{
						c.actionAssistant.sendMessage("You need to have a magic level of " +c.MAGIC_SPELLS[c.spellId][1]+" to cast this spell.");
						c.getCombat().resetAttack();
						return;
					}
				}
				if(!c.getCombat().checkReqs()) 
				{
					resetAttack();
					return;
				}
				if(c.reCast == 0)
				{
					c.getActionAssistant().startAnimation(c.MAGIC_SPELLS[c.spellId][2]);
					c.inCombat = true;
					targetPlayer.inCombat = true;
					if(c.MAGIC_SPELLS[c.spellId][0] == 1190 || c.MAGIC_SPELLS[c.spellId][0] == 1191 || c.MAGIC_SPELLS[c.spellId][0] == 1192)
					{
						c.reCast = 4;
						//c.attackTimer += 2000;
						c.attackTimer = System.currentTimeMillis();
						c.lastWeaponSpeed = 2600;
					} 
					else 
					{
						c.reCast = 5;
						//c.attackTimer += 2700;
						c.attackTimer = System.currentTimeMillis();
						c.lastWeaponSpeed = 2600;
					}
				} 
				else 
				{
					return;
				}				
				//c.getActionAssistant().startAnimation(c.MAGIC_SPELLS[c.spellId][2]);
			}		
			if(!usingBow && !c.usingMagic && !usingOtherRangeWeapons) 
			{ // melee hit delay
				Client k = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
				if (!c.follower || c.followId != k.playerId){
					c.followId = k.playerId;
					c.follower = true;
				}				
				c.hitDelay = getHitDelay();
				c.projectileStage = 0;
				c.oldPlayerIndex = attackplayer;
			}
			if ((c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 11283 && Misc.random(15) == 1 )|| (c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13655 && Misc.random(20) == 1)) 
			{
				Client k = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
				c.getActionAssistant().startAnimation(6696);
				c.gfx100(1165);
				k.delayedGfx(1167,36,100);
				k.hitDiff = Misc.random(25);
				k.playerLevel[3] -= k.hitDiff;
				k.hitUpdateRequired = true;
				k.updateRequired = true;
				k.getActionAssistant().Send("The dragonfire shield's dragon breath burns you!");
			} // make these work jag
			/*if (c.playerEquipment[PlayerConstants.PLAYER_SHIELD] == 13742 && Misc.random(9) >= 6) 
			{
				Client k = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
				c.elysian = 1;
				c.getActionAssistant().sendMessage("You feel the soul of your combatant flow through your body, decreasing the damage");
			}*/
		/*	if (c.playerEquipment[PlayerConstants.PLAYER_SHIELD] == 13740) 
			{ // pat edit
				Client k = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
				c.divine = 1;
				c.getActionAssistant().sendMessage("You feel your prayer flowing through the shield, making the shield stronger.");
			}*/
			if(usingBow && !usingOtherRangeWeapons) 
			{ // range hit delay
				if ((usingBow) && c.withinDistance(targetPlayer.absX, targetPlayer.absY, c.absX, c.absY, 8))
				{
					if (!c.follower || c.followId != targetPlayer.playerId){
						c.followId = targetPlayer.playerId;
						c.follower = true;
					}					
				}		
			
				if((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] >= 4212 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] <= 4223) || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 20171) 
				{
					c.rangeItemUsed = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
					c.inAir = true; 
				} 
				else 
				{
					c.rangeItemUsed = c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS];
					if(c.getCombatFormulas().ava() && Misc.random(4) == 1) 
					{
					
					} 
					else 
					{
						c.getActionAssistant().deleteArrow();
						c.inAir = true;
					}							
					
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 9185 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18357)
				{
					switch(c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS])
					{
						case 7868:
							if(Misc.random(10) == 1)
							{
								targetPlayer.gfx100(752);
								targetPlayer.hitDiff = calculateRangeMaxHit() + Misc.random(5);
								if(targetPlayer.prayerActive[17] || targetPlayer.curseActive[8])
								{ 
									targetPlayer.hitDiff = targetPlayer.hitDiff / 2;
								}
								if (targetPlayer.playerLevel[3] - targetPlayer.hitDiff < 0) 
								{ 
									targetPlayer.hitDiff = targetPlayer.playerLevel[3];
								}		
								if(c.prayerActive[23]) 
								{ // smite
									double killSmite = 1;
		if (c.killStreak >= 15 &&  c.killStreak <= 17) {
			killSmite = 1.5;
		}
									smited =(int) killSmite*targetPlayer.hitDiff / 3;
									if(targetPlayer.playerRights > 1)
									{
									} 
									else 
									{
										targetPlayer.playerLevel[5] -= smited;
									}
									if(targetPlayer.playerLevel[5] <= 0) 
									{
										targetPlayer.playerLevel[5] = 0;
									}
									targetPlayer.getActionAssistant().refreshSkill(5);
								}					
								if(c.fightType == 3) 
								{
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*Config.RANGE_EXP_RATE/3), 4); 
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*11), 3);
									c.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(4);
								} 
								else 
								{
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*Config.RANGE_EXP_RATE), 4); 
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*11), 3);
									c.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(4);
								}
								boolean dropArrows = true;
										
								for(int noArrowId : c.NO_ARROW_DROP) 
								{
									if(c.lastWeaponUsed == noArrowId) 
									{
										c.inAir = true;
										dropArrows = false;
										break;
									}
								}
								if(dropArrows) 
								{
								try{
									c.getCombat().dropItem(c.rangeItemUsed, 1,targetPlayer.getX(), targetPlayer.getY(),c.heightLevel);
									c.inAir = true;
									}
									catch (Exception e) {}
								}
								if (targetPlayer.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 2550) 
								{
									int recoilDam = (int) ((double) (targetPlayer.hitDiff) * .10);
									if(recoilDam > 0)
									{
										c.hitDiff = recoilDam;
										c.playerLevel[3] -= recoilDam;
										c.getActionAssistant().refreshSkill(3);
										c.hitUpdateRequired = true;
										c.updateRequired = true;
										targetPlayer.recoil = c.recoil + 1;
										if(targetPlayer.recoil >= 50) 
										{
											targetPlayer.getActionAssistant().deleteEquipment(targetPlayer.playerEquipment[PlayerEquipmentConstants.PLAYER_RING],PlayerEquipmentConstants.PLAYER_RING);
											targetPlayer.getActionAssistant().sendMessage("Your ring of recoil has shattered!");
											targetPlayer.recoil = 0;
										}
									}
								}		
								if (targetPlayer.canVengeance && targetPlayer.hitDiff > 0) 
								{
									targetPlayer.hitDiff = targetPlayer.hitDiff;
									c.hitDiff = targetPlayer.hitDiff/2;
									targetPlayer.canVengeance = false;
									targetPlayer.forcedChat("Taste vengeance!");
									c.hitUpdateRequired = true;
									c.updateRequired = true;
									c.playerLevel[3] -= targetPlayer.hitDiff/2;
									targetPlayer.hitUpdateRequired = true;
									targetPlayer.updateRequired = true;
									targetPlayer.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(3);
									targetPlayer.underAttackBy = c.playerId;
									targetPlayer.killedbyid = c.playerId;
									targetPlayer.hitUpdateRequired = true;	
									targetPlayer.updateRequired = true;
								}
								targetPlayer.logoutDelay = System.currentTimeMillis();
								targetPlayer.underAttackBy = c.playerId;
								targetPlayer.killedbyid = c.playerId;
								c.killedBy = targetPlayer.playerId;
								targetPlayer.singleCombatDelay = System.currentTimeMillis();
								targetPlayer.singleCombatDelay2 = System.currentTimeMillis();								
								targetPlayer.playerLevel[3] -= targetPlayer.hitDiff;
								c.totalPlayerDamageDealt += targetPlayer.hitDiff;
								targetPlayer.hitUpdateRequired = true;
								targetPlayer.updateRequired = true;
								targetPlayer.getActionAssistant().refreshSkill(3);
								return;								
							}
							break;
						case 7869:
							if(Misc.random(15) == 2)
							{
								targetPlayer.gfx100(754);
								targetPlayer.hitDiff = calculateRangeMaxHit() + Misc.random(15);
								if(targetPlayer.prayerActive[17] || targetPlayer.curseActive[8])
								{ 
									targetPlayer.hitDiff = targetPlayer.hitDiff / 2;
								}
								if (targetPlayer.playerLevel[3] - targetPlayer.hitDiff < 0) 
								{ 
									targetPlayer.hitDiff = targetPlayer.playerLevel[3];
								}		
								if(c.prayerActive[23]) 
								{ // smite
								double killSmite = 1;
		if (c.killStreak >= 15 &&  c.killStreak <= 17) {
			killSmite = 1.5;
		}
									smited =(int) killSmite*targetPlayer.hitDiff / 3;
									if(targetPlayer.playerRights > 1)
									{
									} 
									else 
									{
										targetPlayer.playerLevel[5] -= smited;
									}
									if(targetPlayer.playerLevel[5] <= 0) 
									{
										targetPlayer.playerLevel[5] = 0;
									}
									targetPlayer.getActionAssistant().refreshSkill(5);
								}					
								if(c.fightType == 3) 
								{
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*Config.RANGE_EXP_RATE/3), 4); 
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*11), 3);
									c.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(4);
								} 
								else 
								{
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*Config.RANGE_EXP_RATE), 4); 
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*11), 3);
									c.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(4);
								}
								boolean dropArrows = true;
										
								for(int noArrowId : c.NO_ARROW_DROP) 
								{
									if(c.lastWeaponUsed == noArrowId) 
									{
										c.inAir = true;
										dropArrows = false;
										break;
									}
								}
								if(dropArrows) 
								{
								try{
									c.getCombat().dropItem(c.rangeItemUsed, 1,targetPlayer.getX(), targetPlayer.getY(),c.heightLevel);
									c.inAir = true;
									}
									catch (Exception e) {}
								}
								if (targetPlayer.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 2550) 
								{
									int recoilDam = (int) ((double) (targetPlayer.hitDiff) * .10);
									if(recoilDam > 0)
									{
										c.hitDiff = recoilDam;
										c.playerLevel[3] -= recoilDam;
										c.getActionAssistant().refreshSkill(3);
										c.hitUpdateRequired = true;
										c.updateRequired = true;
										targetPlayer.recoil = c.recoil + 1;
										if(targetPlayer.recoil >= 50) 
										{
											targetPlayer.getActionAssistant().deleteEquipment(targetPlayer.playerEquipment[PlayerEquipmentConstants.PLAYER_RING],PlayerEquipmentConstants.PLAYER_RING);
											targetPlayer.getActionAssistant().sendMessage("Your ring of recoil has shattered!");
											targetPlayer.recoil = 0;
										}
									}
								}		
								if (targetPlayer.canVengeance && targetPlayer.hitDiff > 0) 
								{
									targetPlayer.hitDiff = targetPlayer.hitDiff;
									c.hitDiff = targetPlayer.hitDiff/2;
									targetPlayer.canVengeance = false;
									targetPlayer.forcedChat("Taste vengeance!");
									c.hitUpdateRequired = true;
									c.updateRequired = true;
									c.playerLevel[3] -= targetPlayer.hitDiff/2;
									targetPlayer.hitUpdateRequired = true;
									targetPlayer.updateRequired = true;
									targetPlayer.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(3);
									targetPlayer.underAttackBy = c.playerId;
									targetPlayer.killedbyid = c.playerId;
									targetPlayer.hitUpdateRequired = true;	
									targetPlayer.updateRequired = true;
								}
								targetPlayer.logoutDelay = System.currentTimeMillis();
								targetPlayer.underAttackBy = c.playerId;
								targetPlayer.killedbyid = c.playerId;
								c.killedBy = targetPlayer.playerId;
								targetPlayer.singleCombatDelay = System.currentTimeMillis();
								targetPlayer.singleCombatDelay2 = System.currentTimeMillis();								
								targetPlayer.playerLevel[3] -= targetPlayer.hitDiff;
								c.totalPlayerDamageDealt += targetPlayer.hitDiff;
								targetPlayer.hitUpdateRequired = true;
								targetPlayer.updateRequired = true;
								targetPlayer.getActionAssistant().refreshSkill(3);
								return;
							}	
							break;
						case 7870:
							if(Misc.random(15) == 3)
							{
								targetPlayer.gfx100(758);
								targetPlayer.hitDiff = 20 + Misc.random(15);
								if(targetPlayer.prayerActive[17] || targetPlayer.curseActive[8])
								{ 
									targetPlayer.hitDiff = targetPlayer.hitDiff / 2;
								}
								if (targetPlayer.playerLevel[3] - targetPlayer.hitDiff < 0) 
								{ 
									targetPlayer.hitDiff = targetPlayer.playerLevel[3];
								}		
								if(c.prayerActive[23]) 
								{ // smite
								double killSmite = 1;
		if (c.killStreak >= 15 &&  c.killStreak <= 17) {
			killSmite = 1.5;
		}
			
									smited =(int) killSmite*targetPlayer.hitDiff / 3;
									if(targetPlayer.playerRights > 1)
									{
									} 
									else 
									{
										targetPlayer.playerLevel[5] -= smited;
									}
									if(targetPlayer.playerLevel[5] <= 0) 
									{
										targetPlayer.playerLevel[5] = 0;
									}
									targetPlayer.getActionAssistant().refreshSkill(5);
								}					
								if(c.fightType == 3) 
								{
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*Config.RANGE_EXP_RATE/3), 4); 
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*11), 3);
									c.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(4);
								} 
								else 
								{
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*Config.RANGE_EXP_RATE), 4); 
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*11), 3);
									c.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(4);
								}
								boolean dropArrows = true;
										
								for(int noArrowId : c.NO_ARROW_DROP) 
								{
									if(c.lastWeaponUsed == noArrowId) 
									{
										c.inAir = true;
										dropArrows = false;
										break;
									}
								}
								if(dropArrows) 
								{
								try {
									c.getCombat().dropItem(c.rangeItemUsed, 1,targetPlayer.getX(), targetPlayer.getY(),c.heightLevel);
									c.inAir = true;
									}
									catch (Exception e) {}
								}
								if (targetPlayer.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 2550) 
								{
									int recoilDam = (int) ((double) (targetPlayer.hitDiff) * .10);
									if(recoilDam > 0)
									{
										c.hitDiff = recoilDam;
										c.playerLevel[3] -= recoilDam;
										c.getActionAssistant().refreshSkill(3);
										c.hitUpdateRequired = true;
										c.updateRequired = true;
										targetPlayer.recoil = c.recoil + 1;
										if(targetPlayer.recoil >= 50) 
										{
											targetPlayer.getActionAssistant().deleteEquipment(targetPlayer.playerEquipment[PlayerEquipmentConstants.PLAYER_RING],PlayerEquipmentConstants.PLAYER_RING);
											targetPlayer.getActionAssistant().sendMessage("Your ring of recoil has shattered!");
											targetPlayer.recoil = 0;
										}
									}
								}		
								if (targetPlayer.canVengeance && targetPlayer.hitDiff > 0) 
								{
									targetPlayer.hitDiff = targetPlayer.hitDiff;
									c.hitDiff = targetPlayer.hitDiff/2;
									targetPlayer.canVengeance = false;
									targetPlayer.forcedChat("Taste vengeance!");
									c.hitUpdateRequired = true;
									c.updateRequired = true;
									c.playerLevel[3] -= targetPlayer.hitDiff/2;
									targetPlayer.hitUpdateRequired = true;
									targetPlayer.updateRequired = true;
									targetPlayer.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(3);
									targetPlayer.underAttackBy = c.playerId;
									targetPlayer.killedbyid = c.playerId;
									targetPlayer.hitUpdateRequired = true;	
									targetPlayer.updateRequired = true;
								}
								targetPlayer.logoutDelay = System.currentTimeMillis();
								targetPlayer.underAttackBy = c.playerId;
								targetPlayer.killedbyid = c.playerId;
								c.killedBy = targetPlayer.playerId;
								targetPlayer.singleCombatDelay = System.currentTimeMillis();
								targetPlayer.singleCombatDelay2 = System.currentTimeMillis();								
								targetPlayer.playerLevel[3] -= targetPlayer.hitDiff;
								c.totalPlayerDamageDealt += targetPlayer.hitDiff;
								targetPlayer.hitUpdateRequired = true;
								targetPlayer.updateRequired = true;
								targetPlayer.getActionAssistant().refreshSkill(3);
								return;
							}
							break;
						case 7871:
							if(Misc.random(25) == 4)
							{
								targetPlayer.gfx100(756);
								targetPlayer.hitDiff = 30 + Misc.random(30);
								if(targetPlayer.prayerActive[17] || targetPlayer.curseActive[8])
								{ 
									targetPlayer.hitDiff = targetPlayer.hitDiff / 2;
								}
								if (targetPlayer.playerLevel[3] - targetPlayer.hitDiff < 0) 
								{ 
									targetPlayer.hitDiff = targetPlayer.playerLevel[3];
								}		
								if(c.prayerActive[23]) 
								{ // smite
								double killSmite = 1;
		if (c.killStreak >= 15 &&  c.killStreak <= 17) {
			killSmite = 1.5;
		}
			
									smited =(int) killSmite*targetPlayer.hitDiff / 3;
									if(targetPlayer.playerRights > 1)
									{
									} 
									else 
									{
										targetPlayer.playerLevel[5] -= smited;
									}
									if(targetPlayer.playerLevel[5] <= 0) 
									{
										targetPlayer.playerLevel[5] = 0;
									}
									targetPlayer.getActionAssistant().refreshSkill(5);
								}					
								if(c.fightType == 3) 
								{
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*Config.RANGE_EXP_RATE/3), 4); 
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*11), 3);
									c.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(4);
								} 
								else 
								{
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*Config.RANGE_EXP_RATE), 4); 
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*11), 3);
									c.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(4);
								}
								boolean dropArrows = true;
										
								for(int noArrowId : c.NO_ARROW_DROP) 
								{
									if(c.lastWeaponUsed == noArrowId) 
									{
										c.inAir = true;
										dropArrows = false;
										break;
									}
								}
								if(dropArrows) 
								{
								try {
									c.getCombat().dropItem(c.rangeItemUsed, 1,targetPlayer.getX(), targetPlayer.getY(),c.heightLevel);
									c.inAir = true;
								}
									catch (Exception e) {}
								}
								if (targetPlayer.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 2550) 
								{
									int recoilDam = (int) ((double) (targetPlayer.hitDiff) * .10);
									if(recoilDam > 0)
									{
										c.hitDiff = recoilDam;
										c.playerLevel[3] -= recoilDam;
										c.getActionAssistant().refreshSkill(3);
										c.hitUpdateRequired = true;
										c.updateRequired = true;
										targetPlayer.recoil = c.recoil + 1;
										if(targetPlayer.recoil >= 50) 
										{
											targetPlayer.getActionAssistant().deleteEquipment(targetPlayer.playerEquipment[PlayerEquipmentConstants.PLAYER_RING],PlayerEquipmentConstants.PLAYER_RING);
											targetPlayer.getActionAssistant().sendMessage("Your ring of recoil has shattered!");
											targetPlayer.recoil = 0;
										}
									}
								}		
								if (targetPlayer.canVengeance && targetPlayer.hitDiff > 0) 
								{
									targetPlayer.hitDiff = targetPlayer.hitDiff;
									c.hitDiff = targetPlayer.hitDiff/2;
									targetPlayer.canVengeance = false;
									targetPlayer.forcedChat("Taste vengeance!");
									c.hitUpdateRequired = true;
									c.updateRequired = true;
									c.playerLevel[3] -= targetPlayer.hitDiff/2;
									targetPlayer.hitUpdateRequired = true;
									targetPlayer.updateRequired = true;
									targetPlayer.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(3);
									targetPlayer.underAttackBy = c.playerId;
									targetPlayer.killedbyid = c.playerId;
									targetPlayer.hitUpdateRequired = true;	
									targetPlayer.updateRequired = true;
								}
								targetPlayer.logoutDelay = System.currentTimeMillis();
								targetPlayer.underAttackBy = c.playerId;
								targetPlayer.killedbyid = c.playerId;
								c.killedBy = targetPlayer.playerId;
								targetPlayer.singleCombatDelay = System.currentTimeMillis();
								targetPlayer.singleCombatDelay2 = System.currentTimeMillis();						
								targetPlayer.playerLevel[3] -= targetPlayer.hitDiff;
								c.totalPlayerDamageDealt += targetPlayer.hitDiff;
								targetPlayer.hitUpdateRequired = true;
								targetPlayer.updateRequired = true;
								targetPlayer.getActionAssistant().refreshSkill(3);
								return;
							}
							break;
						case 7872:
							if(Misc.random(40) == 5)
							{
								targetPlayer.gfx100(753);
								targetPlayer.hitDiff = 20 +Misc.random(20);
								if(targetPlayer.prayerActive[17] || targetPlayer.curseActive[8])
								{ 
									targetPlayer.hitDiff = targetPlayer.hitDiff / 2;
								}
								if (targetPlayer.playerLevel[3] - targetPlayer.hitDiff < 0) 
								{ 
									targetPlayer.hitDiff = targetPlayer.playerLevel[3];
								}		
								if(c.prayerActive[23]) 
								{ // smite
								double killSmite = 1;
		if (c.killStreak >= 15 &&  c.killStreak <= 17) {
			killSmite = 1.5;
		}
			
									smited =(int) killSmite*targetPlayer.hitDiff / 3;
									if(targetPlayer.playerRights > 1)
									{
									} 
									else 
									{
										targetPlayer.playerLevel[5] -= smited;
									}
									if(targetPlayer.playerLevel[5] <= 0) 
									{
										targetPlayer.playerLevel[5] = 0;
									}
									targetPlayer.getActionAssistant().refreshSkill(5);
								}					
								if(c.fightType == 3) 
								{
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*Config.RANGE_EXP_RATE/3), 4); 
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*11), 3);
									c.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(4);
								} 
								else 
								{
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*Config.RANGE_EXP_RATE), 4); 
									c.getActionAssistant().addSkillXP((targetPlayer.hitDiff*11), 3);
									c.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(4);
								}
								boolean dropArrows = true;
										
								for(int noArrowId : c.NO_ARROW_DROP) 
								{
									if(c.lastWeaponUsed == noArrowId) 
									{
										c.inAir = true;
										dropArrows = false;
										break;
									}
								}
								if(dropArrows) 
								{
									try {
									c.getCombat().dropItem(c.rangeItemUsed, 1,targetPlayer.getX(), targetPlayer.getY(),c.heightLevel);
									c.inAir = true;
									}
									catch (Exception e) {}
								}
								if (targetPlayer.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 2550) 
								{
									int recoilDam = (int) ((double) (targetPlayer.hitDiff) * .10);
									if(recoilDam > 0)
									{
										c.hitDiff = recoilDam;
										c.playerLevel[3] -= recoilDam;
										c.getActionAssistant().refreshSkill(3);
										c.hitUpdateRequired = true;
										c.updateRequired = true;
										targetPlayer.recoil = c.recoil + 1;
										if(targetPlayer.recoil >= 50) 
										{
											targetPlayer.getActionAssistant().deleteEquipment(targetPlayer.playerEquipment[PlayerEquipmentConstants.PLAYER_RING],PlayerEquipmentConstants.PLAYER_RING);
											targetPlayer.getActionAssistant().sendMessage("Your ring of recoil has shattered!");
											targetPlayer.recoil = 0;
										}
									}
								}		
								if (targetPlayer.canVengeance && targetPlayer.hitDiff > 0) 
								{
									targetPlayer.hitDiff = targetPlayer.hitDiff;
									c.hitDiff = targetPlayer.hitDiff/2;
									targetPlayer.canVengeance = false;
									targetPlayer.forcedChat("Taste vengeance!");
									c.hitUpdateRequired = true;
									c.updateRequired = true;
									c.playerLevel[3] -= targetPlayer.hitDiff/2;
									targetPlayer.hitUpdateRequired = true;
									targetPlayer.updateRequired = true;
									targetPlayer.getActionAssistant().refreshSkill(3);
									c.getActionAssistant().refreshSkill(3);
									targetPlayer.underAttackBy = c.playerId;
									targetPlayer.killedbyid = c.playerId;
									targetPlayer.hitUpdateRequired = true;	
									targetPlayer.updateRequired = true;
								}
								targetPlayer.logoutDelay = System.currentTimeMillis();
								targetPlayer.underAttackBy = c.playerId;
								targetPlayer.killedbyid = c.playerId;
								c.killedBy = targetPlayer.playerId;
								targetPlayer.singleCombatDelay = System.currentTimeMillis();
								targetPlayer.singleCombatDelay2 = System.currentTimeMillis();
								targetPlayer.playerLevel[3] -= targetPlayer.hitDiff;
								c.totalPlayerDamageDealt += targetPlayer.hitDiff;
								targetPlayer.hitUpdateRequired = true;
								targetPlayer.updateRequired = true;
								targetPlayer.getActionAssistant().refreshSkill(3);
								return;
							}
							break;
					}				
				}	
				c.usingBow = true;			
				c.gfx100(getRangeStartGFX());
				c.hitDelay = getHitDelay();
				c.projectileStage = 1;
				c.oldPlayerIndex = c.playerIndex;
			}		
			if(usingOtherRangeWeapons) 
			{	// knives, darts, etc hit delay
				c.rangeItemUsed = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
		 
				if(c.getCombatFormulas().ava() && Misc.random(4) == 1) 
				{
				} 
				else 
				{
					c.getActionAssistant().deleteEquipment();
				}
			
				c.usingRangeWeapon = true;
				/*if (c.inDuelArena()){
				 } else {
				 c.followId = PlayerManager.getSingleton().getPlayers()[c.playerIndex].playerId;
				 //c.getActionAssistant().followPlayer();
				 c.getActionAssistant().follow(c.followId, 1, c.getDis(c.absX, c.absY, PlayerManager.getSingleton().getPlayers()[c.followId].absX, PlayerManager.getSingleton().getPlayers()[c.followId].absY));
				 //c.followHandler.follow(c.followId);
				 }	*/
				Client k = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
				if ((usingOtherRangeWeapons) && c.withinDistance(k.absX, k.absY, c.absX, c.absY, 6))
				{
					if (!c.follower || c.followId != targetPlayer.playerId){
						c.followId = targetPlayer.playerId;
						c.follower = true;
					}	
				}			
				c.gfx100(getRangeStartGFX());
				c.hitDelay = getHitDelay();
				c.projectileStage = 1;
				c.inAir = true;
				c.oldPlayerIndex = c.playerIndex;
			} 
			if(c.usingMagic)
			{// && !c.inMulti() && c.canCast) {	// magic hit delay
				if(c.usingMagic && Config.MAGIC_LEVEL_REQUIRED) 
				{ // check magic level
					if(c.playerLevel[6] < c.MAGIC_SPELLS[c.spellId][1]) 
					{
						c.actionAssistant.sendMessage("You need to have a magic level of " +c.MAGIC_SPELLS[c.spellId][1]+" to cast this spell.");
						c.getCombat().resetAttack();
						return;
					}
				}
				c.attackTimer = System.currentTimeMillis();
				c.lastWeaponSpeed = 2600;
				Client k = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
				if ((c.usingMagic) && c.withinDistance(k.absX, k.absY, c.absX, c.absY, 15))
				{
					/*if (!c.follower || c.followId != targetPlayer.playerId){
						c.followId = targetPlayer.playerId;
						c.follower = true;
					}*/		
				}		
//autocast pvp				
	if(c.autoCast && !c.newSpell) 
			{
		//	c.getActionAssistant().sendMessage("1854");
		if(!c.getCombat().checkReqs()) 
		{
			c.faceUpdate(0);
			c.getFollowing().resetFollowing();
			c.playerIndex = 0;
			resetAttack2();
			//c.getActionAssistant().sendMessage("test");
			return;
		}
		if(System.currentTimeMillis() - c.lastAttack < AutoCast.coolDown[AutoCast.coolDownGroup[c.AutoCast_SpellIndex]])
						{
							return;
						}
			if(c.duelRule[4]) {
						c.getActionAssistant().sendMessage("Magic has been disabled in this duel!");
						c.getCombat().resetAttack();
						return;
					}
			if (c.AutoCast_SpellIndex <0) {
			return;
			}
if(((c.duelStatus >3) && (Misc.random(10+k.getCombat().calculateMagicDefence()) > Misc.random(12+calculateMagicAttack()))) || 
((c.duelStatus <1) && Misc.random(10+k.getCombat().calculateMagicDefence()) > Misc.random(calculateMagicAttack())))
				{
					magicDamage = 1;
					c.magicFailed2 = true;
					k.underAttackBy = c.playerId;
					k.logoutDelay = System.currentTimeMillis();;
					k.singleCombatDelay = System.currentTimeMillis();
					k.singleCombatDelay2 = System.currentTimeMillis();
				} 
				else 
				{
					magicDamage = 0;
				c.magicFailed2 = false;	
						
					int freezeDelay = c.getMagicData().getFreezeTimeAuto(c.AutoCast_SpellIndex)*2;//freeze time
					if(freezeDelay > 0 && k.freezeTimer == -5) 
					{ 
						k.freezeTimer = freezeDelay;
											 if(k.prayerActive[16] ||k.curseActive[7]) {
					 freezeDelay = (int)freezeDelay/2;
					 }
						k.stopMovement();
						k.getActionAssistant().Send("You have been frozen!");
						k.newFreeze = true;
						k.frozenBy = c.playerId;
					}
					k.underAttackBy = c.playerId;
					k.logoutDelay = System.currentTimeMillis();;
					k.singleCombatDelay = System.currentTimeMillis();
					k.singleCombatDelay2 = System.currentTimeMillis();
				}
				for(int a = 0; a < c.staffs.length; a++)
				{					
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == c.staffs[a] && c.AutoCast_SpellIndex >= 0)
					{
							if(AutoCast.MagicType[c.AutoCast_SpellIndex] == 0) 
						{//Projectile
						
							c.getActionAssistant().startAnimation(AutoCast.animG[AutoCast.animationGroup[c.AutoCast_SpellIndex]]);
							c.gfx100(AutoCast.MageStartingGFX[c.AutoCast_SpellIndex]);
							//Projectile2(c.absY, c.absX, offsetY, offsetX, 50, 80, MageMovingGFX[c.AutoCast_SpellIndex], 43, 31, attacknpc+1);
							//createPlayersProjectile(pX, pY, offX, offY, 50, 78, AutoCast.MageMovingGFX[c.AutoCast_SpellIndex], 43, 31, attackplayer + 1, 20);
							//createPlayersProjectile(pY, pX, offY, offX, 50, 78, c.MAGIC_SPELLS[c.spellId][4], getStartHeight(), getEndHeight(), attackplayer + 1, getStartDelay());
							//k.gfx0(AutoCast.MageEndingGFX[c.AutoCast_SpellIndex]);//, npc.getAbsY(), npc.getAbsX(), 90, AutoCast.gfxHeight[c.AutoCast_SpellIndex]);
						}
						if(AutoCast.MagicType[c.AutoCast_SpellIndex] == 1) 
						{//No Projectile
							c.getActionAssistant().startAnimation(AutoCast.animG[AutoCast.animationGroup[c.AutoCast_SpellIndex]]);
							c.gfx100(AutoCast.MageStartingGFX[c.AutoCast_SpellIndex]);
							//k.gfx0(AutoCast.MageEndingGFX[c.AutoCast_SpellIndex]);
						}            
						if(AutoCast.MagicType[c.AutoCast_SpellIndex] == 2) 
						{//Ending Only
							if(AutoCast.isMultiCast[c.AutoCast_SpellIndex] == 1) 
							{
								c.getActionAssistant().startAnimation(AutoCast.animG[AutoCast.animationGroup[c.AutoCast_SpellIndex]]);
								//k.gfx0(AutoCast.MageEndingGFX[c.AutoCast_SpellIndex]);
							}
							if(AutoCast.isMultiCast[c.AutoCast_SpellIndex] == 0) 
							{
								c.getActionAssistant().startAnimation(AutoCast.animG[AutoCast.animationGroup[c.AutoCast_SpellIndex]]);
								//k.gfx0(AutoCast.MageEndingGFX[c.AutoCast_SpellIndex]);
							}
						}  
						if(!c.getMagicData().hasComboRunes(c.AutoCast_SpellIndex)){
						if (AutoCast.runeGroup[c.AutoCast_SpellIndex] == 0) 
						{
							if (!c.actionAssistant.playerHasItem(AutoCast.RequiredRune1[c.AutoCast_SpellIndex], AutoCast.RequiredAmount1[c.AutoCast_SpellIndex]) || !c.actionAssistant.playerHasItem(AutoCast.RequiredRune2[c.AutoCast_SpellIndex], AutoCast.RequiredAmount2[c.AutoCast_SpellIndex])) 
							{    
								c.actionAssistant.sendMessage("You do not have the required runes for this spell!");
								resetAttack();
								return;
							}    
						}    
						if (AutoCast.runeGroup[c.AutoCast_SpellIndex] == 1) 
						{
							if (!c.actionAssistant.playerHasItem(AutoCast.RequiredRune1[c.AutoCast_SpellIndex], AutoCast.RequiredAmount1[c.AutoCast_SpellIndex]) || !c.actionAssistant.playerHasItem(AutoCast.RequiredRune2[c.AutoCast_SpellIndex], AutoCast.RequiredAmount2[c.AutoCast_SpellIndex]) ||
							((!c.actionAssistant.playerHasItem(AutoCast.RequiredRune3[c.AutoCast_SpellIndex], AutoCast.RequiredAmount3[c.AutoCast_SpellIndex])) && !(AutoCast.RequiredRune3[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)))
							{    
								c.actionAssistant.sendMessage("You do not have the required runes for this spell!");
								resetAttack();
								return;
							}    
						}    
						if (AutoCast.runeGroup[c.AutoCast_SpellIndex] == 2) 
						{
							if (!c.actionAssistant.playerHasItem(AutoCast.RequiredRune1[c.AutoCast_SpellIndex], AutoCast.RequiredAmount1[c.AutoCast_SpellIndex]) || !c.actionAssistant.playerHasItem(AutoCast.RequiredRune2[c.AutoCast_SpellIndex], AutoCast.RequiredAmount2[c.AutoCast_SpellIndex]) || 
							(!c.actionAssistant.playerHasItem(AutoCast.RequiredRune3[c.AutoCast_SpellIndex], AutoCast.RequiredAmount3[c.AutoCast_SpellIndex]) && !(AutoCast.RequiredRune3[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346))
							|| !c.actionAssistant.playerHasItem(AutoCast.RequiredRune4[c.AutoCast_SpellIndex], AutoCast.RequiredAmount4[c.AutoCast_SpellIndex])) 
							{    
								c.actionAssistant.sendMessage("You do not have the required runes for this spell!");
								resetAttack();
								return;
							}    
						}    
						if(c.playerLevel[6] < AutoCast.RequiredLevel[c.AutoCast_SpellIndex])
						{
							c.actionAssistant.sendMessage("You need a magic level of " + AutoCast.RequiredLevel[c.AutoCast_SpellIndex]);
							resetAttack();
							return;
						}            
						c.lastAttack = System.currentTimeMillis();				
				
						if (AutoCast.runeGroup[c.AutoCast_SpellIndex] == 0) 
						{
						if (!(AutoCast.RequiredRune1[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune1[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune1[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount1[c.AutoCast_SpellIndex]);
							}
							if (!(AutoCast.RequiredRune2[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune2[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune2[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount2[c.AutoCast_SpellIndex]);            
							}
						}    
						if (AutoCast.runeGroup[c.AutoCast_SpellIndex] == 1) 
						{
							if (!(AutoCast.RequiredRune1[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune1[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune1[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount1[c.AutoCast_SpellIndex]);
							}
							if (!(AutoCast.RequiredRune2[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune2[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune2[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount2[c.AutoCast_SpellIndex]);            
							}
							if (!(AutoCast.RequiredRune3[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune3[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune3[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount3[c.AutoCast_SpellIndex]);            
							}
						}    
						if (AutoCast.runeGroup[c.AutoCast_SpellIndex] == 2) 
						{
								if (!(AutoCast.RequiredRune1[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune1[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune1[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount1[c.AutoCast_SpellIndex]);
							}
							if (!(AutoCast.RequiredRune2[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune2[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune2[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount2[c.AutoCast_SpellIndex]);            
							}
							if (!(AutoCast.RequiredRune3[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune3[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune3[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount3[c.AutoCast_SpellIndex]);            
							}
								if (!(AutoCast.RequiredRune4[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune4[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune4[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount4[c.AutoCast_SpellIndex]);            
							}
						}
					}
					//c.hitDelay = getSpellHitDelay();
									//c.oldPlayerIndex = c.playerIndex;
									c.AutoCastplayerIndex = c.playerIndex;
									c.castingMagic = true;
									if ( multiMageAuto() && k.inMulti()) {
										MultiMageAutoCast();
									}
									else {
									c.autoCastPvpDelay = 4;
									}
								//magicFailed = false;
								
								//c.projectileStage = 2;
								//c.getActionAssistant().sendMessage("2029");
									//
					
				/*	if(!Server.getFightPits().isInFightPitsGame(c))*/
						return;
					}
				}				
			} 
			else if (c.autoCast)
			{
			
			//c.getActionAssistant().sendMessage("2035");
				if(c.newSpell) 
				{
					c.autoCast = true;
					c.attackTimer = System.currentTimeMillis();
				}				
			}
				int pX = c.getX();
				int pY = c.getY();
				int nX = Server.getPlayerManager().getPlayers()[c.playerIndex].getAbsX();
				int nY = Server.getPlayerManager().getPlayers()[c.playerIndex].getAbsY();
				int offX = (pY - nY)* -1;
				int offY = (pX - nX)* -1;
				c.castingMagic = true;
				c.projectileStage = 2;
				if(c.MAGIC_SPELLS[c.spellId][3] > 0) 
				{
					if(c.getMagicData().getStartGfxHeight() == 100) 
					{
						c.gfx100(c.MAGIC_SPELLS[c.spellId][3]);
					} 
					else 
					{
						c.gfx0(c.MAGIC_SPELLS[c.spellId][3]);
					}
				}
				if(c.MAGIC_SPELLS[c.spellId][4] > 0) 
				{
					createPlayersProjectile(pY, pX, offY, offX, 50, 78, c.MAGIC_SPELLS[c.spellId][4], c.getMagicData().getStartHeight(), c.getMagicData().getEndHeight(), attackplayer + 1, c.getMagicData().getStartDelay());
				}
				c.hitDelay = c.getMagicData().getSpellHitDelay();
				c.oldPlayerIndex = c.playerIndex;
				c.playerIndex = 0;
				Client o = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
				if(c.sounds == 1 && c.mage == 0) 
				{
					c.getActionAssistant().frame174(c.MAGIC_SPELLS[c.spellId][16], 000, 025);	
				}	
				if(c.sounds == 1 && c.mage == 1) 
				{
					c.getActionAssistant().frame174(c.MAGIC_SPELLS[c.spellId][16], 000, 000);	
				}
						
			} 
		//multi barrage
				if( c.usingMagic && multiMage() && c.canCast ){
				int hitcount = 0;
			c.attackTimer = System.currentTimeMillis();
						c.lastWeaponSpeed = 2600;
				//Client targetPlayer = (Client) PlayerManager.getSingleton().getPlayers()[attackplayer];
					Client mb = (Client) PlayerManager.getSingleton().getPlayers()[attackplayer];
					if (!mb.inMulti()) {
						return;
					}
					for (Player p : Server.getPlayerManager().getPlayers()) {
					magicFailed = true;
						int count = 0;
						Client m = (Client)p;
						if(m == null) continue;
						if(m.playerId == c.playerId) continue;
						if(!m.inMulti()) continue;
							if(Config.COMBAT_LEVEL_DIFFERENCE) {
								int combatDif1 = c.getCombat().getCombatDifference(c.combatLevel, PlayerManager.getSingleton().getPlayers()[m.playerId].combatLevel);
								if(combatDif1 > c.wildLevel || combatDif1 > PlayerManager.getSingleton().getPlayers()[m.playerId].wildLevel) {
								//	c.actionAssistant.sendMessage("Your combat level difference is too great to attack that player here.");
									c.stopMovement();
									continue;
								}
							}
							if(!PlayerManager.getSingleton().getPlayers()[m.playerId].inWild()) {
							//	c.actionAssistant.sendMessage("That player is not in the wilderness.");
								c.stopMovement();
								continue;
							}
							if(!c.inWild()) {
								//c.actionAssistant.sendMessage("You're not in the wilderness.");
								c.stopMovement();
								continue;
							}
							if (hitcount > 9) {
							break;
							}
							if(mb.goodDistance(PlayerManager.getSingleton().getPlayers()[m.playerId].getAbsX(), PlayerManager.getSingleton().getPlayers()[m.playerId].getAbsY(), mb.getAbsX(), mb.getAbsY(), 1)) {
							hitcount += 1;
							mb.underAttackBy = c.playerId;
					mb.logoutDelay = System.currentTimeMillis();;
					mb.singleCombatDelay = System.currentTimeMillis();
					mb.singleCombatDelay2 = System.currentTimeMillis();
							//if(Math.abs(m.absX - mb.absX) < 2 && Math.abs(m.absY - mb.absY) < 2) {
							magicFailed = false;
							magicDamage = 0;
if(((c.duelStatus >3) && (Misc.random(10+m.getCombat().calculateMagicDefence()) > Misc.random(12+calculateMagicAttack()))) ||
 ((c.duelStatus <1) && Misc.random(10+m.getCombat().calculateMagicDefence()) > Misc.random(calculateMagicAttack()))) {
						PlayerManager.getSingleton().getPlayers()[m.playerId].gfx100(85);
						magicDamage = 1;
						magicFailed = true;
					}
							int freezeDelay = c.getMagicData().getFreezeTime()*2;//freeze time
					if(freezeDelay > 0 && m.freezeTimer == -5 && !magicFailed) 
					{ 
						m.freezeTimer = freezeDelay;
											 if(m.prayerActive[16] || m.curseActive[7]) {
					 freezeDelay = (int)freezeDelay/2;
					 }
						m.stopMovement();
						m.getActionAssistant().Send("You have been frozen!");
						m.newFreeze = true;
						m.frozenBy = c.playerId;
					}
									c.reCast = 6;
									count++;
									if (count >= 8) break;
									int pX = c.getX();
									int pY = c.getY();
									int nX = Server.getPlayerManager().getPlayers()[m.playerId].getAbsX();
									int nY = Server.getPlayerManager().getPlayers()[m.playerId].getAbsY();
									int offX = (pY - nY)* -1;
									int offY = (pX - nX)* -1;
									c.castingMagic = true;
									c.projectileStage = 2;
									if(c.MAGIC_SPELLS[c.spellId][3] > 0) {
										if(c.getMagicData().getStartGfxHeight() == 100) {
											//m.gfx100(c.MAGIC_SPELLS[c.spellId][3]);
											PlayerManager.getSingleton().getPlayers()[m.playerId].gfx100(c.MAGIC_SPELLS[c.spellId][3]);
										} else {
											//m.gfx0(c.MAGIC_SPELLS[c.spellId][3]);
											PlayerManager.getSingleton().getPlayers()[m.playerId].gfx0(c.MAGIC_SPELLS[c.spellId][3]);
										}
									}
									
									
									if(c.MAGIC_SPELLS[c.spellId][4] > 0) {
										createPlayersProjectile(pY, pX, offY, offX, 50, 78, c.MAGIC_SPELLS[c.spellId][4], c.getMagicData().getStartHeight(), c.getMagicData().getEndHeight(), attackplayer + 1, c.getMagicData().getStartDelay());
									}
									c.hitDelay = c.getMagicData().getSpellHitDelay();
									c.oldPlayerIndex = c.playerIndex;
									c.playerIndex = 0;
									Client o = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
									if(c.sounds == 1) {
										c.getActionAssistant().frame174(c.MAGIC_SPELLS[c.spellId][16], 020, 000);
										c.getActionAssistant().frame174(c.MAGIC_SPELLS[c.spellId][17], 020, 075);					
									}
								

									playerDelayedHit(m.playerId);
								//	c.usingMagic = false;
								//	c.castingMagic = false;
								//	c.projectileStage = 0;
								
							}
					}
			/*	} else if(c.usingMagic && o.inMulti() && c.canCast){
						c.reCast = 4;
						int pX = c.getX();
						int pY = c.getY();
						int nX = Server.getPlayerManager().getPlayers()[c.playerIndex].getAbsX();
						int nY = Server.getPlayerManager().getPlayers()[c.playerIndex].getAbsY();
						int offX = (pY - nY)* -1;
						int offY = (pX - nX)* -1;
						c.castingMagic = true;
						c.projectileStage = 2;
						if(c.MAGIC_SPELLS[c.spellId][3] > 0) {
							if(getStartGfxHeight() == 100) {
								c.gfx100(c.MAGIC_SPELLS[c.spellId][3]);
							} else {
								c.gfx0(c.MAGIC_SPELLS[c.spellId][3]);
							}
						}
						if(c.MAGIC_SPELLS[c.spellId][4] > 0) {
							createPlayersProjectile(pY, pX, offY, offX, 50, 78, c.MAGIC_SPELLS[c.spellId][4], getStartHeight(), getEndHeight(), attackplayer + 1, getStartDelay());
						}
						c.hitDelay = getSpellHitDelay();
						c.oldPlayerIndex = c.playerIndex;
						c.playerIndex = 0;
						Client o = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
						if(c.sounds == 1) {
							c.getActionAssistant().frame174(c.MAGIC_SPELLS[c.spellId][16], 020, 000);
							c.getActionAssistant().frame174(c.MAGIC_SPELLS[c.spellId][17], 020, 075);					
						}
						//c.usingMagic = false;
						//c.castingMagic = false;
						//c.projectileStage = 0;
					
				} else {
				
				}*/
			}
		
		}
	}
		
	
	public void applyPlayerMeleeDamage(int i, int damageMask) 
	{
		Client targetPlayer = (Client) PlayerManager.getSingleton().getPlayers()[c.playerIndex];
		Client o = (Client) PlayerManager.getSingleton().getPlayers()[i];
		if (o.autoRetaliate && o.playerIndex == 0 && o.oldPlayerIndex == 0 && !o.inMulti()) {
			o.getAutoRetaliate().retaliate(c.playerId);
		}
			
		int pdamage = 0;
		double accuracyBoost = 1;
		if (c.lastWeaponUsed == c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) 
		{
			damage = Misc.random((int)(calculateMeleeMaxHit()));
			if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 14484 && c.dHit) {  // d claws spec
				accuracyBoost = 3.05;
			}
		}
		else 
		{
			damage = 0;
		}
		if(Misc.random(20+o.getCombat().calculateMeleeDefence()) > Misc.random((int)(10+ calculateMeleeAttack()*accuracyBoost))) 
		{
			damage = 0;
		}
		if (Misc.random(10) == 1) 
		{
			if(!o.isDead)
			{
				givepoison();
			}
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 6528 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == 6577) 
		{
			//	if (Misc.random(2) == 1) {
			damage += damage*0.2;
			//	}
		}
		boolean KorasiTemp = false;
		if (c.KorasiSpec) {	
			int	damage2 = (int)(calculateMeleeMaxHit());
			o.gfx100(1248);
			c.KorasiSpec = false;
			damage = (int)(Misc.random(100));
			damage +=  50;
			double percentincrease = damage/100D;
			damage = (int)(percentincrease*damage2);
			if (damage > 66) {damage = 66;}
			KorasiTemp = true;
		}		
		
		boolean AncientSpec = false;
		if (c.amace) {	
			int	damage2 = (int)(calculateMeleeMaxHit());
			c.amace = false;
			damage = (int)(Misc.random(100));
			damage +=  55;
			double percentincrease = damage/100D;
			damage = (int)(percentincrease*damage2);
			if (damage > 66) {damage = 66;}
			AncientSpec = true;
		}
		
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 14484 && c.dHit) {
			if (clawStrikes==1)
				damage = firstClawStrikeDamage / 2;
			else if (clawStrikes==2) {
				if (firstClawStrikeDamage > 4)
					damage = (firstClawStrikeDamage / 4) - 1 + Misc.random(2);
				else
					damage = 0;
			}
			else if (clawStrikes==3) {
				if (firstClawStrikeDamage > 4)
					damage = (firstClawStrikeDamage / 4) - 1 + Misc.random(2);
				else
					damage = 0;
			}
			else {  //must be 0 or 4
				firstClawStrikeDamage = damage;
			}
		}
		
		if(c.prayerActive[23]){ // smite			
			double killSmite = 1;
		if (c.killStreak >= 15 &&  c.killStreak <= 17) {
			killSmite = 1.5;
		}
			smited = (int)(killSmite*damage / 2.5);
			if(o.playerRights > 1){
			} else {
				o.playerLevel[5] -= smited;
			}
			if(o.playerLevel[5] <= 0){
				o.playerLevel[5] = 0;
			}
			o.getActionAssistant().refreshSkill(5);
		}
		
		if(o.prayerActive[18]  && !c.getCombatFormulas().fullVeracEquipped() && !KorasiTemp){// && o.inMulti()){// || o.prayerActive[18] && o.wildLevel >= 30) { // if prayer active reduce damage by half 
			damage = (int)(damage*0.60);
		} 
		if (o.curseActive[9] && !c.getCombatFormulas().fullVeracEquipped() && !KorasiTemp) {
			if (Misc.random(10) >= 4) {
					c.hitDiff = (int)damage/10;
					c.playerLevel[3] -= c.hitDiff;
					c.getActionAssistant().refreshSkill(3);
					c.hitUpdateRequired = true;
					c.updateRequired = true;
			
			}
			damage = (int)(damage*0.60);
		}
		else if((o.prayerActive[18] || o.curseActive[9]) && c.getCombatFormulas().fullVeracEquipped()){
			damage = damage;
		}
		if ((o.prayerActive[16] || o.curseActive[7]) && KorasiTemp) {
		damage = (int)(damage*0.60);
		}
	if(o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13740 && o.playerLevel[5] > 0){ // Divine Shield
			double div = damage *0.85;
			if (o.playerRights < 2 || o.prayer >= 1) 
			{
				o.playerLevel[5] -= (int)(0.25 * damage);
			}
			if(o.playerLevel[5] <= 0) 
			{
				o.playerLevel[5] = 0;
			}
			o.getActionAssistant().refreshSkill(5);
			damage = (int)div;
		}
		if (o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13742 && Misc.random(9) >= 6) {
			damage = (int)(damage*0.75);
		}
		if (damage > 20 && o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] ==18363) {
		int reduce = damage-20;
		damage -= (int)(0.14*reduce);
		}
		if (damage > 20 && o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] ==18359) {
		int reduce = damage-20;
		damage -= (int)(0.07*reduce);
		}
		damage = c.getCombatFormulas().GanodermicReductionMelee(damage);
		if (System.currentTimeMillis() - c.SOLspec < 60000) {
		damage = (int)(damage/2);
		}
		if (goliathGloves(c,o)) {
			damage = Misc.random((int)(calculateMeleeMaxHit()));
		}
		if (PlayerManager.getSingleton().getPlayers()[i].playerLevel[3] - damage < 0){ 
			damage = PlayerManager.getSingleton().getPlayers()[i].playerLevel[3];
		}	
		if (c.getCombatFormulas().fullAkrisaeEquipped() && Misc.random(6)==3){
			Client k = (Client) PlayerManager.getSingleton().getPlayers()[c.playerIndex];
			k.gfx100(398);
			int heal = damage;
			if(c.playerLevel[5] + heal >= c.getActionAssistant().getLevelForXP(c.playerXP[5])){
				c.playerLevel[5] = c.getActionAssistant().getLevelForXP(c.playerXP[5]);
			} else {
				c.playerLevel[5] += heal;
			}
			c.getActionAssistant().refreshSkill(5);
			c.updateRequired = true;
		}		
		if (c.getCombatFormulas().fullGuthanEquipped() && Misc.random(6)==3){
			Client k = (Client) PlayerManager.getSingleton().getPlayers()[c.playerIndex];
			k.gfx100(398);
			int heal = damage;
			if(c.playerLevel[3] + heal >= c.getActionAssistant().getLevelForXP(c.playerXP[3])){
				c.playerLevel[3] = c.getActionAssistant().getLevelForXP(c.playerXP[3]);
			} else {
				c.playerLevel[3] += heal;
			}
			c.getActionAssistant().refreshSkill(3);
			c.updateRequired = true;
		}	
		/*	if (c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 35 || c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 667 ||c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 746 ||c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 2402) {
		 if(c.playerRights > 9) {
		 damage = Misc.random((int)(calculateMeleeMaxHit()));
		 } else {
		 damage = 0;
		 }
		 }		*/	
		if(c.fightType == 3){
			c.getActionAssistant().addSkillXP((damage*Config.MELEE_EXP_RATE), 0); 
			c.getActionAssistant().addSkillXP((damage*Config.MELEE_EXP_RATE), 1);
			c.getActionAssistant().addSkillXP((damage*Config.MELEE_EXP_RATE), 2); 				
			c.getActionAssistant().addSkillXP((damage*Config.HP_RATE), 3);
			c.getActionAssistant().refreshSkill(0);
			c.getActionAssistant().refreshSkill(1);
			c.getActionAssistant().refreshSkill(2);
			c.getActionAssistant().refreshSkill(3);
		} else {
			c.getActionAssistant().addSkillXP((damage*Config.MELEE_EXP_RATE), c.fightType); 
			c.getActionAssistant().addSkillXP((damage*Config.HP_RATE), 3);
			c.getActionAssistant().refreshSkill(c.fightType);
			c.getActionAssistant().refreshSkill(3);
		}	

		if(c.zamGod && damage > 0){
			o.gfx0(539);
			o.freezeTimer = 40; // 20 seconds
			o.getActionAssistant().sendMessage("You've been frozen!");
			c.zamGod = false;
		}
		if (c.blackWhip){
			o.gfx100(382);
			if (damage > 0){
				int att = 0;
				att = damage / 2;
				o.playerLevel[0] -= att;
				if (o.playerLevel[0] <= 0){
					o.playerLevel[0] = 0;
				}
				o.getActionAssistant().refreshSkill(0);
				o.getActionAssistant().sendMessage("Your attack has been reduced!");
				c.blackWhip = false;
				
			}
		}
		if ((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13899 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13901) && damage >= 63) {damage = 62;}
		if (c.eleWhip) 
		{
			c.eleWhip = false;
			o.gfx100(76);
			damage += damage*0.1;
		}
				if (c.SARASWORD) {
		o.gfx100(1194);
		c.SARASWORD = false;
		}
		if (c.runeWhip && damage <0) 
		{
			o.gfx100(629);
			o.runeWhip = false;
		}
		else if (c.runeWhip && damage >0) 
		{
			o.gfx0(539);
			o.freezeTimer = 20; // 10 seconds
			o.getActionAssistant().sendMessage("You've been frozen!");
			c.runeWhip = false;
		}
		if (c.bWhip) 
		{
			o.gfx100(399);
			c.bWhip = false;
		}
		if (c.dragonWhip) 
		{
			o.gfx100(448);
			c.dragonWhip = false;
		}
		if(c.zamGod && damage < 0)
		{
			o.gfx100(629);
			c.zamGod = false;
		}
		
		if(c.bandosGod && damage > 0) 
		{
			int def = 0;
			def = damage / 2;
			o.playerLevel[1] -= def;
			
			if(o.playerLevel[1] <= 0) 
			{
				o.playerLevel[1] = 0;
			}
			o.getActionAssistant().refreshSkill(1);
			o.getActionAssistant().sendMessage("Your defence has been reduced!");
			c.bandosGod = false;
		}
		if(c.bandosGod && damage < 0)
		{
			c.bandosGod = false;
		}
		if (c.amace && damage > 0) { //Ancient Mace Effect
			/*int smite = 0;
			smite = o.playerLevel[5] = 0;
			o.playerLevel[5] = smite;
			c.playerLevel[5] += Misc.random(30) + 20;*/
			o.playerLevel[5] -= damage;
			c.playerLevel[5] += Misc.random(30) + 20;
			o.stopMovement();
			o.foodDelay = (System.currentTimeMillis() + 7000);
			o.potionDelay = (System.currentTimeMillis() + 7000);
			o.logoutDelay = System.currentTimeMillis();;
			o.singleCombatDelay = System.currentTimeMillis();
			o.singleCombatDelay2 = System.currentTimeMillis();
			//o.prayWait = 15;
			o.getActionAssistant().sendMessage("@blu@You've been stunned.");
			o.inCombat = true;
			o.attackTimer += 2500;
			o.frozenBy = c.playerId;
			o.getActionAssistant().sendMessage("@dre@Your prayer is fully drained.");
			c.getActionAssistant().sendMessage("@dre@Your target is smited, take him down!");
		if(o.prayerActive[18] || o.prayerActive[17] || o.prayerActive[16] || o.curseActive[7]|| o.curseActive[8]|| o.curseActive[9]) {
				o.prayerActive[18] = false;
				o.prayerActive[17] = false;
				o.prayerActive[16] = false;
				o.actionAssistant.sendFrame36(o.PRAYER_GLOW[17], 0);
				o.actionAssistant.sendFrame36(o.PRAYER_GLOW[18], 0);
				o.actionAssistant.sendFrame36(o.PRAYER_GLOW[19], 0);	
				o.curseActive[7] = false;
				o.curseActive[8] = false;
				o.curseActive[9] = false;
				o.actionAssistant.sendFrame36(o.CURSE_GLOW[7], 0);
				o.actionAssistant.sendFrame36(o.CURSE_GLOW[8], 0);
				o.actionAssistant.sendFrame36(o.CURSE_GLOW[9], 0);
				o.headIcon = -1;
				o.prayWait = 15;
				o.getActionAssistant().sendMessage("@dre@You feel a wild force blocking the usage of your prayers");
				o.updateRequired = true;
				o.getActionAssistant().requestUpdates();
				c.getActionAssistant().requestUpdates();
				//System.out.println("message sent");
			}
			double factor = 1;
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 19669) {
				factor = 0.9;
			}
			if(c.absX > o.absX) {
				o.freezeTimer = 0;
				o.getActionAssistant().playerWalk(o.absX - 1, o.absY,"cm 886 attack");
				o.gfx100(254); 
				o.freezeTimer = 12; // 20 seconds
			}
			if(c.absX < o.absX) {
				o.freezeTimer = 0;
				o.getActionAssistant().playerWalk(o.absX + 1, o.absY ,"cm 891 attack");
				o.gfx100(254); 
				o.freezeTimer = 12; // 20 seconds
			}
			if(c.absY > o.absY) {
				o.freezeTimer = 0;
				o.getActionAssistant().playerWalk(o.absX, o.absY - 1,"cm 898 attack");
				o.gfx100(254); 
				o.freezeTimer = 12; // 20 seconds
			}
			if(c.absY < o.absY) {
				o.freezeTimer = 0;
				o.getActionAssistant().playerWalk(o.absX, o.absY + 1,"cm 904 attack");
				o.gfx100(254); 
				o.freezeTimer = 12; // 20 seconds
			}
			if (o.playerLevel[5] < 0) {
				o.playerLevel[5] = 0;
			}
			o.getActionAssistant().refreshSkill(5);
			c.amace = false;
		}
		if(c.amace && damage <= 0)
		{
			c.amace = false;
		}
		if(c.dSpear) {

		}
		if((c.dScim) && (Misc.random(100) > 25)) 
		{
			if(o.prayerActive[18] || o.prayerActive[17] || o.prayerActive[16] || o.curseActive[7]|| o.curseActive[8]|| o.curseActive[9]) 
			{
				o.prayerActive[18] = false;
				o.prayerActive[17] = false;
				o.prayerActive[16] = false;
				o.actionAssistant.sendFrame36(o.PRAYER_GLOW[17], 0);
				o.actionAssistant.sendFrame36(o.PRAYER_GLOW[18], 0);
				o.actionAssistant.sendFrame36(o.PRAYER_GLOW[19], 0);	
				o.curseActive[7] = false;
				o.curseActive[8] = false;
				o.curseActive[9] = false;
				o.actionAssistant.sendFrame36(o.CURSE_GLOW[7], 0);
				o.actionAssistant.sendFrame36(o.CURSE_GLOW[8], 0);
				o.actionAssistant.sendFrame36(o.CURSE_GLOW[9], 0);
				o.headIcon = -1;
				o.prayWait = 10;
				o.updateRequired = true;
				o.getActionAssistant().requestUpdates();
				c.getActionAssistant().requestUpdates();
				//System.out.println("message sent");
			}
			c.dScim = false;
		}
		if (o.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 2550) 
		{
			int recoilDam = (int) ((double) (damage) * .10);
			if(recoilDam > 0)
			{
				c.hitDiff = recoilDam;
				c.playerLevel[3] -= recoilDam;
				c.getActionAssistant().refreshSkill(3);
				c.hitUpdateRequired = true;
				c.updateRequired = true;
				o.recoil = c.recoil + 1;
				if(o.recoil >= 50) 
				{
					o.getActionAssistant().deleteEquipment(o.playerEquipment[PlayerEquipmentConstants.PLAYER_RING],PlayerEquipmentConstants.PLAYER_RING);
					o.getActionAssistant().sendMessage("Your ring of recoil has shattered!");
					o.recoil = 0;
				}
			}
		}
		degradepvp(damage);
		PlayerKiller.addHits(o, c, damage);
		Server.getDegradeManager().doDegrade(o,damage);
		if(damage > 50 && damage < 59)
		{
			c.getNRAchievements().checkCombat(c,0);
		} 
		else if (damage >= 60 && damage < 69) 
		{
			c.getNRAchievements().checkCombat(c,1);
			c.getNRAchievements().checkCombat(c,0);
		} 
		else if (damage >= 70 && damage < 79) 
		{
			c.getNRAchievements().checkCombat(c,0);
			c.getNRAchievements().checkCombat(c,2);
			c.getNRAchievements().checkCombat(c,1);
		} 
		else if (damage >= 80) 
		{
			c.getNRAchievements().checkCombat(c,0);
			c.getNRAchievements().checkCombat(c,2);
			c.getNRAchievements().checkCombat(c,1);
			c.getNRAchievements().checkCombat(c,3);
		} 
				
		if(c.playerXP[2] > 999999999 && damage > 80)
		{
			c.getNRAchievements().checkCombat(c,7);
		} 
		if(c.specAch)
		{
			if(c.hits3 < 1 && damage >= 40)
			{
				c.hits3++;
			}
			if(c.hits3 > 0 && damage >= 40)
			{
				c.getNRAchievements().checkCombat(c,6);
				c.hits3 = 0;
			} 
			else 
			{
				c.hits3 = 0;
			}
		}
		if(c.playerXP[0] > 999999999 && damage > 40)
		{
			if(damage > 40)
			{
				c.attackhit++;
				if(c.attackhit == 3)
				{
					c.getNRAchievements().checkCombat(c,8);
					c.attackhit = 0;
				}
			} 
			else 
			{
				c.attackhit  = 0;
			}
		}
		if(damage == 0)
		{
			o.zeros++;
			if(o.zeros == 5)
			{
				o.getNRAchievements().checkCombat(o,9);
			}
		} 
		else 
		{
			o.zeros = 0;
		}
		c.specAch = false; 
		/*if (o.playerEquipment[PlayerConstants.PLAYER_HAT] == 13898) 
		{
			o.stathelm -= damage;
			if (o.stathelm <= 0) 
			{
				o.stathelm = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
	
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_HAT] == 13866) 
		{
			o.zurhelm -= damage;
			if (o.zurhelm <= 0) 
			{
				o.zurhelm = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_HAT] == 13878) 
		{
			o.morrhelm -= damage;
			if (o.morrhelm <= 0) 
			{
				o.morrhelm = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		if (o.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13889) 
		{
			o.vplate -= damage;
			if (o.vplate <= 0) 
			{
				o.vplate = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13860) 
		{
			o.zplate -= damage;
			if (o.zplate <= 0) 
			{
				o.zplate = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13872) 
		{
			o.mplate -= damage;
			if (o.mplate <= 0) 
			{
				o.mplate = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13886) 
		{
			o.splate -= damage;
			if (o.splate <= 0) 
			{
				o.splate = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		if (o.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13895) 
		{
			o.vleg -= damage;
			if (o.vleg <= 0) 
			{
				o.vleg = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13875) 
		{
			o.mleg -= damage;
			if (o.mleg <= 0) 
			{
				o.mleg = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13863) 
		{
			o.zleg -= damage;
			if (o.zleg <= 0) 
			{
				o.zleg = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13892) 
		{
			o.sleg -= damage;
			if (o.sleg <= 0) 
			{
				o.sleg = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}*/
		double wildFactor = 1;
		if (c.wildLevel >= 20) { wildFactor = 1.25;}
			c.epDamageCounter += (int)damage*wildFactor;
			if (c.epDamageCounter >= 500 && !c.inCWar()) {
				c.EP += 10+Misc.random(10);
				if (c.expansion) { c.EP += 5; }
				if (c.EP > 100) { c.EP = 100;}
				c.epDamageCounter = 0;
				c.lastEPgained = 0;
				c.getActionAssistant().writeQuestTab();
			}
		if (o.canVengeance && damage > 0) 
		{
			PlayerManager.getSingleton().getPlayers()[i].hitDiff = damage;
			c.hitDiff = PlayerManager.getSingleton().getPlayers()[i].hitDiff/2;
			o.canVengeance = false;
			o.forcedChat("Taste vengeance!");
			c.hitUpdateRequired = true;
			c.updateRequired = true;
			c.playerLevel[3] -= PlayerManager.getSingleton().getPlayers()[i].hitDiff/2;
			o.hitUpdateRequired = true;
			o.updateRequired = true;
			o.getActionAssistant().refreshSkill(3);
			c.getActionAssistant().refreshSkill(3);
			PlayerManager.getSingleton().getPlayers()[i].hitUpdateRequired = true;	
			PlayerManager.getSingleton().getPlayers()[i].updateRequired = true;
		}
		PlayerManager.getSingleton().getPlayers()[i].logoutDelay = System.currentTimeMillis();
		PlayerManager.getSingleton().getPlayers()[i].underAttackBy = c.playerId;
		PlayerManager.getSingleton().getPlayers()[i].killedbyid = c.playerId;
		c.killedBy = PlayerManager.getSingleton().getPlayers()[i].playerId;
		PlayerManager.getSingleton().getPlayers()[i].singleCombatDelay = System.currentTimeMillis();
		PlayerManager.getSingleton().getPlayers()[i].singleCombatDelay2 = System.currentTimeMillis();
		
		
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 14484 && c.dHit) {
			if (clawStrikes==1)
				damage = firstClawStrikeDamage / 2;
			else if (clawStrikes==2) {
				if (firstClawStrikeDamage > 4)
					damage = (firstClawStrikeDamage / 4) - 1 + Misc.random(2);
				else
					damage = 0;
			}
			else if (clawStrikes==3) {
				if (firstClawStrikeDamage > 4)
					damage = (firstClawStrikeDamage / 4) - 1 + Misc.random(2);
				else
					damage = 0;
			}
			else {  //must be 0 or 4
				clawStrikes=0;
				firstClawStrikeDamage = damage;
			}
			clawStrikes++;
		}
		/* CURSES */
		if (c.curseActive[19]) { // Turmoil
							if (PlayerManager.getSingleton().getPlayers()[c.playerIndex] != null) {
                                c.getStr = Server.getPlayerManager().getPlayers()[c.playerIndex].playerLevel[2] * 10 / 100;
                                c.getDef = Server.getPlayerManager().getPlayers()[c.playerIndex].playerLevel[1] * 15 / 100;
                                c.getAtt = Server.getPlayerManager().getPlayers()[c.playerIndex].playerLevel[0] * 15 / 100;
							}
                        } if(c.curseActive[10]) { // LeechAtt
								leechAttack(c.playerIndex);
                        } 
						if(c.curseActive[11]) { // Leech Ranged
								leechRanged(c.playerIndex);
                        } 
						if(c.curseActive[12]) { // Leech Magic
								leechMagic(c.playerIndex);
                        }  if (c.curseActive[13]) {//leechDef
								leechDefence(c.playerIndex);
                        } if (c.curseActive[14]) {//LeechStr
									leechStrength(c.playerIndex);
                        }
						if (c.curseActive[16]) {//Leechspecial
									leechSpecial(c.playerIndex);
                        }
						if(c.curseActive[18]) { // Asshole_rule
                                        if(c.oldPlayerIndex > 0 && PlayerManager.getSingleton().getPlayers()[c.oldPlayerIndex] != null) {
                                                try {
                                                        final int pX = c.getX();
                                                        final int pY = c.getY();
                                                        final int nX = PlayerManager.getSingleton().getPlayers()[i].getX();
                                                        final int nY = PlayerManager.getSingleton().getPlayers()[i].getY();
                                                        final int offX = (pY - nY)* -1;
                                                        final int offY = (pX - nX)* -1;
														final Client otherplayer = (Client) PlayerManager.getSingleton().getPlayers()[c.oldPlayerIndex];
                                                        c.getCombat().createPlayersProjectile2(pX, pY, offX, offY, 50, 50, 2263, 9, 9, c.oldPlayerIndex - 1, 24);
														c.playerLevel[3] += (int)damage/5;
														if (c.playerLevel[3] >= (c.getLevelForXP(c.playerXP[3]) + c.getActionAssistant().getMaxHP())) {
														c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]) + c.getActionAssistant().getMaxHP();
														}
															if (otherplayer.playerRights < 2 || otherplayer.prayer == 1) {
														otherplayer.playerLevel[5] -= (int)damage/5;
														}
														if (otherplayer.playerLevel[5] <= 0) {
														otherplayer.playerLevel[5] = 0;
														}
														otherplayer.getActionAssistant().refreshSkill(5);
														c.getActionAssistant().refreshSkill(3);
														if (Misc.random(4) == 0) {
                                                        EventManager.getSingleton().addEvent(c,"soulsplit1", new Event() {
                                                                public void execute(EventContainer b1) {
                                                                        if (otherplayer != null){
																		  Server.getStillGraphicsManager().stillGraphics(otherplayer,
				otherplayer.getAbsX(), otherplayer.getAbsY(), 0, 2264, 0);
                                                  //                              otherplayer.gfx0(2264); // 1738
																				}
																				b1.stop();
                                                                }
																@Override
																public void stop() {
																}
                                                        }, 500);
                                                        EventManager.getSingleton().addEvent(c,"soulsplit2", new Event() {
                                                                public void execute(EventContainer b2) {
                                                                        c.getCombat().createPlayersProjectile2(nX, nY, -offX, -offY, 50, 50, 2263, 9, 9, c.playerId - 1, 24);
                                                                                b2.stop();
                                                                }
																@Override
																public void stop() {
																}
                                                        }, 800);
														}
                                                } catch (Exception e) {
                                                        e.printStackTrace();
                                                }
                                        }
                                }
		if(Server.getCastleWars().isInCw(c)){
			c.cwDamage += damage;
		}
		switch(damageMask) 
		{
			case 1:
				PlayerManager.getSingleton().getPlayers()[i].hitDiff = damage;
				PlayerManager.getSingleton().getPlayers()[i].playerLevel[3] -= damage;
				c.totalPlayerDamageDealt += damage;
				o.getActionAssistant().refreshSkill(3);
				
				PlayerManager.getSingleton().getPlayers()[i].hitUpdateRequired = true;	
				PlayerManager.getSingleton().getPlayers()[i].updateRequired = true;
				break;
		
			case 2:
				PlayerManager.getSingleton().getPlayers()[i].hitDiff2 = damage;
				PlayerManager.getSingleton().getPlayers()[i].playerLevel[3] -= damage;
				c.totalPlayerDamageDealt += damage;
				o.getActionAssistant().refreshSkill(3);
				PlayerManager.getSingleton().getPlayers()[i].hitUpdateRequired2 = true;	
				PlayerManager.getSingleton().getPlayers()[i].updateRequired = true;
				c.dHit = false;
				break;
			
			case 3://poison
				PlayerManager.getSingleton().getPlayers()[i].hitDiff3 = pdamage;
				PlayerManager.getSingleton().getPlayers()[i].playerLevel[3] -= pdamage;
				c.totalPlayerDamageDealt += pdamage;
				o.getActionAssistant().refreshSkill(3);
				PlayerManager.getSingleton().getPlayers()[i].hitUpdateRequired3 = true;	
				PlayerManager.getSingleton().getPlayers()[i].updateRequired = true;
				c.dHit = false;
				break;			
		}
		
		c.inAir = false;
		if(c.didMove && !c.dHit)
		{
			resetAttack();
			c.didMove = false;
		}
	}

	public void applyPlayerRangeDamage(int i, int damageMask) 
	{
		Client targetPlayer = (Client) PlayerManager.getSingleton().getPlayers()[c.playerIndex];
		Client o = (Client) PlayerManager.getSingleton().getPlayers()[i];
		
		if(!c.getCombat().checkReqs()) 
		{
			c.faceUpdate(0);
			c.getFollowing().resetFollowing();
			c.playerIndex = 0;
			resetAttack2();
			return;
		}
		if (c.lastWeaponUsed == c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) 
		{
			damage = Misc.random((int)(calculateRangeMaxHit()));
		} 
		else 
		{
			damage = 0;
		}
		if(Misc.random(10+o.getCombat().calculateRangeDefence()) > Misc.random(10+calculateRangeAttack())) 
		{
			damage = 0;
		}
		
		if(o.prayerActive[17])
		{// && o.inMulti()){//|| o.prayerActive[17] && o.wildLevel >= 30) { // if prayer active reduce damage by half 
			damage = (int)(damage*0.60);
		}
		if (o.autoRetaliate && o.playerIndex == 0 && o.oldPlayerIndex == 0 && !o.inMulti()) {
			o.getAutoRetaliate().retaliate(c.playerId);
		}
		if (o.curseActive[8]) {
		if (Misc.random(10) >= 4) {
				c.hitDiff = (int)damage/10;
				c.playerLevel[3] -= c.hitDiff;
				c.getActionAssistant().refreshSkill(3);
				c.hitUpdateRequired = true;
				c.updateRequired = true;
		
		}
			damage = (int)(damage*0.60);
		}	
				
		if (Misc.random(10) == 1) 
		{
			if(!o.isDead)
			{				
				giveArrowPoison();
			}
		}			
		if(o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13740 && o.playerLevel[5] > 0){ // Divine Shield
			double div = damage *0.85;
			if (o.playerRights < 2 || o.prayer >= 1) 
			{
				o.playerLevel[5] -= (int)(0.25 * damage);
			}
			if(o.playerLevel[5] <= 0) 
			{
				o.playerLevel[5] = 0;
			}
			o.getActionAssistant().refreshSkill(5);
			damage = (int)div;
		}
		if (o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13742 && Misc.random(9) >= 6) {
			damage = (int)(damage*0.75);
		}
		
		if (damage > 20 && o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] ==18359) {
		int reduce = damage-20;
		damage -= (int)(0.14*reduce);
		}
		if (damage > 20 && o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] ==18361) {
		int reduce = damage-20;
		damage -= (int)(0.07*reduce);
		}
		if (PlayerManager.getSingleton().getPlayers()[i].playerLevel[3] - damage < 0) 
		{ 
			damage = PlayerManager.getSingleton().getPlayers()[i].playerLevel[3];
		}
		if(c.zbow && damage > 0) 
		{
			o.gfx0(539);
			o.freezeTimer = 60; // 30 seconds
			o.getActionAssistant().sendMessage("You've been frozen!");
			o.tbed = true;
			o.teleBlockDelay = 250;
			c.getActionAssistant().sendMessage(o.playerName+" has been half teleblocked");
			o.getActionAssistant().sendMessage("You have been half teleblocked");
			c.zbow = false;
		}
		else if (c.zbow && damage <= 0) 
		{
			o.gfx100(629);
			c.zbow=false;
		}
		if (c.sbow) 
		{
			c.sbow = false;
			o.gfx100(1194);
		}
		if (c.HANDCANNON) { 
		c.gfx100(2141);
		c.HANDCANNON = false;
		if (damage >40) { damage = 40; }
		}
		if(c.zamGod && damage <= 0)
		{
			o.gfx100(629);
			c.zamGod = false;
		}
		
		if(c.bbow && damage > 0) 
		{
			double pcnt =  (Misc.random(30) +20);
			pcnt = pcnt/100;
			int bowsmite = (int)(pcnt * o.playerLevel[5]); //Misc.random(30) + 20;
			o.gfx100(542);
			if(o.playerRights > 1)
			{
			} 
			else 
			{
				o.playerLevel[5] -= bowsmite;
			}
			if(o.playerLevel[5] <= 0) 
			{
				o.playerLevel[5] = 0;
			}
			c.bbow = false;
			o.getActionAssistant().refreshSkill(5);
		}
		else if (c.bbow && damage <= 0) 
		{
			c.bbow = false;
		}
		if(c.zaryteBow && damage > 0) {
			o.playerLevel[5] -= damage;
			o.specialAmount -= damage;
			o.gfx100(1194);
			o.getActionAssistant().sendMessage("@dre@Your prayer has been drained.");
			o.getActionAssistant().sendMessage("@dre@Your special has been drained.");
			if(o.playerLevel[5] <= 0) {
			   o.playerLevel[5] = 0;
			}
			if(o.specialAmount <= 0) {
			   o.specialAmount = 0;
			}
			c.zaryteBow = false;
			o.getActionAssistant().refreshSkill(5);
		}
		else if (c.zaryteBow && damage <= 0) 
		{
			c.zaryteBow = false;
		}
		if(c.bandosGod && damage < 0)
		{
			c.bandosGod = false;
		}
		if(c.amace && damage <= 0)
		{
			c.amace = false;
		}
		if (c.DarkBowSpec && !c.DarkBowSpec2 && (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4827|| (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] >= 15701 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] <= 15704))) {
			if (damage <8) {
				damage = 8;
				c.DarkBowSpec2 = true;
				//c.DarkBowSpec = false;
			}
		}
		else if (c.DarkBowSpec2 && (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4827|| (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] >= 15701 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] <= 15704))) {
		if (damage <8) {
				damage = 8;
				c.DarkBowSpec2 = false;
				c.DarkBowSpec = false;
			}
		}
		if(c.prayerActive[23]) 
		{ // smite			
		double killSmite = 1;
		if (c.killStreak >= 15 &&  c.killStreak <= 17) {
			killSmite = 1.5;
		}
			smited = (int)(killSmite*damage / 2.5);
			if(o.playerRights > 1)
			{
			} 
			else 
			{
				o.playerLevel[5] -= smited;
			}
			if(o.playerLevel[5] <= 0) 
			{
				o.playerLevel[5] = 0;
			}
			o.getActionAssistant().refreshSkill(5);
		}					

		if(c.fightType == 3) 
		{
			c.getActionAssistant().addSkillXP((damage*Config.RANGE_EXP_RATE/3), 4); 
			c.getActionAssistant().addSkillXP((damage*11), 3);
			c.getActionAssistant().refreshSkill(3);
			c.getActionAssistant().refreshSkill(4);
		} 
		else 
		{
			c.getActionAssistant().addSkillXP((damage*Config.RANGE_EXP_RATE), 4); 
			c.getActionAssistant().addSkillXP((damage*11), 3);
			c.getActionAssistant().refreshSkill(3);
			c.getActionAssistant().refreshSkill(4);
		}
		boolean dropArrows = true;
						
		for(int noArrowId : c.NO_ARROW_DROP) 
		{
			if(c.lastWeaponUsed == noArrowId) 
			{
				c.inAir = true;
				dropArrows = false;
				break;
			}
		}
		if(dropArrows) 
		{
		try {
			c.getCombat().dropItem(c.rangeItemUsed, 1,o.getX(), o.getY(),c.heightLevel);
			c.inAir = true;
		}
		catch (Exception e) {}
		}
		if (o.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 2550) 
		{
			int recoilDam = (int) ((double) (damage) * .10);
			if(recoilDam > 0)
			{
				c.hitDiff = recoilDam;
				c.playerLevel[3] -= recoilDam;
				c.getActionAssistant().refreshSkill(3);
				c.hitUpdateRequired = true;
				c.updateRequired = true;
				o.recoil = c.recoil + 1;
				if(o.recoil >= 50) 
				{
					o.getActionAssistant().deleteEquipment(o.playerEquipment[PlayerEquipmentConstants.PLAYER_RING],PlayerEquipmentConstants.PLAYER_RING);
					o.getActionAssistant().sendMessage("Your ring of recoil has shattered!");
					o.recoil = 0;
				}
			}
		}
		degradepvp(damage);
		PlayerKiller.addHits(o, c, damage);
		Server.getDegradeManager().doDegrade(o,damage);
		if(damage > 30)
		{
			c.rangeAchi++;
			if(c.rangeAchi == 2)
			{
				c.getNRAchievements().checkCombat(c,10);
				c.rangeAchi = 0;
			}
		} 
		else 
		{
			c.rangeAchi  = 0;
		}
	/*	if (o.playerEquipment[PlayerConstants.PLAYER_HAT] == 13898) 
		{
			o.stathelm -= damage;
			if (o.stathelm <= 0) 
			{
				o.stathelm = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
	
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_HAT] == 13866) 
		{
			o.zurhelm -= damage;
			if (o.zurhelm <= 0) 
			{
				o.zurhelm = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_HAT] == 13878) 
		{
			o.morrhelm -= damage;
			if (o.morrhelm <= 0) 
			{
				o.morrhelm = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		if (o.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13889) 
		{
			o.vplate -= damage;
			if (o.vplate <= 0) 
			{
				o.vplate = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13860) 
		{
			o.zplate -= damage;
			if (o.zplate <= 0) 
			{
				o.zplate = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13872) 
		{
			o.mplate -= damage;
			if (o.mplate <= 0) 
			{
				o.mplate = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13886) 
		{
			o.splate -= damage;
			if (o.splate <= 0) 
			{
				o.splate = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		if (o.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13895) 
		{
			o.vleg -= damage;
			if (o.vleg <= 0) 
			{
				o.vleg = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13875) 
		{
			o.mleg -= damage;
			if (o.mleg <= 0) 
			{
				o.mleg = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13863) 
		{
			o.zleg -= damage;
			if (o.zleg <= 0) 
			{
				o.zleg = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}
		else if (o.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13892) 
		{
			o.sleg -= damage;
			if (o.sleg <= 0) 
			{
				o.sleg = 0;
				DegradeCheck degradecheck = new DegradeCheck(o);
			}
		}*/
			/* CURSES */
		if (c.curseActive[19]) { // Turmoil
							if (PlayerManager.getSingleton().getPlayers()[c.playerIndex] != null) {
                                c.getStr = Server.getPlayerManager().getPlayers()[c.playerIndex].playerLevel[2] * 10 / 100;
                                c.getDef = Server.getPlayerManager().getPlayers()[c.playerIndex].playerLevel[1] * 15 / 100;
                                c.getAtt = Server.getPlayerManager().getPlayers()[c.playerIndex].playerLevel[0] * 15 / 100;
							}
                        } if(c.curseActive[10]) { // LeechAtt
								leechAttack(c.playerIndex);
                        } 
						if(c.curseActive[11]) { // Leech Ranged
								leechRanged(c.playerIndex);
                        } 
						if(c.curseActive[12]) { // Leech Magic
								leechMagic(c.playerIndex);
                        }  if (c.curseActive[13]) {//leechDef
								leechDefence(c.playerIndex);
                        } if (c.curseActive[14]) {//LeechStr
									leechStrength(c.playerIndex);
                        }
						if (c.curseActive[16]) {//Leechspecial
									leechSpecial(c.playerIndex);
                        }
						if(c.curseActive[18]) { // Asshole_rule
                                        if(c.oldPlayerIndex > 0 && PlayerManager.getSingleton().getPlayers()[c.oldPlayerIndex] != null) {
                                                try {
                                                        final int pX = c.getX();
                                                        final int pY = c.getY();
                                                        final int nX = PlayerManager.getSingleton().getPlayers()[i].getX();
                                                        final int nY = PlayerManager.getSingleton().getPlayers()[i].getY();
                                                        final int offX = (pY - nY)* -1;
                                                        final int offY = (pX - nX)* -1;
														final Client otherplayer = (Client) PlayerManager.getSingleton().getPlayers()[c.oldPlayerIndex];
                                                        c.getCombat().createPlayersProjectile2(pX, pY, offX, offY, 50, 50, 2263, 9, 9, c.oldPlayerIndex - 1, 24);
														c.playerLevel[3] += (int)damage/5;
														if (c.playerLevel[3] >= (c.getLevelForXP(c.playerXP[3]) + c.getActionAssistant().getMaxHP())) {
														c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]) + c.getActionAssistant().getMaxHP();
														}
															if (otherplayer.playerRights < 2 || otherplayer.prayer == 1) {
														otherplayer.playerLevel[5] -= (int)damage/5;
														}
														if (otherplayer.playerLevel[5] <= 0) {
														otherplayer.playerLevel[5] = 0;
														}
														otherplayer.getActionAssistant().refreshSkill(5);
														c.getActionAssistant().refreshSkill(3);
														if (Misc.random(4) == 0) {
                                                        EventManager.getSingleton().addEvent(c,"soulsplit1", new Event() {
                                                                public void execute(EventContainer b1) {
                                                                        if (otherplayer != null){
																		  Server.getStillGraphicsManager().stillGraphics(otherplayer,
				otherplayer.getAbsX(), otherplayer.getAbsY(), 0, 2264, 0);
                                                  //                              otherplayer.gfx0(2264); // 1738
																				}
																				b1.stop();
                                                                }
																@Override
																public void stop() {
																}
                                                        }, 500);
                                                        EventManager.getSingleton().addEvent(c,"soulsplit2", new Event() {
                                                                public void execute(EventContainer b2) {
                                                                        c.getCombat().createPlayersProjectile2(nX, nY, -offX, -offY, 50, 50, 2263, 9, 9, c.playerId - 1, 24);
                                                                                b2.stop();
                                                                }
																@Override
																public void stop() {
																}
                                                        }, 800);
														}
                                                } catch (Exception e) {
                                                        e.printStackTrace();
                                                }
                                        }
                                }
		if(Server.getCastleWars().isInCw(c)){
			c.cwDamage += damage;
		}
		double wildFactor = 1;
		if (c.wildLevel >= 20) { wildFactor = 1.25;}
			c.epDamageCounter += (int)damage*wildFactor;
			if (c.epDamageCounter >= 500 && !c.inCWar()) {
				c.EP += 10+Misc.random(10);
				if (c.expansion) { c.EP += 5; }
				if (c.EP > 100) { c.EP = 100;}
				c.epDamageCounter = 0;
				c.lastEPgained = 0;
				c.getActionAssistant().writeQuestTab();
			}
		if (o.canVengeance && damage > 0) 
		{
			PlayerManager.getSingleton().getPlayers()[i].hitDiff = damage;
			c.hitDiff = PlayerManager.getSingleton().getPlayers()[i].hitDiff/2;
			o.canVengeance = false;
			o.forcedChat("Taste vengeance!");
			c.hitUpdateRequired = true;
			c.updateRequired = true;
			c.playerLevel[3] -= PlayerManager.getSingleton().getPlayers()[i].hitDiff/2;
			o.hitUpdateRequired = true;
			o.updateRequired = true;
			o.getActionAssistant().refreshSkill(3);
			c.getActionAssistant().refreshSkill(3);
			PlayerManager.getSingleton().getPlayers()[i].underAttackBy = c.playerId;
			PlayerManager.getSingleton().getPlayers()[i].killedbyid = c.playerId;
			PlayerManager.getSingleton().getPlayers()[i].hitUpdateRequired = true;	
			PlayerManager.getSingleton().getPlayers()[i].updateRequired = true;
		}
		PlayerManager.getSingleton().getPlayers()[i].logoutDelay = System.currentTimeMillis();
		PlayerManager.getSingleton().getPlayers()[i].underAttackBy = c.playerId;
		PlayerManager.getSingleton().getPlayers()[i].killedbyid = c.playerId;
		c.killedBy = PlayerManager.getSingleton().getPlayers()[i].playerId;
		PlayerManager.getSingleton().getPlayers()[i].singleCombatDelay = System.currentTimeMillis();
		PlayerManager.getSingleton().getPlayers()[i].singleCombatDelay2 = System.currentTimeMillis();
		switch(damageMask) 
		{
			case 1:
				PlayerManager.getSingleton().getPlayers()[i].hitDiff = damage;
				PlayerManager.getSingleton().getPlayers()[i].playerLevel[3] -= damage;
				c.totalPlayerDamageDealt += damage;
				o.getActionAssistant().refreshSkill(3);
				PlayerManager.getSingleton().getPlayers()[i].hitUpdateRequired = true;	
				PlayerManager.getSingleton().getPlayers()[i].updateRequired = true;
				break;
		
			case 2:
				PlayerManager.getSingleton().getPlayers()[i].hitDiff2 = damage;
				PlayerManager.getSingleton().getPlayers()[i].playerLevel[3] -= damage;
				c.totalPlayerDamageDealt += damage;
				o.getActionAssistant().refreshSkill(3);
				PlayerManager.getSingleton().getPlayers()[i].hitUpdateRequired2 = true;	
				PlayerManager.getSingleton().getPlayers()[i].updateRequired = true;
				c.dHit = false;
				break;	
		}
		c.inAir = false;
		if(c.didMove && !c.dHit)
		{
			resetAttack();
			c.didMove = false;
		}
	}		
		
		public void playerAutoCast(int i, boolean magefail) 
	{
		Client targetPlayer = (Client) PlayerManager.getSingleton().getPlayers()[c.playerIndex];

		if (targetPlayer != null) 
		{
			if (targetPlayer.isDead()) 
			{
				resetAttack();
				return;
			}
		}
		
		if (PlayerManager.getSingleton().getPlayers()[i] != null) 
		{
			if (PlayerManager.getSingleton().getPlayers()[i].isDead) 
			{
				c.playerIndex = 0;
				return;
			}
			
			if (PlayerManager.getSingleton().getPlayers()[i].respawnTimer > 0) 
			{
				c.faceUpdate(0);
				c.playerIndex = 0;
				return;
			}
			
			Client o = (Client) PlayerManager.getSingleton().getPlayers()[i];
			if (o.autoRetaliate && o.playerIndex == 0 && o.oldPlayerIndex == 0 && !o.inMulti()) {
				o.getAutoRetaliate().retaliate(c.playerId);
			}
			o.getActionAssistant().removeAllWindows();
			
			//if(o.attackTimer <= 1500 || o.attackTimer == 0 && o.playerIndex == 0) { // block animation
			if (System.currentTimeMillis() - c.attackTimer > 0 && o.playerIndex == 0)
			{

				o.getActionAssistant().startAnimation(o.getCombatEmotes().getBlockEmote());
			}
			
				c.inCombat = true;
				int damage = Misc.random(AutoCast.MageHit[c.AutoCast_SpellIndex]);
				if (magefail) {
				damage = 0;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 15486) 
				{
					damage += damage*0.15;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 17017) 
				{
					damage += damage*0.30;
				}
					int bonus = 0;
				if (c.playerLevel[6] > c.getLevelForXP(c.playerXP[6])) {
				bonus += (c.getLevelForXP(c.playerXP[6]) - c.playerLevel[6]) *0.03;
				}
				damage += damage*bonus;
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18355) 
				{
					damage += damage*0.20;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == 18335) {
				damage += damage*0.15;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == 18334) {
				damage += damage*0.10;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == 18333) {
				damage += damage*0.05;
				}
				if(magicDamage == 1) 
				{
					damage = 0;
					magicDamage = 0;
				}
				if(o.prayerActive[16])
				{// && o.inMulti()){//|| o.prayerActive[16] && o.wildLevel >= 30) { // if prayer active reduce damage by half 
					damage = damage / 2;
				}
			if(o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13740 && o.playerLevel[5] > 0){ // Divine Shield
			double div = damage *0.85;
			if (o.playerRights < 2 || o.prayer >= 1) 
			{
				o.playerLevel[5] -= (int)(0.25 * damage);
			}
			if(o.playerLevel[5] <= 0) 
			{
				o.playerLevel[5] = 0;
			}
			o.getActionAssistant().refreshSkill(5);
			damage = (int)div;
		}
		if (o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13742 && Misc.random(9) >= 6) {
			damage = (int)(damage*0.75);
		}
		if (damage > 20 && o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] ==18363) {
		int reduce = damage-20;
		damage -= (int)(0.07*reduce);
		}
		if (damage > 20 && o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] ==18361) {
		int reduce = damage-20;
		damage -= (int)(0.14*reduce);
		}
				if(c.prayerActive[23]) 
				{ // smite			
					double killSmite = 1;
		if (c.killStreak >= 15 &&  c.killStreak <= 17) {
			killSmite = 1.5;
		}
			smited = (int)(killSmite*damage / 2.5);
					if(o.playerRights > 1)
					{
					} 
					else 
					{
						o.playerLevel[5] -= smited;
					}
					if(o.playerLevel[5] <= 0) 
					{
						o.playerLevel[5] = 0;
					}
					o.getActionAssistant().refreshSkill(5);
				}		
			if (o.curseActive[7]) {
		if (Misc.random(10) >= 4) {
				c.hitDiff = (int)damage/10;
				c.playerLevel[3] -= c.hitDiff;
				c.getActionAssistant().refreshSkill(3);
				c.hitUpdateRequired = true;
				c.updateRequired = true;
		
		}
			damage = damage / 2;
		}				
				if (PlayerManager.getSingleton().getPlayers()[i].playerLevel[3] - damage < 0) 
				{ 
					damage = PlayerManager.getSingleton().getPlayers()[i].playerLevel[3];
				}				
				c.getActionAssistant().addSkillXP((damage*Config.RANGE_EXP_RATE), 6); 
				c.getActionAssistant().addSkillXP((damage*11), 3);
				c.getActionAssistant().refreshSkill(3);
				c.getActionAssistant().refreshSkill(6);
					
				if (!magefail) {
					if(AutoCast.MagicType[c.AutoCast_SpellIndex] == 0) 
						{//Projectile
						
							c.gfx100(AutoCast.MageStartingGFX[c.AutoCast_SpellIndex]);
							//Projectile2(c.absY, c.absX, offsetY, offsetX, 50, 80, MageMovingGFX[c.AutoCast_SpellIndex], 43, 31, attacknpc+1);
							createPlayersProjectile(pX, pY, offX, offY, 50, 78, AutoCast.MageMovingGFX[c.AutoCast_SpellIndex], 43, 31, o.playerId + 1, 20);
							//createPlayersProjectile(pY, pX, offY, offX, 50, 78, c.MAGIC_SPELLS[c.spellId][4], getStartHeight(), getEndHeight(), attackplayer + 1, getStartDelay());
							
							o.gfx0(AutoCast.MageEndingGFX[c.AutoCast_SpellIndex]);//, npc.getAbsY(), npc.getAbsX(), 90, AutoCast.gfxHeight[c.AutoCast_SpellIndex]);
						}
						if(AutoCast.MagicType[c.AutoCast_SpellIndex] == 1) 
						{//No Projectile
							c.gfx100(AutoCast.MageStartingGFX[c.AutoCast_SpellIndex]);
							o.gfx0(AutoCast.MageEndingGFX[c.AutoCast_SpellIndex]);
						}            
						if(AutoCast.MagicType[c.AutoCast_SpellIndex] == 2) 
						{//Ending Only
							if(AutoCast.isMultiCast[c.AutoCast_SpellIndex] == 1) 
							{
							if (c.AutoCast_SpellIndex == 31 && !o.newFreeze) {
							o.gfx50(1677);
							}
							else {
								o.gfx0(AutoCast.MageEndingGFX[c.AutoCast_SpellIndex]);
							}
							o.newFreeze = false;
							}
							if(AutoCast.isMultiCast[c.AutoCast_SpellIndex] == 0) 
							{
								o.gfx0(AutoCast.MageEndingGFX[c.AutoCast_SpellIndex]);
							}
						}  
				}
				
				if(magefail) 
				{	
					PlayerManager.getSingleton().getPlayers()[i].gfx100(85);
					if(c.sounds == 1) 
					{
						c.getActionAssistant().frame174(940,020,025);
					}
				}
				if (o.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 2550) 
				{
					int recoilDam = (int) ((double) (damage) * .10);
					if(recoilDam > 0)
					{
						c.hitDiff = recoilDam;
						c.playerLevel[3] -= recoilDam;
						c.getActionAssistant().refreshSkill(3);
						c.hitUpdateRequired = true;
						c.updateRequired = true;
						o.recoil = c.recoil + 1;
						if(o.recoil >= 50) 
						{
							o.getActionAssistant().deleteEquipment(o.playerEquipment[PlayerEquipmentConstants.PLAYER_RING],PlayerEquipmentConstants.PLAYER_RING);
							o.getActionAssistant().sendMessage("Your ring of recoil has shattered!");
							o.recoil = 0;
						}
					}
				}				
double wildFactor = 1;
		if (c.wildLevel >= 20) { wildFactor = 1.25;}
			c.epDamageCounter += (int)damage*wildFactor;
			if (c.epDamageCounter >= 500 && !c.inCWar()) {
				c.EP += 10+Misc.random(10);
				if (c.expansion) { c.EP += 5; }
				if (c.EP > 100) { c.EP = 100;}
				c.epDamageCounter = 0;
				c.lastEPgained = 0;
				c.getActionAssistant().writeQuestTab();
			}
				if (o.canVengeance && damage > 0) 
				{
					PlayerManager.getSingleton().getPlayers()[i].hitDiff = damage;
					PlayerManager.getSingleton().getPlayers()[i].underAttackBy = c.playerId;
					PlayerManager.getSingleton().getPlayers()[i].killedbyid = c.playerId;
					c.hitDiff = PlayerManager.getSingleton().getPlayers()[i].hitDiff/2;
					o.canVengeance = false;
					o.forcedChat("Taste vengeance!");
					c.hitUpdateRequired = true;
					c.updateRequired = true;
					c.playerLevel[3] -= PlayerManager.getSingleton().getPlayers()[i].hitDiff/2;
					o.hitUpdateRequired = true;
					o.updateRequired = true;
					o.getActionAssistant().refreshSkill(3);
					c.getActionAssistant().refreshSkill(3);
					PlayerManager.getSingleton().getPlayers()[i].hitUpdateRequired = true;	
					PlayerManager.getSingleton().getPlayers()[i].updateRequired = true;
				}
		
					/* CURSES */
		if (c.curseActive[19]) { // Turmoil
							if (PlayerManager.getSingleton().getPlayers()[c.playerIndex] != null) {
                                c.getStr = Server.getPlayerManager().getPlayers()[c.playerIndex].playerLevel[2] * 10 / 100;
                                c.getDef = Server.getPlayerManager().getPlayers()[c.playerIndex].playerLevel[1] * 15 / 100;
                                c.getAtt = Server.getPlayerManager().getPlayers()[c.playerIndex].playerLevel[0] * 15 / 100;
							}
                        } if(c.curseActive[10]) { // LeechAtt
								leechAttack(o.playerId);
                        } 
						if(c.curseActive[11]) { // Leech Ranged
								leechRanged(o.playerId);
                        } 
						if(c.curseActive[12]) { // Leech Magic
								leechMagic(o.playerId);
                        }  if (c.curseActive[13]) {//leechDef
								leechDefence(o.playerId);
                        } if (c.curseActive[14]) {//LeechStr
									leechStrength(o.playerId);
                        }
						if (c.curseActive[16]) {//Leechspecial
									leechSpecial(o.playerId);
                        }
						if(c.curseActive[18]) { // Asshole_rule
                                        if(o != null) {
                                                try {
                                                        final int pX = c.getX();
                                                        final int pY = c.getY();
                                                        final int nX = PlayerManager.getSingleton().getPlayers()[i].getX();
                                                        final int nY = PlayerManager.getSingleton().getPlayers()[i].getY();
                                                        final int offX = (pY - nY)* -1;
                                                        final int offY = (pX - nX)* -1;
														final Client otherplayer = (Client) PlayerManager.getSingleton().getPlayers()[o.playerId];
                                                        c.getCombat().createPlayersProjectile2(pX, pY, offX, offY, 50, 50, 2263, 9, 9, c.oldPlayerIndex - 1, 24);
														c.playerLevel[3] += (int)damage/5;
														if (c.playerLevel[3] >= (c.getLevelForXP(c.playerXP[3]) + c.getActionAssistant().getMaxHP())) {
														c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]) + c.getActionAssistant().getMaxHP();
														}
															if (otherplayer.playerRights < 2 || otherplayer.prayer == 1) {
														otherplayer.playerLevel[5] -= (int)damage/5;
														}
														if (otherplayer.playerLevel[5] <= 0) {
														otherplayer.playerLevel[5] = 0;
														}
														otherplayer.getActionAssistant().refreshSkill(5);
														c.getActionAssistant().refreshSkill(3);
														if (Misc.random(4) == 0) {
                                                        EventManager.getSingleton().addEvent(c,"soulsplit1", new Event() {
                                                                public void execute(EventContainer b1) {
                                                                        if (otherplayer != null){
																		  Server.getStillGraphicsManager().stillGraphics(otherplayer,
				otherplayer.getAbsX(), otherplayer.getAbsY(), 0, 2264, 0);
                                                  //                              otherplayer.gfx0(2264); // 1738
																				}
																				b1.stop();
                                                                }
																@Override
																public void stop() {
																}
                                                        }, 500);
                                                        EventManager.getSingleton().addEvent(c,"soulsplit2", new Event() {
                                                                public void execute(EventContainer b2) {
                                                                        c.getCombat().createPlayersProjectile2(nX, nY, -offX, -offY, 50, 50, 2263, 9, 9, c.playerId - 1, 24);
                                                                                b2.stop();
                                                                }
																@Override
																public void stop() {
																}
                                                        }, 800);
														}
                                                } catch (Exception e) {
                                                        e.printStackTrace();
                                                }
                                        }
                                }
				PlayerManager.getSingleton().getPlayers()[i].logoutDelay = System.currentTimeMillis();
				PlayerManager.getSingleton().getPlayers()[i].underAttackBy = c.playerId;
				PlayerManager.getSingleton().getPlayers()[i].killedbyid = c.playerId;
				PlayerManager.getSingleton().getPlayers()[i].singleCombatDelay = System.currentTimeMillis();
				PlayerManager.getSingleton().getPlayers()[i].singleCombatDelay2 = System.currentTimeMillis();
				o.inCombat = true;
				
					PlayerManager.getSingleton().getPlayers()[i].hitDiff = damage;
					PlayerManager.getSingleton().getPlayers()[i].playerLevel[3] -= damage;
					c.totalPlayerDamageDealt += damage;
					PlayerManager.getSingleton().getPlayers()[i].hitUpdateRequired = true;	
				c.killedBy = PlayerManager.getSingleton().getPlayers()[i].playerId;	
				PlayerKiller.addHits(o, c, damage);
				Server.getDegradeManager().doDegrade(o,damage);
				if(damage > 30)
				{
					c.mageAchi++;
					if(c.mageAchi == 2)
					{
						c.getNRAchievements().checkCombat(c,11);
						c.mageAchi = 0;
					}
				} 
				else 
				{
					c.mageAchi  = 0;
				}
				o.getActionAssistant().refreshSkill(3);				
				PlayerManager.getSingleton().getPlayers()[i].updateRequired = true;
		c.getActionAssistant().requestUpdates();
		
		c.projectileStage = 0;
		}
	}
		
	public void playerDelayedHit(int i) 
	{
		Client targetPlayer = (Client) PlayerManager.getSingleton().getPlayers()[c.playerIndex];

		if (targetPlayer != null) 
		{
			if (targetPlayer.isDead()) 
			{
				resetAttack();
				return;
			}
		}
		
		if (PlayerManager.getSingleton().getPlayers()[i] != null) 
		{
			if (PlayerManager.getSingleton().getPlayers()[i].isDead) 
			{
				c.playerIndex = 0;
				return;
			}
			
			if (PlayerManager.getSingleton().getPlayers()[i].respawnTimer > 0) 
			{
				c.faceUpdate(0);
				c.playerIndex = 0;
				return;
			}
			
			Client o = (Client) PlayerManager.getSingleton().getPlayers()[i];
			o.getActionAssistant().removeAllWindows();
			if (o.autoRetaliate && o.playerIndex == 0 && o.oldPlayerIndex == 0 && !o.inMulti()) {
				o.getAutoRetaliate().retaliate(c.playerId);
			}
			//if(o.attackTimer <= 1500 || o.attackTimer == 0 && o.playerIndex == 0) { // block animation
			if (System.currentTimeMillis() - c.attackTimer > 0 && o.playerIndex == 0)
			{

				o.getActionAssistant().startAnimation(o.getCombatEmotes().getBlockEmote());
				/*if(o.autoRetaliate){
					o.faceUpdate(c.playerId);
					o.getCombat().attackPlayer(o.underAttackBy);
				}*/
			}
			if(c.projectileStage == 0) 
			{ // melee hit damage

				if(c.duelRule[3]) {
						c.getActionAssistant().sendMessage("Melee has been disabled in this duel!");
						c.getCombat().resetAttack();
						return;
					}
				applyPlayerMeleeDamage(i, 1);
				if(c.dHit) 
				{
				if(c.duelRule[3]) {
						c.getActionAssistant().sendMessage("Melee has been disabled in this duel!");
						c.getCombat().resetAttack();
						return;
					}
					applyPlayerMeleeDamage(i, 2);
				}	
			}
			if(!c.castingMagic && c.projectileStage > 0) 
			{
				applyPlayerRangeDamage(i, 1);
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]== 4827 || (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] >= 15701 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]<= 15704)) {
					c.dHit = true;
				}
				if(c.dHit || swiftGloves(c, o))
				{
					applyPlayerRangeDamage(i, 2);
				}	
			} 
			else if (c.projectileStage > 0) 
			{ // magic hit damage
				c.inCombat = true;
				int damage = Misc.random(c.MAGIC_SPELLS[c.spellId][6]);
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 15486) 
				{
					damage += damage*0.15;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 17017) 
				{
					damage += damage*0.30;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 8841) 
				{
					damage += damage*0.20;
				}
				if (spellGloves(c, o)) {
					damage += damage*0.20;
				}
				if (c.getCombatFormulas().polyPore()) {
					damage += damage * 0.13;
				}
				if (c.getCombatFormulas().armaStaff()) {
					damage += damage * 0.15;
				}				
					int bonus = 0;
				if (c.playerLevel[6] > c.getLevelForXP(c.playerXP[6])) {
				bonus += (c.getLevelForXP(c.playerXP[6]) - c.playerLevel[6]) *0.03;
				}
				damage += damage*bonus;
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == 18335) {
				damage += damage*0.15;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == 18334) {
				damage += damage*0.10;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == 18333) {
				damage += damage*0.05;
				}
				
				if(c.getMagicData().godSpells()) 
				{
					int staffRequired = c.getMagicData().getStaffNeeded();
					if(c.usingMagic && staffRequired > 0 && Config.RUNES_REQUIRED) 
					{ // staff required
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != staffRequired) 
						{
							c.actionAssistant.sendMessage("You need a "+Server.getItemManager().getItemDefinition(staffRequired).getName().toLowerCase()+" to cast this spell.");
							c.getCombat().resetAttack();
							return;
						}
					}	
					int capeRequired = c.getMagicData().getCapes();
					if(c.usingMagic && capeRequired > 0 && Config.RUNES_REQUIRED) 
					{ // cape required
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE] != capeRequired) 
						{
							c.actionAssistant.sendMessage("You need a "+Server.getItemManager().getItemDefinition(capeRequired).getName().toLowerCase()+" to cast this spell.");
							c.getCombat().resetAttack();
							return;
						}
					}					
					if(System.currentTimeMillis() - c.godSpellDelay < Config.GOD_SPELL_CHARGE) 
					{
						damage = 10+Misc.random(20);
					}
				}
				c.playerIndex = 0;
				if(magicDamage == 1) 
				{
					damage = 0;
					magicDamage = 0;
				}
				if(o.prayerActive[16])
				{// && o.inMulti()){//|| o.prayerActive[16] && o.wildLevel >= 30) { // if prayer active reduce damage by half 
					damage =(int)(damage*0.60);
				}
				if (o.curseActive[7]) {
					if (Misc.random(10) >= 4) {
							c.hitDiff = (int)damage/10;
							c.playerLevel[3] -= c.hitDiff;
							c.getActionAssistant().refreshSkill(3);
							c.hitUpdateRequired = true;
							c.updateRequired = true;
					
					}
					damage = (int)(damage*0.60);
				}
				if (c.getCombatFormulas().fullAkrisaeEquipped() && Misc.random(6)==3){
						o.gfx100(398);
						int heal = damage;
						if(c.playerLevel[5] + heal >= c.getActionAssistant().getLevelForXP(c.playerXP[5])){
							c.playerLevel[5] = c.getActionAssistant().getLevelForXP(c.playerXP[5]);
						} else {
							c.playerLevel[5] += heal;
						}
						c.getActionAssistant().refreshSkill(5);
						c.updateRequired = true;
				}	
		if(o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13740 && o.playerLevel[5] > 0){ // Divine Shield
			double div = damage *0.85;
			if (o.playerRights < 2 || o.prayer >= 1) 
			{
				o.playerLevel[5] -= (int)(0.25 * damage);
			}
			if(o.playerLevel[5] <= 0) 
			{
				o.playerLevel[5] = 0;
			}
			o.getActionAssistant().refreshSkill(5);
			damage = (int)div;
		}
		if (o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13742 && Misc.random(9) >= 6) {
			damage = (int)(damage*0.75);
		}
		if (damage > 20 && o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] ==18363) {
		int reduce = damage-20;
		damage -= (int)(0.07*reduce);
		}
		if (damage > 20 && o.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] ==18361) {
		int reduce = damage-20;
		damage -= (int)(0.14*reduce);
		}
		damage = c.getCombatFormulas().GanodermicReductionMage(damage);
		
				if(c.prayerActive[23]) 
				{ // smite			
						double killSmite = 1;
		if (c.killStreak >= 15 &&  c.killStreak <= 17) {
			killSmite = 1.5;
		}
			smited = (int)(killSmite*damage / 2.5);
					if(o.playerRights > 1)
					{
					} 
					else 
					{
						o.playerLevel[5] -= smited;
					}
					if(o.playerLevel[5] <= 0) 
					{
						o.playerLevel[5] = 0;
					}
					o.getActionAssistant().refreshSkill(5);
				}		
		if (o.curseActive[7]) {
		if (Misc.random(10) >= 4) {
				c.hitDiff = (int)damage/10;
				c.playerLevel[3] -= c.hitDiff;
				c.getActionAssistant().refreshSkill(3);
				c.hitUpdateRequired = true;
				c.updateRequired = true;
		
		}
		damage = (int)damage / 2;
			}
		
				if (PlayerManager.getSingleton().getPlayers()[i].playerLevel[3] - damage < 0) 
				{ 
					damage = PlayerManager.getSingleton().getPlayers()[i].playerLevel[3];
				}				
				c.getActionAssistant().addSkillXP((c.MAGIC_SPELLS[c.spellId][7] + damage*Config.MAGIC_EXP_RATE), 6); 
				c.getActionAssistant().addSkillXP((c.MAGIC_SPELLS[c.spellId][7] + damage*11), 3);
				c.getActionAssistant().refreshSkill(3);
				c.getActionAssistant().refreshSkill(6);
					
				if(c.getMagicData().getEndGfxHeight() == 100 && !magicFailed)
				{ // end GFX
					PlayerManager.getSingleton().getPlayers()[i].gfx100(c.MAGIC_SPELLS[c.spellId][5]);
				} 
				else if (!magicFailed)
				{
				if (c.spellId == 48 && !o.newFreeze) {
							o.gfx50(1677);
				}
				else {
					PlayerManager.getSingleton().getPlayers()[i].gfx0(c.MAGIC_SPELLS[c.spellId][5]);
				}
				o.newFreeze = false;
				}
				
				if(magicFailed) 
				{	
					PlayerManager.getSingleton().getPlayers()[i].gfx100(85);
					if(c.sounds == 1) 
					{
						c.getActionAssistant().frame174(940,020,025);
					}
				}
				
				if(!magicFailed) 
				{	
					// int freezeDelay = getFreezeTime();//freeze time
					// if(freezeDelay > 0 && PlayerManager.getSingleton().getPlayers()[i].freezeTimer == -6) { 
					// PlayerManager.getSingleton().getPlayers()[i].freezeTimer = freezeDelay;
					// o.stopMovement();
					// }
					if(c.sounds == 1 && c.mage == 0) 
					{
						c.getActionAssistant().frame174(c.MAGIC_SPELLS[c.spellId][17], 020, 000);
					}
					if(c.sounds == 1 && c.mage == 1) 
					{
						c.getActionAssistant().frame174(c.MAGIC_SPELLS[c.spellId][17], 000, 000);
					}					
					if(System.currentTimeMillis() - PlayerManager.getSingleton().getPlayers()[i].reduceStat > 10000){
						PlayerManager.getSingleton().getPlayers()[i].reduceStat = System.currentTimeMillis();
						switch(c.MAGIC_SPELLS[c.spellId][0]) 
						{ 
							case 12987:
								PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] -= ((o.getActionAssistant().getLevelForXP(PlayerManager.getSingleton().getPlayers()[i].playerXP[0]) * 10) / 100);
								if(PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] <= 0) 
								{
									PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] = 0;
								}
								break;
							
							case 13011:
								PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] -= ((o.getActionAssistant().getLevelForXP(PlayerManager.getSingleton().getPlayers()[i].playerXP[0]) * 10) / 100);
								if(PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] <= 0) 
								{
									PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] = 0;
								}
								break;
							
							case 12999:
								PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] -= ((o.getActionAssistant().getLevelForXP(PlayerManager.getSingleton().getPlayers()[i].playerXP[0]) * 10) / 100);
								if(PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] <= 0) 
								{
									PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] = 0;
								}
								break;
							
							case 13023:
								//PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] -= ((o.getActionAssistant().getLevelForXP(PlayerManager.getSingleton().getPlayers()[i].playerXP[0]) * 15) / 100);
								PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] -= ((PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] * 15) / 100);
								if(PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] <= 0) 
								{
									PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] = 0;
								}
								o.getActionAssistant().refreshSkill(0);
								break;
							
							case 12975:
								if(!o.isDead)
								{
									if (Misc.random(3) == 1) 
									{
										o.getCombat().poison(5);
										c.getActionAssistant().sendMessage("Your opponent is poisoned!");
									}
								}
								break;
						}
					}
					
					switch(c.MAGIC_SPELLS[c.spellId][0]) 
					{ 	
						case 12445: //teleblock
							o.underAttackBy = c.playerId;
							o.logoutDelay = System.currentTimeMillis();;
							o.singleCombatDelay = System.currentTimeMillis();
							o.singleCombatDelay2 = System.currentTimeMillis();
							/*if(o.tbImmunity > 0) 
							{
								c.getActionAssistant().sendMessage(o.playerName+ " is currently immune to teleblock for " +(o.tbImmunity/2)+ " more seconds!");
								resetAttack();
								break;	}	*/ //Teleblock immunity disabled.
								
							if(o.tbed == true) 
							{
								c.getActionAssistant().sendMessage(o.playerName+" has already been teleblocked");
								resetAttack();
								break;								
									
							} 
							else 
							{
								if(!o.prayerActive[16] && !o.curseActive[7]) 
								{
									o.tbed = true;
									o.teleBlockDelay = 599;
									resetAttack();
									c.getActionAssistant().sendMessage(o.playerName+" has been full teleblocked");
									o.getActionAssistant().sendMessage("You have been full teleblocked");
								} 
								else if(o.prayerActive[16] || o.curseActive[7])
								{
									o.tbed = true;
									o.teleBlockDelay = 299;
									resetAttack();
									c.getActionAssistant().sendMessage(o.playerName+" has been half teleblocked");
									o.getActionAssistant().sendMessage("You have been half teleblocked");
								}
							}
							break;						
						case 12901:
						case 12919: // blood spells
						case 12911:
						case 12929:
							int heal = damage / 4;
							if(c.playerLevel[3] + heal >= c.getActionAssistant().getLevelForXP(c.playerXP[3])) 
							{
								c.playerLevel[3] = c.getActionAssistant().getLevelForXP(c.playerXP[3]);
							} 
							else 
							{
								c.playerLevel[3] += heal;
							}
							if(c.playerLevel[3] <= 0) 
							{
								c.playerLevel[3] = 0;
							}
							c.getActionAssistant().refreshSkill(3);
							break;
						
						case 1153:						
							PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] -= ((o.getActionAssistant().getLevelForXP(PlayerManager.getSingleton().getPlayers()[i].playerXP[0]) * 5) / 100);
							o.getActionAssistant().sendMessage("Your attack level has been reduced!");
							PlayerManager.getSingleton().getPlayers()[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
							if(o.playerLevel[0] <= 0) 
							{
								o.playerLevel[0] = 0;
							}						
							o.getActionAssistant().refreshSkill(0);
							break;
						
						case 1157:
							PlayerManager.getSingleton().getPlayers()[i].playerLevel[2] -= ((o.getActionAssistant().getLevelForXP(PlayerManager.getSingleton().getPlayers()[i].playerXP[2]) * 5) / 100);
							o.getActionAssistant().sendMessage("Your strength level has been reduced!");
							PlayerManager.getSingleton().getPlayers()[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
							if(o.playerLevel[2] <= 0) 
							{
								o.playerLevel[2] = 0;
							}						
							o.getActionAssistant().refreshSkill(2);
							break;
						
						case 1161:
							PlayerManager.getSingleton().getPlayers()[i].playerLevel[1] -= ((o.getActionAssistant().getLevelForXP(PlayerManager.getSingleton().getPlayers()[i].playerXP[1]) * 5) / 100);
							o.getActionAssistant().sendMessage("Your defence level has been reduced!");
							PlayerManager.getSingleton().getPlayers()[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
							if(o.playerLevel[1] <= 0) 
							{
								o.playerLevel[1] = 0;
							}						
							o.getActionAssistant().refreshSkill(1);
							break;
						
						case 1542:
							PlayerManager.getSingleton().getPlayers()[i].playerLevel[1] -= ((o.getActionAssistant().getLevelForXP(PlayerManager.getSingleton().getPlayers()[i].playerXP[1]) * 10) / 100);
							o.getActionAssistant().sendMessage("Your defence level has been reduced!");
							PlayerManager.getSingleton().getPlayers()[i].reduceSpellDelay[c.reduceSpellId] =  System.currentTimeMillis();
							if(o.playerLevel[1] <= 0) 
							{
								o.playerLevel[1] = 0;
							}						
							o.getActionAssistant().refreshSkill(1);
							break;
						
						case 1543:
							PlayerManager.getSingleton().getPlayers()[i].playerLevel[2] -= ((o.getActionAssistant().getLevelForXP(PlayerManager.getSingleton().getPlayers()[i].playerXP[2]) * 10) / 100);
							o.getActionAssistant().sendMessage("Your strength level has been reduced!");
							PlayerManager.getSingleton().getPlayers()[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
							if(o.playerLevel[2] <= 0) 
							{
								o.playerLevel[2] = 0;
							}						
							o.getActionAssistant().refreshSkill(2);
							break;
						
						case 1562:					
							PlayerManager.getSingleton().getPlayers()[i].playerLevel[0] -= ((o.getActionAssistant().getLevelForXP(PlayerManager.getSingleton().getPlayers()[i].playerXP[0]) * 10) / 100);
							o.getActionAssistant().sendMessage("Your attack level has been reduced!");
							PlayerManager.getSingleton().getPlayers()[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
							if(o.playerLevel[0] <= 0) 
							{
								o.playerLevel[0] = 0;
							}						
							o.getActionAssistant().refreshSkill(0);
							break;
					}					
				}
				if (o.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 2550) 
				{
					int recoilDam = (int) ((double) (damage) * .10);
					if(recoilDam > 0)
					{
						c.hitDiff = recoilDam;
						c.playerLevel[3] -= recoilDam;
						c.getActionAssistant().refreshSkill(3);
						c.hitUpdateRequired = true;
						c.updateRequired = true;
						o.recoil = c.recoil + 1;
						if(o.recoil >= 50) 
						{
							o.getActionAssistant().deleteEquipment(o.playerEquipment[PlayerEquipmentConstants.PLAYER_RING],PlayerEquipmentConstants.PLAYER_RING);
							o.getActionAssistant().sendMessage("Your ring of recoil has shattered!");
							o.recoil = 0;
						}
					}
				}					
				double wildFactor = 1;
		if (c.wildLevel >= 20) { wildFactor = 1.25;}
			c.epDamageCounter += (int)damage*wildFactor;
			if (c.epDamageCounter >= 500 && !c.inCWar()) {
				c.EP += 10+Misc.random(10);
				if (c.expansion) { c.EP += 5; }
				if (c.EP > 100) { c.EP = 100;}
				c.epDamageCounter = 0;
				c.lastEPgained = 0;
				c.getActionAssistant().writeQuestTab();
			}
				if (o.canVengeance && damage > 0) 
				{
					PlayerManager.getSingleton().getPlayers()[i].hitDiff = damage;
					PlayerManager.getSingleton().getPlayers()[i].underAttackBy = c.playerId;
					PlayerManager.getSingleton().getPlayers()[i].killedbyid = c.playerId;
					c.hitDiff = PlayerManager.getSingleton().getPlayers()[i].hitDiff/2;
					o.canVengeance = false;
					o.forcedChat("Taste vengeance!");
					c.hitUpdateRequired = true;
					c.updateRequired = true;
					c.playerLevel[3] -= PlayerManager.getSingleton().getPlayers()[i].hitDiff/2;
					o.hitUpdateRequired = true;
					o.updateRequired = true;
					o.getActionAssistant().refreshSkill(3);
					c.getActionAssistant().refreshSkill(3);
					PlayerManager.getSingleton().getPlayers()[i].hitUpdateRequired = true;	
					PlayerManager.getSingleton().getPlayers()[i].updateRequired = true;
				}
					/*
		if (c.curseActive[19]) { // Turmoil
							if (PlayerManager.getSingleton().getPlayers()[c.playerIndex] != null) {
                                c.getStr = Server.getPlayerManager().getPlayers()[c.playerIndex].playerLevel[2] * 10 / 100;
                                c.getDef = Server.getPlayerManager().getPlayers()[c.playerIndex].playerLevel[1] * 15 / 100;
                                c.getAtt = Server.getPlayerManager().getPlayers()[c.playerIndex].playerLevel[0] * 15 / 100;
							}
                        } if(c.curseActive[10]) { // LeechAtt
								leechAttack(c.playerIndex);
                        } 
						if(c.curseActive[11]) { // Leech Ranged
								leechRanged(c.playerIndex);
                        } 
						if(c.curseActive[12]) { // Leech Magic
								leechMagic(c.playerIndex);
                        }  if (c.curseActive[13]) {//leechDef
								leechDefence(c.playerIndex);
                        } if (c.curseActive[14]) {//LeechStr
									leechStrength(c.playerIndex);
                        }
						if (c.curseActive[16]) {//Leechspecial
									leechSpecial(c.playerIndex);
                        }
						if(c.curseActive[18]) { // Asshole_rule
                                        if(c.oldPlayerIndex > 0 && PlayerManager.getSingleton().getPlayers()[c.oldPlayerIndex] != null) {
                                                try {
                                                        final int pX = c.getX();
                                                        final int pY = c.getY();
                                                        final int nX = PlayerManager.getSingleton().getPlayers()[i].getX();
                                                        final int nY = PlayerManager.getSingleton().getPlayers()[i].getY();
                                                        final int offX = (pY - nY)* -1;
                                                        final int offY = (pX - nX)* -1;
														final Client otherplayer = (Client) PlayerManager.getSingleton().getPlayers()[c.oldPlayerIndex];
                                                        c.getCombat().createPlayersProjectile2(pX, pY, offX, offY, 50, 50, 2263, 9, 9, c.oldPlayerIndex - 1, 24);
														c.playerLevel[3] += (int)damage/5;
														if (c.playerLevel[3] >= (c.getLevelForXP(c.playerXP[3]) + c.getActionAssistant().getMaxHP())) {
														c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]) + c.getActionAssistant().getMaxHP();
														}
															if (otherplayer.playerRights < 2 || otherplayer.prayer == 1) {
														otherplayer.playerLevel[5] -= (int)damage/5;
														}
														if (otherplayer.playerLevel[5] <= 0) {
														otherplayer.playerLevel[5] = 0;
														}
														otherplayer.getActionAssistant().refreshSkill(5);
														c.getActionAssistant().refreshSkill(3);
                                                        EventManager.getSingleton().addEvent(c,"soulsplit1", new Event() {
                                                                public void execute(EventContainer b1) {
                                                                        if (otherplayer != null){
																		  Server.getStillGraphicsManager().stillGraphics(otherplayer,
				otherplayer.getAbsX(), otherplayer.getAbsY(), 0, 2264, 0);
                                                  //                              otherplayer.gfx0(2264); // 1738
																				}
																				b1.stop();
                                                                }
																@Override
																public void stop() {
																}
                                                        }, 500);
                                                        EventManager.getSingleton().addEvent(c,"soulsplit2", new Event() {
                                                                public void execute(EventContainer b2) {
                                                                        c.getCombat().createPlayersProjectile2(nX, nY, -offX, -offY, 50, 50, 2263, 9, 9, c.playerId - 1, 24);
                                                                                b2.stop();
                                                                }
																@Override
																public void stop() {
																}
                                                        }, 800);
                                                } catch (Exception e) {
                                                        e.printStackTrace();
                                                }
                                        }
                                }*/
												/* CURSES */
		if (c.curseActive[19]) { // Turmoil
							if (PlayerManager.getSingleton().getPlayers()[o.playerId] != null) {
                                c.getStr = Server.getPlayerManager().getPlayers()[o.playerId].playerLevel[2] * 10 / 100;
                                c.getDef = Server.getPlayerManager().getPlayers()[o.playerId].playerLevel[1] * 15 / 100;
                                c.getAtt = Server.getPlayerManager().getPlayers()[o.playerId].playerLevel[0] * 15 / 100;
							}
                        } if(c.curseActive[10]) { // LeechAtt
								leechAttack(o.playerId);
                        } 
						if(c.curseActive[11]) { // Leech Ranged
								leechRanged(o.playerId);
                        } 
						if(c.curseActive[12]) { // Leech Magic
								leechMagic(o.playerId);
                        }  if (c.curseActive[13]) {//leechDef
								leechDefence(o.playerId);
                        } if (c.curseActive[14]) {//LeechStr
									leechStrength(o.playerId);
                        }
						if (c.curseActive[16]) {//Leechspecial
									leechSpecial(o.playerId);
                        }
						if(c.curseActive[18]) { // Asshole_rule
                                        if(o.playerId > 0 && PlayerManager.getSingleton().getPlayers()[o.playerId] != null) {
                                                try {
                                                        final int pX = c.getX();
                                                        final int pY = c.getY();
                                                        final int nX = PlayerManager.getSingleton().getPlayers()[i].getX();
                                                        final int nY = PlayerManager.getSingleton().getPlayers()[i].getY();
                                                        final int offX = (pY - nY)* -1;
                                                        final int offY = (pX - nX)* -1;
														final Client otherplayer = (Client) PlayerManager.getSingleton().getPlayers()[o.playerId];
                                                        c.getCombat().createPlayersProjectile2(pX, pY, offX, offY, 50, 50, 2263, 9, 9, o.playerId - 1, 24);
														c.playerLevel[3] += (int)damage/5;
														if (c.playerLevel[3] >= (c.getLevelForXP(c.playerXP[3]) + c.getActionAssistant().getMaxHP())) {
														c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]) + c.getActionAssistant().getMaxHP();
														}
															if (otherplayer.playerRights < 2 || otherplayer.prayer == 1) {
														otherplayer.playerLevel[5] -= (int)damage/5;
														}
														if (otherplayer.playerLevel[5] <= 0) {
														otherplayer.playerLevel[5] = 0;
														}
														otherplayer.getActionAssistant().refreshSkill(5);
														c.getActionAssistant().refreshSkill(3);
														if (Misc.random(4) == 0) {
                                                        EventManager.getSingleton().addEvent(c,"soulsplit1", new Event() {
                                                                public void execute(EventContainer b1) {
                                                                        if (otherplayer != null){
																		  Server.getStillGraphicsManager().stillGraphics(otherplayer,
				otherplayer.getAbsX(), otherplayer.getAbsY(), 0, 2264, 0);
                                                  //                              otherplayer.gfx0(2264); // 1738
																				}
																				b1.stop();
                                                                }
																@Override
																public void stop() {
																}
                                                        }, 500);
                                                        EventManager.getSingleton().addEvent(c,"soulsplit2", new Event() {
                                                                public void execute(EventContainer b2) {
                                                                        c.getCombat().createPlayersProjectile2(nX, nY, -offX, -offY, 50, 50, 2263, 9, 9, c.playerId - 1, 24);
                                                                                b2.stop();
                                                                }
																@Override
																public void stop() {
																}
                                                        }, 800);
														}
                                                } catch (Exception e) {
                                                        e.printStackTrace();
                                                }
                                        }
                                }
				PlayerManager.getSingleton().getPlayers()[i].logoutDelay = System.currentTimeMillis();
				PlayerManager.getSingleton().getPlayers()[i].underAttackBy = c.playerId;
				PlayerManager.getSingleton().getPlayers()[i].killedbyid = c.playerId;
				PlayerManager.getSingleton().getPlayers()[i].singleCombatDelay = System.currentTimeMillis();
				PlayerManager.getSingleton().getPlayers()[i].singleCombatDelay2 = System.currentTimeMillis();
				o.inCombat = true;
				if(c.MAGIC_SPELLS[c.spellId][6] != 0) 
				{
					PlayerManager.getSingleton().getPlayers()[i].hitDiff = damage;
					PlayerManager.getSingleton().getPlayers()[i].playerLevel[3] -= damage;
					c.totalPlayerDamageDealt += damage;
					PlayerManager.getSingleton().getPlayers()[i].hitUpdateRequired = true;	
				}

				c.killedBy = PlayerManager.getSingleton().getPlayers()[i].playerId;	
				PlayerKiller.addHits(o, c, damage);
				Server.getDegradeManager().doDegrade(o,damage);
				if(damage > 30)
				{
					c.mageAchi++;
					if(c.mageAchi == 2)
					{
						c.getNRAchievements().checkCombat(c,11);
						c.mageAchi = 0;
					}
				} 
				else 
				{
					c.mageAchi  = 0;
				}
				o.getActionAssistant().refreshSkill(3);				
				PlayerManager.getSingleton().getPlayers()[i].updateRequired = true;
				c.usingMagic = false;
				c.castingMagic = false;
			}

		}	
		if(c.sounds == 1) 
		{
			c.getActionAssistant().frame174(816, 000, 000);	
		}		
		c.getActionAssistant().requestUpdates();
		c.oldPlayerIndex = 0;	
		c.projectileStage = 0;
		c.lastWeaponUsed = 0;
	}	
	
	public int pX;
	public int pY;
	public int nX;
	public int nY;
	public int offX;
	public int offY;

	
	
	public void attackNPC(int id) 
	{
		if(!c.npcClick) return;
		NPC npc = Server.getNpcManager().getNPC(id);
		targetNPC = npc;

		if (targetNPC != null){
			if (targetNPC.isDead()){
				resetAttack();
				return;
			}
		}
		if (npc.getAttacker() == null){
			if(!c.inMulti()){
				npc.setAttacker(c);
			}
		}
		npc.getAttackers().put(c, 0);		
		if (npc.isToBeRemoved){
			resetAttack();
			return;
		}
		if(npc.underAttack2 && npc.killerId != 0 && npc.killerId != c.playerId && !c.inMulti()){
			c.getActionAssistant().Send("Someone else is already fighting your opponent .");
			c.faceUpdate(c.npcIndex);
			c.getCombat().resetAttackVsNpc();
			return;
		}
		
		if(!npc.underAttack2 && c.inCombat && npc.npcId != c.underAttackByNPC && !c.inMulti()){
			c.getActionAssistant().Send("I'm already under attack .");
			c.faceUpdate(c.npcIndex);
			return;
		}			
		if (System.currentTimeMillis() - c.attackTimer < c.lastWeaponSpeed){
			//WeaponSpeed.getInstance().getWeaponSpeed(c.playerEquipment[PlayerConstants.PLAYER_WEAPON])){
			//		if (System.currentTimeMillis() - c.attackTimer < WeaponSpeed.getInstance().getWeaponSpeed(c.playerEquipment[PlayerConstants.PLAYER_WEAPON])){
			return;
		}
		c.isFighting = true;
		npc.underAttack = true;
		npc.underAttack2 = true;
		npc.setAttacker(c);
		if(npc.getDefinition().getType() != 1532){
			npc.facePlayer(c.playerId);
		}
	
		if(npc.getDefinition().getType() == 14696){
					c.isSkulled = true;
					c.skullTimer = Config.SKULL_TIMER;
					if(Config.SKULL_HEAD_ICON) 
					{
						c.skullIcon = 0;
					}
					c.getActionAssistant().requestUpdates();
				} 	
		c.faceUpdate(c.npcIndex);
		c.lastWeaponUsed = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
		c.lastWeaponSpeed = WeaponSpeed.getInstance().getWeaponSpeed(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]);
		//c.getActionAssistant().Send("speed: " +c.lastWeaponSpeed);
		/*	boolean usingBow = c.tempusingBow;
			boolean usingArrows = c.tempusingArrows;
			boolean usingOtherRangeWeapons = c.tempusingOtherRangeWeapons;*/
				boolean usingBow = false;
		boolean usingArrows = false;
		boolean usingOtherRangeWeapons = false;
		npc.lastHit = System.currentTimeMillis();
		c.projectileStage = 0;
		if(!c.usingMagic){
				//set bow and arrow here
			for (int bowId : c.BOWS) {
				if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == bowId) {
					//c.tempusingBow = true;
					 usingBow = true;
					for (int arrowId : c.ARROWS) {
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == arrowId) {
						//	c.tempusingArrows = true;
							usingArrows = true;
						}
					}
				}
			}				
					
			for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
				if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == otherRangeId) {
				//	c.tempusingOtherRangeWeapons = true;
					usingOtherRangeWeapons = true;
				}
			}
		}
		if(!usingArrows && usingBow && ((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] < 4212 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > 4223) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 20171)) 
		{
			c.getActionAssistant().sendMessage("You have run out of arrows!");
			return;
		} 
		
			if (!usingBow && !c.usingMagic){
			/***
			*
			* Diagonal Fix
			*
			***/
			if(c.getCombatFormulas().diagonal(c.getAbsX(),c.getAbsY(),npc.getAbsX(),npc.getAbsY())){
				//c.getActionAssistant().walkTo(1,0);
				c.getActionAssistant().playerWalk(npc.getX()+1, npc.getY(),"npc attack 3");
			}		
			c.autoCast = false;
			c.autoCastMarker = false;
			if (!Region.canAttack(c, npc)) {
				c.getActionAssistant().playerWalk(npc.getX(), npc.getY(),"npc attack 4");
					return;
			}
		} else if (usingBow){
			if (!Region.canAttack(c, npc)) {
				if(c.inWild() || npc.inWild()){
					c.getActionAssistant().playerWalk(npc.getX(), npc.getY(),"npc attack");
					return;
				}
			}
		} else if (c.usingMagic){
			if (!Region.canAttack(c, npc)) {
				if(c.inWild() || npc.inWild()){
					c.getActionAssistant().playerWalk(npc.getX(), npc.getY(),"npc2 attack");
					return;
				}
			}	
		}
		if(((usingHally() && !usingOtherRangeWeapons && !usingBow && !c.usingMagic) && !c.goodDistance(c.getX(), c.getY(), npc.getAbsX(), npc.getAbsY(), 4)) ||
			((usingOtherRangeWeapons && !usingBow && !c.usingMagic) && !c.goodDistance(c.getX(), c.getY(), npc.getAbsX(), npc.getAbsY(), 4)) || 
			((!usingOtherRangeWeapons && !usingHally() && !usingBow && !c.usingMagic) && !c.goodDistance(c.getX(), c.getY(), npc.getAbsX(), npc.getAbsY(), 1)) || 
			((usingBow || c.usingMagic) && (!c.goodDistance(c.getX(), c.getY(), npc.getAbsX(), npc.getAbsY(), 10)))) 
		{
			c.attackTimer += 1000;
			return;
		}			
		if(usingBow)
		{
			if (!(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > 4219 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] < 4224) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 4212&& c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 20171 ) 
			{
				c.getCombat().correctBowAndArrows();
				if(!c.bowlevel)
				{
					c.getActionAssistant().sendMessage("You can't use "+Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS]).getName().toLowerCase()+"s with a "+Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]).getName().toLowerCase()+".");
					c.stopMovement();
					resetAttack();
					c.npcIndex = 0;
					return;					
				}
			}
		}				
		if(usingBow || c.usingMagic || usingOtherRangeWeapons || (usingHally() &&c.goodDistance(c.getAbsX(), c.getAbsY(),npc.getAbsX(), npc.getAbsY(), 4))) 
		{
			c.stopMovement();
		}
			
		if(usingBow && checkBolts() && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 9185 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 18357) 
		{
			c.getActionAssistant().sendMessage("You can't use bolt's with this.");
			c.stopMovement();
			resetAttack();
			return;
		}
		if(usingBow && checkxBolts() && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 4734) 
		{
			c.getActionAssistant().sendMessage("You can't use bolt's with this.");
			c.stopMovement();
			resetAttack();
			return;
		}
		if (npc.definition.type == 1832 && c.playerLevel[18] < 7) 
		{
			c.getActionAssistant().Send("You must be 7 Slayer to slay Cave Bugs.");
			return;
		}		
		if (npc.definition.type == 1601 && c.playerLevel[18] < 10) 
		{
			c.getActionAssistant().Send("You must be 10 Slayer to slay Cave Crawlers.");
			return;
		}		
		if (npc.definition.type == 1612 && c.playerLevel[18] < 15) 
		{
			c.getActionAssistant().Send("You must be 15 Slayer to slay Banshee's.");
			return;
		}	
		if (npc.definition.type == 1831 && c.playerLevel[18] < 17) 
		{
			c.getActionAssistant().Send("You must be 17 Slayer to slay Cave Slime.");
			return;
		}		
		if (npc.definition.type == 1622 && c.playerLevel[18] < 20) 
		{
			c.getActionAssistant().Send("You must be 20 Slayer to slay Rock Slugs.");
			return;
		}	
		if (npc.definition.type == 1620 && c.playerLevel[18] < 25 ) 
		{
			c.getActionAssistant().Send("You must be 25 Slayer to slay Cockatice's.");
			return;
		}
		if (npc.definition.type == 1633 && c.playerLevel[18] < 30) 
		{
			c.getActionAssistant().Send("You must be 30 Slayer to slay Pyrefiends.");
			return;
		}				
		if (npc.definition.type == 1632 && c.playerLevel[18] < 55) 
		{
			c.getActionAssistant().Send("You must be 55 Slayer to slay Turoths.");
			return;
		}				
		if (npc.definition.type == 1609 && c.playerLevel[18] < 70) 
		{
			c.getActionAssistant().Send("You must be 70 Slayer to slay Kurasks.");
			return;
		}				
		if (npc.definition.type == 2783 && c.playerLevel[18] < 90) 
		{
			c.getActionAssistant().Send("You must be 90 Slayer to slay Dark beasts.");
			return;
		}
		if (npc.definition.type == 1615 && c.playerLevel[18] < 85) 
		{
			c.getActionAssistant().Send("You must be 85 Slayer to slay Abyssal demons.");
			return;
		}
		if (npc.definition.type == 3794 && c.playerLevel[18] < 90) 
		{
			c.getActionAssistant().Send("You must be 90 Slayer to slay Tormented Demons.");
			return;
		}
		if (npc.definition.type == 1613 && c.playerLevel[18] < 80) 
		{
			c.getActionAssistant().Send("You must be 80 Slayer to slay Nechryaels.");
			return;
		}
		if (npc.definition.type == 1610 && c.playerLevel[18] < 75) 
		{
			c.getActionAssistant().Send("You must be 75 Slayer to slay Gargoyles.");
			return;
		}
		if (npc.definition.type == 1616 && c.playerLevel[18] < 40) 
		{
			c.getActionAssistant().Send("You must be 40 Slayer to slay Balilisks.");
			return;
		}
		if (npc.definition.type == 1637 && c.playerLevel[18] < 52) 
		{
			c.getActionAssistant().Send("You must be 52 Slayer to slay Jellys.");
			return;
		}
		if (npc.definition.type == 1624 && c.playerLevel[18] < 65) 
		{
			c.getActionAssistant().Send("You must be 65 Slayer to slay Dust Devils.");
			return;
		}
		if (npc.definition.type == 1619 && c.playerLevel[18] < 50) 
		{
			c.getActionAssistant().Send("You must be 50 Slayer to slay Bloodvelds.");
			return;
		}
		if (npc.definition.type == 1643 && c.playerLevel[18] < 45) 
		{
			c.getActionAssistant().Send("You must be 45 Slayer to slay Infernal Mages.");
			return;
		}
		if (npc.definition.type == 1604 && c.playerLevel[18] < 60) 
		{
			c.getActionAssistant().Send("You must be 60 Slayer to slay Abby Specs.");
			return;
		}	
		if (npc.definition.type == 1832 && c.playerLevel[18] < 7) 
		{
			c.getActionAssistant().Send("You must be 7 Slayer to slay Cave Bugs.");
			return;
		}	
		if (npc.definition.type == 6221 && c.playerLevel[18] < 83) 
		{
			c.getActionAssistant().Send("You must be 83 Slayer to slay Spirit Mages");
			return;
		}		
		if (npc.definition.type == 6257 && c.playerLevel[18] < 83) 
		{
			c.getActionAssistant().Send("You must be 83 Slayer to slay Spirit Mages");
			return;
		}		
		if ((npc.definition.type >= 9463 && npc.definition.type <= 9467) && (c.playerLevel[18] < 99) ) 
		{
			c.getActionAssistant().Send("You must be 99 Slayer to slay this monster.");
			return;
		}
		if (npc.definition.type == 9463 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE] != 6570) {
			c.getActionAssistant().Send("You must be wearing a fire cape to hit through the ice!");
			return;
		}
		if ((npc.definition.type >= 8281 && npc.definition.type <= 8283) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] != 1580) {
			c.getActionAssistant().Send("You must be wearing ice gloves to attack this npc!");
			return;
			}
		pX = c.getAbsX();
		pY = c.getAbsY();
		nX = npc.getAbsX();
		nY = npc.getAbsY();
		offX = (pY - nY)* -1;
		offY = (pX - nX)* -1;				
		if(!c.usingMagic && !c.autoCast) 
		{
			c.lastWeaponUsed = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
			if(!c.specOn && !owedDouble) {
				if(doubleWait) {
					if(System.currentTimeMillis()-temptime < 2000)
						return;
					else
						doubleWait=false;
				}
				c.getActionAssistant().startAnimation(c.getCombatEmotes().getWepAnim());
				c.attackTimer = System.currentTimeMillis();
				if(c.sounds == 1) {
					c.getActionAssistant().frame174(Server.GetWepSound.GetWeaponSound(c.playerId), 050, 000);
				}
			}
			else if(owedDouble) {
				c.dHit=true;
				owedDouble=false;
				doubleWait=true;
				temptime = System.currentTimeMillis();
			}
			else 
			{
				if(doubleWait) {
					if(System.currentTimeMillis()-temptime < 2000)
						return;
					else {
						doubleWait=false;
					}
				}
				if(c.getSpecials().checkSpecial(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON])) 
				{
					c.getActionAssistant().startAnimation(c.getSpecials().getSpecialAnim(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]));
					c.gfx100(c.getSpecials().getSpecialGFX(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]));
					c.attackTimer = System.currentTimeMillis();
					
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 14484) {  // d claws
						owedDouble=true; 
						c.attackTimer = 1000; 
					}
					
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4151) 
					{
						npc.gfx100(341);
					}
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 21371) { npc.gfx100(2011); }
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 21372) { npc.gfx100(2011); }
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 21373) { npc.gfx100(2011); }
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 21374) { npc.gfx100(2011); }
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 21375) { npc.gfx100(2011); }
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 7822) 
					{
						if(c.sounds == 1) 
						{
							c.getActionAssistant().frame174(1081, 050, 000);
						}
						npc.gfx100(78);
					}
					if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 19784) 
						{
							npc.gfx100(1248);
						}
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1305 && c.sounds == 1) 
					{
						c.getActionAssistant().frame174(390, 050, 000);
					}						
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 5698 && c.sounds == 1) 
					{								
						c.getActionAssistant().frame174(385, 050, 000);
					}
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1215 && c.sounds == 1) 
					{
						c.getActionAssistant().frame174(385, 050, 000);
					}			
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1231 && c.sounds == 1) 
					{
						c.getActionAssistant().frame174(385, 050, 000);
					}	
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 5680 && c.sounds == 1) 
					{
						c.getActionAssistant().frame174(385, 050, 000);
					}						
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4153) 
					{
						c.attackTimer += 2000;
						c.hitDelay = 1;
						npc.gfx100(340);
						c.specOn = false;
						delayedHit(c.npcIndex);
						double factor = 1;
							if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 19669) {
								 factor = 0.9;
							}
							c.specialAmount -= (c.getSpecials().getSpecialDrain(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) *factor);
						return;
					}
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11730) 
					{
						npc.gfx100(1194);
					}
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13879) 
					{
						createPlayersProjectile(pY, pX, offY, offX, 50, 19, 1837, 43, 31, c.npcIndex + 1, 5);
						if(c.sounds == 1) 
						{
						}
					}					
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13883) 
					{
						createPlayersProjectile(pY, pX, offY, offX, 50, 19, 1839, 43, 31, c.npcIndex + 1, 5);
						if(c.sounds == 1) 
						{
						}
					}
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 861) 
					{
						c.getCombat().correctBowAndArrows();
						if(!c.bowlevel)
						{
							c.getCombat().resetAttack();
							c.getActionAssistant().sendMessage("@red@Hrm... that shouldn't have happened?");
							return;				
						}
						createPlayersProjectile(pY, pX, offY, offX, 50, 50, 249, 43, 31, c.npcIndex + 1, 25);
						createPlayersProjectile(pY, pX, offY, offX, 50, 75, 249, 43, 31, c.npcIndex + 1, 50);
						c.projectileStage = 8;
						c.castingMagic = false;
						c.doubleHit3 = 4;
						c.rangeSpec = true;
						c.inAir = true;
						if(c.sounds == 1) 
						{
							c.getActionAssistant().frame174(386, 020, 000);
						}							
					}	
					if((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4827 || (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] >= 15701 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] <= 15704))) 
					{
						createPlayersProjectile(pY, pX, offY, offX, 50, 50, 1099, 43, 31, c.npcIndex + 1, 25);
						createPlayersProjectile(pY, pX, offY, offX, 50, 75, 1099, 43, 31, c.npcIndex + 1, 50);
						c.projectileStage = 8;
						c.castingMagic = false;
						c.doubleHit3 = 4;
						c.rangeSpec = true;
					}									
					c.specOn = false;
					double factor = 1;
							if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 19669) {
								 factor = 0.9;
							}
							c.specialAmount -= (c.getSpecials().getSpecialDrain(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) *factor);
					if(c.getSpecials().doubleHit(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON])) 
					{
						c.dHit = true;
					}
					c.bonusDamage = c.getSpecials().getBonusDamage(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]);
					c.getSpecials().specialBar();
				} 
				else 
				{
					c.getActionAssistant().startAnimation(c.getCombatEmotes().getWepAnim());
					c.specOn = false;
					c.getActionAssistant().sendMessage("You don't have enough special power left.");
					c.attackTimer += 3000;
				}
			}
		} 
		else 
		{
			if(c.usingMagic && Config.MAGIC_LEVEL_REQUIRED) 
			{ // check magic level
				if(c.playerLevel[6] < c.MAGIC_SPELLS[c.spellId][1]) 
				{
					c.actionAssistant.sendMessage("You need to have a magic level of " +c.MAGIC_SPELLS[c.spellId][1]+" to cast this spell.");
					c.getCombat().resetAttack();
					return;
				}
			}
			c.stopMovement();
			if(c.autoCast && !c.newSpell) 
			{
				c.attackTimer += 3000;
				for(int a = 0; a < c.staffs.length; a++)
				{					
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == c.staffs[a] && c.AutoCast_SpellIndex >= 0)
					{
						if(System.currentTimeMillis() - c.lastAttack < AutoCast.coolDown[AutoCast.coolDownGroup[c.AutoCast_SpellIndex]])
						{
							return;
						}
					if(!c.getMagicData().hasComboRunes(c.AutoCast_SpellIndex)){
						if (AutoCast.runeGroup[c.AutoCast_SpellIndex] == 0) 
						{
							if (!c.actionAssistant.playerHasItem(AutoCast.RequiredRune1[c.AutoCast_SpellIndex], AutoCast.RequiredAmount1[c.AutoCast_SpellIndex]) || !c.actionAssistant.playerHasItem(AutoCast.RequiredRune2[c.AutoCast_SpellIndex], AutoCast.RequiredAmount2[c.AutoCast_SpellIndex])) 
							{    
								c.actionAssistant.sendMessage("You do not have the required runes for this spell!");
								resetAttack();
								return;
							}    
						}    
						if (AutoCast.runeGroup[c.AutoCast_SpellIndex] == 1) 
						{
							if (!c.actionAssistant.playerHasItem(AutoCast.RequiredRune1[c.AutoCast_SpellIndex], AutoCast.RequiredAmount1[c.AutoCast_SpellIndex]) || !c.actionAssistant.playerHasItem(AutoCast.RequiredRune2[c.AutoCast_SpellIndex], AutoCast.RequiredAmount2[c.AutoCast_SpellIndex]) ||
							((!c.actionAssistant.playerHasItem(AutoCast.RequiredRune3[c.AutoCast_SpellIndex], AutoCast.RequiredAmount3[c.AutoCast_SpellIndex])) && !(AutoCast.RequiredRune3[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)))
							{    
								c.actionAssistant.sendMessage("You do not have the required runes for this spell!");
								resetAttack();
								return;
							}    
						}    
						if(npc.definition.type == 3836)
						{ //kq edit
							c.getActionAssistant().Send("You cannot mage the kalphite queen!");
							damage = 0;
							return;
						}
						if(npc.definition.type == 14696){
							switch(npc.phase){
								case 0:
								case 1:
									c.getActionAssistant().Send("@red@You can not mage Ganodermic Beast in this phase!");
									damage = 0;
									c.getCombat().resetAttack();
								return;
							}
						}
						if(npc.definition.type == 8282 || npc.definition.type == 8283)
						{ //kq edit
							c.getActionAssistant().Send("You cannot mage the elemental!");
							damage = 0;
							return;
						}
						if (AutoCast.runeGroup[c.AutoCast_SpellIndex] == 2) 
						{
							if (!c.actionAssistant.playerHasItem(AutoCast.RequiredRune1[c.AutoCast_SpellIndex], AutoCast.RequiredAmount1[c.AutoCast_SpellIndex]) || !c.actionAssistant.playerHasItem(AutoCast.RequiredRune2[c.AutoCast_SpellIndex], AutoCast.RequiredAmount2[c.AutoCast_SpellIndex]) || 
							(!c.actionAssistant.playerHasItem(AutoCast.RequiredRune3[c.AutoCast_SpellIndex], AutoCast.RequiredAmount3[c.AutoCast_SpellIndex]) && !(AutoCast.RequiredRune3[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346))
							|| !c.actionAssistant.playerHasItem(AutoCast.RequiredRune4[c.AutoCast_SpellIndex], AutoCast.RequiredAmount4[c.AutoCast_SpellIndex])) 
							{    
								c.actionAssistant.sendMessage("You do not have the required runes for this spell!");
								resetAttack();
								return;
							}    
						}    
						}
						if(c.playerLevel[6] < AutoCast.RequiredLevel[c.AutoCast_SpellIndex])
						{
							c.actionAssistant.sendMessage("You need a magic level of " + AutoCast.RequiredLevel[c.AutoCast_SpellIndex]);
							resetAttack();
							return;
						}            
						c.lastAttack = System.currentTimeMillis();
						
						if(AutoCast.MagicType[c.AutoCast_SpellIndex] == 0) 
						{//Projectile
							c.getActionAssistant().startAnimation(AutoCast.animG[AutoCast.animationGroup[c.AutoCast_SpellIndex]]);
							c.gfx100(AutoCast.MageStartingGFX[c.AutoCast_SpellIndex]);
							//Projectile2(c.absY, c.absX, offsetY, offsetX, 50, 80, MageMovingGFX[c.AutoCast_SpellIndex], 43, 31, attacknpc+1);
							createPlayersProjectile(pX, pY, offX, offY, 50, 78, AutoCast.MageMovingGFX[c.AutoCast_SpellIndex], 43, 31, npc.definition.type + 1, 20);
							npc.gfx0(AutoCast.MageEndingGFX[c.AutoCast_SpellIndex]);//, npc.getAbsY(), npc.getAbsX(), 90, AutoCast.gfxHeight[c.AutoCast_SpellIndex]);
						}
						if(AutoCast.MagicType[c.AutoCast_SpellIndex] == 1) 
						{//No Projectile
							c.getActionAssistant().startAnimation(AutoCast.animG[AutoCast.animationGroup[c.AutoCast_SpellIndex]]);
							c.gfx100(AutoCast.MageStartingGFX[c.AutoCast_SpellIndex]);
							npc.gfx0(AutoCast.MageEndingGFX[c.AutoCast_SpellIndex]);//, npc.getAbsY(), npc.getAbsX(), 90, AutoCast.gfxHeight[c.AutoCast_SpellIndex]);
						}            
						if(AutoCast.MagicType[c.AutoCast_SpellIndex] == 2) 
						{//Ending Only
							if(AutoCast.isMultiCast[c.AutoCast_SpellIndex] == 1) 
							{
								c.getActionAssistant().startAnimation(AutoCast.animG[AutoCast.animationGroup[c.AutoCast_SpellIndex]]);
								npc.gfx0(AutoCast.MageEndingGFX[c.AutoCast_SpellIndex]);
							}
							if(AutoCast.isMultiCast[c.AutoCast_SpellIndex] == 0) 
							{
								c.getActionAssistant().startAnimation(AutoCast.animG[AutoCast.animationGroup[c.AutoCast_SpellIndex]]);
								npc.gfx0(AutoCast.MageEndingGFX[c.AutoCast_SpellIndex]);//, npc.getAbsY(), npc.getAbsX(), 90, AutoCast.gfxHeight[c.AutoCast_SpellIndex]);
							}
						}
						if(!c.getMagicData().hasComboRunes(c.AutoCast_SpellIndex)){						
						if (AutoCast.runeGroup[c.AutoCast_SpellIndex] == 0) 
						{
						if (!(AutoCast.RequiredRune1[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune1[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune1[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount1[c.AutoCast_SpellIndex]);
							}
							if (!(AutoCast.RequiredRune2[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune2[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune2[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount2[c.AutoCast_SpellIndex]);            
							}
						}    
						if (AutoCast.runeGroup[c.AutoCast_SpellIndex] == 1) 
						{
							if (!(AutoCast.RequiredRune1[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune1[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune1[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount1[c.AutoCast_SpellIndex]);
							}
							if (!(AutoCast.RequiredRune2[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune2[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune2[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount2[c.AutoCast_SpellIndex]);            
							}
							if (!(AutoCast.RequiredRune3[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune3[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune3[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount3[c.AutoCast_SpellIndex]);            
							}
						}    
						if (AutoCast.runeGroup[c.AutoCast_SpellIndex] == 2) 
						{
								if (!(AutoCast.RequiredRune1[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune1[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune1[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount1[c.AutoCast_SpellIndex]);
							}
							if (!(AutoCast.RequiredRune2[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune2[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune2[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount2[c.AutoCast_SpellIndex]);            
							}
							if (!(AutoCast.RequiredRune3[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune3[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune3[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount3[c.AutoCast_SpellIndex]);            
							}
								if (!(AutoCast.RequiredRune4[c.AutoCast_SpellIndex] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
							c.actionAssistant.deleteItem(AutoCast.RequiredRune4[c.AutoCast_SpellIndex], c.actionAssistant.getItemSlot(AutoCast.RequiredRune4[c.AutoCast_SpellIndex]), AutoCast.RequiredAmount4[c.AutoCast_SpellIndex]);            
							}
						}
						}
						if(c.AutoCast_SpellIndex == 31 || c.AutoCast_SpellIndex == 27 || 
							c.AutoCast_SpellIndex == 23 || c.AutoCast_SpellIndex == 19)
						{
							int freezeDelay = 60;//freeze 
							if(freezeDelay > 0 && npc.freezeTimer == 0) 
							{
								npc.freezeTimer = freezeDelay;
							}
						}
						int damage = Misc.random(AutoCast.MageHit[c.AutoCast_SpellIndex]);
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 15486) 
				{
								damage += damage*0.15;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 17017) 
				{
					damage += damage*0.30;
				}
				int bonus = 0;
				if (c.playerLevel[6] > c.getLevelForXP(c.playerXP[6])) {
				bonus += (c.getLevelForXP(c.playerXP[6]) - c.playerLevel[6]) *0.03;
				}
				damage += damage*bonus;
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18355) 
				{
					damage += damage*0.20;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == 18335) {
				damage += damage*0.15;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == 18334) {
				damage += damage*0.10;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == 18333) {
				damage += damage*0.05;
				}
						c.getActionAssistant().addSkillXP((damage*Config.RANGE_EXP_RATE), 6); 
						c.getActionAssistant().addSkillXP((damage*11), 3);
						c.getActionAssistant().refreshSkill(3);
						c.getActionAssistant().refreshSkill(6);
						npc.hitDiff = damage;
						npc.hit = damage;
						npc.hp -= damage;
						npc.underAttack = true;
						npc.underAttack2 = true;
						c.killingNpcIndex = npc.definition.type;
						npc.killerId = c.playerId;
						c.totalDamageDealt += damage;
						NPCKiller.addHits(npc, c, damage);
						if(damage > 30)
						{
							c.mageAchi++;
							if(c.mageAchi == 2)
							{
								c.getNRAchievements().checkCombat(c,11);
								c.mageAchi = 0;
							}
						} 
						else 
						{
							c.mageAchi  = 0;
						}
						npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));
						npc.setAnimUpdateRequired(true);
						npc.setUpdateRequired(true);
						npc.hitUpdateRequired = true;
						c.oldNpcIndex = id;
						return;
					}
				}				
			} 
			else 
			{
				if(c.newSpell) 
				{
					c.autoCast = true;
					c.attackTimer += 1500;
				}				
				if(c.reCast == 0)
				{
					c.getActionAssistant().startAnimation(c.MAGIC_SPELLS[c.spellId][2]);
					c.reCast = 5;
					c.attackTimer += 1500;
				} 
				else 
				{
					//resetAttack();
					return;
				}

			}
		}
		if(!usingBow && !c.usingMagic && !usingOtherRangeWeapons) 
		{ // melee hit delay
			if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4151) 
			{
				c.hitDelay = 1;
			} 
			else 
			{
				c.hitDelay = getHitDelay();
			}
			if ((c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 11283 && Misc.random(15) == 1 )|| (c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13655 && Misc.random(20) == 1)) 
			{
				c.getActionAssistant().startAnimation(6696);
				c.gfx100(1165);
				npc.delayedGfx(1167,36,100);
				npc.hitDiff = Misc.random(25);
				npc.hit = npc.hitDiff;
				npc.hp -= npc.hitDiff;
				c.totalDamageDealt += npc.hitDiff;
				npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));	
				npc.setAnimUpdateRequired(true);
				npc.setUpdateRequired(true);
				npc.hitUpdateRequired = true;	
			}				
			c.projectileStage = 0;
			c.oldNpcIndex = id;
		}
			
		if(usingBow && !usingOtherRangeWeapons && !c.usingMagic && !c.castingMagic) 
		{ // range hit delay //marker8
			c.castingMagic = false;
			if ((c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 11283 && Misc.random(15) == 1 )|| (c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13655 && Misc.random(20) == 1)) 
			{
				c.getActionAssistant().startAnimation(6696);
				c.gfx100(1165);
				npc.gfx100(1167);
				npc.delayedGfx(1167,36,100);
				npc.hitDiff = Misc.random(25);
				npc.hit = npc.hitDiff;
				npc.hp -= npc.hitDiff;
				c.totalDamageDealt += npc.hitDiff;
				npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));	
				npc.setAnimUpdateRequired(true);
				npc.setUpdateRequired(true);
				npc.hitUpdateRequired = true;
			}					
			if ((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 9185 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18357) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7868 && Misc.random(15) == 5) 
			{
				npc.gfx100(752);
				npc.hitDiff = 25 + Misc.random(5);
				npc.hit = npc.hitDiff;
				npc.hp -= npc.hitDiff;
				c.totalDamageDealt += npc.hitDiff;
				npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));	
				npc.setAnimUpdateRequired(true);
				npc.setUpdateRequired(true);
				npc.hitUpdateRequired = true;				
				return;
			}				
			if ((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 9185 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18357) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7870 && Misc.random(15) == 9) 
			{
				Client k = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
				npc.gfx100(758);
				npc.hitDiff = 20 + Misc.random(15);
				npc.hit = npc.hitDiff;
				npc.hp -= npc.hitDiff;
				c.totalDamageDealt += npc.hitDiff;
				npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));	
				npc.setAnimUpdateRequired(true);
				npc.setUpdateRequired(true);
				npc.hitUpdateRequired = true;	
				return;
			}					
			if ((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 9185 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18357) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7871 && Misc.random(25) == 8) 
			{
				Client k = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
				npc.gfx100(756);
				npc.hitDiff = 30 + Misc.random(30);
				npc.hit = npc.hitDiff;
				npc.hp -= npc.hitDiff;
				c.totalDamageDealt += npc.hitDiff;
				npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));	
				npc.setAnimUpdateRequired(true);
				npc.setUpdateRequired(true);
				npc.hitUpdateRequired = true;
				return;
			}
			if ((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 9185 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18357) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7872 && Misc.random(40) == 22) 
			{
				Client k = (Client) Server.getPlayerManager().getPlayers()[c.playerIndex];
				npc.gfx100(753);
				npc.hitDiff = 20 + Misc.random(20);
				npc.hit = npc.hitDiff;
				npc.hp -= npc.hitDiff;
				c.totalDamageDealt += npc.hitDiff;
				npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));	
				npc.setAnimUpdateRequired(true);
				npc.setUpdateRequired(true);
				npc.hitUpdateRequired = true;
					
				return;
			}					
			if((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] >= 4212 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] <= 4223) ||c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 20171 ) 
			{
				c.rangeItemUsed = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
				//c.crystalBowArrowCount++;
			} 
			else 
			{
				c.rangeItemUsed = c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS];
				if(c.getCombatFormulas().ava() && Misc.random(4) == 1) 
				{
				} 
				else 
				{
					c.getActionAssistant().deleteArrow();
					c.inAir = true;
				}	
			}	
			c.lastWeaponUsed = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
			c.gfx100(getRangeStartGFX());	
			c.hitDelay = getHitDelay();
			c.projectileStage = 1;
			c.oldNpcIndex = id;
		}
		if(usingOtherRangeWeapons) 
		{	// knives, darts, etc hit delay		
			c.rangeItemUsed = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
			if(c.getCombatFormulas().ava() && Misc.random(4) == 1) 
			{
			} 
			else 
			{
				c.getActionAssistant().deleteEquipment();
			}				
			c.gfx100(getRangeStartGFX());
			c.hitDelay = getHitDelay();
			c.projectileStage = 1;
			c.oldNpcIndex = id;
		}
		if(c.usingMagic) 
		{	
			if(c.usingMagic && Config.MAGIC_LEVEL_REQUIRED) 
			{ // check magic level
				if(c.playerLevel[6] < c.MAGIC_SPELLS[c.spellId][1]) 
				{
					c.actionAssistant.sendMessage("You need to have a magic level of " +c.MAGIC_SPELLS[c.spellId][1]+" to cast this spell.");
					c.getCombat().resetAttack();
					return;
				}
			}					
			c.castingMagic = true;
			c.projectileStage = 2;
			if(c.MAGIC_SPELLS[c.spellId][3] > 0) 
			{
				if(c.getMagicData().getStartGfxHeight() == 100) 
				{
					c.gfx100(c.MAGIC_SPELLS[c.spellId][3]);
				} 
				else 
				{
					c.gfx0(c.MAGIC_SPELLS[c.spellId][3]);
				}
			}
			if(c.MAGIC_SPELLS[c.spellId][4] > 0) 
			{
				c.getCombat().createPlayersProjectile(pX, pY, offX, offY, 50, 78, c.MAGIC_SPELLS[c.spellId][4], c.getMagicData().getStartHeight(), c.getMagicData().getEndHeight(), id + 1, c.getMagicData().getStartDelay());
			}
			c.hitDelay = c.getMagicData().getSpellHitDelay();
			c.oldNpcIndex = id;
			c.npcIndex = 0;
			if(c.sounds == 1 && c.mage == 0) 
			{
				c.getActionAssistant().frame174(c.MAGIC_SPELLS[c.spellId][16], 000, 025);	
			}	
			if(c.sounds == 1 && c.mage == 1) 
			{
				c.getActionAssistant().frame174(c.MAGIC_SPELLS[c.spellId][16], 000, 000);	
			}						
		}
		//}
	}
	public void degradepvp(int damage) 
	{

		/*if (c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 9000) 
		{
			c.vlsdmg -= damage;
			if (c.vlsdmg <= 0) 
			{
				c.vlsdmg = 0;
				DegradeCheck degradecheck = new DegradeCheck(c);
			}

		}
		else if (c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 13904) 
		{
			c.statdmg -= damage;
			if (c.statdmg <= 0) 
			{
				c.statdmg = 0;
				DegradeCheck degradecheck = new DegradeCheck(c);
			}
		}
		else if (c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 13907) 
		{
			c.vsdmg -= damage;
			if (c.vsdmg <= 0) 
			{
				c.vsdmg = 0;
				DegradeCheck degradecheck = new DegradeCheck(c);
			}
		}*/
	}

	public void applyNpcMeleeDamage(int i, int damageMask){
		NPC npc = targetNPC;
		double temp = c.bonusDamage;
		int damage = Misc.random((int)(calculateMeleeMaxHit()*c.bonusDamage));
		if (c.lastWeaponUsed == c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) {
			if (temp < 1)
				temp=1;
			damage = Misc.random((int)(calculateMeleeMaxHit()*temp));
		} else {
			damage = 0;
		}
		if(npc.definition.type == 8281 || npc.definition.type == 8283){ 
			c.getActionAssistant().Send("You cannot melee the elemental!");
			damage = 0;
						
		}

		if(npc.definition.type == 3836 && !c.getCombatFormulas().fullVeracEquipped())
		{
			c.getActionAssistant().Send("The kalphite queen seems uneffected by your attacks..");
			damage = 0;
		}
		if (npc.definition.type == 3811) 
		{
			c.getActionAssistant().Send("Kree'arra seems uneffected by your attacks..");
			damage = 0;	
		}
		if (npc.definition.type == 763) 
		{
			c.getActionAssistant().Send("Flight Kilisa seems uneffected by your attacks..");
			damage = 0;	
		}
		if (npc.definition.type == 764) 
		{
			c.getActionAssistant().Send("Flockleader Geerin seems uneffected by your attacks..");
			damage = 0;	
		}
		if (npc.definition.type == 765) 
		{
			c.getActionAssistant().Send("Wingman Skree seems uneffected by your attacks..");
			damage = 0;	
		}
		if (npc.hp - damage < 0) 
		{ 
			damage = npc.hp;
		}
		if(Misc.random(npc.defence) > Misc.random(10 + calculateMeleeAttack()))
		{
			damage = 0;
		}  

		if (c.getCombatFormulas().fullAkrisaeEquipped() && Misc.random(6)==3) 
		{
			int nX = npc.getAbsX();
			int nY = npc.getAbsY();
			int heal = damage;
			npc.gfx100(398);
			if(c.playerLevel[5] + heal >= c.getActionAssistant().getLevelForXP(c.playerXP[5])) 
			{
				c.playerLevel[5] = c.getActionAssistant().getLevelForXP(c.playerXP[5]);
			} 
			else 
			{
				c.playerLevel[5] += heal;
			}
			c.getActionAssistant().refreshSkill(5);
			c.updateRequired = true;
		}
		
		if (c.getCombatFormulas().fullGuthanEquipped() && Misc.random(6)==3) 
		{
			int nX = npc.getAbsX();
			int nY = npc.getAbsY();
			int heal = damage;
			npc.gfx100(398);
			if(c.playerLevel[3] + heal >= c.getActionAssistant().getLevelForXP(c.playerXP[3])) 
			{
				c.playerLevel[3] = c.getActionAssistant().getLevelForXP(c.playerXP[3]);
			} 
			else 
			{
				c.playerLevel[3] += heal;
			}
			c.getActionAssistant().refreshSkill(3);
			c.updateRequired = true;
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 13263) 
		{
			damage += damage*0.20;
		}
				
		if (npc.definition.type == 1653 && c.slayerTask == 1653) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1676 && c.slayerTask == 1677) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1677 && c.slayerTask == 1677) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 122 && c.slayerTask == 122) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 122 && c.slayerTask == 122) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 123 && c.slayerTask == 122) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1265 && c.slayerTask == 1265) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1832 && c.slayerTask == 1832) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1601 && c.slayerTask == 1601) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1612 && c.slayerTask == 1612) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1831 && c.slayerTask == 1831) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1622 && c.slayerTask == 1622) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1620 && c.slayerTask == 1620) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 112 && c.slayerTask == 112) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 117 && c.slayerTask == 117) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1633 && c.slayerTask == 1633) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 941 && c.slayerTask == 941) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 55 && c.slayerTask == 55) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1632 && c.slayerTask == 1632) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1609 && c.slayerTask == 1609) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 84 && c.slayerTask == 84) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 677 && c.slayerTask == 677) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 83 && c.slayerTask == 83) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1616 && c.slayerTask == 1616) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1637 && c.slayerTask == 1637) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1618 && c.slayerTask == 1619) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1619 && c.slayerTask == 1619) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1643 && c.slayerTask == 1643) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1604 && c.slayerTask == 1604) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1624 && c.slayerTask == 1624) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1610 && c.slayerTask == 1610) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1613 && c.slayerTask == 1613) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1615 && c.slayerTask == 1615) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 2783 && c.slayerTask == 2783) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		degradepvp(damage);
		NPCKiller.addHits(npc, c, damage);
		if(damage > 50 && damage < 59)
		{
			c.getNRAchievements().checkCombat(c,0);
		} 
		else if (damage >= 60 && damage < 69) 
		{
			c.getNRAchievements().checkCombat(c,1);
		} 
		else if (damage >= 70 && damage < 79) 
		{
			c.getNRAchievements().checkCombat(c,2);
		} 
		else if (damage >= 80) 
		{
			c.getNRAchievements().checkCombat(c,3);
		}
				
		if(c.playerXP[2] > 999999999 && damage > 80)
		{
			c.getNRAchievements().checkCombat(c,7);
		} 
		if(c.playerXP[0] > 999999999 && damage > 40)
		{
			if(damage > 40)
			{
				c.attackhit++;
				if(c.attackhit == 3)
				{
					c.getNRAchievements().checkCombat(c,8);
					c.attackhit = 0;
				}
			} 
			else 
			{
				c.attackhit  = 0;
			}
		}
		if (npc.definition.type == 6221 && c.playerLevel[18] < 83) 
		{
			c.getActionAssistant().Send("You must be 83 Slayer to slay Spirit Mages");
			return;
		}		
		if (npc.definition.type == 6257 && c.playerLevel[18] < 83) 
		{
			c.getActionAssistant().Send("You must be 83 Slayer to slay Spirit Mages");
			return;
		}			
		if (npc.definition.type == 1832 && c.playerLevel[18] < 7) 
		{
			c.getActionAssistant().Send("You must be 7 Slayer to slay Cave Bugs.");
			return;
		}		
		if (npc.definition.type == 1601 && c.playerLevel[18] < 10) 
		{
			c.getActionAssistant().Send("You must be 10 Slayer to slay Cave Crawlers.");
			return;
		}		
		if (npc.definition.type == 1612 && c.playerLevel[18] < 15) 
		{
			c.getActionAssistant().Send("You must be 15 Slayer to slay Banshee's.");
			return;
		}	
		if (npc.definition.type == 1831 && c.playerLevel[18] < 17) 
		{
			c.getActionAssistant().Send("You must be 17 Slayer to slay Cave Slime.");
			return;
		}		
		if (npc.definition.type == 1622 && c.playerLevel[18] < 20) 
		{
			c.getActionAssistant().Send("You must be 20 Slayer to slay Rock Slugs.");
			return;
		}	
		if (npc.definition.type == 1620 && c.playerLevel[18] < 25 ) 
		{
			c.getActionAssistant().Send("You must be 25 Slayer to slay Cockatice's.");
			return;
		}
		if (npc.definition.type == 1633 && c.playerLevel[18] < 30) 
		{
			c.getActionAssistant().Send("You must be 30 Slayer to slay Pyrefiends.");
			return;
		}				
		if (npc.definition.type == 1632 && c.playerLevel[18] < 55) 
		{
			c.getActionAssistant().Send("You must be 55 Slayer to slay Turoths.");
			return;
		}				
		if (npc.definition.type == 1609 && c.playerLevel[18] < 70) 
		{
			c.getActionAssistant().Send("You must be 70 Slayer to slay Kurasks.");
			return;
		}				
		if (npc.definition.type == 2783 && c.playerLevel[18] < 90) 
		{
			c.getActionAssistant().Send("You must be 90 Slayer to slay Dark beasts.");
			return;
		}
		if (npc.definition.type == 1615 && c.playerLevel[18] < 85) 
		{
			c.getActionAssistant().Send("You must be 85 Slayer to slay Abyssal demons.");
			return;
		}
		if (npc.definition.type == 3794 && c.playerLevel[18] < 90) 
		{
			c.getActionAssistant().Send("You must be 90 Slayer to slay Tormented Demons.");
			return;
		}
		if (npc.definition.type == 1613 && c.playerLevel[18] < 80) 
		{
			c.getActionAssistant().Send("You must be 80 Slayer to slay Nechryaels.");
			return;
		}
		if (npc.definition.type == 1610 && c.playerLevel[18] < 75) 
		{
			c.getActionAssistant().Send("You must be 75 Slayer to slay Gargoyles.");
			return;
		}
		if (npc.definition.type == 1616 && c.playerLevel[18] < 40) 
		{
			c.getActionAssistant().Send("You must be 40 Slayer to slay Balilisks.");
			return;
		}
		if (npc.definition.type == 1637 && c.playerLevel[18] < 52) 
		{
			c.getActionAssistant().Send("You must be 52 Slayer to slay Jellys.");
			return;
		}
		if (npc.definition.type == 1624 && c.playerLevel[18] < 65) 
		{
			c.getActionAssistant().Send("You must be 65 Slayer to slay Dust Devils.");
			return;
		}
		if (npc.definition.type == 1619 && c.playerLevel[18] < 50) 
		{
			c.getActionAssistant().Send("You must be 50 Slayer to slay Bloodvelds.");
			return;
		}
		if (npc.definition.type == 1643 && c.playerLevel[18] < 45) 
		{
			c.getActionAssistant().Send("You must be 45 Slayer to slay Infernal Mages.");
			return;
		}
		if (npc.definition.type == 1604 && c.playerLevel[18] < 60) 
		{
			c.getActionAssistant().Send("You must be 60 Slayer to slay Abby Specs.");
			return;
		}
		if ((npc.definition.type == 934 || npc.definition.type == 221) && (c.playerLevel[18] < 95) ) 
		{
			c.getActionAssistant().Send("You must be 99 Slayer to slay this monster.");
			return;
		}
		if (npc.definition.type == 9463 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE] != 6570) {
			c.getActionAssistant().Send("You must be wearing a fire cape to hit through the ice!");
			return;
		}
		if ((npc.definition.type >= 8281 && npc.definition.type <= 8283) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] != 1580) {
			c.getActionAssistant().Send("You must be wearing ice gloves to attack this npc!");
			return;
			}
		/*
	        if (c.playerEquipment[PlayerConstants.PLAYER_RING] == 2550) {
                int recoilDam = (int) ((double) (damage) * .10);
				if(recoilDam > 0){
					npc.hitDiff = recoilDam;
					npc.hit = damage;
					npc.hp -= damage;
					c.totalDamageDealt += damage;
					npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));
					npc.setAnimUpdateRequired(true);
					npc.setUpdateRequired(true);
					npc.hitUpdateRequired = true;	
					c.recoil = c.recoil + 1;
					if(c.recoil >= 50) {
						c.getActionAssistant().deleteEquipment(c.playerEquipment[PlayerConstants.PLAYER_RING],PlayerConstants.PLAYER_RING);
						c.getActionAssistant().sendMessage("Your ring of recoil has shattered!");
						c.recoil = 0;
					}
				}
            }	*/	
			
		if(c.fightType == 3) 
		{
			c.getActionAssistant().addSkillXP((damage*Config.MELEE_EXP_RATE), 0); 
			c.getActionAssistant().addSkillXP((damage*Config.MELEE_EXP_RATE), 1);
			c.getActionAssistant().addSkillXP((damage*Config.MELEE_EXP_RATE), 2); 				
			c.getActionAssistant().addSkillXP((damage*Config.HP_RATE), 3);
			c.getActionAssistant().refreshSkill(0);
			c.getActionAssistant().refreshSkill(1);
			c.getActionAssistant().refreshSkill(2);
			c.getActionAssistant().refreshSkill(3);
		} 
		else 
		{
			c.getActionAssistant().addSkillXP((damage*Config.MELEE_EXP_RATE), c.fightType); 
			c.getActionAssistant().addSkillXP((damage*Config.HP_RATE), 3);
			c.getActionAssistant().refreshSkill(c.fightType);
			c.getActionAssistant().refreshSkill(3);
		}
		npc.underAttack = true;
		npc.underAttack2 = true;
		npc.killerId = c.playerId;
		c.killingNpcIndex = npc.definition.type;
		c.singleCombatDelay = System.currentTimeMillis();
		c.singleCombatDelay2 = System.currentTimeMillis();
		
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 14484 && c.dHit) {
			if (clawStrikes==1)
				damage = firstClawStrikeDamage / 2;
			else if (clawStrikes==2) {
				if (firstClawStrikeDamage > 4)
					damage = (firstClawStrikeDamage / 4) - 1 + Misc.random(2);
				else
					damage = 0;
			}
			else if (clawStrikes==3) {
				if (firstClawStrikeDamage > 4)
					damage = (firstClawStrikeDamage / 4) - 1 + Misc.random(2);
				else
					damage = 0;
			}
			else { //must be 0 or 4
				clawStrikes=0;
				firstClawStrikeDamage = damage;
			}
			clawStrikes++;
		}
		
		switch(damageMask) 
		{
			case 1:
				npc.hitDiff = damage;
				npc.hit = damage;
				npc.hp -= damage;
				c.totalDamageDealt += damage;
				npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));
				npc.setAnimUpdateRequired(true);
				npc.setUpdateRequired(true);
				npc.hitUpdateRequired = true;	
				break;
		
			case 2:
				npc.hitDiff2 = damage;
				npc.hit2 = damage;
				npc.hp -= damage;
				c.totalDamageDealt += damage;
				npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));
				npc.setAnimUpdateRequired(true);
				npc.setUpdateRequired(true);
				npc.hitUpdateRequired2 = true;
				c.dHit = false;
				break;
		}
	}
	public void applyNpcRangeDamage(int i, int damageMask) 
	{
		NPC npc = targetNPC;
		int damage = 0;
		if (c.lastWeaponUsed == c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) 
		{
			damage = Misc.random((int)(calculateRangeMaxHit()));
		} 
		else 
		{
			damage = 0;
		}
		if (Misc.random(10+calculateRangeAttack()) < Misc.random(npc.defence)) 
		{
			damage = 0;
		}
				if(npc.definition.type == 8281 || npc.definition.type == 8282)
						{ //kq edit
							c.getActionAssistant().Send("You cannot range the elemental!");
							damage = 0;
						
						}
		if(npc.definition.type == 14696){
			switch(npc.phase){
				case 0:
				case 1:
					c.getActionAssistant().Send("@red@You can not range Ganodermic Beast in this phase!");
					damage = 0;
					c.getCombat().resetAttack();
					return;
			}
		
		}
		if(npc.definition.type == 3836 && !c.getCombatFormulas().fullVeracEquipped())
		{
			c.getActionAssistant().Send("The kalphite queen seems uneffected by your attacks..");
			damage = 0;
		}				
		if (npc.hp - damage < 0) 
		{ 
			damage = npc.hp;
		}
		if ((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 9185 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18357) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] == 7869 && Misc.random(15) == 7) 
		{
			npc.gfx100(754);
			//int recoilDam = (int) ((double) (npc.hp) * .10);
			npc.hitDiff = npc.hp / 8;
			npc.hit = npc.hitDiff;
			//c.playerLevel[3] -= c.playerLevel[3]/10;
			c.hitUpdateRequired = true;
			c.updateRequired = true;
			damage += npc.hitDiff;
			npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));	
			npc.setAnimUpdateRequired(true);
			npc.setUpdateRequired(true);
			npc.hitUpdateRequired = true;	
			c.getActionAssistant().Send("spec: " +damage);
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 13263) 
		{
			damage += damage*0.20;
		}
		if (npc.definition.type == 1653 && c.slayerTask == 1653) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1676 && c.slayerTask == 1677) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1677 && c.slayerTask == 1677) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 122 && c.slayerTask == 122) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 122 && c.slayerTask == 122) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 123 && c.slayerTask == 122) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1265 && c.slayerTask == 1265) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1832 && c.slayerTask == 1832) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1601 && c.slayerTask == 1601) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1612 && c.slayerTask == 1612) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1831 && c.slayerTask == 1831) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1622 && c.slayerTask == 1622) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1620 && c.slayerTask == 1620) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 112 && c.slayerTask == 112) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 117 && c.slayerTask == 117) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1633 && c.slayerTask == 1633) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 941 && c.slayerTask == 941) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 55 && c.slayerTask == 55) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1632 && c.slayerTask == 1632) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1609 && c.slayerTask == 1609) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 84 && c.slayerTask == 84) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 677 && c.slayerTask == 677) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 83 && c.slayerTask == 83) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1616 && c.slayerTask == 1616) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1637 && c.slayerTask == 1637) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//	
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1618 && c.slayerTask == 1619) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//	
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1619 && c.slayerTask == 1619) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//	
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1643 && c.slayerTask == 1643) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//	
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1604 && c.slayerTask == 1604) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1624 && c.slayerTask == 1624) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				//c.getActionAssistant().sendMessage("OLD " + damage);
				damage += damage*0.15;
				//	c.getActionAssistant().sendMessage("NEW " + damage);
			}
		}
		else if (npc.definition.type == 1610 && c.slayerTask == 1610) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//	
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1613 && c.slayerTask == 1613) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 1615 && c.slayerTask == 1615) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				//
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}
		else if (npc.definition.type == 2783 && c.slayerTask == 2783) 
		{
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 7003) 
			{
				damage += damage*0.05;
				
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6335) 
			{
				damage += damage*0.10;
			}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 6131) 
			{
				damage += damage*0.15;
			}
		}

		NPCKiller.addHits(npc, c, damage);
		if(damage > 30)
		{
			c.rangeAchi++;
			if(c.rangeAchi == 2)
			{
				c.getNRAchievements().checkCombat(c,10);
				c.rangeAchi = 0;
			}
		} 
		else 
		{
			c.rangeAchi  = 0;
		}
		if (npc.definition.type == 6221 && c.playerLevel[18] < 83) 
		{
			c.getActionAssistant().Send("You must be 83 Slayer to slay Spirit Mages");
			return;
		}		
		if (npc.definition.type == 6257 && c.playerLevel[18] < 83) 
		{
			c.getActionAssistant().Send("You must be 83 Slayer to slay Spirit Mages");
			return;
		}			
		if (npc.definition.type == 1832 && c.playerLevel[18] < 7) 
		{
			c.getActionAssistant().Send("You must be 7 Slayer to slay Cave Bugs.");
			return;
		}		
		if (npc.definition.type == 1601 && c.playerLevel[18] < 10) 
		{
			c.getActionAssistant().Send("You must be 10 Slayer to slay Cave Crawlers.");
			return;
		}		
		if (npc.definition.type == 1612 && c.playerLevel[18] < 15) 
		{
			c.getActionAssistant().Send("You must be 15 Slayer to slay Banshee's.");
			return;
		}	
		if (npc.definition.type == 1831 && c.playerLevel[18] < 17) 
		{
			c.getActionAssistant().Send("You must be 17 Slayer to slay Cave Slime.");
			return;
		}		
		if (npc.definition.type == 1622 && c.playerLevel[18] < 20) 
		{
			c.getActionAssistant().Send("You must be 20 Slayer to slay Rock Slugs.");
			return;
		}	
		if (npc.definition.type == 1620 && c.playerLevel[18] < 25 ) 
		{
			c.getActionAssistant().Send("You must be 25 Slayer to slay Cockatice's.");
			return;
		}
		if (npc.definition.type == 1633 && c.playerLevel[18] < 30) 
		{
			c.getActionAssistant().Send("You must be 30 Slayer to slay Pyrefiends.");
			return;
		}				
		if (npc.definition.type == 1632 && c.playerLevel[18] < 55) 
		{
			c.getActionAssistant().Send("You must be 55 Slayer to slay Turoths.");
			return;
		}				
		if (npc.definition.type == 1609 && c.playerLevel[18] < 70) 
		{
			c.getActionAssistant().Send("You must be 70 Slayer to slay Kurasks.");
			return;
		}				
		if (npc.definition.type == 2783 && c.playerLevel[18] < 90) 
		{
			c.getActionAssistant().Send("You must be 90 Slayer to slay Dark beasts.");
			return;
		}
		if (npc.definition.type == 1615 && c.playerLevel[18] < 85) 
		{
			c.getActionAssistant().Send("You must be 85 Slayer to slay Abyssal demons.");
			return;
		}
		if (npc.definition.type == 3794 && c.playerLevel[18] < 90) 
		{
			c.getActionAssistant().Send("You must be 90 Slayer to slay Tormented Demons.");
			return;
		}
		if (npc.definition.type == 1613 && c.playerLevel[18] < 80) 
		{
			c.getActionAssistant().Send("You must be 80 Slayer to slay Nechryaels.");
			return;
		}
		if (npc.definition.type == 1610 && c.playerLevel[18] < 75) 
		{
			c.getActionAssistant().Send("You must be 75 Slayer to slay Gargoyles.");
			return;
		}
		if (npc.definition.type == 1616 && c.playerLevel[18] < 40) 
		{
			c.getActionAssistant().Send("You must be 40 Slayer to slay Balilisks.");
			return;
		}
		if (npc.definition.type == 1637 && c.playerLevel[18] < 52) 
		{
			c.getActionAssistant().Send("You must be 52 Slayer to slay Jellys.");
			return;
		}
		if (npc.definition.type == 1624 && c.playerLevel[18] < 65) 
		{
			c.getActionAssistant().Send("You must be 65 Slayer to slay Dust Devils.");
			return;
		}
		if (npc.definition.type == 1619 && c.playerLevel[18] < 50) 
		{
			c.getActionAssistant().Send("You must be 50 Slayer to slay Bloodvelds.");
			return;
		}
		if (npc.definition.type == 1643 && c.playerLevel[18] < 45) 
		{
			c.getActionAssistant().Send("You must be 45 Slayer to slay Infernal Mages.");
			return;
		}
		if (npc.definition.type == 1604 && c.playerLevel[18] < 60) 
		{
			c.getActionAssistant().Send("You must be 60 Slayer to slay Abby Specs.");
			return;
		}					
		if ((npc.definition.type == 934 || npc.definition.type == 221) && (c.playerLevel[18] < 95) ) 
		{
			c.getActionAssistant().Send("You must be 95 Slayer to slay this monster.");
			return;
		}
			if ((npc.definition.type >= 9463 && npc.definition.type <= 9467) && (c.playerLevel[18] < 99) ) 
		{
			c.getActionAssistant().Send("You must be 99 Slayer to slay this monster.");
			return;
		}
		if (npc.definition.type == 9463 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE] != 6570) {
			c.getActionAssistant().Send("You must be wearing a fire cape to hit through the ice!");
			return;
		}
		if ((npc.definition.type >= 8281 && npc.definition.type <= 8283) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] != 1580) {
			c.getActionAssistant().Send("You must be wearing ice gloves to attack this npc!");
			return;
			}
		c.getActionAssistant().addSkillXP((damage*Config.RANGE_EXP_RATE), 4); 
		c.getActionAssistant().addSkillXP((damage*Config.HP_RATE), 3);
		c.getActionAssistant().refreshSkill(3);
		c.getActionAssistant().refreshSkill(4);
				
		boolean dropArrows = true;
						
		for(int noArrowId : c.NO_ARROW_DROP) 
		{
			if(c.lastWeaponUsed == noArrowId) 
			{
				dropArrows = false;
				c.inAir = true;
				break;
			}
		}
		if(dropArrows) 
		{
		try {
			c.getCombat().dropItem(c.rangeItemUsed, 1,Server.getNpcManager().getNPC(c.oldNpcIndex).getX(), Server.getNpcManager().getNPC(c.oldNpcIndex).getY(),c.heightLevel);
			c.inAir = true;
		}
		catch (Exception e) {}
		}
		npc.underAttack = true;
		npc.underAttack2 = true;
		npc.killerId = c.playerId;
		c.killingNpcIndex = npc.definition.type;
		c.singleCombatDelay = System.currentTimeMillis();
		c.singleCombatDelay2 = System.currentTimeMillis();
		switch(damageMask) 
		{
			case 1:
				npc.hitDiff = damage;
				npc.hit = damage;
				npc.hp -= damage;
				c.totalDamageDealt += damage;
				npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));
				npc.setAnimUpdateRequired(true);
				npc.setUpdateRequired(true);
				npc.hitUpdateRequired = true;	
				break;
		
			case 2:
				npc.hitDiff2 = damage;
				npc.hit2 = damage;
				npc.hp -= damage;
				c.totalDamageDealt += damage;
				npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));
				npc.setAnimUpdateRequired(true);
				npc.setUpdateRequired(true);
				npc.hitUpdateRequired2 = true;
				c.dHit = false;
				break;
			
		}
		c.inAir = false;
		if(c.didMove && !c.dHit)
		{
			resetAttack();
			c.didMove = false;
		}
	}
	
	public void delayedHit(int id) 
	{
		NPC npc = targetNPC;
		if (targetNPC != null) 
		{
			if (targetNPC.isDead()) 
			{
				resetAttack();
				return;
			}
			npc.faceTo(c.playerId);

			if(c.projectileStage == 0) 
			{ // melee hit damage
				applyNpcMeleeDamage(id, 1);
				if(c.dHit) 
				{
					applyNpcMeleeDamage(id, 2);
				}
			}
			if(!c.castingMagic && c.projectileStage > 0) 
			{ // range hit damage
				applyNpcRangeDamage(id, 1);
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]== 4827 || (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] >= 15701 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]<= 15704)) {
					c.dHit = true;
				}
				if(c.dHit) 
				{
					applyNpcRangeDamage(id, 2);
				}

			} 
			else if(c.projectileStage > 0) 
			{ // magic hit damage
			
				if(c.autoCast) 
				{ //speed hit punk fix
					c.actionAssistant.sendMessage("You can not do this with autocast enabled.");
					//return;
				}
				
				int damage = Misc.random(c.MAGIC_SPELLS[c.spellId][6]);
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 15486) 
				{
								damage += damage*0.15;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 17017) 
				{
					damage += damage*0.30;
				}
				int bonus = 0;
				if (c.playerLevel[6] > c.getLevelForXP(c.playerXP[6])) {
				bonus += (c.getLevelForXP(c.playerXP[6]) - c.playerLevel[6]) *0.03;
				}
				damage += damage*bonus;
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18355) 
				{
					damage += damage*0.20;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == 18335) {
				damage += damage*0.15;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == 18334) {
				damage += damage*0.10;
				}
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] == 18333) {
				damage += damage*0.05;
				}
				if(c.getMagicData().godSpells()) 
				{
					int staffRequired = c.getMagicData().getStaffNeeded();
					if(c.usingMagic && staffRequired > 0 && Config.RUNES_REQUIRED) 
					{ // staff required
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != staffRequired) 
						{
							c.actionAssistant.sendMessage("You need a "+Server.getItemManager().getItemDefinition(staffRequired).getName().toLowerCase()+" to cast this spell.");
							c.getCombat().resetAttack();
							return;
						}
					}	
					int capeRequired = c.getMagicData().getCapes();
					if(c.usingMagic && capeRequired > 0 && Config.RUNES_REQUIRED) 
					{ // cape required
						if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE] != capeRequired) 
						{
							c.actionAssistant.sendMessage("You need a "+Server.getItemManager().getItemDefinition(capeRequired).getName().toLowerCase()+" to cast this spell.");
							c.getCombat().resetAttack();
							return;
						}
					}					
					if(System.currentTimeMillis() - c.godSpellDelay < Config.GOD_SPELL_CHARGE) 
					{
						damage = 10+Misc.random(20);
					}
				}
				boolean magicFailed = false;
				c.npcIndex = 0;
				if(!c.getCombatFormulas().fullVeracEquipped() && npc.definition.type == 3836)
				{
					c.getActionAssistant().Send("The kalphite queen seems uneffected by your attacks..");
					damage = 0;
				}
				if(npc.definition.type == 8282 || npc.definition.type == 8283)
						{ //kq edit
							c.getActionAssistant().Send("You cannot mage the elemental!");
							damage = 0;
							magicFailed = true;
						}
				if(npc.definition.type == 14696){
					switch(npc.phase){
						case 0:
							c.getActionAssistant().Send("@red@You can not mage Ganodermic Beast in this phase!");
							damage = 0;
							return;
					}
				}
				if (Misc.random(npc.defence) > 10+ Misc.random(calculateMagicAttackNPC())) 
				{
					damage = 0;
					magicFailed = true;
				}
				if (npc.hp - damage < 0) 
				{ 
					damage = npc.hp;
				}
				NPCKiller.addHits(npc, c, damage);
				if (npc.definition.type == 6221 && c.playerLevel[18] < 83) 
				{
					c.getActionAssistant().Send("You must be 83 Slayer to slay Spirit Mages");
					return;
				}		
				if (npc.definition.type == 6257 && c.playerLevel[18] < 83) 
				{
					c.getActionAssistant().Send("You must be 83 Slayer to slay Spirit Mages");
					return;
				}					
				if (npc.definition.type == 1832 && c.playerLevel[18] < 7) 
				{
					c.getActionAssistant().Send("You must be 7 Slayer to slay Cave Bugs.");
					return;
				}		
				if (npc.definition.type == 1601 && c.playerLevel[18] < 10) 
				{
					c.getActionAssistant().Send("You must be 10 Slayer to slay Cave Crawlers.");
					return;
				}		
				if (npc.definition.type == 1612 && c.playerLevel[18] < 15) 
				{
					c.getActionAssistant().Send("You must be 15 Slayer to slay Banshee's.");
					return;
				}	
				if (npc.definition.type == 1831 && c.playerLevel[18] < 17) 
				{
					c.getActionAssistant().Send("You must be 17 Slayer to slay Cave Slime.");
					return;
				}		
				if (npc.definition.type == 1622 && c.playerLevel[18] < 20) 
				{
					c.getActionAssistant().Send("You must be 20 Slayer to slay Rock Slugs.");
					return;
				}	
				if (npc.definition.type == 1620 && c.playerLevel[18] < 25 ) 
				{
					c.getActionAssistant().Send("You must be 25 Slayer to slay Cockatice's.");
					return;
				}
				if (npc.definition.type == 1633 && c.playerLevel[18] < 30) 
				{
					c.getActionAssistant().Send("You must be 30 Slayer to slay Pyrefiends.");
					return;
				}				
				if (npc.definition.type == 1632 && c.playerLevel[18] < 55) 
				{
					c.getActionAssistant().Send("You must be 55 Slayer to slay Turoths.");
					return;
				}				
				if (npc.definition.type == 1609 && c.playerLevel[18] < 70) 
				{
					c.getActionAssistant().Send("You must be 70 Slayer to slay Kurasks.");
					return;
				}				
				if (npc.definition.type == 2783 && c.playerLevel[18] < 90) 
				{
					c.getActionAssistant().Send("You must be 90 Slayer to slay Dark beasts.");
					return;
				}
				if (npc.definition.type == 1615 && c.playerLevel[18] < 85) 
				{
					c.getActionAssistant().Send("You must be 85 Slayer to slay Abyssal demons.");
					return;
				}
				if (npc.definition.type == 3794 && c.playerLevel[18] < 90) 
				{
					c.getActionAssistant().Send("You must be 90 Slayer to slay Tormented Demons.");
					return;
				}
				if (npc.definition.type == 1613 && c.playerLevel[18] < 80) 
				{
					c.getActionAssistant().Send("You must be 80 Slayer to slay Nechryaels.");
					return;
				}
				if (npc.definition.type == 1610 && c.playerLevel[18] < 75) 
				{
					c.getActionAssistant().Send("You must be 75 Slayer to slay Gargoyles.");
					return;
				}
				if (npc.definition.type == 1616 && c.playerLevel[18] < 40) 
				{
					c.getActionAssistant().Send("You must be 40 Slayer to slay Balilisks.");
					return;
				}
				if (npc.definition.type == 1637 && c.playerLevel[18] < 52) 
				{
					c.getActionAssistant().Send("You must be 52 Slayer to slay Jellys.");
					return;
				}
				if (npc.definition.type == 1624 && c.playerLevel[18] < 65) 
				{
					c.getActionAssistant().Send("You must be 65 Slayer to slay Dust Devils.");
					return;
				}
				if (npc.definition.type == 1619 && c.playerLevel[18] < 50) 
				{
					c.getActionAssistant().Send("You must be 50 Slayer to slay Bloodvelds.");
					return;
				}
				if (npc.definition.type == 1643 && c.playerLevel[18] < 45) 
				{
					c.getActionAssistant().Send("You must be 45 Slayer to slay Infernal Mages.");
					return;
				}
				if (npc.definition.type == 1604 && c.playerLevel[18] < 60) 
				{
					c.getActionAssistant().Send("You must be 60 Slayer to slay Abby Specs.");
					return;
				}			
				if ((npc.definition.type == 934 || npc.definition.type == 221) && (c.playerLevel[18] < 95) ) 
				{
					c.getActionAssistant().Send("You must be 95 Slayer to slay this monster.");
					return;
				}			
					if ((npc.definition.type >= 9463 && npc.definition.type <= 9467) && (c.playerLevel[18] < 99) ) 
		{
			c.getActionAssistant().Send("You must be 99 Slayer to slay this monster.");
			return;
		}
		if (npc.definition.type == 9463 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE] != 6570) {
			c.getActionAssistant().Send("You must be wearing a fire cape to hit through the ice!");
			return;
		}
		if ((npc.definition.type >= 8281 && npc.definition.type <= 8283) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] != 1580) {
			c.getActionAssistant().Send("You must be wearing ice gloves to attack this npc!");
			return;
			}
				c.getActionAssistant().addSkillXP((c.MAGIC_SPELLS[c.spellId][7] + damage*Config.MAGIC_EXP_RATE), 6); 
				c.getActionAssistant().addSkillXP((c.MAGIC_SPELLS[c.spellId][7] + damage*Config.HP_RATE), 3);
				c.getActionAssistant().refreshSkill(3);
				c.getActionAssistant().refreshSkill(6);
				
				if(c.getMagicData().getEndGfxHeight() == 100 && !magicFailed)
				{ // end GFX
					npc.gfx100(c.MAGIC_SPELLS[c.spellId][5]);
				} 
				else if (!magicFailed)
				{
					npc.gfx0(c.MAGIC_SPELLS[c.spellId][5]);
				}
				
				if(magicFailed) 
				{	
					npc.gfx100(85);
					if(c.sounds == 1) 
					{
						c.getActionAssistant().frame174(940,020,025);
					}
				}			
				if(!magicFailed) 
				{
					int freezeDelay = c.getMagicData().getFreezeTime();//freeze 
					if(freezeDelay > 0 && npc.freezeTimer == 0) 
					{
						npc.freezeTimer = freezeDelay;
					}
					if(c.sounds == 1 && c.mage == 0) 
					{
						c.getActionAssistant().frame174(c.MAGIC_SPELLS[c.spellId][17], 020, 000);
					}
					if(c.sounds == 1 && c.mage == 1) 
					{
						c.getActionAssistant().frame174(c.MAGIC_SPELLS[c.spellId][17], 000, 035);	
					}	
					switch(c.MAGIC_SPELLS[c.spellId][0]) 
					{ 
						case 12901:
						case 12919: // blood spells
						case 12911:
						case 12929:
							int heal = damage / 4;
							if(c.playerLevel[3] + heal >= c.getActionAssistant().getLevelForXP(c.playerXP[3])) 
							{
								c.playerLevel[3] = c.getActionAssistant().getLevelForXP(c.playerXP[3]);
							} 
							else 
							{
								c.playerLevel[3] += heal;
							}
							if(c.playerLevel[3] <= 0) 
							{
								c.playerLevel[3] = 0;
							}						
							c.getActionAssistant().refreshSkill(3);
							break;
					}
					
					
				}
				npc.underAttack = true;
				npc.underAttack2 = true;
				npc.killerId = c.playerId;
				if(c.MAGIC_SPELLS[c.spellId][6] != 0) 
				{
					npc.hitDiff = damage;
					npc.hit = damage;
					npc.hp -= damage;
					npc.hitUpdateRequired = true;
					c.totalDamageDealt += damage;
				}
				c.killingNpcIndex = npc.definition.type;
				npc.setAnimNumber(AnimationManager.getDefendAnimation(npc));
				npc.setAnimUpdateRequired(true);
				npc.setUpdateRequired(true);
				npc.hitUpdateRequired = true;
				c.usingMagic = false;
				c.castingMagic = false;
			}
		}
		if(c.sounds == 1) 
		{
			c.getActionAssistant().frame174(816, 000, 000);	
		}			
		c.oldNpcIndex = 0;
		c.projectileStage = 0;
		c.lastWeaponUsed = 0;
		c.newSpell = false;
	}

	public void calculateBonus() 
	{
		for (int i = 0; i < bonus.length; i++) 
		{
			bonus[i] = 0;
		}
		for (int i = 0; i < c.playerEquipment.length; i++) 
		{
			if (c.playerEquipment[i] > -1) 
			{
				ItemDefinition def = Server.getItemManager().getItemDefinition(
					c.playerEquipment[i]);
				for (int k = 0; k < bonus.length; k++) 
				{
					bonus[k] += def.getBonus(k);
				}
			}
		}
		for (int i = 0; i < bonus.length; i++) 
		{
			String text = "";
			int offset = 0;
			if (bonus[i] >= 0) 
			{
				text = Constants.BONUS_NAME[i] + ": +" + bonus[i];
			} 
			else 
			{
				text = Constants.BONUS_NAME[i] + ": -" + Math.abs(bonus[i]);
			}
			if (i == 12) 
			{
				offset = 1;
			}
		
			/*c.getActionAssistant().sendFrame126(text, (1675+i+offset));
			 c.getActionAssistant().sendFrame126(text, (1689+i+offset));
			 c.getActionAssistant().sendFrame126(text, (1688+i+offset));*/
		}
		c.getActionAssistant().sendFrame126("Stab: +"+bonus[0]+"",1675);
		c.getActionAssistant().sendFrame126("Slash: +"+bonus[1]+"",1676);
		c.getActionAssistant().sendFrame126("Crush: +"+bonus[2]+"",1677);
		c.getActionAssistant().sendFrame126("Mage: +"+bonus[3]+"",1678);
		c.getActionAssistant().sendFrame126("Range: +"+bonus[4]+"",1679);
		c.getActionAssistant().sendFrame126("Stab: +"+bonus[5]+"",1680);
		c.getActionAssistant().sendFrame126("Slash: +"+bonus[6]+"",1681);
		c.getActionAssistant().sendFrame126("Crush: +"+bonus[7]+"",1682);
		c.getActionAssistant().sendFrame126("Mage: +"+bonus[8]+"",1683);
		c.getActionAssistant().sendFrame126("Range: +"+bonus[9]+"",1684);
		c.getActionAssistant().sendFrame126("Strength: +"+bonus[10]+"",1686);
		c.getActionAssistant().sendFrame126("Prayer: +"+bonus[11]+"",1687);			
	}

	// send weapon

	
	/*	public int correctBowAndArrows() {
	 switch(c.playerEquipment[PlayerConstants.PLAYER_ARROWS]) {
	 case 7858:
	 case 7859:
	 case 7860:
	 case 7861:
	 case 7862:
	 case 7863:
	 case 7864:
	 case 7865:
	 case 7866:
	 case 7867:
	 case 7868:
	 case 7869:
	 case 7870:
	 case 7871:
	 case 7872:
	 return 9185;
	 case 882:	
	 case 884:
	 case 886:
	 case 888:
	 case 890:
	 case 892:
	 return 861;	
				
	 case 11212:
	 return 4827;
	 case 4740:
	 return 4734;
	 }
	 return -1;
	 }*/
	public void correctBowAndArrows() 
	{
		int weapon = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
		int arrows = c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS];
		c.bowlevel = false;
		c.boltGfx = false;
		if ((weapon == 9185 || weapon == 18357) && ((arrows >= 7858 && arrows <= 7872))) 
		{//rune c bow
			c.bowlevel = true;
		}	
		else if (weapon == 15241 && arrows == 15243) {
c.bowlevel = true;
}		
		else if (weapon == 861 && ((arrows >= 882 && arrows <= 893)
			|| arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623 || arrows == 5618|| arrows == 5624 
			|| arrows == 5619 || arrows == 5625 || arrows == 5620 || arrows == 5626 || arrows == 5621 || arrows == 5627 
			)) 
		{//magic shortbow Rune 
			c.bowlevel = true;
		}
		else   if (weapon == 839 && ((arrows >= 882 && arrows <= 885) || arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623)) 
		{//longbow Iron
			c.bowlevel = true;
		}
		else   if (weapon == 841 && ((arrows >= 882 && arrows <= 885)|| arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623)) 
		{//shortbow Iron
			c.bowlevel = true;
		}
		else   if (weapon == 843 && ((arrows >= 882 && arrows <= 887)
			|| arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623 || arrows == 5618|| arrows == 5624  
			)) 
		{//oak shortbow Steel
			c.bowlevel = true;
		}
		else     if (weapon == 845 && ((arrows >= 882 && arrows <= 887)
			|| arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623 || arrows == 5618|| arrows == 5624 
			)) 
		{//oak longbow Steel
			c.bowlevel = true;
		}
		else   if (weapon == 847 && ((arrows >= 882 && arrows <= 889)
			|| arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623 || arrows == 5618|| arrows == 5624 
			|| arrows == 5619 || arrows == 5625
			)) 
		{//willow longbow Mith
			c.bowlevel = true;
		}
		else     if (weapon == 849 && ((arrows >= 882 && arrows <= 889)
			|| arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623 || arrows == 5618|| arrows == 5624 
			|| arrows == 5619 || arrows == 5625
			)) 
		{//willow shortbow Mith
			c.bowlevel = true;
		}
		else    if (weapon == 851 && ((arrows >= 882 && arrows <= 891)
			|| arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623 || arrows == 5618|| arrows == 5624 
			|| arrows == 5619 || arrows == 5625 || arrows == 5620 || arrows == 5626
			)) 
		{//maple longbow Addy
			c.bowlevel = true;
		}
		else     if (weapon == 853 && ((arrows >= 882 && arrows <= 891)
			|| arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623 || arrows == 5618|| arrows == 5624 
			|| arrows == 5619 || arrows == 5625 || arrows == 5620 || arrows == 5626
			)) 
		{//maple shortbow Addy
			c.bowlevel = true;
		}
		else    if (weapon == 855 && ((arrows >= 882 && arrows <= 893)
			|| arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623 || arrows == 5618|| arrows == 5624 
			|| arrows == 5619 || arrows == 5625 || arrows == 5620 || arrows == 5626 || arrows == 5621 || arrows == 5627 
			))
		{//yew longbow Rune
			c.bowlevel = true;
		}
		else    if (weapon == 857 && ((arrows >= 882 && arrows <= 893)
			|| arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623 || arrows == 5618|| arrows == 5624 
			|| arrows == 5619 || arrows == 5625 || arrows == 5620 || arrows == 5626 || arrows == 5621 || arrows == 5627 
			)) 
		{//yew shortbow Rune
			c.bowlevel = true;
		}
		else     if (weapon == 859 && ((arrows >= 882 && arrows <= 893)
			|| arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623 || arrows == 5618|| arrows == 5624 
			|| arrows == 5619 || arrows == 5625 || arrows == 5620 || arrows == 5626 || arrows == 5621 || arrows == 5627 
			)) 
		{//magic longbow Rune
			c.bowlevel = true;
		}
        
		else    if ((weapon == 4827  || (weapon >= 15701 && weapon <= 15704))&& ((arrows >= 882 && arrows <= 893) || arrows == 11212
			|| arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623 || arrows == 5618|| arrows == 5624 
			|| arrows == 5619 || arrows == 5625 || arrows == 5620 || arrows == 5626 || arrows == 5621 || arrows == 5627 
		
			)) 
		{//darkbow Drag
			c.bowlevel = true;
		}
		else    if (weapon == 6724 && ((arrows >= 882 && arrows <= 893)
			|| arrows == 5616 || arrows == 5622 || arrows == 5617 || arrows == 5623 || arrows == 5618|| arrows == 5624 
			|| arrows == 5619 || arrows == 5625 || arrows == 5620 || arrows == 5626 || arrows == 5621 || arrows == 5627 		
			)) 
		{//seercull rune 
			c.bowlevel = true;
		}		
       
		else    if (weapon == 4734 && arrows == 4740) 
		{//karils c bow
			c.boltGfx = true;
			c.bowlevel = true;
		}		
	}
	public int getRangeStartGFX() 
	{
		switch(c.rangeItemUsed) 
		{

			case 863:
			case 870:
			case 5654:
			case 5661:
				return 220;
			case 864:
			case 872:
			case 5655:
			case 5662:
				return 219;
			case 865:
			case 873:
			case 5656:
			case 5663:
				return 221;
			case 866: // knives
			case 874:
			case 5657:
			case 5664:
				return 223;
			case 867:
			case 875:
			case 5659:
			case 5666:
				return 224;
			case 868:
			case 876:
			case 5660:
			case 5667:
				return 225;
			case 869:
				return 222;
			
			case 806:
			case 812:
			case 5628:
			case 5635:
				return 232;
			case 807:
			case 813:
			case 5629:
			case 5636:
				return 233;
			case 808:
			case 814:
			case 5630:
			case 5637:
				return 234;
			case 809: // darts
			case 815:
			case 5632:
			case 5639:
				return 235;
			case 810:
			case 816:
			case 5633:
			case 5640:
				return 236;
			case 811:
			case 817:
			case 5634:
			case 5641:
				return 237;
			
			case 825:
				return 206;
			case 826:
				return 207;
			case 827: // javelin
				return 208;
			case 828:
				return 209;
			case 829:
				return 210;
			case 830:
				return 211;
			case 13879:
				return 1836;

			case 800:
				return 42;
			case 801:
				return 43;
			case 802:
				return 44; // axes
			case 803:
				return 45;
			case 804:
				return 46;
			case 805:
				return 48;
			case 13883:
				return 1838;
								
			case 882:
			case 883:
			case 5616:
			case 5622:
				return 19;
			
			case 884:
			case 885:
			case 5617:
			case 5623:
				return 18;
			
			case 886:
			case 887:
			case 5618:
			case 5624:
				return 20;

			case 888:
			case 889:
			case 5619:
			case 5625:
				return 21;
			
			case 890:
			case 891:
			case 5620:
			case 5626:
				return 22;
			
			case 892:
			case 893:
			case 5621:
			case 5627:
			case 11212:
			case 7618:
				return 24;
			
			case 4212:
			case 4214:
			case 4215:
			case 4216:
			case 4217:
			case 4218:
			case 4219:
			case 4220:
			case 20171:
			case 4221:
			case 4222:
			case 4223:
				return 250;
			
		}
		return -1;
	}
	public int getRangeProjectileGFX() 
	{
		switch(c.rangeItemUsed) 
		{
			case 881:
			case 7858:
			case 7859:
			case 7860:
			case 7861:
			case 7862:
			case 7863:
			case 7864:
			case 7865:
			case 7866:
			case 7867:
			case 7868:
			case 7869:
			case 7870:
			case 7871:
			case 7872:
			case 4740:
				return 28;
				
			case 863:
			case 870:
			case 5654:
			case 5661:
				return 213;
			case 864:
			case 872:
			case 5655:
			case 5662:
				return 212;
			case 865:
			case 873:
			case 5656:
			case 5663:
				return 214;
			case 866: // knives
			case 874:
			case 5657:
			case 5664:
				return 216;
			case 867:
			case 875:
			case 5659:
			case 5666:
				return 217;
			case 868:
			case 876:
			case 5660:
			case 5667:
				return 218;	
			case 869:
				return 215;  

			case 806:
			case 812:
			case 5628:
			case 5635:
				return 226;
			case 807:
			case 813:
			case 5629:
			case 5636:
				return 227;
			case 808:
			case 814:
			case 5630:
			case 5637:
				return 228;
			case 809: // darts
			case 815:
			case 5632:
			case 5639:
				return 229;
			case 810:
			case 816:
			case 5633:
			case 5640:
				return 230;
			case 811:
			case 817:
			case 5634:
			case 5641:
				return 231;	

			case 825:
				return 200;
			case 826:
				return 201;
			case 827: // javelin
				return 202;
			case 828:
				return 203;
			case 829:
				return 204;
			case 830:
				return 205;	
			case 13879:
				return 1837;
			
			case 6522: // Toktz-xil-ul
				return 442;

			case 800:
				return 36;
			case 801:
				return 35;
			case 802:
				return 37; // axes
			case 803:
				return 38;
			case 804:
				return 39;
			case 805:
				return 40;
			case 13883:
				return 1839;
			case 882:
			case 883:
			case 5616:
			case 5622:
				return 10;
			
			case 884:
			case 885:
			case 5617:
			case 5623:
				return 9;
			
			case 886:
			case 887:
			case 5618:
			case 5624:
				return 11;

			case 888:
			case 889:
			case 5619:
			case 5625:
				return 12;
			
			case 890:
			case 891:
			case 5620:
			case 5626:
				return 13;
			
			case 892:
			case 893:
			case 5621:
			case 5627:
			case 11212:
			case 7618:
				return 15;
			
			case 4212:
			case 4214:
			case 4215:
			case 4216:
			case 4217:
			case 4218:
			case 4219:
			case 4220:
			case 20171:
			case 4221:
			case 4222:
			case 4223:
				return 249;
		}
		return -1;
	}
	
	public int getProjectileSpeed() 
	{
		switch(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) 
		{
		
			default:
				return 28;
		}
	}
	
	public int getProjectileShowDelay() 
	{
		if(c.usingMagic) 
		{
			switch(c.MAGIC_SPELLS[c.spellId][0]) 
			{
				case 12871: // ice blitz
				case 13023: // shadow barrage
				case 12891: // ice barrage
					return 6;	
				
				default:
					return 6;
			
	
			}
		} 
		else 
		{
			switch(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) 
			{
				case 863:
				case 864:
				case 865:
				case 866: // knives
				case 867:
				case 868:
				case 869:
				case 870:
				case 871:
				case 872:
				case 873:
				case 874:
				case 875:
				case 876:
				case 3093:
				case 5628:
				case 5629:
				case 5630:
				case 5631:
				case 5632:
				case 5633:
				case 5634:
				case 5635:
				case 5636:
				case 5637:
				case 5638:
				case 5639:
				case 5640:
				case 5641:
				
				case 806:
				case 807:
				case 808:
				case 809: // darts
				case 810:
				case 811:
				case 812:
				case 813:
				case 814:
				case 815:
				case 816:
				case 817:
				case 5654:
				case 5655:
				case 5656:
				case 5657:
				case 5658:
				case 5659:
				case 5660:
				case 5661:
				case 5662:
				case 5663:
				case 5664:
				case 5665:
				case 5666:
				case 5667:
			
				case 825:
				case 826:
				case 827: // javelin
				case 828:
				case 829:
				case 830:
				case 13879:
				case 13883:
				case 800:
				case 801:
				case 802:
				case 803: // axes
				case 804:
				case 805:
				case 881:				
			
					return 15;  
	 
			
				default:
					return 8;
			}
		}
	}

	
	public int getAttackDelay() 
	{
		if(c.usingMagic) 
		{
			switch(c.MAGIC_SPELLS[c.spellId][0]) 
			{
				case 12871: // ice blitz
					return 5;	
				
				default:
					return 5;
			
	
			}
		} 
		else 
		{
			switch(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) 
			{
				case 6522: // Toktz-xil-ul
					return 5;
				case 3190:
				case 3192:
				case 3194:
				case 3196:
				case 3198:
				case 3200:
				case 3202:	
				case 3204:
				case 4718:
				case 7158:
				case 1307: // 2handed
				case 1309:
				case 1313:
				case 1315:
				case 1317:
				case 1319:
				case 11694:
				case 16909:
				case 18369:
				case 16907:
				case 11696:
				case 1311:
				case 11730:
				case 6609:
				case 6528:
				case 1377:
				case 13905:
				case 11700:
				case 11698:	
				case 6527:				
					return 8;
				case 863:
				case 864:
				case 865:
				case 866: // knives
				case 867:
				case 868:
				case 869:
				case 870:
				case 871:
				case 872:
				case 873:
				case 874:
				case 875:
				case 876:
				case 3093:
				case 5628:
				case 5629:
				case 5630:
				case 5631:
				case 5632:
				case 5633:
				case 5634:
				case 5635:
				case 5636:
				case 5637:
				case 5638:
				case 5639:
				case 5640:
				case 5641:
				
				case 806:
				case 807:
				case 808:
				case 809: // darts
				case 810:
				case 811:
				case 812:
				case 813:
				case 814:
				case 815:
				case 816:
				case 817:
				case 5654:
				case 5655:
				case 5656:
				case 5657:
				case 5658:
				case 5659:
				case 5660:
				case 5661:
				case 5662:
				case 5663:
				case 5664:
				case 5665:
				case 5666:
				case 5667:
					return 3;
				case 13660:
				case 13661:
				case 13662:
				case 13663:
				case 13664:
				case 13665:
				case 13666:
				case 13667:
				case 13668:
				case 13669:
				case 13670:
				case 13671:
				case 4587:
				case 4151:	
				case 4747:
				case 4755:
				case 4726:				
					return 4;
				case 7822:
				case 7901:
				case 21371:
				case 21372:
				case 21373:
				case 21374:
				case 21375:
				case 861:
				case 839: //bows
				case 845:
				case 847:
				case 851:
				case 855:
				case 859:
				case 841:
				case 843:
				case 849:
				case 853:
				case 857:
				case 4214:
				case 4215:
				case 4216:
				case 4217:
				case 4218:
				case 4219:
				case 4827:
				case 15704:
				case 15701:
				case 15702:
				case 15703:
				case 6724:
					return 4;
				case 1215:
				case 1231:
				case 5680:
				case 5698:
				case 9185:				
				case 18357:
					return 5;
				
				case 4220:
				case 20171:
				case 4212:
				case 4221:
				case 4222:
				case 4223:
					return 7;
				case 800:
				case 801:
				case 802:
				case 803: // axes
				case 804:
				case 805:
				case 13883:
					return 6;
				
				case 825:
				case 826:
				case 827: // javelin
				case 828:
				case 829:
				case 830:
				case 13879:
				case 1305:
					return 6;
				
				default:
					return 6;
			}
		}
	}
	public boolean usingHally() 
	{
		switch(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) 
		{
			case 3190:
			case 3192:
			case 3194:
			case 3196:
			case 3198:
			case 3200:
			case 3202:
			case 3204:
				return true;
			
			default:
				return false;
		}
	}	
	
	public int getHitDelay() 
	{
		if(c.usingMagic) 
		{
			switch(c.MAGIC_SPELLS[c.spellId][0]) 
			{

				case 12891:
					return 0;
				default:
					return 6;
			
	
			}
		} 
		else 
		{
			switch(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]) 
			{
				case 4151:
					return 1;
				case 863:
				case 864:
				case 865:
				case 866: // knives
				case 867:
				case 868:
				case 869:
				case 870:
				case 871:
				case 872:
				case 873:
				case 874:
				case 875:
				case 876:
				case 3093:
				case 5628:
				case 5629:
				case 5630:
				case 5631:
				case 5632:
				case 5633:
				case 5634:
				case 5635:
				case 5636:
				case 5637:
				case 5638:
				case 5639:
				case 5640:
				case 5641:
				
				case 806:
				case 807:
				case 808:
				case 809: // darts
				case 810:
				case 811:
				case 812:
				case 813:
				case 814:
				case 815:
				case 816:
				case 817:
				case 5654:
				case 5655:
				case 5656:
				case 5657:
				case 5658:
				case 5659:
				case 5660:
				case 5661:
				case 5662:
				case 5663:
				case 5664:
				case 5665:
				case 5666:
				case 5667:
				case 9185:
				case 18357:
					return 2;
				
				case 825:
				case 826:
				case 827: // javelin
				case 828:
				case 829:
				case 830:
				case 13879:
				case 13883:
				case 800:
				case 801:
				case 802:
				case 803: // axes
				case 804:
				case 805:
				case 6522: // Toktz-xil-ul
				case 15241:
					return 3;

				case 839: //bows
				case 845:
				case 847:
				case 851:
				case 855:
				case 859:
				case 841:
				case 843:
				case 849:
				case 853:
				case 857:
				case 4212:
				case 4214:
				case 4215:
				case 4216:
				case 4217:
				case 4218:
				case 4219:
				case 4220:
				case 20171:
				case 4221:
				case 4222:
				case 4223:
				case 4827:
				case 15704:
				case 15701:
				case 15702:
				case 15703:
				case 6724:
					return 2;
				case 861:
					return 2;
				
				default:
					return 1;
			}
		}
	}
	

	public int getRequiredDistance()
	{
		if(c.followId > 0 && c.freezeTimer <= 0 && c.isMoving){
			return 5;
		} 
		else 
		{
			return 1;
		}
	}
	
	public void MultiMageAutoCast() {
		if((Config.WORLD_NUMBER == 3 || Config.WORLD_NUMBER == 2|| Config.WORLD_NUMBER == 1) && c.usingMagic && c.canCast && c.autoCast ){
				int hitcount = 0;
				boolean magicFailed3 = true;
			c.attackTimer = System.currentTimeMillis();
						c.lastWeaponSpeed = 2600;
				//Client targetPlayer = (Client) PlayerManager.getSingleton().getPlayers()[attackplayer];
					Client mb = (Client) PlayerManager.getSingleton().getPlayers()[c.AutoCastplayerIndex];
					if (!mb.inMulti()) {
						return;
					}
					for (Player p : Server.getPlayerManager().getPlayers()) {
						int count = 0;
						Client m = (Client)p;
						if(m == null) continue;
						if(m.playerId == c.playerId) continue;
						if(!m.inMulti()) continue;
							if(Config.COMBAT_LEVEL_DIFFERENCE) {
								int combatDif1 = c.getCombat().getCombatDifference(c.combatLevel, PlayerManager.getSingleton().getPlayers()[m.playerId].combatLevel);
								if(combatDif1 > c.wildLevel || combatDif1 > PlayerManager.getSingleton().getPlayers()[m.playerId].wildLevel) {
									//c.actionAssistant.sendMessage("Your combat level difference is too great to attack that player here.");
									c.stopMovement();
									continue;
								}
							}
							if(!PlayerManager.getSingleton().getPlayers()[m.playerId].inWild()) {
								//c.actionAssistant.sendMessage("That player is not in the wilderness.");
								c.stopMovement();
								continue;
							}
							if(!c.inWild()) {
								//c.actionAssistant.sendMessage("You're not in the wilderness.");
								c.stopMovement();
								continue;
							}
							if (hitcount > 9) {
							break;
							}
							if(mb.goodDistance(PlayerManager.getSingleton().getPlayers()[m.playerId].getAbsX(), PlayerManager.getSingleton().getPlayers()[m.playerId].getAbsY(), mb.getAbsX(), mb.getAbsY(), 1)) {
							hitcount += 1;
							mb.underAttackBy = c.playerId;
					mb.logoutDelay = System.currentTimeMillis();;
					mb.singleCombatDelay = System.currentTimeMillis();
					mb.singleCombatDelay2 = System.currentTimeMillis();
							//if(Math.abs(m.absX - mb.absX) < 2 && Math.abs(m.absY - mb.absY) < 2) {
							magicFailed3 = false;
					if(Misc.random(10+m.getCombat().calculateMagicDefence()) > Misc.random(12+calculateMagicAttack())) {
						PlayerManager.getSingleton().getPlayers()[m.playerId].gfx100(85);
						magicFailed3 = true;
					}
							int freezeDelay = c.getMagicData().getFreezeTime()*2;//freeze time
					if(freezeDelay > 0 && m.freezeTimer == -5 && !magicFailed3) 
					{ 
					 if(m.prayerActive[16] || m.curseActive[7]) {
					 freezeDelay = (int)freezeDelay/2;
					 }
						m.freezeTimer = freezeDelay;
						m.stopMovement();
						m.getActionAssistant().Send("You have been frozen!");
						m.newFreeze = true;
						m.frozenBy = c.playerId;
					}
									c.reCast = 6;
									count++;
									if (count >= 8) break;
									int pX = c.getX();
									int pY = c.getY();
									int nX = Server.getPlayerManager().getPlayers()[m.playerId].getAbsX();
									int nY = Server.getPlayerManager().getPlayers()[m.playerId].getAbsY();
									int offX = (pY - nY)* -1;
									int offY = (pX - nX)* -1;
									c.castingMagic = true;
								
								playerAutoCast(m.playerId, magicFailed3);
									//playerDelayedHit(m.playerId);
								//	c.usingMagic = false;
								//	c.castingMagic = false;
								//	c.projectileStage = 0;
								
							}
					}
			
			}
	}
	
	public boolean multiMageAuto() 
	{
	if (((c.AutoCast_SpellIndex >= 28) && (c.AutoCast_SpellIndex <=31)) || ((c.AutoCast_SpellIndex >= 20) &&( c.AutoCast_SpellIndex <= 23))) {
	return true;
	}
	return false;
	}
	public boolean multiMage() 
	{
		if(c.MAGIC_SPELLS[c.spellId][0] == 12891 || c.MAGIC_SPELLS[c.spellId][0] == 13023 ||
			c.MAGIC_SPELLS[c.spellId][0] == 12881 || c.MAGIC_SPELLS[c.spellId][0] == 12929 ||
			c.MAGIC_SPELLS[c.spellId][0] == 12919 || c.MAGIC_SPELLS[c.spellId][0] == 13011 || 
			c.MAGIC_SPELLS[c.spellId][0] == 12963 || c.MAGIC_SPELLS[c.spellId][0] == 12975)
		{
			return true;
		}
		return false;
	}
	
	public boolean checkMagicReqs(int spell) 
	{
	c.FrozenBook = false;
		if(c.usingMagic && Config.MAGIC_LEVEL_REQUIRED) 
		{ // check magic level
			if(c.playerLevel[6] < c.MAGIC_SPELLS[spell][1]) 
			{
				c.actionAssistant.sendMessage("You need to have a magic level of " +c.MAGIC_SPELLS[spell][1]+" to cast this spell.");
				//c.attackTimer += 2000;
				c.lastWeaponSpeed = 2500;
				c.attackTimer += 4000;
				c.glitchTimer = 4;
				//c.getCombat().resetAttack();
				return false;
			}
		}
		if(c.usingMagic && Config.RUNES_REQUIRED) 
		{ // check for runes
			// combo rune
			if(c.getMagicData().hasComboRunes(c.MAGIC_SPELLS[spell][0]))
				return true;
			if (!c.actionAssistant.playerHasItem(c.MAGIC_SPELLS[spell][8], c.MAGIC_SPELLS[spell][9])) {
			if (c.MAGIC_SPELLS[spell][8] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] != 18346) {
				c.actionAssistant.sendMessage("You don't have the required runes to cast this spell.");
				c.lastWeaponSpeed = 2500;
				c.attackTimer += 5000;
				c.glitchTimer = 4;
				c.getCombat().resetAttack();
				return false;
			}
			else if (c.MAGIC_SPELLS[spell][8] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346) {
			//c.FrozenBook = true;
			return true;
			}
			else { 
			c.actionAssistant.sendMessage("You don't have the required runes to cast this spell.");
				c.lastWeaponSpeed = 2500;
				c.attackTimer += 5000;
				c.glitchTimer = 4;
				c.getCombat().resetAttack();
				return false;
			}
			}
			if 	(!c.actionAssistant.playerHasItem(c.MAGIC_SPELLS[spell][10], c.MAGIC_SPELLS[spell][11])) {
			if (c.MAGIC_SPELLS[spell][10] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] != 18346) {
				c.actionAssistant.sendMessage("You don't have the required runes to cast this spell.");
				c.lastWeaponSpeed = 2500;
				c.attackTimer += 5000;
				c.glitchTimer = 4;
				c.getCombat().resetAttack();
				return false;
			}
			else if (c.MAGIC_SPELLS[spell][10] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346) {
			return true;
			}
			else { 
			c.actionAssistant.sendMessage("You don't have the required runes to cast this spell.");
				c.lastWeaponSpeed = 2500;
				c.attackTimer += 5000;
				c.glitchTimer = 4;
				c.getCombat().resetAttack();
				return false;
			}
			}
			
				if (!c.actionAssistant.playerHasItem(c.MAGIC_SPELLS[spell][12], c.MAGIC_SPELLS[spell][13])) {
				if (c.MAGIC_SPELLS[spell][12] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] != 18346) {
				c.actionAssistant.sendMessage("You don't have the required runes to cast this spell.");
				c.lastWeaponSpeed = 2500;
				c.attackTimer += 5000;
				c.glitchTimer = 4;
				c.getCombat().resetAttack();
				return false;
			}
			else if (c.MAGIC_SPELLS[spell][12] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346) {
			//c.FrozenBook = true;
			return true;
			}
			else { 
			c.actionAssistant.sendMessage("You don't have the required runes to cast this spell.");
				c.lastWeaponSpeed = 2500;
				c.attackTimer += 5000;
				c.glitchTimer = 4;
				c.getCombat().resetAttack();
				return false;
			}
			}
			if 	(!c.actionAssistant.playerHasItem(c.MAGIC_SPELLS[spell][14], c.MAGIC_SPELLS[spell][15])) {
			if (c.MAGIC_SPELLS[spell][14] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] != 18346) {
				c.actionAssistant.sendMessage("You don't have the required runes to cast this spell.");
				c.lastWeaponSpeed = 2500;
				c.attackTimer += 5000;
				c.glitchTimer = 4;
				c.getCombat().resetAttack();
				return false;
			}
			else if (c.MAGIC_SPELLS[spell][14] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346) {
			//c.FrozenBook = true;
			return true;
			}
			else { 
			c.actionAssistant.sendMessage("You don't have the required runes to cast this spell.");
				c.lastWeaponSpeed = 2500;
				c.attackTimer += 5000;
				c.glitchTimer = 4;
				c.getCombat().resetAttack();
				return false;
			}
			
				
			} 
		}
	

		if(c.usingMagic && c.playerId > 0) 
		{
			if(PlayerManager.getSingleton().getPlayers()[c.playerId] != null) 
			{
			
				for(int r = 0; r < c.REDUCE_SPELLS.length; r++)
				{	// reducing spells, confuse etc
					if(PlayerManager.getSingleton().getPlayers()[c.playerId].REDUCE_SPELLS[r] == c.MAGIC_SPELLS[spell][0]) 
					{
						c.reduceSpellId = r;
						if((System.currentTimeMillis() - PlayerManager.getSingleton().getPlayers()[c.playerId].reduceSpellDelay[c.reduceSpellId]) > PlayerManager.getSingleton().getPlayers()[c.playerId].REDUCE_SPELL_TIME[c.reduceSpellId]) 
						{
							PlayerManager.getSingleton().getPlayers()[c.playerId].canUseReducingSpell[c.reduceSpellId] = true;
						} 
						else 
						{
							PlayerManager.getSingleton().getPlayers()[c.playerId].canUseReducingSpell[c.reduceSpellId] = false;
						}
						break;
					}			
				}
				if(!PlayerManager.getSingleton().getPlayers()[c.playerId].canUseReducingSpell[c.reduceSpellId]) 
				{
					c.actionAssistant.sendMessage("That player is currently immune to this spell.");
					c.usingMagic = false;
					c.stopMovement();
					c.glitchTimer = 4;
					//c.getCombat().resetAttack();
					return false;
				}
			}
		}

		int staffRequired = c.getMagicData().getStaffNeeded();
		if(c.usingMagic && staffRequired > 0 && Config.RUNES_REQUIRED) 
		{ // staff required
			if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != staffRequired) 
			{
				c.actionAssistant.sendMessage("You need a "+Server.getItemManager().getItemDefinition(staffRequired).getName().toLowerCase()+" to cast this spell.");
				//c.attackTimer = 3;
				c.glitchTimer = 4;
				c.getCombat().resetAttack();
				return false;
			}
		}
		int capeRequired = c.getMagicData().getCapes();
		if(c.usingMagic && capeRequired > 0 && Config.RUNES_REQUIRED) 
		{ // cape required
			if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE] != capeRequired) 
			{
				c.actionAssistant.sendMessage("You need a "+Server.getItemManager().getItemDefinition(capeRequired).getName().toLowerCase()+" to cast this spell.");
				c.getCombat().resetAttack();
				c.glitchTimer = 4;
				return false;
			}
		}			
		if(c.usingMagic && Config.MAGIC_LEVEL_REQUIRED) 
		{ // check magic level
			if(c.playerLevel[6] < c.MAGIC_SPELLS[spell][1]) 
			{
				c.actionAssistant.sendMessage("You need to have a magic level of " +c.MAGIC_SPELLS[spell][1]+" to cast this spell.");
				//c.getCombat().resetAttack();
				c.attackTimer += 2000;
				c.glitchTimer = 4;
				return false;
			}
		}
		if(c.usingMagic && Config.RUNES_REQUIRED) 
		{
			if(c.MAGIC_SPELLS[spell][8] > 0) 
			{ // deleting runes
			if (!(c.MAGIC_SPELLS[spell][8] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
				c.actionAssistant.deleteItem(c.MAGIC_SPELLS[spell][8], c.actionAssistant.getItemSlot(c.MAGIC_SPELLS[spell][8]), c.MAGIC_SPELLS[spell][9]);
			}
			}
			if(c.MAGIC_SPELLS[spell][10] > 0) 
			{
			if (!(c.MAGIC_SPELLS[spell][10] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
				c.actionAssistant.deleteItem(c.MAGIC_SPELLS[spell][10], c.actionAssistant.getItemSlot(c.MAGIC_SPELLS[spell][10]), c.MAGIC_SPELLS[spell][11]);
				}
			}
			if(c.MAGIC_SPELLS[spell][12] > 0) 
			{
			if (!(c.MAGIC_SPELLS[spell][12] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
				c.actionAssistant.deleteItem(c.MAGIC_SPELLS[spell][12], c.actionAssistant.getItemSlot(c.MAGIC_SPELLS[spell][12]), c.MAGIC_SPELLS[spell][13]);
				}
			}
			if(c.MAGIC_SPELLS[spell][14] > 0) 
			{
			if (!(c.MAGIC_SPELLS[spell][14] == 555 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 18346)) {
				c.actionAssistant.deleteItem(c.MAGIC_SPELLS[spell][14], c.actionAssistant.getItemSlot(c.MAGIC_SPELLS[spell][14]), c.MAGIC_SPELLS[spell][15]);
				}
			}
		}
		return true;
	}

	public int getArrowStartHeight() 
	{
		switch(c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS]) 
		{
			case 7858:
			case 7859:
			case 7860:
			case 7861:
			case 7862:
			case 7863:
			case 7864:
			case 7865:
			case 7866:
			case 7867:
			case 7868:
			case 7869:
			case 7870:
			case 7871:
			case 7872:
				return 35;
			default:
				return 43;
		}
	}
		public int getArrowEndHeight(){
		switch(c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS]) 
		{
			case 7858:
			case 7859:
			case 7860:
			case 7861:
			case 7862:
			case 7863:
			case 7864:
			case 7865:
			case 7866:
			case 7867:
			case 7868:
			case 7869:
			case 7870:
			case 7871:
			case 7872:
				return 35;
			default:
				return 31;
		}
	}	

	public void createProjectile(int casterY, int casterX, int offsetY, int offsetX, int angle, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time) 
	{      
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC(casterY - (c.getMapRegionY() * 8) - 2);
		c.getOutStream().writeByteC(casterX - (c.getMapRegionX() * 8) - 3);
		c.getOutStream().createFrame(117);
		c.getOutStream().writeByte(angle);
		c.getOutStream().writeByte(offsetY);
		c.getOutStream().writeByte(offsetX);
		c.getOutStream().writeWord(lockon);
		c.getOutStream().writeWord(gfxMoving);
		c.getOutStream().writeByte(startHeight);
		c.getOutStream().writeByte(endHeight);
		c.getOutStream().writeWord(time);
		c.getOutStream().writeWord(speed);
		c.getOutStream().writeByte(16);
		c.getOutStream().writeByte(64);
	}
	
	public void createPlayersProjectile(int casterY, int casterX, int offsetY, int offsetX, int angle, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time) 
	{
		//if (Server.Projectile) 
		if (true)
		{
			for (int i = 1; i < Constants.MAXIMUM_PLAYERS; i++)  
			{
				Player p = PlayerManager.getSingleton().getPlayers()[i];
				if(p != null) 
				{
					Client person = (Client) PlayerManager.getSingleton().getPlayers()[i];
					if(person.distanceToPoint(casterX, casterY) <= 25)
					{	
						person.getCombat().createProjectile(casterY, casterX, offsetY, offsetX, angle, speed, gfxMoving, startHeight, endHeight, lockon, time);	
					}
				}
			} 
		}
	}

	public void createPlayersProjectile2(int casterX, int casterY, int offsetX, int offsetY, int angle, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time) 
	{
	startHeight =  43;
	endHeight = 31;
	time = 5;
		//if (Server.Projectile) 
		if (true)
		{
			for (int i = 1; i < Constants.MAXIMUM_PLAYERS; i++)  
			{
				Player p = PlayerManager.getSingleton().getPlayers()[i];
				if(p != null) 
				{
					Client person = (Client) PlayerManager.getSingleton().getPlayers()[i];
					if(person.distanceToPoint(casterX, casterY) <= 25)
					{	
						person.getCombat().createProjectile(casterY, casterX, offsetY, offsetX, angle, speed, gfxMoving, startHeight, endHeight, lockon, time);	
					}
				}
			} 
		}
	}

	

	/**
	 * Range
	 **/

	public int getArrowBonus() 
	{
		switch(c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS]) 
		{
			case 882:
			case 883:
			case 5616:
			
				return 1;
			
			case 884:
			case 885:
			case 5617:
				return 2;
			
			case 886:
			case 887:
			case 5618:
			case 15243:
				return 3;

			case 888:
			case 889:
			case 5619:
				return 4;
			
			case 890:
			case 891:
			case 5610:
				return 5;
			
			case 893:
			case 892:
			case 5621:
				return 6;
			case 11212:
			case 7618:
				return 7;
			case 7858:
			case 7859:
			case 7860:
				return 2;
			case 7861:
				return 3;
			case 7862:
				return 4;
			case 7863:
				return 5;
			case 7864:
			case 7865:
			case 7866:
				return 2;
			case 7867:
				return 6;
			case 7868:
				return 2;
			case 7869:
				return 2;
			case 7870:
				return 2;
			case 7871:
			case 7872:
				return 3;
			default:
				return 0;
		}
	}
	/**Prayer**/
		
	public void activateCurse(int i) {
		if (c.combatLevel < 100) {
			c.actionAssistant.sendMessage("You cannot use curses unless you are level 100 or higher");
			for(int p = 0; p < c.CURSE.length; p++) 
			{ // reset prayer glows 
				c.curseActive[p] = false;
				c.actionAssistant.sendFrame36(c.CURSE_GLOW[p], 0);	
			}
			return;
		}
	/*if(!c.canEat)
		{
for(int p = 0; p < c.CURSE.length; p++) 
			{ // reset prayer glows 
				c.curseActive[p] = false;
				c.actionAssistant.sendFrame36(c.CURSE_GLOW[p], 0);	
			}
			return;
}*/
		if(c.duelRule[7])
		{ //markerduel
			for(int p = 0; p < c.PRAYER.length; p++) 
			{ // reset prayer glows 
				c.curseActive[p] = false;
				c.actionAssistant.sendFrame36(c.CURSE_GLOW[p], 0);	
			}
			c.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
			return;
		}
		c.getActionAssistant().resetRegPrayers();
		c.getCurse().activateCurse(i);

		}

	public void activatePrayer(int i) 
	{
	c.getActionAssistant().resetCursePrayers();
/*		if(!c.canEat)
		{
			for(int p = 0; p < c.PRAYER.length; p++) 
			{ // reset prayer glows 
				c.prayerActive[p] = false;
				c.actionAssistant.sendFrame36(c.PRAYER_GLOW[p], 0);	
			}
			return;
		}*/
		if(c.duelRule[7])
		{ //markerduel
			for(int p = 0; p < c.PRAYER.length; p++) 
			{ // reset prayer glows 
				c.prayerActive[p] = false;
				c.actionAssistant.sendFrame36(c.PRAYER_GLOW[p], 0);	
			}
			c.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
			return;
		}
		else {
			/*	if (client.duelStatus >4) {
					Client o = (Client) PlayerManager.getSingleton().getPlayers()[c.duelingWith];
					if (o != null) {
						if (o.duelStatus > 4 && o.duelRule[7]) {
							try {
										BanProcessor.banUser(c.playerName,"James");
										c.kick();
										c.disconnected = true;
							} catch (IOException e) {
										e.printStackTrace();
							}
						}
					}
				}*/
		}
		if(c.playerLevel[5] > 0 || !Config.PRAYER_POINTS_REQUIRED)
		{
			if(c.actionAssistant.getLevelForXP(c.playerXP[5]) >= c.PRAYER_LEVEL_REQUIRED[i] || !Config.PRAYER_LEVEL_REQUIRED) 
			{
				boolean headIcon = false;
				c.prayOff = 1;
				switch(i) 
				{
					case 0:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[5], 0);
							c.prayerActive[5] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[13], 0);
							c.prayerActive[13] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);						
							c.prayerActive[24] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;
						}
						break;
					
					case 1:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[3], 0);
							c.prayerActive[3] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[4], 0);
							c.prayerActive[4] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[6], 0);
							c.prayerActive[6] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[11], 0);
							c.prayerActive[11] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[12], 0);
							c.prayerActive[12] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[14], 0);
							c.prayerActive[14] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
							c.prayerActive[19] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[20], 0);
							c.prayerActive[20] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;	
						}
						break;
					
					case 2:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[3], 0);
							c.prayerActive[3] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[4], 0);
							c.prayerActive[4] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[7], 0);
							c.prayerActive[7] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[11], 0);
							c.prayerActive[11] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[12], 0);
							c.prayerActive[12] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[15], 0);
							c.prayerActive[15] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
							c.prayerActive[19] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[20], 0);
							c.prayerActive[20] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;	
						}
						break;
					
					case 3:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[1], 0);
							c.prayerActive[1] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[2], 0);
							c.prayerActive[2] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[4], 0);
							c.prayerActive[4] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[6], 0);
							c.prayerActive[6] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[7], 0);
							c.prayerActive[7] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[11], 0);
							c.prayerActive[11] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[12], 0);
							c.prayerActive[12] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[14], 0);
							c.prayerActive[14] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[15], 0);
							c.prayerActive[15] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
							c.prayerActive[19] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[20], 0);
							c.prayerActive[20] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;							
						}
						break;
					case 4:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[1], 0);
							c.prayerActive[1] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[2], 0);
							c.prayerActive[2] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[3], 0);
							c.prayerActive[3] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[6], 0);
							c.prayerActive[6] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[7], 0);
							c.prayerActive[7] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[11], 0);
							c.prayerActive[11] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[12], 0);
							c.prayerActive[12] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[14], 0);
							c.prayerActive[14] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[15], 0);
							c.prayerActive[15] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
							c.prayerActive[19] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[20], 0);
							c.prayerActive[20] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;		
						}
						break;
					case 5:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[0], 0);
							c.prayerActive[0] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[13], 0);
							c.prayerActive[13] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;
						}
						break;
					case 6:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[1], 0);
							c.prayerActive[1] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[3], 0);
							c.prayerActive[3] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[4], 0);
							c.prayerActive[4] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[14], 0);
							c.prayerActive[14] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[11], 0);
							c.prayerActive[11] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[12], 0);
							c.prayerActive[12] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
							c.prayerActive[19] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[20], 0);
							c.prayerActive[20] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;	
						}
						break;
					case 7:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[2], 0);
							c.prayerActive[2] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[3], 0);
							c.prayerActive[3] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[4], 0);
							c.prayerActive[4] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[11], 0);
							c.prayerActive[11] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[12], 0);
							c.prayerActive[12] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[15], 0);
							c.prayerActive[15] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
							c.prayerActive[19] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[20], 0);
							c.prayerActive[20] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;	
						}
						break;
					case 10:
						
						break;
					
					case 11:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[1], 0);
							c.prayerActive[1] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[2], 0);
							c.prayerActive[2] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[3], 0);
							c.prayerActive[3] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[4], 0);
							c.prayerActive[4] = false;		
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[6], 0);
							c.prayerActive[6] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[7], 0);
							c.prayerActive[7] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[12], 0);
							c.prayerActive[12] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[14], 0);
							c.prayerActive[14] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[15], 0);
							c.prayerActive[15] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
							c.prayerActive[19] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[20], 0);
							c.prayerActive[20] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;	
						
						}
						break;
					case 12:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[1], 0);
							c.prayerActive[1] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[2], 0);
							c.prayerActive[2] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[3], 0);
							c.prayerActive[3] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[4], 0);
							c.prayerActive[4] = false;		
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[6], 0);
							c.prayerActive[6] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[7], 0);
							c.prayerActive[7] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[11], 0);
							c.prayerActive[11] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[14], 0);
							c.prayerActive[14] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[15], 0);
							c.prayerActive[15] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
							c.prayerActive[19] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[20], 0);
							c.prayerActive[20] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;	
						
						}					
					
						break;
					case 13:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[0], 0);
							c.prayerActive[0] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[5], 0);
							c.prayerActive[5] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);						
							c.prayerActive[24] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;
						}
						break;
					case 14:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[1], 0);
							c.prayerActive[1] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[3], 0);
							c.prayerActive[3] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[4], 0);
							c.prayerActive[4] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[6], 0);
							c.prayerActive[6] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[11], 0);
							c.prayerActive[11] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[12], 0);
							c.prayerActive[12] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
							c.prayerActive[19] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[20], 0);
							c.prayerActive[20] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;	
						}					
						break;
					case 15:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[2], 0);
							c.prayerActive[2] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[3], 0);
							c.prayerActive[3] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[4], 0);
							c.prayerActive[4] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[7], 0);
							c.prayerActive[7] = false;		
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[11], 0);
							c.prayerActive[11] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[12], 0);
							c.prayerActive[12] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
							c.prayerActive[19] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[20], 0);
							c.prayerActive[20] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;	
						}
						break;
						/*case 15:
						 headIcon = true;
						 if(c.prayerActive[i] == false) {
						 c.actionAssistant.sendFrame36(c.PRAYER_GLOW[17], 0);
						 c.prayerActive[16] = false;
						 c.actionAssistant.sendFrame36(c.PRAYER_GLOW[18], 0);
						 c.prayerActive[17] = false;
						 c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
						 c.prayerActive[18] = false;
						 c.actionAssistant.sendFrame36(c.PRAYER_GLOW[22], 0);
						 c.prayerActive[21] = false;
						 c.actionAssistant.sendFrame36(c.PRAYER_GLOW[23], 0);
						 c.prayerActive[22] = false;
						 c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
						 c.prayerActive[23] = false;
						 }					
						 break;*/
					
					case 16:
						headIcon = true;
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[17], 0);
							c.prayerActive[17] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[18], 0);
							c.prayerActive[18] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[21], 0);
							c.prayerActive[21] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[22], 0);
							c.prayerActive[22] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[23], 0);
							c.prayerActive[23] = false;
							c.headIcon = 2;
						}
						break;	
					case 17:
						headIcon = true;
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[16], 0);
							c.prayerActive[16] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[18], 0);
							c.prayerActive[18] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[21], 0);
							c.prayerActive[21] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[22], 0);
							c.prayerActive[22] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[23], 0);
							c.prayerActive[23] = false;
							c.headIcon = 1;
							c.actionAssistant.requestUpdates();
						}
						break;
					case 18:
						headIcon = true;
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[16], 0);
							c.prayerActive[16] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[17], 0);
							c.prayerActive[17] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[21], 0);
							c.prayerActive[21] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[22], 0);
							c.prayerActive[22] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[23], 0);
							c.prayerActive[23] = false;
							c.headIcon = 0;
							c.actionAssistant.requestUpdates();
						}
						break;
					case 19:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[1], 0);
							c.prayerActive[1] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[2], 0);
							c.prayerActive[2] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[3], 0);
							c.prayerActive[3] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[4], 0);
							c.prayerActive[4] = false;		
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[6], 0);
							c.prayerActive[6] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[7], 0);
							c.prayerActive[7] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[11], 0);
							c.prayerActive[11] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[12], 0);
							c.prayerActive[12] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[14], 0);
							c.prayerActive[14] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[15], 0);
							c.prayerActive[15] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[20], 0);
							c.prayerActive[20] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;
						
						
						}					
						prayUp();
						c.actionAssistant.requestUpdates();
						break;
					case 20:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[1], 0);
							c.prayerActive[1] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[2], 0);
							c.prayerActive[2] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[3], 0);
							c.prayerActive[3] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[4], 0);
							c.prayerActive[4] = false;		
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[6], 0);
							c.prayerActive[6] = false;			
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[7], 0);
							c.prayerActive[7] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[11], 0);
							c.prayerActive[11] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[12], 0);
							c.prayerActive[12] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[14], 0);
							c.prayerActive[14] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[15], 0);
							c.prayerActive[15] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
							c.prayerActive[19] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;
						
						}							
					
						break;
					case 21:
						headIcon = true;
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[16], 0);
							c.prayerActive[16] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[17], 0);
							c.prayerActive[17] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[18], 0);
							c.prayerActive[18] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[22], 0);
							c.prayerActive[22] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[23], 0);
							c.prayerActive[23] = false;
							c.headIcon = 3;
							c.actionAssistant.requestUpdates();
						}
						break;
					case 22:
						headIcon = true;
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[16], 0);
							c.prayerActive[16] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[17], 0);
							c.prayerActive[17] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[18], 0);
							c.prayerActive[18] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[21], 0);
							c.prayerActive[21] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[23], 0);
							c.prayerActive[23] = false;
							c.headIcon = 5;
							c.actionAssistant.requestUpdates();
						}
						break;
					case 23:
						headIcon = true;
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[16], 0);
							c.prayerActive[16] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[17], 0);
							c.prayerActive[17] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[18], 0);
							c.prayerActive[18] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[21], 0);
							c.prayerActive[21] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[22], 0);
							c.prayerActive[22] = false;
							c.headIcon = 4;
						}
						c.actionAssistant.requestUpdates();
						break;
					case 24:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[0], 0);
							c.prayerActive[0] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[1], 0);
							c.prayerActive[1] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[2], 0);
							c.prayerActive[2] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[3], 0);
							c.prayerActive[3] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[4], 0);
							c.prayerActive[4] = false;		
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[5], 0);
							c.prayerActive[5] = false;							
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[6], 0);
							c.prayerActive[6] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[7], 0);
							c.prayerActive[7] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[11], 0);
							c.prayerActive[11] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[12], 0);
							c.prayerActive[12] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[13], 0);
							c.prayerActive[13] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[14], 0);
							c.prayerActive[14] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[15], 0);
							c.prayerActive[15] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
							c.prayerActive[19] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[20], 0);
							c.prayerActive[20] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[25], 0);
							c.prayerActive[25] = false;	
						}					
					
						break;
					
					case 25:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[0], 0);
							c.prayerActive[0] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[1], 0);
							c.prayerActive[1] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[2], 0);
							c.prayerActive[2] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[3], 0);
							c.prayerActive[3] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[4], 0);
							c.prayerActive[4] = false;		
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[5], 0);
							c.prayerActive[5] = false;							
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[6], 0);
							c.prayerActive[6] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[7], 0);
							c.prayerActive[7] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[11], 0);
							c.prayerActive[11] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[12], 0);
							c.prayerActive[12] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[13], 0);
							c.prayerActive[13] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[14], 0);
							c.prayerActive[14] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[15], 0);
							c.prayerActive[15] = false;
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[19], 0);
							c.prayerActive[19] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[20], 0);
							c.prayerActive[20] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[24], 0);
							c.prayerActive[24] = false;	
						}	
						break;
						case 26:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[35], 0);
							c.prayerActive[35] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[36], 0);
							c.prayerActive[36] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[37], 0);
							c.prayerActive[37] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[38], 0);
							c.prayerActive[38] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[39], 0);
							c.prayerActive[39] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[40], 0);
							c.prayerActive[40] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[41], 0);
							c.prayerActive[41] = false;	
						}	
						break;
						case 27:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[35], 0);
							c.prayerActive[35] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[36], 0);
							c.prayerActive[36] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[37], 0);
							c.prayerActive[37] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[38], 0);
							c.prayerActive[38] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[39], 0);
							c.prayerActive[39] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[40], 0);
							c.prayerActive[40] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[41], 0);
							c.prayerActive[41] = false;	
						}	
						break;
						case 28:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[35], 0);
							c.prayerActive[35] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[36], 0);
							c.prayerActive[36] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[37], 0);
							c.prayerActive[37] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[38], 0);
							c.prayerActive[38] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[39], 0);
							c.prayerActive[39] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[40], 0);
							c.prayerActive[40] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[41], 0);
							c.prayerActive[41] = false;	
						}	
						break;
						case 29:
						if(c.prayerActive[i] == false) 
						{
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[35], 0);
							c.prayerActive[35] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[36], 0);
							c.prayerActive[36] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[37], 0);
							c.prayerActive[37] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[38], 0);
							c.prayerActive[38] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[39], 0);
							c.prayerActive[39] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[40], 0);
							c.prayerActive[40] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[41], 0);
							c.prayerActive[41] = false;	
						}	
						break;
						case 30:
						if(c.prayerActive[i] == false) 
						{
								
						}	
						break;
						case 31: //deflect summoning
						headIcon = true;
						if(c.prayerActive[i] == false) 
						{
							
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[32], 0);
							c.prayerActive[32] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[33], 0);
							c.prayerActive[33] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[34], 0);
							c.prayerActive[34] = false;	
							c.headIcon = 12;
						}	
						c.actionAssistant.requestUpdates();
						break;
						case 32: //deflect magic
						headIcon = true;
						if(c.prayerActive[i] == false) 
						{
							
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[31], 0);
							c.prayerActive[31] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[33], 0);
							c.prayerActive[33] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[34], 0);
							c.prayerActive[34] = false;	
							c.headIcon = 10;
						}	
						c.actionAssistant.requestUpdates();
						break;
						case 33: //deflect missiles
						headIcon = true;
						if(c.prayerActive[i] == false) 
						{
							
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[31], 0);
							c.prayerActive[31] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[32], 0);
							c.prayerActive[32] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[34], 0);
							c.prayerActive[34] = false;	
							c.headIcon = 11;
						}	
						c.actionAssistant.requestUpdates();
						break;
						case 34: //deflect melee
						headIcon = true;
						if(c.prayerActive[i] == false) 
						{
							
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[31], 0);
							c.prayerActive[31] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[32], 0);
							c.prayerActive[32] = false;	
							c.actionAssistant.sendFrame36(c.PRAYER_GLOW[33], 0);
							c.prayerActive[33] = false;	
							c.headIcon = 9;
						}	
						c.actionAssistant.requestUpdates();
						break;

				}
				
				if(!headIcon) 
				{
					if(c.prayerActive[i] == false) 
					{
						c.prayerActive[i] = true;
						c.actionAssistant.sendFrame36(c.PRAYER_GLOW[i], 1);		
						if (i == 10) 
						{
							//c.actionAssistant.sendMessage("Protect item activated. Use ::prot to confirm!");							
							//c.protOn = true;
							}	
					} 
					else 
					{
						if (i == 10) {
						//c.actionAssistant.sendMessage("Protect item deactivated. Use ::prot to confirm!");
						}
						c.prayerActive[i] = false;
						//c.protOn = false;
						c.actionAssistant.sendFrame36(c.PRAYER_GLOW[i], 0);
					}
					
				} 
				else 
				{
					if(c.prayerActive[i] == false) 
					{
						c.prayerActive[i] = true;
						c.actionAssistant.sendFrame36(c.PRAYER_GLOW[i], 1);
						//c.headIcon = c.PRAYER_HEAD_ICONS[i];
						prayUp();
						c.actionAssistant.requestUpdates();
					} 
					else 
					{
						c.prayerActive[i] = false;
						c.actionAssistant.sendFrame36(c.PRAYER_GLOW[i], 0);
						c.headIcon = -1;
						c.actionAssistant.requestUpdates();
					}
				}
				//prayUp();
				c.actionAssistant.requestUpdates();
			} 
			else 
			{
				c.actionAssistant.sendFrame36(c.PRAYER_GLOW[i],0);
				c.actionAssistant.sendFrame126("You need a @blu@Prayer level of "+c.PRAYER_LEVEL_REQUIRED[i]+" to use "+c.PRAYER_NAME[i]+".", 357);
				c.actionAssistant.sendFrame126("Click here to continue", 358);
				c.actionAssistant.sendFrame164(356);
			}
		} 
		else 
		{
			c.actionAssistant.sendFrame36(c.PRAYER_GLOW[i],0);
			c.actionAssistant.sendMessage("You have run out of prayer points!");
			if(c.isSkulled) 
			{
				c.skullIcon = 0;
			} 
			else 
			{
				c.skullIcon = -1;
			}
			c.actionAssistant.requestUpdates();
			if(c.sounds == 1) 
			{
				c.actionAssistant.frame174(437,050,000);
				c.prayOff = 0;
			}
		}
		if (i==10 && c.prayerActive[i]==true) c.protItem.put(System.currentTimeMillis(),"ON"); // ERIC
		else if (i==10 && c.prayerActive[i]==false) c.protItem.put(System.currentTimeMillis(),"OFF"); // ERIC
				
	}
	
public boolean goliathGloves(Client c, Client o) {

switch (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS])
{
case 22358:
case 22359:
case 22360:
case 22361:
if (Misc.random(100) == 0) {
o.gfx100(181);
o.freezeTimer = 5;
o.stopMovement();
o.getActionAssistant().Send("You have been frozen!");
o.frozenBy = c.playerId;
o.playerLevel[0] -= 7;
o.playerLevel[1] -= 7;
o.playerLevel[2] -= 7;
o.playerLevel[3] -= 7;
o.playerLevel[4] -= 7;
o.playerLevel[6] -= 7;
o.getActionAssistant().requestUpdates();
return true;
}
return false;
default:
return false;

}
}
public boolean swiftGloves(Client c, Client o) {

switch (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS])
{
case 22362:
case 22363:
case 22364:
case 22365:
if (Misc.random(100) == 0 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 15241) {
o.gfx100(181);
o.freezeTimer = 5;
o.stopMovement();
o.getActionAssistant().Send("You have been frozen!");
o.frozenBy = c.playerId;
return true;
}
return false;
default:
return false;

}
}
public boolean spellGloves(Client c, Client o) {
switch (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS])
{
case 22366:
case 22367:
case 22368:
case 22369:
if (Misc.random(100) == 0) {
o.gfx100(181);
o.playerLevel[0] *= 0.90;
o.playerLevel[1] *= 0.90;
o.playerLevel[2] *= 0.90;
o.getActionAssistant().requestUpdates();
return true;
}
return false;
default:
return false;

}}


	public void prayUp()
	{
		if(c.prayerActive[16] && c.isSkulled)
		{
			c.headIcon = 2;
			c.skullIcon = 0;
		}
		if(c.prayerActive[17] && c.isSkulled)
		{
			c.headIcon = 1;
			c.skullIcon = 0;
		} 	
		if(c.prayerActive[18] && c.isSkulled)
		{
			c.headIcon = 0;
			c.skullIcon = 0;
		}
		if(c.prayerActive[21] && c.isSkulled)
		{
			c.headIcon = 3;
			c.skullIcon = 0;
		}
		if(c.prayerActive[22] && c.isSkulled)
		{
			c.headIcon = 5;
			c.skullIcon = 0;
		}
		if(c.prayerActive[23] && c.isSkulled)
		{
			c.headIcon = 4;
			c.skullIcon = 0;
		}
		if(c.prayerActive[16] && !c.isSkulled)
		{
			c.headIcon = 2;
		}
		if(c.prayerActive[17] && !c.isSkulled)
		{
			c.headIcon = 1;
		} 	
		if(c.prayerActive[18] && !c.isSkulled)
		{
			c.headIcon = 0;
		}
		if(c.prayerActive[21] && !c.isSkulled)
		{
			c.headIcon = 3;
		}
		if(c.prayerActive[22] && !c.isSkulled)
		{
			c.headIcon = 5;
		}
		if(c.prayerActive[23] && !c.isSkulled)
		{
			c.headIcon = 4;
		}
		if(c.playerLevel[5] <= 0) 
		{
			if(c.prayOff == 1) 
			{
				c.actionAssistant.sendMessage("You have run out of prayer points!");
				c.actionAssistant.resetPrayers();
				if(c.sounds == 1) 
				{
					c.actionAssistant.frame174(437,050,000);
				}
				c.prayOff = 0;
			}
			if(c.isSkulled) 
			{
				c.skullIcon = 0;
			}
		}
	}
	/*  public boolean getCombatDifference(int combat1, int combat2) {
	 if (((combat + c.wildLevel >= pcombat) && (pcombat >= combat)) || ((combat - wildLevel <= pcombat) && (pcombat <= combat))) {
	 return true;
	 } else {
	 return false;
	 }
	 }*/
	
	public int getCombatDifference(int combat1, int combat2) 
	{
	
		return (Math.abs(combat1 - combat2));
		// if(combat1 > combat2) {
		// return (combat1 - combat2);
		// }
		// if(combat2 > combat1) {
		// return (combat2 - combat1);
		// }	
		// return 0;

	}
	public boolean checkNpcReqs() 
	{	
		NPC npc = Server.getNpcManager().getNPC(c.npcIndex);
		if(!c.inMulti()) 
		{	// single combat zones npcs
		//c.getActionAssistant().Send("fighting "+c.isFighting);
			//client.getActionAssistant().Send("under attack "+npc.underAttack);
			if(npc.underAttack2 && npc.killerId != 0 && npc.killerId != c.playerId && !c.inMulti())
			{
				c.getActionAssistant().Send("Someone else is already fighting your opponent.");
				c.faceUpdate(c.npcIndex);
				c.getCombat().resetAttackVsNpc();
				return false;
			}		
			if((c.isFighting || ((c.underAttackByNPC != npc.npcId) && c.underAttackByNPC != 0)) && !c.inMulti() && npc.killerId != c.playerId) 
			{
				c.getActionAssistant().sendMessage("You are already in combat.");
				c.stopMovement();
				c.getCombat().resetAttackVsNpc();
				return false;
			}				
		}
		return true;
	}
	public boolean checkReqs() 
	{
		Client o = (Client) PlayerManager.getSingleton().getPlayers()[c.playerIndex];
		if(o == null) 
		{
		c.inAir = false;
		c.getCombat().resetAttack3();
			return false;
		}
		if (c.heightLevel != o.heightLevel) {
		return false;
		}
		if(!c.usingMagic && c.duelStatus != 5 && o.inDuelArena() ) 
		{
			if(c.arenas() || c.duelStatus == 5) 
			{
				c.actionAssistant.sendMessage("You can't challenge inside the arena!");
				c.inAir = false;
		c.getCombat().resetAttack3();
				return false;
			}
			c.actionAssistant.requestDuel(c.playerIndex);
			c.inAir = false;
		c.getCombat().resetAttack3();
			return false;
		}
		if (Config.CastleWars) {
			if ((c.isInZamSafe() || c.isInSaraSafe() )&& c.heightLevel == 1) {
				c.actionAssistant.sendMessage("You can't attack from a safe zone!");
				c.getCombat().resetAttack3();
				return false;
			}
			if ((o.isInZamSafe() || o.isInSaraSafe()) && o.heightLevel == 1) {
				c.actionAssistant.sendMessage("You can't attack a player in a safe zone!");
				c.getCombat().resetAttack3();
				return false;
			}
			//return true;
			
			if(Server.getFightPits().isInFightPitsGame(c) && Server.getFightPits().isInFightPitsGame(o))
				return true;
		}
		if(c.duelStatus == 5 && o.duelStatus == 5){
			if(o.duelingWith == c.getId()){
				for (int i = 1; i < 22; i++)  
			{
						if (!c.duelRule[i] && o.duelRule[i]) {
						try {
											BanProcessor.banUser(c.playerName,"duel bug abuse");
											c.kick();
											c.disconnected = true;
										} catch (IOException e) {
											e.printStackTrace();
										}
						}
			}
				return true;
			} else {
				c.actionAssistant.sendMessage("This isn't your opponent!");
				c.inAir = false;
				c.getCombat().resetAttack3();
				return false;
			}
		}
		if(c.inred && o.inblue || c.inblue && o.inred){
			return true;
		}	
		if (c.inred && o.inred || c.inblue && o.inblue){
			c.actionAssistant.sendMessage("Attack your enemies!");
			c.getCombat().resetAttack();
			c.inAir = false;
			c.getCombat().resetAttack3();
			return false;
		}
		if(c.inWild()) 
		{
			if(!o.inWild()) 
			{
				c.actionAssistant.sendMessage("That player is not in the wilderness.");
				c.stopMovement();
				c.inAir = false;
				c.getCombat().resetAttack3();
				return false;
			}
		else if(Config.COMBAT_LEVEL_DIFFERENCE) 
			{
					int combatDif1 = c.getCombat().getCombatDifference(c.getCombatLevel(), o.getCombatLevel());
					if((combatDif1 > c.wildLevel || combatDif1 > o.wildLevel) && (!Config.CastleWars || (Config.CastleWars && !Server.getCastleWars().isSaraTeam(c) && !Server.getCastleWars().isZammyTeam(c)))) 
					{
						c.actionAssistant.sendMessage("Your combat level difference is too great to attack that player here.");
						c.stopMovement();
						c.inAir = false;
		c.getCombat().resetAttack3();
						return false;
					}
			}
		}
		else {
			c.actionAssistant.sendMessage("You are not in the wilderness.");
			c.stopMovement();
			c.inAir = false;
		c.getCombat().resetAttack3();
			return false;
		}
		
		
		// if(c.edgeWild()){
		// int combatDif1 = c.getCombat().getCombatDifference(c.getCombatLevel(), o.getCombatLevel());
		// if(combatDif1 > c.wildLevel || combatDif1 > o.wildLevel) {
		// c.actionAssistant.sendMessage("Your combat level difference is too great to attack that player here.2");
		// c.stopMovement();
		// c.getCombat().resetAttack2();
		// return false;
		// }
		// }			
		
		if(Config.SINGLE_AND_MULTI_ZONES) 
		{
			if(!o.inMulti()) 
			{	// single combat zones players
				if(o.underAttackBy != c.playerId  && o.underAttackBy != 0) 
				{
					c.actionAssistant.sendMessage("That player is already in combat.");
					c.stopMovement();
					c.inAir = false;
		c.getCombat().resetAttack3();
					return false;
				}
				if(o.playerId != c.underAttackBy && c.underAttackBy != 0) 
				{
					c.getActionAssistant().sendMessage("You are already in combat.");
					c.stopMovement();
					c.inAir = false;
		c.getCombat().resetAttack3();
					return false;
				}
				if(c.isFighting) 
				{
					c.getActionAssistant().sendMessage("You are already in combat.");
					c.stopMovement();
					c.inAir = false;
		c.getCombat().resetAttack3();
					return false;
				}				
			}	
		}
		return true;
	}
	public void reducePrayerLevel() 
	{
		// if(CheatProcessor.checkCheat(c.playerName)){
		// c.getActionAssistant().refreshSkill(5);
		// c.getActionAssistant().sendFrame126("@gre@ "+c.playerLevel[5]+" / "+c.getActionAssistant().getLevelForXP(c.playerXP[5]), 13356);
		// c.getActionAssistant().sendFrame126("@or1@ "+c.playerLevel[5]+" / "+c.getActionAssistant().getLevelForXP(c.playerXP[5]), 25104); }
		if(c.playerRights > 1 && c.prayer == 0) 
		{
			c.getActionAssistant().refreshSkill(5);
			c.getActionAssistant().sendFrame126("@gre@ "+c.playerLevel[5]+" / "+c.getActionAssistant().getLevelForXP(c.playerXP[5]), 13356);		
			c.getActionAssistant().sendFrame126("@or1@ "+c.playerLevel[5]+" / "+c.getActionAssistant().getLevelForXP(c.playerXP[5]), 25104);
		} 
		else 
		{
			c.prayerDelay = System.currentTimeMillis();
			if(c.playerLevel[5] - 1 > 0) 
			{
				c.playerLevel[5]--;
			} 
			else 
			{
				c.getActionAssistant().sendMessage("You have run out of prayer points!");
				if(c.sounds == 1) 
				{
					c.getActionAssistant().frame174(437,050,000);
				}
				c.prayOff = 0;
				c.playerLevel[5] = 0;
				c.actionAssistant.resetPrayers();
				c.prayerId = -1;	
			}
			c.getActionAssistant().refreshSkill(5);
			c.getActionAssistant().sendFrame126("@gre@ "+c.playerLevel[5]+" / "+c.getActionAssistant().getLevelForXP(c.playerXP[5]), 13356);
			c.getActionAssistant().sendFrame126("@or1@ "+c.playerLevel[5]+" / "+c.getActionAssistant().getLevelForXP(c.playerXP[5]), 25104);
		} 
	}
	
	public int getPrayerDelay() 
	{
		c.usingPrayer = false;
		int delay = 10000;	
		for(int i = 0; i < c.prayerActive.length; i++) 
		{
			if(c.prayerActive[i] == true) 
			{
				c.usingPrayer = true;
				delay -= c.PRAYER_DRAIN_RATE[i];
			}
		}
		for (int j = 0; j < c.curseData.length; j++) {
                        if (c.curseActive[j]) {
                                delay -= c.curseData[j]/20;
                                c.usingPrayer = true;
                        }
                }
		delay += bonus[11]*500;
		if (c.killStreak >= 15 && c.killStreak < 20) {
			delay = delay/2;
		}
		return delay;
		
	}
	public int calculateMagicAttack() {
		int magicBonus = bonus[3];
		int magicLevel = c.playerLevel[6];
		double power = 1.800;
		double amount = 0.80;
		if (magicBonus >= 150) { // equivalent of max mage w/ ancient staff
			amount = 6.20;
		} 
		else if (magicBonus >= 115) { // equivalent of max mage w/ wand or better
			amount = 5.50;
		}
		else if (magicBonus >= 105) { // equivalent of max mage w/ ancient staff
			amount = 3.25;
		} 
		else if (magicBonus >= 90) { // equivalent of max mage w/whip or mystic + ancient staff
			amount = 1.65;
		} 
		else if (magicBonus >= 80) { // equivalent of max mage w/ zerker+whip
			amount = 1.25;
		} 
		power += (magicBonus *= amount) + (magicLevel *= 0.0120);
		 if(c.prayerActive[4]) {
			power *= 1.05;
		 }
		 if(c.prayerActive[12]) {
			power *= 1.10;
		 }
		 if(c.prayerActive[20]) {
			power *= 1.15;
		 }	
		if (c.getCombatFormulas().voidMage()) {
			power *= 1.30;
		}
		if (c.getCombatFormulas().eliteVoidMage()) {
			power *= 1.50;
		}
		if (c.curseActive[12] || c.magicInc < 5) {
			power += power * c.magicInc/100d;
		}
		//power = power * 3;
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] > -1) {
			ItemDefinition def = Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT]);
			if (def.getBonus(3) < 0) {
				power = power*0.85;
			}
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] > -1) {
			ItemDefinition	def = Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST]);
			if (def.getBonus(3) < 0) {
				power = power*0.10;
			}
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] > -1) {
			ItemDefinition	def = Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS]);
			if (def.getBonus(3) < 0) {
				power = power*0.15;
			}
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_FEET] > -1) {
			ItemDefinition	def = Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_FEET]);
			if (def.getBonus(3) < 0) {
				power = power*0.90;
			}
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] > -1) {
			ItemDefinition	def = Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD]);
			if (def.getBonus(3) < 0) {
				power = power*0.75;
			}
		}
		if (c.combatLevel < 105) { power = power * 1.60; }
		//c.getActionAssistant().sendMessage("Magic attack Power Total: "+(int)Math.floor(power) );
		return (int)Math.floor(power);		
	
	}
	
	public int calculateMagicAttackNPC(){
		int attackLevel = c.playerLevel[6];
		if (c.getCombatFormulas().voidMage())
			attackLevel += c.getLevelForXP(c.playerXP[6]) * 0.2;
		if (c.getCombatFormulas().eliteVoidMage())
			attackLevel += c.getLevelForXP(c.playerXP[6]) * 0.35;
		if (c.prayerActive[4])
			attackLevel *= 1.05;
		else if (c.prayerActive[12])
			attackLevel *= 1.10;
		else if (c.prayerActive[20])
			attackLevel *= 1.15;
		
		if (Config.killStreak && (c.killStreak == 7 || c.killStreak >= 25)) {
		attackLevel *= 1.08;
		}
		if (bonus[3] > 130) {
			return (int) (attackLevel + bonus[3]*5.75);
		}
		else if (bonus[3] > 113) {
			return (int) (attackLevel + (bonus[3] * 5.2));
		}
		else if (bonus[3] > 103) {
			return (int) (attackLevel + (bonus[3] * 4.5));
		}
		else if (bonus[3] < 45) {
			return (int) (attackLevel + (bonus[3] * 2.25));
		}
		else if (bonus[3] < 60) {
			return (int) (attackLevel + (bonus[3] * 3));
		}
		
		return (int) (attackLevel + (bonus[3] * 4.0));
	}
	
		public int calculateMagicDefence() {
		int magicBonus = bonus[8];
		int magicLevel = c.playerLevel[6];
		int defenceLevel = c.playerLevel[1];
		double power = 1.100;
		double amount = 0.85;
		if (magicBonus >= 200) { // any higher
			amount = 2.50;
		}
		else if (magicBonus >= 173) { // any higher
			amount = 2.00;
		}
		else if (magicBonus >= 150) { // equivalent of max mage w/ karil
			amount = 1.60;
		} 
		else if (magicBonus >= 120) { // equivalent of max mage w/ahrim
			amount = 1.40;
		} 
		else if (magicBonus > 100) { // equivalent of max mage w/ zerker+whip
			amount = 1.0;
		}
		else if (magicBonus >= 90) {
			amount = 0.95;
		}
		power += (magicLevel * 0.1) + (magicBonus * amount) + (defenceLevel * 0.1);
		if (c.getCombatFormulas().voidMage()) {
			power *= 1.30;
		}
		if (c.getCombatFormulas().eliteVoidMage()) {
			power *= 1.50;
		}	
		if (c.prayerActive[0]) {
			power *= 1.05;
		} else if (c.prayerActive[5]) {
			power *= 1.10;
		} else if (c.prayerActive[13]) {
			power *= 1.15;
		} else if (c.prayerActive[24]) {
			power *= 1.20;
		} else if (c.prayerActive[25]) {
			power *= 1.25;
		}
		//power *= 1.50;
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] > -1) {
			ItemDefinition def = Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT]);
			if (def.getBonus(8) < 0) {
				power = power*0.85;
			}
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] > -1) {
			ItemDefinition	def = Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST]);
			if (def.getBonus(8) < 0) {
				power = power*0.10;
			}
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] > -1) {
			ItemDefinition	def = Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS]);
			if (def.getBonus(8) < 0) {
				power = power*0.20;
			}
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_FEET] > -1) {
			ItemDefinition	def = Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_FEET]);
			if (def.getBonus(8) < 0) {
				power = power*0.90;
			}
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] > -1) {
			ItemDefinition	def = Server.getItemManager().getItemDefinition(c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD]);
			if (def.getBonus(8) < 0) {
				power = power*0.70;
			}
		}
		power = power * 1.75;
		//c.getActionAssistant().sendMessage("Magic Defence Power Total: "+(int)Math.floor(power) );
		return (int)Math.floor(power);	
		}
	
	public int calculateMeleeMaxHit() 
	{
		double maxHit = 0;
		int strBonus = bonus[10];
		int strength = c.playerLevel[SkillConstants.STRENGTH];

		if (c.prayerActive[1]) 
		{
			strength += (strength*0.05);
		} 
		else if (c.prayerActive[6]) 
		{
			strength += (strength*0.10);
		} 
		else if (c.prayerActive[14]) 
		{
			strength += (strength*0.15);
		} 
		else if (c.prayerActive[24]) 
		{
			strength += (strength*0.20);
		} 
		else if (c.prayerActive[25]) 
		{
			strength += (strength*0.25);
		}
		if(c.curseActive[14]) { // Leech Str
                        strength += c.strInc;
                } else if(c.curseActive[19]) { // turmoil
                        strength += (int)(c.getActionAssistant().getLevelForXP(c.playerXP[2]) * 0.23 + c.getStr);
                }
                if (c.strInc < 0 && !c.curseActive[14])
                        strength += (int)(c.getActionAssistant().getLevelForXP(c.playerXP[2]) * (c.strInc / 100d));

		if (c.fightType == 0 || c.fightType == 2) 
		{
			maxHit += ((double) ((double) strBonus + strength) / ((double) 6.8275862068965517241388460344828));
			//maxHit += (double) (1.05 + (double) ((double) (strBonus * strength) * 0.00175));
		} 
		else if (c.fightType == 1) 
		{
			maxHit += ((double) ((double) strBonus + strength) / ((double) 6.6551724138846034482758620689655));
			//maxHit += (double) (1.35 + (double) ((double) (strBonus * strength) * 0.00175));
		} 
		else if (c.fightType == 5) 
		{
			maxHit += ((double) ((double) strBonus + strength) / ((double) 6.6551724138846034482758620689655));
		} 
		else if (c.fightType == 3) 
		{
			maxHit += ((double) ((double) strBonus + strength) / ((double) 6.7586206896551724138846034482759));
			//maxHit += (double) (1.15 + (double) ((double) (strBonus * strength) * 0.00175));
		}
		
		/***
		*
		* Dharok HP Bug Fixed - Chachi 07/08/2011
		* When hp was over the real hp lvl it would cause a negative hit now
		* it will just assume you are at your max HP lvl
		*/
		if (c.getCombatFormulas().fullDharokEquipped()) 
		{
			int realHp = c.getActionAssistant().getLevelForXP(c.playerXP[SkillConstants.HITPOINTS]);
			maxHit += (c.getActionAssistant().getLevelForXP(c.playerXP[SkillConstants.HITPOINTS]) - c.playerLevel[SkillConstants.HITPOINTS]) / 3;
			if(maxHit < 0) {
				maxHit = 0;
				maxHit += realHp / 3;
			}
			
		}
		if(c.getCombatFormulas().voidMelee()){
			maxHit += Misc.random(5);
		}
		if(c.getCombatFormulas().eliteVoidMelee()){
			maxHit += Misc.random(10);
		}
	if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 19784) { maxHit += 5; }
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 7959 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18353 || 
			c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18351 ||c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 16909 ||
			c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 16425){
			maxHit += Misc.random(6);
		} else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]==14484) {
			maxHit += 6;
		}
		if (maxHit > 40 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]== 11730) {
			maxHit = 40;
		}
	
		if(c.bonusDamage > 0 && c.lastSpecialUsedWeapon == c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]){
			maxHit = maxHit * c.bonusDamage;
			c.bonusDamage = 0;
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]== 14484) {
				if (maxHit >42) {
					maxHit = 42;
				}
			}			
		}
		c.bonusDamage = 0;
		if (maxHit > 68 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]== 7901) {
		maxHit = 68;
		}
		return (int)Math.floor(maxHit);
	}

	public int getHighestAttBonus() {
		int highestBonus = 0;
		for (int i = 0; i < 3; i++) {
			if (bonus[i] > highestBonus) {
				highestBonus = bonus[i];
			}
		}
		return highestBonus;
	}

	public int calculateMeleeAttack(){
		int attBonus = getHighestAttBonus();
		int attLevel = c.playerLevel[0];
		double attack = (attLevel + attBonus) * 0.01365;
		double amount = 1.260;
		attack *= (attLevel * (attack * 0.12)) + (attBonus * amount) * (attack * 0.009);
		if (c.prayerActive[2]) {
			attack *= 1.05;
		} else if (c.prayerActive[7]) {
			attack *= 1.10;
		} else if (c.prayerActive[15] || c.prayerActive[24]) {
			attack *= 1.15;
		} else if (c.prayerActive[25]) {
			attack *= 1.20;
		}
				else if (c.curseActive[10] || c.attInc < 5) {
						int temp = c.getActionAssistant().getLevelForXP(c.playerXP[0]);
						double CurseBonus = (c.attInc+5)/100d;    
						attack += c.getActionAssistant().getLevelForXP(c.playerXP[0]) * (CurseBonus);    								
                } else if (c.curseActive[19]) { // turmoil
                        attack += c.getActionAssistant().getLevelForXP(c.playerXP[0]) * (0.15 + (c.getAtt / 100d));
                }
                if (c.getAtt < 0 && !c.curseActive[10])
                        attack += c.getActionAssistant().getLevelForXP(c.playerXP[0]) * (c.getAtt / 100d);
		if (c.getCombatFormulas().voidMelee()) {
			attack *= 1.10;
		}
		if (c.getCombatFormulas().eliteVoidMelee()) {
			attack *= 1.30;
		}		
		if(	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11696 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11700 ||  
			c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11694 ||  c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11698){
			attack += (attack * 60 / 85 );
		/*} else if (c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 11694) {
			attack += (attack * 30 / 120);*/
		} else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 7959 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18353 || 
			c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18351 ||c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 16909 ||c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18369 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1434){
			attack += (attack * 60 / 120 );
		} else if ((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]== 1215 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] ==1231 
			|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]==5698) && c.combatLevel > 115 ){
			attack += attack*0.15;
		} else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]==14484 && !c.dHit) {
		 attack -= attack*0.65;
		}		
		if ((c.killStreak == 9|| c.killStreak >= 25) && Config.killStreak) {
				attack += attack *0.05;
			}
		//if (c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 19784)  { attack += attack*0.5; }
		//c.getActionAssistant().Send("attack: " +attack);
		return (int)Math.floor(attack);
 
				//OLD Attack formula that was shit - Chachi
	/*	int attack = 0;
		int attackBonus = 0;
		
		switch(c.fightType) 
		{
			case 0:
				attackBonus = bonus[0];
				break;
			
			case 1:
				attackBonus = bonus[1];
				break;
			
			case 2:
				attackBonus = bonus[2];
				break;
			
			case 3:
				attackBonus = bonus[1];
				break;
		}
		int attacklvl = c.playerLevel[0]; 
		attackBonus = attackBonus;
		attacklvl = attacklvl;

		attack = attackBonus + attacklvl;
		
		if(c.prayerActive[2]) 
		{
			attack += (attack * 5 / 120);
		}	
		if(c.prayerActive[7]) 
		{
			attack += (attack * 10 / 120);
		}
		if(c.prayerActive[15]) 
		{
			attack += (attack * 15 / 120);
		}
		if(c.prayerActive[24]) 
		{
			attack += (attack * 15 / 120);
		}
		if(c.prayerActive[25]) 
		{
			attack += (attack * 15 / 120);
		}		// pat
		if(	c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 11696 || c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 11700 ||  
			c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 11694 ||  c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 11698){
			attack += (attack * 60 / 120 );
		}
		else if (c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 7959 || c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 18353 || 
			c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 18351 ||c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 16909 ||c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 18369 || c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 1434) 
		{
			attack += (attack * 60 / 120 );
		}
		else if ((c.playerEquipment[PlayerConstants.PLAYER_WEAPON]== 1215 || c.playerEquipment[PlayerConstants.PLAYER_WEAPON] ==1231 
			|| c.playerEquipment[PlayerConstants.PLAYER_WEAPON]==5698) && c.combatLevel > 115 )
		{
			attack += attack*0.15;
		}
		else if (c.playerEquipment[PlayerConstants.PLAYER_WEAPON]==14484 && !c.dHit) {
		 attack -= attack*0.65;
		}
		
		return attack;*/
	}	
		
	
	
	public int bestMeleeDef()
	{
		if(bonus[5] > bonus[6] && bonus[5] > bonus[7])
			return 5;
		if(bonus[6] > bonus[5] && bonus[6] > bonus[7])
			return 6;
		return bonus[7] <= bonus[5] || bonus[7] <= bonus[6] ? 5 : 7;
	}
	private int getHighestDefBonus() {
		int highestBonus = 0;
		for (int i = 5; i < 8; i++) {
			if (bonus[i] > highestBonus) {
				highestBonus = bonus[i];
			}
		}
		return highestBonus;
	}
	
	public int calculateMeleeDefence(){
		int defBonus = getHighestDefBonus();
		int defLevel = c.playerLevel[1];
		double def = (defLevel + defBonus) * 0.0085; // was 0.0095
		double amount = 1.160;
		if (defBonus > 180) {
			amount = 1.268;
		} else if (defBonus > 290) {
			amount = 2.080;
		} else if (defBonus > 355) {
			amount = 3.080;
		}
		def *= (defLevel * (def * 0.10)) + (defBonus * amount) * (def * 0.012);
		if (c.prayerActive[0]) {
			def *= 1.05;
		} else if (c.prayerActive[5]) {
			def *= 1.10;
		} else if (c.prayerActive[13]) {
			def *= 1.15;
		} else if (c.prayerActive[24]) {
			def *= 1.20;
		} else if (c.prayerActive[25]) {
			def *= 1.25;
		}
		else if (c.curseActive[13] || c.defInc < 5) {
                        def += c.getLevelForXP(c.getActionAssistant().getLevelForXP(c.playerXP[1])) * (c.defInc / 100d);
                } else if (c.curseActive[19]) {
                        def += c.getLevelForXP(c.getActionAssistant().getLevelForXP(c.playerXP[1])) * 0.15 + c.getDef;
                }

				
		if (c.getCombatFormulas().fullVeracEquipped()) {
			def = (defLevel * 0.890); 
		}
		if (c.killStreak == 11) {
			def += def *0.1;
		}
		if (c.killStreak >= 25) {
			def -= def*0.1;
		}
		//c.getActionAssistant().Send("def: " +def);
		return (int)Math.floor(def);
	/* Old shit ass def formula - CHachi	
		int def = 0;
		int defBonus = bonus[5] + bonus[6] + bonus[7];
		int deflvl = c.playerLevel[1];
		
		defBonus = defBonus / 5;
		deflvl = deflvl / 5;
		def = defBonus + deflvl;
		
		if(c.prayerActive[0]) 
		{
			def += (def * 5 / 70);
		}	
		if(c.prayerActive[5]) 
		{
			def += (def * 10 / 70);
		}
		if(c.prayerActive[13]) 
		{
			def += (def * 15 / 70);
		}
		if(c.prayerActive[24]) 
		{
			def += (def * 20 / 70);
		}
		if(c.prayerActive[25]) 
		{
			def += (def * 25 / 70);
		}
		return def;*/
	}
	public int calculateRangeAttack() 
	{
		int attackLevel = c.playerLevel[4];
		//attackLevel *= c.specAccuracy;
		if (c.prayerActive[3])
			attackLevel *= 1.05;
		else if (c.prayerActive[11])
			attackLevel *= 1.10;
		else if (c.prayerActive[19])
			attackLevel *= 1.15;
		//dbow spec
		if (c.getCombatFormulas().voidRange())
			attackLevel += c.getLevelForXP(c.playerXP[4]) * 1.3;
		if (c.getCombatFormulas().eliteVoidRange())
		attackLevel += c.getLevelForXP(c.playerXP[4]) * 2.5;
		
		 if (c.curseActive[11])
                        attackLevel *= (1 + (c.getRange / 100));
if (c.getRange < 0 && !c.curseActive[11])
                        attackLevel *= (1 + (c.getRange / 100));
		if ((c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4827 || (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] >= 15701 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] <= 15704))|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] ==15241 ||c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13879|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13883) 
		{
			attackLevel += attackLevel*0.75;
		}
		else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 18357) {
			if (c.combatLevel <= 105) {
				attackLevel -= attackLevel*0.40;
			}
		attackLevel -= attackLevel * 0.18; //was 25
		}
		else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 9185) 
		{
			attackLevel += attackLevel*0.05;
		}
		if (c.killStreak >= 25) {
			attackLevel += attackLevel * 0.1;
		}
		else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 861) 
		{
			return (int) (attackLevel + (bonus[4]*0.90)); //Before 1.15
		}      
if ((c.DarkBowSpec || c.DarkBowSpec2) && (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4827 || (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] >= 15701 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] <= 15704))) {
			attackLevel += attackLevel*1.25;
			}
		return (int) (attackLevel + (bonus[4] * 2.25));
		
		/*int rangeAttack = 0;	
		 int rangeAttackBonus = (bonus[4]/3);//4
		 int rangeAttackLevel = (c.playerLevel[4]/4);	
		 rangeAttack = rangeAttackLevel + rangeAttackBonus;

		 if(c.prayerActive[3]) {
		 rangeAttack += (rangeAttack*0.05);
		 }
		 if(c.prayerActive[11]) {
		 rangeAttack += (rangeAttack*0.10);
		 }
		 if(c.prayerActive[19]) {
		 rangeAttack += (rangeAttack*0.15);
		 }	
		 if(voidRange())
		 {
		 rangeAttack += (rangeAttack*0.10);
		 }
		 rangeAttack += rangeAttack*0.10;
		 return rangeAttack;*/
	}
	
	public int calculateRangeDefence() 
	{
		int defenceLevel = c.playerLevel[1];
		if (c.prayerActive[0]) 
		{
			defenceLevel += c.getLevelForXP(c.playerXP[1]) * 0.05;
		} 
		else if (c.prayerActive[5]) 
		{
			defenceLevel += c.getLevelForXP(c.playerXP[1]) * 0.1;
		} 
		else if (c.prayerActive[13]) 
		{
			defenceLevel += c.getLevelForXP(c.playerXP[1]) * 0.15;
		} 
		else if (c.prayerActive[24]) 
		{
			defenceLevel += c.getLevelForXP(c.playerXP[1]) * 0.2;
		} 
		else if (c.prayerActive[25]) 
		{
			defenceLevel += c.getLevelForXP(c.playerXP[1]) * 0.25;
		}
		else if (c.curseActive[13]) { // turmoil
                        defenceLevel += c.getActionAssistant().getLevelForXP(c.playerXP[1]) * (c.getDef / 100);
                } else if (c.curseActive[19]) { // turmoil
                        defenceLevel += c.getActionAssistant().getLevelForXP(c.playerXP[1]) * 0.15 + c.getDef;
                }
                          if (c.getDef < 0 && !c.curseActive[13])
                        defenceLevel += c.getActionAssistant().getLevelForXP(c.playerXP[1]) * (c.getDef / 100);
						
		return (int) (defenceLevel + bonus[9]*1.75);
		
		/*int def = 0;
		 int defBonus = bonus[9]; 
		 int deflvl = c.playerLevel[4]; 
		 defBonus = defBonus / 4;//3
		 deflvl = deflvl / 5;
		 def = defBonus + deflvl;

		 if(c.prayerActive[0]) {
		 def += (def * 5 / 100);
		 }	
		 if(c.prayerActive[5]) {
		 def += (def * 10 / 100);
		 }
		 if(c.prayerActive[13]) {
		 def += (def * 15 / 100);
		 }
		 if(c.prayerActive[24]) {
		 def += (def * 15 / 100);
		 }
		 if(c.prayerActive[25]) {
		 def += (def * 15 / 100);
		 }
		 return def;*/
	}
	
	public int calculateRangeMaxHit() 
	{
		double d = 0.0D;
		double d1 = c.playerLevel[4];
		int i = c.getActionAssistant().getLevelForXP(c.playerXP[4]);
		int j = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
		int k = c.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS];

		if (c.getCombatFormulas().voidRange()) 
		{
			d1 += (double)i * 0.10000000000000001D;
		}		
		if(c.prayerActive[3]) 
		{
			d1 += (d1 * 0.05);
		}	
		if(c.prayerActive[11]) 
		{
			d1 += (d1 * 0.10);
		}
		if(c.prayerActive[19]) 
		{
			d1 += (d1 * 0.15);
		}
		d += 1.05D + d1 * 0.00125D;
		d += d1 * 0.11D;

		if (j == 800 || j == 806|| j == 870 || j == 5654|| j == 5661|| j == 5628|| j == 5635|| j == 812|| j == 825 || j == 864) 
		{
			d *= 0.69999999999999996D;
		} 
		else if (j == 13883 ||
			j == 13879  ) 
		{
			d *= 2.5D;
		}
		else if (j == 15421) {
		d *= 2.5D;
		}
		else if (j == 801 || j == 807 || j == 5655|| j == 5662|| j == 871|| j == 820 || j == 863|| j == 813|| j == 5629|| j == 5636) 
		{
			d *= 0.72999999999999997D;
		} 
		else if (j == 802 || j == 808 || j == 872|| j == 5656|| j == 5663|| j == 5665|| j == 5658|| j == 874|| j == 827 || j == 865|| j == 814|| j == 5637|| j == 5638|| j == 3093|| j == 5630|| j == 5631) 
		{
			d *= 0.79000000000000004D;
		} 
		else if (j == 803 || j == 809 || j == 828 || j == 873|| j == 5657|| j == 866|| j == 5664|| j == 815|| j == 5632|| j == 5639) 
		{
			d *= 0.83999999999999997D;
		} 
		else if (j == 804 || j == 810 || j == 829 || j == 875|| j == 5659|| j == 867|| j == 5666|| j == 816|| j == 5633|| j == 5640) 
		{
			d *= 0.98999999999999999D;
		} 
		else if (j == 805 || j == 811 || j == 830 || j == 876|| j == 5660|| j == 5667|| j == 817|| j == 5634|| j == 5641) 
		{
			d *= 1.24D;
		} 
		else if (j == 868) {
		d *= 1D;
		}
		else if (j == 6522) 
		{
			d *= 1.6399999999999999D;
		} 
		else if (j == 15241 || j == 18357) {
			if (c.combatLevel <= 105) {
				d *= 2.50D; //2.15
			}
			else {
				d *= 2.85D; //2.65
			}
		}
		else if (j == 4212 || j == 4214) 
		{
			d *= 2.25D;
		} 
		else if (k == 7858 ||k == 882 || k == 883) 
		{
			d *= 1.042D;
		} 
		else if (k == 7859 ||k == 884 || k == 885) 
		{
			d *= 1.044D;
		} 
		else if (k == 7867 ||k == 7860 ||k == 886 || k == 887) 
		{
			d *= 1.1339999999999999D;
		} 
		else if (k == 7868 ||k == 7866 ||k == 7865 ||k == 7864 ||k == 7861 ||k == 888 || k == 889) 
		{
			d *= 1.2D;
		} 
		else if (k == 7870 ||k == 7869 ||k == 7862 ||k == 890 || k == 891) 
		{
			d *= 1.3500000000000001D;
		} 
		else if (k == 7863 ||k == 892 || k == 893) 
		{
			d *= 1.7500000000000001D;
		} 
		else if (k == 4740) 
		{
			d *= 1.95D;
		} 
		else if (k == 8065) 
		{
			d *= 1.5D;
		} 
		else if(k == 11212 && ((j >= 15701 && j <= 15704) || j == 4827) )
		{
			d *= 1.8000000000000001D;		
		} 
		else if(k == 892 && (j == 4827 || (j >= 15701 && j <= 15704))) 
		{
			d *= 1.7500000000000001D;				
		} 
		else if (k == 7872 ||k == 7871 ||j == 4827 || (j >= 15701 && j <= 15704)) 
		{
			d *= 1.7500000000000001D;
		} 
		else if (j == 15243) {
		d *= 2.1500000000000001D;
		}
		else if (k == 7990) 
		{
			d *= 2.6000000000000001D;
		} 
		else if (j == 9185) 
		{
			d *= 2.3000000000000001D;
		}
		else if (j == 9185) {
			d *= 2.5000000000000001D;
		}
		else if (j == 4220 || 
			j == 4221 || 
			j == 4222 || 
			j == 4223) 
		{
			d *= 2.45D;	// godbows nerf
		}
		else if (j == 20171) {
		
			d *= 2.75D;
		}
		if(c.bonusDamage > 0) 
		{
			d += (d*c.bonusDamage/3);
			c.bonusDamage = 0;
		}
		return (int)Math.floor(d);
	}		
 
	public void slayCheck(int npc){

		if (npc == 1832 && c.playerLevel[18] < 7){
			c.getActionAssistant().Send("You must be 7 Slayer to slay Cave Bugs.");
			return;
		}		
		if (npc == 1601 && c.playerLevel[18] < 10){
			c.getActionAssistant().Send("You must be 10 Slayer to slay Cave Crawlers.");
			return;
		}		
		if (npc == 1612 && c.playerLevel[18] < 15){
			c.getActionAssistant().Send("You must be 15 Slayer to slay Banshee's.");
			return;
		}	
		if (npc == 1831 && c.playerLevel[18] < 17){
			c.getActionAssistant().Send("You must be 17 Slayer to slay Cave Slime.");
			return;
		}		
		if (npc == 1622 && c.playerLevel[18] < 20){
			c.getActionAssistant().Send("You must be 20 Slayer to slay Rock Slugs.");
			return;
		}	
		if (npc == 1620 && c.playerLevel[18] < 25){
			c.getActionAssistant().Send("You must be 25 Slayer to slay Cockatice's.");
			return;
		}
		if (npc == 1633 && c.playerLevel[18] < 30){
			c.getActionAssistant().Send("You must be 30 Slayer to slay Pyrefiends.");
			return;
		}				
		if (npc == 1632 && c.playerLevel[18] < 55){
			c.getActionAssistant().Send("You must be 55 Slayer to slay Turoths.");
			return;
		}				
		if (npc == 1609 && c.playerLevel[18] < 70){
			c.getActionAssistant().Send("You must be 70 Slayer to slay Kurasks.");
			return;
		}				
		if (npc == 2783 && c.playerLevel[18] < 90){
			c.getActionAssistant().Send("You must be 90 Slayer to slay Dark beasts.");
			return;
		}
		if (npc == 1615 && c.playerLevel[18] < 85){
			c.getActionAssistant().Send("You must be 85 Slayer to slay Abyssal demons.");
			return;
		}
		if (npc == 3794 && c.playerLevel[18] < 90){
			c.getActionAssistant().Send("You must be 90 Slayer to slay Tormented Demons.");
			return;
		}
		if (npc == 1613 && c.playerLevel[18] < 80){
			c.getActionAssistant().Send("You must be 80 Slayer to slay Nechryaels.");
			return;
		}
		if (npc == 1610 && c.playerLevel[18] < 75){
			c.getActionAssistant().Send("You must be 75 Slayer to slay Gargoyles.");
			return;
		}
		if (npc == 1616 && c.playerLevel[18] < 40){
			c.getActionAssistant().Send("You must be 40 Slayer to slay Balilisks.");
			return;
		}
		if (npc == 1637 && c.playerLevel[18] < 52){
			c.getActionAssistant().Send("You must be 52 Slayer to slay Jellys.");
			return;
		}
		if (npc == 1624 && c.playerLevel[18] < 65){
			c.getActionAssistant().Send("You must be 65 Slayer to slay Dust Devils.");
			return;
		}
		if (npc == 1619 && c.playerLevel[18] < 50){
			c.getActionAssistant().Send("You must be 50 Slayer to slay Bloodvelds.");
			return;
		}
		if (npc == 1643 && c.playerLevel[18] < 45){
			c.getActionAssistant().Send("You must be 45 Slayer to slay Infernal Mages.");
			return;
		}
		if (npc == 1604 && c.playerLevel[18] < 60){
			c.getActionAssistant().Send("You must be 60 Slayer to slay Abby Specs.");
			return;
		}		
		if ((npc == 934 || npc == 221) && (c.playerLevel[18] < 95)){
			c.getActionAssistant().Send("You must be 95 Slayer to slay this monster.");
			return;
		}
	}

	public void leechRanged(int index) {
	if (Misc.random(5) != 4) {
						 return;
						 }
                if (PlayerManager.getSingleton().getPlayers()[index] != null) {
                        final Client c2 = (Client)PlayerManager.getSingleton().getPlayers()[index];
                        final int pX = c.getX();
                        final int pY = c.getY();
                        final int oX = c2.getX();
                        final int oY = c2.getY();
                        if (Misc.random(10) == 0) {
                        c.getActionAssistant().startAnimation(12575);
						}
                        EventManager.getSingleton().addEvent(c,"leechRanged", new Event() {
                                int loops = 0;
                                public void execute(EventContainer s) {
                                        loops++;
                                        if (loops == 5) {
                                                int offX = (pY - oY)* -1;
                                                int offY = (pX - oX)* -1;
                                                c.getActionAssistant().sendMessage("You leech your opponent's range.");
                                                c.getCombat().createPlayersProjectile2(oX, oY, -offX, -offY, 50, 45, 2236, 43, 31, c.oldPlayerIndex - 1, 0);
												 
                                                if (c.rangeInc < 10)
                                                        c.rangeInc += 1;
                                                if (c2.rangeInc > -25)
                                                        c2.rangeInc -= 1;
                                        } else if (loops == 7) {
                                                c2.gfx0(2238);
                                                s.stop();
                                        }
                                }
								@Override
							public void stop() {
							}
                        }, 100);
                }
        }
       
        public void leechMagic(int index) {
                if (PlayerManager.getSingleton().getPlayers()[index] != null) {
				if (Misc.random(5) != 4) {
						 return;
						 }
                        final Client c2 = (Client)PlayerManager.getSingleton().getPlayers()[index];
                        final int pX = c.getX();
                        final int pY = c.getY();
                        final int oX = c2.getX();
                        final int oY = c2.getY();
                        if (Misc.random(10) == 0) {
                        c.getActionAssistant().startAnimation(12575);
						}
                       EventManager.getSingleton().addEvent(c,"leechMagic", new Event() {
                                int loops = 0;
                                public void execute(EventContainer s) {
                                        loops++;
                                        if (loops == 5) {
                                                int offX = (pY - oY)* -1;
                                                int offY = (pX - oX)* -1;
                                                c.getActionAssistant().sendMessage("You leech your opponent's magic.");
                                                c.getCombat().createPlayersProjectile2(oX, oY, -offX, -offY, 50, 45, 2240, 43, 31,c.oldPlayerIndex - 1, 2);
												 
                                                if (c.magicInc < 10)
                                                        c.magicInc += 1;
                                                if (c2.magicInc > -25)
                                                        c2.magicInc -= 1;
														
                                        } else if (loops == 7) {
                                                c2.gfx0(2242);
                                                s.stop();
                                        }
                                }
								@Override
							public void stop() {
							}
                        }, 100);
                }
        }
       
        public void leechDefence(int index) {
		if (Misc.random(5) != 4) {
						 return;
						 }
               if (PlayerManager.getSingleton().getPlayers()[index] != null) {
                        final Client c2 = (Client)PlayerManager.getSingleton().getPlayers()[index];
                        final int pX = c.getX();
                        final int pY = c.getY();
                        final int oX = c2.getX();
                        final int oY = c2.getY();
                        if (Misc.random(10) == 0) {
                        c.getActionAssistant().startAnimation(12575);
						}
						
                         EventManager.getSingleton().addEvent(c,"leechDefence", new Event() {
                                int loops = 0;
                                public void execute(EventContainer s) {
                                        loops++;
                                        if (loops == 5) {
                                                int offX = (pY - oY)* -1;
                                                int offY = (pX - oX)* -1;
                                                c.getActionAssistant().sendMessage("You leech your opponent's defence.");
                                                c.getCombat().createPlayersProjectile2(oX, oY, -offX, -offY, 50, 45, 2244, 43, 31,c.oldPlayerIndex - 1, 3);
                                                
													if (c.defInc < 10)
															c.defInc += 1;
													if (c2.defInc > -25)
															c2.defInc -= 1;
												
                                        } else if (loops == 7) {
                                                c2.gfx0(2246);
                                                s.stop();
                                        }
                                }
								@Override
							public void stop() {
							}
                        }, 100);
                }
        }

        public void leechSpecial(int index) {
                if (PlayerManager.getSingleton().getPlayers()[index] != null) {
                        final Client c2 = (Client)PlayerManager.getSingleton().getPlayers()[index];
                        final int pX = c.getX();
                        final int pY = c.getY();
                        final int oX = c2.getX();
                        final int oY = c2.getY();
                        if (c.specialAmount >= 100)  {
                                return;
                        }
                        if (c2.specialAmount <= 0) {
                                return;
                        }
						if (Misc.random(20) <= 18) {
							return;
						}
						if (Misc.random(10) == 0) {
                        c.getActionAssistant().startAnimation(12575);
						}
                       EventManager.getSingleton().addEvent(c,"leechSpecial", new Event() {
                                int loops = 0;
                                public void execute(EventContainer s) {
                                        loops++;
                                       if (loops == 5) {
                                                int offX = (pY - oY)* -1;
                                                int offY = (pX - oX)* -1;
                                                c.getActionAssistant().sendMessage("You leech your opponent's special attack.");
                                                c.getCombat().createPlayersProjectile2(oX, oY, -offX, -offY, 50, 45, 2256, 43, 31,c.oldPlayerIndex - 1, 6);
											//	int drain = Misc.random(10);
											int drain = 10;
                                                c.specialAmount += drain;
												if (c.specialAmount > 100) {c.specialAmount = 100; }
                                                c2.specialAmount -= drain;
												if (c2.specialAmount <= 0) { c2.specialAmount = 0; }
                                                //c.getItems().addSpecialBar(c.playerEquipment[PlayerConstants.PLAYER_WEAPON]);
                                                c2.getActionAssistant().sendMessage("Your special attack has been drained.");
                                       } else if (loops == 7) {
									   Server.getStillGraphicsManager().stillGraphics(c2,
				c2.getAbsX(), c2.getAbsY(), 0, 2258, 0);
                                               // c2.gfx0(2258);
                                                s.stop();
                                        }
                                }
								@Override
							public void stop() {
							}
                        }, 100);
                }
        }

        public void leechStrength(int index) {
		if (Misc.random(5) != 4) {
						 return;
						 }
                if (PlayerManager.getSingleton().getPlayers()[index] != null) {
                        final Client c2 = (Client)PlayerManager.getSingleton().getPlayers()[index];
                        final int pX = c.getX();
                        final int pY = c.getY();
                        final int oX = c2.getX();
                        final int oY = c2.getY();
						if (Misc.random(10) == 0) {
                        c.getActionAssistant().startAnimation(12575);
						}
                        EventManager.getSingleton().addEvent(c,"leechStrength", new Event() {
                                int loops = 0;
                                 public void execute(EventContainer s) {
                                        loops++;
                                        if (loops == 5) {
                                                int offX = (pY - oY)* -1;
                                                int offY = (pX - oX)* -1;
                                                c.getActionAssistant().sendMessage("You leech your opponent's strength.");
                                                c.getCombat().createPlayersProjectile2(oX, oY, -offX, -offY, 50, 45, 2248, 43, 31,c.oldPlayerIndex - 1, 4);
                                                if (c.strInc < 10) {
                                                        c.strInc += 1;
                                                }
                                                if (c2.strInc > -25) {
                                                        c2.strInc -= 1;
                                                }
                                        } else if (loops == 7) {
                                               Server.getStillGraphicsManager().stillGraphics(c2,
				c2.getAbsX(), c2.getAbsY(), 0, 2250, 0);
//				c2.gfx0(2250);
                                                s.stop();
                                        }
                                }
								@Override
							public void stop() {
							}
                        }, 100);
                }
        }
		public void resetLeechBonus() {
			c.strInc = 5;
			c.attInc = 5;
			c.rangeInc = 5;
			c.magicInc = 5;
			c.defInc = 5;
		}
        public void leechAttack(int index) {
		if (Misc.random(5) != 4) {
						 return;
						 }
                if (PlayerManager.getSingleton().getPlayers()[index] != null) {
                        final Client c2 = (Client)PlayerManager.getSingleton().getPlayers()[index];
                        final int pX = c.getX();
                        final int pY = c.getY();
                        final int oX = c2.getX();
                        final int oY = c2.getY();
                        if (Misc.random(10) == 0) {
                        c.getActionAssistant().startAnimation(12575);
						}
                       EventManager.getSingleton().addEvent(c,"leechAttack", new Event() {
                                int loops = 0;
                                public void execute(EventContainer s) {
                                        loops++;
                                        if (loops == 5) {
                                                int offX = (pY - oY)* -1;
                                                int offY = (pX - oX)* -1;
                                                if (c.attInc < 10)
                                                        c.attInc += 1;
                                                if (c2.attInc > -25)
                                                        c2.attInc -= 1;
                                                c.getActionAssistant().sendMessage("You leech your opponent's attack.");
                                                c.getCombat().createPlayersProjectile(oX, oY, -offX, -offY, 50, 45, 2231, 43, 31,c.oldPlayerIndex - 1, 1);
                                        } else if (loops == 7) {
                                                c2.gfx0(2232);
                                                s.stop();
                                        }
                                }
								@Override
							public void stop() {
							}
                        }, 100);
                }
        }
	/*public int getAttackDelay() {
	 switch(c.playerEquipment[PlayerConstants.PLAYER_WEAPON]) {

	 case -1://fist or foot
	 return 2400;


	 case 1307://brone 2h
	 case 1909://iron 2h
	 case 1311://steel 2h
	 case 1313://black 2h
	 case 1315://mith 2h
	 case 1317://addy 2h
	 case 1319://rune 2h
	 case 6609://white 2h [non bronze-dragon type]
	 return 4200;

	 case 1215://Dragon dagger (n)
	 case 1231://Dragon dagger (p)
	 case 5680://Dragon dagger (+)
	 case 5698://Dragon dagger (s)
	 return 2400;

	 case 1419://scythe(holiday)
	 return 2400;

	 case 4151://whip
	 return 2400;

	 case 746://dark dagger [non bronze-dragon type]
	 case 747://glowing dagger [glowing dark dagger] [non bronze-dragon type]
	 case 1203://iron dagger
	 case 1205://bronze dagger
	 case 1207://steel dagger
	 case 1209://mith dagger
	 case 1211://addy dagger
	 case 1213://rune dagger
	 case 1217://black dagger
	 case 1219://iron dagger (p)
	 case 1221://bronze dagger (p)
	 case 1223://steel dagger (p)
	 case 1225://mith dagger (p)
	 case 1227://addy dagger (p)
	 case 1229://rune dagger (p)
	 case 1233://black dagger (p)
	 case 1235://poisoned_dagger(p) [non bronze-dragon type]
	 case 2952://wolfbane [preist in paril]/[non bronze-dragon type]
	 case 5668://iron dagger (+)
	 case 5670://bronze dagger (+)
	 case 5672://steel dagger (+)
	 case 5674://mith dagger (+)
	 case 5676://addy dagger (+)
	 case 5678://rune dagger (+)
	 case 5682://black dagger (+)
	 case 5684://poisoned_dagger (+) [non bronze-dragon type]
	 case 5686://iron dagger (s)
	 case 5688://bronze dagger (s)
	 case 5690://steel dagger (s)
	 case 5692://mith dagger (s)
	 case 5694://addy dagger (s)
	 case 5696://rune dagger (s)
	 case 5700://black dagger (s)
	 case 5702://poisoned_dagger (s) [non bronze-dragon type]
	 case 6595://white dagger (+) [non bronze-dragon type]
	 case 6593://white dagger (p) [non bronze-dragon type]
	 case 6597://white dagger (s) [non bronze-dragon type]
	 case 6591://white dagger [non bronze-dragon type]
	 return 2400;


	 case 3190://bronze halberd
	 case 3192://iron halberd
	 case 3194://steel halberd
	 case 3196://black halberd
	 case 3197://mith halberd
	 case 3200://addy halberd
	 case 3202://rune halberd
	 case 3204://dragon halberd
	 case 6599://white halberd [non bronze-dragon type]
	 return 4200;

	 case 667://blurite sword [non bronze-dragon type]
	 case 1277://bronze sword
	 case 1279://iron sword
	 case 1281://steel sword
	 case 1283://black sword
	 case 1285://mith sword
	 case 1287://addy sword
	 case 1289://rune sword
	 case 1305://dragon longsword
	 case 1291://bronze longsword
	 case 1293://iron longsword
	 case 1295://steel longsword
	 case 1297://black longsword
	 case 1299://mith longsword
	 case 1301://addy longsword
	 case 1303://rune longsword
	 case 6607://white longsword [non bronze-dragon type]
	 case 6605://white sword [non bronze-dragon type]
	 return 3000;

	 case 4153://granite maul
	 return 4200;

	 case 1377://dragon battleaxe
	 return 3600;//1833=dragon_baxe

	 case 6082://fixed device [non bronze-dragon type]
	 return 4000;//2323=fixed_device_kicking

	 case 4755://veraces flail new
	 case 4082://veraces flail 100%
	 case 4083://veraces flail 75%
	 case 4084://veraces flail 50%
	 case 4085://veraces flail 25%
	 case 4086://veraces flail 0%
	 return 3000;

	 case 4718://dharoks greataxe new
	 case 4887://dharoks greataxe 100%
	 case 4888://dharoks greataxe 75%
	 case 4889://dharoks greataxe 50%
	 case 4890://dharoks greataxe 25%
	 case 4891://dharoks greataxe 0%
	 return 4200;

	 case 4958://torags hammers new
	 case 4959://torags hammers 100%
	 case 4960://torags hammers 75%
	 case 4961://torags hammers 50%
	 case 4962://torags hammers 25%
	 case 4963://torags hammers 0%
	 return 3000;

	 case 4934://karils X-bow new
	 case 4935://karils X-bow 100%
	 case 4936://karils X-bow 75%
	 case 4937://karils X-bow 50%
	 case 4938://karils X-bow 25%
	 case 4939://karils X-bow 0%
	 return 2400;

	 case 4862://ahrims staff new
	 case 4863://ahrims staff 100%
	 case 4864://ahrims staff 75%
	 case 4865://ahrims staff 50%
	 case 4866://ahrims staff 25%
	 case 4867://ahrims staff 0%
	 return 3600;

	 case 4910://guthans spear new
	 case 4911://guthans spear 100%
	 case 4912://guthans spear 75%
	 case 4913://guthans spear 50%
	 case 4914://guthans spear 25%
	 case 4915://guthans spear 0%
	 return 3000;

	 case 841://shortbow
	 case 843://oak shortbow
	 case 849://willow shortbow
	 case 853://maple shortbow
	 case 857://yew shortbow
	 case 861://magic shortbow
	 case 845://oak longbow
	 case 847://willow longbow
	 case 851://maple longbow
	 case 855://yew longbow
	 case 859://magic longbow
	 return 1800;

	 case 1363://iron battleaxe
	 case 1365://steel battleaxe
	 case 1367://black battleaxe
	 case 1369://mith battleaxe
	 case 1371://addy battleaxe
	 case 1373://rune battleaxe
	 case 1375://bronze battleaxe
	 case 6598://white battleaxe [non bronze-dragon type]
	 return 3600;

	 case 1321://bronze scimitar
	 case 1323://iron scimitar
	 case 1325://steel scimitar
	 case 1327://black scimitar
	 case 1329://mith scimitar
	 case 1331://addy scimitar
	 case 1333://rune scimitar
	 case 4587://dragon scimitar
	 case 6611://white scimitar [non bronze-dragon type]
	 return 2400;

	 case 1237://bronze spear
	 case 1239://iron spear
	 case 1241://steel spear
	 case 4850://black spear
	 case 1243://mith spear
	 case 1245://addy spear
	 case 1247://rune spear
	 case 1249://dragon spear
	 case 1251://bronze spear (p)
	 case 1253://iron spear (p)
	 case 1255://steel spear (p)
	 case 4852://black spear (p)
	 case 1257://mith spear (p)
	 case 1259://addy spear (p)
	 case 1262://rune spear (p)
	 case 1263://dragon spear (p)
	 case 3170://bronze spear (kp)
	 case 3171://iron spear (kp)
	 case 3172://steel spear (kp)
	 case 4584://black spear (kp)
	 case 3173://mith spear (kp)
	 case 3174://addy spear (kp)
	 case 3175://rune spear (kp)
	 case 3176://dragon spear (kp)
	 case 5704://bronze spear (+)
	 case 5706://iron spear (+)
	 case 5708://steel spear (+)
	 case 5734://black spear (+)
	 case 5710://mith spear (+)
	 case 5712://addy spear (+)
	 case 5714://rune spear (+)
	 case 5716://dragon spear (+)
	 case 5718://bronze spear (s)
	 case 5720://iron spear (s)
	 case 5722://steel spear (s)
	 case 5737://black spear (s)
	 case 5724://mith spear (s)
	 case 5726://addy spear (s)
	 case 5728://rune spear (s)
	 case 5730://dragon spear (s)
	 return 3000;

	 case 1337://bronze warhammer
	 case 1335://iron warhammer
	 case 1339://steel warhammer
	 case 1341://black warhammer
	 case 1343://mith warhammer
	 case 1345://addy warhammer
	 case 1347://rune warhammer
	 case 1415://warhammer [non bronze-dragon type]
	 return 3600;

	 case 1351://bronze woodcutting axe
	 case 1349://iron woodcutting axe
	 case 1353://steel woodcutting axe
	 case 1361://black woodcutting axe
	 case 1355://mith woodcutting axe
	 case 1357://addy woodcutting axe
	 case 1359://rune woodcutting axe
	 case 6739://dragon woodcutting axe
	 return 3600;

	 case 1265://bronze pickaxe
	 case 1267://iron pickaxe
	 case 1269://steel pickaxe
	 case 1271://mith pickaxe
	 case 1273://addy pickaxe
	 case 1275://rune pickaxe
	 return 3600;

	 case 141422://bronze mace
	 case 1420://iron mace
	 case 1424://steel mace
	 case 1426://black mace
	 case 1428://mith mace
	 case 1430://addy mace
	 case 1432://rune mace
	 case 1434://dragon mace
	 return 3600;

	 case 772://dramen staff
	 case 4170://slayers staff
	 case 4675://ancient staff [desert treasure]
	 case 6526://Toktz-mej-tal [obsidian staff]
	 case 1379://staff
	 case 1381://air staff
	 case 1383://water staff
	 case 1385://earth staff
	 case 1387://fire staff
	 case 1389://magic staff
	 case 1391://battle staff
	 case 1394://fire battlestaff
	 case 1395://water battlestaff
	 case 1397://air battlestaff
	 case 1399://earth battlestaff
	 case 3053://lava battlestaff
	 case 6562://mud battlestaff
	 case 3054://mystic lava staff
	 case 6563://mystic mud staff
	 case 1401://mystic fire staff
	 case 1403://mystic water staff
	 case 1405://mystic air staff
	 case 1407://mystic earth staff
	 case 1409://Ibans staff
	 return 3000;

	 case 2415://saradomin godstaff
	 case 2416://guthix godstaff
	 case 2417://zamorak godstaff
	 return 3000;

	 case 863://bronze knifes
	 case 864://iron knifes
	 case 865://steel knifes
	 case 869://black knifes
	 case 866://mith knifes
	 case 867://addy knifes
	 case 868://rune knifes
	 return 1500;

	 case 825://broze javalin
	 case 826://iron javalin
	 case 827://steel javalin
	 case 828://mith javalin
	 case 829://addy javalin
	 case 830://rune javalin
	 return 3600;

	 case 800://bronze thrown axe
	 case 801://iron thrown axe
	 case 802://steel thrown axe
	 case 803://mith thrown axe
	 case 804://addy thrown axe
	 case 805://rune thrown axe
	 return 1500;

	 default:
	 //System.out.println("[COMBAT_HANDLER]: unhandled weapon speed. weapon ID: " + client.playerEquipment[PlayerConstants.PLAYER_WEAPON]);
	 return 2000;

	 }	
	 }*/	
}
