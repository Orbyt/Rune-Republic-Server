package com.rs.worldserver.model.player.packet;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.world.PlayerManager;

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
 * Pickup item packet
 * 
 * @author Graham
 * 
 */
public class PickupItem implements Packet {

	@Override
	public void handlePacket(final Client client, int packetType, int packetSize) {
		final int itemY = client.getInStream().readSignedWordBigEndian();
		final int itemID = client.getInStream().readUnsignedWord();
		final int itemX = client.getInStream().readSignedWordBigEndian();

		/*client.println_debug("pickupItem: " + itemX + "," + itemY + " itemID: "
				+ itemID);*/
		if(client.blackMarks  > 0) {
			client.getActionAssistant().sendMessage("This action is disabled while jailed");
			return;
		}
		if(!client.respawning) {
			//if (client.withinDistance(itemX, itemY, client.absX, client.absY, 1)) {
			if(client.absX==itemX && client.absY==itemY) {
				client.getActionAssistant().pickUpItem(itemX, itemY, itemID);
				return;
			}
			client.atObjectStartTime=System.currentTimeMillis();
			EventManager.getSingleton().addEvent(client,"atobject"+itemID, new Event() {
				private long eventStartTime=client.atObjectStartTime;
				public void execute(EventContainer ec) {
					if (client.atObjectStartTime > eventStartTime) {
						ec.stop();
						return;
					}
					//if (!client.withinDistance(itemX, itemY, client.absX, client.absY, 1)) {
					if(client.absX!=itemX || client.absY!=itemY) {
						return;
					}
					client.getActionAssistant().pickUpItem(itemX, itemY, itemID);
					ec.stop();
				}
				@Override
				public void stop() {}
			}, 100);
		}
			//PlayerManager.getSingleton().saveGame(client);
	}

}
