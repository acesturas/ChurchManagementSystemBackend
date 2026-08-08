package tim.dev.gfs.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import tim.dev.gfs.dto.AddEventRequest;
import tim.dev.gfs.dto.AddEventResponse;
import tim.dev.gfs.google.client.GoogleSheetsClient;
import tim.dev.gfs.model.Event;
import tim.dev.gfs.utils.TransactionIdGenerator;

@Repository
public class EventsDao {

    private final DataSource dataSource;

    // Client responsible for communicating with Google Apps Script
//    private final GoogleSheetsClient googleSheetsClient;
    private final TransactionIdGenerator idGenerator;

    public EventsDao(
    		DataSource dataSource, 
    		GoogleSheetsClient client, 
            TransactionIdGenerator idGenerator) {
    	
        this.dataSource = dataSource;
//		this.googleSheetsClient = client;
        this.idGenerator = idGenerator;
        
    }

    /**
     * Retrieves the latest Event ID from Google Sheets.
     *
     * Apps Script is responsible for looking at the sheet and returning
     * the last Event ID (e.g. EVPC2026080400005).
     *
     * This method simply forwards the request and returns the result.
     */
//    public String getLastEventId() {
//
//        try {
//
//            LastEventIdResponse response =
//                    googleSheetsClient.post(
//                            "EVENT",
//                            "GET_LAST_EVENT_ID",
//                            null,
//                            LastEventIdResponse.class);
//
//            return response.getEventId();
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return "";
//    }

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
    
    
    public AddEventResponse updateEvent(AddEventRequest event){

        System.out.println("Inside EventsDao.addEvent()");
        
        String sql = """
        		UPDATE events
        		   SET event_name = ?,
        			   description = ?, 
        			   event_start_date = ?, 
        			   event_end_date = ?, 
        			   start_time = ?, 
        			   end_time = ?, 
        			   location = ?, 
        			   updted_by = ?
        		 WHERE id = ?
        		""";
        
        try(Connection conn = dataSource.getConnection();
        		PreparedStatement ps = conn.prepareStatement(sql)){

        	ps.setString(1, event.getEventName());
        	ps.setString(2, event.getEventDescription());
        	ps.setDate(3, Date.valueOf(event.getStartDate()));
        	ps.setDate(4, Date.valueOf(event.getEndDate()));
        	ps.setTime(5, Time.valueOf(event.getStartTime()));
        	ps.setTime(6, Time.valueOf(event.getEndTime()));
        	ps.setString(7, event.getEventLocation());
        	ps.setString(8, event.getCreatedBy());
        	ps.setString(9, event.getEventId());
        	
        	int updated = ps.executeUpdate();

        	if(updated > 0) {
        		return new AddEventResponse(true, "Event Successfully Updated!");
        	}
    		return new AddEventResponse(false, "Error! Updating Failed.");
        	
        } catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
    		return new AddEventResponse(false, "Error! Updating Failed.");
		}
        		
    }

	public AddEventResponse addEvent(AddEventRequest event) {

        System.out.println("Inside EventsDao.addEvent()");
        
        String sql = """
        		INSERT INTO events(
        			id, 
        			event_name, 
        			description, 
        			event_start_date, 
        			event_end_date, 
        			start_time, 
        			end_time, 
        			location, 
        			created_by)
        		VALUES
        		(?, ?, ?, ?, ?, ?, ?, ?, ?)
        		""";
        
        try(Connection conn = dataSource.getConnection();
        		PreparedStatement ps = conn.prepareStatement(sql)){
        	
        	event.setEventId(idGenerator.generateId("events", "EV", "PC"));
        	
        	ps.setString(1, event.getEventId());
        	ps.setString(2, event.getEventName());
        	ps.setString(3, event.getEventDescription());
        	ps.setDate(4, Date.valueOf(event.getStartDate()));
        	ps.setDate(5, Date.valueOf(event.getEndDate()));
        	ps.setTime(6, Time.valueOf(event.getStartTime()));
        	ps.setTime(7, Time.valueOf(event.getEndTime()));
        	ps.setString(8, event.getEventLocation());
        	ps.setString(9, event.getCreatedBy());
        	
        	int inserted = ps.executeUpdate();
        	
        	if(inserted > 0) {
        		return new AddEventResponse(true, "Event Successfully Saved!");
        	}
    		return new AddEventResponse(false, "Error! Saving Failed.");
        	
        	
        } catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
    		return new AddEventResponse(false, "Error! Saving Failed.");
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