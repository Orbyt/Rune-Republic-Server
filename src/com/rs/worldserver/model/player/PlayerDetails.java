package com.rs.worldserver.model.player;

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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
/**
 * Details of the player that are saved.
 * 
 * @author Graham
 * 
 */
public class PlayerDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	public int loginTimes = 0;
	public int rewardPoints;
	public int totalFightPitWins;
	public int fightPitStreak;
	public int cwPoints;
	public int dPoints;
	public int vPoints;
	public long specRecharge;
	public String ranks = "";
	// public ArrayList<AchievementList> combatAchieve;
	// public ArrayList<AchievementList> skillingAchieve;
	// public ArrayList<AchievementList> monsterAchieve;
	// public ArrayList<AchievementList> miscAchieve;
	// public ArrayList<AchievementList> pkingAchieve;
	public int firstBlood = 0, warrior = 0, bersk = 0, nomercy = 0, hitman = 0, bountyhunter = 0; // pking
	public int kbd = 0, td = 0, sara = 0, zam = 0, cali = 0, barrow = 0; // monsters
	public int digs = 0,firstTime2 = 0,saraDrink = 0, brews = 0, caskets = 0, stake = 0, fires = 0, lamps = 0;
	public String playerPass = "";
	public int[] degradeItems;
	public String playerName = "";
	public String connectedFrom = "";
	public boolean playerMember;
	public int newLog;
	public String bankpin = "";
	public int bankPinSet = 0;
	public int bankPinActived = 0;
	public int bandosPoints = 0;
	public String salt = "a";
	public int loggedIn = 0;
	public boolean modDay = false;
	public boolean modDays = false;
	public long doublePkP = 0;
	public int prayer = 0;
	public int monkey = 0;
	//public long HSDelay = 0;
	public boolean tempusingBow = false, tempusingArrows = false, tempusingOtherRangeWeapons = false, Disable = false;
	public int stuckX = 1;
	public int stuckY = 1;
	public int stuckHeight = 0, killStreak = 0;
	public long lastLogged = 0;
	public int mutetimer = 0;
	public int EP = 0, epDamageCounter = 0;
	public int pkpreset = 0;
	public int armaPoints = 0;
	public int saraPoints = 0;
	public int zamPoints = 0;
	public int vlsdmg = 0;
	public int statdmg = 0;
	public int vsdmg = 0;
	public int splate = 0;
	public int vplate = 60;
	public int mplate = 0;
	public int zplate = 0;
	public int sleg = 0;
	public int mleg = 0;
	public int zleg = 0;
	public int vleg = 0;
	public int morrhelm = 0;
	public int zurhelm = 0;
	public int stathelm = 0;
	public int lastEPgained = 0, EPtimer = 0;
	public int firstBankPin;
	public int secondBankPin;
	public int thirdBankPin;
	public int fourthBankPin;
	public boolean hasBankPin;
	public int attemptsRemaining;
	public int playerPosX;
	public int playerPosY;
	public int playerHeight;
	public int playerRights;
	public int playerStatus;
	public int playerHeadIcon;
	public int playerMac;
	public int world;
	public int starter;
	public int faction;
	public boolean hasSetFaction;
	public int mage;
	public int pkpoints;
	public String lastPkp = "";
	public int pkp;
	public int pkd;
	public int veng;
	public int blackMarks;
	public int specialAmount;
	public int sounds;
	public int music;
	public int pmstatus;
	public int fightType;
	public int barrowsKc;
	public int zamKc;
	public int banKc;
	public int saraKc;
	public int armKc;
	public int ahrim;
	public int karil;
	public int torag;
	public int guthan;
	public int dharok;
	public int cluelevel = 0;
	public int Muted;
	public String clueString = "none";
	public String jailedBy = "";
	public int jailTimer;
	public int skullTimer;
	public int itemOnNpc;
	public int pcPoints;
	public int slayerTask;
	public int slayerAmount;
	public int recoil;
	public int newFag;
	public int newFag2;
	public int slayerPoints;
	public long gemTimer;
	public int[] playerItem;
	public int[][] achievements;
	public int reset;
	public int[] playerItemN;
	public int[] playerEquipment;
	public int[] playerEquipmentN;
	public int[] achievementProgress; // 0 = skilling 8, 1 = skilling 12, 2 = skilling 13, 3 = skilling 17, 4 = misc 18, 5 = misc 19, 6 = monster 6, 7= monster 7
	public int[] bankItems;
	public int[] bankItemsN;
	public int playerBankSize;
	public int[] playerLevel;
	public int[] playerXP;
	public int[] playerQuest;
	public int[] playerAppearance;
	public boolean playerAppearanceSet;
	public boolean expLock;
	public int apset;
	public int apset2;
	public int apset3;
	public int pHead;
	public int pTorso;
	public int pArms;
	public int pHands;
	public int pLegs;
	public int pFeet;
	public int pGender;
	public int pBeard;
	public int pHairC;
	public int pTorsoC;
	public int pLegsC;
	public int pFeetC;
	public int pSkinC;
	public long[] friends;
	public int friendsSize;
	public long[] ignores;
	public int ignoresSize;
	public Map<String, Object> extraData;
	public int newClue = 0;

	public PlayerDetails(Player plr) {
		extraData = new HashMap<String, Object>();	
		loginTimes = plr.loginTimes;
		bankPinSet = plr.bankPinSet;
		bankpin = plr.bankpin;
		ranks = plr.ranks;
		playerPass = plr.getPlayerPass();
		playerName = plr.getPlayerName();
		playerPosX = plr.getAbsX();
		playerPosY = plr.getAbsY();
		bandosPoints = plr.bandosPoints;
		salt = plr.salt;
		lastEPgained = plr.lastEPgained;
		EPtimer = plr.EPtimer;
		EP = plr.EP;
		epDamageCounter = plr.epDamageCounter;
		loggedIn = plr.loggedIn;
		killStreak = plr.killStreak;
		lastLogged = plr.lastLogged;
		modDay = plr.modDay;
		modDays = plr.modDays;
		doublePkP = plr.doublePkP;
		prayer = plr.prayer;
		Disable = plr.Disable;
		monkey = plr.monkey;
		newClue = plr.newClue;
		//HSDelay = plr.HSDelay;
		tempusingBow = plr.tempusingBow;
		tempusingArrows = plr.tempusingArrows;
		tempusingOtherRangeWeapons = plr.tempusingOtherRangeWeapons;
		stuckX = plr.stuckX;
    	stuckY = plr.stuckY;
	    stuckHeight = plr.stuckHeight;
		mutetimer = plr.mutetimer;
		pkpreset = plr.pkpreset;
		armaPoints = plr.armaPoints;
		saraPoints = plr.saraPoints;
		zamPoints = plr.zamPoints;
		vlsdmg = plr.vlsdmg;
		totalFightPitWins = plr.totalFightPitWins;
		fightPitStreak = plr.fightPitStreak;
		rewardPoints = plr.rewardPoints;
		cwPoints = plr.cwPoints;
		dPoints = plr.dPoints;
		vPoints = plr.vPoints;
		specRecharge=plr.specRecharge;
		firstBlood = plr.firstBlood;
		warrior = plr.warrior;
		bersk = plr.bersk;
		nomercy = plr.nomercy;
		hitman = plr.hitman;
		bountyhunter = plr.bountyhunter;
	    kbd = plr.kbd;
		lamps = plr.lamps;
		fires = plr.fires;
		digs = plr.digs;
		saraDrink = plr.saraDrink;
		firstTime2 = plr.firstTime2;
		brews = plr.brews;
		caskets = plr.caskets;
		stake = plr.stake;
		td = plr.td;
		sara = plr.sara;
		zam = plr.zam;
		cali = plr.cali;
		barrow = plr.barrow; // monsters
		// combatAchieve = plr.combatAchieve;
		// skillingAchieve = plr.skillingAchieve;
		// monsterAchieve = plr.monsterAchieve;
		// miscAchieve  = plr.miscAchieve;
		// pkingAchieve = plr.pkingAchieve;
		statdmg = plr.statdmg;
		vsdmg = plr.vsdmg;
		splate = plr.splate;
		vplate = plr.vplate;
		mplate = plr.mplate;
		zplate = plr.zplate;
		sleg = plr.sleg;
		mleg = plr.mleg;
		zleg = plr.zleg;
		vleg = plr.vleg;
		morrhelm = plr.morrhelm;
		zurhelm = plr.zurhelm;
		stathelm = plr.stathelm;
		playerMember = plr.isPlayerMember();
		playerHeight = plr.getHeightLevel();
		playerRights = plr.playerRights;
		playerMac = plr.playerMac;
		newLog = plr.newLog;
		firstBankPin = plr.firstPin;
		secondBankPin = plr.secondPin;
		thirdBankPin = plr.thirdPin;
		fourthBankPin = plr.fourthPin;
		hasBankPin = plr.hasBankPin;
		attemptsRemaining = plr.attemptsRemaining;
		world = plr.world;
		starter = plr.starter;
		faction = plr.faction;
		hasSetFaction = plr.hasSetFaction;
		mage = plr.mage;
		pkpoints = plr.pkpoints;
		lastPkp = plr.lastPkp;
		pkp = plr.pkp;
		pkd = plr.pkd;
		veng = plr.veng;
		blackMarks = plr.blackMarks;
		specialAmount = plr.specialAmount;
		sounds = plr.sounds;
		music = plr.music;
		pmstatus = plr.pmstatus;
		fightType = plr.fightType;
		barrowsKc = plr.barrowsKc;
		zamKc = plr.zamKc;
		banKc = plr.banKc;
		saraKc = plr.saraKc;
		armKc = plr.armKc;
		ahrim = plr.ahrim;
		karil = plr.karil;
		torag = plr.torag;
		guthan = plr.guthan;
		dharok = plr.dharok;
		cluelevel = plr.cluelevel;
		clueString = plr.clueString;
		jailedBy = plr.jailedBy;
		jailTimer = plr.jailTimer;
		skullTimer = plr.skullTimer;
		itemOnNpc = plr.itemOnNpc;
		pcPoints = plr.pcPoints;
		slayerTask = plr.slayerTask;
		slayerAmount = plr.slayerAmount;
		recoil = plr.recoil;
		newFag = plr.newFag;
		newFag2 = plr.newFag2;
		expLock = plr.expLock;
		slayerPoints = plr.slayerPoints;
		gemTimer = plr.gemTimer;
		Muted = plr.Muted;
		achievements = plr.achievements;
		reset = plr.reset;
		playerItem = plr.playerItems;
		playerItemN = plr.playerItemsN;
		bankItems = plr.bankItems;
		bankItemsN = plr.bankItemsN;
		degradeItems = plr.degradeItems;
		playerEquipment = plr.playerEquipment;
		playerEquipmentN = plr.playerEquipmentN;
		achievementProgress = plr.achievementProgress;
		playerLevel = plr.playerLevel;
		playerXP = plr.playerXP;
		connectedFrom = plr.connectedFrom;
		apset = plr.apset;
		apset2 = plr.apset2;
		apset3 = plr.apset3;
		playerAppearanceSet = plr.playerAppearanceSet;
		if (plr.getLook() != null) {
			playerAppearance = plr.getLook();
		} else {
			playerAppearance = new int[13];
		}
		if (plr.getFriends() != null) {
			friends = plr.getFriends();
		} else {
			friends = new long[200];
		}
		friendsSize = plr.getFriendsSize();
		if (plr.getIgnores() != null) {
			ignores = plr.getIgnores();
		} else {
			ignores = new long[100];
		}
		ignoresSize = plr.getIgnoresSize();
		playerBankSize = plr.getPlayerBankSize();
	}

	public static void copyDetails(Client c, PlayerDetails loadgame) {
		c.teleportToZ = loadgame.playerHeight;
		if (loadgame.playerPosX > 0 && loadgame.playerPosY > 0) {
			c.teleportToX = loadgame.playerPosX;
			c.teleportToY = loadgame.playerPosY;
			c.teleportToZ = loadgame.playerHeight;
		}
		c.bankPinSet = loadgame.bankPinSet;
		c.bankpin = loadgame.bankpin;
		c.loginTimes = loadgame.loginTimes;
		c.lastConnectionFrom = loadgame.connectedFrom;
		c.playerRights = loadgame.playerRights;
		c.playerMac = loadgame.playerMac;
		c.newLog = loadgame.newLog;
		c.firstPin = loadgame.firstBankPin;
		c.secondPin = loadgame.secondBankPin;
		c.thirdPin = loadgame.thirdBankPin;
		c.fourthPin = loadgame.fourthBankPin;
		c.hasBankPin = loadgame.hasBankPin;
		// c.combatAchieve = loadgame.combatAchieve;
		// c.skillingAchieve = loadgame.skillingAchieve;
		// c.monsterAchieve = loadgame.monsterAchieve;
		// c.miscAchieve  = loadgame.miscAchieve;
		// c.pkingAchieve = loadgame.pkingAchieve;
		c.attemptsRemaining = loadgame.attemptsRemaining;
		c.world = loadgame.world;
		c.starter = loadgame.starter;
		c.faction = loadgame.faction;
		c.hasSetFaction = loadgame.hasSetFaction;
		c.mage = loadgame.mage;
		c.pkpoints = loadgame.pkpoints;
		c.lastPkp = loadgame.lastPkp;
		c.pkp = loadgame.pkp;
		c.pkd = loadgame.pkd;
		c.veng = loadgame.veng;
		c.blackMarks = loadgame.blackMarks;
		c.specialAmount = loadgame.specialAmount;
		c.sounds = loadgame.sounds;
		c.music = loadgame.music;
		c.pmstatus = loadgame.pmstatus;
		c.fightType = loadgame.fightType;
		c.barrowsKc = loadgame.barrowsKc;
		c.zamKc = loadgame.zamKc;
		c.banKc = loadgame.banKc;
		c.saraKc = loadgame.saraKc;
		c.armKc = loadgame.armKc;
		c.ahrim = loadgame.ahrim;
		c.ranks = loadgame.ranks;
		c.karil = loadgame.karil;
		c.torag = loadgame.torag;
		c.bandosPoints = loadgame.bandosPoints;
		c.salt = loadgame.salt;
		c.lastEPgained = loadgame.lastEPgained;
		c.EPtimer = loadgame.EPtimer;
		c.EP =loadgame.EP;
		c.epDamageCounter = loadgame.bandosPoints;
		c.loggedIn = loadgame.loggedIn;
		c.killStreak = loadgame.killStreak;
		c.lastLogged = loadgame.lastLogged;
		c.modDay = loadgame.modDay;
		c.modDays = loadgame.modDays;
		c.doublePkP = loadgame.doublePkP;
		c.prayer = loadgame.prayer;
		c.Disable = loadgame.Disable;
		c.monkey = loadgame.monkey;
		c.newClue = loadgame.newClue;
		//c.HSDelay = loadgame.HSDelay;
		c.tempusingBow = loadgame.tempusingBow;
		c.tempusingArrows = loadgame.tempusingArrows;
		c.tempusingOtherRangeWeapons = loadgame.tempusingOtherRangeWeapons;
		c.stuckX = loadgame.stuckX;
		c.stuckY= loadgame.stuckY;
		c.stuckHeight= loadgame.stuckHeight;
		c.mutetimer = loadgame.mutetimer;
		c.pkpreset = loadgame.pkpreset;
		c.armaPoints = loadgame.armaPoints;
		c.saraPoints = loadgame.saraPoints;
		c.zamPoints = loadgame.zamPoints;
		c.vlsdmg = loadgame.vlsdmg;
		c.statdmg = loadgame.statdmg;
		c.vsdmg = loadgame.vsdmg;
		c.splate = loadgame.splate;
		c.vplate = loadgame.vplate;
		c.mplate = loadgame.mplate;
		c.zplate = loadgame.zplate;
		c.sleg = loadgame.sleg;
		c.mleg = loadgame.mleg;
		c.zleg = loadgame.zleg;
		c.vleg = loadgame.vleg;
		c.morrhelm = loadgame.morrhelm;
		c.zurhelm = loadgame.zurhelm;
		c.stathelm = loadgame.stathelm;
		c.guthan = loadgame.guthan;
		c.dharok = loadgame.dharok;
		c.cluelevel = loadgame.cluelevel;
		c.clueString = loadgame.clueString;
		c.jailedBy = loadgame.jailedBy;
		c.jailTimer = loadgame.jailTimer;
		c.skullTimer = loadgame.skullTimer;
		c.itemOnNpc = loadgame.itemOnNpc;
		c.pcPoints = loadgame.pcPoints;
		c.slayerTask = loadgame.slayerTask;
		c.slayerAmount = loadgame.slayerAmount;
		c.recoil = loadgame.recoil;
		c.newFag = loadgame.newFag;
		c.rewardPoints = loadgame.rewardPoints;
		c.cwPoints = loadgame.cwPoints;
		c.dPoints = loadgame.dPoints;
		c.vPoints = loadgame.vPoints;
		c.specRecharge = loadgame.specRecharge;
		c.firstBlood = loadgame.firstBlood;
		c.warrior = loadgame.warrior;
		c.bersk = loadgame.bersk;
		c.nomercy = loadgame.nomercy;
		c.hitman = loadgame.hitman;
		c.bountyhunter = loadgame.bountyhunter;
		c.kbd = loadgame.kbd;
		c.digs = loadgame.digs;
		c.lamps = loadgame.lamps;
		c.fires = loadgame.fires;
		c.saraDrink = loadgame.saraDrink;
		c.firstTime2 = loadgame.firstTime2;
		c.brews = loadgame.brews;
		c.caskets = loadgame.caskets;
		c.stake = loadgame.stake;
		c.td = loadgame.td;
		c.sara = loadgame.sara;
		c.zam = loadgame.zam;
		c.cali = loadgame.cali;
		c.totalFightPitWins = loadgame.totalFightPitWins;
		c.fightPitStreak = loadgame.fightPitStreak;
		c.barrow = loadgame.barrow;
		c.newFag2 = loadgame.newFag2;
		c.expLock = loadgame.expLock;
		c.slayerPoints = loadgame.slayerPoints;
		c.gemTimer = loadgame.gemTimer;
		c.Muted = loadgame.Muted;
		c.playerItems = loadgame.playerItem;
		c.achievements = loadgame.achievements;
		c.reset = loadgame.reset;
		c.playerItemsN = loadgame.playerItemN;
		c.playerEquipment = loadgame.playerEquipment;
		c.playerEquipmentN = loadgame.playerEquipmentN;
		c.achievementProgress = loadgame.achievementProgress;
		c.bankItems = loadgame.bankItems;
		c.bankItemsN = loadgame.bankItemsN;
		c.degradeItems = loadgame.degradeItems;
		c.playerLevel = loadgame.playerLevel;
		c.playerXP = loadgame.playerXP;
		c.apset = loadgame.apset;
		c.apset2 = loadgame.apset2;
		c.apset3 = loadgame.apset3;
		c.playerAppearanceSet = loadgame.playerAppearanceSet;
		if (loadgame.playerAppearance != null) {
			c.setLook(loadgame.playerAppearance);
		} else {
			c.setLook(new int[13]);
			
		}
		c.setPlayerMember(loadgame.playerMember);
		if (loadgame.friends != null) {
			c.setFriends(loadgame.friends);
		} else {
			c.setFriends(new long[200]);
		}
		c.setFriendsSize(loadgame.friendsSize);
		if (loadgame.ignores != null) {
			c.setIgnores(loadgame.ignores);
		} else {
			c.setIgnores(new long[100]);
		}
		c.setIgnoresSize(loadgame.ignoresSize);
		c.setPlayerBankSize(loadgame.playerBankSize);
	}

}