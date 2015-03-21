package com.rs.worldserver;

//import org.runetoplist.VoteChecker;
import java.sql.*;
import java.util.Hashtable;

import com.rs.worldserver.admin.GUI;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.hash.RehashManager;
import com.rs.worldserver.model.player.BloodLust;
import com.rs.worldserver.io.IOThread;
import com.rs.worldserver.util.log.Log;
import com.rs.worldserver.world.DialogueManager;
import com.rs.worldserver.world.ItemManager;
import com.rs.worldserver.world.NPCManager;
import com.rs.worldserver.world.ObjectManager;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.world.ShopManager;
import com.rs.worldserver.world.StillGraphicsManager;
import com.rs.worldserver.model.npc.NPCCombat;
import com.rs.worldserver.world.PublicEvent;
import com.rs.worldserver.model.player.ClanChat;
import com.rs.worldserver.model.player.FightPits;
import com.rs.worldserver.model.player.WebEvent;
import com.rs.worldserver.model.player.CastleWars;
import com.rs.worldserver.model.player.InGameEventManager;
import com.rs.worldserver.model.player.HelpManager;
import com.rs.worldserver.model.player.AchievementLoader;
import com.rs.worldserver.model.player.GetWepSound;
import com.rs.worldserver.model.player.WalkingCheck;
import com.rs.worldserver.model.player.DoorManager;
import com.rs.worldserver.model.player.WeaponSpeed;
import com.rs.worldserver.util.BanProcessor;
import com.rs.worldserver.util.RightsProcessor;
import com.rs.worldserver.model.player.DegradeManager;


/**
 * Server main class.
 * 
 * @author Winterlove
 * @author Daiki
 * @author Graham
 * 
 */
public class Server {
	public static Connection con = null;
	public static Statement statement;

	public static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://quantumrsps.com/quantumr_informations","quantumr_inform","lkASOIFJ421!");
			statement = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
   // public static VoteChecker voteChecker = null;
	public static final String CREDITS = "Shard Revolutions\r\nCreated by Graham, Vastico, Jonas and thiefmn6092\r\nInspired by NR";

	/**
	 * We use this to synchronize any threads that deal with game logic.
	 * Probably not the best way, but the easiest at the moment.
	 */
	public static Object gameLogicLock = new Object();

	/**
	 * Flag to say if debug mode is enabled.
	 */
	private static boolean debugModeEnabled = false;

	/**
	 * Shut down server flag.
	 */
	private static boolean shutdownServer = false;
	public static boolean UpdateServer = false;
	public static boolean FFA = false;
	public static boolean varrockRaid = false;
	public static boolean faladorRaid = false;
	public static boolean FFAMULTI = false;
	public static boolean DICE = false;
	public static boolean DICING = false;
	public static boolean Trade = true;
	public static boolean Tradeloc = true;
	public static boolean NewChat = true;
	public static boolean Friend = true;
	public static long FDelay = System.currentTimeMillis();
	public static int npcSlot = 0;
	public static boolean Projectile = true;
	public static boolean portal = false;
	public static int pure = 126;
	public static long Loadtime = System.currentTimeMillis();
    public static boolean lowCpu;
	public static boolean safe = false;
	public static boolean FFALog = false;
	public static long lever = 0;
	private static long processed = 0;
	public static boolean WorldFriendsReady = false;
	//public static long[] playernames;
	public static Hashtable playerSQLnames = new Hashtable(2000);
	public static Hashtable atObjExceptions = new Hashtable(25);
	public static int EventCount = 0;

	/**
	 * Manages/handlers/whatever you want to call them.
	 */
	private static PlayerManager playerManager = null;
	private static FightPits fightpits = null;
	private static BloodLust bl = null;
	private static DegradeManager dm = null;
	private static InGameEventManager igem = null;
	private static IOThread ioThread = null;
	private static CastleWars castleWars = null;
	private static ItemManager itemManager = null;
	private static PublicEvent publicEvent = null;
	private static ObjectManager objectManager = null;
	private static NPCManager npcManager = null;
	private static NPCCombat npcCombat = null;
	private static WebEvent webEvent = null;
	private static DialogueManager dialogueManager = null;
	private static StillGraphicsManager stillGraphicsManager = null;
	private static ShopManager shopManager = null;
	public static WorldMap WorldMap = null;
	public static GetWepSound GetWepSound = null;
	private static GUI gui = null;
	public static DoorManager doorManager = null;
	
	private Server() {

	}

	public static void printUsage() {
		System.out.println("Usage:");
		System.out.println("    java com.rs.Server [debug/release] [gui/nogui]");
		System.exit(0);
	}

	private static int lastProcessTime = 0;
	private static double averageProcessTime = -1;
	public static int seconds = 0;
	public static int minutes = 0;
	public static int hours = 0;
	public static int days = 0;

	public static String uptime() {
		if (seconds == 120) {
			seconds = 0;
			minutes++;
		}
		if (minutes == 60) {
			minutes = 0;
			hours++;
		}
		if (hours == 24) {
			hours = 0;
			days++;
		}
		return "@whi@"+days + "@red@D @whi@" + hours + "@red@H @whi@" + minutes + "@red@M @whi@" + seconds / 2 + "@red@S";
	}
	//height,absX,absY,toAbsX,toAbsY,type
    public static final boolean checkPos(int height,int absX,int absY,int toAbsX,int toAbsY,int type)
    {
        return I.I(height,absX,absY,toAbsX,toAbsY,type);
    }	
	@SuppressWarnings("unchecked")
	public static void main(java.lang.String args[]) {
		if (args.length >= 2) {
			if (args[0].equalsIgnoreCase("debug")) {
				debugModeEnabled = true;
			} else if (args[0].equalsIgnoreCase("release")) {
				debugModeEnabled = false;
			} else {
				printUsage();
			}
			if (args[1].equalsIgnoreCase("gui")) {
				gui = new GUI();
			} else if (args[1].equalsIgnoreCase("nogui")) {
				gui = null;
			} else {
				printUsage();
			}
		} else {
			debugModeEnabled = false;
			gui = new GUI();
		}
		System.setOut(new Log(System.out));
		System.setErr(new Log(System.err));
		String[] credits = CREDITS.split("\r\n");
		for (String credit : credits) {
			System.out.println(credit);
		}
		System.out.println("");
		System.out.println("Loading...");
		try {
			//atObjExceptions.put(objectID, new int[]{X,Y,distance,X1,Y1,distance2...})
			atObjExceptions.put(13615,new int[]{3201,3423,2,2994,3389,2}); //Edgeville Portals (varrock, falador)
			atObjExceptions.put(13623,new int[]{3203,3426,2}); 
			atObjExceptions.put(13624,new int[]{3203,3422,2}); //Falador Portals
			atObjExceptions.put(13627,new int[]{2990,3389,2}); //Varrock Portals
			atObjExceptions.put(5097,new int[]{2637,9516,1});atObjExceptions.put(10177,new int[]{2545,10143,0});
			atObjExceptions.put(1733,new int[]{3045,3927,0});atObjExceptions.put(8966,new int[]{2442,10147,1});
			atObjExceptions.put(5959,new int[]{3090,3956,0});atObjExceptions.put(4493,new int[]{3438,3537,1});
			atObjExceptions.put(9706,new int[]{3105,3956,0});atObjExceptions.put(10596,new int[]{3056,9562,1,3056,9555,1});
			atObjExceptions.put(9356,new int[]{2438,5168,1});atObjExceptions.put(3353,new int[]{2356,9800,4});
			atObjExceptions.put(8929,new int[]{2523,3740,1});atObjExceptions.put(4388,new int[]{2437,3083,1});
			atObjExceptions.put(4408,new int[]{2435,3090,3});atObjExceptions.put(4387,new int[]{2438,3096,1});
			atObjExceptions.put(4420,new int[]{2382,3133,1,2382,3131,1});atObjExceptions.put(4418,new int[]{2380,3130,0,2372,3126,0,2374,3130,0});
			atObjExceptions.put(4419,new int[]{2417,3077,0,2416,3074,1});atObjExceptions.put(4417,new int[]{2425,3077,0,2427,3081,0,2419,3077,0});
			atObjExceptions.put(4389,new int[]{2372,9486,4});atObjExceptions.put(4390,new int[]{2425,9530,2});
			atObjExceptions.put(4406,new int[]{2431,3072,2});atObjExceptions.put(4407,new int[]{2368,3135,2});
			atObjExceptions.put(13199,new int[]{3362,3270,2,3092,3506,1,2331,9814,2,2590,9433,2,2542,4714,2,3296,3183,2,3661,3516,2,2769,3214,2,3218,3210,2}); // blue altar
			atObjExceptions.put(411,new int[]{3367,3270,2,3085,3508,1,2336,9814,2,2593,9433,2,2538,4719,2,3299,3181,2,3659,3516,2,2768,3215,2,3218,3213,2}); // chaos altar
			atObjExceptions.put(6552,new int[]{2341,9814,2,2597,9433,2,3085,3511,2,3372,3271,2,2542,4718,2,3297,3181,2,3657,3516,2,2765,3215,2,3218,3216,2}); // ancient altar
			WalkingCheck.check();
			WorldMap = new WorldMap();
			WorldMap.loadWorldMap();
			RehashManager.rehash();
			EventManager.initialise();
			HelpManager.load();
			npcCombat = new NPCCombat();
			playerManager = PlayerManager.getSingleton();
			itemManager = new ItemManager();
			objectManager = new ObjectManager();
			npcManager = new NPCManager();
			publicEvent = new PublicEvent();
			dialogueManager = new DialogueManager();
			stillGraphicsManager = new StillGraphicsManager();
			shopManager = new ShopManager();
			ClanChat.createPermChannels();
			GetWepSound = new GetWepSound();
			AchievementLoader.loadAchievements();
			ioThread = new IOThread();
			castleWars = new CastleWars();
            //voteChecker = new VoteChecker(VoteConfig.DATABASE_ADDRESS, VoteConfig.DATABASE_NAME, VoteConfig.DATASE_UESRNAME, VoteConfig.DATABASE_PASSWORD);
			doorManager = new DoorManager();
			BanProcessor.loadPunishments();
			fightpits = new FightPits();
			bl = new BloodLust();
			dm = new DegradeManager();
			//igem = new InGameEventManager();
			RightsProcessor.loadRights();
			WeaponSpeed.getInstance();
			ObjectDef.loadConfig();
            Region.load();
		//	VirtualWorld.init();	
			(new Thread(ioThread, "ioThread")).start();
		} catch (Exception e) {
			System.err.println("Error starting server: " + e.getMessage() + "!");
			e.printStackTrace();
			if (gui != null)
				gui.handleException(e);
			System.exit(0);
		}

		EventManager.getSingleton().addEvent(null,"null server", new Event() {

			@Override
			public void execute(EventContainer container) {
				long started = System.currentTimeMillis();
				if (shutdownServer) {
					System.out.println("Shutting down SQL...");
					System.out.println("Shutting down selector...");
					ioThread.shutdown();
					System.out.println("Shutting down event manager...");
					EventManager.getSingleton().shutdown();
					System.out.println("Shutting down player manager...");
					playerManager.destruct();
					System.out.println("Shutting down periodic update event...");
					container.stop();
				} else {
					try {
						itemManager.process();
						objectManager.process();
						npcManager.process();
						playerManager.process();
						processed = System.currentTimeMillis();
						shopManager.process();
						//seconds++;
                        if (lowCpu) {
                            Thread.sleep(3000);
                        }
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				long ended = System.currentTimeMillis();
				Server.lastProcessTime = (int) (ended - started);
				if (Server.averageProcessTime == -1) {
					Server.averageProcessTime = lastProcessTime;
				} else {
					Server.averageProcessTime = (Server.averageProcessTime + lastProcessTime) / 2;
				}
			}

			@Override
			public void stop() {
			}

		}, Constants.CYCLE_TIME);
		
	}

    public static int garbageCollectionCycle = 0;
	public static DialogueManager getDialogueManager() {
		return dialogueManager;
	}

	public static IOThread getIoThread() {
		return ioThread;
	}
	
	public static CastleWars getCastleWars(){
		return castleWars;
	}

	public static ItemManager getItemManager() {
		return itemManager;
	}
	public static PublicEvent getPublicEvent() {
		return publicEvent;
	}
	public static ObjectManager getObjectManager() {
		return objectManager;
	}
	public static InGameEventManager getInGameEventManager(){
		return igem;
	}
	public static BloodLust getBloodLust(){
		return bl;
	}
	public static DegradeManager getDegradeManager(){
		return dm;
	}
	public static FightPits getFightPits(){
		return fightpits;
	}
	public static WebEvent getWebEvent(){
		return webEvent;
	}
	public static PlayerManager getPlayerManager() {
		return playerManager;
	}

	public static NPCManager getNpcManager() {
		return npcManager;
	}

	public static StillGraphicsManager getStillGraphicsManager() {
		return stillGraphicsManager;
	}

	public static ShopManager getShopManager() {
		return shopManager;
	}

	public static boolean isDebugModeEnabled() {
		return false;
	}

	/**
	 * @param gui
	 *            the gui to set
	 */
	public static void setGui(GUI gui) {
		Server.gui = gui;
	}

	/**
	 * @return the gui
	 */
	public static GUI getGui() {
		return gui;
	}

	/**
	 * @param lastProcessTime
	 *            the lastProcessTime to set
	 */
	public static void setLastProcessTime(int lastProcessTime) {
		Server.lastProcessTime = lastProcessTime;
	}

	/**
	 * @return the lastProcessTime
	 */
	public static int getLastProcessTime() {
		return lastProcessTime;
	}

	/**
	 * @param averageProcessTime
	 *            the averageProcessTime to set
	 */
	public static void setAverageProcessTime(int averageProcessTime) {
		Server.averageProcessTime = averageProcessTime;
	}

	/**
	 * @return the averageProcessTime
	 */
	public static int getAverageProcessTime() {
		return (int) Math.ceil(averageProcessTime);
	}

	public static void shutdown() {
		shutdownServer = true;
	}

}