package tim.dev.gfs.service;

import org.springframework.stereotype.Service;

import tim.dev.gfs.dao.EventsDao;
import tim.dev.gfs.model.AddEventRequest;

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

}