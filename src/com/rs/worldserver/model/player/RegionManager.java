package com.rs.worldserver.model.player;
import java.util.LinkedList;
import java.util.List;
import com.rs.worldserver.io.Stream;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.player.RegionManager.Region.Tile;

/**
 * Handles a lazily allocated
 * map region system
 * 
 */
public class RegionManager {

    //Must be an EVEN number, 2, 4, 6, ect
    public static final int MAX_RENDER_DISTANCE = 64;
    public static final RegionManager reg = new RegionManager();
    public static final int WORLD_DEPTH = 4;
    public static final int REGION_SIZE = 128;
    public static final int WORLD_SIZE = Short.MAX_VALUE / REGION_SIZE;

    public RegionManager() {
    }
    private Region[][][] regions;

    {
        regions = new Region[WORLD_SIZE][WORLD_SIZE][WORLD_DEPTH];
    }

    public void addPlayer(Player p, int absX, int absY, int absZ) {
        Tile tile = createIfAbsent(absX, absY, absZ);
        tile.players.add(p);
    }

    public void delPlayer(Player p, int absX, int absY, int absZ) {
        Tile tile = getTile(absX, absY, absZ);
        if (tile != null) {
            tile.players.remove(p);
        }
    }

    public void movePlayer(Player p, int fX, int fY, int fZ, int tX, int tY, int tZ) {
        if (fX > -1 && fY > -1) {
            delPlayer(p, fX, fY, fZ);
        }
        addPlayer(p, tX, tY, tZ);
    }

    private Tile createIfAbsent(int absX, int absY, int absZ) {
        Region r = getRegion(absX, absY, absZ);
        if (r == null) {
            r = setRegion(new Region(), absX, absY, absZ);
        }
        return r.getTile(
                toRegionLocal(absX),
                toRegionLocal(absY));
    }

    public Tile getTile(int absX, int absY, int absZ) {
        Region r = getRegion(absX, absY, absZ);
        return r == null ? null : r.getTile(
                toRegionLocal(absX),
                toRegionLocal(absY));
    }

    private Region setRegion(Region r, int absX, int absY, int absZ) {
        return (regions[toRegion(absX)][toRegion(absY)][absZ] = r);
    }

    private Region getRegion(int absX, int absY, int absZ) {
        return regions[toRegion(absX)][toRegion(absY)][absZ];
    }

    private int toRegion(double abs) {
        return (int) Math.floor(abs / REGION_SIZE);
    }

    private int toRegionLocal(int abs) {
	int sum = abs - ((abs / toRegion(abs)) * toRegion(abs));
		if(sum == 0){
			return 1;
		} else {
			return sum;
		}
    }

    public static class Region {

        private Tile[][] tiles;

        {
            tiles = new Tile[REGION_SIZE][REGION_SIZE];
            for (int x = 0; x < REGION_SIZE; x++) {
                for (int y = 0; y < REGION_SIZE; y++) {
                    tiles[x][y] = new Tile();
                }
            }
        }

        private Tile getTile(int regX, int regY) {
            return tiles[regX][regY];
        }

        public static class Tile {

            private Stream cache;
            private List<Player> players;

            {
                players = new LinkedList<Player>();
            }

            public List<Player> getPlayers() {
                return players;
            }
        }
    }
}