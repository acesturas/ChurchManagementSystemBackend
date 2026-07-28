package tim.dev.gfs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import tim.dev.gfs.dao.EventsDao;
import tim.dev.gfs.model.Events;

@Service
public class EventsService {

    private final EventsDao eventsDao;

    public EventsService(EventsDao eventsDao) {
        this.eventsDao = eventsDao;
    }

    public List<Events> getAllEvents() {
        return eventsDao.getAllEvents();
    }

}