package com.rs.worldserver.model.player;

import com.rs.worldserver.*;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.*;
import com.rs.worldserver.model.player.RegionManager.Region.Tile;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.object.GameObject;

public class WebEvent {

	public static void startCut(final Client player,final int x, final int y, final int h){
		final GameObject web = Server.getObjectManager().getObjectAt(x, y, h);
		final GameObject cutWeb = new GameObject(x,y, h, web.getFace(), 10,
		Server.getObjectManager().getDefinition(734));
		//player.removeObject(x,y,734,web.getFaceID(),10);
		Server.getObjectManager().replaceObject(web,cutWeb);
		Region.delClipping(x, y, h);
		player.getActionAssistant().sendMessage("You slash the web.");
		player.getActionAssistant().requestUpdates();
	
	}
	
}

