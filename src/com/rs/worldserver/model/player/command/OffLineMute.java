package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.Config;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.*;
import java.io.*;
import java.net.*;

public class OffLineMute implements Command {
	@Override
	public void execute(Client c, String command) {
	if (c.playerRights > 3) {
	if (!Config.MainServer) {
	c.getActionAssistant().sendMessage("@red@You can't use that command on this world");
	return;
	}
		String username = command.substring(8, command.length());
		username = username.toLowerCase();
		username = username.replaceAll(" ", "_");
		Unmute(username);
		c.getActionAssistant().sendMessage("@red@Unmuted " + username);
	}
}
	public void createBat(String userName){
		String charName = userName.replaceAll("_", " ");
		File outputFile = new File("C:\\nrpkdatviewer\\run.bat");
		try {
			PrintWriter output = null;
			output = new PrintWriter(outputFile);
			output.println("java -classpath c:\\nrpkdatviewer\\bin com.rs.worldserver.model.player.main -unmute \""+charName+"\"");
			output.println("EXIT");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void Unmute(String Username) {
	    createBat(Username);
	    Runtime r=Runtime.getRuntime();
		Process p=null;
		try
		{
			p = r.exec(new String[]{"cmd", "/C", "start", "C:\\nrpkdatviewer\\run.bat"});
			
			InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            System.out.println("<ERROR>");
            while ( (line = br.readLine()) != null)
                System.out.println(line);
            System.out.println("</ERROR>");
            int exitVal = p.waitFor();
            System.out.println("Process exitValue: " + exitVal);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}