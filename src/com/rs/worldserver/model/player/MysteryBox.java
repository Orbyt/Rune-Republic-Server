package com.rs.worldserver.model.player;

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.*;
import com.rs.worldserver.Server;

/**
 * Mystery box handler
 * 
 * @author Orbit
 * 
 */

public class MysteryBox {

	public static boolean Canusebox = true;
	
	public static int Common [] = {
	
	555, 556, 4085, 1921, 2015, 7758, 4675,
	555, 556, 4085, 1921, 2015, 7758, 4675,
	
	
	}; // Add more item Id's
	
	public static int Uncommon [] = {
	
	4151, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 4151, 11732,
	4151, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 4151, 11732,
	4151, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 4151, 11732,
	4151, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 4151, 11732,
	4151, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 4151, 11732, 
	4151, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 4151, 11732, 
	4151, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 4151, 11732, 
	4151, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 4151, 11732, 
	4151, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 4151, 11732, 
	4151, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 4151, 11732, 
	4151, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 4151, 11732, 
	7668, 7671, 3054, 3057, 3058, 3059, 3060, 3061, 2460, 2461, 4037, 4039, 4081, 4079, 7597, 7598, 7599, 7600, 7601, 7602, 7603, 7604, 7605, 7606, 7607, 7608, 
	
	}; // Add more item Id's
	
	public static int Rare [] = {
	
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581, 11718, 11720, 11722, 11726, 11724, 2577, 2581,
	7901, 6562, 13655, 22406, 13665, 13668, 13667, 13662, 7901, 7901, 13667, 13662
	
	}; // Add more item Id's

	public static int GenerateMyrsteryPrize(final Client c) {
		EventManager.getSingleton().addEvent(c,"MysteryBox", new Event() {
			int BoxTimer = 2;
			int Coins = 50000 + Misc.random(200000);
			public void execute(EventContainer Box) {
				Canusebox = false;
				//if (BoxTimer == 2) {
			//		c.getActionAssistant().sendMessage("Calculating prize...");
			//	}
				if (BoxTimer == 0) {
					c.getActionAssistant().addItem(995, Coins);
					int Random = Misc.random(100);
					if (Random <= 79) {
						int low = Common[(int) (Math.random() * Common.length)];
						c.getActionAssistant().addItem(low, 1);
						c.getActionAssistant().sendMessage("You have recieved a @gre@common @bla@item and @blu@"+ Coins +" @bla@coins.");
						//PlayerManager.getSingleton().sendGlobalMessage("[@red@Mystery Box@bla@]@mag@ " + c.playerName + " has won " + Server.getItemManager().getItemDefinition(Common[(int) (Math.random() * Common.length)]).getName() + " from the Mystery Box!");
					} else if (Random >= 80 && Random <= 98) {
						int middle = Uncommon[(int) (Math.random() * Uncommon.length)];
						c.getActionAssistant().addItem(middle, 1);
						c.getActionAssistant().sendMessage("You have recieved an @yel@uncommon @bla@item and @blu@"+ Coins +" @bla@coins.");
						PlayerManager.getSingleton().sendGlobalMessage("[@red@Mystery Box@bla@]@mag@ " + c.playerName + " has obtained @dre@" + Server.getItemManager().getItemDefinition(middle).getName() + " @mag@from the Mystery Box!");
					} else if (Random >= 99 && Random <= 100) {
						int high = Rare[(int) (Math.random() * Rare.length)];
						c.getActionAssistant().addItem(high, 1);
						c.getActionAssistant().sendMessage("You have recieved a @red@rare @bla@item and @blu@"+ Coins +" @bla@coins.");
						PlayerManager.getSingleton().sendGlobalMessage("[@red@Mystery Box@bla@]@mag@ " + c.playerName + " has obtained @dre@" + Server.getItemManager().getItemDefinition(high).getName() + " @mag@from the Mystery Box!");
					}
				}
				if (c == null || BoxTimer <= 0) {
				   	Box.stop();
					Canusebox = true;
                    return; 
				}
				if (BoxTimer >= 0) {
					BoxTimer--;
				}
			}
			
		/*@Override
			public void execute(EventContainer c) {
					
				c.stop();
			}*/

			@Override
			public void stop() {
						
						
				c.setpBusy(false);

			}
		}, 1000);
		return Common[(int) (Math.random() * Common.length)];
	}
	
	public static void OpenMysteryBox(Client c) {
			if (c.getActionAssistant().freeSlots() > 1) {
				if (Canusebox == true) {
					c.getActionAssistant().deleteItem(18768, 1);
					GenerateMyrsteryPrize(c);
				} else {
					c.getActionAssistant().sendMessage("Please wait while your current process is finished.");
				}
			} else {
				c.getActionAssistant().sendMessage("You need at least 2 slots to open the Mystery box.");
			}
	}
	
}