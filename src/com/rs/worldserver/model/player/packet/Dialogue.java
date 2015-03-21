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

import com.rs.worldserver.model.DialogueMessage;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;

/**
 * Dialogue packet handling
 * 
 * @author Graham
 * 
 */
public class Dialogue implements Packet {

	public static final int CONTINUE = 40;

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		if (packetType == 40) {
			if (client.getDialogueAssistant().isDialogueOpen()) {
				DialogueMessage m = client.getDialogueAssistant()
						.getCurrentDialogue();
				if (m.getType() == DialogueMessage.Type.SKILL_LEVEL_UP
						|| m.getType() == DialogueMessage.Type.NPC_SPEAK
						|| m.getType() == DialogueMessage.Type.PLAYER_SPEAK) {
					client.getDialogueAssistant().doAction(m.getActions()[0]);
				}
			}
		}
	}

}
