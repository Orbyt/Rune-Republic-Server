package com.rs.worldserver.model;

import com.rs.worldserver.model.player.Client;

/**
 * Represents an in-game trade.
 * 
 * @author Graham
 * 
 */
public class Trade {
	
	private Client establisher;
	private Client receiver;
	private boolean open;
	
	public Trade(Client establisher, Client receiver) {
		this.establisher = establisher;
		this.receiver = receiver;
		this.open = false;
	}
	
	public Client getEstablisher() {
		return this.establisher;
	}
	
	public Client getReceiver() {
		return this.receiver;
	}
	
	public boolean isOpen() {
		return this.open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}

}
