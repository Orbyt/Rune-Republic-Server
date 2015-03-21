package com.rs.worldserver.model.player;

import org.nikkii.alertify4j.Alertify;
import org.nikkii.alertify4j.AlertifyBuilder;
import org.nikkii.alertify4j.AlertifyType;

import com.rs.worldserver.Config;
import com.rs.worldserver.Constants;
import com.rs.worldserver.Server;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;

/**
 * Handles the friends/ignores/pm system
 *
 * @author Graham
 */
public class FriendsAssistant {

    private Client client;

    private static int lastChatId = 0;

    public FriendsAssistant(Client client) {
        this.client = client;
        //test++;
    }

    public boolean containsFriend(long l) {
        for (int i = 0; i < client.getFriends().length; i++) {
            if (client.getFriends()[i] == l) {
                return true;
            }
        }
        return false;
    }

    public boolean containsIgnore(long l) {
        for (int i = 0; i < client.getIgnores().length; i++) {
            if (client.getIgnores()[i] == l) {
                return true;
            }
        }
        return false;
    }

    public void updateFlist() {
        if (!Server.WorldFriendsReady) return;
        for (int i = 0; i < client.getFriends().length; i++) {
            if (client.getFriends()[i] <= 0) //if there is no friend at this location, skip to next friend (i)
                continue;
            Integer world = (Integer) Server.playerSQLnames.get(Misc.longToPlayerName(client.getFriends()[i]).replace('_', ' '));
            if (world != null) {
                client.getFriendsAssistant().sendFriend(client.getFriends()[i], world + 1);
                continue;
            }
            Player p = Server.getPlayerManager().getPlayerByName(Misc.longToPlayerName(client.getFriends()[i]).replace('_', ' '));
            if (p != null) {
                if (client.getFriends()[i] == Misc.playerNameToInt64(p.playerName) && p.isActive) {
                    continue;
                }
            }
            //System.out.println("Removed: " + Misc.longToPlayerName(client.getFriends()[i]));
            client.getFriendsAssistant().sendFriend(client.getFriends()[i], 0);
        }
    }

    public void initialize() {
        if (Server.Friend) {
            //if ((System.currentTimeMillis() - Server.FDelay >  2000) || (System.currentTimeMillis()- Server.Loadtime < 1200000)) {
            client.checkFriends = 1;
            Server.FDelay = System.currentTimeMillis();
            sendStatus(2);
            //refreshLists();
            for (int i1 = 0; i1 < Constants.MAXIMUM_PLAYERS; i1++) {
                Player p = Server.getPlayerManager().getPlayers()[i1];
                if (p != null && p.isActive) {
                    Client o = (Client) p;
                    if (o != null) {
                        o.getFriendsAssistant().updatePM(client.playerId, 1);
                    }
                }
            }
            boolean pmLoaded = false;
            for (int i = 0; i < client.getFriends().length; i++) {
                if (client.getFriends()[i] != 0 && client.getFriends()[i] > 0) {
                    try {
                        Player p = Server.getPlayerManager().getPlayerByName(Misc.longToPlayerName(client.getFriends()[i]).replace('_', ' '));
                        if (p != null && p.isActive && Misc.playerNameToInt64(p.playerName) == client.getFriends()[i]) {
                            Client o = (Client) p;
                            if (o != null) {
                                if (p.pmstatus == 0 || (p.pmstatus == 1 && o.getFriendsAssistant().containsFriend(Misc.playerNameToInt64(client.playerName)))) {
                                    sendFriend(client.getFriends()[i], 1);
                                    pmLoaded = true;
                                }
                            }
                        }
                        if (!pmLoaded) {
                            sendFriend(client.getFriends()[i], 0);
                        }
                        pmLoaded = false;
                    } catch (Exception e) {
                        client.getFriendsAssistant().removeFriend(client.getFriends()[i]);
                        continue;
                    }
                }

            }
        }
        //}
    }



/*		if (client.getFriends() == null) {
			client.setFriends(new long[200]);
		}
		if (client.getIgnores() == null) {
			client.setIgnores(new long[100]);
		}
		refreshLists();
		long l = Misc.playerNameToInt64(client.getPlayerName());
		int world = getWorld(l);
		for (Player p : Server.getPlayerManager().getPlayers()) {
			if (p == null)
				continue;
			if (!p.isActive || p.disconnected)
				continue;
			Client c = (Client) p;
			if (c.getFriendsAssistant().containsFriend(l)) {
				if (c.pmstatus == 0 || (c.pmstatus == 1)) {
					c.getFriendsAssistant().sendFriend(l, world);
				}
			}
		}
	}*/

    public void destruct() {
        long l = Misc.playerNameToInt64(client.getPlayerName());
        for (Player p : Server.getPlayerManager().getPlayers()) {
            if (p == null)
                continue;
            if (!p.isActive || p.disconnected)
                continue;
            Client c = (Client) p;
            if (c.getFriendsAssistant().containsFriend(l)) {
                c.getFriendsAssistant().sendFriend(l, 0);
            }
        }
    }

    public void refreshLists() {
        for (int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
            long l = client.getFriends()[i];
            if (l == 0)
                continue;
            sendFriend(l, getWorld(l));
        }
        sendStatus(2);
    }

    public void sendFriend(long l, int world) {
        if (world != 0) {
            world += 9;
        } else if (!Config.WORLD_LIST_FIX) {
            world += 1;
        }
        client.getOutStream().createFrame(50);
        client.getOutStream().writeQWord(l);
        client.getOutStream().writeByte(world);
        client.flushOutStream();
    }

    public void sendStatus(int status) {
        client.getOutStream().createFrame(221);
        client.getOutStream().writeByte(status);
        client.flushOutStream();
    }

    public int getWorld(long who) {
        Player p = Server.getPlayerManager().getPlayerByNameLong(who);
        int world = 0;
        if (p != null) {
            world = 1;
        }
        return world;
    }

    public void addFriend(long l) {

        if (getAmountOf(client.getFriends()) >= 199) {
            client.getNRAchievements().checkMisc(client, 14);
        }
        if (getAmountOf(client.getFriends()) > client.getFriendsSize()) {
            // TODO is there a message? and is this correct?
            client.getActionAssistant().sendMessage(
                    "Your friends list is full.");
            return;
        } else {
            if (containsFriend(l)) {
                client.getActionAssistant().sendMessage(
                        Misc.longToPlayerName(l)
                                + " is already in your friendlist.");
                return;
            }
            int slot = getFreeSlot(client.getFriends());
            if (slot == -1)
                return;
            client.getFriends()[slot] = l;
            sendFriend(l, getWorld(l));
        }
    }

    public void removeFriend(long l) {
        for (int i = 0; i < client.getFriends().length; i++) {
            if (client.getFriends()[i] == l) {
                client.getFriends()[i] = 0;
                //client.getFriendsAssistant().sendFriend(l, 0);
            }
        }
    }

    public void addIgnore(long l) {

        if (getAmountOf(client.getIgnores()) >= 199) {
            client.getNRAchievements().checkMisc(client, 15);
        }
        if (getAmountOf(client.getIgnores()) > client.getIgnoresSize()) {
            // TODO is there a message? and is this correct?
            client.getActionAssistant().sendMessage(
                    "Your ignores list is full.");

            return;
        } else {
            int slot = getFreeSlot(client.getIgnores());
            if (slot == -1)
                return;
            client.getIgnores()[slot] = l;
        }
    }

    public void removeIgnore(long l) {
        for (int i = 0; i < client.getIgnores().length; i++) {
            if (client.getIgnores()[i] == l) {
                client.getIgnores()[i] = 0;
            }
        }
    }

    public int getAmountOf(long[] array) {
        int count = 0;
        for (long l : array) {
            if (l > 0) {
                count++;
            }
        }
        return count;
    }

    public int getFreeSlot(long[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    public void sendMessage(long to, byte[] chatText, int chatTextSize) {
        Player p = Server.getPlayerManager().getPlayerByNameLong(to);
        Client c = (Client) p;
        Integer friendWorld = (Integer) Server.playerSQLnames.get(Misc.longToPlayerName(to).replace('_', ' '));
        //String s = new String(chatText);
        if (friendWorld != null) {

        } else {
            if (c == null || c.pmstatus == 2) {
                client.getActionAssistant().sendMessage(Misc.longToPlayerName(to) + " is currently unavailable.");
                return;
            } else {
                c.getFriendsAssistant().sendPM(
                        Misc.playerNameToInt64(client.getPlayerName()),
                        client.playerRights, chatText, chatTextSize);
            }
        }
    }

    public void sendNewPM(long from, String message) {
        client.getOutStream().createFrameVarSize(31);
        client.getOutStream().writeString(message);
        client.getOutStream().writeQWord(from);
        client.getOutStream().endFrameVarSize();
    }

    public void sendPM(long from, int playerRights, byte[] chatText,
                       int chatTextSize) {
        if (lastChatId == 0) {
            lastChatId++;
        }
        String msg = client.decodeChat(chatText, chatTextSize);
        if (msg.toLowerCase().contains("pkfast") && client.combatLevel > 10) {
            return;
        }
        if (msg.toLowerCase().contains("fire-pk") && client.combatLevel > 10) {
            return;
        }
        if (msg.toLowerCase().contains("k.com") && client.combatLevel > 10) {
            return;
        }
        if (msg.toLowerCase().contains("t.com") && client.combatLevel > 10) {
            return;
        }
        client.getOutStream().createFrameVarSize(196);
        client.getOutStream().writeQWord(from);
        client.getOutStream().writeDWord(++lastChatId);
        client.getOutStream().writeByte(playerRights);
        client.getOutStream().writeBytes(chatText, chatTextSize, 0);

        client.getOutStream().endFrameVarSize();
        client.flushOutStream();
       
    }

    public void updatePM(int pID, int world) { // used for private chat updates
        Player p = PlayerManager.getSingleton().getPlayers()[pID];
        if (p == null || p.playerName == null || p.playerName.equals("null")) {
            return;
        }
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[pID];
        if (o == null) {
            return;
        }
        long l = Misc.playerNameToInt64(PlayerManager.getSingleton().getPlayers()[pID].playerName);

        if (o.pmstatus == 0) {
            for (int i = 0; i < client.getFriends().length; i++) {
                if (client.getFriends()[i] > 0) {
                    if (l == client.getFriends()[i]) {
                        client.getFriendsAssistant().sendFriend(l, world);
                        return;
                    }
                }
            }
        }
        if (o.pmstatus == 1) {
            for (int i = 0; i < client.getFriends().length; i++) {
                if (client.getFriends()[i] > 0) {
                    if (l == client.getFriends()[i]) {
                        if (o.getFriendsAssistant().containsFriend(Misc.playerNameToInt64(client.playerName))) {
                            client.getFriendsAssistant().sendFriend(l, world);
                            return;
                        } else {
                            client.getFriendsAssistant().sendFriend(l, 0);
                            return;
                        }
                    }
                }
            }
        }
        if (o.pmstatus == 2) {
            for (int i = 0; i < client.getFriends().length; i++) {
                if (client.getFriends()[i] > 0) {
                    if (l == client.getFriends()[i]) {
                        client.getFriendsAssistant().sendFriend(l, 0);
                        return;
                    }
                }
            }
        }
    }
}
