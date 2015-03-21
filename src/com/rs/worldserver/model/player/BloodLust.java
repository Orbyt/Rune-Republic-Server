package com.rs.worldserver.model.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import com.rs.worldserver.Config;
import java.util.List;
import java.util.ArrayList;

public class BloodLust {
	/** Connection **/
	public Connection con = null;
	/** Statement **/
	public Statement statement;

	/** Create Connection **/
	public void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://" + Config.SQLHost
					+ "/vanquish", "root", "testing");
			statement = con.createStatement();
			System.out.println("[BloodLust] Database pooled");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Database methods **/

	/**
	 * isATeam Checks database to see if team exists
	 * 
	 * @param name
	 *            - Team Name
	 * @return True or False
	 */
	public int isATeam(String name) {
		try {
			String query = "SELECT * FROM blteams";
			ResultSet results =  query(query);
			while (results.next()) {
				if (results.getString("tname").equalsIgnoreCase(name))
					return results.getInt("tid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	

	/**
	 * isATeamPlayer Checks database to see if player is on a team
	 * 
	 * @param name
	 *            - player Name
	 * @return -1 if not found
	 * @return tid if found
	 */
	public int isATeamPlayer(String name) {
		try {
			String query = "SELECT * FROM blplayer";
			ResultSet results =  query(query);
			while (results.next()) {
				if (results.getString("pname").equalsIgnoreCase(name))
					return results.getInt("tid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
		public int getPlayerId(String name) {
		try {
			String query = "SELECT * FROM blplayer";
			ResultSet results =  query(query);
			while (results.next()) {
				if (results.getString("pname").equalsIgnoreCase(name))
					return results.getInt("pid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	public int geTRating(int tid){
		try {
			String query = "SELECT * FROM blteams where tid="+tid;
			ResultSet results =  query(query);
			while (results.next()) {
					return results.getInt("TRating");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public String getTeamFromPlayer(String name) {
		try {
			String query = "SELECT t.tname FROM blteams t, blplayer p WHERE p.pname = '"+name+"' AND t.tid = p.tid";
			ResultSet results = query(query);
			while (results.next()) {
					return results.getString("tname");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * getTeamInfo Retrieve our team data from database
	 * 
	 * @param teamid
	 * @return team object
	 */
	public Team getTeamInfo(int id) {
		try {
			String query = "SELECT * FROM blteams where tid='" + id + "'";
			ResultSet results = query(query);
			while (results.next()) {
				return new Team(results.getInt("tid"),
						results.getString("tcapt"), results.getString("tname"),
						results.getInt("tkills"), results.getInt("tdeaths"),
						results.getInt("TRating"),results.getString("tratio"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateKills(int p1, int o1){
		String query = "Update blplayer set pdeaths = pdeaths + 1 where pid="+p1;
		try {
			 query(query);
		} catch (SQLException e) {
			
		}
		String query2 = "Update blplayer set pkills=pkills + 1 where pid="+o1;
		try {
			 query(query2);
		} catch (SQLException e) {
			
		}
	}
	/**
	 * createTeam Create a Team
	 * 
	 * @param teamname
	 * @param c
	 * @return
	 */

	public boolean createTeam(String teamname, String pass, String playername) {
		try {
			String query = "INSERT INTO blteams(tid,tname,tcapt,tkills,tdeaths,TRating,tpass,tratio) VALUES('null', '"
					+ teamname
					+ "','"
					+ playername
					+ "','0','0','1500','"
					+ pass + "','0')";
			 query(query);
			// add capt
			String query2 = "INSERT INTO blplayer (pid,pname,pkills,pdeaths,pratio,tid) VALUES('null','"
					+ playername + "','0','0','0','" + isATeam(teamname) + "')";
			 query(query2);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean createPlayer(String playername, int id) {
		String query2 = "INSERT INTO blplayer (pid,pname,pkills,pdeaths,pratio,tid) VALUES('null','"
				+ playername + "','0','0','0','" + id + "')";
		try {
			 query(query2);
		} catch (SQLException e) {
			return false;
			
		}
		return true;
	}

	public void recordKill(int kid,int oid,int pkp){
		Team t1 = getTeamInfo(kid);
		Team t2 = getTeamInfo(oid);
		int t1kills = t1.getTkills() + 1;
		int t1deaths = t1.getTdeaths();
		int t1rating = calculateRating(kid, oid, pkp, 1);
		int t2kills = t2.getTkills();
		int t2deaths = t2.getTdeaths() + 1;
		int t2rating = calculateRating(oid, kid, pkp, -1);
		String query = "Update blteams set tkills="+t1kills+",tdeaths="+t1deaths+",TRating="+t1rating+" where tid="+t1.gettId();
		try {
			 query(query);
		} catch (SQLException e) {
			
		}
		String query2 = "Update blteams set tkills="+t2kills+",tdeaths="+t2deaths+",TRating="+t2rating+" where tid="+t2.gettId();
		try {
			 query(query2);
		} catch (SQLException e) {
			
		}
		
	}

	public ResultSet query(String s) throws SQLException {
	
		try {
			statement = con.createStatement();
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = statement.executeQuery(s);
				return rs;
			} else {
				statement.executeUpdate(s);
			}
			return null;
		} catch (Exception e) {
			System.out.println("MySQL Error:"+s);
			e.printStackTrace();
		}
		return null;
	}
	
	
	public List<Team> getTeam(int ladder){
		List<Team> teams = new ArrayList<Team>();
		switch(ladder){
			// 
			case 0: // best overal
				try { 
					String query = "SELECT * FROM blteams ORDER BY TRating DESC";
					ResultSet results = query(query);
					while (results.next()) {
						teams.add(new Team(results.getInt("tid"),
								results.getString("tcapt"), results.getString("tname"),
								results.getInt("tkills"), results.getInt("tdeaths"),
								results.getInt("TRating"),results.getString("tratio")));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return teams;
			case 1: // best oplayer overall
				try { 
					String query = "SELECT * FROM blplayer ORDER BY pkills DESC";
					ResultSet results = query(query);
					while (results.next()) {
						teams.add(new Team(results.getInt("pid"),
								"", results.getString("pname"),
								results.getInt("pkills"), results.getInt("pdeaths"),
								0,""));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return teams;
			case 2: // most kills
				try { 
					String query = "SELECT * FROM blteams ORDER BY tkills DESC";
					ResultSet results = query(query);
					while (results.next()) {
						teams.add(new Team(results.getInt("tid"),
								results.getString("tcapt"), results.getString("tname"),
								results.getInt("tkills"), results.getInt("tdeaths"),
								results.getInt("TRating"),results.getString("tratio")));
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
				return teams;
			case 3: // ratio
				try { 
					String query = "SELECT * FROM blteams ORDER BY tratio DESC";
					ResultSet results = query(query);
					while (results.next()) {
						teams.add(new Team(results.getInt("tid"),
								results.getString("tcapt"), results.getString("tname"),
								results.getInt("tkills"), results.getInt("tdeaths"),
								results.getInt("TRating"),results.getString("tratio")));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return teams;
		}
		return teams;
	}	
	
	/** Bloodlust **/
	public BloodLust() {
//		createConnection();
	}

	/** Show rankings interface **/
	public void showRankings(Client client, int ladder) {
			List<Team> team = getTeam(ladder);
		switch (ladder) {
			case 0:
				for(int k = 0; k < 100; k++){ 
					client.getActionAssistant().sendFrame126("0", 46713+k); // rating
					client.getActionAssistant().sendFrame126("", 46814+k); // name
				}
				for(int k = 0; k < team.size() ; k++){ 
					client.getActionAssistant().sendFrame126(""+team.get(k).getTRatings(), 46713+k); // rating
					client.getActionAssistant().sendFrame126(team.get(k).getTName(), 46814+k); // name
				}
				break;
			case 1:
				for(int k = 0; k < 100; k++){ 
					client.getActionAssistant().sendFrame126("0", 46713+k); // rating
					client.getActionAssistant().sendFrame126("", 46814+k); // name
				}
				for(int k = 0; k < team.size() ; k++){ 
					client.getActionAssistant().sendFrame126(""+team.get(k).getTkills(), 46713+k); // rating
					client.getActionAssistant().sendFrame126(team.get(k).getTName(), 46814+k); // name
				}
				break;
			case 2:
				for(int k = 0; k < 100; k++){ 
					client.getActionAssistant().sendFrame126("0", 46713+k); // rating
					client.getActionAssistant().sendFrame126("", 46814+k); // name
				}
				for(int k = 0; k < team.size() ; k++){ 
					client.getActionAssistant().sendFrame126(""+team.get(k).getTkills(), 46713+k); // rating
					client.getActionAssistant().sendFrame126(team.get(k).getTName(), 46814+k); // name
				}
				break;
			case 3:
				for(int k = 0; k < 100; k++){ 
					client.getActionAssistant().sendFrame126("0", 46713+k); // rating
					client.getActionAssistant().sendFrame126("", 46814+k); // name
				}
				for(int k = 0; k < team.size() ; k++){ 
					client.getActionAssistant().sendFrame126(""+team.get(k).getTRatio(), 46713+k); // rating
					client.getActionAssistant().sendFrame126(team.get(k).getTName(), 46814+k); // name
				}
				break;
		}
					client.getActionAssistant().showInterface(46000);
	}

	/** Elo Ranking **/
	public int calculateRating(int kid, int oid, int pkp, int win) {
		Team t1 = getTeamInfo(kid);
		Team t2 = getTeamInfo(oid);
		int c = pkp >> 1;
		int s = win;
		int rating = 0;
		if (t1 == null || t2 == null)
			return -1;
		rating = (int) (((s) * (c * expectancy(
				t1.getTRatings(), t2.getTRatings()))));
			if(rating == 0)
					rating = 3;	
			else if (rating > 60)
					rating = 60;
					
			rating += t1.getTRatings();
					
		return rating;
	}
	public int calcEstRating(int o1rate,int p1rate,int pkp,int win){
		Team t1 = getTeamInfo(o1rate);
		Team t2 = getTeamInfo(p1rate);
		int c = pkp >> 1;
		int s = win;
		int rating = 0;
		if (t1 == null || t2 == null)
			return -1;
		rating = (int) (((s) * (c * expectancy(
				t1.getTRatings(), t2.getTRatings()))));
			if(rating == 0)
					rating = 3;	
			else if (rating > 60)
					rating = 60;
					
		return rating;
	
	}
	/** Expected ratio of winning **/
	private double expectancy(int rating1, int rating2) {
		int diff = rating1 - rating2;
		return 1.0 / (1.0 + Math.pow(10.0, diff / 400.0));
	}
}

class Team {
	private int tId;
	private String tchat;
	private String tname;
	private String tratio;
	private int tkills;
	private int tdeaths;
	private int tRatings;
	
	public Team(int tId, String tchat, String tname, int tkills, int tdeaths, int TRatings,String ratio) {
		super();
		this.tId = tId;
		this.tchat = tchat;
		this.tname = tname;
		this.tkills = tkills;
		this.tdeaths = tdeaths;
		this.tRatings = TRatings;
		this.tratio = ratio;
	}

	public String getTName(){
		return tname;
	}
	public int gettId() {
		return tId;
	}

	public String getTchat() {
		return tchat;
	}

	public int getTkills() {
		return tkills;
	}

	public int getTdeaths() {
		return tdeaths;
	}

	public int getTRatings() {
		return tRatings;
	}
	public String getTRatio(){
		return tratio;
	}
}
