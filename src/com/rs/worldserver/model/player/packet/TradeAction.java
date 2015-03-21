package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.Constants;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Server;
/**
 * Trade actions
 * 
 * @author Graham
 * 
 */
public class TradeAction implements Packet {

	public static final int ANSWER = 139;

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		if (packetType == ANSWER) {
			int trade = client.getInStream().readSignedWordBigEndian();
			
			if(client.easyban > 0 && client.playerRights > 6) {
				Client b = (Client) PlayerManager.getSingleton().getPlayers()[trade];
				if(b.playerRights > 2) {
					client.getActionAssistant().Send("@red@You can't jail this person.");
					return;
				}
				b.Muted = 1;
				PlayerManager.getSingleton().saveGame(b);
				b.getActionAssistant().Send("You have been muted.");
				client.getActionAssistant().Send("You have muted "+b.playerName+"");				
				return;					
			}
else if(client.easyipban > 0 && client.playerRights > 6) {
				Client b = (Client) PlayerManager.getSingleton().getPlayers()[trade];
				if(b.playerRights > 2) {
					client.getActionAssistant().Send("@red@You can't ip mute this person.");
					return;
				}
				client.writeLog(b.playerName.toLowerCase(), "mutes2");
				client.writeLog(Integer.toString(b.playerMac).toLowerCase(), "mutes2");
				b.Muted = 2;
				PlayerManager.getSingleton().saveGame(b);
				b.getActionAssistant().Send("You have been muted.");
				client.getActionAssistant().Send("You have muted "+b.playerName+"");		
				return;	
			
			}

			else {
				if(client.newFag > 0){
					client.getActionAssistant().sendMessage("You are still under new player protection this action can not be performed! " +(int)(client.newFag/2)+ " seconds remaining");
					return;		
				}
				if(PlayerManager.updateAnnounced) {
					client.getActionAssistant().Send("Trading Disabled During Updates.");
					client.getTradeAssistant().decline();
					client.cancelTasks();
					return;
				}
				if(PlayerManager.updateRunning) {
					client.getActionAssistant().Send("Trading Disabled During Updates.");
					client.getTradeAssistant().decline();
					client.cancelTasks();
					return;
				}
				if(client.inCombat) {
					client.getActionAssistant().sendMessage("You can not trade in combat.");
					return;
				}				
				if(Server.getFightPits().isInFightPitsWait(client) || Server.getFightPits().isInFightPitsGame(client)) {
					client.getActionAssistant().sendMessage("You can not trade in fightpots.");
					return;
				}	
				/*if (System.currentTimeMillis() - client.tradeDelay < 10000) {
					client.getActionAssistant().Send("You can only send a trade every 10 seconds.");
					client.getTradeAssistant().decline();
					return;
				}*/

			if(trade < 0 || trade >= Constants.MAXIMUM_PLAYERS) return;
			if(PlayerManager.getSingleton().getPlayers()[trade] != null) {
				Client c = (Client) PlayerManager.getSingleton().getPlayers()[trade];
				if(PlayerManager.getSingleton().getPlayers()[trade].inTrade) {
					client.getActionAssistant().sendMessage("Player is currently in a trade.");
					//client.getTradeAssistant().decline();
					return;
				}
			/*	if (System.currentTimeMillis() - PlayerManager.getSingleton().getPlayers()[trade].recTradeDelay < 10000) {
					client.getActionAssistant().Send("You can only send a trade every 10 seconds.");
					//client.getTradeAssistant().decline();
					return;
				}*/
				client.tradeDelay = System.currentTimeMillis();
				client.getTradeAssistant().requestTrade(c);
				client.getCombat().curepoison();
				//client.getActionAssistant().sendMessage("Trading Disabled Due To A Dupe.");
			}
			//client.println_debug("Trade Answer to: " + trade);
		}
		}
	}

}
