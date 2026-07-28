package tim.dev.gfs.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Events {

    private String id;
    private String eventName;
    private String description;
    private LocalDate eventStartDate;
    private LocalDate eventEndDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
	
	public Events() {}

	public Events(String id, String eventName, String description, LocalDate eventStartDate, LocalDate eventEndDate,
			LocalTime startTime, LocalTime endTime, String location) {
		super();
		this.id = id;
		this.eventName = eventName;
		this.description = description;
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getEventStartDate() {
		return eventStartDate;
	}

	public void setEventStartDate(LocalDate eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	public LocalDate getEventEndDate() {
		return eventEndDate;
	}

	public void setEventEndDate(LocalDate eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	

	

}
