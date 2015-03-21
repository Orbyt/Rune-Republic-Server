package com.rs.worldserver.model.player.packet;

import java.util.HashMap;
import java.util.Map;

import com.rs.worldserver.Constants;
import com.rs.worldserver.Server;
import com.rs.worldserver.Config;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.FloorItem;
import com.rs.worldserver.model.ItemDefinition;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.npc.NPCDefinition;
import com.rs.worldserver.world.AnimationManager;
import com.rs.worldserver.world.StillGraphicsManager;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.model.player.packet.Attack;

/**
 * Challenge Player
 **/
public class ChallengePlayer implements Packet {

	@Override
	public void handlePacket(Client c, int packetType, int packetSize) {		
			int answerPlayer = c.getInStream().readUnsignedWord();
			if(PlayerManager.getSingleton().getPlayers()[answerPlayer] != null) {
				return;
			}			
			if (Config.WORLD_NUMBER != 1 || Config.WORLD_NUMBER != 1 || Config.WORLD_NUMBER != 5) {
			c.getActionAssistant().sendMessage("You can only stake on world 2 or 4!");
				return;
			}
			/*if (true) {
			c.getActionAssistant().sendMessage("Click me: +http://www.runerepublic.com#url#");
			return;
			}*/
			if(c.arenas() && c.playerRights != 3) {
				c.getActionAssistant().sendMessage("You can't challenge inside the arena!");
				return;
			}
			if(c.newFag > 0) {
	            c.getActionAssistant().sendMessage("You cannot duel under New Player Protection. ("+(int)(c.newFag/2)+" seconds remaining)");
				return;
			} 
//			c.killer.clear();
			c.getActionAssistant().requestDuel(answerPlayer);
			return;
	}		
}	

