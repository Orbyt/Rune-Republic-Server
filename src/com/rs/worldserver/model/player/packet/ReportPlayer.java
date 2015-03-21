package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.util.Misc;

/**
 * Remove items packet
 * 
 * @author Graham
 * 
 */
public class ReportPlayer implements Packet {

	@Override
	public void handlePacket(Client c, int packetType, int packetSize) {
        c.getActionAssistant().sendMessage("SQL");
	}
	
	public String capitalize(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}