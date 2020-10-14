package com.nesterov.university.model;

import java.time.Duration;

public class LessonTime {

	private int orderNumber;
	private Duration start;
	private Duration end;
	
	public int getOrderNumber() {
		return orderNumber;
	}
	
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public Duration getStart() {
		return start;
	}
	
	public void setStart(Duration start) {
		this.start = start;
	}
	
	public Duration getEnd() {
		return end;
	}
	
	public void setEnd(Duration end) {
		this.end = end;
	}
	
}
