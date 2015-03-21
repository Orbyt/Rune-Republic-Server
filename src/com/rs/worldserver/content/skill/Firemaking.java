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
import com.rs.worldserver.model.player.CastleWars;
import com.rs.worldserver.Config;
/**
 * Firemaking
 * 
 * @author Chachi
 * 
 */
public class Firemaking implements Skill {

	public static final int fireShow = 60000;
	 
	public static void fire(final Client player, int lvl, final int log, final int xpGain) {
		if (player.inDuelArena()) {
			player.getActionAssistant().Send("You can not fire make in Duel Arena.");
			return;
		}
		if(Config.CastleWars && Server.getCastleWars().isInCw(player)){
			return;
		}
		if (player.absX >= 2800 && player.absX <= 2950 && player.absY >= 5245 && player.absY <= 5385){
			player.getActionAssistant().sendMessage("@red@Firemaking in godwars? WAT?!?!?!?");
			return;
		}
		if (player.inEdg()) {
		player.getActionAssistant().sendMessage("@red@Firemaking removed from edgeville");
		return;
		}
		if (player.inWild()) {
		player.getActionAssistant().sendMessage("@red@Firemaking removed from edgeville");
		return;
		}
		if(player.blackMarks > 0) {
			player.getActionAssistant().sendMessage("Naw Idiot.");
			return;
		}
		if(player.playerLevel[11] < lvl) {
  			player.getActionAssistant()
					.sendMessage("You need a firemaking level of "+lvl+" light this log.");
			return;
 		}
		if (player.checkBusy()) {
				return;
		}
		player.setBusy(true);
		player.getActionAssistant().startAnimation(733);
		player.fires++;
		if(player.fires > 4999){
			player.getNRAchievements().checkSkilling(player,7);
		}
		EventManager.getSingleton().addEvent(player,"firemaking", new Event() {
			@Override
			public void execute(EventContainer c) {
				final int objectX = player.getX();
				final int objectY = player.getY();
				player.makeGlobalObject(player.getX(), player.getY(), 3038, -1, 10);
				player.getActionAssistant().sendMessage("You light the logs.");
				player.getActionAssistant().addSkillXP(xpGain, 11);
				player.getActionAssistant().deleteItem(log, 1);	
				player.getActionAssistant().refreshSkill(11);
				EventManager.getSingleton().addEvent(null, "Null firemaking", new Event() {

					@Override
					public void execute(EventContainer cont) {						
						cont.stop();
					}

					@Override
					public void stop() {
						player.ReplaceObject2(objectX, objectY, 6951, 0, 10);
						FloorItem item = new FloorItem(592, 1, player, objectX, objectY, player.getHeightLevel());
						Server.getItemManager().newDrop(item, null);
						Server.getItemManager().showDrop(item);						
					}

						},Misc.random(fireShow));				
				c.stop();

			}

			@Override
			public void stop() {
				int objectX = player.getX();
				int objectY = player.getY();
				player.getActionAssistant().startAnimation(2552);
				player.setBusy(false);
				//player.deletethatwall(objectX, objectY);
				//player.getActionAssistant().walkTo(-1, 0);
				//player.getActionAssistant().turnTo(objectX, objectY);
				

			}

		}, 2100);
	}
}
