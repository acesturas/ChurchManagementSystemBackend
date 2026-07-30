package tim.dev.gfs.service;

import org.springframework.stereotype.Service;

import tim.dev.gfs.dao.EventsDao;
import tim.dev.gfs.dto.EventResponse;
import tim.dev.gfs.model.Event;

@Service
public class EventsService {

    private final EventsDao eventsDao;

    public EventsService(EventsDao eventsDao) {
        this.eventsDao = eventsDao;
    }

    public EventResponse addEvent(Event event) {
        return eventsDao.addEvent(event);
    }

}