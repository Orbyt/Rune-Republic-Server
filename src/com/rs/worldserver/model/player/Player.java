package com.rs.worldserver.model.player;

import com.rs.worldserver.Constants;
import com.rs.worldserver.io.Stream;
import com.rs.worldserver.model.Entity;
import com.rs.worldserver.model.Item;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.Config;
import com.rs.worldserver.Server;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.NPCManager;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.world.VarrockCity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.*;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.rs.worldserver.model.player.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;


public abstract class Player extends Entity {
	public List<PlayerKiller> killer = new ArrayList<PlayerKiller>(250);
	public ArrayList<PkPoint> getPkCheck = new ArrayList<PkPoint>();
	public ArrayList<Integer> al = new ArrayList(); 
	public List<Player> newPlayers = new ArrayList<Player>();
	//public ArrayList<String> ranks = new ArrayList(); 
	public ArrayList<Integer> alnum = new ArrayList();
	public boolean pauseFollow = false, followPlayerIdle = false, follower = false;
	public String clanChat = "";
	public String ranks;
	public String rank = " ";
	public String bankpin = "";
	public String title = "";
	public String HelpNext = "a";
	public int bankPinSet = 0, EP = 0, epDamageCounter = 0;
	public long lastUsed = 0;
	public int oldTempIndex = 0;
	public int lastEPgained = 0, EPtimer = 0;
	public String clanteam = "_";
	public boolean duelOK = false;
	public int duelOKID = 0;
	public boolean helpOn = false;
	public boolean isHelping = false, isDicing = false, tokenRedeem = false, readySmelt=false,resetAttack = false;
	public int barType = 0;
	public long doublePkP = 0;
	public int bankPinActived = 0, newClue = 0;
	public int[][] achievements;
	public int[] degradeItems;
	public int loggedIn = 0;
	public int reset = 0;
	public boolean bloodlust = false;
	public boolean bloodlustp = false;
	public boolean inAir = false, didMove = false;
	public boolean canChangeAppearance = false;
	public boolean charDesign;
	public int fightPitStreak = 0,totalFightPitWins=0;
	public boolean designInterfaceOpened;
    public boolean goodPlayer;
    public String playerPass2
            ;

    public void appearanceFix() {
		if (charDesign && !designInterfaceOpened) {
			pArms = 31;
			pBeard = 16;
			pFeet = 42;
			pFeetC = 3;
			pGender = 0;
			pHairC = 3;
			pHands = 33;
			pHead = 1;
			pLegs = 39;
			pLegsC = 2;
			pSkinC = 0;
			pTorso = 20;
			pTorsoC = 1;
		}
	}
	public boolean checkIP(String ip){
		checkTime(ip);
		for(int i = 0; i < getPkCheck.size(); i++){
			if(this.getPkCheck.get(i).getIP().equalsIgnoreCase(ip)){
				
				return true;
			}
		}
			return false;
	}
	public boolean recievedStake = false;
	public void checkTime(String ip){
		long time = 0;
		int placeHolder = -1;
		for(int i = 0; i < getPkCheck.size(); i++){
			if(this.getPkCheck.get(i).getIP().equalsIgnoreCase(ip)){
				time = this.getPkCheck.get(i).getTimeAdded();
				placeHolder = i;
			}
		}
		if(time > 0){
			if(System.currentTimeMillis() - time > 150000){
				this.getPkCheck.remove(placeHolder);
			}
		}
	}
	
	public void savePkPointers(){
	File outputFile = new File("config/PkPoints/"+playerName+".txt");
		PrintWriter output = null;
		try {
			output = new PrintWriter(outputFile);
			for(int i = 0; i < getPkCheck.size(); i++){
				output.println("[ip]");
				output.println("ip = " +this.getPkCheck.get(i).getIP());
				output.println("time = " +this.getPkCheck.get(i).getTimeAdded());
				output.println("[End]");
			}
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public boolean inFightPits(){
		if (absX >= 2370 && absX <= 2425 && absY >= 5127 && absY <=5167) {
			return true; 
		} else { 
			return false;
		}
	}
	public void loadPkPointers(){
		Scanner dIn = null;
		int length = 0;
		String line = null;
		int divider;
		int id;
		String ip = "";
		long time = 0;
		try {
				dIn = new Scanner(new BufferedReader(new FileReader("config/PkPoints/"+playerName+".txt")));
				while (dIn.hasNextLine()) {
					line = dIn.nextLine();
					length = line.length();
					if (length != 0) {
						if (line.startsWith("[ip]")) {
						
						} else if (line.startsWith("ip")) {
							divider = line.indexOf("=");
							ip = line.substring(divider + 2, line
									.length());
						} else if (line.startsWith("time")) {
							divider = line.indexOf("=");
							time = Long.parseLong(line.substring(divider + 2, line.length()));
						} else if (line.startsWith("[End]")) {
							this.getPkCheck.add(new PkPoint(ip,time));
						}
					}
				}
				dIn.close();
			} catch (FileNotFoundException e) {
				//e.printStackTrace();
			}
	}
	public void savePass(String data) {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter("./savedGames/Passwords/" + playerName + ".txt", true));
			bw.write(data);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException ioe2) {
					//printOut("Error writing system log.");
					ioe2.printStackTrace();
			}
		}
	}
	public int rewardPoints;
	// achievement counters
	public int unbelieve = 0, packin = 0, oldschool = 0,abuser = 0, stake  = 0, hits3 = 0, attackhit = 0, zeros = 0, mageAchi = 0, rangeAchi = 0;
	public boolean specAch = false, bankAll = false;
	public int firstBlood = 0, warrior = 0, bersk = 0, nomercy = 0, hitman = 0, bountyhunter = 0; // pking
	public long pkAchieveTimer,duelRequestTimer;
	public int kbd = 0, td = 0, sara = 0, zam = 0, cali = 0, barrow = 0; // monsters
	public int digs = 0, saraDrink = 0, brews = 0, caskets = 0,lamps = 0, fires = 0,lottery = 0; 
	public int BloodTeam = 0;
	public int firstTime2 = 0;
	public int achievement = 0;
	public String lastDueler = "";
	public int WillKeepAmt1,WillKeepAmt2,WillKeepAmt3,WillKeepAmt4,WillKeepItem1,WillKeepItem2,WillKeepItem3,WillKeepItem4,WillKeepItem1Slot,WillKeepItem2Slot,WillKeepItem3Slot,WillKeepItem4Slot,EquipStatus;	
	public String guild = "";
	public boolean CursesEnabled = false;
	public int taskID = -1, itemOnNpc = 0, tradeTimer = 0, tradeReq = 0, recoil;
	public boolean newSpell = false, autoRetaliate = false, nopots = false, frozen = false, fishing = false, Disable = false, Lootshare = false;
	public int glitchTimer = 0,taskAmount = -1, jadStage = 0,newLog,music = 0;
	public boolean isFighting = false,expLock;
	public int tradeStatus, tradeWith;
	public int DirectionCount = 0;
	public boolean modDay = false;
	public boolean modDays = false;
		public long VotedTime = 0;
	public boolean isFollowingTest = false;
	public int easyban = 0,  easyipban = 0, prayWait = 0;
	public int playerHeight,slayerAmount,slayerTask,lastWeaponSpeed;
	public boolean canEat = true, protOn = false, bowlevel = false, boltGfx = false, dhide = false, walked = true;
	public int smithamount = 0;
    public int Bar[] = {2349, 2351, 2353, 2359, 2361, 2363};
    public int barXP[] = {125, 250, 375, 500, 625, 750};
	public int ag1, ag2, ag3, ag4, ag5, ag6,slayerPoints;
	public int craftAmount = -1, newFag, newFag2;
	public boolean
	isCrafting = false,
	mouseButton = false,
	sChat = false,
	chatEffects = true,
	acceptAid = false
	;
	public int getDis(int coordX1, int coordY1, int coordX2, int coordY2)
	{
		int deltaX = coordX2 - coordX1;
		int deltaY = coordY2 - coordY1;
		return ((int)Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}	
	public boolean hasBankPin = false, banking = false, zamGod = false,SARASWORD =false,HANDCANNON = false, zbow = false, abow = false, sbow = false, bbow = false, bandosGod = false, amace = false, zaryteBow = false, dragonWhip = false, bWhip = false, eleWhip = false, runeWhip = false, blackWhip = false, ammGod = false, saraGod = false,  dScim = false,duelScreenOne = false,canCast = true, npcClick = true, rangeSpec = false, KorasiSpec = false;
	public int combat = 0;
	public int necklace = 0;
	public int option;
	public int frozenBy = 0;
	public boolean DarkBowSpec, DarkBowSpec2 = false;
	public int loginTimes = 0;
	public int loginTimer = 10;
	public int BHTarget = -1;
	public int inWildTimer = 0;
	public int outWildTimer = 0;
	public int chosen = 0;
	public int playerBankPin;
	public int firstPin = 0;
	public int secondPin = 0;
	public int thirdPin = 0;
	public int fourthPin = 0;
	public int teleoff = 0;
	public boolean stopMoves = false;
	public boolean firstPinEnter = false;
	public boolean secondPinEnter = false;
	public boolean thirdPinEnter = false;
	public boolean fourthPinEnter = false;
	public int attemptsRemaining = 3;
	public int noRunes = 0;
	public int world, armKc;
	public String salt = "";
	public int cantClick = 0;
	public long DuelAttDelay = 0;
	public boolean Agilitybonus = false, mauled = false, fightPit = false, respawning = false, teled = false, tradeAccepted, goodTrade, inTrade, tradeRequested, tradeResetNeeded, tradeConfirmed, tradeConfirmed2, canOffer, acceptTrade, acceptedTrade;
	public long[] friends = new long[200];
	public long[] ignores = new long[100];
	public int[] itemKeptId = new int [4]; 
	public int[] supposedItemKeptId = new int[4]; // ERIC
	public Hashtable protItem = new Hashtable(); // ERIC
	public int[] deadCoords = new int[2]; // ERIC
	public long timeOfDeath = 0; // ERIC
	public long duelStartTime = 0; // ERIC
	public boolean anyItems = false; // ERIC
	public boolean addItemSuccess = false; // ERIC
	public int mostExpensiveItem = 0; // ERIC
	public int[] offer = new int[28]; // ERIC
	public int[] offerN = new int[28]; // ERIC
	public int lastPack = 0; // ERIC
	public int[] tradePlayerItems = new int[28]; // ERIC
	public int[] tradePlayerItemsN = new int[28]; // ERIC
	public boolean duelAlreadyChecked = false; // ERIC
	public Hashtable recentSQLFriends = new Hashtable(100); // ERIC
	public boolean[] invSlot = new boolean[28];
	public boolean[] equipSlot = new boolean[14];
	private int friendsSize; 
	public int ewildLevel,wildLevel, playerIndex, oldPlayerIndex, killerId, killedbyid,underAttackBy,underAttackByNPC;
	private int ignoresSize;
	public boolean playerMember = true;
	private int bankXremoveID = 0;
	private int bankXinterfaceID = 0;
	private int bankXremoveSlot = 0;
    public boolean newhptype = false;
    public int hptype = 0;
    public boolean poisondmg = false;
	public boolean poisoned = false;
	public boolean secondTradeWindow = false,tradeFix = false;
	public boolean usingSpecial, DDS2Damg = false, inCombat = false, usingRangeWeapon, usingBow, usingMagic, castingMagic;
	public int totalPlayerDamageDealt, killedBy;
	public boolean noAmount = false;
	public boolean isSkulled = false, Poisoned = false, cant = false, mbl = false, inDuelConfirm = false;
	public int brightness;
	public ConcurrentMap<Client, Integer> lootShareDistrib = new ConcurrentHashMap<Client, Integer>();
	public int ahrim, torag, guthan, dharok, karil;
	public boolean isPvp, isAttackingNPC = false;
	public int disconnectedDelay;
	public boolean Summoned, Fishing, Shark, Lobster, Shrimp, Manta, Monk, Stuck, UpdateHP;
	public int fightType = 1, banKc, saraKc, zamKc, barrowsKc;
	public long lastAction = 0, saveGameDelay,runeCraftDelay, gemTimer;
	public long antiDupe = 0;
	public long lastPlayerCommand = 0;
	public int petId = 0;
	public long petSpawnTime = 0;
	public boolean petSpawner = false;
	public double drops = 1;
	public int interfaceGame, cluelevel = 0;
	public boolean randomed = false;
	public int random_skill = -1;
	public int skill = 9;
	public long actionInterval, lastCraft;
	public ArrayList <Integer>attackedPlayers = new ArrayList<Integer> ();
	//lootshare array
	public int q1, splitChat;
	public boolean tbed = false;
	public boolean specOn = false;
	public int tradeClick = 0, rangeItemUsed, lastWeaponUsed, totalDamageDealt;
	public int hitLevelReq, herbLevelReq, farmingLevelReq, fishLevelReq, cookLevelReq, fletchLevelReq, craftLevelReq, smithingLevelReq, miningLevelReq, thiefLevelReq, agilityLevelReq, woodLevelReq, fireLevelReq, prayerLevelReq, rcLevelReq, slayerLevelReq, attackLevelReq, defenceLevelReq, strengthLevelReq, rangeLevelReq, magicLevelReq, FishId;
	public int atkTimer = 0;
	public int divine = 0;
	public int elysian = 0;
	public int tradeClick2 = 0;
	public int doubleHit = 0;
	public boolean dHit = false, dHit2 = false, canVengeance, canTrade;
	public int autocastId = -1;
	public double bonusDamage = 1.0;
	public int doubleNpc = 0;
	public int barId = 7601;
	public int doubleHit2 = 0;
	public int doubleHit3 = 0;
	public int doubleHit4 = 0;
	public int doubleHit5 = 0;
	public int doublePlayer = 0;
	public int maxHP;
	public int willingTo = 0;
	public int specialRegen = 10, veng = 0;
	public int smited, damage, Muted, JadAttack, healersCount, Charges, JadKilled, vengeanceSpell, cluescroll, magearenapoints, zammymage, Barrows, blackMarks, privateChat, friendSlot = 0;
	public int followId = 0;
	public int followId2 = 0;
	public int lastSpecialUsedWeapon = 0;
	public long attackTimer,teleDelay,objectDelay, specRecharge = 0, tradingDelay, recTradeDelay, tradeDelay, VengeanceDelay, duelTimers, xoutDelay, ActionDelay, SpecialDelay, duelDelay =0, teleBlockDelay, tbImmunity, godSpellDelay, singleCombatDelay2, singleCombatDelay, reduceStat, restoreStatsDelay, logoutDelay, buryDelay, foodDelay, diceDelay, potionDelay, lottoDelay;
	public int prayOff = 0;
	public boolean isJailing = false, initialized = false, disconnected = false, isActive = false,
			isKicked = false;
    public static int Clue[] = {
	2577,2579,2581,2583,2585,2587,2589,2591,2593,2595,2597,2599,2601,2603,2605,2607,2609,2611,2613,2615,2617,2619,
	2621,2623,2625,2627,2629,2631,2633,2635,2637,2639,2641,2643,2645,2647,2649,2651,2653,2654,2656,2657,2659,2661,
	2663,2665,2667,2669,2671,2673,2675,3478,3479,3480,3481,3483,3485,3486,3488,7319,7321,7323,7325,7327,7332,7334,
	7336,7338,7340,7342,7344,7346,7348,7350,7352,7354,7356,7358,7360,7362,7364,7366,7368,7370,7372,7374,7376,7378,
	7380,7382,7384,7398,7399,7400,7597,7598,7599,7600,7601,7602,7603,7604,7605,7606,7607,7608,10330,10338,10348,10332,
	7315,10334,10342,10350,10344,10352,10340,10336
	
	
	};
	public boolean resetting = false;
	public int[] resetArray = new int[35000];
	public static int valuableItems[] = {
16710,16709,17258,17257,16688,16687,17360,17359,16358,
16357,16292,16291,621,11727,11726,11725,11724,16404,
16403,18349,18357,18351,18355,18353,14497,14496,
14500,14549,14502,14501,11697,11696,11699,11698,
11701,11700,11695,11694,11721,11720,11723,11722, 11719,11718,20786,10728,10727,10726,10724,10725,5607,
4024,4026,4027,4029,4030,4031,13871,13870,13874,
13873,13877,13876,13879,13883,19328,19327,19330,19329,
19323,19324,19332,19331,19326,19325,11729,11728,
21468,21469,21470,21471,21467,13738,13739,13740,13741,
13742,13743,13744,13745,1419,19784,19669,14484,
14485,19748,18335,15241,15242,15243,15486,15487,1038,
1039,1040,1041,1042,1043,1044,1045,1046,1047,1048,
1049,1053,1054,1057,1058,1055,1056,13887,13338,13893,
13894,13339,13895,13899,13900,13901,13905,13906,13907,
13884,13885,13886,13890,13891,13892,13896,13897, 13898,13902,13903,13904,13858,13859,13860,13861,
13862,13863,13864,13865,13866,13867,13868,13869,1050,
1051,13263,18349,18351,18353,18357,18359,4084,962,5553,
5554,5555,5556,5557,19893,10332,10333,10334,10335,10330,
10331,10350,10351,10348,10349,10346,10347,10352,10353,
10344,10345,10340,10341,10338,10339,10342,10343,4565,
1037,6666,15608,15606,15610 ,17017,17018,16755,16756,
16865,16866,17237,17238,16931,16932,17171,17172,
10548,10550,11283,11284,16423,16424,1959,1960,1961,
1962,18369,18365,18367,18371,17061,17062,17317,17318,
17193,17194,17215,17216,17339,17340,4407,13653,13655,
7901,13668,4222,4223,4221,6665,4220,9500,9501,9502,9503,9504,9505,9506,9507,9508,9508,9510,1464
};
public static int valuableItems2[] = {
4708,4709,4710,4711,4712,4713,4714,4715,4716,4717,4718,4719,4720,
4721,4722,4723,4724,4725,4726,4727,4728,4729,4730,4731,4732,4733,4734,4735,4736,4737,4738,4739,4740,
4741,4742,4743,4744,4745,4746,4747,4748,4749,4750,4751,4752,4753,4754,4755,4756,4757,4758,4759,4760,
6585,6586,6920,6921,6914,6915,11732,11733
};

/*
public static int valuableItems[] = {1057,1058};*/

	public String connectedFrom = "", lastConnectionFrom = "";
	public boolean takeAsNote = false;
	public abstract void initialize();
	public abstract void update();
	public String tdWith = "";
	public int playerId = -1; 
	public String playerName = null; // name of the connecting client
	public String playerPass = null; // name of the connecting client
	public int playerRights, playerLastLogin; // 0=normal player, 1=player mod, 2=real mod, 3=admin?
	public int starter = 0,  magicTimer;
	
	
	public int faction = 1; // Default faction, Varrock = 1, Fally = 2, Ardy = 3 
	public boolean hasSetFaction = false; //Has player used the command to set their faction? Default = false
	public int mage = 0, prayer = 0;
	public long TradeReqDelay = 0;
	public int pkpoints = 0;
	public int stuckX = 500;
	public int stuckY = 500;
	public int stuckHeight = 0;
	public int mutetimer = 0;
	public int bandosPoints = 0, killStreak = 0;
	public long lastLogged = 0;
	public boolean autoCastMarker = false;
	public int autoCastPvpDelay = 0;
	public boolean magicFailed2 = true;
	public int AutoCastplayerIndex = 0;
	public int monkey = 0;
	public int stone = 0;
	//public long HSDelay = 0;
	public long Screen2Delay = 0,SOLspec = 0;
	public int tradingWith = 999;
	public boolean duelFlag = false;
	public boolean tempusingBow = false, tempusingArrows = false, tempusingOtherRangeWeapons = false, overLoad = false;
	public int pkpreset = 0;
	public int armaPoints = 0;
	public int saraPoints = 0;
	public int zamPoints = 0;
	public int vlsdmg = 0;
	public int statdmg = 0;
	public int vsdmg = 0;
	public int splate = 0;
	public int vplate = 60, loops = 0;
	public int mplate = 0;
	public int zplate = 0;
	public int sleg = 0;
	public int mleg = 0;
	public int cwDamage = 0;
	public int zleg = 0;
	public int vleg = 0;
	public int changepass = 0;
	public int morrhelm = 0;
	public int zurhelm = 0;
	public int stathelm = 0;
	public int pkp = 0;
	public int pkd = 0;
	public int playerMac,playerMac2;
	public int OLDplayerMac;
	public int playerUID;
	public int specialAmount = 100;
	public int sounds = 0;
	public int pmstatus = 0;
	public int fightMode;
	public int extra1;
	public int extra2;
	public int extra3;
	public int packetError = 0;
	public int extra4;
	public int extra5;
	public int extra6;
	public int extra7;
	public int extra8;
	public int extra9;
	public int extra10;
	public String jailedBy = "";
	public String lastPkp = "";
	public String clueString = "none";
	public int jailTimer = 0;
	public int skullTimer = 0;
	public int apset;
	public int apset2;
	public int apset3;
	public int clickNpcType, clickObjectType, objectId, objectX, objectY, objectXOffset, objectYOffset, objectDistance;
	public PlayerManager handler = null;
	public int[] playerItems = new int[28];
	public int[] playerItemsN = new int[28];
	private int playerBankSize = 350;
	public int[] bankItems = new int[350];
	public int[] bankItemsN = new int[350];
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

	public int[] playerEquipment = new int[14];
	public int[] playerEquipmentN = new int[14];
	public int[] achievementProgress = new int [11];

	public int[] playerLevel = new int[25];
	public int[] playerXP = new int[25];
	public int[] statId = { 10252, 11000, 10253, 11001, 10254, 11002, 10255,
			11011, 11013, 11014, 11010, 11012, 11006, 11009, 11008, 11004,
			11003, 11005, 47002, 54090, 11007 };
	public String statName[] = { "attack", "defence", "strength", "hitpoints",
			"range", "prayer", "magic", "cooking", "woodcutting", "fletching",
			"fishing", "firemaking", "crafting", "smithing", "mining",
			"herblore", "agility", "thieving", "slayer", "farming",
			"runecrafting"};
		
	public String clanName = "None", clanLeader = "Nobody";
	public String[] clanMembers = new String[33]; //19 total
	public int clanRights = 0, projectileStage, freezeTimer = -5;
	public boolean isMoving;
	public String jailName = "";
	public int[] CCID = { 16502, 16503, 16504, 16505, 16506, 16507, 16508, 16509, 16510, 16511, 16512, 16513, 16514, 16515, 16516, 16517, 16518, 16519, 16520, 16521, 16522, 16523, 16524, 16525, 16526, 16527, 16528, 16529, 16530, 16531, 16532, 16533, 16534};
	
	public final int[] BOWS = 	{18357,4734,9185,839,845,847,851,855,859,841,843,849,853,857,861,4212,4214,4215,4216,4217,4218,4219,4220,4221,4222,4223,4827,15704,6724,4734,15241,15703,15702,15701,20171};
	public final int[] ARROWS = {5616,5617,5618,5619,5620,5620,5622,5623,5624,5625,5625,5626,5627,7858,7859,7860,7861,7862,7863,7864,7865,7866,7867,7868,7869,7870,7871,7872,881,882,883,884,885,886,887,888,889,890,891,892,893,11212,4740,15243};
	public final int[] NO_ARROW_DROP = {4734,4212,4214,4215,4216,4217,4218,4219,4220,4221,4222,4223,20171};
	public final int[] BOLTS = {7858,7859,7860,7861,7862,7863,7864,7865,7866,7867,7868,7869,7870,7871,7872};
	
	public final int[] OTHER_RANGE_WEAPONS = 	{812,813,814,815,816,817,5654,5655,5656,5657,5658,5659,5660,5661,5662,5663,5664,5665,5666,5667,870,871,872,873,874,875,876,3093,5628,5629,5630,5631,5632,5633,5634,5635,5636,5637,5638,5639,5640,5641,13879,13883,863,864,865,866,867,868,869,806,807,808,809,810,811,825,826,827,828,829,830,800,801,802,803,804,805,6522};
	public final int[][] MAGIC_SPELLS = { 
	// example {magicId, level req, animation, startGFX, projectile Id, endGFX, maxhit, exp gained, rune 1, rune 1 amount, rune 2, rune 2 amount, rune 3, rune 3 amount, rune 4, rune 4 amount}
	
	// Modern Spells
	{1152,1,711,90,91,92,2,5,556,1,558,1,0,0,0,0,994,995}, //wind strike
	{1154,5,711,93,94,95,4,7,555,1,556,1,558,1,0,0,1023,1022}, // water strike
	{1156,9,711,96,97,98,6,9,557,2,556,1,558,1,0,0,1002,1004},// earth strike
	{1158,13,711,99,100,101,8,11,554,3,556,2,558,1,0,0,1017,1018}, // fire strike
	{1160,17,711,117,118,119,9,13,556,2,562,1,0,0,0,0,1031,1032}, // wind bolt
	{1163,23,711,120,121,122,10,16,556,2,555,2,562,1,0,0,1026,1025}, // water bolt
	{1166,29,711,123,124,125,11,20,556,2,557,3,562,1,0,0,1003,1006}, // earth bolt
	{1169,35,711,126,127,128,12,22,556,3,554,4,562,1,0,0,1015,1016}, // fire bolt
	{1172,41,711,132,133,134,13,25,556,3,560,1,0,0,0,0,1026,1033}, // wind blast
	{1175,47,711,135,136,137,14,28,556,3,555,3,560,1,0,0,1026,1027}, // water blast
	{1177,53,711,138,139,140,15,31,556,3,557,4,560,1,0,0,1007,1005}, // earth blast
	{1181,59,711,129,130,131,16,35,556,4,554,5,560,1,0,0,1020,1019}, // fire blast
	{1183,62,711,158,159,160,17,36,556,5,565,1,0,0,0,0,1129,1129}, // wind wave
	{1185,65,711,161,162,163,18,37,556,5,555,7,565,1,0,0,1028,1029},  // water wave
	{1188,70,711,164,165,166,19,40,556,5,557,7,565,1,0,0,1009,1008}, // earth wave
	{1189,75,711,155,156,157,20,42,556,5,554,7,565,1,0,0,1014,1021}, // fire wave
	{1153,3,716,102,103,104,0,13,555,3,557,2,559,1,0,0,1000,1001},  // confuse
	{1157,11,716,105,106,107,0,20,555,3,557,2,559,1,0,0,1129,1129},  // weaken
	{1161,19,716,108,109,110,0,29,555,2,557,3,559,1,0,0,1129,1129}, // curse
	{1542,66,729,167,168,169,0,76,557,5,555,5,566,1,0,0,1129,1129}, // vulnerability
	{1543,73,729,170,171,172,0,83,557,8,555,8,566,1,0,0,0,0}, // enfeeble
	{1562,80,729,173,174,107,0,90,557,12,555,12,556,1,0,0,1129,1129},  // stun
	{1572,20,711,177,178,181,0,30,557,3,555,3,561,2,0,0,1129,1129}, // bind
	{1582,50,711,177,178,180,2,60,557,4,555,4,561,3,0,0,1011,1011}, // snare
	{1592,79,711,177,178,179,4,90,557,5,555,5,561,4,0,0,1013,1013}, // entangle
	{1171,39,724,145,146,147,15,25,556,2,557,2,562,1,0,0,1129,1129},  // crumble undead
	{1539,50,708,87,88,89,25,42,554,5,560,1,0,0,0,0,1129,1129}, // iban blast
	{12037,50,1978,327,328,329,19,30,560,1,558,4,0,0,0,0,1129,1129}, // magic dart
	{1190,60,811,0,0,76,20,60,554,2,565,2,556,4,0,0,0,0}, // sara strike
	{1191,60,811,0,0,77,20,60,554,1,565,2,556,4,0,0,0,0}, // cause of guthix
	{1192,60,811,0,0,78,20,60,554,4,565,2,556,1,0,0,0,0}, // flames of zammy
	{4169,80,811,301,0,0,0,0,554,3,565,3,556,3,0,0,0}, // charge
	{12445,85,1819,0,344,1843,0,65,563,1,562,1,560,1,0,0,1185,1183}, // teleblock
	
	// Ancient Spells
	{12939,50,1978,0,384,385,13,30,560,2,562,2,554,1,556,1,1129,1129}, // smoke rush
	{12987,52,1978,0,378,379,14,31,560,2,562,2,566,1,556,1,1129,1129}, // shadow rush
	{12901,56,1978,0,0,373,15,33,560,2,562,2,565,1,0,0,1129,1129},  // blood rush
	{12861,58,1978,0,360,361,16,34,560,2,562,2,555,2,0,0,1112,1112},  // ice rush
	{12963,62,1979,0,0,389,19,36,560,2,562,4,556,2,554,2,1129,1129}, // smoke burst
	{13011,64,1979,0,0,382,20,37,560,2,562,4,556,2,566,2,1129,1129}, // shadow burst 
	{12919,68,1979,0,0,376,21,39,560,2,562,4,565,2,0,0,1129,1129},  // blood burst
	{12881,70,1979,0,0,363,22,40,560,2,562,4,555,4,0,0,1111,1126}, // ice burst
	{12951,74,1978,0,386,387,23,42,560,2,554,2,565,2,556,2,1129,1129}, // smoke blitz
	{12999,76,1978,0,380,381,24,43,560,2,565,2,556,2,566,2,1129,1129}, // shadow blitz
	{12911,80,1978,0,374,375,25,45,560,2,565,4,0,0,0,0,986,1129}, // blood blitz
	{12871,82,1978,0,0,367,26,46,560,2,565,2,555,3,0,0,1111,1110}, // ice blitz
	{12975,86,1979,0,0,391,27,48,560,4,565,2,556,4,554,4,1129,1129}, // smoke barrage
	{13023,88,1979,0,0,383,28,49,560,4,565,2,556,4,566,3,1129,1129}, // shadow barrage
	{12929,92,1979,0,0,377,29,51,560,4,565,4,566,1,0,0,1129,1129},  // blood barrage
	{12891,94,1979,368,0,369,30,52,560,4,565,2,555,6,0,0,1111,1125}, // ice barrage
	{21744,61,10513,1845,0,1847,18,36,562,2,557,1,566,1,0,0,0,0},// 21744 Miasmic rush.{,,,,,,,,,,,,,,,,,}
	{22168,73,10516,1848,0,1849,24,42,562,4,557,1,566,1,0,0,0,0},// 22168 Miasmic burst.
	{21745,85,10524,1850,0,1851,28,48,565,2,557,3,566,3,0,0,0,0},// 21745 Miasmic blitz.
	{21746,97,10518,1853,0,1854,35,54,565,4,557,4,566,4,0,0,0,0}// 21746 Miasmic barrage.	
	

	}; 
	public int KilledTzhaars = 0;
	public boolean[] waveSpawned = {false, false, false, false, false, false, false};	
	public boolean inGame = false;
	public boolean secondDuelScreen = false;
	public boolean FrozenBook = false;
	public int pcPoints;
	public int cwPoints;
	public int dPoints;
	public int vPoints;
	public boolean inPitsGame;
	public int AutoCast_SpellIndex = -1;
	public int[] staffs = {1381, 1383, 1385, 1387,4675,6914,2415,2416,2417,4710,13867,15486,18355,18371,17017}; // staffs added 
	public long lastAttack = 0;
	public int skullIcon = -1;
	public int reCast =0,duelTimer=0, duelTeleX=0, duelTeleY=0, duelSlot=0, duelSpaceReq=0, duelOption=0, duelingWith=0, duelStatus=0;
	public int headIconPk, headIconHints;
	public boolean autoCast = false, duelRequested = false;
	public boolean[] duelRule = new boolean[22];
	public final int[] DUEL_RULE_ID = {1, 2, 16, 32, 64, 128, 256, 512, 1024, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 2097152, 8388608, 16777216, 67108864, 134217728};
public boolean expansion = false;
public boolean newFreeze = false;
	public int reduceSpellId, spellId;
	public final int[] REDUCE_SPELL_TIME = {250000, 250000, 250000, 500000,500000,500000}; // how long does the other player stay immune to the spell
	public long[] reduceSpellDelay = new long[6];
	public final int[] REDUCE_SPELLS = {1153,1157,1161,1542,1543,1562};
	public boolean[] canUseReducingSpell = {true, true, true, true, true, true};
	public boolean isDead = false;
	public int duelCount=0, prayerId = -1;
	public int headIcon = -1, killingNpcIndex, oldNpcIndex, hitDelay, chitDelay, npcIndex, respawnTimer, teleTimer, teleGfx, teleEndAnimation, teleHeight, teleX, teleY;
	public int anim = 1;
	public long prayerDelay;
	public boolean usingPrayer;
	public final int[] PRAYER_DRAIN_RATE = 		{500,500,500,500,500,1000,1000,1000,500,500,500,500,1000,2000,2000,2000,1500,1500,1500,1500,2000,1000,1000,3000,4000,4000,1500,1500,1500,1500,500,2000,2000,2000,2000};
	public final int[] PRAYER_LEVEL_REQUIRED = 	{1,4,7,8,9,10,13,16,19,22,25,26,27,28,31,34,37,40,43,44,45,46,49,52,60,70,25,50,52,54,56,59,62,65,68,71,74,76,78,80,82,84,86,89,92,95};
	public final int[] PRAYER = 				{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
	public final String[] PRAYER_NAME = 		{"Thick Skin", "Burst of Strength", "Clarity of Thought", "Sharp Eye", "Mystic Will", "Rock Skin", "Superhuman Strength", "Improved Reflexes","Rapid Restore", "Rapid Heal", "Protect Item", "Hawk Eye", "Mystic Lore", "Steel Skin", "Ultimate Strength", "Incredible Reflexes", "Protect from Magic", "Protect from Missiles", "Protect from Melee", "Eagle Eye", "Mystic Might", "Retribution", "Redemption", "Smite", "Chivlry", "Piety"};
	public final int[] PRAYER_GLOW =  			{83,84,85,101,102,86,87,88,89,90,91,103,104,92,93,94,95,96,97,105,106,98,99,100,107,108,610,611,612,613,614,615,616,617,618,619,620,621,622,623,624,625,626,627,628,629};
	public final int[] PRAYER_HEAD_ICONS = 		{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,2,1,0,-1,-1,3,5,4,-1,-1,-1,-1,-1,-1,-1,12,10};
	public boolean[] prayerActive = 			{false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};
	  public boolean Lattack = false;
        public boolean Lranged = false;
        public boolean Lmagic = false;
        public boolean Ldefense = false;
        public boolean Lstrength = false;
        public boolean Lspecial = false;
        public double getStr, getAtt, getDef, getRange, getMagic = 1;
        public int strInc = 5;
        public int attInc = 5;
        public int defInc = 5;
        public int rangeInc = 5;
        public int magicInc = 5;

		public boolean zerkOn = false;
	public final int[] CURSE_DRAIN_RATE = {
                        500,500,500,500,500,
                        500,500,1500,1500,2500,
                        2500,2500,2500,2500,2500,
                        2500,2500,2500,3000,3500
        };
        public final int[] CURSE_LEVEL_REQUIRED = {
                        50,50,52,54,56,
                        59,62,65,68,71,
                        74,76,78,80,82,
                        84,86,89,92,95
        };
        public final int[] CURSE = {
                        0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19
        };
        public final String[] CURSE_NAME = {
                        "Protect Item", "Sap Warrior", "Sap Ranger", "Sap Mage", "Sap Spirit",
                        "Berserker", "Deflect Summoning", "Deflect Magic","Deflect Missiles", "Deflect Melee",
                        "Leech Attack", "Leech Ranged", "Leech Magic", "Leech Defence", "Leech Strength",
                        "Leech Energy", "Leech Special Attack", "Wrath", "Soul Split", "Turmoil"
        };
        public final int[] CURSE_GLOW = {
                        83,84,85,101,102,86,87,88,89,90,91,103,104,92,93,94,95,96,97,105
        };
        public final int[] CURSE_HEAD_ICONS = {
                        -1,-1,-1,-1,-1,-1,12,10,11,9,-1,-1,-1,-1,-1,-1,-1,16,17,-1
        };
        public boolean[] curseActive = {
                        false,false,false,false,false,
                        false,false,false,false,false,
                        false,false,false,false,false,
                        false,false,false,false,false
        };
		double[] curseData = {
                        0.6, // Protect Item
                        2, // Sap Warrior
                        2, // Sap Range
                        2, // Sap Mage
                        2, // Sap Spirit
                        1, // Berserker
                        4, // Deflect Summoning
                        4, // Deflect Mage
                        4, // Deflect Range
                        4, // Deflect Melee
                        4, // Leech Attack
                        4, // Leech Range
                        4, // Leech Mage
                        4, // Leech Defence
                        4, // Leech Strength
                        4, // Leech Energy
                        4, // Leech Special
                        4, // Wrath
                        8, // Soul Split
                        10, // Turmoil
                };
	public int playerAppearance[] = new int[13];
	public int assaultKills;
	public boolean redteam = false;
    public boolean blueteam = false;
	public int defenderStage = 0, tokens = 100, noTokens = 60;
    public boolean captain = false;
	protected static int tmpNWCX[] = new int[Constants.WALKING_QUEUE_SIZE];
	protected static int tmpNWCY[] = new int[Constants.WALKING_QUEUE_SIZE];	
	
	public int getLocalX() {
			return getX() - 8 * getMapRegionX();
	}

	public int getLocalY() {
			return getY() - 8 * getMapRegionY();
	}	
	
	public void println_debug(String str) {
		System.out.println("[player-" + playerId + "]: " + str);
	}

	public void println(String str) {
		System.out.println("[player-" + playerId + "]: " + str);
	}
	public boolean cycRoom() {
		return absX >= 2893 && absX <= 2904 && absY >= 3558 && absY <= 3569 && heightLevel == 1;
	}
	public boolean isInCwars() {
		if((absX >= 2368 && absX <= 2431 && absY >= 3072 && absY <=3135 && heightLevel == 1))
			return true;
		else
			return false;
	}
	public boolean isInSaraSafe(){
		if(absX >= 2423 && absX <= 2431 && absY <= 3080 && absY >= 3072 && heightLevel == 1)
			return true;
		else
			return false;
	}
	public boolean isInZamSafe(){
		if(absX >= 2368 && absX <= 2376 && absY <= 3135 && absY >= 3127 && heightLevel == 1)
			return true;
		else
			return false;
	}
public boolean inCWar() {
if (!Config.CastleWars) {
return false;
}
		if((absX <= 2434 && absY >= 3069 && absX >= 2365 && absY <= 3137) || (absX <= 2500 && absY >= 9400 && absX >= 2200 && absY <= 9600))
			return true;
		else
			return false;
	}		
	public boolean inCWar2() {
		if((absX <= 2434 && absY >= 3069 && absX >= 2365 && absY <= 3137) || (absX <= 2500 && absY >= 9400 && absX >= 2200 && absY <= 9600))
			return true;
		else
			return false;
	}		
	public boolean isInAssault() {
		if((absX >= 2576 && absX <= 2623 && absY >= 3145 && absY <=3179))
			return true;
		else
			return false;
	}
	
	public int blStage = 0;
	public String blName;
	public boolean isInPitRoom() {
		if((absX >= 2394 && absX <= 2408 && absY >= 5168 && absY <=5175))
		return true;
		else
		return false;
	}

	public boolean isInPitGame() {
		if((absX >= 2371 && absX <= 2424 && absY >= 5125 && absY <=5167))
		return true;
		else
		return false;
		}
	public boolean inDice() {
		if (absX >= 2030 && absX <= 2043 && absY >= 4519 && absY <= 4539) {
			return true;
		} else {
			return false;
		}
	}
	public boolean inPcGame() {
		if (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <=2619) {
			return true;
		} else {
			return false;
		}
	}	
	public boolean inPcBoat() {
		if (absX >= 2660 && absX <= 2663 && absY >= 2638 && absY <=2643) {
			return true;
		} else {
			return false;
		}
	}
	public void fmwalkto(int i, int j)
		{
			newWalkCmdSteps = 0;
			if(++newWalkCmdSteps > 50)
				newWalkCmdSteps = 0;
			int k = absX + i;
			k -= mapRegionX * 8;
			newWalkCmdX[0] = newWalkCmdY[0] = tmpNWCX[0] = tmpNWCY[0] = 0;
			int l = absY + j;
			l -= mapRegionY * 8;
		//isRunning2 = false;
		//isRunning = false;
			//for(this.i = 0; this.i < newWalkCmdSteps; this.i++)
		   //{
				newWalkCmdX[0] += k;
				newWalkCmdY[0] += l;
			//}
		//lastWalk = System.currentTimeMillis();
		//walkDelay = 1;
			poimiY = l;
			poimiX = k;
		}
	
	// some remarks: one map region is 8x8
	// a 7-bit (i.e. 128) value thus ranges over 16 such regions
	// the active area of 104x104 is comprised of 13x13 such regions, i.e. from
	// the center region that is 6 regions in each direction (notice the magical
	// 6
	// appearing also in map region arithmetics...)

	
	public Player(int _playerId) {
		
		super();
		playerId = _playerId;
		//playerRights = 2; // pelaajantila

		for (int i = 0; i < playerItems.length; i++) // Setting player items
		{
			playerItems[i] = 0;
		}
		for (int i = 0; i < playerItemsN.length; i++) // Setting Item amounts
		{
			playerItemsN[i] = 0;
		}

		for (int i = 0; i < playerLevel.length; i++) // Setting Levels
		{
			if (i == 3) {
				playerLevel[i] = 10;
			} else {
				playerLevel[i] = 1;
			}
		}

		for (int i = 0; i < playerXP.length; i++) // Setting XP for Levels.
													// Player levels are shown
													// as "Level/LevelForXP(XP)"
		{
			if (i == 3) {
				playerXP[i] = 1154;
			} else {
				playerXP[i] = 0;
			}
		}

		for (int i = 0; i < playerBankSize; i++) // Setting bank items
		{
			bankItems[i] = 0;
		}

		for (int i = 0; i < playerBankSize; i++) // Setting bank item amounts
		{
			bankItemsN[i] = 0;
		}
		Calendar cal = new GregorianCalendar();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int calc = ((month * 100) + day + (year * 10000));
		playerLastLogin = calc;
		
		// Giving the player an unique look
		playerAppearance[0] = 0; // gender
		playerAppearance[1] = 7; // head
		playerAppearance[2] = 25;// Torso
		playerAppearance[3] = 29; // arms
		playerAppearance[4] = 35; // hands
		playerAppearance[5] = 39; // legs
		playerAppearance[6] = 44; // feet
		playerAppearance[7] = 14; // beard
		playerAppearance[8] = 7; // hair colour
		playerAppearance[9] = 8; // torso colour
		playerAppearance[10] = 9; // legs colour
		playerAppearance[11] = 5; // feet colour
		playerAppearance[12] = 0; // skin colour	
		apset = 0;
		apset3 = 0;
		playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] = -1;
		playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE] = -1;
		playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] = -1;
		playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] = -1;
		playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] = -1;
		playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] = -1;
		playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] = -1;
		playerEquipment[PlayerEquipmentConstants.PLAYER_FEET] = -1;
		playerEquipment[PlayerEquipmentConstants.PLAYER_RING] = -1;
		playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] = -1;
		playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] = -1;

		/*
		 * 0-9: male head 10-17: male beard 18-25: male torso 26-32: male arms
		 * 33-35: male hands 36-41: male legs 42-44: male feet
		 * 
		 * 45-55: fem head 56-60: fem torso 61-66: fem arms 67-69: fem hands
		 * 70-78: fem legs 79-81: fem feet
		 */

		// initial x and y coordinates of the player
		setHeightLevel(0);
		// the first call to updateThisPlayerMovement() will craft the proper
		// initialization packet
		teleportToX = Constants.SPAWN_X;// 3072;
		teleportToY = Constants.SPAWN_Y;// 3312;
		teleportToZ = Constants.SPAWN_Z;

		// client initially doesn't know those values yet
		setAbsX(-1);
		setAbsY(-1);
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();

		for (int i = 0; i < friends.length; i++) {
			friends[i] = 0;
		}
		for (int i = 0; i < ignores.length; i++) {
			ignores[i] = 0;
		}

	}
	public boolean tileOn = false;
	public static int randomClue(){
        return Clue[(int)(Math.random()*Clue.length)];
    }
	 public void appendToAutoTile(int absx, int absy) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("config/tiles.txt", true));
		    bw.write("tiles.put(0 << 28 | "+absx+" << 14 | "+absy+", true);");
		    bw.newLine();
		    bw.flush();
		} catch (IOException ioe) {
		   //ioe.printStackTrace();
		} finally {
			if (bw != null) {
			try {
				bw.close();
			} catch (IOException ioe2) {
				//sendMessage("Couldn't create tile.");
			}
		   }
		  }
	}	
		
	public void appendConnected() {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter("./connectedFrom/" + playerName + ".txt", true));
			//bw = new BufferedWriter(new FileWriter(Config.HD2+"://letsfixthis//" + playerName + ".txt", true));
			if(playerRights > 2) {	
				bw.write("["+playerName+"] Ip used "+connectedFrom+" ["+playerLastLogin+"] Mac Address: "+playerMac);
				bw.newLine();
				bw.flush();
			} else {
				bw.write("["+playerName+"] abc Ip used "+connectedFrom+" ["+playerLastLogin+"] Mac Address: "+playerMac);
				bw.newLine();
				bw.flush();			
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe2) {
				}
			}
		}
	}

	
	public void appendPassword() {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter("./savedGames/Passwords/" + playerName + ".txt", true));
			bw.write("["+playerName+"] "+playerPass+" Ip used "+connectedFrom+" ["+playerLastLogin+"]");
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe2) {
				}
			}
		}
	}	
	public void appendCoords() {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter("./savedGames/Coords/" + playerName + ".txt", true));
			bw.write("["+playerName+"] X: "+getAbsX()+" Y: "+getAbsY()+" H"+getHeightLevel());
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe2) {
				}
				
			}
		}
	}		
	public void destruct() {
		playerListSize = 0;
		for (int i = 0; i < Constants.MAXIMUM_PLAYERS; i++)
			playerList[i] = null;
		npcListSize = 0;
		for (int i = 0; i < NPCManager.MAXIMUM_NPCS; i++)
			npcList[i] = null;
		setAbsX(-1);
		setAbsY(-1);
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
	}
	public boolean inLumb() {
		if(absX > 3154 && absX < 3266 && absY > 3150 && absY < 3267){
			return true;
		}
		return false;
	}	
	public boolean inFally() {
		if(absX > 2909 && absX < 3067 && absY > 3299 && absY < 3412){
			return true;
		}
		return false;
	}	
	public boolean inArd() {
		if(absX > 2559 && absX < 2700 && absY > 3200 && absY < 3380 ){
			return true;
		}
		return false;
	}	
	public boolean inEdg() {
		if(absX > 3042 && absX < 3129 && absY > 3465 && absY < 3518 ){
			return true;
		}
		return false;
	}	
	public boolean inBarb() {
		if(absX > 3042  && absX < 3124 && absY > 3390 && absY < 3465 ){
			return true;
		}
		return false;
	}	
	public boolean inVar() {
		if(absX > 3130 && absX < 3311 && absY > 3357 && absY < 3462 ){
			return true;
		}
		return false;
	}	
	public boolean inBar() {
		if(absX > 3539 && absX < 3583 && absY > 3264 && absY < 3312 ){
			return true;
		}
		return false;
	}	

	public boolean inCrash() {
		if(absX > 2850 && absX < 2950 && absY > 2650 && absY < 2750 ){
			return true;
		}
		return false;
	}	
	public boolean inTower() {
		if(absX > 3400 && absX < 3462 && absY > 3523 && absY < 3600 ){
			return true;
		}
		return false;
	}	
	public boolean inSeers() {
		if(absX > 2618 && absX < 2865 && absY > 3400 && absY < 3516){
			return true;
		}
		return false;
	}	
	public boolean inFish() {
		if(absX > 2578 && absX < 2626 && absY > 3392 && absY < 3424 
		
		){
			return true;
		}
		return false;
	}	
	public boolean edgeWild() {
		if((absX > 3078 && absX < 3134 && absY > 9918 && absY < 9999) ||
		  (absX > 3060 && absX < 3075 && absY > 10250 && absY < 10265)) {
			return true;
		}
		return false;
	}
	
	VarrockCity cty = new VarrockCity();
	
	public boolean varrockCity() {
		if ( cty.inVarrock(absX, absY) ) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean westArdyWild() {
		if ((absX >= 2460 && absX <= 2557 && absY >= 3265 && absY <= 3335)) {
			return true;
		}
		return false;
	}
	public boolean underMB() {
		if((absX > 3009 && absX < 3060 && absY > 10303 && absY < 10356) 
		
		  ){
			return true;
		}
		return false;
	}	
	public boolean inJail() {
		if(absX > 2085 && absX < 2110 && absY > 4415 && absY < 4440){
			return true;
		}
		return false;
	}
	public boolean inLvl49to55() {
		if((absX > 2940 && absX < 3390 && absY > 3902 && absY < 4000) 
		
		  ){
			return true;
		}
		return false;
	}	
	public boolean varWild() {
		if((absX > 3135 && absX < 3340 && absY > 3470 && absY < 3656) 
		
		  ){
			return true;
		}
		return false;
	}	
	public boolean inLvl1to18() {
		if((absX > 2940 && absX < 3135 && absY > 3518 && absY < 3645) 
		
		  ){
			return true;
		}
		return false;
	}
	public boolean inLvl19to49() {
		if((absX > 2945 && absX < 3367 && absY > 3657 && absY < 3903)){
			return true;
		}
		return false;
	}	
	public boolean inHotSpot1() {
		//(absX > 3060 && absX < 3128 && absY > 3519 && absY < 3552) ||
		if(absX > 2985 && absX < 3014 && absY < 3646 && absY > 3590)
		  {
			return true;
		}
		return false;
	}
	
	/*
	 * inWild() b4 making everywhere except cities wild
	 * 
	   (absX >= 2460 && absX <= 2557 && absY >= 3265 && absY <= 3335) ||
	   (absX > 2941 && absX < 3392 && absY > 3519 && absY < 3967) ||
		  (absX > 3009 && absX < 3060 && absY > 10303 && absY < 10356)||
		  (absX > 3078 && absX < 3134 && absY > 9918 && absY < 9999) ||
		  (absX > 3300 && absX < 3326 && absY > 9826 && absY < 9856) ||
		  (absX > 3060 && absX < 3075 && absY > 10250 && absY < 10265) || 
		  (absX <= 2434 && absY >= 3069 && absX >= 2365 && absY <= 3137) ||
		  (absX <= 2500 && absY >= 9400 && absX >= 2200 && absY <= 9600) ||
		  (absX >= 2370 && absX <= 2425 && absY >= 5127 && absY <= 5167) ||
		  (absX > 2595 && absX < 2609 && absY > 4769 && absY < 4779) || 
  		  (varrockCity())
	 
	 */
	
	public boolean inWild() {
		if 	 ((absX >= 2460 && absX <= 2557 && absY >= 3265 && absY <= 3335) ||
		     (absX > 2941 && absX < 3392 && absY > 3519 && absY < 3967) ||
			  (absX > 3009 && absX < 3060 && absY > 10303 && absY < 10356)||
			  (absX > 3078 && absX < 3134 && absY > 9918 && absY < 9999) ||
			  (absX > 3300 && absX < 3326 && absY > 9826 && absY < 9856) ||
			  (absX > 3060 && absX < 3075 && absY > 10250 && absY < 10265) || 
			  (absX <= 2434 && absY >= 3069 && absX >= 2365 && absY <= 3137) ||
			  (absX <= 2500 && absY >= 9400 && absX >= 2200 && absY <= 9600) ||
			  (absX >= 2370 && absX <= 2425 && absY >= 5127 && absY <= 5167) ||
			  (absX > 2595 && absX < 2609 && absY > 4769 && absY < 4779)) {
		return true;
		} else {
			return false;
		}
	}
	public boolean inFFA() {
	if ((absX > 3300 && absX < 3326 && absY > 9826 && absY < 9856)) {
	return true;
	}
	return false;
	}
	public boolean inWild2() {
		if((absX > 2941 && absX < 3392 && absY > 3512 && absY < 3967) ||
		  (absX > 3009 && absX < 3060 && absY > 10303 && absY < 10356) ||
		  (absX > 3078 && absX < 3134 && absY > 9918 && absY < 9999) ||
		  (absX > 1986 && absX < 2004 && absY > 4480 && absY < 4495)
		  
		  ){
			return true;
		}
		return false;
	}
	public boolean arenas() {
		if(absX > 3331 && absX < 3391 && absY > 3242 && absY < 3260) {	
			return true;
		}
		return false;
	}
	
	public boolean inDuelArena() {
		if((absX > 3322 && absX < 3394 && absY > 3195 && absY < 3291) ||
		(absX > 3311 && absX < 3323 && absY > 3223 && absY < 3248)) {
			return true;
		}
		return false;
	}
	public boolean inDi() {
		if((absX > 2300 && absX < 2370 && absY > 9750 && absY < 9925)){
			return true;
		}
		return false;
	}
	public boolean inDray() {
		if((absX > 3070 && absX < 3111 && absY > 3234 && absY < 3286 )){
			return true;
		}
		return false;
	}	
	public boolean inYan() {
		if((absX > 2538 && absX < 2621 && absY > 3073 && absY < 3113 )){
			return true;
		}
		return false;
	}	
	public boolean inAlk() {
		if((absX > 3267 && absX < 3332 && absY > 3147 && absY < 3321 )){
			return true;
		}
		return false;
	}		
	public boolean inPorts() {
		if((absX > 2997 && absX < 3064 && absY > 3200 && absY < 3300 )){
			return true;
		}
		return false;
	}	
	public boolean inTradeArea() {
		if (absX > 2980 && absX < 3025 && absY > 3366 && absY < 3394) {
			return true;
		}
		return false;
	}
	public boolean inVarrock() {
		if((absX > 3206 && absX < 3225 && absY > 3419 && absY < 3437)
		){
			return true;
		}
		return false;
	}
	public int distanceToPoint(int pointX,int pointY) {
		return (int) Math.sqrt(Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2));
	}
	
	public int getMapRegionX() {
		return mapRegionX;
	}
	public int getMapRegionY() {
		return mapRegionY;
	}
	public int getX() {
		return absX;
	}
	
	public int getY() {
		return absY;
	}
	/** 
	*Face Update
	**/
	public void faceTo(int playerId) {
		face = 32768 + playerId;
		faceUpdateRequired = true;
		updateRequired = true;
	}
	protected boolean faceUpdateRequired = false;
    public int face = -1;
	public int focusPointX = -1, focusPointY = -1;
	
	public void faceUpdate(int index) {
		face = index;
		faceUpdateRequired = true;
		updateRequired = true;
    }

	public void appendFaceUpdate(Stream str) {
		str.writeWordBigEndian(face);
	}
	
	public void turnPlayerTo(int pointX, int pointY){
		focusPointX = 2*pointX+1;
		focusPointY = 2*pointY+1;
		updateRequired = true;
    }
	

	/**
	*Graphics
	**/
	
	public int mask100var1 = 0;
    public int mask100var2 = 0;

	protected boolean mask100update = false;
	
	public void appendMask100Update(Stream str) {
		str.writeWordBigEndian(mask100var1);
        str.writeDWord(mask100var2);
    }
	/*Simple Calling of the method
	--Usage: Enemy.delayedGfx(A1,A2,A3);

	A1 = the ID of the graphics you wanted
	A2 = the Delay you want before the graphics is created
	A3 = The Height of the graphics
	--0 is floor
	--100 is middle
	--200 is your head	*/
    public void delayedGfx(int gfx, int delay , int height) {
		mask100var1 = gfx;
		mask100var2 = delay+(65536*height);
		mask100update = true;
		gfxUpdateRequired = true;
    }	
	
	public void gfx100(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 6553600;
		mask100update = true;
		gfxUpdateRequired = true;
	}
	public void gfx0(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 65536;
		mask100update = true;
		gfxUpdateRequired = true;
	}
	public void gfx(int gfx) { 
		mask100var1 = gfx;
		mask100var2 = 6553600;
		mask100update = true;
		gfxUpdateRequired = true;
	}	
	public void gfx50(int gfx) { 
		mask100var1 = gfx;
		mask100var2 = 3276800;
		mask100update = true;
		gfxUpdateRequired = true;
	}
	
	/* 
	 * inMulti b4 changes
	 * 
	 * (absX >= 3029 && absX <= 3374 && absY >= 3759 && absY <= 3903) || 
		   (absX >= 2250 && absX <= 2280 && absY >= 4670 && absY <= 4720) ||
		   (absX >= 3198 && absX <= 3380 && absY >= 3904 && absY <= 3970) ||
		   (absX >= 3191 && absX <= 3326 && absY >= 3510 && absY <= 3759) ||
		   (absX >= 2987 && absX <= 3006 && absY >= 3912 && absY <= 3937) ||
		   (absX >= 2245 && absX <= 2295 && absY >= 4675 && absY <= 4720) ||
		  // (absX >= 2450 && absX <= 3520 && absY >= 9450 && absY <= 9550) ||
		   (absX >= 3006 && absX <= 3071 && absY >= 3602 && absY <= 3710) ||
		   (absX >= 3134 && absX <= 3192 && absY >= 3519 && absY <= 3646) ||
		   (absX >= 2308 && absX <= 2328 && absY >= 9890 && absY <= 9905) ||
		   (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619) ||
		   (absX >= 2368 && absX <= 2431 && absY >= 3072 && absY <= 3135) ||
		   (absX >= 2371 && absX <= 2424 && absY >= 5125 && absY <= 5169) ||
		   (absX >= 2600 && absX <= 2550 && absY >= 2690 && absY <= 2620) ||
		   (absX >= 2880 && absX <= 2930 && absY >= 4430 && absY <= 4470) ||
		   (absX >= 2365 && absX <= 2450 && absY >= 5050 && absY <= 5120) || 
		   (absX >= 2800 && absX <= 2950 && absY >= 5245 && absY <= 5385) ||
		   (absX >= 2948 && absX <= 2957 && absY >= 3817 && absY <= 3824) ||
		   (absX > 1986 && absX < 2004 && absY > 4480 && absY < 4495) || 
		   (absX <= 2500 && absY >= 9400 && absX >= 2200 && absY <= 9600) || (absX >= 2353 && absX <= 2362 && absY >= 9908 && absY <= 9917) ||
		   (absX >= 3290 && absX <= 3329  && absY >= 3055 && absY<= 3095) || (absX >= 2316 && absX <= 2330  && absY >= 9904 && absY<= 9918)
		   || (absX >= 3026 && absX <= 3017 && absY >= 9584 && absY <= 9575) ||
		   (absX >= 3015 && absX <= 3031 && absY >= 9594 && absY <= 9581 ) ||
		   (absX >= 2992 && absX <= 3001 && absY >= 9550 && absY <= 9543) ||
		     (absX >= 3046 && absX <= 3059 && absY >= 9592 && absY <= 9579) ||
			 (Server.FFAMULTI && inFFA())
	*/
	public boolean inMulti() {
		if( inWild() ) {
			return true;
		}
		return false;
	}
	public boolean inGWD() {
		if (absX >= 2800 && absX <= 2950 && absY >= 5245 && absY <= 5385) {
			return true;
		}
		return false;
	}
	public boolean inCold(){
		if ((absX >= 2965) && (absX <= 3012) && (absY >=3586) && (absY <=3644) || 
		    (absX >= 3232) && (absX <= 3351) && (absY >=3853) && (absY <=3903)) {
			return true;
		}
		return false;

	}
	
	public boolean inHot(){
		if((absX >= 3061) && (absX <= 3149) && (absY >= 3904) && (absY <= 3956) || 
		   (absX >= 3123) && (absX <= 3150) && (absY >= 3624) && (absY <= 3693) ||
		   (absX >= 2947) && (absX <= 3025) && (absY >= 3816) && (absY <= 3862) ||
		   (absX >= 3152) && (absX <= 3162) && (absY >= 3921) && (absY <= 3957)) {
			return true;
		}
		return false;
	}
	
	public boolean port13() {
	if((absX >= 3153 && absX <= 3158 && absY >= 3617 && absY <= 3622)){
		return true;
		}
		return false;
	}	
	public boolean port20() {
	if((absX >= 3224 && absX <= 3229 && absY >= 3664 && absY <= 3669)){
		return true;
		}
		return false;
	}
	public boolean port34() {
	if((absX >= 3103 && absX <= 3108 && absY >= 3791 && absY <= 3796)){
		return true;
		}
		return false;
	}	
	public boolean port44() {
	if((absX >= 2977 && absX <= 2982 && absY >= 3863 && absY <= 3868)){
		return true;
		}
		return false;
	}
	public boolean port50() {
	if((absX >= 3304 && absX <= 3309 && absY >= 3913 && absY <= 3918)){
		return true;
		}
		return false;
	}
	public boolean fightCave(){
		if((absX >= 2365 && absX <= 2450 && absY >= 5050 && absY <= 5120)){
		return true;
		}
		return false;
	}		
	public boolean portals() {
		if((absX >= 3153 && absX <= 3158 && absY >= 3617 && absY <= 3622) ||
		   (absX >= 3224 && absX <= 3229 && absY >= 3664 && absY <= 3669) ||
		   (absX >= 3103 && absX <= 3108 && absY >= 3791 && absY <= 3796) ||
		   (absX >= 2977 && absX <= 2982 && absY >= 3863 && absY <= 3868) ||
		   (absX >= 3304 && absX <= 3309 && absY >= 3913 && absY <= 3918))
		   {
			return true;
		}
		return false;
	}	
	public Player playerList[] = new Player[Constants.MAXIMUM_PLAYERS];
	public int playerListSize = 0;
	// bit at position playerId is set to 1 incase player is currently in
	// playerList
	public byte playerInListBitmap[] = new byte[(Constants.MAXIMUM_PLAYERS + 7) >> 3];

	
    public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
      /*  for (int i = 0; i <= distance; i++) {
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
        return false;*/
		if (((int) Math.sqrt(Math.pow(objectX - playerX, 2) + Math.pow(objectY - playerY, 2))) <= distance) {
		return true;
		}
		return false;
    }	
	
	public boolean withinDistance(int j, int k, int l, int i1, int j1) {
      /*  for(int k1 = 0; k1 <= j1; k1++)
        {
            for(int l1 = 0; l1 <= j1; l1++)
            {
                if(j + k1 == l && (k + l1 == i1 || k - l1 == i1 || k == i1))
                    return true;
                if(j - k1 == l && (k + l1 == i1 || k - l1 == i1 || k == i1))
                    return true;
                if(j == l && (k + l1 == i1 || k - l1 == i1 || k == i1))
                    return true;
            }

        }
        return false;*/
		
		if (((int) Math.sqrt(Math.pow(j - l, 2) + Math.pow(k - i1, 2))) <= j1) {
			return true;
		}
		return false;
    }

	public boolean withinDistance(Player otherPlr) {
		if (getHeightLevel() != otherPlr.getHeightLevel())
			return false;
		int deltaX = otherPlr.getAbsX() - getAbsX(), deltaY = otherPlr.getAbsY() - getAbsY();
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}
	
	public boolean withinDistance(int absX, int absY, int heightLevel) {
		if (this.getHeightLevel() != heightLevel)
			return false;
		int deltaX = this.getAbsX() - absX, deltaY = this.getAbsY() - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
		// if (((int) Math.sqrt(Math.pow(this.getAbsX() - absX, 2) + Math.pow(this.getAbsY() - absY, 2))) <= 16) {
		// return true;
		// }
		// return false;
	}

	public boolean withinInteractionDistance(int x, int y, int z) {
		if (getHeightLevel() != z)
			return false;
		int deltaX = x - getAbsX(), deltaY = y - getAbsY();
		return deltaX <= 2 && deltaX >= -2 && deltaY <= 2 && deltaY >= -2;
		// if (((int) Math.sqrt(Math.pow(x - getAbsX(), 2) + Math.pow(y - getAbsY(), 2))) <= 2) {
		// return true;
		// }
		// return false;
		
	}
	public boolean withinInteractionDistanceWC(int x, int y, int z) {
		if (getHeightLevel() != z)
			return false;
		int deltaX = x - getAbsX(), deltaY = y - getAbsY();
		return deltaX <= 4 && deltaX >= -4 && deltaY <= 4 && deltaY >= -4;
		// if (((int) Math.sqrt(Math.pow(x - getAbsX(), 2) + Math.pow(y - getAbsY(), 2))) <= 4) {
		// return true;
		// }
		// return false;
		
	}	public boolean withinDistance(NPC npc) {
		if (getHeightLevel() != npc.getHeightLevel())
			return false;
		int deltaX = npc.getAbsX() - getAbsX(), deltaY = npc.getAbsY()
				- getAbsY();
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
		// if (((int) Math.sqrt(Math.pow(npc.getAbsX() - getAbsX(), 2) + Math.pow(npc.getAbsY() - getAbsY(), 2))) <= 16) {
		// return true;
		// }
		// return false;
	}

	public boolean withinInteractionDistance(Client otherClient) {
		return withinInteractionDistance(otherClient.getAbsX(), otherClient.getAbsY(), otherClient
				.getHeightLevel());
	}
	
	public boolean withinInteractionDistance(NPC otherClient) {
		return withinInteractionDistance(otherClient.getAbsX(), otherClient.getAbsY(), otherClient
				.getHeightLevel());
	}

	public int mapRegionX, mapRegionY; 
	public int currentX, currentY; 
	public boolean updateRequired = true; 

	public int walkingQueueX[] = new int[Constants.WALKING_QUEUE_SIZE],
			walkingQueueY[] = new int[Constants.WALKING_QUEUE_SIZE];
	public int wQueueReadPtr = 0; // points to slot for reading from queue
	public int wQueueWritePtr = 0; // points to (first free) slot for writing to
	public boolean isRunning = false;
	public boolean isRunning2 = false;
	public int teleportToX = -1, teleportToY = -1, teleportToZ = -1; 

	public void resetWalkingQueue() {
		wQueueReadPtr = wQueueWritePtr = 0;
		// properly initialize this to make the "travel back" algorithm work
		for (int i = 0; i < Constants.WALKING_QUEUE_SIZE; i++) {
			walkingQueueX[i] = currentX;
			walkingQueueY[i] = currentY;
		}
	}

	public void addToWalkingQueue(int x, int y) {
		int next = (wQueueWritePtr + 1) % Constants.WALKING_QUEUE_SIZE;
		if (next == wQueueWritePtr)
			return; // walking queue full, silently discard the data
		walkingQueueX[wQueueWritePtr] = x;
		walkingQueueY[wQueueWritePtr] = y;
		wQueueWritePtr = next;
	}

	public int getNextWalkingDirection() {
		if (wQueueReadPtr == wQueueWritePtr)
			return -1; // walking queue empty
		int dir;
		do {
	
			dir = Misc.direction(currentX, currentY,
					walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);
			if (dir == -1)
				wQueueReadPtr = (wQueueReadPtr + 1)
						% Constants.WALKING_QUEUE_SIZE;
			else if ((dir & 1) != 0) {
				println_debug("Invalid waypoint in walking queue!");
				resetWalkingQueue();
				return -1;
			}
		} while (dir == -1 && wQueueReadPtr != wQueueWritePtr);
		if (dir == -1)
			return -1;
		dir >>= 1;
		if (Config.CastleWars && Server.getCastleWars().isInCw((Client) this)) {
			for (Barricade x : Server.getCastleWars().barricades) {
				if (getHeightLevel() != x.getHeightLevel())
					continue;
				if (getAbsX() + Misc.directionDeltaX[dir] == x.getAbsX()
					&& getAbsY() + Misc.directionDeltaY[dir] == x.getAbsY()) {
					resetWalkingQueue();
					return -1;
				}
			}
		}
		int oldX = absX, oldY = absY;
		currentX += Misc.directionDeltaX[dir];
		currentY += Misc.directionDeltaY[dir];
		setAbsX(getAbsX() + Misc.directionDeltaX[dir]);
		setAbsY(getAbsY() + Misc.directionDeltaY[dir]);
	

		return dir;
	}
		
	public boolean didTeleport = false; // set to true if char did teleport in
										// this cycle
	public boolean didWalk = false; // set to true if char did walk in this
									// cycle
	public boolean mapRegionDidChange = false;
	public int dir1 = -1, dir2 = -1; // direction char is going in this cycle
	public boolean createItems = false;
	public int poimiX = 0, poimiY = 0;

	protected boolean zoneRequired = true;

	public void getNextPlayerMovement() {
		mapRegionDidChange = false;
		boolean heightLevelDidChange = false;
		didTeleport = false;
		dir1 = dir2 = -1;

		if (teleportToZ != -1) {
			setHeightLevel(teleportToZ);
			teleportToZ = -1;
			if (teleportToX == -1) {
				teleportToX = getAbsX();
			}
			if (teleportToY == -1) {
				teleportToY = getAbsY();
			}
			heightLevelDidChange = true;
		}
		if (teleportToX != -1 && teleportToY != -1) {
			mapRegionDidChange = true;
			if (mapRegionX != -1 && mapRegionY != -1) {
				// check, whether destination is within current map region
				int relX = teleportToX - mapRegionX * 8, relY = teleportToY
						- mapRegionY * 8;
				if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8
						&& relY < 11 * 8)
					mapRegionDidChange = false;
			}
			if (mapRegionDidChange) {
				println_debug("Region: " + mapRegionX+" "+mapRegionY);
				// after map region change the relative coordinates range
				// between 48 - 55

				mapRegionX = (teleportToX >> 3) - 6;
				mapRegionY = (teleportToY >> 3) - 6;
				
				// Jonas said this caused a problem
				// playerListSize = 0; // completely rebuild playerList after
									// teleport AND map region change
			}

			currentX = teleportToX - 8 * mapRegionX;
			currentY = teleportToY - 8 * mapRegionY;
			setAbsX(teleportToX);
			setAbsY(teleportToY);
			resetWalkingQueue();

			teleportToX = teleportToY = -1;
			didTeleport = true;
			//checkVisiblePlayers();

		} else {
			dir1 = getNextWalkingDirection();
			if (dir1 == -1)
				return; // standing

			if (isRunning)
				dir2 = getNextWalkingDirection();

			// check, if we're required to change the map region
			int deltaX = 0, deltaY = 0;
			if (currentX < 2 * 8) {
				deltaX = 4 * 8;
				mapRegionX -= 4;
				mapRegionDidChange = true;
			} else if (currentX >= 11 * 8) {
				deltaX = -4 * 8;
				mapRegionX += 4;
				mapRegionDidChange = true;
			}
			if (currentY < 2 * 8) {
				deltaY = 4 * 8;
				mapRegionY -= 4;		
				mapRegionDidChange = true;
			} else if (currentY >= 11 * 8) {
				deltaY = -4 * 8;
				mapRegionY += 4;
				mapRegionDidChange = true;
			}
			if (dir1 != -1 || dir2 != -1) {
				//checkVisiblePlayers();
			}
			if (mapRegionDidChange) {
		
				// have to adjust all relative coordinates
				println_debug("Region: " + mapRegionX+" "+mapRegionY);
				currentX += deltaX;
				currentY += deltaY;
				for (int i = 0; i < Constants.WALKING_QUEUE_SIZE; i++) {
					walkingQueueX[i] += deltaX;
					walkingQueueY[i] += deltaY;
				}
			}

		}

		if (mapRegionDidChange || heightLevelDidChange) {
			zoneRequired = true;
			//println_debug("Region: " + mapRegionX+" "+mapRegionY);
		}
	}

	// handles anything related to character position, i.e. walking,running and
	// teleportation
	// applies only to the char and the client which is playing it
	/*public void updateThisPlayerMovement(Stream str) {
		if (mapRegionDidChange) {
			str.createFrame(73);
			str.writeWordA(mapRegionX + 6); // for some reason the client
											// substracts 6 from those values
			str.writeWord(mapRegionY + 6);
		}

		if (didTeleport) {
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);
			str.writeBits(2, 3); // updateType
			str.writeBits(2, getHeightLevel());
			str.writeBits(1, 1); // set to true, if discarding (clientside)
									// walking queue
			str.writeBits(1, (updateRequired) ? 1 : 0);
			str.writeBits(7, currentY);
			str.writeBits(7, currentX);
			return;
		}

		if (dir1 == -1) {
			// don't have to update the character position, because we're just
			// standing
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			if (updateRequired) {
				// tell client there's an update block appended at the end
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else
				str.writeBits(1, 0);
		} else {
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);

			if (dir2 == -1) {
				// send "walking packet"
				str.writeBits(2, 1); // updateType
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				if (updateRequired)
					str.writeBits(1, 1); // tell client there's an update block
											// appended at the end
				else
					str.writeBits(1, 0);
			} else {
				// send "running packet"
				str.writeBits(2, 2); // updateType
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
				if (updateRequired)
					str.writeBits(1, 1); // tell client there's an update block
											// appended at the end
				else
					str.writeBits(1, 0);
			}
		}

	}

	// handles anything related to character position basically walking, running
	// and standing
	// applies to only to "non-thisPlayer" characters
	public void updatePlayerMovement(Stream str) {
		if (dir1 == -1) {
			// don't have to update the character position, because the char is
			// just standing
			if (updateRequired || chatTextUpdateRequired) {
				// tell client there's an update block appended at the end
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else
				str.writeBits(1, 0);
		} else if (dir2 == -1) {
			// send "walking packet"
			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str
					.writeBits(1,
							(updateRequired || chatTextUpdateRequired) ? 1 : 0);
		} else {
			// send "running packet"
			str.writeBits(1, 1);
			str.writeBits(2, 2);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
			str
					.writeBits(1,
							(updateRequired || chatTextUpdateRequired) ? 1 : 0);
		}
	}*/
	public ArrayList<Integer> addPlayerList = new ArrayList<Integer>();
	public int addPlayerSize = 0;
	
	/**
	 * Checks for nearby visible players that aren't being updated.
	 */
	private void checkVisiblePlayers() {
		for (int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
			if (Server.getPlayerManager().getPlayers()[i] == null || !Server.getPlayerManager().getPlayers()[i].isActive || Server.getPlayerManager().getPlayers()[i].equals(this))
				continue; // No need to update this player.

			// Check if they're already in the local list.
			int id = Server.getPlayerManager().getPlayers()[i].playerId;
			if ((playerInListBitmap[id >> 3] & (1 << (id & 7))) != 0)
				continue;

			// Are they already queued to be added?
			if (newPlayers.contains(Server.getPlayerManager().getPlayers()[i]))
				continue;

			// Perform the distance check.
			if (!withinDistance(Server.getPlayerManager().getPlayers()[i]))
				continue;

			// Add each to the others list.
			newPlayers.add(Server.getPlayerManager().getPlayers()[i]);
			Server.getPlayerManager().getPlayers()[i].newPlayers.add(this);
		}
	}
	
	public void updateThisPlayerMovement(Stream str) {
		if (dir1 != -1 || dir2 != -1){
			//checkVisiblePlayers(); // they/you could come in or out of their/your area
		}
		if(mapRegionDidChange) {
			str.createFrame(73);
			str.writeWordA(mapRegionX+6);	
			str.writeWord(mapRegionY+6);
		}

		if(didTeleport) {
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);
			str.writeBits(2, 3);			
			str.writeBits(2, getHeightLevel());
			str.writeBits(1, 1);			
			str.writeBits(1, (updateRequired) ? 1 : 0);
			str.writeBits(7, currentY);
			str.writeBits(7, currentX);
			return ;
		}
		

		if(dir1 == -1) {
			// don't have to update the character position, because we're just standing
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			isMoving = false;
			if(updateRequired) {
				// tell client there's an update block appended at the end
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
			if (DirectionCount < 50) {
				DirectionCount++;
			}
		} else {
			DirectionCount = 0;
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);

			if(dir2 == -1) {
				isMoving = true;
				str.writeBits(2, 1);		
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				if(updateRequired) str.writeBits(1, 1);		
				else str.writeBits(1, 0);
			}
			else {
				isMoving = true;
				str.writeBits(2, 2);		
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
				if(updateRequired) str.writeBits(1, 1);		
				else str.writeBits(1, 0);
			}
		}

	}

	
	public void updatePlayerMovement(Stream str) {
		if(dir1 == -1) {
			if(updateRequired || chatTextUpdateRequired) {
				
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			}
			else str.writeBits(1, 0);
		}
		else if(dir2 == -1) {
			
			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(1, (updateRequired || chatTextUpdateRequired) ? 1: 0);
		}
		else {
			
			str.writeBits(1, 1);
			str.writeBits(2, 2);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
			str.writeBits(1, (updateRequired || chatTextUpdateRequired) ? 1: 0);
		}
	}
	public void stopMovement() {
		if(respawnTimer > 0 || isDead) {
			return;
		}
        if(teleportToX <= 0 && teleportToY <= 0) {
            teleportToX = absX;
            teleportToY = absY;
        }
		
        newWalkCmdSteps = 0;
        newWalkCmdX[0] = newWalkCmdY[0] = travelBackX[0] = travelBackY[0] = 0;
        getNextPlayerMovement();
    }
	// a bitmap of players that we want to keep track of whether char appearance
	// has changed so
	// we know if we have to transmit those or can make use of the cached char
	// appearances in the client
	public byte cachedPropertiesBitmap[] = new byte[(Constants.MAXIMUM_PLAYERS + 7) >> 3];

	public void addNewPlayer(Player plr, Stream str, Stream updateBlock) {
		int id = plr.playerId;
		playerInListBitmap[id >> 3] |= 1 << (id & 7); // set the flag
		playerList[playerListSize++] = plr;

		str.writeBits(11, id); // client doesn't seem to like id=0

		// TODO: properly implement the character appearance handling
		// send this everytime for now and don't make use of the cached ones in
		// client
		str.writeBits(1, 1); // set to true, if player definitions follow below
		boolean savedFlag = plr.appearanceUpdateRequired;
		boolean savedUpdateRequired = plr.updateRequired;
		plr.appearanceUpdateRequired = true;
		plr.updateRequired = true;
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.appearanceUpdateRequired = savedFlag;
		plr.updateRequired = savedUpdateRequired;

		str.writeBits(1, 1); // set to true, if we want to discard the
								// (clientside) walking queue
		// no idea what this might be useful for yet
		int z = plr.getAbsY() - getAbsY();
		if (z < 0)
			z += 32;
		str.writeBits(5, z); // y coordinate relative to thisPlayer
		z = plr.getAbsX() - getAbsX();
		str.writeBits(5, z); // x coordinate relative to thisPlayer
	}

	// player appearance related stuff
	public boolean appearanceUpdateRequired = true; // set to true if the player
													// appearance wasn't
													// synchronized
	// with the clients yet or changed recently
	public int playerStatusMask = 0;
	public int playerSkullMask = 0;
	public boolean isMaskSupported = false;
	public int combatLevel = 3;
	protected static Stream playerProps;
	static {
		playerProps = new Stream(new byte[PlayerEquipmentConstants.PLAYER_PROPS_SIZE]);
	}
	public int playerStandIndex = 0x328;
	public int playerTurnIndex = 0x337;
	public int playerWalkIndex = 0x333;
	public int playerTurn180Index = 0x334;
	public int playerTurn90CWIndex = 0x335;
	public int playerTurn90CCWIndex = 0x336;
	public int playerRunIndex = 0x338;
	public int skulls = 0;
	public int hint = 0;
	public boolean playerIsNPC = false;
    public int playerNPCID = 555;//incase of error
	protected void appendPlayerAppearance(Stream str) {
		playerProps.currentOffset = 0;

		playerProps.writeByte(playerAppearance[0]); // player sex. 0=Male and 1=Female
		//playerProps.writeByte(skulls);
		//playerProps.writeByte(hint);
		playerProps.writeByte(headIcon);
		playerProps.writeByte(skullIcon);
		if(!playerIsNPC){  		
			if (playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] > 1)
				playerProps.writeWord(0x200 + playerEquipment[PlayerEquipmentConstants.PLAYER_HAT]);
			else
				playerProps.writeByte(0);

			if (playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE] > 1)
				playerProps.writeWord(0x200 + playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE]);
			else
				playerProps.writeByte(0);

			if (playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] > 1)
				playerProps.writeWord(0x200 + playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET]);
			else
				playerProps.writeByte(0);

			if (playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > 1)
				playerProps.writeWord(0x200 + playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]);
			else
				playerProps.writeByte(0);

			if (playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] > 1)
				playerProps.writeWord(0x200 + playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST]);
			else
				playerProps.writeWord(0x100 + playerAppearance[2]);

			if (playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] > 1)
				playerProps.writeWord(0x200 + playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD]);
			else
				playerProps.writeByte(0);

			if (!Item.isPlate(playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST]))
				playerProps.writeWord(0x100 + playerAppearance[3]);
			else
				playerProps.writeByte(0);

			if (playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] > 1)
				playerProps.writeWord(0x200 + playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS]);
			else
				playerProps.writeWord(0x100 + playerAppearance[5]);

			if (!Item.isFullHelm(playerEquipment[PlayerEquipmentConstants.PLAYER_HAT])
					&& !Item.isFullMask(playerEquipment[PlayerEquipmentConstants.PLAYER_HAT]))
				playerProps.writeWord(0x100 + playerAppearance[1]); // head
			else
				playerProps.writeByte(0);

			if (playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] > 1)
				playerProps.writeWord(0x200 + playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS]);
			else
				playerProps.writeWord(0x100 +playerAppearance[4]);

			if (playerEquipment[PlayerEquipmentConstants.PLAYER_FEET] > 1)
				playerProps.writeWord(0x200 + playerEquipment[PlayerEquipmentConstants.PLAYER_FEET]);
			else
				playerProps.writeWord(0x100 +playerAppearance[6]);
			if (!Item.isFullMask(playerEquipment[PlayerEquipmentConstants.PLAYER_HAT])
					&& (playerAppearance[0] != 1))
				playerProps.writeWord(0x100 + playerAppearance[7]); // Beard
			else 
				playerProps.writeByte(0);
		} else {
            playerProps.writeWord(-1);//Tells client that were being a npc
            playerProps.writeWord(playerNPCID);//send NpcID
        }
		if (playerStandIndex > 16000) { 
			playerStandIndex = 0x328;
		}
		if ( playerTurnIndex > 16000) {
			playerTurnIndex = 0x337;
		}	
	if (playerWalkIndex > 16000) {
	playerWalkIndex = 0x333;
	}
	if (playerTurn180Index > 16000){
		playerTurn180Index =	0x334;
	}
	if (playerTurn90CWIndex > 16000) {
		playerTurn90CWIndex	= 0x335;
	}
	if ( playerTurn90CCWIndex > 16000) {
playerTurn90CCWIndex	= 0x336;
}
	if (playerRunIndex  > 16000) {
	playerRunIndex= 0x338;
	}
		playerProps.writeByte(playerAppearance[8]);	
		playerProps.writeByte(playerAppearance[9]);	
		playerProps.writeByte(playerAppearance[10]);	
		playerProps.writeByte(playerAppearance[11]);	
		playerProps.writeByte(playerAppearance[12]);	
		playerProps.writeWord(playerStandIndex);		// standAnimIndex
		playerProps.writeWord(playerTurnIndex);		// standTurnAnimIndex
		playerProps.writeWord(playerWalkIndex);		// walkAnimIndex
		playerProps.writeWord(playerTurn180Index);		// turn180AnimIndex
		playerProps.writeWord(playerTurn90CWIndex);		// turn90CWAnimIndex
		playerProps.writeWord(playerTurn90CCWIndex);		// turn90CCWAnimIndex
		playerProps.writeWord(playerRunIndex);		// runAnimIndex	

		/*playerProps.writeByte(pHairC); // hair color
		playerProps.writeByte(pTorsoC); // torso color.
		playerProps.writeByte(pLegsC); // leg color
		playerProps.writeByte(pFeetC); // feet color
		playerProps.writeByte(pSkinC); // skin color (0-6)

		playerProps.writeWord(playerStandIndex);		// standAnimIndex
		playerProps.writeWord(playerTurnIndex);		// standTurnAnimIndex
		playerProps.writeWord(playerWalkIndex);		// walkAnimIndex
		playerProps.writeWord(playerTurn180Index);		// turn180AnimIndex
		playerProps.writeWord(playerTurn90CWIndex);		// turn90CWAnimIndex
		playerProps.writeWord(playerTurn90CCWIndex);		// turn90CCWAnimIndex
		playerProps.writeWord(playerRunIndex);		// runAnimIndex	*/


		playerProps.writeQWord(Misc.playerNameToInt64(playerName));

		//int combatLevel = 0;
		playerProps.putRS2String(getGuild());
		playerProps.putRS2String(getRank());
		playerProps.putRS2String(clanChat);
		playerProps.writeByte(getCombatLevel());		// combat level		
			if (Config.v13)
		{
		
		playerProps.writeWord(skill);
						boolean ChatAllowed = true;
						String legalChars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ .!@#$%^&*()[]-,'`<>=;?";
						 for ( int i = 0; i < title.length(); i++ )
							{	
							 if ( legalChars.indexOf( title.charAt( i ) ) < 0 )
							{
								ChatAllowed = false;
							}
						}
						if (!ChatAllowed) {
								title = "";
						}
		playerProps.putRS2String(title + " ");
		playerProps.putRS2String("false");
		}
		playerProps.writeWord(0);
	
		str.writeByteC(playerProps.currentOffset); // size of player appearance
													// block
		str.writeBytes(playerProps.buffer, playerProps.currentOffset, 0);
	}
		public String getGuild() {
    return guild;
}
public void setGuild(String value) {
    guild = value;
}
	public String getRank() {
		return rank;
	}	
public void setRank(String value) {
    rank = value;
}

		public int extraCb;
	public int getCombatLevel() {
		/*int attack = getLevelForXP(playerXP[0]);
		int defence = getLevelForXP(playerXP[1]);
		int strength = getLevelForXP(playerXP[2]);
		int hitpoints = getLevelForXP(playerXP[3]);
		int ranged = getLevelForXP(playerXP[4]);
		int prayer = getLevelForXP(playerXP[5]);
		int magic = getLevelForXP(playerXP[6]);

		int baseCombat = (defence * 100) + (hitpoints * 100);
		if ((prayer % 2) == 0) {
			baseCombat = (baseCombat + (prayer * 50)) / 400;
		} else {
			baseCombat = (baseCombat + ((prayer - 1) * 50)) / 400;
		}

		int meleeCombat = ((attack * 130) + (strength * 130)) / 400;

		int rangedCombat = ranged * 195;
		if ((ranged % 2) == 1) {
			rangedCombat -= 65;
		}
		rangedCombat /= 400;

		int magicCombat = magic * 195;
		if ((magic % 2) == 1) {
			magicCombat -= 65;
		}
		magicCombat /= 400;

		int classCombat;
		if (meleeCombat >= rangedCombat && meleeCombat >= magicCombat) {
			classCombat = meleeCombat;
		} else if (rangedCombat >= meleeCombat && rangedCombat >= magicCombat) {
			classCombat = rangedCombat;
		} else {
			classCombat = magicCombat;
		}

		int combatLevel = baseCombat + classCombat;
		// It doesn't calculate your combat level properly if you're at the max,
		// or minimum stats, so we just do this.
		if (combatLevel == 2 || attack == 99 && strength == 99 && defence == 99
				&& hitpoints == 99 && prayer >= 98 || attack == 99 && strength == 99 && defence == 99
				&& hitpoints == 99 && prayer >= 74 ||  attack == 99 && strength == 99 && defence == 99
				&& hitpoints == 99 && prayer >= 82 || attack == 99 && strength == 99 && defence == 99
				&& hitpoints == 99 && prayer >= 90) {
			combatLevel++;
		}

			
		return combatLevel; */
		
		
		int j = getLevelForXP(playerXP[0]);
        int k = getLevelForXP(playerXP[1]);
        int l = getLevelForXP(playerXP[2]);
        int i1 = getLevelForXP(playerXP[3]);
        int j1 = getLevelForXP(playerXP[5]);
        int k1 = getLevelForXP(playerXP[4]);
        int l1 = getLevelForXP(playerXP[6]);
        combatLevel = (int)(((double)(k + i1) + Math.floor(j1 / 2)) * 0.25D) + 1;
        double d = (double)(j + l) * 0.32500000000000001D;
        double d1 = Math.floor((double)k1 * 1.5D) * 0.32500000000000001D;
        double d2 = Math.floor((double)l1 * 1.5D) * 0.32500000000000001D;
        if(d >= d1 && d >= d2 && extraCb == 0)
        {
            combatLevel += d;
        } else
        if(d1 >= d && d1 >= d2 && extraCb == 0)
        {
            combatLevel += d1;
        } else
        if(d2 >= d && d2 >= d1 && extraCb == 0)
        {
            combatLevel += d2;
        } else
        if(extraCb > 0)
        {
            combatLevel = extraCb;
        }
		//if (playerName.equalsIgnoreCase("James")) { combatLevel=254; return 254; }
		//if (true) {return 126;}
		return combatLevel;
    }	
	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl < 100; lvl++) {
			points += Math.floor((double) lvl + 300.0
					* Math.pow(2.0, (double) lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public boolean chatTextUpdateRequired = false;
	public byte chatText[] = new byte[4096];
	public byte chatTextSize = 0;
	public int chatTextEffects = 0;
	public int chatTextColor = 0;
	public boolean playerAppearanceSet = false;

	public boolean animationUpdateRequired = false;
	public int animationRequest = -1, animationWaitCycles;

protected void appendPlayerChatText(Stream str) {
        str.writeWordBigEndian(((chatTextColor & 0xFF) << 8)
                + (chatTextEffects & 0xFF));
        str.writeByte(playerRights);
        str.writeByteC(chatTextSize);
        str.writeBytes_reverse(chatText, chatTextSize, 0);
    }

   /*private void appendAnimationRequest(Stream str) {
        str.writeWordBigEndian(animationRequest);
        str.writeByteC(animationWaitCycles);
    }*/
	public void appendAnimationRequest(Stream str) {
		if (animationRequest > 16000) {animationRequest = 451;} //crash f
		str.writeWordBigEndian((animationRequest==-1) ? 65535 : animationRequest);
		str.writeByteC(animationWaitCycles);
	}
   // public int focusPointX = -1, focusPointY = -1;
    private void appendSetFocusDestination(Stream str) {
        str.writeWordBigEndianA(focusPointX);
        str.writeWordBigEndian(focusPointY);
        //faceFocusRequired = true;
        updateRequired = true;
    }
    


    public boolean hitUpdateRequired3, hitUpdateRequired2, hitUpdateRequired = false, poisonHit = false;
    public int hitDiff, hitType, hitDiff2, hitDiff3;
	public int NewHP = getLevelForXP(playerXP[3]);
    public int poisondamg = 0;
    public int poisondamage = 0;
    public int poisontimer = 0;	
	
	private void appendHitUpdate(Stream str) {
		str.writeByte(hitDiff); // What the perseon got 'hit' for
		
			//if (hitDiff > 0) {
			//	str.writeByteA(1); // 0: red hitting - 1: blue hitting
            if (hitDiff > 0 && newhptype == false && poisondmg == false) {
                str.writeByteA(1); // 0: red hitting - 1: blue hitting
            } else if (hitDiff > 0 && poisondmg == true) {
                str.writeByteA(2); // 0: red hitting - 1: blue hitting 2: poison 3: orange
            } else if (hitDiff > 0 && newhptype == true) {
                str.writeByteA(hptype); // 0: red hitting - 1: blue hitting
            } else {
                str.writeByteA(0); // 0: red hitting - 1: blue hitting
            }
		//str.writeByteA(hitType); // 0: red hitting - 1: blue hitting - 2: green
									// hitting - 3,4: orange hitting.
			NewHP = (playerLevel[3]);
			if (NewHP <= 0) {
				poisontimer = 0;
				poisondamg = 0;
				poisondamage = 0;			
				NewHP = 0;
				isDead = true;
				playerIndex = 0;
				oldPlayerIndex = 0;
				usingMagic = false;
				faceUpdate(0);
			}
		str.writeByteC(NewHP); // Their current hp, for HP bar
		str.writeByte(((Client) this).getActionAssistant().getLevelForXP(
				playerXP[3])); // Their max hp, for HP bar
				poisondmg = false;
	}
	protected void appendHitUpdate2(Stream str) {
		try {
			str.writeByte(hitDiff2); // What the perseon got 'hit' for
			if (!poisonHit) {
				if (hitDiff2 > 0) {
					str.writeByteS(1);
				} else {
					str.writeByteS(0);
				}
			} else {
				str.writeByteS(2);
			}			
			
		/*	//if (hitDiff2 > 0) {
		//		str.writeByteS(1); // 0: red hitting - 1: blue hitting
             if (hitDiff2 > 0 && newhptype == false && poisondmg == false) {
                str.writeByteS(1); // 0: red hitting - 1: blue hitting
            } else if (hitDiff2 > 0 && poisondmg) {
                str.writeByteS(2); // 0: red hitting - 1: blue hitting 2: poison 3: orange
            } else if (hitDiff2 > 0 && newhptype == true) {
                str.writeByteS(hptype); // 0: red hitting - 1: blue hitting
            } else {
                str.writeByteS(0); // 0: red hitting - 1: blue hitting
            }*/
			if (playerLevel[3] <= 0) {
				poisontimer = 0;
				poisondamg = 0;
				poisondamage = 0;			
				playerLevel[3] = 0;
				isDead = true;
				playerIndex = 0;
				oldPlayerIndex = 0;
				usingMagic = false;
				faceUpdate(0);	
			}
			str.writeByte(playerLevel[3]); // Their current hp, for HP bar
			str.writeByteC(getLevelForXP(playerXP[3])); // Their max hp, for HP bar
		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
	protected void appendHitUpdate3(Stream str) {
		try {
			str.writeByte(hitDiff3); // What the perseon got 'hit' for
			if (hitDiff3 > 0) {
				str.writeByteS(2); // 0: red hitting - 1: blue hitting
			}
			if (playerLevel[3] <= 0) {
				poisontimer = 0;
				poisondamg = 0;
				poisondamage = 0;			
				playerLevel[3] = 0;
				isDead = true;	
				playerIndex = 0;
				oldPlayerIndex = 0;
				usingMagic = false;
				faceUpdate(0);
			}
			str.writeByte(playerLevel[3]); // Their current hp, for HP bar
			str.writeByteC(getLevelForXP(playerXP[3])); // Their max hp, for HP bar
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	
    public boolean gfxUpdateRequired = false;    
    public int gfxId, gfxDelay; 
	
    private void appendGfxUpdate(Stream str) {
        str.writeWordBigEndian(gfxId);
        str.writeDWord(gfxDelay);
    }
    
    public boolean forceChatUpdateRequired = false;
    public String chatMessage;
    private void appendForceText(Stream str) {
        str.writeString(chatMessage);
    }
    
    public boolean faceEntityRequired = false;
    public int entityId;
    public boolean isPlayer = false;
    private void appendFaceEntity(Stream str) {
        str.writeWordBigEndian(entityId);
    }
    
    public boolean secondHitRequired = false;    
    private void appendSecondHitUpdate(Stream str) {
        str.writeByte(50); 
        str.writeByteS(1);
        str.writeByteC(playerLevel[3]);
        str.writeByte(((Client) this).getActionAssistant().getLevelForXP(playerXP[3])); 
    }
    
    public boolean faceFocusRequired = false;
	public boolean forcedChatUpdateRequired;
	
		public void forcedChat(String text) {
		forcedText = text;
		forcedChatUpdateRequired = true;
		updateRequired = true;
		appearanceUpdateRequired = true;
	}
	public String forcedText = "null";
	public void appendForcedChat(Stream str) {
		str.writeString(forcedText);
    }
	public void appendPlayerUpdateBlock(Stream str) {
		if (!updateRequired && !chatTextUpdateRequired)
		return; // nothing required
		int updateMask = 0;
		if(mask100update)
			updateMask |= 0x100;
		if (animationRequest != -1)
			updateMask |= 8;
		if(forcedChatUpdateRequired)
			updateMask |= 4;
		if (chatTextUpdateRequired)
			updateMask |= 0x80;
		if (faceUpdateRequired)
			updateMask |= 1;
		if (appearanceUpdateRequired)
			updateMask |= 0x10;
		if (focusPointX != -1 && focusPointY != -1)
			updateMask |= 2;
		if (hitUpdateRequired)
			updateMask |= 0x20;
		if(hitUpdateRequired2) {
			updateMask |= 0x200;
		}
		if (updateMask >= 0x100) {
			updateMask |= 0x40; 
		str.writeByte(updateMask & 0xFF);
		str.writeByte(updateMask >> 8);
		} else
		str.writeByte(updateMask);

		if(mask100update)
			appendMask100Update(str);
		if(animationRequest != -1) {
			appendAnimationRequest(str);	
		}
		if(forcedChatUpdateRequired)
			appendForcedChat(str);
		if (chatTextUpdateRequired)
			appendPlayerChatText(str);
		if (faceUpdateRequired)
			appendFaceUpdate(str);
		if (appearanceUpdateRequired)
			appendPlayerAppearance(str);
		if (focusPointX != -1 && focusPointY != -1)
			appendSetFocusDestination(str);
		if (hitUpdateRequired)
			appendHitUpdate(str);
		if(hitUpdateRequired2) {
			appendHitUpdate2(str); 
		}
	} 

	public void clearUpdateFlags(){
		updateRequired = false;
		chatTextUpdateRequired = false;
		appearanceUpdateRequired = false;
		animationUpdateRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		hitUpdateRequired3 = false;
		forcedChatUpdateRequired = false;
		mask100update = false;
		animationRequest = -1;
		focusPointX = -1;
		focusPointY = -1;
		faceUpdateRequired = false;
        face = 65535;
		poisonHit = false;
	}

	public int newWalkCmdX[] = new int[Constants.WALKING_QUEUE_SIZE];
	public int newWalkCmdY[] = new int[Constants.WALKING_QUEUE_SIZE];
	public int newWalkCmdSteps = 0;
	public boolean newWalkCmdIsRunning = false;
	protected int travelBackX[] = new int[Constants.WALKING_QUEUE_SIZE];
	protected int travelBackY[] = new int[Constants.WALKING_QUEUE_SIZE];
	protected int numTravelBackSteps = 0;

	public void preProcessing() {
		newWalkCmdSteps = 0;
	}
	public boolean packetLimitIncreased = false;
	public final int MAX_PACKETS_ALLOWED = 16;
	public int packetsHandled = 0, packetsAllowed = 24;
	
	// is being called regularily every 500ms - do any automatic player actions
	// herein
	public abstract void process();

	public void postProcessing() {
		packetsHandled = 0;
		packetLimitIncreased = false;
		didWalk = false;
		if (newWalkCmdSteps > 0) {
			// place this into walking queue
			// care must be taken and we can't just append this because usually
			// the starting point (clientside) of
			// this packet and the current position (serverside) do not
			// coincide. Therefore we might have to go
			// back in time in order to find a proper connecting vertex. This is
			// also the origin of the character
			// walking back and forth when there's noticeable lag and we keep on
			// seeding walk commands.
			int firstX = newWalkCmdX[0], firstY = newWalkCmdY[0]; // the point
																	// we need
																	// to
																	// connect
																	// to from
																	// our
																	// current
																	// position
																	// ...

			// travel backwards to find a proper connection vertex
			int lastDir = 0;
			boolean found = false;
			numTravelBackSteps = 0;
			int ptr = wQueueReadPtr;
			int dir = Misc.direction(currentX, currentY, firstX, firstY);
			if (dir != -1 && (dir & 1) != 0) {
				// we can't connect first and current directly
				do {
					lastDir = dir;
					if (--ptr < 0)
						ptr = Constants.WALKING_QUEUE_SIZE - 1;

					travelBackX[numTravelBackSteps] = walkingQueueX[ptr];
					travelBackY[numTravelBackSteps++] = walkingQueueY[ptr];
					dir = Misc.direction(walkingQueueX[ptr],
							walkingQueueY[ptr], firstX, firstY);
					if (lastDir != dir) {
						found = true;
						break; // either of those two, or a vertex between those
								// is a candidate
					}

				} while (ptr != wQueueWritePtr);
			} else
				found = true; // we didn't need to go back in time because the
								// current position
			// already can be connected to first

			if (!found)
				println_debug("Fatal: couldn't find connection vertex! Dropping packet.");
			else {
				wQueueWritePtr = wQueueReadPtr; // discard any yet unprocessed
												// waypoints from queue

				addToWalkingQueue(currentX, currentY); // have to add this in
														// order to keep
														// consistency in the
														// queue

				if (dir != -1 && (dir & 1) != 0) {
					// need to place an additional waypoint which lies between
					// walkingQueue[numTravelBackSteps-2] and
					// walkingQueue[numTravelBackSteps-1] but can be connected
					// to firstX/firstY

					for (int i = 0; i < numTravelBackSteps - 1; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
					int wayPointX2 = travelBackX[numTravelBackSteps - 1], wayPointY2 = travelBackY[numTravelBackSteps - 1];
					int wayPointX1, wayPointY1;
					if (numTravelBackSteps == 1) {
						wayPointX1 = currentX;
						wayPointY1 = currentY;
					} else {
						wayPointX1 = travelBackX[numTravelBackSteps - 2];
						wayPointY1 = travelBackY[numTravelBackSteps - 2];
					}
					// we're coming from wayPoint1, want to go in direction
					// wayPoint2 but only so far that
					// we get a connection to first

					// the easiest, but somewhat ugly way:
					// maybe there is a better way, but it involves shitload of
					// different
					// cases so it seems like it isn't anymore
					dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2,
							wayPointY2);
					if (dir == -1 || (dir & 1) != 0) {
						println_debug("Fatal: The walking queue is corrupt! wp1=("
								+ wayPointX1
								+ ", "
								+ wayPointY1
								+ "), "
								+ "wp2=("
								+ wayPointX2
								+ ", "
								+ wayPointY2
								+ ")");
					} else {
						dir >>= 1;
						found = false;
						int x = wayPointX1, y = wayPointY1;
						while (x != wayPointX2 || y != wayPointY2) {
							x += Misc.directionDeltaX[dir];
							y += Misc.directionDeltaY[dir];
							if ((Misc.direction(x, y, firstX, firstY) & 1) == 0) {
								found = true;
								break;
							}
						}
						if (!found) {
							println_debug("Fatal: Internal error: unable to determine connection vertex!"
									+ "  wp1=("
									+ wayPointX1
									+ ", "
									+ wayPointY1
									+ "), wp2=("
									+ wayPointX2
									+ ", "
									+ wayPointY2
									+ "), "
									+ "first=("
									+ firstX + ", " + firstY + ")");
						} else
							addToWalkingQueue(wayPointX1, wayPointY1);
					}
				} else {
					for (int i = 0; i < numTravelBackSteps; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
				}

				// now we can finally add those waypoints because we made sure
				// about the connection to first
				for (int i = 0; i < newWalkCmdSteps; i++) {
					addToWalkingQueue(newWalkCmdX[i], newWalkCmdY[i]);
				}

			}

			isRunning = newWalkCmdIsRunning || isRunning2;
			didWalk = true;
		}
		preProcessing();
	}

	public void kick() {
		Client client = (Client) this;
		client.getActionAssistant().logout();
		isKicked = true;
	}

	// npc stuff here
	public static final int maxNPCListSize = NPCManager.MAXIMUM_NPCS;
	public NPC[] npcList = new NPC[maxNPCListSize];
	public int npcListSize = 0;

	// bit at position npcId is set to 1 in case player is currently in
	// playerList
	public byte[] npcInListBitmap = new byte[(NPCManager.MAXIMUM_NPCS + 7) >> 3];
	public boolean rebuildNPCList = false;

	public void addNewNPC(NPC npc, Stream str, Stream updateBlock) {

		int slot = npc.getNpcId();
		npcInListBitmap[slot >> 3] |= 1 << (slot & 7); // set the flag
		npcList[npcListSize++] = npc;

		str.writeBits(14, slot); // client doesn't seem to like id=0

		int z = npc.getAbsY() - getAbsY();
		if (z < 0)
			z += 32;
		str.writeBits(5, z); // y coordinate relative to thisPlayer
		z = npc.getAbsX() - getAbsX();
		if (z < 0)
			z += 32;
		str.writeBits(5, z); // x coordinate relative to thisPlayer

		str.writeBits(1, 0); // something??
		str.writeBits(16, npc.getDefinition().getType());

		boolean savedUpdateRequired = npc.isUpdateRequired();
		npc.setUpdateRequired(true);
		npc.appendNPCUpdateBlock(updateBlock);
		npc.setUpdateRequired(savedUpdateRequired);
		str.writeBits(1, 1); // update required
	}

	/**
	 * @param playerName
	 *            the playerName to set
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * @return the playerName
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @param playerPass
	 *            the playerPass to set
	 */
	public void setPlayerPass(String playerPass) {
		this.playerPass = playerPass;
	}

	/**
	 * @return the playerPass
	 */
	public String getPlayerPass() {
		return playerPass;
	}

	public void setLook(int[] playerAppearance) {
		this.playerAppearance = playerAppearance;
	}

	/**
	 * @return the friends
	 */
	public int[] getLook() {
		return playerAppearance;
	}

	
	
	/**
	 * @param friends
	 *            the friends to set
	 */
	public void setFriends(long[] friends) {
		this.friends = friends;
	}

	/**
	 * @return the friends
	 */
	public long[] getFriends() {
		return friends;
	}

	/**
	 * @param ignoresSize
	 *            the ignoresSize to set
	 */
	public void setIgnoresSize(int ignoresSize) {
		this.ignoresSize = ignoresSize;
	}

	/**
	 * @return the ignoresSize
	 */
	public int getIgnoresSize() {
		return ignoresSize;
	}

	/**
	 * @param friendsSize
	 *            the friendsSize to set
	 */
	public void setFriendsSize(int friendsSize) {
		this.friendsSize = friendsSize;
	}

	/**
	 * @return the friendsSize
	 */
	public int getFriendsSize() {
		return friendsSize;
	}

	/**
	 * @param playerMember
	 *            the playerMember to set
	 */
	public void setPlayerMember(boolean playerMember) {
		this.playerMember = playerMember;
	}

	/**
	 * @return the playerMember
	 */
	public boolean isPlayerMember() {
		return playerMember;
	}

	/**
	 * @param ignores
	 *            the ignores to set
	 */
	public void setIgnores(long[] ignores) {
		this.ignores = ignores;
	}

	/**
	 * @return the ignores
	 */
	public long[] getIgnores() {
		return ignores;
	}

	/**
	 * @param playerBankSize
	 *            the playerBankSize to set
	 */
	public void setPlayerBankSize(int playerBankSize) {
		this.playerBankSize = playerBankSize;
	}

	/**
	 * @return the playerBankSize
	 */
	public int getPlayerBankSize() {
		return playerBankSize;
	}

	/**
	 * @param bankXinterfaceID
	 *            the bankXinterfaceID to set
	 */
	public void setBankXinterfaceID(int bankXinterfaceID) {
		this.bankXinterfaceID = bankXinterfaceID;
	}

	/**
	 * @return the bankXinterfaceID
	 */
	public int getBankXinterfaceID() {
		return bankXinterfaceID;
	}

	/**
	 * @param bankXremoveSlot
	 *            the bankXremoveSlot to set
	 */
	public void setBankXremoveSlot(int bankXremoveSlot) {
		this.bankXremoveSlot = bankXremoveSlot;
	}

	/**
	 * @return the bankXremoveSlot
	 */
	public int getBankXremoveSlot() {
		return bankXremoveSlot;
	}

	/**
	 * @param bankXremoveID
	 *            the bankXremoveID to set
	 */
	public void setBankXremoveID(int bankXremoveID) {
		this.bankXremoveID = bankXremoveID;
	}

	/**
	 * @return the bankXremoveID
	 */
	public int getBankXremoveID() {
		return bankXremoveID;
	}

 	public int getId() {
		return playerId;
	}

	public String decodeChat(byte[] msg, int chatLength) {
		int j = 0;
		int k = -1;
		int currentPosition=0;
		aCharArray631 = new char[msg.length];
		for(int l = 0; l < chatLength; l++) {
			int i1 = msg[currentPosition++] & 0xff;
			aCharArray631[j++] = validChars[i1];
		}
		boolean flag1 = true;
		for(int k1 = 0; k1 < j; k1++) {
			char c = aCharArray631[k1];
			if(flag1 && c >= 'a' && c <= 'z') {
				aCharArray631[k1] += '\uFFE0';
				flag1 = false;
			}
			if(c == '.' || c == '!' || c == '?') flag1 = true;
		}
		return new String(aCharArray631, 0, j);
	}
	private static char[] aCharArray631 = new char[256];
	private static final char[] validChars = {
		' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 
		'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 
		'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', 
		'3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', 
		'.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', 
		'\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', 
		']', '^', '<','>' ,'_'
	};
	
	/**
	 * System to optimize sendFrame126 performance.
	 * @author MikeRSPS
	 * UltimateScape
	 * http://ultimatescape2.com
	 */
	private Map<Integer, TinterfaceText> interfaceText = new HashMap<Integer, TinterfaceText>();
	
	public class TinterfaceText {
		public int id;
		public String currentState;
		
		public TinterfaceText(String s, int id) {
			this.currentState = s;
			this.id = id;
		}
		
	}

	public boolean checkPacket126Update(String text, int id) {
		if(!interfaceText.containsKey(id)) {
			interfaceText.put(id, new TinterfaceText(text, id));
		} else {
			TinterfaceText t = interfaceText.get(id);
			if(text.equals(t.currentState)) {
				return false;
			}
			t.currentState = text;
		}
		return true;
	}
	
	
	
	
	
}