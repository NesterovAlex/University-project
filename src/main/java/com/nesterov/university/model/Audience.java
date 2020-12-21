package com.nesterov.university.model;

public class Audience {

	private long id;
	private int roomNumber;
	private int capacity;

	public Audience() {
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capacity;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + roomNumber;
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
		Audience other = (Audience) obj;
		if (capacity != other.capacity)
			return false;
		if (id != other.id)
			return false;
		else {
			return roomNumber == other.roomNumber;
		}
	}

	@Override
	public String toString() {
		return "Audience [id=" + id + ", roomNumber=" + roomNumber + ", capacity=" + capacity + "]";
	}
}
