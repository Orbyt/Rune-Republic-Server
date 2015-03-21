package com.rs.worldserver.world;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Player;import com.rs.worldserver.Config;
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

/**
 * Handles still graphics
 * 
 * @author Graham
 * 
 */
public class StillGraphicsManager {

	/**
	 * Nothing to load, as of yet.
	 */
	public StillGraphicsManager() {
	}

	public void stillGraphics(Client curPlr, int absX, int absY,
			int heightLevel, int id, int pause) {
		for (Player p : Server.getPlayerManager().getPlayers()) {
			if (p == null)
				continue;
			if (!p.isActive)
				continue;
			if (p.disconnected)
				continue;
			Client c = (Client) p;
			if (c == curPlr || c.withinDistance(absX, absY, heightLevel)) {
				c.getActionAssistant().sendStillGraphics(id, heightLevel, absY,
						absX, pause);
			}
		}
	}

}
