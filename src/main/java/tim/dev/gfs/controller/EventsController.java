package tim.dev.gfs.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim.dev.gfs.dto.AddEventRequest;
import tim.dev.gfs.dto.AddEventResponse;
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
	public AddEventResponse addEvent(@RequestBody AddEventRequest event){
    	System.out.println("Inside EventsController addEvent()");
		return eventsService.addEvent(event);
	}
	
	@GetMapping("/getEvents")
	public List<Event> getEvents(){
    	System.out.println("Inside EventsController readEvents()");
		return eventsService.getEvents();
	}
}
