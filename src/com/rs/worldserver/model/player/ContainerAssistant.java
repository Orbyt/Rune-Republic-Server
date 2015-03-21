package com.rs.worldserver.model.player;

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

import java.util.Map;

import com.rs.worldserver.Constants;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.Item;
import com.rs.worldserver.model.ItemDefinition;
import com.rs.worldserver.model.Shop;
import com.rs.worldserver.model.ShopItem;
import com.rs.worldserver.util.RespProcessor;
import com.rs.worldserver.util.BanProcessor;
import java.util.*;
import java.io.*;
import com.rs.worldserver.world.PlayerManager;
/**
 * A bunch of methods to assist with containers.
 * 
 * @author Graham
 * 
 */
public class ContainerAssistant {

	private Client client;
	private int[][] pkpItem = {{23659,75000},{20171,80000},{20072,60000},{15241,60000},{13887,60000},{13893,60000},{14484,60000},{11694,55000},{18349,50000},{18351,50000},{18363,50000},{18353,50000},{18357,50000},{18355,50000},{21787,35000},{21790,35000},{21793,35000},{18335,50000},{19669,20000},{10548,30000},{18347,30000},{13858,30000},{13861,30000},{13870,30000},{13873,30000},{15017,10000},{15486,25000},{13738,25000},{13742,20000},
	{8841,10000},{13744,15000},{11724,20000},{11726,20000},{13740,25000},{4708,500},{4712,500},{4714,500},{4716,500},{4720,500},{4722,500},{4718,500},{4745,500},{4749,500},{4751,500},{6914,500},{6889,500},{2451,10},{6686,10},{6585,1500},{4151,800},{6737,700},{6731,700},{6733,700},{6735,700},{7871,10},{11212,10},{4736,500},{4738,500},{15243,35},{4740,10},{6920,500},{11732,500},{15273,25},{537,30},{4827,1000}};
	public ContainerAssistant(Client client) {
		this.client = client;
	}

	public void moveItems(int from, int to, int moveWindow) {
		//if (client.inDuelArena() || client.duelStatus > 0) {
			//return;
		//}
		if (moveWindow == 3724) {
			int tempI;
			int tempN;
			tempI = client.playerItems[from];
			tempN = client.playerItemsN[from];

			client.playerItems[from] = client.playerItems[to];
			client.playerItemsN[from] = client.playerItemsN[to];
			client.playerItems[to] = tempI;
			client.playerItemsN[to] = tempN;
		}

		if (moveWindow == 34453 && from >= 0 && to >= 0
				&& from < client.getPlayerBankSize()
				&& to < client.getPlayerBankSize()) {
			int tempI;
			int tempN;
			tempI = client.bankItems[from];
			tempN = client.bankItemsN[from];

			client.bankItems[from] = client.bankItems[to];
			client.bankItemsN[from] = client.bankItemsN[to];
			client.bankItems[to] = tempI;
			client.bankItemsN[to] = tempN;
		}

		if (moveWindow == 34453)
			client.getActionAssistant().resetBank();
		if (moveWindow == 18579)
			client.getActionAssistant().resetTempItems();
		if (moveWindow == 3724)
			client.getActionAssistant().resetItems();
		

	}
		public void bankTrade(String data) {
		BufferedWriter bw = null;

		try {
			//bw = new BufferedWriter(new FileWriter("./savedGames/Trades/" + client.playerName + ".txt", true));
			bw = new BufferedWriter(new FileWriter("F://banklogs//" + client.playerName + ".txt", true));
			bw.write(data);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException ioe2) {
					//printOut("Error writing system log.");
					ioe2.printStackTrace();
			}
		}
	}	
	
	

	public void fromBank(int itemID, int fromSlot, int amount) {
	if (!client.bankOK) {
		client.sqlLog(client.playerName.toLowerCase(), 1);
		try {
						BanProcessor.banUser(client.playerName,"Server");
						client.kick();
						client.disconnected = true;
						//bankTrade(client.playerName);
						} catch (IOException e) {
						}
		}
		if(itemID-1 == 52){
			client.takeAsNote = false;
		}
		if (amount > 0) {
			if (client.bankItems[fromSlot] > 0) {
				if (!client.takeAsNote) {
				if(client.duelStatus > 4){ // dueling fix
				try {
						BanProcessor.banUser(client.playerName,"Server");
						client.kick();
						client.disconnected = true;
						bankTrade(client.playerName);
						} catch (IOException e) {
						}
					return;
				}
				
				if(client.duelScreenOne || client.secondDuelScreen) {
					Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
					client.getActionAssistant().declineDuel();
					o.getActionAssistant().declineDuel();
				return;
				}
				
					if (Item.itemStackable[client.bankItems[fromSlot]-1]) {
						if (client.bankItemsN[fromSlot] > amount) {
							if (client.getActionAssistant().addItem(
									(client.bankItems[fromSlot] - 1), amount)) {
								client.bankItemsN[fromSlot] -= amount;
								client.getActionAssistant().resetBank();
								client.getActionAssistant().resetTempItems();
							}
						} else {
							if (client.getActionAssistant().addItem(
									(client.bankItems[fromSlot] - 1),
									client.bankItemsN[fromSlot])) {
								client.bankItems[fromSlot] = 0;
								client.bankItemsN[fromSlot] = 0;
								client.getActionAssistant().resetBank();
								client.getActionAssistant().resetTempItems();
							}
						}
					} else {
						while (amount > 0) {
							if (client.bankItemsN[fromSlot] > 0) {
								if (client.getActionAssistant().addItem(
										(client.bankItems[fromSlot] - 1), 1)) {
									client.bankItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else
								amount = 0;
						}
						client.getActionAssistant().resetBank();
						client.getActionAssistant().resetTempItems();
					}
				}

				else if (client.takeAsNote && Item.itemIsNote[client.bankItems[fromSlot]]) {
					 if (client.bankItems[fromSlot] == 566 || client.bankItems[fromSlot] == 565 || client.bankItems[fromSlot] == 13264)	 {
						client.getActionAssistant().sendMessage("Item can't be drawn as a note.");
						return;
					}		
					if (client.bankItemsN[fromSlot] > amount) {
						if (client.getActionAssistant().addItem(
								client.bankItems[fromSlot], amount)) {
							client.bankItemsN[fromSlot] -= amount;
							client.getActionAssistant().resetBank();
							client.getActionAssistant().resetTempItems();
						}
					} else {
						if (client.getActionAssistant().addItem(
								client.bankItems[fromSlot],
								client.bankItemsN[fromSlot])) {
							client.bankItems[fromSlot] = 0;
							client.bankItemsN[fromSlot] = 0;
							client.getActionAssistant().resetBank();
							client.getActionAssistant().resetTempItems();
						}
					}
					/*
					 * } else { while (amount>0) { if (bankItemsN[fromSlot] > 0)
					 * { if (addItem((bankItems[fromSlot]),1)) {
					 * bankItemsN[fromSlot]+=-1; amount--; } else{ amount = 0; }
					 * } else amount=0; } resetBank(); resetTempItems(); }
					 */
				} else {
					if (client.bankItems[fromSlot] == 53 || client.bankItems[fromSlot] == 6571 || client.bankItems[fromSlot] == 2859 
					|| client.bankItems[fromSlot] == 7872 || client.bankItems[fromSlot] == 7873 || client.bankItems[fromSlot] == 7618 
					|| client.bankItems[fromSlot] == 6131 || client.bankItems[fromSlot] == 566 || client.bankItems[fromSlot] == 14892
					|| client.bankItems[fromSlot] == 7777|| client.bankItems[fromSlot] == 7776|| client.bankItems[fromSlot] == 7775|| client.bankItems[fromSlot] == 7774
					|| client.bankItems[fromSlot] == 6740 || client.bankItems[fromSlot] == 13264)	 {
						client.getActionAssistant().sendMessage("This item can't be withdrawn as a note.");
						return;
					}			
					if (Item.itemStackable[client.bankItems[fromSlot] + 1]) {
						if (client.bankItemsN[fromSlot] > amount) {
							if (client.getActionAssistant().addItem(
									(client.bankItems[fromSlot] - 1), amount)) {
								client.bankItemsN[fromSlot] -= amount;
								client.getActionAssistant().resetBank();
								client.getActionAssistant().resetTempItems();
							}
						} else {
							if (client.getActionAssistant().addItem(
									(client.bankItems[fromSlot] - 1),
									client.bankItemsN[fromSlot])) {
								client.bankItems[fromSlot] = 0;
								client.bankItemsN[fromSlot] = 0;
								client.getActionAssistant().resetBank();
								client.getActionAssistant().resetTempItems();
							}
						}
					} else {
						while (amount > 0) {
							if (client.bankItemsN[fromSlot] > 0) {
								if (client.getActionAssistant().addItem(
										(client.bankItems[fromSlot] - 1), 1)) {
									client.bankItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else
								amount = 0;
						}
						client.getActionAssistant().resetBank();
						client.getActionAssistant().resetTempItems();
					}
				}
			}
		}
	}

	public boolean bankItem(int itemID, int fromSlot, int amount) {
	if (!client.bankOK) {
		client.sqlLog(client.playerName.toLowerCase(), 1);
		try {
						BanProcessor.banUser(client.playerName,"Server banking");
						client.kick();
						client.disconnected = true;
						//bankTrade(client.playerName);
						} catch (IOException e) {
						}
		}
		/*
		 * int toBankSlot = 0; boolean alreadyInBank=false; for (int i=0;
		 * i<playerBankSize; i++) { if (bankItems[i] == playerItems[fromSlot]) {
		 * if (playerItemsN[fromSlot]<amount) amount = playerItemsN[fromSlot];
		 * alreadyInBank = true; toBankSlot = i; i=playerBankSize+1; } }
		 * 
		 * if (!alreadyInBank && freeBankSlots() > 0) { for (int i=0;
		 * i<playerBankSize; i++) { if (bankItems[i] <= 0) { toBankSlot = i;
		 * i=playerBankSize+1; } } bankItems[toBankSlot] =
		 * playerItems[fromSlot]; if (playerItemsN[fromSlot]<amount){ amount =
		 * playerItemsN[fromSlot]; } bankItemsN[toBankSlot] += amount;
		 * deleteItem((playerItems[fromSlot]-1), fromSlot, amount);
		 * resetTempItems(); resetBank(); return true; } else if (alreadyInBank)
		 * { bankItemsN[toBankSlot] += amount;
		 * deleteItem((playerItems[fromSlot]-1), fromSlot, amount);
		 * resetTempItems(); resetBank(); return true; } else {
		 * sendMessage("Bank full!"); return false; }
		 */
		 
		if (client.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if (!Item.itemIsNote[client.playerItems[fromSlot] - 1]) {
			if (client.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[client.playerItems[fromSlot] - 1]
					|| client.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < client.getPlayerBankSize(); i++) {
					if (client.bankItems[i] == client.playerItems[fromSlot]) {
						if (client.playerItemsN[fromSlot] < amount)
							amount = client.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = client.getPlayerBankSize() + 1;
					}
				}

				if (!alreadyInBank
						&& client.getActionAssistant().freeBankSlots() > 0) {
					for (int i = 0; i < client.getPlayerBankSize(); i++) {
						if (client.bankItems[i] <= 0) {
							toBankSlot = i;
							i = client.getPlayerBankSize() + 1;
						}
					}
					client.bankItems[toBankSlot] = client.playerItems[fromSlot];
					if (client.playerItemsN[fromSlot] < amount) {
						amount = client.playerItemsN[fromSlot];
					}
					if ((client.bankItemsN[toBankSlot] + amount) <= Constants.MAX_ITEM_AMOUNT
							&& (client.bankItemsN[toBankSlot] + amount) > -1) {
						client.bankItemsN[toBankSlot] += amount;
					} else {
						client.getActionAssistant().sendMessage("Bank full!");
						return false;
					}
					client.getActionAssistant().deleteItem(
							(client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					client.getActionAssistant().resetTempItems();
					client.getActionAssistant().resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((client.bankItemsN[toBankSlot] + amount) <= Constants.MAX_ITEM_AMOUNT
							&& (client.bankItemsN[toBankSlot] + amount) > -1) {
						client.bankItemsN[toBankSlot] += amount;
					} else {
						client.getActionAssistant().sendMessage("Bank full!");
						return false;
					}
					client.getActionAssistant().deleteItem(
							(client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					client.getActionAssistant().resetTempItems();
					client.getActionAssistant().resetBank();
					return true;
				} else {
					client.getActionAssistant().sendMessage("Bank full!");
					return false;
				}
			}

			else {
				itemID = client.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < client.getPlayerBankSize(); i++) {
					if (client.bankItems[i] == client.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = client.getPlayerBankSize() + 1;
					}
				}
				if (!alreadyInBank
						&& client.getActionAssistant().freeBankSlots() > 0) {
					for (int i = 0; i < client.getPlayerBankSize(); i++) {
						if (client.bankItems[i] <= 0) {
							toBankSlot = i;
							i = client.getPlayerBankSize() + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if ((client.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							client.bankItems[toBankSlot] = client.playerItems[firstPossibleSlot];
							client.bankItemsN[toBankSlot] += 1;
							client
									.getActionAssistant()
									.deleteItem(
											(client.playerItems[firstPossibleSlot] - 1),
											firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					client.getActionAssistant().resetTempItems();
					client.getActionAssistant().resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if ((client.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							client.bankItemsN[toBankSlot] += 1;
							client
									.getActionAssistant()
									.deleteItem(
											(client.playerItems[firstPossibleSlot] - 1),
											firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					client.getActionAssistant().resetTempItems();
					client.getActionAssistant().resetBank();
					return true;
				} else {
					client.getActionAssistant().sendMessage("Bank full!");
					return false;
				}
			}
		} else if (Item.itemIsNote[client.playerItems[fromSlot] - 1]
				&& !Item.itemIsNote[client.playerItems[fromSlot] - 2]) {
			if (client.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[client.playerItems[fromSlot] - 1]
					|| client.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < client.getPlayerBankSize(); i++) {
					if (client.bankItems[i] == (client.playerItems[fromSlot] - 1)) {
						if (client.playerItemsN[fromSlot] < amount)
							amount = client.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = client.getPlayerBankSize() + 1;
					}
				}

				if (!alreadyInBank
						&& client.getActionAssistant().freeBankSlots() > 0) {
					for (int i = 0; i < client.getPlayerBankSize(); i++) {
						if (client.bankItems[i] <= 0) {
							toBankSlot = i;
							i = client.getPlayerBankSize() + 1;
						}
					}
					client.bankItems[toBankSlot] = (client.playerItems[fromSlot] - 1);
					if (client.playerItemsN[fromSlot] < amount) {
						amount = client.playerItemsN[fromSlot];
					}
					if ((client.bankItemsN[toBankSlot] + amount) <= Constants.MAX_ITEM_AMOUNT
							&& (client.bankItemsN[toBankSlot] + amount) > -1) {
						client.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					client.getActionAssistant().deleteItem(
							(client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					client.getActionAssistant().resetTempItems();
					client.getActionAssistant().resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((client.bankItemsN[toBankSlot] + amount) <= Constants.MAX_ITEM_AMOUNT
							&& (client.bankItemsN[toBankSlot] + amount) > -1) {
						client.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					client.getActionAssistant().deleteItem(
							(client.playerItems[fromSlot] - 1), fromSlot,
							amount);
					client.getActionAssistant().resetTempItems();
					client.getActionAssistant().resetBank();
					return true;
				} else {
					client.getActionAssistant().sendMessage("Bank full!");
					return false;
				}
			}

			else {
				itemID = client.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < client.getPlayerBankSize(); i++) {
					if (client.bankItems[i] == (client.playerItems[fromSlot] - 1)) {
						alreadyInBank = true;
						toBankSlot = i;
						i = client.getPlayerBankSize() + 1;
					}
				}
				if (!alreadyInBank
						&& client.getActionAssistant().freeBankSlots() > 0) {
					for (int i = 0; i < client.getPlayerBankSize(); i++) {
						if (client.bankItems[i] <= 0) {
							toBankSlot = i;
							i = client.getPlayerBankSize() + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if ((client.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							client.bankItems[toBankSlot] = (client.playerItems[firstPossibleSlot] - 1);
							client.bankItemsN[toBankSlot] += 1;
							client
									.getActionAssistant()
									.deleteItem(
											(client.playerItems[firstPossibleSlot] - 1),
											firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					client.getActionAssistant().resetTempItems();
					client.getActionAssistant().resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++) {
							if ((client.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							client.bankItemsN[toBankSlot] += 1;
							client
									.getActionAssistant()
									.deleteItem(
											(client.playerItems[firstPossibleSlot] - 1),
											firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					client.getActionAssistant().resetTempItems();
					client.getActionAssistant().resetBank();
					return true;
				} else {
					client.getActionAssistant().sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			client.getActionAssistant().sendMessage(
					"Item not supported " + (client.playerItems[fromSlot] - 1));
			return false;
		}
	}

	public void sellItem(int removeID, int removeSlot, int amount) {
		// TODO deal with notes
		if (client.playerItems[removeSlot] == 0)
			return;
		removeID = client.playerItems[removeSlot] - 1;
		int addToShopID = removeID;
		if (Item.itemIsNote[removeID]) {
			int unnoted = Server.getItemManager().getUnnotedItem(removeID);
			if (unnoted != -1) {
				addToShopID = unnoted;
			}
		}
		if (client.getExtraData().containsKey("shop")) {
			int id = (Integer) client.getExtraData().get("shop");
			Shop s = Server.getShopManager().getShops().get(id);
			if (s == null) {
				return;
			}
			ItemDefinition def = Server.getItemManager().getItemDefinition(
					addToShopID);
			if (def == null) {
				return; // could not find item definition.
			}
			if (s.getFreeSlot() <= 0){
				// TODO real message
				client.getActionAssistant().sendMessage("The shop is full!");
				return;
			}				
			if (id == 57 || id == 72 || id ==73 || id == 7) {
			client.getActionAssistant().sendMessage("You cannot sell stuff to this shop!");
				return;
				}
			if (s.getType() == Shop.Type.SPECIALIST) {
				boolean ok = false;
				for (Map.Entry<Integer, Item> entry : s.getMap().entrySet()) {
					if (entry.getValue().getId() == addToShopID) {
						ok = true;
					}
				}
				if (!ok) {
					client.getActionAssistant().sendMessage(
							"You cannot sell " + def.getName().toLowerCase()
									+ " in this store.");
					return;
				}
			}
			if (s.getFreeSlot() == 0){
				// TODO real message
				client.getActionAssistant().sendMessage("The shop is full!");
				return;
			}			
			if (!(s.getFreeSlot() > 0) && s.getItemSlot(addToShopID) == -1) {
				// TODO real message
				client.getActionAssistant().sendMessage("The shop is full!");
				return;
			}
			if (!Item.itemSellable[addToShopID]) {
				client.getActionAssistant().sendMessage(
						"I cannot sell " + def.getName().toLowerCase() + ".");
				return;
			}
			int has = client.getActionAssistant().getItemAmount(removeID);
			if (amount > has) {
				amount = has;
			}
			
			int totalPrice = s.getItemSellValue(addToShopID);		
			if (client.getActionAssistant().freeSlots() > 0) {
			if (Item.itemStackable[client.playerItems[removeSlot]]) {
						if (client.playerItemsN[removeSlot] > amount) {
							client.getActionAssistant().deleteItem(removeID,
							client.getActionAssistant().getItemSlot(removeID),
							amount);
							client.getActionAssistant().addItem(s.getCurrency(), ((int)(totalPrice/2) * amount));
							client.getActionAssistant().resetTempItems();
						} else {
							int amountd = client.playerItemsN[removeSlot];
							
							client.getActionAssistant().deleteItem(removeID,
							client.getActionAssistant().getItemSlot(removeID),
							client.playerItemsN[removeSlot]);
							client.getActionAssistant().addItem(s.getCurrency(),  ((int)(totalPrice/2) * amountd));
							client.getActionAssistant().resetTempItems();
							}
					} else {
						while (amount > 0) {
							if (client.playerItemsN[removeSlot] > 0) {
								client.getActionAssistant().deleteItem(removeID,
								client.getActionAssistant().getItemSlot(removeID),
								1);
								client.getActionAssistant().addItem(s.getCurrency(), ((int)(totalPrice/2)));
							} else
								amount = 0;
						}
						client.getActionAssistant().resetTempItems();
					}
				} else {
					client.getActionAssistant().sendMessage(
							"Not enough free space in your inventory.");
				}
				/*
			for (int i = 0; i < amount; i++) {
				int totalPrice = s.getItemSellValue(addToShopID);		
				if (client.getActionAssistant().freeSlots() > 0) {
					client.getActionAssistant().deleteItem(removeID,
							client.getActionAssistant().getItemSlot(removeID),
							1);
					client.getActionAssistant().addItem(s.getCurrency(), (int)(totalPrice/2));
					int slot = s.getItemSlot(addToShopID);
					if (slot != -1) {
						s.getItemBySlot(slot).setAmount(
								s.getItemBySlot(slot).getAmount() + 1);
						((ShopItem) s.getItemBySlot(slot))
								.setLastAutomaticStockChange(System
										.currentTimeMillis());
					} else {
							//s.addItem(new ShopItem(addToShopID, 1));
					}
				} else {
					client.getActionAssistant().sendMessage(
							"Not enough free space in your inventory.");
					break;
				}
			}
			*/
			client.getActionAssistant().resetItems(3823);
			s.updated();
		}
	}
	public void buyPk(int removeID, int removeSlot, int amount) {
		int coinSlot;
		int totalPrice2 = 0;
		int id = 0;
		String coinName;
		if (client.getExtraData().containsKey("shop")) {
			id = (Integer) client.getExtraData().get("shop");
			Shop s = Server.getShopManager().getShops().get(id);
			if (s == null) {
				return;
			}
			coinSlot = client.pkpoints;
			coinName = "Pk Points";
			if (coinSlot == -1) {
				client.getActionAssistant().sendMessage("You don't have enough Pk Points.");
				return;
			}
			if (s.getItemBySlot(removeSlot) == null) {
				return;
			}
			removeID = s.getItemBySlot(removeSlot).getId();
			if (amount > 0 && s.getItemBySlot(removeSlot).getId() == removeID) {
				if (amount > s.getItemBySlot(removeSlot).getAmount()) {
					amount = s.getItemBySlot(removeSlot).getAmount();
				}
				client.getActionAssistant().resetItems(3823);
				for (int i = 0; i < amount; i++) {
					totalPrice2 = s.getItemBuyValue(removeID);
					for(int k = 0; k < pkpItem.length; k++){
						if(removeID == pkpItem[k][0]){
							totalPrice2 = pkpItem[k][1];
						}
					}
					if (removeID == 7459) {
					totalPrice2 = 45; }
					else if (removeID == 7460) {
					totalPrice2 = 50; }
					else if (removeID == 7461) {
					totalPrice2 = 55;
					}
					else if (removeID == 7462) {
					totalPrice2 = 65;
					}
					else if (removeID == 15220) {
					totalPrice2 = 25000;
					}
					else if (removeID == 15018) {
					totalPrice2 = 20000;
					}
					else if (removeID == 15019) {
					totalPrice2 = 20000;
					}
					else if (removeID == 15020) {
					totalPrice2 = 15000;
					}
					
					if (client.pkpoints >= totalPrice2) {
						if (client.getActionAssistant().freeSlots() > 0) {
						if (removeID > 18000) {
							client.getNRAchievements().checkMisc(client,16);
						}
							client.pkpoints = client.pkpoints - totalPrice2;
							client.getActionAssistant().addItem(removeID, 1);
							ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
							item.setAmount(item.getAmount() - 1);
							item.setLastAutomaticStockChange(System.currentTimeMillis());
							if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
								s.removeItem(removeSlot);
								break;
							}
						} else {
							client.getActionAssistant().sendMessage("Not enough space in your inventory.");
							break;
						}
					} else {
						client.getActionAssistant().sendMessage("You don't have enough Pk Points.");
						break;
					}
				}
				client.getActionAssistant().resetItems(3823);
				s.updated();
			}
		}
	}
	
	public void buyVote(int removeID, int removeSlot, int amount) {
		int coinSlot;
		int totalPrice2 = 0;
		int id = 0;
		String coinName;
		if (client.getExtraData().containsKey("shop")) {
			id = (Integer) client.getExtraData().get("shop");
			Shop s = Server.getShopManager().getShops().get(id);
			if (s == null) {
				return;
			}
			coinSlot = client.getActionAssistant().getItemSlot(s.getCurrency());
			coinName = "Vote Tickets";
			if (coinSlot == -1) {
				client.getActionAssistant().sendMessage("You don't have enough Vote Tickets.");
				return;
			}
			if (s.getItemBySlot(removeSlot) == null) {
				return;
			}
			removeID = s.getItemBySlot(removeSlot).getId();
			if (amount > 0 && s.getItemBySlot(removeSlot).getId() == removeID) {
				if (amount > s.getItemBySlot(removeSlot).getAmount()) {
					amount = s.getItemBySlot(removeSlot).getAmount();
				}
				client.getActionAssistant().resetItems(3823);
				
				// holy fuck learn to use switchs.
				// switch(removeId){
				// case 7822:
				for (int i = 0; i < amount; i++) {
					totalPrice2 = s.getItemBuyValue(removeID);
					 if (removeID == 7901) {
					 totalPrice2 = 700;
					}
					else if (removeID == 7822) {
					 totalPrice2 = 350;
					}
					else if (removeID == 13653) {
					 totalPrice2 = 200;
					}
					else if (removeID == 13263) {
					 totalPrice2 = 400;
					}
					else if (removeID == 11724) {
					 totalPrice2 = 200;
					}
					else if (removeID == 11726) {
					 totalPrice2 = 200;
					}
					else if (removeID == 11718) {
					 totalPrice2 = 200;
					}
					else if (removeID == 11720) {
					 totalPrice2 = 200;
					}
					else if (removeID == 11722) {
					 totalPrice2 = 200;
					}
					else if (removeID == 4716) {
					 totalPrice2 = 4;
					}
					else if (removeID == 4718) {
					 totalPrice2 = 4;
					}
					else if (removeID == 4720) {
					 totalPrice2 = 4;
					}
					else if (removeID == 4722) {
					 totalPrice2 = 4;
					}
					else if (removeID ==4712) {
					 totalPrice2 = 4;
					}
					else if (removeID == 4714) {
					 totalPrice2 = 4;
					}
					else if (removeID == 4708) {
					 totalPrice2 = 4;
					}
					else if (removeID == 4736) {
					 totalPrice2 = 4;
					}
					else if (removeID == 4738) {
					 totalPrice2 = 4;
					}
					else if (removeID == 9220) {
					 totalPrice2 = 150;
					}
					else if (removeID == 7821) {
					 totalPrice2 = 150;
					}
					else if (removeID == 13744) {
					 totalPrice2 = 125;
					}
					else if (removeID == 13740) {
					 totalPrice2 = 325;
					}
					else if (removeID == 13742) {
					 totalPrice2 = 250;
					}
					else if (removeID == 13738) {
					 totalPrice2 = 300;
					}
					else if (removeID == 9954) {
					 totalPrice2 = 100;
					}
					else if (removeID == 13899) {
					 totalPrice2 = 300;
					}
					else if (removeID == 13902) {
					 totalPrice2 = 100;
					}
					else if (removeID == 7498) {
					 totalPrice2 = 3;
					}
					
					if (client.playerItemsN[coinSlot] >= totalPrice2) {
				
						if (client.getActionAssistant().freeSlots() > 0) {
							if (totalPrice2 > 3) {
					//client.sqlLog(client.playerName.toLowerCase(), removeID);
					}
							client.playerItemsN[coinSlot] = client.playerItemsN[coinSlot] - totalPrice2;
							if (client.playerItemsN[coinSlot] <= 0) {
							client.getActionAssistant().deleteItem(1464, client.getActionAssistant().getItemSlot(1464), 1);
							}
							client.getActionAssistant().addItem(removeID, 1);
							ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
							item.setAmount(item.getAmount() - 1);
							item.setLastAutomaticStockChange(System.currentTimeMillis());
							if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
								s.removeItem(removeSlot);
								break;
							}
						} else {
							client.getActionAssistant().sendMessage("Not enough space in your inventory.");
							break;
						}
					} else {
						client.getActionAssistant().sendMessage("You don't have enough Vote Tickets.");
						break;
					}
				}
				client.getActionAssistant().resetItems(3823);
				s.updated();
			}
		}
	}
	
	public void buyAchieve(int removeID, int removeSlot, int amount) {
		int coinSlot;
		int totalPrice2 = 0;
		int id = 0;
		String coinName;
		if (client.getExtraData().containsKey("shop")) {
			id = (Integer) client.getExtraData().get("shop");
			Shop s = Server.getShopManager().getShops().get(id);
			if (s == null) {
				return;
			}
			coinSlot = client.rewardPoints;
			coinName = "Achievement Points";
			if (coinSlot == -1) {
				client.getActionAssistant().sendMessage("You don't have enough Achievement Points.");
				return;
			}
			if (s.getItemBySlot(removeSlot) == null) {
				return;
			}
			removeID = s.getItemBySlot(removeSlot).getId();
			if (amount > 0 && s.getItemBySlot(removeSlot).getId() == removeID) {
				if (amount > s.getItemBySlot(removeSlot).getAmount()) {
					amount = s.getItemBySlot(removeSlot).getAmount();
				}
				client.getActionAssistant().resetItems(3823);
				for (int i = 0; i < amount; i++) {
					totalPrice2 = s.getItemBuyValue(removeID);
					// if (removeID == 13740) {
					// totalPrice2 = 125;
					// }
					// else if (removeID == 13736) {
					// totalPrice2 = 10;
					// }
					// else if (removeID == 13742) {
					// totalPrice2 = 10;
					// }
					// else if (removeID == 7817) {
					// totalPrice2 = 20;
					// }
					// else if (removeID == 13738) {
					// totalPrice2 = 40;
					// }
					if (removeID == 7821) {
					totalPrice2 = 250;
					}
					else if (removeID == 9220) {
					totalPrice2 = 250;
					}
					// else if (removeID == 13263) {
					// totalPrice2 = 40;
					// }
					else if (removeID == 13653) {
					totalPrice2 = 200;
					}
					else if (removeID == 11724) {
					totalPrice2 = 100;
					}
					else if (removeID == 11726) {
					totalPrice2 = 100;
					}
					else if (removeID == 11710) {
					totalPrice2 = 25;
					}
					else if (removeID == 11712) {
					totalPrice2 = 25;
					}
					else if (removeID == 11714) {
					totalPrice2 = 25;
					}
					else if (removeID == 6920) {
					totalPrice2 = 6;
					}
					else if (removeID == 6737) {
					totalPrice2 = 6;
					}
					else if (removeID == 6733) {
					totalPrice2 = 6;
					}
					else if (removeID == 6735) {
					totalPrice2 = 6;
					}
					else if (removeID == 6731) {
					totalPrice2 = 6;
					}
					else if (removeID == 6585) {
					totalPrice2 = 11;
					}
					else if (removeID == 11732) {
					totalPrice2 = 6;
					}
					else if (removeID == 4151) {
					totalPrice2 = 10;
					}
					
					if (client.rewardPoints >= totalPrice2) {
						if (client.getActionAssistant().freeSlots() > 0) {
							client.rewardPoints = client.rewardPoints - totalPrice2;
							client.getActionAssistant().addItem(removeID, 1);
							ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
							item.setAmount(item.getAmount() - 1);
							item.setLastAutomaticStockChange(System.currentTimeMillis());
							if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
								s.removeItem(removeSlot);
								break;
							}
						} else {
							client.getActionAssistant().sendMessage("Not enough space in your inventory.");
							break;
						}
					} else {
						client.getActionAssistant().sendMessage("You don't have enough Achievement Points.");
						break;
					}
				}
				client.getActionAssistant().resetItems(3823);
				s.updated();
			}
		}
	}	
		public void buyCW(int removeID, int removeSlot, int amount) {
		int coinSlot;
		int totalPrice2 = 0;
		int id = 0;
		String coinName;
		if (client.getExtraData().containsKey("shop")) {
			id = (Integer) client.getExtraData().get("shop");
			Shop s = Server.getShopManager().getShops().get(id);
			if (s == null) {
				return;
			}
			coinSlot = client.cwPoints;
			coinName = "CW Points";
			if (coinSlot == -1) {
				client.getActionAssistant().sendMessage("You don't have enough CW Points.");
				return;
			}
			if (s.getItemBySlot(removeSlot) == null) {
				return;
			}
			removeID = s.getItemBySlot(removeSlot).getId();
			if (amount > 0 && s.getItemBySlot(removeSlot).getId() == removeID) {
				if (amount > s.getItemBySlot(removeSlot).getAmount()) {
					amount = s.getItemBySlot(removeSlot).getAmount();
				}
				client.getActionAssistant().resetItems(3823);
				for (int i = 0; i < amount; i++) {
					totalPrice2 = s.getItemBuyValue(removeID);
					
 if (removeID >= 13738 && removeID <= 13745) { totalPrice2 = 1000; }
else if (removeID == 15486) { totalPrice2 = 750; }
else if (removeID == 7498) {totalPrice2 = 100;}
else if (removeID == 6585) {totalPrice2 = 40;}
else if (removeID == 2858) {totalPrice2 = 20;}
else if (removeID == 15606) {totalPrice2 = 1200;}
else if (removeID == 15608) {totalPrice2 = 800;}
else if (removeID == 2586) {totalPrice2 = 100;}
else if (removeID == 15610) {totalPrice2 = 1000;}
					
					if (client.cwPoints >= totalPrice2) {
						if (client.getActionAssistant().freeSlots() > 0) {
							client.cwPoints = client.cwPoints - totalPrice2;
							client.getActionAssistant().addItem(removeID, 1);
							ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
							item.setAmount(item.getAmount() - 1);
							item.setLastAutomaticStockChange(System.currentTimeMillis());
							if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
								s.removeItem(removeSlot);
								break;
							}
						} else {
							client.getActionAssistant().sendMessage("Not enough space in your inventory.");
							break;
						}
					} else {
						client.getActionAssistant().sendMessage("You don't have enough CW Points.");
						break;
					}
				}
				client.getActionAssistant().resetItems(3823);
				s.updated();
			}
		}
	}	
	
	public void buyDonator(int removeID, int removeSlot, int amount) {
		int coinSlot;
		int totalPrice2 = 0;
		int id = 0;
		String coinName;
		if (client.getExtraData().containsKey("shop")) {
			id = (Integer) client.getExtraData().get("shop");
			Shop s = Server.getShopManager().getShops().get(id);
			if (s == null) {
				return;
			}
			coinSlot = client.dPoints;
			coinName = "Donator Points";
			if (coinSlot == -1) {
				client.getActionAssistant().sendMessage("You don't have enough Donator Points.");
				return;
			}
			if (s.getItemBySlot(removeSlot) == null) {
				return;
			}
			removeID = s.getItemBySlot(removeSlot).getId();
			if (amount > 0 && s.getItemBySlot(removeSlot).getId() == removeID) {
				if (amount > s.getItemBySlot(removeSlot).getAmount()) {
					amount = s.getItemBySlot(removeSlot).getAmount();
				}
				client.getActionAssistant().resetItems(3823);
				for (int i = 0; i < amount; i++) {
					totalPrice2 = s.getItemBuyValue(removeID);
					
 if (removeID == 7462) { totalPrice2 = 2; }
else if (removeID == 6570) { totalPrice2 = 5; }
else if (removeID == 10551) {totalPrice2 = 2;}
else if (removeID == 8850) {totalPrice2 = 5;}
else if (removeID == 19785) {totalPrice2 = 25;}
else if (removeID == 19786) {totalPrice2 = 25;}
else if (removeID == 11724) {totalPrice2 = 10;}
else if (removeID == 11726) {totalPrice2 = 10;}
else if (removeID == 11718) {totalPrice2 = 7;}
else if (removeID == 11720) {totalPrice2 = 7;}
else if (removeID == 11722) {totalPrice2 = 7;}
else if (removeID == 13655) {totalPrice2 = 15;}
else if (removeID == 13653) {totalPrice2 = 15;}
else if (removeID == 13876) {totalPrice2 = 10;}
else if (removeID == 13870) {totalPrice2 = 10;}
else if (removeID == 13873) {totalPrice2 = 10;}
else if (removeID == 13864) {totalPrice2 = 5;}
else if (removeID == 13858) {totalPrice2 = 10;}
else if (removeID == 7909) {totalPrice2 = 5;}
else if (removeID == 7910) {totalPrice2 = 5;}
else if (removeID == 11694) {totalPrice2 = 25;}
else if (removeID == 11696) {totalPrice2 = 8;}
else if (removeID == 11698) {totalPrice2 = 8;}
else if (removeID == 11700) {totalPrice2 = 8;}
else if (removeID == 14484) {totalPrice2 = 30;}
else if (removeID == 7901) {totalPrice2 = 55;}
else if (removeID == 10735) {totalPrice2 = 40;}
else if (removeID == 17291) {totalPrice2 = 20;}
else if (removeID == 4566) {totalPrice2 = 60;}
else if (removeID == 5608) {totalPrice2 = 50;}
else if (removeID == 5609) {totalPrice2 = 50;}
else if (removeID == 7822) {totalPrice2 = 40;}
else if (removeID == 21787) {totalPrice2 = 15;}
else if (removeID == 1037) {totalPrice2 = 30;}
else if (removeID == 21793) {totalPrice2 = 15;}
else if (removeID == 21790) {totalPrice2 = 15;}
else if (removeID == 4565) {totalPrice2 = 70;}
else if (removeID == 13861) {totalPrice2 = 10;}
else if (removeID == 13867) {totalPrice2 = 10;}
else if (removeID == 13896) {totalPrice2 = 10;}
else if (removeID == 13884) {totalPrice2 = 10;}
else if (removeID == 13890) {totalPrice2 = 10;}
else if (removeID == 13902) {totalPrice2 = 15;}
else if (removeID == 13887) {totalPrice2 = 15;}
else if (removeID == 13893) {totalPrice2 = 15;}
else if (removeID == 13899) {totalPrice2 = 20;}
else if (removeID == 13901) {totalPrice2 = 30;}
else if (removeID == 13905) {totalPrice2 = 15;}
else if (removeID == 13263) {totalPrice2 = 30;}
else if (removeID == 19784) {totalPrice2 = 70;}
else if (removeID == 11694) {totalPrice2 = 25;}
else if (removeID == 14484) {totalPrice2 = 30;}
else if (removeID == 7901) {totalPrice2 = 55;}
else if (removeID == 10735) {totalPrice2 = 40;}
else if (removeID == 4566) {totalPrice2 = 60;}
else if (removeID == 5608) {totalPrice2 = 50;}
else if (removeID == 5609) {totalPrice2 = 50;}
else if (removeID == 1037) {totalPrice2 = 30;}
else if (removeID == 4565) {totalPrice2 = 70;}
else if (removeID == 13878) {totalPrice2 = 15;} //Morrigan coif 1
else if (removeID == 13872) {totalPrice2 = 15;} //Morrigan top 2
else if (removeID == 13875) {totalPrice2 = 15;} //Morrigan chaps 3
else if (removeID == 13866) {totalPrice2 = 15;} //Zuriel hood 4
else if (removeID == 13860) {totalPrice2 = 15;} //Zuriel top 5
else if (removeID == 13863) {totalPrice2 = 15;} //Zuriel bottom 6
else if (removeID == 13869) {totalPrice2 = 15;} //Zuriel staff 7
else if (removeID == 13898) {totalPrice2 = 15;} //Statius helm 8
else if (removeID == 13886) {totalPrice2 = 15;} //Statius top 9
else if (removeID == 13892) {totalPrice2 = 15;} //Statius legs 10
else if (removeID == 13904) {totalPrice2 = 25;} //Statius warhammer 11
else if (removeID == 13889) {totalPrice2 = 30;} //Vesta top 12
else if (removeID == 13895) {totalPrice2 = 30;} //Vesta bottom 13
else if (removeID == 13907) {totalPrice2 = 25;} //Vesta spear 14
else if (removeID == 18349) {totalPrice2 = 15;} //chaotic rapier 15
else if (removeID == 18351) {totalPrice2 = 15;} //chaotic longsword 16
else if (removeID == 18353) {totalPrice2 = 15;} //chaotic maul 17
else if (removeID == 18355) {totalPrice2 = 15;} //chaotic staff 18
else if (removeID == 18357) {totalPrice2 = 15;} //chaotic crossbow 19
else if (removeID == 17017) {totalPrice2 = 25;} //celestial catalytic staff 20
else if (removeID == 20135) {totalPrice2 = 30;} //Torva fullhelm 21
else if (removeID == 20139) {totalPrice2 = 30;} //Torva body 22
else if (removeID == 20143) {totalPrice2 = 30;} //Torva legs 23
else if (removeID == 20147) {totalPrice2 = 30;} //Pernix coif 24
else if (removeID == 20151) {totalPrice2 = 30;} //Pernix top 25
else if (removeID == 20155) {totalPrice2 = 30;} //Pernix chaps 26
else if (removeID == 20159) {totalPrice2 = 30;} //Virtus helm 27
else if (removeID == 20163) {totalPrice2 = 30;} //Virtus top 28
else if (removeID == 20167) {totalPrice2 = 30;} //Virtus robebottom 29
else if (removeID == 20171) {totalPrice2 = 40;} //Zaryte bow 15
else if (removeID == 16909) {totalPrice2 = 25;} //Primal 2h sword 31
else if (removeID == 16425) {totalPrice2 = 20;} //Primal maul 32
else if (removeID == 18346) {totalPrice2 = 20;} //Tome of frost 33
else if (removeID == 18337) {totalPrice2 = 20;} //Bone crusher 34
else if (removeID == 10548) {totalPrice2 = 25;} //Fighter hat 35
else if (removeID == 10550) {totalPrice2 = 25;} //Ranger hat 36
else if (removeID == 19748) {totalPrice2 = 25;} //Ardougne cloak 37
else if (removeID == 20072) {totalPrice2 = 30;} //Dragon Defender 38
else if (removeID == 15241) {totalPrice2 = 30;} //Hand Cannon 39
else if (removeID == 23659) {totalPrice2 = 30;} //Tokhar 40
else if (removeID == 16711) {totalPrice2 = 25;} //Primail helm 1
else if (removeID == 17259) {totalPrice2 = 35;} //Primail top 2
else if (removeID == 16689) {totalPrice2 = 35;} //Primail legs 3
else if (removeID == 17361) {totalPrice2 = 25;} //Primail kiteshield 4
else if (removeID == 16359) {totalPrice2 = 15;} //Primail boots 5
else if (removeID == 16293) {totalPrice2 = 15;} //Primail gloves 6
else if (removeID == 16709) {totalPrice2 = 15;} //Promethium helm 7
else if (removeID == 17257) {totalPrice2 = 25;} //Promethium top 8
else if (removeID == 16687) {totalPrice2 = 25;} //Promethium legs 9
else if (removeID == 17359) {totalPrice2 = 15;} //Promethium kiteshield 10
else if (removeID == 16357) {totalPrice2 = 10;} //Promethium boots 11
else if (removeID == 16291) {totalPrice2 = 10;} //Promethium gloves 12
else if (removeID == 15126) {totalPrice2 = 15;} //Amulet of Ranging 13
else if (removeID == 18335) {totalPrice2 = 25;} //Arcane Stream Necklace 14
else if (removeID == 15486) {totalPrice2 = 25;} //Staff of light 15
else if (removeID == 18359) {totalPrice2 = 25;} //Chaotic Kiteshield 16
else if (removeID == 18361) {totalPrice2 = 25;} //Eagle-eye shield 17
else if (removeID == 18363) {totalPrice2 = 25;} //Farseer kiteshield 18
else if (removeID == 22482) {totalPrice2 = 20;} //Gano visor 19
else if (removeID == 22490) {totalPrice2 = 25;} //Gano top 20
else if (removeID == 22486) {totalPrice2 = 25;} //Gano bottom 21
else if (removeID == 4024) {totalPrice2 = 50;} //Ninja Gree Gree 22
else if (removeID == 4031) {totalPrice2 = 50;} //Karamja Gree Gree 23
else if (removeID == 4026) {totalPrice2 = 50;} //Gorilla Gree Gree 24
else if (removeID == 4029) {totalPrice2 = 50;} //Zombie Gree Gree 25
else if (removeID == 22406) {totalPrice2 = 150;} //Ancient Mace 26
else if (removeID == 9119) { totalPrice2 = 65; } //orange mask
else if (removeID == 9508) { totalPrice2 = 85; } //black phat
else if (removeID == 9510) { totalPrice2 = 85; } //pink phat
else if (removeID == 9516) { totalPrice2 = 75; } //black santa
else if (removeID == 20888) { totalPrice2 = 120; } //rainbow partyhat
else if (removeID == 9501) { totalPrice2 = 80; } //lime phat


					
					if (client.dPoints >= totalPrice2) {
						if (client.getActionAssistant().freeSlots() > 0) {
							client.dPoints = client.dPoints - totalPrice2;
							client.getActionAssistant().addItem(removeID, 1);
							ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
							item.setAmount(item.getAmount() - 1);
							item.setLastAutomaticStockChange(System.currentTimeMillis());
							if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
								s.removeItem(removeSlot);
								break;
							}
						} else {
							client.getActionAssistant().sendMessage("Not enough space in your inventory.");
							break;
						}
					} else {
						client.getActionAssistant().sendMessage("You don't have enough Donator Points.");
						break;
					}
				}
				client.getActionAssistant().resetItems(3823);
				s.updated();
			}
		}
	}	
	
	public void buyVotePoint(int removeID, int removeSlot, int amount) {
		int coinSlot;
		int totalPrice2 = 0;
		int id = 0;
		String coinName;
		if (client.getExtraData().containsKey("shop")) {
			id = (Integer) client.getExtraData().get("shop");
			Shop s = Server.getShopManager().getShops().get(id);
			if (s == null) {
				return;
			}
			coinSlot = client.vPoints;
			coinName = "Vote Points";
			if (coinSlot == -1) {
				client.getActionAssistant().sendMessage("You don't have enough Vote Points.");
				return;
			}
			if (s.getItemBySlot(removeSlot) == null) {
				return;
			}
			removeID = s.getItemBySlot(removeSlot).getId();
			if (amount > 0 && s.getItemBySlot(removeSlot).getId() == removeID) {
				if (amount > s.getItemBySlot(removeSlot).getAmount()) {
					amount = s.getItemBySlot(removeSlot).getAmount();
				}
				client.getActionAssistant().resetItems(3823);
				for (int i = 0; i < amount; i++) {
					totalPrice2 = s.getItemBuyValue(removeID);
					
if (removeID == 7901) { totalPrice2 = 600; }
else if (removeID == 7822) { totalPrice2 = 350; }

	
			
					if (client.vPoints >= totalPrice2) {
						if (client.getActionAssistant().freeSlots() > 0) {
							client.vPoints = client.vPoints - totalPrice2;
							client.getActionAssistant().addItem(removeID, 1);
							ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
							item.setAmount(item.getAmount() - 1);
							item.setLastAutomaticStockChange(System.currentTimeMillis());
							if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
								s.removeItem(removeSlot);
								break;
							}
						} else {
							client.getActionAssistant().sendMessage("Not enough space in your inventory.");
							break;
						}
					} else {
						client.getActionAssistant().sendMessage("You don't have enough Vote Points.");
						break;
					}
				}
				client.getActionAssistant().resetItems(3823);
				s.updated();
			}
		}
	}	
	
		public void buyStaff(int removeID, int removeSlot, int amount) {
		int coinSlot;
		int totalPrice2 = 0;
		int id = 0;
		String coinName;
		if (client.getExtraData().containsKey("shop")) {
			id = (Integer) client.getExtraData().get("shop");
			Shop s = Server.getShopManager().getShops().get(id);
			if (s == null) {
				return;
			}
		coinSlot =	client.getActionAssistant().getItemSlot(7963);
			coinName = "Staff tokens";
			if (coinSlot == -1) {
				client.getActionAssistant().sendMessage("You don't have enough Staff Tokens.");
				return;
			}
			if (s.getItemBySlot(removeSlot) == null) {
				return;
			}
			removeID = s.getItemBySlot(removeSlot).getId();
			if (amount > 0 && s.getItemBySlot(removeSlot).getId() == removeID) {
				if (amount > s.getItemBySlot(removeSlot).getAmount()) {
					amount = s.getItemBySlot(removeSlot).getAmount();
				}
				client.getActionAssistant().resetItems(3823);
				for (int i = 0; i < amount; i++) {
					totalPrice2 = s.getItemBuyValue(removeID);
						if (removeID == 13744) { totalPrice2 = 4; }
else if (removeID == 13740) { totalPrice2 = 5; }
else if (removeID == 13742) { totalPrice2 = 4; }
else if (removeID == 13738) { totalPrice2 = 4; }
else if (removeID == 13752) { totalPrice2 = 3; }
else if (removeID == 13740) { totalPrice2 = 5; }
else if (removeID == 13896) { totalPrice2 = 5; }
else if (removeID == 7821) { totalPrice2 = 3; }
else if (removeID == 9220) { totalPrice2 = 3; }
else if (removeID == 11718) { totalPrice2 = 3; }
else if (removeID == 11720) { totalPrice2 = 3; }
else if (removeID == 11722) { totalPrice2 = 3; }
else if (removeID == 11724) { totalPrice2 = 4; }
else if (removeID == 11726) { totalPrice2 = 4; }
else if (removeID == 13653) { totalPrice2 = 5; }
else if (removeID == 7901) { totalPrice2 = 14; }
else if (removeID == 7822) { totalPrice2 = 8; }
else if (removeID == 7983) { totalPrice2 = 10; }
else if (removeID == 11698) { totalPrice2 = 6; }
else if (removeID == 7819) { totalPrice2 = 4; }
else if (removeID == 9954) { totalPrice2 = 3; }
else if (removeID == 13899) { totalPrice2 = 6; }
else if (removeID == 13902) { totalPrice2 = 5; }
else if (removeID == 11694) { totalPrice2 = 10; }
else if (removeID == 11730) { totalPrice2 = 5; }
					
					if (client.playerItemsN[coinSlot] >= totalPrice2) {
						if (client.getActionAssistant().freeSlots() > 0) {
							client.playerItemsN[coinSlot] = client.playerItemsN[coinSlot] - totalPrice2;
							if (client.playerItemsN[coinSlot] <= 0) {
							client.getActionAssistant().deleteItem(7963, client.getActionAssistant().getItemSlot(7963), 1);
							}
							client.getActionAssistant().addItem(removeID, 1);
							ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
							item.setAmount(item.getAmount() - 1);
							item.setLastAutomaticStockChange(System.currentTimeMillis());
							if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
								s.removeItem(removeSlot);
								break;
							}
						} else {
							client.getActionAssistant().sendMessage("Not enough space in your inventory.");
							break;
						}
					} else {
						client.getActionAssistant().sendMessage("You don't have enough Staff tokens.");
						break;
					}
				}
				client.getActionAssistant().resetItems(3823);
				s.updated();
			}
		}
	}	
	
	public void buySlay(int removeID, int removeSlot, int amount) {
		int coinSlot;
		int totalPrice2 = 0;
		int id = 0;
		String coinName;
		if (client.getExtraData().containsKey("shop")) {
			id = (Integer) client.getExtraData().get("shop");
			Shop s = Server.getShopManager().getShops().get(id);
			if (s == null) {
				return;
			}
			coinSlot = client.slayerPoints;
			coinName = "Slayer Points";
			if (coinSlot == -1) {
				client.getActionAssistant().sendMessage("You don't have enough Slayer Points.");
				return;
			}
			if (s.getItemBySlot(removeSlot) == null) {
				return;
			}
			removeID = s.getItemBySlot(removeSlot).getId();
			if (amount > 0 && s.getItemBySlot(removeSlot).getId() == removeID) {
				if (amount > s.getItemBySlot(removeSlot).getAmount()) {
					amount = s.getItemBySlot(removeSlot).getAmount();
				}
				client.getActionAssistant().resetItems(3823);
				for (int i = 0; i < amount; i++) {
					totalPrice2 = s.getItemBuyValue(removeID);
					if (removeID == 13263) {
					totalPrice2 = 175;
					}
					else if (removeID == 7498) {
					totalPrice2 = 10;
					}
					else if (removeID == 7003) {
					totalPrice2 = 10;
					}
					else if (removeID == 6335) {
					totalPrice2 = 20;
					}
					else if (removeID == 6131) {
					totalPrice2 = 40;
					}
					if (client.slayerPoints >= totalPrice2) {
						if (client.getActionAssistant().freeSlots() > 0) {
							client.slayerPoints = client.slayerPoints - totalPrice2;
							client.getActionAssistant().addItem(removeID, 1);
							ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
							item.setAmount(item.getAmount() - 1);
							item.setLastAutomaticStockChange(System.currentTimeMillis());
							if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
								s.removeItem(removeSlot);
								break;
							}
						} else {
							client.getActionAssistant().sendMessage("Not enough space in your inventory.");
							break;
						}
					} else {
						client.getActionAssistant().sendMessage("You don't have enough Slayer Points.");
						break;
					}
				}
				client.getActionAssistant().resetItems(3823);
				s.updated();
			}
		}
	}	
	public void buyTok(int removeID, int removeSlot, int amount) {
		int coinSlot;
		int totalPrice2 = 0;
		int id = 0;
		String coinName;
		if (client.getExtraData().containsKey("shop")) {
			id = (Integer) client.getExtraData().get("shop");
			Shop s = Server.getShopManager().getShops().get(id);
			if (s == null) {
				return;
			}
			coinSlot = client.getActionAssistant().getItemSlot(6529);
			coinName = "Tokkul";
			if (coinSlot == -1) {
				client.getActionAssistant().sendMessage("You don't have enough Tokkul.");
				return;
			}
			if (s.getItemBySlot(removeSlot) == null) {
				return;
			}
			removeID = s.getItemBySlot(removeSlot).getId();
			if (amount > 0 && s.getItemBySlot(removeSlot).getId() == removeID) {
				if (amount > s.getItemBySlot(removeSlot).getAmount()) {
					amount = s.getItemBySlot(removeSlot).getAmount();
				}
				client.getActionAssistant().resetItems(3823);
				for (int i = 0; i < amount; i++) {
					totalPrice2 = s.getItemBuyValue(removeID);
					if (client.playerItemsN[coinSlot] >= totalPrice2) {
						if (client.getActionAssistant().freeSlots() > 0) {
							client.playerItemsN[coinSlot] = client.playerItemsN[coinSlot] - totalPrice2;
							client.getActionAssistant().addItem(removeID, 1);
							ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
							item.setAmount(item.getAmount() - 1);
							item.setLastAutomaticStockChange(System.currentTimeMillis());
							if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
								s.removeItem(removeSlot);
								break;
							}
						} else {
							client.getActionAssistant().sendMessage("Not enough space in your inventory.");
							break;
						}
					} else {
						client.getActionAssistant().sendMessage("You don't have enough Tokkul.");
						break;
					}
				}
				client.getActionAssistant().resetItems(3823);
				s.updated();
			}
		}
	}	
	public void buyVoid(int removeID, int removeSlot, int amount) {
		int coinSlot;
		int totalPrice2 = 0;
		int id = 0;
		String coinName;
		if (client.getExtraData().containsKey("shop")) {
			id = (Integer) client.getExtraData().get("shop");
			Shop s = Server.getShopManager().getShops().get(id);
			if (s == null) {
				return;
			}
			coinSlot = client.pcPoints;
			coinName = "PC Points";
			if (coinSlot == -1) {
				client.getActionAssistant().sendMessage("You don't have enough PC Points.");
				return;
			}
			if (s.getItemBySlot(removeSlot) == null) {
				return;
			}
			removeID = s.getItemBySlot(removeSlot).getId();
			if (amount > 0 && s.getItemBySlot(removeSlot).getId() == removeID) {
				if (amount > s.getItemBySlot(removeSlot).getAmount()) {
					amount = s.getItemBySlot(removeSlot).getAmount();
				}
				client.getActionAssistant().resetItems(3823);
				for (int i = 0; i < amount; i++) {
					totalPrice2 = s.getItemBuyValue(removeID);
					if (client.pcPoints >= totalPrice2) {
						if (client.getActionAssistant().freeSlots() > 0) {
							client.pcPoints = client.pcPoints - totalPrice2;
							client.getActionAssistant().addItem(removeID, 1);
							ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
							item.setAmount(item.getAmount() - 1);
							item.setLastAutomaticStockChange(System.currentTimeMillis());
							if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
								s.removeItem(removeSlot);
								break;
							}
						} else {
							client.getActionAssistant().sendMessage("Not enough space in your inventory.");
							break;
						}
					} else {
						client.getActionAssistant().sendMessage("You don't have enough PC Points.");
						break;
					}
				}
				client.getActionAssistant().resetItems(3823);
				s.updated();
			}
		}
	}	
	
	public void buyItem(int removeID, int removeSlot, int amount) {
		int coinSlot;
		int totalPrice2 = 0;
		int id = 0;
		String coinName;

		if (client.getExtraData().containsKey("shop")) {
			id = (Integer) client.getExtraData().get("shop");
			Shop s = Server.getShopManager().getShops().get(id);
			if (s == null) {
				return;
			}
			if(id == 49){
				if(client.getCombatLevel() < 80) {
					client.getActionAssistant().sendMessage("You need a higher combat level to use this shop.");
					return;
				}
			}			
			if(id == 6 && !RespProcessor.checkresp(client.playerName)) {
				client.getActionAssistant().sendMessage("You are not a respected donator. You do not have access to this shop.");
				return;
			}
			if(id == 50 && client.playerRights < 0) {
				client.getActionAssistant().sendMessage("You are not a donator. You do not have access to this shop.");
				return;
			}			
			if(id == 7 || id == 67 || id == 74) {
				coinSlot = client.pkpoints;
				coinName = "Pk Points";
				buyPk(removeID, removeSlot, amount);
				return;
			} else if (id == 12){
				coinSlot = client.pcPoints;
				coinName = "PC Points";
				buyVoid(removeID, removeSlot, amount);
				return;		
			}	else if (id == 52){
				coinSlot = client.slayerPoints;
				coinName = "Slayer Points";
				buySlay(removeID, removeSlot, amount);
				return;		
			
			} else if (id == 27){
				coinSlot = client.getActionAssistant().getItemSlot(s.getCurrency());
				coinName = "Tokkul";
				//buyTok(removeID, removeSlot, amount);
				//return;					
			} 
			else if (id == 72 || id == 73) {
				coinSlot = client.getActionAssistant().getItemSlot(s.getCurrency());
				coinName = "EP tickets (Ship tickets)";
				}
			else if (id == 57) {
				coinSlot = client.getActionAssistant().getItemSlot(s.getCurrency());
				coinName = "Vote Tickets";
				buyVote(removeID, removeSlot, amount);
				return;
				}
			else if (id == 55) {
				coinSlot = client.rewardPoints;
				coinName = "Achievement Points";
				buyAchieve(removeID, removeSlot, amount);
				return;		

			}
else if (id == 61) {
				coinSlot = client.getActionAssistant().getItemSlot(s.getCurrency());
				coinName = "Staff token";
				buyStaff(removeID, removeSlot, amount);
				return;
				}
				else if (id == 65) {
				coinSlot = client.cwPoints;
				coinName = "CastleWars Points";
				buyCW(removeID, removeSlot, amount);
				return;
				}
				else if (id == 81 || id == 82 || id == 83) {
				coinSlot = client.dPoints;
				coinName = "Donator Points";
				buyDonator(removeID, removeSlot, amount);
				return;
				}
				else if (id == 84) {
				coinSlot = client.vPoints;
				coinName = "Vote Points";
				buyVotePoint(removeID, removeSlot, amount);
				return;					
			} else {
				coinSlot = client.getActionAssistant().getItemSlot(s.getCurrency());
				coinName = Server.getItemManager().getItemDefinition(s.getCurrency()).getName();
			}
			if (coinSlot == -1) {
				client.getActionAssistant().sendMessage("You don't have enough "+ coinName.toLowerCase() + ".");
				return;
			}
			if (s.getItemBySlot(removeSlot) == null) {
				return;
			}
			removeID = s.getItemBySlot(removeSlot).getId();
			if (amount > 0 && s.getItemBySlot(removeSlot).getId() == removeID) {
				if (amount > s.getItemBySlot(removeSlot).getAmount()) {
					amount = s.getItemBySlot(removeSlot).getAmount();
				}
// Start Buy X
			if (amount > 27) {
	if ((int)(Math.floor(s.getItemBuyValue(removeID))/1000000)*amount > 1900) {
	client.getActionAssistant().sendMessage("Oops! Report this on forums 0x8438");
					return;
					}
					if (id == 72 || id == 73) {
					totalPrice2 = (int)Math.floor(client.getActionAssistant().getEpShopPrice(removeID))*amount;
					}
					else {
				totalPrice2 = (int)Math.floor(s.getItemBuyValue(removeID))*amount;
				}
				if (coinSlot == -1 && totalPrice2 != 0) {
					client.getActionAssistant().sendMessage("You don't have enough "+ coinName.toLowerCase() + ".");
					return;
				}
				if(totalPrice2 <= 1) {
					totalPrice2 = (int)Math.floor(s.getItemBuyValue(removeID))*amount;
					if ((removeID >= 4708 && removeID <= 4751)) {
					totalPrice2 = (int)Math.floor(3000000) * amount;
					} else if (removeID >= 6731 && removeID <= 6737 || removeID == 6920) {
					totalPrice2 = (int)Math.floor(2000000) * amount;
					} else if (removeID == 6585) {
					totalPrice2 = (int)Math.floor(10000000) * amount;
					}
					else if ( removeID == 11732) {
					totalPrice2 = (int)Math.floor(2000000) * amount;
					}
				}
				if (totalPrice2 <= 0) {
					if (client.getActionAssistant().freeSlots() > 0) {
						client.getActionAssistant().addItem(removeID, amount);
						ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
						item.setLastAutomaticStockChange(System.currentTimeMillis());
						if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
							s.removeItem(removeSlot);
							return;
						}
						client.getActionAssistant().resetItems(3823);
					} else {
						client.getActionAssistant().sendMessage("Not enough space in your inventory.");
					}
				
				} else if (client.playerItemsN[coinSlot] >= totalPrice2) {
					if(Item.itemIsNote[removeID] || Item.itemStackable[removeID]){
						if (client.getActionAssistant().freeSlots() > 0) {
							client.getActionAssistant().deleteItem(995, totalPrice2);
							client.getActionAssistant().addItem(removeID, amount);
							client.flushOutStream();
							ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
							item.setAmount(item.getAmount() - amount);
							item.setLastAutomaticStockChange(System.currentTimeMillis());
								if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
									s.removeItem(removeSlot);
									return;
								}
								client.getActionAssistant().resetItems(3823);
						} else {
							client.getActionAssistant().sendMessage("Not enough space in your inventory.");
							return;
						}
					} else {
						if (client.getActionAssistant().freeSlots() > amount) {
							client.getActionAssistant().deleteItem(995, totalPrice2);
							client.getActionAssistant().addItem(removeID, amount);
							client.flushOutStream();
							ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
							item.setAmount(item.getAmount() - amount);
							item.setLastAutomaticStockChange(System.currentTimeMillis());
								if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
									s.removeItem(removeSlot);
									return;
								}
								client.getActionAssistant().resetItems(3823);
						} else {
							client.getActionAssistant().sendMessage("Not enough space in your inventory.");
							return;
						}					
					
					}
				} else {
					client.getActionAssistant().sendMessage("You don't have enough "+ coinName.toLowerCase() + ".");
					return;
				}
            }else{
//end buy x
				client.getActionAssistant().resetItems(3823);
				for (int i = 0; i < amount; i++) {
				
					totalPrice2 = s.getItemBuyValue(removeID);
				
				
					if ((removeID >= 4708 && removeID <= 4751)) {
					totalPrice2 = 3000000;
					} else if ((removeID >= 6731 && removeID <= 6737) || removeID == 6920) {
					totalPrice2 = 2000000;
					}
					else if (removeID == 6585) {
					totalPrice2 = 10000000;
					}
					else if ( removeID == 11732) {
					totalPrice2 = 2000000;
					}
					if (id == 72 || id == 73) {
					totalPrice2 = (int)Math.floor(client.getActionAssistant().getEpShopPrice(removeID));
					}
					if (client.playerItemsN[coinSlot] >= totalPrice2) {
						if (client.getActionAssistant().freeSlots() > 0) {
							client.getActionAssistant().deleteItem(s.getCurrency(), totalPrice2);
							client.getActionAssistant().addItem(removeID, 1);
							ShopItem item = (ShopItem) s.getItemBySlot(removeSlot);
							item.setAmount(item.getAmount() - 1);
							item.setLastAutomaticStockChange(System.currentTimeMillis());
							if (item.getAmount() == 0 && !item.isAlwaysStocked()) {
								s.removeItem(removeSlot);
								break;
							}
						} else {
							client.getActionAssistant().sendMessage("Not enough space in your inventory.");
							break;
						}
					} else {
						client.getActionAssistant().sendMessage("You don't have enough "+ coinName.toLowerCase() + ".");
						break;
					}
				}
				client.getActionAssistant().resetItems(3823);
				s.updated();
			}
		}
	}
//id = 0;	
}
}
