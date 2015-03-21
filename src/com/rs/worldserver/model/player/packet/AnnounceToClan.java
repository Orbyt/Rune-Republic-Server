package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.content.skill.Fletching;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.*;
import com.rs.worldserver.content.skill.*;
import java.util.Collections;
import java.util.Comparator;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Server;
import java.util.ArrayList;
import java.util.Arrays;

/**
*
* @Author Orbit 2013
*
*/

public class AnnounceToClan implements Packet {
	@Override
	public void handlePacket(Client c, int packetType, int packetSize) {
	int itemId = c.getInStream().readSignedWordA();
	switch (itemId) {
	case 15098: //Up to 100 //Percentile Dice
	if (!c.inDice()) {
		c.getActionAssistant().sendMessage("You can only dice in the Dicing Arena.");
		return;
	}
	/*if (c.playerRights != 12) {
	if (!c.playerName.equalsIgnoreCase("Orbit")) {
		c.getActionAssistant().Send("@dre@You must obtain the Dice Rank to Host!");
		return;
	}
	}*/
	final int roll = Misc.random(100);
	if (ClanChat.isInClan(c) && roll >= 1) {
	if (System.currentTimeMillis() - c.diceDelay >= 5000) {
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(2075);
		ClanChat.sendClanMessage(c,"[Official Dicer]: " +c.playerName+ " has rolled ["+roll+"%] on the percentile dice.");
		c.forcedChat("[Official Dicer][CC Roll]: " +c.playerName+ " has rolled ["+roll+"%] on the percentile dice.");
		c.diceDelay = System.currentTimeMillis();
		}
		} else if (ClanChat.isInClan(c) && roll == 0) {
			c.getActionAssistant().sendMessage("Try Again. The Dice Rolled 0.");
		} else {
			c.getActionAssistant().sendMessage("Please Join a Clan Chat to Roll to Clan.");
		}
	break;
	case 15086: //Die 6 sides
	if (!c.inDice()) {
		c.getActionAssistant().sendMessage("You can only dice in the Dicing Arena.");
		return;
	}
	final int roll1 = Misc.random(6);
	if (ClanChat.isInClan(c) && roll1 >= 1) {
	if (System.currentTimeMillis() - c.diceDelay >= 5000) {
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(2072);
		ClanChat.sendClanMessage(c,"[Official Dicer]: " +c.playerName+ " has rolled ["+roll1+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.forcedChat("[Official Dicer][CC Roll]: " +c.playerName+ " has rolled ["+roll1+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.diceDelay = System.currentTimeMillis();
		}
		} else if (ClanChat.isInClan(c) && roll1 == 0) {
			c.getActionAssistant().sendMessage("Try Again. The Dice Rolled 0.");
		} else {
			c.getActionAssistant().sendMessage("Please Join a Clan Chat to Roll to Clan.");
		}
	break;
	case 15088: //Dice (2, 6 sides)
	if (!c.inDice()) {
		c.getActionAssistant().sendMessage("You can only dice in the Dicing Arena.");
		return;
	}
	final int roll2 = Misc.random(6) + Misc.random(6);
	if (ClanChat.isInClan(c) && roll2 >= 2) {
	if (System.currentTimeMillis() - c.diceDelay >= 5000) {
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(2074);
		ClanChat.sendClanMessage(c,"[Official Dicer]: " +c.playerName+ " has rolled ["+roll2+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.forcedChat("[Official Dicer][CC Roll]: " +c.playerName+ " has rolled ["+roll2+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.diceDelay = System.currentTimeMillis();
		}
		} else if (ClanChat.isInClan(c) && roll2 == 0) {
			c.getActionAssistant().sendMessage("Try Again. The Dice Rolled 0.");
		} else {
			c.getActionAssistant().sendMessage("Please Join a Clan Chat to Roll to Clan.");
		}
	break;
	case 15090: //Die 8 sides
	if (!c.inDice()) {
		c.getActionAssistant().sendMessage("You can only dice in the Dicing Arena.");
		return;
	}
	final int roll3 = Misc.random(8);
	if (ClanChat.isInClan(c) && roll3 >= 1) {
	if (System.currentTimeMillis() - c.diceDelay >= 5000) {
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(2071);
		ClanChat.sendClanMessage(c,"[Official Dicer]: " +c.playerName+ " has rolled ["+roll3+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.forcedChat("[Official Dicer][CC Roll]: " +c.playerName+ " has rolled ["+roll3+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.diceDelay = System.currentTimeMillis();
		}
		} else if (ClanChat.isInClan(c) && roll3 == 0) {
			c.getActionAssistant().sendMessage("Try Again. The Dice Rolled 0.");
		} else {
			c.getActionAssistant().sendMessage("Please Join a Clan Chat to Roll to Clan.");
		}
	break;
	case 15092: //Die 10 sides
	if (!c.inDice()) {
		c.getActionAssistant().sendMessage("You can only dice in the Dicing Arena.");
		return;
	}
	final int roll4 = Misc.random(10);
	if (ClanChat.isInClan(c) && roll4 >= 1) {
	if (System.currentTimeMillis() - c.diceDelay >= 5000) {
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(2070);
		ClanChat.sendClanMessage(c,"[Official Dicer]: " +c.playerName+ " has rolled ["+roll4+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.forcedChat("[Official Dicer][CC Roll]: " +c.playerName+ " has rolled ["+roll4+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.diceDelay = System.currentTimeMillis();
		}
		} else if (ClanChat.isInClan(c) && roll4 == 0) {
			c.getActionAssistant().sendMessage("Try Again. The Dice Rolled 0.");
		} else {
			c.getActionAssistant().sendMessage("Please Join a Clan Chat to Roll to Clan.");
		}
	break;
	case 15094: //Die 12 sides
	if (!c.inDice()) {
		c.getActionAssistant().sendMessage("You can only dice in the Dicing Arena.");
		return;
	}
	final int roll5 = Misc.random(12);
	if (ClanChat.isInClan(c) && roll5 >= 1) {
	if (System.currentTimeMillis() - c.diceDelay >= 5000) {
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(2073);
		ClanChat.sendClanMessage(c,"[Official Dicer]: " +c.playerName+ " has rolled ["+roll5+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.forcedChat("[Official Dicer][CC Roll]: " +c.playerName+ " has rolled ["+roll5+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.diceDelay = System.currentTimeMillis();
		}
		} else if (ClanChat.isInClan(c) && roll5 == 0) {
			c.getActionAssistant().sendMessage("Try Again. The Dice Rolled 0.");
		} else {
			c.getActionAssistant().sendMessage("Please Join a Clan Chat to Roll to Clan.");
		}
	break;
	case 15096: //Die 20 sides
	if (!c.inDice()) {
		c.getActionAssistant().sendMessage("You can only dice in the Dicing Arena.");
		return;
	}
	final int roll6 = Misc.random(20);
	if (ClanChat.isInClan(c) && roll6 >= 1) {
	if (System.currentTimeMillis() - c.diceDelay >= 5000) {
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(2068);
		ClanChat.sendClanMessage(c,"[Official Dicer]: " +c.playerName+ " has rolled ["+roll6+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.forcedChat("[Official Dicer][CC Roll]: " +c.playerName+ " has rolled ["+roll6+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.diceDelay = System.currentTimeMillis();
		}
		} else if (ClanChat.isInClan(c) && roll6 == 0) {
			c.getActionAssistant().sendMessage("Try Again. The Dice Rolled 0.");
		} else {
			c.getActionAssistant().sendMessage("Please Join a Clan Chat to Roll to Clan.");
		}
	break;
	case 15100: //Die 4 sides
	if (!c.inDice()) {
		c.getActionAssistant().sendMessage("You can only dice in the Dicing Arena.");
		return;
	}
	final int roll7 = Misc.random(4);
	if (ClanChat.isInClan(c) && roll7 >= 1) {
	if (System.currentTimeMillis() - c.diceDelay >= 5000) {
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(2069);
		ClanChat.sendClanMessage(c,"[Official Dicer]: " +c.playerName+ " has rolled ["+roll7+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.forcedChat("[Official Dicer][CC Roll]: " +c.playerName+ " has rolled ["+roll7+"] on the "+ Server.getItemManager().getItemDefinition(itemId).getName() +".");
		c.diceDelay = System.currentTimeMillis();
		}
		} else if (ClanChat.isInClan(c) && roll7 == 0) {
			c.getActionAssistant().sendMessage("Try Again. The Dice Rolled 0.");
		} else {
			c.getActionAssistant().sendMessage("Please Join a Clan Chat to Roll to Clan.");
		}
	break;
	}
}
}
