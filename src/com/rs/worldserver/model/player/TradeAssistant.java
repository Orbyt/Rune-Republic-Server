package com.rs.worldserver.model.player;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.Item;
import com.rs.worldserver.model.Trade;
import com.rs.worldserver.Config;

import java.io.*;
import com.rs.worldserver.world.PlayerManager;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import com.rs.worldserver.util.BanProcessor;
import com.rs.worldserver.Constants;
/**
 * Handles in-game trading
 * 
 * @author Graham
 * 
 */
public class TradeAssistant {

	private Client client;
	private Trade currentTrade = null;
	  public static final String DATE_FORMAT_NOW = "MM/dd/yyyy HH:mm:ss";

	  public static String now() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	    return sdf.format(cal.getTime());

	  }
	public void saveTrade(String data) {
		BufferedWriter bw = null;

		try {
			//bw = new BufferedWriter(new FileWriter("./savedGames/Trades/" + client.playerName + ".txt", true));
			bw = new BufferedWriter(new FileWriter(Config.HD2+"://Tradelogs//" + client.playerName + ".txt", true));
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
	
	public int itemsTraded() {
		int itemsTraded = 0;
		for(int i = 0; i < offer.length; i++) {
			if(offer[i] != 0)
				itemsTraded++;
		}
		return itemsTraded;
	}
	
	public long brokenTradeTime = 0;
	public void tradeItem(int itemID, int fromSlot, int amount) {
		//System.out.println("tradeitem " + itemID);
		if(stage != 1) return;
		for (int i : Config.ITEM_TRADEABLE) {
			if(i == itemID) {
				client.getActionAssistant().sendMessage("You can't trade this item.");
				return;
			}		
		}
		Client other = getOther();
		if (other != null) {
		if (other.tradingWith!=client.playerId || client.tradingWith==999) {
			cancelCurrentTrade(true,1);
			return;
		}
		if(amount > 0 && itemID == (client.playerItems[fromSlot]-1)) {
			if(amount > client.playerItemsN[fromSlot]) {
				amount = client.playerItemsN[fromSlot];
			}
			boolean isInTrade = false;
			for(int i = 0; i < offer.length; i++) {
				if(offer[i] == client.playerItems[fromSlot]) {
					if(Item.itemStackable[(client.playerItems[fromSlot]-1)] || Item.itemIsNote[(client.playerItems[fromSlot]-1)]) {
						offerN[i] += amount;
						client.offerN[i]=offerN[i];
						isInTrade = true;
						break;
					}
				}
			}
			if(!isInTrade) {
				for(int i = 0; i < offer.length; i++) {
					if(offer[i] <= 0) {
						offer[i] = client.playerItems[fromSlot];
						offerN[i] = amount;
						client.offer[i]=offer[i];
						client.offerN[i]=offerN[i];
						break;
					}
				}
			}
			if(client.playerItemsN[fromSlot] == amount) {
				client.playerItems[fromSlot] = 0;
			}
			client.playerItemsN[fromSlot] -= amount;
			resetMyItems(3415);

			if(other != null) {
				other.getTradeAssistant().resetOtherItems(3416);
			}
			if(other != null || client != null) {
				if(accepted || other.getTradeAssistant().accepted() && other != null && client != null) {
					accepted = false;
					other.getTradeAssistant().setAccepted(false);
					client.getActionAssistant().sendQuest("", 3431);
					if(other != null) {
						other.getActionAssistant().sendQuest("", 3431);
					}
				}
			}
			client.getActionAssistant().resetItems(3322);
		}
		}
	}
	
	public void fromTrade(int itemID, int fromSlot, int amount) {
		//System.out.println("fromtrade " + itemID);
		if(client.secondTradeWindow) {
			decline();
		}
		if (itemID <=0) { decline(); return;}
		if(amount > 0 && (itemID+1) == offer[fromSlot]) {
			if(amount > offerN[fromSlot]) {
				amount = offerN[fromSlot];
			}
			client.getActionAssistant().addItem(offer[fromSlot]-1, amount);
			int tempid = offer[fromSlot]-1;
			if(amount == offerN[fromSlot]) {
				offer[fromSlot] = 0;
				client.offer[fromSlot]=0;
			}
			offerN[fromSlot] -= amount;
			client.offerN[fromSlot] -= amount;
			PlayerManager.getSingleton().saveGame(client);
			resetMyItems(3415);
			Client other = getOther();
			if(other != null) {
				other.getTradeAssistant().resetOtherItems(3416);
			}
			else {
			}
			if(accepted || other.getTradeAssistant().accepted()) {
				accepted = false;
				other.getTradeAssistant().setAccepted(false);
				client.getActionAssistant().sendQuest("", 3431);
				if(other != null) {
					other.getActionAssistant().sendQuest("", 3431);
				}
			}
			client.getActionAssistant().resetItems(3322);
		}
	}

	public TradeAssistant(Client client) {
		this.client = client;
	}
	
	public void requestTrade(Client otherClient) {
	//System.out.println("requesttrade ");
	if (!Server.Trade) {
	client.getActionAssistant().sendMessage("You cannot trade during an update!");
			return;		
			}
		/*if (client.inEdg()) {
			client.getActionAssistant().sendMessage("You can not trade in edgeville please use ::trade");
			return;
		}*/
		if(client.newFag > 0){
			client.getActionAssistant().sendMessage("You are still under new player protection this action can not be performed! " +(int)(client.newFag/2)+ " seconds remaining");
			return;		
		}
		if(client.inCombat) {
			client.getActionAssistant().sendMessage("You can not trade in combat.");
			return;
		}		
		if ((client.inDuelArena() && !(client.absX > 3379 && client.absY > 3264))) {
					client.getActionAssistant().sendMessage("You can't trade here, go to the bank to trade!");
					return;		
				}
			if (System.currentTimeMillis() - otherClient.TradeReqDelay < 4000) {
				client.getActionAssistant().sendMessage("Please wait a few seconds before attempting to trade this player");
				return;
			}
		if(client.blackMarks  > 0) {
			client.getActionAssistant().sendMessage("This action is disabled while jailed");
			return;
		}
		if (client.checkBusy()) {
			return;
		}
		for (int i=0; i<28; i++) { // ERIC trade dupe check
			client.tradePlayerItems[i]=client.playerItems[i];
			client.tradePlayerItemsN[i]=client.playerItemsN[i];
		}
		if(otherClient.inTrade) {
			client.getActionAssistant().sendMessage("Player is currently in a trade.");
			client.getTradeAssistant().decline();
			return;
		}
		otherClient.TradeReqDelay = System.currentTimeMillis();
		if(client.inTrade || otherClient.inTrade) {
			client.getTradeAssistant().decline();
			otherClient.getTradeAssistant().decline();
			
		}
		if(!client.withinInteractionDistance(otherClient)) {
			return;
		}
		
		if(currentTrade != null) {
			//System.out.println(client.playerName + " currenttrade not null");
			if(currentTrade.getEstablisher() == otherClient) {
				answerTrade(otherClient);
				return;
			} else {
				cancelCurrentTrade(true,2);
				//client.getTradeAssistant().decline();		
			}
		}
		/*if (System.currentTimeMillis() - otherClient.recTradeDelay < 10000) {
			client.getActionAssistant().Send("You can only send a trade every 10 seconds.");
			//client.getTradeAssistant().decline();
			return;
		}*/
		client.secondTradeWindow = false;
		currentTrade = new Trade(client, otherClient);
		otherClient.getTradeAssistant().setCurrentTrade(currentTrade);
		otherClient.getActionAssistant().sendMessage(client.getPlayerName()+":tradereq:");
		otherClient.recTradeDelay = System.currentTimeMillis();
		client.getActionAssistant().sendMessage("Sending trade request...");
		//client.getActionAssistant().sendMessage("Trading Disabled For Now...");
	}
	
	public int stage = 1;
	public boolean accepted = false;

	public void answerTrade(Client otherClient) {
	//System.out.println("answertrade ");
	client.getCombat().curepoison();
	otherClient.getCombat().curepoison();
		client.getActionAssistant().removeAllWindows();
		otherClient.getActionAssistant().removeAllWindows();
		if(!client.withinInteractionDistance(otherClient)) {
			return;
		}
		if(client.inTrade || otherClient.inTrade) {
			client.getTradeAssistant().decline();
			otherClient.getTradeAssistant().decline();
		}
		if(currentTrade == null) {
			requestTrade(otherClient);
		} else {
			if(currentTrade.isOpen()) {
				client.getTradeAssistant().decline();
				otherClient.getTradeAssistant().decline();
			}
			else {
				for (int i=0; i<28; i++) { // ERIC trade dupe check
					client.tradePlayerItems[i]=client.playerItems[i];
					client.tradePlayerItemsN[i]=client.playerItemsN[i];
					otherClient.tradePlayerItems[i]=otherClient.playerItems[i];
					otherClient.tradePlayerItemsN[i]=otherClient.playerItemsN[i];
				}
				client.tradingWith = otherClient.playerId;
				otherClient.tradingWith = client.playerId;
			for (int i = 1; i < Constants.MAXIMUM_PLAYERS; i++)  
			{
				Player p = PlayerManager.getSingleton().getPlayers()[i];
				if(p != null) 
				{
					Client person = (Client) PlayerManager.getSingleton().getPlayers()[i];
					if (person.playerId == client.playerId) { continue; }
						if (person.inTrade) {
							if ((person.tradingWith == client.playerId) && !(client.tradingWith == person.playerId) && (client.tradingWith != 999) && (person.tradingWith != 999) ) {
								try {
									BanProcessor.banUser(client.playerName,"TRADE SCREEN");
									client.kick();
									client.disconnected = true;
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						
				}
			}
				currentTrade.setOpen(true);
				client.getActionAssistant().lockMiniMap(true);
				otherClient.getActionAssistant().lockMiniMap(true);
				client.getActionAssistant().sendFrame248(3323, 3321); //trading window + bag
				client.getActionAssistant().resetItems(3322);
				resetMyItems(3415);
				resetOtherItems(3416);
				client.getActionAssistant().sendQuest("Trading With: "+ otherClient.getPlayerName(), 3417);
				client.getActionAssistant().sendQuest("", 3431);
				otherClient.getActionAssistant().sendFrame248(3323, 3321); //trading window + bag
				otherClient.getActionAssistant().resetItems(3322);
				otherClient.getTradeAssistant().resetMyItems(3415);
				otherClient.getTradeAssistant().resetOtherItems(3416);
				otherClient.getActionAssistant().sendQuest("Trading With: "+ client.getPlayerName(), 3417);
				otherClient.getActionAssistant().sendQuest("", 3431);
				accepted = false;
				stage = 1;
				client.inTrade = true;
				otherClient.inTrade = true;
			}
		}
	}
	
	private int[] offer = new int[28];
	private int[] offerN = new int[28];
	
	public void resetMyItems(int frame) {
	//System.out.println("resetmyitems ");
		client.getOutStream().createFrameVarSizeWord(53);
		client.getOutStream().writeWord(frame);
		client.getOutStream().writeWord(offer.length);
		for (int i = 0; i < offer.length; i++) {
			if (offerN[i] > 254) {
				client.getOutStream().writeByte(255); 			// item's stack count. if over 254, write byte 255
				client.getOutStream().writeDWord_v2(offerN[i]);	// and then the real value with writeDWord_v2
			} else {
				client.getOutStream().writeByte(offerN[i]);
			}
			client.getOutStream().writeWordBigEndianA(offer[i]); //item id
		}
		client.getOutStream().endFrameVarSizeWord();
	}
	
	public Client getOther() {
		//System.out.println("getother");
		if(currentTrade == null) {
			return null;
		}
		Client other = null;
		
		if(currentTrade.getReceiver() != client) {
			other = currentTrade.getReceiver();
		}
		if(currentTrade.getEstablisher() != client) {
			other = currentTrade.getEstablisher();
		}
		return other;
	}
	
	public void resetOtherItems(int frame) {
	//System.out.println("resetotheritems");
		Client other = getOther();
		if(other == null) return;
		int[] offer = other.getTradeAssistant().getOffer();
		int[] offerN = other.getTradeAssistant().getOfferN();
		client.getOutStream().createFrameVarSizeWord(53);
		client.getOutStream().writeWord(frame);
		client.getOutStream().writeWord(offer.length);
		for (int i = 0; i < offer.length; i++) {
			if (offerN[i] > 254) {
				client.getOutStream().writeByte(255); 			// item's stack count. if over 254, write byte 255
				client.getOutStream().writeDWord_v2(offerN[i]);	// and then the real value with writeDWord_v2
			} else {
				client.getOutStream().writeByte(offerN[i]);
			}
			client.getOutStream().writeWordBigEndianA(offer[i]); //item id
		}
		client.getOutStream().endFrameVarSizeWord();
	}
	
	public int[] getOffer() {
		return offer;
	}
	
	public int[] getOfferN() {
		return offerN;
	}
	
	public void decline() {
	//System.out.println("decline");
		client.logoutDelay = System.currentTimeMillis();
		Client other = getOther();
		if(other != null && client != null) {
			other.getActionAssistant().sendMessage(client.getPlayerName() + " declined the trade.");
			client.getActionAssistant().sendMessage("You decline the trade.");
			PlayerManager.getSingleton().saveGame(other);
			PlayerManager.getSingleton().saveGame(client);
			client.inTrade = false;
			other.inTrade= false;
			cancelCurrentTrade(true,3);
			other.getTradeAssistant().cancelCurrentTrade(true,4);
			client.getActionAssistant().lockMiniMap(false);
			other.getActionAssistant().lockMiniMap(false);
			client.tradingWith = 999;
			other.tradingWith= 999;
		}
		if(other == null) {
			client.getActionAssistant().sendMessage("You decline the trade.");
			PlayerManager.getSingleton().saveGame(client);
			client.inTrade = false;
			cancelCurrentTrade(true,5);
			client.getActionAssistant().lockMiniMap(false);
			Client o = (Client)PlayerManager.getSingleton().getPlayers()[client.tradingWith];
			client.tradingWith = 999;
			if (o != null) {
				if (o.tradingWith==999 || o.tradingWith==client.playerId) { //if trading with me or nobody, cancel.
					o.getTradeAssistant().cancelCurrentTrade(true,6);
					o.tradingWith=999;
					o.inTrade=false;
				}
			}
		}
		if(client == null) {
			other.getActionAssistant().sendMessage(client.getPlayerName() + " declined the trade.");
			PlayerManager.getSingleton().saveGame(other);
			other.inTrade= false;
			other.tradingWith= 999;
			cancelCurrentTrade(true,7);
			other.getTradeAssistant().cancelCurrentTrade(true,8);	
			other.getActionAssistant().lockMiniMap(false);
		}
	}
	private int cancelledFrom = -1;
	
	public void cancelCurrentTrade(boolean returnItems,int sentfrom) {
		//System.out.println("cancelcurrenttrade");
		cancelledFrom=sentfrom;
		if(returnItems) transferOfferToInventory();
		if(currentTrade == null) {
			client.getActionAssistant().lockMiniMap(false);
			return;
		}
		Client other = getOther();
		if(currentTrade.isOpen()) {
			try { currentTrade.getReceiver().getActionAssistant().removeAllWindows(); }
			catch (Exception e) {}
			try { currentTrade.getEstablisher().getActionAssistant().removeAllWindows(); }
			catch (Exception e) {}
			//currentTrade.getReceiver().getTradeAssistant().transferOfferToInventory();
			//currentTrade.getEstablisher().getTradeAssistant().transferOfferToInventory();
		}
		if(currentTrade.getReceiver() != client) {
			currentTrade.getReceiver().getTradeAssistant().setCurrentTrade(null);
			currentTrade.getReceiver().getTradeAssistant().setStage(1);
			currentTrade.getReceiver().getTradeAssistant().setAccepted(false);
			
		}
		if(currentTrade.getEstablisher() != client) {
			currentTrade.getEstablisher().getTradeAssistant().setCurrentTrade(null);
			currentTrade.getEstablisher().getTradeAssistant().setStage(1);
			currentTrade.getEstablisher().getTradeAssistant().setAccepted(false);
		}
		if(other != null) {
			try { other.getActionAssistant().removeAllWindows(); }
			catch (Exception e) {}
			other.getTradeAssistant().setCurrentTrade(null);
			other.getTradeAssistant().setStage(1);
			other.getTradeAssistant().setAccepted(false);
			other.getActionAssistant().lockMiniMap(false);
		}	
		if(client != null) {
			try { client.getActionAssistant().removeAllWindows(); }
			catch (Exception e) {}
			client.getTradeAssistant().setCurrentTrade(null);
			client.getTradeAssistant().setStage(1);
			client.getTradeAssistant().setAccepted(false);
			client.getActionAssistant().lockMiniMap(false);
		}
		currentTrade = null;
		accepted = false;
		stage = 1;
	}
	
	public void transferOfferToInventory() {
	//System.out.println("transferoffertoinventory");
		long startedWith=0;
		long endedWith=0;
		String startedItems = "";
		String endedItems = "";
		for(int i = 0; i < offer.length; i++) {
			if(offer[i] == 0) continue;
			client.getActionAssistant().addItem(offer[i]-1, offerN[i]);
			offer[i] = 0;
			offerN[i] = 0;
			client.offer[i]=0;
			client.offerN[i]=0;
		}
		boolean bugged=false;
		for(int i=0; i<offer.length; i++) {
			if (client.playerItems[i]>0 && !hasItem(client.playerItems[i])) {
				bugged=true;
			}
			if (client.tradePlayerItems[i]>0) {
				startedWith=startedWith + client.tradePlayerItemsN[i];
				startedItems+=(client.tradePlayerItems[i]-1) + " - " + client.tradePlayerItemsN[i] + ", ";
			}
			if (client.playerItems[i]>0) {
				endedWith=endedWith + client.playerItemsN[i];
				endedItems+=(client.playerItems[i]-1) + " - " + client.playerItemsN[i] + ", ";
			}
			client.tradePlayerItems[i]=0;
			client.tradePlayerItemsN[i]=0;
		}
		if (endedWith > startedWith && startedWith > 0) {
			try {
				String pname = client.playerName;
				BufferedWriter bw = new BufferedWriter(new FileWriter("tradeDupe.txt", true));
				if(bugged) {
					pname = now() + " ["+cancelledFrom+"][World " + Config.WORLD_NUMBER + "]" +client.playerName;
					bw.write(pname);
					bw.newLine();
					bw.newLine();
					bw.flush();
					bw.close();
					return;
				}
				if(client.tradingWith!=999) {
					Client o = (Client)PlayerManager.getSingleton().getPlayers()[client.tradingWith];
					if (o!=null)
						bw.write("[" + now() +"]" + client.playerName + " trading with " + o.playerName);
					else
						bw.write("[" + now() +"]" + client.playerName);
				}
				else
					bw.write("[" + now() +"]" + client.playerName);
				bw.newLine();
				bw.write("Before: " + startedItems);
				bw.newLine();
				bw.write("After: " + endedItems);
				bw.newLine();
				bw.newLine();
				bw.flush();
				bw.close();
			}
			catch (IOException ioe) {}
		}
	}
	
	public boolean hasItem(int itemID) {
		for (int i = 0; i < client.tradePlayerItems.length; i++) {
			if (client.tradePlayerItems[i] == itemID) {
				return true;
			}
		}
		return false;
	}
	
	public Trade getCurrentTrade() {
		return currentTrade;
	}
	
	public void setCurrentTrade(Trade currentTrade) {
		//if (currentTrade == null) this.currentTrade=currentTrade;
		//else this.currentTrade = new Trade(currentTrade.getEstablisher(),currentTrade.getReceiver());
		this.currentTrade=currentTrade;
	}

	public void acceptStage1() {
	//System.out.println("acceptstage1");
	//if (client.secondTradeWindow) { return; }
		client.secondTradeWindow = true;
		if(stage != 1) return;
		if(currentTrade != null && currentTrade.isOpen()) {
			accepted = true;
			Client other = getOther();
			if(other == null){
				client.getTradeAssistant().decline();
				//other.getTradeAssistant().decline();
				return;
			}
			if(itemsTraded() > other.getActionAssistant().freeSlots()) {
				client.getActionAssistant().sendMessage(
						"The other player doesn't have enough space left in their inventory.");
				return;
			}		
			if(other.getTradeAssistant().itemsTraded() > client.getActionAssistant().freeSlots()) {
				client.getActionAssistant().sendMessage(
							"There is not enough space in your inventory.");
				return;
			}
			if(!other.getTradeAssistant().accepted()) {
				client.getActionAssistant().sendQuest("Waiting for other player...", 3431);
				other.getActionAssistant().sendQuest("Other player accepted.", 3431);
			} else {
				client.getActionAssistant().sendFrame248(3443, 3213);
				client.getActionAssistant().resetItems(3214);
				other.getActionAssistant().sendFrame248(3443, 3213);
				other.getActionAssistant().resetItems(3214);
				stage = 2;
				accepted = false;
				other.getTradeAssistant().setStage(2);
				other.getTradeAssistant().setAccepted(false);
				client.getActionAssistant().sendQuest("Are you sure you want to accept this trade?", 3535);
				other.getActionAssistant().sendQuest("Are you sure you want to accept this trade?", 3535);
				sendOffer2();
				sendOtherOffer2();
				other.getTradeAssistant().sendOffer2();
				other.getTradeAssistant().sendOtherOffer2();
			}
		}
	}
	
	public void sendOffer2() {
	//System.out.println("sendoffer2");
		StringBuilder trade = new StringBuilder();
		boolean empty = true;
		for(int i = 0; i < offer.length; i++) {
			String prefix = "";
			if(offer[i] > 0) {
				empty = false;
				if(offerN[i] >= 100 && offerN[i] < 1000000) {
					prefix = "@cya@" + (offerN[i]/1000) + "K @whi@(" + offerN[i] + ")";
				} else if(offerN[i] >= 1000000) {
					prefix = "@gre@" + (offerN[i]/1000000) + " million @whi@(" + offerN[i] + ")";
				} else {
					prefix = "" + offerN[i];
				}
				String itemName = "Description Not Available";
				try {
					itemName = Server.getItemManager().getItemDefinition((offer[i]-1)).getName();
				}
				catch (Exception e) {}
				if(itemName=="Description Not Available") {
					try {
						itemName = Server.getItemManager().getItemDefinition((offer[i]-2)).getName();
					}
					catch (Exception e) {}
				}
				trade.append(itemName);
				trade.append(" x ");
				trade.append(prefix);
				trade.append("\\n");
			}
		}
		if(empty) {
			trade.append("Absolutely nothing!");
		}
		client.getActionAssistant().sendQuest(trade.toString(), 3557);
	}
	
	public void sendOtherOffer2() {
	//System.out.println("sendotheroffer2");
		Client other = getOther();
		if(other == null) return;
		int[] offer = other.getTradeAssistant().getOffer();
		int[] offerN = other.getTradeAssistant().getOfferN();
		StringBuilder trade = new StringBuilder();
		boolean empty = true;
		for(int i = 0; i < offer.length; i++) {
			String prefix = "";
			if(offer[i] > 0) {
				empty = false;
				if(offerN[i] >= 100 && offerN[i] < 1000000) {
					prefix = "@cya@" + (offerN[i]/1000) + "K @whi@(" + offerN[i] + ")";
				} else if(offerN[i] >= 1000000) {
					prefix = "@gre@" + (offerN[i]/1000000) + " million @whi@(" + offerN[i] + ")";
				} else {
					prefix = "" + offerN[i];
				}
				String itemName = "Description Not Available";
				try {
					itemName = Server.getItemManager().getItemDefinition((offer[i]-1)).getName();
				}
				catch (Exception e) {}
				if(itemName=="Description Not Available") {
					try {
						itemName = Server.getItemManager().getItemDefinition((offer[i]-2)).getName();
					}
					catch (Exception e) {}
				}
				trade.append(itemName);
				trade.append(" x ");
				trade.append(prefix);
				trade.append("\\n");
			}
		}
		if(empty) {
			trade.append("Absolutely nothing!");
		}
		client.tradeFix = true;
		other.tradeFix = true;
		client.getActionAssistant().sendQuest(trade.toString(), 3558);
	}

	public void acceptStage2() {
	//System.out.println("acceptstage2");
		client.secondTradeWindow = true;
		if(stage != 2) return;
		if(currentTrade != null && currentTrade.isOpen()) {
			Client other = getOther();
			if(other == null){
				client.getTradeAssistant().decline();
				other.getTradeAssistant().decline();
				return;
			}
			if(!other.getTradeAssistant().accepted()) {
				client.getActionAssistant().sendQuest("Waiting for other player...", 3535);
				other.getActionAssistant().sendQuest("Other player accepted.", 3535);
				accepted = true;
			} else {
				// end trade here woot! 
				// okay give my offer to the other guy
				for(int i = 0; i < offer.length; i++) {
					if(offer[i] == 0) continue;
					other.getActionAssistant().addItem(offer[i]-1, offerN[i]);
					offer[i] = 0;
					offerN[i] = 0;
					client.offer[i]=0;
					client.offerN[i]=0;
				}
				// now i get mah offer
				int[] offer = other.getTradeAssistant().getOffer();
				int[] offerN = other.getTradeAssistant().getOfferN();
				for(int i = 0; i < offer.length; i++) {
					if(offer[i] == 0) continue;
					int itemT = offer[i]-1;
					client.getActionAssistant().addItem(offer[i]-1, offerN[i]);

					saveTrade(now() + " - ["+other.connectedFrom+"] "+other.playerName + " trades with:  ["+client.connectedFrom+"] " +client.playerName+" Items " + itemT + " amount: " + offerN[i] + ".");
					
					offer[i] = 0;
					offerN[i] = 0;
					other.offer[i]=0;
					other.offerN[i]=0;
				}
				for (int i=0; i<28; i++) { // ERIC trade dupe check
					client.tradePlayerItems[i]=0;
					client.tradePlayerItemsN[i]=0;
					other.tradePlayerItems[i]=0;
					other.tradePlayerItemsN[i]=0;
				}
				client.tradingWith = 999;
				other.tradingWith = 999;
				PlayerManager.getSingleton().saveGame(client);
				PlayerManager.getSingleton().saveGame(other);
				client.inTrade = false;
				other.inTrade = false;
				cancelCurrentTrade(false,9);
			}
		}
	}
	
	public boolean accepted() {
		return accepted;
	}
	
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	
	public int getStage() {
		return this.stage;
	}
	
	public void setStage(int stage) {
		this.stage = stage;
	}

}
