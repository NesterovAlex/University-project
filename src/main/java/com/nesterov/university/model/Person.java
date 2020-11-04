package com.nesterov.university.model;

import java.time.LocalDate;

public abstract class Person {

	private long id;
	private String  firstName;
	private String  lastName;
	private LocalDate birthDate;
	private String  address;
	private String  email;
	private String  phone;
	private Gender  gender;
	
	public Person() {}
	
	public Person(String firstName, String lastName, LocalDate  birthDate, String address, String email, String phone,
			Gender gender) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.address = address;
		this.email = email;
		this.phone = phone;
		this.gender = gender;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
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
		return birthDate;
	}
	
	public void setBirthDate(LocalDate localDate) {
		this.birthDate = localDate;
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

	public String getGender() {
		return gender.getTitle();
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
}
