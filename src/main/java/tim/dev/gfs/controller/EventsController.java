package tim.dev.gfs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<String> addEvent(@RequestBody Event event){
    	System.out.println("Inside EventsController addEvent()");
		return eventsService.addEvent(event);
	}
}
