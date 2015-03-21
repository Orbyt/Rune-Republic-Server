package com.rs.worldserver.model.player;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.Server;

import java.util.HashMap;
import com.rs.worldserver.world.PlayerManager;
import java.util.*;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;

public class FightPits {
 
	private HashMap<Client, Integer> waitingRoom = new HashMap<Client, Integer>();
    private HashMap<Client, Integer> gameRoom = new HashMap<Client, Integer>();
	private final int DEFAULT_WAIT_TIME = 150;
	public int waitTime = DEFAULT_WAIT_TIME;
	public int totalPlayers = 0;
	public int startWaitTime = 0;
	private boolean allowAttack = false;
	private long second;
	public boolean gameRunning = false;
	public boolean started = false;
	private String champion = "Orbit"; // by default s^
	public FightPits(){
	EventManager.getSingleton().addEvent(null,"Web Respawn", new Event() {
			public void execute(EventContainer webRespawn) {
					process();
				}
										
				@Override
				public void stop() {
										
				}
			}, 1000);
	}
	public void updateWaitInterfaces(Client c) {
		if(!started){
			c.getActionAssistant().sendFrame126("Current Champion: JalYt-Ket-"+champion, 2805);
			if(champion.equalsIgnoreCase(c.playerName))
				c.getActionAssistant().sendFrame36(560, 0);
			else {
				c.getActionAssistant().sendFrame126("You're not the Winner!", 2806);
				c.getActionAssistant().sendFrame36(560, 1);
			}
		} else {
			c.getActionAssistant().sendFrame126("Currently there is a game running...", 2805);
			c.getActionAssistant().sendFrame126("Enemies left: " + (totalPlayers), 2806);
			c.getActionAssistant().sendFrame36(560, 1);
		}
			//c.getActionAssistant().showInterfaceWalkable(2804);
	}
	
	public void process() {
			if(waitTime > 0 && !started) {
				waitTime--;
				announceTime();
			}else{
				if(!gameRunning)
					startGame();
					gameTimer++;
			}
			if(gameTimer > 3000)
				endGame();
			if(totalPlayers == 1 && started)
				endGame();
	}
	
	public void updateGameInterfaces(Client c) {
		int foes = totalPlayers - 1;
		c.getActionAssistant().sendFrame126("Current Champion: JalYt-Ket-"+champion, 2805);
		c.getActionAssistant().sendFrame126("Foes: "+foes, 2806);
		c.getActionAssistant().sendFrame36(560, 1);
		c.getActionAssistant().showInterfaceWalkable(2804);
	}
	
	//private FightPitsEvent e;
	public void addPlayerToWait(Client c) {
		if(c == null)
			return;
		if(waitingRoom.containsKey(c)) { // waiting room already contains them I guess they want to leave.
			removePlayerFromWait(c);
			return;
		}
		waitingRoom.put(c, c.playerId);
		c.getActionAssistant().movePlayer(2399, 5175, 0);
		c.getActionAssistant().sendMessage("You entered fight pits!");
		updateWaitInterfaces(c);
		c.getActionAssistant().requestUpdates();
	}
	
	public void removePlayerFromWait(Client c) {
		if(c == null)
			return;
		if(waitingRoom.containsKey(c)) {
			waitingRoom.remove(c);
		}
		c.getActionAssistant().sendMessage("@red@You have left fight pits!");
		c.getActionAssistant().movePlayer(2399, 5177, 0);
		c.getActionAssistant().showInterfaceWalkable(-1);
	//	c.miasmicEffect = 0;	
		c.canEat = true;
		c.getActionAssistant().resetPrayers();
	}
	
	public void removePlayerFromGame(Client c) {
		if(c == null)
			return;
		c.dieTimer = 10;
		c.getActionAssistant().movePlayer(2399, 5173, 0);
		//c.miasmicEffect = 0;	
		c.canEat = true;
		c.getActionAssistant().resetPrayers();
		gameRoom.remove(c);
		totalPlayers = gameRoom.size();
	}
	
	public boolean isInFightPitsWait(Client c){
		if(c == null)
			return false;
		if(waitingRoom.containsKey(c)){
			return true;
		}
		return false;
	}
	
	public boolean isInFightPitsGame(Client c){
		if(c == null)
			return false;
		if(gameRoom.containsKey(c)){
			return true;
		}
		return false;
	}
	
	private void addPlayerToGame(Client c) {
		if(c == null)
			return;
			
			c.getActionAssistant().showInterfaceWalkable(-1);
			c.getActionAssistant().movePlayer(2392 + Misc.random(12), 5139 + Misc.random(25), 0);
			
			///c.getActionAssistant().sendMessage("teleported");
		if(waitingRoom.containsKey(c)) {
			waitingRoom.remove(c);
			gameRoom.put(c, c.playerId);
		}
	}	
	
	public void announceTime(){
	Iterator iterator = waitingRoom.keySet().iterator();
		switch(waitTime){
			case 300: // 5 mins
			case 240: // 4 mins 
			case 180: // 3 mins
			case 120: // 2 mins
			while (iterator.hasNext()) {
				Client c = (Client) iterator.next();
				if(c == null){
					continue;
				}
				c.getActionAssistant().sendMessage("Alert##Fight Pits##The game will begin in ##"+(waitTime / 60)+" minutes");
			
			}
			//PlayerManager.getSingleton().sendGlobalMessage("[@red@Fight Pits@bla@] There are " + (((waitTime) / 60)) +" minutes left till the game starts!"); 
		
				break;
			case 60: // 60 seconds 
			iterator = waitingRoom.keySet().iterator();
			while (iterator.hasNext()) {
				Client c = (Client) iterator.next();
				if(c == null){
					continue;
				}
				c.getActionAssistant().sendMessage("Alert##Fight Pits##The game will begin in ##"+waitTime+" seconds");
			}
				//PlayerManager.getSingleton().sendGlobalMessage("[@red@Fight Pits@bla@] There are " + (((waitTime) / 60 )) +" minute left till the game starts!"); 
				break;
			case 30:
			iterator = waitingRoom.keySet().iterator();
			while (iterator.hasNext()) {
				Client c = (Client) iterator.next();
				if(c == null){
					continue;
				}
				c.getActionAssistant().sendMessage("Alert##Fight Pits##The game will begin in ##"+waitTime+" seconds");
			}
				break;
			case 15:
			iterator = waitingRoom.keySet().iterator();
			while (iterator.hasNext()) {
				Client c = (Client) iterator.next();
				if(c == null){
					continue;
				}
				c.getActionAssistant().sendMessage("Alert##Fight Pits##The game will begin in ##"+waitTime+" seconds");
			}
				break;
			case 10:
			case 9:
			case 8:
			case 7:
			case 6:
			iterator = waitingRoom.keySet().iterator();
			while (iterator.hasNext()) {
				Client c = (Client) iterator.next();
				if(c == null){
					continue;
				}
				c.getActionAssistant().sendMessage("Alert##Fight Pits##The game will begin in ##"+waitTime+" seconds");
			}
				break;
			case 5:
			case 4:
			case 3:
			case 2:
			case 1:
			iterator = waitingRoom.keySet().iterator();
			while (iterator.hasNext()) {
				Client c = (Client) iterator.next();
				if(c == null){
					continue;
				}
				c.getActionAssistant().sendMessage("Alert##Fight Pits##The game will begin in ##"+waitTime+" seconds");
			}
				break;
		}
	}
	
	public int gameTimer = 0;	
	private int startPlayerAmount = 0;
	public void startGame() {
		gameTimer = 0;
		Iterator iterator = waitingRoom.keySet().iterator();
		if(waitingRoom.size() < 2) {
			waitTime = DEFAULT_WAIT_TIME;
			while (iterator.hasNext()) {
				Client c = (Client) iterator.next();
				if(c == null)
				continue;
				c.getActionAssistant().sendMessage("Alert##Fight Pits## Not enough players to start :( ##"+(waitTime /60)+"  minutes till it starts again");
			}
			return;
		}
		canAttack = false;
		while (iterator.hasNext()) {
			Client c = (Client) iterator.next();
			if(c == null)
				continue;
			c.getActionAssistant().movePlayer(2392 + Misc.random(12), 5139 + Misc.random(25), 0);
			gameRoom.put(c, c.playerId);
		}
		
		waitingRoom.clear();
		totalPlayers = gameRoom.size();
		startPlayerAmount = totalPlayers;
		startWaitTime = 5;
		announce = true;
		canAttack = false;
			EventManager.getSingleton().addEvent(null,"Web Respawn", new Event() {
			public void execute(EventContainer webRespawn) {
				
				Iterator iterator = gameRoom.keySet().iterator();
					while (iterator.hasNext()) {
						Client c = (Client) iterator.next();
						if(c == null)
							continue;
						if(startWaitTime > 1 && announce)
							c.forcedChat(""+startWaitTime);
						else{
							if(announce)
							c.forcedChat("Fight!!");
					
						}
					}
					
					startWaitTime--;
					if(startWaitTime == 0)
						stop();
					
				}
										
				@Override
				public void stop() {
					startWaitTime = 5;
					canAttack = true;
					announce = false;					
				}
			}, 1000);
	
		//startKilling();
		second = System.currentTimeMillis();
		started = true;
		gameRunning = true;
	}
	
	public boolean canAttack = true;
	public boolean announce = true;
	public int[] rewards = {4151,3025,4719,6686,4151,2450,6585,4737,4739,4151,6914,4711,6914,6889,6920,15486,6914,6585,6920,3025,4719,6585,3025,21787,
	4151,6686,21793,6585,4719,21790,6585,13653,7822,4151,4713,6585,6585,13665,13664,15272,13667,4750,13662,13668,13667,13668,13660,4717,4719};
	public int getReward(Client winner){
		switch(winner.fightPitStreak + winner.fightPitsKills){
			case 1:
			case 2:
			
				return Misc.randomRange(0,3);
			case 3:
			case 4:
				
				return Misc.randomRange(4,8); 
			case 5:
			case 6:
				return Misc.randomRange(9,14);
			case 7:
			case 8:
				return Misc.randomRange(15,22);
			case 9:
			case 10:
				winner.getActionAssistant().sendMessage("@red@ You earned the new title!");
				winner.getActionAssistant().sendMessage("@red@ Dominus");
				return Misc.randomRange(15,22);
			case 11:
			case 12:
			case 13:
			case 14:
				return Misc.randomRange(18,23);
			case 15:
				winner.getActionAssistant().sendMessage("@red@ You earned the new title!");
				winner.getActionAssistant().sendMessage("@red@ Canes Pugnaces");
				return Misc.randomRange(18,23);
		}
		return Misc.randomRange(18,23);
	}
	public void endGame() {
	announce = true;
	gameTimer = 0;
	startWaitTime = 5;
		if(gameRoom.size() < 2) {
			Iterator iterator = gameRoom.keySet().iterator();
			while (iterator.hasNext()) {
				Client winner = (Client) iterator.next();
					if(winner == null){
						champion = "Orbit";
						continue;
					}
				winner.skullIcon = 1;
				winner.totalFightPitWins++;
				winner.fightPitStreak++; // pat edit - in ACTIONASSISTANT under death/teleport for fightpits set this to 0
				if(startPlayerAmount > 5) {
					winner.getActionAssistant().addItem(rewards[getReward(winner)], 10);
					PlayerManager.getSingleton().sendGlobalMessage("[@red@Fight Pits@bla@]@mag@ " + winner.playerName + " has won " + Server.getItemManager().getItemDefinition(rewards[getReward(winner)]).getName() + " from Fight Pits!");
					winner.getActionAssistant().sendMessage("Alert##Fight Pits##Congrats you won fight pits! ## You received a reward!");
				} else {
					winner.getActionAssistant().sendMessage("Alert##Fight Pits##Congrats you won fight pits! ## However, there was not enough players to award a prize.");
				}
				winner.dieTimer = 10;
				winner.fightPitsKills = 0;
				champion = winner.playerName;
				winner.getActionAssistant().movePlayer(2399, 5175, 0);
				winner.getActionAssistant().requestUpdates();
				removePlayerFromGame(winner);
				addPlayerToWait(winner);
			}
		} else {
			// iterate whoever is left in gameRoom
			Iterator iterator = gameRoom.keySet().iterator();
			while (iterator.hasNext()) {
				Client winner = (Client) iterator.next(); 
				if(winner == null) {
					continue;
				}
				winner.dieTimer = 10;
				winner.getActionAssistant().movePlayer(2399, 5175, 0); // move us to the gate
				winner.getActionAssistant().requestUpdates(); // request screen updates to client
				removePlayerFromGame(winner); // remove player from hasmap
				addPlayerToWait(winner);
			}
		}
		gameRoom.clear(); // clear our hashmap
		waitTime = 300; // game runs at 500 ms' 1000 ms = 1 second, 5 mins = 5*60 = 300 seconds
		PlayerManager.getSingleton().sendGlobalMessage("[@red@Fight Pits@bla@] The current game just finished!"); 
		started = false;
		gameRunning = false;
	}	
}