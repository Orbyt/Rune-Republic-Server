package com.rs.worldserver.model.player.command;

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

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.TopPker;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
/**
 * Pickup command
 * 
 * @author Graham
 * 
 */
public class Update implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.playerName.equalsIgnoreCase("Orbit")) {
			if (command.length() > 7) {

				for (Player p : Server.getPlayerManager().getPlayers()) {
						if (p == null)
							continue;
						if (!p.isActive || p.disconnected)
							continue;
							Client d = (Client) p;
				
						Server.getPlayerManager().updateSeconds = (Integer.parseInt(command.substring(7)) + 1);
						Server.getPlayerManager().updateAnnounced = false;
						Server.getPlayerManager().updateRunning = true;
						Server.getPlayerManager().updateStartTime = System.currentTimeMillis();
						startUpdate(d,Integer.parseInt(command.substring(7)));
						d.cancelTasks();
						PlayerManager.getSingleton().saveGame(d);
						d.getActionAssistant().sendMessage("[@red@SERVER@bla@]: Update In Progress - All Tasks Cancelled");
				}
			}
		}
	}
	
	
		private void startUpdate(final Client client, final int timer) {
			EventManager.getSingleton().addEvent(client,"degrade", new Event() {

			int time = timer;
			@Override
			public void execute(EventContainer c) {
				if (client.disconnected || client == null) {
					c.stop();
					return;
				}
				client.getActionAssistant().sendMessage("Alert##Update##An update is incoming Please Log out!## " + time +" seconds till update!");
				time--;
				}

				@Override
				public void stop() {
				}
			}, 1000);
		}
}
