package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.content.skill.Fletching;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.*;
import com.rs.worldserver.content.skill.*;
/**
 * Item action
 * 
 * @author Jonas
 * 
 */
public class ItemOnPlayer implements Packet {

	@Override
	public void handlePacket(Client c, int packetType, int packetSize) {
		    int getSlotA;
			int getSlotB;
			int getSlotC;

			getSlotA = c.getInStream().readSignedWordBigEndianA();
			getSlotB = c.getInStream().readSignedWordBigEndian();
			getSlotC = c.getInStream().readUnsignedWordA();
			
			int crackerSlot = c.getInStream().readSignedWordBigEndian();
			int usedOn = (int) (Misc.hexToInt(c.getInStream().buffer, 3, 1) / 1000);

			int random = (int)(2 * Math.random()) + 1;
			int crackerId = c.playerItems[crackerSlot];

			crackerId -= 1;
			Client c2 = (Client) PlayerManager.getSingleton().getPlayers()[usedOn];
			if ((crackerId == 962) && c.getActionAssistant().playerHasItem(962,1)) {
				c.getActionAssistant().sendMessage("You pull a Christmas cracker...");
				c2.getActionAssistant().sendMessage("Someone pulls a Christmas Cracker on you.");
				c.getActionAssistant().deleteItem(crackerId, crackerSlot, 1);
			if (random == 1) {
				c.getActionAssistant().addItem(c.getActionAssistant().randomHat(), 1);
				c.getActionAssistant().sendMessage("Yay! I got a Party Hat!");
				c2.getActionAssistant().sendMessage("Aww.. The other person got the party hat..");
			} else {
				c.getActionAssistant().sendMessage("Aww.. The other person got the party hat..");
				c2.getActionAssistant().addItem(c.getActionAssistant().randomHat(), 1);
				c2.getActionAssistant().sendMessage("Yay! I got the party hat!");
				}
			}
		
	}
}