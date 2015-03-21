package com.rs.worldserver.io;

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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.rs.worldserver.Constants;
import com.rs.worldserver.Server;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.model.player.Player;

/**
 * Handles all client IO through the selector.
 * 
 * @author Graham
 * 
 */
public class IOThread implements Runnable {

	private ServerSocketChannel serverChannel;
	private Selector selector;

	private Map<SocketChannel, IOClient> ioClientMap;
	private Map<IOClient, SocketChannel> revIoClientMap;
	private Map<SocketChannel, Client> clientMap;
	private Map<Client, SocketChannel> revClientMap;

	private ByteBuffer buffer;

	public static final int BUFFER_SIZE = 4096;
	public static final int BACKLOG = 100;

	private boolean toggleShutdown = false;
	private volatile boolean isShutdown = false;

	public IOThread() throws IOException {
		buffer = ByteBuffer.allocate(BUFFER_SIZE);
		ioClientMap = new HashMap<SocketChannel, IOClient>();
		clientMap = new HashMap<SocketChannel, Client>();
		revIoClientMap = new HashMap<IOClient, SocketChannel>();
		revClientMap = new HashMap<Client, SocketChannel>();
		selector = SelectorProvider.provider().openSelector();
		serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		InetSocketAddress iAddress = new InetSocketAddress/* ("69.197.58.81" */(
				"127.0.0.1", Constants.PORT);
		serverChannel.socket().bind(iAddress, BACKLOG);
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("Ready and listening on port " + Constants.PORT
				+ ".");
	}

	@Override
	public void run() {
		while (true) {
			try {
				selector.select();
				if (toggleShutdown) {
					isShutdown = true;
					break;
				}
				Iterator<SelectionKey> selectedIterator = selector
						.selectedKeys().iterator();
				while (selectedIterator.hasNext()) {
					SelectionKey selectionKey = (SelectionKey) selectedIterator
							.next();
					selectedIterator.remove();
					synchronized (this) {
						if (selectionKey.isValid()) {
							if (selectionKey.isAcceptable()) {
								accept(selectionKey);
							} else if (selectionKey.isReadable()) {
								read(selectionKey);
							}
						}
					}
				}
			} catch (Exception e) {
			}
		}
		System.out.println("Selector thread shut down.");
		if (EventManager.getSingleton().isShutdown()) {
			System.exit(0);
		}
	}

	private void read(SelectionKey selectionKey) throws IOException {

		SocketChannel sc = (SocketChannel) selectionKey.channel();
		buffer.clear();
		int readStatus;

		String address = null;

		IOClient ioc = ioClientMap.get(sc);
		if (ioc != null) {
			address = ioc.connectedFrom.toString();
		}

		Client c = clientMap.get(sc);
		if (c != null) {
			address = c.connectedFrom;
		}

		try {
			try {
				readStatus = sc.read(buffer);
			} catch (IOException e) {
				destroySocket(sc, address, true);
				return;
			}
			if (readStatus == -1) {
				destroySocket(sc, address, true);
				return;
			}
		} catch (Exception e) {
		}

		ioc = ioClientMap.get(sc);
		if (ioc != null) {
			ioc.read(buffer);
			return;
		}

		c = clientMap.get(sc);
		if (c != null) {
			synchronized (Server.gameLogicLock) {
				c.read(buffer);
			}
		}
	}

	private void accept(SelectionKey selectionKey) throws IOException,
			Exception {
		ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
		SocketChannel sc = ssc.accept();
		String address = sc.socket().getInetAddress().getHostAddress();
		synchronized (IOHostList.class) {

			/*
			 * if(address.contains("90.219.") || address.contains("94.194.") ||
			 * address.contains("84.26.") || address.contains("71.41.") ||
			 * address.contains("24.188.") || address.contains("219.90.") ||
			 * address.contains("84.52.") || address.contains("93.136.")
			 * 
			 * ) { destroySocket(sc, address, false); }
			 */
			if (!IOHostList.has(address, Constants.MAX_CONNECTIONS_PER_IP)) {
				Misc.println_debug("Accepted connection from " + address + ".");
				IOClient ioc = PlayerManager.getSingleton().newPlayerClient(
						sc.socket(), address);
				if (ioc == null) {
					destroySocket(sc, address, false);
				} else {
					IOHostList.add(address);
					ioClientMap.put(sc, ioc);
					revIoClientMap.put(ioc, sc);
					sc.socket().setTcpNoDelay(true);
					sc.configureBlocking(false);
					sc.register(selector, SelectionKey.OP_READ);
				}
			} else {
				Misc.println_debug("Rejected connection from " + address + ".");
				destroySocket(sc, address, false);
			}
		}
	}

	public void destroySocket(SocketChannel sc, String connectedFrom,
			boolean removeFromHostList) throws IOException, Exception {
		if (removeFromHostList) {
			synchronized (IOHostList.class) {
				IOHostList.remove(connectedFrom);
			}
		}
		if (ioClientMap.containsKey(sc)) {
			IOClient ioc = ioClientMap.get(sc);
			Server.getPlayerManager().removeIOClient(ioc);
			ioClientMap.remove(sc);
			revIoClientMap.remove(ioc);
		}
		if (clientMap.containsKey(sc)) {
			Client c = clientMap.get(sc);
			c.setLoggedOut(true);
			clientMap.remove(sc);
			revClientMap.remove(c);
		}
		sc.close();
		Misc.println_debug("Closed connection from " + connectedFrom + ".");
	}

	public void switchIoClientToClient(IOClient ioc, Client c) {
		synchronized (this) {
			SocketChannel sc = revIoClientMap.get(ioc);
			revIoClientMap.remove(ioc);
			ioClientMap.remove(sc);
			clientMap.put(sc, c);
			revClientMap.put(c, sc);
			System.out.println("Registered " + c.getPlayerName() + " [idx="
					+ c.getPlayerName() + ", online="
					+ PlayerManager.getSingleton().getPlayerCount() + "]");
			c.appendConnected();
		}
	}

	public SocketChannel socketFor(IOClient ioc) {
		return revIoClientMap.get(ioc);
	}

	public SocketChannel socketFor(Client c) {
		return revClientMap.get(c);
	}

	public void writeReq(SocketChannel sc, ByteBuffer buf) throws IOException {
		sc.write(buf);
	}

	public void shutdown() {
		toggleShutdown = true;
		selector.wakeup();
	}

	public boolean isShutdown() {
		return this.isShutdown;
	}

}