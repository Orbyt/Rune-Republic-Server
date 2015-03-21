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

import com.rs.worldserver.content.skill.Prayer;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;

/**
 * Bury bones packet.
 * 
 * @author Graham
 * 
 */
public class BuryBones implements Packet {

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		@SuppressWarnings("unused")
		int buryA = client.getInStream().readSignedWordBigEndianA();
		int burySlot = (client.getInStream().readUnsignedWord() - 128);
		@SuppressWarnings("unused")
		int buryItemID = (client.getInStream().readSignedWordBigEndianA() - 128);
		if (!(burySlot >= 0 && burySlot < client.playerItems.length))
			return;
		// bury bones hook
		if (Prayer.buryBones(client, burySlot)) {
		}
	}

}
