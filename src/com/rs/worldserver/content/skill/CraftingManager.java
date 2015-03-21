package com.rs.worldserver.content.skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rs.worldserver.Server;
import com.rs.worldserver.content.Skill;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.ActionAssistant;
import com.rs.worldserver.model.FloorItem;
import com.rs.worldserver.model.object.GameObject;
import com.rs.worldserver.world.ObjectManager;



public class CraftingManager implements Skill {

	public CraftingManager(){
	}


	public static void startLeather(final Client c, final int hides, final int hideId, final int itemId, final int level, final int xp, final int thread, final int emote, final int amount){
		if(c.isCrafting){
			return;
		}
		if (c.checkBusy()) {
			return;
		}		
		if(c.playerLevel[SkillConstants.CRAFTING] < level){
			c.getActionAssistant().sendMessage("Your crafting level is not high enough to make this.");
			return;
		}
		if(c.getActionAssistant().getItemAmount(1734) < thread){
			c.getActionAssistant().sendMessage("You don't have enough thread to craft with.");
			return;
		}
		if(!c.getActionAssistant().playerHasItem(1733,1)){
			c.getActionAssistant().sendMessage("You don't have a needle to craft with.");
			return;
		}
		c.getActionAssistant().removeAllWindows();
		c.isCrafting = true;
		c.inCombat = true;
		c.getActionAssistant().startAnimation(emote);
		c.craftAmount = amount;
		c.setBusy(true);
		c.getActionAssistant().sendMessage("You start crafting your supplies...");
		EventManager.getSingleton().addEvent(c,"crafting1", new Event() {
			@Override
			public void execute(EventContainer cont) {
				if(!c.isCrafting){
					cont.stop();
					return;
				}
				c.getActionAssistant().startAnimation(emote);				
				c.getActionAssistant().deleteItem(1734, c.getActionAssistant().getItemSlot(1734), thread);
				for(int i = 0; i < hides; i++)

					c.getActionAssistant().deleteItem(hideId, c.getActionAssistant().getItemSlot(hideId), 1);
					c.getActionAssistant().addItem(itemId, 1);
					c.getActionAssistant().sendMessage("You turn your leather into a piece of armor.");
					c.getActionAssistant().addSkillXP(xp, SkillConstants.CRAFTING);
					c.getActionAssistant().refreshSkill(SkillConstants.CRAFTING);
				
				if(!c.getActionAssistant().playerHasItem(1734,1)){
					c.getActionAssistant().sendMessage("You ran out of thread.");
					cont.stop();
				}			
				if(!c.getActionAssistant().playerHasItem(hideId,1)){
					cont.stop();
				}
				if(c.craftAmount <= 1){
					cont.stop();
					return;
				} else c.craftAmount--;
			}
			@Override
			public void stop(){
				c.getActionAssistant().startAnimation(65535);
				c.isCrafting = false;
				c.craftAmount = -1;
				c.setBusy(false);
			}
		}, 1500);
	}

}