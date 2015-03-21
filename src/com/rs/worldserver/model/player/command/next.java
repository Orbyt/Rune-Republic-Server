package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.player.HelpManager;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.ItemProcessor;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.io.*;
public class next implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.playerRights > 3) {
			if(c.helpOn){
				c.isHelping = false;
				if(!c.HelpNext.equalsIgnoreCase("a")){
					Client p2 = (Client) Server.getPlayerManager().getPlayerByName(c.HelpNext);
					if(p2 == null) {
						return;
					}
					ClanChat.removeMember(p2,c.playerName + " Help");
					ClanChat.removeCCMenu(p2);
					p2.getActionAssistant().sendMessage("@blu@"+p2.playerName+" , Thanks for using the Rune Republic Help System!");
				}
				HelpManager.assignPlayerToMe(c);
				//saveCommands(c,"Used at " +  now());
			}
		} else {

		}			
	
	}
		  public static final String DATE_FORMAT_NOW = "MM/dd/yyyy HH:mm:ss";

	  public static String now() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	    return sdf.format(cal.getTime());

	  }
		public void saveCommands(Client c, String data) {
		BufferedWriter bw = null;

		try {
			//bw = new BufferedWriter(new FileWriter("./savedGames/Trades/" + client.playerName + ".txt", true));
			bw = new BufferedWriter(new FileWriter("F://helplogs//" + c.playerName + ".txt", true));
			bw.write(data);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException ioe2) {
					//printOut("Error writing system log.");
					ioe2.printStackTrace();
			}
		}
	}	
}