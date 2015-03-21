package com.rs.worldserver.model.player;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.model.player.*;

public class Musics {
	private Client 		client;
	
	public static enum CITY_MUSIC {
		VARROCK(169),
		WATERFALL(125);

		int musics;

		CITY_MUSIC(int music) {
			this.musics = music;
		}

		public int getMusic() {
			return musics;
		}
	}

}