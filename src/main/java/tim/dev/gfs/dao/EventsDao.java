package tim.dev.gfs.dao;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tim.dev.gfs.dto.AddEventRequest;
import tim.dev.gfs.dto.LastEventIdResponse;
import tim.dev.gfs.google.client.GoogleSheetsClient;
import tim.dev.gfs.model.Event;
import tim.dev.gfs.utils.StaticUtils;

@Repository
public class EventsDao {

    // Client responsible for communicating with Google Apps Script
    private final GoogleSheetsClient googleSheetsClient;

    public EventsDao(GoogleSheetsClient googleSheetsClient) {
        this.googleSheetsClient = googleSheetsClient;
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
    public String addEvent(AddEventRequest event) {

        System.out.println("Inside EventsDao.addEvent()");
        

        int nextSequence = StaticUtils.getNextSequence(getLastEventId());
        System.out.println("\ngetLastEventId():" + getLastEventId());
        System.out.println("\nnextSequence:" + nextSequence);

        // TODO:
        // Replace 0 with the actual next sequence once
        // getLastEventId() and getNextSequence() are connected.
        event.setEventId(StaticUtils.generateId("EV", "PC", nextSequence));

        event.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        event.setUpdatedOn(null);
        event.setUpdatedBy("");

        String response = null;
		try {
			response = googleSheetsClient.post(
			        "EVENT",
			        "CREATE",
			        event,
			        String.class
			);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return response;
    }
    
    public List<Event> getEvents() {

        System.out.println("Inside EventsDao.getEvents()");

        try {

            String response = googleSheetsClient.post(
                    "EVENT",
                    "READ",
                    null,
                    String.class
            );

            System.out.println("Read Events response: " + response);

            Gson gson = StaticUtils.getGson();

            Type listType = new TypeToken<List<Event>>() {}.getType();

            return gson.fromJson(response, listType);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return new ArrayList<>();
    }
}