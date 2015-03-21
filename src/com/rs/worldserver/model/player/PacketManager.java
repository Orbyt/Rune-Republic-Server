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

import java.util.HashMap;
import java.util.Map;

import com.rs.worldserver.model.player.packet.ActionButtons;
import com.rs.worldserver.model.player.packet.AtObject;
import com.rs.worldserver.model.player.packet.AnnounceToClan;
import com.rs.worldserver.model.player.packet.Attack;
import com.rs.worldserver.model.player.packet.BankAll;
import com.rs.worldserver.model.player.packet.BankFive;
import com.rs.worldserver.model.player.packet.BankTen;
import com.rs.worldserver.model.player.packet.BankX;
import com.rs.worldserver.model.player.packet.BuryBones;
import com.rs.worldserver.model.player.packet.ChallengePlayer;
import com.rs.worldserver.model.player.packet.CharacterDesign;
import com.rs.worldserver.model.player.packet.Chat;
import com.rs.worldserver.model.player.packet.ClickNPC;
import com.rs.worldserver.model.player.packet.Clicking;
import com.rs.worldserver.model.player.packet.ClientGUIActions;
import com.rs.worldserver.model.player.packet.CustomCommand;
import com.rs.worldserver.model.player.packet.Dialogue;
import com.rs.worldserver.model.player.packet.DropItem;
import com.rs.worldserver.model.player.packet.IdleLogout;
import com.rs.worldserver.model.player.packet.LoadingComplete;
import com.rs.worldserver.model.player.packet.MoveItems;
import com.rs.worldserver.model.player.packet.NullPacket;
import com.rs.worldserver.model.player.packet.PickupItem;
import com.rs.worldserver.model.player.packet.RemoveItem;
import com.rs.worldserver.model.player.packet.RunWalkAction;
import com.rs.worldserver.model.player.packet.TradeAction;
import com.rs.worldserver.model.player.packet.StringInput;
import com.rs.worldserver.model.player.packet.Walking;
import com.rs.worldserver.model.player.packet.WearItem;
import com.rs.worldserver.model.player.packet.ClickItem;
import com.rs.worldserver.model.player.packet.MagicOnItems;
import com.rs.worldserver.model.player.packet.FollowPlayer;
import com.rs.worldserver.model.player.packet.ItemOnItem;
import com.rs.worldserver.model.player.packet.ItemOnObject;
import com.rs.worldserver.model.player.packet.ItemOnNpc;
import com.rs.worldserver.model.player.packet.ItemOnPlayer;
import com.rs.worldserver.model.player.packet.RubItem;
import com.rs.worldserver.model.player.packet.ClanJoin;
import com.rs.worldserver.model.player.packet.ReportPlayer;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.model.player.*;

/**
 * Packet manager.
 * 
 * @author Graham
 * 
 */
public class PacketManager {

	private static Map<Integer, Packet> packetMap = new HashMap<Integer, Packet>();
	static {
		NullPacket np = new NullPacket();
		packetMap.put(0, np);
		packetMap.put(226, np);
		packetMap.put(78, np);
		//packetMap.put(148, np);
		packetMap.put(183, np);
		packetMap.put(230, np);
		packetMap.put(136, np);
		packetMap.put(189, np);
		packetMap.put(200, np);
		packetMap.put(85, np);
		packetMap.put(165, np);
		packetMap.put(238, np);
		packetMap.put(150, np);
		packetMap.put(36, np);
		packetMap.put(246, np);
		packetMap.put(77, np);
		packetMap.put(16, new AnnounceToClan());
		//packetMap.put(57, np);
		packetMap.put(228, np);
		packetMap.put(234, np);
		packetMap.put(181, np);
		packetMap.put(218, new ReportPlayer());
		packetMap.put(60, new ClanJoin());
		packetMap.put(202, new IdleLogout());
		packetMap.put(101, new CharacterDesign());
		LoadingComplete lc = new LoadingComplete();
		packetMap.put(121, lc);
		packetMap.put(210, lc);
		packetMap.put(122, new ClickItem());
		Walking w = new Walking();
		packetMap.put(248, w);
		packetMap.put(164, w);
		packetMap.put(98, w);
		//packetMap.put(175, w);
		Chat c = new Chat();
		packetMap.put(4, c);
		packetMap.put(95, c);
		packetMap.put(188, c);
		packetMap.put(215, c);
		packetMap.put(133, c);
		packetMap.put(74, c);
		packetMap.put(126, c);
		AtObject ao = new AtObject();
		packetMap.put(132, ao);
		packetMap.put(252, ao);
		packetMap.put(70, ao);
		packetMap.put(236, new PickupItem());
		TradeAction ta = new TradeAction();
		//packetMap.put(73, ta);
		packetMap.put(139, ta);
		ClientGUIActions cga = new ClientGUIActions();
		packetMap.put(3, cga);
		packetMap.put(86, cga);
		packetMap.put(241, cga);
		packetMap.put(103, new CustomCommand());
		packetMap.put(214, new MoveItems());
		packetMap.put(41, new WearItem());
		packetMap.put(145, new RemoveItem());
		packetMap.put(117, new BankFive());
		packetMap.put(43, new BankTen());
		packetMap.put(129, new BankAll());
		BankX bx = new BankX();
		packetMap.put(135, bx);
		packetMap.put(208, bx);
		packetMap.put(87, new DropItem());
		ActionButtons ab = new ActionButtons();
		packetMap.put(185, ab);
		packetMap.put(130, new Clicking());
		ClickNPC cn = new ClickNPC();
		packetMap.put(155, cn);
		packetMap.put(17, cn);
		packetMap.put(21, cn);
		Dialogue d = new Dialogue();
		packetMap.put(40, d);
		RunWalkAction rw = new RunWalkAction();
		packetMap.put(153, rw);
		packetMap.put(152, rw);
		packetMap.put(14, new ItemOnPlayer());
		packetMap.put(53, new ItemOnItem());
		packetMap.put(192, new ItemOnObject());
		packetMap.put(57, new ItemOnNpc());
		packetMap.put(75, new RubItem());
		Attack atk = new Attack();
		packetMap.put(73, atk);
		packetMap.put(72, atk);
		packetMap.put(249, atk);	
		packetMap.put(131, atk);		
		packetMap.put(237, new MagicOnItems());
		packetMap.put(39, new FollowPlayer());
		packetMap.put(128, new ChallengePlayer());
		packetMap.put(124, new StringInput());
		System.out.println("Loaded " + packetMap.size() + " packets.");
	}

	public static void handlePacket(Client client, int type, int size) {
		if(type == 124){
					String playerCommand = client.getInStream().readString();
					System.out.println("Type:" + type + " size " +  size);
			}
		if (type == -1)
			return;
		
		if(type!=0) { // npc spam clicking fix
			if(client.lastPack==98 || client.lastPack==164 || client.lastPack==248) {
				if(!client.follower) {
					if(client.isFighting || client.inCombat) {
						if (type!=72 && type!=98 && type!=0 && type!=241 && type!=73 && type!=249 && type!=131)
							client.getCombat().resetAttack();
					}
				}
			}
		}
		client.lastPack=type;
		Packet p = packetMap.get(type);
		if (p != null) {
			try {
				if (type == 101) {
					if (size != 13) {
						System.err.println(client.playerName+": NEW Error while handling packet type=" + type
										+ ", size=" + size + "]: ");	
						return;				
					}
				}			
				if (type == 36) {
					if (client.getInStream().readDWord() == 5) {
						client.getFollowing().resetFollowing();
					}
				}
if (type == 4 && (size == 129 || size == 130)) {
System.err.println(client.playerName+": NEW Error while handling packet type=" + type
						+ ", size=" + size + "]: ");
return;
}
				p.handlePacket(client, type, size);
			} catch (Exception e) {
				client.packetError += 1;
				if (client.packetError >= 5) {
					//	client.kick();
						//client.disconnected=true;
				}
				System.err.println(client.playerName+": Error while handling packet type=" + type
						+ ", size=" + size + "]: " + String.valueOf(e.getMessage()));
				e.printStackTrace();
			}
		} else {
			System.out.println(client.playerName+": Unhandled packet [type=" + type + ", size="
					+ size + "]");
		}
	}

}
