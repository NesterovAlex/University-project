package com.nesterov.university.model;

import java.sql.Time;

public class LessonTime {

	private long id;
	private int orderNumber;
	private Time start;
	private Time end;
	
	public LessonTime() {}

	public LessonTime(int orderNumber, Time start, Time end) {
		this.orderNumber = orderNumber;
		this.start = start;
		this.end = end;
	}
	
	public LessonTime(long id, int orderNumber, Time start, Time end) {
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
	
	public Time getStart() {
		return start;
	}
	
	public void setStart(Time start) {
		this.start = start;
	}
	
	public Time getEnd() {
		return end;
	}
	
	public void setEnd(Time end) {
		this.end = end;
	}
	
}
