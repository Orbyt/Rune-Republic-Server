package com.rs.worldserver.model.player;

import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;
public class DegradeCheck {
	
	private Client 		client;
	
	public DegradeCheck(Client c) {
		this.client = c;
		//startCheck();
	} //


	private void startCheck() {
			EventManager.getSingleton().addEvent(client,"degrade", new Event() {
			@Override
			public void execute(EventContainer c) {
				if (client.disconnected || client == null) {
					c.stop();
					return;
				}

		/*if (client.getActionAssistant().playerHasItem(13901, 1) == true) {
			if (client.vlsdmg == 0) {
				client.getActionAssistant().deleteItem(13901, 1);
				client.getActionAssistant().Send("Your vls degrades into dust!");
				client.vlsdmg = 30000;
			}
		}
		else if (client.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 13901) {
		if (client.vlsdmg == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_WEAPON] = -1;
				client.getActionAssistant().Send("Your vls degrades into dust!");
				client.vlsdmg = 30000;
			}
		}
		else if (client.getActionAssistant().playerHasItem(13907, 1) == true) {
			if (client.vsdmg == 0) {
				client.getActionAssistant().deleteItem(13907, 1);
				client.getActionAssistant().Send("Your Vesta Spear degrades into dust!");
				client.vsdmg = 30000;
			}
		}
		else if (client.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 13907) {
		if (client.vsdmg == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_WEAPON] = -1;
				client.getActionAssistant().Send("Your Vesta Spear degrades into dust!");
				client.vsdmg = 30000;
			}
		}
		else if (client.getActionAssistant().playerHasItem(13904, 1) == true) {
			if (client.statdmg == 0) {
				client.getActionAssistant().deleteItem(13904, 1);
				client.getActionAssistant().Send("Your Statius Hammer degrades into dust!");
				client.statdmg = 30000;
			}
		}
		else if (client.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 13904) {
		if (client.statdmg == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_WEAPON] = -1;
				client.getActionAssistant().Send("Your Statius Hammer degrades into dust!");
				client.statdmg = 30000;
			}
		}
		else if (client.getActionAssistant().playerHasItem(13898, 1) == true) {
			if (client.stathelm == 0) {
				client.getActionAssistant().deleteItem(13898, 1);
				client.getActionAssistant().Send("Your Statius Helmet degrades into dust!");
				client.stathelm = 40000;
			}
		}			
		else if (client.playerEquipment[PlayerConstants.PLAYER_HAT] == 13898) {
			if (client.stathelm == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_HAT] = -1;
				client.getActionAssistant().Send("Your Statius Helmet degrades into dust!");
				client.stathelm = 40000;
			}
		}
		else if (client.getActionAssistant().playerHasItem(13866, 1) == true) {
			if (client.zurhelm == 0) {
				client.getActionAssistant().deleteItem(13866, 1);
				client.getActionAssistant().Send("Your Zuriel's Helmet degrades into dust!");
				client.zurhelm = 40000;
			}
		}
		else if (client.playerEquipment[PlayerConstants.PLAYER_HAT] == 13866) {
			if (client.zurhelm == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_HAT] = -1;
				client.getActionAssistant().Send("Your Zuriel's Helmet degrades into dust!");
				client.zurhelm = 40000;
			}
		}
		else if (client.getActionAssistant().playerHasItem(13878, 1) == true) {
			if (client.morrhelm == 0) {
				client.getActionAssistant().deleteItem(13878, 1);
				client.getActionAssistant().Send("Your Morrigan's Helmet degrades into dust!");
				client.morrhelm = 40000;
			}
		}
		else if (client.playerEquipment[PlayerConstants.PLAYER_HAT] == 13878) {
			if (client.morrhelm == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_HAT] = -1;
				client.getActionAssistant().Send("Your Morrigan's Helmet degrades into dust!");
				client.morrhelm = 40000;
			}
		}
		else if (client.getActionAssistant().playerHasItem(13889, 1) == true) {
			if (client.vplate == 0) {
				client.getActionAssistant().deleteItem(13889, 1);
				client.getActionAssistant().Send("Your Vesta's Chainbody degrades into dust!");
				client.vplate = 40000;
			}
		}
		else if (client.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13889) {
			if (client.vplate == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_CHEST] = -1;
				client.getActionAssistant().Send("Your Vesta's Chainbody degrades into dust!");
				client.vplate = 40000;
			}
		}
		else if (client.getActionAssistant().playerHasItem(13860, 1) == true) {
			if (client.zplate == 0) {
				client.getActionAssistant().deleteItem(13860, 1);
				client.getActionAssistant().Send("Your Zuriel's Robetop degrades into dust!");
				client.zplate = 40000;
			}
		}
		else if (client.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13860) {
			if (client.zplate == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_CHEST] = -1;
				client.getActionAssistant().Send("Your Zuriel's Robetop degrades into dust!");
				client.zplate = 40000;
			}
		}
		else if (client.getActionAssistant().playerHasItem(13872, 1) == true) {
			if (client.mplate == 0) {
				client.getActionAssistant().deleteItem(13872, 1);
				client.getActionAssistant().Send("Your Morrigan's Top degrades into dust!");
				client.mplate = 40000;
			}
		}
		else if (client.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13872) {
			if (client.mplate == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_CHEST] = -1;
				client.getActionAssistant().Send("Your Morrigan's Top degrades into dust!");
				client.mplate = 40000;
			}
		}
		else if (client.getActionAssistant().playerHasItem(13886, 1) == true) {
			if (client.splate == 0) {
				client.getActionAssistant().deleteItem(13886, 1);
				client.getActionAssistant().Send("Your Statius Plate degrades into dust!");
				client.splate = 40000;
			}
		}
		else if (client.playerEquipment[PlayerConstants.PLAYER_CHEST] == 13886) {
			if (client.splate == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_CHEST] = -1;
				client.getActionAssistant().Send("Your Statius Plate degrades into dust!");
				client.splate = 40000;
			}
		}
		else if (client.getActionAssistant().playerHasItem(13895, 1) == true) {
			if (client.vleg == 0) {
				client.getActionAssistant().deleteItem(13895, 1);
				client.getActionAssistant().Send("Your Vesta's Plateskirt degrades into dust!");
				client.vleg = 40000;
				
			}
		}
		else if (client.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13895) {
			if (client.vleg == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_LEGS] = -1;
				client.getActionAssistant().Send("Your Vesta's Plateskirt degrades into dust!");
				client.vleg = 40000;
			}
		}
		else if (client.getActionAssistant().playerHasItem(13875, 1) == true) {
			if (client.mleg == 0) {
				client.getActionAssistant().deleteItem(13875, 1);
				client.getActionAssistant().Send("Your Morrigan's Chaps degrades into dust!");
				client.mleg = 40000;
			}
		}
		else if (client.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13875) {
			if (client.mleg == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_LEGS] = -1;
				client.getActionAssistant().Send("Your Morrigan's Chaps degrades into dust!");
				client.mleg = 40000;
				
			}
		}
		else if (client.getActionAssistant().playerHasItem(13863, 1) == true) {
			if (client.zleg == 0) {
				client.getActionAssistant().deleteItem(13863, 1);
				client.getActionAssistant().Send("Your Zuriel's Robe degrades into dust!");
				client.zleg = 40000;
			}
		}
		else if (client.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13863) {
			if (client.zleg == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_LEGS] = -1;
				client.getActionAssistant().Send("Your Zuriel's Robe degrades into dust!");
				client.zleg = 40000;
				
			}
		}
		else if (client.getActionAssistant().playerHasItem(13892, 1) == true) {
			if (client.sleg == 0) {
				client.getActionAssistant().deleteItem(13892, 1);
				client.getActionAssistant().Send("Your Statius Platelegs degrade into dust!");
				client.sleg = 40000;
			}
		}
		else if (client.playerEquipment[PlayerConstants.PLAYER_LEGS] == 13892) {
			if (client.sleg == 0) {
				client.playerEquipment[PlayerConstants.PLAYER_LEGS] = -1;
				client.getActionAssistant().Send("Your Statius Platelegs degrade into dust!");
				client.sleg = 40000;
				
			}
		}*/
			}

			@Override
			public void stop() {
			}
			}, 10000);
	}

}
