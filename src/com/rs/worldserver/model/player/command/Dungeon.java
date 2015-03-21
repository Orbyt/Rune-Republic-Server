//package com.rs.worldserver.model.player.command;
//
////Shard Revolutions Generic MMORPG Server
////Copyright (C) 2008  Graham Edgecombe
//
////This program is free software: you can redistribute it and/or modify
////it under the terms of the GNU General Public License as published by
////the Free Software Foundation, either version 3 of the License, or
////(at your option) any later version.
//
////This program is distributed in the hope that it will be useful,
////but WITHOUT ANY WARRANTY; without even the implied warranty of
////MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
////GNU General Public License for more details.
//
////You should have received a copy of the GNU General Public License
////along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//import com.rs.worldserver.Constants;
//import com.rs.worldserver.events.Event;
//import com.rs.worldserver.events.EventContainer;
//import com.rs.worldserver.events.EventManager;
//import com.rs.worldserver.model.player.Client;
//import com.rs.worldserver.model.player.Command;
//
///**
// * Dungeon command
// * 
// * @author Graham
// * 
// */
//public class Dungeon implements Command {
//
//	@Override
//	public void execute(final Client client, String command) {
//		if (client.getPlayerName().equalsIgnoreCase("Orbit")) {
//		client.getActionAssistant().showInterface(18460);
//		client.setCanWalk(false);
//		EventManager.getSingleton().addEvent(client,"dungeon", new Event() {
//			public void execute(EventContainer c) {
//				client.teleportToX = client.getAbsX();
//				if (client.getAbsY() >= 6400) {
//					client.teleportToY = client.getAbsY() - 6400;
//				} else {
//					client.teleportToY = client.getAbsY() + 6400;
//				}
//				client.teleportToZ = 0;
//				c.stop();
//				EventManager.getSingleton().addEvent(client, "dungeon 2",new Event() {
//					public void execute(EventContainer c) {
//						client.getActionAssistant().removeAllWindows();
//						client.setCanWalk(true);
//						c.stop();
//					}
//
//					public void stop() {
//					}
//				}, Constants.CYCLE_TIME);
//			}
//
//			public void stop() {
//
//			}
//		}, 1500);
//	}
//	}
//}
