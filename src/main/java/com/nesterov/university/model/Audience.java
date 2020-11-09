package com.nesterov.university.model;

import org.springframework.stereotype.Component;

@Component
public class Audience {

	private long id;
	private int roomNumber;
	private int capacity;
	
	public Audience() {}

	public Audience(int roomNumber, int capacity) {
		this.roomNumber = roomNumber;
		this.capacity = capacity;
	}

	public Audience(long id, int roomNumber, int capacity) {
		this.id = id;
		this.roomNumber = roomNumber;
		this.capacity = capacity;
	}

	public int getRoomNumber() {
		return roomNumber;
	}
	
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
