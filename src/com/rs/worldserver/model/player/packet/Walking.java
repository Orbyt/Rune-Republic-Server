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

import com.rs.worldserver.Constants;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.world.PlayerManager;

/**
 * Walking packet
 * 
 * @author Graham
 * 
 */
public class Walking implements Packet {

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
           if (!client.follower) {
                client.faceUpdate(-1);				
                //if (client.isFighting || client.inCombat) {
                //    client.getCombat().resetAttack();
                //}
				
            } else { 
				client.getFollowing().resetFollowing();
			}			
			if(System.currentTimeMillis() - client.teleDelay < 4000){
				client.getActionAssistant().sendMessage("You can not walk just yet!");
				return;
			}
			if(client.clickTime > 0){
				client.getActionAssistant().sendMessage("You can not walk just yet!");
				return;
			}
			if(client.duelFlag){
				client.getActionAssistant().sendMessage("@red@You can not walk just yet!");
				return;
			}
			if (packetType == 248) { //mini map walk
				if(client.duelStatus > 4){ // dueling fix
					return;
				}
				client.getCombat().resetAttack();
				if(client.inAir){
					client.didMove = true;
				}
			}
       		 else if(packetType == 164) //main walk
        	{
            	client.getCombat().resetAttack();
				if(client.inAir){
					client.didMove = true;
				}
			}
			
			if(!client.canWalk()) {
				return; // ignore walk packet
			}
			client.walked = true;	
			if(client.duelStatus == 2 || client.duelStatus == 3 || client.duelStatus == 4) {
				return;
			}
			if(client.duelRule[1]) {
				client.getActionAssistant().sendMessage("Walking has been disabled in this duel.");
				return;
			}
			
			if((client.duelStatus >= 1 && client.duelStatus <= 4) || client.duelStatus == 6) {
				return;
			}		
			if(client.freezeTimer > 0) {
				client.getActionAssistant().sendMessage("A magical force stops you from moving.");
				return;
			}
			if(client.respawnTimer > 3) {
				return;
			}
			client.cancelTasks();
			if (packetType == 248) {
				packetSize -= 14;
				packetType = 98;
			}		
			client.newWalkCmdSteps = packetSize - 5;
			if (client.newWalkCmdSteps % 2 != 0)
				client.println_debug("Warning: walkTo(" + packetType
						+ ") command malformed: "
						+ Misc.hex(client.getInStream().buffer, 0, packetSize));
			client.newWalkCmdSteps /= 2;
			if (++client.newWalkCmdSteps > Constants.WALKING_QUEUE_SIZE) {
				client.println_debug("Warning: walkTo(" + packetType
						+ ") command contains too many steps ("
						+ client.newWalkCmdSteps + ").");
				client.newWalkCmdSteps = 0;
				return;
			}
			int firstStepX = client.getInStream().readSignedWordBigEndianA()
					- client.mapRegionX * 8;
			for (int i = 1; i < client.newWalkCmdSteps; i++) {
				client.newWalkCmdX[i] = client.getInStream().readSignedByte();
				client.newWalkCmdY[i] = client.getInStream().readSignedByte();
			}
			client.newWalkCmdX[0] = client.newWalkCmdY[0] = 0;
			int firstStepY = client.getInStream().readSignedWordBigEndian()
					- client.mapRegionY * 8;
			client.newWalkCmdIsRunning = client.getInStream().readSignedByteC() == 1;
			for (int i = 0; i < client.newWalkCmdSteps; i++) {
				client.newWalkCmdX[i] += firstStepX;
				client.newWalkCmdY[i] += firstStepY;
			}
			client.poimiY = firstStepY;
			client.poimiX = firstStepX;
			if(client.tileOn){
				client.appendToAutoTile(client.absX, client.absY);
			}
			/*if(client.followId > 0){
				client.faceUpdate(client.followId+32768);
				client.getActionAssistant().follow(0, 3, 1);
			}	*/
		
	}

}
