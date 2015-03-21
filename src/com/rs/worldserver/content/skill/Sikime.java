//package com.rs.worldserver.content.skill;
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
//import com.rs.worldserver.Server;
//import com.rs.worldserver.Config;
//import com.rs.worldserver.model.player.Client;
//import com.rs.worldserver.model.player.Command;
//import com.rs.worldserver.util.*;
//import com.rs.worldserver.util.CheatProcessor;
//import com.rs.worldserver.model.Item;
//import com.rs.worldserver.model.ItemDefinition;
//
//public class Sikime implements Command {
//
//	@Override
//	public void execute(Client c, String command) {
//			String cmd[] = command.split(" ");
//			if (Integer.valueOf(cmd[1]) == null) {
//				return;
//			}
//			int amount = 1;
//			try {
//				amount = Integer.valueOf(cmd[2]);
//				if (amount > 1 && !Item.itemIsNote[Integer.valueOf(cmd[1])] && !Item.itemStackable[Integer.valueOf(cmd[1])]) {
//					for (int i = 0; i < amount; i++) {
//						if (!c.getActionAssistant().addItem(Integer.valueOf(cmd[1]))) {
//							break;
//						}
//					}
//				} else {
//					c.getActionAssistant().addItem(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
//				}
//			} catch (Exception e) {
//				c.getActionAssistant().addItem(Integer.valueOf(cmd[1]));
//			}		
//	}
//}
