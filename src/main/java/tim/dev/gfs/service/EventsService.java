package tim.dev.gfs.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tim.dev.gfs.dao.EventsDao;
import tim.dev.gfs.dto.AddEventRequest;
import tim.dev.gfs.dto.AddEventResponse;
import tim.dev.gfs.model.Event;

@Service
public class EventsService {

    private final EventsDao eventsDao;

    public EventsService(EventsDao eventsDao) {
        this.eventsDao = eventsDao;
    }

    @Transactional
    public AddEventResponse addEvent(AddEventRequest event) {
    	System.out.println("Inside EventsService addEvent()");
        return eventsDao.addEvent(event);
    }

    @Transactional
    public AddEventResponse updateEvent(AddEventRequest event) {
    	System.out.println("Inside EventsService addEvent()");
        return eventsDao.updateEvent(event);
    }
    
    
    
    @Transactional
    public List<Event> getEvents() {
    	System.out.println("Inside EventsService readEvents()");
    	try{
            return eventsDao.getEvents();
    	} catch (Exception e) {
    		return null;
		}
    }

}