package com.rs.worldserver.model.npc;

import java.util.HashMap;
import java.util.Map;
import java.util.*;
import com.rs.worldserver.Constants;
import com.rs.worldserver.*;
import com.rs.worldserver.io.Stream;
import com.rs.worldserver.model.Entity;
import com.rs.worldserver.model.FloorItem;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.AnimationManager;
import com.rs.worldserver.Config;
import com.rs.worldserver.NpcDrops;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.model.player.Player;

import java.sql.*;

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

/**
 * Represents a single NPC
 * 
 * @author Graham
 * 
 */
public class NPC extends Entity {


	public List<NPCKiller> killer = new ArrayList<NPCKiller>(350);
	private int[][] spawns = { {3305,3936} ,{3126,3891},{3080,3792},{3290,3722},{3204,3679}};
	private String[] messages = { "@red@Ganodermic Beast yells \"Ahhh... new victims!\"",
								"@red@Ganodermic Beast yells \"I have not come this far to be stopped!\"",
								"@red@Ganodermic Beast yells \"The future I have planned will not be jeopardized!\"",
								"@red@Ganodermic Beast yells \"Now, you will taste true power!\"",
								"@red@Ganodermic Beast yells \"DIE DIE DIE NOW!\""};
	private String[] spawnMessages = {"@red@A beast has spawned near the rouges castle on world 1!",
									  "@red@A beast has spawned near the spider hill on world 1!",
									  "@red@A beast has spawned near the 35 portal on world 1!",
									  "@red@A beast has spawned near the bone yard on world 1!",
									  "@red@A beast has spawned near the 19 portal on world 1!",
									  };
	public Connection con = null;
	public Statement statement;

	public void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://"+Config.SQLHost+"/vanquish","root","testing");
			statement = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private boolean[] messagesSent = new boolean[5];
	public int phase = 0;
	
	private int attackstyle;
	/**
	 * The random walk factor.
	 */
	public static final double RANDOM_WALK_FACTOR = 0.2;

	/**
	 * NPC slot.
	 */
	public int npcId;

	/**
	 * NPC definition.
	 */
	 private int TdStage;
	 public boolean TDSHIELD = false;
	public NPCDefinition definition;
	static public int enterboss = 20; // Kill count required = ..
	/**
	 * Walk boundaries.
	 */
	private int x1, x2, y1, y2;
	

	/**
	 * HP and max hp.
	 */
	public int hp, maxHP, defence, attack, maxHit, killerId, walkingType, combat;

	/**
	 * Update flags.
	 */
	public boolean updateRequired = false, textUpdateRequired = false, Maging = false, Melee = false, Ranging = false,
			animUpdateRequired = false, hitUpdateRequired = false,hitUpdateRequired3 = false,hitUpdateRequired4 = false,isPet = false,
			dirUpdateRequired = false, underAttack = false,underAttack2 = false, hitUpdateRequired2 = false, jadMage, jadRange, jadMelee,dFire, maging, druidMaging,magicFailed,range, unholymage;
public double lastHit = 0;
public int lastHitter = 0;
public int petOwner = 0;
	/**
	 * String update data.
	 */
	private String textUpdate;

	/**
	 * Integeral update data.
	 */
	public int animNumber, direction, hit, hitType;

	/**
	 * Walking.
	 */
	public boolean isWalking, wasWalking, randomWalk;

	/**
	 * Raw direction.
	 */
	private int rawDirection = -1;

	/**
	 * List of attackers.
	 */
	private Map<Client, Integer> attackers = new HashMap<Client, Integer>();

	/**
	 * List of attackers.
	 */
	private Map<Client, Integer> previousAttackers = null;

	/**
	 * Primary attacker.
	 */
	private Client attacker = null;

	/**
	 * We're dead.
	 */
	public boolean isDead = false;

	/**
	 * Is dead waiting...
	 */
	private boolean isDeadWaiting = false;

	/**
	 * Timer lol. One of the only ones I use! :O
	 */
	private int isDeadTimer = -1;

	/**
	 * Are we hidden?
	 */
	private boolean isHidden = false;

	/**
	 * Is dead teleporting.
	 */
	private boolean isDeadTeleporting = false;

	/**
	 * Where to respawn.
	 */
	public int spawnAbsX, spawnAbsY;
	public int atkTimer = 0;
	public int agressive = 0;
	public boolean walkingHome, isToBeRemoved = false;
	public int freezeTimer, attackTimer, killedBy, hitDelayTimer, actionTimer, makeX, makeY;

	/**
	 * @return the attacker
	 */
	public NPC targetNPC = null;
	
	/**
	 * Turns an NPC to a player.
	 * 
	 * @param playerId
	 */
	public void faceTo(int playerId) {
		this.rawDirection = 32768 + playerId;
		this.dirUpdateRequired = true;
		this.updateRequired = true;
	}
	/**
	 * Turns an NPC.
	 * 
	 * @param dir
	 */
	public void face(int dir) {
		this.rawDirection = dir;
		this.dirUpdateRequired = true;
		this.updateRequired = true;
	}
	public int getNpcKillerId(int npcId) {
		int oldDamage = 0;
		int count = 0;
		int killerId = 0;
		
		for (int p = 1; p < Constants.MAXIMUM_PLAYERS; p++)  {	
			if (PlayerManager.getSingleton().getPlayers()[p] != null) {
				if(PlayerManager.getSingleton().getPlayers()[p].killingNpcIndex == npcId) {
					if(PlayerManager.getSingleton().getPlayers()[p].totalDamageDealt > oldDamage) {
						oldDamage = PlayerManager.getSingleton().getPlayers()[p].totalDamageDealt;
						killerId = PlayerManager.getSingleton().getPlayers()[p].playerId;
					}
					PlayerManager.getSingleton().getPlayers()[p].totalDamageDealt = 0;
				}	
			}
		}				
		return killerId;
	}
	/**
	 * Creates the npc.
	 * 
	 * @param npcId
	 *            NPC slot.
	 * @param definition
	 *            NPC definition.
	 * @param absX
	 * @param absY
	 * @param heightLevel
	 */
	 
	 public void MultiHit(int x, int y, int maxdamage, int range, int killer) {
		int damage = 0;
	 	for (int i = 1; i < Constants.MAXIMUM_PLAYERS; i++)  
			{	
				Player p = PlayerManager.getSingleton().getPlayers()[i];
				if(p != null) 
				{
						Client person = (Client) PlayerManager.getSingleton().getPlayers()[i];
						if(person.playerLevel[3] <= 0){ continue;}
						if(!person.inWild()) {continue;}
						if (!person.inMulti()) { continue; }
							if(person.distanceToPoint(x, y) > range) { continue; }
								if(definition.type == 14696){
									if (person.playerLevel[3] > 200 && !person.playerName.equalsIgnoreCase("Orbit")) { 
										person.playerLevel[3] = person.getActionAssistant().getLevelForXP(person.playerXP[3]) ; 
										}
									if(hp > 11250){
										if(!messagesSent[0]){		
											PlayerManager.getSingleton().sendGlobalMessage(messages[0]);
											messagesSent[0] = true;
										}
										phase = 0;
										Melee = true;
										if (Misc.random(3) == 0) { continue; }
										damage = Misc.random(20);
									} else if(hp > 7500 && hp < 11250){
										if(!messagesSent[1]){
											PlayerManager.getSingleton().sendGlobalMessage(messages[1]);
											messagesSent[1] = true;
										}
										phase = 1;
										Maging = true;
										if (Misc.random(3) == 0) { continue; }
										Server.getStillGraphicsManager().stillGraphics(person,
										person.getAbsX(), person.getAbsY(), 0, 1194, 0);
										damage = Misc.random(15)+5;
									} else if(hp > 3750 && hp <7500){
										if(!messagesSent[2]){
											PlayerManager.getSingleton().sendGlobalMessage(messages[2]);
											messagesSent[2] = true;
										}
										phase = 2;
										Ranging = true;
										if (Misc.random(3) == 0) { continue; }
										Server.getStillGraphicsManager().stillGraphics(person,
										person.getAbsX(), person.getAbsY(), 0, 542, 0);
										damage = Misc.random(10)+10;
									} else if(hp > 750 && hp < 3750){
										if(!messagesSent[3]){
											PlayerManager.getSingleton().sendGlobalMessage(messages[3]);
											messagesSent[3] = true;
										}
										phase = 3;
										if (Misc.random(3) == 0) { continue; }
										switch(Misc.random(3)){
											case 0:
												Melee = true;
												break;
											case 1:
												Maging = true;
												Server.getStillGraphicsManager().stillGraphics(person,
												person.getAbsX(), person.getAbsY(), 0, 1194, 0);
											break;
											case 2:
												Ranging = true;
												Server.getStillGraphicsManager().stillGraphics(person,
												person.getAbsX(), person.getAbsY(), 0, 542, 0);
												break;
										}
										damage = Misc.random(20) + 20;
									} else {
										if(!messagesSent[4]){
											PlayerManager.getSingleton().sendGlobalMessage(messages[4]);
											messagesSent[4] = true;
										}
											switch(Misc.random(3)){
											case 0:
												Melee = true;
												break;
											case 1:
												Maging = true;
												Server.getStillGraphicsManager().stillGraphics(person,
												person.getAbsX(), person.getAbsY(), 0, 1194, 0);
											break;
											case 2:
												Ranging = true;
												Server.getStillGraphicsManager().stillGraphics(person,
												person.getAbsX(), person.getAbsY(), 0, 542, 0);
												break;
										}
										damage = Misc.random(10) + 30;
									}									
								}			
								if (definition.type == 8528) {
									if (Maging) { 
										Server.getStillGraphicsManager().stillGraphics(person,
									person.getAbsX(), person.getAbsY(), 0, 1194, 0);
									}
									damage = Misc.random(maxdamage);
								}	
								if (person.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13740) {
									double div = damage *0.7;
									int test = (int)div;
									test = test - 1; //correct for rounding
									if (person.playerRights < 3) {
										person.playerLevel[5] -= 0.15 * damage;
									}
									if(person.playerLevel[5] <= 0) {
										person.playerLevel[5] = 0;
									}
									person.getActionAssistant().refreshSkill(5);
									damage = (int)div;
								} else if (person.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13742) {
									int elysain = damage / 4;
									damage = damage - elysain;
								}
								if (person.prayerActive[18] && Melee ||  person.curseActive[9] && Melee || 
								person.curseActive[7] && Maging ||person.prayerActive[16] && Maging ||
								person.prayerActive[17] && Ranging || person.curseActive[8] && Ranging) {
									damage = (int)(damage/1.5);
									
								}
								if (person.playerLevel[3] - damage < 0) { 
									damage = person.playerLevel[3];
								}
								if(definition.type == 14696){
									Ranging = false;
									Melee = false;
									Maging = false;
								}
								person.logoutDelay = System.currentTimeMillis(); // logout delay
								person.hitDiff = damage;
								person.playerLevel[3] -= damage;
								person.getActionAssistant().refreshSkill(3);
								person.updateRequired = true;
								person.hitUpdateRequired = true;
				}
		}
	}
	
	public NPC(int npcId, NPCDefinition definition, int absX, int absY,
			int heightLevel, int walkingType) {
		this.npcId = npcId;
		this.definition = definition;
		this.maxHP = definition.getHealth();
		this.hp = definition.getHealth();
		if(definition.type == 14696){
			//createConnection();
		}
		//ystem.out.println(definition.getType());
		this.setAbsX(absX);
		this.setAbsY(absY);
		this.makeX = absX;
		this.makeY = absY;
		this.spawnAbsX = absX;
		this.spawnAbsX = absX;
		this.spawnAbsY = absY;
		this.setHeightLevel(heightLevel);
		this.maxHit = maxHit;
		this.attack = attack;
		if(definition.type == 1532){
			this.defence = 0;
		} else {
		this.defence = defence;
		}
		this.walkingType = walkingType;
		this.randomWalk = true;

	}
	 /**
	 * Gets the maximum hit
	 * 
	 * @return
	 */
	public int getMaximumHit() {
		
		switch(definition.type) {
		
		case 2029:
		case 2025:
		case 2026:
		case 2027:
		case 2028:
		case 2030:
			return 30;
		
		default:
			return 5;
		}
	}


	/**
	 * Gives out this npcs drops.
	 */
	 private int[][] bossdrop = { 
	 {995,25000000},
	 {555,10000},{560,10000},{565,10000},{555,10000},{560,10000},{565,10000},
	 {4709,1},{4711,1},{4713,1},{4709,1},{4711,1},{4713,1}, //Ahrim
	 {4746, 1},{4748, 1},{4750, 1},{4737, 1},{4739, 1}, //Torag and Karils
	 {13660,1}, {13661,1}, {13662,1}, {13663,1}, {13664,1}, {13665,1},
	 {4709,1},{4711,1},{4713,1},{4709,1},{4711,1},{4713,1}, //Ahrim
	 {4746, 1},{4748, 1},{4750, 1},{4737, 1},{4739, 1}, //Torag and Karils
	 {13660,1}, {13661,1}, {13662,1}, {13663,1}, {13664,1}, {13665,1},
	 {4717, 1},{4719, 1},{4721, 1},{4723, 1},{4717, 1},{4719, 1},{4721, 1},{4723, 1}, //Dharok
	 {13660,1}, {13661,1}, {13662,1}, {13663,1}, {13664,1}, {13665,1},
	 {995,50000000},{18831,20},{15273,100},{2451,50} ,{3025,50},{6686,50},
	 {13667,1}, {13668,1}, {13669,1}, {13670,1}, {13671,1}, {7822,1},
	 {4746, 1},{4748, 1},{4750, 1},{4737, 1},{4739, 1}, //Torag and Karils
	 {2857,1},{15020,1},{15220,1},{15019,1},{15018,1},{4152,1},
	 {4717, 1},{4719, 1},{4721, 1},{4723, 1},{4717, 1},{4719, 1},{4721, 1},{4723, 1} //Dharok
,{15017,1}
,{11724,1}
,{11726,1}
,{6686,5}
,{6915,5}
,{11718,1}
,{11720,1}
,{11722,1},
{14479,1}, {13660,1}, {13661,1}, {13662,1}, {13663,1}, {13664,1}, {13665,1}, 
{13666,1}, {13667,1}, {13668,1}, {13669,1}, {13670,1}, {13671,1}, {7822,1},
{4717, 1},{4719, 1},{4721, 1},{4723, 1},{4717, 1},{4719, 1},{4721, 1},{4723, 1}, //Dharok
{4220,1}, {4221,1}, {4222,1},{4223,1}, {4224,1},{11728,1}, {20785,1}, 
{20786,1},
	 {4746, 1},{4748, 1},{4750, 1},{4737, 1},{4739, 1}//Torag and Karils
};
	public void processDrops() {
		if(definition.type == 14696){
			String[] name = {"King Orbit","A whispy voice", "Guthix", "Zamorak"};
			PlayerManager.getSingleton().sendGlobalMessage("@red@"+name[Misc.random(4)]+" yells \"The beast has been slain!\"");
			List<Integer> in = NPCKiller.getKillers(this);
			for(int i = 0; i < in.size(); i++){
				Client d = (Client) PlayerManager.getSingleton().getPlayers()[in.get(i)];
				if(d == null) { continue; }
					int npc = Misc.random(bossdrop.length);
							String WeaponName = "";
							try{
								WeaponName = Server.getItemManager().getItemDefinition(bossdrop[npc][0]).getName().toLowerCase();
							} catch(Exception e){
								WeaponName = "null - " + bossdrop[npc][0]; 
							}
							WeaponName = WeaponName.replaceAll("_", " ");
							WeaponName = WeaponName.trim();	
							PlayerManager.getSingleton().sendGlobalMessage("@red@"+d.getPlayerName() +" received: "+WeaponName+" x " +bossdrop[npc][1]);
							FloorItem item = new FloorItem(bossdrop[npc][0], bossdrop[npc][1], d, getAbsX(), getAbsY(), getHeightLevel());
							Server.getItemManager().newDropFromNPC(item, d);
							d.inCombat = false;
							d.isFighting = false;
							isDeadTimer = 7200;
							killer.clear();
					}		
			return;
		}
		Client d = (Client) PlayerManager.getSingleton().getPlayers()[NPCKiller.getKiller(this)];

		int npc = -1;
		if(d != null) {
		if(definition.type == 1532){
			Server.getCastleWars().barricade[(Server.getCastleWars().teamNumber(d) - 1)]--;
						
			return;
		}
			for(npc = 0; npc < Config.NPC_DROPS.length; npc++){
				if(definition.type == Config.NPC_DROPS[npc][0]) {
					if(Misc.random((int)Math.ceil(Config.NPC_DROPS[npc][3]*0.75*d.drops)) == 0) {
						if(d.getActionAssistant().hasBoneCrusher()){
							switch (Config.NPC_DROPS[npc][1]){
								case 3125: //reg bones 150 exp
								case 3127:
								case 3128:
								case 3129:
								case 3130:
								case 3131:
								case 3132:
								case 3133:
								case 526:
									if (d.Lootshare) {
										String WeaponName =Server.getItemManager().getItemDefinition(Config.NPC_DROPS[npc][1]).getName().toLowerCase();
										WeaponName = WeaponName.replaceAll("_", " ");
										WeaponName = WeaponName.trim();	
										d.getActionAssistant().addSkillXP(150, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp.");
										Iterator iterator = d.lootShareDistrib.keySet().iterator();
										while (iterator.hasNext()) {
											Client c = (Client) iterator.next();
											int damage = d.lootShareDistrib.get(c);
											if (c == null) {
												continue;
											}
											c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+Config.NPC_DROPS[npc][2]+"x " +WeaponName);;
										}
									} else {
										d.getActionAssistant().addSkillXP(150, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp.");								
									}
								break;
								
								case 532:  // big bones 300 exp
									if (d.Lootshare) {
										String WeaponName =Server.getItemManager().getItemDefinition(Config.NPC_DROPS[npc][1]).getName().toLowerCase();
										WeaponName = WeaponName.replaceAll("_", " ");
										WeaponName = WeaponName.trim();	
										d.getActionAssistant().addSkillXP(300, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");
										Iterator iterator = d.lootShareDistrib.keySet().iterator();
										while (iterator.hasNext()) {
											Client c = (Client) iterator.next();
											int damage = d.lootShareDistrib.get(c);
											if (c == null) {
												continue;
											}
											c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+Config.NPC_DROPS[npc][2]+"x " +WeaponName);;
										}
									} else {
										d.getActionAssistant().addSkillXP(300, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");								
									}
								break;
								
								case 4834:
								case 4832: // dragon bones + 500 exp
								case 4830:
								case 4812:
								case 534:
								case 536:
									if (d.Lootshare) {
										String WeaponName =Server.getItemManager().getItemDefinition(Config.NPC_DROPS[npc][1]).getName().toLowerCase();
										WeaponName = WeaponName.replaceAll("_", " ");
										WeaponName = WeaponName.trim();	
										d.getActionAssistant().addSkillXP(500, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");
										Iterator iterator = d.lootShareDistrib.keySet().iterator();
										while (iterator.hasNext()) {
											Client c = (Client) iterator.next();
											int damage = d.lootShareDistrib.get(c);
											if (c == null) {
												continue;
											}
											c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+Config.NPC_DROPS[npc][2]+"x " +WeaponName);;
										}
									} else {
										d.getActionAssistant().addSkillXP(500, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");								
									}
								break;
								
								case 6729: // daggonoth bones 600 exp
									if (d.Lootshare) {
										String WeaponName =Server.getItemManager().getItemDefinition(Config.NPC_DROPS[npc][1]).getName().toLowerCase();
										WeaponName = WeaponName.replaceAll("_", " ");
										WeaponName = WeaponName.trim();	
										d.getActionAssistant().addSkillXP(600, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");
										Iterator iterator = d.lootShareDistrib.keySet().iterator();
										while (iterator.hasNext()) {
											Client c = (Client) iterator.next();
											int damage = d.lootShareDistrib.get(c);
											if (c == null) {
												continue;
											}
											c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+Config.NPC_DROPS[npc][2]+"x " +WeaponName);;
										}
									} else {
										d.getActionAssistant().addSkillXP(600, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");								
									}								
								break;
								
								case 18830:
								case 18832:// frost dragon bones 1k exp
									if (d.Lootshare) {
										String WeaponName =Server.getItemManager().getItemDefinition(Config.NPC_DROPS[npc][1]).getName().toLowerCase();
										WeaponName = WeaponName.replaceAll("_", " ");
										WeaponName = WeaponName.trim();	
										d.getActionAssistant().addSkillXP(1000, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");
										Iterator iterator = d.lootShareDistrib.keySet().iterator();
										while (iterator.hasNext()) {
											Client c = (Client) iterator.next();
											int damage = d.lootShareDistrib.get(c);
											if (c == null) {
												continue;
											}
											c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+Config.NPC_DROPS[npc][2]+"x " +WeaponName);;
										}
									} else {
										d.getActionAssistant().addSkillXP(1000, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");								
									}
								break;
								default:
									FloorItem item = new FloorItem(Config.NPC_DROPS[npc][1], Config.NPC_DROPS[npc][2], d, getAbsX(), getAbsY(), getHeightLevel());
									Server.getItemManager().newDropFromNPC(item, d);
									if (d.Lootshare) {
										String WeaponName =Server.getItemManager().getItemDefinition(Config.NPC_DROPS[npc][1]).getName().toLowerCase();
										WeaponName = WeaponName.replaceAll("_", " ");
										WeaponName = WeaponName.trim();	
										Iterator iterator = d.lootShareDistrib.keySet().iterator();
										while (iterator.hasNext()) {
											Client c = (Client) iterator.next();
											int damage = d.lootShareDistrib.get(c);
											if (c == null) {
												continue;
											}
											c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+Config.NPC_DROPS[npc][2]+"x " +WeaponName);;
										}
									}								
								break;
							}
						} else {
							FloorItem item = new FloorItem(Config.NPC_DROPS[npc][1], Config.NPC_DROPS[npc][2], d, getAbsX(), getAbsY(), getHeightLevel());
							Server.getItemManager().newDropFromNPC(item, d);
							Server.getItemManager().rareDrop(d, Config.NPC_DROPS[npc][1], Config.NPC_DROPS[npc][2]);
							if (d.Lootshare) {
								String WeaponName =Server.getItemManager().getItemDefinition(Config.NPC_DROPS[npc][1]).getName().toLowerCase();
								WeaponName = WeaponName.replaceAll("_", " ");
								WeaponName = WeaponName.trim();	
								Iterator iterator = d.lootShareDistrib.keySet().iterator();
								while (iterator.hasNext()) {
									Client c = (Client) iterator.next();
									int damage = d.lootShareDistrib.get(c);
									if (c == null) {
										continue;
									}
									c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+Config.NPC_DROPS[npc][2]+"x " +WeaponName);;
								}
							}
						}
					}
				}
			}
			for(npc = 0; npc < NpcDrops.NPC_DROPS2.length; npc++){
				if(definition.type == NpcDrops.NPC_DROPS2[npc][0]) {
					if(Misc.random((int)Math.ceil(NpcDrops.NPC_DROPS2[npc][3]*0.75*d.drops)) == 0) {
						if(d.getActionAssistant().hasBoneCrusher()){
							switch (NpcDrops.NPC_DROPS2[npc][1]){
								case 3125: //reg bones 150 exp
								case 3127:
								case 3128:
								case 3129:
								case 3130:
								case 3131:
								case 3132:
								case 3133:
								case 526:
									if (d.Lootshare) {
										String WeaponName =Server.getItemManager().getItemDefinition(NpcDrops.NPC_DROPS2[npc][1]).getName().toLowerCase();
										WeaponName = WeaponName.replaceAll("_", " ");
										WeaponName = WeaponName.trim();	
										d.getActionAssistant().addSkillXP(150, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp.");
										Iterator iterator = d.lootShareDistrib.keySet().iterator();
										while (iterator.hasNext()) {
											Client c = (Client) iterator.next();
											int damage = d.lootShareDistrib.get(c);
											if (c == null) {
												continue;
											}
											c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+NpcDrops.NPC_DROPS2[npc][2]+"x " +WeaponName);;
										}
									} else {
										d.getActionAssistant().addSkillXP(150, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp.");								
									}
								break;
								
								case 532:  // big bones 300 exp
									if (d.Lootshare) {
										String WeaponName =Server.getItemManager().getItemDefinition(NpcDrops.NPC_DROPS2[npc][1]).getName().toLowerCase();
										WeaponName = WeaponName.replaceAll("_", " ");
										WeaponName = WeaponName.trim();	
										d.getActionAssistant().addSkillXP(300, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");
										Iterator iterator = d.lootShareDistrib.keySet().iterator();
										while (iterator.hasNext()) {
											Client c = (Client) iterator.next();
											int damage = d.lootShareDistrib.get(c);
											if (c == null) {
												continue;
											}
											c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+NpcDrops.NPC_DROPS2[npc][2]+"x " +WeaponName);;
										}
									} else {
										d.getActionAssistant().addSkillXP(300, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");								
									}
								break;
								
								case 4834:
								case 4832: // dragon bones + 500 exp
								case 4830:
								case 4812:
								case 534:
								case 536:
									if (d.Lootshare) {
										String WeaponName =Server.getItemManager().getItemDefinition(NpcDrops.NPC_DROPS2[npc][1]).getName().toLowerCase();
										WeaponName = WeaponName.replaceAll("_", " ");
										WeaponName = WeaponName.trim();	
										d.getActionAssistant().addSkillXP(500, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");
										Iterator iterator = d.lootShareDistrib.keySet().iterator();
										while (iterator.hasNext()) {
											Client c = (Client) iterator.next();
											int damage = d.lootShareDistrib.get(c);
											if (c == null) {
												continue;
											}
											c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+NpcDrops.NPC_DROPS2[npc][2]+"x " +WeaponName);;
										}
									} else {
										d.getActionAssistant().addSkillXP(500, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");								
									}
								break;
								
								case 6729: // daggonoth bones 600 exp
									if (d.Lootshare) {
										String WeaponName =Server.getItemManager().getItemDefinition(NpcDrops.NPC_DROPS2[npc][1]).getName().toLowerCase();
										WeaponName = WeaponName.replaceAll("_", " ");
										WeaponName = WeaponName.trim();	
										d.getActionAssistant().addSkillXP(600, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");
										Iterator iterator = d.lootShareDistrib.keySet().iterator();
										while (iterator.hasNext()) {
											Client c = (Client) iterator.next();
											int damage = d.lootShareDistrib.get(c);
											if (c == null) {
												continue;
											}
											c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+NpcDrops.NPC_DROPS2[npc][2]+"x " +WeaponName);;
										}
									} else {
										d.getActionAssistant().addSkillXP(600, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");								
									}								
								break;
								
								case 18830:
								case 18832:// frost dragon bones 1k exp
									if (d.Lootshare) {
										String WeaponName =Server.getItemManager().getItemDefinition(NpcDrops.NPC_DROPS2[npc][1]).getName().toLowerCase();
										WeaponName = WeaponName.replaceAll("_", " ");
										WeaponName = WeaponName.trim();	
										d.getActionAssistant().addSkillXP(1000, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");
										Iterator iterator = d.lootShareDistrib.keySet().iterator();
										while (iterator.hasNext()) {
											Client c = (Client) iterator.next();
											int damage = d.lootShareDistrib.get(c);
											if (c == null) {
												continue;
											}
											c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+NpcDrops.NPC_DROPS2[npc][2]+"x " +WeaponName);;
										}
									} else {
										d.getActionAssistant().addSkillXP(1000, 5);
										d.getActionAssistant().sendMessage("@blu@Your bone crusher turns your bones into exp");								
									}
								break;
								default:
									FloorItem item = new FloorItem(NpcDrops.NPC_DROPS2[npc][1], NpcDrops.NPC_DROPS2[npc][2], d, getAbsX(), getAbsY(), getHeightLevel());
									Server.getItemManager().newDropFromNPC(item, d);
									if (d.Lootshare) {
										String WeaponName =Server.getItemManager().getItemDefinition(NpcDrops.NPC_DROPS2[npc][1]).getName().toLowerCase();
										WeaponName = WeaponName.replaceAll("_", " ");
										WeaponName = WeaponName.trim();	
										Iterator iterator = d.lootShareDistrib.keySet().iterator();
										while (iterator.hasNext()) {
											Client c = (Client) iterator.next();
											int damage = d.lootShareDistrib.get(c);
											if (c == null) {
												continue;
											}
											c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+NpcDrops.NPC_DROPS2[npc][2]+"x " +WeaponName);;
										}
									}								
								break;
							}
						} else {					
							FloorItem item = new FloorItem(NpcDrops.NPC_DROPS2[npc][1], NpcDrops.NPC_DROPS2[npc][2], d, getAbsX(), getAbsY(), getHeightLevel());
							Server.getItemManager().newDropFromNPC(item, d);
							Server.getItemManager().rareDrop(d, NpcDrops.NPC_DROPS2[npc][1], NpcDrops.NPC_DROPS2[npc][2]);
							if (d.Lootshare) {
								String WeaponName = Server.getItemManager().getItemDefinition(NpcDrops.NPC_DROPS2[npc][1]).getName().toLowerCase();
								WeaponName = WeaponName.replaceAll("_", " ");
								WeaponName = WeaponName.trim();	
								Iterator iterator = d.lootShareDistrib.keySet().iterator();
								while (iterator.hasNext()) {
									Client c = (Client) iterator.next();
									int damage = d.lootShareDistrib.get(c);
									if (c == null) {
										continue;
									}
									c.getActionAssistant().sendMessage("@red@"+d.getPlayerName() +" received: "+NpcDrops.NPC_DROPS2[npc][2]+" x " +WeaponName);
								}
							}
						}	
					}
				}
			}
			d.lootShareDistrib.clear();
			/*X mas Event Random Drop
			if(Misc.random(50) == 21) {
				FloorItem b = new FloorItem(19864, 1, d, getAbsX(), getAbsY(), getHeightLevel());
				Server.getItemManager().newDropFromNPC(b, d);
			}			
			//New Years Event Random Drop
			if(Misc.random(200) == 43) {
				FloorItem b = new FloorItem(14664, 1, d, getAbsX(), getAbsY(), getHeightLevel());
				Server.getItemManager().newDropFromNPC(b, d);
			}
			*/
			if(Misc.random(50) == 30) {
				FloorItem b = new FloorItem(d.getActionAssistant().randomGem(), 1, d, getAbsX(), getAbsY(), getHeightLevel());
				Server.getItemManager().newDropFromNPC(b, d);
			}
	
			if(Misc.random(50) == 15) {
				FloorItem b = new FloorItem(d.getActionAssistant().randomHerb(), 1, d, getAbsX(), getAbsY(), getHeightLevel());
				Server.getItemManager().newDropFromNPC(b, d);
			}	
			if(Misc.random(100) == 69) {
				FloorItem b = new FloorItem(d.getActionAssistant().randomOre(), 30, d, getAbsX(), getAbsY(), getHeightLevel());
				Server.getItemManager().newDropFromNPC(b, d);
			}	
			if(Misc.random(200) == 102) {
				FloorItem b = new FloorItem(d.getActionAssistant().randomBar(), 20, d, getAbsX(), getAbsY(), getHeightLevel());
				Server.getItemManager().newDropFromNPC(b, d);
			}	
			if(Misc.random(50) == 20) {
				FloorItem b = new FloorItem(d.getActionAssistant().randomBolt(), Misc.random(300), d, getAbsX(), getAbsY(), getHeightLevel());
				Server.getItemManager().newDropFromNPC(b, d);
			}			
			if(Misc.random(1000) == 369) {
				FloorItem b = new FloorItem(2366, 1, d, getAbsX(), getAbsY(), getHeightLevel());
				Server.getItemManager().newDropFromNPC(b, d);
			}	
			if(Misc.random(1000) == 489) {
				FloorItem b = new FloorItem(2368, 1, d, getAbsX(), getAbsY(), getHeightLevel());
				Server.getItemManager().newDropFromNPC(b, d);
			}	
		d.inCombat = false;
		d.isFighting = false;			
		}

	}
	
	//}
	public boolean inWild() {
		if((absX > 2941 && absX < 3392 && absY > 3518 && absY < 3967) ||
		  (absX > 3009 && absX < 3060 && absY > 10303 && absY < 10356) ||
		  (absX > 3078 && absX < 3134 && absY > 9918 && absY < 9999)
		  
		  ){
			return true;
		}
		return false;
	}
	/**
	 * Processing.
	 */
	 int placeHolder = -1;
	public void process() {
		if (petOwner != 0) {
			Client k = (Client)	PlayerManager.getSingleton().getPlayers()[petOwner];
			if (k != null) {
				NPCPathFinder.follow(this, k);
				if (Config.WORLD_NUMBER == 2 || Config.WORLD_NUMBER == 4) {
					if (k.inDuelArena()) {
						Server.getNpcManager().deleteNPC(npcId);
					}
				}
				if(k.inWild()) {
					Server.getNpcManager().deleteNPC(npcId);
				}
			}
			else {
				Server.getNpcManager().deleteNPC(npcId);
			}
		}
			if(definition.type == 14696){
				if (Config.WORLD_NUMBER != 1 && Config.WORLD_NUMBER != 2){
					Server.getNpcManager().deleteNPC(npcId);
				}
			}
				//annoyNpcs(npcId);
				if (actionTimer > 0) {
					actionTimer--;
				}
				
				if (freezeTimer > 0) {
					freezeTimer--;
				}
				
				if (hitDelayTimer > 0) {
					hitDelayTimer--;
				}
				
				if (hitDelayTimer == 1) {
					hitDelayTimer = 0;
					applyDamage(definition.type);
				}
				if (System.currentTimeMillis() - lastHit > 4500) {
					underAttack2 = false;
				}
				if(attackTimer > 0) {
					attackTimer--;
				}
				if(underAttack && !walkingHome) {
					if(!isDead) {
						int p = killerId;
						if(PlayerManager.getSingleton().getPlayers()[p] != null ) {
							Client c = (Client)	PlayerManager.getSingleton().getPlayers()[killerId];
							//followPlayer(c);
								NPCPathFinder.follow(this, c);
							if(attackTimer == 0) {						
								if(PlayerManager.getSingleton().getPlayers()[p] != null) {
									attackPlayer(c, npcId);								
								} else {
									killerId = 0;
									underAttack = false;
									killedBy = 0;
									facePlayer(0);
								}
							}
						} else {
							killerId = 0;
							underAttack = false;
							killedBy = 0;
							facePlayer(0);
						}

					}	
				}
				if(!underAttack2 && randomWalk && !isDead && !isDeadWaiting){
					annoyNpcs(npcId);
				}
			
				/***
				* Random walking and walking home
				***/
			/*	if((!underAttack || walkingHome) && !isDead){
					//annoyNpcs(npcId);
					//killerId = 0;
					//underAttack = false;
					//killedBy = 0;
					//facePlayer(0);
					if(definition.type == 127) {
					if((getAbsX() > makeX + 2) || (getAbsX() < makeX - 2) || (getAbsY() > makeY + 2) || (getAbsY() < makeY - 2)) {
						walkingHome = true;
					} else if((getAbsX() > makeX + Config.NPC_RANDOM_WALK_DISTANCE) || (getAbsX() < makeX - Config.NPC_RANDOM_WALK_DISTANCE) || (getAbsY() > makeY + Config.NPC_RANDOM_WALK_DISTANCE) || (getAbsY() < makeY - Config.NPC_RANDOM_WALK_DISTANCE)) {
						walkingHome = true;
					}
					}
					if (walkingHome && getAbsX() == makeX && getAbsY() == makeY) {
						walkingHome = false;
					} else if(walkingHome) {
						moveX = GetMove(getAbsX(), makeX);
			      		moveY = GetMove(getAbsY(), makeY);
						getNextNPCMovement(definition.type);
						updateRequired = true;
					}
					if(walkingType == 1) {
						if(Misc.random(6)== 1 && !walkingHome) {
							int MoveX = 0;
							int MoveY = 0;			
							int Rnd = Misc.random(9);
							if (Rnd == 1) {
								MoveX = 1;
								MoveY = 1;
							} else if (Rnd == 2) {
								MoveX = -1;
							} else if (Rnd == 3) {
								MoveY = -1;
							} else if (Rnd == 4) {
								MoveX = 1;
							} else if (Rnd == 5) {
								MoveY = 1;
							} else if (Rnd == 6) {
								MoveX = -1;
								MoveY = -1;
							} else if (Rnd == 7) {
								MoveX = -1;
								MoveY = 1;
							} else if (Rnd == 8) {
								MoveX = 1;
								MoveY = -1;
							}
										
							if (MoveX == 1) {
								if (getAbsX() + MoveX < makeX + 1) {
									moveX = MoveX;
								} else {
									moveX = 0;
								}
							}
							
							if (MoveX == -1) {
								if (getAbsX() - MoveX > makeX - 1)  {
									moveX = MoveX;
								} else {
									moveX = 0;
								}
							}
							
							if(MoveY == 1) {
								if(getAbsY() + MoveY < makeY + 1) {
									moveY = MoveY;
								} else {
									moveY = 0;
								}
							}
							
							if(MoveY == -1) {
								if(getAbsY() - MoveY > makeY - 1)  {
									moveY = MoveY;
								} else {
									moveY = 0;
								}
							}
							if(!VirtualWorld.I(heightLevel, getAbsX(), getAbsY(), (getAbsX() + moveX), 
										(getAbsY() + moveY), 0))
							{
								moveX = 0;
								moveY = 0;
							}
							
							
					int x = (getAbsX() + moveX);
					int y = (getAbsY() + moveY);		
					if (VirtualWorld.I(heightLevel, getAbsX(), getAbsY(), getAbsX()-moveX, getAbsY()-moveY, 0))
						getNextNPCMovement(definition.type);
					else {
						moveX = 0;
						moveY = 0;
					}        							
					updateRequired = true;
						}
					}
				}*/		
		if (isDead) {
			if (!isDeadWaiting && !isDeadTeleporting) {
				animNumber = AnimationManager.getDeathAnimation(this);
				animUpdateRequired = true;
				updateRequired = true;
				isDeadWaiting = true;
				underAttack = false;
				if(npcInMulti()) {
					killedBy = getNpcKillerId(definition.type);
				} else {
					killedBy = killerId;
				}
				isDeadTimer = 4;
				freezeTimer = 0;
				if (isWalking) {
					wasWalking = true;
				}
				Client c = (Client) getAttacker();
				if(c != null) {
					c.isFighting = false;
					c.underAttackByNPC = 0;
					if(c.sounds == 1) {
						c.getActionAssistant().frame174(getNpcDeadSound(), 000, 000);	
					}	
					c.isFighting = false;
					/*if (definition.type == 3803 ||definition.type == 3804 ||definition.type == 3805 ||
					    definition.type == 3806 ||definition.type == 3807 ||definition.type == 3808 ||
						definition.type == 3809 ||definition.type == 2627 ||definition.type == 2628 ||
						definition.type == 2738 ||definition.type == 2629 ||definition.type == 2630 ||
						definition.type == 2631 ||definition.type == 2632 ||definition.type == 2741 ||
						definition.type == 2742 ||definition.type == 2743 ||definition.type == 2744 ||
						definition.type == 2745 ||definition.type == 2746) {
						AnimationManager.getDeathAnimation(this);
						animUpdateRequired = true;
						updateRequired = true;
						processDrops();
						isToBeRemoved = true;
						c.oldNpcIndex = 0;
						c.npcIndex = 0;
						c.getCombat().resetAttack();
						animUpdateRequired = true;
						updateRequired = true;
						isDeadWaiting = false;
						isHidden = true;
					}				
					if (definition.type == 3777) {
						PlayerManager.portal1 = true;
						isToBeRemoved = true;
						c.oldNpcIndex = 0;
						c.npcIndex = 0;
						c.getCombat().resetAttack();
						animUpdateRequired = true;
						updateRequired = true;
						isDeadWaiting = false;
						isHidden = true;
					}
					if (definition.type == 3778) {
						PlayerManager.portal2 = true;
						isToBeRemoved = true;
						c.oldNpcIndex = 0;
						c.npcIndex = 0;
						c.getCombat().resetAttack();
						animUpdateRequired = true;
						updateRequired = true;
						isDeadWaiting = false;
						isHidden = true;
					}
					if (definition.type == 3779) {
						PlayerManager.portal3 = true;
						isToBeRemoved = true;
						c.oldNpcIndex = 0;
						c.npcIndex = 0;
						c.getCombat().resetAttack();
						animUpdateRequired = true;
						updateRequired = true;
						isDeadWaiting = false;
						isHidden = true;
					}
					if (definition.type == 3780) {
						PlayerManager.portal4 = true;
						isToBeRemoved = true;
						c.oldNpcIndex = 0;
						c.npcIndex = 0;
						c.getCombat().resetAttack();
						animUpdateRequired = true;
						updateRequired = true;
						isDeadWaiting = false;
						isHidden = true;
					}*/
					/*if (absX >= 2800 && absX <= 3000 && absY >= 5245 && absY <= 5385) {
						if (definition.type == 116 || definition.type == 113 || definition.type == 71 || definition.type == 123 || definition.type == 101 || definition.type == 122 || definition.type == 102 || definition.type == 100 ) {
							if (c.bandosPoints == enterboss) {
								c.getActionAssistant().sendMessage("@red@You already have "+enterboss+ " kills! Enter the bandos room!");
								}
							else {
								c.bandosPoints += 1;
								c.getActionAssistant().updateKillCount(1);
							}
						}
						else if ( definition.type == 3823 || definition.type == 3829 ) {
							if (c.saraPoints == enterboss) {
								c.getActionAssistant().sendMessage("@red@You already have "+enterboss+ " kills! Enter the sara room!");
								}
							else {
								c.saraPoints += 1;
								c.getActionAssistant().updateKillCount(3);
							}
						}
						else if ( definition.type == 182 || definition.type == 3821 ) {
							if (c.armaPoints == enterboss) {
								c.getActionAssistant().sendMessage("@red@You already have "+enterboss+ " kills! Enter the arma room!");
								}
							else {
								c.armaPoints += 1;
								c.getActionAssistant().updateKillCount(0);
							}
						}
						else if ( definition.type == 6221 || definition.type == 1618 ||  definition.type == 83 || definition.type == 1030 || definition.type == 1031 || definition.type == 1032 || definition.type == 708 ) {
							if (c.zamPoints == enterboss) {
								c.getActionAssistant().sendMessage("@red@You already have "+enterboss+ " kills! Enter the boss room!");
								}
							else {
								c.zamPoints += 1;
								c.getActionAssistant().updateKillCount(2);
							}
						}
					}*/
					
					if(definition.type == 6203){
						c.zam++;
						if(c.zam >= 150){
							c.NRAchievements.checkMonsters(c,3);
						}
					}
					if(definition.type == 6247) {
						c.sara++;
						if(c.sara >= 150){
							c.NRAchievements.checkMonsters(c,4);
						}
					}
					
					if(definition.type == 1158 || definition.type == 1159 || definition.type == 3836) {
						c.cali++;
						if(c.cali >= 100){
							c.NRAchievements.checkMonsters(c,1);
						}
					}
					if(definition.type == 8366){
						c.td++;
						if(c.td >= 100){
							c.NRAchievements.checkMonsters(c,2);
						}
					}
					if (definition.type == 50){
						c.kbd++;
						if(c.kbd >= 100){
							c.NRAchievements.checkMonsters(c,0);
						}
					}
					if (definition.type == 795) {
					c.achievementProgress[6] += 1;
					if (c.achievementProgress[6] == 500) {
						c.NRAchievements.checkMonsters(c,6);
					}
					}
					if (definition.type == 8596 || definition.type == 8597) {
					c.achievementProgress[7] += 1;
					if (c.achievementProgress[7] == 150) {
						c.NRAchievements.checkMonsters(c,7);
					}
					}
					if(definition.type == 2025 || definition.type == 2026 ||  definition.type == 2027 ||  definition.type == 2028 || 
					   definition.type == 2029 || definition.type == 2030){
						c.barrow++;
						if(c.barrow >= 100){
							c.NRAchievements.checkMonsters(c,5);
						}
					}					   
					int slaymultiple = 2;
				if (definition.type == 1653 && c.slayerTask == 1653) {
					c.getActionAssistant().addSkillXP((slaymultiple*5), 18);
					c.getActionAssistant().refreshSkill(18); //hand
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
					
				}
				if (definition.type == 1676 && c.slayerTask == 1677) {
					c.getActionAssistant().addSkillXP((slaymultiple*85), 18);
					c.getActionAssistant().refreshSkill(18); //experiment
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}					
				if (definition.type == 1677 && c.slayerTask == 1677) {
					c.getActionAssistant().addSkillXP((slaymultiple*85), 18);
					c.getActionAssistant().refreshSkill(18); //experiment
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type == 122 && c.slayerTask == 122) {
					c.getActionAssistant().addSkillXP((slaymultiple*25), 18);
					c.getActionAssistant().refreshSkill(18); //hobgoblin
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}		
				if (definition.type == 123 && c.slayerTask == 122) {
					c.getActionAssistant().addSkillXP((slaymultiple*25), 18);
					c.getActionAssistant().refreshSkill(18); //hobgoblin
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}				
				if (definition.type == 1265 && c.slayerTask == 1265) {
					c.getActionAssistant().addSkillXP((slaymultiple*5), 18);
					c.getActionAssistant().refreshSkill(18); //rock crab
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type == 1832 && c.slayerTask == 1832 ) {
					c.getActionAssistant().addSkillXP((slaymultiple*5), 18);
					c.getActionAssistant().refreshSkill(18); //cav bug
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type == 1601 && c.slayerTask == 1601 ) {
					c.getActionAssistant().addSkillXP((slaymultiple*25), 18);
					c.getActionAssistant().refreshSkill(18); //cave crawler
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type == 1612 && c.slayerTask == 1612) {
					c.getActionAssistant().addSkillXP((slaymultiple*22), 18);
					c.getActionAssistant().refreshSkill(18); //banshe
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}		
				if (definition.type == 1831 && c.slayerTask == 1831) {
					c.getActionAssistant().addSkillXP((slaymultiple*25), 18);
					c.getActionAssistant().refreshSkill(18); //cave slime
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}		
				if (definition.type == 1622 && c.slayerTask == 1622) {
					c.getActionAssistant().addSkillXP((slaymultiple*27), 18);
					c.getActionAssistant().refreshSkill(18); //rock slug
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type == 1620 && c.slayerTask == 1620) {
					c.getActionAssistant().addSkillXP((slaymultiple*37), 18);
					c.getActionAssistant().refreshSkill(18); //cockatrice
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type == 112 && c.slayerTask == 112 ) {
					c.getActionAssistant().addSkillXP((slaymultiple*60), 18);
					c.getActionAssistant().refreshSkill(18); //moss giant
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type == 117 && c.slayerTask == 117) {
					c.getActionAssistant().addSkillXP((slaymultiple*35), 18);
					c.getActionAssistant().refreshSkill(18); //hill giant
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type == 1633 && c.slayerTask == 1633) {
					c.getActionAssistant().addSkillXP((slaymultiple*45), 18);
					c.getActionAssistant().refreshSkill(18); //Pyrefiend
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type ==  941 && c.slayerTask == 941) {
					c.getActionAssistant().addSkillXP((slaymultiple*75), 18);
					c.getActionAssistant().refreshSkill(18); //green dragon
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type == 55 && c.slayerTask == 55 ) {
					c.getActionAssistant().addSkillXP((slaymultiple*112), 18);
					c.getActionAssistant().refreshSkill(18); //blue dragon
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type == 1632 && c.slayerTask == 1632 ) {
					c.getActionAssistant().addSkillXP((slaymultiple*88), 18);
					c.getActionAssistant().refreshSkill(18); //turoth
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type == 1609 && c.slayerTask == 1609) {
					c.getActionAssistant().addSkillXP((slaymultiple*97), 18);
					c.getActionAssistant().refreshSkill(18); //kurask
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type == 84 && (c.slayerTask == 84 || c.slayerTask == 677)) {
					c.getActionAssistant().addSkillXP((slaymultiple*157), 18);
					c.getActionAssistant().refreshSkill(18); //black demon
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}	
				if (definition.type == 677 && (c.slayerTask == 677 || c.slayerTask == 84)) {
					c.getActionAssistant().addSkillXP((slaymultiple*157), 18);
					c.getActionAssistant().refreshSkill(18); //black demon
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}				
				if (definition.type == 83  && c.slayerTask == 83) {
					c.getActionAssistant().addSkillXP((slaymultiple*157), 18);
					c.getActionAssistant().refreshSkill(18); //Greater Demon
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}						
				if (definition.type == 1616 && c.slayerTask == 1616) {
					c.getActionAssistant().addSkillXP((slaymultiple*75), 18);
					c.getActionAssistant().refreshSkill(18); //Basilisk
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}
				if (definition.type == 1637 && c.slayerTask == 1637) {
					c.getActionAssistant().addSkillXP((slaymultiple*75), 18);
					c.getActionAssistant().refreshSkill(18); //jelly
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}
				if (definition.type == 1618 && c.slayerTask == 1619) {
					c.getActionAssistant().addSkillXP((slaymultiple*120), 18);
					c.getActionAssistant().refreshSkill(18);//Bloodveld
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}
				if (definition.type == 1619 && c.slayerTask == 1619) {
					c.getActionAssistant().addSkillXP((slaymultiple*120), 18);
					c.getActionAssistant().refreshSkill(18);//Bloodveld
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}				
				if (definition.type == 1643 && c.slayerTask == 1643) {
					c.getActionAssistant().addSkillXP((slaymultiple*60), 18);
					c.getActionAssistant().refreshSkill(18);//Infernal Mage
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}
				if (definition.type == 1604 && c.slayerTask == 1604) {
					c.getActionAssistant().addSkillXP((slaymultiple*60), 18);
					c.getActionAssistant().refreshSkill(18);//Abby Specs
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}
				if (definition.type == 1624 && c.slayerTask == 1624) {
					c.getActionAssistant().addSkillXP((slaymultiple*105), 18);
					c.getActionAssistant().refreshSkill(18);//Dust Devil
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}
				if (definition.type == 1610 && c.slayerTask == 1610) {
					c.getActionAssistant().addSkillXP((slaymultiple*105), 18);
					c.getActionAssistant().refreshSkill(18);//Garg
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}
				if (definition.type == 1613 && c.slayerTask == 1613) {
					c.getActionAssistant().addSkillXP((slaymultiple*105), 18);
					c.getActionAssistant().refreshSkill(18);//Nech
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}
				if (definition.type == 1615 && c.slayerTask == 1615) {
					c.getActionAssistant().addSkillXP((slaymultiple*150), 18);
					c.getActionAssistant().refreshSkill(18);//Abbys
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}
				if (definition.type == 2783 && c.slayerTask == 2783) {
					c.getActionAssistant().addSkillXP((slaymultiple*225), 18);
					c.getActionAssistant().refreshSkill(18);//Dark beast
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}
				if (definition.type == 2783 && c.slayerTask == 2783) {
					c.getActionAssistant().addSkillXP((slaymultiple*225), 18);
					c.getActionAssistant().refreshSkill(18);//Dark beast
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}
				if ((definition.type == 6221 || definition.type == 6257 || definition.type ==6231 || definition.type ==6231
						|| definition.type == 6221 ||definition.type == 6257 || definition.type == 6278 || definition.type == 1643 
						|| definition.type == 1644  || definition.type == 1645 || definition.type ==  1646  || definition.type == 1647
					) && c.slayerTask == 6221) {
					c.getActionAssistant().addSkillXP((slaymultiple*150), 18);
					c.getActionAssistant().refreshSkill(18);//spirit mage
					c.getActionAssistant().requestUpdates();
					c.slayerAmount -= 1;
					if(c.slayerAmount <= 0){
						c.slayerAmount = -1;
						c.slayerTask = -1;
						c.slayerPoints += 1;
						c.getActionAssistant().sendMessage("You have completed your task. Talk to a slayer master to get a new one.");
					}
				}
					
				}
				isWalking = false;
			} else if (isDeadTeleporting) {
				if (isDeadTimer == 0) {
					if(definition.type == 1532){
						for(int i = 0; i < Server.getCastleWars().barricades.size(); i++){
							if(Server.getCastleWars().barricades.get(i).getId() == npcId){	
									Server.getNpcManager().deleteNPC(npcId);
									Server.getCastleWars().barricades.remove(Server.getCastleWars().barricades.get(i));
								//	Server.getCastleWars().barricade[Server.getCastleWars().barricades.get(i).getHolder()]--;
								break;
							}
						}
						return;
					}
					isDead = false;
					isHidden = false;
					if (wasWalking) {
						isWalking = true;
					}
					hp = maxHP;
					isDeadTeleporting = false;
					isDead = false;
					if(definition.type == 14696){
					int type = Misc.random(5);
						switch(type){
							case 0:
							case 1:
							case 2:
							case 3:
							case 4:
								PlayerManager.getSingleton().sendGlobalMessage(
								"[@red@Server@bla@]"+spawnMessages[type]);
							try {
								String query = "INSERT INTO servermessage(playername,message,prefix,world) values ('','" + spawnMessages[type]+"','Server',";
								String tempquery = "";
								for (int i=1; i <= Config.MAX_WORLDS; i++) {
									if (i != Config.WORLD_NUMBER) {
										tempquery = query + Integer.toString(i) + ");";
										statement.executeUpdate(tempquery);
									}
								}
							}
							catch (Exception e) 
							{
								e.printStackTrace();
							}
								setAbsX(spawns[type][0]);
								setAbsY(spawns[type][1]);
								break;
						default:
								PlayerManager.getSingleton().sendGlobalMessage(
								"[@red@Server@bla@]"+spawnMessages[type]);
							try {
								String query = "INSERT INTO servermessage(playername,message,prefix,world) values ('','" + spawnMessages[type]+"','Server',";
								String tempquery = "";
								for (int i=1; i <= Config.MAX_WORLDS; i++) {
									if (i != Config.WORLD_NUMBER) {
										tempquery = query + Integer.toString(i) + ");";
										statement.executeUpdate(tempquery);
									}
								}
							}
							catch (Exception e) 
							{
								e.printStackTrace();
							}
								setAbsX(spawns[0][0]);
								setAbsY(spawns[0][1]);
						}
						for(int i = 0; i < messagesSent.length; i++)
							messagesSent[i] = false;
						phase = 0;
						
					} else {
						setAbsX(spawnAbsX);
						setAbsY(spawnAbsY);
					}
				}
				if(definition.type == 14696){
					switch(isDeadTimer){
						case 5400:
						case 3600:
						case 1800:
						case 1200:
						case 600:
						case 120:
							PlayerManager.getSingleton().sendGlobalMessage(
								"[@red@Server@bla@]:The beast will be spawning in "+(isDeadTimer/120)+ " min(s)");
							try {
								String query = "INSERT INTO servermessage(playername,message,prefix,world) values ('','[@red@Server@bla@]:The beast will be spawning in "+(isDeadTimer/120)+ " min(s)','Server',";
								String tempquery = "";
								for (int i=1; i <= Config.MAX_WORLDS; i++) {
									if (i != Config.WORLD_NUMBER) {
										tempquery = query + Integer.toString(i) + ");";
										statement.executeUpdate(tempquery);
									}
								}
							}
							catch (Exception e) 
							{
								e.printStackTrace();
							}
						break;
					
					}
				}
				isDeadTimer--;
			} else {
				if (isDeadTimer == 0) {
					animUpdateRequired = true;
					updateRequired = true;
					isDeadWaiting = false;
					isHidden = true;
					isDeadTimer = definition.getRespawn();
					isDeadTeleporting = true;
					processDrops();
					killerId = 0;
				}
				isDeadTimer--;
			}
		}
	}

	/**
	 * Appends the NPC update block
	 * 
	 * @param str
	 */
	/**
	 * Appends the NPC update block
	 * 
	 * @param str
	 */
	 public int gfxId;
	 public int viewX, viewY, moveX, moveY;
	 public int gfxDelay, animDelay, faceX, faceY, transformId;
	 public boolean secondHitUpdateRequired, gfxUpdateRequired, faceToEntityUpdateRequired, transformUpdateRequired, faceToUpdateRequired;
	/**
	*
	Graphics
	*
	**/	
	
	public int mask80var1 = 0;
    public int mask80var2 = 0;
    protected boolean mask80update = false;
	
    public void appendMask80Update(Stream str) {
		str.writeWord(mask80var1);
	    str.writeDWord(mask80var2);
    }
    public void delayedGfx(int gfx, int delay , int height) {
		mask80var1 = gfx;
		mask80var2 = delay+(65536*height);
        mask80update = true;
		updateRequired = true;
    }		
	public void gfx100(int gfx){
		mask80var1 = gfx;
        mask80var2 = 6553600;
        mask80update = true;
		updateRequired = true;
	}
	
	public void gfx0(int gfx){
		mask80var1 = gfx;
        mask80var2 = 65536;
        mask80update = true;
		updateRequired = true;
	}
	
	public void appendAnimUpdate(Stream str) {
		str.writeWordBigEndian(animNumber);
		str.writeByte(1);
	}
	
	public void appendHitUpdate(Stream str) {		
		if (hp <= 0) {
			isDead = true;
		}
		str.writeByteC(hit); 
		if (hit > 0) {
			str.writeByteS(1); 
		} else {
			str.writeByteS(0); 
		}
		str.writeByteS(Misc.getCurrentHP(hp, maxHP, 100));
		str.writeByteC(100);		
		//str.writeByteS(hp); 
		//str.writeByteC(maxHP); 	
	}
	public int hit2,hit3,hit4;
	public void appendHitUpdate2(Stream str) {		
		if (hp <= 0) {
			isDead = true;
		}
		str.writeByteA(hit2); 
		if (hit2 > 0) {
			str.writeByteC(1); 
		} else {
			str.writeByteC(0); 
		}	
		str.writeByteS(Misc.getCurrentHP(hp, maxHP, 100));
		str.writeByteC(100); 	
	}	
	public void appendHitUpdate3(Stream str) {		
		if (hp <= 0) {
			isDead = true;
		}
		str.writeByteA(hit3); 
		if (hit3 > 0) {
			str.writeByteC(1); 
		} else {
			str.writeByteC(0); 
		}	
		str.writeByteS(Misc.getCurrentHP(hp, maxHP, 100));
		str.writeByteC(100); 	
	}		
		public void appendHitUpdate4(Stream str) {		
		if (hp <= 0) {
			isDead = true;
		}
		str.writeByteA(hit4); 
		if (hit4 > 0) {
			str.writeByteC(1); 
		} else {
			str.writeByteC(0); 
		}	
		str.writeByteS(Misc.getCurrentHP(hp, maxHP, 100));
		str.writeByteC(100); 	
	}	
	/*public void appendHitUpdate(Stream str) {
		str.writeByteC(hit); // What the perseon got 'hit' for
		str.writeByteS(hitType); // 0: red hitting - 1: blue hitting
		str.writeByteS(hp); // Their current hp, for HP bar
		str.writeByteC(maxHP); // Their max hp, for HP bar
	}*/
	/**
	*
	Face
	*
	**/
	
	public int FocusPointX = -1, FocusPointY = -1;
	public int face = 0,HP, MaxHP, hitDiff, hitDiff2;
	
	private void appendSetFocusDestination(Stream str) {
        str.writeWordBigEndian(FocusPointX);
        str.writeWordBigEndian(FocusPointY);
    }
	
	public void turnNpc(int i, int j) {
        FocusPointX = 2 * i + 1;
        FocusPointY = 2 * j + 1;
        updateRequired = true;

    }
	
	public void appendFaceEntity(Stream str) {
		str.writeWord(face);
	}
        	
	public void facePlayer(int player) {
		face = player + 32768;
		dirUpdateRequired = true;
		updateRequired = true;
	}

	public void appendFaceToUpdate(Stream str) {
			str.writeWordBigEndian(viewX);
			str.writeWordBigEndian(viewY);
			
	}
	
	
	public void appendNPCUpdateBlock(Stream str) {
		if(!updateRequired) return ;		
		int updateMask = 0;
		if(animUpdateRequired) updateMask |= 0x10;
		if(hitUpdateRequired2) updateMask |= 8;
		if(mask80update) updateMask |= 0x80;
		if(textUpdateRequired) updateMask |= 1;
		if(dirUpdateRequired) updateMask |= 0x20;
		if(hitUpdateRequired) updateMask |= 0x40;	
		if(FocusPointX != -1) updateMask |= 4;		
			
		str.writeByte(updateMask);
				
		if (animUpdateRequired) appendAnimUpdate(str);
		if (hitUpdateRequired2) appendHitUpdate2(str);

		if (mask80update)       appendMask80Update(str);
		if (dirUpdateRequired)  appendFaceEntity(str);
		if(textUpdateRequired) 
		{
			str.writeString(textUpdate);
		}
		if (hitUpdateRequired)  appendHitUpdate(str);
		if(FocusPointX != -1) appendSetFocusDestination(str);
		
	}

	public void clearUpdateFlags() {
		updateRequired = false;
		textUpdateRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		hitUpdateRequired3 = false;
		hitUpdateRequired4 = false;
		animUpdateRequired = false;
		dirUpdateRequired = false;
		mask80update = false;
		textUpdate = null;
		moveX = 0;
		moveY = 0;
		direction = -1;
		FocusPointX = -1;
		FocusPointY = -1;
	}

	/**
	 * Updates NPC movement
	 * 
	 * @param str
	 */
	public void updateNPCMovement(Stream str) {
		if (direction == -1) {
			// don't have to update the npc position, because the npc is just
			// standing
			if (updateRequired) {
				// tell client there's an update block appended at the end
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else
				str.writeBits(1, 0);
		} else {
			// send "walking packet"
			str.writeBits(1, 1);
			str.writeBits(2, 1); // updateType
			str.writeBits(3, Misc.xlateDirectionToClient[direction]);
			if (updateRequired)
				str.writeBits(1, 1); // tell client there's an update block
										// appended at the end
			else
				str.writeBits(1, 0);
		}
	}

	/**
	 * @param definition
	 *            the definition to set
	 */
	public void setDefinition(NPCDefinition definition) {
		this.definition = definition;
	}

	/**
	 * @return the definition
	 */
	public NPCDefinition getDefinition() {
		return definition;
	}

	/**
	 * @param animNumber
	 *            the animNumber to set
	 */
	public void setAnimNumber(int animNumber) {
		this.animNumber = animNumber;
	}

	/**
	 * @return the animNumber
	 */
	public int getAnimNumber() {
		return animNumber;
	}

	/**
	 * @param animUpdateRequired
	 *            the animUpdateRequired to set
	 */
	public void setAnimUpdateRequired(boolean animUpdateRequired) {
		this.animUpdateRequired = animUpdateRequired;
	}

	/**
	 * @return the animUpdateRequired
	 */
	public boolean isAnimUpdateRequired() {
		return animUpdateRequired;
	}

	/**
	 * @param updateRequired
	 *            the updateRequired to set
	 */
	public void setUpdateRequired(boolean updateRequired) {
		this.updateRequired = updateRequired;
	}

	/**
	 * @return the updateRequired
	 */
	public boolean isUpdateRequired() {
		return updateRequired;
	}


	/**
	 * @param attacker
	 *            the attacker to set
	 */
	public void setAttacker(Client attacker) {
		this.attacker = attacker;
	}

	/**
	 * @return the attacker
	 */
	public Client getAttacker() {
		return attacker;
	}

	/**
	 * @return the attackers
	 */
	public Map<Client, Integer> getAttackers() {
		return attackers;
	}

	/**
	 * @param isWalking
	 *            the isWalking to set
	 */
	public void setWalking(boolean isWalking) {
		this.isWalking = isWalking;
		this.randomWalk = true;
	}

	/**
	 * @return the isWalking
	 */
	public boolean isWalking() {
		return isWalking;
	}

	/**
	 * @param wasWalking
	 *            the wasWalking to set
	 */
	public void setWasWalking(boolean wasWalking) {
		this.wasWalking = wasWalking;
	}

	/**
	 * @return the wasWalking
	 */
	public boolean isWasWalking() {
		return wasWalking;
	}

	/**
	 * @param isDead
	 *            the isDead to set
	 */
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	/**
	 * @return the isDead
	 */
	public boolean isDead() {
		return isDead;
	}

	/**
	 * @param npcId
	 *            the npcId to set
	 */
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	/**
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * @param isHidden
	 *            the isHidden to set
	 */
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	/**
	 * @return the isHidden
	 */
	public boolean isHidden() {
		return isHidden;
	}

	/**
	 * @param x1
	 *            the x1 to set
	 */
	public void setX1(int x1) {
		this.x1 = x1;
	}
	public void defence(int defence) {
		this.defence = defence;
	}
	public void attack(int attack) {
		this.attack = attack;
	}
	public void maxHit(int maxHit) {
		this.maxHit = maxHit;
	}
	public void walkingType(int walkingType) {
		this.walkingType = walkingType;
	}
	/**
	 * @return the x1
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * @param y1
	 *            the y1 to set
	 */
	public void setY1(int y1) {
		this.y1 = y1;
	}

	/**
	 * @return the y1
	 */
	public int getY1() {
		return y1;
	}

	/**
	 * @param x2
	 *            the x2 to set
	 */
	public void setX2(int x2) {
		this.x2 = x2;
	}

	/**
	 * @return the x2
	 */
	public int getX2() {
		return x2;
	}

	/**
	 * @param y2
	 *            the y2 to set
	 */
	public void setY2(int y2) {
		this.y2 = y2;
	}

	/**
	 * @return the y2
	 */
	public int getY2() {
		return y2;
	}
	/**
	* Death Sounds
	**/
	public int getNpcDeadSound() {
		
		switch(definition.type) {
			case 47:
			case 86:
			case 87:
			case 88:
			case 224:
			case 446:
			case 748:
			case 950:
			case 978:
			case 2032:
			case 2033:
				return 15;
			case 118:
			case 120:
			case 121:
			case 119:
			case 206:
			case 232:
			case 382:
			case 2423:
				return 905;
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 459:
			case 750:
			case 1471:
			case 1539:
			case 1973:
			case 2036:
			case 2037:
				return 1051;
			default:
			return 70;				

		}
	}
	/**
	* Emotes
	**/
	
	public int getAttackEmote(int i, Client c) {
		switch(definition.type) {		
		case 8528:
		int type = Misc.random(4);
		switch (type){ 
			case 0:
				Maging = true;
				Server.getStillGraphicsManager().stillGraphics(c,
				c.getAbsX(), c.getAbsY(), 0, 1194, 0);
				return 12699;
			case 1:
				Maging = true;
				Server.getStillGraphicsManager().stillGraphics(c,
				c.getAbsX(), c.getAbsY(), 0, 1194, 0);
				return 12701;
			case 2:
			case 3:
				Melee = true;
				return 12697;
			case 4:
				Melee = true;
				return 12696;
		}
		case 15177:
		return Config.anim;
		case 7134:
		return 8754;
		case 14696:
		return 15466;
		case 10110:
		
		return 13001;
		case 10127:
		if (Misc.random(1)== 0) {
		unholymage = false;
		return 13169;
		}
		else {
		unholymage = true;
		return 13172;
		}
		case 5001:
		return Config.anim;
		case 10468:
		case 11125:
		return Config.anim;
		case 9964:
		return 13717;
		case 9051:
		case 9839:
		return Config.anim;
		case 9928:
		return 13337;
		case 9947:
		if (Misc.random(1) == 0) {
		return 13770;
		}
		else {
		return 13775;
		}
		case 5666:
		if (Misc.random(1) == 0) {
		return 5894;
		}
		else {
		return 5895;
		}
		case 3847:
		return 3991;
		case 8549:
		return 11076;
		case 6024:
		return 6536;
		case 8281:
		return 10674;
		case 8282:
		return 10669;
		case 8283:
		return 10681;
		case 78:
		return 4915;
		case 750:
		return 6726;
		case 118:
		case 119:
		case 120:
		case 121:
		return 99;
		case 1648:
			case 1649:
			case 1650:
			case 1651:
			case 1652:
			case 1653:
			case 1654:
			case 1655:
			case 1656:
			case 1657:
			return 9444;
		case 3836:
		if (Misc.random(1) == 1) {
		return 6235;}
		else { return 6234;}
		case 6250:
		return 7018;
		case 6248:
		return 6376;
		case 9911://Har'Lakk the Riftsplitter
		return Config.anim;
		case 9780: //Rammernaut
		if (Misc.random(1) == 1) {
		return 13703;
		}
		else {
		return 13705;
		}
		case 9737://Night-gazer Khighorahk
		return 13422;
case 9463://ICE STRYKEWYRM
		return 12791;
		case 9465://DESERT STRYKEWYRM
		return 12791;
		case 9467://JUNGLE STRYKEWYRM
		return 12791;
				case 4813://vyrewatch
		return 12791;
		case 5188://nial swiftling
		return Config.anim;
				case 5247://PENANCE QUEEN
				return 5411;
				case 10665:
				return Config.anim;
				case 8596://AVATAR OF SOMETHING
				return 11197;
				case 8597://AVATAR OF CREATION
				return 11202;
				case 8366://TORMENTED
					if (Misc.random(10) == 5) {
					gfx100(1885);
					}
				 attackstyle = Misc.random(2);
				if (attackstyle == 0) {
				TdStage = 0;
				return 10919;
				}
				else if (attackstyle == 1) {
				TdStage = 0;
				return 10922;
				}
				else if (attackstyle == 2) { //RANGE
				TdStage = 1;
				return 10918;
				}
				case 8133:
				attackstyle = Misc.random(3);
				if (attackstyle == 0) {
				return 10057;
				}
				else if (attackstyle == 1) {
				return 10058;
				}
					else if (attackstyle == 2) {
					return 10066;
					}
						else if (attackstyle == 3) {
						return 10053;
						}
		case 6225:
		case 6246:
			return 6953;
		case 6223:
			return 6954;
		case 6227:
			return 6953;
		case 6252:
			return 7009;
		case 771:
			return 6376;
		case 770:
			return 7018;
		case 6263:
			return 6154;
		case 6261:
			return 6154;
		case 6204:
			return 6945;
		case 6206:
			return 6945;
		case 6208:
			return 6945;
			case 2745:
				if (Misc.random(8) != 5){
					jadMage = false;
					if (Misc.random(2) == 2){
						jadRange = true;
						return 9276;
					}else{
						jadMelee = true;
						return 9277;
					}
				}else{
					jadMage = true;
					return 9276;
				}
			case 2631:
			case 2632:
				return 2633;
			case 2626:
			case 2627:
				return 2621;
			case 2629:
			case 2630:
			case 2738:
				return 2625;
			case 2744:
			case 2743:
				return 9265;
			case 2741:
			case 2742:
				return 9252;
			case 2591:
				return 2613;				
			case 2604:
				return 2612;
			case 2617:
				return 2609;
			case 2610:
				return 2610;
			case 2619:
				return 2610;				
			case 3259:
				range = true;
				gfx100(19);
				return 426;
			case 127:
				return 185;
			case 1265:
				return 1312;
			case 1622:
				return 9508;
			case 1620:
				return 1562;
			case 1633:
				return 1582;
			case 1632:
				return 1595;
			case 1609:
				return 9439;
			case 1612:
				return 1523;
			case 123:
				return 12405;
			case 122:
				return 12405;
			case 181:
				maging = true;
				if (Misc.random(20) == 2){
					druidMaging = false;
					return 716;
				}else{
					druidMaging = true;}
					gfx100(90);
					return 711;	
			case 90:
			case 91:
			case 92:
			case 93:
			case 3581:
				return 5485;
				case 221:
				return 128;
			case 58:
			case 59:
			case 60:
			case 61:
			case 62:
			case 63:
			case 64:
			case 134:
			case 977:
			case 1004:
			case 1009:
			case 1221:
			case 1473:
			case 1474:
			case 1478:
			case 1524:
			case 2034:
			case 2491:
			case 2492:
			case 2850:
				return 143;
			
			case 100:
			case 102:
			case 101:
			case 298:
			case 299:
			case 444:
			case 445:
				return 309;
			case 89:
			case 133:
			case 987:
				return 289;
			case 6203:
				return 6945;
			case 6222:
				return 6974;
			case 6265:
				return 6154;
			case 6260:
				return 7060;
			case 761:
				return 6977;
			case 3803:
			case 3804:
			case 3805:
			case 3806:
			case 3807:
			case 3809:
				return 451;
			case 6247:
				return 6964;
				
			case 3777:
			case 3778:
			case 3779:
			case 3780:
				return 3933;
			case 1604:
				return 9466;
				case 297:
				return 7218;
			case 1643:
				return 729;
			case 3200:
			return 3146;
			case 1619:
				return 1552;
			case 1616:
			return 260;
			case 1637:
				return 9491;
			case 1624:
				return 1557;
			case 49:
				return 6562;
			case 269:
				return 158;
			case 941:
			case 51:
			case 52:
			case 53:
			case 54:
			case 55:
			case 1589:
			case 1590:
			case 1591:
			case 1592:
			case 5363:
			
				if (Misc.random(8) != 5){
					dFire = false;
					if (Misc.random(2) == 2){
						return 91;
						}else{}
						return 80;
				}else{
					dFire = true;}
					return 81;			
			case 50:
				if (Misc.random(8) != 5){
					dFire = false;
					if (Misc.random(2) == 2){
						maging = false;
						return 91;
						}else{}
						maging = true;
						return 80;
				}else{
					maging = false;
					dFire = true;}
					return 81;
			case 82:
			case 83:
			case 84:
			case 934:
			case 677:
			case 752:
			case 1472:
				return 64;
			case 110:
				return 4658;
			case 914:
				return 197;
			case 913: // battlemages
			case 912:
			case 3829:
				return 811;
			case 6221:
				return 811;
			case 1610:
			case 1611:
				return 9454;
			case 2783:
			return 2731;
			case 1613:
			return 9491;
			case 1615: // Aby Demon
			return 1537;
			case 2035: //spider
			return 143;	
			
			case 2033: //rat
			return 138;	
			
			case 2031: // bloodveld
			return 2070;
			
		
			case 81: // cow
			case 397:
			case 955:
			case 1691:
			case 1766:
			case 1767:
			case 1768:
			case 2310:
			case 3309:
				return 59;
			
			case 21: // hero
			return 451;	
			
			case 41: // chicken
			return 55;	
			
			case 9: // guard
			case 32: // guard
			case 20: // paladin
			case 3816:
			return 451;	
			
			case 1338: // dagannoth
			case 1340:
			case 1342:
			return 1341;
		
			case 19: // white knight
			return 406;
			case 112:
			return 4658;
			case 111:
			return			4672;
			case 117:
			case 116:
			return 4652;
			case 114:
			case 115:
			case 113:
			case 125: // ice warrior
			return 451;
			
			case 2028: // karil
			return 2075;
					
			case 2025: // ahrim
			return 1979;
			
			case 2026: // dharok
			return 2067;
			
			case 2027: // guthan
			return 2080;
			
			case 2029: // torag
			return 0x814;
			case 2881:
				return 2855;
			case 2882:
				maging = true;
				return 2854;
			case 2883:
				return 2851;
			case 2030: // verac
			return 2062;
			case 3340:
				return 3312;
			case 12:
				return 806;
			default:
			//return 0x326;		
			return c.anim;
		}
	}	

	public boolean npcGetsAnnoyed(int Npc) {
		switch(definition.type) {
			case 8528:
			case 119:
			case 183:
			case 185:
			case 83:
			case 84:
			case 2882:
			case 2881:
			case 2883:
			case 58:
			case 3200:
			case 1459:
			case 2627:
			case 2630:
			case 2631:
			case 2738:
			case 2741:
			case 2743:
			case 184:
			case 2745:
			case 2746:
			case 1472:
			case 6247:
			case 3785:
			case 3786:
			case 63:
			case 111:
			case 125:
			case 59:
			case 1153:
			case 127:
			case 6260:
			case 6265:
			case 6263:
			case 770:
			case 771:
			case 6208:
			case 6252:
			case 6223:
			case 6206:
			case 6204:
			case 6225:
			case 6227:
			case 3873:
			case 6203:
			case 6261:
			case 912: //battlemages
			case 913:
			case 914:
			case 195: //castle bandits
			case 2724: //skeletons
			case 172: //dark wizard
			case 13: //wizard
			case 73: //zombies 
			case 117: //hill giants
			case 110: //fire giants
			case 64: //ice spiders
			case 3793:
			case 6222:
			case 78: //bat
				return true;
			
			default:
				return false;
			}
	}
	
	public int getDistanceForNpc(int Npc) {
		switch(definition.type) {
			
			case 181:
			case 6221:
			case 3829:
			case 12:
			case 3822:
			case 3259:			
			case 912: //battlemages
			case 913:
			case 914:
			case 8281:
			case 8282:
			case 8283:
				return 5;
				case 6222:
				case 6223:
				case 6225:
				case 6227:
				return 5;
				
			case 6260:
			case 6247:
			case 6203:
			case 6252:
			case 770:
			case 771:
			case 6204:
			case 6206:
			case 6208:
			case 3847:
			
				return 20;
				case 8596:
				case 8597:
				return 15;
			case 2627:
			case 2630:
			case 2631:
			case 2738:
			case 2743:
			case 2745:
			case 1472:
			case 2883:
			case 2882:
			case 14696:
				return 15;
			case 2881:
				return 30;			
			case 3836:
			case 50:
			case 9737:
				return 10;
			case 3785:
			case 7134:
				return 10;
			case 3786:
				return 10;
			case 2744:
			case 2741:
			case 3793:
			case 8133:
				return 30;				
			case 2746:
				return 10;
			case 3200:
			case 5666:
			case 9780:
			case 8549:
			case 6261:
			case 6263:
			case 6265:
				return 8;
			case 63:
			case 1459:
			case 111:
			case 125:
			case 59:
			case 91:
			case 82:
			case 83:
			case 1153:
			case 1154:
			case 2263:
			case 2264:
			case 2265:
			case 9463:
			case 9465:
			case 9467:
			case 172: //dark wizard
			case 13: //wizard
				return 3;
			case 1:
				return 2;
			case 195: //bandit
			case 117: //hill giant
				return 2;
			case 8528:
				return 4;
			default:
				return 1;
		}
	}

	

	public int getNpcDelay(int i) {
		switch(definition.type) {
			case 1: // man
			return 6;
			case 941:
			case 51:
			case 52:
			case 53:
			case 54:
			case 55:
			case 1589:
			case 1590:
			case 1591:
			case 1592:
			case 50:
			case 5363:
			case 3793:
			case 8133:
				return 8;
			default:
			return 6;
		}
	}
	

	public int getHitDelay(int i) {
		switch(definition.type) {
		case 181:
		case 6221:
		case 3829:
		case 1643:
		case 2025:
			return 4;
		default:
			return 2;
		}
	}
	public void annoyNpcs(int npcId) {
		if (petOwner != 0) { return; }
		for (Player p : Server.getPlayerManager().getPlayers()) {
			if(p != null) {
				Client player = (Client) p;
				if(player.combatLevel > (definition.combat*2) && definition.type != 78 && !player.inWild() ) {//|| !Server.getNpcManager().getNPC(npcId).inWild()){
					return;
				}
				if(definition.type == 2258){ 
					return;
				}
				if(goodDistance(getAbsX(), getAbsY(), player.getAbsX(), player.getAbsY(), getDistanceForNpc(npcId)) && player.heightLevel == heightLevel ) {
					if(npcGetsAnnoyed(npcId) || Server.getNpcManager().getNPC(npcId).inWild()){
						if(Server.getNpcManager().getNPC(npcId) != null && Server.getNpcManager().getNPC(npcId).killerId == 0) {
							if(!player.isFighting && player.inMulti()){
								setAttacker(player);
								attackPlayer(player, npcId);
								player.isFighting = true;
								Server.getNpcManager().getNPC(npcId).killerId = player.playerId;
								Server.getNpcManager().getNPC(npcId).underAttack = true;
								Server.getNpcManager().getNPC(npcId).randomWalk = false;
								return;
							}				
							if(player.isFighting && !player.inMulti()){
								Server.getNpcManager().getNPC(npcId).underAttack = false;
								Server.getNpcManager().getNPC(npcId).randomWalk = true;
								return;
							} else if(!player.isFighting && !player.inMulti()){
								setAttacker(player);
								Server.getNpcManager().getNPC(npcId).killerId = player.playerId;
								Server.getNpcManager().getNPC(npcId).underAttack = true;
								Server.getNpcManager().getNPC(npcId).randomWalk = false;				
								attackPlayer(player, npcId);
								player.isFighting = true;
								return;
							}						
						}
						
					}
				}
			}
			
		}
	}
	public void resetNpcCombat() {
									killerId = 0;
									underAttack = false;
									killedBy = 0;
									facePlayer(0);
	}
	
	public void attackPlayer(Client c, int i) {
		if (petOwner != 0) { return; }
		if(c != null) {
			if (definition.type == 3777 || definition.type == 1532 || definition.type == 3778 || definition.type == 3779 || definition.type == 3780 ||definition.type == 1532){
				return;
			}
			if ((c.inCombat || c.isFighting)  && c.underAttackByNPC != this.npcId && !c.inMulti()) {
				resetNpcCombat();
				return;
			}
				if (!Region.canAttack(c, this)) {
				facePlayer(c.playerId);
				return;
			}
			if(c.goodDistance(getAbsX(), getAbsY(), c.getX(), c.getY(), getDistanceForNpc(i))) {
				if(c.respawnTimer <= 0) {	
				
					facePlayer(c.playerId);
					attackTimer = getNpcDelay(definition.type);
					hitDelayTimer = getHitDelay(definition.type);
					startAnimation(getAttackEmote(definition.type, c), i);
					c.getActionAssistant().removeAllWindows();
					c.singleCombatDelay = System.currentTimeMillis();
					c.singleCombatDelay2 = System.currentTimeMillis();
					c.underAttackByNPC = this.npcId;
					c.isFighting = true;
				}
			} 	
		}
	}
	public void startAnimation(int animId, int i) {
		animNumber = animId;
		animUpdateRequired = true;
		updateRequired = true;
	}

	public void applyDamage(int i) {
		if(this != null) {
			if(PlayerManager.getSingleton().getPlayers()[killerId] == null) {
				return;
			}
			Client c = (Client)	PlayerManager.getSingleton().getPlayers()[killerId];

			//if(c.attackTimer <= 3 || c.attackTimer == 0 && c.npcIndex == 0 && c.oldNpcIndex == 0) {
			if (System.currentTimeMillis() - c.attackTimer > 0 && c.npcIndex == 0 && c.oldNpcIndex == 0){
				c.getActionAssistant().startAnimation(c.getCombatEmotes().getBlockEmote());
			}
			if(c.respawnTimer <= 0) {	
				int damage = Misc.random(maxHit);
				if(definition.type == 14696){
						//c.getActionAssistant().sendMessage("HP- "+hp);
						MultiHit(getAbsX(), getAbsY(), 0, 10,killerId);
						return;
					}
					
				
				if(jadMage){
					if (10 + Misc.random(c.getCombat().calculateMagicDefence()) < 10 + Misc.random(attack)) {
						if(definition.type == 2745 && !(c.prayerActive[16] || c.curseActive[7])) {
								int pX = c.getX();
								int pY = c.getY();// range here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								
								//c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,28, 448, 42, 31, -i - 1, 6);
							c.gfx100(446);
							damage = Misc.random(30);
						}
						if(definition.type == 2745 && (c.prayerActive[16] || c.curseActive[7])) {
								int pX = c.getX();
								int pY = c.getY();// range here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								
								//c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,28, 448, 42, 31, -i - 1, 6);
							c.gfx100(85);
							return;							
						}
					}
					jadMage = false;
					if (c.playerLevel[3] - damage < 0) { 
						damage = c.playerLevel[3];
					}
					c.logoutDelay = System.currentTimeMillis(); // logout delay
					c.hitDiff = damage;
					c.playerLevel[3] -= damage;
					c.getActionAssistant().refreshSkill(3);
					c.updateRequired = true;
					c.hitUpdateRequired = true;						
					return;
				}
				if(jadRange){
					if (10 + Misc.random(c.getCombat().calculateRangeDefence()) < 10 + Misc.random(attack)) {
						if(definition.type == 2745 && !(c.prayerActive[17] || c.curseActive[8])) {
								int pX = c.getX();
								int pY = c.getY();// range here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								
								//c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,28, 451, 42, 31, -i - 1, 6);
							c.gfx100(157);
							damage = Misc.random(30);						
						}
						if(definition.type == 2745 && (c.prayerActive[17] || c.curseActive[8])) {
								int pX = c.getX();
								int pY = c.getY();// range here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								
								//c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,28, 451, 42, 31, -i - 1, 6);
							damage = 0;					
						}						
					}
					jadRange = false;
					if (c.playerLevel[3] - damage < 0) { 
						damage = c.playerLevel[3];
					}
					c.logoutDelay = System.currentTimeMillis(); // logout delay
					c.hitDiff = damage;
					c.playerLevel[3] -= damage;
					c.getActionAssistant().refreshSkill(3);
					c.updateRequired = true;
					c.hitUpdateRequired = true;						
					return;
				}
				if(jadMelee){
					if (10 + Misc.random(c.getCombat().calculateMeleeDefence()) < 10 + Misc.random(attack)) {
						if(definition.type == 2745 && !(c.prayerActive[18] || c.curseActive[9])) {
							damage = Misc.random(30);
						}
						if(definition.type == 2745 && (c.prayerActive[18] || c.curseActive[9])) {
							damage = 0;
						}						
					}
					jadMelee = false;
					if (c.playerLevel[3] - damage < 0) { 
						damage = c.playerLevel[3];
					}
					c.logoutDelay = System.currentTimeMillis(); // logout delay
					c.hitDiff = damage;
					c.playerLevel[3] -= damage;
					c.getActionAssistant().refreshSkill(3);
					c.updateRequired = true;
					c.hitUpdateRequired = true;						
					return;
				}			
				if(range){
					if (10 + Misc.random(c.getCombat().calculateRangeDefence()) > 10 + Misc.random(attack)) {
						damage = 0;
					}	
					if(definition.type == 3259 && !(c.prayerActive[17] || c.curseActive[8])) {
								int pX = c.getX();
								int pY = c.getY();// range here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								
								c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,28, 10, 42, 31, -i - 1, 6);
								//c.gfx100(85);
								damage = Misc.random(6);						
					} else if(definition.type == 3259 && (c.prayerActive[17] || c.curseActive[8])) {
								int pX = c.getX();
								int pY = c.getY();// range here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								
								c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,28, 10, 42, 31, -i - 1, 6);
								//c.gfx100(85);
								damage = 0;					
					}
				}
				if(maging){
					if (10 + Misc.random(c.getCombat().calculateMagicDefence()) > 10 + Misc.random(attack)) {
						damage = 0;
						magicFailed = true;
						if(definition.type == 181 && !(c.prayerActive[16] || c.curseActive[7])) {
							int freezeDelay = 60;//freeze 
							if(freezeDelay > 0 && freezeTimer == 0) {
								freezeTimer = freezeDelay;
							}	
							if(druidMaging){
								int pX = c.getX();
								int pY = c.getY();// mage here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								
								c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,15, 91, 50, 20, -i - 1, 1);
								c.gfx100(85);
								damage = Misc.random(5);
							} else if(!druidMaging) {
								int pX = c.getX();
								int pY = c.getY();// mage here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,50, 109, 50, 20, -i - 1, 10);
								c.gfx100(85);
								damage = 0;	
							}
							
							druidMaging = false;
							maging = false;
							return;
						} else if(definition.type == 2882 && !(c.prayerActive[16] || c.curseActive[7])) {
						int freezeDelay = 60;//freeze 
						if(freezeDelay > 0 && freezeTimer == 0) {
							freezeTimer = freezeDelay;
						}	
							int pX = c.getX();
							int pY = c.getY();// mage here
							int nX = getAbsX();
							int nY = getAbsY();
							int offX = (nY - pY)* -1;
							int offY = (nX - pX)* -1;
							
							c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,15, 162, 50, 20, i - 1, 1);
							c.gfx100(85);
							damage = 0;
							maging = false;
							return;
						}	
					}	
				}
				
				if(definition.type == 50 || definition.type == 53 || definition.type == 54 || definition.type == 55 || 
					definition.type == 5363 || definition.type == 941 || definition.type == 1590 || definition.type == 1591 || 
					definition.type == 1592){
						if(dFire && !maging){
							if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 1540 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 11283 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13655 || (c.prayerActive[16] || c.curseActive[7])) {
								gfx100(0);
								damage = 3 + Misc.random(6);
								c.getActionAssistant().sendMessage("You are protected from the dragon's fire!");
							} else if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] != 1540 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] != 11283&& c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] != 13655) {
								gfx100(0);
								damage = 30 + Misc.random(30);
								c.getActionAssistant().sendMessage("You are burnt by the fire!");
							}
						} else if(maging && !(c.prayerActive[16] || c.curseActive[7])){
							int mageType = Misc.random(3);
							if( mageType == 1){
								int pX = c.getX();
								int pY = c.getY();// mage here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								//c.stillgfx2(96, nY+1, nX+1);
								c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,25, 396, 50, 20, -i - 1, 0);
								//c.gfx100(128);
								damage = Misc.random(30);
							}
							if( mageType == 2){
								int pX = c.getX();
								int pY = c.getY();//white mage here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								//c.stillgfx2(96, nY+1, nX+1);
								c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,25, 395, 50, 20, -i - 1, 0);
								//c.gfx100(128);
								damage = Misc.random(30);
							}	
							if( mageType == 3){
								int pX = c.getX();
								int pY = c.getY();// green mage here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								//c.stillgfx2(96, nY+1, nX+1);
								c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,25, 394, 50, 20, -i - 1, 0);
								if (Misc.random(5) == 1) {
								c.getCombat().givepoison();
								}
								damage = Misc.random(30);
							}							
						} else if(maging && (c.prayerActive[16] || c.curseActive[7])){
							int mageType = Misc.random(3);
							if( mageType == 1){
								int pX = c.getX();
								int pY = c.getY();// blue mage here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								//c.stillgfx2(96, nY+1, nX+1);
								c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,25, 396, 50, 20, -i - 1, 0);
								
								damage = Misc.random(15);
							}
							if( mageType == 2){
								int pX = c.getX();
								int pY = c.getY();// mage here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								//c.stillgfx2(96, nY+1, nX+1);
								c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,25, 395, 50, 20, -i - 1, 0);
								//c.gfx100(128);
								damage = Misc.random(15);
							}	
							if( mageType == 3){
								int pX = c.getX();
								int pY = c.getY();// mage here
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								//c.stillgfx2(96, nY+1, nX+1);
								c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,25, 394, 50, 20, -i - 1, 0);
								//c.gfx100(128);
								damage = Misc.random(15);
							}	
						} else {	
							if((c.prayerActive[18] || c.curseActive[9])){
								damage = Misc.random(7);
							} else {
								damage = Misc.random(10);
							}
						}
					}
					else if (definition.type == 10127 && unholymage) {
					c.gfx100(78);
					if (!(c.prayerActive[16] || c.curseActive[7])) {
					damage = Misc.random(14);
					}
					else {
					damage = Misc.random(4);
					}
					}
					else if (definition.type == 10127 && !unholymage) {
					if (!(c.prayerActive[18] || c.curseActive[9])) {
					damage = Misc.random(14);
					}
					else {
					damage = Misc.random(4);
					}
					}
					else if (definition.type == 3847) {
					
					damage = Misc.random(15);
					MultiHit(getAbsX(), getAbsY(), 15, 20,killerId);
							 
					}
					else if (definition.type == 9780) {
					if (!(c.prayerActive[18] || c.curseActive[9])) {
					damage = Misc.random(18);
					}
					else {
					damage = Misc.random(10);
					}
					}
					else if (definition.type == 8549) {
					if (!(c.prayerActive[18] || c.curseActive[9])) {
					damage = Misc.random(12);
					}
					else {
					damage = Misc.random(6);
					}
					}
					else if (definition.type == 5666) {
					if (!(c.prayerActive[18] || c.curseActive[9])) {
					damage = Misc.random(12);
					}
					else {
					damage = Misc.random(5);
					}
					}
					else if (definition.type == 9463 || definition.type == 9465 || definition.type == 9467) {
					if (!(c.prayerActive[18] || c.curseActive[9])) {
					damage = Misc.random(12);
					}
					else {
					damage = Misc.random(5);
					}
					}
					else if (definition.type == 8281) {
					if (!(c.prayerActive[16] || c.curseActive[7])) {
					damage = Misc.random(20);
					}
					else {
					damage = Misc.random(12);
					}
					}
					else if (definition.type == 8282) {
					if (!(c.prayerActive[18] || c.curseActive[9])) {
					damage = Misc.random(20);
					}
					else {
					damage = Misc.random(12);
					}
					}
					else if (definition.type == 8283) {
					if (!(c.prayerActive[17] || c.curseActive[8])) {
					damage = Misc.random(20);
					}
					else {
					damage = Misc.random(12);
					}
					}
						else if (definition.type == 7134) {
					if (!(c.prayerActive[18] || c.curseActive[9])) {
					damage = Misc.random(15);
					}
					else {
					damage = Misc.random(9);
					}
	
					}
					else if (definition.type == 8596) {
					if (!(c.prayerActive[18] || c.curseActive[9])) {
					damage = Misc.random(45);
					}
					else {
					damage = Misc.random(25);
					}
					MultiHit(getAbsX(), getAbsY(), 25, 8, killerId);
					
					}
					else if (definition.type == 8597) {
					if (!(c.prayerActive[18] || c.curseActive[9])) {
					damage = Misc.random(45);
					}
					else {
					damage = Misc.random(25);
					}
					MultiHit(getAbsX(), getAbsY(), 25, 8, killerId);
					
					}
					else if (definition.type == 9737) {
					if (!(c.prayerActive[18] || c.curseActive[9])) {
					damage = Misc.random(25);
					}
					else {
					damage = Misc.random(15);
					}
					MultiHit(getAbsX(), getAbsY(), 15, 8, killerId);
					
					}else if (definition.type == 9928) {
					if (!(c.prayerActive[18] || c.curseActive[9])) {
					damage = Misc.random(13);
					}
					else {
					damage = Misc.random(7);
					}
					
					}
					else if (definition.type == 9964 || definition.type == 10110) {
					if (!(c.prayerActive[18] || c.curseActive[9])) {
					damage = Misc.random(13);
					}
					else {
					damage = Misc.random(7);
					}
					
					}

					else if(definition.type == 2025 && (c.prayerActive[16] || c.curseActive[7])) {
							c.gfx100(85);
							damage = 0;	
					} else if(definition.type == 181 && !(c.prayerActive[16] || c.curseActive[7])) {
						int freezeDelay = 60;//freeze 
						if(freezeDelay > 0 && freezeTimer == 0) {
							freezeTimer = freezeDelay;
						}	
						if(druidMaging){
							int pX = c.getX();
							int pY = c.getY();// mage here
							int nX = getAbsX();
							int nY = getAbsY();
							int offX = (nY - pY)* -1;
							int offY = (nX - pX)* -1;
							
							c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,15, 91, 50, 20, -i - 1, 1);
							c.gfx100(92);
							damage = Misc.random(5);
						} else if(!druidMaging) {
							int pX = c.getX();
							int pY = c.getY();// mage here
							int nX = getAbsX();
							int nY = getAbsY();
							int offX = (nY - pY)* -1;
							int offY = (nX - pX)* -1;
							c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,50, 109, 50, 20, -i - 1, 10);
							c.gfx100(110);
							damage = 0;	
						}
						druidMaging = false;
						maging = false;
					} else if(definition.type == 181 && (c.prayerActive[16] || c.curseActive[7])) {
						int freezeDelay = 60;//freeze 
						if(freezeDelay > 0 && freezeTimer == 0) {
							freezeTimer = freezeDelay;
						}	
						if(druidMaging){
							int pX = c.getX();
							int pY = c.getY();// mage here
							int nX = getAbsX();
							int nY = getAbsY();
							int offX = (nY - pY)* -1;
							int offY = (nX - pX)* -1;
							
							c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,15, 91, 50, 20, -i - 1, 1);
							c.gfx100(85);
							//damage = Misc.random(5);
						} else if(!druidMaging) {
							int pX = c.getX();
							int pY = c.getY();// mage here
							int nX = getAbsX();
							int nY = getAbsY();
							int offX = (nY - pY)* -1;
							int offY = (nX - pX)* -1;
							c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,15, 109, 50, 20, -i - 1, 1);
							c.gfx100(85);
							//damage = 0;	
						}
						druidMaging = false;
						maging = false;		//marker 3
					} else if((definition.type == 3793 || definition.type == 8133)) {
							damage = Misc.random(12);	
					}
					else if (definition.type == 8281) {
					c.gfx100(86);
					}
					else if (definition.type == 8366) {
					if (TdStage == 0) {
					if (!(c.prayerActive[18] || c.curseActive[9])) {
								damage = Misc.random(28);
								}
								else {
								damage = Misc.random(12);
								}
					}
					if (TdStage == 1) { //RANGE
					
							int pX = c.getX();
								int pY = c.getY();
								int nX = getAbsX();
								int nY = getAbsY();
								int offX = (nY - pY)* -1;
								int offY = (nX - pX)* -1;
								
							//c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,15, 1884, 50, 20, -i - 1, 1);
							//gfx100(1884);
								TdStage = 0;
								if (!(c.prayerActive[17] || c.curseActive[8])) {
								damage = Misc.random(28);
								}
								else {
								damage = Misc.random(12);
								}
								}
					}
					//else if (absX >= 2800 && absX <= 2950 && absY >= 5245 && absY <= 5385) {
					else if (definition.type == 6221 && (c.prayerActive[16] || c.curseActive[7])) {
						c.gfx100(78);
						damage = 0;
					} else if (definition.type == 6221 && !(c.prayerActive[16] || c.curseActive[7])) {
						c.gfx100(78);
						damage = Misc.random(10);
					} else if (definition.type == 3829 && (c.prayerActive[16] || c.curseActive[7])) {
						c.gfx100(76);
						damage = 0;
					}else if (definition.type == 3829 && !(c.prayerActive[16] || c.curseActive[7])) {
						c.gfx100(76);
						damage = Misc.random(10);
					} else if ((definition.type == 3816 || definition.type == 3822) && (c.prayerActive[18] || c.curseActive[9])) {
						damage = 0;
					}else if ((definition.type == 3816 || definition.type == 3822) && !(c.prayerActive[18] || c.curseActive[9])) {
						damage = Misc.random(10);
					} else if ((definition.type == 116 || definition.type == 113 || definition.type == 71 ||
							definition.type == 123 || definition.type == 101 || definition.type == 122 || 
							definition.type == 102 || definition.type == 100) && !(c.prayerActive[18] || c.curseActive[9])) { // NO PRAYER CONDITION bandos minor
										damage = Misc.random(10);
					} else if ((definition.type == 116 || definition.type == 113 || definition.type == 71 ||
							definition.type == 123 || definition.type == 101 || definition.type == 122 || 
							definition.type == 102 || definition.type == 100) && (c.prayerActive[18] || c.curseActive[9])) { // NO PRAYER CONDITION bandos minor
										damage = 0;
					}
					else if ((definition.type == 6260 || definition.type == 6247 || definition.type == 6203) && !(c.prayerActive[18] || c.curseActive[9])) { // NO PRAYER CONDITION bosses
										damage = Misc.random(45);
										MultiHit(getAbsX(), getAbsY(), 15, 15, killerId);
							}
							else if ((definition.type == 6265 || definition.type == 6263 || definition.type == 6261 ||
								  definition.type == 6248 || definition.type == 6250 || definition.type == 6252 ||
								  definition.type == 6208 || definition.type == 6206 || definition.type == 6204 ||
								  definition.type == 6223) && !(c.prayerActive[18] || c.curseActive[9])) { //Minions no prayer
								  damage = Misc.random(25);
							}
						else if ((definition.type == 6227 || definition.type == 6225) && !(c.prayerActive[17] || c.curseActive[8])) { // NO PRAYER CONDITION ARM MINIONS
											damage = Misc.random(20);
						}
						else if (definition.type == 6222 && !(c.prayerActive[17] || c.curseActive[8])) { // NO PRAYER CONDITION arma
											damage = Misc.random(60);
											MultiHit(getAbsX(), getAbsY(), 15, 15, killerId);
						}
						 else if ((definition.type == 6260 || definition.type == 6247 || definition.type == 6203)  && (c.prayerActive[18] || c.curseActive[9])) { // Hit through melee pray bosses
								if (Misc.random(10) == 1) {
									damage = Misc.random(25);
									MultiHit(getAbsX(), getAbsY(), 15, 15, killerId);
								} else {
									damage = Misc.random(20);
									MultiHit(getAbsX(), getAbsY(), 15, 15, killerId);
								}
						}
						 else if (definition.type == 6222  && (c.prayerActive[17] || c.curseActive[8])) { // Hit through range pray arma
								if (Misc.random(10) == 1) {
									damage = Misc.random(25);
									MultiHit(getAbsX(), getAbsY(), 15, 15, killerId);
								} else {
									damage = Misc.random(20);
									MultiHit(getAbsX(), getAbsY(), 15, 15, killerId);
								}
						}
						else if ((definition.type == 6265 || definition.type == 6263 || definition.type == 6261 ||
								  definition.type == 6248 || definition.type == 6250 || definition.type == 6252 ||
								  definition.type == 6208 || definition.type == 6206 || definition.type == 6204 ||
								  definition.type == 6223
						) && (c.prayerActive[18] || c.curseActive[9])) { //minions hit through melee
								if (Misc.random(10) == 1) {
									damage = Misc.random(13);
								} else {
									damage = Misc.random(10);
								}
						} 
						else if ((definition.type == 6225 || definition.type == 6227) && (c.prayerActive[17] || c.curseActive[8])) { //ARMADYL minions range
								if (Misc.random(10) == 1) {
									damage = Misc.random(13);
								} else {
									damage = Misc.random(10);
								}
						} 
					//}
					
					else if((definition.type == 3793 || definition.type == 8133) && !(c.prayerActive[18] || c.curseActive[9])) {
							damage = Misc.random(15);
					} else if((definition.type == 3793 || definition.type == 8133) && !(c.prayerActive[17] || c.curseActive[8])) {
							damage = Misc.random(15);
					} else if((definition.type == 3793 || definition.type == 8133) && !(c.prayerActive[16] || c.curseActive[7])) {
							damage = Misc.random(15);
					} else if((definition.type == 3793 || definition.type == 8133) && (c.prayerActive[18] || c.curseActive[9])) {
							damage = Misc.random(5);
					} else if((definition.type == 3793 || definition.type == 8133) && (c.prayerActive[17] || c.curseActive[8])) {
							damage = Misc.random(5);
					} else if((definition.type == 3793 || definition.type == 8133) && (c.prayerActive[16] || c.curseActive[7])) {
							damage = Misc.random(5);						
					} else if(definition.type == 2028 && !(c.prayerActive[17] || c.curseActive[8])) {
							damage = Misc.random(25);							
					} else if(definition.type == 2028 && (c.prayerActive[17] || c.curseActive[8])) {
							damage = 0;								
					} else if(definition.type == 3795 && (c.prayerActive[18] || c.curseActive[9])) {
							damage = Misc.random(15);
							c.playerLevel[5] = c.playerLevel[5] - damage/3;	
							c.getActionAssistant().refreshSkill(5);
					} else if(definition.type == 3795 && !(c.prayerActive[18] || c.curseActive[9])) {
							damage = Misc.random(70);
							c.playerLevel[5] = c.playerLevel[5] - damage/3;	
							c.getActionAssistant().refreshSkill(5);
					} else if(definition.type == 1643 && (c.prayerActive[16] || c.curseActive[7])) {
							int pX = c.getX();
							int pY = c.getY();// mage here
							int nX = getAbsX();
							int nY = getAbsY();
							int offX = (nY - pY)* -1;
							int offY = (nX - pX)* -1;
							//c.stillgfx2(96, nY+1, nX+1);
							c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,25, 130, 50, 20, -i - 1, 0);
							c.gfx100(85);
							damage = 0;	
					} else if(definition.type == 12 && (c.prayerActive[16] || c.curseActive[7])) {
							gfx0(35);
							int pX = c.getX();
							int pY = c.getY();// mage here
							int nX = getAbsX();
							int nY = getAbsY();
							int offX = (nY - pY)* -1;
							int offY = (nX - pX)* -1;
							//c.stillgfx2(96, nY+1, nX+1);
							c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,25, 35, 50, 20, -i - 1, 0);
							//c.gfx100(85);
							damage = 0;		
					} else if(definition.type == 12 && !(c.prayerActive[16] || c.curseActive[7])) {
							int pX = c.getX();
							int pY = c.getY();// mage here
							int nX = getAbsX();
							int nY = getAbsY();
							int offX = (nY - pY)* -1;
							int offY = (nX - pX)* -1;
							//c.stillgfx2(96, nY+1, nX+1);
							c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,25, 35, 50, 20, -i - 1, 0);
							//c.gfx100(85);
							damage = Misc.random(5);								
					} else if(definition.type == 1643 && !(c.prayerActive[16] || c.curseActive[7])) {
							int pX = c.getX();
							int pY = c.getY();// mage here
							int nX = getAbsX();
							int nY = getAbsY();
							int offX = (nY - pY)* -1;
							int offY = (nX - pX)* -1;
							//c.stillgfx2(96, nY+1, nX+1);
							c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,25, 130, 50, 20, -i - 1, 0);
							c.gfx100(128);
							damage = Misc.random(12);	
					} else if(definition.type == 1604 && (c.prayerActive[16] || c.curseActive[7])) {
							int pX = c.getX();
							int pY = c.getY();// mage here
							int nX = getAbsX();
							int nY = getAbsY();
							int offX = (nY - pY)* -1;
							int offY = (nX - pX)* -1;
							//c.stillgfx2(96, nY+1, nX+1);
							//c.getActionAssistant().createPlayersProjectile(nY, nX, offY, offX, 50,25, 130, 50, 20, -i - 1, 0);
							c.gfx100(255);
							damage = 0;	
					} else if(definition.type == 1604 && !(c.prayerActive[16] || c.curseActive[7])) {
							int pX = c.getX();
							int pY = c.getY();// mage here
							int nX = getAbsX();
							int nY = getAbsY();
							int offX = (nY - pY)* -1;
							int offY = (nX - pX)* -1;
							//c.stillgfx2(96, nY+1, nX+1);
							//c.getActionAssistant().createPlayersProjectile(nY, nX, offY, offX, 50,25, 130, 50, 20, -i - 1, 0);
							c.gfx100(255);
							damage = Misc.random(20);							
					} else if(definition.type == 2025 && !(c.prayerActive[16] || c.curseActive[7])) {
							//c.gfx100(369);
							//Server.getStillGraphicsManager().stillGraphics(c,c.getAbsX(), c.getAbsY(), 0, 369, 0);
							c.gfx0(369);
							int freezeDelay = 60;//freeze 
							if(freezeDelay > 0 && c.freezeTimer == 0) {
								c.freezeTimer = freezeDelay;
							}
							damage = Misc.random(30);	
					} else if(definition.type == 2882 && (c.prayerActive[16] || c.curseActive[7])) {
						int freezeDelay = 60;//freeze 
						if(freezeDelay > 0 && freezeTimer == 0) {
							freezeTimer = freezeDelay;
						}	
							int pX = c.getX();
							int pY = c.getY();// mage here
							int nX = getAbsX();
							int nY = getAbsY();
							int offX = (nY - pY)* -1;
							int offY = (nX - pX)* -1;
							
							c.getCombat().createPlayersProjectile(nY, nX+1, offY, offX, 50,15, 162, 50, 20, i - 1, 1);
							c.gfx100(85);
							damage = 0;
							maging = false;
					} else if(definition.type == 2882 && !(c.prayerActive[16] || c.curseActive[7])) {
						int freezeDelay = 60;//freeze 
						if(freezeDelay > 0 && freezeTimer == 0) {
							freezeTimer = freezeDelay;
						}	
							int pX = c.getX();
							int pY = c.getY();// mage here
							int nX = getAbsX();
							int nY = getAbsY();
							int offX = (nY - pY)* -1;
							int offY = (nX - pX)* -1;
							
							c.getCombat().createPlayersProjectile(nY, nX, offY, offX, 50,15, 162, 50, 20, i - 1, 1);
							c.gfx100(163);
							damage = Misc.random(50);
							maging = false;
					} else if(definition.type == 912 && (c.prayerActive[16] || c.curseActive[7])) {
							c.gfx100(85);
							damage = 0;	
					} else if(definition.type == 914 && (c.prayerActive[16] || c.curseActive[7])) {
							c.gfx100(85);
							damage = 0;	
					} else if(definition.type == 913 && (c.prayerActive[16] || c.curseActive[7])) {
							c.gfx100(85);
							damage = 0;							
					} else if(definition.type == 912 && !(c.prayerActive[16] || c.curseActive[7])) {
							c.gfx100(78);
							damage = Misc.random(30);
					} else if(definition.type == 914 && !(c.prayerActive[16] || c.curseActive[7])) {
							c.gfx100(77);
							damage = Misc.random(30);
					} else if(definition.type == 913 && !(c.prayerActive[16] || c.curseActive[7])) {
							c.gfx100(76);
							damage = Misc.random(30);							
					} else if(definition.type == 2881 && (c.prayerActive[17] || c.curseActive[8])) {
							c.gfx100(28);
							damage = Misc.random(5);	
					} else if(definition.type == 2881 && !(c.prayerActive[17] || c.curseActive[8])) {
							c.gfx100(28);
							damage = Misc.random(40);							
					} else if(definition.type == 3836 && (c.prayerActive[16] || c.curseActive[7])) {
							damage = Misc.random(25);	
					} else if(definition.type == 3836 && !(c.prayerActive[16] || c.curseActive[7])) {
							damage = Misc.random(50);						
					} else if(definition.type == 3200 || definition.type == 3340 || definition.type == 1914 && (c.prayerActive[16] || c.curseActive[7])) {
							damage = Misc.random(10);	
					} else if(definition.type == 3200 || definition.type == 3340 || definition.type == 1914 && !(c.prayerActive[16] || c.curseActive[7])) {
							damage = Misc.random(15);						
					} else if(definition.type == 3200 || definition.type == 3340 || definition.type == 1914 && (c.prayerActive[17] || c.curseActive[8])) {
							damage = Misc.random(10);	
					} else if(definition.type == 3200 || definition.type == 3340 || definition.type == 1914 && !(c.prayerActive[17] || c.curseActive[8])) {
							damage = Misc.random(15);
					} else if(definition.type == 3200 || definition.type == 3340 || definition.type == 1914 && (c.prayerActive[18] || c.curseActive[9])) {
							damage = Misc.random(10);	
					} else if(definition.type == 3200 || definition.type == 3340 || definition.type == 1914 && !(c.prayerActive[18] || c.curseActive[9])) {
							damage = Misc.random(15);				
					} else if(definition.type == 3200 && Misc.random(3)==1){
						//animNumber = 3146;
						damage = 0;  
					
						//c.getItems().removeItem(c.playerEquipment[3], 3);
						//c.getActionAssistant().sendMessage("The chaos elemental removes your weapon!");
						
											
					} else if (definition.type == 86 || definition.type == 41 || definition.type == 3) {
					damage = 0;
					}
					else if ((c.prayerActive[18] || c.curseActive[9]) && (definition.type ==934 ||definition.type ==237 ||definition.type ==277)) {
				damage = Misc.random(10);
				}
					else if((c.prayerActive[18] || c.curseActive[9])) { // protect from melee
						damage = 0;
				}
				
				if(definition.type == 6246 && (c.prayerActive[17] || c.curseActive[8])) {
							damage = 0;	
					

} else if(definition.type == 1618 && (c.prayerActive[18] || c.curseActive[9])) {
							damage = 0;	}
				if (definition.type == 8528) {
								damage = Misc.random(25);
								if (Maging && c.prayerActive[16]) { damage = (int)(damage*0.5); }
								if (Melee && c.prayerActive[18]) { damage = (int)(damage*0.5); }
								MultiHit(getAbsX(), getAbsY(), 20, 9, killerId);
								Maging = false;
								Melee = false;
				}
							
							if (c.canVengeance && damage > 0) {
					//c.hitDiff = damage;
					c.canVengeance = false;
					c.forcedChat("Taste vengeance!");
					c.hitUpdateRequired = true;
					c.updateRequired = true;
					c.appearanceUpdateRequired = true;
					hitDiff -= damage/2;
					hp -= damage;				
					setAnimUpdateRequired(true);
					setUpdateRequired(true);
					hitUpdateRequired = true;
				}
				
				if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13740) {
			
					double div = damage *0.7;
					int test = (int)div;
					test = test - 1; //correct for rounding

					if (c.playerRights < 3) {
						c.playerLevel[5] -= 0.15 * damage;
					}
					if(c.playerLevel[5] <= 0) {
						c.playerLevel[5] = 0;
					}
					c.getActionAssistant().refreshSkill(5);
					damage = (int)div;
					if (c.playerLevel[3] - damage < 0) { 
						damage = c.playerLevel[3];
					}
				}
			else if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] == 13742) {
						int elysain = damage / 4;
			damage = damage - elysain;
			}
		/*	if (c.playerEquipment[PlayerConstants.PLAYER_HAT] == 13898) {
				c.stathelm -= damage;
				if (c.stathelm <= 0) {
					c.stathelm = 0;
				}
	
			}
			else if (c.playerEquipment[PlayerConstants.PLAYER_HAT] == 13866) {
				c.zurhelm -= damage;
				if (c.zurhelm <= 0) {
					c.zurhelm = 0;
				}
			}
			else if (c.playerEquipment[PlayerConstants.PLAYER_HAT] == 13878) {
				c.morrhelm -= damage;
				if (c.morrhelm <= 0) {
					c.morrhelm = 0;
				}
			}
			if (c.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13889) {
				c.vplate -= damage;
				if (c.vplate <= 0) {
					c.vplate = 0;
				}
			}
			else if (c.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13860) {
				c.zplate -= damage;
				if (c.zplate <= 0) {
					c.zplate = 0;
				}
			}
			else if (c.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13872) {
				c.mplate -= damage;
				if (c.mplate <= 0) {
					c.mplate = 0;
				}
			}
			else if (c.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13886) {
				c.splate -= damage;
				if (c.splate <= 0) {
					c.splate = 0;
				}
			}
			if (c.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13895) {
				c.vleg -= damage;
				if (c.vleg <= 0) {
					c.vleg = 0;
				}
			}
			else if (c.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13875) {
				c.mleg -= damage;
				if (c.mleg <= 0) {
					c.mleg = 0;
				}
			}
			else if (c.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13863) {
				c.zleg -= damage;
				if (c.zleg <= 0) {
					c.zleg = 0;
				}
			}
			else if (c.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13892) {
				c.sleg -= damage;
				if (c.sleg <= 0) {
					c.sleg = 0;
				}
			}*/
				if (c.playerLevel[3] - damage < 0) { 
					damage = c.playerLevel[3];
				}
				c.logoutDelay = System.currentTimeMillis(); // logout delay
				c.hitDiff = damage;
				c.playerLevel[3] -= damage;
				c.getActionAssistant().refreshSkill(3);
				c.updateRequired = true;
				c.hitUpdateRequired = true;
				
			}
		}
	}
	/**
	* Npc Follow Player
	**/
	
	public int GetMove(int Place1,int Place2) { 
		if ((Place1 - Place2) == 0) {
            return 0;
		} else if ((Place1 - Place2) < 0) {
			return 1;
		} else if ((Place1 - Place2) > 0) {
			return -1;
		}
        	return 0;
   	 }
	 
	  /* public void killMyNPCs(){
        for(int i = 0; i < Server.getNpcManager().getNPC(i); i++)
            if(Server.getNpcManager().getNPC(i) != null) {
				if(npc.definition.type == 2627 || npc.definition.type == 2630 || 
					npc.definition.type == 2631 || npc.definition.type == 2741 || 
					npc.definition.type == 2743 || npc.definition.type == 2745 || 
					npc.definition.type == 2746 || npc.definition.type == 2738) {
					if(npc.heightLevel == client.heightLevel){
						npc.absX = 0;
						npc.absY = 0;
						npc.followPlayer = null;
					}
				}
			}
	}*/
	
	public void getNextNPCMovement(int i) {
		if(definition.type == 1532){
			return;
		}
		direction = -1;
		if(freezeTimer <= 0) {
			direction = getNextWalkingDirection();
		}
	}
/*	public int getNextWalkingDirection() {
		int dir;
		dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
		if(dir == -1) return -1;
		dir >>= 1;
		absX += moveX;
		absY += moveY;
		dirUpdateRequired = true;
		return dir;
	}*/
	
	public int getNextWalkingDirection() {
			int dir;
			dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
			if (dir == -1)
				return -1;
			dir >>= 1;
			absX += moveX;
			absY += moveY;
			return dir;
	}
	
	public void setWalk(int tgtX, int tgtY, boolean restrict) {
		if (isDead) {
			return;
		}
		direction = Misc.direction(getAbsX(), getAbsY(), tgtX, tgtY);
		if (restrict) {
			if (tgtX > x1 || tgtX < x2 || tgtY > y1 || tgtY < y2) {
				direction = -1;
				return;
			}
		}
		//Tile[] oldLocation = TileManager.getTiles(this);
			
		//for (Tile oldTiles : oldLocation) 
		//	FlagMap.set(TileManager.currentLocation(oldTiles), false);
		
		if (direction != -1) {
			direction >>= 1;
			setAbsX(tgtX);
			setAbsY(tgtY);
		}
		//Tile[] newLocation = TileManager.getTiles(this);
		
			
		//for (Tile newTiles : newLocation) 
		//	FlagMap.set(TileManager.currentLocation(newTiles), true);
			
		updateRequired = true;
	}	
	
	public boolean npcInMulti() {
		if((absX >= 3029 && absX <= 3374 && absY >= 3759 && absY <= 3903) || 
		   (absX >= 2250 && absX <= 2280 && absY >= 4670 && absY <= 4720) ||
		   (absX >= 3198 && absX <= 3380 && absY >= 3904 && absY <= 3970) ||
		   (absX >= 3191 && absX <= 3326 && absY >= 3510 && absY <= 3759) ||
		   (absX >= 2987 && absX <= 3006 && absY >= 3912 && absY <= 3937) ||
		   (absX >= 2245 && absX <= 2295 && absY >= 4675 && absY <= 4720) ||
		   (absX >= 2450 && absX <= 3520 && absY >= 9450 && absY <= 9550) ||
		   (absX >= 3006 && absX <= 3071 && absY >= 3602 && absY <= 3710) ||
		   (absX >= 3134 && absX <= 3192 && absY >= 3519 && absY <= 3646)
		   ){
			return true;
		}
		return false;
	}
	/*public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
		  for (int j = 0; j <= distance; j++) {
			if ((objectX + i) == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
				return true;
			} else if ((objectX - i) == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
				return true;
			} else if (objectX == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
				return true;
			}
		  }
		}
		return false;
	}*/
	 public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
  
		if (((int) Math.sqrt(Math.pow(objectX - playerX, 2) + Math.pow(objectY - playerY, 2))) <= distance) {
		return true;
		}
		return false;
    }	

}