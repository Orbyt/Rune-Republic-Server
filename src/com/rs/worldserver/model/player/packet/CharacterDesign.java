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

import com.rs.worldserver.model.player.*;

/**
 * Handles the character design packet
 * 
 * @author Graham
 * 
 */
public class CharacterDesign implements Packet {
	private static final int[][] MALE_VALUES = { 
		{0, 8}, // head
		{10, 17}, // jaw
		{18, 25}, // torso
		{26, 31}, // arms
		{33, 34}, // hands
		{36, 40}, // legs
		{42, 43}, // feet
	};
	
	private static final int[][] FEMALE_VALUES = { 
		{45, 54}, // head
		{-1, -1}, // jaw
		{56, 60}, // torso
		{61, 65}, // arms
		{67, 68}, // hands
		{70, 77}, // legs
		{79, 80}, // feet
	};
	
	private static final int[][] ALLOWED_COLORS = {
		{0, 11}, // hair color
		{0, 15}, // torso color
		{0, 15}, // legs color
		{0, 5}, // feet color
		{0, 7}  // skin color
	};
	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		if(packetType == 101 && packetSize == 13){
			if(client.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == 2653){
					client.getActionAssistant().removeItem(client.playerEquipment[4], 4);
					client.getActionAssistant().sendMessage("Removed the zammy first for ya! <3");
									
			} 
			if(client.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 2655){
					client.getActionAssistant().removeItem(client.playerEquipment[7], 7);
					client.getActionAssistant().sendMessage("Removed the zammy first for ya! <3");
									
			}	
			if(client.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 4509){
					client.getActionAssistant().removeItem(client.playerEquipment[7], 7);
					client.getActionAssistant().sendMessage("Removed the dec armor first for ya! <3");
									
			}
			if(client.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 2669){
					client.getActionAssistant().removeItem(client.playerEquipment[7], 7);
					client.getActionAssistant().sendMessage("Removed the plate first for ya! <3");
									
			}			
			final int gender = client.getInStream().readSignedByte();

			if (gender != 0 && gender != 1)
				return;

			final int[] apperances = new int[MALE_VALUES.length]; // apperance's
																	// value
			// check
			for (int i = 0; i < apperances.length; i++) {
				int value = client.getInStream().readSignedByte();
				if (value < (gender == 0 ? MALE_VALUES[i][0] : FEMALE_VALUES[i][0]) || value > (gender == 0 ? MALE_VALUES[i][1] : FEMALE_VALUES[i][1]))
					value = (gender == 0 ? MALE_VALUES[i][0] : FEMALE_VALUES[i][0]);
				apperances[i] = value;
			}

			final int[] colors = new int[ALLOWED_COLORS.length]; // color value
																	// check
			for (int i = 0; i < colors.length; i++) {
				int value = client.getInStream().readSignedByte();
				if (value < ALLOWED_COLORS[i][0] || value > ALLOWED_COLORS[i][1])
					value = ALLOWED_COLORS[i][0];
				colors[i] = value;
			}			
		//	if (true) { 
			//client.getActionAssistant().sendMessage("Changing appearance is disabled for now, sorry");
		//	return; }
			if (client.canChangeAppearance) {
				client.playerAppearance[0] = gender; // gender
				client.playerAppearance[1] = apperances[0]; // head
				client.playerAppearance[2] = apperances[2]; // torso
				client.playerAppearance[3] = apperances[3]; // arms
				client.playerAppearance[4] = apperances[4]; // hands
				client.playerAppearance[5] = apperances[5]; // legs
				client.playerAppearance[6] = apperances[6]; // feet
				client.playerAppearance[7] = apperances[1]; // beard
				client.playerAppearance[8] = colors[0]; // hair colour
				client.playerAppearance[9] = colors[1]; // torso colour
				client.playerAppearance[10] = colors[2]; // legs colour
				client.playerAppearance[11] = colors[3]; // feet colour
				client.playerAppearance[12] = colors[4]; // skin colour
				client.appearanceUpdateRequired = true;
				client.updateRequired = true;
				client.playerAppearanceSet = true;
				client.charDesign = true;
				client.getActionAssistant().removeAllWindows();
				client.setCanWalk(true);
				client.canChangeAppearance = false;
			}
		}
	}
}
