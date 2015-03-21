package com.rs.worldserver.content.skill;

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
import com.rs.worldserver.content.Skill;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.Client;

/**
 * Shamelessley stolen from RS2DBase. But I did write some of it!
 * 
 * @author Graham
 * 
 */
public class Magic implements Skill {

	/**
	 * The teleport delay, please leave it at this value as the animation needs
	 * time to complete!
	 */
	private static final int TELEPORT_DELAY = 1400;
	private static final int TELEPORT_DELAY2 = 2500;
	
	/**
	 * Teleport event.
	 * 
	 * @param location
	 * @param player
	 */
		public static void startTeleport(int x, int y, int height, String teleportType, final Client player) {
		if (player.checkBusy())
			return;
		player.setBusy(true);

		final int destX = x, destY = y;

		if(teleportType.equalsIgnoreCase("modern")) {
			player.getActionAssistant().startAnimation(714, 0);
			Server.getStillGraphicsManager().stillGraphics(player,player.getAbsX(), player.getAbsY(), 90, 308, 49);
			player.setCanWalk(false);
			player.resetWalkingQueue();
			if(player.sounds == 1){
				player.getActionAssistant().frame174(202,050,000);
			}
		EventManager.getSingleton().addEvent(player,"magic", new Event() {

			@Override
			public void execute(EventContainer c) {
				player.teleportToX = destX;
				player.teleportToY = destY;
				player.teleportToZ = 0;
				player.getActionAssistant().startAnimation(715, 0);
				c.stop();
			}

			@Override
			public void stop() {
				player.setCanWalk(true);
				player.setBusy(false);
				player.resetWalkingQueue();
			}

		}, TELEPORT_DELAY);
		}
		if(teleportType.equalsIgnoreCase("ancient")) {
			player.getActionAssistant().startAnimation(1979, 0);
			Server.getStillGraphicsManager().stillGraphics(player,player.getAbsX(), player.getAbsY(), 0, 392, 0);
			player.setCanWalk(false);
			player.resetWalkingQueue();
			if(player.sounds == 1){
				player.getActionAssistant().frame174(1048,050,000);
			}
		EventManager.getSingleton().addEvent(player,"magic2", new Event() {

			@Override
			public void execute(EventContainer c) {
				player.teleportToX = destX;
				player.teleportToY = destY;
				player.teleportToZ = 0;
				player.getActionAssistant().startAnimation(0, 0);
				c.stop();
			}

			@Override
			public void stop() {
				player.setCanWalk(true);
				player.setBusy(false);
				player.resetWalkingQueue();
			}

		}, TELEPORT_DELAY2);
		}
		if(teleportType.equalsIgnoreCase("ladderup")) {
			player.getActionAssistant().startAnimation(828, 0);
			Server.getStillGraphicsManager().stillGraphics(player,player.getAbsX(), player.getAbsY(), 90, 0, 0);
			player.setCanWalk(false);
			player.resetWalkingQueue();

		EventManager.getSingleton().addEvent(player,"magic3", new Event() {

			@Override
			public void execute(EventContainer c) {
				player.teleportToX = destX;
				player.teleportToY = destY;
				player.teleportToZ = 0;
				player.getActionAssistant().startAnimation(0, 0);
				c.stop();
			}

			@Override
			public void stop() {
				player.setCanWalk(true);
				player.setBusy(false);
				player.resetWalkingQueue();
			}

		}, TELEPORT_DELAY);
		}
		player.setCanWalk(true);
		player.setBusy(false);
		player.resetWalkingQueue();
	} 
	public static void teleport(String location, final Client player) {
		int x = 0;
		int y = 0;

		if (player.checkBusy())
			return;
		player.setBusy(true);

		if (location.equals("varrock")) {
			x = 3210;
			y = 3424;
		} else if (location.equals("falador")) {
			x = 2964;
			y = 3378;
		} else if (location.equals("lumbridge")) {
			x = 3222;
			y = 3218;
		} else if (location.equals("camelot")) {
			x = 2757;
			y = 3477;
		} else if (location.equals("ardougne")) {
			x = 2662;
			y = 3305;
		} else if (location.equals("watchtower")) {
			x = 2549;
			y = 3113;
		} else if (location.equals("apeatoll")) {
			x = 2755;
			y = 2784;
		} else if (location.equals("home")) {
			x = 3087;
			y = 3494;
		} else if (location.equals("draynorbank")) {
			x = 3092;
			y = 3245;
		} else {
			return;
		}

		final int destX = x, destY = y;

		// 392 for ancients tele 308 for normal tele
		player.getActionAssistant().startAnimation(714, 0);
		Server.getStillGraphicsManager().stillGraphics(player,
				player.getAbsX(), player.getAbsY(), 90, 308, 49);
		player.setCanWalk(false);
		player.resetWalkingQueue();

		EventManager.getSingleton().addEvent(player,"magic2", new Event() {

			@Override
			public void execute(EventContainer c) {
				player.teleportToX = destX;
				player.teleportToY = destY;
				player.teleportToZ = 0;
				player.getActionAssistant().startAnimation(715, 0);
				c.stop();
			}

			@Override
			public void stop() {
				player.setCanWalk(true);
				player.setBusy(false);
				player.resetWalkingQueue();
			}

		}, TELEPORT_DELAY);
		player.setBusy(false);
	}

}
