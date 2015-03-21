package com.rs.worldserver.world;

import com.rs.worldserver.*;
import com.rs.worldserver.util.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;

/**
 *  Public Event command
 *  
 * @author Patrick and some other people i guess
 *
 */
public class PublicEvent implements Command {
	private String randomChar[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "x", "1", "2", "3", "$"};
	private String commandString = "";
	private boolean isRunning;
	
	
	/** Public Event command **/
	@Override
	public void execute(Client client, String command) {
		/** We are running already **/
		if(isRunning)
			return; 
			startEvent();
	}
	
	public String getString(){
		return commandString;
	}
	
	/*
	* Lets make this a singleton instance
	*/
	private static PublicEvent instance; 
	
	/** our singleton instance **/
	public static PublicEvent getInstance(){
		if(instance == null)
			return new PublicEvent();
		
		return instance;
	}
	
	/**
	 * Generate a random String
	 * 
	 * @return
	 */
	private String getRandomString() {
		String returns = "";
		while(returns.length() < 10)
			returns += randomChar[Misc.random(randomChar.length - 1)];
		return returns;
	}
	
	/** Start our event **/
	private String startEvent(PublicEventReward reward) {
		int rewardId = reward.getID();
		int rewardAmount = reward.getAmount();
		isRunning = true;
		CommandManager.commandMap.put(commandString, new PublicEventCommand(reward));
		return "[@yel@Trivia@bla@]The first person to type ::"+commandString+" will receive "+Server.getItemManager().getItemDefinition(rewardId).getName()+" x "+rewardAmount;
	}

	private void startEvent(){
		EventManager.getSingleton().addEvent(null,"doncheck", new Event() {
			@Override
			public void execute(EventContainer c) {
				if(isRunning)
					return; 
				/** Do checks to check who can use this command **/
				
				/** Generate our string **/
				commandString = getRandomString();
				
				/** Get a prize **/
				PublicEventReward reward = generateReward();
				
				/** start the event **/
				PlayerManager.getSingleton().sendGlobalMessage(startEvent(reward));
			}

			@Override
			public void stop() {
			
			}
			}, 3600000);
	}
	/** Generate a reward **/
	private PublicEventReward generateReward() {
		int rewardLevel = Misc.random(50);
		int rewardId = -1;
		int rewardAmount = 0;
		if(rewardLevel < 40) {
			rewardLevel = 1;
		} else if(rewardLevel < 49) {
			rewardLevel = 2;
		} else if(rewardLevel == 50) {
			rewardLevel = 3;
		} else {
			rewardLevel = 1;
		}
		switch(rewardLevel) {
			case 1:
				switch(Misc.random(3)) {
					case 0:
						return new PublicEventReward(995,(Misc.random(3) + 1) * 10000);
					case 1:
						rewardId = 533;
						rewardAmount = (Misc.random(14) + 1) * 10;
						return new PublicEventReward(rewardId,rewardAmount);
					case 2:
						rewardId = 1624;
						rewardAmount = (Misc.random(9) + 1) * 10;
						return new PublicEventReward(rewardId,rewardAmount);
					case 3:
						rewardId = 1740;
						rewardAmount = (Misc.random(19) + 1) * 10;
						return new PublicEventReward(rewardId,rewardAmount);
				}
			break;
			case 2:
				switch(Misc.random(3)) {
					case 0:
						rewardId = 995;
						rewardAmount = (Misc.random(15) + 5) * 10000;
						return new PublicEventReward(rewardId,rewardAmount);
						
					case 1:
						rewardId = 537;
						rewardAmount = (Misc.random(14) + 6) * 10;
						return new PublicEventReward(rewardId,rewardAmount);
						
					case 2:
						rewardId = 1622;
						rewardAmount = (Misc.random(10) + 10) * 10;
						return new PublicEventReward(rewardId,rewardAmount);
						
					case 3:
						rewardId = 386;
						rewardAmount = (Misc.random(80) + 20) * 10;
						return new PublicEventReward(rewardId,rewardAmount);
						
					
				}
			break;
			case 3:
				switch(Misc.random(5)) {
					case 0:
						rewardId = 995;
						rewardAmount = (Misc.random(50) + 10) * 10000;
						return new PublicEventReward(rewardId,rewardAmount);
						
					case 1:
						rewardId = 537;
						rewardAmount = (Misc.random(100) + 30) * 10;
						return new PublicEventReward(rewardId,rewardAmount);
						
					case 2:
						rewardId = 4151;
						rewardAmount = 1;
						return new PublicEventReward(rewardId,rewardAmount);
						
					case 3:
						rewardId = 4151;
						rewardAmount = 1;
						return new PublicEventReward(rewardId,rewardAmount);
						
					case 4:
						rewardId = 4151;
						rewardAmount = 1;
						return new PublicEventReward(rewardId,rewardAmount);
						
					case 5:
						rewardId = 4151;
						rewardAmount = 1;
						return new PublicEventReward(rewardId,rewardAmount);
						
				}
			break;
		}
		return null;
	}
	

	
	/** Reward class **/
	class PublicEventReward {
		private int rewardId;
		private int rewardAmount;
		public PublicEventReward(int id, int amt){
			this.rewardId = id;
			this.rewardAmount = amt;
		}
		public int getAmount() {
			return rewardAmount;
		}
		public int getID() {
			return rewardId;
		}
	}
	
	/** Our actual ::randomstring command **/
	class PublicEventCommand  implements Command {
		/** Our reward for this instance **/
		private PublicEventReward reward;
		
		public PublicEventCommand(PublicEventReward reward){
			this.reward = reward;
		}
		@Override
		public void execute(Client client, String command) {
			int rewardId = this.reward.getID();
			int rewardAmount = this.reward.getAmount();
			PlayerManager.getSingleton().sendGlobalMessage("[@red@Trivia@bla@]@mag@ " + client.playerName + " has won a " + Server.getItemManager().getItemDefinition(rewardId).getName() +" x "+rewardAmount+"!");
			client.getActionAssistant().addItem(rewardId, rewardAmount);
			/** remove this command **/
			String oldCommand = PublicEvent.getInstance().getString();
			CommandManager.commandMap.remove(oldCommand);
			PublicEvent.getInstance().isRunning = false;
		}
	}

	
}