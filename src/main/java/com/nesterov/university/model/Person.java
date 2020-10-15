package com.nesterov.university.model;

import java.time.LocalDate;

public abstract class Person {

	private String  firstName;
	private String  lastName;
	private LocalDate  bithDate;
	private String  address;
	private String  email;
	private String  phone;
	private Gender  gender;
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public LocalDate getBithDate() {
		return bithDate;
	}
	
	public void setBithDay(LocalDate bithDate) {
		this.bithDate = bithDate;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
}
