package com.rs.worldserver.model.player.packet;

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

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;

/**
 * Client GUI actions
 * 
 * @author Graham
 * 
 */
public class ClientGUIActions implements Packet {

	public static final int FOCUS_CHANGE = 3, CAMERA_ANGLE = 86, CLICK = 241;

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		// TODO Auto-generated method stub
		if (packetType == CLICK) {
			//client.getActionAssistant().follow(0, 3, 1);
		}		
	}

}
