package com.rs.worldserver.world;

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
import java.util.HashMap;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.io.File;
import java.io.FileWriter;

import com.rs.worldserver.Constants;
import com.rs.worldserver.Server;
import com.rs.worldserver.io.IOClient;
import com.rs.worldserver.io.Stream;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.PlayerDetails;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.*;

import java.util.ArrayList;

/**
 * Player manager
 * 
 * @author Daiki
 * @author Graham
 * 
 */
public class PlayerManager {

	private static PlayerManager singleton = null;
	
	public static PlayerManager getSingleton() {
		if (singleton == null) {
			singleton = new PlayerManager();
		}
		return singleton;
	}
	public static int obelisktimer[] = new int[5];
	public static int pitsWaitTimer = 120;
	public static int pcWaitTimer = 500;
	public static int pcGameTimer = 450;
	public static int playersInPit = 0;
	public static boolean portal1 = false;
	public static boolean portal2 = false;
	public static boolean portal3 = false;
	public static boolean portal4 = false;	
	//Capture the captain
	public static int blueteam = 0;
	public static int redteam = 0;
	public static int redwaiters = 0;
	public static int bluewaiters = 0;
	public static int CtCtimer = 120;
	public static int redpoints = 0;
	public static int bluepoints = 0;
	public static Client BlueCaptain = null;
	public static Client RedCaptain = null;	
	public static Player players[] = new Player[Constants.MAXIMUM_PLAYERS];
	private int playerSlotSearchStart = 1; // where we start searching at when
											// adding a new player
	private String kickNick = "";
	public boolean kickAllPlayers = false;
	// private int playerCount=0;

	private Map<String, Player> playerNameMap;
	private Map<Long, Player> playerNameLongMap;

	private Queue<IOClient> ioClientsToAdd = new LinkedList<IOClient>();
	private Queue<IOClient> ioClients = new LinkedList<IOClient>();
	private Queue<IOClient> ioClientsToRemove = new LinkedList<IOClient>();
	public static boolean updateAnnounced;
	public static boolean updateRunning;
	public static int updateSeconds = 60;
	public static int remaindingSeconds = 0;
	public static long updateStartTime;
	
	public PlayerManager() {
		for (int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
			players[i] = null;
		}
		playerNameMap = new HashMap<String, Player>();
		playerNameLongMap = new HashMap<Long, Player>();
	}


	public Player getPlayerByNameLong(long name) {
		return playerNameLongMap.get(name);
	}
	public int inCtc = 0;
	
	public static void newCtCround() {
		int blueguys[] = new int[blueteam];
		int redguys[] = new int[redteam];
		int inCtc = 1;
		int redslot = 0;
		int blueslot = 0;
		for(int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
		//for (Player p : Server.getPlayerManager().getPlayers()) {
			if(players[i] == null) continue;
			Client c = (Client) Server.getPlayerManager().players[i];
			if(c.inred) {
				c.captain = false;
				redguys[redslot] = redguys[redslot] = i;
				redslot++;
				c.skulls = 0;
				c.updateRequired = true;
				c.appearanceUpdateRequired = true;
				//c.Refresh();
				c.ctctimer = 14;
			}
			if(c.inblue) {
				c.captain = false;
				blueguys[blueslot] = blueguys[blueslot] = i;
				blueslot++;
				c.skulls = 0;
				c.updateRequired = true;
				c.appearanceUpdateRequired = true;
				//c.Refresh();
				c.ctctimer = 11;
			}
		}
		Client r = (Client) PlayerManager.getSingleton().getPlayers()[redguys[Misc.random(redguys.length-1)]];
		Client b = (Client) PlayerManager.getSingleton().getPlayers()[blueguys[Misc.random(blueguys.length-1)]];
		RedCaptain = r;
		BlueCaptain = b;
		r.captain = true;
		b.captain = true;
		b.playerLevel[3] = 999;
		r.playerLevel[3] = 999;
		b.NewHP = 999;
		r.NewHP = 999;
		r.getActionAssistant().refreshSkill(3);
		b.getActionAssistant().refreshSkill(3);
		//b.skulls = 2;
		//r.skulls = 2;
		//for(int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
		for (Player p : Server.getPlayerManager().getPlayers()) {
			if(p != null) {
				Client c = (Client) p;
				if(c.inred) {
					c.getActionAssistant().startround(b.playerId);
				}
				if(c.inblue) {
					c.getActionAssistant().startround(r.playerId);
				}
			}
		}
	}
	
	public static void endCtC() {
	//	for(int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
		for (Player p : Server.getPlayerManager().getPlayers()) {
			if(p != null) {
				Client c = (Client) p;
				if(!c.inred && !c.inblue) continue;
				c.actionAssistant.endgame();
				c.actionAssistant.armourgone(c.playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE]);
				c.captain = false;
				c.inred= false;
				c.inblue = false;
				//c.Refresh();
				int inCtc = 0;
			}
		}
		bluepoints = 0;
		redpoints = 0;
		redteam = 0;
		blueteam = 0;
	}
	
	public static void startCtC() {
		//for(int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
		for (Player p : Server.getPlayerManager().getPlayers()) {
			if(p != null) {
				Client c = (Client) p;
				if(c.redteam) {
					int inCtc = 1;
					c.inred = true;
					c.redteam = false;
					redteam += 1;
				}
				if(c.blueteam) {
					c.inblue = true;
					c.blueteam = false;
					blueteam += 1;
				}
			}
		}
		redwaiters = 0;
		bluewaiters = 0;
		newCtCround();
	}	
	public int getFreeSlot() {
		int slot = -1, i = 1;
		do {
			if (players[i] == null) {
				slot = i;
				break;
			}
			i++;
			if (i >= Constants.MAXIMUM_PLAYERS)
				i = 0; // wrap around
		} while (i <= Constants.MAXIMUM_PLAYERS);
		return slot;
	}
    public Client player(String otherPname){
        int otherPIndex = getPlayerID(otherPname);
        if(otherPIndex != -1){
			Client p = (Client) players[otherPIndex];
			return p;
        }
		return null;
    }
	
	public void addClient(int slot, Client newClient) {
		if (newClient == null)
			return;
		players[slot] = newClient;
		playerNameMap.put(newClient.getPlayerName().toLowerCase(), newClient); 
		playerNameLongMap.put(Misc.playerNameToInt64(newClient.getPlayerName().toLowerCase()), newClient);
		// players[slot].connectedFrom=connectedFrom;

		// start at next slot when issuing the next search to speed it up
		playerSlotSearchStart = slot + 1;
		if (playerSlotSearchStart > Constants.MAXIMUM_PLAYERS)
			playerSlotSearchStart = 1;

		if (Server.getGui() != null) {
			Server.getGui().addPlayer(newClient.getPlayerName());
		}
	}

	// a new client connected
	public IOClient newPlayerClient(java.net.Socket s, String connectedFrom) {
		IOClient ioc;
		try {
			ioc = new IOClient(s, connectedFrom);
			ioc.handler = this;
		} catch (Exception e) {
			return null;
		}
		synchronized(ioClientsToAdd) {
			ioClientsToAdd.add(ioc);
		}
		return ioc;
	}

	public void removeIOClient(IOClient ioc) {
		synchronized(ioClientsToRemove) {
			ioClientsToRemove.add(ioc);
		}
	}

	public void updateIOClients() {
		synchronized(ioClientsToAdd) {
			for(IOClient ioc : ioClientsToAdd) {
				ioClients.add(ioc);
			}
			ioClientsToAdd.clear();
		}
		synchronized(ioClients) {
			for(IOClient ioc : ioClients) {
				if(!ioc.checkTimeout()) {
					ioClientsToRemove.add(ioc);
				}
			}
		}
		synchronized(ioClientsToRemove) {
			for(IOClient ioc : ioClientsToRemove) {
				ioClients.remove(ioc);
			}
			ioClientsToRemove.clear();
		}
	}

	public void destruct() {
		for (int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
			if (players[i] == null)
				continue;
			players[i].destruct();
			players[i] = null;
		}
		System.out.println("Player manager shut down.");
	}

	public int getPlayerCount() {
		 return playerNameMap.size() * 1 + Misc.random(1);
	}

	public void sendGlobalMessage(String message) {
		for (int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
			if (players[i] != null) {
				Client c = (Client) players[i];
				c.getActionAssistant().sendMessage(message);
			}
		}
	}
	public void sendGlobalModMessage(String message) {
		for (int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
			if (players[i] != null) {
				Client c = (Client) players[i];
				if(c.playerRights > 2) {
					c.getActionAssistant().sendMessage(message);
				}
			}
		}
	}	
	public Player getPlayerByName(String name) {  
		name = name.toLowerCase(); 
		return playerNameMap.get(name);  
	}
	public boolean isPlayerOn(String playerName) {
		playerName = playerName.toLowerCase();
		return playerNameMap.containsKey(playerName);
	}
	public static String playersCurrentlyOn[] = new String[Constants.MAXIMUM_PLAYERS];
	
	public static int getPlayerID(String playerName)  {
		for (int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
			if(playersCurrentlyOn[i] != null) {
				if(playersCurrentlyOn[i].equalsIgnoreCase(playerName))
					return i;
			}
		}
		return -1;
	}

	
	public void process() {
		updateIOClients();
		
		if (kickAllPlayers) {
			int kickID = 1;
			do {
				
				if (players[kickID] != null) {
					//saveGame(players[kickID]);
					players[kickID].isKicked = true;
				}
				kickID++;
			} while (kickID < Constants.MAXIMUM_PLAYERS);
			kickAllPlayers = false;
		}

		// at first, parse all the incoming data
		// this has to be seperated from the outgoing part because we have to
		// keep all the player data
		// static, so each client gets exactly the same and not the one clients
		// are ahead in time
		// than the other ones.
		for (int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
			try {
				if (players[i] == null)	continue;
				players[i].process();
				players[i].postProcessing();
				players[i].getNextPlayerMovement();

				if (players[i].getPlayerName().equalsIgnoreCase(kickNick)) {
					players[i].kick();
					kickNick = "";
				}
				if (players[i].disconnected) {
					if (saveGame(players[i])) {
						Misc.println_debug("Game saved for player "+ players[i].getPlayerName());
					} else {
						Misc.println_debug("Could not save for "+ players[i].getPlayerName());
					};
					removePlayer(players[i]);
					players[i] = null;
				}
				} catch (Exception ex) {
					players[i].disconnected = true;
					ex.printStackTrace();
				}
		}

		// loop through all players and do the updating stuff
		for (int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
			try {
				if (players[i] == null)
					continue;
				if (players[i].disconnected) {
					if (saveGame(players[i])) {
						Misc.println_debug("Game saved for player "+ players[i].getPlayerName());
					} else {
						Misc.println_debug("Could not save for "+ players[i].getPlayerName());
					};
					removePlayer(players[i]);
					players[i] = null;
				} else {
					if (!players[i].initialized) {
						players[i].initialize();
						players[i].initialized = true;
					} else
						players[i].update();
				}
			} catch (Exception ex) {
				players[i].disconnected = true;
				ex.printStackTrace();
			}
		}

		// post processing
		for (int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
			try {
				if (players[i] == null)
					continue;
				players[i].clearUpdateFlags();
				if (updateRunning && !updateAnnounced) {
					updateAnnounced = true;
					//Server.UpdateServer = true;
				}
			if (updateRunning && System.currentTimeMillis() - updateStartTime > (updateSeconds * 1000)) {
				kickAllPlayers = true;
				Server.shutdown();
			}
			} catch (Exception ex) {
				players[i].disconnected = true;
				ex.printStackTrace();
			}
		}
		for (int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
			if (players[i] == null)
				continue;
			players[i].clearUpdateFlags();
		}
		// clear NPC update flags here
		for (Map.Entry<Integer, NPC> entry : Server.getNpcManager().getNPCMap()
				.entrySet()) {
			NPC n = entry.getValue();
			n.clearUpdateFlags();
		}
			for(int i = 0; i < 5; i++) {
				if(obelisktimer[i] < 1) continue;
				obelisktimer[i]--;
				boolean gone = false;
				if(obelisktimer[i] == 0) {
					int X[] = {3156, 3307, 3106, 3227, 2980};
					int Y[] = {3620, 3916, 3794, 3667, 3866};
					int ID[] = {14829, 14831, 14828, 14830, 14826};
					int number = Misc.random(X.length-1);
					for(int I = 0; I <  Constants.MAXIMUM_PLAYERS; I++) {
					
						if(players[I] == null) continue;
						Client c = (Client)players[I];
						if(c.playerLevel[3] <= 0){ continue;}
						if(!c.inWild()) {continue;}
						boolean dist1 = c.goodDistance(X[i], Y[i], c.absX, c.absY, 20);
						boolean dist2 = c.goodDistance(X[i], Y[i], c.absX, c.absY, 1);
						if(!dist1) 
							continue; 
						if(dist2)
							c.getActionAssistant().moveObPlayer(X[number]-1+Misc.random(2), Y[number]-1+Misc.random(2), 0);//make a new tele method or you'll have issues ;)
					}
				}		
			}
			if (obelisktimer[0] == 0){
				resetObelisk(13);
				obelisktimer[0] = -1;
			}
			if (obelisktimer[1] == 0){
				resetObelisk(50);
				obelisktimer[1] = -1;
			}
			if (obelisktimer[2] == 0) {
				resetObelisk(35);
				obelisktimer[2] = -1;
			}
			if (obelisktimer[3] == 0){
				resetObelisk(19);
				obelisktimer[3] = -1;
			}
			if (obelisktimer[4] == 0){
				resetObelisk(44);
				obelisktimer[4] = -1;
			}
	}

	private Stream updateBlock = new Stream(
			new byte[Constants.INITIAL_BUFFER_SIZE]);

	public static void updateTimer(Client d, int time){
		while(time > -1){
			System.out.println(time);
			d.getActionAssistant().sendMessage("Alert##Update##An update is incoming Please Log out!## " + time +" seconds till update!");
			time--;
		}
	}
	public static int[] toIntArray(ArrayList<Integer> integerList)
	{
		int[] intArray = new int[integerList.size()];
		
		for (int i = 0; i < integerList.size(); i++)
		{
			intArray[i] = integerList.get(i);
		}
		
		return intArray;
	}	
	
	public void ReplaceObjectOblisk(int objectX, int objectY, int heightlevel, int NewObjectID, int Face, int ObjectType)
	{
		
	}
	
	public void resetObelisk(int silgar){
			if(silgar == 50){
				ReplaceObjectOblisk(3305, 3914, 0, 14831, -1, 10);
				ReplaceObjectOblisk(3305, 3918, 0,  14831, -1, 10);
				ReplaceObjectOblisk(3309, 3918,  0, 14831, -1, 10);
				ReplaceObjectOblisk(3309, 3914, 0,  14831, -1, 10);		
			} else if(silgar == 35){
				ReplaceObjectOblisk(3104, 3796, 0,  14828, -1, 10);
				ReplaceObjectOblisk(3104, 3792,  0, 14828, -1, 10);
				ReplaceObjectOblisk(3108, 3796,  0, 14828, -1, 10);
				ReplaceObjectOblisk(3108, 3792,  0, 14828, -1, 10);	
			} else if(silgar == 13){
				ReplaceObjectOblisk(3154, 3618,  0, 14829, -1, 10);
				ReplaceObjectOblisk(3154, 3622,  0, 14829, -1, 10);
				ReplaceObjectOblisk(3158, 3618,  0, 14829, -1, 10);
				ReplaceObjectOblisk(3158, 3622,  0, 14829, -1, 10);		
			} else if(silgar == 19){
				ReplaceObjectOblisk(3225, 3669,  0, 14830, -1, 10);
				ReplaceObjectOblisk(3229, 3665,  0, 14830, -1, 10);
				ReplaceObjectOblisk(3225, 3665,  0, 14830, -1, 10);
				ReplaceObjectOblisk(3229, 3669,  0, 14830, -1, 10);
			} else if(silgar == 44){
				ReplaceObjectOblisk(2978, 3864,  0, 14826, -1, 10);
				ReplaceObjectOblisk(2982, 3864,  0, 14826, -1, 10);
				ReplaceObjectOblisk(2978, 3868,  0, 14826, -1, 10);
				ReplaceObjectOblisk(2982, 3868,  0, 14826, -1, 10);
			} else if (silgar == 27){
				ReplaceObjectOblisk(3033, 3734,  0, 14827, -1, 10);
				ReplaceObjectOblisk(3037, 3734,  0, 14827, -1, 10);
				ReplaceObjectOblisk(3033, 3730,  0, 14827, -1, 10);
				ReplaceObjectOblisk(3037, 3730,  0, 14827, -1, 10);
			}
		}
	// should actually be moved to client.java because it's very client specific
	public void updatePlayer(Player plr, Stream str) {
		updateBlock.currentOffset = 0;
		if (updateRunning && !updateAnnounced) {

		}
		plr.updateThisPlayerMovement(str); // handles walking/running and
		boolean saveChatTextUpdate = plr.chatTextUpdateRequired;
		plr.chatTextUpdateRequired = false;
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.chatTextUpdateRequired = saveChatTextUpdate;
		str.writeBits(8, plr.playerListSize);
		int size = plr.playerListSize;
		plr.playerListSize = 0;	
		for(int i = 0; i < size; i++) {			
			if(!plr.didTeleport && !plr.playerList[i].didTeleport && plr.withinDistance(plr.playerList[i])) {
				plr.playerList[i].updatePlayerMovement(str);
				plr.playerList[i].appendPlayerUpdateBlock(updateBlock);
				plr.playerList[plr.playerListSize++] = plr.playerList[i];
			} else {
				int id = plr.playerList[i].playerId;
				plr.playerInListBitmap[id>>3] &= ~(1 << (id&7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}
		int j = 0;
		for(int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
			//if(updateBlock.currentOffset >= 4000)
			//break;
			if(plr.playerListSize >= 254) break;
			if(updateBlock.currentOffset+str.currentOffset >= 9000)//4900
			break;
			if(players[i] == null || !players[i].isActive || players[i] == plr) continue;
			int id = players[i].playerId;
			if((plr.playerInListBitmap[id>>3]&(1 << (id&7))) != 0) continue;
			if(j >= 10) break;	
			if(!plr.withinDistance(players[i])) continue;		
			plr.addNewPlayer(players[i], str, updateBlock);//open player .java for me
			j++;
		}

		if(updateBlock.currentOffset > 0) {
			str.writeBits(11, 2047);	
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		}
		else str.finishBitAccess();

		str.endFrameVarSizeWord();
	}

public void updateNPC(Player plr, Stream str) {
		updateBlock.currentOffset = 0;

		str.createFrameVarSizeWord(65);
		str.initBitAccess();

		str.writeBits(8, plr.npcListSize);
		int size = plr.npcListSize;
		plr.npcListSize = 0;
		//boolean temptr = plr.inTradeArea();
		for (int i = 0; i < size; i++) {
			if (plr.rebuildNPCList == false && plr.withinDistance(plr.npcList[i]) && !plr.npcList[i].isHidden()) {
				plr.npcList[i].updateNPCMovement(str);
				plr.npcList[i].appendNPCUpdateBlock(updateBlock);
				plr.npcList[plr.npcListSize++] = plr.npcList[i];
			} else {
				int id = plr.npcList[i].getNpcId();
				plr.npcInListBitmap[id >> 3] &= ~(1 << (id & 7)); // clear the
																	// flag
				str.writeBits(1, 1);
				str.writeBits(2, 3); // tells client to remove this NPC from
										// list
			}
		}

		// iterate through all NPC's to check whether there's new NPC to add
		// for (int i = 0; i < NPCHandler.getMaxNPCs(); i++) {
		for (int i = 1; i < NPCManager.MAXIMUM_NPCS; i++) {
			if (Server.getNpcManager().getNPC(i) != null) {
				int id = Server.getNpcManager().getNPC(i).getNpcId();
				if (plr.rebuildNPCList == false
						&& (plr.npcInListBitmap[id >> 3] & (1 << (id & 7))) != 0) {
					// npc already in npcList
				} else if (Server.getNpcManager().getNPC(i).isHidden()) {
					// npc is hidden
				} else if (!plr.withinDistance(Server.getNpcManager().getNPC(i))) {
					// out of sight
				} else {
					plr.addNewNPC(Server.getNpcManager().getNPC(i), str,
							updateBlock);
				}
			}
		}
		// }

		plr.rebuildNPCList = false;

		if (updateBlock.currentOffset > 0) {
			str.writeBits(14, 16383); // magic EOF - needed only when NPC
										// updateblock follows
			str.finishBitAccess();
			// append update block
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}
		str.endFrameVarSizeWord();
	}

	private void removePlayer(Player plr) {
		// anything can be done here like unlinking this player structure from
		// any of the other existing structures
		plr.destruct();
		if (Server.getGui() != null) {
			Server.getGui().removePlayer(plr.getPlayerName());
		}

		playerNameMap.remove(plr.getPlayerName().toLowerCase());
		playerNameLongMap.remove(Misc.playerNameToInt64(plr.getPlayerName().toLowerCase()));
	}

	public static boolean saveGame(Player plr) {
		if((updateSeconds * 50 / 30) > 20){
		//if(!updateAnnounced || !updateRunning) {
			PlayerDetails tempSave = new PlayerDetails(plr);
			String firstLetters = Character.toString(tempSave.playerName.charAt(0));
			String file = "./savedGames/" +firstLetters.toLowerCase()+"/"+  tempSave.playerName + ".json";
				
			try {
				File dir = new File("./savedGames/" +firstLetters.toLowerCase());
				if(!dir.exists()){
				    boolean result = dir.mkdir();  
				}
			
				File f = new File(file);
				if(!f.exists())
					f.createNewFile();
			
				String json = new Gson().toJson(tempSave);
				FileWriter writer = new FileWriter(file);
				writer.write(json);
				writer.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
		Misc.println_debug("Could not save.");
		return false;

	}

	/**
	 * @param players
	 *            the players to set
	 */
	public void setPlayers(Player players[]) {
		this.players = players;
	}

	/**
	 * @return the players
	 */
	public Player[] getPlayers() {
		return players;
	}

	public void kick(String name) {
		for (Player p : players) {
			if (p == null)
				continue;
			if (!p.isActive || p.disconnected)
				continue;
			if (p.getPlayerName().equalsIgnoreCase(name)) {
					PlayerManager.getSingleton().saveGame(p);
					p.kick();
					p.disconnected = true;
				
			}
		}
	}
	

}