package com.rs.worldserver.model.player;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.Server;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.object.GameObject;
import java.util.HashMap;
import com.rs.worldserver.world.PlayerManager;
import java.util.*;

public class CastleWars  {
    private final int GAME_TIMER = 2400; //20 minutes
    private int waitRoomTimer = 300; // 5 mins 
	private Map<Client, Integer> waitingRoom = new HashMap<Client, Integer>();
    private Map<Client, Integer> gameRoom = new HashMap<Client, Integer>();
    public List<Barricade> barricades = new ArrayList<Barricade>();
	private int[][] location = {
		{2429, 3074}, //sara {X-Coord, Y-Coord)
        {2370, 3133} //zammy
	};
	private final int[][] WAIT_ROOM = {
        {2377, 9485}, //sara
        {2421, 9524} //zammy
    };
    private final int[][] GAME_ROOM = {
        {2426, 3076}, //sara
        {2372, 3131} //zammy
    };
    private final int[][] FLAG_STANDS = {
        {2429, 3074}, //sara {X-Coord, Y-Coord)
        {2370, 3133} //zammy
    };
    private int[] scores = {0, 0};
	private int[] doors = {100,100};
	private int[] rebuildTime = {260,260};
	public int[] barricade = { 0, 0};
    private int zammyFlag = 0,saraFlag = 0;
    private final int SARA_BANNER = 4037, ZAMMY_BANNER = 4039, SARA_CAPE = 4041, ZAMMY_CAPE = 4042;
	private int zammyFlagTimer = 60,saraFlagTimer = 60, timeRemaining = 0, saraX = 0,saraY = 0, zammyX = 0, zammyY = 0, zammyRocks = 0,saraRocks = 0;
	public int zamRocks1 = 0,zamRocks2 = 0,saraRocks1 = 0,saraRocks2 = 0,message1 = 0,message2 = 0;
    private boolean gameStarted = false;
    public int timeElapsed = 0;
	private boolean zamDoor = false;
	private boolean saraDoor = false;
	public CastleWars(){
	EventManager.getSingleton().addEvent(null,"Web Respawn", new Event() {
			public void execute(EventContainer webRespawn) {
					process();
				}
										
				@Override
				public void stop() {
										
				}
			}, 500);	
	}
    public void addToWaitRoom(Client player, int team) {
		int time = (waitRoomTimer / 2);
        if (player == null) {
            return;
		}
        if (player.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] > 0 || player.playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE] > 0) {
            player.getActionAssistant().sendMessage("You may not wear capes or helmets inside of castle wars.");
            return;
        }
		for (int i = 0; i < 28; i++) {
			if (player.getActionAssistant().playerHasItem(SARA_CAPE,1)) {
                player.getActionAssistant().deleteItem(SARA_CAPE, player.getActionAssistant().getItemSlot(SARA_CAPE), 1);
			}
			if (player.getActionAssistant().playerHasItem(ZAMMY_CAPE,1)) {
                player.getActionAssistant().deleteItem(ZAMMY_CAPE, player.getActionAssistant().getItemSlot(ZAMMY_CAPE), 1);
			}
			
			if (player.getActionAssistant().playerHasItem(4037,1)) {
                player.getActionAssistant().deleteItem(4037,player.getActionAssistant().getItemSlot(4037), 1);
			}
			if (player.getActionAssistant().playerHasItem(4039,1)) {
                player.getActionAssistant().deleteItem(4039, player.getActionAssistant().getItemSlot(4039), 1);
			}
		}
			if( player.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4037 || player.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4039) {
                player.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] =0;
			 }
        switch(team){
			case 1:
				if (getSaraPlayersCount() > getZammyPlayersCount() && getSaraPlayersCount() > 0) {
					player.getActionAssistant().sendMessage("The saradomin team is full, try again later!");
					return;
				} else {
					player.getActionAssistant().sendMessage("You have been added to the @red@Saradomin@bla@ team.");
					player.getActionAssistant().sendMessage("Next Game Begins In:@red@ " + (time) + " @bla@seconds.");
					addCapes(player, SARA_CAPE);
					waitingRoom.put(player, team);
					player.getActionAssistant().movePlayer((WAIT_ROOM[team - 1][0] + 0 + Misc.random(5)),(WAIT_ROOM[team - 1][1] + Misc.random(5)), 0);
				}
				break;
			case 2:
				if (getZammyPlayersCount() > getSaraPlayersCount() && getZammyPlayersCount() > 0) {
					player.getActionAssistant().sendMessage("The zamorak team is full, try again later!");
					return;
				} else {
					player.getActionAssistant().sendMessage("You have been added to the @red@Zamorak@bla@ team.");
					player.getActionAssistant().sendMessage("Next Game Begins In:@red@ " + (time) + " @bla@seconds.");
					addCapes(player, ZAMMY_CAPE);
					waitingRoom.put(player, team);
					player.getActionAssistant().movePlayer((WAIT_ROOM[team - 1][0] + 0 + Misc.random(5)),(WAIT_ROOM[team - 1][1] + Misc.random(5)), 0);
				}
				break;
			case 3:
				addToWaitRoom(player,(getZammyPlayersCount() > getSaraPlayersCount() ? 1 : 2));
				return;
			
		}
    }  
    public void leaveWaitingRoom(Client player) {
        if (player == null) {
            return;
        }
        if (waitingRoom.containsKey(player)) {
            waitingRoom.remove(player);
            player.getActionAssistant().createPlayerHints(10, -1);
            player.getActionAssistant().sendMessage("You left your team!");
            deleteGameItems(player);
            player.getActionAssistant().movePlayer((2439 + Misc.random(4)), (3085 + Misc.random(5)), 0);
            return;
        }
        player.getActionAssistant().movePlayer((2439 + Misc.random(4)), (3085 + Misc.random(5)), 0);
    }
	private boolean messageSent = false;
    public void process() {
		if(!gameStarted){ // game hasn't started but theres br0s on a team
			if(waitRoomTimer > 0){
				waitRoomTimer--;
			} else {
			   startGame();
			   waitRoomTimer = 600 + GAME_TIMER; // 5 mins + 5 mins + 15 mins = 30 mins between games.
			}
		} else {
			if(waitRoomTimer > 0){
				waitRoomTimer--;
			}
			if(((((GAME_TIMER - timeElapsed) / 2) / 60 ) % 5) == 0 ) {
				if(!messageSent){
					if((((GAME_TIMER - timeElapsed) / 2) / 60 ) > 1) {
						PlayerManager.getSingleton().sendGlobalMessage("[@red@Castle Wars@bla@] There are " + (((GAME_TIMER - timeElapsed) / 2) / 60 ) +" mins left in the current game!"); 
						messageSent = true;
					} else {
						PlayerManager.getSingleton().sendGlobalMessage("[@red@Castle Wars@bla@] There are " + (((GAME_TIMER - timeElapsed) / 2)) +" seconds left in the current game!"); 
						messageSent = true;
					}
				}
			} else {
				messageSent = false;
			}			
			if(saraDoor){
				if(rebuildTime[0] < 1){
					rebuildTime[0] = 20;
					doorObject(1,0);
					doors[1] = 100;
					saraDoor = false;
					} else {
					rebuildTime[0]--;
			 	}
			}
			
			if(zamDoor){
				if(rebuildTime[1] < 1){
					rebuildTime[1] = 20;
					doorObject(0,0);
					doors[0] = 100;
					zamDoor = false;
					} else {
					rebuildTime[1]--;
				}
			}
			
			if(timeElapsed < GAME_TIMER){
				timeElapsed++;
				if(zammyFlag == 2){
					if(zammyFlagTimer > 0){
						zammyFlagTimer--;
					} else {
						returnFlag(2);
					}
				}
				
				if(saraFlag ==2){
					if(saraFlagTimer > 0){
						saraFlagTimer--;
						//System.out.println(saraFlagTimer);
					} else{
						returnFlag(1);
					}
				}
			} else {
			 endGame();
			}
		}
    }
	public void updateInGamePlayers(Client c) {
		timeRemaining = (((GAME_TIMER - timeElapsed) / 2) / 60 ); 
        c.getActionAssistant().sendFrame126("Zamorak = " + scores[1], 11147);
        c.getActionAssistant().sendFrame126(scores[0] + " = Saradomin", 11148);
		if(timeRemaining > 0) {
			c.getActionAssistant().sendFrame126(timeRemaining +"Mins", 11155);
		} else {
			timeRemaining = ((GAME_TIMER - timeElapsed) / 2);
			c.getActionAssistant().sendFrame126(timeRemaining +"Secs", 11155);
		}
		int config = 0;
		config = doors[1];
		config += 128 * 0;
		config += 128 * zammyRocks;
		config += 128 * saraRocks;
		config += 128 * 8;
		config += (2097152 * zammyFlag);
        c.getActionAssistant().sendFrame87(gameRoom.get(c) == 2 ? 377 : 378, config);
        config = 0;
		config = doors[0];
		config += 128 * 0;
		config += 128 * zammyRocks;
		config += 128 * saraRocks;
		config += 128 * 8;
		config += (2097152 * saraFlag); //flags 0 = safe 1 = taken 2 = dropped
        c.getActionAssistant().sendFrame87(gameRoom.get(c) == 2 ? 378 : 377, config);
    }
 

    public void startGame() {
        System.out.println("Starting Castle Wars game.");
        gameStarted = true;
        Iterator iterator = waitingRoom.keySet().iterator();
        while (iterator.hasNext()) {
            Client c = (Client) iterator.next();
            int team = waitingRoom.get(c);
            if (c == null) {
                continue;
            }
			c.cwDamage = 0;
			c.getActionAssistant().object(4275,2377,3135,0,10);
            c.getActionAssistant().movePlayer(GAME_ROOM[team - 1][0] + Misc.random(3), GAME_ROOM[team - 1][1] - Misc.random(3), Misc.random(2));
            c.getActionAssistant().movePlayer(GAME_ROOM[team - 1][0] + Misc.random(3), GAME_ROOM[team - 1][1] - Misc.random(3),Misc.random(2));
            gameRoom.put(c, team);
        }
        waitingRoom.clear();
    }


	public void updatePlayers(Client c) {
		if(((waitRoomTimer / 2) / 60) > 1){
			c.getActionAssistant().sendFrame126("Next Game Begins In:@red@ " + ((waitRoomTimer / 2) / 60) + " @whi@minutes.", 6570);
		} else if (((waitRoomTimer / 2) / 60) == 1 ){
			c.getActionAssistant().sendFrame126("Next Game Begins In:@red@ " + ((waitRoomTimer / 2) / 60) + " @whi@minute.", 6570);
		} else {
			c.getActionAssistant().sendFrame126("Next Game Begins In:@red@ " + ((waitRoomTimer / 2)) + " @whi@Seconds.", 6570);
		}
		c.getActionAssistant().sendFrame126("Zam Players: @red@" + getZammyPlayersCount() + "@whi@.", 6572);
		c.getActionAssistant().sendFrame126("Sara Players: @red@" + getSaraPlayersCount() + "@whi@.", 6664);
	}
	
	public void removePlayerFromCw(Client player) {
        if (player == null) {
            System.out.println("Error removing player from castle wars [REASON = null]");
            return;
        }
        if (gameRoom.containsKey(player)) {
            /*
             * Logging/leaving with flag
             */
			player.stopCwEvent();
            if (player.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == (SARA_BANNER)) {
                player.getActionAssistant().removeItem(player.playerEquipment[3], 3);
				saraFlag = 0;
            } else if (player.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == (ZAMMY_BANNER)) {
                player.getActionAssistant().removeItem(player.playerEquipment[3], 3);
				zammyFlag = 0;
            }
            deleteGameItems(player);
            player.getActionAssistant().movePlayer(2440, 3089, 0);
            player.getActionAssistant().sendMessage("[@red@CASTLE WARS@bla@] The Casle Wars Game has ended for you!");
            player.getActionAssistant().createPlayerHints(10, -1);
            gameRoom.remove(player);
        }
        if (getZammyPlayersCount() <= 0 || getSaraPlayersCount() <= 0) {
           //endGame();
        }
    }

    public void deleteGameItems(Client player) {
        switch (player.playerEquipment[3]) {
            case 4037:
            case 4039:
                player.getActionAssistant().removeItem(player.playerEquipment[3], 3);
                break;
        }
        switch (player.playerEquipment[1]) {
            case 4042:
            case 4041:
                player.getActionAssistant().removeItem(player.playerEquipment[1], 1);
                break;
        }
		System.out.println("Deleting Items");
        int[] items = {4049, 1265, 4045, 4053, 4042, 4041, 4037, 4039};
        for(int k = 0; k < player.playerItems.length; k++){
			for (int i = 0; i < items.length; i++) {
				if((player.playerItems[k] - 1) == items[i]){
					player.playerItems[k] = 0;
					player.playerItemsN[k] = 0;
				}
			}
        }
		player.getActionAssistant().resetItems();
    }
	public void updateRocks(int x, int y, int mode){
		Iterator iterator = gameRoom.keySet().iterator();
			while (iterator.hasNext()) {
				Client teamPlayer = (Client) iterator.next();
				switch(mode){
				
					case 1: // stage 1 
					    teamPlayer.getActionAssistant().object(4438, x, y, 0, 10);		
					break;
					case 2: // clear em
						teamPlayer.getActionAssistant().object(-1, x, y, 0, 10);
						if(x == 2400 && y == 9512){
							zamRocks1 = 1;
						} else if(x == 2391 && y == 9501){
							zamRocks2 = 1;
						} else if(x == 2409 && y == 9503){
							saraRocks1 = 1;
						} else if(x == 2401 && y == 9494) {
							saraRocks2 = 1;
						}
						
						
						if(saraRocks1 == 1 && saraRocks2 == 1 && message1 == 0){
							saraRocks = 4;
							message1 = 1;
							teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##The Sara tunnel has been cleared!## ");
						} 
						if(zamRocks1 == 1 && zamRocks2 == 1 && message2 == 0){
							zammyRocks = 2;
							message2 = 1;
							teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##The Zamorak tunnel has been cleared!## ");
						}
						break;
					case 3: // regenerate (collapsed)
					teamPlayer.getActionAssistant().object(4437, x, y, 0, 10);
						if(x == 2400 && y == 9512){
							zamRocks1 = 0;
						} else if(x == 2391 && y == 9501){
							zamRocks2 = 0;
						} else if(x == 2409 && y == 9503){
							saraRocks1 = 0;
						} else if(x == 2401 && y == 9494) {
							saraRocks2 = 0;
						}
						if(saraRocks1 == 0 && saraRocks2 == 0){
							teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##The Sara tunnel has collapsed!## ");
							saraRocks = 0;
							message1 = 0;
						}
						
						if(zamRocks1 == 0 && zamRocks2 == 0){
							teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##The Zamorak tunnel has collapsed!## ");
							zammyRocks = 0;
							message2 = 0;
						}
					break;

				}
								teamPlayer.getActionAssistant().requestUpdates();	
			}
		}
	   public void getFlag(Client player) {
        if (player.playerEquipment[3] > 0) {
            player.getActionAssistant().sendMessage("Please remove your weapon before attempting to get the flag again!");
            return;
        }
        int team = gameRoom.get(player);
        if (team == 2 && saraFlag == 0) { //sara flag
    		saraFlag = 1;
			addFlag(player,4037);
			GameObject stump = new GameObject(FLAG_STANDS[0][0], FLAG_STANDS[0][1], 3, GameObject.Face.NORTH, 10,
							Server.getObjectManager().getDefinition(4377));
							Server.getObjectManager().replaceObject(Server.getObjectManager().getObjectAt(FLAG_STANDS[0][0], FLAG_STANDS[0][1], 3),stump);

			Iterator iterator = gameRoom.keySet().iterator();
			while (iterator.hasNext()) {
				Client teamPlayer = (Client) iterator.next();
				if (gameRoom.get(teamPlayer) == 1) {
					System.out.println("created hint icons for playernam(e " + teamPlayer.playerName);
					teamPlayer.getActionAssistant().createPlayerHints(10, player.playerId);
					teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##The Enemy has your flag!## ");
				} else {
					teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##Your team has the Enemy flag!## ");
				}
					teamPlayer.getActionAssistant().requestUpdates();	
			}
        }
        if (team == 1 && zammyFlag == 0) {
            zammyFlag = 1;
            addFlag(player,4039);
			Iterator iterator = gameRoom.keySet().iterator();
			GameObject stump2 = new GameObject(FLAG_STANDS[1][0], FLAG_STANDS[1][1], 3, GameObject.Face.NORTH, 10,
							Server.getObjectManager().getDefinition(4378));
				Server.getObjectManager().replaceObject(Server.getObjectManager().getObjectAt(FLAG_STANDS[1][0], FLAG_STANDS[1][1], 3),stump2);
			while (iterator.hasNext()) {
				Client teamPlayer = (Client) iterator.next();
				if (gameRoom.get(teamPlayer) == 2){
					System.out.println("created hint icons for playernam(e " + teamPlayer.playerName);
					teamPlayer.getActionAssistant().createPlayerHints(10, player.playerId);
					teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##The Enemy has your flag!## ");
				} else {
					teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##Your team has the Enemy flag!## ");
				}
					teamPlayer.getActionAssistant().requestUpdates();	
			}
        }
    }
	private boolean teamHasFlag(int object){
		switch(object){
			case 4900:
			Iterator iterator = gameRoom.keySet().iterator();
			while (iterator.hasNext()) {
				Client teamPlayer = (Client) iterator.next();
				if(gameRoom.get(teamPlayer) == 2){
					if(teamPlayer.playerEquipment[3] == 4037){
						return true;
					}
				} 
			}
				break;
			case 4901:
			Iterator iterators = gameRoom.keySet().iterator();
			while (iterators.hasNext()) {
				Client teamPlayer = (Client) iterators.next();
				if(gameRoom.get(teamPlayer) == 1){
					if(teamPlayer.playerEquipment[3] == 4039){
						return true;
					}
				} 
			}			
			break;
		}
		return false;
	}
	public void pickupFlag(Client player, int object,int x, int y,int h) {
	
	    if (player.playerEquipment[3] > 0) {
            player.getActionAssistant().sendMessage("Please remove your weapon before attempting to get the flag again!");
            return;
        }
		if(player.teleDelay > 0){
			player.getActionAssistant().sendMessage("@red@You can not reach the flag.");
			return;
		}
        switch (object) {
            case 4900: //sara
                if(isSaraTeam(player)){
					return;
				}
				if(teamHasFlag(object)){
					return;
				}
				//player.getActionAssistant().sendMessage("x = "+location[0][0] +" y = "+ location[0][1]);
				if((x != location[0][0]) && (y != location[0][1]))
					return;
				saraFlag = 1;
				addFlag(player,4037);
                break;
            case 4901: //zammy
				if(isZammyTeam(player)){
					return;
				}
				if(teamHasFlag(object)){
					return;
				}
	//player.getActionAssistant().sendMessage("x = "+location[1][0] +" y = "+ location[1][1]);			
			if((x != location[1][0]) && (y != location[1][1]))
					return;
                zammyFlag = 1;
                addFlag(player,4039);
                break;
        }
        Iterator iterator = gameRoom.keySet().iterator();
        while (iterator.hasNext()) {
            Client teamPlayer = (Client) iterator.next();
            teamPlayer.getActionAssistant().object(-1, x, y, 0, 10);
		   if (gameRoom.get(teamPlayer) == (gameRoom.get(player) == 1 ? 2 : 1)) {
				teamPlayer.getActionAssistant().createPlayerHints(10,-1);
				//System.out.println("created hint icons for playername " + teamPlayer.playerName);
				teamPlayer.getActionAssistant().createPlayerHints(10, player.playerId);
				teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##Your flag has been taken!## ");
			} else {
				teamPlayer.getActionAssistant().createObjectHints(-1,-1, -1,-1);
				teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##Your team has the enemy flag!## ");
			}
		teamPlayer.getActionAssistant().requestUpdates();	
		}
    }
	
	 public void endGame() {
        Iterator iterator = gameRoom.keySet().iterator();
        while (iterator.hasNext()) {
            Client player = (Client) iterator.next();
            int team = gameRoom.get(player);
            if (player == null) {
                continue;
            }
			player.stopCwEvent();
			player.dieTimer = 10;
			int newArea = Misc.random(2);
			switch(newArea){
				case 0:
					player.getActionAssistant().movePlayer(2440 + Misc.random(3), 3089 - Misc.random(3), 0);
					break;
				case 1:
					player.getActionAssistant().movePlayer(3209 + Misc.random(6), 3421 + Misc.random(6), 0);
					break;
				default:
				player.getActionAssistant().movePlayer(3497 + Misc.random(6), 3488 + Misc.random(6), 0);
				
			
			}
		    player.getActionAssistant().sendMessage("[@red@CASTLE WARS@bla@] The Castle Wars Game has ended!");
            player.getActionAssistant().createPlayerHints(10, -1);
            deleteGameItems(player);
			if(player.cwDamage > 250){
				if (scores[0] == scores[1]) {
					player.getActionAssistant().sendMessage("Alert##Castle Wars##Tie game!## You gain 20 CastleWars points!");
					player.cwPoints += 20;
				} else if (team == 1) {
					if (scores[0] > scores[1]) {
						player.cwPoints += 30;
						player.getActionAssistant().sendMessage("Alert##Castle Wars##Your team won the game!## You gain 30 CastleWars points!");
					} else if (scores[0] < scores[1]) {
						player.cwPoints += 10;
						player.getActionAssistant().sendMessage("Alert##Castle Wars##Your team lost the game!## You gain 10 CastleWars points!");
					}
				} else if (team == 2) {
					if (scores[1] > scores[0]) {
						player.cwPoints += 30;
						player.getActionAssistant().sendMessage("Alert##Castle Wars##Your team won the game!## You gain 30 CastleWars points!");
					} else if (scores[1] < scores[0]) {
						 player.cwPoints += 10;
						player.getActionAssistant().sendMessage("Alert##Castle Wars##Your team lost the game!## You gain 10 CastleWars points!");
					}
				}
			} else {
				player.getActionAssistant().sendMessage("Alert##Castle Wars##You did not gain any castle war points!## AFKing points is not allowed.");
			}
			GameObject stump2 = new GameObject(FLAG_STANDS[0][0], FLAG_STANDS[0][1], 3, GameObject.Face.NORTH, 10,
							Server.getObjectManager().getDefinition(4902));
							Server.getObjectManager().replaceObject(Server.getObjectManager().getObjectAt(FLAG_STANDS[0][0], FLAG_STANDS[0][1], 3),stump2);
			GameObject stump = new GameObject(FLAG_STANDS[1][0], FLAG_STANDS[1][1], 3, GameObject.Face.NORTH, 10,
							Server.getObjectManager().getDefinition(4903));
							Server.getObjectManager().replaceObject(Server.getObjectManager().getObjectAt(FLAG_STANDS[1][0], FLAG_STANDS[1][1], 3),stump);
							//player.stopMovement();
			player.getActionAssistant().requestUpdates();
			player.cwDamage = 0;
		}
		PlayerManager.getSingleton().sendGlobalMessage("[@red@Castle Wars@blk@] The current game is over! ::Cw to join the next one!");
        resetGame();
    }

    /**
     * reset the game variables
     */
    public void resetGame() {
        zammyFlag = 0;
        saraFlag = 0;
        timeRemaining = -1;
		barricade[0] = 0;
		barricade[1] = 0;
		gameStarted = false;
        System.out.println("Ending Castle Wars game.");
        timeElapsed = 0;
		scores[1] = 0;
		scores[0] = 0; 
		doorObject(0,0);
		doorObject(1,0);
		rebuildTime[0] = 240;
		rebuildTime[1] = 240;
		doors[0] = 100;
		doors[1] = 100;
		for (Barricade x : barricades) {
			Server.getNpcManager().deleteNPC(x.getId());
		}
		barricades.clear();
        gameRoom.clear();
    }

    private GameObject door1;
	private GameObject door2;
	
    private void doorObject(int team,int state){
	Iterator iterator = gameRoom.keySet().iterator();
		switch(team){
			case 0:
				switch(state){
					case 0:
						while (iterator.hasNext()) {
							Client teamPlayer = (Client) iterator.next();
							teamPlayer.getActionAssistant().sendReplaceObject(2372,3119, 4428, -1, 0);
							teamPlayer.getActionAssistant().sendReplaceObject(2373,3119, 4427,-1, 0);
							teamPlayer.getActionAssistant().requestUpdates();
						}
							door1 = new GameObject(2372,3119,0, GameObject.Face.NORTH, 0,
							Server.getObjectManager().getDefinition(4428));	
							door2 = new GameObject(2373,3119,0, GameObject.Face.NORTH, 0,
							Server.getObjectManager().getDefinition(4427));	
							Server.getObjectManager().addObject(door2);
							Server.getObjectManager().addObject(door1);
					break;
					case 1:
					while (iterator.hasNext()) {
							Client teamPlayer = (Client) iterator.next();
							teamPlayer.getActionAssistant().sendReplaceObject(2372,3119, -1, -1, 0);
							teamPlayer.getActionAssistant().sendReplaceObject(2373,3119, -1,-1, 0);
							teamPlayer.getActionAssistant().requestUpdates();
					
						}
						
							Server.getObjectManager().removeObject(Server.getObjectManager().getObjectAt(2372,3119,0));
							Server.getObjectManager().removeObject(Server.getObjectManager().getObjectAt(2373,3119,0));	
					break;
				
				
				}
			break;
			
			case 1:
				switch(state){
					case 0:
					while (iterator.hasNext()) {
							Client teamPlayer = (Client) iterator.next();
							teamPlayer.getActionAssistant().sendReplaceObject(2427,3088,4424, -1, 0);
							teamPlayer.getActionAssistant().sendReplaceObject(2426,3088, 4423, -1, 0);
							teamPlayer.getActionAssistant().requestUpdates();
						}
						door1 = new GameObject(2427,3088,0, GameObject.Face.NORTH, 0,
							Server.getObjectManager().getDefinition(4424));	
							door2 = new GameObject(2426,3088,0, GameObject.Face.NORTH, 0,
							Server.getObjectManager().getDefinition(4423));	
							Server.getObjectManager().addObject(door2);
							Server.getObjectManager().addObject(door1);
						break;
					case 1:
						while (iterator.hasNext()) {
							Client teamPlayer = (Client) iterator.next();
							teamPlayer.getActionAssistant().sendReplaceObject(2427,3088, -1, -1, 0);
							teamPlayer.getActionAssistant().sendReplaceObject(2426,3088, -1, -1, 0);
							
			
							teamPlayer.getActionAssistant().requestUpdates();
						}
								Server.getObjectManager().removeObject(Server.getObjectManager().getObjectAt(2426,3088,0));	
			
							Server.getObjectManager().removeObject(Server.getObjectManager().getObjectAt(2427,3088,0));	
						break;
				}
			break;
		}
		door1 = null;
		door2 = null;
	}
	public void attackDoor(int team){
		switch(team){
			case 0:
					doors[0]-= 14;
					if(doors[0] < 1){
						doors[0] = 0;
						doorObject(0,1);
						zamDoor = true;
					}
				break;
			case 1:
					doors[1]-= 14;
					if(doors[1] < 1){	
						doors[1] = 0;
						doorObject(1,1);
						saraDoor = true;
					
					}
				break;
		}
	
	}
	
	public void returnFlag(int team){
		int objectId = -1;
		switch (team) {
            case 2:
                objectId = 4903;
                zammyFlag = 0;
				zammyFlagTimer = 60;
				Iterator iterator = gameRoom.keySet().iterator();
				location[1][0] = FLAG_STANDS[1][0];
				location[1][1] = FLAG_STANDS[1][1];
				GameObject stump = new GameObject(FLAG_STANDS[1][0], FLAG_STANDS[1][1], 3, GameObject.Face.NORTH, 10,
							Server.getObjectManager().getDefinition(objectId));
							Server.getObjectManager().replaceObject(Server.getObjectManager().getObjectAt(FLAG_STANDS[1][0], FLAG_STANDS[1][1], 3),stump);		
				while (iterator.hasNext()) {
					Client teamPlayer = (Client) iterator.next();
					teamPlayer.getActionAssistant().object(-1, zammyX, zammyY, 0, 10);
					teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##The Zamorak flag has returned!## ");
					if (gameRoom.get(teamPlayer) == 1){
						teamPlayer.getActionAssistant().createObjectHints(-1,-1, -1,-1);
					}
					teamPlayer.getActionAssistant().requestUpdates();	
				}
                break;
            case 1:
                objectId = 4902;
                saraFlag = 0;
				saraFlagTimer = 60;
				location[0][0] = FLAG_STANDS[0][0];
				location[0][1] = FLAG_STANDS[0][1];
                Iterator iterator2 = gameRoom.keySet().iterator();
				GameObject stump2 = new GameObject(FLAG_STANDS[0][0], FLAG_STANDS[0][1], 3, GameObject.Face.NORTH, 10,
							Server.getObjectManager().getDefinition(objectId));
							Server.getObjectManager().replaceObject(Server.getObjectManager().getObjectAt(FLAG_STANDS[0][0], FLAG_STANDS[0][1], 3),stump2);
				while (iterator2.hasNext()) {
					Client teamPlayer = (Client) iterator2.next();
					teamPlayer.getActionAssistant().object(-1, saraX, saraY, 0, 10);
					teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##The Sara flag has returned!## ");
					if (gameRoom.get(teamPlayer) == 2){
						teamPlayer.getActionAssistant().createObjectHints(-1,-1, -1,-1);
					}
					teamPlayer.getActionAssistant().requestUpdates();	
				}
                break;
        }
	
	}
	public void captureFlag(Client player, int wearItem) {
		if(zammyFlag > 0 && saraFlag > 0){
			player.getActionAssistant().sendMessage("@red@You can not capture a flag while your flag is taken!");
			return;
		}
		player.stopCwEvent();
		player.cwDamage += 300;
        int team = gameRoom.get(player);
        int objectId = -1;
        switch (team) {
            case 1:
                location[1][0] = FLAG_STANDS[1][0];
				location[1][1] = FLAG_STANDS[1][1];
				objectId = 4903;
                zammyFlag = 0;
                scores[0]++; //upping the score of a team; team 0 = sara, team 1 = zammy
				Iterator iterator = gameRoom.keySet().iterator();
				GameObject stump = new GameObject(FLAG_STANDS[1][0], FLAG_STANDS[1][1], 3, GameObject.Face.NORTH, 10,
							Server.getObjectManager().getDefinition(objectId));
							Server.getObjectManager().replaceObject(Server.getObjectManager().getObjectAt(FLAG_STANDS[1][0], FLAG_STANDS[1][1], 3),stump);
				while (iterator.hasNext()) {
					Client teamPlayer = (Client) iterator.next();
					
					teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##Sara Team Scores!##"+ player.playerName+" scored the point!");
					if (gameRoom.get(teamPlayer) == 2){
						teamPlayer.getActionAssistant().createPlayerHints(10, -1);
					}
					teamPlayer.getActionAssistant().requestUpdates();	
				}
				stump = null;
                break;
            case 2:
				location[0][0] = FLAG_STANDS[0][0];
				location[0][1] = FLAG_STANDS[0][1];
                objectId = 4902;
                saraFlag = 0;
                scores[1]++; //upping the score of a team; team 0 = sara, team 1 = zammy
                Iterator iterator2 = gameRoom.keySet().iterator();
				GameObject stump2 = new GameObject(FLAG_STANDS[0][0], FLAG_STANDS[0][1], 3, GameObject.Face.NORTH, 10,
							Server.getObjectManager().getDefinition(objectId));
							Server.getObjectManager().replaceObject(Server.getObjectManager().getObjectAt(FLAG_STANDS[0][0], FLAG_STANDS[0][1], 3),stump2);
				while (iterator2.hasNext()) {
					Client teamPlayer = (Client) iterator2.next();
					teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##Zammy Team Scores!##"+ player.playerName+" scored the point!");
					if (gameRoom.get(teamPlayer) == 1){
						teamPlayer.getActionAssistant().createPlayerHints(10, -1);
					}
                    teamPlayer.getActionAssistant().requestUpdates();	
				}
				stump = null;
				stump2 = null;
                break;
        }
        player.getActionAssistant().createPlayerHints(10, -1);
		if(player.playerEquipment[3] == ZAMMY_BANNER || player.playerEquipment[3] == SARA_BANNER){

		player.getOutStream().createFrameVarSizeWord(34);
		player.getOutStream().writeWord(1688);
		player.getOutStream().writeByte(3); // slot
		player.getOutStream().writeWord(-1 + 1);
		player.getOutStream().writeByte(0); // amount
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();
        player.playerEquipment[3] = -1;
        player.playerEquipmentN[3] = 0;
		player.getSpecials().specialBar();
		player.getCombat().calculateBonus();
		player.getCombatEmotes().getPlayerAnimIndex();
		player.getCombatFormulas().sendWeapon();
        player.appearanceUpdateRequired = true;
        player.updateRequired = true;
        player.getActionAssistant().resetItems();
		}
    }
	public void dropFlag(Client player, int flagId) {
	    Iterator iterator = null;
        int object = -1;
		int teamId = -1;
		player.stopCwEvent();
        switch (flagId) {
            case SARA_BANNER: //sara
                saraFlag = 2;
                object = 4900;
				teamId = 2; 
				location[0][0] = player.getX();
				location[0][1] = player.getY();
                break;
            case ZAMMY_BANNER: //zammy
                zammyFlag = 2;
                object = 4901;
				teamId = 1;
				location[1][0] = player.getX();
				location[1][1] = player.getY();
                break;
        }
        iterator = gameRoom.keySet().iterator();
        while (iterator.hasNext()) {
            Client teamPlayer = (Client) iterator.next();
			if(gameRoom.get(teamPlayer) == teamId){
				teamPlayer.getActionAssistant().createObjectHints(player.getX(), player.getY(), 170, 2); // show where the flag is on the map to opposite team.
				teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##Your team dropped the Enemy flag!## ");
			} else {
				teamPlayer.getActionAssistant().createPlayerHints(10, -1);
				teamPlayer.getActionAssistant().sendMessage("Alert##Castle Wars##Your flag was dropped!## ");
			}
				if(teamPlayer.getHeightLevel() != player.getHeightLevel()){
					teamPlayer.getActionAssistant().requestUpdates();
					continue;
				}
            teamPlayer.getActionAssistant().object(object, player.getX(), player.getY(), player.getHeightLevel(), 10);
			teamPlayer.getActionAssistant().requestUpdates();	
		}
		
    }
    
    public void addCapes(Client player, int capeId) {
		player.getOutStream().createFrameVarSizeWord(34);
		player.getOutStream().writeWord(1688);
		player.getOutStream().writeByte(1); // slot
		player.getOutStream().writeWord(capeId + 1);
		player.getOutStream().writeByte(1); // amount
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();
		player.getCombatEmotes().getPlayerAnimIndex();
		player.getCombatFormulas().sendWeapon();
		player.playerEquipment[1] = capeId;
		player.playerEquipmentN[1] = 1;
		player.appearanceUpdateRequired = true;
		player.updateRequired = true;
    }

    public int getZammyPlayersCount() {
        int players = 0;
        Iterator iterator = (!waitingRoom.isEmpty()) ? waitingRoom.values().iterator() : gameRoom.values().iterator();
        while (iterator.hasNext()) {
            if ((Integer) iterator.next() > 1) {
                players++;
            }
        }
        return players;
    }


    public int getSaraPlayersCount() {
        int players = 0;
        Iterator iterator = (!waitingRoom.isEmpty()) ? waitingRoom.values().iterator() : gameRoom.values().iterator();
        while (iterator.hasNext()) {
            if ((Integer) iterator.next() == 1) {
                players++;
            }
        }
        return players;
    }


    public boolean isInCw(Client player) {
        return gameRoom.containsKey(player);
    }


    public boolean isInCwWait(Client player) {
        return waitingRoom.containsKey(player);
    }

	public boolean isSaraTeam(Client player) {
        if (player != null) {
			if(gameRoom.containsKey(player)){
				if(gameRoom.get(player) == 1){
					return true;
				}
			}
		}
		return false;
    }
	
	public boolean isZammyTeam(Client player) {
        if (player != null) {
			if(gameRoom.containsKey(player)){
				if(gameRoom.get(player) == 2){
					return true;
				}
			}
		}
		return false;
    }
	
	public int teamNumber(Client c){
		int team = -1;
		if(c != null){
			if(gameRoom.containsKey(c)){
				team = gameRoom.get(c);
			}
		}
		return team;
	}
    
	public void addFlag(Client player, int flagId) {
		player.startCWEvent();
		player.getOutStream().createFrameVarSizeWord(34);
		player.getOutStream().writeWord(1688);
		player.getOutStream().writeByte(3); // slot
		player.getOutStream().writeWord(flagId + 1);
		player.getOutStream().writeByte(1); // amount
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();
        player.playerEquipment[3] = flagId;
        player.playerEquipmentN[3] = 1;
        player.getSpecials().specialBar();
		player.getCombat().calculateBonus();
		player.getCombatEmotes().getPlayerAnimIndex();
		player.getCombatFormulas().sendWeapon();
        player.appearanceUpdateRequired = true;
        player.updateRequired = true;
    }
	
}