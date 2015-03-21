package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.content.skill.Fletching;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.model.DialogueMessage;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.Config;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Constants;
import com.rs.worldserver.Server;

/**
 * Handles most client buttons
 * 
 * @author Graham
 * 
 */
public class ClanJoin implements Packet {

	@Override
	public void handlePacket(Client c, int packetType, int packetSize) {
        long chat = c.getInStream().readQWord();
		System.out.println(Misc.longToPlayerName(chat));
	}
}
/*	} else {
		c.getActionAssistant().sendMessage("Invalid player specified. Please make sure you've entered their name correctly.");
	}



/* String clan = "";
            for (int i4 = 1; i4 < Constants.MAXIMUM_PLAYERS; i4++) {
                if ((Server.getPlayerManager().playersCurrentlyOn[i4] != null) && (Misc.playerNameToInt64(Server.getPlayerManager().playersCurrentlyOn[i4]) == chat)){
                    clan = Server.getPlayerManager().playersCurrentlyOn[i4];
                }
			}
                if (c.clanLeader != "nobody") {
                    c.getActionAssistant().sendMessage("you are already in a clan!");
                    return;
                }
            
                if(Server.getPlayerManager().player(clan) == null) {
					c.getActionAssistant().sendMessage("That player is not online");
					return;
                }
        if(c.clanName.equalsIgnoreCase("none") && c.clanLeader.equalsIgnoreCase("nobody")) {
            Client c2 = Server.getPlayerManager().player(clan); //clanLeader
                         try {
            if(c2.clanLeader == c2.playerName) {
                c.clanName = c2.clanName;
                c.clanLeader = c2.playerName;
                for(int i2=0; i2<=17; i2++) {
                    if(c2.clanMembers[i2] == null) {
                        c2.clanMembers[i2] = c.playerName;
                        c.getActionAssistant().sendMessage("You have joined the clan "+c.clanName+".");
                        c2.getActionAssistant().sendMessage(c.playerName+" has joined your clan.");
                        break;
                    }
                }
                c.getActionAssistant().updateCCMenu();
            } else {
                c.getActionAssistant().sendMessage("That player is not the leader of a clan.");
               return;
            }}catch(Exception e){}
        } else {
            c.getActionAssistant().sendMessage("You are already in a clan!");
            return;
        }*/
	

