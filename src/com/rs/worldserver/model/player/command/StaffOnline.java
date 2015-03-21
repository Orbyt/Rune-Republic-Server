package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.util.RightsProcessor;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import java.util.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;

public class StaffOnline implements Command {

	class StaffMember implements Comparable {
		private String name;
		private int world;
		private int rank = 0;
		private int privacy;
		public StaffMember(String name, int world, int rank){
			this.name = name;
			this.world = world;
			this.rank = rank;
			this.privacy = 0;
		}
		public int getPM(){
			return this.privacy;
		}
		public int getRank(){
			return this.rank;
		}
		public String getName(){
			return this.name;
		}
		public int getWorld(){
			return this.world;
		}
		public void setWorld(int w){
			this.world = w;
		}
		public int compareTo(Object anotherPlayer) throws ClassCastException {
		if (!(anotherPlayer instanceof StaffMember))
		throw new ClassCastException("A player object expected.");
		int anotherPlayerRating = ((StaffMember) anotherPlayer).getRank();
		return this.rank - anotherPlayerRating;
	}
	}
	public Set<String> staff = new HashSet<String>();
	public List<StaffMember> online = new ArrayList<StaffMember>();
	public void invoke(){
		for(String name : RightsProcessor.getSS()){
			staff.add(name);
		}
		for(String name: RightsProcessor.getAdv()){
			staff.add(name);
		}	
		for(String name: RightsProcessor.getJags()){
			staff.add(name);
		}	
		for(String name : RightsProcessor.getMod()){
			staff.add(name);
		}
		for(String name : RightsProcessor.getAdmin()){
			staff.add(name);
		}
		for(String name : RightsProcessor.getDev()){
			staff.add(name);
		}
		Iterator<String> it = staff.iterator();
		while(it.hasNext()){
			String s = it.next();
		}
	}

	@Override
	public void execute(Client c, String command) {
		if (c.inDuelArena()) {
			c.getActionAssistant().Send("You cannot use this here.");
			return;
		}	
			invoke();
			System.out.println(online.size());
			Comparator comparator = Collections.reverseOrder();
			Collections.sort(online);
			c.getActionAssistant().clearQuestInterface();
			c.getActionAssistant().sendFrame126("@dre@Rune Republic Staff Online", 8144); // Title
			c.getActionAssistant().sendFrame126("@dbl@Players Online: @gre@" + PlayerManager.getSingleton().getPlayerCount(),8146 );
			int count = 0;
			for(int i = 8147; i < (8147+online.size()); i++) {
				switch(RightsProcessor.checkRights(online.get(count).getName())){
					case 3:
						c.getActionAssistant().sendFrame126("@blu@ Support: " + online.get(count).getName() + " - @gre@ World: " + online.get(count).getWorld(), i);
						break;
					case 4:
						c.getActionAssistant().sendFrame126("@mag@ Mod: " + online.get(count).getName() + " - @gre@ World: " + online.get(count).getWorld(), i);
						break;
					case 10:
						c.getActionAssistant().sendFrame126("@gre@ Advisor: " + online.get(count).getName() + " - @gre@ World: " + online.get(count).getWorld(), i);
						break;
					case 8:
						c.getActionAssistant().sendFrame126("@yel@ Admin: " + online.get(count).getName() + " - @gre@ World: " + online.get(count).getWorld(), i);
						break;
					case 9:
					case 11:
						c.getActionAssistant().sendFrame126("@red@ Developer: " + online.get(count).getName() + " - @gre@ World: " + online.get(count).getWorld(), i);
						break;
				}
				System.out.println( online.get(count).getName() + " " +RightsProcessor.checkRights(online.get(count).getName()) + " " + online.get(count).getWorld());
				count++;
			} 
			c.getActionAssistant().showInterface(8134);
			c.flushOutStream();
			staff.clear();
			online.clear();
	}
}