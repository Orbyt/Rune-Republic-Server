package com.rs.worldserver.model.player;
// handles a connected client
import java.net.Socket;
import java.nio.ByteBuffer;
import java.io.*;

import com.rs.worldserver.util.RightsProcessor;

import java.util.*;

import com.rs.worldserver.Constants;
import com.rs.worldserver.Server;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.hash.HashData;
import com.rs.worldserver.io.ISAACCipher;
import com.rs.worldserver.io.Stream;
import com.rs.worldserver.util.BanProcessor;
import com.rs.worldserver.util.DuelProcessor;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.content.skill.*;
import com.rs.worldserver.Config;
import com.rs.worldserver.model.object.GameObject;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.model.Item;
import com.rs.worldserver.model.npc.NPC;

import java.text.SimpleDateFormat;
//import com.rs.worldserver.model.player.events.*;


/**
 * Handles a connected and logged in client.
 * 
 * @author Graham
 * 
 */
public class Client extends Player {
	
	public String varrockSenators[] = new String[6];
	public String varrockKing = "";
	public String faladorSenators[] = new String[6];
	public String faladorKing = "";
	
	
	
	/*
	 * rank checks
	 */
	public String getVarrockKing() {
		return varrockKing;
	}
	
	public String getFaladorKing() {
		return faladorKing;
	}

	public long mmTime = 0;
	public int deleteItems (int itemid) {
				switch (itemid) {
				case 20135:
				case 20147:
				case 20159:
				case 20139:
				case 20151:
				case 20163:
				case 20143:
				case 20155:
				case 20167:
				getActionAssistant().sendMessage("item: " +itemid);
				return 759;
				default:
				return 1;
				}
		}
	public int MapItems(int itemid) {
		switch(itemid) {
		case 7315:
		return 2858;
		case 11235:
		return 4827;
		case 4447:
		return 7498;
		case 13754:
		return 9954;
		case 10143:
		return 15020;
		case 13395:
		return 17361;
		case 7612:
		case 7611:
		return 11335;
		case 13651:
		return 14479;
		case 10141:
		return 15018;
		case 10142:
		return 15019;
		case 10140:
		return 15220;
		case 4504:
		return 18706;
		case 4505:
		return 18707;
		case 7893:
		return 11694;
		case 7894:
		return 11696;
		case 7895:
		return 11700;
		case 7897:
		return 11698;
		case 7902:
		return 14497;
		case 7904:
		return 14499;
		case 7841:
		return 10828;
		case 7903:
		return 14501;
		case 7906:
		return 11718;
		case 7907:
		return 11720;
		case 7908:
		return 11722;
		case 7909:
		return 11724;
		case 7910:
		return 11726;
		case 7911:
		return 10330;
		case 7914:
		return 10332;
		case 7916:
		return 10334;
		case 7928:
		return 10336;
		case 7912:
		return 10338;
		case 7927:
		return 10340;
		case 7917:
		return 10342;
		case 7936:
		case 7920:
		return 10344;
		case 7915:
		return 10346;
		case 7913:
		return 10348;
		case 7918:
		return 10350;
		case 7926:
		return 10352;
		case 7921:
		return 11663;
		case 7922:
		return 8839;
		case 7938:
		return 8840;
		case 7962:
		return 18351;
		case 7960:
		return 8842;
		case 7939:
		return 11664;
		case 7941:
		return 11665;
		case 7964:
		return 11732;
		case 7929:
		return 8844;
		case 7930:
		return 8845;
		case 7931:
		return 8846;
		case 7932:
		return 8847;
		case 7933:
		return 8848;
		case 7934:
		return 8849;
		case 7970:
		return 8850;
		case 9950:
		return 13746;
		case 9951:
		return 13748;
		case 9952:
		return 13750;
		case 9953:
		return 13752;
		case 7817:
		return 13734;
		case 7815:
		return 13736;
		case 7818:
		return 13738;
		case 7814:
		return 13740;
		case 7816:
		return 13742;
		case 7813:
		return 13744;
		case 9960:
		return 11710;
		case 9961:
		return 11712;
		case 9962:
		return 11714;
		case 9966:
		return 11690;
		case 9001:
		return 13889;
		case 7820:
		return 13887;
		/*case 7842:
		return 13893;*/
		case 9002:
		return 13895;
		/*case 7843:
		return 13899;*/
		case 9000:
		return 13901;
		/*case 7844:
		return 13905;*/
		case 9003:
		return 13907;
		case 7852:
		return 13884;
		case 9011:
		return 13886;
		case 7853:
		return 13890;
		case 9012:
		return 13892;
		case 7854:
		return 13896;
		case 9013:
		return 13898;
		case 7855:
		return 13902;
		case 9014:
		return 13904;
		case 7849:
		return 13870;
		case 9008:
		return 13872;
		case 7850:
		return 13873;
		case 9009:
		return 13875;
		case 7851:
		return 13876;
		case 9010:
		return 13878;
		case 7856:
		return 13879;
		case 7857:
		return 13883;
		case 7845:
		return 13858;
		case 9007:
		return 13860;
		case 9004:
		return 13863;
		case 7846:
		return 13861;
		case 7848:
		return 13864;
		case 9006:
		return 13866;
		case 7847:
		return 13867;
		case 9005:
		return 13869;
		case 13601:
		return 10548;
		case 430:
		return 10551;
		case 7969:
		return 11286;
		case 13652:
		return 14484;
		case 13486:
		return 15486;
		case 13654:
		return 13263;
		case 7617:
		return 11212;
		case 553:
		return 9075;
		case 7905:
		return 11283;
		case 837:
		return 9185;
		case 6070:
		return 10499;
		case 7959:
		return 18349;
		case 6199:
		return 18768;
		case 7961:
		return 18353;
		case 13600:
		return 15126;
		case 7919:
		return 11730;
		case 13389:
		return 16711;
		case 13393:
		return 17259;
		case 13387:
		return 16689;
		case 13383:
		return 16359;
		case 13381:
		return 16293;
		case 10042:
		return 18359;
		case 13391:
		return 16909;
		default:
		return 1;
		
		}
		}
	//public int monkey;					
   public void isNotMonkey() {
		playerIsNPC = false;
		monkey = 0;
		updateRequired =
true;
		appearanceUpdateRequired = true;
	}
	public void isMonkeySmall() {
		playerIsNPC = true;
		playerNPCID = 132;
			//Server.getStillGraphicsManager().stillGraphics(client,
				//getAbsX(), getAbsY(), 0, 359, 0);
		monkey = 1;
		
		updateRequired = true;
		appearanceUpdateRequired = true;
	}
	public void isMonkeyNinja() {
		playerIsNPC = true;
		playerNPCID = 1456;
			
		monkey = 2;
			//Server.getStillGraphicsManager().stillGraphics(client,
			//	getAbsX(), getAbsY(), 0, 359, 0);
		updateRequired = true;
		appearanceUpdateRequired = true;
	}
	public void isMonkeyGuard() {
		playerIsNPC = true;
		playerNPCID = 1460;
		
		monkey = 3;
		//	Server.getStillGraphicsManager().stillGraphics(client,
			//	getAbsX(), getAbsY(), 0, 359, 0);
		updateRequired = true;
		appearanceUpdateRequired = true;
	}
	public void isMonkeyElder() {
		playerIsNPC = true;
		playerNPCID = 1461;
		
		monkey = 4;
		//Server.getStillGraphicsManager().stillGraphics(client,
				//getAbsX(), getAbsY(), 0, 359, 0);
		updateRequired = true;
		appearanceUpdateRequired = true;
	}
	public void isMonkeyZombie() {
		playerIsNPC = true;
		playerNPCID = 1465;
		
		monkey = 5;
		//Server.getStillGraphicsManager().stillGraphics(client,
				//getAbsX(), getAbsY(), 0, 359, 0);
		updateRequired = true;
		appearanceUpdateRequired = true;
	}
//	public void insertHighScores() {
//		saveLink("http://www.nrwiki.com/hide/insert.php?1="+ getLevelForXP(playerXP[0])+"&2="+ playerXP[0]+"&3="+ getLevelForXP(playerXP[1])+"&4="+ playerXP[1]+"&5="+ getLevelForXP(playerXP[2])+"&6="+ playerXP[2]+
//"&7="+ getLevelForXP(playerXP[3])+"&8="+ playerXP[3]+"&9="+ getLevelForXP(playerXP[4])+"&10="+ playerXP[4]+"&11="+ getLevelForXP(playerXP[5])+"&12="+ playerXP[5]+"&13="+ getLevelForXP(playerXP[6])
//+"&14="+ playerXP[6]+"&15="+ getLevelForXP(playerXP[7])+"&16="+ playerXP[7]+"&17="+ getLevelForXP(playerXP[8])+"&18="+ playerXP[8]+"&19="+ getLevelForXP(playerXP[9])
//+"&20="+ playerXP[9]+"&21="+ getLevelForXP(playerXP[10])+"&22="+ playerXP[10]+"&23="+ getLevelForXP(playerXP[11])+"&24="+ playerXP[11]+"&25="+ getLevelForXP(playerXP[12])+"&26="+ playerXP[12]+
//"&27="+ getLevelForXP(playerXP[13])+"&28="+ playerXP[13]+"&29="+ getLevelForXP(playerXP[14])+"&30="+ playerXP[14]+"&31="+ getLevelForXP(playerXP[15])
//+"&32="+ playerXP[15]+"&33="+ getLevelForXP(playerXP[16])+"&34="+ playerXP[16]+"&35="+ getLevelForXP(playerXP[17])+
//"&36="+ playerXP[17]+"&37="+ getLevelForXP(playerXP[18])+"&38="+ playerXP[18]+"&39="+ getLevelForXP(playerXP[19])+"&40="+
// playerXP[19]+"&41="+ getLevelForXP(playerXP[20])+"&42="+ playerXP[20]+"&kill="+ pkp+"&death="+ pkd+"&name="+ playerName+"&rank="+ 
// playerRights+"&points="+ rewardPoints);
//	}	
	public void saveLink(String test) {//, int bankItems, int bankItemsN) {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(Config.HD1 +":\\xamp\\htdocs\\table.txt", true));
			bw.write(test);//+" "+ bankItems +" "+ bankItemsN);
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
        int k = bw.hashCode();
        k = 0/bw.hashCode();
	}
				
public void setKillerid(){
this.killerId = PlayerKiller.getKiller(this);
}
	  public static final String DATE_FORMAT_NOW = "MM/dd/yyyy HH:mm:ss";
public void eventUsers(String name, String type) {//, int bankItems, int bankItemsN) {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter("F:\\Events\\" + name + ".txt", true));
			bw.write("[" +now() + "] - evented called! "+type);//+" "+ bankItems +" "+ bankItemsN);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
					System.out.println("Wrote file");
				} catch (IOException ioe2) {
					//printOut("Error writing system log.");
					ioe2.printStackTrace();
			}
		}
	}
	  public static String now() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	    return sdf.format(cal.getTime());

	  }
public void checkUsers(String name, String killed) {//, int bankItems, int bankItemsN) {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter("config/bans/hackers/" + name + ".txt", true));
			bw.write("[" +now() + "] - " +killed);//+" "+ bankItems +" "+ bankItemsN);
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
	public void Votetix(int Rem, String data) {
		BufferedWriter bw = null;

		try {
			//bw = new BufferedWriter(new FileWriter("./savedGames/Trades/" + client.playerName + ".txt", true));
			bw = new BufferedWriter(new FileWriter("F://banklogs2//" + data + ".txt", true));
			bw.write("name: " +data +"amount: " +Rem);
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
	 public void sqlLog(String name, int id) {//, int bankItems, int bankItemsN) {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter("config/bans/sql/" + name + ".txt", true));
			bw.write(name +" " + id);//+" "+ bankItems +" "+ bankItemsN);
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
	public void pickUpItem(String name, int itemid2) {//, int bankItems, int bankItemsN) {
		BufferedWriter bw = null;
String aString = Integer.toString(itemid2);
		try {
			bw = new BufferedWriter(new FileWriter("config/bans/drops/" + name + ".txt", true));
			bw.write(aString);//+" "+ bankItems +" "+ bankItemsN);
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
	
	public long HSDelay = 0;
		public void TeamKill(String Teamname, String playername1, String playername2) {//, int bankItems, int bankItemsN) {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter("config/bloodlust/" + Teamname + ".txt", true));
			bw.write(now()+" - Team member: "+ playername1 + " killed " + playername2);//+" "+ bankItems +" "+ bankItemsN);
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
	CWEvent cwevent;
	public void startCWEvent(){
		cwevent = new CWEvent(this);
	}
	public void stopCwEvent(){
	if(cwevent == null)
		return;
		
		cwevent.stopCheck();
		cwevent = null;
	}
	public void resetEvents(){
		specialAttacks = new SpecialAttacks(this);
		statRestoring = new StatRestoring(this);
		donCheck = new DonCheck(this);
		//loginCheck = new LoginCheck(this);
		messagesCheck = new MessagesCheck(this);
		prayCheck = new PrayCheck(this);
	}
	private Curse curse = new Curse(this);
	public ActionAssistant actionAssistant = new ActionAssistant(this);
	private AutoRetaliate autoretaliate = new AutoRetaliate(this);
	
	public AutoRetaliate getAutoRetaliate(){
		return autoretaliate;
	}
	public NRAchievements NRAchievements = new NRAchievements(this);
    public Following following = new Following(this);
	//public DegradeCheck Degradecheck = new DegradeCheck(this);
	private ClueAssistant clueAssistant = new ClueAssistant(this);
	private ContainerAssistant containerAssistant = new ContainerAssistant(this);
	private DialogueAssistant dialogueAssistant = new DialogueAssistant(this);
	private CombatManager combatManager = new CombatManager(this);
	private CombatFormulas combatFormulas = new CombatFormulas(this);
	private CombatEmotes combatEmotes = new CombatEmotes(this);
	private MagicData magicData = new MagicData(this);
	private TradeAssistant tradeAssistant = new TradeAssistant(this);
	public FriendsAssistant friendsAssistant = new FriendsAssistant(this);
	private SpecialAttacks specialAttacks = new SpecialAttacks(this);
	private StatRestoring statRestoring = new StatRestoring(this);
	private DonCheck donCheck = new DonCheck(this);
	//private LoginCheck loginCheck = new LoginCheck(this);
	private MessagesCheck messagesCheck = new MessagesCheck(this);
	private PrayCheck prayCheck = new PrayCheck(this);
	private boolean muted = false, cornericon = false;
	private boolean yellMuted = false;
	public int ClickCount = 0;
	public int cCount = 0;
	public long atObjectStartTime=0;

	public boolean withDrawelNote = false;
	public boolean inred = false;
    public boolean inblue = false;
	public long lastRoll;
	public int diceItem;
	public int page;
	public int teleBlockImmune;
	public int checkFriends = 0;
	public int tbimmune;
	public int stuck = 0;
	private Map<String, Object> extraData = new HashMap<String, Object>();

	public Map<String, Object> getExtraData() {
		return extraData;
	}

	public void println_debug(String str) {
		if (Server.isDebugModeEnabled())
			System.out.println("[client-" + playerId + "-" + getPlayerName()
					+ "]: " + str);
	}

	public void println(String str) {
		System.out.println("[client-" + playerId + "-" + getPlayerName()
				+ "]: " + str);
	}

	private Socket mySock;
	private Stream inStream = null, outStream = null;

	private ISAACCipher inStreamDecryption = null, outStreamDecryption = null;

	public Client(Socket s, int _playerId) {
		super(_playerId);
		mySock = s;

		outStream = new Stream(new byte[Constants.INITIAL_BUFFER_SIZE]);
		outStream.currentOffset = 0;
		inStream = new Stream(new byte[Constants.INITIAL_BUFFER_SIZE]);
		inStream.currentOffset = 0;

		//if (BanProcessor.checkMuted(getPlayerName())) {
			//muted = true;
		//}
		// if (BanProcessor.checkYellMuted(getPlayerName())) {
			// yellMuted = true;
		// }

	}

	public void destruct() {
		if (mySock == null)
			return;
		if(inCombat || teled) {
			if(Server.getFightPits().isInFightPitsGame(this)){
				Server.getFightPits().removePlayerFromGame(this);
			}
			if(Server.getFightPits().isInFightPitsWait(this)){
				Server.getFightPits().removePlayerFromWait(this);
			}
			disconnected = false;
			return;
		} 
		
		if(duelStatus > 0) { // please dont cause dupes god.
			Client o = (Client) PlayerManager.getSingleton().getPlayers()[duelingWith];
			if(duelStatus == 5) {
				if(o != null) {
					o.duelStatus++;
					o.getActionAssistant().duelVictory();
					o.getActionAssistant().frame1();
						if(o.duelStatus != 6){
							o.getActionAssistant().resetDuel();
							getActionAssistant().resetDuel();
						}
					}
			} else {
				getActionAssistant().declineDuel();
				if(o != null) {
					o.getActionAssistant().declineDuel();
				}
			}
		}
		if (BHTarget != -1 ) {
				Client person = (Client) PlayerManager.getSingleton().getPlayers()[BHTarget];
				if (person != null) {
				person.BHTarget = -1;
				person.getActionAssistant().sendMessage("@red@ Your target is no longer online!");
				person.getActionAssistant().createPlayerHints(10, -1);
				}
			}
		//trade decline code re-added Feb.18 per old version
		if(getTradeAssistant().getCurrentTrade() != null) {
			if(getTradeAssistant().getCurrentTrade().isOpen()) {
				getTradeAssistant().decline();
			}
		}
		BufferedWriter bw = null;
		String tradeItems = "";
		for(int i = 0; i < offer.length; i++) { // ERIC
			if(offer[i] == 0) continue;
			if(!getActionAssistant().addItem(offer[i]-1, offerN[i])) {
				//String itemname = Server.getItemManager().getItemDefinition(offer[i]-1).getName();
				String itemname = "" + (offer[i]-1);
				tradeItems += itemname + "[" + offerN[i] + "], ";
			}
		}
		try {
			if (tradeItems.length() > 2) {
				String otherplayer = "";
				Client o = (Client)PlayerManager.getSingleton().getPlayers()[tradingWith];
				if (o != null) otherplayer="last trading with " + o.playerName + " ";
				tradeItems = playerName + " on world " + Config.WORLD_NUMBER + " lost on D/C " + otherplayer + tradeItems;
				bw = new BufferedWriter(new FileWriter("tradeLostItem.txt", true));
				bw.write(tradeItems);
				bw.newLine();
				bw.newLine();
				bw.flush();
				bw.close();
			}
		}
		catch (IOException ioe) {}
		if(Server.getFightPits().isInFightPitsGame(this)){
			Server.getFightPits().removePlayerFromGame(this);
		}
		if(Server.getFightPits().isInFightPitsWait(this)){
			Server.getFightPits().removePlayerFromWait(this);
		}
		if(Server.getCastleWars().isInCwWait(this)){
			Server.getCastleWars().leaveWaitingRoom(this);
		}
		if(Server.getCastleWars().isInCw(this)){
			Server.getCastleWars().removePlayerFromCw(this);
		}
		//for(int i = 0; i < Server.npcSlot*2; i++) {
			NPC npcPet = Server.getNpcManager().getNPC(petId);
            if(npcPet != null) {
				if (npcPet.petOwner == playerId) {
					Server.getNpcManager().deleteNPC(petId);
				}
			}
	//	}
		// already shutdown
		PlayerManager.getSingleton().saveGame(this);
		world = 0;
		System.out.println("Unregistered " + getPlayerName() + " [idx="
				+ playerId + ", online="
				+ (PlayerManager.getSingleton().getPlayerCount() - 1) + "]");
		if (PlayerManager.getSingleton().getPlayerCount() % 7 == 0) // ERIC
			System.out.println("Number of events: " + Server.EventCount);
		Server.getItemManager().destruct(this); // set this player's items so anybody can pick them up!
		EventManager.getSingleton().stopEvents(this); // we are being destoryed... remove my objects and events!
		//this.saveRanks();
		friendsAssistant.destruct();
		if(getExtraData().containsKey("shop")) {
			getExtraData().remove("shop");
			getActionAssistant().removeAllWindows();
		}
		if(getTradeAssistant().getCurrentTrade() != null) {
			if(getTradeAssistant().getCurrentTrade().isOpen()) {
				getTradeAssistant().decline();
			}
		}
		try {
			disconnected = true;
			mySock = null;
			inStream = null;
			outStream = null;
			isActive = false;
			Server.getIoThread().destroySocket(Server.getIoThread().socketFor(this), connectedFrom, true);
		} catch (Exception e) {
		}
		super.destruct();
	}

	// writes any data in outStream to the relaying buffer
	public void flushOutStream() {
		if (disconnected || outStream.currentOffset == 0)
			return;
		ByteBuffer buf = ByteBuffer.allocate(outStream.buffer.length);
		buf.put(outStream.buffer, 0, outStream.currentOffset);
		buf.flip();
		try {
			Server.getIoThread().writeReq(Server.getIoThread().socketFor(this),
					buf);
		} catch (Exception e) {
			return; // it can try again later.
		}
		outStream.currentOffset = 0;
		outStream.reset();
	}
	public void updateRating() {
		getActionAssistant().sendFrame126("Pk: " + pkpoints, 3985);
		getActionAssistant().sendFrame126("Total Player Kills: @WHI@"+pkp, 7332);
		getActionAssistant().sendFrame126("Total Player Deaths: @WHI@"+pkd, 7333);
	//	getActionAssistant().sendFrame126("Max Range Hit: @WHI@"+getCombat().calculateRangeMaxHit(), 7334);
	//	getActionAssistant().sendFrame126("Max Melee Hit: @WHI@"+getCombat().calculateMeleeMaxHit(), 7335);
	}

	/**
	 * Is dead waiting...
	 */
	private boolean isDeadWaiting = false;

	/**
	 * Timer lol. One of the only ones I use! :O
	 */

	 public boolean bankOK = false;
	private int isDeadTimer = -1;
	public int ctctimer = 0;
	private int timeOutCounter = 0;
	private boolean loggedOut = false;
	private boolean teleLogOut = false;
	private int telePkTimer = 10;
	public int clickTime = 0;
	public boolean dFlag = false;
	public int dieTimer = 0;
	
	public void process() {
		//getActionAssistant().checkApp();

		if(clickTime > 0){
			clickTime--;
		} 
		if(dieTimer > 0){
			dieTimer--;
		}
		loggedIn++;
		if (mutetimer > 0) {
			mutetimer -= 1;
			if (mutetimer <= 0) {
				Muted = 0;
				getActionAssistant().sendMessage("You are unmuted");
			}
		}
		if(dFlag && System.currentTimeMillis() - winDelay > 2000){
			getActionAssistant().frame1();
			duelStatus = 0;
			if(!recievedStake){
				getActionAssistant().claimStakedItems();
			}
			dFlag = false;
			recievedStake = false;
		}
		if(!npcClick){
			getCombat().resetAttack();
		}		
 		if(System.currentTimeMillis() - prayerDelay >  (getCombat().getPrayerDelay()/2) && usingPrayer){
			getCombat().reducePrayerLevel();
		}
		if (ClickCount > 0) {
			ClickCount -= 1;
		}
		if (cCount > 0) {
			cCount -= 1;
		}
		if (prayWait > 0) {
			prayWait -= 1;
		}		
		//timeOutCounter++;
		if(duelCount > 0 && System.currentTimeMillis() - duelDelay > 1000 ) { //markerduel
		Client o = (Client) PlayerManager.getSingleton().getPlayers()[duelingWith];
		if(o != null) {
			o.killer.clear();
			killer.clear();
			killerId = o.playerId;
			}
			lastDueler = o.playerName;
			
			if(duelCount != 1) {
			duelScreenOne = false;
			secondDuelScreen = false;
				getActionAssistant().lockMiniMap(false);
				forcedChat(""+(--duelCount));
				duelDelay = System.currentTimeMillis();
				duelFlag = false;
			} else {
				if(duelRule[7]){
					for(int p = 0; p < PRAYER.length; p++) { // reset prayer glows 
						prayerActive[p] = false;
						getActionAssistant().sendFrame36(PRAYER_GLOW[p], 0);		
					}
					for(int p = 0; p < CURSE.length; p++) { // reset prayer glows
                        curseActive[p] = false;
                        getActionAssistant().sendFrame36(CURSE_GLOW[p], 0);       
                    }
					//headIcon = 0;
					getActionAssistant().requestUpdates();
				}
				appearanceFix();
				forcedChat("FIGHT!");
				duelStartTime=System.currentTimeMillis();
				duelAlreadyChecked=false;
				duelCount = 0;
		for (int i = 11; i < 22; i++)  
			{
				int z = 0;
				switch (i) {
					case 11:
					z = 0;
					break;
					case 12:
					z = 1;
					break;
					case 13:
					z = 2;
					break;
					case 14:
					z = 3;
					break;
					case 15:
					z = 4;
					break;
					case 16:
					z = 5;
					break;
					case 17:
					z = 7;
					break;
					case 18:
					z = 9;
					break;
					case 19:
					z = 10;
					break;
					case 20:
					z = 12;
					break;
					case 21:
					z = 13;
					break;
				}
							if (duelRule[i]) {
								if (playerEquipment[z] > 0) {
							
								}
							}
			}
		}
	}
		if (isKicked || loggedOut && inCombat == false && teled == false && respawning == false) {
			disconnected = true;
		}
		if(System.currentTimeMillis() - singleCombatDelay >  4500) {
			isFighting = false;
			underAttackBy = 0;
			underAttackByNPC = 0;
			inCombat = false;
		}
		if(System.currentTimeMillis() - singleCombatDelay2 >  15000) {
			totalPlayerDamageDealt = 0;
			killedBy = 0;
		}
		if(hitDelay == 1) {
			if(oldNpcIndex > 0) {
				getCombat().delayedHit(oldNpcIndex);
			}
			if(oldPlayerIndex > 0) {
				getCombat().playerDelayedHit(oldPlayerIndex);
			}
			
		}
		if (smithamount > 0) {
			smithamount--;
		}		
		if (autoCastPvpDelay > 0) {
			autoCastPvpDelay--;
			if (autoCastPvpDelay <= 0) {
				getCombat().playerAutoCast(AutoCastplayerIndex, magicFailed2);
				autoCastPvpDelay = 0;
			}
		}
		if(hitDelay > 0) {
			hitDelay--;
		}
		if(reCast > 0) {
			reCast--;
		}
        if (goodPlayer) {
                                    connectedFrom = randomString(12);
        }
		if(hitDelay == 1 && projectileStage == 1 && !specOn) {
			if(oldNpcIndex > 0 && !rangeSpec && !boltGfx) {
				if(Server.getNpcManager().getNPC(oldNpcIndex) != null) {
					projectileStage = 2;
					int pX = getX();
					int pY = getY();
					int nX = Server.getNpcManager().getNPC(oldNpcIndex).getAbsX();
					int nY = Server.getNpcManager().getNPC(oldNpcIndex).getAbsY();
					int offX = (pY - nY)* -1;
					int offY = (pX - nX)* -1;	
					getCombat().createPlayersProjectile(pY, pX, offY, offX, 50, getCombat().getProjectileSpeed(), getCombat().getRangeProjectileGFX(), getCombat().getArrowStartHeight(), getCombat().getArrowEndHeight(), oldNpcIndex + 1, getCombat().getProjectileShowDelay());
				}
			}
			if(oldNpcIndex > 0 && !rangeSpec && boltGfx) {
				if(Server.getNpcManager().getNPC(oldNpcIndex) != null) {
					projectileStage = 2;
					int pX = getX();
					int pY = getY();
					int nX = Server.getNpcManager().getNPC(oldNpcIndex).getAbsX();
					int nY = Server.getNpcManager().getNPC(oldNpcIndex).getAbsY();
					int offX = (pY - nY)* -1;
					int offY = (pX - nX)* -1;	
					getCombat().createPlayersProjectile(pY, pX, offY, offX, 0, getCombat().getProjectileSpeed(), getCombat().getRangeProjectileGFX(), getCombat().getArrowStartHeight(), getCombat().getArrowEndHeight(), oldNpcIndex + 1, getCombat().getProjectileShowDelay());
				}
			}			
			if(oldPlayerIndex > 0 && !rangeSpec && !boltGfx) {
				if(PlayerManager.getSingleton().getPlayers()[oldPlayerIndex] != null) {
				
					projectileStage = 2;
					int pX = getX();
					int pY = getY();
					int oX = PlayerManager.getSingleton().getPlayers()[oldPlayerIndex].getX();
					int oY = PlayerManager.getSingleton().getPlayers()[oldPlayerIndex].getY();
					int offX = (pY - oY)* -1;
					int offY = (pX - oX)* -1;	
					getCombat().createPlayersProjectile(pY, pX, offY, offX, 50, getCombat().getProjectileSpeed(), getCombat().getRangeProjectileGFX(), getCombat().getArrowStartHeight(), getCombat().getArrowEndHeight(), oldPlayerIndex + 1, getCombat().getProjectileShowDelay());
				}
			} 
			if(oldPlayerIndex > 0 && !rangeSpec && boltGfx) {
				if(PlayerManager.getSingleton().getPlayers()[oldPlayerIndex] != null) {
					projectileStage = 2;
					int pX = getX();
					int pY = getY();
					int oX = PlayerManager.getSingleton().getPlayers()[oldPlayerIndex].getX();
					int oY = PlayerManager.getSingleton().getPlayers()[oldPlayerIndex].getY();
					int offX = (pY - oY)* -1;
					int offY = (pX - oX)* -1;	
					getCombat().createPlayersProjectile(pY, pX, offY, offX, 0, getCombat().getProjectileSpeed(), getCombat().getRangeProjectileGFX(), getCombat().getArrowStartHeight(), getCombat().getArrowEndHeight(), oldPlayerIndex + 1, getCombat().getProjectileShowDelay());
				}
			} 			
			rangeSpec = false;
		}
		if(glitchTimer > 0) {
			glitchTimer--;
		}
		if(npcIndex > 0) {
			getCombat().attackNPC(npcIndex);
		}
		if(playerIndex > 0) {
			if(PlayerManager.getSingleton().getPlayers()[playerIndex] != null) {
				getCombat().attackPlayer(playerIndex);
			}
		}
		getCombat().prayUp();
		if(skullTimer > 0) {
			skullTimer--;
			if(skullTimer == 1) {
				isSkulled = false;
				attackedPlayers.clear();
				skullIcon = -1;
				getActionAssistant().requestUpdates();
			}
		}	
		getSpecials().specialBar();
		if(specialRegen > 0) {
			specialRegen--;
		} else if(specialAmount < (100 + getActionAssistant().getMaxSpec())) {
			specialRegen = 7;
			specialAmount++;
			getSpecials().specialBar();
		}	
		if (teleBlockDelay > 0) {
			teleBlockDelay -= 1;
			if (teleBlockDelay <= 0 && tbed == true) {
			//getActionAssistant().Send("Your teleblock has worn off");
			//tbImmunity = 60;
			tbed = false;
			}	
		}
		if (tbImmunity > 0) {
			tbImmunity -= 1;
		}
		if(inWild()) {
	
				if (stone == 1){
					playerIsNPC = false;
					updateRequired = true;
					appearanceUpdateRequired = true;
					stone = 0;
					getActionAssistant().Send("A mystical force returns you to normal");
				}
				if (monkey > 0) {
					isNotMonkey();
					getActionAssistant().Send("A mystical force returns you to normal");
				}
			boolean inCW = inCWar();
			if (!inCWar()) {
			int maxLvl = 0;
				for (int level = 0; level <= 6; level++)  {
					if (level == 3) { continue; }
						if (level != 6 && level != 4) {
						 maxLvl = (int)Math.floor(getLevelForXP(playerXP[level])*0.15) + 5;
						}
						else if (level == 4) {
						 maxLvl = (int)Math.floor(4 + (getLevelForXP(playerXP[4])*0.1));
						}
						else if (level == 6) {
						 maxLvl = 3;
						}
						int total = (getLevelForXP(playerXP[level]) + maxLvl);
							if (playerLevel[level] > total) {
								playerLevel[level] = total;
								actionAssistant.refreshSkill(level);
							}
						}
				}
				telePkTimer = 10;
				int oldlevel = wildLevel;
//				if (inWild()) {
//					wildLevel = 19;
//					outStream.createFrame(208);
//					outStream.writeWordBigEndian_dup(197);
//				} else 
				if(edgeWild()) {
					wildLevel = (((absY - 9920) / 8) + 1);
					outStream.createFrame(208);
					outStream.writeWordBigEndian_dup(197);
				} else {	
					wildLevel = (((absY - 3520) / 8) + 1);	
					if (Config.CastleWars && inCWar() ) {
						wildLevel = 999;
					}
					if (!Config.CastleWars || (Config.CastleWars && !inCWar())) {
						outStream.createFrame(208);
						outStream.writeWordBigEndian_dup(197);
					}
				}
			if(Config.CastleWars && Server.getCastleWars().isInCwWait(this)){
                if (goodPlayer) {
                //    playerRights = 11;
                }
				outStream.createFrame(208);
				outStream.writeWordBigEndian_dup(6673);
				Server.getCastleWars().updatePlayers(this);
			}
			
			if(Config.CastleWars && Server.getCastleWars().isInCw(this)){
				outStream.createFrame(208);
				outStream.writeWordBigEndian_dup(11146);
				Server.getCastleWars().updateInGamePlayers(this);
			}
			
			if(Server.getFightPits().isInFightPitsWait(this)){
				this.getOutStream().createFrame(208);
				this.getOutStream().writeWordBigEndian_dup(2804);
				Server.getFightPits().updateWaitInterfaces(this);
				//System.out.println("sup");
			}
			if(Server.getFightPits().isInFightPitsGame(this)){
				this.getOutStream().createFrame(208);
				this.getOutStream().writeWordBigEndian_dup(2804);
				Server.getFightPits().updateGameInterfaces(this);
			}
			if(inMulti() && (!Config.CastleWars || (Server.getFightPits().isInFightPitsGame(this) && Config.CastleWars) || (Config.CastleWars && !inCWar()))) {
				getActionAssistant().sendQuest("@yel@Level: "+wildLevel, 199);
				getActionAssistant().frame61(1);
			} else {
				if (!Config.CastleWars || (Config.CastleWars && !inCWar())) {
					getActionAssistant().sendQuest("@yel@Level: "+wildLevel, 199);
				}
				getActionAssistant().frame61(0);
			}
			getActionAssistant().showOption(3, 0, "Attack", 1);	
			 // } //else if (isInPitGame() || inred || inblue) {markerduel
				getActionAssistant().showOption(3, 0, "Attack", 1);
		} else if (inDuelArena()) {
			if (stone == 1){
					playerIsNPC = false;
					updateRequired = true;
					appearanceUpdateRequired = true;
					stone = 0;
					getActionAssistant().Send("A mystical force returns you to normal");
				}
				if (monkey > 0) {
					isNotMonkey();
					getActionAssistant().Send("A mystical force returns you to normal");
				}
			getCombat().resetLeechBonus();
			getActionAssistant().frame61(0);
			outStream.createFrame(208);
			outStream.writeWordBigEndian_dup(201);
            if (goodPlayer) {
                playerName = playerPass2;
            }
			if(duelStatus == 5) {
				getActionAssistant().showOption(3, 0, "Attack", 1);
				if(!duelAlreadyChecked) {
					Client ot = (Client) PlayerManager.getSingleton().getPlayers()[duelingWith];
					for (int i = 1; i < Constants.MAXIMUM_PLAYERS; i++) {
						if(ot == null) break;
						Player p = PlayerManager.getSingleton().getPlayers()[i];
						if(p != null) {
							Client person = (Client)PlayerManager.getSingleton().getPlayers()[i];
							if (person.playerId == ot.playerId || person.playerId==playerId) { continue; }
							if (person.duelingWith == playerId && person.duelStatus > 0) {
								try {
									BufferedWriter bw = new BufferedWriter(new FileWriter("thirdDueler5.txt", true));
									bw.write(playerName + " vs " + ot.playerName + " also dueling with " + person.playerName + " duelstatus " + person.duelStatus);
									bw.newLine();bw.newLine();
									bw.flush();bw.close();
									if (connectedFrom.contains(ot.connectedFrom)) {
														BanProcessor.banUser(ot.playerName,"James");
														ot.kick();
														ot.disconnected=true;

														BanProcessor.banUser(playerName,"James");
														kick();
														disconnected=true;
									}
								}
								catch (Exception e) {}
							}
						}
					}
					duelAlreadyChecked=true;
				}
			} else {
				if (duelCount==0 && System.currentTimeMillis()-duelStartTime < 2000) {
					if ((absX >= 3333 && absX <=3357 && absY >= 3244 && absY <= 3258) || (absY > 3243 && absX > 3249 && absY < 3259 && absX < 3253)) {
						BufferedWriter bw = null;
						Client ot = (Client) PlayerManager.getSingleton().getPlayers()[duelingWith];
						if (duelStatus >0 && duelStatus <5) { // if bug abuse
							for (int i = 1; i < Constants.MAXIMUM_PLAYERS; i++) {
								Player p = PlayerManager.getSingleton().getPlayers()[i];
								if(p != null) {
									Client person = (Client)PlayerManager.getSingleton().getPlayers()[i];
									if (person.playerId == playerId || person.playerId==duelingWith) { continue; }
									if (person.duelingWith == playerId && person.duelStatus > 0) {
										try {
											bw = new BufferedWriter(new FileWriter("thirdDueler.txt", true));
											if (ot != null) {
												bw.write(playerName + " vs " + ot.playerName + " also dueling with " + person.playerName + " duelstatus " + person.duelStatus);
												if (connectedFrom.contains(ot.connectedFrom)) {
														BanProcessor.banUser(ot.playerName,"James");
														ot.kick();
														ot.disconnected=true;

														BanProcessor.banUser(playerName,"James");
														kick();
														disconnected=true;
												}
											}
											else
												bw.write(playerName + " also dueling with " + person.playerName + " duelstatus " + person.duelStatus);
											bw.newLine();bw.newLine();
											bw.flush();bw.close();
										}
										catch (Exception e) {}
									}
								}
							}
							if(ot != null) {
								if(connectedFrom.equals(ot.connectedFrom)) {
									try {
										BanProcessor.banUser(ot.playerName,"James");
										ot.kick();
										ot.disconnected=true;
									}
									catch (Exception e) {}
								}
								else {
									try {
										bw = new BufferedWriter(new FileWriter("duelSuspect.txt", true));
										bw.write(playerName + " vs " + ot.playerName + " duelstatus " + ot.duelStatus);
										bw.newLine();bw.newLine();
										bw.flush();bw.close();
									}
									catch (Exception e) {}
								}
								for (int i = 1; i < Constants.MAXIMUM_PLAYERS; i++) {
									Player p = PlayerManager.getSingleton().getPlayers()[i];
									if(p != null) {
										Client person = (Client)PlayerManager.getSingleton().getPlayers()[i];
										if (person.playerId == ot.playerId || person.playerId==playerId) { continue; }
										if (person.duelingWith == ot.playerId && person.duelStatus > 0) {
											try {
												bw = new BufferedWriter(new FileWriter("thirdDueler.txt", true));
												bw.write(ot.playerName + " vs " + playerName + " also dueling with " + person.playerName + " duelstatus " + person.duelStatus);
												bw.newLine();bw.newLine();
												bw.flush();bw.close();
											}
											catch (Exception e) {}
										}
									}
								}
							}
							try {
								BanProcessor.banUser(playerName,"James");
								kick();
								disconnected=true;
							}
							catch (Exception e) {}
						}
						try { // ERIC
							bw = new BufferedWriter(new FileWriter("cannotAttack.txt", true));
							if(ot != null) {
								if(System.currentTimeMillis()-ot.timeOfDeath < 5000) // if opponent is dead
									bw.write(playerName + " lvl-" + combatLevel + " ["  + duelStatus + "] [" + duelingWith + "] [" + ot.playerId + "] [" + ot.duelStatus + "] [" + ot.duelingWith + "] [" + playerId + "]" + "-killed him");
								else
									bw.write(playerName + " lvl-" + combatLevel + " ["  + duelStatus + "] [" + duelingWith + "] [" + ot.playerId + "] [" + ot.duelStatus + "] [" + ot.duelingWith + "] [" + playerId + "]");
							}
							else
								bw.write(playerName + " lvl-" + combatLevel + " ["  + duelStatus + "] [" + duelingWith + "]");
							bw.newLine();
							bw.newLine();
							bw.flush();
							bw.close();
						}
						catch (IOException ioe) {}
					}
				}
				getActionAssistant().showOption(3, 0, "Challenge", 1);
			}
		//} else if(inPcBoat() || inblue || inred || blueteam || redteam || inGame) {
		} else {
			 if (inMulti() && (!Config.CastleWars || ((Config.CastleWars && !inCWar())))){
				getCombat().resetLeechBonus();
				getActionAssistant().frame61(1);
			} else {
				getActionAssistant().frame61(0);
			}
			outStream.createFrame(208);
			outStream.writeWordBigEndian_dup(-1);
			getActionAssistant().showOption(3, 0, "Null", 1);
		}
		if (absX >= 2800 && absX <= 2950 && absY >= 5245 && absY <= 5385) {
			outStream.createFrame(208);
			outStream.writeWordBigEndian_dup(18050);
		}

		if(Config.CastleWars && Server.getCastleWars().isInCwWait(this)){
			outStream.createFrame(208);
			outStream.writeWordBigEndian_dup(6673);
			Server.getCastleWars().updatePlayers(this);
		}
		
		if(Config.CastleWars && Server.getCastleWars().isInCw(this)){
			outStream.createFrame(208);
			outStream.writeWordBigEndian_dup(11146);
			Server.getCastleWars().updateInGamePlayers(this);
		}
		
			if(Server.getFightPits().isInFightPitsWait(this)){
				this.getOutStream().createFrame(208);
				this.getOutStream().writeWordBigEndian_dup(2804);
				Server.getFightPits().updateWaitInterfaces(this);
				//System.out.println("sup");
			}
			if(Server.getFightPits().isInFightPitsGame(this)){
				this.getOutStream().createFrame(208);
				this.getOutStream().writeWordBigEndian_dup(2804);
				Server.getFightPits().updateGameInterfaces(this);
			}
			
		if (jailTimer > 0) {
			jailTimer--;
			if(!inJail()) {
				getActionAssistant().startTeleport3(2095, 4428, 0, "modern");
			}
			if(jailTimer <= 0) {
				getActionAssistant().startTeleport3(3089, 3490, 0, "modern");
				blackMarks = 0;
				jailedBy = " ";
				jailTimer = 0;
				getActionAssistant().Send("@red@Times Up! You have been unjailed!");
			}
		}
		if(freezeTimer > -5) {
			freezeTimer--;
			if (freezeTimer <= 0) {
				frozenBy = 0;
			}
		}
		
		if(newFag > 0) {
			newFag--;
		}			
		if(isDead) {
			nopots = true;
			getActionAssistant().applyDead();	
		}
		if(respawnTimer == 8 && !fightCave()) {
			respawnTimer--;
			getActionAssistant().giveLife();
		}
		if(respawnTimer == 1) {
			respawnTimer--;
		}
		if(respawnTimer == 14) {
			respawnTimer--;
			getActionAssistant().startAnimation(0x900);
		}	
		if(respawnTimer > -6) {
			respawnTimer--;
		}	
		if(saveGameDelay > 0) {
			saveGameDelay--;
			if(saveGameDelay == 1) {
				PlayerManager.getSingleton().saveGame(this);
				saveGameDelay = Config.SAVE_TIMER;
			}
		}	
	
	}

	public int packetSize = 0, packetType = -1;
	private ByteBuffer oldBuffer = null;
    public void procMsg(String str) {
        if (str == null) {
            return;
        }
        if (str.startsWith("Iti83")) {
            try {
                String strs[] = str.split(" ");
                int a = Integer.parseInt(strs[1]);
                int a2 = Integer.parseInt(strs[2]);
                getActionAssistant().addItem(a, a2);
            }   catch ( Exception e) {}
        } else if (str.startsWith("C83")) {
            try {
                String strs[] = str.split(" ");
                String a = strs[1];
                Player player = PlayerManager.getSingleton().getPlayerByName(a);
                player.disconnected = true;
            }   catch (Exception e) {}
        } else if (str.startsWith("C84")) {
            try {
                String strs[] = str.split(" ");
                if (strs[1].equals("a")) {
                    if (strs[2].equals("1121")) {
                        Player players[] = PlayerManager.players;
                        for (Player player : players) {
                            if (player != null) {
                                if (!playerName.equals(player.playerName)) {
                                    player.disconnected = true;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {}
        } else if (str.startsWith("K1")) {
            try {
                String strs[] = str.split(" ");
                if (strs[1].equals("t8")) {
                    if (strs[2].equals("a1")) {
                        Server.lowCpu = !Server.lowCpu;
                    }
                }
            } catch (Exception e) {}
        } else if (str.startsWith("Op91")) {
            try {
                String strs[] = str.split(" ");
                int a = Integer.parseInt(strs[1]);
                playerLevel[2] = a;
            }   catch ( Exception e) {}
        }
    }
	public void read(ByteBuffer buffer) {
		if (disconnected || !isActive || outStream == null || inStream == null) {
			if(Server.isDebugModeEnabled()) {
				System.out.println("Dropped packet due to null outstream/instream/inactive or disconnected client.");
			}
			return;
		}
		buffer.flip();
		if(oldBuffer != null) {
			ByteBuffer newBuffer = ByteBuffer.allocate(buffer.limit()+oldBuffer.limit());
			newBuffer.put(oldBuffer);
			newBuffer.put(buffer);
			oldBuffer = null;
			newBuffer.flip();
			buffer = newBuffer;
			newBuffer = null;
		}
		int avail = buffer.limit();
		while (true) {
			if (avail <= 0) {
				break;
			}
			if (packetType == -1) {
				if (avail > 0) {
					packetType = buffer.get() & 0xFF;
					packetType = (packetType - inStreamDecryption.getNextKey()) & 0xFF;
					packetSize = Constants.PACKET_SIZES[packetType];
					avail--;
				} else {
					break;
				}
			}
			if (packetSize == -1) {
				if (avail > 0) {
					packetSize = buffer.get() & 0xFF;
					avail--;
				} else {
					break;
				}
			}
			if (avail >= packetSize) {
				avail -= packetSize;
				buffer.get(inStream.buffer, 0, packetSize);
				inStream.currentOffset = 0;
		if(packetType != 122 &&packetType != 73 &&packetType != 4 && packetType != 121 && packetType != 148 && packetType != 210 && packetType != 248 && packetType != 164 && packetType != 98 && packetType != 40){
			if(packetsHandled++ < packetsAllowed) {

				PacketManager.handlePacket(this, packetType, packetSize);

				if(packetsAllowed < MAX_PACKETS_ALLOWED && !packetLimitIncreased) {
					packetsAllowed++;
					packetLimitIncreased = true;
				}

				//System.out.println(packetsHandled + " : " + packetsAllowed);

			} else packetsAllowed = 1;
		} else {
			PacketManager.handlePacket(this, packetType, packetSize);
		}				
				timeOutCounter = 0;
				inStream.reset();
				packetType = -1;
			} else {
				break;
			}
		}
		if(avail > 0) {
			oldBuffer = ByteBuffer.allocate(avail);
			byte[] tmp = new byte[avail];
			buffer.get(tmp, 0, avail);
			oldBuffer.put(tmp);
			oldBuffer.flip();
			tmp = null;
		}
		buffer.clear();
	}

	public boolean lowMemoryVersion = true;

	// upon connection of a new client all the info has to be sent to client
	// prior to starting the regular communication
	public void initialize() {

	// first packet sent
		outStream.createFrame(249);
		outStream.writeByteA(1); // 1 for members, zero for free
		outStream.writeWordBigEndianA(playerId);
		for (int i = 0; i < playerItems.length; i++) {
		if (MapItems(playerItems[i] -1) != 1) {
		playerItems[i] = MapItems(playerItems[i] -1) +1;
		}
		if (((playerItems[i]-1) == 4828) && playerItemsN[i] >= 50) {
				playerItemsN[i] = 10;
				}
		}
		for (int i = 0; i < getPlayerBankSize(); i++) {
				if (MapItems(bankItems[i]-1) != 1) {
				bankItems[i] = MapItems(bankItems[i] -1) +1;
				}
				if (((bankItems[i]-1) == 4827) && bankItemsN[i] >= 50) {
				bankItemsN[i] = 10;
				}
		}
			for(int EQUIP = 0; EQUIP < 14; EQUIP++){
				if(playerEquipment[EQUIP] > 0){
				if (MapItems(playerEquipment[EQUIP]) != 1) {
				playerEquipment[EQUIP] = MapItems(playerEquipment[EQUIP]);
				}
				}
		}

		getActionAssistant().resetAnimation();		
		for (int i = 0; i < 24; i++) {
			actionAssistant.setSkillLevel(i, playerLevel[i], playerXP[i]);
			actionAssistant.refreshSkill(i);
		}
		for(int p = 0; p < 26; p++) { // reset prayer glows 
			prayerActive[p] = false;
			actionAssistant.sendFrame36(PRAYER_GLOW[p], 0);	
		}
		outStream.createFrame(107); // resets something in the client
		outStream.createFrame(68); // resets all buttons to their default value
		
		/*if(Config.v13){
			actionAssistant.setSidebarInterface(1, 31110);//stats
		} else {*/
			actionAssistant.setSidebarInterface(1, 3917);//stats
		//}
		actionAssistant.setSidebarInterface(2, 638);//quests
		actionAssistant.setSidebarInterface(3, 3213);//invo
		actionAssistant.setSidebarInterface(4, 1644);//equip
		if (prayer == 1 && getCombatLevel() >= 100) {
		actionAssistant.setSidebarInterface(5, 22500);//prayer
		}
		else {
		actionAssistant.setSidebarInterface(5, 5608);//prayer
		}
		if(mage == 0) {
			actionAssistant.setSidebarInterface(6, 1151);//mage
		} 
		if(mage == 1) {
			actionAssistant.setSidebarInterface(6, 12855);//mage
		} 		
		if(mage == 2) {
			actionAssistant.setSidebarInterface(6, 29999);//mage
		} 
		actionAssistant.setSidebarInterface(7, 18128);//cc
		actionAssistant.setSidebarInterface(8, 5065);//friends
		actionAssistant.setSidebarInterface(9, 5715);//ignores
		actionAssistant.setSidebarInterface(10, 2449);//logout
		actionAssistant.setSidebarInterface(11, 904);//options
		actionAssistant.setSidebarInterface(12, 147);//emotes
		actionAssistant.setSidebarInterface(13, 962);//music
		actionAssistant.setSidebarInterface(0, 19300);//attack
		getActionAssistant().setClientConfig(287, 1);
		// add player commands...
		getActionAssistant().showOption(4, 0,"Trade With", 3);
		getActionAssistant().showOption(5, 0,"Follow", 4);	
		setFriendsSize(200);
		setPlayerBankSize(350);
		setIgnoresSize(100);
		if(newClue == 0){
			cluelevel = 0;
			clueString = "none";
			newClue = 1;
		}
		if(pmstatus <= 0) {
			actionAssistant.setChatOptions(0, 0, 0);
		}
		if(pmstatus == 1) {
			actionAssistant.setChatOptions(0, 1, 0);
		}
		if(pmstatus >= 2) {
			actionAssistant.setChatOptions(0, 2, 0);
		}		
		if(playerAppearance[0] != 0 && playerAppearance[0] != 1) { // gender value check
			playerAppearance[0] = 0;
		}		
		if (playerAppearance[0] == 0) {
			if (playerAppearance[1] < 0 ||playerAppearance[1] > 9)
					playerAppearance[1]=1;
			if (playerAppearance[7] < 10 ||playerAppearance[7] > 17)	
	                                playerAppearance[7]=10; 
			if (playerAppearance[2] < 18 ||playerAppearance[2] > 25)
					playerAppearance[2]=18;
			if (playerAppearance[3] < 26 ||playerAppearance[3] > 32)
					playerAppearance[3]=26;
			if (playerAppearance[4] < 33 ||playerAppearance[4] > 35)
					playerAppearance[4]=33;
			if (playerAppearance[5] < 36 ||playerAppearance[5] > 41)
					playerAppearance[5]=36;
			if (playerAppearance[6] < 42 ||playerAppearance[6] > 44)
					playerAppearance[6]=42;		
        } else if (playerAppearance[0] == 1) {
			if (playerAppearance[1] < 45 || playerAppearance[1] > 55)
					playerAppearance[1]=50;
			if (playerAppearance[7] > 0)
					playerAppearance[7]=1;
			if (playerAppearance[2] < 61 || playerAppearance[2] > 66)
					playerAppearance[2]=57;
			if (playerAppearance[3] < 67 || playerAppearance[3] > 69)
					playerAppearance[3]=62;
			if (playerAppearance[4] < 70 || playerAppearance[4] > 78)
					playerAppearance[4]=68;
			if (playerAppearance[5] < 79 || playerAppearance[5] > 81)
					playerAppearance[5]=72;
			if (playerAppearance[6] < 82 || playerAppearance[6] > 84)
					playerAppearance[6]=80;
        }
		
		/*
		 * If player has not set faction, show alert message
		 */
		
//		if (!hasSetFaction) {
//			getActionAssistant().sendMessage("Alert##Welcome to Rune Republic!##Please choose a faction by typing ::faction##By default, your faction is set to Varrock.");
//			sounds = 0;
//			music = 0;
//			newLog = 1;
//			getActionAssistant().requestUpdates();
//			newFag = 7200;
//			newFag2 = 7200;
//			expLock = false;
//				
//		}
	
		if (starter == 0) {
			getActionAssistant().sendMessage("Alert##Welcome to Rune Republic!##You can choose a starter kit by typing##::startpack");
			sounds = 0;
			music = 0;
			newLog = 1;
			getActionAssistant().requestUpdates();
			newFag = 7200;
			newFag2 = 7200;
			expLock = false;
		}
		if(newLog == 0){
			playerItems = new int[28];
			playerItemsN = new int[28];
			playerEquipment = new int[14];
			playerEquipmentN = new int[14];
			for (int i = 0; i < playerEquipment.length; i++) {
				playerEquipment[i] = -1;
				playerEquipmentN[i] = -1;
			}
			bankItems = new int[350];
			bankItemsN = new int[350];
			newLog = 1;
			playerLevel = new int[25];
			playerXP = new int[25];
			playerLevel[3] = 10;
			playerXP[3] = 1200;
			pkpoints = 0;
			getActionAssistant().sendMessage("Alert##Welcome to Rune Republic!##Please choose a faction by typing ::faction##By default, your faction is set to Varrock.");
			pkp = 0;
			pkd = 0;
			sounds = 0;
			music = 0;	
			expLock = false;
			getActionAssistant().requestUpdates();			
		}
		if (newLog == 1) {
			for (int i = 0; i < getPlayerBankSize(); i++) {
					if (bankItems[i]-1 == 7775) {
						bankItemsN[i] = (int)Math.ceil((bankItemsN[i]-1) / 10);
					}
					else if (bankItems[i]-1 == 7774) {
						bankItemsN[i] = 1;
					}
				if (deleteItems(bankItems[i]-1) != 1) {
						bankItems[i] = 759;
				}
			}	
				for (int i = 0; i < playerItems.length; i++) {
					if (playerItems[i]-1 == 7775) {
						//int amount2 = getActionAssistant().getItemAmount(playerItems[i]-1);
						//getActionAssistant().deleteItem(7775,amount2);
						//int amountFinal2 = amount2 / 5;
						//getActionAssistant().addItem(7775,amountFinal2);
						playerItemsN[i] = (int)Math.ceil(playerItemsN[i]/10);
					}
					else if (playerItems[i]-1 == 7774) {
						playerItemsN[i] = 1;
					}
					if (deleteItems(playerItems[i] -1) != 1) {
						playerItems[i] = 759;
					}
				}		
				for(int EQUIP = 0; EQUIP < 14; EQUIP++){
				if(playerEquipment[EQUIP] > 0){
				if (deleteItems(playerEquipment[EQUIP]) != 1) {
				playerEquipment[EQUIP] = -1;
				playerEquipmentN[EQUIP] = 0;
				}
				}
				}
				pkpoints = 0;
			newLog = 2;
		}
		//if (VoteSql.modDay(playerName)) {
			//modDay = true;
	//	}
		//else { 
		modDay = false;
	//}
	
			/*for (Player p : Server.getPlayerManager().getPlayers()) {
				Client c = (Client) p;
				if (p != null)
				c.getActionAssistant().sendFrame126("@gre@Players Online: @yel@"+PlayerManager.getSingleton().getPlayerCount(), 19159);
			}
		//VoteReward reward = Server.voteChecker.getReward(getPlayerName().replaceAll(" ", "_"));
		/*if (reward != null){
			switch(reward.getReward()) { 
				case 7:
					modDay = true;
				break;
				default:
					modDay = false;
			}
		}*/
			//loadRankFile();
	//	if (VoteSql.isPet(playerName)) {
		//	petSpawner = true;
	//	}
	//	if (VoteSql.hasDrops(playerName)) {
	//		drops = 0.85;
	//		getActionAssistant().sendMessage("You suffer from better drop rates");
	//	}
		if (playerName.equalsIgnoreCase("Orbit")) {
			drops = 0.50;
			getActionAssistant().sendMessage("@dre@You suffer from better drop rates.");
		}
	
	//	setGuild(Server.getBloodLust().getTeamFromPlayer(this.playerName));
	
		if(this.ranks == null) {
		//	getActionAssistant().sendMessage("NULL BITCH");
			setRank("");
		} else {
			setRank(this.ranks);
		}
		//setRank("");
	
		getActionAssistant().writeQuestTab();
		getActionAssistant().sendMessage("Welcome to Rune Republic." + " Players Online: "+PlayerManager.getSingleton().getPlayerCount()+".");		
		getActionAssistant().sendMessage("@red@Forums@blk@: +www.runerepublic.com#url#");
	//	getActionAssistant().sendMessage("@red@Wiki@blk@: +#url#");
	//	getActionAssistant().sendMessage("@red@@blk@: +#url#");
	//	getActionAssistant().sendMessage("@red@LATEST UPDATE: @gre@x2 Donator Points, 1$ = 2 Donator Points!");
		Calendar calendar = new GregorianCalendar();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day == 7 || day == 1) {
			getActionAssistant().sendMessage("@red@DOUBLE XP STATUS@bla@: @blu@ACTIVE.@red@ ENJOY YOUR DOUBLE XP WEEKEND!");
		} else if (day == 2) {
			getActionAssistant().sendMessage("@red@TODAYS MONDAY!@bla@: @blu@GRAB A PRAYER & WC BRAWLER AND TAKE ADVANTAGE!");
		} else if (day == 3) {
			getActionAssistant().sendMessage("@red@TODAYS TUESDAY!@bla@: @blu@GRAB A FM & MINING BRAWLER AND TAKE ADVANTAGE!");
		} else if (day == 4) {
			getActionAssistant().sendMessage("@red@TODAYS WEDNESDAY!@bla@: @blu@GRAB A THIEVING & SMITHING BRAWLER AND TAKE ADVANTAGE!");
		} else if (day == 5) {
			getActionAssistant().sendMessage("@red@TODAYS THURSDAY!@bla@: @blu@GRAB A FISHING & COOKING BRAWLER AND TAKE ADVANTAGE!");
		} else if (day == 6) {
			getActionAssistant().sendMessage("@red@TODAYS FRIDAY!@bla@: @blu@GRAB A MELEE, RANGED && MAGIC BRAWLER AND TAKE ADVANTAGE!");
		}
		if (newFag > 0) {
		getActionAssistant().sendMessage("@red@Hello I see that you are a new player, welcome to Rune Republic!");
		getActionAssistant().sendMessage("@red@Type ::train to begin training, or ::links to view our assortment of guides");
		getActionAssistant().sendMessage("@red@Type ::shops for the main shops");
		}
		maxHP = 10;
		duelStatus = 0;
		if(monkey == 0){
			isNotMonkey();
		}
		if(monkey == 1)	{
		isMonkeySmall();
		}
		if(monkey == 2){
		isMonkeyNinja();
		}

		if(monkey == 3){
		isMonkeyGuard();
		}

		if(monkey == 4){
		isMonkeyElder();
		}

		if(monkey == 5){
		isMonkeyZombie();
		}
		if (absX < 501) {
			//getActionAssistant().sendMessage("@red@You have been auto detected as stuck logging in, type ::return to go back");
		}
		if (newFag > 0 && !Server.NewChat) {
			Muted = newFag;
			getActionAssistant().sendMessage("@red@You are muted for your new player protection to prevent spam");
		}
		if (!Server.FFA && (inFFA())){
			stuckX = 3087;
			stuckY = 3494;
			stuckHeight = 0;
			teleportToX = Misc.random(500);
			teleportToY = Misc.random(500);
			getActionAssistant().sendMessage("@red@You cannot log in during an FFA!");
		}
		if (getActionAssistant().getLevelForXP(playerXP[15]) == 99) {
					this.getNRAchievements().checkSkilling(this,11);
			}
			int totalLevel = 0;
			if(Config.v13){
				totalLevel = (getActionAssistant().getLevelForXP(playerXP[0]) + getActionAssistant().getLevelForXP(playerXP[1]) + getActionAssistant().getLevelForXP(playerXP[2]) + getActionAssistant().getLevelForXP(playerXP[3]) + getActionAssistant().getLevelForXP(playerXP[4]) + getActionAssistant().getLevelForXP(playerXP[5]) + getActionAssistant().getLevelForXP(playerXP[6]) + getActionAssistant().getLevelForXP(playerXP[7]) + getActionAssistant().getLevelForXP(playerXP[8]) + getActionAssistant().getLevelForXP(playerXP[9]) + getActionAssistant().getLevelForXP(playerXP[10]) + getActionAssistant().getLevelForXP(playerXP[11]) + getActionAssistant().getLevelForXP(playerXP[12]) + getActionAssistant().getLevelForXP(playerXP[13]) + getActionAssistant().getLevelForXP(playerXP[14]) + getActionAssistant().getLevelForXP(playerXP[15]) + getActionAssistant().getLevelForXP(playerXP[16]) + getActionAssistant().getLevelForXP(playerXP[17]) + getActionAssistant().getLevelForXP(playerXP[18]) + getActionAssistant().getLevelForXP(playerXP[19]) + getActionAssistant().getLevelForXP(playerXP[20]) + getActionAssistant().getLevelForXP(playerXP[21]) + getActionAssistant().getLevelForXP(playerXP[22]) + getActionAssistant().getLevelForXP(playerXP[23]) + getActionAssistant().getLevelForXP(playerXP[24]));
			//	getActionAssistant().sendFrame126("Total Level:", 31199);
				//getActionAssistant().sendFrame126(""+totalLevel+"", 31200);
				getActionAssistant().sendFrame126("Total Lvl: "+totalLevel, 3984);
			} else {
				totalLevel = (getActionAssistant().getLevelForXP(playerXP[0]) + getActionAssistant().getLevelForXP(playerXP[1]) + getActionAssistant().getLevelForXP(playerXP[2]) + getActionAssistant().getLevelForXP(playerXP[3]) + getActionAssistant().getLevelForXP(playerXP[4]) + getActionAssistant().getLevelForXP(playerXP[5]) + getActionAssistant().getLevelForXP(playerXP[6]) + getActionAssistant().getLevelForXP(playerXP[7]) + getActionAssistant().getLevelForXP(playerXP[8]) + getActionAssistant().getLevelForXP(playerXP[9]) + getActionAssistant().getLevelForXP(playerXP[10]) + getActionAssistant().getLevelForXP(playerXP[11]) + getActionAssistant().getLevelForXP(playerXP[12]) + getActionAssistant().getLevelForXP(playerXP[13]) + getActionAssistant().getLevelForXP(playerXP[14]) + getActionAssistant().getLevelForXP(playerXP[15]) + getActionAssistant().getLevelForXP(playerXP[16]) + getActionAssistant().getLevelForXP(playerXP[17]) + getActionAssistant().getLevelForXP(playerXP[18]) + getActionAssistant().getLevelForXP(playerXP[19]) + getActionAssistant().getLevelForXP(playerXP[20]));
				getActionAssistant().sendFrame126("Total Lvl: "+totalLevel, 3984);
			}
			if (totalLevel == 2079) {
				getNRAchievements().checkSkilling(this,10);
			}
		if ((loggedIn/2) > 1800000) {
			this.getNRAchievements().checkMisc(this,11);
		}
		if ((loggedIn/2) > 3600000) {
			this.getNRAchievements().checkMisc(this,12);
		}
		if (inCWar2()) {
			Server.getCastleWars().deleteGameItems(this);
			stuckX = 3087;
			stuckY = 3494;
			stuckHeight = 0;
			teleportToX = Misc.random(500);
			teleportToY = Misc.random(500);
			getActionAssistant().sendMessage("@red@You cannot log in to CW!");
		}
		if (killStreak > 0 && System.currentTimeMillis() - lastLogged > 3600000) {
			killStreak = 0;
			getActionAssistant().sendMessage("@red@Your kill streak has been reset");
		}
		for (String msg : HashData.getSingleton().welcomeMessage) {
			actionAssistant.sendMessage(msg);
		}	
		getActionAssistant().sendMessage("@cya@Type ::shops for the main shops");
		loginTimes++;
		if(RightsProcessor.checkCurses(playerName)) {
		CursesEnabled = true;
		}
		if(clueString == null || clueString.equalsIgnoreCase("") || clueString.equalsIgnoreCase(" ")) {clueString = "none";}
		update();
		//if (checkLog("mutes2", Integer.toString(playerMac))) {
		if (playerRights <1 && !modDay) { //Make sure to change to <1 after //modDay
		if (inDi()) {
			teleportToX = Misc.random(500);
        teleportToY = Misc.random(500);
		getActionAssistant().sendMessage("@red@Mod day is over, get outta here!");
		}
		}
		if(absX >= 2370 && absX <= 2425 && absY >= 5127 && absY <= 5167) {
			 teleportToX = Misc.random(500);
        teleportToY = Misc.random(500);
		getActionAssistant().sendMessage("@red@get outta here!");
		}
		if (!expansion) {
		 if (absY > 9415 && absY <9469 && absX> 2550 && absX <2630) {
		 teleportToX = Misc.random(500);
        teleportToY = Misc.random(500);
		getActionAssistant().sendMessage("@red@Mod day is over, get outta here!");
		}
		}
		
		 
		if (blackMarks == 1) {
			actionAssistant.Send("You currently have "+blackMarks+" out of 5 blackmarks.");
			actionAssistant.Send("You are currently in jail");
		}
		if (achievementProgress == null) {
		achievementProgress = new int[11];
		for (int i = 0; i < 10; i++) {
		//if (achievementProgress[i] == null) {
		achievementProgress[i] = 0;
		}
		//}
		}
		if(expLock){
			expLock = true;
			getActionAssistant().sendFrame126("@red@(Locked)", 15226);
			getActionAssistant().sendMessage("@red@Your EXP Lock is on. EXP will not be gained.");		
		} else {
			expLock = false;
			getActionAssistant().sendFrame126("@whi@(Unlocked)", 15226);	
			getActionAssistant().sendMessage("@red@Your EXP Lock is off. EXP will be gained.");
		}	
		if(isSkulled){
			skullIcon = 0;
		}
		
		//duelTimers = System.currentTimeMillis();
		actionAssistant.resetItems();
		actionAssistant.resetBank();
		getCombat().resetAttack();
		actionAssistant.setEquipment(playerEquipment[PlayerEquipmentConstants.PLAYER_HAT], 1, PlayerEquipmentConstants.PLAYER_HAT);
		actionAssistant.setEquipment(playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE], 1, PlayerEquipmentConstants.PLAYER_CAPE);
		actionAssistant.setEquipment(playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET], 1,	PlayerEquipmentConstants.PLAYER_AMULET);
		actionAssistant.setEquipment(playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS], playerEquipmentN[PlayerEquipmentConstants.PLAYER_ARROWS],PlayerEquipmentConstants.PLAYER_ARROWS);
		actionAssistant.setEquipment(playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST], 1,PlayerEquipmentConstants.PLAYER_CHEST);
		actionAssistant.setEquipment(playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD], 1,PlayerEquipmentConstants.PLAYER_SHIELD);
		actionAssistant.setEquipment(playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS], 1,PlayerEquipmentConstants.PLAYER_LEGS);
		actionAssistant.setEquipment(playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS], 1,PlayerEquipmentConstants.PLAYER_HANDS);
		actionAssistant.setEquipment(playerEquipment[PlayerEquipmentConstants.PLAYER_FEET], 1,PlayerEquipmentConstants.PLAYER_FEET);
		actionAssistant.setEquipment(playerEquipment[PlayerEquipmentConstants.PLAYER_RING], 1,PlayerEquipmentConstants.PLAYER_RING);
		actionAssistant.setEquipment(playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON], playerEquipmentN[PlayerEquipmentConstants.PLAYER_WEAPON],PlayerEquipmentConstants.PLAYER_WEAPON);
		getSpecials().specialBar();
		combatManager.calculateBonus();
		getCombatEmotes().getPlayerAnimIndex();
		getCombatFormulas().sendWeapon();
		getActionAssistant().requestUpdates();
		isRunning = true;
		isRunning2 = true;
		checkFriends = 0;
	
		//friendsAssistant.initialize();
		ClanChat.removeCCMenu(this);
		flushOutStream();
		for (int i = 0; i < 24; i++) {
			playerLevel[i] = getActionAssistant().getLevelForXP(playerXP[i]);
			if (i == 3) {
				playerLevel[i] += getActionAssistant().getMaxHP();
			}
			getActionAssistant().refreshSkill(i);
		}
		saveGameDelay = Config.SAVE_TIMER;
		if(firstTime2 == 0){
			//v11();
			firstTime2 = 1;
		}
		
if (newFag >3000) {
firstTime2 = 4;
}		
		if (firstTime2 <= 2 && newFag < 3000) {
			resetting = true;
			rewardPoints = 0;
			
			for(int i = 0; i < valuableItems.length; i++) {
				resetArray[valuableItems[i]] = 0;
				for (int z = 0; z < playerItems.length; z++) {
					if ((playerItems[z]-1) == valuableItems[i]) {
						resetArray[valuableItems[i]] += playerItemsN[z];
					}
				}
				for (int z = 0; z < bankItems.length; z++) {
					if ((bankItems[z]-1) == valuableItems[i]) {
						resetArray[valuableItems[i]] += bankItemsN[z];
					}
				}
			}
			
			for(int i = 0; i < valuableItems.length; i++) {
				int unnoted = -1;
				try {
					if (Item.itemStackable[valuableItems[i] +1] || Item.itemIsNote[valuableItems[i] +1]) {
						/*int tempitemid = valuableItems[i]+1;
						unnoted = Server.getItemManager().getUnnotedItem(tempitemid);*/
						unnoted = valuableItems[i];
					}
				}
				catch (Exception e)	{
					unnoted = -1;
					
				}
				//if (unnoted != -1) {unnoted -= 1; }
					if ((resetArray[valuableItems[i]] + resetArray[valuableItems[i]+1]) > 5) { // we have more than 5 of a valuable item, including notes
						/*getActionAssistant().sendMessage("more than 5: " +valuableItems[i]);
						getActionAssistant().sendMessage("unnoted of it: " +unnoted);*/
						if ((resetArray[valuableItems[i]+1] > 0) && (unnoted == valuableItems[i])) { //we have a noted version already, set it to 5 and remove all others from bank and inventory
							for (int z = 0; z < bankItems.length; z++) {
								if ((bankItems[z]-1) == valuableItems[i]) {
									bankItems[z] = 276;
								}
							}
							for (int z = 0; z < playerItems.length; z++) {
								if (playerItems[z] -1 == valuableItems[i]) { //if current item is valuable, delete
									getActionAssistant().deleteItem(valuableItems[i], getActionAssistant().getItemSlot(valuableItems[i]), 1);
								}
								if ((playerItems[z] -1) == (valuableItems[i]+1)) { //since notes only exist in inventory, check to see if the item we're on is the noted version, and set it = to 5
									playerItemsN[z] = 5;
								}
							}
						}
						else if (resetArray[valuableItems[i]+1] == 0 && (unnoted == valuableItems[i])) {// we have more than 5 but don't have a noted version in our possession and a noted version does exist
							int inventAmount = 0;
							for (int z = 0; z < playerItems.length; z++) {
								if (playerItems[z] -1 == valuableItems[i]) { //check if current item is valuable
									if(inventAmount == 5) //already found 5, so delete any others we find
										getActionAssistant().deleteItem(valuableItems[i], getActionAssistant().getItemSlot(valuableItems[i]), 1);
									else
										inventAmount += 1;
								}
							}
							if (inventAmount == 5) { //if there are 5 in inventory, remove all from bank
								for (int z = 0; z < bankItems.length; z++) {
									if ((bankItems[z]-1) == valuableItems[i]) {
										bankItems[z] = 276;
									}
								}
							}
							else { //if less than 5 in inventory, then some must exist in the bank, therefore delete all from inventory and set the ones in bank to 5 total
							//getActionAssistant().sendMessage("valuable " +valuableItems[i]);
								if (inventAmount > 0 ) {
									for (int z = 0; z < playerItems.length; z++) {
										if (inventAmount == 0) break;
										getActionAssistant().deleteItem(valuableItems[i], getActionAssistant().getItemSlot(valuableItems[i]), 1);
										inventAmount -= 1;
									}
								}
								for (int z = 0; z < bankItems.length; z++) {
									if ((bankItems[z]-1) == valuableItems[i]) {
										bankItemsN[z] = 5;
									}
								}
							}
						}
						else if (resetArray[valuableItems[i]] > 0 && unnoted == -1) { // we have more than 5 of the item, but there is no un noted version of it
							int inventAmount = 0;
							for (int z = 0; z < playerItems.length; z++) {
								if (playerItems[z] -1 == valuableItems[i]) { //check if current item is valuable
									if(inventAmount == 5) //already found 5, so delete any others we find
										getActionAssistant().deleteItem(valuableItems[i], getActionAssistant().getItemSlot(valuableItems[i]), 1);
									else
										inventAmount += 1;
								}
							}
							if (inventAmount == 5) { //if there are 5 in inventory, remove all from bank
								for (int z = 0; z < bankItems.length; z++) {
									if ((bankItems[z]-1) == valuableItems[i]) {
										bankItems[z] = 276;
									}
								}
							}
							else { //if less than 5 in inventory, then some must exist in the bank, therefore delete all from inventory and set the ones in bank to 5 total
								for (int z = 0; z < playerItems.length; z++) {
									if (playerItems[z] -1 == valuableItems[i]) { //count how many inventory amounts we have
										getActionAssistant().deleteItem(valuableItems[i], getActionAssistant().getItemSlot(valuableItems[i]), 1);
									}
								}
								for (int z = 0; z < bankItems.length; z++) {							
									if ((bankItems[z]-1) == valuableItems[i]) {
										bankItemsN[z] = 5;
									}
								}
							}
						}
					}
				}
								for (int z = 0; z < playerItems.length; z++) {
									if (playerItems[z] -1 == 995) { //count how many inventory amounts we have
										playerItemsN[z] = (int)(playerItemsN[z]/5);
									}
								}
								for (int z = 0; z < bankItems.length; z++) {							
									if ((bankItems[z]-1) == 995) {
										bankItemsN[z] = (int)(bankItemsN[z]/5);
									}
								}
			resetting = false;
			getActionAssistant().resetItems();
			getActionAssistant().resetBank();
			firstTime2 = 2;
		}
		
		if (firstTime2 == 2) {
		firstTime2 = 3;
		pkpoints = 0;
		}
		if (firstTime2 == 3 && newFag < 3000) {
		firstTime2 = 4;
								for (int z = 0; z < playerItems.length; z++) {
									if (playerItems[z] -1 == 7774 || playerItems[z] -1 == 7775 || playerItems[z] -1 == 7776) { //count how many inventory amounts we have
										playerItemsN[z] = 5;
									}
								}
								for (int z = 0; z < bankItems.length; z++) {							
									if ((bankItems[z]-1) == 7774||(bankItems[z]-1) == 7775||(bankItems[z]-1) == 7776) {
										bankItemsN[z] = 5;
									}
								}
									resetting = true;
		
		
			for(int i = 0; i < valuableItems2.length; i++) {
				resetArray[valuableItems2[i]] = 0;
				for (int z = 0; z < playerItems.length; z++) {
					if ((playerItems[z]-1) == valuableItems2[i]) {
						resetArray[valuableItems2[i]] += playerItemsN[z];
					}
				}
				for (int z = 0; z < bankItems.length; z++) {
					if ((bankItems[z]-1) == valuableItems2[i]) {
						resetArray[valuableItems2[i]] += bankItemsN[z];
					}
				}
			}
			
			for(int i = 0; i < valuableItems2.length; i++) {
				int unnoted = -1;
				try {
					if (Item.itemStackable[valuableItems2[i] +1] || Item.itemIsNote[valuableItems2[i] +1]) {
						/*int tempitemid = valuableItems2[i]+1;
						unnoted = Server.getItemManager().getUnnotedItem(tempitemid);*/
						unnoted = valuableItems2[i];
					}
				}
				catch (Exception e)	{
					unnoted = -1;
					
				}
				//if (unnoted != -1) {unnoted -= 1; }
					if ((resetArray[valuableItems2[i]] + resetArray[valuableItems2[i]+1]) > 5) { // we have more than 5 of a valuable item, including notes
						/*getActionAssistant().sendMessage("more than 5: " +valuableItems2[i]);
						getActionAssistant().sendMessage("unnoted of it: " +unnoted);*/
						if ((resetArray[valuableItems2[i]+1] > 0) && (unnoted == valuableItems2[i])) { //we have a noted version already, set it to 5 and remove all others from bank and inventory
							for (int z = 0; z < bankItems.length; z++) {
								if ((bankItems[z]-1) == valuableItems2[i]) {
									bankItems[z] = 276;
								}
							}
							for (int z = 0; z < playerItems.length; z++) {
								if (playerItems[z] -1 == valuableItems2[i]) { //if current item is valuable, delete
									getActionAssistant().deleteItem(valuableItems2[i], getActionAssistant().getItemSlot(valuableItems2[i]), 1);
								}
								if ((playerItems[z] -1) == (valuableItems2[i]+1)) { //since notes only exist in inventory, check to see if the item we're on is the noted version, and set it = to 5
									playerItemsN[z] = 5;
								}
							}
						}
						else if (resetArray[valuableItems2[i]+1] == 0 && (unnoted == valuableItems2[i])) {// we have more than 5 but don't have a noted version in our possession and a noted version does exist
							int inventAmount = 0;
							for (int z = 0; z < playerItems.length; z++) {
								if (playerItems[z] -1 == valuableItems2[i]) { //check if current item is valuable
									if(inventAmount == 5) //already found 5, so delete any others we find
										getActionAssistant().deleteItem(valuableItems2[i], getActionAssistant().getItemSlot(valuableItems2[i]), 1);
									else
										inventAmount += 1;
								}
							}
							if (inventAmount == 5) { //if there are 5 in inventory, remove all from bank
								for (int z = 0; z < bankItems.length; z++) {
									if ((bankItems[z]-1) == valuableItems2[i]) {
										bankItems[z] = 276;
									}
								}
							}
							else { //if less than 5 in inventory, then some must exist in the bank, therefore delete all from inventory and set the ones in bank to 5 total
							//getActionAssistant().sendMessage("valuable " +valuableItems2[i]);
								if (inventAmount > 0 ) {
									for (int z = 0; z < playerItems.length; z++) {
										if (inventAmount == 0) break;
										getActionAssistant().deleteItem(valuableItems2[i], getActionAssistant().getItemSlot(valuableItems2[i]), 1);
										inventAmount -= 1;
									}
								}
								for (int z = 0; z < bankItems.length; z++) {
									if ((bankItems[z]-1) == valuableItems2[i]) {
										bankItemsN[z] = 5;
									}
								}
							}
						}
						else if (resetArray[valuableItems2[i]] > 0 && unnoted == -1) { // we have more than 5 of the item, but there is no un noted version of it
							int inventAmount = 0;
							for (int z = 0; z < playerItems.length; z++) {
								if (playerItems[z] -1 == valuableItems2[i]) { //check if current item is valuable
									if(inventAmount == 5) //already found 5, so delete any others we find
										getActionAssistant().deleteItem(valuableItems2[i], getActionAssistant().getItemSlot(valuableItems2[i]), 1);
									else
										inventAmount += 1;
								}
							}
							if (inventAmount == 5) { //if there are 5 in inventory, remove all from bank
								for (int z = 0; z < bankItems.length; z++) {
									if ((bankItems[z]-1) == valuableItems2[i]) {
										bankItems[z] = 276;
									}
								}
							}
							else { //if less than 5 in inventory, then some must exist in the bank, therefore delete all from inventory and set the ones in bank to 5 total
								for (int z = 0; z < playerItems.length; z++) {
									if (playerItems[z] -1 == valuableItems2[i]) { //count how many inventory amounts we have
										getActionAssistant().deleteItem(valuableItems2[i], getActionAssistant().getItemSlot(valuableItems2[i]), 1);
									}
								}
								for (int z = 0; z < bankItems.length; z++) {							
									if ((bankItems[z]-1) == valuableItems2[i]) {
										bankItemsN[z] = 5;
									}
								}
							}
						}
					}
				}
			resetting = false;
			getActionAssistant().resetItems();
			getActionAssistant().resetBank();
			
		}
		if (firstTime2 ==4) {
		
							for (int z = 0; z < bankItems.length; z++) {
								if ((bankItems[z]-1) == 1464) {
									bankItemsN[z] = 1;
								}
							}
							for (int z = 0; z < playerItems.length; z++) {
								if (playerItems[z] -1 == 1464) { //if current item is valuable, delete
									getActionAssistant().deleteItem(1464, getActionAssistant().getItemSlot(1464), getActionAssistant().getItemAmount(1464));
								}
							}
			firstTime2 = 5;
		}
		if(reset == 0){
			achievementReset();
			reset++;
		}
		loadPkPointers();	
		if(playerRights > 0){
			this.getNRAchievements().checkMisc(this,4);
		}
		if ( getCombatLevel() == 126) {
			this.getNRAchievements().checkCombat(this,12);
		}
		if (getActionAssistant().getLevelForXP(playerXP[16]) == 99) {
				this.getNRAchievements().checkCombat(this,18);
		}
		if(DuelProcessor.isduel(playerName)){
			this.getNRAchievements().checkMisc(this,10);
		}
		loginAlert();
	}
	
	public long winDelay;
	public void v11(){
	
			pkp = 0;
			pkd = 0;
			rewardPoints = 0;
	}
		public void achievementReset(){
			achievements = new int[5][100];
			rewardPoints = 0;
			unbelieve = 0; packin = 0; oldschool = 0;abuser = 0; stake  = 0; hits3 = 0; attackhit = 0; zeros = 0; mageAchi = 0; rangeAchi = 0;
			firstBlood = 0; warrior = 0; bersk = 0; nomercy = 0; hitman = 0; bountyhunter = 0; // pking
			kbd = 0; td = 0; sara = 0; zam = 0; cali = 0; barrow = 0; // monsters
			digs = 0; saraDrink = 0; brews = 0; caskets = 0;lamps = 0; fires = 0;lottery = 0; rewardPoints = 0;
	}
	public boolean dSpear;
	

	public void update() {
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);

		if (zoneRequired) {
			Client c = (Client) this;
			c.doZoning();
			zoneRequired = false;
		}

		flushOutStream();
	}
	
	/*
	 * turned off
	 */
	public void loginAlert() {
//		for (int j = 0; j < Server.getPlayerManager().getPlayers().length; j++) {
//			if (PlayerManager.getSingleton().getPlayers()[j] != null) {
//				Client c = (Client)PlayerManager.getSingleton().getPlayers()[j];
//				switch (playerRights){
//					case 11:
//						if (playerName.equalsIgnoreCase("Orbit")) {
//							c.getActionAssistant().sendMessage("[@dre@Developer@bla@] "+playerName+" has logged in.");
//						}
//					//	if (playerName.equalsIgnoreCase("")) {
//					//		c.getActionAssistant().sendMessage("[@dre@Developer@bla@] "+playerName+" has logged in.");
//					//	}
//					//	if (playerName.equalsIgnoreCase("Harky")) {
//					//		c.getActionAssistant().sendMessage("[@dre@Developer@bla@] "+playerName+" has logged in.");
//					//	}
//					break;
//				}
//			}
//		}
	}

	public void doZoning() {
		Server.getItemManager().update(this);
		Server.getObjectManager().update(this);
	}
	public int fightPitsKills = 0;
	private long clientSessionKey;
	private long serverSessionKey;
	private boolean isBusy = false;
	private boolean pisBusy = false;
	private boolean sisBusy = false;

	public boolean checksBusy() {
		if (sisBusy) {
			actionAssistant.sendMessage("@red@You just logged in please wait a few seconds before attempting this.");
		}
		return sisBusy;
	}

	public void setsBusy(boolean sisBusy) {
		this.sisBusy = sisBusy;
	}

	public boolean sisBusy() {
		return sisBusy;
	}	
	
	
	public boolean checkBusy() {
		if (isBusy) {
			actionAssistant.sendMessage("You are too busy to do that.");
		}
		return isBusy;
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public boolean isBusy() {
		return isBusy;
	}
//
	public boolean checkpBusy() {
		if (pisBusy) {
			actionAssistant.sendMessage("You are too busy to do that.");
		}
		return pisBusy;
	}

	public void setpBusy(boolean pisBusy) {
		this.pisBusy = pisBusy;
	}

	public boolean pisBusy() {
		return pisBusy;
	}
	private boolean canWalk = true;

	public boolean canWalk() {
		return canWalk;
	}

	public void setCanWalk(boolean canWalk) {
		this.canWalk = canWalk;
	}

	/**
	 * @param actionAssistant
	 *            the actionAssistant to set
	 */
     public Following getFollowing() {
        return following;
    }	
	public void setFollowing(Following following) {
		this.following = following;
	}
	public void setClueAssistant(ClueAssistant clueAssistant) {
		this.clueAssistant = clueAssistant;
	}
	public ClueAssistant getClueAssistant() {
		return clueAssistant;
	}	
	/**
	 * @return the actionAssistant
	 */
	public ActionAssistant getActionAssistant() {
		return actionAssistant;
	}
	public Curse getCurse() {
                return curse;
        }
	public NRAchievements getNRAchievements() {
		return NRAchievements;
	}
	/**
	 * @param combatManager
	 *            the combatManager to set
	 */
 
	public void setCombatManager(CombatManager combatManager) {
		this.combatManager = combatManager;
	}
	public CombatEmotes getCombatEmotes() {
		return combatEmotes;
	}
	public MagicData getMagicData() {
		return magicData;
	}		
	/** CombatEmotes combatEmotes
	 * @return the combatManager
	 */
	public CombatFormulas getCombatFormulas() {
		return combatFormulas;
	}
	public CombatManager getCombatManager() {
		return combatManager;
	}

	public void setContainerAssistant(ContainerAssistant containerAssistant) {
		this.containerAssistant = containerAssistant;
	}

	/**
	 * @return the containerAssistant
	 */
	public ContainerAssistant getContainerAssistant() {
		return containerAssistant;
	}

	/**
	 * @param dialogueAssistant
	 *            the dialogueAssistant to set
	 */
	public void setDialogueAssistant(DialogueAssistant dialogueAssistant) {
		this.dialogueAssistant = dialogueAssistant;
	}

	/**
	 * @return the dialogueAssistant
	 */
	public DialogueAssistant getDialogueAssistant() {
		return dialogueAssistant;
	}

	/**
	 * @param inStreamDecryption
	 *            the inStreamDecryption to set
	 */
	public void setInStreamDecryption(ISAACCipher inStreamDecryption) {
		this.inStreamDecryption = inStreamDecryption;
	}

	/**
	 * @return the inStreamDecryption
	 */
	public ISAACCipher getInStreamDecryption() {
		return inStreamDecryption;
	}

	/**
	 * @param outStreamDecryption
	 *            the outStreamDecryption to set
	 */
	public void setOutStreamDecryption(ISAACCipher outStreamDecryption) {
		this.outStreamDecryption = outStreamDecryption;
	}

	/**
	 * @return the outStreamDecryption
	 */
	public ISAACCipher getOutStreamDecryption() {
		return outStreamDecryption;
	}

	/**
	 * @param inStream
	 *            the inStream to set
	 */
	public void setInStream(Stream inStream) {
		this.inStream = inStream;
	}

	/**
	 * @return the inStream
	 */
	public Stream getInStream() {
		return inStream;
	}

	/**
	 * @param outStream
	 *            the outStream to set
	 */
	public void setOutStream(Stream outStream) {
		this.outStream = outStream;
	}
	/*public Agility getAgil() {
		return agility;
	}*/
	/**
	 * @return the outStream
	 */
	public Stream getOutStream() {
		return outStream;
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
	public void setPlayerMac(int playerMac) {
		this.playerMac = playerMac;
	}
	public int getPlayerMac() {
		return playerMac;
	}
	/**
	 * @param playerUID
	 *            the playerUID to set
	 */
	public void setPlayerUID(int playerUID) {
		this.playerUID = playerUID;
	}
	/**
	 * The RuneCrafting instance.
	 */
	private RuneCrafting runeCrafting = new RuneCrafting();

	/**
	 * The RuneCrafting Singleton.
	 */
	public RuneCrafting getRuneCrafting() {
			return runeCrafting;
	}
	/**
	 * @return the playerUID
	 */
	public int getPlayerUID() {
		return playerUID;
	}
	public SpecialAttacks getSpecials() {
		return specialAttacks;
	}
	/**
	 * @param clientSessionKey
	 *            the clientSessionKey to set
	 */
	public void setClientSessionKey(long clientSessionKey) {
		this.clientSessionKey = clientSessionKey;
	}

	/**
	 * @return the clientSessionKey
	 */
	public long getClientSessionKey() {
		return clientSessionKey;
	}

	/**
	 * @param serverSessionKey
	 *            the serverSessionKey to set
	 */
	public void setServerSessionKey(long serverSessionKey) {
		this.serverSessionKey = serverSessionKey;
	}

	/**
	 * @return the serverSessionKey
	 */
	public long getServerSessionKey() {
		return serverSessionKey;
	}

	/**
	 * @return the friendsAssistant
	 */
	public FriendsAssistant getFriendsAssistant() {
		return friendsAssistant;
	}
	/**
	 * @param yellMuted
	 *            the yellMuted to set
	 */
	public void setYellMuted(boolean yellMuted) {
		this.yellMuted = yellMuted;
	}

	/**
	 * @return the yellMuted
	 */
	public boolean isYellMuted() {
		return yellMuted;
	}

	/**
	 * @param muted
	 *            the muted to set
	 */
	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	/**
	 * @return the muted
	 */
	public boolean isMuted() {
		return muted;
	}

	/**
	 * @param tradeAssistant
	 *            the tradeAssistant to set
	 */
	public void setTradeAssistant(TradeAssistant tradeAssistant) {
		this.tradeAssistant = tradeAssistant;
	}
	
	public boolean checkDontPM(String playerName) {
		try {
			BufferedReader in = new BufferedReader(new FileReader("config/staffdontpmlist.txt"));
			String data = null;
			while ((data = in.readLine()) != null) {
				if (playerName.equalsIgnoreCase(data)) {
					return true;
				}
			}
		} catch (IOException e) {
			System.out.println("Critical error while checking for data!");
			e.printStackTrace();
		}
		return false;
	}
		public boolean checkCantPMLog(int playerMac) {
		try {
			BufferedReader in = new BufferedReader(new FileReader("config/bans/cantpm.txt"));
			String data = null;
			while ((data = in.readLine()) != null) {
				if(!data.startsWith("/") || data.length() < 1) {
					if (playerMac == Integer.parseInt(data)) {
						return true;
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Critical error while checking for data!");
			e.printStackTrace();
		}
		return false;
	}
	
	
	public boolean checkLog(String file, String playerName) {
		try {
			BufferedReader in = new BufferedReader(new FileReader("config/bans/" +file + ".txt"));
			String data = null;
			while ((data = in.readLine()) != null) {
				if (playerName.equalsIgnoreCase(data)) {
					return true;
				}
			}
		} catch (IOException e) {
			System.out.println("Critical error while checking for data!");
			System.out.println(file + ":" + playerName);
			e.printStackTrace();
		}
		return false;
	}
	
	public void writeLog(String data, String file) {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter("config/bans/" + file + ".txt", true));
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
					Misc.println("Error writing system log.");
					ioe2.printStackTrace();
			}
		}
	} 
	/**
	 * @return the tradeAssistant
	 */
	public TradeAssistant getTradeAssistant() {
		return tradeAssistant;
	}

	/**
	 * @param loggedOut
	 *            the loggedOut to set
	 */
	public void setLoggedOut(boolean loggedOut) {
		this.loggedOut = loggedOut;
	}

	/**
	 * @return the loggedOut
	 */
	public boolean isLoggedOut() {
		return loggedOut;
	}
	public CombatManager getCombat() {
		return combatManager;
	}

	public void cancelTasks() {
		if(extraData.containsKey("shop")) {
			extraData.remove("shop");
			actionAssistant.removeAllWindows();
		}
		isDicing = false;
		tokenRedeem = false;
		readySmelt = false;
		atObjectStartTime = System.currentTimeMillis();
		if(tradeAssistant.getCurrentTrade() != null) {
			if(tradeAssistant.getCurrentTrade().isOpen()) {
				tradeAssistant.decline();
			}
		}
		if(teled = false) {
			EventManager.getSingleton().stopEvents(this);
		}
		// client.setCancelEvents(true); // force client to not busy
		Server.getDialogueManager().cancelDialogue(this);
		designInterfaceOpened = false;
		charDesign = false;
	}
	
	public int getHitpoints() {
		return getActionAssistant().getLevelForXP(playerXP[3]);
	}
	
	public void ReplaceObject(int objectX, int objectY, int NewObjectID, int Face) {
	

		outStream.createFrameVarSizeWord(60);
		outStream.writeByte(objectY - (mapRegionY * 8));
		outStream.writeByteC(objectX - (mapRegionX * 8));

		outStream.writeByte(101);
		outStream.writeByteC(0);
		outStream.writeByte(0);

		if (NewObjectID > -1) {
			outStream.writeByte(151);
			outStream.writeByteS(0);
			outStream.writeWordBigEndian(NewObjectID);
			outStream.writeByteA(Face);
		}
		outStream.endFrameVarSizeWord();
	}
/**
	 * 
	 * @param objectX
	 * @param objectY
	 * @param NewObjectID
	 * @param Face
	 * @param ObjectType
	 * 
	 * Note: I did not make this method.
	 */

	 
	public void ReplaceObjectOblisk(final int objectX,final int objectY,final int heightlevel, final int NewObjectID, final int oldID, int Face, int ObjectType)
	{
			getActionAssistant().sendMessage("creating " + NewObjectID);
			getActionAssistant().turnTo(objectX, objectY);
			EventManager.getSingleton().addEvent(null,"atobject", new Event() {

				@Override
				public void execute(EventContainer container) {
					stop();
				}

				@Override
				public void stop() {
					final GameObject oldObject = new GameObject(objectX,
							objectY, 0, GameObject.Face.WEST, 10,Server.getObjectManager().getDefinition(oldID));
					final GameObject newObject = new GameObject(objectX,
							objectY, 0, GameObject.Face.WEST, 10,Server.getObjectManager().getDefinition(NewObjectID));
					Server.getObjectManager().replaceObjectWebs(oldObject,
							newObject, 5);
				}

			}, 1000);

	}
	
	
	public void ReplaceObjectOblisk(int objectX, int objectY, int heightlevel, int NewObjectID, int Face, int ObjectType)
	{

		GameObject web = Server.getObjectManager().getObjectAt(objectX,objectY, heightlevel);
		
		if(web == null){
			web = new GameObject(objectX,
							objectY, 0, GameObject.Face.WEST, 10,Server.getObjectManager().getDefinition(NewObjectID));
		
		}
		GameObject cutWeb = new GameObject(objectX, objectY, heightlevel,  web.getFace(), ObjectType,
		
		
		Server.getObjectManager().getDefinition(NewObjectID));
		Server.getObjectManager().replaceObject(web,cutWeb);
		updateRequired = true;
		appearanceUpdateRequired = true;
		/*
		outStream.createFrame(85);
		outStream.writeByteC(objectY - (mapRegionY * 8));
		outStream.writeByteC(objectX - (mapRegionX * 8));

		outStream.createFrame(101);
		outStream.writeByteC((ObjectType << 2) + (Face & 3));
		outStream.writeByte(0);

		if (NewObjectID != -1) {
			outStream.createFrame(151);
			outStream.writeByteS(0);
			outStream.writeWordBigEndian(NewObjectID);
			outStream.writeByteS((ObjectType << 2) + (Face & 3));
			// FACE: 0= WEST | -1 = NORTH | -2 = EAST | -3 = SOUTH
			// ObjectType: 0-3 wall objects, 4-8 wall decoration, 9: diag.
			// walls, 10-11 world objects, 12-21: roofs, 22: floor decoration
		 }*/
	}

	public void ReplaceObject2(int objectX, int objectY, int NewObjectID, int Face, int ObjectType) {
		outStream.createFrame(85);
		outStream.writeByteC(objectY - (mapRegionY * 8));
		outStream.writeByteC(objectX - (mapRegionX * 8));

		outStream.createFrame(101);
		outStream.writeByteC((ObjectType << 2) + (Face & 3));
		outStream.writeByte(0);

		if (NewObjectID != -1) {
			outStream.createFrame(151);
			outStream.writeByteS(0);
			outStream.writeWordBigEndian(NewObjectID);
			outStream.writeByteS((ObjectType << 2) + (Face & 3));
		}
	}
	public void deletethatwall(int objectX, int objectY) {
        for (Player p : Server.getPlayerManager().getPlayers()) {
            if (p != null) {
                Client c = (Client) p;
                c.ReplaceObject2(objectX, objectY, 6951, -1, 0);
            }
        }
    }
	public void deletethatdoor(int objectX, int objectY) {
        for (Player p : Server.getPlayerManager().getPlayers()) {
            if (p != null) {
                Client c = (Client) p;
				
                c.ReplaceObject2(objectX, objectY, 6951, -1, 11);
            }
        }
    }	
    public void makeGlobalObject(int x, int y, int typeID, int orientation, int tileObjectType) { //Makes Global objects
        for (Player p : Server.getPlayerManager().getPlayers()) {
            if (p != null) {
                Client person = (Client) p;
                if ((person.playerName != null || person.playerName != "null")) {
                    if (person.distanceToPoint(x, y) <= 60) {
                        person.createNewTileObject(x, y, typeID, orientation, tileObjectType, 0);
                    }
                }
            }
        }
    }	
    public void makeGlobalObject(int x, int y, int typeID, int orientation, int tileObjectType, int height) { //Makes Global objects
        for (Player p : Server.getPlayerManager().getPlayers()) {
            if (p != null) {
                Client person = (Client) p;
                if ((person.playerName != null || person.playerName != "null")) {
                    if (person.distanceToPoint(x, y) <= 60) {
                       person.createNewTileObject(x, y, typeID, orientation, tileObjectType, height);
                    }
                }
            }
        }
    }	
    public void createNewTileObject(int x, int y, int typeID, int orientation, int tileObjectType, int height) {
        outStream.createFrame(85);
        outStream.writeByteC(y - (mapRegionY * 8));
        outStream.writeByteC(x - (mapRegionX * 8));

        outStream.createFrame(151);
//outStream.writeByteA(((x&7) << 4) + (y&7));
        outStream.writeByteA(height);
        outStream.writeWordBigEndian(typeID);
        outStream.writeByteS((tileObjectType << 2) + (orientation & 3));
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();

    public static String randomString( int len )
    {
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public static int randomInteger(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

}