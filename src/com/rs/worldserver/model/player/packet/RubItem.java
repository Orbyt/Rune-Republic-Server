package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.content.skill.Fletching;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.*;
import com.rs.worldserver.content.skill.*;

public class RubItem implements Packet {
	@Override
	public void handlePacket(Client c, int packetType, int packetSize) {
			int itemId = c.getInStream().readSignedWordA();
			int item2ID = c.getInStream().readSignedWordBigEndian();
			int item2ID3 = c.getInStream().readSignedWordA();
			int item2ID4 = c.getInStream().readUnsignedWord();
			c.println_debug("ItemAction: " + itemId + ","+ item2ID+ ","+ item2ID3+ ","+ item2ID4);
			switch (item2ID3) {
			case 4079:
				c.getActionAssistant().startAnimation(1460);
			break;
			case 1712:
				c.necklace = 1;
				c.getActionAssistant().clearQuestInterface();
				c.getActionAssistant().sendFrame126("Relleka", 2494);
				c.getActionAssistant().sendFrame126("Draynor Village", 2495);
				c.getActionAssistant().sendFrame126("Karamja", 2496);
				c.getActionAssistant().sendFrame126("Al kharid", 2497);
				c.getActionAssistant().sendFrame126("Nowhere", 2498);
				c.getActionAssistant().sendQuestSomething(8143);
				c.getActionAssistant().sendFrame164(2492);
				c.flushOutStream();
				
			break;
			case 3853:
				c.necklace = 2;
				c.getActionAssistant().clearQuestInterface();
				c.getActionAssistant().sendFrame126("Fight Pits", 2494);
				c.getActionAssistant().sendFrame126("Slayer Tower", 2495);
				c.getActionAssistant().sendFrame126("Lumbridge Slayer Cave", 2496);
				c.getActionAssistant().sendFrame126("Experiments", 2497);
				c.getActionAssistant().sendFrame126("Releka Slayer Dungeon", 2498);
				c.getActionAssistant().sendQuestSomething(8143);
				c.getActionAssistant().sendFrame164(2492);
				c.flushOutStream();		
				
			break;
			case 3867:
				c.necklace = 3;
				c.getActionAssistant().clearQuestInterface();
				c.getActionAssistant().sendFrame126("Phoenix", 2494);
				c.getActionAssistant().sendFrame126("To be Added...", 2495);
				c.getActionAssistant().sendFrame126("To be Added...", 2496);
				c.getActionAssistant().sendFrame126("To be Added...", 2497);
				c.getActionAssistant().sendFrame126("To be Added...", 2498);
				c.getActionAssistant().sendQuestSomething(8143);
				c.getActionAssistant().sendFrame164(2492);
				c.flushOutStream();		
				
			break;
			case 15099:
				c.getActionAssistant().deleteItem(15099, 1);
				c.getActionAssistant().addItem(15084, 1);
				c.getActionAssistant().sendMessage("@dre@You put away the Die (55x2).");
			break;
			case 15098:
				c.getActionAssistant().deleteItem(15098, 1);
				c.getActionAssistant().addItem(15084, 1);
				c.getActionAssistant().sendMessage("@dre@You put away the Dice (up to 100).");
			break;
			case 15086:
				c.getActionAssistant().deleteItem(15086, 1);
				c.getActionAssistant().addItem(15084, 1);
				c.getActionAssistant().sendMessage("@dre@You put away the Die (6 sides).");
			break;
			case 15088:
				c.getActionAssistant().deleteItem(15088, 1);
				c.getActionAssistant().addItem(15084, 1);
				c.getActionAssistant().sendMessage("@dre@You put away the Dice (2,6 sides).");
			break;
			case 15090:
				c.getActionAssistant().deleteItem(15090, 1);
				c.getActionAssistant().addItem(15084, 1);
				c.getActionAssistant().sendMessage("@dre@You put away the Die (8 sides).");
			break;
			case 15092:
				c.getActionAssistant().deleteItem(15092, 1);
				c.getActionAssistant().addItem(15084, 1);
				c.getActionAssistant().sendMessage("@dre@You put away the Die (10 sides).");
			break;
			case 15094:
				c.getActionAssistant().deleteItem(15094, 1);
				c.getActionAssistant().addItem(15084, 1);
				c.getActionAssistant().sendMessage("@dre@You put away the Die (12 sides).");
			break;
			case 15096:
				c.getActionAssistant().deleteItem(15096, 1);
				c.getActionAssistant().addItem(15084, 1);
				c.getActionAssistant().sendMessage("@dre@You put away the Die (20 sides).");
			break;
			case 15100:
				c.getActionAssistant().deleteItem(15100, 1);
				c.getActionAssistant().addItem(15084, 1);
				c.getActionAssistant().sendMessage("@dre@You put away the Die (4 sides).");
			break;
			case 115:
			case 117:
			case 119:
			case 121:
			case 123:
			case 125:
			case 127:
			case 129:
			case 131:
			case 133:
			case 135:
			case 137:
			case 139:
			case 141:
			case 143:
			case 145:
			case 147:
			case 149:
			case 151:
			case 153:
			case 155:
			case 157:
			case 159:
			case 161:
			case 163:
			case 165:
			case 167:
			case 169:
			case 171:
			case 173:
			case 175:
			case 177:
			case 179:
			case 181:
			case 183:
			case 185:
			case 189:
			case 191:
			case 193:
			case 2452:
			case 2454:
			case 2456:
			case 2458:
			case 3008:
			case 3010:
			case 3012:
			case 3014:
			case 3016:
			case 3018:
			case 3020:
			case 3022:
			case 3024:
			case 3026:
			case 3028:
			case 3030:
			case 3032:
			case 3034:
			case 3036:
			case 3038:
			case 3040:
			case 3042:
			case 3044:
			case 3046:
			case 6685:
			case 6687:
			case 6689:
			case 6691:
			case 2428:
			case 2430:
			case 2432:
			case 2434:
			case 2436:
			case 2438:
			case 2440:
			case 2442:
			case 2444:
			case 2446:
			case 2448:
			case 2450:
			case 113:
				c.getActionAssistant().deleteItem(item2ID3, c.getActionAssistant().getItemSlot(item2ID3), 1);
				c.getActionAssistant().addItem(229, 1);
			break;		
			}
		return;

	}
}

