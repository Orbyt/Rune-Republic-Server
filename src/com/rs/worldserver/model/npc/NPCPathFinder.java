package com.rs.worldserver.model.npc;

import java.util.HashMap;
import java.util.Map;
import java.util.*;
import com.rs.worldserver.io.Stream;
import com.rs.worldserver.model.Entity;
import com.rs.worldserver.model.FloorItem;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.npc.NPCKiller;
import com.rs.worldserver.world.AnimationManager;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.ActionAssistant;
import com.rs.worldserver.model.player.CastleWars;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.model.ItemDefinition;
import com.rs.worldserver.world.ItemManager;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.*;


public class NPCPathFinder {
	
	private static final int NORTH = 0, EAST = 1,  SOUTH = 2, WEST = 3;
	
	public static void follow(NPC npc, Client following) {
		
		/** the tiles the npc is occupying **/
		Tile[] npcTiles = TileManager.getTiles(npc);
		
		/** locations **/
		int[] npcLocation = TileManager.currentLocation(npc);
		int[] followingLocation = TileManager.currentLocation(following);
		
		/** test 4 movements **/
		boolean[] moves = new boolean[4];
		
		int dir = -1;//the direction to go.
		int distancePet = 0;
		int distance = TileManager.calculateDistance(npc, following);
		if (npc.petOwner != 0) {
			distancePet = following.distanceToPoint(npc.absX,npc.absY);
		}
		if (distance > 16 && npc.petOwner == 0) {
			//npc.following = null;
			npc.facePlayer(0);
			npc.randomWalk = true;
	      	npc.underAttack = false;
			npc.killerId = 0;
			npc.killedBy = 0;
			return;
		}
		else if (npc.petOwner != 0 && distancePet  >16) {
			Client k = (Client)	PlayerManager.getSingleton().getPlayers()[npc.petOwner];
			if (k != null) {
				npc.absX = k.absX;
				npc.absY = k.absY;
				npc.heightLevel = k.heightLevel;
				return;
			}
		}
		npc.facePlayer(following.playerId);
		if((npc.getAbsX() < npc.makeX + Config.NPC_FOLLOW_DISTANCE) && (npc.getAbsX() > npc.makeX - Config.NPC_FOLLOW_DISTANCE) && 
			(npc.getAbsY() < npc.makeY + Config.NPC_FOLLOW_DISTANCE) && (npc.getAbsY() > npc.makeY - Config.NPC_FOLLOW_DISTANCE) || npc.petOwner != 0) {
			if(npc.heightLevel != following.heightLevel)
				return;
			if(npc.freezeTimer > 0)
				return;
			if (following == null) {
				return;
			}
			if (following.respawnTimer > 0) {
				npc.facePlayer(0);
				npc.randomWalk = true;
				npc.underAttack = false;
				npc.killerId = 0;
				npc.killedBy = 0;			
				return;
			}	
		/*if (npc.petOwner != 0 && distancePet  >16) {
			Client k = (Client)	PlayerManager.getSingleton().getPlayers()[npc.petOwner];
			if (k != null) {
				npc.absX = k.absX;
				npc.absY = k.absY;
				npc.heightLevel = k.heightLevel;
				return;
			}
		}*/
			if (distance > 1) {
				
				/** set all our moves to true **/
				for (int i = 0; i < moves.length; i++) {
					moves[i] = true;
				}
				
				/** remove false moves **/
				if (npcLocation[0] < followingLocation[0]) {
					moves[EAST] = true;	
					moves[WEST] = false;
				} else if (npcLocation[0] > followingLocation[0]) {
					moves[WEST] = true;	
					moves[EAST] = false;	
				} else {
					moves[EAST] = false;	
					moves[WEST] = false;
				}
				if (npcLocation[1] > followingLocation[1]) {
					moves[SOUTH] = true;
					moves[NORTH] = false;
				} else if (npcLocation[1] < followingLocation[1]) {
					moves[NORTH] = true;	
					moves[SOUTH] = false;
				} else {
					moves[NORTH] = false;	
					moves[SOUTH] = false;
				}	
				for (Tile tiles : npcTiles) {
					if (tiles.getTile2()[0] == following.getAbsX()) { //same x line
						moves[EAST] = false;
						moves[WEST] = false;
					} else if (tiles.getTile2()[1] == following.getAbsY()) { //same y line
						moves[NORTH] = false;
						moves[SOUTH] = false;
					}
				}
				boolean[] blocked = new boolean[3];
				
				if (moves[NORTH] && moves[EAST]) {
					for (Tile tiles : npcTiles) {
						if (Region.blockedNorth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							blocked[0] = true;
						}
						if (Region.blockedEast(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							blocked[1] = true;
						}
						if (Region.blockedNorthEast(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							blocked[2] = true;
						}
					}
					if (!blocked[2] && !blocked[0] && !blocked[1]) {  //northeast
						dir = 2;
					} else if (!blocked[0]) { //north
						dir = 0;
					} else if (!blocked[1]) { //east
						dir = 4;
					}	
					
				} else if (moves[NORTH] && moves[WEST]) {
					for (Tile tiles : npcTiles) {
						if (Region.blockedNorth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							blocked[0] = true;
						}
						if (Region.blockedWest(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							blocked[1] = true;
						}
						if (Region.blockedNorthWest(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							blocked[2] = true;
						}
					}
					if (!blocked[2] && !blocked[0] && !blocked[1]) { //north-west
						dir = 14;
					} else if (!blocked[0]) { //north
						dir = 0;
					} else if (!blocked[1]) { //west
						dir = 12;
					}	
				} else if (moves[SOUTH] && moves[EAST]) {
					for (Tile tiles : npcTiles) {
						if (Region.blockedSouth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							blocked[0] = true;
						}
						if (Region.blockedEast(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							blocked[1] = true;
						}
						if (Region.blockedSouthEast(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							blocked[2] = true;
						}
					}
					if (!blocked[2] && !blocked[0] && !blocked[1]) { //south-east
						dir = 6; 
					} else if (!blocked[0]) { //south
						dir = 8;
					} else if (!blocked[1]) { //east
						dir = 4;
					}	
				} else if (moves[SOUTH] && moves[WEST]) {
					for (Tile tiles : npcTiles) {
						if (Region.blockedSouth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							blocked[0] = true;
						}
						if (Region.blockedWest(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							blocked[1] = true;
						}
						if (Region.blockedSouthWest(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							blocked[2] = true;
						}
					}
					if (!blocked[2] && !blocked[0] && !blocked[1]) { //south-west
						dir = 10; 
					} else if (!blocked[0]) { //south
						dir = 8;
					} else if (!blocked[1]) { //west
						dir = 12;
					}	
					
				} else if (moves[NORTH]) {
					dir = 0;
					for (Tile tiles : npcTiles) {
						if (Region.blockedNorth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							dir = -1;
						}
					}
				} else if (moves[EAST]) {
					dir = 4;
					for (Tile tiles : npcTiles) {
						if (Region.blockedEast(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							dir = -1;
						}
					}
				} else if (moves[SOUTH]) {
					dir = 8;
					for (Tile tiles : npcTiles) {
						if (Region.blockedSouth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							dir = -1;
						}
					}
				} else if (moves[WEST]) {
					dir = 12;
					for (Tile tiles : npcTiles) {
						if (Region.blockedWest(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
							dir = -1;
						}
					}
				}
			} else if (distance == 0) {
				for (int i = 0; i < moves.length; i++) {
					moves[i] = true;
				}
				for (Tile tiles : npcTiles) {
					
					if (Region.blockedNorth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						moves[NORTH] = false;
					}
					if (Region.blockedEast(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						moves[EAST] = false;
					}
					if (Region.blockedSouth(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						moves[SOUTH] = false;
					}
					if (Region.blockedWest(tiles.getTileX(), tiles.getTileY(), tiles.getTileHeight())) {
						moves[WEST] = false;
					}
				}
				int randomSelection = Misc.random(3);
				
				if (moves[randomSelection]) {
					dir = randomSelection * 4;
				} else if (moves[NORTH]) {
					dir = 0;
				} else if (moves[EAST]) {
					dir = 4;
				} else if (moves[SOUTH])	{
					dir = 8;
				} else if (moves[WEST]) {	
					dir = 12;
				}
			}
			if (dir == -1) {
				return;
			}
			dir >>= 1;	
				
			if (dir < 0) {
				return;
			}
			int x = Misc.directionDeltaX[dir];
			int y = Misc.directionDeltaY[dir];
			npc.setWalk(npcLocation[0] + x, npcLocation[1] + y, false);
		}	
	}

}