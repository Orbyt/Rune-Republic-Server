package com.rs.worldserver;

import com.rs.worldserver.Tile;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.npc.NPC;


public class TileManager {
	
	public static Tile generate(Client entity, int x, int y, int z) {
		return new Tile(entity, new int[] {x, y, z});
	}
	public static Tile generate(Object entity, int x, int y, int z) {
		return new Tile(entity, new int[] {x, y, z});
	}
	
	public static Tile generate(int x, int y, int z) {
		return new Tile(x, y, z);
	}
	public static Tile[] getTiles(Client client, int x, int y, int z) {
		
		int size = 1, tileCount = 0;
				
		Tile[] tiles = new Tile[size * size];
		
		if (tiles.length == 1)
			tiles[0] = generate(client, x, y, z);
		else {
			for (int x1 = 0; x1 < size; x1++)
				for (int y1 = 0; y1 < size; y1++)
					tiles[tileCount++] = generate(client, client.getAbsX() + x1, client.getAbsY() + y1, client.heightLevel);
		}	
		return tiles;
	}

	public static Tile[] getTiles(NPC npc, int x, int y, int z) {
		
		int size = 1, tileCount = 0;
				
		Tile[] tiles = new Tile[size * size];
		
		if (tiles.length == 1)
			tiles[0] = generate(npc, x, y, z);
		else {
			for (int x1 = 0; x1 < size; x1++)
				for (int y1 = 0; y1 < size; y1++)
					tiles[tileCount++] = generate(npc, npc.getAbsX() + x1, npc.getAbsY() + y1, npc.heightLevel);
		}	
		return tiles;
	}
	
	public static Tile[] getTiles(NPC entity) {
		
		int size = 1, tileCount = 0;
		
		//size = NPC.getNPCSize(((NPC)entity).getDefinition().getType());
		
		Tile[] tiles = new Tile[size * size];
		
		if (tiles.length == 1) 
			tiles[0] = generate(entity.getAbsX(), entity.getAbsY(), entity.getHeightLevel());
		else {
			for (int x = 0; x < size; x++) 
				for (int y = 0; y < size; y++) 
					tiles[tileCount++] = generate(entity.getAbsX() + x, entity.getAbsY() + y, entity.getHeightLevel());
		}	
		return tiles;
	}	
	
	
	public static int calculateDistance(Client entity, NPC following) {
		
		Tile[] tiles = getTiles(entity, entity.getAbsX(), entity.getAbsY(),entity.heightLevel);
		
		int[] location = currentLocation(entity);
		int[] pointer = new int[tiles.length > 1 ? tiles.length : 1];
		
		int lowestCount = 20, count = 0;
		
		for (Tile newtiles : tiles) {
			if (newtiles.getTile() == location)
				pointer[count++] = 0;
			else 
				pointer[count++] = TileManager.calculateDistance(newtiles, following);
		}
		for (int i = 0; i < pointer.length; i++)
			if (pointer[i] < lowestCount)
				lowestCount = pointer[i];
		
		return lowestCount;
	}
	public static int calculateDistance(NPC entity, Client following) {
		
		Tile[] tiles = getTiles(entity, entity.getAbsX(), entity.getAbsY(),entity.heightLevel);
		
		int[] location = currentLocation(entity);
		int[] pointer = new int[tiles.length > 1 ? tiles.length : 1];
		
		int lowestCount = 20, count = 0;
		
		for (Tile newtiles : tiles) {
			if (newtiles.getTile() == location)
				pointer[count++] = 0;
			else 
				pointer[count++] = TileManager.calculateDistance(newtiles, following);
		}
		for (int i = 0; i < pointer.length; i++)
			if (pointer[i] < lowestCount)
				lowestCount = pointer[i];
		
		return lowestCount;
	}	
	public static int calculateDistance(Client entity, Client following) {
		
		Tile[] tiles = getTiles(entity, entity.getAbsX(), entity.getAbsY(),entity.heightLevel);
		
		int[] location = currentLocation(entity);
		int[] pointer = new int[tiles.length > 1 ? tiles.length : 1];
		
		int lowestCount = 20, count = 0;
		
		for (Tile newtiles : tiles) {
			if (newtiles.getTile() == location)
				pointer[count++] = 0;
			else 
				pointer[count++] = TileManager.calculateDistance(newtiles, following);
		}
		for (int i = 0; i < pointer.length; i++)
			if (pointer[i] < lowestCount)
				lowestCount = pointer[i];
		
		return lowestCount;
	}
	public static int calculateDistance(Tile location, NPC other) {
		int X = Math.abs(location.getTile()[0] - other.getAbsX());
		int Y = Math.abs(location.getTile()[1] - other.getAbsY());
		return X > Y ? X : Y;
	}
	
	public static int calculateDistance(Tile location, Client other) {
		int X = Math.abs(location.getTile()[0] - other.getAbsX());
		int Y = Math.abs(location.getTile()[1] - other.getAbsY());
		return X > Y ? X : Y;
	}
	
	public static int calculateDistance(int[] location, Client other) {
		int X = Math.abs(location[0] - other.getAbsX());
		int Y = Math.abs(location[1] - other.getAbsY());
		return X > Y ? X : Y;
	}
	
	public static int calculateDistance(int[] location, int[] other) {
		int X = Math.abs(location[0] - other[0]);
		int Y = Math.abs(location[0] - other[1]);
		return X > Y ? X : Y;
	}
	
	public static int[] currentLocation(Client entity) {
		int[] currentLocation = new int[3];
		if(entity != null) {
			currentLocation[0] = entity.getAbsX();
			currentLocation[1] = entity.getAbsY();
			currentLocation[2] = entity.getHeightLevel();
		}
		return currentLocation;
	}

	public static int[] currentLocation(NPC entity) {
		int[] currentLocation = new int[3];
		if(entity != null) {
			currentLocation[0] = entity.getAbsX();
			currentLocation[1] = entity.getAbsY();
			currentLocation[2] = entity.getHeightLevel();
		}
		return currentLocation;
	}
	
	public static int[] currentLocation(Tile tileLocation) {
		
		int[] currentLocation = new int[3];
		
		if(tileLocation != null) {
			currentLocation[0] = tileLocation.getTile()[0];
			currentLocation[1] = tileLocation.getTile()[1];
			currentLocation[2] = tileLocation.getTile()[2];
		}
		return currentLocation;
	}
	
	public static boolean inAttackablePosition(Client entity, Client following) {
		
		Tile[] tiles = getTiles(entity, entity.getAbsX(), entity.getAbsY(), entity.heightLevel);
		Tile[] followingTiles = getTiles(following, following.getAbsX(), following.getAbsY(),following.heightLevel);
		
		for (Tile followTile : followingTiles) {
			for (Tile followTile2 : tiles) {
				if (followTile2.getTile()[0] - 1 == followTile.getTile()[0] && followTile2.getTile()[1] == followTile.getTile()[1] || 
					followTile2.getTile()[0] + 1 == followTile.getTile()[0] && followTile2.getTile()[1] == followTile.getTile()[1] ||
					followTile2.getTile()[1] - 1 == followTile.getTile()[1] && followTile2.getTile()[0] == followTile.getTile()[0] || 
					followTile2.getTile()[1] + 1 == followTile.getTile()[1] && followTile2.getTile()[0] == followTile.getTile()[0])
					return true;
			}
		}
		return false;
	}
	
}