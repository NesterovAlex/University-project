package com.nesterov.university.model;

import java.time.LocalTime;

public class LessonTime {

	private long id;
	private int orderNumber;
	private LocalTime start;
	private LocalTime end;
	
	public LessonTime() {}

	public LessonTime(int orderNumber, LocalTime start, LocalTime end) {
		this.orderNumber = orderNumber;
		this.start = start;
		this.end = end;
	}
	
	public LessonTime(long id, int orderNumber, LocalTime start, LocalTime end) {
		this.id = id;
		this.orderNumber = orderNumber;
		this.start = start;
		this.end = end;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public int getOrderNumber() {
		return orderNumber;
	}
	
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public LocalTime getStart() {
		return start;
	}
	
	public void setStart(LocalTime start) {
		this.start = start;
	}
	
	public LocalTime getEnd() {
		return end;
	}
	
	public void setEnd(LocalTime end) {
		this.end = end;
	}
	
}
