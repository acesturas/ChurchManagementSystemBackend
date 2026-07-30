package tim.dev.gfs.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim.dev.gfs.dto.EventResponse;
import tim.dev.gfs.model.Event;
import tim.dev.gfs.service.EventsService;

@RestController
@RequestMapping("/api/events")
public class EventsController {
	private final EventsService eventsService;

	public EventsController(EventsService eventsService) {
		this.eventsService = eventsService;
	}
	
	@PostMapping("/addEvent")
	public EventResponse addEvent(@RequestBody Event event){
		return eventsService.addEvent(event);
	}
}
