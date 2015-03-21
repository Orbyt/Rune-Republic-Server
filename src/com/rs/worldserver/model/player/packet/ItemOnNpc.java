package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.content.skill.Fletching;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.*;
import com.rs.worldserver.content.skill.*;
import com.rs.worldserver.util.BanProcessor;
import java.io.IOException;

/**
 * Item action
 * 
 * @author Jonas
 * 
 */
public class ItemOnNpc implements Packet {

	@Override
	public void handlePacket(Client c, int packetType, int packetSize) {
		//c.getActionAssistant().sendMessage("test!");
			//PlayerManager.getSingleton().saveGame(c);
			int itemId = c.getInStream().readUnsignedWordA();
			int npcSlot = c.getInStream().readUnsignedWordA();
			int itemSlot = c.getInStream().readSignedByte();
			NPC npc = Server.getNpcManager().getNPC(npcSlot);
			System.out.println(itemId);
			System.out.println(npcSlot);
			System.out.println(npc.getDefinition().getType());
			System.out.println(itemSlot);
			
			switch(npc.getDefinition().getType()){
				case 1532:
					if(itemId == 4045){
						npc.hitDiff = 100;
						npc.hit = 100;
						npc.hp -= 100;
						npc.setUpdateRequired(true);
						npc.hitUpdateRequired = true;
						c.getActionAssistant().deleteItem(4045, itemSlot, 1);
						c.getActionAssistant().sendMessage("You used an explosive potion!");
					}
					break;
			
			}
	}
}