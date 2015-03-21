package com.rs.worldserver.model.player;
/*
 * Blood lust pvp team class
 * @author - Orbit
 */
public class Teams implements Comparable<Object>{
	private int id = -1;
	private String teamName,teamLeader;
	private String[] teamMembers;
	private int teamRating,teamKills,teamDeaths = 0;
	
	public Teams(int id, String teamName, String teamLeader, String[] TeamMembers, int teamKills, int teamDeaths, int teamRating){
		this.id = id;
		this.teamName = teamName;
		this.teamLeader = teamLeader;
		this.teamMembers = TeamMembers;
		this.teamKills = teamKills;
		this.teamDeaths = teamDeaths;
		this.teamRating = teamRating;
	}
	
	public int getID(){
		return id;
	}
	
	public int getTeamKills(){
		return teamKills;
	}
	
	public int getTeamDeaths(){
		return teamDeaths;
	}
	
	public String getTeamName(){
		return teamName;
	}
	public String getTeamLeader(){
		return teamLeader;
	}
	
	public String[] getTeamMembers(){
		return teamMembers;
	}

	public int getTeamMembersLength(){
		return teamMembers.length;
	}
	
	public int getTeamRating(){
		return teamRating;
	}
    public int compareTo(Object anotherTeam) throws ClassCastException {
		    if (!(anotherTeam instanceof Teams))
		      throw new ClassCastException("A Team object expected.");
		    int anotherTeamsRating = ((Teams) anotherTeam).getTeamRating();  
		    return this.teamRating - anotherTeamsRating;    
	 }
	

}
