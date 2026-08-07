package tim.dev.gfs.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import tim.dev.gfs.dto.AddEventRequest;
import tim.dev.gfs.dto.LastEventIdResponse;
import tim.dev.gfs.google.client.GoogleSheetsClient;
import tim.dev.gfs.model.Event;
import tim.dev.gfs.utils.StaticUtils;

@Repository
public class EventsDao {

    private final DataSource dataSource;

    // Client responsible for communicating with Google Apps Script
    private final GoogleSheetsClient googleSheetsClient;

    public EventsDao(DataSource dataSource, GoogleSheetsClient client) {
        this.dataSource = dataSource;
		this.googleSheetsClient = client;
    }

    /**
     * Retrieves the latest Event ID from Google Sheets.
     *
     * Apps Script is responsible for looking at the sheet and returning
     * the last Event ID (e.g. EVPC2026080400005).
     *
     * This method simply forwards the request and returns the result.
     */
    public String getLastEventId() {

        try {

            LastEventIdResponse response =
                    googleSheetsClient.post(
                            "EVENT",
                            "GET_LAST_EVENT_ID",
                            null,
                            LastEventIdResponse.class);

            return response.getEventId();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Sends a new event to Google Apps Script for insertion.
     *
     * Before sending, the backend generates the Event ID and sets
     * the audit fields such as createdOn, updatedOn, and updatedBy.
     */
//    public String addEvent(AddEventRequest event) {
//
//        System.out.println("Inside EventsDao.addEvent()");
//        
//
//        int nextSequence = StaticUtils.getNextSequence(getLastEventId());
//        System.out.println("\ngetLastEventId():" + getLastEventId());
//        System.out.println("\nnextSequence:" + nextSequence);
//
//        // TODO:
//        // Replace 0 with the actual next sequence once
//        // getLastEventId() and getNextSequence() are connected.
//        event.setEventId(StaticUtils.generateId("EV", "PC", nextSequence));
//
//        event.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
//        event.setUpdatedOn(null);
//        event.setUpdatedBy("");
//
//        String response = null;
//		try {
//			response = googleSheetsClient.post(
//			        "EVENT",
//			        "CREATE",
//			        event,
//			        String.class
//			);
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//        return response;
//    }
    

	public boolean addEvent(AddEventRequest event) {

        System.out.println("Inside EventsDao.addEvent()");
        
        String sql = """
        		INSERT INTO events(id, event_name, description, event_start_date, event_end_date, start_time, end_time, location, created_by)
        		VALUES
        		(?, ?, ?, ?, ?, ?, ?, ?, ?)
        		""";
        
        try(Connection conn = dataSource.getConnection();
        		PreparedStatement ps = conn.prepareStatement(sql)){
        	
        	ps.setString(1, sql);
        	
        	int inserted = ps.executeUpdate();
        	
        	if(inserted > 0) {
        		return true;
        	}
        	return false;
        	
        	
        } catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
        	return false;
		}
	}
    
    public List<Event> getEvents() {

        System.out.println("Inside EventsDao.getEvents()");
        
        List<Event> eventList = new ArrayList<Event>();
        
        String sql = """
        		SELECT *
        		  FROM events
        		""";

        try(Connection conn = dataSource.getConnection();
        		PreparedStatement ps = conn.prepareStatement(sql);
        		ResultSet rs = ps.executeQuery()) {

        	while(rs.next()) {
        		System.out.println("Events table has records");
        		Event e = new Event();
        		e.setEventId(rs.getString("id"));
        		e.setEventName(rs.getString("event_name"));
        		e.setEventDescription(rs.getString("description"));
        		
        		Date startDate = rs.getDate("event_start_date");
        		if (startDate != null) {
            		e.setStartDate(startDate.toLocalDate());
        		}

        		Date endDate = rs.getDate("event_end_date");
        		if(endDate != null) {
            		e.setEndDate(endDate.toLocalDate());
        		}
        		
        		Time startTime = rs.getTime("start_time");
        		if(startTime != null) {
            		e.setStartTime(startTime.toLocalTime());
        		}
        		
        		Time endTime = rs.getTime("end_time");
        		if(endTime != null) {
            		e.setEndTime(endTime.toLocalTime());
        		}
        		
        		e.setEventLocation(rs.getString("location"));
        		e.setCreatedBy(rs.getString("created_by"));
        		
        		Timestamp createdOn = rs.getTimestamp("created_at");
        		if(createdOn != null) {
            		e.setCreatedOn(createdOn);
        		}
        		
        		eventList.add(e);
        	}
        	
        	return eventList;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return new ArrayList<>();
    }
}