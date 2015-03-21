//package com.rs.worldserver.model.player.command;
//
//import com.rs.worldserver.Server;
//import com.rs.worldserver.model.player.Client;
//import com.rs.worldserver.model.player.Command;
//import com.rs.worldserver.content.skill.SkillConstants;
//import com.rs.worldserver.util.*;
//import java.io.*;
//
//
///**
//@Author Orbit
//@parm - Reverts accounts from and old account and changes the password
//*/
//
//
//public class Revert implements Command {
//	String username;
//	String password;
//	@Override
//	public void execute(Client c, String command) {
	//	if (c.playerName.equalsIgnoreCase("Orbit")) 
//		int divider = command.indexOf(",");
//		username = command.substring(7, divider);
//		username = username.toLowerCase();
//		username = username.replaceAll(" ", "_");
//		password = command.substring(divider + 1, command.length());
//		
//		if (c.playerName.equalsIgnoreCase("Orbit")) {//||  c.playerName.equalsIgnoreCase("Orbit")) {
//		if (username.equalsIgnoreCase("Orbit")) {
//		return;
//		}
//				moveAccount(username);
//				changePassword(username,password);
//				c.getActionAssistant().sendMessage("@red@Changed "+ username + " password to " + password);
//		} else {
//			c.getActionAssistant().sendMessage("Revert is an unknown command!");
//		}
	//}

//
//	public void moveFile(String userName){
//		String charName = userName.replaceAll("_", " ");
//		File outputFile = new File("C:\\nrpkdatviewer\\move.bat");
//		try {
//			PrintWriter output = null;
//			output = new PrintWriter(outputFile);
//			output.println("move \"c:\\Revert\\" + charName +".dat\" \"c:\\Shard\\savedGames\\\"");
//			output.println("EXIT");
//			output.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	public void createBat(String userName, String PassWord){
//		String charName = userName.replaceAll("_", " ");
//		File outputFile = new File("C:\\nrpkdatviewer\\run.bat");
//		try {
//			PrintWriter output = null;
//			output = new PrintWriter(outputFile);
//			output.println("java -classpath c:\\nrpkdatviewer\\bin com.rs.worldserver.model.player.main -changepass \""+charName+"\" \""+PassWord+"\"");
//			output.println("EXIT");
//			output.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void moveAccount(String Username) {
//	    moveFile(Username);
//	    Runtime r=Runtime.getRuntime();
//		Process p=null;
//		try
//		{
//			p = r.exec(new String[]{"cmd", "/C", "start", "C:\\nrpkdatviewer\\move.bat"});
//			
//			InputStream stderr = p.getErrorStream();
//            InputStreamReader isr = new InputStreamReader(stderr);
//            BufferedReader br = new BufferedReader(isr);
//            String line = null;
//            System.out.println("<ERROR>");
//            while ( (line = br.readLine()) != null)
//                System.out.println(line);
//            System.out.println("</ERROR>");
//            int exitVal = p.waitFor();
//            //System.out.println("Process exitValue: " + exitVal);
//			//c.getActionAssistant().sendMessage("Moved "+ Username);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void changePassword(String Username, String newPassword) {
//	    createBat(Username,newPassword);
//	    Runtime r=Runtime.getRuntime();
//		Process p=null;
//		try
//		{
//			p = r.exec(new String[]{"cmd", "/C", "start", "C:\\nrpkdatviewer\\run.bat"});
//			
//			InputStream stderr = p.getErrorStream();
//            InputStreamReader isr = new InputStreamReader(stderr);
//            BufferedReader br = new BufferedReader(isr);
//            String line = null;
//            System.out.println("<ERROR>");
//            while ( (line = br.readLine()) != null)
//                System.out.println(line);
//            System.out.println("</ERROR>");
//            int exitVal = p.waitFor();
//           // System.out.println("Process exitValue: " + exitVal);
//			//c.getActionAssistant().sendMessage("Changed "+ Username + " password to " + newPassword);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
		
//}