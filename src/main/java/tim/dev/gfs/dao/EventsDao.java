package tim.dev.gfs.dao;

import static tim.dev.gfs.google.constant.GoogleSheetsConstants.ACTION_CREATE;
import static tim.dev.gfs.google.constant.GoogleSheetsConstants.MODULE_EVENT;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import tim.dev.gfs.dto.EventResponse;
import tim.dev.gfs.dto.GoogleSheetRequest;
import tim.dev.gfs.google.client.GoogleSheetsClient;
import tim.dev.gfs.model.Event;

@Repository
public class EventsDao {

    private final GoogleSheetsClient googleSheetsClient;

    public EventsDao(GoogleSheetsClient googleSheetsClient) {
        this.googleSheetsClient = googleSheetsClient;
    }

    public EventResponse addEvent(Event event) {

    	//si backend ngayon ang magrerequest sa google API, isesend nya yung mga data na iadd as events
        GoogleSheetRequest<Event> request = new GoogleSheetRequest<>();

        request.setModule(MODULE_EVENT);
        request.setAction(ACTION_CREATE);

        event.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        event.setUpdatedOn(null);
        event.setUpdatedBy("");

        request.setData(event);

//        return googleSheetsClient.post(
//                request,
//                EventResponse.class
//        );


        return googleSheetsClient.post(request);
    }

}