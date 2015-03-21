package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.player.CastleWars;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Server;
import com.rs.worldserver.Config;



/**
 * Clicking an item, bury bone, eat food etc
 **/
public class ClickItem implements Packet {

	@Override
	public void handlePacket(final Client client, int packetType, int packetSize) {
		int junk = client.getInStream().readSignedWordBigEndianA();
		int abc = 0, cba = 0, aaa = 0, abc2 = 0, heal = 0, add = 0;
		int itemSlot = client.getInStream().readUnsignedWordA();
		int itemId = client.getInStream().readUnsignedWordBigEndian();
		client.getCombat().resetAttack();
		if (itemId > 25838 || itemSlot > 28 || itemId <= 0 || itemSlot < 0) {//change
			client.getActionAssistant().sendMessage("Error code 9333");
			return;
		}
		if (itemId != client.playerItems[itemSlot] - 1) {
			return;
		}
		Misc.println_debug("Item ID: " + itemId +" Slot: "+itemSlot);
		//client.getActionAssistant().sendMessage(""+itemId);
		switch(itemId) {
			case 4155:
					if(client.slayerAmount > 0){
						client.getActionAssistant().sendFrame200(4888, 591);
						client.getActionAssistant().sendQuest(
								Server.getNpcManager().getNPCDefinition(8274).getName(),
								4889);
						client.getActionAssistant().sendQuest("Your task is to kill "+client.slayerAmount+" "+Server.getNpcManager().getNPCDefinition(client.slayerTask).getName()+".", 4890);
						client.getActionAssistant().sendQuest("You have "+client.slayerPoints+" Slayer Points.", 4891);
						client.getActionAssistant().sendFrame75(8274, 4888);
						client.getActionAssistant().sendFrame164(4887);
						client.flushOutStream();
					} else {
						client.getActionAssistant().sendFrame200(4888, 591);
						client.getActionAssistant().sendQuest(
								Server.getNpcManager().getNPCDefinition(8274).getName(),
								4889);
						client.getActionAssistant().sendQuest("Visit a slayer master to recieve another task.", 4890);
						client.getActionAssistant().sendQuest("You have "+client.slayerPoints+" Slayer Points.", 4891);
						client.getActionAssistant().sendFrame75(8274, 4888);
						client.getActionAssistant().sendFrame164(4887);
						client.flushOutStream();
					}
            break;
			case 4053:
				if (Server.getCastleWars().isInCw(client)) {
					if (Server.getCastleWars().barricade[(Server.getCastleWars().teamNumber(client) - 1)] < 11) {
						for (Barricade x : Server.getCastleWars().barricades) {
							if (x.getAbsX() == client.getAbsX()
								&& x.getAbsY() == client.getAbsY()
								&& x.getHeightLevel() == client.getHeightLevel()) {
								client.getActionAssistant().sendMessage("There is already a barricade set up here!");
								return;
							}
						}
						Server.getCastleWars().barricades.add(new Barricade(
							Server.getNpcManager().spawnNPC(1532, client.getAbsX(), client.getAbsY(), client.getHeightLevel(), 0),
							client.getAbsX(), client.getAbsY(), client.getHeightLevel(),Server.getCastleWars().teamNumber(client) - 1));
						client.getActionAssistant().sendMessage("You set up a barricade.");
						System.out.println(Server.getCastleWars().barricades.get(0).getId());
						Server.getCastleWars().barricade[(Server.getCastleWars().teamNumber(client) - 1)]++;
						client.getActionAssistant().deleteItem(4053, itemSlot, 1);
					} else {
						client.getActionAssistant().sendMessage("Your team has already set up 10 barricades!");
						return;
					}
				} else {
					client.getActionAssistant().sendMessage("You have to be in castlewars to use barricades!");
					return;
				}
				break;
			case 7775:
				client.getOutStream().createFrame(27);
				//int jt = client.getInStream().readDWord();
				client.flushOutStream();
				client.tokenRedeem = true;
			break;
            case 2446: //Antipoison(4)
				if(!client.canEat){return;}
				if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                client.getActionAssistant().sendMessage("You drink a dose of the potion");
				//client.getActionAssistant().deleteItem(2446, itemSlot, 1);
				//client.getActionAssistant().addItem(175, 1);
				client.getActionAssistant().replaceItem(2446, 175, itemSlot, 1);
                client.getCombat().curepoison();
				if(client.sounds == 1) {
					client.getActionAssistant().frame174(334, 050, 000);
				}	
				EventManager.getSingleton().addEvent(client,"c 2446", new Event() {

				@Override
					public void execute(EventContainer c) {
					
						c.stop();
					}

					@Override
					public void stop() {
						
						
						client.setpBusy(false);client.canEat = true;

					}

				}, 900);
                break;
            case 175: //Antipoison(3)
				if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink a dose of the antipoison.");
             //   client.getActionAssistant().deleteItem(175, itemSlot, 1);
			//	client.getActionAssistant().addItem(177, 1);
				client.getActionAssistant().replaceItem(175, 177, itemSlot, 1);
                    client.getCombat().curepoison();
 			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 175", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);               
                break;
            case 177: //Antipoison(2)
				if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink a dose of the antipoison.");
              //  client.getActionAssistant().deleteItem(177, itemSlot, 1);
			//	client.getActionAssistant().addItem(179, 1);
			client.getActionAssistant().replaceItem(177, 179, itemSlot, 1);
 			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 177",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);               
                break;
            case 179: //Antipoison(1)
				if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink the last dose of the super anti-poison.");
                //client.getActionAssistant().deleteItem(179, itemSlot, 1);
				//client.getActionAssistant().addItem(229, 1);
				client.getActionAssistant().replaceItem(179, 229, itemSlot, 1);
               			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 179",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
                break;
            case 2448: //superAntipoison(4)
				if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink a dose of the super anti-poison.");
                //client.getActionAssistant().deleteItem(2448, itemSlot, 1);
				//client.getActionAssistant().addItem(181, 1);
				client.getActionAssistant().replaceItem(2448, 181, itemSlot, 1);
                			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 2448", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
                break;
            case 181: //superAntipoison(3)
				if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink a dose of the super anti-poison.");
                //client.getActionAssistant().deleteItem(181, itemSlot, 1);
				//client.getActionAssistant().addItem(183, 1);
				client.getActionAssistant().replaceItem(181, 183, itemSlot, 1);
                			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 181", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
                break;
            case 183: //superAntipoison(2)
				if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink a dose of the super anti-poison.");
                //client.getActionAssistant().deleteItem(183, itemSlot, 1);
				//client.getActionAssistant().addItem(185, 1);
				client.getActionAssistant().replaceItem(183, 185, itemSlot, 1);
                			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 183", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
                break;
            case 185: //superAntipoison(1)
				if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink the last dose of the super anti-poison.");
                //client.getActionAssistant().deleteItem(185, itemSlot, 1);
				//client.getActionAssistant().addItem(229, 1);
				client.getActionAssistant().replaceItem(185, 229, itemSlot, 1);
                			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 185",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
                break;
            case 5943: //extra-strongAntidote(4)
				if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink a dose of the extra strong antidote");
                //client.getActionAssistant().deleteItem(5943, itemSlot, 1);
				//client.getActionAssistant().addItem(5945, 1);
                			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 5943",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
                break;
            case 5945: //extra-strongAntidote(3)
				if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink a dose of the extra strong antidote");
                //client.getActionAssistant().deleteItem(5945, itemSlot, 1);
				//client.getActionAssistant().addItem(5947, 1);
                			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 5945", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
                break;
            case 5947: //extra-strongAntidote(2)
				if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink a dose of the extra strong antidote");
                //client.getActionAssistant().deleteItem(5947, itemSlot, 1);
				//client.getActionAssistant().addItem(5949, 1);
                			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 5947", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
                break;
            case 5949: //extra-strongAntidote(1)
				if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink the last dose of the extra strong antidote");
                //client.getActionAssistant().deleteItem(5949, itemSlot, 1);
				//client.getActionAssistant().addItem(229, 1);
 			EventManager.getSingleton().addEvent(client, "c 5949",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);               
                break;
            case 5952: //super-strongAntidote(4)
			if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink a dose of the super strong antidote");
                //client.getActionAssistant().deleteItem(5952, itemSlot, 1);
				//client.getActionAssistant().addItem(5954, 1);
                			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 5952", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
                break;
            case 5954: //super-strongAntidote(3)
			if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink a dose of the super strong antidote");
                //client.getActionAssistant().deleteItem(5954, itemSlot, 1);
				//client.getActionAssistant().addItem(5956, 1);
                			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 5954", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
                break;
            case 5956: //super-strongAntidote(2)
			if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink a dose of the super strong antidote");
                //client.getActionAssistant().deleteItem(5956, itemSlot, 1);
				//client.getActionAssistant().addItem(5958, 1);
                			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 5956", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
                break;
				case 10025:
					int box2[] = {15272,15272,15272,15272,15272,15272,4151,15272,15272,15272,15272,15272,15272,15272,15272,13845,15272,15272,15272,15272,6585,
					15272,15272,4151,15272,15272,15272,15272,15272,15272,13846,15272,15272,15272,15272,15272,4151,15272,15272,13847,15272,15272,15272,15272,15272,
					15272,15272,4151,15272,15272,15272,15272,15272,15272,13846,15272,15272,15272,15272,15272,4151,15272,15272,13847,15272,15272,15272,15272,15272,
					15272,15272,15272,15272,15272,15272,15272,6585,15272,15272,15272,13848,15272,15272,15272,15272,15272,15272,6585,15272,6585,15272,4151,15272,
					15272,13850,15272,15272,15272,13851,15272,15272,4151,15272,15272,13852,15272,15272,15272,15272,15272,15272,15272,15272,15272,15272,15272,15272,
					15272,15272,4151,15272,15272,15272,15272,15272,15272,13846,15272,15272,15272,15272,15272,4151,15272,15272,13847,15272,15272,15272,15272,15272,
					15272,15272,4151,15272,15272,15272,15272,15272,15272,13846,15272,15272,15272,15272,15272,4151,15272,15272,13847,15272,15272,15272,15272,15272,
					15272,15272,15272,15272,15272,15272,15272,15272,15272,15272,15272,15272,6585,15272,15272,15272,15272,15272,15272,15272,15272,15272,13854,15272,
					15272,15272,15272,15272,15272,15272,13855,15272,15272,15272,15272,15272,13856,15272,15272,15272,15272,13857,15272,15272,15272,15272,15272,15272,};
					int item2 = box2[(int)(Math.random()*box2.length)];
					client.getActionAssistant().deleteItem(10025, client.getActionAssistant().getItemSlot(10025), 1);
					client.getActionAssistant().addItem(item2, 1);
					client.getActionAssistant().sendMessage("@red@Well Done! or is it Better Luck Next Time!?");
				break;
				//case 14664:
					//int box2[] = {20077,6856,6857,6858,6859,6860,6861,6862,6863,15422,15423,15424,15425};
					//int item2 = box2[(int)(Math.random()*box2.length)];
					//client.getActionAssistant().deleteItem(14664, client.getActionAssistant().getItemSlot(14664), 1);
					//client.getActionAssistant().addItem(item2, 1);
				//break;
				case 6183:
					PlayerManager.getSingleton().sendGlobalMessage("[@red@Mystery Box@bla@]@mag@ " + client.playerName + " has obtained @dre@Lime Whip @mag@from the Mystery Box!");
				break;
				case 14664:
					PlayerManager.getSingleton().sendGlobalMessage("[@red@Mystery Box@bla@]@mag@ " + client.playerName + " has obtained @dre@Ancient Mace @mag@from the Mystery Box!");
				break;
				/*case 6199:
				 int box[] = {537,4151,952,2451,6686,2431,6915,6890,7498,4717,4719,4721,4723,11724,11726,11700,11698,11696,946,2347,227,229,301,590,15273,11733,6921,311,2572,6735,6737,6733,6731,1540,1511,526,626,1007,1113,1127,1201,440,1067,1712,1725,1727,1729,1731,841,839,855,1923,1921,9185,4827,6570,4725,4727,4729,4731,4733,4735,4737,4739,4754,4756,4758,4760,4709,4711,4713,4715,4746,4748,4750,4752,1106,1157,1071,1143,1197,1181,1249,1434,1615,4587,4087,526,3751,3753,10828,2550,2497,1305,4584,6586,1149,3140};
        //int item = Config.CustomHats[(int)(Math.random()*Config.CustomHats.length)];
		int item = box[(int)(Math.random()*box.length)];
		int amount = client.getActionAssistant().mysteryBoxAmounts(item);
		client.getActionAssistant().deleteItem(6199, client.getActionAssistant().getItemSlot(6199), 1);
		   client.getActionAssistant().addItem(item, amount);
										client.achievementProgress[4] += 1;
										if (client.achievementProgress[4] == 50) {
											client.getNRAchievements().checkMisc(client,18);
										}
    	
				
				break;*/
				case 15086:
				case 15088:
				case 15090:
				case 15092:
				case 15094:
				case 15096:
				case 15098:
				case 15100:
				if (!client.inDice()) {
					client.getActionAssistant().sendMessage("You can only dice in the Dicing Arena.");
					return;
				}
				if (client.playerRights != 12) {
				if (!client.playerName.equalsIgnoreCase("Orbit")) {
					client.getActionAssistant().Send("@dre@You must obtain the Dice Rank to Host!");
					return;
				}
				}
				if (itemId == 15086 ||itemId == 15088 ||itemId == 15090 ||itemId == 15092 ||itemId == 15094 ||itemId == 15096 ||itemId == 15098 ||itemId == 15100) { //Dice Bag ID
				if (System.currentTimeMillis() - client.diceDelay >= 5000) {
					DiceManager.rollDice(client, itemId);
					client.diceDelay = System.currentTimeMillis();
				}
				}
				break;
				case 15084:
				if (!client.inDice()) {
					client.getActionAssistant().sendMessage("You can only open the Dicing Bag inside the Dicing Arena.");
					return;
				}
				if (client.playerRights != 12) {
				if (!client.playerName.equalsIgnoreCase("Orbit")) {
					client.getActionAssistant().Send("@dre@You must obtain the Dice Rank to open the Dice Bag!");
					return;
				}
				}
				client.necklace = 4;
				client.getActionAssistant().clearQuestInterface();
				client.getActionAssistant().sendFrame126("Die (4 sides)", 2494);
				client.getActionAssistant().sendFrame126("Die (6 sides)", 2495);
				client.getActionAssistant().sendFrame126("Die (8 sides)", 2496);
				client.getActionAssistant().sendFrame126("Die (10 sides)", 2497);
				client.getActionAssistant().sendFrame126("Next set of Dices", 2498);
				client.getActionAssistant().sendQuestSomething(8143);
				client.getActionAssistant().sendFrame164(2492);
				client.flushOutStream();	
				break;
				case 10831:
				if (itemId == 10831 && client.getActionAssistant().playerHasItem(995, 2000000000)) {
				if (System.currentTimeMillis() - client.diceDelay >= 2000) {
					client.getActionAssistant().deleteItem(10831, 1);
					client.getActionAssistant().deleteItem(995, 2000000000);
					client.getActionAssistant().addItem(10835, 1);
					client.getActionAssistant().sendMessage("@blu@You fill the Empty Cash Bag with 2b(2,000,000,000) Coins.");
					client.diceDelay = System.currentTimeMillis();
				}
				}
				break;
				case 10835:
				if (itemId == 10835 && client.getActionAssistant().playerHasItem(995)) {
					client.getActionAssistant().sendMessage("Please remove the coins from your inventory before opening");
				} else if (System.currentTimeMillis() - client.diceDelay >= 2000) {
					client.getActionAssistant().deleteItem(10835, 1);
					client.getActionAssistant().addItem(10831, 1);
					client.getActionAssistant().addItem(995, 2000000000);
					client.getActionAssistant().sendMessage("@red@You empty the Full Cash Bag and get 2b(2,000,000,000) Coins.");
					client.diceDelay = System.currentTimeMillis();
				}
				break;
				case 18768:
				if (itemId == 18768) {
						MysteryBox.OpenMysteryBox(client);
					}
				break;
            case 5958: //super-strongAntidote(1
			if(!client.canEat){return;}
                if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.nopots) {
					return;
				}				
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
                    client.getCombat().curepoison();
                   client.getActionAssistant().sendMessage("You drink the last dose of the super strong antidote");
                //client.getActionAssistant().deleteItem(5958, itemSlot, 1);
				//client.getActionAssistant().addItem(229, 1);
            if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 5958", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
                break;		
			case 4079:
				client.getActionAssistant().startAnimation(1457);
			break;
			case 7498:
			if(client.inDuelArena()){
				return;
			}
				client.lamps++;
				if(client.lamps > 44){
					client.getNRAchievements().checkSkilling(client,0);
				}
				client.getActionAssistant().triggerRandom();
				break;
			case 2856:
				if(client.clueString.equalsIgnoreCase("none") || client.clueString.equals(null)) {
					client.clueString = client.getClueAssistant().randomClueString();
					client.getActionAssistant().Send(client.clueString);
					client.cluelevel = 2856;
				} else {
					client.getActionAssistant().Send(client.clueString);
					client.cluelevel = 2856;
				}
				break;
			case 2857:
				if(client.clueString.equalsIgnoreCase("none") || client.clueString.equals(null)) {
					client.clueString = client.getClueAssistant().randomClueString();
					client.getActionAssistant().Send(client.clueString);
					client.cluelevel = 2857;
				} else {
					client.getActionAssistant().Send(client.clueString);
					client.cluelevel = 2857;
				}
				break;
			case 2858:
				if(client.clueString.equalsIgnoreCase("none") || client.clueString.equals(null)) {
					client.clueString = client.getClueAssistant().randomClueString();
					client.getActionAssistant().Send(client.clueString);
					client.cluelevel = 2858;
				} else {
					client.getActionAssistant().Send(client.clueString);
					client.cluelevel = 2858;
				}
				break;
			case 952:
				client.getActionAssistant().digSpade();
				break;
			case 2830:
				client.caskets++;
				if(client.caskets >= 20){
					client.getNRAchievements().checkMisc(client,6);
				}
				if(client.caskets >= 50){
					client.getNRAchievements().checkMisc(client,7);
				}
				if(client.caskets >= 150){
					client.getNRAchievements().checkMisc(client,8);
				}
			
				int casket = Misc.random(3);
				if(Misc.random(75) == 8) {
					client.getActionAssistant().addItem(client.getClueAssistant().randomReward1(), 1);
					client.getActionAssistant().deleteItem(2830, client.getActionAssistant().getItemSlot(2830), 1);
		
				}                                                 
				else if(casket == 0) {
				int runereward = Misc.random(5000) + 2000;
						client.getActionAssistant().deleteItem(2830, client.getActionAssistant().getItemSlot(2830), 1);
						client.getActionAssistant().addItem(client.getClueAssistant().randomReward4(), runereward);
		
				} 
				else if(casket == 1) {
				int potionrewards = Misc.random(500) +100;
						client.getActionAssistant().deleteItem(2830, client.getActionAssistant().getItemSlot(2830), 1);
						client.getActionAssistant().addItem(client.getClueAssistant().randomReward6(), potionrewards);
				}
				else if(casket == 2) {                
						client.getActionAssistant().deleteItem(2830, client.getActionAssistant().getItemSlot(2830), 1);
						client.getActionAssistant().addItem(client.getClueAssistant().randomReward5(), 1);
				}
				else if(casket == 3) {
					int foodreward = Misc.random(400) + 200;
						client.getActionAssistant().deleteItem(2830, client.getActionAssistant().getItemSlot(2830), 1);
						client.getActionAssistant().addItem(client.getClueAssistant().randomReward3(), foodreward);
				}
				
				break;
			case 1891: // cake
			client.getActionAssistant().eatFood(4, 1600, itemId, itemSlot);
			break;
			
			case 1893: // 2/3 cake
			client.getActionAssistant().eatFood(4, 1600, itemId, itemSlot);
			break;
			
			case 1895: // 1/4 cake
			client.getActionAssistant().eatFood(4, 1600, itemId, itemSlot);
			break;
			
			case 2309: // bread
			client.getActionAssistant().eatFood(2, 1600, itemId, itemSlot);
			break;
			
			case 1901: // 1/4 choc cake
			client.getActionAssistant().eatFood(5, 1600, itemId, itemSlot);
			break;
			case 1917: // beer
			client.getActionAssistant().eatFood(3, 1600, itemId, itemSlot);
			break;			
			case 319: // anchovies
			client.getActionAssistant().eatFood(3, 1600, itemId, itemSlot);
			break;
			case 315: // shrimp
			client.getActionAssistant().eatFood(3, 1600, itemId, itemSlot);
			break;
			case 325: // shrimp
			client.getActionAssistant().eatFood(4, 1600, itemId, itemSlot);
			break;				
			case 355: // mackerel
			client.getActionAssistant().eatFood(6, 1600, itemId, itemSlot);
			break;
			
			case 339: // cod
			client.getActionAssistant().eatFood(7, 1600, itemId, itemSlot);
			break;	
						
			case 333: // trout
			client.getActionAssistant().eatFood(7, 1600, itemId, itemSlot);
			break;
						
			case 351: // pike
			client.getActionAssistant().eatFood(8, 1600, itemId, itemSlot);
			break;	
			
			case 329: // salmon
			client.getActionAssistant().eatFood(9, 1600, itemId, itemSlot);
			break;
					
			case 361: // tuna
			client.getActionAssistant().eatFood(10, 1600, itemId, itemSlot);
			break;
			case 7946: // monk
			client.getActionAssistant().eatFood(16, 1600, itemId, itemSlot);
			break;	
			case 7943: // monk
			client.getActionAssistant().eatFood(16, 1600, itemId, itemSlot);
			break;			
			case 397: // sea turtle
			client.getActionAssistant().eatFood(21, 1600, itemId, itemSlot);
			break;				
			case 379: // lobster
			if(client.duelRule[6]){
				client.getActionAssistant().sendMessage("Food has been disabled in this duel!");
				return;
			}
			client.getActionAssistant().eatFood(12, 1600, itemId, itemSlot);
			break;
			
			case 365: // bass
			client.getActionAssistant().eatFood(13, 1600, itemId, itemSlot);
			break;
			
			case 373: // swordfish
			client.getActionAssistant().eatFood(14, 1600, itemId, itemSlot);
			break;
			
			case 385:
			if(client.duelRule[6]){
				client.getActionAssistant().sendMessage("Food has been disabled in this duel!");
				return;
			}
			client.getActionAssistant().eatFood(20, 1600, itemId, itemSlot);
			break;
			
			case 391: // manta
			if(client.duelRule[6]){
				client.getActionAssistant().sendMessage("Food has been disabled in this duel!");
				return;
			}
			client.getActionAssistant().eatFood(22, 1600, itemId, itemSlot);
			break;
			case 4049: // bandage
			if(client.duelRule[6]){
				client.getActionAssistant().sendMessage("Food has been disabled in this duel!");
				return;
			}
			client.getActionAssistant().eatFood(18, 500, itemId, itemSlot);
			break;
			case 10102: // rock
			case 15272:
			if(client.duelRule[6]){
				client.getActionAssistant().sendMessage("Food has been disabled in this duel!");
				return;
			}
			client.getActionAssistant().eatFood(23, 1600, itemId, itemSlot);
			/*if(client.playerLevel[3] + 10 > client.getActionAssistant().getLevelForXP(client.playerXP[3]) + client.getActionAssistant().getMaxHP() + 10) {
				client.playerLevel[3] = 109;
			} 
			else {
				client.playerLevel[3] +=  10;
			}*/
			client.getActionAssistant().refreshSkill(3);
			break;
			case 2142: // meat
			if(client.duelRule[6]){
				client.getActionAssistant().sendMessage("Food has been disabled in this duel!");
				return;
			}
			client.getActionAssistant().eatFood(3, 1600, itemId, itemSlot);
			break;				

			case 3125:
			case 3127:
			case 3128:
			case 3129:
			case 3130:
			case 3131:
			case 3132:
			case 3133:
			case 526:
				client.getActionAssistant().buryBone(150, 1500, itemId, itemSlot);
			break;
			
			case 532:
				client.getActionAssistant().buryBone(300, 1500, itemId, itemSlot);
			break;
			case 4834:
			case 4832:
			case 4830:
			case 4812:
			case 534:
			case 536:
				client.getActionAssistant().buryBone(500, 1500, itemId, itemSlot);
			break;
			case 6729:
				client.getActionAssistant().buryBone(600, 1500, itemId, itemSlot);
			break;
			case 15308:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[0]) *0.22) + 5);
			client.playerLevel[0] += abc;
			if (client.playerLevel[0] > client.getLevelForXP(client.playerXP[0]) +abc) {
			client.playerLevel[0] = abc + client.getLevelForXP(client.playerXP[0]);
			}
			client.getActionAssistant().replaceItem(15308, 15309, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme attack potion");
			client.getActionAssistant().refreshSkill(0);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
			case 15309:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[0]) *0.22) + 5);
			
			
			client.playerLevel[0] += abc;
			if (client.playerLevel[0] > client.getLevelForXP(client.playerXP[0]) +abc) {
			client.playerLevel[0] = abc + client.getLevelForXP(client.playerXP[0]);
			}
			client.getActionAssistant().replaceItem(15309, 15310, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme attack potion");
			client.getActionAssistant().refreshSkill(0);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
				case 15310:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[0]) *0.22) + 5);
			
			
			client.playerLevel[0] += abc;
			if (client.playerLevel[0] > client.getLevelForXP(client.playerXP[0]) +abc) {
			client.playerLevel[0] = abc + client.getLevelForXP(client.playerXP[0]);
			}
			client.getActionAssistant().replaceItem(15310, 15311, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme attack potion");
			client.getActionAssistant().refreshSkill(0);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;	
			case 15311:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[0]) *0.22) + 5);
			
			
			client.playerLevel[0] += abc;
			if (client.playerLevel[0] > client.getLevelForXP(client.playerXP[0]) +abc) {
			client.playerLevel[0] = abc + client.getLevelForXP(client.playerXP[0]);
			}
			client.getActionAssistant().replaceItem(15311, 229, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme attack potion");
			client.getActionAssistant().refreshSkill(0);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
			case 15312:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = ((int)Math.floor(client.getLevelForXP(client.playerXP[2]) *0.22) +5);
			
			
			client.playerLevel[2] += abc;
			if (client.playerLevel[2] > client.getLevelForXP(client.playerXP[2]) +abc) {
			client.playerLevel[2] = abc + client.getLevelForXP(client.playerXP[2]);
			}
			client.getActionAssistant().replaceItem(15312,15313, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme strength potion");
			client.getActionAssistant().refreshSkill(2);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;			case 15313:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[2]) *0.22)+5);
			
			
			client.playerLevel[2] += abc;
			if (client.playerLevel[2] > client.getLevelForXP(client.playerXP[2]) +abc) {
			client.playerLevel[2] = abc + client.getLevelForXP(client.playerXP[2]);
			}
			client.getActionAssistant().replaceItem(15313,15314, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme strength potion");
			client.getActionAssistant().refreshSkill(2);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
						case 15314:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[2]) *0.22)+5);
			
			
			client.playerLevel[2] += abc;
			if (client.playerLevel[2] > client.getLevelForXP(client.playerXP[2]) +abc) {
			client.playerLevel[2] = abc + client.getLevelForXP(client.playerXP[2]);
			}
			client.getActionAssistant().replaceItem(15314,15315, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme strength potion");
			client.getActionAssistant().refreshSkill(2);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
						case 15315:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[2]) *0.22) +5);
			
			
			client.playerLevel[2] += abc;
			if (client.playerLevel[2] > client.getLevelForXP(client.playerXP[2]) +abc) {
			client.playerLevel[2] = abc + client.getLevelForXP(client.playerXP[2]);
			}
			client.getActionAssistant().replaceItem(15315,229, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme strength potion");
			client.getActionAssistant().refreshSkill(2);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
				case 15316:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
		
				abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[1]) *0.22)+5);
			
			
			client.playerLevel[1] += abc;
			if (client.playerLevel[1] > client.getLevelForXP(client.playerXP[1]) +abc) {
			client.playerLevel[1] = abc + client.getLevelForXP(client.playerXP[1]);
			}
			client.getActionAssistant().replaceItem(15316,15317, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme defence potion");
			client.getActionAssistant().refreshSkill(1);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
				case 15317:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[1]) *0.22)+5);
			
			
			client.playerLevel[1] += abc;
			if (client.playerLevel[1] > client.getLevelForXP(client.playerXP[1]) +abc) {
			client.playerLevel[1] = abc + client.getLevelForXP(client.playerXP[1]);
			}
			client.getActionAssistant().replaceItem(15317,15318, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme defence potion");
			client.getActionAssistant().refreshSkill(1);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
			case 15318:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[1]) *0.22)+5);
			
			
			client.playerLevel[1] += abc;
			if (client.playerLevel[1] > client.getLevelForXP(client.playerXP[1]) +abc) {
			client.playerLevel[1] = abc + client.getLevelForXP(client.playerXP[1]);
			}
			client.getActionAssistant().replaceItem(15318,15319, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme defence potion");
			client.getActionAssistant().refreshSkill(1);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
				case 15319:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[1]) *0.22)+5);
			
			
			client.playerLevel[1] += abc;
			if (client.playerLevel[1] > client.getLevelForXP(client.playerXP[1]) +abc) {
			client.playerLevel[1] = abc + client.getLevelForXP(client.playerXP[1]);
			}
			client.getActionAssistant().replaceItem(15319,229, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme defence potion");
			client.getActionAssistant().refreshSkill(1);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
				case 15324:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = (int)Math.floor(4 + (client.getLevelForXP(client.playerXP[4]) /5.2));
			
			
			client.playerLevel[4] += abc;
			if (client.playerLevel[4] > client.getLevelForXP(client.playerXP[4]) +abc) {
			client.playerLevel[4] = abc + client.getLevelForXP(client.playerXP[4]);
			}
			client.getActionAssistant().replaceItem(15324,15325, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme ranging potion");
			client.getActionAssistant().refreshSkill(4);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
				case 15325:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = (int)Math.floor(4 + (client.getLevelForXP(client.playerXP[4]) /5.2));
			
			
			client.playerLevel[4] += abc;
			if (client.playerLevel[4] > client.getLevelForXP(client.playerXP[4]) +abc) {
			client.playerLevel[4] = abc + client.getLevelForXP(client.playerXP[4]);
			}
			client.getActionAssistant().replaceItem(15325,15326, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme ranging potion");
			client.getActionAssistant().refreshSkill(4);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
				case 15326:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = (int)Math.floor(4 + (client.getLevelForXP(client.playerXP[4]) /5.2));
			
			
			client.playerLevel[4] += abc;
			if (client.playerLevel[4] > client.getLevelForXP(client.playerXP[4]) +abc) {
			client.playerLevel[4] = abc + client.getLevelForXP(client.playerXP[4]);
			}
			client.getActionAssistant().replaceItem(15326,15327, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme ranging potion");
			client.getActionAssistant().refreshSkill(4);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
				case 15327:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = (int)Math.floor(4 + (client.getLevelForXP(client.playerXP[4]) /5.2));
			
			
			client.playerLevel[4] += abc;
			if (client.playerLevel[4] > client.getLevelForXP(client.playerXP[4]) +abc) {
			client.playerLevel[4] = abc + client.getLevelForXP(client.playerXP[4]);
			}
			client.getActionAssistant().replaceItem(15327, 229, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme ranging potion");
			client.getActionAssistant().refreshSkill(4);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
				case 15320:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = 7;
			
			
			client.playerLevel[6] += abc;
			if (client.playerLevel[6] > client.getLevelForXP(client.playerXP[6]) +abc) {
			client.playerLevel[6] = abc + client.getLevelForXP(client.playerXP[6]);
			}
			client.getActionAssistant().replaceItem(15320,15321, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme magic potion");
			client.getActionAssistant().refreshSkill(6);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
				case 15321:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = 7;
			
			
			client.playerLevel[6] += abc;
			if (client.playerLevel[6] > client.getLevelForXP(client.playerXP[6]) +abc) {
			client.playerLevel[6] = abc + client.getLevelForXP(client.playerXP[6]);
			}
			client.getActionAssistant().replaceItem(15321,15322, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme magic potion");
			client.getActionAssistant().refreshSkill(6);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
				case 15322:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = 7;
			
			
			client.playerLevel[6] += abc;
			if (client.playerLevel[6] > client.getLevelForXP(client.playerXP[6]) +abc) {
			client.playerLevel[6] = abc + client.getLevelForXP(client.playerXP[6]);
			}
			client.getActionAssistant().replaceItem(15322,15323, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme magic potion");
			client.getActionAssistant().refreshSkill(6);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
				case 15323:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = 7;
			
			
			client.playerLevel[6] += abc;
			if (client.playerLevel[6] > client.getLevelForXP(client.playerXP[6]) +abc) {
			client.playerLevel[6] = abc + client.getLevelForXP(client.playerXP[6]);
			}
			client.getActionAssistant().replaceItem(15323,229, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the extreme magic potion");
			client.getActionAssistant().refreshSkill(6);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
	case 15332:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.playerLevel[3] <= 51) {
					client.getActionAssistant().sendMessage("You need more hitpoints to do this");
					break;
				}
			
				client.hitDiff = 50;
						client.playerLevel[3] -= 50;
						client.getActionAssistant().refreshSkill(3);
						client.hitUpdateRequired = true;
						client.updateRequired = true;
						client.appearanceUpdateRequired = true;
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			
			abc = 7;
			client.playerLevel[6] += abc;
			if (client.playerLevel[6] > client.getLevelForXP(client.playerXP[6]) +abc) {
			client.playerLevel[6] = abc + client.getLevelForXP(client.playerXP[6]);
			}
			
			abc = (int)Math.floor(4 + (client.getLevelForXP(client.playerXP[0]) /5.2));
			client.playerLevel[4] += abc;
			if (client.playerLevel[4] > client.getLevelForXP(client.playerXP[4]) +abc) {
			client.playerLevel[4] = abc + client.getLevelForXP(client.playerXP[4]);
			}
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[0]) *0.22) + 5);
			client.playerLevel[0] += abc;
			if (client.playerLevel[0] > client.getLevelForXP(client.playerXP[0]) +abc) {
			client.playerLevel[0] = abc + client.getLevelForXP(client.playerXP[0]);
			}
			abc = ((int)Math.floor(client.getLevelForXP(client.playerXP[2]) *0.22) +5);
			
			
			client.playerLevel[2] += abc;
			if (client.playerLevel[2] > client.getLevelForXP(client.playerXP[2]) +abc) {
			client.playerLevel[2] = abc + client.getLevelForXP(client.playerXP[2]);
			}
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[1]) *0.22)+5);
			
			
			client.playerLevel[1] += abc;
			if (client.playerLevel[1] > client.getLevelForXP(client.playerXP[1]) +abc) {
			client.playerLevel[1] = abc + client.getLevelForXP(client.playerXP[1]);
			}
			client.getActionAssistant().replaceItem(15332,15333, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the overload potion");
			client.getActionAssistant().refreshSkill(6);
			client.getActionAssistant().refreshSkill(1);
			client.getActionAssistant().refreshSkill(0);
			client.getActionAssistant().refreshSkill(2);
			client.getActionAssistant().refreshSkill(4);
			client.achievementProgress[3] += 1;
			if (client.achievementProgress[3] == 1000) {
					client.getNRAchievements().checkMisc(client,17);
			}
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
				if (!client.overLoad) {
				client.getActionAssistant().overLoadEvent(client);
			}
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
			case 15333:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.playerLevel[3] <= 51) {
					client.getActionAssistant().sendMessage("You need more hitpoints to do this");
					break;
				}
			
				client.hitDiff = 50;
						client.playerLevel[3] -= 50;
						client.getActionAssistant().refreshSkill(3);
						client.hitUpdateRequired = true;
						client.updateRequired = true;
						client.appearanceUpdateRequired = true;
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			
			abc = 7;
			client.playerLevel[6] += abc;
			if (client.playerLevel[6] > client.getLevelForXP(client.playerXP[6]) +abc) {
			client.playerLevel[6] = abc + client.getLevelForXP(client.playerXP[6]);
			}
			
			abc = (int)Math.floor(4 + (client.getLevelForXP(client.playerXP[0]) /5.2));
			client.playerLevel[4] += abc;
			if (client.playerLevel[4] > client.getLevelForXP(client.playerXP[4]) +abc) {
			client.playerLevel[4] = abc + client.getLevelForXP(client.playerXP[4]);
			}
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[0]) *0.22) + 5);
			client.playerLevel[0] += abc;
			if (client.playerLevel[0] > client.getLevelForXP(client.playerXP[0]) +abc) {
			client.playerLevel[0] = abc + client.getLevelForXP(client.playerXP[0]);
			}
			abc = ((int)Math.floor(client.getLevelForXP(client.playerXP[2]) *0.22) +5);
			
			
			client.playerLevel[2] += abc;
			if (client.playerLevel[2] > client.getLevelForXP(client.playerXP[2]) +abc) {
			client.playerLevel[2] = abc + client.getLevelForXP(client.playerXP[2]);
			}
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[1]) *0.22)+5);
			
			
			client.playerLevel[1] += abc;
			if (client.playerLevel[1] > client.getLevelForXP(client.playerXP[1]) +abc) {
			client.playerLevel[1] = abc + client.getLevelForXP(client.playerXP[1]);
			}
			client.getActionAssistant().replaceItem(15333,15334, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the overload potion");
			client.getActionAssistant().refreshSkill(6);
			client.getActionAssistant().refreshSkill(1);
			client.getActionAssistant().refreshSkill(0);
			client.getActionAssistant().refreshSkill(2);
			client.getActionAssistant().refreshSkill(4);
			client.achievementProgress[3] += 1;
			if (client.achievementProgress[3] == 1000) {
					client.getNRAchievements().checkMisc(client,17);
			}
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
				if (!client.overLoad) {
				client.getActionAssistant().overLoadEvent(client);
			}
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
			case 15334:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.playerLevel[3] <= 51) {
					client.getActionAssistant().sendMessage("You need more hitpoints to do this");
					break;
				}
			
			
				client.hitDiff = 50;
						client.playerLevel[3] -= 50;
						client.getActionAssistant().refreshSkill(3);
						client.hitUpdateRequired = true;
						client.updateRequired = true;
						client.appearanceUpdateRequired = true;
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			
			abc = 7;
			client.playerLevel[6] += abc;
			if (client.playerLevel[6] > client.getLevelForXP(client.playerXP[6]) +abc) {
			client.playerLevel[6] = abc + client.getLevelForXP(client.playerXP[6]);
			}
			
			abc = (int)Math.floor(4 + (client.getLevelForXP(client.playerXP[0]) /5.2));
			client.playerLevel[4] += abc;
			if (client.playerLevel[4] > client.getLevelForXP(client.playerXP[4]) +abc) {
			client.playerLevel[4] = abc + client.getLevelForXP(client.playerXP[4]);
			}
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[0]) *0.22) + 5);
			client.playerLevel[0] += abc;
			if (client.playerLevel[0] > client.getLevelForXP(client.playerXP[0]) +abc) {
			client.playerLevel[0] = abc + client.getLevelForXP(client.playerXP[0]);
			}
			abc = ((int)Math.floor(client.getLevelForXP(client.playerXP[2]) *0.22) +5);
			
			
			client.playerLevel[2] += abc;
			if (client.playerLevel[2] > client.getLevelForXP(client.playerXP[2]) +abc) {
			client.playerLevel[2] = abc + client.getLevelForXP(client.playerXP[2]);
			}
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[1]) *0.22)+5);
			
			
			client.playerLevel[1] += abc;
			if (client.playerLevel[1] > client.getLevelForXP(client.playerXP[1]) +abc) {
			client.playerLevel[1] = abc + client.getLevelForXP(client.playerXP[1]);
			}
			client.getActionAssistant().replaceItem(15334,15335, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the overload potion");
			client.getActionAssistant().refreshSkill(6);
			client.getActionAssistant().refreshSkill(1);
			client.getActionAssistant().refreshSkill(0);
			client.getActionAssistant().refreshSkill(2);
			client.getActionAssistant().refreshSkill(4);
			client.achievementProgress[3] += 1;
			if (client.achievementProgress[3] == 1000) {
					client.getNRAchievements().checkMisc(client,17);
			}
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			if (!client.overLoad) {
				client.getActionAssistant().overLoadEvent(client);
			}
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
			case 15335:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				if (client.playerLevel[3] <= 51) {
					client.getActionAssistant().sendMessage("You need more hitpoints to do this");
					break;
				}
				client.hitDiff = 50;
						client.playerLevel[3] -= 50;
						client.getActionAssistant().refreshSkill(3);
						client.hitUpdateRequired = true;
						client.updateRequired = true;
						client.appearanceUpdateRequired = true;
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			
			abc = 7;
			client.playerLevel[6] += abc;
			if (client.playerLevel[6] > client.getLevelForXP(client.playerXP[6]) +abc) {
			client.playerLevel[6] = abc + client.getLevelForXP(client.playerXP[6]);
			}
			
			abc = (int)Math.floor(4 + (client.getLevelForXP(client.playerXP[0]) /5.2));
			client.playerLevel[4] += abc;
			if (client.playerLevel[4] > client.getLevelForXP(client.playerXP[4]) +abc) {
			client.playerLevel[4] = abc + client.getLevelForXP(client.playerXP[4]);
			}
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[0]) *0.22) + 5);
			client.playerLevel[0] += abc;
			if (client.playerLevel[0] > client.getLevelForXP(client.playerXP[0]) +abc) {
			client.playerLevel[0] = abc + client.getLevelForXP(client.playerXP[0]);
			}
			abc = ((int)Math.floor(client.getLevelForXP(client.playerXP[2]) *0.22) +5);
			
			
			client.playerLevel[2] += abc;
			if (client.playerLevel[2] > client.getLevelForXP(client.playerXP[2]) +abc) {
			client.playerLevel[2] = abc + client.getLevelForXP(client.playerXP[2]);
			}
			abc = (int)(Math.floor(client.getLevelForXP(client.playerXP[1]) *0.22)+5);
			
			
			client.playerLevel[1] += abc;
			if (client.playerLevel[1] > client.getLevelForXP(client.playerXP[1]) +abc) {
			client.playerLevel[1] = abc + client.getLevelForXP(client.playerXP[1]);
			}
			client.getActionAssistant().replaceItem(15335,229, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the overload potion");
			client.getActionAssistant().refreshSkill(6);
			client.getActionAssistant().refreshSkill(1);
			client.getActionAssistant().refreshSkill(0);
			client.getActionAssistant().refreshSkill(2);
			client.getActionAssistant().refreshSkill(4);
			client.achievementProgress[3] += 1;
			if (client.achievementProgress[3] == 1000) {
					client.getNRAchievements().checkMisc(client,17);
			}
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
				if (!client.overLoad) {
				client.getActionAssistant().overLoadEvent(client);
			}
			EventManager.getSingleton().addEvent(client, "c 15308",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;
			case 6685: //Sara Brew
			if(!client.canEat){return;}
				if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}	
				if(client.duelRule[6]){
					client.getActionAssistant().sendMessage("Food has been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				// client.foodDelay = System.currentTimeMillis();
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
				client.getActionAssistant().Send("You drink a dose of Sara brew.");
				client.saraDrink++;
				if(client.saraDrink >= 101){
					client.getNRAchievements().checkMisc(client,2);
				}
				if(client.playerLevel[4] <= 0){
					client.playerLevel[4] = 0;
				} else {
					//client.playerLevel[4] = client.getLevelForXP(client.playerXP[4]);
					client.playerLevel[4] -= 10;
					if(client.playerLevel[4] <= 0){
						client.playerLevel[4] = 0;
					}
				}
				if(client.playerLevel[6] <= 0){
					client.playerLevel[6] = 0;
				} else {
					//client.playerLevel[6] = client.getLevelForXP(client.playerXP[6]);
					client.playerLevel[6] -= 10;
					if(client.playerLevel[6] <= 0){
						client.playerLevel[6] = 0;
					}
				}
				if(client.playerLevel[2] <= 0){
					client.playerLevel[2] = 0;
				} else {
					//client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]);
					client.playerLevel[2] -= 10;
					if(client.playerLevel[2] <= 0){
						client.playerLevel[2] = 0;
					}
				}
				client.getActionAssistant().boostSkill(client, 1);//def
				if(client.playerLevel[0] <= 0){
					client.playerLevel[0] = 0;
				} else {
					//client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
					client.playerLevel[0] -= 10;
					if(client.playerLevel[0] <= 0){
						client.playerLevel[0] = 0;
					}
				}
				//client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
				//client.playerLevel[0] -= 15;
				//client.getActionAssistant().deleteItem(6685, itemSlot, 1);
				//client.getActionAssistant().addItem(6687, 1);		
				client.getActionAssistant().replaceItem(6685, 6687, itemSlot, 1);				
				if(client.playerLevel[3] + 15 >= 115 + client.getActionAssistant().getMaxHP()) {
					client.playerLevel[3] = 115 + client.getActionAssistant().getMaxHP();
				}
else if (client.playerLevel[3] + 15 >= (client.getLevelForXP(client.playerXP[3]) +15 + client.getActionAssistant().getMaxHP())) {
				client.playerLevel[3] = client.getLevelForXP(client.playerXP[3]) + 15 + client.getActionAssistant().getMaxHP();
				}	
				else {
					client.playerLevel[3] += 15;
				}
				if(client.sounds == 1) {
					client.getActionAssistant().frame174(334, 050, 000);
				}
					client.getActionAssistant().refreshSkill(0);
					client.getActionAssistant().refreshSkill(1);
					client.getActionAssistant().refreshSkill(2);
					client.getActionAssistant().refreshSkill(3);
					client.getActionAssistant().refreshSkill(4);
					client.getActionAssistant().refreshSkill(6);
					client.updateRequired = true;
					client.appearanceUpdateRequired = true;
				
			EventManager.getSingleton().addEvent(client,"c 6685", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {

					client.setpBusy(false);client.canEat = true;
					client.canEat = true;

				}

			}, 900);
			break;
			case 6687: //sara brew
			if(!client.canEat){return;}
			
				if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}	
				if(client.duelRule[6]){
					client.getActionAssistant().sendMessage("Food has been disabled in this duel!");
					return;
				}				
				if (client.checkpBusy()) {
					return;
				}
				// client.foodDelay = System.currentTimeMillis();
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
				client.saraDrink++;
				if(client.saraDrink >= 101){
					client.getNRAchievements().checkMisc(client,2);
				}
				client.getActionAssistant().Send("You drink a dose of Sara brew.");
				if(client.playerLevel[4] <= 0){
					client.playerLevel[4] = 0;
				} else {
					//client.playerLevel[4] = client.getLevelForXP(client.playerXP[4]);
					client.playerLevel[4] -= 15;
					if(client.playerLevel[4] <= 0){
						client.playerLevel[4] = 0;
					}
				}
				if(client.playerLevel[6] <= 0){
					client.playerLevel[6] = 0;
				} else {
					//client.playerLevel[6] = client.getLevelForXP(client.playerXP[6]);
					client.playerLevel[6] -= 15;
					if(client.playerLevel[6] <= 0){
						client.playerLevel[6] = 0;
					}
				}
				if(client.playerLevel[2] <= 0){
					client.playerLevel[2] = 0;
				} else {
					//client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]);
					client.playerLevel[2] -= 15;
					if(client.playerLevel[2] <= 0){
						client.playerLevel[2] = 0;
					}
				}
				client.getActionAssistant().boostSkill(client, 1);//def

				if(client.playerLevel[0] <= 0){
					client.playerLevel[0] = 0;
				} else {
					//client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
					client.playerLevel[0] -= 15;
					if(client.playerLevel[0] <= 0){
						client.playerLevel[0] = 0;
					}
				}
				//client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
				//client.playerLevel[0] -= 15;
				//client.getActionAssistant().deleteItem(6687, itemSlot, 1);
				//client.getActionAssistant().addItem(6689, 1);	
				client.getActionAssistant().replaceItem(6687, 6689, itemSlot, 1);
				if(client.playerLevel[3] + 15 >= 115 + client.getActionAssistant().getMaxHP()) {
					client.playerLevel[3] = 115 + client.getActionAssistant().getMaxHP();
				}
else if (client.playerLevel[3] + 15 >= (client.getLevelForXP(client.playerXP[3]) +15 + client.getActionAssistant().getMaxHP())) {
				client.playerLevel[3] = client.getLevelForXP(client.playerXP[3]) + 15 + client.getActionAssistant().getMaxHP();
				}	
				else {
					client.playerLevel[3] += 15;
				}
				if(client.sounds == 1) {
					client.getActionAssistant().frame174(334, 050, 000);
				}
					client.getActionAssistant().refreshSkill(0);
					client.getActionAssistant().refreshSkill(1);
					client.getActionAssistant().refreshSkill(2);
					client.getActionAssistant().refreshSkill(3);
					client.getActionAssistant().refreshSkill(4);
					client.getActionAssistant().refreshSkill(6);
					client.updateRequired = true;
					client.appearanceUpdateRequired = true;
				
			EventManager.getSingleton().addEvent(client, "c 6687",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {

					client.setpBusy(false);client.canEat = true;
					client.canEat = true;

				}

			}, 900);
			break;
			case 6689:
			if(!client.canEat){return;}
				if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if(client.duelRule[6]){
					client.getActionAssistant().sendMessage("Food has been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				// client.foodDelay = System.currentTimeMillis();
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
				client.getActionAssistant().Send("You drink a dose of Sara brew.");
				client.saraDrink++;
				if(client.saraDrink >= 101){
					client.getNRAchievements().checkMisc(client,2);
				}
				if(client.playerLevel[4] <= 0){
					client.playerLevel[4] = 0;
				} else {
					//client.playerLevel[4] = client.getLevelForXP(client.playerXP[4]);
					client.playerLevel[4] -= 15;
					if(client.playerLevel[4] <= 0){
						client.playerLevel[4] = 0;
					}
				}
				if(client.playerLevel[6] <= 0){
					client.playerLevel[6] = 0;
				} else {
					//client.playerLevel[6] = client.getLevelForXP(client.playerXP[6]);
					client.playerLevel[6] -= 15;
					if(client.playerLevel[6] <= 0){
						client.playerLevel[6] = 0;
					}
				}
				if(client.playerLevel[2] <= 0){
					client.playerLevel[2] = 0;
				} else {
					//client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]);
					client.playerLevel[2] -= 15;
					if(client.playerLevel[2] <= 0){
						client.playerLevel[2] = 0;
					}
				}
				client.getActionAssistant().boostSkill(client, 1);//def

				if(client.playerLevel[0] <= 0){
					client.playerLevel[0] = 0;
				} else {
					//client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
					client.playerLevel[0] -= 15;
					if(client.playerLevel[0] <= 0){
						client.playerLevel[0] = 0;
					}
				}
				//client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
				//client.playerLevel[0] -= 15;
				//client.getActionAssistant().deleteItem(6689, itemSlot, 1);
				//client.getActionAssistant().addItem(6691, 1);		
				client.getActionAssistant().replaceItem(6689, 6691, itemSlot, 1);
				if(client.playerLevel[3] + 15 >= 115 + client.getActionAssistant().getMaxHP()) {
					client.playerLevel[3] = 115 + client.getActionAssistant().getMaxHP();
				}
else if (client.playerLevel[3] + 15 >= (client.getLevelForXP(client.playerXP[3]) +15 + client.getActionAssistant().getMaxHP())) {
				client.playerLevel[3] = client.getLevelForXP(client.playerXP[3]) + 15 + client.getActionAssistant().getMaxHP();
				}	
				else {
					client.playerLevel[3] += 15;
				}
				if(client.sounds == 1) {
					client.getActionAssistant().frame174(334, 050, 000);
				}
					client.getActionAssistant().refreshSkill(0);
					client.getActionAssistant().refreshSkill(1);
					client.getActionAssistant().refreshSkill(2);
					client.getActionAssistant().refreshSkill(3);
					client.getActionAssistant().refreshSkill(4);
					client.getActionAssistant().refreshSkill(6);
					client.updateRequired = true;
					client.appearanceUpdateRequired = true;
				
			EventManager.getSingleton().addEvent(client,"c 6689", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {

					client.setpBusy(false);client.canEat = true;
					client.canEat = true;

				}

			}, 900);
			break;
			case 6691:
			if(!client.canEat){return;}
				if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if(client.duelRule[6]){
					client.getActionAssistant().sendMessage("Food has been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
				// client.foodDelay = System.currentTimeMillis();
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
				client.getActionAssistant().Send("You drink a dose of Sara brew.");
				client.saraDrink++;
				if(client.saraDrink >= 101){
					client.getNRAchievements().checkMisc(client,2);
				}
				if(client.playerLevel[4] <= 0){
					client.playerLevel[4] = 0;
				} else {
					//client.playerLevel[4] = client.getLevelForXP(client.playerXP[4]);
					client.playerLevel[4] -= 15;
					if(client.playerLevel[4] <= 0){
						client.playerLevel[4] = 0;
					}
				}
				if(client.playerLevel[6] <= 0){
					client.playerLevel[6] = 0;
				} else {
					//client.playerLevel[6] = client.getLevelForXP(client.playerXP[6]);
					client.playerLevel[6] -= 15;
					if(client.playerLevel[6] <= 0){
						client.playerLevel[6] = 0;
					}
				}
				if(client.playerLevel[2] <= 0){
					client.playerLevel[2] = 0;
				} else {
					//client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]);
					client.playerLevel[2] -= 15;
					if(client.playerLevel[2] <= 0){
						client.playerLevel[2] = 0;
					}
				}
				client.getActionAssistant().boostSkill(client, 1);//def

				if(client.playerLevel[0] <= 0){
					client.playerLevel[0] = 0;
				} else {
					//client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
					client.playerLevel[0] -= 15;
					if(client.playerLevel[0] <= 0){
						client.playerLevel[0] = 0;
					}
				}
				//client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
				//client.playerLevel[0] -= 15;
				//client.getActionAssistant().deleteItem(6691, itemSlot, 1);
				//client.getActionAssistant().addItem(229, 1);				
				client.getActionAssistant().replaceItem(6691, 229, itemSlot, 1);
				if(client.playerLevel[3] + 15 >= 115 + client.getActionAssistant().getMaxHP()) {
					client.playerLevel[3] = 115 + client.getActionAssistant().getMaxHP();
				}
else if (client.playerLevel[3] + 15 >= (client.getLevelForXP(client.playerXP[3]) +15 + client.getActionAssistant().getMaxHP())) {
				client.playerLevel[3] = client.getLevelForXP(client.playerXP[3]) + 15 + client.getActionAssistant().getMaxHP();
				}	
				else {
					client.playerLevel[3] += 15;
				}
				if(client.sounds == 1) {
					client.getActionAssistant().frame174(334, 050, 000);
				}
					client.getActionAssistant().refreshSkill(0);
					client.getActionAssistant().refreshSkill(1);
					client.getActionAssistant().refreshSkill(2);
					client.getActionAssistant().refreshSkill(3);
					client.getActionAssistant().refreshSkill(4);
					client.getActionAssistant().refreshSkill(6);
					client.updateRequired = true;
					client.appearanceUpdateRequired = true;
				
			EventManager.getSingleton().addEvent(client,"c 6691", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {

					client.setpBusy(false);client.canEat = true;
					client.canEat = true;

				}

			}, 900);
			break;
			case 2450:
			if(!client.canEat){return;}
				if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
				client.brews++;
				if(client.brews >= 101){
					client.getNRAchievements().checkMisc(client,3);
				}
				client.getActionAssistant().Send("You drink a dose of Zammy brew.");
				abc = client.getLevelForXP(client.playerXP[4]);
				cba = abc / 10;
				abc2 = cba + 3;
				if (client.playerLevel[4] + abc2 >= client.getLevelForXP(client.playerXP[4]) + abc2) {
				client.playerLevel[4] = client.getLevelForXP(client.playerXP[4]) + abc2;
				}
				else {
				client.playerLevel[4] += abc2;
				}
				abc = client.getLevelForXP(client.playerXP[2]);
				cba = abc / 10;
				abc2 = cba * 2;
				if (abc2 <= 1)
					abc2 = 2;
			if (client.playerLevel[2] + abc2 >= client.getLevelForXP(client.playerXP[2]) + abc2) {
				client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]) + abc2;
				}
				else {
				client.playerLevel[2] += abc2;
				}
				
				abc = client.getLevelForXP(client.playerXP[1]);
				cba = abc / 10;
				abc2 = cba * 2;
				if (abc2 <= 1)
					abc2 = 2;
				if (client.playerLevel[1] + abc2 >= client.getLevelForXP(client.playerXP[1]) + abc2) {
				client.playerLevel[1] = client.getLevelForXP(client.playerXP[1]) + abc2;
				}
				else {
				client.playerLevel[1] += abc2;
				}
			
				abc = client.getLevelForXP(client.playerXP[0]);
				cba = abc / 10;
				abc2 = cba * 2;
				if (abc2 <= 1)
					abc2 = 2;
				if (client.playerLevel[0] + abc2 >= client.getLevelForXP(client.playerXP[0]) + abc2) {
					client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]) + abc2;
				}
				else {
				client.playerLevel[0] += abc2;
				}
//				client.getActionAssistant().deleteItem(2450, itemSlot, 1);
//				client.getActionAssistant().addItem(189, 1);
				client.getActionAssistant().replaceItem(2450, 189, itemSlot, 1);
				client.getActionAssistant().Send("You have 3 doses of potion left.");
				if(client.sounds == 1) {
					client.getActionAssistant().frame174(334, 050, 000);
				}
					client.getActionAssistant().refreshSkill(0);
					client.getActionAssistant().refreshSkill(1);
					client.getActionAssistant().refreshSkill(2);
					client.getActionAssistant().refreshSkill(4);
					client.updateRequired = true;
					client.appearanceUpdateRequired = true;
				
			EventManager.getSingleton().addEvent(client,"c 2450", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {

					client.setpBusy(false);client.canEat = true;
					client.canEat = true;
				}

			}, 900);
				break;
			case 189:
			if(!client.canEat){return;}
				if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.brews++;
				if(client.brews >= 101){
					client.getNRAchievements().checkMisc(client,3);
				}
			client.getActionAssistant().Send("You drink a dose of Zammy brew.");
			abc = client.getLevelForXP(client.playerXP[4]);
			cba = abc / 10;
			abc2 = cba + 3;
			if (client.playerLevel[4] + abc2 >= client.getLevelForXP(client.playerXP[4]) + abc2) {
				client.playerLevel[4] = client.getLevelForXP(client.playerXP[4]) + abc2;
				}
				else {
				client.playerLevel[4] += abc2;
				}
			abc = client.getLevelForXP(client.playerXP[2]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			if (client.playerLevel[2] + abc2 >= client.getLevelForXP(client.playerXP[2]) + abc2) {
				client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]) + abc2;
				}
				else {
				client.playerLevel[2] += abc2;
				}
			abc = client.getLevelForXP(client.playerXP[1]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			if (client.playerLevel[1] + abc2 >= client.getLevelForXP(client.playerXP[1]) + abc2) {
				client.playerLevel[1] = client.getLevelForXP(client.playerXP[1]) + abc2;
				}
				else {
				client.playerLevel[1] += abc2;
				}
			abc = client.getLevelForXP(client.playerXP[0]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			if (client.playerLevel[0] + abc2 >= client.getLevelForXP(client.playerXP[0]) + abc2) {
				client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]) + abc2;
				}
				else {
				client.playerLevel[0] += abc2;
				}
			//client.getActionAssistant().deleteItem(189, itemSlot, 1);
			//client.getActionAssistant().addItem(191, 1);
			client.getActionAssistant().replaceItem(189, 191, itemSlot, 1);
			client.getActionAssistant().Send("You have 2 doses of potion left.");			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}			
					client.getActionAssistant().refreshSkill(0);
					client.getActionAssistant().refreshSkill(1);
					client.getActionAssistant().refreshSkill(2);
					client.getActionAssistant().refreshSkill(4);
					client.updateRequired = true;
					client.appearanceUpdateRequired = true;
			EventManager.getSingleton().addEvent(client,"c 189", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;
					client.canEat = true;
				}

			}, 900);
			
			
			
			break;
			case 191:
			if(!client.canEat){return;}
				if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.brews++;
				if(client.brews >= 101){
					client.getNRAchievements().checkMisc(client,3);
				}
			client.getActionAssistant().Send("You drink a dose of Zammy brew.");
			abc = client.getLevelForXP(client.playerXP[4]);
			cba = abc / 10;
			abc2 = cba + 3;
			if (client.playerLevel[4] + abc2 >= client.getLevelForXP(client.playerXP[4]) + abc2) {
				client.playerLevel[4] = client.getLevelForXP(client.playerXP[4]) + abc2;
				}
				else {
				client.playerLevel[4] += abc2;
				}
			abc = client.getLevelForXP(client.playerXP[2]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			if (client.playerLevel[2] + abc2 >= client.getLevelForXP(client.playerXP[2]) + abc2) {
				client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]) + abc2;
				}
				else {
				client.playerLevel[2] += abc2;
				}
			abc = client.getLevelForXP(client.playerXP[1]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			if (client.playerLevel[1] + abc2 >= client.getLevelForXP(client.playerXP[1]) + abc2) {
				client.playerLevel[1] = client.getLevelForXP(client.playerXP[1]) + abc2;
				}
				else {
				client.playerLevel[1] += abc2;
				}
			abc = client.getLevelForXP(client.playerXP[0]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			if (client.playerLevel[0] + abc2 >= client.getLevelForXP(client.playerXP[0]) + abc2) {
				client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]) + abc2;
				}
				else {
				client.playerLevel[0] += abc2;
				}
			//client.getActionAssistant().deleteItem(191, itemSlot, 1);
			//client.getActionAssistant().addItem(193, 1);
			client.getActionAssistant().replaceItem(191, 193, itemSlot, 1);
			client.getActionAssistant().Send("You have 1 dose of potion left.");
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}			
					client.getActionAssistant().refreshSkill(0);
					client.getActionAssistant().refreshSkill(1);
					client.getActionAssistant().refreshSkill(2);
					client.getActionAssistant().refreshSkill(4);
					client.updateRequired = true;
					client.appearanceUpdateRequired = true;
			EventManager.getSingleton().addEvent(client, "c 191",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;
					client.canEat = true;
				}

			}, 900);
			
			
			
			break;	
			case 193:
			if(!client.canEat){return;}
				if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.brews++;
				if(client.brews >= 101){
					client.getNRAchievements().checkMisc(client,3);
				}
			client.getActionAssistant().Send("You drink a dose of Zammy brew.");
			abc = client.getLevelForXP(client.playerXP[4]);
			cba = abc / 10;
			abc2 = cba + 3;
			if (client.playerLevel[4] + abc2 >= client.getLevelForXP(client.playerXP[4]) + abc2) {
				client.playerLevel[4] = client.getLevelForXP(client.playerXP[4]) + abc2;
				}
				else {
				client.playerLevel[4] += abc2;
				}
			abc = client.getLevelForXP(client.playerXP[2]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
		if (client.playerLevel[2] + abc2 >= client.getLevelForXP(client.playerXP[2]) + abc2) {
				client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]) + abc2;
				}
				else {
				client.playerLevel[2] += abc2;
				}
			abc = client.getLevelForXP(client.playerXP[1]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			if (client.playerLevel[1] + abc2 >= client.getLevelForXP(client.playerXP[1]) + abc2) {
				client.playerLevel[1] = client.getLevelForXP(client.playerXP[1]) + abc2;
				}
				else {
				client.playerLevel[1] += abc2;
				}
			abc = client.getLevelForXP(client.playerXP[0]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
		if (client.playerLevel[0] + abc2 >= client.getLevelForXP(client.playerXP[0]) + abc2) {
				client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]) + abc2;
				}
				else {
				client.playerLevel[0] += abc2;
				}
			//client.getActionAssistant().deleteItem(193, itemSlot, 1);
			//client.getActionAssistant().addItem(229, 1);
			client.getActionAssistant().replaceItem(193, 229, itemSlot, 1);
			client.getActionAssistant().Send("You have finished the potion.");
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}			
					client.getActionAssistant().refreshSkill(0);
					client.getActionAssistant().refreshSkill(1);
					client.getActionAssistant().refreshSkill(2);
					client.getActionAssistant().refreshSkill(4);
					client.updateRequired = true;
					client.appearanceUpdateRequired = true;
			EventManager.getSingleton().addEvent(client,"c 193", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;
					client.canEat = true;
				}

			}, 900);
			
			
			
			break;			
			case 161:
			if(!client.canEat){return;}
				if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.getActionAssistant().Send("You drink a dose of the super strength potion");
			abc = client.getLevelForXP(client.playerXP[2]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]);
			client.playerLevel[2] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[2]+"", 4006);
			//client.getActionAssistant().deleteItem(161, itemSlot, 1);
			//client.getActionAssistant().addItem(229, 1);
			client.getActionAssistant().replaceItem(161, 229, itemSlot, 1);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}			
			client.getActionAssistant().refreshSkill(2);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			EventManager.getSingleton().addEvent(client,"c 161", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
			break;

		case 159:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.getActionAssistant().Send("You drink a dose of the super strength potion");
			abc = client.getLevelForXP(client.playerXP[2]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]);
			client.playerLevel[2] += abc2;
			////client.getActionAssistant().SendString(""+client.playerLevel[2]+"", 4006);
			//client.getActionAssistant().deleteItem(159, itemSlot, 1);
			//client.getActionAssistant().addItem(161, 1);
			client.getActionAssistant().replaceItem(159, 161, itemSlot, 1);
			client.getActionAssistant().refreshSkill(2);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 159", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);			
			break;

		case 157:
		if(!client.canEat){return;}
				if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.getActionAssistant().Send("You drink a dose of the super strength potion");
			abc = client.getLevelForXP(client.playerXP[2]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]);
			client.playerLevel[2] += abc2;
			////client.getActionAssistant().SendString(""+client.playerLevel[2]+"", 4006);
			//client.getActionAssistant().deleteItem(157, itemSlot, 1);
			//client.getActionAssistant().addItem(159, 1);
			client.getActionAssistant().replaceItem(157, 159, itemSlot, 1);
			client.getActionAssistant().refreshSkill(2);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 157", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
			break;

		case 2440:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}	
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.getActionAssistant().Send("You drink a dose of the super strength potion");
			abc = client.getLevelForXP(client.playerXP[2]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]);
			client.playerLevel[2] += abc2;
			////client.getActionAssistant().SendString(""+client.playerLevel[2]+"", 4006);
			//client.getActionAssistant().deleteItem(2440, itemSlot, 1);
			//client.getActionAssistant().addItem(157, 1);
			client.getActionAssistant().replaceItem(2440, 157, itemSlot, 1);
			client.getActionAssistant().refreshSkill(2);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 2440", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
			break;

		case 113:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
				client.getActionAssistant().startAnimation(829);
				client.getActionAssistant().Send("You drink a dose of the strength potion");
				abc = client.getLevelForXP(client.playerXP[2]);
				cba = abc / 10;
				aaa = cba / 2;
				abc2 = aaa + cba;
				if (abc2 <= 1)
					abc2 = 2;
				client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]);
				client.playerLevel[2] += abc2;
				//client.getActionAssistant().deleteItem(113, itemSlot, 1);
				//client.getActionAssistant().addItem(115, 1);
				client.getActionAssistant().replaceItem(113, 115, itemSlot, 1);
				client.getActionAssistant().Send("You have 3 doses of potion left.");
			client.getActionAssistant().refreshSkill(2);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;	
				
			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 113", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
			break;

		case 115:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.getActionAssistant().Send("You drink a dose of the strength potion.");
			abc = client.getLevelForXP(client.playerXP[2]);
			cba = abc / 10;
			aaa = cba / 2;
			abc2 = aaa + cba;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]);
			client.playerLevel[2] += abc2;
			////client.getActionAssistant().SendString(""+client.playerLevel[2]+"", 4006);
			//client.getActionAssistant().deleteItem(115, itemSlot, 1);
			//client.getActionAssistant().addItem(117, 1);
			client.getActionAssistant().replaceItem(115, 117, itemSlot, 1);
			client.getActionAssistant().Send("You have 2 doses of potion left.");
			client.getActionAssistant().refreshSkill(2);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 115",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
			break;

		case 117:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.getActionAssistant().Send("You drink a dose of the strength potion");
			abc = client.getLevelForXP(client.playerXP[2]);
			cba = abc / 10;
			aaa = cba / 2;
			abc2 = aaa + cba;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]);
			client.playerLevel[2] += abc2;
			////client.getActionAssistant().SendString(""+client.playerLevel[2]+"", 4006);
			//client.getActionAssistant().deleteItem(117, itemSlot, 1);
			//client.getActionAssistant().addItem(119, 1);
			client.getActionAssistant().replaceItem(117, 119, itemSlot, 1);
			client.getActionAssistant().Send("You have 1 dose of potion left.");
			client.getActionAssistant().refreshSkill(2);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 117", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
			break;

		case 119:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().Send("You drink a dose of the strength potion.");
			abc = client.getLevelForXP(client.playerXP[2]);
			cba = abc / 10;
			aaa = cba / 2;
			abc2 = aaa + cba;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[2] = client.getLevelForXP(client.playerXP[2]);
			client.playerLevel[2] += abc2;
			////client.getActionAssistant().SendString(""+client.playerLevel[2]+"", 4006);
			//client.getActionAssistant().deleteItem(119, itemSlot, 1);
			//client.getActionAssistant().addItem(229, 1);
			client.getActionAssistant().replaceItem(119, 229, itemSlot, 1);
			client.getActionAssistant().Send("You have finished the potion.");
			client.getActionAssistant().refreshSkill(2);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 119", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);
			break;



		case 3030:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			if (client.nopots) {
				return;
			}	
			client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.getActionAssistant().Send("You drink a dose of the super restore potion.");
			client.playerLevel[5] += 23;
			client.getActionAssistant().refreshSkill(5);
			if (client.playerLevel[5] > client.getLevelForXP(client.playerXP[5])){
				client.playerLevel[5] = client.getLevelForXP(client.playerXP[5]);
				client.getActionAssistant().refreshSkill(5);
			}
			for (int level = 0; level < client.playerLevel.length; level++)  {
				if (client.playerLevel[level] < client.getLevelForXP(client.playerXP[level])) {
					if(level != 3) { // hp doesn't restore
					if(level != 5) {
						client.playerLevel[level] += 45;
						if(client.playerLevel[level] > client.getLevelForXP(client.playerXP[level])) {
							client.playerLevel[level] = client.getLevelForXP(client.playerXP[level]);
						}
						client.getActionAssistant().setSkillLevel(level, client.playerLevel[level], client.playerXP[level]);
						client.getActionAssistant().refreshSkill(level);
					}
					}
				} 
			}			
			//client.getActionAssistant().deleteItem(3030, itemSlot, 1);
			//client.getActionAssistant().addItem(229, 1);
			client.getActionAssistant().replaceItem(3030, 229, itemSlot, 1);
			client.getActionAssistant().Send("You have finished your potion.");
			client.getActionAssistant().refreshSkill(5);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 3030", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 3028:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			if (client.nopots) {
				return;
			}				
			client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.getActionAssistant().Send("You drink a dose of the super restore potion.");
			client.playerLevel[5] += 23;
			client.getActionAssistant().refreshSkill(5);
			if (client.playerLevel[5] > client.getLevelForXP(client.playerXP[5])){
				client.playerLevel[5] = client.getLevelForXP(client.playerXP[5]);
				client.getActionAssistant().refreshSkill(5);
			}
			for (int level = 0; level < client.playerLevel.length; level++)  {
				if (client.playerLevel[level] < client.getLevelForXP(client.playerXP[level])) {
					if(level != 3) { // hp doesn't restore
					if(level != 5) {
						client.playerLevel[level] += 45;
						if(client.playerLevel[level] > client.getLevelForXP(client.playerXP[level])) {
							client.playerLevel[level] = client.getLevelForXP(client.playerXP[level]);
						}
						client.getActionAssistant().setSkillLevel(level, client.playerLevel[level], client.playerXP[level]);
						client.getActionAssistant().refreshSkill(level);
					}
					}
				} 
			}			
			//client.getActionAssistant().deleteItem(3028, itemSlot, 1);
			//client.getActionAssistant().addItem(3030, 1);
			client.getActionAssistant().replaceItem(3028, 3030, itemSlot, 1);
			client.getActionAssistant().Send("You have 1 dose of potion left.");
			client.getActionAssistant().refreshSkill(5);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 3028", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 3026:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			if (client.nopots) {
				return;
			}				
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.getActionAssistant().Send("You drink a dose of the super restore potion.");
			client.playerLevel[5] += 23;
			client.getActionAssistant().refreshSkill(5);
			if (client.playerLevel[5] > client.getLevelForXP(client.playerXP[5])){
				client.playerLevel[5] = client.getLevelForXP(client.playerXP[5]);
				client.getActionAssistant().refreshSkill(5);
			}
			for (int level = 0; level < client.playerLevel.length; level++)  {
				if (client.playerLevel[level] < client.getLevelForXP(client.playerXP[level])) {
					if(level != 3) { // hp doesn't restore
					if(level != 5) {
						client.playerLevel[level] += 45;
						if(client.playerLevel[level] > client.getLevelForXP(client.playerXP[level])) {
							client.playerLevel[level] = client.getLevelForXP(client.playerXP[level]);
						}
						client.getActionAssistant().setSkillLevel(level, client.playerLevel[level], client.playerXP[level]);
						client.getActionAssistant().refreshSkill(level);
					}
					}
				} 
			}
			//client.getActionAssistant().deleteItem(3026, itemSlot, 1);
			//client.getActionAssistant().addItem(3028, 1);
			client.getActionAssistant().replaceItem(3026, 3028, itemSlot, 1);
			client.getActionAssistant().Send("You have 2 doses of potion left.");
			client.getActionAssistant().refreshSkill(5);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 3026", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 3024:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
			if (client.checkpBusy()) {
				return;
			}
			if (client.nopots) {
				return;
			}	
			client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.getActionAssistant().Send("You drink a dose of the super restore potion.");
			client.playerLevel[5] += 23;
			client.getActionAssistant().refreshSkill(5);
			if (client.playerLevel[5] > client.getLevelForXP(client.playerXP[5])){
				client.playerLevel[5] = client.getLevelForXP(client.playerXP[5]);
				client.getActionAssistant().refreshSkill(5);
			}
			for (int level = 0; level < client.playerLevel.length; level++)  {
				if (client.playerLevel[level] < client.getLevelForXP(client.playerXP[level])) {
					if(level != 3) { // hp doesn't restore
					if(level != 5) {
						client.playerLevel[level] += 45;
						if(client.playerLevel[level] > client.getLevelForXP(client.playerXP[level])) {
							client.playerLevel[level] = client.getLevelForXP(client.playerXP[level]);
						}
						client.getActionAssistant().setSkillLevel(level, client.playerLevel[level], client.playerXP[level]);
						client.getActionAssistant().refreshSkill(level);
					}
					}
				} 
			}
			//client.getActionAssistant().deleteItem(3024, itemSlot, 1);
			//client.getActionAssistant().addItem(3026, 1);
			client.getActionAssistant().replaceItem(3024, 3026, itemSlot, 1);
			client.getActionAssistant().Send("You have 3 doses of potion left.");
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}
			EventManager.getSingleton().addEvent(client,"c 3024", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 143:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			if (client.nopots) {
				return;
			}				
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.playerLevel[5] += 19;
			if (client.playerLevel[5] > client.getLevelForXP(client.playerXP[5]))
				client.playerLevel[5] = client.getLevelForXP(client.playerXP[5]);
			//client.getActionAssistant().deleteItem(143, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the prayer potion.");
			client.getActionAssistant().Send("You have finished your potion.");
			//client.getActionAssistant().addItem(229, 1);
			client.getActionAssistant().replaceItem(143, 229, itemSlot, 1);
			client.getActionAssistant().refreshSkill(5);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}
			EventManager.getSingleton().addEvent(client,"c 143", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 141:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			if (client.nopots) {
				return;
			}				
			client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.playerLevel[5] += 19;
			if (client.playerLevel[5] > client.getLevelForXP(client.playerXP[5]))
				client.playerLevel[5] = client.getLevelForXP(client.playerXP[5]);
			//client.getActionAssistant().deleteItem(141, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the prayer potion.");
			client.getActionAssistant().Send("You have 1 dose of potion left.");
			//client.getActionAssistant().addItem(143, 1);
			client.getActionAssistant().replaceItem(141, 143, itemSlot, 1);
			client.getActionAssistant().refreshSkill(5);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}
			EventManager.getSingleton().addEvent(client,"c 141", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 139:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			if (client.nopots) {
				return;
			}				
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.playerLevel[5] += 19;
			if (client.playerLevel[5] > client.getLevelForXP(client.playerXP[5]))
				client.playerLevel[5] = client.getLevelForXP(client.playerXP[5]);
			//client.getActionAssistant().deleteItem(139, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the prayer potion.");
			client.getActionAssistant().Send("You have 2 doses of potion left.");
			//client.getActionAssistant().addItem(141, 1);
			client.getActionAssistant().replaceItem(139, 141, itemSlot, 1);
			client.getActionAssistant().refreshSkill(5);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}
			EventManager.getSingleton().addEvent(client, "c 139",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 2434:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			if (client.nopots) {
				return;
			}				
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.playerLevel[5] += 19;
			if (client.playerLevel[5] > client.getLevelForXP(client.playerXP[5]))
				client.playerLevel[5] = client.getLevelForXP(client.playerXP[5]);
			//client.getActionAssistant().deleteItem(2434, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the prayer potion.");
			client.getActionAssistant().Send("You have 3 doses of potion left.");
			//client.getActionAssistant().addItem(139, 1);
			client.getActionAssistant().replaceItem(2434, 139, itemSlot, 1);
			client.getActionAssistant().refreshSkill(5);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 2434", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 167:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[1]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[1] = client.getLevelForXP(client.playerXP[1]);
			client.playerLevel[1] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[1]+"", 4008);
			//client.getActionAssistant().deleteItem(167, itemSlot, 1);
			//client.getActionAssistant().addItem(229, 1);
			client.getActionAssistant().replaceItem(167, 229, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the super defence potion");
			client.getActionAssistant().refreshSkill(1);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}
			EventManager.getSingleton().addEvent(client,"c 167", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 165:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[1]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[1] = client.getLevelForXP(client.playerXP[1]);
			client.playerLevel[1] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[1]+"", 4008);
			//client.getActionAssistant().deleteItem(165, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the super defence potion");
			//client.getActionAssistant().addItem(167, 1);
			client.getActionAssistant().replaceItem(165, 167, itemSlot, 1);
			client.getActionAssistant().refreshSkill(1);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 165", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 163:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[1]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[1] = client.getLevelForXP(client.playerXP[1]);
			client.playerLevel[1] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[1]+"", 4008);
			//client.getActionAssistant().deleteItem(163, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the super defence potion");
			//client.getActionAssistant().addItem(165, 1);
			client.getActionAssistant().replaceItem(163, 165, itemSlot, 1);
			client.getActionAssistant().refreshSkill(1);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
EventManager.getSingleton().addEvent(client,"c 163", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 2442:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[1]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[1] = client.getLevelForXP(client.playerXP[1]);
			client.playerLevel[1] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[1]+"", 4008);
			//client.getActionAssistant().deleteItem(2442, itemSlot, 1);
			//client.getActionAssistant().addItem(163, 1);
			client.getActionAssistant().replaceItem(2442, 163, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the super defence potion");
			client.getActionAssistant().refreshSkill(1);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 2442", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 137:
		if(!client.canEat){return;}
			if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[1]);
			cba = abc / 10;
			aaa = cba / 2;
			abc2 = aaa + cba;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[1] = client.getLevelForXP(client.playerXP[1]);
			client.playerLevel[1] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[1]+"", 4008);
			//client.getActionAssistant().deleteItem(137, itemSlot, 1);
			//client.getActionAssistant().addItem(229, 1);
			client.getActionAssistant().replaceItem(137, 229, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the defence potion");
			client.getActionAssistant().refreshSkill(1);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
EventManager.getSingleton().addEvent(client,"c 137", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 135:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[1]);
			cba = abc / 10;
			aaa = cba / 2;
			abc2 = aaa + cba;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[1] = client.getLevelForXP(client.playerXP[1]);
			client.playerLevel[1] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[1]+"", 4008);
			//client.getActionAssistant().deleteItem(135, itemSlot, 1);
			//client.getActionAssistant().addItem(137, 1);
			client.getActionAssistant().replaceItem(135, 137, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the defence potion");
			client.getActionAssistant().refreshSkill(1);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
EventManager.getSingleton().addEvent(client,"c 135", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 133:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[1]);
			cba = abc / 10;
			aaa = cba / 2;
			abc2 = aaa + cba;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[1] = client.getLevelForXP(client.playerXP[1]);
			client.playerLevel[1] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[1]+"", 4008);
			//client.getActionAssistant().deleteItem(133, itemSlot, 1);
			//client.getActionAssistant().addItem(135, 1);
			client.getActionAssistant().replaceItem(133, 135, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the defence potion");
			client.getActionAssistant().refreshSkill(1);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
EventManager.getSingleton().addEvent(client,"c 133", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 2432:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[1]);
			cba = abc / 10;
			aaa = cba / 2;
			abc2 = aaa + cba;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[1] = client.getLevelForXP(client.playerXP[1]);
			client.playerLevel[1] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[1]+"", 4008);
			//client.getActionAssistant().deleteItem(2432, itemSlot, 1);
			//client.getActionAssistant().addItem(133, 1);
			client.getActionAssistant().replaceItem(2432, 133, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the defence potion");
			client.getActionAssistant().refreshSkill(1);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}
EventManager.getSingleton().addEvent(client,"c 2432", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 3046:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.playerLevel[6] = client.getLevelForXP(client.playerXP[6]);
			client.playerLevel[6] += 4;
			//client.getActionAssistant().SendString(""+client.playerLevel[6]+"", 4014);
			//client.getActionAssistant().deleteItem(3046, itemSlot, 1);
			//client.getActionAssistant().addItem(229, 1);
			client.getActionAssistant().replaceItem(3046, 229, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the magic potion");
			client.getActionAssistant().refreshSkill(6);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
EventManager.getSingleton().addEvent(client,"c 3046", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 3044:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.playerLevel[6] = client.getLevelForXP(client.playerXP[6]);
			client.playerLevel[6] += 4;
			//client.getActionAssistant().SendString(""+client.playerLevel[6]+"", 4014);
			//client.getActionAssistant().deleteItem(3044, itemSlot, 1);
			//client.getActionAssistant().addItem(3046, 1);
			client.getActionAssistant().replaceItem(3044, 3046, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the magic potion");
			
			client.getActionAssistant().refreshSkill(6);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 3044",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 3042:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.playerLevel[6] = client.getLevelForXP(client.playerXP[6]);
			client.playerLevel[6] += 4;
			//client.getActionAssistant().SendString(""+client.playerLevel[6]+"", 4014);
			//client.getActionAssistant().deleteItem(3042, itemSlot, 1);
			//client.getActionAssistant().addItem(3044, 1);
			client.getActionAssistant().replaceItem(3042, 3044, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the magic potion");
			client.getActionAssistant().refreshSkill(6);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
EventManager.getSingleton().addEvent(client, "c 3042", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 3040:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			client.playerLevel[6] = client.getLevelForXP(client.playerXP[6]);
			client.playerLevel[6] += 4;
			//client.getActionAssistant().SendString(""+client.playerLevel[6]+"", 4014);
			//client.getActionAssistant().deleteItem(3040, itemSlot, 1);
			//client.getActionAssistant().addItem(3042, 1);
			client.getActionAssistant().replaceItem(3040, 3042, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the magic potion");
			client.getActionAssistant().refreshSkill(6);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 3040", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 173:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[4]);
			cba = abc / 10;
			abc2 = cba + 3;
			client.playerLevel[4] = client.getLevelForXP(client.playerXP[4]);
			client.playerLevel[4] += abc2;
			//client.getActionAssistant().deleteItem(173, itemSlot, 1);
			//client.getActionAssistant().addItem(229, 1);
			client.getActionAssistant().replaceItem(173, 229, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the range potion.");
			client.getActionAssistant().refreshSkill(4);
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}
EventManager.getSingleton().addEvent(client,"c 173", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 171:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[4]);
			cba = abc / 10;
			abc2 = cba + 3;
			client.playerLevel[4] = client.getLevelForXP(client.playerXP[4]);
			client.playerLevel[4] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[4]+"", 4010);
			//client.getActionAssistant().deleteItem(171, itemSlot, 1);
			//client.getActionAssistant().addItem(173, 1);
			client.getActionAssistant().replaceItem(171, 173, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the range potion");
			client.getActionAssistant().refreshSkill(4);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 171", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 169:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[4]);
			cba = abc / 10;
			abc2 = cba + 3;
			client.playerLevel[4] = client.getLevelForXP(client.playerXP[4]);
			client.playerLevel[4] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[4]+"", 4010);
			//client.getActionAssistant().deleteItem(169, itemSlot, 1);
			//client.getActionAssistant().addItem(171, 1);
			client.getActionAssistant().replaceItem(169, 171, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the range potion");
			client.getActionAssistant().refreshSkill(4);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
EventManager.getSingleton().addEvent(client,"c 169", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 2444:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[4]);
			cba = abc / 10;
			abc2 = cba + 3;
			client.playerLevel[4] = client.getLevelForXP(client.playerXP[4]);
			client.playerLevel[4] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[4]+"", 4010);
			//client.getActionAssistant().deleteItem(2444, itemSlot, 1);
			//client.getActionAssistant().addItem(169, 1);
			client.getActionAssistant().replaceItem(2444, 169, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the range potion");
			client.getActionAssistant().refreshSkill(4);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}
EventManager.getSingleton().addEvent(client,"c 2444", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 149:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[0]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
			client.playerLevel[0] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[0]+"", 4004);
			//client.getActionAssistant().deleteItem(149, itemSlot, 1);
			//client.getActionAssistant().addItem(229, 1);
			client.getActionAssistant().replaceItem(149, 229, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the super attack potion");
			client.getActionAssistant().refreshSkill(0);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 149",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 147:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[0]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
			client.playerLevel[0] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[0]+"", 4004);
			//client.getActionAssistant().deleteItem(147, itemSlot, 1);
			//client.getActionAssistant().addItem(149, 1);
			client.getActionAssistant().replaceItem(147, 149, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the super attack potion");
			client.getActionAssistant().refreshSkill(0);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
EventManager.getSingleton().addEvent(client,"c 147", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 145:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[0]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
			client.playerLevel[0] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[0]+"", 4004);
			//client.getActionAssistant().deleteItem(145, itemSlot, 1);
			//client.getActionAssistant().addItem(147, 1);
			client.getActionAssistant().replaceItem(145, 147, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the super attack potion");
			client.getActionAssistant().refreshSkill(0);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c 145",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 2436:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[0]);
			cba = abc / 10;
			abc2 = cba * 2;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
			client.playerLevel[0] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[0]+"", 4004);
			//client.getActionAssistant().deleteItem(2436, itemSlot, 1);
			//client.getActionAssistant().addItem(145, 1);
			client.getActionAssistant().replaceItem(2436, 145, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the super attack potion");
			client.getActionAssistant().refreshSkill(0);
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client, "c2436",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 125:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[0]);
			cba = abc / 10;
			aaa = cba / 2;
			abc2 = aaa + cba;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
			client.playerLevel[0] += abc2;
			//client.getActionAssistant().SendString(""+client.playerLevel[0]+"", 4004);
			//client.getActionAssistant().deleteItem(125, itemSlot, 1);
			//client.getActionAssistant().addItem(229, 1);
			client.getActionAssistant().replaceItem(125, 229, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the attack potion");
			client.getActionAssistant().refreshSkill(0);
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
EventManager.getSingleton().addEvent(client,"c 125", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 123:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[0]);
			cba = abc / 10;
			aaa = cba / 2;
			abc2 = aaa + cba;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
			client.playerLevel[0] += abc2;
			//client.getActionAssistant().deleteItem(123, itemSlot, 1);
			//client.getActionAssistant().addItem(125, 1);
			client.getActionAssistant().replaceItem(123, 125, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the attack potion");
			client.getActionAssistant().refreshSkill(0);
			
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}	
			EventManager.getSingleton().addEvent(client,"c 123", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);	
			break;

		case 121:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[0]);
			cba = abc / 10;
			aaa = cba / 2;
			abc2 = aaa + cba;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
			client.playerLevel[0] += abc2;
			//client.getActionAssistant().deleteItem(121, itemSlot, 1);
			//client.getActionAssistant().addItem(123, 1);
			client.getActionAssistant().replaceItem(121, 123, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the attack potion");
			client.getActionAssistant().refreshSkill(0);
			if(client.sounds == 1) {
			client.getActionAssistant().frame174(334, 050, 000);
			}
			EventManager.getSingleton().addEvent(client,"c 121", new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;

		case 2428:
		if(!client.canEat){return;}
		if(client.duelRule[5]){
					client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
					return;
				}
				if (client.checkpBusy()) {
					return;
				}
			
				client.setpBusy(true);
			client.getActionAssistant().startAnimation(829);
			abc = client.getLevelForXP(client.playerXP[0]);
			cba = abc / 10;
			aaa = cba / 2;
			abc2 = aaa + cba;
			if (abc2 <= 1)
				abc2 = 2;
			client.playerLevel[0] = client.getLevelForXP(client.playerXP[0]);
			client.playerLevel[0] += abc2;
			//client.getActionAssistant().deleteItem(2428, itemSlot, 1);
			//client.getActionAssistant().addItem(121, 1);
			client.getActionAssistant().replaceItem(2428, 121, itemSlot, 1);
			client.getActionAssistant().Send("You drink a dose of the attack potion");
			client.getActionAssistant().refreshSkill(0);
			
			if(client.sounds == 1) {
				client.getActionAssistant().frame174(334, 050, 000);
			}
			EventManager.getSingleton().addEvent(client, "c 2428",new Event() {

			@Override
				public void execute(EventContainer c) {
				
					c.stop();
				}

				@Override
				public void stop() {
					
					client.getActionAssistant().requestUpdates();
					client.setpBusy(false);client.canEat = true;

				}

			}, 900);				
			break;
			
		}
		//PlayerManager.getSingleton().saveGame(client);
	}
	
}
