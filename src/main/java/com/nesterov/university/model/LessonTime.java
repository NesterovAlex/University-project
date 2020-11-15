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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + orderNumber;
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LessonTime other = (LessonTime) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} 
		else if (!end.equals(other.end))
			return false;
		if (id != other.id)
			return false;
		if (orderNumber != other.orderNumber)
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} 
		else if (!start.equals(other.start))
			return false;
		return true;
	}
}
