package com.rs.worldserver.model.player;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InGameEventManager {
	/** MySQl DataBase Info **/
	/** Connection **/
	public Connection con = null;
	/** Statement **/
	public Statement statement;

	/** Create Connection **/
	public void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://" + Config.SQLHost
					+ "/donationsystem", "root", "NUIDSfnu7387ca");
			statement = con.createStatement();
			System.out.println("[Ingame Event Calendar] Database pooled");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public InGameEventManager(){
		createConnection();
	}

	public void showInterface(Client client){
		List<Events> event = getEvents();
		int i = 0,j = 0,k = 0;
		for(i = 0; i< 14; i++){
			client.getActionAssistant().sendFrame126("", 50019+i);
			client.getActionAssistant().sendFrame126("", 50033+j);
			client.getActionAssistant().sendFrame126("Coming soon!", 50047+k);
			j++;
			k++;
		}
		j = 0;k = 0;
		for(i = 0; i< event.size(); i++){
			client.getActionAssistant().sendFrame126(event.get(i).getEventTime(), 50019+i);
			client.getActionAssistant().sendFrame126(event.get(i).getEventDay(), 50033+j);
			client.getActionAssistant().sendFrame126(event.get(i).getEventName(), 50047+k);
			j++;
			k++;
		}
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		client.getActionAssistant().sendFrame126(sdf.format(cal.getTime()) +" Server Time (EST)", 50062);
		sdf = new SimpleDateFormat("MMM d");
		client.getActionAssistant().sendFrame126(sdf.format(cal.getTime()), 50063);
		client.getActionAssistant().showInterface(50000);
	}
	
	public void showDetails(Client client, int i){
		List<Events> event = getEvents();
		if(i < event.size()){
			client.getActionAssistant().sendFrame126(event.get(i).getEventName(), 51011);
			client.getActionAssistant().sendFrame126(event.get(i).getInfo().getEventHost(), 51012);
			client.getActionAssistant().sendFrame126(event.get(i).getInfo().getEventWorld(), 51013);
			client.getActionAssistant().sendFrame126(event.get(i).getEventDay()+ " "+ event.get(i).getEventTime(), 51014);
			client.getActionAssistant().sendFrame126(event.get(i).getInfo().getEventLocation(), 51015);
			client.getActionAssistant().sendFrame126(event.get(i).getInfo().getEventDescription(), 51016);
			client.getActionAssistant().sendFrame126(" ", 51017);
			client.getActionAssistant().sendFrame126(" ", 51018);
			client.getActionAssistant().sendFrame126(" ", 51062);
			client.getActionAssistant().showInterface(51000);
		}
	}
	
	
	public List<Events> getEvents(){
		List<Events> tempEvents = new ArrayList<Events>();
		try {
			Statement statement = con.createStatement();
			String query = "Select events.eID, events.eventName, events.eventDay,  events.eventTime, eventinfo.eventHost,eventinfo.eventWorld,eventinfo.eventLocation,eventinfo.eventDescription from events inner join eventinfo on events.eID = eventinfo.eId ORDER BY events.eventDay, events.EventTime DESC"; 
			ResultSet results = statement.executeQuery(query);
			while (results.next()) {
					tempEvents.add(new Events(results.getInt("eID"),results.getString("eventName"), results.getString("eventTime"), results.getString("eventDay"), new EventInfo(results.getString("eventName"),results.getString("eventHost"),results.getString("eventWorld"),results.getString("eventTime"),results.getString("eventLocation"),results.getString("eventDescription"))));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempEvents;
	}
	public ResultSet query(String s) throws SQLException {
		try {
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
}

class Events {
	private int uID;
	private String eventTime;
	private String eventDay;
	private String eventName;
	private EventInfo info;
	public Events(int uID,String eventName, String eventTime, String eventDay, EventInfo info) {
		super();
		this.uID = uID;
		this.eventTime = eventTime;
		this.eventDay = eventDay;
		this.eventName = eventName;
		this.info = info;
	}
	public int getuID() {
		return uID;
	}
	public String getEventTime() {
		return eventTime;
	}
	public String getEventDay() {
		return eventDay;
	}
	public String getEventName() {
		return eventName;
	}
	public EventInfo getInfo() {
		return info;
	}
	
	public void setEventInfo(EventInfo info){
		this.info = info;
	}
}

class EventInfo {
	private String eventName;
	private String eventHost;
	private String eventWorld;
	private String eventTime;
	private String eventLocation;
	private String eventDescription;
	
	public EventInfo(String eventName, String eventHost, String eventWorld,
			String eventTime, String eventLocation, String eventDescription) {
		super();
		this.eventName = eventName;
		this.eventHost = eventHost;
		this.eventWorld = eventWorld;
		this.eventTime = eventTime;
		this.eventLocation = eventLocation;
		this.eventDescription = eventDescription;
	}
	
	public String getEventName() {
		return eventName;
	}
	public String getEventHost() {
		return eventHost;
	}
	public String getEventWorld() {
		return eventWorld;
	}
	public String getEventTime() {
		return eventTime;
	}
	public String getEventLocation() {
		return eventLocation;
	}
	public String getEventDescription() {
		return eventDescription;
	}
}