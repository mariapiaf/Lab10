package it.polito.tdp.rivers.model;

import java.time.LocalDate;


public class Event implements Comparable<Event>{

	public enum EventType {
		INGRESSO,
		USCITA,
		TRACIMAZIONE, 
		IRRIGAZIONE
	}
	
	private EventType type;
	private LocalDate date;
	private Flow flow;
	
	public Event(EventType type, LocalDate date, Flow flow) {
		super();
		this.type = type;
		this.date = date;
		this.flow = flow;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	@Override
	public int compareTo(Event o) {
		if(!this.getDate().equals(o.getDate()))
			return this.getDate().compareTo(o.getDate());
		else {
			if(this.getType()== EventType.INGRESSO)
				return -1;
			else
				return 1;
		}
		
	}

	@Override
	public String toString() {
		return "Event [type=" + type + ", date=" + date + ", flow=" + flow + "]";
	}
	
	
}
