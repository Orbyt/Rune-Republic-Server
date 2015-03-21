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

import com.rs.worldserver.Server;
import com.rs.worldserver.Config;
import com.rs.worldserver.model.Item;
import com.rs.worldserver.model.player.CastleWars;
import com.rs.worldserver.model.Shop;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.util.BanProcessor;
import java.util.*;
import java.io.*;


/**
 * Remove items packet
 * 
 * @author Graham
 * 
 */
public class RemoveItem implements Packet {
private int[][] pkpItem = {{23659,75000},{20171,80000},{20072,60000},{15241,60000},{13887,60000},{13893,60000},{14484,60000},{11694,55000},{18349,50000},{18351,50000},{18363,50000},{18353,50000},{18357,50000},{18355,50000},{21787,35000},{21790,35000},{21793,35000},{18335,50000},{19669,20000},{10548,30000},{18347,30000},{13858,30000},{13861,30000},{13870,30000},{13873,30000},{15017,10000},{15486,25000},{13738,25000},{13742,20000},
	{8841,10000},{13744,15000},{11724,20000},{11726,20000},{13740,25000},{4708,500},{4712,500},{4714,500},{4716,500},{4720,500},{4722,500},{4718,500},{4745,500},{4749,500},{4751,500},{6914,500},{6889,500},{2451,10},{6686,10},{6585,1500},{4151,800},{6737,700},{6731,700},{6733,700},{6735,700},{7871,10},{11212,10},{4736,500},{4738,500},{15243,35},{4740,10},{6920,500},{11732,500},{15273,25},{537,30},{4827,1000}};
	
		
	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		int interfaceID = client.getInStream().readUnsignedWordA();
		int removeSlot = client.getInStream().readUnsignedWordA();
		int removeID = client.getInStream().readUnsignedWordA();
		int temp = client.getActionAssistant().getItemSlot(removeID);
		/*client.println_debug("RemoveItem: " + removeID + " interface "
				+ interfaceID + " slot: " + removeSlot);*/
		try {
		
		if ((interfaceID == 5382 || interfaceID == 5292) && !client.bankOK) {
		client.sqlLog(client.playerName.toLowerCase(), 1);
		try {
						BanProcessor.banUser(client.playerName,"Server");
						client.kick();
						client.disconnected = true;
						//bankTrade(client.playerName);
						} catch (IOException e) {
						}
		}
					if(interfaceID == 15445){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(1, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15452){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(1, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15459){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(1, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15466){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(1, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15473){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(1, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15481){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(1, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
				if(interfaceID == 4233){
					for(int k = 0; k < 7; k++){
						if(client.getActionAssistant().rings[k] == removeID){
							client.getActionAssistant().removeAllWindows();
							client.getActionAssistant().goldCraft("ring", 1, k);
						}
					}
				}
				if(interfaceID == 4239){
					for(int k = 0; k < 7; k++){
						if(client.getActionAssistant().necks[k] == removeID){
							client.getActionAssistant().removeAllWindows();
							client.getActionAssistant().goldCraft("neck", 1, k);
						}
					}
				}
				if(interfaceID == 4245){
					for(int k = 0; k < 7; k++){
						if(client.getActionAssistant().ammys[k] == removeID){
							client.getActionAssistant().removeAllWindows();
							client.getActionAssistant().goldCraft("ammy", 1, k);
						}
					}
				}		
                if (interfaceID >= 1119 && interfaceID <= 1123){
					client.getActionAssistant().smithamount = 1;
                    client.getActionAssistant().smith(removeSlot, interfaceID);	}	
		if (interfaceID == 1688) {
			if (client.playerEquipment[removeSlot] > 0)
				//System.out.println(removeID);
				if(Config.CastleWars && (removeID == 4041 && Server.getCastleWars().isInCwWait(client) || removeID == 4041 && Server.getCastleWars().isInCw(client) || 
				removeID == 4042 && Server.getCastleWars().isInCwWait(client) || removeID == 4042 && Server.getCastleWars().isInCw(client))){
					client.getActionAssistant().sendMessage("You can not remove this during CW!");
					return;
				}
				client.getActionAssistant().remove(removeID, removeSlot);
			client.getCombatManager().calculateBonus();
		} else if (interfaceID == 5064) {
			if (temp == -1) {
		client.getActionAssistant().sendMessage("AN ERROR HAS OCCURED 8");
		return;
		}
			client.getContainerAssistant().bankItem(removeID, client.getActionAssistant().getItemSlot(removeID), 1);
		} else if (interfaceID == 5382) {
			client.getContainerAssistant().fromBank(removeID, removeSlot, 1);
		} else if (interfaceID == 3823) {
			String name = Server.getItemManager().getItemDefinition(removeID)
					.getName();
			if ((Item.itemSellable[removeID] == false)) {
				client.getActionAssistant().sendMessage(
						"I cannot sell " + name + ".");
			} else {
				if (!client.getExtraData().containsKey("shop"))
					return;
				int shop = (Integer) client.getExtraData().get("shop");
				Shop s = Server.getShopManager().getShops().get(shop);
				if (s == null)
					return;
				if ((s.isItemSellable(removeID)) && (shop != 57) && (shop != 72) && (shop != 73) && shop != 7) {
				
					int value = s.getItemBuyValue(removeID);
				if(shop == 27) {
					client.getActionAssistant().sendMessage(name + ": shop will buy for " + value + " tokkul"+ Misc.formatAmount(value));
				} else {
					
					client.getActionAssistant().sendMessage(
							name + ": shop will buy for: " + (int)value/2 + " coins"
									+ Misc.formatAmount(value/2));
				}
				} else {
					client.getActionAssistant().sendMessage(
							"I cannot sell " + name + " in this store.");
				}
			}
		} else if (interfaceID == 3900) {
			String name = Server.getItemManager().getItemDefinition(removeID)
					.getName();
			if (!client.getExtraData().containsKey("shop"))
				return;
			int shop = (Integer) client.getExtraData().get("shop");
			Shop s = Server.getShopManager().getShops().get(shop);
			if (s == null)
				return;
			int value = s.getItemSellValue(removeID);
			if(shop == 7 || shop == 67 || shop == 74) {
				for(int k = 0; k < pkpItem.length; k++){
						if(removeID == pkpItem[k][0]){
							value = pkpItem[k][1];
						}
					}
			if (removeID == 7459) {
					value = 45; }
					else if (removeID == 7460) {
					value = 50; }
					else if (removeID == 7461) {
					value = 55;
					}
					else if (removeID == 7462) {
					value = 65;
					}
					else if (removeID == 15220) {
					value = 25000;
					}
					else if (removeID == 15018) {
					value = 20000;
					}
					else if (removeID == 15019) {
					value = 20000;
					}
					else if (removeID == 15020) {
					value = 15000;
					}
					
				client.getActionAssistant().sendMessage(name + ": currently costs " + value + " Pk Points"+ Misc.formatAmount(value));
			} else if(shop == 27) {
				client.getActionAssistant().sendMessage(name + ": currently costs " + value + " tokkul"+ Misc.formatAmount(value));
			} 
			 else if(shop == 57) {
			if (removeID == 7901) {
					 value = 700;
					}
					else if (removeID == 7822) {
					 value = 350;
					}
					else if (removeID == 13653) {
					 value = 200;
					}
					else if (removeID == 13263) {
					 value = 400;
					}
					else if (removeID == 11724) {
					 value = 200;
					}
					else if (removeID == 11726) {
					 value = 200;
					}
					else if (removeID == 11718) {
					 value = 200;
					}
					else if (removeID == 11720) {
					 value = 200;
					}
					else if (removeID == 11722) {
					 value = 200;
					}
					else if (removeID == 4716) {
					 value = 4;
					}
					else if (removeID == 4718) {
					 value = 4;
					}
					else if (removeID == 4720) {
					 value = 4;
					}
					else if (removeID == 4722) {
					 value = 4;
					}
					else if (removeID ==4712) {
					 value = 4;
					}
					else if (removeID == 4714) {
					 value = 4;
					}
					else if (removeID == 4708) {
					 value = 4;
					}
					else if (removeID == 4736) {
					 value = 4;
					}
					else if (removeID == 4738) {
					 value = 4;
					}
					else if (removeID == 9220) {
					 value = 150;
					}
					else if (removeID == 7821) {
					 value = 150;
					}
					else if (removeID == 13744) {
					 value = 125;
					}
					else if (removeID == 13740) {
					 value = 325;
					}
					else if (removeID == 13742) {
					 value = 250;
					}
					else if (removeID == 13738) {
					 value = 300;
					}
					else if (removeID == 9954) {
					 value = 100;
					}
					else if (removeID == 13899) {
					 value = 300;
					}
					else if (removeID == 13902) {
					 value = 100;
					}
					else if (removeID == 7498) {
					 value = 3;
					}
				client.getActionAssistant().sendMessage(name + ": currently costs " + value + " Vote Tickets"+ Misc.formatAmount(value));
			}
			else if (shop == 72 || shop == 73) {
			value = client.getActionAssistant().getEpShopPrice(removeID);
			
			client.getActionAssistant().sendMessage(name + ": currently costs " + value + " EP tickets (ship tickets)"+ Misc.formatAmount(value));
			
			
			}
			else if (shop == 55) {
			if (removeID == 7821) {
					value = 250;
					}
					else if (removeID == 9220) {
					value = 250;
					}
					// else if (removeID == 13263) {
					// value = 40;
					// }
					else if (removeID == 13653) {
					value = 200;
					}
					else if (removeID == 11724) {
					value = 100;
					}
					else if (removeID == 11726) {
					value = 100;
					}
					else if (removeID == 11710) {
					value = 25;
					}
					else if (removeID == 11712) {
					value = 25;
					}
					else if (removeID == 11714) {
					value = 25;
					}
					else if (removeID == 6920) {
					value = 6;
					}
					else if (removeID == 6737) {
					value = 6;
					}
					else if (removeID == 6733) {
					value = 6;
					}
					else if (removeID == 6735) {
					value = 6;
					}
					else if (removeID == 6731) {
					value = 6;
					}
					else if (removeID == 6585) {
					value = 11;
					}
					else if (removeID == 11732) {
					value = 6;
					}
					else if (removeID == 4151) {
					value = 10;
					}
			
			client.getActionAssistant().sendMessage(name + ": currently costs " + value + " Achievement Points"+ Misc.formatAmount(value));
			
			}
			//else if (shop ==57) {
			
			
		//	client.getActionAssistant().sendMessage(name + ": currently costs " + value + " Vote Tickets"+ Misc.formatAmount(value));
		//	}
		else if (shop == 61) {
								if (removeID == 13744) { value = 4; }
else if (removeID == 13740) { value = 5; }
else if (removeID == 13742) { value = 4; }
else if (removeID == 13738) { value = 4; }
else if (removeID == 13752) { value = 3; }
else if (removeID == 13740) { value = 5; }
else if (removeID == 13896) { value = 5; }
else if (removeID == 7821) { value = 3; }
else if (removeID == 9220) { value = 3; }
else if (removeID == 11718) { value = 3; }
else if (removeID == 11720) { value = 3; }
else if (removeID == 11722) { value = 3; }
else if (removeID == 11724) { value = 4; }
else if (removeID == 11726) { value = 4; }
else if (removeID == 13653) { value = 5; }
else if (removeID == 7901) { value = 14; }
else if (removeID == 7822) { value = 8; }
else if (removeID == 7983) { value = 10; }
else if (removeID == 11700) { value = 6; }
else if (removeID == 11698) { value = 6; }
else if (removeID == 7819) { value = 4; }
else if (removeID == 9954) { value = 3; }
else if (removeID == 13899) { value = 6; }
else if (removeID == 13902) { value = 5; }
else if (removeID == 11694) { value = 10; }
else if (removeID == 11730) { value = 5; }
	client.getActionAssistant().sendMessage(name + ": currently costs " + value + " Staff tokens"+ Misc.formatAmount(value));
}
else if (shop == 65) {
if (removeID >= 13738 && removeID <= 13745) { value = 1000; }
else if (removeID == 15486) { value = 750; }
else if (removeID == 7498) {value = 100;}
else if (removeID == 6585) {value = 40;}
else if (removeID == 2858) {value = 20;}
else if (removeID == 2586) {value = 100;}
else if (removeID == 15606) {value = 1200;}
else if (removeID == 15608) {value = 800;}
else if (removeID == 15610) {value = 1000;}
client.getActionAssistant().sendMessage(name + ": currently costs " + value + " CW Points"+ Misc.formatAmount(value));


}
else if (shop == 81) {
 if (removeID == 7462) { value = 2; }
else if (removeID == 6570) { value = 5; }
else if (removeID == 10551) {value = 2;}
else if (removeID == 8850) {value = 5;}
else if (removeID == 19785) {value = 25;}
else if (removeID == 19786) {value = 25;}
else if (removeID == 11724) {value = 10;}
else if (removeID == 11726) {value = 10;}
else if (removeID == 11718) {value = 7;}
else if (removeID == 11720) {value = 7;}
else if (removeID == 11722) {value = 7;}
else if (removeID == 13655) {value = 15;}
else if (removeID == 13653) {value = 15;}
else if (removeID == 13876) {value = 10;}
else if (removeID == 13870) {value = 10;}
else if (removeID == 13873) {value = 10;}
else if (removeID == 13864) {value = 5;}
else if (removeID == 13858) {value = 10;}
else if (removeID == 13861) {value = 10;}
else if (removeID == 13867) {value = 10;}
else if (removeID == 13896) {value = 10;}
else if (removeID == 13884) {value = 10;}
else if (removeID == 13890) {value = 10;}
else if (removeID == 13902) {value = 15;}
else if (removeID == 13887) {value = 15;}
else if (removeID == 13893) {value = 15;}
else if (removeID == 13899) {value = 20;}
else if (removeID == 13901) {value = 30;}
else if (removeID == 13905) {value = 15;}
else if (removeID == 13263) {value = 30;}
else if (removeID == 19784) {value = 70;}
else if (removeID == 11694) {value = 25;}
else if (removeID == 11696) {value = 8;}
else if (removeID == 11698) {value = 8;}
else if (removeID == 11700) {value = 8;}
else if (removeID == 14484) {value = 30;}
else if (removeID == 7901) {value = 55;}
else if (removeID == 10735) {value = 40;}
else if (removeID == 4566) {value = 60;}
else if (removeID == 5608) {value = 50;}
else if (removeID == 5609) {value = 50;}
else if (removeID == 7822) {value = 40;}
else if (removeID == 21787) {value = 15;}
else if (removeID == 1037) {value = 30;}
else if (removeID == 21793) {value = 15;}
else if (removeID == 21790) {value = 15;}
else if (removeID == 4565) {value = 70;}
client.getActionAssistant().sendMessage(name + ": currently costs " + value + " Donator Points"+ Misc.formatAmount(value));


}

else if (shop == 82) {
 if (removeID == 13878) {value = 15;} //Morrigan coif 1
else if (removeID == 13872) {value = 15;} //Morrigan top 2
else if (removeID == 13875) {value = 15;} //Morrigan chaps 3
else if (removeID == 13866) {value = 15;} //Zuriel hood 4
else if (removeID == 13860) {value = 15;} //Zuriel top 5
else if (removeID == 13863) {value = 15;} //Zuriel bottom 6
else if (removeID == 13869) {value = 15;} //Zuriel staff 7
else if (removeID == 13898) {value = 15;} //Statius helm 8
else if (removeID == 13886) {value = 15;} //Statius top 9
else if (removeID == 13892) {value = 15;} //Statius legs 10
else if (removeID == 13904) {value = 15;} //Statius warhammer 11
else if (removeID == 13889) {value = 15;} //Vesta top 12
else if (removeID == 13895) {value = 15;} //Vesta bottom 13
else if (removeID == 13907) {value = 15;} //Vesta spear 14
else if (removeID == 18349) {value = 15;} //chaotic rapier 15
else if (removeID == 18351) {value = 15;} //chaotic longsword 16
else if (removeID == 18353) {value = 15;} //chaotic maul 17
else if (removeID == 18355) {value = 15;} //chaotic staff 18
else if (removeID == 18357) {value = 15;} //chaotic crossbow 19
else if (removeID == 17017) {value = 25;} //celestial catalytic staff 20
else if (removeID == 20135) {value = 30;} //Torva fullhelm 21
else if (removeID == 20139) {value = 30;} //Torva body 22
else if (removeID == 20143) {value = 30;} //Torva legs 23
else if (removeID == 20147) {value = 30;} //Pernix coif 24
else if (removeID == 20151) {value = 30;} //Pernix top 25
else if (removeID == 20155) {value = 30;} //Pernix chaps 26
else if (removeID == 20159) {value = 30;} //Virtus helm 27
else if (removeID == 20163) {value = 30;} //Virtus top 28
else if (removeID == 20167) {value = 30;} //Virtus robebottom 29
else if (removeID == 20171) {value = 40;} //Zaryte bow 30
else if (removeID == 16909) {value = 25;} //Primal 2h sword 31
else if (removeID == 16425) {value = 20;} //Primal maul 32
else if (removeID == 18346) {value = 15;} //Tome of frost 33
else if (removeID == 18337) {value = 15;} //Bone crusher 34
else if (removeID == 10548) {value = 25;} //Fighter hat 35
else if (removeID == 10550) {value = 25;} //Ranger hat 36
else if (removeID == 19748) {value = 25;} //Ardougne cloak 37
else if (removeID == 20072) {value = 30;} //Dragon Defender 38
else if (removeID == 15241) {value = 30;} //Hand Cannon 39
else if (removeID == 23659) {value = 30;} //Tokhar 40
else if (removeID == 4024) {value = 35;} //Ninja Gree Gree 22
//else if (removeID == 4027) {value = 35;} //Nigger Gree Gree 23
else if (removeID == 4031) {value = 35;} //Karamja Gree Gree 23
else if (removeID == 4026) {value = 35;} //Gorilla Gree Gree 24
else if (removeID == 4029) {value = 35;} //Zombie Gree Gree 25
else if (removeID == 22482) {value = 15;} //Gano visor 19
else if (removeID == 22490) {value = 15;} //Gano top 20
else if (removeID == 22486) {value = 15;} //Gano bottom 21
else if (removeID == 9119) {value = 65;} //Orange Hween
else if (removeID == 11694) {value = 25;}
else if (removeID == 11696) {value = 8;}
else if (removeID == 11698) {value = 8;}
else if (removeID == 11700) {value = 8;}
else if (removeID == 14484) {value = 30;}
else if (removeID == 7901) {value = 55;}
else if (removeID == 10735) {value = 40;}
else if (removeID == 17291) {value = 20;}
else if (removeID == 4566) {value = 60;}
else if (removeID == 5608) {value = 50;}
else if (removeID == 5609) {value = 50;}
else if (removeID == 7822) {value = 40;}
else if (removeID == 21787) {value = 15;}
else if (removeID == 1037) {value = 30;}
else if (removeID == 21793) {value = 15;}
else if (removeID == 21790) {value = 15;}
else if (removeID == 4565) {value = 70;}
else if (removeID == 17259) {value = 15;} //Primail top 2
else if (removeID == 16689) {value = 15;} //Primail legs 3
else if (removeID == 17361) {value = 15;} //Primail kiteshield 4
else if (removeID == 16359) {value = 15;} //Primail boots 5
else if (removeID == 16293) {value = 15;} //Primail gloves 6
else if (removeID == 16711) {value = 15;} //Primail helm 1
client.getActionAssistant().sendMessage(name + ": currently costs " + value + " Donator Points"+ Misc.formatAmount(value));


}

else if (shop == 83) {
 if (removeID == 6570) { value = 2; }
else if (removeID == 10551) {value = 2;}
else if (removeID == 8850) {value = 5;}
else if (removeID == 19785) {value = 25;}
else if (removeID == 19786) {value = 25;}
else if (removeID == 11724) {value = 10;}
else if (removeID == 11726) {value = 10;}
else if (removeID == 11718) {value = 7;}
else if (removeID == 11720) {value = 7;}
else if (removeID == 11722) {value = 7;}
else if (removeID == 13655) {value = 15;}
else if (removeID == 13653) {value = 15;}
else if (removeID == 13876) {value = 10;}
else if (removeID == 13870) {value = 10;}
else if (removeID == 13873) {value = 10;}
else if (removeID == 13864) {value = 5;}
else if (removeID == 13858) {value = 10;}
else if (removeID == 11694) {value = 25;}
else if (removeID == 11696) {value = 8;}
else if (removeID == 11698) {value = 8;}
else if (removeID == 11700) {value = 8;}
else if (removeID == 14484) {value = 30;}
else if (removeID == 7901) {value = 55;}
else if (removeID == 10735) {value = 40;}
else if (removeID == 17291) {value = 20;}
else if (removeID == 4566) {value = 60;}
else if (removeID == 5608) {value = 50;}
else if (removeID == 5609) {value = 50;}
else if (removeID == 7822) {value = 40;}
else if (removeID == 21787) {value = 15;}
else if (removeID == 1037) {value = 30;}
else if (removeID == 7909) {value = 5;}
else if (removeID == 7910) {value = 5;}
else if (removeID == 21793) {value = 15;}
else if (removeID == 21790) {value = 15;}
else if (removeID == 4565) {value = 70;}
else if (removeID == 13861) {value = 10;}
else if (removeID == 13867) {value = 10;}
else if (removeID == 13896) {value = 10;}
else if (removeID == 13884) {value = 10;}
else if (removeID == 13890) {value = 10;}
else if (removeID == 13902) {value = 15;}
else if (removeID == 13887) {value = 15;}
else if (removeID == 13893) {value = 15;}
else if (removeID == 13899) {value = 20;}
else if (removeID == 13901) {value = 30;}
else if (removeID == 13905) {value = 15;}
else if (removeID == 13263) {value = 30;}
else if (removeID == 19784) {value = 70;}
else if (removeID == 11694) {value = 25;}
else if (removeID == 14484) {value = 30;}
else if (removeID == 7901) {value = 55;}
else if (removeID == 10735) {value = 40;}
else if (removeID == 4566) {value = 60;}
else if (removeID == 5608) {value = 50;}
else if (removeID == 5609) {value = 50;}
else if (removeID == 1037) {value = 30;}
else if (removeID == 4565) {value = 70;}
else if (removeID == 22406) {value = 150;} //Ancient Mace 26
else if (removeID == 9508) {value = 85;}//black phat
else if (removeID == 9510) {value = 85;}//pink phat
else if (removeID == 9516) {value = 75;}//black santa
else if (removeID == 20888) {value = 120;}//rainbow phat
else if (removeID == 9501) {value = 80;}//lime phat

client.getActionAssistant().sendMessage(name + ": currently costs " + value + " Donator Points"+ Misc.formatAmount(value));

}
else if (shop == 84) {
 if (removeID == 7901) { value = 600; }
else if (removeID == 7822) { value = 350; }
client.getActionAssistant().sendMessage(name + ": currently costs " + value + " Vote Points"+ Misc.formatAmount(value));

}
			else if(shop == 52) {
					if (removeID == 13263) {
					value = 175;
					}
					else if (removeID == 7498) {
					value = 10;
					}
					else if (removeID == 7003) {
					value = 10;
					}
					else if (removeID == 6335) {
					value = 20;
					}
					else if (removeID == 6131) {
					value = 40;
					}
				client.getActionAssistant().sendMessage(name + ": currently costs " + value + " Slayer Points"+ Misc.formatAmount(value));
			} else {
			if ((removeID >= 4708 && removeID <= 4751)) {
					value = 3000000;
					} else if (removeID >= 6731 && removeID <= 6737 || removeID == 6920) {
					value = 2000000;
					}
					else if (removeID == 6585) {
					value = 10000000;
					}
					else if ( removeID == 11732) {
					value = 2000000;
					}
				client.getActionAssistant().sendMessage(name + ": currently costs " + value + " coins"+ Misc.formatAmount(value));
			}
		} else if(interfaceID == 3322) {
			if(client.duelStatus <= 0) {
				client.getTradeAssistant().tradeItem(removeID, removeSlot, 1);
			} else {
				client.getActionAssistant().stakeItem(removeID, removeSlot, 1);
			}
		} else if(interfaceID == 3415) {
			if(client.duelStatus <= 0) { 
			if(!client.secondTradeWindow) {
				client.getTradeAssistant().decline();
				//client.getTradeAssistant().fromTrade(removeID, removeSlot, 1);
			}
			}
		} else if(interfaceID == 6669){
			client.getActionAssistant().fromDuel(removeID, removeSlot, 1);
		
		}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
