package tim.dev.gfs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import tim.dev.gfs.dao.EventsDao;
import tim.dev.gfs.dto.AddEventRequest;
import tim.dev.gfs.model.Event;

@Service
public class EventsService {

    private final EventsDao eventsDao;

    public EventsService(EventsDao eventsDao) {
        this.eventsDao = eventsDao;
    }

    public String addEvent(AddEventRequest event) {
    	System.out.println("Inside EventsService addEvent()");
        return eventsDao.addEvent(event);
    }
    
    public List<Event> getEvents() {
    	System.out.println("Inside EventsService readEvents()");
        return eventsDao.getEvents();
    }

}