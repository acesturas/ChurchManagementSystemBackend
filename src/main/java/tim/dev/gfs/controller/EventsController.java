package tim.dev.gfs.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim.dev.gfs.model.Events;
import tim.dev.gfs.service.EventsService;

@RestController
@RequestMapping("/api/events")
public class EventsController {
	private final EventsService eventsService;

	public EventsController(EventsService eventsService) {
		this.eventsService = eventsService;
	}
	
	@GetMapping("/getAllEvents")
	public List<Events> getAllEvents(){
		return eventsService.getAllEvents();
	}
}
